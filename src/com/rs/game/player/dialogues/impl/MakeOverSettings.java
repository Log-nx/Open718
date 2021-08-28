 package com.rs.game.player.dialogues.impl;

import com.rs.game.player.content.PlayerDesign;
import com.rs.game.player.dialogues.Dialogue;

public class MakeOverSettings extends Dialogue {

	private int npcId;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 0:
			stage = 1;
			sendOptionsDialogue("Make-Over Mage", 
					"How does this work?",
					"Edit my Character, please.",
					"Close, I gotta go.");
			
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(npcId, 9827, "In short, this system will change your appearance, your clothes, gender, hair style and colors. So, try either than not to do it! You have nothing to lose?");
				stage = 4;
				break;
			case OPTION_2:
				if (player.getEquipment().wearingArmour()) {
					player.sm("Please remove all of your equipment(s) first.");
					end();
    		} else {
				PlayerDesign.open(player);
				end();
    		}
			case OPTION_3:
				end();
				break;
			}
			break;
		case 4:
			end();
			break;
		}

	}

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		stage = 0;
		sendNPCDialogue(
				npcId,
				9827,
				"Hello there! I am known as the Make-over Mage! I have",
				"spent many years researching magics that can change",
				"your physical appearence.");
	}
}