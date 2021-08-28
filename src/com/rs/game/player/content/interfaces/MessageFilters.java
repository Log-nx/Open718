package com.rs.game.player.content.interfaces;

import com.rs.game.player.Player;
import com.rs.utils.Colors;

public class MessageFilters {
	
	public static void sendInterface(Player player, boolean full) {
		if (full) {
			player.getTemporaryAttributtes().put("MessageFilter", true);
			player.getInterfaceManager().sendInterface(1157);
		}
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Message Filter Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Settings");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "World Messages");
		player.getPackets().sendIComponentText(1157, 48, (player.isWorldMessageOff() ? Colors.GREEN + "Filtered" : Colors.RED + "Not Filtered"));
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Yell Messages");
		player.getPackets().sendIComponentText(1157, 51, (player.isYellOff() ? Colors.GREEN + "Filtered" : Colors.RED + "Not Filtered"));

		player.getPackets().sendIComponentText(1157, 52, "");
		player.getPackets().sendIComponentText(1157, 53, "Killcount");
		player.getPackets().sendIComponentText(1157, 54, (player.isToggleKillcount() ? Colors.GREEN + "Filtered" : Colors.RED + "Not Filtered"));

		player.getPackets().sendIComponentText(1157, 55, "");
		player.getPackets().sendIComponentText(1157, 56, "");
		player.getPackets().sendIComponentText(1157, 57, "");

		player.getPackets().sendIComponentText(1157, 58, "");
		player.getPackets().sendIComponentText(1157, 59, "");
		player.getPackets().sendIComponentText(1157, 60, "");
		
		player.getPackets().sendIComponentText(1157, 61, "");
		player.getPackets().sendIComponentText(1157, 62, "");
		player.getPackets().sendIComponentText(1157, 63, "");

		player.getPackets().sendIComponentText(1157, 64, "");
		player.getPackets().sendIComponentText(1157, 65, "");
		player.getPackets().sendIComponentText(1157, 66, "");
		
		player.getPackets().sendIComponentText(1157, 67, "");
		player.getPackets().sendIComponentText(1157, 68, "");
		player.getPackets().sendIComponentText(1157, 69, "");
		
		player.getPackets().sendIComponentText(1157, 70, "");
		player.getPackets().sendIComponentText(1157, 71, "");
		player.getPackets().sendIComponentText(1157, 72, "");
		
		player.getPackets().sendIComponentText(1157, 74, "");
		player.getPackets().sendIComponentText(1157, 75, "");
		player.getPackets().sendIComponentText(1157, 76, "");
		AccountManager.removeComponents(player);
		AccountManager.removeAttributes(player, "MessageFilter");
	}
	
	public static void handleInterface(Player player, int componentId) {
		switch (componentId) {
		case 0:
			player.setWorldMessageOff(!player.isWorldMessageOff());
			player.out("You have " + (player.isWorldMessageOff() ? "enabled" : "disabled") + " the world messages filter.");
			sendInterface(player, false);
			break;

		case 1:
			player.setYellOff(!player.isYellOff());
			player.out("You have " + (player.isYellOff() ? "enabled" : "disabled") + " the yell messages  filter.");
			sendInterface(player, false);
			break;
			
		case 2:
			player.setToggleKillcount(!player.isToggleKillcount());
			player.out("You have " + (player.isToggleKillcount() ? "enabled" : "disabled") + " the killcount messages filter.");
			sendInterface(player, false);
			break;
			
		}
	}
}