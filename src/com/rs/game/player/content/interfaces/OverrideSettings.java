package com.rs.game.player.content.interfaces;

import com.rs.game.player.Player;

public class OverrideSettings {

	public static void sendInterface(Player player, boolean full) {
		if (full) {
			player.getInterfaceManager().sendInterface(1157);
			player.getTemporaryAttributtes().put("OverrideSettings", true);
		}
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Override Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Settings");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "Cosmetic");
		player.getPackets().sendIComponentText(1157, 48, "Press to change cosmetic overrides!");
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Teleport");
		player.getPackets().sendIComponentText(1157, 51, "Press to change teleport overrides!");

		player.getPackets().sendIComponentText(1157, 52, "");
		player.getPackets().sendIComponentText(1157, 53, "");
		player.getPackets().sendIComponentText(1157, 54, "");

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
		AccountManager.removeAttributes(player, "OverrideSettings");
	}
	
	public static void handleInterface(Player player, int componentId) {
		switch (componentId) {
		case 0:
			player.getCosmeticManager().sendInterface();
			break;

		case 1:
			TeleportOverrideSettings.sendInterface(player, true);
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
}