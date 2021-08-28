package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class MediumLevelTrainingTeleports {
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2326, 3802, 0)); //yaks
			break;
		case 67:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3027, 3473, 0)); //icefiends
			break;
		case 66:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2683, 9564, 0)); //brimhaven dungeon
			break;
		case 65:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2575, 9870, 0)); //waterfall dungeon
			break;
		case 64:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4667, 5059, 0)); //tzhaar city

			break;
		case 73:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1520, 4704, 0)); //chaos dwarf battleground
			break;
		case 72:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2218, 4532, 0)); //taverly dungeon
			break;
		case 71:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3652, 5121, 0)); //living rock cavern
			break;
		case 70:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1206, 6371, 0));	 //grotworm lair
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
