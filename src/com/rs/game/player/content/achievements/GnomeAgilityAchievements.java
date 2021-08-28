package com.rs.game.player.content.achievements;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class GnomeAgilityAchievements {
	
	public static void handleInterface(Player player, int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 93:
			sendLapsEasy(player);
			break;

		case 97:
			sendLapsMedium(player);
			break;
			
		case 101:
			sendLapsHard(player);
			break;
			
		case 105:
			sendLapsElite(player);
			break;
			
		case 109:
			break;
			
		}
	}
	
	public static void sendLapsEasy(Player player) {
		if (player.getStatistics().getGnomeLapsRan() < 10) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 10 Laps <br> <br>You have completed " +player.getStatistics().getGnomeLapsRan() + "/10 laps"
				+ "<br>");
		}
		if (player.getStatistics().getGnomeLapsRan() >= 10) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 10 Laps <br> <br>You have completed 10/10 laps"
				+ "<br>");
		}
	}
	
	public static void sendLapsMedium(Player player) {
		if (player.getStatistics().getGnomeLapsRan() < 50) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 50 Laps <br> <br>You have completed " +player.getStatistics().getGnomeLapsRan() + "/50 laps"
				+ "<br>");
		}
		if (player.getStatistics().getGnomeLapsRan() >= 50) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 50 Laps <br> <br>You have completed 50/50 laps"
				+ "<br>");
		}
	}
	
	public static void sendLapsHard(Player player) {
		if (player.getStatistics().getGnomeLapsRan() < 150) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 150 Laps <br> <br>You have completed " +player.getStatistics().getGnomeLapsRan() + "/150 laps"
				+ "<br>");
		}
		if (player.getStatistics().getGnomeLapsRan() >= 150) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 150 Laps <br> <br>You have completed 150/150 laps"
				+ "<br>");
		}
	}
	
	public static void sendLapsElite(Player player) {
		if (player.getStatistics().getGnomeLapsRan() < 250) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 250 Laps <br> <br>You have completed " +player.getStatistics().getGnomeLapsRan() + "/250 laps"
				+ "<br>");
		}
		if (player.getStatistics().getGnomeLapsRan() >= 250) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Complete 250 Laps <br> <br>You have completed 250/250 laps"
				+ "<br>");
		}
	}
	
	public static void sendInterface(Player player) {
		player.getTemporaryAttributtes().put("GnomeAgilityAchievements", true);
		player.getPackets().sendHideIComponent(825, 29, true);
		player.getPackets().sendHideIComponent(825, 28, true);
		player.getPackets().sendHideIComponent(825, 27, true);
		player.getPackets().sendHideIComponent(825, 30, true);
		player.getPackets().sendHideIComponent(825, 31, true);
		player.getInterfaceManager().sendInterface(825);
		player.getPackets().sendIComponentText(825, 95, "<col=00ff00>Achievement");
		player.getPackets().sendIComponentText(825, 96, "Laps (Easy)");
		player.getPackets().sendIComponentText(825, 99, "<col=00ff00>Achievement");
		player.getPackets().sendIComponentText(825, 100, "Laps (Medium)");
		player.getPackets().sendIComponentText(825, 103, "<col=00ff00>Achievement");
		player.getPackets().sendIComponentText(825, 104, "Laps (Hard)");
		player.getPackets().sendIComponentText(825, 107, "<col=00ff00>Achievement");
		player.getPackets().sendIComponentText(825, 108, "Laps (Elite)");
		player.getPackets().sendHideIComponent(825, 111, true);
		player.getPackets().sendHideIComponent(825, 112, true);
		player.getPackets().sendHideIComponent(825, 115, true);
		player.getPackets().sendHideIComponent(825, 116, true);
		player.getPackets().sendHideIComponent(825, 119, true);
		player.getPackets().sendHideIComponent(825, 120, true);
		player.getPackets().sendHideIComponent(825, 123, true);
		player.getPackets().sendHideIComponent(825, 124, true);
		player.getPackets().sendHideIComponent(825, 127, true);
		player.getPackets().sendHideIComponent(825, 128, true);
		player.getPackets().sendHideIComponent(825, 131, true);
		player.getPackets().sendHideIComponent(825, 132, true);
		player.getPackets().sendHideIComponent(825, 134, true);
		player.getPackets().sendHideIComponent(825, 135, true);
		player.getPackets().sendHideIComponent(825, 138, true);
		player.getPackets().sendHideIComponent(825, 139, true);
		player.getPackets().sendIComponentText(825, 54, "  Achievement Points : ");
		player.getPackets().sendIComponentText(825, 55, "<col=82FA58>" + player.getStatistics().getAchievementPoints() + "");
		player.getPackets().sendIComponentText(825, 66, "");
		player.getPackets().sendIComponentText(825, 10, "Achievement Tab");
		
		}

}
