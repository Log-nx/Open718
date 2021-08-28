package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.player.dialogues.Dialogue;

public class CombatInstructor extends Dialogue {
	
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
				sendPlayerDialogue(NORMAL, "Hi! My name's " + player.getDisplayName());
				break;
			case 2:
				stage = 2;
				sendNPCDialogue(944, NORMAL, "You'll do barely any damage with your bare hands. Let's start by wielding that butter knife you're carrying. Left-click the bronze dagger in your backpack to wield it.");
				break;
			case 3:
				stage = 5;
				sendNPCDialogue(944, NORMAL, "Now you're ready for combat. Attack a rat! You'll continue to fight each other until it's dead or you do something else.");
				break;
			case 4:
				stage = 3;
				sendNPCDialogue(944, NORMAL, "Well done! You've defeated your first monster. You should eat some of your food to heal yourself. There's a lot more to combat, including the Ranged and Magic skills. When you get to the mainland, you should check out the combat academy in Lumbridge.");
				break;
			case 5:
				stage = 2;
				sendNPCDialogue(944, NORMAL, "For now, you're finished in this cave. Return to the surface and continue on your journey.");
				break;
			}
		}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			player.getStatistics().tutorialStage = 17;
			sendNPCDialogue(944, NORMAL, "To me, you're just another newcomer who thinks they're ready to fight. I am Vannaka, the greatest swordsman alive. You'll do barely any damage with your bare hands. Let's start by wielding that butter knife you're carrying. Left-click the bronze dagger in your backpack to wield it.");
			break;
		case 2:
			end();
			break;
		case 3:
			stage = 4;
			sendNPCDialogue(944, NORMAL, "The combat academy is north of Lumbridge lodestone, near the general store. There are lessons about ranged combat, casting magic spells, the combat triangle, managing adrenaline, and more!");
			break;
		case 4:
			stage = 2;
			player.getStatistics().tutorialStage = 20;
			sendNPCDialogue(944, NORMAL, "For now, you're finished in this cave. Return to the surface and continue on your journey.");
			break;
		case 5:
			stage = 2;
			sendNPCDialogue(944, NORMAL, "You can view a creatures drop table by doing ::npcdrops then examining the creature. You can also do ::testdrops 'kills' 'name' without the ' of course.");
			break;
		}
	}

	@Override
	public void finish() {

	}
}
