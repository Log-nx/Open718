package com.rs.game.npc.araxxor;

import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class EggAttack implements AraxxorAttack {
	
	int X;
	int Y;
	WorldObject One;
	WorldObject Two;
	WorldObject Three;

	@Override
	public int attack(final Araxxor npc, final Player p) { 
		final WorldTile SpawnSpiders = new WorldTile(X, Y, p.getPlane());
		
		if(!p.hasSpawnedEggs) {
	WorldTasksManager.schedule(new WorldTask() {
		int time;
		@Override
		public void run() {
			time ++;
			npc.setPhase(1);
			p.AraxxorAttackCount ++;
			@SuppressWarnings("unused")
			int hit = 0;
			p.hasSpawnedEggs = true;
	if(time == 1) {
		X = p.FINALAGGX;
		Y = p.FINALAGGY;
		p.eggSpidersX = X;
		p.eggSpidersY = Y;
		//p.sendMessage(""+p.FINALAGGX+"      "+p.FINALAGGY+"");
		World.sendGraphics(null, new Graphics(5009), new WorldTile(X+3, Y, p.getPlane()));
		World.sendGraphics(null, new Graphics(5009), new WorldTile(X+2, Y, p.getPlane()));
		World.sendGraphics(null, new Graphics(5009), new WorldTile(X+2, Y+1, p.getPlane()));
	}
	if(time == 3) {
		WorldObject object = new WorldObject(91641, 22, 1, X+3, Y, p.getPlane());
		WorldObject object2 = new WorldObject(91641, 22, 1, X+2, Y, p.getPlane());
		WorldObject object3 = new WorldObject(91641, 22, 1, X+2, Y+1, p.getPlane());
		World.spawnObject(new WorldObject (91641, 22, 1, X+3, Y, p.getPlane()));
		World.spawnObject(new WorldObject (91641, 22, 1, X+2, Y, p.getPlane()));
		World.spawnObject(new WorldObject (91641, 22, 1, X+2, Y+1, p.getPlane()));
		One = object;
		Two = object2;
		Three = object3;
	}
	if(time >= 4) {
		if(p.getX() == X+3 && p.getY() == Y && !p.AraxxorEggBurst|| p.getX() == X+2 && p.getY() == Y && !p.AraxxorEggBurst|| p.getX() == X+2 && p.getY() == Y+1 && !p.AraxxorEggBurst) {
			p.eggBurst(X, Y, p.getPlane());
			World.removeObject(One);
			World.removeObject(Two);
			World.removeObject(Three);
		}
	if(time >= 15) {
		//p.sendMessage("SPAWNED SPIDERS");
		//NPC bob = new NPC(19468, SpawnSpiders, 1, false);
		//World.spawnNPC(19468, SpawnSpiders, 1, false);
		//Entity npc1 = World.spawnNPC(19468, SpawnSpiders, -1, true);
		//final NPC npc2 = (NPC)npc1;
		//npc.spawnEggSpiders(p);
		
		@SuppressWarnings("unused")
		NPC spider = World.spawnNPC(19468, SpawnSpiders, -1, true);
		p.eggBurst(X, Y, p.getPlane());
		World.removeObject(One);
		World.removeObject(Two);
		World.removeObject(Three);
	}
		
	if(p.AraxxorEggBurst) {
		stop();
	}
	}
		
		
			//p.applyHit(new Hit(p, hit, hit == 0 ? HitLook.MISSED : HitLook.MAGIC_DAMAGE));

		//	p.applyHit(new Hit(p, 200, HitLook.MAGIC_DAMAGE, 0));
			
		}
	}, 0, 0);;
		}
	//return Utils.random(20, 21);
		return 21;
} 
		

@Override
public boolean canAttack(Araxxor npc, Player victim) {
	return victim.getY() > npc.getBase().getY() + 10;
}

}
