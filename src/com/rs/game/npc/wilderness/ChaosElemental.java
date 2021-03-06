package com.rs.game.npc.wilderness;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

@SuppressWarnings("serial")
public class ChaosElemental extends NPC {

	public ChaosElemental(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setIntelligentRouteFinder(true);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
	}
}
