package com.rs.game.npc.combat.impl.bosses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Projectile;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class KalphiteQueenCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
				{ "Kalphite Queen", "Exiled Kalphite Queen" };
	}

	public static void attackMageTarget(final List<Player> arrayList, Entity fromEntity, final NPC startTile, Entity t, final int projectile, final int gfx) {
		final Entity target = t == null ? getTarget(arrayList, fromEntity, startTile) : t;
		if (target == null)
			return;
		if (target instanceof Player)
			arrayList.add((Player) target);
		Projectile proj = World.sendProjectileNew(fromEntity, target, projectile, fromEntity == startTile ? 70 : 20, 20, 30, 6, 0, 0);
		int endTime = Utils.projectileTimeToCycles(proj.getEndTime()) - 1;
		delayHit(startTile, endTime, target, getMagicHit(startTile, getRandomMaxHit(startTile, startTile.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.setNextGraphics(new Graphics(gfx));
				attackMageTarget(arrayList, target, startTile, null, projectile, gfx);
			}
		}, endTime);
	}

	private static void attackMageTarget(final List<Player> arrayList, Entity fromEntity, final NPC npc, Entity t) {
		final Entity target = t == null ? getTarget(arrayList, fromEntity, npc) : t;
		if (target == null)
			return;
		if (target instanceof Player)
			arrayList.add((Player) target);
		Projectile proj = fromEntity == npc ? World.sendProjectileNew(npc, target, 5048, npc.getId() == 1158 || npc.getId() == 16707 ? 58 : 68, 30, 53, 1, 20, 50) : 
		World.sendProjectileNew(fromEntity, target, 5048, 30, 30, 30, 3, 0, 0);
		int endTime = Utils.projectileTimeToCycles(proj.getEndTime()) - 1;
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				int damage = Utils.random(npc
						.getMaxHit(NPCCombatDefinitions.MAGE));
				  if (target instanceof Player && ((Player)
				  target).getPrayer().isMageProtecting()) damage /= 2;
				target.applyHit(new Hit(npc, damage, HitLook.MAGIC_DAMAGE));
				target.setNextGraphics(new Graphics(5049, 0, 100));
				attackMageTarget(arrayList, target, npc, null);
			}
		}, endTime);
	}

	public static Player getTarget(List<Player> list, final Entity fromEntity, NPC startTile) {
		if (fromEntity == null)
			return null;
		ArrayList<Player> added = new ArrayList<Player>();
		for (Entity entity : startTile.getPossibleTargets()) {
			if (!(entity instanceof Player))
				continue;
			Player player = (Player) entity;
			if (list.contains(player) || !player.withinDistance(fromEntity) || !player.withinDistance(startTile))
				continue;
			added.add(player);
		}
		if (added.isEmpty())
			return null;
		Collections.sort(added, new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				if (Utils.getDistance(o1, fromEntity) > Utils.getDistance(o2, fromEntity) || o1 == null)
					return 1;
				else if (Utils.getDistance(o1, fromEntity) < Utils.getDistance(o2, fromEntity) || o2 == null)
					return -1;
				else
					return 0;
			}
		});
		return added.get(0);

	}

	private static final Graphics FIRST_MAGIC_START = new Graphics(5046), SECOND_MAGIC_START = new Graphics(5047), RANGE_START = new Graphics(5041);

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		boolean secondForm = npc.getId() != 1158 && npc.getId() != 16707;
		int style = Utils.random(!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0) ? 1 : 0, 3);
		switch(style) {
			case 0://Melee
				npc.setNextAnimation(new Animation(secondForm ? 24277 : 24275));
				delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
				break;
			case 1://Magic
				npc.setNextAnimation(new Animation(secondForm ? 24285 : 24281));
				npc.setNextGraphics(secondForm ? SECOND_MAGIC_START : FIRST_MAGIC_START);
				attackMageTarget(new ArrayList<Player>(), npc, npc, target);
				break;
			case 2://Range
				npc.setNextAnimation(new Animation(secondForm ? 24284 : 24282));
				if (!secondForm)
					npc.setNextGraphics(RANGE_START);
				delayHit(npc, 2, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
				World.sendProjectile(npc, target, 288, 46, 31, 50, 30, 16, 0);
				for (Entity t : npc.getPossibleTargets()) {
					t.setNextGraphics(new Graphics(secondForm ? 5045 : 5043, 30, 100));
					int damage = Utils.random(npc.getMaxHit(NPCCombatDefinitions.RANGE));
					if (t instanceof Player && ((Player) t).getPrayer().isRangeProtecting()) {
						damage /= 2;
					}
					target.applyHit(new Hit(npc, damage, HitLook.RANGE_DAMAGE));
				}
				break;
		}
		return defs.getAttackDelay();
	}
}