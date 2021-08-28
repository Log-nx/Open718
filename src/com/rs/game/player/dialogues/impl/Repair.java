package com.rs.game.player.dialogues.impl;

import com.rs.game.player.content.RepairItems;
import com.rs.game.player.dialogues.Dialogue;

public class Repair extends Dialogue {
	private int itemId;

	@Override
	public void start() {
		itemId = (Integer) parameters[0];
		sendOptionsDialogue("What would you like to do?", "Repair my item!", "How much would it cost?", "Nevermind..");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		int amount = player.getInventory().getItems().getNumberOf(itemId);
		if (stage == -1) {
			if (componentId == OPTION_1) {
				if (amount == 1) {
					RepairItems.Repair(player, itemId, 1);
					return;
				}
				sendOptionsDialogue("What would you like to do?", "Repair one item.", "Repair X of your items", "Repair all items.");
				stage = 2;
			} else if (componentId == OPTION_2) {
				RepairItems.CheckPrice(player, itemId, amount);
			} else {
				end();
			}

		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				RepairItems.Repair(player, itemId, 1);
			} else if (componentId == OPTION_2) {
				player.getTemporaryAttributtes().put("Repair", Integer.valueOf(0));
				player.getTemporaryAttributtes().put("Ritem", itemId);
				player.getPackets().sendInputIntegerScript("You have " + amount + " items that need to be repaired, how many would you like to repair?");
				end();
			} else {
				RepairItems.Repair(player, itemId, amount);
			}
		}
	}

	@Override
	public void finish() {
	}
}