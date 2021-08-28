package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class DragonTypeTeleports {
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			player.getInterfaceManager().sendChromaticDragonTeleports();
			TeleportSystem.removeAttributes(player, "ChromaticDragonTeleports");
			break;
		case 67:
			player.getInterfaceManager().sendMetalDragonTeleports();
			TeleportSystem.removeAttributes(player, "MetalDragonTeleports");
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4767, 6076, 1));	
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1312, 4528, 0));
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
