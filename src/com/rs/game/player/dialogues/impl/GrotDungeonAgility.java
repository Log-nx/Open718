package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.dialogues.Dialogue;

public class GrotDungeonAgility extends Dialogue {

	@Override
	public void start() {
		sendDialogue("This shorcut leads to the deepest level of the dungeon, the worms in that area are more dangerous.");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			sendOptionsDialogue("Still want to do this?", "Yes, let's do this!", "No, let's go back!");
			stage = 1;		
		} else if (stage == 1) {
			if (componentId == OPTION_1)
				player.setNextWorldTile(new WorldTile(1206, 6507, 0));
			end();
			if (componentId == OPTION_2)
				end();
		}
	}

	@Override
	public void finish() {

	}
}