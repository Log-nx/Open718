package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;

public class DragonBoneSetting extends Dialogue {

    private int UPGRADEKIT = 24352, ROYALCBOW = 24338;
    private int[] royalRequiredItems = { 24340, 24342, 24344, 24346 };
	public DragonBoneSetting() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Do you want to make an Item?", "Dragonbone full Helm",
				"Dragonbone Platebody", "Dragonbone Platelegs", "Dragonbone Plateskirt",
				"Next Page");

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				if (player.getInventory().containsItem(UPGRADEKIT, 1)
						&& player.getInventory().containsItem(11335, 1)) {
					    player.getInventory().deleteItem(UPGRADEKIT, 1);
					    player.getInventory().deleteItem(11335, 1);
					    player.getInventory().addItem(24359, 1);
					    player.getPackets().sendGameMessage(
						    "You made a Dragonbone full helm.");
					    end();
					} else {
					    sendDialogue("You need a Dragonbone upgrade kit and one Dragon full helm to make that.");
					    stage = 3;
					}
			
			} else if (componentId == OPTION_2) {
				if (player.getInventory().containsItem(UPGRADEKIT, 1)
						&& player.getInventory().containsItem(14479, 1)) {
					    player.getInventory().deleteItem(UPGRADEKIT, 1);
					    player.getInventory().deleteItem(14479, 1);
					    player.getInventory().addItemDrop(24360, 1);
					    player.getPackets().sendGameMessage(
						    "You made a Dragonbone platebody.");
					    end();
					} else {
					    sendDialogue("You need a Dragonbone upgrade kit and one Dragon platebody to make that.");
					    stage = 3;
					}
			
			} else if (componentId == OPTION_3) {
				if (player.getInventory().containsItem(UPGRADEKIT, 1)
						&& player.getInventory().containsItem(4087, 1)) {
					    player.getInventory().deleteItem(UPGRADEKIT, 1);
					    player.getInventory().deleteItem(4087, 1);
					    player.getInventory().addItemDrop(24363, 1);
					    player.getPackets().sendGameMessage(
						    "You made a Dragonbone platelegs.");
					    end();
					} else {
					    sendDialogue("You need a Dragonbone upgrade kit and one Dragon platelegs to make that.");
					    stage = 3;
					}
			} else if (componentId == OPTION_4) {
				if (player.getInventory().containsItem(UPGRADEKIT, 1)
						&& player.getInventory().containsItem(4585, 1)) {
					    player.getInventory().deleteItem(UPGRADEKIT, 1);
					    player.getInventory().deleteItem(4585, 1);
					    player.getInventory().addItemDrop(24364, 1);
					    player.getPackets().sendGameMessage(
						    "You made a Dragonbone plateskirt.");
					    end();
					} else {
					    sendDialogue("You need a Dragonbone upgrade kit and one Dragon plateskirt to make that.");
					    stage = 3;
					}
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Do you want to make an Item?",
						"Dragonbone mage Hat", "Dragonbone mage Top",
						"Dragonbone Bottoms", "Royal Crossbow");
				stage = 4;
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {	
				if (player.getInventory().containsItem(UPGRADEKIT, 1)
						&& player.getInventory().containsItem(6918, 1)) {
					    player.getInventory().deleteItem(UPGRADEKIT, 1);
					    player.getInventory().deleteItem(6918, 1);
					    player.getInventory().addItemDrop(24354, 1);
					    player.getPackets().sendGameMessage(
						    "You made a Dragonbone mage hat.");
					    end();
					} else {
					    sendDialogue("You need a Dragonbone upgrade kit and one Infinity hat to make that.");
					    stage = 3;
					}
			} else if (componentId == OPTION_2) {
				if (player.getInventory().containsItem(UPGRADEKIT, 1)
						&& player.getInventory().containsItem(6916, 1)) {
					    player.getInventory().deleteItem(UPGRADEKIT, 1);
					    player.getInventory().deleteItem(6916, 1);
					    player.getInventory().addItemDrop(24355, 1);
					    player.getPackets().sendGameMessage(
						    "You made a Dragonbone mage top.");
					    end();
					} else {
					    sendDialogue("You need a Dragonbone upgrade kit and one Infinity top to make that.");
					    stage = 3;
					}
			} else if (componentId == OPTION_3) {
				if (player.getInventory().containsItem(UPGRADEKIT, 1)
						&& player.getInventory().containsItem(6924, 1)) {
					    player.getInventory().deleteItem(UPGRADEKIT, 1);
					    player.getInventory().deleteItem(6924, 1);
					    player.getInventory().addItemDrop(24356, 1);
					    player.getPackets().sendGameMessage(
						    "You made a Dragonbone mage bottoms.");
					    end();
					} else {
					    sendDialogue("You need a Dragonbone upgrade kit and one Infinity bottoms to make that.");
					    stage = 3;
					}
			} else if (componentId == OPTION_4) {
				if (player.getInventory().containsItem(24344, 1)
		    			&& player.getInventory().containsItem(24340, 1)
		    			&& player.getInventory().containsItem(24342, 1)
		    			&& player.getInventory().containsItem(24346, 1)) {
		    		    for (int ITEMS : royalRequiredItems) {
		    			player.getInventory().deleteItem(ITEMS, 1);
		    		    }
		    		    player.getInventory().addItemDrop(ROYALCBOW, 1);
		    		    player.getPackets().sendGameMessage(
		    			    "You made a Royal crossbow.");
		    		    end();
		    		} else {
		    		    sendDialogue("You don't have the items required to make a Royal crossbow.");
		    		    stage = 3;
		    		}
			} else if (componentId == OPTION_4) {{
		        end();
			}
			} else if (stage == 3) {
				end();
			}
			end();
		}}
	

	@Override
	public void finish() {
	}

}