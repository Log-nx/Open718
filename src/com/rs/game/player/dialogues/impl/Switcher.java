package com.rs.game.player.dialogues.impl;

import com.rs.game.player.content.PlayerLook;
import com.rs.game.player.dialogues.Dialogue;

public class Switcher extends Dialogue {

	public Switcher() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Make-Over Mage",
				"Do you want to change your Prayer List?",
				"Do you want to change your Spellbook?",
				"<col=FF0000>Prestige System</col>",
				"Character Customization");

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Pick a Prayer List?", 
						"Curses Prayer List",
						"Normal Prayer List");
				stage = 2;
			} else if (componentId == OPTION_2) {

				sendOptionsDialogue("Pick a Spellbook?",
						"Modern Spellbook",
						"Ancient Spellbook",
						"Lunar Spellbook");
				stage = 3;
			} else if (componentId == OPTION_3) {
				player.getDialogueManager().startDialogue("Prestige", 2676);
			
		} else if (componentId == OPTION_4) {
			player.getDialogueManager().startDialogue("MakeOverSettings", 2676);
		} else if (componentId == OPTION_5) {
			player.getInterfaceManager().closeScreenInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControllerManager().forceStop();
			player.getControllerManager().removeControllerWithoutCheck();
			end();
		}
		} else if (stage == 8) {
			if (componentId == OPTION_1) {
				player.getInterfaceManager().closeChatBoxInterface();
				PlayerLook.openMageMakeOver(player);
			} else if (componentId == OPTION_2) {
				PlayerLook.openHairdresserSalon(player);
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_3) {
				PlayerLook.openThessaliasMakeOver(player);
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_4) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				player.getPrayer().setPrayerBook(true);
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("You've just changed your prayer list.");
			} else if (componentId == OPTION_2) {
				player.getPrayer().setPrayerBook(false);
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("You've just changed your prayer list.");
			}

		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				player.getCombatDefinitions().setSpellBook(0);
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("You've just changed your spellbook.");
			} else if (componentId == OPTION_2) {
				player.getCombatDefinitions().setSpellBook(1);
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("You've just changed your spellbook.");
			} else if (componentId == OPTION_3) {
				player.getCombatDefinitions().setSpellBook(2);
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("You've just changed your spellbook.");
			}
		}
}
	@Override
	public void finish() {
	}

}
