package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.game.player.dialogues.Dialogue;

public class Prestige extends Dialogue {

        private int npcId = 25715;

        @Override
        public void start() {
    		int option = (int) parameters[0];
    			switch (option) {
    			case 1:
                    sendNPCDialogue(npcId, NORMAL, "Hello "+ player.getDisplayName() +", would you like to learn about "+ Settings.SERVER_NAME +"'s Prestige system?");
                    stage = 1;
    				break;
    			case 2:
    				sendNPCDialogue(npcId, NORMAL, "I'll have more things avaliable in the future.");
                	stage = 3;
    				break;
    			}
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
        	switch (stage) {
			case 1:
                 sendOptionsDialogue("Learn about the Prestige System?", "Yes", "No");
				 stage = 2;
				 break;
			case 2:
				switch (componentId) {
				case OPTION_1:
					sendPlayerDialogue(NORMAL, "Yes I would actually.");
					stage = 4;
					break;
				case OPTION_2:
					sendPlayerDialogue(NORMAL, "No thanks.");
					stage = 3;
					break;
				}
			break;
			case 3:
				end();
				break;
			case 4:
            	sendNPCDialogue(npcId, NORMAL, "The Prestige System allows you to reset your skills for items and buffs. Each prestige will give you prestige points depending on your current gamemode.");
            	stage = 5;
				break;
			case 5:
				sendPlayerDialogue(QUESTIONS, "Can I check what my current gamemode is?");
            	stage = 6;
				break;
			case 6:
            	sendNPCDialogue(npcId, HAPPY, "Of course you can! Just use the command ::mymode to find out!");
            	stage = 7;
				break;
			case 7:
				sendPlayerDialogue(QUESTIONS, "When can I prestige?");
            	stage = 8;
				break;
			case 8:
            	sendNPCDialogue(npcId, NORMAL, "To prestige, first you need to have all of your stats to at least level 99.");
            	stage = 9;
				break;
			case 9:
				sendNPCDialogue(npcId, HAPPY, "That's about it for now, if you have anymore questions you come back and ask me.");
				player.setHasTalkedToJade(true);
            	stage = 3;
				break;
			}
          }


        @Override
        public void finish() {

        }
    }