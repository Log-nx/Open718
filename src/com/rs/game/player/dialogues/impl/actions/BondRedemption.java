package com.rs.game.player.dialogues.impl.actions;

import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.dialogues.Dialogue;

public class BondRedemption extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		sendItemDialogue(ItemIdentifiers.BOND_UNTRADEABLE, "This is can be used to increase your donation amount.");
		
	}
	
	@Override
	public void run(int interfaceId, int componentId) throws ClassNotFoundException {
		switch (stage) {
		case 1:
			stage = 2;
			sendOptionsDialogue("Do you want to redeem this bond?", "Yes.", "No.");
			break;
		case 2:
			if (componentId == OPTION_1) {
				player.getInventory().deleteItem(ItemIdentifiers.BOND_UNTRADEABLE, 1);
				player.getDonationManager().increaseDonationAmount(5);
				end();
			}
			if (componentId == OPTION_2) {
				end();
			}
			break;
			}
		}
	
	@Override
	public void finish() {
	}

}
