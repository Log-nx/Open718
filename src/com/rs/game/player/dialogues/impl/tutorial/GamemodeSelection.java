package com.rs.game.player.dialogues.impl.tutorial;

import com.rs.game.World;
import com.rs.game.player.dialogues.Dialogue;

public class GamemodeSelection extends Dialogue {

	@Override
	public void finish() { }

	@Override
	public void start() {
		stage = 1;
		sendNPCDialogue(946, NORMAL, "Nexus has three gamemodes for adventures to select from: Squire, Legend, and Veteran.");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			stage = 2;
			if (player.isAnIronMan()) {
				sendOptionsDialogue("Select a Gamemode",
						"Normal (x<col=ff0000>20</col> EXP & x<col=ff0000>1.5</col> drop rate)",
						"Legend (x<col=ff0000>10</col> EXP & x<col=ff0000>3</col> increased drop rate)",
						"Veteran (x<col=ff0000>5</col> EXP & x<col=ff0000>5</col> increased drop rate)");
			} else {
				sendOptionsDialogue("Choose a Gamemode",
						"Normal (x<col=ff0000>20</col> EXP & x<col=ff0000>1</col> drop rate)",
						"Legend (x<col=ff0000>10</col> EXP & <col=ff0000>2</col> increased drop rate)",
						"Veteran (x<col=ff0000>5</col> EXP & x<col=ff0000>3</col> increased drop rate)");
			}
			break;
		case 2:
			stage = 3;
			if (componentId == OPTION_1) {
				stage = 3;
				player.setSquire(true);
				player.setLegend(false);
				player.setVeteran(false);
				sendNPCDialogue(946, NORMAL, "So you'd like to play as a Normal?");
			} else if (componentId == OPTION_2) {
				stage = 3;
				player.setLegend(true);
				player.setSquire(false);
				player.setVeteran(false);
				sendNPCDialogue(946, NORMAL, "So you'd like to play as a Legend?");
			} else if (componentId == OPTION_3) {
				stage = 3;
				player.setVeteran(true);
				player.setLegend(false);
				player.setSquire(false);
				sendNPCDialogue(946, NORMAL, "So you'd like to play as a Veteran?");
			}
			break;
		case 3:
			stage = 4;
			sendOptionsDialogue("Confirm Gamemode?", "Yes", "No");
			break;
		case 4:
			if (componentId == OPTION_1) {
				if (player.isSquire()) {
					player.setXpRate(20);
					stage = 5;
					sendNPCDialogue(946, NORMAL, "I have set your gamemode as Normal.");
				}
				if (player.isLegend()) {
					player.setXpRate(10);
					stage = 5;
					sendNPCDialogue(946, NORMAL, "I have set your gamemode as Legend.");
				}
				if (player.isVeteran()) {
					player.setXpRate(5);
					stage = 5;
					sendNPCDialogue(946, NORMAL, "I have set your gamemode as Veteran.");
				}
			} else if (componentId == OPTION_2) {
				stage = 1;
				sendNPCDialogue(946, NORMAL, "It's okay, take your time to select your gamemode.");
			}
			break;
		case 5:
			stage = 6;
			player.getStatistics().tutorialStage = 26;
			player.choseGameMode = true;
			sendNPCDialogue(946, NORMAL, "You can now use the ::home command to leave tutorial island and start your adventure.");
			break;
		case 6:
			end();
			break;
		}
	}
}
