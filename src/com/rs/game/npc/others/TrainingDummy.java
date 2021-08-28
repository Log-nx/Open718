package com.rs.game.npc.others;

import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class TrainingDummy extends NPC {

	public TrainingDummy(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(1000);
		setCantFollowUnderCombat(true);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		super.handleIngoingHit(hit);
	}

}
