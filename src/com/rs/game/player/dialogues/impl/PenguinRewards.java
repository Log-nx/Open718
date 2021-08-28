package com.rs.game.player.dialogues.impl;

import com.rs.game.player.content.PenguinEvent;
import com.rs.game.player.dialogues.Dialogue;

public class PenguinRewards extends Dialogue {

	public PenguinRewards() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Select an Option", "I'd like to check my current points", "What's the current hint?", "I'd like to see the rewards", "Nevermind");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
	 if(stage == 1) {
		if(componentId == OPTION_1) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.sm("You currently have "+player.penguinpoints+" penguins points.");
		} else if(componentId == OPTION_2) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.sm("<col=70c2bc>The current hint: "+PenguinEvent.current+"");
		} else if(componentId == OPTION_3) {
			stage = 2;
			sendOptionsDialogue("Select an Option", "Cosmetics & Outfits", "Nevermind");
		} else if(componentId == OPTION_4) {
			player.getInterfaceManager().closeChatBoxInterface();
		}
	 } else if(stage == 2) {
			 if(componentId == OPTION_1) {
				sendOptionsDialogue("Select an Option", "Botanist Mask (35 points)", "Scarecrow Mask (45 points)", "Scabara Mask (50 points)", "Scabara Mask (50 points)", "More");
				stage = 3;
				} else if(componentId == OPTION_2) { 
				player.getInterfaceManager().closeChatBoxInterface();
				}
	} else if(stage == 3) {
			if(componentId == OPTION_1) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 35) {
					player.penguinpoints -= 35;
					player.getInventory().addItem(25190, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 45) {
					player.penguinpoints -= 45;
					player.getInventory().addItem(25322, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_3) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 50) {
					player.penguinpoints -= 50;
					player.getInventory().addItem(25124, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_4) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 20) {
					player.penguinpoints -= 20;
					player.getInventory().addItem(7003, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_5) {
				stage = 4;
				sendOptionsDialogue("Select an Option", "Apmeken Mask (45 points)", "Factory Mask (40 points)", "Gorilla Mask (50 points)", "Sheep Mask (15 points)", "More");
			}
	 } else if(stage == 4) {
			if(componentId == OPTION_1) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 45) {
					player.penguinpoints -= 45;
					player.getInventory().addItem(25122, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 40) {
					player.penguinpoints -= 40;
					player.getInventory().addItem(22959, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_3) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 50) {
					player.penguinpoints -= 50;
					player.getInventory().addItem(22314, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_4) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 15) {
					player.penguinpoints -= 15;
					player.getInventory().addItem(13107, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_5) {
				stage = 5;
				sendOptionsDialogue("Select an Option", "Bat Mask (17 points)", "Penguin Mask (20 points)", "Cat Mask (23 points)", "Wolf Mask (25 points)", "More");
			}
	 } else if(stage == 5) {
			if(componentId == OPTION_1) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 17) {
					player.penguinpoints -= 17;
					player.getInventory().addItem(13109, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 20) {
					player.penguinpoints -= 20;
					player.getInventory().addItem(13111, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_3) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 23) {
					player.penguinpoints -= 23;
					player.getInventory().addItem(13113, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_4) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 25) {
					player.penguinpoints -= 25;
					player.getInventory().addItem(13115, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_5) {
				stage = 6;
				sendOptionsDialogue("Select an Option", "Fox Mask (35 points)", "White Unicorn Mask (45 points)", "Black Unicorn Mask (45 points)", "Nevermind");
			}
	 } else if(stage == 6) {
			if(componentId == OPTION_1) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 35) {
					player.penguinpoints -= 35;
					player.getInventory().addItem(19272, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 45) {
					player.penguinpoints -= 45;
					player.getInventory().addItem(19275, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_3) {
				player.getInterfaceManager().closeChatBoxInterface();
				if (player.penguinpoints >= 45) {
					player.penguinpoints -= 45;
					player.getInventory().addItem(19278, 1);
				} else {
					player.sm("You do not have enough penguin points to purchase this.");
				}
			} else if(componentId == OPTION_4) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		 }
		
	}

	@Override
	public void finish() {
		
	}
	
}