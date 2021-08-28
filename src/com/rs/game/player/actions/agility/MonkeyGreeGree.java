package com.rs.game.player.actions.agility;

import java.util.ArrayList;

import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 * @info NPC transformations.
 */
public class MonkeyGreeGree {
	
	
	private static MonkeyGreeGree instance = new MonkeyGreeGree();

	private int transformationId;


	/**
	 * Transforms a player to a NPC.
	 * @param player
	 * @param npcId
	 */
	public void transform(Player player, int npcId) {
		if (player.isTransformed()) {
			stop(player);
			player.getAppearence().generateAppearenceData();
			return;
		}
			
		
		switch (npcId) { //if you transform to special ncp you give here special actions
		
		case 4363:
			player.getPackets().sendGameMessage("You transform to a ape.");
			break;
		}
		player.getAppearence().asNPC(npcId);
		player.setTransformed(true);
		player.getAppearence().generateAppearenceData();
		
	}
	/** ? what does this array do you could give actions to people that is transformed i give u example metho
	 * Force stop transformation.  
	 * @param p
	 */
	/**
	 * @param x
	 * @param y
	 * @param plane
	 */
	public void stop(Player p) {
		p.setTransformed(false);
		p.getPackets().sendGameMessage("You have transformed back to a human.");
		p.getAppearence().asNPC(-1);
		p.getAppearence().getAppearanceBlock();
	}
	public static MonkeyGreeGree getInstance() {
		return instance;
	}
	
	public int getTransformationId() {
		return transformationId;
	}
	
	public void setTransformation(Player player, int transformationId, int npcId) {
		this.transformationId = transformationId;
		transform(player, npcId);
	}

	
/*	
	public static void MonkeyTransform(Player player, Item item) {
		if (item.getId() == 4031) {
		player.getAppearence().asNPC(4363);
		}
	}
	
	public static void TranformBack(Player player, Item item) {
		if (item.getId() == 4031)
		player.getEquipment().deleteItem(4363, 1);
		player.getAppearence().asNPC(-1);
	
	}*/

}
