package com.rs.game.player.content.interfaces;

import com.rs.Settings;
import com.rs.game.player.Player;

public class NewsDashboard {
	
 public static int NEWS = 205;
 
	public static void MainBoard(Player player) {
		if (player.ToggleNews = true) {
			player.getPackets().sendIComponentText(NEWS, 61, "Updates");
			player.getPackets().sendIComponentText(NEWS, 57, "Skilling");
			player.getPackets().sendIComponentText(NEWS, 53, "Minigames");
			player.getPackets().sendIComponentText(NEWS, 49, "Community");
			player.getPackets().sendIComponentText(NEWS, 65, "Dear "+player.getUsername());
			player.getPackets().sendIComponentText(NEWS, 64, "Be Please and" + " select an option from the categories " + " anything you want to know about.");
			player.getInterfaceManager().sendInterface(NEWS);
		} else {
			player.sm("You have skipped news upon login.");
		}
	}
	public static void sendNews(Player player) {
		player.closeInterfaces();
		player.getPackets().sendIComponentText(NEWS, 61, "Updates");
		player.getPackets().sendIComponentText(NEWS, 57, "Skilling");
		player.getPackets().sendIComponentText(NEWS, 53, "Minigames");
		player.getPackets().sendIComponentText(NEWS, 49, "Community");
		/*Title*/
		player.getPackets().sendIComponentText(NEWS, 65, "Updates");
		player.getPackets().sendIComponentText(NEWS, 64, Settings.LATEST_UPDATES);
		player.getInterfaceManager().sendInterface(NEWS);
	}
	public static void sendUpdateLogs(Player player) {
		player.closeInterfaces();
		player.getPackets().sendIComponentText(NEWS, 61, "Updates");
		player.getPackets().sendIComponentText(NEWS, 57, "Skilling");
		player.getPackets().sendIComponentText(NEWS, 53, "Minigames");
		player.getPackets().sendIComponentText(NEWS, 49, "Community");
		/*Title*/
		player.getPackets().sendIComponentText(NEWS, 65, "Skilling");
		player.getPackets().sendIComponentText(NEWS, 64, "Training skills is an activity done in order to increase a player's experience in one or more skills." +
					" Activities which are not done primarily for gaining experience are not usually considered to be training, for example, fighting Vorago in order to obtain rare item drops.");
		player.getInterfaceManager().sendInterface(NEWS);
	} 
	
	public static void sendNotifications(Player player) {
	player.closeInterfaces();
	player.getPackets().sendIComponentText(NEWS, 61, "Updates");
	player.getPackets().sendIComponentText(NEWS, 57, "Skilling");
	player.getPackets().sendIComponentText(NEWS, 53, "Minigames");
	player.getPackets().sendIComponentText(NEWS, 49, "Community");
	/*Title*/
	player.getPackets().sendIComponentText(NEWS, 65, "Minigames");
	player.getPackets().sendIComponentText(NEWS, 64, "Minigames formerly known as activities, are small games in which a player set out to complete a certain objective." +
			" Minigames allow players to gain experiences and items in some games.");
	player.getInterfaceManager().sendInterface(NEWS);
	} 

	public static void sendEvents(Player player) {
	player.closeInterfaces();
	player.getPackets().sendIComponentText(NEWS, 61, "Updates");
	player.getPackets().sendIComponentText(NEWS, 57, "Skilling");
	player.getPackets().sendIComponentText(NEWS, 53, "Minigames");
	player.getPackets().sendIComponentText(NEWS, 49, "Community");
	/*Title*/
	player.getPackets().sendIComponentText(NEWS, 65, "Community");
	player.getPackets().sendIComponentText(NEWS, 64, "Be please to be a part of our community by registering on our community forum." +
			" You will have the opportunity to post and learn more about "+Settings.SERVER_NAME+". " +
			"COMING SOON");
	player.getInterfaceManager().sendInterface(NEWS);
	} 
	 public static void handleButtons(Player player, int componentId) {
		  if (componentId == 49) {	
		        sendEvents(player);
		        
		        }
		  if (componentId == 57) {	
		        sendUpdateLogs(player);
		        }
		  if (componentId == 53) {	
		        sendNotifications(player);
		        }
	        if (componentId == 61) {	
	        sendNews(player);
	        }
	 }

	
}
