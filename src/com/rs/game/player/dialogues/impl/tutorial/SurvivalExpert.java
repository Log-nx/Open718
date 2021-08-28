package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.dialogues.Dialogue;

public class SurvivalExpert extends Dialogue {
	
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
				sendNPCDialogue(943, HAPPY, "Hello there, newcomer. My name is Brynna. I'm going to teach you about skills you'll need to survive. You have many skills you can train. The more you practice, the better you get.");
				break;
			case 2:
				stage = 4;
				sendNPCDialogue(943, HAPPY, "You need to chop a tree until you get somes logs.");
				break;
			case 3:
				stage = 5;
				sendNPCDialogue(943, HAPPY, "Well done! You can cook food on a fire. If you're ever injured, eating food will restore your health. We'll need something to cook. There are shrimp in the pond, so let's catch and cook some. Wherever you see bubbles in the water, there's probably some good fishing to be had there!");
				break;
			case 4:
				stage = 6;
				sendNPCDialogue(943, NORMAL, "There are shrimp in the pond, so let's catch and cook some. Wherever you see bubbles in the water, there's probably some good fishing to be had there!");
				break;
			case 5:
				stage = 4;
				sendNPCDialogue(943, NORMAL, "Now you have caught some shrimp, let's cook it! You'll cook your shrimp on a fire. If your fire's gone out, chop a tree to get some logs and make a new fire. Then use the shrimp on the fire.");
				break;
			case 6:
				stage = 4;
				sendNPCDialogue(943, HAPPY, "I've taught you all I can about Woodcutting, Firemaking, and Fishing. Open the gate, follow the path to the next area and talk to the master chef! He'll teach you more about Cooking!");
				break;
			case 7:
				stage = 5;
				sendNPCDialogue(943, NORMAL, "Very good! Now let's put those logs to use. Light the logs in your backpack to make a fire.");
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
			sendNPCDialogue(943, HAPPY, "I'll tell you about the Woodcutting, Fishing, Firemaking and Cooking skills. Let's start with Woodcutting. Chop a tree until you get some logs in your backpack.");
			break;
		case 2:
			if (player.getInventory().containsOneItem(ItemIdentifiers.BRONZE_HATCHET)) {
				end();
			}
			if (!player.getInventory().containsOneItem(ItemIdentifiers.BRONZE_HATCHET)) {
			stage = 3;
			sendItemDialogue(ItemIdentifiers.BRONZE_HATCHET, "Brynna gives you a bronze hatchet.");
			player.getInventory().addItem(ItemIdentifiers.BRONZE_HATCHET, 1);
			}
			break;
		case 3:
			stage = 4;
			player.getStatistics().tutorialStage = 2;
			sendNPCDialogue(943, HAPPY, "Chop a tree until you get some logs in your backpack.");
			break;
		case 4:
			end();
			break;
		case 5:
			if (player.getInventory().containsOneItem(ItemIdentifiers.TINDERBOX)) {
				end();
			}
			if (!player.getInventory().containsOneItem(ItemIdentifiers.TINDERBOX)) {
			stage = 4;
			sendItemDialogue(ItemIdentifiers.TINDERBOX, "Brynna gives you a tinderbox.");
			player.getInventory().addItem(ItemIdentifiers.TINDERBOX, 1);
			}
			break;
		case 6:
			if (player.getInventory().containsOneItem(ItemIdentifiers.SMALL_FISHING_NET)) {
				end();
			}
			if (!player.getInventory().containsOneItem(ItemIdentifiers.SMALL_FISHING_NET)) {
			stage = 4;
			sendItemDialogue(ItemIdentifiers.SMALL_FISHING_NET, "Brynna gives you a small fishing net.");
			player.getInventory().addItem(ItemIdentifiers.SMALL_FISHING_NET, 1);
			}
			break;
		}
	}

	@Override
	public void finish() {

	}
}