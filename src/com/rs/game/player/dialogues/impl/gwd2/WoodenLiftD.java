package com.rs.game.player.dialogues.impl.gwd2;

import com.rs.game.WorldTile;
import com.rs.game.player.dialogues.Dialogue;

public class WoodenLiftD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Ascend to the desert?", "Yes, I'd like to leave.",
				"No, not right now.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			switch (componentId) {
			case OPTION_1:
				player.setNextWorldTile(new WorldTile(3382, 2877, 0));
				end();
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {
	}

}
