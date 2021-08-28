package com.rs.game.player.content.perks;

import com.rs.Settings;
import com.rs.game.player.DropLog;
import com.rs.game.player.Player;
import com.rs.game.player.content.PlayerDesign;
import com.rs.utils.Color;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class SkillingPerks {

	public static void sendInterface(Player player) {
		player.getTemporaryAttributtes().put("SkillingPerkSettings", true);
		player.getInterfaceManager().sendInterface(1157);
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Skilling Perk Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Perks");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "Certified Chef");
		player.getPackets().sendIComponentText(1157, 48, (player.getPerkManager().hasPerk(PlayerPerks.NO_BURNING)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Divination Expert");
		player.getPackets().sendIComponentText(1157, 51, (player.getPerkManager().hasPerk(PlayerPerks.MORE_DIVINATION)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 52, "");
		player.getPackets().sendIComponentText(1157, 53, "Green Thumb");
		player.getPackets().sendIComponentText(1157, 54, (player.getPerkManager().hasPerk(PlayerPerks.FARMING_MASTER)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 55, "");
		player.getPackets().sendIComponentText(1157, 56, "Herb Law");
		player.getPackets().sendIComponentText(1157, 57, (player.getPerkManager().hasPerk(PlayerPerks.HERB_DROPS)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 58, "");
		player.getPackets().sendIComponentText(1157, 59, "Huntsman");
		player.getPackets().sendIComponentText(1157, 60, (player.getPerkManager().hasPerk(PlayerPerks.MASTER_HUNTSMAN)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 61, "");
		player.getPackets().sendIComponentText(1157, 62, "Lumberjack");
		player.getPackets().sendIComponentText(1157, 63, (player.getPerkManager().hasPerk(PlayerPerks.BANK_LOGS)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 64, "");
		player.getPackets().sendIComponentText(1157, 65, "Master Fisherman");
		player.getPackets().sendIComponentText(1157, 66, (player.getPerkManager().hasPerk(PlayerPerks.MORE_FISH)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 67, "");
		player.getPackets().sendIComponentText(1157, 68, "Prayer Betrayer");
		player.getPackets().sendIComponentText(1157, 69, (player.getPerkManager().hasPerk(PlayerPerks.SLOWER_PRAYER_DRAIN)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 70, "");
		player.getPackets().sendIComponentText(1157, 71, "Quarrymaster");
		player.getPackets().sendIComponentText(1157, 72, (player.getPerkManager().hasPerk(PlayerPerks.MORE_ORE)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 74, "");
		player.getPackets().sendIComponentText(1157, 75, "Sneak Factor");
		player.getPackets().sendIComponentText(1157, 76, (player.getPerkManager().hasPerk(PlayerPerks.SLEIGHT_OF_HAND)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		int[] componentIds = { 73 };
		// let's clear everything else
		for (int id : componentIds) {
			player.getPackets().sendIComponentText(1157, id, "");
		}
	}
	
	public static void handleInterface(Player player, int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 0:
			if (player.getPerkManager().hasPerk(PlayerPerks.CERTIFIED_CHEF) && !player.getPerkManager().hasPerk(PlayerPerks.NO_BURNING)) {
				player.getPerkManager().unlockPerk(PlayerPerks.NO_BURNING);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Certified Chef perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.CERTIFIED_CHEF) && player.getPerkManager().hasPerk(PlayerPerks.NO_BURNING)) {
				player.getPerkManager().lockPerk(PlayerPerks.NO_BURNING);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Certified Chef perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Certified Chef perk.");
			break;

		case 1:
			if (player.getPerkManager().hasPerk(PlayerPerks.MASTER_DIVINER) && !player.getPerkManager().hasPerk(PlayerPerks.MORE_DIVINATION)) {
				player.getPerkManager().unlockPerk(PlayerPerks.MORE_DIVINATION);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Divination Expert perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.MASTER_DIVINER) && player.getPerkManager().hasPerk(PlayerPerks.MORE_DIVINATION)) {
				player.getPerkManager().lockPerk(PlayerPerks.MORE_DIVINATION);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Divination Expert perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Divination Expert perk.");
			break;
			
		case 2:
			if (player.getPerkManager().hasPerk(PlayerPerks.GREEN_THUMB) && !player.getPerkManager().hasPerk(PlayerPerks.FARMING_MASTER)) {
				player.getPerkManager().unlockPerk(PlayerPerks.FARMING_MASTER);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Green Thumb perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.GREEN_THUMB) && player.getPerkManager().hasPerk(PlayerPerks.FARMING_MASTER)) {
				player.getPerkManager().lockPerk(PlayerPerks.FARMING_MASTER);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Green Thumb perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Green Thumb perk.");
			break;
			
		case 3:
			if (player.getPerkManager().hasPerk(PlayerPerks.HERB_LAW) && !player.getPerkManager().hasPerk(PlayerPerks.HERB_DROPS)) {
				player.getPerkManager().unlockPerk(PlayerPerks.HERB_DROPS);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Herb Law perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.HERB_LAW) && player.getPerkManager().hasPerk(PlayerPerks.HERB_DROPS)) {
				player.getPerkManager().lockPerk(PlayerPerks.HERB_DROPS);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Herb Law perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Herb Law perk.");
			break;

		case 4:
			if (player.getPerkManager().hasPerk(PlayerPerks.HUNTSMAN) && !player.getPerkManager().hasPerk(PlayerPerks.MASTER_HUNTSMAN)) {
				player.getPerkManager().unlockPerk(PlayerPerks.MASTER_HUNTSMAN);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Huntsman perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.HUNTSMAN) && player.getPerkManager().hasPerk(PlayerPerks.MASTER_HUNTSMAN)) {
				player.getPerkManager().lockPerk(PlayerPerks.MASTER_HUNTSMAN);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Huntsman perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Huntsman perk.");
			break;
			
		case 6:
			if (player.getPerkManager().hasPerk(PlayerPerks.MASTER_FISHERMAN) && !player.getPerkManager().hasPerk(PlayerPerks.MORE_FISH)) {
				player.getPerkManager().unlockPerk(PlayerPerks.MORE_FISH);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Master Fisherman perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.MASTER_FISHERMAN) && player.getPerkManager().hasPerk(PlayerPerks.MORE_FISH)) {
				player.getPerkManager().lockPerk(PlayerPerks.MORE_FISH);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Master Fisherman perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Master Fisherman perk.");
			break;

		case 7:
			if (player.getPerkManager().hasPerk(PlayerPerks.PRAYER_BETRAYER) && !player.getPerkManager().hasPerk(PlayerPerks.SLOWER_PRAYER_DRAIN)) {
				player.getPerkManager().unlockPerk(PlayerPerks.SLOWER_PRAYER_DRAIN);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Prayer Betrayer perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.PRAYER_BETRAYER) && player.getPerkManager().hasPerk(PlayerPerks.SLOWER_PRAYER_DRAIN)) {
				player.getPerkManager().lockPerk(PlayerPerks.SLOWER_PRAYER_DRAIN);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Prayer Betrayer perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Prayer Betrayer perk.");
			break;
			
		case 5:
			if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERMAN) && !player.getPerkManager().hasPerk(PlayerPerks.BANK_LOGS)) {
				player.getPerkManager().unlockPerk(PlayerPerks.BANK_LOGS);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Lumberjack perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERMAN) && player.getPerkManager().hasPerk(PlayerPerks.BANK_LOGS)) {
				player.getPerkManager().lockPerk(PlayerPerks.BANK_LOGS);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Lumberjack perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Lumberjack perk.");
			break;
			
		case 8:
			if (player.getPerkManager().hasPerk(PlayerPerks.QUARRY_MASTER) && !player.getPerkManager().hasPerk(PlayerPerks.MORE_ORE)) {
				player.getPerkManager().unlockPerk(PlayerPerks.MORE_ORE);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Quarrymaster perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.QUARRY_MASTER) && player.getPerkManager().hasPerk(PlayerPerks.MORE_ORE)) {
				player.getPerkManager().lockPerk(PlayerPerks.MORE_ORE);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Quarrymaster perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Quarrymaster perk.");
			break;
			
		case 9:
			if (player.getPerkManager().hasPerk(PlayerPerks.SNEAK_FACTOR) && !player.getPerkManager().hasPerk(PlayerPerks.SLEIGHT_OF_HAND)) {
				player.getPerkManager().unlockPerk(PlayerPerks.SLEIGHT_OF_HAND);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Sneak Factor perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.SNEAK_FACTOR) && player.getPerkManager().hasPerk(PlayerPerks.SLEIGHT_OF_HAND)) {
				player.getPerkManager().lockPerk(PlayerPerks.SLEIGHT_OF_HAND);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Sneak Factor perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Sneak Factor perk.");
			break;
			
		}
	}
	
	public static void refreshInterface(Player player) {
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Skilling Perk Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Perks");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "Certified Chef");
		player.getPackets().sendIComponentText(1157, 48, (player.getPerkManager().hasPerk(PlayerPerks.NO_BURNING)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Divination Expert");
		player.getPackets().sendIComponentText(1157, 51, (player.getPerkManager().hasPerk(PlayerPerks.MORE_DIVINATION)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 52, "");
		player.getPackets().sendIComponentText(1157, 53, "Green Thumb");
		player.getPackets().sendIComponentText(1157, 54, (player.getPerkManager().hasPerk(PlayerPerks.FARMING_MASTER)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 55, "");
		player.getPackets().sendIComponentText(1157, 56, "Herb Law");
		player.getPackets().sendIComponentText(1157, 57, (player.getPerkManager().hasPerk(PlayerPerks.HERB_DROPS)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 58, "");
		player.getPackets().sendIComponentText(1157, 59, "Huntsman");
		player.getPackets().sendIComponentText(1157, 60, (player.getPerkManager().hasPerk(PlayerPerks.MASTER_HUNTSMAN)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 61, "");
		player.getPackets().sendIComponentText(1157, 62, "Lumberjack");
		player.getPackets().sendIComponentText(1157, 63, (player.getPerkManager().hasPerk(PlayerPerks.BANK_LOGS)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 64, "");
		player.getPackets().sendIComponentText(1157, 65, "Master Fisherman");
		player.getPackets().sendIComponentText(1157, 66, (player.getPerkManager().hasPerk(PlayerPerks.MORE_FISH)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 67, "");
		player.getPackets().sendIComponentText(1157, 68, "Prayer Betrayer");
		player.getPackets().sendIComponentText(1157, 69, (player.getPerkManager().hasPerk(PlayerPerks.SLOWER_PRAYER_DRAIN)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 70, "");
		player.getPackets().sendIComponentText(1157, 71, "Quarrymaster");
		player.getPackets().sendIComponentText(1157, 72, (player.getPerkManager().hasPerk(PlayerPerks.MORE_ORE)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 74, "");
		player.getPackets().sendIComponentText(1157, 75, "Sneak Factor");
		player.getPackets().sendIComponentText(1157, 76, (player.getPerkManager().hasPerk(PlayerPerks.SLEIGHT_OF_HAND)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		int[] componentIds = { 73 };
		// let's clear everything else
		for (int id : componentIds) {
			player.getPackets().sendIComponentText(1157, id, "");
		}
	}
	
	
}