package com.rs.game.npc.araxxor;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class MagicAttack implements AraxxorAttack {

	@Override
	public int attack(final Araxxor npc, final Player p) { 
	WorldTasksManager.schedule(new WorldTask() {
		@SuppressWarnings("unused")
		int time;
		@Override
		public void run() {
			time ++;
			p.AraxxorAttackCount ++;
			int hit = 0;
		/*	if (p.getPrayer().usingPrayer(0, 19)) {
				p.getAppearence().transformIntoNPC(19472);
				p.getAppearence().setRenderEmote(-1);
				p.lock();
				npc.setCantInteract(true);
				npc.isCantInteract();
				npc.setNextAnimation(new Animation (24047));
				World.sendProjectile(npc, p, 4997, 16, 16, 40, 35, 5, 0);	
				p.AraxxorAttackCount = 0;	
				npc.cacoonTimer(p, npc);
				hit = Utils.random(0 + Utils.random(150), 360);
				//hit = 0;
			} else {*/
		
				World.sendProjectile(npc, p, 4979, 41, 16, 41, 0, 16, -20);
				hit = Utils.random(0 + Utils.random(25), 75);
				npc.setNextAnimation(new Animation (24095));
				p.setNextGraphics(new Graphics (4980));
				
			
			p.applyHit(new Hit(p, hit, hit == 0 ? HitLook.MISSED : HitLook.MAGIC_DAMAGE));

		//	p.applyHit(new Hit(p, 200, HitLook.MAGIC_DAMAGE, 0));
			
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
