package com.rs.game.player.content.interfaces;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class StaffList {

	private final static int INTERFACE = 1158;

	private final static String ONLINE = "<col=02AB2F>Online</col>";
	private final static String OFFLINE = "<col=DB0000>Offline</col>";

	private enum Staff {

		OWNER("BigFuckinChungus", "Founder", "BigFuckinChungus"),
		
		ADMINISTRATOR1("", "Head Administator", "", ""),

		ADMINISTRATOR2("", "Administrator", "", ""),
		
		MODERATOR1("", "Head Moderator", "", ""),
		
		MODERATOR2("", "Moderator", ""),
		
		MODERATOR3("", "Moderator", ""),
		
		SUPPORT1("", "Player Support", ""),

		SUPPORT2("", "Player Support", "");

		private final String username, position;
		private final String[] usernames;

		Staff(String username, String position, String... usernames) {
			this.username = username;
			this.position = position;
			this.usernames = usernames;
		}

		public String getUsername() {
			return username;
		}

		public String getPosition() {
			return position;
		}

		public String getOnline() {
			for (String name : usernames) {
				if (World.containsPlayer(name))
					return ONLINE;
			}
			return OFFLINE;
		}
	}

	public static void send(Player player) {
		player.getInterfaceManager().sendInterface(INTERFACE);
		for (int i = 0; i < Utils
				.getInterfaceDefinitionsComponentsSize(INTERFACE); i++)
			player.getPackets().sendIComponentText(INTERFACE, i, "");
		player.getPackets().sendIComponentText(INTERFACE, 74, "Diccus Staff List");
		int componentId = 8;
		int number = 1;
		for (Staff staff : Staff.values()) {
			if (componentId >= 56) // end of interface
				return;
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					"" + number++);
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					staff.getUsername());
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					staff.getPosition());
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					staff.getOnline());
			componentId++;
		}
	}
}
