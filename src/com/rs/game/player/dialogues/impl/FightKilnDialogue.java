package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;

public class FightKilnDialogue extends Dialogue {

	@Override
	public void start() {
		player.lock();
		sendDialogue("You journey directly to the Kiln.");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		end();
	}

	@Override
	public void finish() {
		player.getControllerManager().startController("FightKilnControler", 0);
	}

}
