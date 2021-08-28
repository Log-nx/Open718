package com.rs.game.player.content.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.custom.CosmeticManager.TeleportOverrides;
import com.rs.utils.Color;
import com.rs.utils.Colors;

public class TeleportOverrideSettings {

	public static void sendInterface(Player player, boolean full) {
		TeleportOverrides override = player.getCosmeticManager().getTeleportOverride();
		if (full) {
			player.getInterfaceManager().sendInterface(1157);
			player.getTemporaryAttributtes().put("TeleportOverrides", true);
		}
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Teleport Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Settings");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "Rocket");
		player.getPackets().sendIComponentText(1157, 48, (override == TeleportOverrides.ROCKET ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled"));
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Assassin");
		player.getPackets().sendIComponentText(1157, 51, (override == TeleportOverrides.ASSASSIN ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled"));

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
		AccountManager.removeAttributes(player, "TeleportOverrides");
	}
	
	public static void handleInterface(Player player, int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 0:
			player.getCosmeticManager().setTeleportOverride(TeleportOverrides.ROCKET);
			sendInterface(player, false);
			break;

		case 1:
			player.getCosmeticManager().setTeleportOverride(TeleportOverrides.ASSASSIN);
			sendInterface(player, false);
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