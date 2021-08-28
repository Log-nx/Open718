package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.magic.Magic;

public class MediumLevelBossTeleports {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			if (player.getSkills().getLevel(Skills.SUMMONING) >= 60) {
				player.getControllerManager().startController("QueenBlackDragonController");
			} else if (player.getSkills().getLevel(Skills.SUMMONING) < 60)
				player.getPackets().sendGameMessage("You need a summoning level of 60 to go to this monster.");
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1912, 4367, 0));
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2970, 4384, 2));
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2564, 5739, 0));
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
