package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.player.dialogues.Dialogue;

public class PrayerInstructor extends Dialogue {
	
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
				sendPlayerDialogue(NORMAL, "Good day, brother. My name's " + player.getDisplayName());
				break;
			case 2:
				stage = 10;
				sendNPCDialogue(25280, NORMAL, "Peace be with you, my child.");
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
			sendNPCDialogue(25280, NORMAL, "I'm Brother Brace. I'm here to tell you all about Prayer.");
			break;
		case 2:
			stage = 3;
			sendNPCDialogue(25280, NORMAL, "Prayer can be trained by burying or scattering the bones/ashes that a creature drops on it's death. However, you can also use those bones on a guilded altar for more experience.");
			break;
		case 3:
			stage = 4;
			sendNPCDialogue(25280, NORMAL, "Bones are usually found on most creatures. Ashes however are usually found on creatures such as demons such as Lesser Demons.");
			break;
		case 4:
			stage = 5;
			sendNPCDialogue(25280, NORMAL, "Praying at an altar can restore your prayer points. So if you ever need to restore your points, feel free to drop by.");
			break;
		case 5:
			stage = 6;
			sendNPCDialogue(25280, NORMAL, "I'm also the community officer 'round here, so it's my job to tell you about the rules.");
			break;
		case 6:
			stage = 7;
			sendPlayerDialogue(NORMAL, "Are there rules for how I should behave?");
			break;
		case 7:
			stage = 8;
			sendNPCDialogue(25280, NORMAL, "Yes. In general, always try to be courteous to others - remember that adventurers like yourself are real people with real feelings. If you go around being abusive or causing trouble, you could end up in trouble.");
			break;
		case 8:
			stage = 9;
			sendPlayerDialogue(NORMAL, "I'll keep that in mind.");
			break;
		case 9:
			stage = 10;
			player.getStatistics().tutorialStage = 23;
			sendNPCDialogue(25280, NORMAL, "Peace be with you, my child.");
			break;
		case 10:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}
