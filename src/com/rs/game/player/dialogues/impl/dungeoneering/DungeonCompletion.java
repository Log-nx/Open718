package com.rs.game.player.dialogues.impl.dungeoneering;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.dungeoneering.Dungeon;
import com.rs.game.player.dialogues.Dialogue;

public class DungeonCompletion extends Dialogue {
	
	@Override
	public void start() {
		sendDialogue("Just to make sure: <col=ff0000>*</col> You won't receive any experience if you're leaving this dungeoneering floor early <col=ff0000>*</col> ..");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			sendOptionsDialogue("Still want to leave?", 
			"Yes, I'd like to leave!", 
			"No, thank you!");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 11) {
				if (player.getDungeon() != null)
					player.getDungeon().end(false);
			}
			end();
		}
	}

	@Override
	public void finish() {

	}
}