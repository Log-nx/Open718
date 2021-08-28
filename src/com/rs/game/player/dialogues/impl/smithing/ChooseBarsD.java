package com.rs.game.player.dialogues.impl.smithing;

import com.rs.game.WorldObject;
import com.rs.game.player.actions.smithing.Smithing.ForgingBar;
import com.rs.game.player.actions.smithing.Smithing.ForgingInterface;
import com.rs.game.player.dialogues.Dialogue;

public class ChooseBarsD extends Dialogue {
	
	private WorldObject object;
	
	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		sendOptionsDialogue("Your inventory contains new bars and older bars, which bars would you like to smith?.", "Regular", "Reworked");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		ForgingBar bar = ForgingBar.getBar(player);
		switch (componentId) {
			case OPTION_1:
				ForgingInterface.sendSmithingInterface(player, bar, object);
				end();
				break;				
			case OPTION_2:
				player.getDialogueManager().startDialogue("NewSmithingD", object);
				break;
		}
	}

	@Override
	public void finish() {

	}
}