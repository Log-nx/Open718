package com.rs.game;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.game.Hit.HitLook;
import com.rs.game.npc.araxxor.MirrorBackSpider;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class EffectsManager implements Serializable {

	private static final long serialVersionUID = -5884310017906704149L;

	private transient Entity entity;
	private transient boolean isPlayer;

	private final List<Effect> effects = new CopyOnWriteArrayList<Effect>();
	
	public static byte BUFF = 0, DEBUFF = 1, HIT_MARK = 2, COMBO_BUFFS = 3, SHIELD_BUFF = 4, CHANNELED = 5;

	public EffectsManager() {
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
		this.isPlayer = entity instanceof Player;
	}

	public Entity getEntity() {
		return entity;
	}

	public void startEffect(Effect effect) {
		EffectType type = effect.type;
		if (!type.canStartEffect(effect, entity))
			return;
		Effect currentEffect = getEffectForType(type);
		if (currentEffect != null)
			effects.set(effects.indexOf(currentEffect), effect);
		else
			effects.add(effect);
	}

	public void processEffects() {
		if (entity.isDead() || entity.hasFinished() || effects.isEmpty())
			return;
		for (Effect effect : effects) {
			int action = effect.type.getAction();
			if (effect.cycle != -1) {
				effect.type.process(entity);
				effect.cycle--;
				if (effect.cycle == 0) {
					removeEffect(effect.type);
					continue;
				}
			}
		}
	}

	public void resetEffects() {
		Effect[] e = effects.toArray(new Effect[effects.size()]);
		effects.clear();
		for (Effect effect : e) {
			effect.setCycle(0);
		}
	}

	public boolean removeEffect(EffectType type) {
		Effect effect = getEffectForType(type);
		if (effect == null)
			return false;
		if (effect.getCycle() > 0)
			effect.setCycle(0);
		type.onRemoval(entity);
		boolean removedEffect = effects.remove(effect);
		return removedEffect;
	}

	public boolean hasActiveEffect(EffectType type) {
		Effect effect = getEffectForType(type);
		if (effect == null)
			return false;
		return effects.contains(effect);
	}

	public boolean hasActiveEffect(int action) {
		return getEffectForAction(action) != null;
	}

	public Effect getEffectForType(EffectType type) {
		for (Effect effect : effects) {
			if (effect.type == type)
				return effect;
		}
		return null;
	}

	public Effect getEffectForAction(int action) {
		for (Effect effect : effects) {
			EffectType type = effect.getType();
			if (type.getAction() == action)
				return effect;
		}
		return null;
	}
	
	private static byte SPECIAL = 0;
	private static byte OVERLOAD = 1;
	private static byte ANTIFIRE = 2;

	public static enum EffectType {
		
		HELWYR_BLEED(HIT_MARK, -1, 2073, -1),
		
		BOUND(DEBUFF, -1, 2057, 14884) {
			@Override
			public boolean canStartEffect(Effect effect, Entity e) {
				if (e.isBoundImmune())
					return false;
				Effect currentEffect = e.getEffectsManager().getEffectForType(this);
				boolean freeze = (boolean) effect.args[0];
				if (freeze && e instanceof Player && currentEffect == null)
					((Player) e).getPackets().sendGameMessage("You have been frozen.");
				if (e.getSize() == 1)
					e.setNextGraphics(new Graphics(4531, 0, 0));
				e.resetWalkSteps();
				e.getEffectsManager()
						.startEffect(new Effect(EffectType.BOUND_IMMUNITY, effect.cycle + (int) effect.args[1]));
				return true;
			}
		},

		BOUND_IMMUNITY(DEBUFF),
		
		MIRRORBACK_SPIDER(SPECIAL) {

			@Override
			public boolean canStartEffect(Effect effect, Entity e) {
				if (e.getEffectsManager().hasActiveEffect(SPECIAL)) {
					e.getAsPlayer().sendMessage("You can't use this right now.");
					return false;
				}
				MirrorBackSpider spider = (MirrorBackSpider) effect.args[0];
				spider.setOwner(e);
				return true;
			}

			@Override
			public void onRemoval(Entity e) {
				MirrorBackSpider spider = (MirrorBackSpider) e.getEffectsManager().getEffectForType(this).args[0];
				if (!spider.isDead())
					spider.finish();
			}
		},		
		OVERLOAD_EFFECT(OVERLOAD) {
			
			@Override
			public boolean canStartEffect(Effect effect, Entity e) {
				if (e.getEffectsManager().hasActiveEffect(OVERLOAD) || !(e instanceof Player)) {
					if (e instanceof Player)
						e.getAsPlayer().sendMessage("You can't drink this right now.");			
					return false;
				}
				Player player = e.getAsPlayer();
				WorldTasksManager.schedule(new WorldTask() {
					
					int count = 4;
					
					@Override
					public void run() {
						if (count == 0)
							stop();
						player.setNextAnimation(new Animation(3170));
						player.setNextGraphics(new Graphics(560));
						count--;
					}
				}, 0, 2);			
				return true;
			}
			
			@Override
			public void process(Entity e) {
				Player player = e.getAsPlayer();
				
				int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
				int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
				int level = actualLevel > realLevel ? realLevel : actualLevel;			
				player.getSkills().set(Skills.ATTACK, 
						(int) (level + 3 + (realLevel * 0.15)));

				actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
				realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.STRENGTH,
						(int) (level + 3 + (realLevel * 0.15)));

				actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
				realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.DEFENCE,
						(int) (level + 3 + (realLevel * 0.15)));

				actualLevel = player.getSkills().getLevel(Skills.MAGIC);
				realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.MAGIC, 
						(int) (level + 3 + (realLevel * 0.05)));

				actualLevel = player.getSkills().getLevel(Skills.RANGE);
				realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.RANGE,
						(int) (level + 3 + (realLevel * 0.08)));
			}
			
			@Override
			public void onRemoval(Entity e) {
				Player player = e.getAsPlayer();
				player.sendMessage("<col=480000>The effects of your overload have worn off and you feel normal again.");
				int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
				int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.ATTACK, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
				realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.STRENGTH, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
				realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.DEFENCE, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.MAGIC);
				realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.MAGIC, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.RANGE);
				realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.RANGE, realLevel);
			}
			
		},
		SUPREME_OVERLOAD_EFFECT(OVERLOAD) {
			
			@Override
			public boolean canStartEffect(Effect effect, Entity e) {
				if (e.getEffectsManager().hasActiveEffect(OVERLOAD) || !(e instanceof Player)) {
					if (e instanceof Player)
						e.getAsPlayer().sendMessage("You can't drink this right now.");			
					return false;
				}
				Player player = e.getAsPlayer();
				WorldTasksManager.schedule(new WorldTask() {
					
					int count = 4;
					
					@Override
					public void run() {
						if (count == 0)
							stop();
						player.setNextAnimation(new Animation(3170));
						player.setNextGraphics(new Graphics(560));
						count--;
					}
				}, 0, 2);			
				return true;
			}
			
			@Override
			public void process(Entity e) {
				Player player = e.getAsPlayer();
				
				int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
				int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
				int level = actualLevel > realLevel ? realLevel : actualLevel;			
				player.getSkills().set(Skills.ATTACK, 
						(int) (level + 4 + (realLevel * 0.16)));

				actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
				realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.STRENGTH,
						(int) (level + 4 + (realLevel * 0.16)));

				actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
				realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.DEFENCE,
						(int) (level + 4 + (realLevel * 0.16)));

				actualLevel = player.getSkills().getLevel(Skills.MAGIC);
				realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.MAGIC, 
						(int) (level + 5 + (realLevel * 0.05)));

				actualLevel = player.getSkills().getLevel(Skills.RANGE);
				realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
				level = actualLevel > realLevel ? realLevel : actualLevel;
				player.getSkills().set(Skills.RANGE,
						(int) (level + 5 + (realLevel * 0.08)));
			}
			
			@Override
			public void onRemoval(Entity e) {
				Player player = e.getAsPlayer();
				player.sendMessage("<col=480000>The effects of your overload have worn off and you feel normal again.");
				int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
				int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.ATTACK, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
				realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.STRENGTH, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
				realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.DEFENCE, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.MAGIC);
				realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.MAGIC, realLevel);
				actualLevel = player.getSkills().getLevel(Skills.RANGE);
				realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
				if (actualLevel > realLevel)
					player.getSkills().set(Skills.RANGE, realLevel);
			}
			
		}, ;

		public boolean canStartEffect(Effect effect, Entity e) {
			return true;
		}

		public void onRemoval(Entity e) {

		}
		
		public void process(Entity e) {
			
		}

		private byte action;

		private EffectType(byte action) {
			this.action = action;
		}

		private int var, varbit, grMap;
		
		private EffectType(byte action, int var, int varbit, int grMap) {
			this.action = action;
			this.var = var;
			this.varbit = varbit;
			this.grMap = grMap;
		}

		public byte getAction() {
			return action;
		}
		
		public int getVar() {
			return var;
		}

		public int getGrMap() {
			return grMap;
		}
		
	}

	public static class Effect implements Serializable {

		private static final long serialVersionUID = 9217587656136559938L;

		private final EffectType type;
		private int cycle;
		private final Object[] args;

		public Effect(EffectType type, int count, Object... args) {
			this.type = type;
			this.cycle = count;
			this.args = args;
		}

		public EffectType getType() {
			return type;
		}

		public int getCycle() {
			return cycle;
		}

		public void setCycle(int cycle) {
			this.cycle = cycle;
		}

		public Object[] getArguments() {
			return args;
		}
	}

	public boolean isEmpty() {
		return effects.isEmpty();
	}
	
	public static void startHelwyrBleedEffect(Entity e, int cycles, int delay, int damage) {
		e.getEffectsManager().startEffect(new Effect(EffectType.HELWYR_BLEED, cycles, HitLook.REGULAR_DAMAGE, null, damage, delay));
	}
}