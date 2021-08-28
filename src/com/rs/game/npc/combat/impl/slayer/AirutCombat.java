package com.rs.game.npc.combat.impl.slayer;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class AirutCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Airut" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		//To avoid making a new NPC class
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final int random = Utils.random(2);
		switch (random) {
		case 0:
			sendRangeAttack(npc, target);
			npc.setNextAnimation(new Animation(22169));
			break;
		default:
			sendMeleeAttack(npc, target);
			npc.setNextAnimation(new Animation(22169));
			break;
		}
		return defs.getAttackDelay();
	}

	/**
	 * Executes Airut's range attack.
	 * 
	 * @param npc
	 *            The Airut NPC.
	 * @param target
	 *            The target.
	 */
	private void sendRangeAttack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Hit hit = getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target));
		if (npc.getId() == 18621) {
			npc.setNextAnimation(new Animation(22169));
		} else {
		npc.setNextAnimation(new Animation(22155));
		}
		delayHit(npc, 2, target, hit);
	}

	/**
	 * Executes Airut's melee attack.
	 * 
	 * @param npc
	 *            The Airut NPC.
	 * @param target
	 *            The target.
	 */
	private void sendMeleeAttack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Hit hit = getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target));
		if (!target.withinDistance(npc, 1))
			sendRangeAttack(npc, target);
		else
			delayHit(npc, 0, target, hit);
	}
}