package com.rs.game.npc.araxxor;

import com.rs.game.Animation;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.player.content.combat.Combat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class MeleeAttack implements AraxxorAttack {

	@Override
	public int attack(final Araxxor npc, final Player victim) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				victim.AraxxorAttackCount ++;
				stop();
				int hit = 0;
			/*	if (victim.getPrayer().usingPrayer(0, 19)) {
					npc.setNextAnimation(new Animation (24046));
					victim.setNextAnimation(new Animation(Combat.getDefenceEmote(victim)));
					//hit = 0;
					hit = Utils.random(0 + Utils.random(150), 360);
				} else {*/
					hit = Utils.random(0 + Utils.random(150), 360);
					npc.setNextAnimation(new Animation (24046));
					victim.setNextAnimation(new Animation(Combat.getDefenceEmote(victim)));
				//}
				victim.applyHit(new Hit(victim, hit, hit == 0 ? HitLook.MISSED : HitLook.MELEE_DAMAGE));
			}
		});
		//return Utils.random(4, 6);
		return 6;
	}


	@Override
	public boolean canAttack(Araxxor npc, Player victim) {
		return victim.getY() > npc.getBase().getY() + 10;
	}

}
