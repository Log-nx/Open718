package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class HighLevelBossTeleports {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3895, 6817, 0));
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2904, 5203, 0));
			player.getControllerManager().startController("GodWars");
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3271, 3599, 0));
			player.getControllerManager().startController("Wilderness");
			break;
		case 65:
			break;
		case 64:
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2973, 3430, 0));
			break;
		case 72:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2916, 3739, 0));
			break;
		case 71:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3368, 2881, 0));
			break;
		case 70:
			break;
		case 69:
			player.getInterfaceManager().sendNPCTeleports();
			TeleportSystem.removeAttributes(player, "NPCTeleports");
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
