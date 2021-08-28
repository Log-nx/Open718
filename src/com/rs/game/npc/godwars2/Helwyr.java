package com.rs.game.npc.godwars2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.EffectsManager;
import com.rs.game.EffectsManager.Effect;
import com.rs.game.EffectsManager.EffectType;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.instances.impl.GodWars2Instance;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.Drop;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class Helwyr extends NPC {
	private List<WorldObject> mushrooms;
	private GodWars2Instance instance;
	private NPC[] wolves;
	private int attackProgress;

	public Helwyr(int id, WorldTile tile, GodWars2Instance instance) {
		super(id, tile, -1, true, true);
		this.instance = instance;
		mushrooms = new ArrayList<WorldObject>();
		wolves = new NPC[4];
		setIntelligentRouteFinder(true);
		setCapDamage(12000);
		setHitpoints(getMaxHitpoints());
		setBonuses();
		getCombat().setCombatDelay(8);
		loadMushrooms();
	}

	private void loadMushrooms() {
		for (WorldObject objects : World.getRegion(getRegionId()).getAllObjects()) {
			if (objects != null && objects.getId() == 101899) {
				mushrooms.add(objects);
			}
		}
	}

	private void removeMushrooms() {
		for (WorldObject mushroom : mushrooms) {
			if (mushroom != null && mushroom.getId() == 101900) {
				World.removeObject(mushroom);
				mushroom.setId(101899);
				World.spawnObject(mushroom);
				World.sendGraphics(this, new Graphics(-1), mushroom);
			}
		}
	}

	private void spawnWolves() {
		int count = 0;
		for (int i = 0; i < wolves.length; i++) {
			if (wolves[i] == null || wolves[i].isDead() || wolves[i].hasFinished()) {
				WorldTile teleTile = this;
				for (int trycount = 0; trycount < 10; trycount++) {
					teleTile = new WorldTile(this, 3);
					if (World.isTileFree(this.getPlane(), teleTile.getX(), teleTile.getY(),
							NPCDefinitions.getNPCDefinitions(22439).size))
						break;
					teleTile = this;
				}
				wolves[i] = new NPC(22439, teleTile, -1, true, true);
				wolves[i].setIntelligentRouteFinder(true);
				Entity target = getRandomAliveTarget();
				if (target != null)
					wolves[i].getCombat().setTarget(target);
				count++;
				if (count == 2)
					break;
			}
		}
	}

	private Entity getRandomAliveTarget() {
		List<Entity> possibleTargets = new ArrayList<Entity>();
		for (Entity e : this.getPossibleTargets()) {
			if (e == null || e.hasFinished() || e.isDead())
				continue;
			possibleTargets.add(e);
		}
		if (possibleTargets.isEmpty())
			return null;
		return possibleTargets.get(Utils.random(possibleTargets.size()));
	}

	private void removeWolves() {
		for (NPC wolve : wolves) {
			if (wolve != null)
				wolve.finish();
		}
	}

	@Override
	public void processNPC() {
		if (!hasFinished() && (getPossibleTargets().isEmpty() || instance.getBossNPC() != this)) {
			finish();
			return;
		}
		super.processNPC();
	}

	@Override
	public void processHit(Hit hit) {
		super.processHit(hit);
		instance.updateInterface(false);
	}

	@Override
	public void finish() {
		removeMushrooms();
		removeWolves();
		super.finish();
	}

	@Override
	public ArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (Player player : instance.getPlayersInBattle()) {
			if (player == null || player.isDead())
				continue;
			possibleTarget.add(player);
		}
		return possibleTarget;
	}

	@Override
	public boolean checkAgressivity() {
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Utils.random(possibleTarget.size()));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	@Override
	public int getMaxHitpoints() {
		if (instance == null)
			return 0;
		return instance.getSettings().isHardMode() ? 300000 : 200000;
	}

	public GodWars2Instance getInstance() {
		return instance;
	}

	public int getAttackProgress() {
		return attackProgress;
	}

	public void setAttackProgress(int attackProgress) {
		this.attackProgress = attackProgress;
	}

	public List<WorldObject> getMushrooms() {
		return mushrooms;
	}

	@Override
	public void reset() {
		getTemporaryAttributtes().remove("CantWalk");
		setCantDoDefenceEmote(false);
		attackProgress = 0;
		super.reset();
	}

	@Override
	public boolean canWalkNPC(int toX, int toY) {
		return getTemporaryAttributtes().get("CantWalk") == null;
	}

	public void setNextGraphics(Graphics nextGraphics) {
		if (getTemporaryAttributtes().get("CantWalk") != null) {
			return;
		}
		super.setNextGraphics(nextGraphics);
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		if (!isDead())
			setHitpoints(0);
		final int deathDelay = defs.getDeathDelay() - 1;
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= deathDelay) {
					if (source instanceof Player) {
						Player player = ((Player) source);
						player.getControllerManager().processNPCDeath(Helwyr.this);
					}
					drop();
					reset();
					instance.removeInterfaces();
					setLocation(instance.getTile(instance.getBossInfo().getSpawnTile()));
					finish();
					setRespawnTask();
					for (Player player : instance.getPlayersInBattle()) {
						if (player == null || player.isDead() || player.hasFinished())
							continue;
					}
					if (source != null && source.getAttackedBy() == Helwyr.this) {
						source.setAttackedByDelay(0);
						source.setAttackedBy(null);
						source.setFindTargetDelay(0);
					}
					stop();
				}
				loop++;
				if (loop > 15)
					stop();
			}
		}, 0, 1);
	}

	@Override
	public void setRespawnTask() {
		if (instance != null && instance.isFinished() && instance.getBossNPC() != this
				&& getPossibleTargets().isEmpty())
			return;
		if (!hasFinished()) {
			reset();
			setLocation(instance.getTile(instance.getBossInfo().getSpawnTile()));
			finish();
		}
		long respawnDelay = getCombatDefinitions().getRespawnDelay() * 600;
		if (instance != null) {
			respawnDelay = instance.getSettings().getSpawnSpeed();
			if (respawnDelay > 0 && respawnDelay < 30000)
				respawnDelay = 30000;
		}
		if (respawnDelay < 0)
			respawnDelay = 0;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (instance != null && instance.isFinished() && instance.getBossNPC() != Helwyr.this
							&& getPossibleTargets().isEmpty()) {
						return;
					}
					spawn();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, respawnDelay, TimeUnit.MILLISECONDS);
	}

	@Override
	public void spawn() {
		super.spawn();
		setNextAnimation(new Animation(28200));
		setNextGraphics(new Graphics(6085));
		getCombat().setCombatDelay(8);
		instance.updateInterface(true);
	}

	public static final String[] RARE_DROPS = { "wand of", "orb of", "crest of", "twisted" };

	@Override
	public Item sendDrop(Player player, Drop drop) {
		int size = getSize();

		boolean stackable = ItemDefinitions.getItemDefinitions(drop.getItemId()).isStackable();
		boolean noted = ItemDefinitions.getItemDefinitions(drop.getItemId()).isNoted();
		int certId = ItemDefinitions.getItemDefinitions(drop.getItemId()).getCertId();

		Item item = stackable ? new Item(drop.getItemId(), (drop.getMinAmount()) + Utils.random(drop.getExtraAmount()))
				: new Item(drop.getItemId(), drop.getMinAmount() + Utils.random(drop.getExtraAmount()));
		// note drops
		boolean beenNoted = false;
		if (!noted && certId != -1 && item.getAmount() > 5) {
			item.setId(certId);
			beenNoted = true;
		}

		if (!stackable && item.getAmount() > 1 && !beenNoted) {
			for (int i = 0; i < item.getAmount(); i++) {
				World.addGroundItem(new Item(item.getId(), 1),
						new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player, true, 60);
			}
		} else {
			World.addGroundItem(item, new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player, true,
					60);
		}
		return item;
	}

	@Override
	public int getAttackSpeed() {
		return 4;
	}

	public static final HelwyrAttacks[] attacks = { HelwyrAttacks.Nature_Attack, HelwyrAttacks.AUTO_ATTACK,
			HelwyrAttacks.AUTO_ATTACK, HelwyrAttacks.BLEED, HelwyrAttacks.AUTO_ATTACK, HelwyrAttacks.AUTO_ATTACK,
			HelwyrAttacks.FRENZY_ATTACK, HelwyrAttacks.AUTO_ATTACK, HelwyrAttacks.AUTO_ATTACK,
			HelwyrAttacks.Howl_Attack, HelwyrAttacks.AUTO_ATTACK, HelwyrAttacks.AUTO_ATTACK };

	public int getAttackDistance(HelwyrAttacks attack) {
		if (attack == HelwyrAttacks.Nature_Attack)
			return 9;
		return 1;
	}

	public static enum HelwyrAttacks {
		Nature_Attack() {
			public int sendAttack(Helwyr helwyr, Player target) {
				helwyr.setNextForceTalk(new ForceTalk("Nature, lend me your aid!"));
				helwyr.setNextAnimation(new Animation(28207));
				Collections.shuffle(helwyr.getMushrooms());
				int size = helwyr.getSize();
				int count = 0;
				for (WorldObject mushroom : helwyr.getMushrooms()) {
					if (mushroom != null && mushroom.getId() == 101899) {
						int delay = Utils.projectileTimeToCycles(World
								.sendProjectileNew(new WorldTile(helwyr.getCoordFaceX(size), helwyr.getCoordFaceY(size),
										helwyr.getPlane()), mushroom, 6122, 70, 10, 10, 1, 16, 5)
								.getEndTime()) - 1;
						WorldTasksManager.schedule(new WorldTask() {
							int loop = 0;

							@Override
							public void run() {
								if (helwyr.hasFinished() || (helwyr.getPossibleTargets().isEmpty()
										|| helwyr.getInstance().getBossNPC() != helwyr)) {
									for (Entity t : helwyr.getPossibleTargets()) {
										if (t == null || t.isDead() || t.hasFinished())
											continue;
										t.getTemporaryAttributtes().remove("mushroom");
									}
									World.removeObject(mushroom);
									mushroom.setId(101899);
									World.spawnObject(mushroom);
									World.sendGraphics(helwyr, new Graphics(-1), mushroom);
									stop();
									return;
								}
								if (loop == 0) {
									World.removeObject(mushroom);
									mushroom.setId(101900);
									World.spawnObject(mushroom);
									World.sendGraphics(helwyr, new Graphics(6124), mushroom);
								} else if (loop >= 48) {
									World.removeObject(mushroom);
									mushroom.setId(101899);
									World.spawnObject(mushroom);
									World.sendGraphics(helwyr, new Graphics(-1), mushroom);
									for (Entity t : helwyr.getPossibleTargets()) {
										if (t == null || t.isDead() || t.hasFinished())
											continue;
										if (Utils.isOnRange(mushroom, t, 1, 1, 1))
											t.getTemporaryAttributtes().remove("mushroom");
									}
									stop();
									return;
								}
								for (Entity t : helwyr.getPossibleTargets()) {
									if (t == null || t.isDead() || t.hasFinished())
										continue;
									if (Utils.isOnRange(mushroom, t, 1, 1, 1)) {
										t.applyHit(new Hit(helwyr, 250, HitLook.REGULAR_DAMAGE));
										Integer mushroomTicks = (Integer) t.getTemporaryAttributtes().get("mushroom");
										t.getTemporaryAttributtes().put("mushroom",
												mushroomTicks != null ? mushroomTicks + 1 : 1);
										mushroomTicks = (Integer) t.getTemporaryAttributtes().get("mushroom");
										if (mushroomTicks != null && mushroomTicks >= 12) {
											t.setBoundDelay(5);
											t.getTemporaryAttributtes().put("mushroom", 0);
										}
									}
								}
								loop++;
							}
						}, delay, 1);
						count++;
						if (count == (helwyr.getInstance().getSettings().isHardMode() ? 6 : 3))
							break;
					}
				}
				return helwyr.getAttackSpeed();
			}
		},
		BLEED() {
			public int sendAttack(Helwyr helwyr, Player target) {
				helwyr.setNextForceTalk(new ForceTalk("YOU. WILL. BLEED!"));
				helwyr.setNextAnimation(new Animation(28214));
				helwyr.setNextGraphics(new Graphics(6126));
				helwyr.getTemporaryAttributtes().put("CantWalk", Boolean.TRUE);
				helwyr.setNextFaceEntity(null);
				helwyr.setNextFaceWorldTile(target);
				helwyr.setCantDoDefenceEmote(true);
				byte[] dir = Utils.getDirection(helwyr.getDirection());
				WorldTile[] start = new WorldTile[5];
				WorldTile faceLocation = helwyr
						.transform(
								(int) (2 * (dir[0] < 0 ? 0
										: dir[0] == 0 && dir[1] != 0 ? 1
												: dir[0] == 0 ? 0.5 : dir[0] == 1 ? 2 : dir[0])),
								(int) (4 * (dir[1] < 0 ? 0 : dir[1] == 0 ? 0.5 : dir[1])), 0);
				List<WorldTile> cone = new ArrayList<WorldTile>();
				start[0] = faceLocation.transform(2 * (dir[1] != 0 ? -dir[1] : dir[1]),
						2 * (dir[0] != 0 ? dir[0] : dir[0]), 0);
				start[1] = faceLocation.transform((dir[1] != 0 ? -dir[1] : dir[1]), (dir[0] != 0 ? dir[0] : dir[0]), 0);
				start[2] = faceLocation;
				start[3] = faceLocation.transform(-1 * (dir[1] != 0 ? -dir[1] : dir[1]),
						-1 * (dir[0] != 0 ? dir[0] : dir[0]), 0);
				start[4] = faceLocation.transform(-2 * (dir[1] != 0 ? -dir[1] : dir[1]),
						-2 * (dir[0] != 0 ? dir[0] : dir[0]), 0);
				for (int i = 0; i < start.length; i++) {
					for (int j = (i == 0 || i == 4) ? -1 : 0; j < ((i == 0 || i == 4) ? 1
							: (i == 1 || i == 3) ? 2 : 3); j++) {
						cone.add(start[i].transform((j * dir[0]), (j * dir[1]), 0));
					}
				}
				if (Utils.isOnRange(helwyr, target, 1, helwyr.getSize(), 1))
					cone.add(target);
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						if (helwyr.hasFinished() || (helwyr.getPossibleTargets().isEmpty()
								|| helwyr.getInstance().getBossNPC() != helwyr)) {
							helwyr.getTemporaryAttributtes().remove("CantWalk");
							helwyr.setCantDoDefenceEmote(false);
							stop();
							return;
						}
						for (WorldTile tile : cone) {
							if (tile == null)
								continue;
							for (Entity e : helwyr.getPossibleTargets()) {
								if (e == null || e.isDead() || e.hasFinished() || !e.matches(tile))
									continue;
								e.applyHit(new Hit(helwyr, Utils.random(1500, 3001), HitLook.MELEE_DAMAGE));
								Effect currentEffect = e.getEffectsManager().getEffectForType(EffectType.HELWYR_BLEED);
								int damage = 50;
								if (currentEffect != null)
									damage += (int) currentEffect.getArguments()[2];
								if (damage >= 1500)
									damage = 1500;
								EffectsManager.startHelwyrBleedEffect(e, 50, 2, damage);
							}
						}
						helwyr.setNextFaceEntity(target);
						helwyr.getTemporaryAttributtes().remove("CantWalk");
						helwyr.setCantDoDefenceEmote(false);
					}
				}, 3);
				return 7;
			}
		},
		FRENZY_ATTACK() {
			public int sendAttack(Helwyr helwyr, Player target) {
				helwyr.setNextForceTalk(new ForceTalk("You cannot escape me. Aaaargh!"));
				helwyr.setNextAnimation(new Animation(28215));
				helwyr.setNextFaceEntity(null);
				helwyr.setNextFaceWorldTile(target);
				helwyr.setCantDoDefenceEmote(true);
				helwyr.getTemporaryAttributtes().put("CantWalk", Boolean.TRUE);
				List<WorldTile> hitTiles = new ArrayList<WorldTile>();
				int startDir = helwyr.getDirection() >> 11;
				for (int k = 0; k < 3; k++) {
					int sss = (startDir + (k * (k == 1 ? 6 : 1))) > 7 ? (startDir + (k * (k == 1 ? 6 : 1))) - 8
							: (startDir + (k * (k == 1 ? 6 : 1)));
					byte[] dirss = Utils.ANGLE_DIRECTION_DELTA[sss];
					WorldTile[] start = new WorldTile[5];
					WorldTile faceLocation = helwyr.transform(
							(int) (2 * (dirss[0] < 0 ? 0
									: dirss[0] == 0 && dirss[1] != 0 ? 1
											: dirss[0] == 0 ? 0.5 : dirss[0] == 1 ? 2 : dirss[0])),
							(int) (4 * (dirss[1] < 0 ? 0 : dirss[1] == 0 ? 0.5 : dirss[1])), 0);
					start[0] = faceLocation.transform(2 * (dirss[1] != 0 ? -dirss[1] : dirss[1]),
							2 * (dirss[0] != 0 ? dirss[0] : dirss[0]), 0);
					start[1] = faceLocation.transform((dirss[1] != 0 ? -dirss[1] : dirss[1]),
							(dirss[0] != 0 ? dirss[0] : dirss[0]), 0);
					start[2] = faceLocation;
					start[3] = faceLocation.transform(-1 * (dirss[1] != 0 ? -dirss[1] : dirss[1]),
							-1 * (dirss[0] != 0 ? dirss[0] : dirss[0]), 0);
					start[4] = faceLocation.transform(-2 * (dirss[1] != 0 ? -dirss[1] : dirss[1]),
							-2 * (dirss[0] != 0 ? dirss[0] : dirss[0]), 0);
					for (int i = 0; i < start.length; i++) {
						for (int j = 0; j < 2; j++) {
							hitTiles.add(start[i].transform((j * dirss[0]), (j * dirss[1]), 0));
						}
					}
				}
				if (Utils.isOnRange(helwyr, target, 1, helwyr.getSize(), 1))
					hitTiles.add(target);
				WorldTasksManager.schedule(new WorldTask() {
					int loop = 0;

					@Override
					public void run() {
						if (helwyr.hasFinished() || (helwyr.getPossibleTargets().isEmpty()
								|| helwyr.getInstance().getBossNPC() != helwyr)) {
							helwyr.getTemporaryAttributtes().remove("CantWalk");
							helwyr.setCantDoDefenceEmote(false);
							stop();
							return;
						}
						if (loop >= 10) {
							helwyr.setNextFaceEntity(target);
							helwyr.getTemporaryAttributtes().remove("CantWalk");
							helwyr.setCantDoDefenceEmote(false);
							stop();
							return;
						}
						if (loop % 2 == 0) {
							for (Entity e : helwyr.getPossibleTargets()) {
								if (e == null || e.isDead() || e.hasFinished())
									continue;
								boolean hit = Utils.isOnRange(helwyr, e, 0, helwyr.getSize() - 1, 1);
								if (!hit)
									for (WorldTile tile : hitTiles) {
										if (tile == null)
											continue;
										if (tile.matches(e)) {
											hit = true;

											break;
										}
									}
								if (hit) {
									e.applyHit(new Hit(helwyr, Utils.random(1000, 1801), HitLook.MELEE_DAMAGE));
									Effect currentEffect = e.getEffectsManager()
											.getEffectForType(EffectType.HELWYR_BLEED);
									int damage = 50;
									if (currentEffect != null)
										damage += (int) currentEffect.getArguments()[2];
									if (damage >= 1500)
										damage = 1500;
									EffectsManager.startHelwyrBleedEffect(e, 50, 2, damage);
								}
							}
						}
						loop++;
					}

				}, 0, 0);
				return 11;
			}
		},
		Howl_Attack() {
			public int sendAttack(Helwyr helwyr, Player target) {
				helwyr.setNextAnimation(new Animation(28213));
				helwyr.setNextGraphics(new Graphics(6127));
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						helwyr.spawnWolves();
					}

				});
				return helwyr.getAttackSpeed();
			}
		},
		AUTO_ATTACK() {
			public int sendAttack(Helwyr helwyr, Player target) {
				helwyr.setNextAnimation(new Animation(28205));
				int damage = CombatScript.getMaxHit(helwyr, NPCCombatDefinitions.MELEE, target);
				CombatScript.delayHit(helwyr, 0, target, CombatScript.getMeleeHit(helwyr, damage));
				return helwyr.getAttackSpeed();
			}
		};
		public int sendAttack(Helwyr helwyr, Player target) {
			return 0;
		}
	}

}