package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;

public class MrEx extends Dialogue {

	private int npcId;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			sendOptionsDialogue("Select an Option", 
					"What kind of Helmet is that?",
					"Which item can I make?",
					"Can you show your Teleports?");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				player.getDialogueManager().startDialogue("WildstalkerD", 3709);
			}
			if (componentId == OPTION_2) {
				player.getDialogueManager().startDialogue("DragonBoneSetting", 3709);
			}
			if (componentId == OPTION_3) {
				player.getDialogueManager().startDialogue("PkTeleports", 3709);
							}
		} else if (stage == 3) {
			end();
		}
	}

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(
				npcId,
				9827,
				"Hello "
						+ player.getDisplayName()
						+ ", how can I help you?");
	}
}