package com.rs.game.npc.slayer;

import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class Airut extends NPC {

	public Airut(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setIntelligentRouteFinder(true);
        setForceTargetDistance(3);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
	}
}