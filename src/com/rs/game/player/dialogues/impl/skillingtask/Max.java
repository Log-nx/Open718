package com.rs.game.player.dialogues.impl.skillingtask;

import com.rs.game.player.content.activities.skillingtask.SkillerTasks;
import com.rs.game.player.dialogues.Dialogue;

public class Max extends Dialogue {

	private int npcId;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			if (player.getSkillTasks().hasTask()) {
				sendOptionsDialogue("Chose an Option",
						"What is my current task?",
						"What do you have in your shop?",
						"What are the task rewards?",
						"I would like to cancel my task.",
						"Nothing, Nevermind.");
				stage = 0;
			} else {
				sendOptionsDialogue("Chose an Option",
						"Please give me a task.",
						"What do you have in your shop?",
						"What are the task rewards?", "Nothing, Nevermind.");
				stage = 1;
			}
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				sendNPCDialogue(npcId, 9827, "Your current task is "
						+ player.getSkillTasks().getCurrentTask()
								.getAssignment() + ".", player.getSkillTasks()
						.getCurrentTask().getDescription(), "You have "
						+ player.getSkillTasks().getTaskAmount()
						+ " more to go!");
				stage = 9;
			} else if (componentId == OPTION_2) {
				sendNPCDialogue(npcId, 9827, "I got a lot available for you!");
				stage = 7;
			} else if (componentId == OPTION_3) {
				sendNPCDialogue(npcId, 9827,
						"The amount of points and cash vary",
						"depending on the task tier that you", "chose.");
				stage = 2;
			} else if (componentId == OPTION_4) {
				sendNPCDialogue(npcId, 9827,
						"That will cost either one point or 25,000gp to cancel your task.");
				stage = 4;
			} else
				end();
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Choose a Tier (Money/Points)",
						"Easy (100k/1)", "Medium (300k/3)", "Hard (700k/7)",
						"Elite (1m/10)");
				stage = 6;
			} else if (componentId == OPTION_2) {
				sendNPCDialogue(npcId, 9827, "I got a lot available for you!");
				stage = 7;
			} else if (componentId == OPTION_3) {
				sendNPCDialogue(npcId, 9827,
						"The amount of points and cash vary",
						"depending on the task tier that you", "chose.");
				stage = 2;
			} else
				end();
		} else if (stage == 2) {
			sendNPCDialogue(npcId, 9827,
					"The harder the task, the more you will",
					"receive upon completion.");
			stage = 3;
		} else if (stage == 3) {
			sendNPCDialogue(npcId, 9827, "The rewards (money/points) are: ",
					"Easy (100k/1), Medium (300k/3),",
					"Hard (700k/7), Elite (1m/10),");
			stage = 69;
		} else if (stage == 4) {
			sendOptionsDialogue("Cancel Task?", "Yes (1 Point)",
					"Yes (250K GP)", "No");
			stage = 5;
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() > 0) {
					sendNPCDialogue(npcId, 9827,"You successfully reset your task.");
					player.setTaskPoints(player.getTaskPoints() - 1);
					player.getSkillTasks().setCurrentTask(null);
				} else {
					sendNPCDialogue(npcId, 9827, "You do not have enough task points to reset your task.");
				}
				stage = 69;
			} else if (componentId == OPTION_2) {
				if (player.getInventory().getCoinsAmount() >= 25000) {
					sendNPCDialogue(npcId, 9827,
							"You successfully reset your task.");
					player.getInventory().removeItemMoneyPouch(995, 25000);
					player.getSkillTasks().setCurrentTask(null);
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough money to reset your task.");
				}
				stage = 69;
			} else
				end();
		} else if (stage == 6) {
			if (componentId == OPTION_1)
				player.getSkillTasks().getNewTask(SkillerTasks.EASY);
			else if (componentId == OPTION_2)
				player.getSkillTasks().getNewTask(SkillerTasks.MEDIUM);
			else if (componentId == OPTION_3)
				player.getSkillTasks().getNewTask(SkillerTasks.HARD);
			else if (componentId == OPTION_4)
				player.getSkillTasks().getNewTask(SkillerTasks.ELITE);
			sendNPCDialogue(npcId, 9827, player.getSkillTasks()
					.getCurrentTask().getDescription(),
					"To view the progress of your task, use the command ::taskinfo.");
			stage = 69;
		} else if (stage == 7) {
			sendOptionsDialogue("Choose a Category", "Server Gold",
					"Squeal Spins", "Exclusive Items", "Mystery Box");
			stage = 9;
		} else if (stage == 8) {
			sendNPCDialogue(npcId, 9827, "Pleasure doing business with you!");
			stage = 69;
		} else if (stage == 9) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Choose a Reward", "35,000 Coins (1)",
						"150,000 Coins (4)", "300,000 Coins (7)",
						"3,000,000 Coins (65)");
				stage = 10;
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue("Choose a Reward", "4 Spins (1)",
						"20 Spins (4)", "40 Spins (7)", "100 Spins (13)");
				stage = 11;
			} else if (componentId == OPTION_3) {
				sendOptionsDialogue("Choose a Reward", "Agile Set (35)",
						"Golden Hammer (10)", "Gorilla Mask (15)",
						"Gilded Set (50)", "More");
				stage = 13;
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("Choose a Reward", "1 Box (1)",
						"5 Boxes (4)", "10 Boxes (7)", "25 Boxes (15)", "More");
				stage = 12;
			}
		} else if (stage == 10) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 1) {
					player.setTaskPoints(player.getTaskPoints() - 1);
					player.getMoneyPouch().sendDynamicInteraction(35000, false);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 4) {
					player.setTaskPoints(player.getTaskPoints() - 4);
					player.getMoneyPouch().sendDynamicInteraction(150000, false);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 7) {
					player.setTaskPoints(player.getTaskPoints() - 7);
					player.getMoneyPouch().sendDynamicInteraction(300000, false);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 65) {
					player.setTaskPoints(player.getTaskPoints() - 65);
					player.getMoneyPouch().sendDynamicInteraction(3000000, false);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			}
		} else if (stage == 11) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 1) {
					player.setTaskPoints(player.getTaskPoints() - 1);
					player.getSquealOfFortune().giveEarnedSpins(4);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 4) {
					player.setTaskPoints(player.getTaskPoints() - 4);
					player.getSquealOfFortune().giveEarnedSpins(20);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 7) {
					player.setTaskPoints(player.getTaskPoints() - 7);
					player.getSquealOfFortune().giveEarnedSpins(40);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 13) {
					player.setTaskPoints(player.getTaskPoints() - 13);
					player.getSquealOfFortune().giveEarnedSpins(100);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			}
		} else if (stage == 12) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 1) {
					player.setTaskPoints(player.getTaskPoints() - 1);
					player.getInventory().addItemMoneyPouchDrop(6199, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 4) {
					player.setTaskPoints(player.getTaskPoints() - 4);
					player.getInventory().addItemMoneyPouchDrop(6199, 5);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 7) {
					player.setTaskPoints(player.getTaskPoints() - 7);
					player.getInventory().addItemMoneyPouchDrop(6199, 10);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 15) {
					player.setTaskPoints(player.getTaskPoints() - 15);
					player.getInventory().addItemMoneyPouchDrop(6199, 25);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			}
		} else if (stage == 13) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 35) {
					player.setTaskPoints(player.getTaskPoints() - 35);
					player.getInventory().addItemMoneyPouchDrop(14936, 1);
					player.getInventory().addItemMoneyPouchDrop(14938, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 10) {
					player.setTaskPoints(player.getTaskPoints() - 10);
					player.getInventory().addItemMoneyPouchDrop(20084, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 15) {
					player.setTaskPoints(player.getTaskPoints() - 15);
					player.getInventory().addItemMoneyPouchDrop(22314, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 50) {
					player.setTaskPoints(player.getTaskPoints() - 50);
					player.getInventory().addItemMoneyPouchDrop(3481, 1);//
					player.getInventory().addItemMoneyPouchDrop(3483, 1);
					player.getInventory().addItemMoneyPouchDrop(3486, 1);
					player.getInventory().addItemMoneyPouchDrop(3488, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Choose a Reward", "Bobble Set (40)",
						"Jester Set (40)", "Tri-Jester Set (40)",
						"Woolly Set (40)", "More");
				stage = 14;
			}
		} else if (stage == 14) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 40) {
					player.setTaskPoints(player.getTaskPoints() - 40);
					player.getInventory().addItemMoneyPouchDrop(6856, 1);
					player.getInventory().addItemMoneyPouchDrop(6857, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 40) {
					player.setTaskPoints(player.getTaskPoints() - 40);
					player.getInventory().addItemMoneyPouchDrop(6858, 1);
					player.getInventory().addItemMoneyPouchDrop(6859, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 40) {
					player.setTaskPoints(player.getTaskPoints() - 40);
					player.getInventory().addItemMoneyPouchDrop(6860, 1);
					player.getInventory().addItemMoneyPouchDrop(6861, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 40) {
					player.setTaskPoints(player.getTaskPoints() - 40);
					player.getInventory().addItemMoneyPouchDrop(6862, 1);
					player.getInventory().addItemMoneyPouchDrop(6863, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Choose a Reward", "Top Hat (15)",
						"Rune Cane (15)", "Soul Wars Cape - Blue (50)",
						"Soul Wars Cape - Red (50)", "More");
				stage = 15;
			}
		} else if (stage == 15) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 15) {
					player.setTaskPoints(player.getTaskPoints() - 15);
					player.getInventory().addItemMoneyPouchDrop(13101, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 15) {
					player.setTaskPoints(player.getTaskPoints() - 15);
					player.getInventory().addItemMoneyPouchDrop(13099, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 50) {
					player.setTaskPoints(player.getTaskPoints() - 50);
					player.getInventory().addItemMoneyPouchDrop(15433, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 50) {
					player.setTaskPoints(player.getTaskPoints() - 50);
					player.getInventory().addItemMoneyPouchDrop(15432, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Choose a Reward", "Chicken Suit (35)",
						"Witchdoctor Set (25)", "Cavalier and Mask (15)",
						"Ringmaster Set (20)", "More");
				stage = 16;
			}
		} else if (stage == 16) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 35) {
					player.setTaskPoints(player.getTaskPoints() - 35);
					player.getInventory().addItemMoneyPouchDrop(11019, 1);
					player.getInventory().addItemMoneyPouchDrop(11020, 1);
					player.getInventory().addItemMoneyPouchDrop(11021, 1);
					player.getInventory().addItemMoneyPouchDrop(11022, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 25) {
					player.setTaskPoints(player.getTaskPoints() - 25);
					player.getInventory().addItemMoneyPouchDrop(20044, 1);
					player.getInventory().addItemMoneyPouchDrop(20045, 1);
					player.getInventory().addItemMoneyPouchDrop(20046, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 15) {
					player.setTaskPoints(player.getTaskPoints() - 15);
					player.getInventory().addItemMoneyPouchDrop(11280, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 20) {
					player.setTaskPoints(player.getTaskPoints() - 20);
					player.getInventory().addItemMoneyPouchDrop(13672, 1);
					player.getInventory().addItemMoneyPouchDrop(13673, 1);
					player.getInventory().addItemMoneyPouchDrop(13674, 1);
					player.getInventory().addItemMoneyPouchDrop(13675, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Choose a Reward", "Boater Set (50)",
						"Gnome Scarf (20)", "Gnome Ball (10)",
						"Black Elegant Set (45)", "More");
				stage = 17;
			}
		} else if (stage == 17) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 50) {
					player.setTaskPoints(player.getTaskPoints() - 50);
					player.getInventory().addItemMoneyPouchDrop(7319, 1);
					player.getInventory().addItemMoneyPouchDrop(7321, 1);
					player.getInventory().addItemMoneyPouchDrop(7323, 1);
					player.getInventory().addItemMoneyPouchDrop(7325, 1);
					player.getInventory().addItemMoneyPouchDrop(7327, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 20) {
					player.setTaskPoints(player.getTaskPoints() - 20);
					player.getInventory().addItemMoneyPouchDrop(9470, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 10) {
					player.setTaskPoints(player.getTaskPoints() - 10);
					player.getInventory().addItemMoneyPouchDrop(751, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 45) {
					player.setTaskPoints(player.getTaskPoints() - 45);
					player.getInventory().addItemMoneyPouchDrop(10400, 1);
					player.getInventory().addItemMoneyPouchDrop(10402, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Choose a Reward", "Red Elegant Set (45)",
						"Blue Elegant Set (45)", "Green Elegant Set (45)",
						"Purple Elegant Set (45)", "White Elegant Set (45)");
				stage = 18;
			}
		} else if (stage == 18) {
			if (componentId == OPTION_1) {
				if (player.getTaskPoints() >= 45) {
					player.setTaskPoints(player.getTaskPoints() - 45);
					player.getInventory().addItemMoneyPouchDrop(10404, 1);
					player.getInventory().addItemMoneyPouchDrop(10406, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_2) {
				if (player.getTaskPoints() >= 45) {
					player.setTaskPoints(player.getTaskPoints() - 45);
					player.getInventory().addItemMoneyPouchDrop(10408, 1);
					player.getInventory().addItemMoneyPouchDrop(10410, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_3) {
				if (player.getTaskPoints() >= 45) {
					player.setTaskPoints(player.getTaskPoints() - 45);
					player.getInventory().addItemMoneyPouchDrop(10412, 1);
					player.getInventory().addItemMoneyPouchDrop(10414, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_4) {
				if (player.getTaskPoints() >= 45) {
					player.setTaskPoints(player.getTaskPoints() - 45);
					player.getInventory().addItemMoneyPouchDrop(10416, 1);
					player.getInventory().addItemMoneyPouchDrop(10418, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			} else if (componentId == OPTION_5) {
				if (player.getTaskPoints() >= 45) {
					player.setTaskPoints(player.getTaskPoints() - 45);
					player.getInventory().addItemMoneyPouchDrop(10420, 1);
					player.getInventory().addItemMoneyPouchDrop(10422, 1);
					sendNPCDialogue(npcId, 9827,
							"Pleasure doing business with you!");
					stage = 69;
				} else {
					sendNPCDialogue(npcId, 9827,
							"You do not have enough points for this...");
					stage = 69;
				}
			}
		} else if (stage == 69) {
			end();
		}
	}
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Welcome to the Skiller Task System!",
				"A great way for skillers to make",
				"money and unlock exclusive items.");
		stage = -1;
	}
}