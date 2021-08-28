package com.rs.game.player.content.achievements;

import com.rs.game.player.Player;
import com.rs.game.player.content.perks.SkillingPerks;

public class Achievement {
	
	public static void handleInterface(Player player, int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 93:
			player.getTemporaryAttributtes().remove("AchievementInterface");
			AgilityLocations.sendInterface(player);
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
	
	public static void sendChopChopInfo(Player player) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Cut 500 Yew Logs <br> <br>You have cut 0/500 logs" + "<br>");
	}
	
	public static void sendMineMineInfo(Player player) {
		player.getPackets().sendIComponentText(825, 67, "<col=00ff00>Current Achievement : <br><br>Mine 500 Runite Ore <br> <br>You have mined 0/500 ore" + "<br>");
	}
	
	public static void sendInterface(Player player) {
		player.getTemporaryAttributtes().put("AchievementInterface", true);
		player.getPackets().sendHideIComponent(825, 29, true);
		player.getPackets().sendHideIComponent(825, 28, true);
		player.getPackets().sendHideIComponent(825, 27, true);
		player.getPackets().sendHideIComponent(825, 30, true);
		player.getPackets().sendHideIComponent(825, 31, true);
		player.getInterfaceManager().sendInterface(825);
		player.getPackets().sendIComponentText(825, 95, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 96, "<col=00ff00>Agility");
		player.getPackets().sendIComponentText(825, 99, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 100, "<col=00ff00>Cooking");
		player.getPackets().sendIComponentText(825, 103, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 104, "<col=00ff00>Crafting");
		player.getPackets().sendIComponentText(825, 107, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 108, "<col=00ff00>Firemaking");
		player.getPackets().sendIComponentText(825, 111, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 112, "<col=00ff00>Fletching");
		player.getPackets().sendIComponentText(825, 115, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 116, "<col=00ff00>Fishing");
		player.getPackets().sendIComponentText(825, 119, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 120, "<col=00ff00>Herblore");
		player.getPackets().sendIComponentText(825, 123, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 124, "<col=00ff00>Hunter");
		player.getPackets().sendIComponentText(825, 127, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 128, "<col=00ff00>Mining");
		player.getPackets().sendIComponentText(825, 131, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 132, "<col=00ff00>Runecrafting");
		player.getPackets().sendIComponentText(825, 134, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 135, "<col=00ff00>Smithing");
		player.getPackets().sendIComponentText(825, 138, "<col=00ff00>Category");
		player.getPackets().sendIComponentText(825, 139, "<col=00ff00>Woodcutting");
		player.getPackets().sendIComponentText(825, 54, "  Achievement Points : ");
		player.getPackets().sendIComponentText(825, 55, "<col=82FA58>" + player.getStatistics().getAchievementPoints() + "");
		player.getPackets().sendIComponentText(825, 66, "");
		player.getPackets().sendIComponentText(825, 10, "Achievement Tab");
		}
	}