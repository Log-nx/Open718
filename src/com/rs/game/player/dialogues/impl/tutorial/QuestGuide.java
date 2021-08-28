package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.Dialogue;

public class QuestGuide extends Dialogue {
	
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
				sendNPCDialogue(949, HAPPY, "Ah. Welcome, adventurer. I'm here to tell you all about quests. Quests should be arriving in the realm of Nexus very soon!");
				break;
			case 2:
				stage = 3;
				DiccusTutorial.addExpertHintIcon(player);
				sendNPCDialogue(949, NORMAL, "I will be in contact with you whenever a new quest is avaliable for Nexus.");
				break;
			default:
				break;
			}
		}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			sendNPCDialogue(949, NORMAL, "Quests will vary greatly from collecting beads to hunting down dragons. You have to experience the thrill of questing yourself to fully understand. You may find some adventure in the cave under my house.");
			break;
		case 2:
			stage = 3;
			player.getStatistics().tutorialStage = 9;
			sendNPCDialogue(949, NORMAL, "You may use the ladder to enter the cave under my house.");
			break;
		case 3:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}