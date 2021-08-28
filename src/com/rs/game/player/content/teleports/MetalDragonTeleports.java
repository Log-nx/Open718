package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.player.Player;

public class MetalDragonTeleports {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			break;
		case 67:
			break;
		case 66:
			break;
		case 65:
			break;
		case 64:
			break;
		case 73:
			break;
		case 72:
			break;
		case 71:
			break;
		case 70:
			break;
		case 69:
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
