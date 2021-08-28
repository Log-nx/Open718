package com.rs.game.player.content.activities.gungame;

import java.util.Map.Entry;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.BotRank;
import com.rs.utils.Utils;

public class Bots extends Survival {
	
	public static void clearInventory(Player player) {
		player.getInventory().deleteItem(BANDAGES, 28);
		player.getInventory().deleteItem(BLOOD_RUNE, Integer.MAX_VALUE);
		player.getInventory().deleteItem(DEATH_RUNE, Integer.MAX_VALUE);
		player.getInventory().deleteItem(WATER_RUNE, Integer.MAX_VALUE);
		player.getInventory().deleteItem(POT_1, 28);
		player.getInventory().deleteItem(POT_2, 28);
	}
	
	public static boolean killAllEvents() {
		for (Entry<Integer, NPC> npcs : npcList.entrySet()) {
			if (npcs.getValue() == null)
				continue;
			npcs.getValue().reset();
			npcs.getValue().finish();
		}
		wave = 0;
		GunGameBoss.reset();
		GunGameBoss.finish();
		GunGameBoss = null;
		sendResetCamera();
		players.clear();
		npcList.clear();
		return true;
	}
	
	public static void applyReward(Player player, int waveId) {
		player.setBotKillstreak(player.getBotKillstreak() + 1);
		player.setBotKills(player.getBotKills() + 1);
		player.getInterfaceManager().sendOverlay(1009, false);
		player.getPackets().sendIComponentText(1009, 0, "Survival Points: <col=FFFFFF>"+player.getBP()+"</col>");
		int killStreak = player.getBotKillstreak();
		int Botkills = player.getBotKills();
		BotRank.checkRank(player);
		
		int points = player.getBotKillstreak() > 10 ? 1 :
			player.getBotKillstreak() < 20 ? 1 :
			player.getBotKillstreak() < 30 ? 1 :
			player.getBotKillstreak() < 40 ? 1 : 1;
		
		player.setBP(player.getBP() + points);
		
		npcList.remove(waveId);
		
		if (Botkills == 1) {
			player.getInventory().add(1277, 1);

		}
		
		if (Botkills == 2) {
			player.getInventory().add(1291, 1);

		}
		
		if (Botkills == 3) {
			player.getInventory().add(1321, 1);

		}
		
		if (Botkills == 4) {
			player.getInventory().add(1203, 1);

		}
		
		if (Botkills == 5) {
			player.getInventory().add(1279, 1);

		}
		
		if (Botkills == 6) {
			player.getInventory().add(1293, 1);

		}
		
		if (Botkills == 7) {
			player.getInventory().add(1323, 1);

		}
		
		if (Botkills == 8) {
			player.getInventory().add(1207, 1);

		}
		
		if (Botkills == 9) {
			player.getInventory().add(1281, 1);

		}
		
		if (Botkills == 10) {
			player.getInventory().add(1295, 1);

		}
		
		if (Botkills == 11) {
			player.getInventory().add(1325, 1);

		}
		
		if (Botkills == 12) {
			player.getInventory().add(1217, 1);

		}
		
		if (Botkills == 13) {
			player.getInventory().add(1283, 1);

		}
		
		if (Botkills == 14) {
			player.getInventory().add(1297, 1);

		}
		
		if (Botkills == 15) {
			player.getInventory().add(1327, 1);

		}
		
		if (Botkills == 16) {
			player.getInventory().add(1209, 1);

		}
		
		if (Botkills == 17) {
			player.getInventory().add(1285, 1);

		}
		
		if (Botkills == 18) {
			player.getInventory().add(1299, 1);

		}
		
		if (Botkills == 19) {
			player.getInventory().add(1329, 1);

		}
		
		if (Botkills == 20) {
			player.getInventory().add(1211, 1);

		}
		
		if (Botkills == 21) {
			player.getInventory().add(1287, 1);

		}
		
		if (Botkills == 22) {
			player.getInventory().add(1301, 1);

		}
		
		if (Botkills == 23) {
			player.getInventory().add(1331, 1);

		}
		
		if (Botkills == 24) {
			player.getInventory().add(1213, 1);

		}
		
		if (Botkills == 25) {
			player.getInventory().add(1289, 1);

		}
		
		if (Botkills == 26) {
			player.getInventory().add(1303, 1);

		}
		
		if (Botkills == 27) {
			player.getInventory().add(1333, 1);

		}
		
		if (Botkills == 28) {
			player.getInventory().add(1215, 1);

		}
		
		if (Botkills == 29) {
			player.getInventory().add(1249, 1);

		}
		
		if (Botkills == 30) {
			player.getInventory().add(1305, 1);

		}
		
		if (Botkills == 31) {
			player.getInventory().add(1377, 1);

		}
		
		if (Botkills == 32) {
			player.getInventory().add(1434, 1);

		}
		
		if (Botkills == 33) {
			player.getInventory().add(4153, 1);

		}
		
		if (player.getBotKillstreak() > player.getMaxBotWave()) {
			player.setMaxBotWave(player.getBotKillstreak());
		}
		
		player.getPackets().sendGameMessage("You've killed a total of "+player.getBotKillstreak()+" Bots.");
		player.lastOnslaughtKill = Utils.currentTimeMillis();
	}
	// 			player.getPackets().sendGameMessage("<col=FF0000>You've upgraded your weapon, check inventory.");
	
	public static void removePlayer(Player player) {
		players.remove(player.getIndex());
		clearInventory(player);
		player.getPackets().sendResetCamera();
		player.setBotKills(0);
		player.getControllerManager().removeControllerWithoutCheck();
	}
	
	private static String[] joinMessages = {
		"You won't keep your items when you die",
		"This'll be the end of you",
		"Welcome to my GunGame",
		"Here you shall fall like the rest",
	};
	
	public static String getJoinMessage(Player player) {
		return joinMessages[Utils.random(joinMessages.length - 1)] + ", "+player.getDisplayName()+"";
	}
	
	public static String[] messages = {
		"Arise my minion.. ARRIISSEEE!!!",
		"All of ye who enter, SHALL PARISH!",
		"Kill thine enemies!",
		"Come to life my minion! Arise!",
	};
	
	public static void sendResetCamera() {
		for (Entry<Integer, Player> plr : players.entrySet()) {
			Player pl = plr.getValue();
			if (pl == null)
				continue;
			pl.getPackets().sendResetCamera();
		}
	}
	
	public static void send(String message) {
		for (Entry<Integer, Player> plr : players.entrySet()) {
			Player player = plr.getValue();
			if (player == null)
				continue;
			player.getPackets().sendGameMessage(message);
		}
	}
	
}