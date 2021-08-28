package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class LowLevelTrainingTeleports {
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3252, 3266, 0)); //cows
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3877, 5526, 1)); //catacombs
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3237, 3296, 0)); //chickens
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3015, 3448, 0)); //dwarfs
			break;
		case 64:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3140, 4230, 2)); //stronghold of safety
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1860, 5244, 0)); //stronghold of security
			break;
		case 72:
			break;
		case 71:
			break;
		case 70:
			break;
		case 69:
			player.getInterfaceManager().sendNPCTeleports();
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