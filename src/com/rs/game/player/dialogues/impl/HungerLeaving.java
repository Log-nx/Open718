package com.rs.game.player.dialogues.impl;

import com.rs.game.minigames.hunger.HungerGames;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;

public class HungerLeaving extends Dialogue {

	@Override
	public void start() {
		sendDialogue("Think thoroughly about this choice, as this choice can't be undone.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			sendOptionsDialogue("Still want to leave?", 
			"Yes, I'd like to leave.", 
			"No, thank you.");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 11) {
				player.getEquipment().reset();
				player.setCanPvp(false);
				HungerGames.getGamePlayers().remove(player);
				player.getAppearence().generateAppearenceData();
				player.getEquipment().init();
				player.unlock();
				player.getControllerManager().forceStop();
				player.setNextWorldTile(HungerGames.LOBBY);
				Player.removeControler(player);
			}
			end();
		}
	}

	@Override
	public void finish() {

	}
}