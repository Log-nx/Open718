package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.ForceTalk;
import com.rs.game.minigames.hunger.HungerGames;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;

public class HungerMatch extends Dialogue {

        private int npcId = 8977; //Grim Reaper

        /**
         * Dialogue is starting..
         */
        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Dear member, do you know, you are currently talking with "+Settings.SERVER_NAME+"'s most cruel man. Are you still willing to have a talk with me?"}, IS_NPC, npcId, 9827);
        }
        
        /**
         * Dialogue is going to run.. its starting..
         */
        @Override
        public void run(int interfaceId, int componentId)  {
                if (stage == -1) {
                        sendOptionsDialogue("What would you like to do?", "I'd like to join the match.", "What's my level in Hunger?", "May I receive my title?", "Show me the Store?");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                        	HungerGames.addPlayer(player);
            				closeInterface(player);
                				end();
                		} 
                        else if (componentId == OPTION_2) {
                        	player.setNextForceTalk(new ForceTalk("My Hunger level is: "+player.hungerNumber+"."));
                        	player.sm("I have: "+player.hungerPoints+"");
                            end();
                            }
                        else if (componentId == OPTION_3) {
                        	if (player.hungerNumber == 1) {
            					player.getAppearence().setTitle(5555);
            				} else if (player.hungerNumber == 2) {
            					player.getAppearence().setTitle(5556);
            				} else if (player.hungerNumber == 3) {
            					player.getAppearence().setTitle(5557);
            				} else if (player.hungerNumber == 4) {
            					player.getAppearence().setTitle(5558);
            				} else if (player.hungerNumber == 5) {
            					player.getAppearence().setTitle(5559);
            				} else {
            					player.getPackets().sendGameMessage("You have not completed a round yet.");
            				}
                            end();
                            }
                        else if (componentId == OPTION_4) {
                        	player.hungerShops();
                            end();
                            }
                       end();
                        }}
        private static void closeInterface(Player player) {
    		player.closeInterfaces();
        }
        @Override
        public void finish() {

        }
}