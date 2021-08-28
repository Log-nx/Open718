package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.player.Player;

public class ChromaticDragonTeleports {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 31:
			break;
		case 32:
			break;
		case 33:
			break;
		case 34:
			break;
		case 35:
			break;
		case 36:
			break;
		case 37:
			break;
		case 38:
			break;
		case 39:
			break;
		case 40:
			player.getInterfaceManager().sendDragonTeleports();
			TeleportSystem.removeAttributes(player, "DragonTeleports");
			break;
		default:
			player.getPackets().sendGameMessage("This button does not have an action set.");
			if (player.isAdministrator() || Settings.BETA || Settings.DEBUG) {
				player.getPackets().sendGameMessage("Missing Button ID: " + componentId);
			}
			break;
		}
	}
}
