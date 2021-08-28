package com.rs.game.player.dialogues.impl.discord;

import com.rs.game.player.dialogues.Dialogue;

public class Discord extends Dialogue {
	
	/*
	 * @author BigFuckinChungus
	 */

	@Override
	public void start() {
		sendOptionsDialogue("Link Your Account To: " + player.getDiscordName() +"?", "Yes", "No");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (componentId) {
		case OPTION_1:
			player.setRealDiscordName(player.getDiscordName());
			end();
			break;
		case OPTION_2:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}
