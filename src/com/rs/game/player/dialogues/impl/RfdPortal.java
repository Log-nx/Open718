package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;

public class RfdPortal extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			end();
		}
	}

	@Override
	public void start() {
		stage = 1;
		sendDialogue("You shouldn't try this again.");
	}

}