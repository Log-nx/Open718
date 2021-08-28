package com.rs.game.player.content;

import com.rs.game.player.Player;

public class LoyaltyStore {

	private static final int INTERFACE_ID = 1143;
	
	private static final int TAB_CONFIG = 2226;
	/**
	 * The current tab
	 */
	private static int currentTab;
	/**
	 * Opens the loyalty shop interface
	 */
	public static void openShop(Player player) {
		player.getPackets().sendWindowsPane(INTERFACE_ID, 0);
		currentTab = -1;
		player.getPackets().sendIComponentText(INTERFACE_ID, 127,""+player.getStatistics().getRuneCoins());
	}
	/**
	 * Opens a tab on the loyalty interface
	 * 
	 * @param tab
	 *            The tab to open
	 */
	public static void openTab(String tab, Player player) {
		switch (tab.toLowerCase()) {
		case "home":
			player.getPackets().sendConfig(TAB_CONFIG, -1);
			currentTab = -1;
		case "auras":
			player.getPackets().sendConfig(TAB_CONFIG, 1);
			currentTab = 1;
			break;
		case "emotes":
			player.getPackets().sendConfig(TAB_CONFIG, 2);
			currentTab = 2;
			break;
		case "outfits":
			player.getPackets().sendConfig(TAB_CONFIG, 3);
			currentTab = 3;
			break;
		case "titles":
			player.getPackets().sendConfig(TAB_CONFIG, 4);
			currentTab = 4;
			break;
		case "recolor":
			player.getPackets().sendConfig(TAB_CONFIG, 5);
			currentTab = 5;
			break;
		case "special-offers":
			player.getPackets().sendConfig(TAB_CONFIG, 6);
			currentTab = 6;
			break;
		case "limmited-edition":
			player.getPackets().sendConfig(TAB_CONFIG, 7);
			currentTab = 7;
			break;
		case "favorites":
			player.getPackets().sendConfig(TAB_CONFIG, 8);
			currentTab = 8;
			break;
		case "effects":
			player.getPackets().sendConfig(TAB_CONFIG, 9);
			currentTab = 9;
			break;
		default:
			player.getPackets().sendGameMessage(
					"This tab is currently un-available"
							+ (player.getRights() >= 2 ? ": " + "\"" + tab
									+ "\"" : "."));
		}
	}
	
	public static void handleButtons(int componentId, int slotId, int slotId2,
			int packetId, Player player) {
		switch (componentId) {
		case 3:
			openTab("favorites",player);
			break;
		case 103:
			player.getPackets().sendWindowsPane(player.getInterfaceManager().hasResizableScreen() ? 746 : 548, 0);
			break;
		case 1:
			openTab("home",player);
			break;
		case 7:
			openTab("auras",player);
			break;
		case 8:
			openTab("effects",player);
			break;
		case 9:
			openTab("emotes",player);
			break;
		case 10:
			openTab("outfits", player);
			break;
		case 11:
			openTab("titles", player);
			break;
		case 12:
			openTab("recolor", player);
			break;
		case 13:
			openTab("special-offers", player);
			break;
		default:
			player.getPackets().sendGameMessage(
					"This button does not have an action set.");
		}
	}
	
	
	
	
}