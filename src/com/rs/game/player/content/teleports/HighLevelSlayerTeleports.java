package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class HighLevelSlayerTeleports {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1661, 5257, 0));
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4316, 6227, 0));
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4060, 6308, 0));
			break;
		case 65:
			player.getInterfaceManager().sendStrykewyrmTeleports();
			TeleportSystem.removeAttributes(player, "WyrmTeleports");
			break;
		case 64:
			player.getInterfaceManager().sendNPCTeleports();
			TeleportSystem.removeAttributes(player, "NPCTeleports");
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3025, 9224, 0));
			player.getControllerManager().startController("JadinkoLair");
			break;
		case 72:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2501, 2886, 0));
			break;
		case 71:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4629, 5454, 3));
			break;
		case 70:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2405, 4386, 0));
			break;
		case 69:
			player.getInterfaceManager().sendHighLevelSlayerTeleportsPage2();
			TeleportSystem.removeAttributes(player, "HighLevelSlayerTeleportsPage2");
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
