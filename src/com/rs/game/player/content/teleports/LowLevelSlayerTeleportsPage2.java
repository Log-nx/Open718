package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class LowLevelSlayerTeleportsPage2 {
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3610, 9736, 0));
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3535, 5186, 0));
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3291, 5538, 0));
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1991, 4175, 0));
			break;
		case 64:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2405, 4386, 0));
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3748, 9373, 0));
			break;
		case 72:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3360, 9353, 0));
			break;
		case 71:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3206, 9373, 0));
			break;
		case 70:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1848, 5986, 0));
			break;
		case 69:
			player.getInterfaceManager().sendLowLevelSlayerTeleports();
			TeleportSystem.removeAttributes(player, "LowLevelSlayerTeleports");
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
