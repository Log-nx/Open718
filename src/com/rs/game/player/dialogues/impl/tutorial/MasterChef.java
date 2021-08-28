package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.dialogues.Dialogue;

public class MasterChef extends Dialogue {
	
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
				sendNPCDialogue(942, HAPPY, "Welcome, newcomer. I am the master chef, Lev. I will teach you how to cook food truly fit for a king.");
				break;
			case 2:
				stage = 5;
				sendNPCDialogue(942, NORMAL, "Fill the bucket with water from the sink, then use it to wet the flour into dough. Then bake the dough into bread on my range.");
				break;
			case 3:
				stage = 6;
				sendNPCDialogue(942, HAPPY, "Ah, there's nothing like the smell of freshly baked break. Be sure to carry a little food with you on your adventures. If you're injured in combat, eating is the best way to feel better. There are all sorts of food you can cook with the right ingredients and a high enough Cooking skill. Pies, cake, stews.");
				break;
			case 4:
				stage = 5;
				sendNPCDialogue(942, NORMAL, "Follow the path to the home of the quest guide.");
				break;
			}
		}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			sendPlayerDialogue(NORMAL, "I already know how to cook. Brynna just taught me.");
			break;
		case 2:
			stage = 3;
			sendNPCDialogue(942, LAUGHING, "Ha! You call THAT cooking? You're much more likely to burn your food if you cook on a log fire out in the open. You should cook at a range whenever you can. Now, I am going to teach you the fine art of baking bread.");
			break;
		case 3:
			stage = 4;
			sendItemDialogue(ItemIdentifiers.POT_OF_FLOUR, "The master chef gives you an empty jug and a pot of flour!");
			player.getInventory().addItem(ItemIdentifiers.POT_OF_FLOUR, 1);
			player.getInventory().addItem(1937, 1);
			break;
		case 4:
			stage = 5;
			player.getStatistics().tutorialStage = 7;
			sendNPCDialogue(942, NORMAL, "Fill the jug with water from the sink, then use it to wet the flour into dough. Then bake the dough into bread on my range.");
			break;
		case 5:
			end();
			break;
		case 6:
			stage = 5;
			sendNPCDialogue(942, NORMAL, "You can even churn cream and butter or brew your own mead! You'll be a master chef before you know it. Now off to the next area with you!");
			break;
		}
	}

	@Override
	public void finish() {

	}
}