package com.rs.game.player.dialogues.impl;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.WorldTile;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class ConstructionTable extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to make?",
						"Wooden Chair - 2 Planks", 
						"Oak Bookcase - 4 Oak Planks", 
						"Teak Armchair - 3 Teak Planks",
						"Mahogany b'kcase - 5 Mahogany Planks",
						"I can't make any of them.");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (componentId == OPTION_1) {
					if (player.getInventory().containsItem(960, 2)
							&& player.getInventory().containsItem(2347, 1)) {
						    player.getInventory().deleteItem(960, 2);
						    player.getInventory().addItemDrop(8498, 1);
						    player.getSkills().addXp(Skills.CONSTRUCTION, 40);
						    player.getPackets().sendGameMessage(
							    "You successfully make a wooden chair.");
						    player.setNextAnimation(new Animation(898));
						    end();
						} else {
						    sendDialogue("You need a hammer and  at least two planks to make a wooden chair.");
						    stage = 3;
						}
				} else if (componentId == OPTION_2) {
					if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 15) {
					    sendDialogue("You need a level of 15 in construction to do this.");
					    stage = 3;
					    return;
					}
					if (player.getInventory().containsItem(8778, 4)
							&& player.getInventory().containsItem(2347, 1)) {
						    player.getInventory().deleteItem(8778, 4);
						    player.getInventory().addItemDrop(8512, 1);
						    player.getSkills().addXp(Skills.CONSTRUCTION, 60);
						    player.getPackets().sendGameMessage(
							    "You successfully make an oak bookcase.");
						    player.setNextAnimation(new Animation(898));
						    end();
						} else {
						    sendDialogue("You need a hammer and  at least four oak planks to make an oak bookcase.");
						    stage = 3;
						}
				} else if (componentId == OPTION_3) {
					if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 60) {
					    sendDialogue("You need a level of 60 in construction to do this.");
					    stage = 3;
					    return;
					}
					if (player.getInventory().containsItem(8780, 3)
							&& player.getInventory().containsItem(2347, 1)) {
						    player.getInventory().deleteItem(8780, 3);
						    player.getInventory().addItemDrop(8506, 1);
						    player.getSkills().addXp(Skills.CONSTRUCTION, 80);
						    player.getPackets().sendGameMessage(
							    "You successfully make a teak armchair.");
						    player.setNextAnimation(new Animation(898));
						    end();
						} else {
						    sendDialogue("You need a hammer and  at least three teak planks to make a teak armchair.");
						    stage = 3;
						}
				} else if (componentId == OPTION_4) {
					if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 75) {
					    sendDialogue("You need a level of 75 in construction to do this.");
					    stage = 3;
					    return;
					}
					if (player.getInventory().containsItem(8782, 5)
							&& player.getInventory().containsItem(2347, 1)) {
						    player.getInventory().deleteItem(8782, 5);
						    player.getInventory().addItemDrop(8514, 1);
						    player.getSkills().addXp(Skills.CONSTRUCTION, 100);
						    player.getPackets().sendGameMessage(
							    "You successfully make a mahogany b'kcase.");
						    player.setNextAnimation(new Animation(898));
						    end();
						} else {
						    sendDialogue("You need a hammer and  at least five teak planks to make a mahogany b'kcase.");
						    stage = 3;
						}
				} else if (componentId == OPTION_5) {
			        end();
					
				} else if (stage == 3) {
					end();
				
				}
				}

	

	@Override
	public void finish() {

	}

}
