package com.rs.game.player.content.tutorial;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.managers.HintIconsManager;

public class Login {

	private Login guide;	
	
	public static void LoginInterface(Player player) {
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		   player.getPackets().sendIComponentText(372, 0, ""+Settings.SERVER_NAME+" Manager");	
		   player.getPackets().sendIComponentText(372, 1, "<col=e65243>Welcome to "+Settings.SERVER_NAME+"");
		   player.getPackets().sendIComponentText(372, 2, "");
		   player.getPackets().sendIComponentText(372, 3, "Welcome " +player.getDisplayName());
		   player.getPackets().sendIComponentText(372, 4, "Talk to the "+Settings.SERVER_NAME+" Manager to start the tutorial.");
		   player.getPackets().sendIComponentText(372, 5, "");
		   player.getPackets().sendIComponentText(372, 6, "");
		   NPC guide = findNPC(945);
		   player.getHintIconsManager().addHintIcon(guide, 0, -1, false);
		 
			}
	 public static NPC findNPC(int id) {
			for (NPC npc : World.getNPCs()) {
				if (npc == null || npc.getId() != id)
					continue;
				return npc;
			}
			return null;
		}
	
}