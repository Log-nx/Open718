package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.Dialogue;

public class FinancialAdvisor extends Dialogue {
	
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
				sendNPCDialogue(947, NORMAL, "Ah, hello there " + player.getDisplayName() + ". I'm Diccus's Financial Advisor and I'll teach you about currencies in Diccus.");
				break;
			case 2:
				stage = 12;
				sendNPCDialogue(947, NORMAL, "Go ahead and take this chance to take a look at your bank.");
				break;
			case 3:
				stage = 12;
				DiccusTutorial.addPrayerHintIcon(player);
				sendNPCDialogue(947, NORMAL, "I've already taught you the basic's of Diccus's currencies. The Prayer instructor would like to have a chat with you in the nearby church.");
				break;
			}
		}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			sendNPCDialogue(947, NORMAL, "Money is useful because you can buy items from the Grand Exchange if you don't want to craft them yourself.");
			break;
		case 2:
			stage = 3;
			sendNPCDialogue(947, NORMAL, "Gold coins are the main currency of Diccus. Your coins are kept in your money pouch at the bottom of your backpack. You currently have " + player.getMoneyPouch().getCoinsAmount() + " coins.");
			break;
		case 3:
			stage = 4;
			sendNPCDialogue(947, NORMAL, "Loyalty points are gained every hour and are useful for purchasing items such as, the luck of the dwarves ring.");
			break;
		case 4:
			stage = 5;
			sendNPCDialogue(947, NORMAL, "Loyalty points can also be used to purchase an aura. Aura's can be found in your worn equipment next to the toolbelt.");
			break;
		case 5:
			stage = 6;
			sendNPCDialogue(947, NORMAL, "Vote points can earned by voting for Diccus. Vote points can be exchanged for perk books, or more items. The complete list of perks can be found on the forums.");
			break;
		case 6:
			stage = 7;
			sendNPCDialogue(947, NORMAL, "There are three basic ways to make money: skilling, combat and trading. Some people have work for an adventurer like you, and will reward you for completing quests. Many enemies will drop items when they die.");
			break;
		case 7:
			stage = 8;
			sendNPCDialogue(947, NORMAL, "Let's talk about skilling for a moment. Skilling is much safer than combating enemies for loot, as such the items aren't worth as much. However, skilling supplies are almost always wanted by someone.");
			break;
		case 8:
			stage = 9;
			sendNPCDialogue(947, NORMAL, "Now, let's talk about combat for a moment. Slayer creatures usually drop rare items that are worth a lot of money, however certain creatures like King Black Dragon, Corporeal Beast, etc usually drop rare items.");
			break;
		case 9:
			stage = 10;
			sendNPCDialogue(947, NORMAL, "You can sell them to a general store or on the Grand Exchange in Varrock. By getting a high level in skills, such as Cooking, Mining, Smithing or Fishing, you can create or gather your own items and sell them for pure profit.");
			break;
		case 10:
			stage = 11;
			sendNPCDialogue(947, NORMAL, "You can only carry so many items in your backpack. You can deposit items you want to keep and not sell into your bank.");
			break;
		case 11:
			stage = 12;
			player.getStatistics().tutorialStage = 21;
			sendNPCDialogue(947, NORMAL, "Go ahead and take this chance to take a look at your bank.");
			break;
		case 12:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}