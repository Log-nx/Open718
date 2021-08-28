package com.rs.game.player.content.creation;

import com.rs.game.player.Player;

public enum VineWhipCreation {
		
	Vine_REGULAR(21371, 21369), Vine_YELLOW(21372, 21369), Vine_BLUE(21373, 21369)
	, Vine_WHITE(21374, 21369), Vine_GREEN(21375, 21369);
	
	private int whipVineId;
	private int vineId;	
	
	/**
	 * Defines the Whip Id
	 */
	VineWhipCreation(int vwId, int vId) {
		whipVineId = vwId;
		vineId = vId;
	}
	
	/**
	 *  Splits the Vine Whips accordingly to there Id
	 */
	public static void split(Player player, int vwId) {
		if(!player.getInventory().containsOneItem(vwId))
			return;
		player.lock();
		switch (vwId) {
		case 21371:
			player.getInventory().deleteItem(Vine_REGULAR.whipVineId, 1);
			player.getInventory().addItem(Vine_REGULAR.vineId, 1);
			player.getInventory().addItem(4151, 1);
			player.sm("You split the Abyssal Vine Whip back into the Vine Whip, and Abyssal Whip.");
			break;
		case 21372:
			player.getInventory().deleteItem(Vine_YELLOW.whipVineId, 1);
			player.getInventory().addItem(Vine_YELLOW.vineId, 1);
			player.getInventory().addItem(15441, 1);
			player.sm("You split the Abyssal Vine Whip back into the Vine Whip, and Abyssal Whip (Yellow).");
			break;
		case 21373:
			player.getInventory().deleteItem(Vine_BLUE.whipVineId, 1);
			player.getInventory().addItem(Vine_BLUE.vineId, 1);
			player.getInventory().addItem(15442, 1);
			player.sm("You split the Abyssal Vine Whip back into the Vine Whip, and Abyssal Whip (Blue).");
			break;
		case 21374:
			player.getInventory().deleteItem(Vine_WHITE.whipVineId, 1);
			player.getInventory().addItem(Vine_WHITE.vineId, 1);
			player.getInventory().addItem(15443, 1);
			player.sm("You split the Abyssal Vine Whip back into the Vine Whip, and Abyssal Whip (White).");
			break;
		case 21375:
			player.getInventory().deleteItem(Vine_GREEN.whipVineId, 1);
			player.getInventory().addItem(Vine_GREEN.vineId, 1);
			player.getInventory().addItem(15444, 1);
			player.sm("You split the Abyssal Vine Whip back into the Vine Whip, and Abyssal Whip (Green).");
			break;
		
			default:
				player.sm("Nothing interesting happens.");
			}
		player.unlock();
	}

	public static void combine(Player player, int vineId) {
	switch(vineId) {
	case 21369:
		if (player.getInventory().containsItem(4151, 1)) {
		player.getInventory().addItem(Vine_REGULAR.whipVineId, 1);
		player.getInventory().deleteItem(Vine_REGULAR.vineId, 1);
		player.getInventory().deleteItem(4151, 1);
		player.sm("<col=850909>You interlace the Whip Vine into your Abyssal whip, turning it into an Abyssal Vine Whip.</col>");
		return;
		} else if (player.getInventory().containsItem(15441, 1)) {
		player.getInventory().addItem(Vine_YELLOW.whipVineId, 1);
		player.getInventory().deleteItem(Vine_YELLOW.vineId, 1);
		player.getInventory().deleteItem(15441, 1);
		player.sm("<col=850909>You interlace the Whip Vine into your Abyssal whip, turning it into an Abyssal Vine Whip.</col>");
		return;
		} else if (player.getInventory().containsItem(15442, 1)) {
		player.getInventory().addItem(Vine_BLUE.whipVineId, 1);
		player.getInventory().deleteItem(Vine_BLUE.vineId, 1);
		player.getInventory().deleteItem(15442, 1);
		player.sm("<col=850909>You interlace the Whip Vine into your Abyssal whip, turning it into an Abyssal Vine Whip.</col>");
		return;
		} else if (player.getInventory().containsItem(15443, 1)) {
		player.getInventory().addItem(Vine_WHITE.whipVineId, 1);
		player.getInventory().deleteItem(Vine_WHITE.vineId, 1);
		player.getInventory().deleteItem(15443, 1);
		player.sm("<col=850909>You interlace the Whip Vine into your Abyssal whip, turning it into an Abyssal Vine Whip.</col>");
		return;			
		} else if (player.getInventory().containsItem(15444, 1)) {
		player.getInventory().addItem(Vine_GREEN.whipVineId, 1);
		player.getInventory().deleteItem(Vine_GREEN.vineId, 1);
		player.getInventory().deleteItem(15444, 1);
		player.sm("<col=850909>You interlace the Whip Vine into your Abyssal whip, turning it into an Abyssal Vine Whip.</col>");
		return;
		}
		break;
		default:
			player.sm("Nothing interesting happens.");
		}
	}
	
	public int getwhipVineId() {
		int vwId = whipVineId;
		return vwId;		
	}
	public int getVineId() {
		return vineId;
	}
}