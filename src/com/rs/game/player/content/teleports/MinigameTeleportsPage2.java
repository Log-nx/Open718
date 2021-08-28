package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class MinigameTeleportsPage2 {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3565, 3306, 0));
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3539, 3302, 0));
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1677, 5599, 0));
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2878, 3553, 0));
			break;
		case 64:
			player.getInterfaceManager().sendMinigameTeleports();
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3101, 3154, 2));
			break;
		case 72:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3342, 3232, 0));
			break;
		case 71:
			break;
		case 70:
			break;
		case 69:
			player.getInterfaceManager().sendMinigameTeleports();
			TeleportSystem.removeAttributes(player, "MinigameTeleports");
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
