package com.rs.game.npc.araxxor;

import com.rs.game.Animation;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.content.combat.Combat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class RangeAttack implements AraxxorAttack {

	@Override
	public int attack(final Araxxor npc, final Player p) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				p.AraxxorAttackCount ++;
				stop();
				int hit = 0;
					World.sendProjectile(npc, p, 4990, 41, 16, 21, 0, 16, -20);
					hit = Utils.random(0 + Utils.random(150), 360);
					npc.setNextAnimation(new Animation (24047));
					p.setNextAnimation(new Animation(Combat.getDefenceEmote(p)));
					
				
				p.applyHit(new Hit(npc, hit, hit == 0 ? HitLook.MISSED : HitLook.RANGE_DAMAGE));
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
