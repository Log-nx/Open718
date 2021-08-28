package com.rs.game.npc.combat.impl.slayer;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.actions.slayer.Slayer;
import com.rs.utils.Utils;

public class BansheeCombat extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions def = npc.getCombatDefinitions();
		
		if (!Slayer.hasEarmuffs(target)) {
			final Player targetPlayer = (Player) target;
			final int randomSkill = Utils.random(0, 6);
			final int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
			targetPlayer.getSkills().set(randomSkill, currentLevel < 5 ? 0 : currentLevel - 5);
			targetPlayer.sm("The screams of the banshee make you feel slightly weaker.");
			npc.setNextForceTalk(new ForceTalk("*EEEEHHHAHHH*"));
			targetPlayer.stopAll();
			targetPlayer.setNextAnimationForce(new Animation(4434));
			delayHit(npc, 0, target, getMeleeHit(npc, targetPlayer.getMaxHitpoints() / 10));
		} else
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
		npc.setNextAnimation(new Animation(def.getAttackEmote()));
		return def.getAttackDelay();
	}

	@Override
	public Object[] getKeys() {
		return new Object[] { "Banshee", "Mighty banshee" };
	}
}
