package com.rs.game.player.dialogues.impl;

import com.rs.game.player.content.custom.CompletionistCape;
import com.rs.game.player.dialogues.Dialogue;

public class CapeStand extends Dialogue{

	public CapeStand() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("What would you like to do?", "Am I worthy to get the cape?", "Open the Requirements List.", "Nevermind.");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
	 if (stage == 1) {
		if (componentId == OPTION_1) {
			player.getPackets().sendGameMessage("You wonder, if you are worthy enough to get the Completionist Cape.");
			CompletionistCape.CheckCompletionist(player);
			player.lock(2);
			end();
		} else if (componentId == OPTION_2) {
			player.getCompCapeManager().sendInterface();
			end();
		} else if(componentId == OPTION_3) {
			end();
		}
	 }
	 }
		
	
	@Override
	public void finish() {
		
	}
	
}
