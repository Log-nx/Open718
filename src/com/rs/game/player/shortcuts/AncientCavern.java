package com.rs.game.player.shortcuts;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;


public class AncientCavern {

	/**
	 * Handles all ladders/shortcuts in Ancient Cavern
	 * @param player
	 * @param object
	 * @return
	 */
	public static boolean climbDown(Player player, WorldObject object) {
		switch (object.getId()) {
		
		case 25338:
			player.setNextWorldTile(new WorldTile(1772, 5366, 0));
			return true;
			
			
		case 25336:
			player.setNextWorldTile(new WorldTile(1768, 5366, 1));
			return true;
			
		case 25339:
			player.setNextWorldTile(new WorldTile(1778, 5343, 1));
			return true;
			
		case 25340:
			player.setNextWorldTile(new WorldTile(1778, 5346, 0));
			return true;
			
		case 25337:
			player.setNextWorldTile(new WorldTile(1744, 5321, 1));
			return true;
			
		case 39468:
			player.setNextWorldTile(new WorldTile(1745, 5325, 0));
			return true;
			
			
		
		}
		return false;
	}
}
