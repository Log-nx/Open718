package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.player.dialogues.Dialogue;

public class MagicInstructor extends Dialogue {
	
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
				sendNPCDialogue(946, HAPPY, "Good day, newcomer. Your journey is nearly at its end. My name is Terrova. Before you leave for the mainland, I'm going to tell you a little about Magic. Let's start by opening your spellbook.");
				break;
			case 2:
				stage = 6;
				sendNPCDialogue(946, NORMAL, "I see you're ready to start your journey eh? However, let's see your home location first.");
				break;
			case 3:
				stage = 9;
				sendNPCDialogue(946, NORMAL, "Are you ready to select your gamemode?");
				break;
			case 4:
				stage = 10;
				sendNPCDialogue(946, NORMAL, "You can now use the ::home command to leave tutorial island and start your adventure.");
				break;
			}
		}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			sendNPCDialogue(946, NORMAL, "Casting spells increases your Magic skill, and consumes runes which you carry in your backpack. You create runes with the Runecrafting skill. With a low Magic level, you can cast only the simplest spells, such as Air Strike. You must wield a magic weapon to cast combat spells.");
			break;
		case 2:
			stage = 3;
			sendNPCDialogue(946, NORMAL, "You can train some magic on these rats over here, but you'll need this.");
			break;
		case 3:
			stage = 4;
			sendItemDialogue(1381, 1, "The magic instructor gives you a staff of air");
			player.getInventory().addItem(1381, 1);
			break;
		case 4:
			stage = 5;
			player.getStatistics().tutorialStage = 24;
			sendNPCDialogue(946, NORMAL, "If you want to start your adventure in the realm of Nexus, come see me once you're done training.");
			break;
		case 5:
			end();
			break;
		case 6:
			stage = 7;
			sendNPCDialogue(946, NORMAL, "These are our current home selections that you may chose from.");
			break;
		case 7:
			stage = 8;
			sendOptionsDialogue("Home Locations Avaliable", "Taverly", "Lumbridge", "Varrock", "Lletya");
			break;
		case 8:
			stage = 9;
			if (componentId == OPTION_1) {
				player.getStatistics().tutorialStage = 25;
				player.setTaverlyHome(true);
				player.setLumbyHome(false);
				player.setVarrockHome(false);
				player.setLletyaHome(false);
				sendNPCDialogue(946, NORMAL, "Good choice, but before you leave let's talk about gamemodes.");
			}
			if (componentId == OPTION_2) {
				player.getStatistics().tutorialStage = 25;
				player.setLumbyHome(true);
				player.setTaverlyHome(false);
				player.setLletyaHome(false);
				player.setVarrockHome(false);
				sendNPCDialogue(946, NORMAL, "Good choice, but before you leave let's talk about gamemodes.");
			}
			if (componentId == OPTION_3) {
				player.getStatistics().tutorialStage = 25;
				player.setVarrockHome(true);
				player.setTaverlyHome(false);
				player.setLletyaHome(false);
				player.setLumbyHome(false);
				sendNPCDialogue(946, NORMAL, "Good choice, but before you leave let's talk about gamemodes.");
			}
			if (componentId == OPTION_4) {
				player.getStatistics().tutorialStage = 25;
				player.setLletyaHome(true);
				player.setVarrockHome(false);
				player.setTaverlyHome(false);
				player.setLumbyHome(false);
				sendNPCDialogue(946, NORMAL, "Good choice, but before you leave let's talk about gamemodes.");
			}
			break;
		case 9:
			player.getDialogueManager().startDialogue("GamemodeSelection");
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