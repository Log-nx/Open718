package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;

public class TrioPortal extends Dialogue {

	public TrioPortal() {
	}

	@Override
	public void start() {
			stage = 1;
			sendOptionsDialogue("What would you like to do?", "Change entry pin",
					 			"Kick player" , "Bank Booth" , "End Instance");

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				player.getTemporaryAttributtes().put("createinstancepin", Boolean.TRUE);
				player.getPackets().sendRunScript(108,
						new Object[] { "Please enter your new pin:" });		
			} else if (componentId == OPTION_2) {
				player.getTemporaryAttributtes().put("kickinstanceplayer", Boolean.TRUE);
				player.getPackets().sendRunScript(109,
						new Object[] { "Please input the player's name:" });	
			} else if (componentId == OPTION_3) {
				if (player.getInstanceBooth() <= 0 && player.isPermaBank() == false) {
					stage = 4;
					sendDialogue("Buying a bank booth costs 500k, and it will only last for this instance.");
				} else {
					player.stopAll();
					player.getBank().openBank();
				}
			} else if (componentId == OPTION_4) {
				sendDialogue("Are you sure you want to end your instance?",
							 "Note: You will be sent home",
							 "You will lose your current instance!",
							 "If you want to return, you'd need to buy a new instance.");
				stage = 2;
		}

		} else if (stage == 2) {
			sendOptionsDialogue("Please select an option.", "Yes.", "No.");
			stage = 3;
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				player.setInstanceEnd(true);
				player.setInstanceBooth(0);
				player.sm("Your instance has ended. You will now be sent to the original room.");
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		} else if (stage == 4) {
			sendOptionsDialogue("Select an option", "Pay [500k]", "Permanent Buy [30m]", "Nevermind");
			stage = 5;
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				if (player.getMoneyPouch().getCoinsAmount() >= 500000) {
					player.setInstanceBooth(player.getTimer());
					player.getMoneyPouch().sendDynamicInteraction(500000, true);
					player.sm("You've unlocked the bank. Access it by the portal.");
					end();
				} else {
					player.sm("You can't afford this.");
					end();
				}
			} else if (componentId == OPTION_2) {
				if (player.getMoneyPouch().getCoinsAmount() >= 20000000) {
					player.setPermaBank(true);
					player.getMoneyPouch().sendDynamicInteraction(20000000, true);
					player.sm("You've unlocked the bank. Access it by the portal. Note: This is for all bosses.");
				} else {
					player.sm("You can't afford this.");
				}
					end();			
			} else if (componentId == OPTION_3) {
				end();
			}
		}
	}
	@Override
	public void finish() {
	}

}
