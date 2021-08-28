package com.rs.game.npc.slayer;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

@SuppressWarnings("serial")
public class DesertLizard extends NPC {
	
	public DesertLizard(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void sendDeath(Entity source) {		
		Player player = source.getAsPlayer();	
		if (!player.hasUsedIceCooler()) {
			setHitpoints((int) (getMaxHitpoints() * 0.1));
		} else {
			player.setUsedIceCooler(false);
			super.sendDeath(source);
		}
	}
	
	public void useIceCooler(Entity source) {
		Player player = source.getAsPlayer();	
		if (getHitpoints() <= 30) {
			if (player.getInventory().contains(6696)) {
				player.getInventory().deleteItem(6696, 1);
				player.setUsedIceCooler(true);
				sendDeath(source);
			}
		} else {
			heal(30);
			player.getInventory().deleteItem(6696, 1);
		}
	}
	
}
