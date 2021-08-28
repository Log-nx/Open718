package com.rs.game.player.managers;

import java.util.concurrent.ConcurrentHashMap;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.npc.Drop;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.interfaces.RSInterface;
import com.rs.game.player.content.interfaces.SophanemChestInterface;
import com.rs.game.player.content.teleports.TeleportSystem;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;

public class InterfaceManager {

	public static final int FIXED_WINDOW_ID = 548;
	public static final int RESIZABLE_WINDOW_ID = 746;
	public static final int CHAT_BOX_TAB = 13;
	public static final int FIXED_SCREEN_TAB_ID = 27;
	public static final int RESIZABLE_SCREEN_TAB_ID = 28;
	public static final int FIXED_INV_TAB_ID = 166; 
	public static final int RESIZABLE_INV_TAB_ID = 108;
	private Player player;
	

	private final ConcurrentHashMap<Integer, int[]> openedinterfaces = new ConcurrentHashMap<Integer, int[]>();

	private boolean resizableScreen;
	private int windowsPane;
	private SophanemChestInterface chestInterface;
	
	public InterfaceManager(Player player) {
		this.player = player;
	}
	
	public void sendInterface(SophanemChestInterface sophanemChestInterface) {
		if (sophanemChestInterface == null) {
			System.out.println("Null interface has attempted to open > ");
			return;
		}
		this.chestInterface = sophanemChestInterface;
		sendInterface(SophanemChestInterface.getId());
		sophanemChestInterface.open();		
	}

	public void sendTab(int tabId, int interfaceId) {
		player.getPackets().sendInterface(true, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, tabId, interfaceId);
	}

	public void sendChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, CHAT_BOX_TAB, interfaceId);
	}

	public void closeChatBoxInterface() {
		player.getPackets().closeInterface(CHAT_BOX_TAB);
	}

	public void sendOverlay(int interfaceId, boolean fullScreen) {
		sendTab(resizableScreen ? fullScreen ? 1 : 11 : 0, interfaceId);
	}
	
	public void closeOverlay(boolean fullScreen) {
		player.getPackets().closeInterface(resizableScreen ? fullScreen ? 1 : 11 : 0);
	}
	
	public void sendInterface(int interfaceId) {
		player.getPackets().sendInterface(false, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID, interfaceId);
	}

	public void sendInventoryInterface(int childId) {
		player.getPackets().sendInterface(false, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID, childId);
	}

	public final void sendInterfaces() {
		if (player.getDisplayMode() == 2 || player.getDisplayMode() == 3) {
			resizableScreen = true;
			sendFullScreenInterfaces();
		} else {
			resizableScreen = false;
			sendFixedInterfaces();
		}
		player.getSkills().sendInterfaces();
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getMusicsManager().unlockMusicPlayer();
		player.getEmotesManager().unlockEmotesBook();
		player.getInventory().unlockInventoryOptions();
		player.getPrayer().unlockPrayerBookButtons();
		ClansManager.unlockBanList(player);
		if (player.getFamiliar() != null && player.isRunning())
			player.getFamiliar().unlockOrb();
		player.getControllerManager().sendInterfaces();
	}

	public void replaceRealChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, 11, interfaceId);
	}

	public void closeReplacedRealChatBoxInterface() {
		player.getPackets().closeInterface(752, 11);
	}

	public void sendWindowPane() {
		player.getPackets().sendWindowsPane(resizableScreen ? 746 : 548, 0);
	}
	
	public void sendFullScreenInterfaces() {
		player.getPackets().sendWindowsPane(746, 0);
		player.getPackets().sendIComponentText(746, 17, "Owner: BigFuckinChungus");
		sendTab(21, 752);
		sendTab(22, 751);
		sendTab(15, 745);
		sendTab(25, 754);
		sendTab(195, 748); 
		sendTab(196, 749);
		sendTab(197, 750);
		sendTab(198, 747); 
		player.getPackets().sendInterface(true, 752, 9, 137);
		sendCombatStyles();
		sendTaskSystem();
		sendSkills();
		sendPlayerCustom();
		//sendTab(114, 190);
		sendInventory();
		sendEquipment();
		sendPrayerBook();
		sendMagicBook();
		sendTab(120, 550); // friend list
		sendTab(121, 1109); // 551 ignore now friendchat
		sendTab(122, 1110); // 589 old clan chat now new clan chat
		sendSettings();
		sendEmotes();
		sendTab(125, 187); // music
		sendTab(126, 34); // notes
		sendTab(129, 182); // logout*/
		sendSquealOfFortuneTab();
	}
	
	public void sendFixedInterfaces() {
		player.getPackets().sendWindowsPane(548, 0);
		sendTab(161, 752);
		sendTab(37, 751);
		sendTab(23, 745);
		sendTab(25, 754);
		sendTab(155, 747); 
		sendTab(151, 748);
		sendTab(152, 749);
		sendTab(153, 750);
		player.getPackets().sendInterface(true, 752, 9, 137);
		player.getPackets().sendInterface(true, 548, 9, 167);
		sendMagicBook();
		sendPrayerBook();
		sendEquipment();
		sendInventory();
		//sendPlayerCustom();
		sendTab(174, 190);//quest
		sendTab(181, 1109);// 551 ignore now friendchat
		sendTab(182, 1110);// 589 old clan chat now new clan chat
		sendTab(180, 550);// friend list
		sendTab(185, 187);// music
		sendTab(186, 34); // notes
		sendTab(189, 182);
		sendSkills();
		sendEmotes();
		sendSettings();
		sendTaskSystem();
		sendCombatStyles();
		sendSquealOfFortuneTab();
	}

	public void sendSquealOfFortuneTab() {
		player.getSquealOfFortune().sendSpinCounts();
		sendTab(resizableScreen ? 119 : 179, 1139);
		player.getPackets().sendGlobalConfig(823, 1);
	}
	
	public void sendSquealOverlay() {
		setWindowInterface(resizableScreen ? 11 : 14, 1252);
	}

	public void closeSquealOfFortuneTab() {
		removeWindowInterface(resizableScreen ? 119 : 179);
		player.getPackets().sendGlobalConfig(823, 0);
	}
	
	public void sendSquealOfFortune() {
		sendTab(resizableScreen ? 119 : 179, 1139);
		player.getPackets().sendGlobalConfig(823, 1);
	}

	public void setWindowInterface(int componentId, int interfaceId) {
		player.getPackets().sendInterface(true, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, componentId, interfaceId);
	}
	
	public void closeSquealOfFortune() {
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? 40 : 200);
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? 41 : 201);
	}
	
	public void sendPlayerCustom() {
		sendTab(resizableScreen ? 114 : 174, 506);
		player.getPackets().sendIComponentText(506, 0, "Playtime: " + player.getStatistics().getPlayPoints() + "m");
		player.getPackets().sendIComponentText(506, 4, "Account");
		player.getPackets().sendIComponentText(506, 6, "Teleports");
		player.getPackets().sendIComponentText(506, 8, "Commands");
		player.getPackets().sendIComponentText(506, 10, "Shops");
		player.getPackets().sendIComponentText(506, 18, "Donator Panel");
		player.getPackets().sendIComponentText(506, 12, "Drop Logs");
		player.getPackets().sendIComponentText(506, 14, "Perks");
		player.getPackets().sendIComponentText(506, 16, "Donator Info");
		if (player.isOwner()) {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <img=1><shad=FFFF00>Owner</shad>");
		} else if (player.isAdministrator()) {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <img=1><shad=FFFF00>Admin</shad>");
		} else if (player.isModerator()) {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <img=0><shad=D8D8D8>Mod</shad>");
		} else if (player.isIronman()) {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <img=11><shad=D8D8D8>Ironman</shad>");
		} else if (player.getDonationManager().isDonator()) {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <shad=D8D8D8>Donator</shad>");
		} else if (player.getDonationManager().isExtremeDonator()) {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <shad=D8D8D8>Donator</shad>");
		} else if (player.getDonationManager().isDivineDonator()) {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <shad=D8D8D8>Donator</shad>");
		} else {
			player.getPackets().sendIComponentText(506, 2, "Diccus Staff<br>Rank: <col=ffffff>Player");
		}
	}
	
	public void sendTimerInterface(Player player) {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? 10 : 8, 3000);
		player.getPackets().sendIComponentText(3000, 5, "");
	    player.getPackets().sendIComponentText(3000, 6, "");
		player.getPackets().sendIComponentText(3000, 7, "");
		player.getPackets().sendIComponentText(3000, 8, "");
		player.getPackets().sendIComponentText(3000, 9, "");
		}
	
	public void sendXPPopup() {
		sendTab(resizableScreen ? 38 : 10, 1213); // xp
	}

	public void sendXPDisplay() {
		sendXPDisplay(1215); // xp counter
	}

	public void sendXPDisplay(int interfaceId) {
		sendTab(resizableScreen ? 27 : 29, interfaceId); // xp counter
	}

	public void closeXPPopup() {
		player.getPackets().closeInterface(resizableScreen ? 38 : 10);
	}

	public void closeXPDisplay() {
		player.getPackets().closeInterface(resizableScreen ? 27 : 29);
	}
	
	public void sendEquipment() {
		sendTab(resizableScreen ? 116 : 176, 387);
	}
	
	public void closeInterface(int one, int two) {
		player.getPackets().closeInterface(resizableScreen ? two : one);
	}

	public void closeEquipment() {
		player.getPackets().closeInterface(resizableScreen ? 116 : 176);
	}

	public void sendInventory() {
		sendTab(resizableScreen ? 115 : 175, Inventory.INVENTORY_INTERFACE);
	}

	public void closeInventory() {
		player.getPackets().closeInterface(resizableScreen ? 115 : 175);
	}
	
	public void closeQuests() {
		player.getPackets().closeInterface(resizableScreen ? 114 : 174);
	}
	
	public void closeSkills() {
		player.getPackets().closeInterface(resizableScreen ? 113 : 206);
	}
	
	public void closeCombatStyles() {
		player.getPackets().closeInterface(resizableScreen ? 111 : 204);
	}
	
	public void closeTaskSystem() {
		player.getPackets().closeInterface(resizableScreen ? 112 : 205);
	}
	
	public void closeFriendsList() {
		player.getPackets().closeInterface(resizableScreen ? 120 : 550);
	}
	
	public void closeFriendsChat() {
		player.getPackets().closeInterface(resizableScreen ? 121 : 1109);
	}
	
	public void closeClanChat() {
		player.getPackets().closeInterface(resizableScreen ? 122 : 1110);
	}
	
	public void closeSettings() {
		player.getPackets().closeInterface(resizableScreen ? 123 : 1111);
	}
	
	public void closeMusic() {
		player.getPackets().closeInterface(resizableScreen ? 125 : 187);
	}
	
	public void closeNotes() {
		player.getPackets().closeInterface(resizableScreen ? 126 : 34);
	}
	
	public void sendCombatStyles() {
		sendTab(resizableScreen ? 111 : 171, 884);
	}
	
	public void sendTaskSystem() {
        sendTab(resizableScreen ? 112 : 172, 1019);
        player.getPackets().sendIComponentText(1019, 3,  "Player support");
        player.getPackets().sendIComponentText(1019, 16,  "Report Bug");
        player.getPackets().sendIComponentText(1019, 18,  "Submit Ticket");
        player.getPackets().sendIComponentText(1019, 11,  " ");
        player.getPackets().sendIComponentText(1019, 0,  "Report any possible bug(s) you have found on " + Settings.SERVER_NAME +"");
        player.getPackets().sendIComponentText(1019, 8,  "Submit help-request ticket to online staff member ");
    }
	
	public void sendWealth() {
		long moneyPouch = player.getMoneyPouch().getCoinsAmount();
		long bankValue = player.getBankValue();
		long inventoryValue = player.getInventoryValue();
		long equipmentValue = player.getEquipment().getEquipmentValue();
		long totalValue = 0;
		long grandexchangeValue = GrandExchange.getTotalOfferValues(player);
		long collectionValue = GrandExchange.getTotalCollectionValue(player);
		totalValue = bankValue + inventoryValue + equipmentValue + moneyPouch + collectionValue + grandexchangeValue;
		player.closeInterfaces();
		player.getInterfaceManager().sendInterface(629);
		player.getPackets().sendIComponentText(629, 11, "Information Tab");
		player.getPackets().sendIComponentText(629, 12, "");
		player.getPackets().sendIComponentText(629, 41, "Money pouch:");
		player.getPackets().sendIComponentText(629, 54, Utils.formatNumber(moneyPouch));
		player.getPackets().sendIComponentText(629, 42, "Bank:");
		player.getPackets().sendIComponentText(629, 55, Utils.formatNumber(bankValue));
		player.getPackets().sendIComponentText(629, 43, "Inventory:");
		player.getPackets().sendIComponentText(629, 56, Utils.formatNumber(inventoryValue));
		player.getPackets().sendIComponentText(629, 44, "Equipment:");
		player.getPackets().sendIComponentText(629, 57, Utils.formatNumber(equipmentValue));
		player.getPackets().sendIComponentText(629, 45, "Grand Exchange");
		player.getPackets().sendIComponentText(629, 58, "");
		player.getPackets().sendIComponentText(629, 46, "Pending Offers:");
		player.getPackets().sendIComponentText(629, 59, Utils.formatNumber(grandexchangeValue));
		player.getPackets().sendIComponentText(629, 47, "Collection Box:");
		player.getPackets().sendIComponentText(629, 60, Utils.formatNumber(collectionValue));
		player.getPackets().sendIComponentText(629, 48, "Total wealth:");
		player.getPackets().sendIComponentText(629, 61, Utils.formatNumber(totalValue));
		player.getPackets().sendIComponentText(629, 49, "");
		player.getPackets().sendIComponentText(629, 62, "");
		player.getPackets().sendIComponentText(629, 50, "Highest value Wildy kill:");
		player.getPackets().sendIComponentText(629, 63, "");
		player.getPackets().sendIComponentText(629, 51, "Total boss kills:");
		player.getPackets().sendIComponentText(629, 64, "");
		player.getPackets().sendIComponentText(629, 52, "Slayer tasks completed:");
		player.getPackets().sendIComponentText(629, 65, "");
		player.getPackets().sendIComponentText(629, 68, "Close");
		player.getPackets().sendHideIComponent(629, 69, true);
	}
	
	public void sendSkills() {
		sendTab(resizableScreen ? 113 : 173, 320);
	}

	public void sendSettings() {
		sendSettings(261);
	}

	public void sendSettings(int interfaceId) {
		sendTab(resizableScreen ? 123 : 183, interfaceId);
	}

	public void sendPrayerBook() {
		sendTab(resizableScreen ? 117 : 177, 271);
	}
	
	public void closePrayerBook() {
		player.getPackets().closeInterface(resizableScreen ? 117 : 210);
	}


	public void sendMagicBook() {
		sendTab(resizableScreen ? 118 : 178, player.getCombatDefinitions().getSpellBook());
	}
	
	public void sendGodwars() {
		player.getPackets().sendIComponentText(601, 8, "" + player.armadyl);
		player.getPackets().sendIComponentText(601, 9, "" + player.bandos);
		player.getInterfaceManager().sendOverlay(601, true);
	}
	
	public void closeMagicBook() {
		player.getPackets().closeInterface(resizableScreen ? 118 : 211);
	}
	
	public void sendEmotes() {
		sendTab(resizableScreen ? 124 : 184, 590);
	}
	
	public void closeEmotes() {
		player.getPackets().closeInterface(resizableScreen ? 124 : 217);
	}

	public boolean addInterface(int windowId, int tabId, int childId) {
		if (openedinterfaces.containsKey(tabId))
			player.getPackets().closeInterface(tabId);
		openedinterfaces.put(tabId, new int[] { childId, windowId });
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public boolean containsInterface(int tabId, int childId) {
		if (childId == windowsPane)
			return true;
		if (!openedinterfaces.containsKey(tabId))
			return false;
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public int getTabWindow(int tabId) {
		if (!openedinterfaces.containsKey(tabId))
			return FIXED_WINDOW_ID;
		return openedinterfaces.get(tabId)[1];
	}

	public boolean containsInterface(int childId) {
		if (childId == windowsPane)
			return true;
		for (int[] value : openedinterfaces.values())
			if (value[0] == childId)
				return true;
		return false;
	}

	public boolean containsTab(int tabId) {
		return openedinterfaces.containsKey(tabId);
	}

	public void removeAll() {
		openedinterfaces.clear();
	}

	public boolean containsScreenInter() {
		return containsTab(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	}

	public void closeScreenInterface() {
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	}

	public boolean containsInventoryInter() {
		return containsTab(resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}

	public void closeInventoryInterface() {
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}

	public boolean containsChatBoxInter() {
		return containsTab(CHAT_BOX_TAB);
	}

	public boolean removeTab(int tabId) {
		return openedinterfaces.remove(tabId) != null;
	}

	public boolean removeInterface(int tabId, int childId) {
		if (!openedinterfaces.containsKey(tabId))
			return false;
		if (openedinterfaces.get(tabId)[0] != childId)
			return false;
		return openedinterfaces.remove(tabId) != null;
	}

	public void sendFadingInterface(int backgroundInterface) {
		if (hasResizableScreen()) 
			player.getPackets().sendInterface(true, RESIZABLE_WINDOW_ID, 12,backgroundInterface);
		else
			player.getPackets().sendInterface(true, FIXED_WINDOW_ID, 11,backgroundInterface);
	}
	
	public void closeFadingInterface() {
		if (hasResizableScreen()) 
			player.getPackets().closeInterface(12);
		else
			player.getPackets().closeInterface(11);
	}
	
	public void sendScreenInterface(int backgroundInterface, int interfaceId) {
		player.getInterfaceManager().closeScreenInterface();

		if (hasResizableScreen()) {
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 40, backgroundInterface);
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 41, interfaceId);
		} else {
			player.getPackets().sendInterface(false, FIXED_WINDOW_ID, 200, 	backgroundInterface);
			player.getPackets().sendInterface(false, FIXED_WINDOW_ID, 201, interfaceId);
			
		}
		

		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				if (hasResizableScreen()) {
					player.getPackets().closeInterface(40);
					player.getPackets().closeInterface(41);
				} else {
					player.getPackets().closeInterface(200);
					player.getPackets().closeInterface(201);
				}
			}
		});
	}

	public void setWindowsPane(int windowsPane) {
		this.windowsPane = windowsPane;
	}

	public int getWindowsPane() {
		return windowsPane;
	}
	
	public void gazeOrbOfOculus() {
		player.getPackets().sendWindowsPane(475, 0);
		player.getPackets().sendInterface(true, 475, 57, 751);
		player.getPackets().sendInterface(true, 475, 55, 752);
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				player.getPackets().sendWindowsPane(player.getInterfaceManager().hasResizableScreen() ? 746 : 548, 0);
				player.getPackets().sendResetCamera();
			}
			
		});
	}
	public boolean hasResizableScreen() {
		return resizableScreen;
	}
	/*
	 * returns lastGameTab
	 */
	public int openGameTab(int tabId) {
		player.getPackets().sendGlobalConfig(168, tabId);
		int lastTab = 4; // tabId
		// tab = tabId;
		return lastTab;
	}
	 public void removeScreenInterface() {
		removeWindowInterface(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	 }
		    
	 public void removeWindowInterface(int componentId) {
		removeInterface(resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, componentId);
	 }
		    
			public void sendItemDrops(ItemDefinitions defs) {
				int i = 0;
				String dropEntry = "";
				player.getInterfaceManager().sendInterface(275);
				player.getPackets().sendIComponentText(275, 1, "Drops: <col=9900FF><shad=000000>" + defs.name + "</col></shad>");
				for (i = 10; i < 310; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}
				i = 0;
				for (int n = 0; n < Utils.getNPCDefinitionsSize(); n++) {
					NPCDefinitions def = NPCDefinitions.getNPCDefinitions(n);
					Drop[] drops = NPCDrops.getDrops(def.getId());
					if (drops != null) {
					for (Drop drop : drops) {
						if (drop.getItemId() == 0)
							continue;
						ItemDefinitions itemDefs = ItemDefinitions.getItemDefinitions(drop.getItemId());
						if (itemDefs.getId() != defs.getId() || !itemDefs.name.contains(defs.name) || !itemDefs.name.equalsIgnoreCase(defs.name))
							continue;
						StringBuilder sb = new StringBuilder("").append(def.name).append(": ").append(itemDefs.name).append(drop.getMaxAmount() == 1 ? 
						("") : drop.getMinAmount() == drop.getMaxAmount() ? (" (" + drop.getMaxAmount() + ")") : (" (" + drop.getMinAmount() + "-" + drop.getMaxAmount() + ")"))
						.append(" {").append((int) drop.getRate()).append("% Chance}");
						dropEntry = sb.toString();
						if (i < 300)
						player.getPackets().sendIComponentText(275, 10 + i, dropEntry);
						dropEntry = "";
						i++;
						}
					}
				}
			}
			
			public void sendNPCDrops(NPCDefinitions defs) {
				int i = 0;
				String dropEntry = "";
				player.getInterfaceManager().sendInterface(275);
				player.getPackets().sendIComponentText(275, 1, "Drops: <col=9900FF><shad=000000>" + defs.name + "</col></shad>");
				for (i = 10; i < 310; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}
				i = 0;
				Drop[] drops = NPCDrops.getDrops(defs.getId());
				if (drops != null) {
				for (Drop drop : drops) {
					if (drop.getItemId() == 0)
						continue;
					ItemDefinitions itemDefs = ItemDefinitions.getItemDefinitions(drop.getItemId());
					StringBuilder sb = new StringBuilder("").append(itemDefs.name)
							.append(drop.getMaxAmount() == 1 ? 
									("") : drop.getMinAmount() == drop.getMaxAmount() ? (" (" + drop.getMaxAmount() + ")") :
										(" (" + drop.getMinAmount() + "-" + drop.getMaxAmount() + ")"))
							.append(" {").append((int) drop.getRate()).append("% Chance}");
					dropEntry = sb.toString();
					if (i < 300)
					player.getPackets().sendIComponentText(275, 10 + i, dropEntry);
					dropEntry = "";
					i++;
					}
				}
			}

			public void sendCountDown() {
				player.getInterfaceManager().sendOverlay(57, false);
				player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? 34 : 0, 57);
				player.getPackets().sendIComponentText(1362, 22, "Assassin Exp: " + (int) player.getSkills().getAssassinXp(Skills.ASSASSIN) + "");
			}
			
		    public void closeSquealOverlay() {
		    	player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 11 : 0);
		    }

			public void sendNPCTeleports() {
				TeleportSystem.removeAttributes(player, "NPCTeleports");
				TeleportSystem.sendInterface(player, 72);
			}
			public void sendLowLevelTrainingTeleports() {
				TeleportSystem.removeAttributes(player, "LowLevelTrainingTeleports");
				player.getPackets().sendIComponentText(72, 55, "Low-Level Training Locations");
				player.getPackets().sendIComponentText(72, 31, "Lumbridge Cows");
				player.getPackets().sendIComponentText(72, 32, "Lumbridge Catacombs");
				player.getPackets().sendIComponentText(72, 33, "Lumbridge Chickens");
				player.getPackets().sendIComponentText(72, 34, "Ice Mountain Dwarfs");
				player.getPackets().sendIComponentText(72, 35, "Stronghold of Safety");
				player.getPackets().sendIComponentText(72, 36, "Stronghold of Security");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendMediumLevelTrainingTeleports() {
				TeleportSystem.removeAttributes(player, "MediumLevelTrainingTeleports");
				player.getPackets().sendIComponentText(72, 55, "Medium-Level Training Locations");
				player.getPackets().sendIComponentText(72, 31, "Yaks");
				player.getPackets().sendIComponentText(72, 32, "Icefiends");
				player.getPackets().sendIComponentText(72, 33, "Brimhaven Dungeon");
				player.getPackets().sendIComponentText(72, 34, "Waterfall Dungeon");
				player.getPackets().sendIComponentText(72, 35, "Tzhaar City");
				player.getPackets().sendIComponentText(72, 36, "Chaos Dwarf Battleground");
				player.getPackets().sendIComponentText(72, 37, "Taverly Dungeon");
				player.getPackets().sendIComponentText(72, 38, "Living Rock Cavern");
				player.getPackets().sendIComponentText(72, 39, "Grotworm Lair");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendHighLevelTrainingTeleports() {
				TeleportSystem.removeAttributes(player, "HighLevelTrainingTeleports");
				player.getPackets().sendIComponentText(72, 55, "High-Level Training Locations");
				player.getPackets().sendIComponentText(72, 31, "Ancient Cavern");
				player.getPackets().sendIComponentText(72, 32, "Forinthry Dungeon");
				player.getPackets().sendIComponentText(72, 33, "Asgarnian Dungeon");
				player.getPackets().sendIComponentText(72, 34, "Monastery of Ascension");
				player.getPackets().sendIComponentText(72, 35, "");
				player.getPackets().sendIComponentText(72, 36, "");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendLowLevelSlayerTeleports() {
				TeleportSystem.removeAttributes(player, "LowLevelSlayerTeleports");
				player.getPackets().sendIComponentText(72, 55, "Low-Level Slayer Locations");
				player.getPackets().sendIComponentText(72, 31, "Slayer Tower");
				player.getPackets().sendIComponentText(72, 32, "Taverly Slayer Dungeon");
				player.getPackets().sendIComponentText(72, 33, "Lumbridge Swamp Caves");
				player.getPackets().sendIComponentText(72, 34, "Fremennik Slayer Dungeon");
				player.getPackets().sendIComponentText(72, 35, "Back");
				player.getPackets().sendIComponentText(72, 36, "Mudskipper Point");
				player.getPackets().sendIComponentText(72, 37, "Tarn's Lair");
				player.getPackets().sendIComponentText(72, 38, "Braindeath Island");
				player.getPackets().sendIComponentText(72, 39, "Desert Lizards");
				player.getPackets().sendIComponentText(72, 40, "Next Page");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendLowLevelSlayerTeleportsPage2() {
				TeleportSystem.removeAttributes(player, "LowLevelSlayerTeleportsPage2");
				player.getPackets().sendIComponentText(72, 55, "Medium-Level Slayer Locations");
				player.getPackets().sendIComponentText(72, 31, "Meiyerditch Dungeon");
				player.getPackets().sendIComponentText(72, 32, "Phoenix Lair");
				player.getPackets().sendIComponentText(72, 33, "Chaos Tunnels");
				player.getPackets().sendIComponentText(72, 34, "Poison Waste Slayer Dungeon");
				player.getPackets().sendIComponentText(72, 35, "Zanaris");
				player.getPackets().sendIComponentText(72, 36, "Mos Le'Harmless Caves");
				player.getPackets().sendIComponentText(72, 37, "Pollnivneach Slayer Dungeon");
				player.getPackets().sendIComponentText(72, 38, "Smoke Dungeon");
				player.getPackets().sendIComponentText(72, 39, "Automatrons");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendMinigameTeleports() {
				TeleportSystem.removeAttributes(player, "MinigameTeleports");
				player.getPackets().sendIComponentText(72, 55, "Minigame Locations");
				player.getPackets().sendIComponentText(72, 31, "Fight Caves");
				player.getPackets().sendIComponentText(72, 32, "Fight Kiln");
				player.getPackets().sendIComponentText(72, 33, "Fight Pits");
				player.getPackets().sendIComponentText(72, 34, "Pest Control");
				player.getPackets().sendIComponentText(72, 35, "Back");
				player.getPackets().sendIComponentText(72, 36, "Dominion Tower");
				player.getPackets().sendIComponentText(72, 37, "Stealing Creation");
				player.getPackets().sendIComponentText(72, 38, "Soul Wars");
				player.getPackets().sendIComponentText(72, 39, "Clan Wars");
				player.getPackets().sendIComponentText(72, 40, "Next Page");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendMinigameTeleportsPage2() {
				TeleportSystem.removeAttributes(player, "MinigameTeleportsPage2");
				player.getPackets().sendIComponentText(72, 55, "Minigame Locations Page - 2");
				player.getPackets().sendIComponentText(72, 31, "Barrows");
				player.getPackets().sendIComponentText(72, 32, "Rise of The Six");
				player.getPackets().sendIComponentText(72, 33, "Fist of Guthix");
				player.getPackets().sendIComponentText(72, 34, "Troll Invasion");
				player.getPackets().sendIComponentText(72, 35, "Runespan");
				player.getPackets().sendIComponentText(72, 36, "Duel Arena");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendMetalDragonTeleports() {
				TeleportSystem.removeAttributes(player, "MetalDragonTeleports");
				player.getPackets().sendIComponentText(72, 55, "Metal Dragons");
				player.getPackets().sendIComponentText(72, 31, "Bronze Dragons");
				player.getPackets().sendIComponentText(72, 32, "Iron Dragons");
				player.getPackets().sendIComponentText(72, 33, "Steel Dragons");
				player.getPackets().sendIComponentText(72, 34, "Mithril Dragons");
				player.getPackets().sendIComponentText(72, 35, "Adamant Dragons");
				player.getPackets().sendIComponentText(72, 36, "Rune Dragons");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendDragonTeleports() {
				TeleportSystem.removeAttributes(player, "DragonTeleports");
				player.getPackets().sendIComponentText(72, 55, "Dragon Types");
				player.getPackets().sendIComponentText(72, 31, "Chromatic Dragons");
				player.getPackets().sendIComponentText(72, 32, "Metal Dragons");
				player.getPackets().sendIComponentText(72, 33, "Gemstone Dragons");
				player.getPackets().sendIComponentText(72, 34, "Frost Dragons");
				player.getPackets().sendIComponentText(72, 35, "Celestial Dragons");
				player.getPackets().sendIComponentText(72, 36, "");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendChromaticDragonTeleports() {
				TeleportSystem.removeAttributes(player, "ChromaticDragonTeleports");
				player.getPackets().sendIComponentText(72, 55, "Chromatic Dragons");
				player.getPackets().sendIComponentText(72, 31, "Green Dragons");
				player.getPackets().sendIComponentText(72, 32, "Blue Dragons");
				player.getPackets().sendIComponentText(72, 33, "Red Dragons");
				player.getPackets().sendIComponentText(72, 34, "Black Dragons");
				player.getPackets().sendIComponentText(72, 35, "");
				player.getPackets().sendIComponentText(72, 36, "");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendLowLevelBossTeleports() {
				TeleportSystem.removeAttributes(player, "LowLevelBossTeleports");
				player.getPackets().sendIComponentText(72, 55, "Low-Level Boss Locations");
				player.getPackets().sendIComponentText(72, 31, "King Black Dragon");
				player.getPackets().sendIComponentText(72, 32, "Giant Mole");
				player.getPackets().sendIComponentText(72, 33, "Bork");
				player.getPackets().sendIComponentText(72, 34, "Kalphite Queen");
				player.getPackets().sendIComponentText(72, 35, "Chaos Elemental <col=FF0000>(Wilderness)");
				player.getPackets().sendIComponentText(72, 36, "Evil Chicken");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendStrykewyrmTeleports() {
				TeleportSystem.removeAttributes(player, "WyrmTeleports");
				player.getPackets().sendIComponentText(72, 55, "Strykewyrm Locations");
				player.getPackets().sendIComponentText(72, 31, "Jungle Strykewyrm");
				player.getPackets().sendIComponentText(72, 32, "Desert Strykewyrm");
				player.getPackets().sendIComponentText(72, 33, "Ice Strykewyrm");
				player.getPackets().sendIComponentText(72, 34, "");
				player.getPackets().sendIComponentText(72, 35, "");
				player.getPackets().sendIComponentText(72, 36, "");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendMediumLevelBossTeleports() {
				TeleportSystem.removeAttributes(player, "MediumLevelBossTeleports");
				player.getPackets().sendIComponentText(72, 55, "Medium-Level Boss Locations");
				player.getPackets().sendIComponentText(72, 31, "Queen Black Dragon");
				player.getPackets().sendIComponentText(72, 32, "Dagannoth Kings");
				player.getPackets().sendIComponentText(72, 33, "Corporeal Beast");
				player.getPackets().sendIComponentText(72, 34, "Tormented Demons");
				player.getPackets().sendIComponentText(72, 35, "");
				player.getPackets().sendIComponentText(72, 36, "");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendHighLevelBossTeleports() {
				TeleportSystem.removeAttributes(player, "HighLevelBossTeleports");
				player.getPackets().sendIComponentText(72, 55, "High-Level Boss Locations");
				player.getPackets().sendIComponentText(72, 31, "Agoroth");
				player.getPackets().sendIComponentText(72, 32, "Nex");
				player.getPackets().sendIComponentText(72, 33, "Wildy Wyrm");
				player.getPackets().sendIComponentText(72, 34, "Kalphite King");
				player.getPackets().sendIComponentText(72, 35, "Araxxi");
				player.getPackets().sendIComponentText(72, 36, "Vorago");
				player.getPackets().sendIComponentText(72, 37, "God Wars");
				player.getPackets().sendIComponentText(72, 38, "God Wars 2");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendHighLevelSlayerTeleports() {
				TeleportSystem.removeAttributes(player, "HighLevelSlayerTeleports");
				player.getPackets().sendIComponentText(72, 55, "High-Level Slayer Locations");
				player.getPackets().sendIComponentText(72, 31, "Kuradal's Dungeon");
				player.getPackets().sendIComponentText(72, 32, "Cradle - Muspah");
				player.getPackets().sendIComponentText(72, 33, "Pit - Nihils");
				player.getPackets().sendIComponentText(72, 34, "Strykewyrm Locations");
				player.getPackets().sendIComponentText(72, 35, "Back");
				player.getPackets().sendIComponentText(72, 36, "Jadinko Lair");
				player.getPackets().sendIComponentText(72, 37, "Monastery of Ascension");
				player.getPackets().sendIComponentText(72, 38, "Polypore Dungeon");
				player.getPackets().sendIComponentText(72, 39, "Nightmares");
				player.getPackets().sendIComponentText(72, 40, "Next Page");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendHighLevelSlayerTeleportsPage2() {
				TeleportSystem.removeAttributes(player, "HighLevelSlayerTeleportsPage2");
				player.getPackets().sendIComponentText(72, 55, "High-Level Slayer Locations Page - 2");
				player.getPackets().sendIComponentText(72, 31, "Aminishi");
				player.getPackets().sendIComponentText(72, 32, "Abbey Mine");
				player.getPackets().sendIComponentText(72, 33, "Camel Warriors Island");
				player.getPackets().sendIComponentText(72, 34, "Mammoth Iceberg");
				player.getPackets().sendIComponentText(72, 35, "Dragon Locations");
				player.getPackets().sendIComponentText(72, 36, "");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}
			public void sendDonationZoneTeleports() {
				TeleportSystem.removeAttributes(player, "DonationZoneTeleports");
				player.getPackets().sendIComponentText(72, 55, "Donation Zone Locations");
				player.getPackets().sendIComponentText(72, 31, "Regular Donation Zone");
				player.getPackets().sendIComponentText(72, 32, "Divine Donation Zone");
				player.getPackets().sendIComponentText(72, 33, "");
				player.getPackets().sendIComponentText(72, 34, "");
				player.getPackets().sendIComponentText(72, 35, "");
				player.getPackets().sendIComponentText(72, 36, "");
				player.getPackets().sendIComponentText(72, 37, "");
				player.getPackets().sendIComponentText(72, 38, "");
				player.getPackets().sendIComponentText(72, 39, "");
				player.getPackets().sendIComponentText(72, 40, "Back");
				player.getInterfaceManager().sendInterface(72);
			}

			public void sendAssassin() {
				boolean res = hasResizableScreen();
				player.getPackets().sendGlobalConfig(168, 8);
				player.getPackets().sendInterface(true, res ? 746 : 548, res ? 119 : 179, 1362);
				player.getPackets().sendIComponentText(1362, 6, "Assassins Level: " + player.getSkills().getAssassinLevel(Skills.ASSASSIN) + "");
				player.getPackets().sendIComponentText(1362, 15, "Assassin Call");
				player.getPackets().sendIComponentText(1362, 16, "" + player.getSkills().getAssassinLevel(Skills.ASSASSIN_CALL) + "");
				player.getPackets().sendIComponentText(1362, 41, "Final Blow");
				player.getPackets().sendIComponentText(1362, 42, "" + player.getSkills().getAssassinLevel(Skills.FINAL_BLOW) + "");
				player.getPackets().sendIComponentText(1362, 55, "Swift Speed");
				player.getPackets().sendIComponentText(1362, 56, "" + player.getSkills().getAssassinLevel(Skills.SWIFT_SPEED) + "");
				player.getPackets().sendIComponentText(1362, 69, "Stealth Moves");
				player.getPackets().sendIComponentText(1362, 70, "" + player.getSkills().getAssassinLevel(Skills.STEALTH_MOVES) + "");
				player.getPackets().sendIComponentText(1362, 22, "Assassin Exp: " + (int) player.getSkills().getAssassinXp(Skills.ASSASSIN) + "");
			}

			public static boolean handleFrameInterface(Player player, int interfaceId, int componentId, int packetId) {
				if (interfaceId == 548 || interfaceId == 746) {
					if (componentId == 207 || componentId == 159) {
						if (packetId == 14) {
							player.getPackets().sendRunScript(5557, 1);
							player.getPackets().sendRunScript(5560, player.getMoneyPouch().getCoinsAmount(), "n");
						}
						if (packetId == 67) {
							if (player.hasPin && !player.enteredPin) {
								player.getPackets().sendRunScript(108, new Object[] { "Please Enter Your PIN" });
								player.getTemporaryAttributtes().put("Write_pin", Boolean.TRUE);
							} else {
								player.getMoneyPouch().withdrawPouch();
							}
						}
						if (packetId == 5) {
							player.sm("Your money pouch currently holds: <col=FF0000>"+Utils.formatNumber(player.getMoneyPouch().getCoinsAmount())+"</col> coins.");
						}
						if (packetId == 55) {
							if (player.getInterfaceManager().containsScreenInter()) {
								player.getPackets().sendGameMessage("Please finish what you're doing before opening the price checker.");
								return true;
							}
							player.stopAll();
							player.getPriceCheckManager().openPriceCheck();
						}
					}
					if (componentId == 17 || componentId == 54) {
						if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
							player.getSkills().switchXPDisplay();
						} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
							player.getSkills().switchXPPopup();
						} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
							player.getSkills().setupXPCounter();
						}
					}
					
					if (componentId == 148 || componentId == 199) {
						if (player.getInterfaceManager().containsScreenInter() || player.getInterfaceManager().containsInventoryInter()) {
							player.getPackets().sendGameMessage("Please finish what you're doing before opening the world map.");
							return true;
						}
						player.getPackets().sendWindowsPane(755, 0);
						int posHash = player.getX() << 14 | player.getY();
						player.getPackets().sendGlobalConfig(622, posHash);
						player.getPackets().sendGlobalConfig(674, posHash);
					
					}
				}
				return false;
			}

			public void closeInterfaces() {
				player.getInterfaceManager().closeCombatStyles();
				player.getInterfaceManager().closeEmotes();
				player.getInterfaceManager().closeMagicBook();
				player.getInterfaceManager().closePrayerBook();
				player.getInterfaceManager().closeEquipment();
				player.getInterfaceManager().closeSkills();
				player.getInterfaceManager().closeSquealOfFortuneTab();
				player.getInterfaceManager().closeTaskSystem();
				player.getInterfaceManager().closeInventory();
				player.getInterfaceManager().closeQuests();
				player.getInterfaceManager().closeFriendsList();
				player.getInterfaceManager().closeClanChat();
				player.getInterfaceManager().closeFriendsChat();
				player.getInterfaceManager().closeMusic();
				player.getInterfaceManager().closeNotes();
				player.getInterfaceManager().closeSettings();
			}

			public boolean containsBankInterface() {
				player.getInterfaceManager().containsInterface(762);
				return false;
			}
}
