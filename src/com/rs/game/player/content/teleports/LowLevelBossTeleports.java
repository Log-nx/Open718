package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class LowLevelBossTeleports {
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3069, 10250, 0));
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2987, 3382, 0));
			break;
		case 66:
			player.getControllerManager().startController("BorkController", 0, null);
			break;
		case 65:
			player.getDialogueManager().startDialogue("KalphiteQueen");
			break;
		case 64:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3285, 3909, 0));
			player.getControllerManager().startController("Wilderness");
			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2643, 10413, 0));
			break;
		case 72:
			break;
		case 71:
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