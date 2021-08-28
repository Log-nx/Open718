package com.rs.game.player.content.perks;

import com.rs.game.player.Player;
import com.rs.game.player.content.interfaces.AccountManager;
import com.rs.utils.Color;
import com.rs.utils.Colors;

public class UtilityPerks {

	public static void sendInterface(Player player) {
		player.getTemporaryAttributtes().put("UtilityPerkSettings", true);
		player.getInterfaceManager().sendInterface(1157);
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Utility Perk Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Perks");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "Charge Befriender");
		player.getPackets().sendIComponentText(1157, 48, (player.getPerkManager().hasPerk(PlayerPerks.INFINITE_CHARGE)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Cranked");
		player.getPackets().sendIComponentText(1157, 51, (player.getPerkManager().hasPerk(PlayerPerks.RUN_ENERGY)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 52, "");
		player.getPackets().sendIComponentText(1157, 53, "Decaying Totem");
		player.getPackets().sendIComponentText(1157, 54, (player.getPerkManager().hasPerk(PlayerPerks.POISON_IMMUNE)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 55, "");
		player.getPackets().sendIComponentText(1157, 56, "God Wars Specialist");
		player.getPackets().sendIComponentText(1157, 57, (player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 58, "");
		player.getPackets().sendIComponentText(1157, 59, "God Wars 2 Specialist");
		player.getPackets().sendIComponentText(1157, 60, (player.getPerkManager().hasPerk(PlayerPerks.GODWARS2_KILLCOUNT)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 61, "");
		player.getPackets().sendIComponentText(1157, 62, "Investigator");
		player.getPackets().sendIComponentText(1157, 63, (player.getPerkManager().hasPerk(PlayerPerks.CLUE_SCROLL)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 64, "");
		player.getPackets().sendIComponentText(1157, 65, "Key Expert");
		player.getPackets().sendIComponentText(1157, 66, (player.getPerkManager().hasPerk(PlayerPerks.KEY_EXPERT)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 67, "");
		player.getPackets().sendIComponentText(1157, 68, "Overclocked");
		player.getPackets().sendIComponentText(1157, 69, (player.getPerkManager().hasPerk(PlayerPerks.OVERCLOCKED)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 70, "");
		player.getPackets().sendIComponentText(1157, 71, "Pet Whisperer");
		player.getPackets().sendIComponentText(1157, 72, (player.getPerkManager().hasPerk(PlayerPerks.PET_WHISPERER)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 74, "");
		player.getPackets().sendIComponentText(1157, 75, "Next Page");
		player.getPackets().sendIComponentText(1157, 76, "Click here to view the next page");

		AccountManager.removeComponents(player);
	}
	
	public static void handleInterface(Player player, int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 0:
			if (player.getPerkManager().hasPerk(PlayerPerks.CHARGE_BEFRIENDER) && !player.getPerkManager().hasPerk(PlayerPerks.INFINITE_CHARGE)) {
				player.getPerkManager().unlockPerk(PlayerPerks.INFINITE_CHARGE);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Charge Befriender perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.CHARGE_BEFRIENDER) && player.getPerkManager().hasPerk(PlayerPerks.INFINITE_CHARGE)) {
				player.getPerkManager().lockPerk(PlayerPerks.INFINITE_CHARGE);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Charge Befriender perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Charge Befriender perk.");
			break;

		case 1:
			if (player.getPerkManager().hasPerk(PlayerPerks.CRANKED) && !player.getPerkManager().hasPerk(PlayerPerks.RUN_ENERGY)) {
				player.getPerkManager().unlockPerk(PlayerPerks.RUN_ENERGY);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Cranked perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.CRANKED) && player.getPerkManager().hasPerk(PlayerPerks.RUN_ENERGY)) {
				player.getPerkManager().lockPerk(PlayerPerks.RUN_ENERGY);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Cranked perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Cranked perk.");
			break;
			
		case 2:
			if (player.getPerkManager().hasPerk(PlayerPerks.DECAY_TOTEM) && !player.getPerkManager().hasPerk(PlayerPerks.POISON_IMMUNE)) {
				player.getPerkManager().unlockPerk(PlayerPerks.POISON_IMMUNE);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Decaying Totem perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.DECAY_TOTEM) && player.getPerkManager().hasPerk(PlayerPerks.POISON_IMMUNE)) {
				player.getPerkManager().lockPerk(PlayerPerks.POISON_IMMUNE);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Decaying Totem perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Decaying Totem perk.");
			break;
			
		case 3:
			if (player.getPerkManager().hasPerk(PlayerPerks.GODWARS_SPECIALIST) && !player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT)) {
				player.getPerkManager().unlockPerk(PlayerPerks.GODWARS_KILLCOUNT);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the God Wars Specialist perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.GODWARS_SPECIALIST) && player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT)) {
				player.getPerkManager().lockPerk(PlayerPerks.GODWARS_KILLCOUNT);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the God Wars Specialist perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the God Wars Specialist perk.");
			break;

		case 4:
			if (player.getPerkManager().hasPerk(PlayerPerks.GODWARS2_SPECIALIST) && !player.getPerkManager().hasPerk(PlayerPerks.GODWARS2_KILLCOUNT)) {
				player.getPerkManager().unlockPerk(PlayerPerks.GODWARS2_KILLCOUNT);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the God Wars 2 Specialist perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.GODWARS2_SPECIALIST) && player.getPerkManager().hasPerk(PlayerPerks.GODWARS2_KILLCOUNT)) {
				player.getPerkManager().lockPerk(PlayerPerks.GODWARS2_KILLCOUNT);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the God Wars 2 Specialist perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the God Wars 2 Specialist perk.");
			break;
			
		case 5:
			if (player.getPerkManager().hasPerk(PlayerPerks.INVESTIGATOR) && !player.getPerkManager().hasPerk(PlayerPerks.CLUE_SCROLL)) {
				player.getPerkManager().unlockPerk(PlayerPerks.CLUE_SCROLL);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Investigator perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.INVESTIGATOR) && player.getPerkManager().hasPerk(PlayerPerks.CLUE_SCROLL)) {
				player.getPerkManager().lockPerk(PlayerPerks.CLUE_SCROLL);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Investigator perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Investigator perk.");
			break;

		case 6:
			if (player.getPerkManager().hasPerk(PlayerPerks.KEY_EXPERT) && !player.getPerkManager().hasPerk(PlayerPerks.DOUBLE_KEYS)) {
				player.getPerkManager().unlockPerk(PlayerPerks.DOUBLE_KEYS);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Key Expert perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.KEY_EXPERT) && player.getPerkManager().hasPerk(PlayerPerks.DOUBLE_KEYS)) {
				player.getPerkManager().lockPerk(PlayerPerks.DOUBLE_KEYS);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Key Expert perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Key Expert perk.");
			break;
			
		case 7:
			if (player.getPerkManager().hasPerk(PlayerPerks.OVERCLOCKED) && !player.getPerkManager().hasPerk(PlayerPerks.DOUBLE_AURA)) {
				player.getPerkManager().unlockPerk(PlayerPerks.DOUBLE_AURA);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Overclocked perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.OVERCLOCKED) && player.getPerkManager().hasPerk(PlayerPerks.DOUBLE_AURA)) {
				player.getPerkManager().lockPerk(PlayerPerks.DOUBLE_AURA);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Overclocked perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Overclocked perk.");
			break;
			
		case 8:
			if (player.getPerkManager().hasPerk(PlayerPerks.PET_WHISPERER) && !player.getPerkManager().hasPerk(PlayerPerks.PET_DROPS)) {
				player.getPerkManager().unlockPerk(PlayerPerks.PET_DROPS);
			player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Pet Whisperer perk.");
			refreshInterface(player);
			return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.PET_WHISPERER) && player.getPerkManager().hasPerk(PlayerPerks.PET_DROPS)) {
				player.getPerkManager().lockPerk(PlayerPerks.PET_DROPS);
			player.getPackets().sendGameMessage(Color.RED, "You have disabled the Pet Whisperer perk.");
			refreshInterface(player);
			return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Pet Whisperer perk.");
			break;
			
		case 9:
			UtilityPerks.sendPageTwo(player);
			break;
			
		}
	}
	
	public static void refreshInterface(Player player) {
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Utility Perk Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Perks");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "Charge Befriender");
		player.getPackets().sendIComponentText(1157, 48, (player.getPerkManager().hasPerk(PlayerPerks.INFINITE_CHARGE)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "Cranked");
		player.getPackets().sendIComponentText(1157, 51, (player.getPerkManager().hasPerk(PlayerPerks.RUN_ENERGY)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 52, "");
		player.getPackets().sendIComponentText(1157, 53, "Decaying Totem");
		player.getPackets().sendIComponentText(1157, 54, (player.getPerkManager().hasPerk(PlayerPerks.POISON_IMMUNE)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 55, "");
		player.getPackets().sendIComponentText(1157, 56, "God Wars Specialist");
		player.getPackets().sendIComponentText(1157, 57, (player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 58, "");
		player.getPackets().sendIComponentText(1157, 59, "God Wars 2 Specialist");
		player.getPackets().sendIComponentText(1157, 60, (player.getPerkManager().hasPerk(PlayerPerks.GODWARS2_KILLCOUNT)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 61, "");
		player.getPackets().sendIComponentText(1157, 62, "Investigator");
		player.getPackets().sendIComponentText(1157, 63, (player.getPerkManager().hasPerk(PlayerPerks.CLUE_SCROLL)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");

		player.getPackets().sendIComponentText(1157, 64, "");
		player.getPackets().sendIComponentText(1157, 65, "Key Expert");
		player.getPackets().sendIComponentText(1157, 66, (player.getPerkManager().hasPerk(PlayerPerks.KEY_EXPERT)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 67, "");
		player.getPackets().sendIComponentText(1157, 68, "Overclocked");
		player.getPackets().sendIComponentText(1157, 69, (player.getPerkManager().hasPerk(PlayerPerks.OVERCLOCKED)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 70, "");
		player.getPackets().sendIComponentText(1157, 71, "Pet Whisperer");
		player.getPackets().sendIComponentText(1157, 72, (player.getPerkManager().hasPerk(PlayerPerks.PET_WHISPERER)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 74, "");
		player.getPackets().sendIComponentText(1157, 75, "Next Page");
		player.getPackets().sendIComponentText(1157, 76, "Click here to view the next page");
		AccountManager.removeComponents(player);
	}
	
	public static void sendPageTwo(Player player) {
		player.getTemporaryAttributtes().remove("UtilityPerkSettings");
		player.getTemporaryAttributtes().put("UtilityPerkSettings2", true);
		player.getInterfaceManager().sendInterface(1157);
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Utility Perk Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Perks");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "The Ninth Brother");
		player.getPackets().sendIComponentText(1157, 48, (player.getPerkManager().hasPerk(PlayerPerks.BARROWS_BROTHER)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "");
		player.getPackets().sendIComponentText(1157, 51, "");

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
		player.getPackets().sendIComponentText(1157, 75, "Previous Page");
		player.getPackets().sendIComponentText(1157, 76, "Click here to view the previous page");

		AccountManager.removeComponents(player);
	}
	
	public static void refreshPageTwo(Player player) {
		player.getInterfaceManager().sendInterface(1157);
		player.getPackets().sendIComponentText(1157, 92, player.getDisplayName() + "'s " + "Utility Perk Settings");
		player.getPackets().sendIComponentText(1157, 95, ""); 
		
		player.getPackets().sendIComponentText(1157, 33, "Perks");
		player.getPackets().sendIComponentText(1157, 34, "Toggle");
		
		player.getPackets().sendIComponentText(1157, 46, "");
		player.getPackets().sendIComponentText(1157, 47, "The Ninth Brother");
		player.getPackets().sendIComponentText(1157, 48, (player.getPerkManager().hasPerk(PlayerPerks.BARROWS_BROTHER)) ? Colors.GREEN + "Enabled" : Colors.RED + "Disabled");
		
		player.getPackets().sendIComponentText(1157, 49, "");
		player.getPackets().sendIComponentText(1157, 50, "");
		player.getPackets().sendIComponentText(1157, 51, "");

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
		player.getPackets().sendIComponentText(1157, 75, "Previous Page");
		player.getPackets().sendIComponentText(1157, 76, "Click here to view the previous page");
		
		AccountManager.removeComponents(player);
	}
	
	public static void handlePageTwo(Player player, int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 0:
			if (player.getPerkManager().hasPerk(PlayerPerks.THE_NINTH_BROTHER) && !player.getPerkManager().hasPerk(PlayerPerks.BARROWS_BROTHER)) {
				player.getPerkManager().unlockPerk(PlayerPerks.BARROWS_BROTHER);
				player.getPackets().sendGameMessage(Color.GREEN, "You have enabled the Ninth Brother perk.");
				refreshPageTwo(player);
				return;
			}
			if (player.getPerkManager().hasPerk(PlayerPerks.THE_NINTH_BROTHER) && player.getPerkManager().hasPerk(PlayerPerks.BARROWS_BROTHER)) {
				player.getPerkManager().lockPerk(PlayerPerks.BARROWS_BROTHER);
				player.getPackets().sendGameMessage(Color.RED, "You have disabled the Ninth Brother perk.");
				refreshPageTwo(player);
				return;
			}
			else
				player.getPackets().sendGameMessage(Color.RED, "You must have the Ninth Brother perk.");
			break;

		case 1:
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
			player.getTemporaryAttributtes().remove("UtilityPerkSettings2");
			UtilityPerks.sendInterface(player);
			break;
		}
	}
}