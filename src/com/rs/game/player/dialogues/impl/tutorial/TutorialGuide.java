package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.Dialogue;

public class TutorialGuide extends Dialogue {
	
	/*
	 * @author BigFuckinChungus
	 */

	@Override
	public void start() {
		int option = (int) parameters[0];
		player.getHintIconsManager().removeUnsavedHintIcon();
			switch (option) {
			case 1:
				stage = 1;
				sendNPCDialogue(945, HAPPY, "Greetings! I see you are a new arrival to the world of Nexus. My job is to welcome all new visitors to this and. So, welcome! You've already learned the first thing needed to succeed in this world: talking to other people!");
				break;
			case 2:
				stage = 4;
				sendOptionsDialogue("Skip Tutorial?", "Yes, I'm experienced.", "No thanks, I'm new.");
				break;
			}
		}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			sendNPCDialogue(945, HAPPY, "Getting around is easy. Just point and click. Left-click a destination to walk there. Left-click a door to open it. Right-click to see all options.");
			break;
		case 2:
			stage = 3;
			player.getStatistics().tutorialStage = 1;
			sendNPCDialogue(945, HAPPY, "You may skip this tutorial, however if you want to continue, open that door over there.");
			break;
		case 3:
			stage = 4;
			sendOptionsDialogue("Skip Tutorial?", "Yes, I'm experienced.", "No thanks, I'm new.");
			break;
		case 4:
			switch (componentId) {
			case OPTION_1:
				player.getStatistics().skippedTutorial(true);
				player.getDialogueManager().startDialogue("GamemodeSelection");
				break;
			case OPTION_2:
				stage = 5;
				sendNPCDialogue(945, HAPPY, "You've already learned the first thing needed to succeed in this world: talking to other people! Now go outside and talk to the Survival Expert.");
				break;
			}
			break;
		case 5:
			end();
			DiccusTutorial.addExpertHintIcon(player);
			break;
		}
	}

	@Override
	public void finish() {

	}
}