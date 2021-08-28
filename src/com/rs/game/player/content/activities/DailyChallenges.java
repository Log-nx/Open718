package com.rs.game.player.content.activities;

import com.rs.game.player.Player;

public class DailyChallenges {

	public static final int MAIN_BOARD = 1343;

	public static void sendChallengesBoard(Player player) {
		player.getPackets().sendWindowsPane(MAIN_BOARD, 0);
		player.getInventory().refresh();
		player.closeInterfaces();
	}

	/* This method is handling the whole screen interface. */
	public void handleButtons(Player player, int buttonId) {
		if (buttonId == 56) {
			player.getPackets().sendWindowsPane(player.getInterfaceManager().hasResizableScreen() ? 746 : 548, 0);
			player.getInventory().refresh();
			player.closeInterfaces();
		}
	}

}
