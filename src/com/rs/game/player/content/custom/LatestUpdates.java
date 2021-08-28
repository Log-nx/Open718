package com.rs.game.player.content.custom;

import com.rs.Settings;
import com.rs.game.player.Player;
public class LatestUpdates {

	
	
	public static void sendInterface(Player player){
		player.getInterfaceManager().sendInterface(116);
		player.getPackets().sendIComponentText(116, 2, ""+Settings.SERVER_NAME+"'s latest update.");
		player.getPackets().sendIComponentText(116, 5, "<br><br>" +Settings.LATEST_UPDATES);
		
	}
}
