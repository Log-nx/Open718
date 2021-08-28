package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class LowLevelSlayerTeleports {
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3423, 3544, 0));
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2218, 4532, 0));
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3170, 9572, 0));
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2808, 10002, 0));
			break;
		case 64:
			player.getInterfaceManager().sendNPCTeleports();
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2998, 3113, 0));
			break;
		case 72:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3149, 4664, 0));
			break;
		case 71:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2152, 5076, 0));
			break;
		case 70:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3423, 3019, 0));
			break;
		case 69:
			player.getInterfaceManager().sendLowLevelSlayerTeleportsPage2();
			TeleportSystem.removeAttributes(player, "LowLevelSlayerTeleportsPage2");
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
