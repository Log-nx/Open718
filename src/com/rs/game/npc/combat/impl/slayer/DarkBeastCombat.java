package com.rs.game.npc.combat.impl.slayer;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class DarkBeastCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
	return new Object[] { 2783 };
    }

    @Override
    public int attack(NPC npc, final Entity target) {
	NPCCombatDefinitions def = npc.getCombatDefinitions();
	npc.setNextAnimation(new Animation(2731));
	if (Utils.isOnRange(target.getX(), target.getY(), target.getSize(), npc.getX(), npc.getY(), npc.getSize(), 3)) {
	    delayHit(npc, 0, target, getMeleeHit(npc, Utils.random(400)));
	} else {
	    World.sendProjectile(npc, target, 2181, 41, 16, 41, 35, 16, 0);
	    delayHit(npc, 2, target, getMagicHit(npc, Utils.random(250)));
	}
	return def.getAttackDelay();
    }
}
