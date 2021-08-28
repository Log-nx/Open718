package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class BarrelChestCombat extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = target instanceof Player ? (Player) target : null;
		int damage;
		switch (1) {
		case 1: // Melee
			damage = Utils.getRandom(450);
			npc.setNextAnimation(new Animation(5895));
			if (target instanceof Player) {
				player.prayer.drainPrayer(Utils.getRandom(650));
			}
			delayHit(npc, 1, target, getMeleeHit(npc, damage));
			break;
		}
		return npc.getAttackSpeed();
	}

	@Override
	public Object[] getKeys() {
		return new Object[] { "Barrelchest" };
	}
}