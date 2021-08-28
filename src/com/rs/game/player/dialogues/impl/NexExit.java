package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.minigames.ZarosGodwars;
import com.rs.game.player.dialogues.Dialogue;

public final class NexExit extends Dialogue {

	@Override
	public void start() {
		sendDialogue(
				"The room beyond this point is a safe place.",
				"You're not going to chicken out are you?");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
    	int players = ZarosGodwars.getPlayers().size();
		if (stage == -1) {
			stage = 0;
		    sendOptionsDialogue(
				    "There "+(players == 1 ? "is" : "are")+" currently " + players
					    + " "+(players == 1 ? "player" : "players")+" fighting",
				    "Climb up.", "Stay here.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				player.setNextWorldTile(new WorldTile(2908, 5203, 0));
				ZarosGodwars.removePlayer(player);
				player.getControllerManager().startController("Godwars");
			}
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
