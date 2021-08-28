package com.rs.game.player.content.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.perks.PerkInterfaceManager;

public class QuestTab {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 2:
			StaffList.send(player);
			break;
		case 4:
			AccountManager.sendInterface(player);
			AccountManager.removeAttributtes(player);
			break;
		case 6:
			player.getInterfaceManager().sendNPCTeleports();
			break;
		case 8:
			break;
		case 10:
			break;
		case 12:
			player.getDropLogs().displayInterface();
			break;
		case 14:
			PerkInterfaceManager.sendInterface(player);
			break;
		default:
			player.getPackets().sendGameMessage("This button does not have an action set.");
			break;
		}
	}
}