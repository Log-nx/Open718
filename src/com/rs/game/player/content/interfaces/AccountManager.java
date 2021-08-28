package com.rs.game.player.content.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.PlayerDesign;
import com.rs.game.player.content.activities.events.WorldEvents;
import com.rs.game.player.content.perks.PerkInterfaceManager;
import com.rs.game.player.content.perks.SkillingPerks;
import com.rs.game.player.content.perks.UtilityPerks;

public class AccountManager {

	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(1157);
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Account Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Settings");
		player.getPackets().sendIComponentText(1157, 34, "Toggle/View");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "Appearence");
		player.getPackets().sendIComponentText(1157, 48, "Press to customize!");
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Message Filters");
		player.getPackets().sendIComponentText(1157, 51, "Press to view message filters!");

		player.getPackets().sendIComponentText(1157, 52, "");
		player.getPackets().sendIComponentText(1157, 53, "Drop Filters");
		player.getPackets().sendIComponentText(1157, 54, "Press to view drop filters!");

		player.getPackets().sendIComponentText(1157, 55, "");
		player.getPackets().sendIComponentText(1157, 56, "Overrides");
		player.getPackets().sendIComponentText(1157, 57, "Press to view overrides!");

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
		removeComponents(player);
	}
	
	public static void handleInterface(Player player, int componentId) {
		switch (componentId) {
		case 0:
			PlayerDesign.open(player);
			break;

		case 1:
			MessageFilters.sendInterface(player, true);
			break;
			
		case 2:
			LootbeamSettings.sendInterface(player, true);
			break;
			
		case 3:
			OverrideSettings.sendInterface(player, true);
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
		default:
			player.getPackets().sendGameMessage("Button "+componentId+" does not have an action set.");
			break;
			
		}
	}
	
	public static boolean handleAccount(Player player, int interfaceId, int componentId) {
		if (interfaceId == 1157) {
			if (player.getTemporaryAttributtes().get("AccountSettings") != null) {
				AccountManager.handleInterface(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("PerkSettings") != null) {
				PerkInterfaceManager.handleInterface(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("UtilityPerkSettings") != null) {
				UtilityPerks.handleInterface(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("UtilityPerkSettings2") != null) {
				UtilityPerks.handlePageTwo(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("SkillingPerkSettings") != null) {
				SkillingPerks.handleInterface(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("LootbeamSettings") != null) {
				LootbeamSettings.handleInterface(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("MessageFilter") != null) {
				MessageFilters.handleInterface(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("OverrideSettings") != null) {
				OverrideSettings.handleInterface(player, componentId);
				return true;
			}
			if (player.getTemporaryAttributtes().get("TeleportOverrides") != null) {
				TeleportOverrideSettings.handleInterface(player, componentId);
				return true;
			} else {
				WorldEvents.handleButtons(player, componentId);
				return true;
			}
		}
		return false;
	}
	
	
	public static void removeComponents(Player player) {
		int[] componentIds = { 73 };
		for (int id : componentIds) {
			player.getPackets().sendIComponentText(1157, id, "");
		}
	}

	public static void removeAttributtes(Player player) {
		player.getTemporaryAttributtes().put("AccountSettings", true);
		player.getTemporaryAttributtes().remove("PerkSettings");
		player.getTemporaryAttributtes().remove("UtilityPerkSettings");
		player.getTemporaryAttributtes().remove("SkillingPerkSettings");
		player.getTemporaryAttributtes().remove("LootbeamSettings");
		player.getTemporaryAttributtes().remove("OverrideSettings");
	}
	
	public static void removeAttributes(Player player, String attribute) {
		player.getTemporaryAttributtes().remove("PerkSettings");
		player.getTemporaryAttributtes().remove("UtilityPerkSettings");
		player.getTemporaryAttributtes().remove("SkillingPerkSettings");
		player.getTemporaryAttributtes().remove("UtilityPerkSettings2");
		player.getTemporaryAttributtes().remove("LootbeamSettings");
		player.getTemporaryAttributtes().remove("OverrideSettings");
		player.getTemporaryAttributtes().remove("AccountSettings");
		player.getTemporaryAttributtes().put(attribute, true);
		
	}
	
}