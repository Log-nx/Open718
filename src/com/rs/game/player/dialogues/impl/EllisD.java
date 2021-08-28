package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Utils;

public class EllisD extends Dialogue {
	
	private static final String[] SALUTATIONS = { "Hello there", "Howdy", "Greetings" };

	int npcId;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 0:
			stage = 1;
			sendOptionsDialogue("What would you like to say?", "Yes, please.",
					"No thanks.");
			break;
		case 1:
			if (componentId == OPTION_1) {
				sendPlayerDialogue(9827, "Yes, I would like to tan my hides.");
				stage = 2;
			} else if (componentId == OPTION_2) {
				sendPlayerDialogue(9827, "No thank you.");
				stage = 3;
			}
			break;
		case 2:
			player.getDialogueManager().startDialogue("TanningD", npcId);
			break;
		case 4:
			end();
			break;
		}
	}
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		stage = 0;
		sendNPCDialogue(npcId, 9827,
				SALUTATIONS[Utils.random(SALUTATIONS.length)]
						+ ", would you like to tan your hides?");
	}

}
