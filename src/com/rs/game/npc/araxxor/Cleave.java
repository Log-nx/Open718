package com.rs.game.npc.araxxor;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class Cleave implements AraxxorAttack {

	@Override
	public int attack(final Araxxor npc, final Player victim ) {
		//npc.addFreezeDelay(10);

		npc.setLocked(true);
        WorldTasksManager.schedule(new WorldTask() {
        	private int time;
            @Override
            public void run() {
				time ++;
				npc.setPhase(1);
				victim.AraxxorAttackCount = 0;
				npc.setNextAnimation(new Animation(24096));
            	npc.setNextGraphics(new Graphics(4986));
				if(time == 4) {
    				if(victim.withinDistance(npc, 5)){
    					CombatScript.delayHit(npc, 2, victim, new Hit(npc, CombatScript.getMaxHit(npc, 300,npc), HitLook.REGULAR_DAMAGE));
    			    	time = 0;
    				}
            	}
            }
        }, 10);
        return Utils.random(4, 15);
	}
	

	@Override
	public boolean canAttack(Araxxor npc, Player victim) {
		return victim.getY() > npc.getBase().getY() + 5;
	}

}

