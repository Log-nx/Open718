package com.rs.game.player.dialogues.impl.playerports;

import com.rs.game.player.Skills;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

/**
 * Handles The Trader's dialogue for rewards option.
 */
public class TheTradersRewardD extends Dialogue {

	// The NPC ID.
	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.getPorts().firstTimer) {
			sendDialogue("The Trader has no goods for sale at the moment. Check bank once you have " + "completed the Port tutorial.");
			stage = 99;
		} else
			sendOptionsDialogue("Select an Option", "See available Loyalty titles", 
					"Exchange resources for Chime", "Exchange resources for Equipment");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case -1:
			switch (componentId) {
			case OPTION_1:
				sendOptionsDialogue(Colors.RED+"Loyalty Titles #1", 
						player.getDisplayName()+Colors.DARK_RED+" the Cabin "+(player.getAppearence().isMale() ? "Boy" : "Girl")+"</col> - "+(player.getPorts().portScore > 0 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 1 Port Score to unlock."), 
						Colors.DARK_RED+"Bo'sun</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 400 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 400 Port Score to unlock."),
						Colors.DARK_RED+"First Mate</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 800 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 800 Port Score to unlock."),
						Colors.DARK_RED+"Cap'n</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 1200 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 1'200 Port Score to unlock."),
						"More Options..");
				stage = 0;
				break;
			case OPTION_2:
				sendNPCDialogue(npcId, NORMAL, "I will exchange any of the 3 resource types for Chime at a 1:5 rate. "
						+ "Simply select which resource you would like to get rid of and enter the amount to exchange.");
				stage = 2;
				break;
			case OPTION_3:
				sendOptionsDialogue(Colors.RED+"Which equipment would you like?", 
						"Tetsu equipment (requires level 90 Smithing)", 
						"Seasinger's equipment (requires level 90 Runecrafting)", 
						"Death Lotus equipment (requires level 90 Crafting)");
				stage = 4;
				break;
			}
			break;
		case 0:
			switch (componentId) {
			case OPTION_1:
				if (player.getPorts().portScore > 0) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1517);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_2:
				if (player.getPorts().portScore >= 400) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1026);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_3:
				if (player.getPorts().portScore >= 800) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1027);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_4:
				if (player.getPorts().portScore >= 1200) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1028);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_5:
				sendOptionsDialogue(Colors.RED+"Loyalty Titles #2", 
						Colors.DARK_RED+"Commodore</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 1600 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 1'600 Port Score to unlock."), 
						Colors.DARK_RED+"Admiral</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 2000 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 2'00 Port Score to unlock."),
						Colors.DARK_RED+"Admiral of the Fleet</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 3500 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 3'500 Port Score to unlock."),
						Colors.DARK_RED+"Portmaster</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 4500 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 4'500 Port Score to unlock."),
						"More Options..");
				stage = 1;
				break;
			}
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				if (player.getPorts().portScore >= 1600) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1029);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_2:
				if (player.getPorts().portScore >= 2000) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1030);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_3:
				if (player.getPorts().portScore >= 3500) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1031);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_4:
				if (player.getPorts().portScore >= 4500) {
					sendNPCDialogue(npcId, NORMAL, "I've successfully changed your Loyalty title!");
					player.getAppearence().setTitle(1032);
					break;
				}
				sendNPCDialogue(npcId, CALM, "Sadly you do not have enough Port Score to use this Loyalty title. "
						+ "Send some ships on soccessful voyages to increase it.");
				stage = 99;
				break;
			case OPTION_5:
				sendOptionsDialogue(Colors.RED+"Loyalty Titles #1", 
						player.getDisplayName()+Colors.DARK_RED+" the Cabin "+(player.getAppearence().isMale() ? "Boy" : "Girl")+"</col> - "+(player.getPorts().portScore > 0 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 1 Port Score to unlock."), 
						Colors.DARK_RED+"Bo'sun</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 400 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 400 Port Score to unlock."),
						Colors.DARK_RED+"First Mate</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 800 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 800 Port Score to unlock."),
						Colors.DARK_RED+"Cap'n</col> "+player.getDisplayName()+"</col> - "+(player.getPorts().portScore >= 1200 ? Colors.GREEN+"Unlocked" : Colors.RED+"Locked</col> - need 1'200 Port Score to unlock."),
						"More Options..");
				stage = 0;
				break;
			}
			break;
		case 2:
			sendOptionsDialogue(Colors.RED+"Exchange Resources for Chime", 
					"Plate (You have: "+Utils.getFormattedNumber(player.getPorts().plate)+")", 
					"Chi Globe (You have: "+Utils.getFormattedNumber(player.getPorts().chiGlobe)+")", 
					"Lacquer (You have: "+Utils.getFormattedNumber(player.getPorts().lacquer)+")");
			stage = 3;
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				end();
				player.getTemporaryAttributtes().put("ports_plate", Boolean.TRUE);
				player.getPackets().sendRunScript(108, new Object[] {
		    		"How many would you like to exchange?" });
				break;
			case OPTION_2:
				end();
				player.getTemporaryAttributtes().put("ports_chiGlobe", Boolean.TRUE);
				player.getPackets().sendRunScript(108, new Object[] {
		    		"How many would you like to exchange?" });
				break;
			case OPTION_3:
				end();
				player.getTemporaryAttributtes().put("ports_lacquer", Boolean.TRUE);
				player.getPackets().sendRunScript(108, new Object[] {
		    		"How many would you like to exchange?" });
				break;
			}
			break;
		case 4:
			switch (componentId) {
			case OPTION_1:
				if (player.getSkills().getLevelForXp(Skills.SMITHING) < 90) {
					sendNPCDialogue(npcId, SAD, "You do not have the appropriate level to unlock this equipment..");
					stage = 98;
					break;
				}
				sendOptionsDialogue(Colors.RED+"Tetsu Equipment<col> (You have "+Utils.getFormattedNumber(player.getPorts().plate)+" plate).", 
						(player.getPorts().plate < 80 ? Colors.RED : Colors.GREEN)+"Tetsu Helm - 80 plate", 
						(player.getPorts().plate < 125 ? Colors.RED : Colors.GREEN)+"Tetsu Body - 125 plate", 
						(player.getPorts().plate < 100 ? Colors.RED : Colors.GREEN)+"Tetsu Legs - 100 plate", 
						(player.getPorts().plate < 150 ? Colors.RED : Colors.GREEN)+"Tetsu Katana - 150 plate");
				stage = 5;
				break;
			case OPTION_2:
				if (player.getSkills().getLevelForXp(Skills.RUNECRAFTING) < 90) {
					sendNPCDialogue(npcId, SAD, "You do not have the appropriate level to unlock this equipment..");
					stage = 98;
					break;
				}
				sendOptionsDialogue(Colors.RED+"Seasinger's Equipment<col> (You have "+Utils.getFormattedNumber(player.getPorts().chiGlobe)+" chi globe).", 
						(player.getPorts().chiGlobe < 80 ? Colors.RED : Colors.GREEN)+"Seasinger's Hood - 80 chi globe", 
						(player.getPorts().chiGlobe < 125 ? Colors.RED : Colors.GREEN)+"Seasinger's Robe Top - 125 chi globe", 
						(player.getPorts().chiGlobe < 100 ? Colors.RED : Colors.GREEN)+"Seasinger's Robe Bottom - 100 chi globe", 
						(player.getPorts().chiGlobe < 150 ? Colors.RED : Colors.GREEN)+"Seasinger's Kiba - 150 chi globe", 
						(player.getPorts().chiGlobe < 100 ? Colors.RED : Colors.GREEN)+"Seasinger's Makigai - 100 chi globe");
				stage = 6;
				break;
			case OPTION_3:
				if (player.getSkills().getLevelForXp(Skills.CRAFTING) < 90) {
					sendNPCDialogue(npcId, SAD, "You do not have the appropriate level to unlock this equipment..");
					stage = 98;
					break;
				}
				sendOptionsDialogue(Colors.RED+"Death Lotus Equipment<col> (You have "+Utils.getFormattedNumber(player.getPorts().lacquer)+" lacquer).", 
						(player.getPorts().lacquer < 80 ? Colors.RED : Colors.GREEN)+"Death Lotus Hood - 80 lacquer", 
						(player.getPorts().lacquer < 125 ? Colors.RED : Colors.GREEN)+"Death Lotus Chestplate - 125 lacquer", 
						(player.getPorts().lacquer < 100 ? Colors.RED : Colors.GREEN)+"Death Lotus Chaps - 100 lacquer", 
						(player.getPorts().lacquer < 1 ? Colors.RED : Colors.GREEN)+"Death Lotus Dart (x25) - 1 lacquer");
				stage = 7;
				break;
			}
			break;
		case 5:
			switch (componentId) {
			case OPTION_1:
				if (player.getPorts().plate < 80) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough plate to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().plate -= 80;
				player.getInventory().addItemDrop(26325, 1);
				player.getSkills().addXp(Skills.SMITHING, 1000);
				sendItemDialogue(26325, 1, "The Trader hands you your item in exchange for plate.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_2:
				if (player.getPorts().plate < 125) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough plate to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().plate -= 125;
				player.getInventory().addItemDrop(26326, 1);
				player.getSkills().addXp(Skills.SMITHING, 3000);
				sendItemDialogue(26326, 1, "The Trader hands you your item in exchange for plate.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_3:
				if (player.getPorts().plate < 100) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough plate to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().plate -= 100;
				player.getInventory().addItemDrop(26327, 1);
				player.getSkills().addXp(Skills.SMITHING, 2000);
				sendItemDialogue(26327, 1, "The Trader hands you your item in exchange for plate.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_4:
				if (player.getPorts().plate < 150) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough plate to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().plate -= 150;
				player.getInventory().addItemDrop(33879, 1);
				player.getSkills().addXp(Skills.SMITHING, 3000);
				sendItemDialogue(33879, 1, "The Trader hands you your item in exchange for plate.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			}
			break;
		case 6:
			switch (componentId) {
			case OPTION_1:
				if (player.getPorts().chiGlobe < 80) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough chi globe to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().chiGlobe -= 80;
				player.getInventory().addItemDrop(26337, 1);
				player.getSkills().addXp(Skills.RUNECRAFTING, 1000);
				sendItemDialogue(26337, 1, "The Trader hands you your item in exchange chi globe.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_2:
				if (player.getPorts().chiGlobe < 125) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough chi globe to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().chiGlobe -= 125;
				player.getInventory().addItemDrop(26338, 1);
				player.getSkills().addXp(Skills.RUNECRAFTING, 3000);
				sendItemDialogue(26338, 1, "The Trader hands you your item in exchange chi globe.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_3:
				if (player.getPorts().chiGlobe < 100) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough chi globe to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().chiGlobe -= 100;
				player.getInventory().addItemDrop(26339, 1);
				player.getSkills().addXp(Skills.RUNECRAFTING, 2000);
				sendItemDialogue(26339, 1, "The Trader hands you your item in exchange chi globe.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_4:
				if (player.getPorts().chiGlobe < 150) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough chi globe to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().chiGlobe -= 150;
				player.getInventory().addItemDrop(33886, 1);
				player.getSkills().addXp(Skills.RUNECRAFTING, 3000);
				sendItemDialogue(33886, 1, "The Trader hands you your item in exchange chi globe.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_5:
				if (player.getPorts().chiGlobe < 100) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough chi globe to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().chiGlobe -= 100;
				player.getInventory().addItemDrop(33889, 1);
				player.getSkills().addXp(Skills.RUNECRAFTING, 1000);
				sendItemDialogue(33889, 1, "The Trader hands you your item in exchange chi globe.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			}
			break;
		case 7:
			switch (componentId) {
			case OPTION_1:
				if (player.getPorts().lacquer < 80) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough lacquer to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().lacquer -= 80;
				player.getInventory().addItemDrop(26346, 1);
				player.getSkills().addXp(Skills.CRAFTING, 1000);
				sendItemDialogue(26346, 1, "The Trader hands you your item in exchange for lacquer.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_2:
				if (player.getPorts().lacquer < 125) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough lacquer to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().lacquer -= 125;
				player.getInventory().addItemDrop(26347, 1);
				player.getSkills().addXp(Skills.CRAFTING, 3000);
				sendItemDialogue(26347, 1, "The Trader hands you your item in exchange for lacquer.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_3:
				if (player.getPorts().lacquer < 100) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough lacquer to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				player.getPorts().lacquer -= 100;
				player.getInventory().addItemDrop(26348, 1);
				player.getSkills().addXp(Skills.CRAFTING, 2000);
				sendItemDialogue(26348, 1, "The Trader hands you your item in exchange for lacquer.<br>"
						+ "You can use this item on The Trader in order to create a Superior version.");
				stage = 99;
				break;
			case OPTION_4:
				if (player.getPorts().lacquer < 1) {
					sendNPCDialogue(npcId, CALM, "Seems like you do not have enough lacquer to get this item. "
							+ "Send some of your ships on voyages to get more.");
					stage = 98;
					break;
				}
				if (player.getSkills().getLevelForXp(Skills.FLETCHING) < 92) {
					sendNPCDialogue(npcId, SAD, "You do not have the appropriate level to unlock this equipment..");
					stage = 98;
					break;
				}
				player.getPorts().lacquer -= 1;
				player.getInventory().addItemDrop(30574, 25);
				player.getSkills().addXp(Skills.FLETCHING, 250);
				sendItemDialogue(30574, 25, "The Trader hands you your item in exchange for lacquer.");
				stage = 98;
				break;
			}
			break;

		case 98:
			sendOptionsDialogue(Colors.RED+"Which equipment would you like?",
					"Tetsu equipment (requires level 90 Smithing)",
					"Seasinger's equipment (requires level 90 Runecrafting)",
					"Death Lotus equipment (requires level 90 Crafting)");
			stage = 4;
			break;
		case 99:
			end();
			break;
		}
	}

	@Override
	public void finish() {
	}
}