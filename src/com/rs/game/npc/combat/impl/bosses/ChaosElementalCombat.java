package com.rs.game.npc.combat.impl.bosses;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Projectile;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.utils.Utils;

public class ChaosElementalCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3200 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		
		if (!(target instanceof Player)) {
			return defs.getAttackDelay();
		}
		
		final Player player = (Player) target;
		int roll = Utils.random(4);
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		
		if (roll == 1 && Utils.getDistance(player, npc) < 7) { // teleport
			World.sendProjectile(npc, player, 555, 18, 18, 50, 25, 0, 0);
			int diffX = 0;
			int diffY = 0;
			if (player.getX() < npc.getX()) {
				diffX = -1;
			} else if (player.getX() > player.getX() + npc.getSize()) {
				diffX = 1;
			} else {
				if (player.getY() < npc.getY()) {
					diffY = -1;
				} else if (player.getY() > npc.getY() + npc.getSize()) {
					diffY = 1;
				}
			}
			player.setNextWorldTile(new WorldTile(player.getX() + diffX, player.getY() + diffY, player.getPlane()));
			delayHit(npc, 1, player, getMagicHit(npc, getRandomMaxHit(npc, 300, NPCCombatDefinitions.MAGE, player)));
		} else if (roll == 2 && player.getInventory().getFreeSlots() > 0) {
			World.sendProjectile(npc, player, 558, 18, 18, 50, 25, 0, 0);
			ButtonHandler.sendRemove(player, Utils.random(13));
			delayHit(npc, 1, player, getMagicHit( npc, getRandomMaxHit(npc, 200, NPCCombatDefinitions.MAGE, player)));
		} else {
			boolean ranged = Math.random() > 0.6;
			delayHit(npc, 1, player, ranged ? getRangeHit(npc, getRandomMaxHit(npc, 290, NPCCombatDefinitions.RANGE, player)) : getMagicHit(npc, getRandomMaxHit(npc, 350, NPCCombatDefinitions.MAGE, player)));
		}
		return defs.getAttackDelay();
		
	}
}