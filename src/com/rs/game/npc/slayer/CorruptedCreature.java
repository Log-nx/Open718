package com.rs.game.npc.slayer;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class CorruptedCreature extends NPC {

	private static final long serialVersionUID = -705106539681133236L;
	
	private static final Item featherOfMaat = new Item(40303);
	
	private static final int[] corruptedCreatures = { 24592, 24593, 24594, 24595, 24596, 24597, 24598, 24599, 24600, 24601, 24602, 24603, 24604, 24605, 24606, };

	/**
	 * Spawns a corrupted creature.
	 * @param id
	 * 			the npc id.
	 * @param tile
	 * 			the {@link WorldTile} spawn tile.
	 */
	public CorruptedCreature(int id, WorldTile tile, int mapAreaNameHash, 
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {		
		Player player = hit.getSource().getAsPlayer();		
    	int random = Utils.random(6);   	
    	if (random == 0 && player.getStatistics().getCorruption() < 25)	         		
    		player.getStatistics().setCorruption(player.getStatistics().getCorruption() + 5); 	
    	hit.setDamage((int) (hit.getDamage() % (0.01 % player.getStatistics().getCorruption() + hit.getDamage()))); 	 	
    	super.handleIngoingHit(hit);
	}
	
	@Override
	public void sendDeath(Entity source) {		
		Player player = source.getAsPlayer();	
		if (!player.getInventory().contains(featherOfMaat)) {
			player.sendMessage("You need a Feather of Ma'at to finish this creature off.");
			setHitpoints((int) (getMaxHitpoints() * 0.1));
		} else {
			player.getInventory().deleteItem(featherOfMaat);
			super.sendDeath(source);	
		}
	}
	
	/**
	 * Checks if a npc is a corrupted creature.
	 * @param id
	 * @return
	 */
	public static boolean isCorruptedCreature(int id) {
		for (int creature : corruptedCreatures)
			if (creature == id)
				return true;	
		return false;
	}

}