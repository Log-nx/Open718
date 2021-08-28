package com.rs.game.npc.combat.impl.bosses;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Projectile;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class AgorothTentacleCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 19326, 19327, 19328, 19329, 19330, 19331 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = !Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0) ? 1 : Utils.random(2);
		switch (attackStyle) {
		case 0:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, Utils.random(300, 400)));
			break;
		case 1:
			Projectile projectile = World.sendProjectileNew(npc, target, 500, 140, 35, 35, 5, 16, 0);
			int endTime = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
			delayHit(npc, endTime, 858, target, getMagicHit(npc, Utils.random(100, 200)));
			break;
		}
		return Utils.random(3, 5);
	}
}
