package com.rs.game.player.dialogues.impl;

import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;

// created by Fatal Resort - eradicationx.com

public class PartnerPortal extends Dialogue {

	public PartnerPortal() {
	}

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Bank Booth", "Leave Instance");
		stage = 1;

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				Player leader = player.getLeaderName();
				if ((leader.getInstanceBooth() <= 0 && !leader.isPermaBank())) {
					end();
					player.sm("The leader hasn't bought this option.");
				} else {
					player.stopAll();
					player.getBank().openBank();
				}
			} else if (componentId == OPTION_2) {
			stage = 2;
			sendOptionsDialogue("Are you sure you want to leave?", "Yes", "No");
			}
	  	} else if (stage == 2) {
			if (componentId == OPTION_1) {
				player.setInstanceEnd(true);
				player.sm("Your instance has ended. You will now be sent to the original room.");
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		}

	}
	@Override
	public void finish() {
	}

}