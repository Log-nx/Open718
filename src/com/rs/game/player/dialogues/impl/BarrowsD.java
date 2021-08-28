package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.dialogues.Dialogue;

public class BarrowsD extends Dialogue {

	@Override
	public void start() {
		sendDialogue("You've found a hidden tunnel, do you want to enter?");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, I'm fearless.", "No way, that looks scary!");
		} else if (stage == 0) {
			if (componentId == OPTION_1) 
				if (player.getPerkManager().hasPerk(PlayerPerks.BARROWS_BROTHER)) {
					player.setNextWorldTile(new WorldTile(3552, 9694, 0));
				} else {
					player.setNextWorldTile(new WorldTile(3534, 9677, 0));
				}
			end();
		}
	}

	@Override
	public void finish() {

	}

}
