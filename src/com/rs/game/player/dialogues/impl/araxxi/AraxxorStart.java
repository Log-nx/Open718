package com.rs.game.player.dialogues.impl.araxxi;

import com.rs.game.player.dialogues.Dialogue;

public class AraxxorStart extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Would you like to start the Araxxi fight?", "Yes", "No");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				player.getControllerManager().startController("AraxxiController", true, player);
				end();
			} else if (componentId == OPTION_2) {
				end();
			}
		}
	}
	

	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}

}
