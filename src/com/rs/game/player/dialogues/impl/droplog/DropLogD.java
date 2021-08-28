package com.rs.game.player.dialogues.impl.droplog;

import com.rs.game.player.content.input.impl.InputIntegerEvent;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class DropLogD extends Dialogue {

	int npcId = 13955;

	@Override
	public void start() {
		if (player.getDropLogs().FIRST_TIME_USE) {
			sendNPCDialogue(npcId, NORMAL, "Hello " + player.getDisplayName() + "! This is the Diccus drop log system. Each drop you receive will be logged on this system. You'll also be able to set a minimum value on the items you'd like to be logged.");
			stage = -1;
		} else {
			sendOptionsDialogue("Minimum value: " + Colors.RED + Utils.formatNumber(player.getDropLogs().getMinmumValue()) + "</col> - Publicity: " + Colors.RED + (!player.getDropLogs().LOCKED_DROP_LOGS ? "Public" : "Private") + "</col>.", "Change minimum value", player.getDropLogs().LOCKED_DROP_LOGS ? "Switch to public" : "Switch to private");
			stage = 3;
		}
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case -1:
			sendNPCDialogue(npcId, NORMAL, "Other players will be able to view your drop logs by default. You'll be able to toggle whether or not you'd like your drop log to be visible to other players.");
			stage = 4;
			break;
		case 0:
			end();
			break;
		case 1:
			player.setInputEvent(new InputIntegerEvent("What is the minimum value of items you would like to be logged?") {
				@Override
				public void onEvent(Integer value) {
					if (value == 0) {
						value = 1;
					}
					player.getDropLogs().setMinmumValue(value);
					sendOptionsDialogue("Drop Log Privacy", "Yes, anyone can view my drop logs", "No, I'd like it set to private");
					stage = 2;
				}
			});
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				player.getDropLogs().FIRST_TIME_USE = false;
				player.getDropLogs().LOCKED_DROP_LOGS = false;
				sendNPCDialogue(npcId, NORMAL, ".:: Drop logs settings set to ::. <br> Minimum value of " + Colors.RED + Utils.formatNumber(player.getDropLogs().getMinmumValue()) + "</col>. <br> Publicity:" + Colors.RED + (!player.getDropLogs().LOCKED_DROP_LOGS ? "Public" : "Private") + "</col>. <br>" + "Feel free to edit them at any time you wish..");
				stage = 0;
				break;
			case OPTION_2:
				player.getDropLogs().FIRST_TIME_USE = false;
				player.getDropLogs().LOCKED_DROP_LOGS = true;
				sendNPCDialogue(npcId, NORMAL, ".:: Drop logs settings set to ::. <br> Minimum value of " + Colors.RED + Utils.formatNumber(player.getDropLogs().getMinmumValue()) + "</col>. <br> Publicity:" + Colors.RED + (!player.getDropLogs().LOCKED_DROP_LOGS ? "Public" : "Private") + "</col>. <br>" + "Feel free to edit them at any time you wish..");
				stage = 0;
				break;
			}
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				player.setInputEvent(new InputIntegerEvent("Enter new minimum value..") {
					@Override
					public void onEvent(Integer value) {
						if (value == 0) {
							value = 1;
						}
						player.getDropLogs().setMinmumValue(value);
						sendNPCDialogue(npcId, NORMAL, "Certainly! Only items with a minimum value of " + Colors.RED + Utils.formatNumber(player.getDropLogs().getMinmumValue()) + "</col> will be logged in your drop logs now.");
						stage = 0;
					}
				});
				break;
			case OPTION_2:
				if (player.getDropLogs().LOCKED_DROP_LOGS) {
					player.getDropLogs().LOCKED_DROP_LOGS = false;
					sendNPCDialogue(npcId, NORMAL, "Certainly! No one will be able to view your drops log now.");
				} else {
					player.getDropLogs().LOCKED_DROP_LOGS = true;
					sendNPCDialogue(npcId, NORMAL, "Certainly! Everyone will be able to view your drops log now.");
				}
				stage = 0;
				break;
			}
			break;
		}

	}

	@Override
	public void finish() {
	}

}
