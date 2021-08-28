package com.rs.game.player.dialogues.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.ForceTalk;
import com.rs.game.player.dialogues.Dialogue;

public class WarriorsGuildD extends Dialogue {

    private int npcId = 4289;
	private int componentId;

    @Override
    public void start() {
            sendEntityDialogue(SEND_2_TEXT_CHAT,
                            new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                            "Ahh.. It seems my Dragon Defender has already been spread out in the world. Anyway, how can I help you "+player.getDisplayName()+"?"}, IS_NPC, npcId, 9827);
    }
    
    @Override
    public void run(int interfaceId, int componentId)  {
            if (stage == -1) {
                    sendOptionsDialogue("Select an Option", "Dragon Defender", "Check Points", "Nevermind");
                    stage = 1;
            } else if (stage == 1) {
                    if (componentId == OPTION_1) {
                    	sendNPCDialogue(npcId, 9827, "The dragon defender is an off-hand defensive item made of dragon metal, and is currently the most powerful defender that players can obtain. And remember wielding the dragon defender only requires level 60 in Attack and Defence.");
                            stage = 2;
            		}
                            }
            if (componentId == OPTION_2) {
            	player.sm("You currently have " + player.getwarriorsPoints() +" Points.");
            	end();
            }
            if (componentId == OPTION_3) {
            	end();
            } else if (stage == 2) {
            	sendPlayerDialogue(9827, "Wow sounds amazing!");
            	stage = 3;
            } else if (stage == 3) {
            	sendNPCDialogue(npcId, 9827, "That's because it is, would you still like to buy one pair of dragon defender? <br>",
            			"Price: <col=ff0000>100 Points</col>.<br>",
            			"Your Points: <col=ff0000>" + player.getwarriorsPoints() +"</col>.");
            	stage = 4;
            } else if (stage == 4) { //Dragon Defender <col=ff0000>100 Points</col>
    			sendOptionsDialogue("You sure?", "Yes!", "No thank you.");
            	stage = 5;
            } else if (stage == 5) {
    			if (componentId == OPTION_1) {
        				if (player.warriorsPoints >= 100) {
        					player.getInventory().addItem(20072, 1);
        					player.warriorsPoints -= 100;
        					player.getInventory().refresh();
        					player.getInterfaceManager().closeChatBoxInterface();
        					end();
        				    }else {
        					player.getPackets().sendGameMessage("You dont have enough Warriors Points.");
        					end();
    				}
    			}
    			else if (componentId == OPTION_2) {
                	end();
                }
            end();
            }
        }


    @Override
    public void finish() {

    }
}