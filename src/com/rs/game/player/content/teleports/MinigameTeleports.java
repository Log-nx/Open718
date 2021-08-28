package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class MinigameTeleports {
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4610, 5129, 0));
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4744, 5172, 0));
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4602, 5062, 0));
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2663, 2652, 0));
			break;
		case 64:
			player.getInterfaceManager().sendNPCTeleports();
			TeleportSystem.removeAttributes(player, "NPCTeleports");
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3373, 3087, 0));
			break;
		case 72:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2968, 9699, 0));
			break;
		case 71:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1890, 3177, 0));
			break;
		case 70:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2994, 9679, 0));
			break;
		case 69:
			player.getInterfaceManager().sendMinigameTeleportsPage2();
			TeleportSystem.removeAttributes(player, "MinigameTeleportsPage2");
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
