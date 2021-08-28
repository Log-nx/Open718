package com.rs.game.player.content.tutorial;

import java.util.ArrayList;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class ServerTutorial {
	public static ArrayList<Player> teleportTeam = new ArrayList<Player>();
	public static void Tutorial(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int timer;
				

	public void run() {
	
		if (timer == 0 && player.completedTutorial == false) {
			player.getDialogueManager().startDialogue("Difficulties");
		}
	if (timer == 1 && player.completedTutorial == false) {
	player.getHintIconsManager().removeUnsavedHintIcon();	
	player.getAppearence().setRenderEmote(2191);
	   player.lock();
	   player.getInterfaceManager().replaceRealChatBoxInterface(372);
	   player.getPackets().sendIComponentText(372, 0, ""+Settings.SERVER_NAME+" Manager");
	   player.getPackets().sendIComponentText(372, 1, "<col=e65243>Welcome to "+Settings.SERVER_NAME+"");
	   player.getPackets().sendIComponentText(372, 2, "");
	   player.getPackets().sendIComponentText(372, 3, "This is the Manager of "+Settings.SERVER_NAME+".");
	   player.getPackets().sendIComponentText(372, 4, "He has everything a player needs.");
	   player.getPackets().sendIComponentText(372, 5, "The tutorial will start in a moment.");
	   teleportTeam.add(player);
	   player.getPackets().sendIComponentText(372, 6, "");
		}
		   
	else if (timer == 2 && player.completedTutorial == false ) {
			player.setNextWorldTile(new WorldTile(2329, 3689, 0));
				   player.getInterfaceManager().replaceRealChatBoxInterface(372);
				   player.getPackets().sendIComponentText(372, 0, ""+Settings.SERVER_NAME+" Manager");
				   player.getPackets().sendIComponentText(372, 1, "<col=e65243>Welcome to "+Settings.SERVER_NAME+"");
				   player.getPackets().sendIComponentText(372, 2, "");
				   player.getPackets().sendIComponentText(372, 3, "Here's the bank & the bankers.");
				   player.getPackets().sendIComponentText(372, 4, "This is where you keep your stuff safe, by putting them in your bank.");
				   player.getPackets().sendIComponentText(372, 5, "<col=ff0000>You can also make yourself a PIN for your bank.");
				   player.getPackets().sendIComponentText(372, 6, "");
		   }
	
	else if (timer == 3 && player.completedTutorial == false) {
		player.setNextWorldTile(new WorldTile(Settings.START_PLAYER_LOCATION));
		   player.getInterfaceManager().replaceRealChatBoxInterface(372);
		   player.getPackets().sendIComponentText(372, 0, ""+Settings.SERVER_NAME+" Manager");
		   player.getPackets().sendIComponentText(372, 1, "<col=e65243>Welcome to "+Settings.SERVER_NAME+"");
		   player.getPackets().sendIComponentText(372, 2, "");
		   player.getPackets().sendIComponentText(372, 3, "This is our home, and you'll be respawned right here.");
		   player.getPackets().sendIComponentText(372, 4, "");
		   player.getPackets().sendIComponentText(372, 5, "");
		   player.getPackets().sendIComponentText(372, 6, "");
	}
	
	else if (timer == 4 && player.completedTutorial == false) {
		player.setNextWorldTile(new WorldTile(3094, 3107, 0));
		   player.getInterfaceManager().replaceRealChatBoxInterface(372);
		   player.getPackets().sendIComponentText(372, 0, ""+Settings.SERVER_NAME+" Manager");
		   player.getPackets().sendIComponentText(372, 1, "<col=e65243>Welcome to "+Settings.SERVER_NAME+"");
		   player.getPackets().sendIComponentText(372, 2, "");
		   player.getPackets().sendIComponentText(372, 3, "Next, we are gonna change our outfit.");
		   player.getPackets().sendIComponentText(372, 4, "<col=FFF0000>(Talk-to the man with the red beret)");
		   player.getPackets().sendIComponentText(372, 5, "");
		   player.getPackets().sendIComponentText(372, 6, "");

		 
	}
	
	else if (timer == 5 && player.completedTutorial == false) {
		ClothGuide.ClothIcon(player);
		player.unlock();
	}
	timer++;
			
			
		}
		},0, 7);
		
		
	

		
	}
	
	public static void Part2(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int timer;
			@Override
			public void run() {
				if (timer == 0 && player.completedTutorial == false) {
					   player.lock();
					   player.getInterfaceManager().replaceRealChatBoxInterface(372);
					   player.setNextWorldTile(new WorldTile(Settings.START_PLAYER_LOCATION));
					   player.getPackets().sendIComponentText(372, 0, ""+Settings.SERVER_NAME+" Manager");
					   player.getPackets().sendIComponentText(372, 1, "<col=e65243>Welcome to "+Settings.SERVER_NAME+"");
					   player.getPackets().sendIComponentText(372, 2, "");
					   player.getPackets().sendIComponentText(372, 3, "We are very grateful that you have chosen us.");
					   player.getPackets().sendIComponentText(372, 4, "We hope you'll enjoy your stay & your starter package.");
					   player.getPackets().sendIComponentText(372, 5, "<u><col=ff0000>Need help/Questions?</col> Type ::ticket.</u>");
					   player.getPackets().sendIComponentText(372, 6, "");
						}
				
				else if (timer == 1) {
					player.unlock();
					player.getInterfaceManager().closeReplacedRealChatBoxInterface();
					player.completedTutorial = true;
					player.hasRecievedStarter = true;
					player.getAppearence().setRenderEmote(-1);
					
				}
				timer++;
			}
			
		},0,7);
	
	}
	
	
	

}
