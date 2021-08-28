package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.dialogues.Dialogue;

public class MiningInstructor extends Dialogue {
	
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
				sendNPCDialogue(948, HAPPY, "Greetings. My name is Dezzick and I'm a miner by trade. On either side of me are rocks containing tin and copper ore. Mine one of each ore. We're going to smelt and smith a melee weapon for you.");
				break;
			case 2:
				stage = 2;
				sendNPCDialogue(948, NORMAL, "Mine some copper and tin ore. We're going to smelt and smith a melee weapon for you.");
				break;
			case 3:
				stage = 3;
				Dialogue.closeNoContinueDialogue(player);
				sendItemDialogue(438, 1, "You show the mining instructor the tin ore and copper ore you mined.");	
				break;
			case 4:
				stage = 2;
				sendNPCDialogue(948, NORMAL, "Simply take the ores required to make a metal to a furnace, then use the ores on the furnace to smelt them into a bar of metal");
				break;
			case 5:
				stage = 13;
				Dialogue.closeNoContinueDialogue(player);
				sendItemDialogue(ItemIdentifiers.BRONZE_BAR, 1, "You show the mining instructor the bronze bar you smelted.");
				break;
			case 6:
				stage = 2;
				sendNPCDialogue(948, NORMAL, "Try making a bronze dagger out of bronze bars.");
				break;
			case 7:
				stage = 2;
				sendNPCDialogue(948, NORMAL, "I've already taught you about mining and smithing, now you're ready to learn about combat. Head to the next area!");
				break;
			}
		}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			player.getStatistics().tutorialStage = 11;
			sendItemDialogue(ItemIdentifiers.BRONZE_PICKAXE, 1, "The mining instructor gives you a bronze pickaxe, and a hammer.");
			player.getInventory().addItem(ItemIdentifiers.BRONZE_PICKAXE, 1);
			player.getInventory().addItem(ItemIdentifiers.HAMMER, 1);
			break;
		case 2:
			end();
			break;
		case 3:
			stage = 4;
			sendNPCDialogue(948, NORMAL, "Great! A bronze pickaxe is the most inefficient pickaxe available, but is perfect for a beginner. The better the pickaxe you use, the faster you will get ore from the rock you're mining. Also, the better the pickaxe, the higher the mining level you'll require to wield it.");
			break;
		case 4:
			stage = 5;
			sendPlayerDialogue(CONFUSED, "So why do I want to mine ore?");
			break;
		case 5:
			stage = 6;
			sendNPCDialogue(948, NORMAL, "You can use your Smithing skill to smelt ore into metal bars at a furnace. Then at an anvil, you smith the bars into melee weapons and armour worn by warriors.");
			break;
		case 6:
			stage = 7;
			sendNPCDialogue(948, NORMAL, "Furnaces are expensive to build and maintain, so there are not that many scattered around the world. I suggest when you find a furnace, you remember its location for future use.");
			break;
		case 7:
			stage = 2;
			player.getStatistics().tutorialStage = 13;
			sendNPCDialogue(948, NORMAL, "You can smelt tin and copper together to make bronze equipment. Simply take the ores required to make a metal to a furnace, then use the ores on the furnace to smelt them into a bar of metal.");
			break;
		case 8:
			stage = 9;
			sendNPCDialogue(948, NORMAL, "When you use a metal bar on an anvil, you'll choose the item you want to smith, as long as you have a high enough Smithing level and the correct number of bars.");
			break;
		case 9:
			stage = 10;
			sendNPCDialogue(948, NORMAL, "Heat level affects how fast an item can be smithed into the respective item. Heat level can be raised by putting the bars into the forge to heat up the item.");
			break;
		case 10:
			stage = 2;
			player.getStatistics().tutorialStage = 15;
			sendNPCDialogue(948, NORMAL, "The higher your Smithing level, the better quality of metal you can work and the more heat your items will have. You start off on bronze and work your way up as your Smithing level increases. Start by smithing a bronze dagger at an anvil.");
			break;
		case 11:
			stage = 12;
			sendNPCDialogue(948, NORMAL, "However while mining for ores or gems, a 'rocktunity' may occur at random nearby. This simply means you have a chance to mine double ores from that ore.");
			break;
		case 12:
			stage = 2;
			sendNPCDialogue(948, NORMAL, "Rocktunity rocks can be seen by the glow around the ore.");
			break;
		case 13:
			stage = 8;
			sendPlayerDialogue(NORMAL, "So how do I make a weapon out of a bronze bar?");
			break;
		}
	}

	@Override
	public void finish() {

	}
}