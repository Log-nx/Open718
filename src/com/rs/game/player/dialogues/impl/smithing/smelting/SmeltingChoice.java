package com.rs.game.player.dialogues.impl.smithing.smelting;

import com.rs.game.WorldObject;
import com.rs.game.player.dialogues.Dialogue;

public class SmeltingChoice extends Dialogue {
	
	private WorldObject object;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		sendOptionsDialogue("Your inventory contains new ores, which ores would you like to smelt?.", "Regular", "Reworked");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
			case OPTION_1:
				player.getDialogueManager().startDialogue("SmeltingD", object);
				break;				
			case OPTION_2:
				player.getDialogueManager().startDialogue("SmeltingNewD", object);
				break;
		}
	}

	@Override
	public void finish() {

	}
}
