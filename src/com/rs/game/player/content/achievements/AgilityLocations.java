package com.rs.game.player.content.achievements;

import com.rs.game.player.Player;

public class AgilityLocations {

	public static void handleInterface(Player player, int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 93:
			GnomeAgilityAchievements.sendInterface(player);
			player.getTemporaryAttributtes().remove("AgilityLocations");
			break;

		case 97:
			break;
			
		case 2:
			break;
			
		case 3:
			break;

		case 4:
			break;
			
		case 5:
			break;

		case 6:
			break;
			
		case 7:
			break;
			
		case 8:
			break;
			
		case 9:
			break;
			
		}
	}
	
	public static void sendInterface(Player player) {
		player.getTemporaryAttributtes().put("AgilityLocations", true);
		player.getPackets().sendHideIComponent(825, 29, true);
		player.getPackets().sendHideIComponent(825, 28, true);
		player.getPackets().sendHideIComponent(825, 27, true);
		player.getPackets().sendHideIComponent(825, 30, true);
		player.getPackets().sendHideIComponent(825, 31, true);
		player.getInterfaceManager().sendInterface(825);
		player.getPackets().sendIComponentText(825, 95, "<col=00ff00>Location");
		player.getPackets().sendIComponentText(825, 96, "<col=00ff00>Gnome Stronghold");
		player.getPackets().sendIComponentText(825, 99, "<col=00ff00>Location");
		player.getPackets().sendIComponentText(825, 100, "<col=00ff00>Barbarian Outpost");
		player.getPackets().sendIComponentText(825, 103, "<col=00ff00>Location");
		player.getPackets().sendIComponentText(825, 104, "<col=00ff00>Ape Atoll");
		player.getPackets().sendIComponentText(825, 107, "<col=FF0000>Location");
		player.getPackets().sendIComponentText(825, 108, "<col=FF0000>Wilderness");
		player.getPackets().sendIComponentText(825, 111, "<col=00ff00>Location");
		player.getPackets().sendIComponentText(825, 112, "<col=00ff00>Heffin");
		player.getPackets().sendIComponentText(825, 115, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 116, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 119, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 120, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 123, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 124, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 127, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 128, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 131, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 132, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 134, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 135, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 138, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 139, "<col=00ff00>");
		player.getPackets().sendIComponentText(825, 54, "  Achievement Points : ");
		player.getPackets().sendIComponentText(825, 55, "<col=82FA58>" + player.getStatistics().getAchievementPoints() + "");
		player.getPackets().sendIComponentText(825, 66, "");
		player.getPackets().sendIComponentText(825, 10, "Achievement Tab");
	}
}
