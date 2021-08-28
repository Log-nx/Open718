package com.rs.game.player.dialogues.impl.instances;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class Corp extends Dialogue {
	

	public void setPerma(boolean bool) {
		player.setPermaCorp(bool);
	}
	private String getString = "CorpInstance";
	public void startController() {
		player.getControllerManager().startController("CorpInstance");	
	}
	public Corp() {
	}
	
	@Override
	public void start() {
		stage = 100;
		player.instanceDialogue = "Corp";
		player.setOutside(new WorldTile(2970, 4384, 2));
		stage = 1;
		sendOptionsDialogue("What would you like to do?",
				"Enter boss room.", "Start a private instance.", "Join an existing instance.");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		boolean getPerma = player.getPermaCorp();
		if (stage == 1) {	
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2872, 5361, 0));
			} else if (componentId == OPTION_2) {
				if (getPerma == true) {
					sendDialogue("A new session will start once you",
							"continue for 1 hour. Do you wish to start?",
							"Note: You only lose the instance if the exit portal is clicked.",
							"If you die, you lose 10 minutes and spawn back into the zone.");
				stage = 2;					
				} else {
				sendDialogue("Starting a new session against this foe cost",
						"200,000 coins for 1 hour. Do you wish to pay?",
						"Note: You only lose the instance if the exit portal is clicked.",
						"If you die, you lose 10 minutes and spawn back into the zone.");
				stage = 2;
				}
			} else if (componentId == OPTION_3) {	
				player.getTemporaryAttributtes().put("instance_name", Boolean.TRUE);
				player.getPackets().sendRunScript(109,
						new Object[] { "Please enter the leader's name:" });									
			}

	} else if (stage == 2) {	
		sendOptionsDialogue("Choose a spawn rate", "Fast", "Medium", "Slow");
		stage = 9;					
		} else if (stage == 3) {
			sendOptionsDialogue("Pay 200k for 1 hour?", "Pay [200k]", "Don't Pay.", "Permanently unlock [10M]");
			stage = 7;
		} else if (stage == 9) {
			if (componentId == OPTION_1) {
				player.setSpawnRate(5);
				sendDialogue("Spawn Rate set to fast.");
				if (getPerma == true) {
					stage = 10;
				} else {
					stage = 3;
				}
			} else if (componentId == OPTION_2) {
				player.setSpawnRate(35);
				sendDialogue("Spawn Rate set to medium.");
				if (getPerma == true) {
					stage = 10;
				} else {
					stage = 3;
				}
			} else if (componentId == OPTION_3) {
				player.setSpawnRate(48);
				sendDialogue("Spawn Rate set to slow.");
				if (getPerma == true) {
					stage = 10;
				} else {
					stage = 3;
				}
			}		
		} else if (stage == 10) {
			sendOptionsDialogue("Choose a level of protection", "Only I can enter.", "Anyone can enter.", "PIN protected.");
			stage = 8;
		} else if (stage == 8) {
			if (componentId == OPTION_1) {
					player.setInstancePin(1);
					player.sm("Enjoy your instance, the NPCs will stop spawning in an hour.");
					startController();
					end();
			} else if (componentId == OPTION_2) {
					player.setInstancePin(2);
					player.sm("Enjoy your instance, the NPCs will stop spawning in an hour.");
					startController();
					end();
			} else if (componentId == OPTION_3) {
				player.setInstanceControler(getString);
				player.getTemporaryAttributtes().put("startfreeinstancepin", Boolean.TRUE);
				player.getPackets().sendRunScript(108,
						new Object[] { "Please enter your new pin:" });							
			}			
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				if (player.getMoneyPouch().getCoinsAmount() >= 200000) {
					player.setInstancePin(1);
					player.getMoneyPouch().sendDynamicInteraction(200000, true);
					player.sm("Enjoy your instance, the NPCs will stop spawning in an hour.");
					startController();
					end();
				} else {
					player.sm("Sorry, but you don't have enough money.");
					end();
				}				
			} else if (componentId == OPTION_2) {
				if (player.getMoneyPouch().getCoinsAmount() >= 200000) {
					player.setInstancePin(2);
					player.getMoneyPouch().sendDynamicInteraction(200000, true);
					player.sm("200,000 coins were deducted from your money pouch.");
					player.sm("Enjoy your instance, the NPCs will stop spawning in an hour.");
					startController();
					end();
				} else {
					player.sm("Sorry, but you don't have enough money.");
					end();
				}				
			} else if (componentId == OPTION_3) {
				player.setInstanceControler(getString);
				player.getTemporaryAttributtes().put("startinstancepin", Boolean.TRUE);
				player.getPackets().sendRunScript(108,
						new Object[] { "Please enter your new pin:" });							
			}
		} else if (stage == 5) {
			sendOptionsDialogue("Unlock forever?", "Yes, Pay [10M]", "No, Don't Pay.");
			stage = 6;
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				if (player.getMoneyPouch().getCoinsAmount() >= 10000000) {
					setPerma(true);
					player.getMoneyPouch().sendDynamicInteraction(10000000, true);
					player.sm("You've permenantly unlocked the instance.");
					end();
				} else {
					player.sm("Sorry, but you don't have enough money.");
					end();
				}						
			} else if (componentId == OPTION_2) {
				end();
			}
		} else if (stage == 7) {	
			if (componentId == OPTION_1) {
				if (player.getMoneyPouch().getCoinsAmount() >= 10000000) {
					sendOptionsDialogue("Choose a level of protection", "Only I can enter.", "Anyone can enter.", "PIN protected.");
					stage = 4;						
				} else {
					player.sm("You can't afford this.");
					end();
				}
			} else if (componentId == OPTION_2) {
				end();	
			} else if (componentId == OPTION_3) {
				sendDialogue("Unlocking this instance forever will allow you to", "access the private instance for free", "forever. This costs 10M. Do you wish to proceed?");
				stage = 5;
			}
		}
	}
	
	@Override
	public void finish() {
	}

}