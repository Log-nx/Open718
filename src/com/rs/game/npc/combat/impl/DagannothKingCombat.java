package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;

public class DagannothKingCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2881, 2882, 2883 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		switch (npc.getId()) {
		case 2881:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 2, target, getRangeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
			World.sendProjectileNew(npc, target, 6359, 100, 70, 55, 3, 1, 0);
			break;
		case 2882:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 2, target, getMagicHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
			npc.setNextGraphics(new Graphics(6355, 1, 150));
			World.sendProjectileNew(npc, target, 6356, 65, 50, 55, 1.4, 1, 1);
			break;
		case 2883:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
			break;
		}
		return npc.getAttackSpeed();
	}
}