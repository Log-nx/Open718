package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class JatixShop extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				ShopsHandler.openShop(player, 26);
				end();
			}
			if (componentId == OPTION_2) {
				ShopsHandler.openShop(player, 27);
				end();
			}
			if (componentId == OPTION_3) {
				end();
			}
		}
	}

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Herblore Grimies",
				"Herbore Ingredients", "Nevermind");
	}
}