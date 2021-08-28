package com.rs.game.player.content.tutorial;

import com.fasterxml.jackson.annotation.Nulls;
import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.managers.QuestManager.Quests;
import com.rs.utils.Utils;

public class Reward {

	
	public static void sendRewardInterface (Player player, Quests quest) {
		if (player.isIronman()) {
			return;
		}
		player.getInterfaceManager().sendInterface(1244);
		player.getPackets().sendIComponentText(1244, 23, "Close");
		if (quest == null ) {
			player.getPackets().sendIComponentText(1244, 25, "You finished nothing.");
		} else {
			player.getPackets().sendIComponentText(1244, 25, "You finished " + Utils.formatPlayerNameForDisplay(quest.toString()) + ".");
		}
		player.getPackets().sendIComponentText(1244, 26, "Reward : <br><br>Starter Package.");
		player.getPackets().sendIComponentText(1244, 27, ""+Settings.SERVER_NAME+" Manager");

	}
	
	public static void rewardPlayer(Player player) {
		if (player.isIronman()) {
			player.getPackets().sendGameMessage("Welcome to " + Settings.SERVER_NAME + " for the first time.");
			player.getPackets().sendGameMessage("Do not hesitate to ask for help, & feel free to message any of the staff members.");
			player.getPackets().sendGameMessage("However, do not forget that we have a community, "+ Settings.SERVER_NAME + " Manager has the links.");
			player.getPackets().sendGameMessage("Congratulations, you've just finished the tutorial.");
			player.getPackets().sendGameMessage("You do not receive any starter package, you must obtain them yourself.");
			FriendChatsManager.joinChat("Diccus", player);
			World.sendWorldMessage("<img=7> Welcome <col=800000>" + player.getDisplayName() + "</col> to "+Settings.SERVER_NAME+" <img=7>", false, false);
			return;
		}
		player.getInventory().addItem(995, 1000000);
		player.getInventory().addItem(386, 100);
		player.getInventory().addItem(11818, 1);
		player.getInventory().addItem(11820, 1);
		player.getInventory().addItem(1755, 1);
		player.getInventory().addItem(1323, 1);
		player.getInventory().addItem(841, 1);
		player.getInventory().addItem(884, 1000);
		player.getInventory().addItem(1725, 1);
		player.getInventory().addItem(4351, 1);
		player.getPackets().sendGameMessage("Welcome to " + Settings.SERVER_NAME + " for the first time.");
		player.getPackets().sendGameMessage("Do not hesitate to ask for help, & feel free to message any of the staff members.");
		player.getPackets().sendGameMessage("However, do not forget that we have a community, "+ Settings.SERVER_NAME + " Manager has the links.");
		player.getPackets().sendGameMessage("Congratulations, you've just finished the tutorial and claimed your starter items.");
		FriendChatsManager.joinChat("Diccus", player);
		for (Player players : World.getPlayers()) {
			if (players == null)
				continue;
			players.getPackets().sendGameMessage("<img=7> Welcome <col=800000>" + player.getDisplayName() + "</col> to "+Settings.SERVER_NAME+" <img=7>");


	}
  }
}