package com.rs.game.player;

import java.io.Serializable;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.MoneyPouch;
import com.rs.game.player.content.clans.Clan;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.DiscordBot;
import com.rs.utils.ItemExamines;
import com.rs.utils.SerializableFilesManager;

public class Bank implements Serializable {

	private static final long serialVersionUID = 1551246756081236625L;

	private Item[][] bankTabs;
	@SuppressWarnings("unused")
	private short bankPin;
	private int lastX;

	private transient Player player;
	private transient int currentTab;
	private transient Item[] lastContainerCopy;
	private transient boolean withdrawNotes;
	private transient boolean insertItems;

	public static final long MAX_BANK_SIZE = Integer.MAX_VALUE;

	public Bank() {
		bankTabs = new Item[1][0];
	}

	public void removeItem(int id) {
		if (bankTabs != null) {
			for (int i = 0; i < bankTabs.length; i++) {
				for (int i2 = 0; i2 < bankTabs[i].length; i2++) {
					if (bankTabs[i][i2].getId() == id)
						bankTabs[i][i2].setId(0);
				}
			}
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		if (bankTabs == null || bankTabs.length == 0)
			bankTabs = new Item[1][0];
	}

	@SuppressWarnings("null")
	public void setItem(int slotId, int amt) {
		Item item = getItem(slotId);
		if (item == null) {
			item.setAmount(amt);
			refreshItems();
			refreshTabs();
			refreshViewingTab();
		}
	}

	public void refreshTabs() {
		for (int slot = 1; slot < 9; slot++)
			refreshTab(slot);
	}

	public int getTabSize(int slot) {
		if (slot >= bankTabs.length)
			return 0;
		return bankTabs[slot].length;
	}

	public void withdrawLastAmount(int bankSlot) {
		withdrawItem(bankSlot, lastX);
	}

	public void withdrawItemButOne(int fakeSlot) {
		int[] fromRealSlot = getRealSlot(fakeSlot);
		Item item = getItem(fromRealSlot);
		if (item == null)
			return;
		if (item.getAmount() <= 1) {
			player.getPackets().sendGameMessage("You only have one of this item in your bank");
			return;
		}
		withdrawItem(fakeSlot, item.getAmount() - 1);
	}

	public void openPlayerBank(Player victim) {
		if (victim == null) {
			return;
		}
		player.getTemporaryAttributtes().put(Key.VIEWING_BANK, true);
		player.getInterfaceManager().sendInterface(762);
		player.getInterfaceManager().sendInventoryInterface(763);
		player.getPackets().sendHideIComponent(762, 119, true);
		player.getPackets().sendHideIComponent(762, 120, true);
		player.getPackets().sendHideIComponent(762, 15, true);
		player.getPackets().sendHideIComponent(762, 16, true);
		for (int i = 0; i < 3; i++)
			player.getPackets().sendHideIComponent(762, 19 + i, true);
		for (int i = 0; i < 8; i++)
			player.getPackets().sendHideIComponent(762, 33 + i, true);
		player.getPackets().sendIComponentText(762, 47, player.getDisplayName() + "'s Bank");
		player.getPackets().sendItems(95, victim.getBank().getContainerCopy());
		refreshViewingTab();
		refreshTabs();
		unlockButtons();
	}

	public void openPlayerClanBank(Player victim) {
		if (victim == null) {
			return;
		}
		player.getTemporaryAttributtes().put(Key.VIEWING_BANK, true);
		player.getInterfaceManager().sendInterface(762);
		player.getInterfaceManager().sendInventoryInterface(763);
		player.getPackets().sendHideIComponent(762, 119, true);
		player.getPackets().sendHideIComponent(762, 120, true);
		player.getPackets().sendHideIComponent(762, 15, true);
		player.getPackets().sendHideIComponent(762, 16, true);
		for (int i = 0; i < 3; i++)
			player.getPackets().sendHideIComponent(762, 19 + i, true);
		for (int i = 0; i < 8; i++)
			player.getPackets().sendHideIComponent(762, 33 + i, true);
		player.getPackets().sendIComponentText(762, 47, player.getClanManager().getClan().getClanName() + "'s Bank");
		player.getPackets().sendItems(95, victim.getClanManager().getClan().getClanBank().getContainerCopy());
		refreshViewingTab();
		refreshTabs();
		unlockButtons();
	}

	public boolean containsItem(int itemId, int amount) {
		for (int i = 0; i < bankTabs.length; i++) {
			for (Item item : bankTabs[i]) {
				if (item.getId() == itemId && item.getAmount() >= amount) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsItem(int id) {
		if (bankTabs != null) {
			for (int i = 0; i < bankTabs.length; i++) {
				for (int i2 = 0; i2 < bankTabs[i].length; i2++) {
					if (bankTabs[i][i2].getId() == id)
						return true;
				}
			}
		}
		return false;
	}

	public void depositLastAmount(int bankSlot) {
		depositItem(bankSlot, lastX, true);
	}

	@SuppressWarnings("rawtypes")
	public ItemsContainer getItems() {
		return null;
	}

	public void depositAllInventory(boolean banking) {
		if (Bank.MAX_BANK_SIZE - getBankSize() < player.getInventory().getItems().getSize()) {
			player.getPackets().sendGameMessage("Not enough space in your bank.");
			return;
		}
		for (int i = 0; i < 28; i++)
			depositItem(i, Integer.MAX_VALUE, false);
		refreshTab(currentTab);
		refreshItems();
	}

	public void depositAllBob(boolean banking) {
		Familiar familiar = player.getFamiliar();
		if (familiar == null || familiar.getBob() == null)
			return;
		int space = addItems(familiar.getBob().getBeastItems().getItems(), banking);
		if (space != 0) {
			for (int i = 0; i < space; i++)
				familiar.getBob().getBeastItems().set(i, null);
			familiar.getBob().sendInterItems();
		}
		if (space < familiar.getBob().getBeastItems().getSize()) {
			player.getPackets().sendGameMessage("Not enough space in your bank.");
			return;
		}
	}

	public void depositAllEquipment(boolean banking) {
		int space = addItems(player.getEquipment().getItems().getItems(), banking);
		if (space != 0) {
			for (int i = 0; i < space; i++)
				player.getEquipment().getItems().set(i, null);
			player.getEquipment().init();
			player.getAppearence().generateAppearenceData();
		}
		if (space < player.getEquipment().getItems().getSize()) {
			player.getPackets().sendGameMessage("Not enough space in your bank.");
			return;
		}
	}

	public void collapse(int tabId) {
		if (tabId == 0 || tabId >= bankTabs.length)
			return;
		Item[] items = bankTabs[tabId];
		for (Item item : items)
			removeItem(getItemSlot(item.getId()), item.getAmount(), false, true);
		for (Item item : items)
			addItem(item.getId(), item.getAmount(), 0, false);
		refreshTabs();
		refreshItems();
	}

	public void switchItem(int fromSlot, int toSlot, int fromComponentId, int toComponentId) {
		if (toSlot == 65535) {
			int toTab = toComponentId >= 76 ? 8 - (84 - toComponentId) : 9 - ((toComponentId - 46) / 2);
			if (toTab < 0 || toTab > 9)
				return;
			if (bankTabs.length == toTab) {
				int[] fromRealSlot = getRealSlot(fromSlot);
				if (fromRealSlot == null)
					return;
				if (toTab == fromRealSlot[0]) {
					switchItem(fromSlot, getStartSlot(toTab));
					return;
				}
				Item item = getItem(fromRealSlot);
				if (item == null)
					return;
				removeItem(fromSlot, item.getAmount(), false, true);
				createTab();
				bankTabs[bankTabs.length - 1] = new Item[] { item };
				refreshTab(fromRealSlot[0]);
				refreshTab(toTab);
				refreshItems();
			} else if (bankTabs.length > toTab) {
				int[] fromRealSlot = getRealSlot(fromSlot);
				if (fromRealSlot == null)
					return;
				if (toTab == fromRealSlot[0]) {
					switchItem(fromSlot, getStartSlot(toTab));
					return;
				}
				Item item = getItem(fromRealSlot);
				if (item == null)
					return;
				boolean removed = removeItem(fromSlot, item.getAmount(), false, true);
				if (!removed)
					refreshTab(fromRealSlot[0]);
				else if (fromRealSlot[0] != 0 && toTab >= fromRealSlot[0])
					toTab -= 1;
				refreshTab(fromRealSlot[0]);
				addItem(item.getId(), item.getAmount(), toTab, true);
			}
		} else
			switchItem(fromSlot, toSlot);
	}

	public void switchItem(int fromSlot, int toSlot) {
		int[] fromRealSlot = getRealSlot(fromSlot);
		Item fromItem = getItem(fromRealSlot);
		if (fromItem == null)
			return;
		int[] toRealSlot = getRealSlot(toSlot);
		Item toItem = getItem(toRealSlot);
		if (toItem == null)
			return;
		bankTabs[fromRealSlot[0]][fromRealSlot[1]] = toItem;
		bankTabs[toRealSlot[0]][toRealSlot[1]] = fromItem;
		refreshTab(fromRealSlot[0]);
		if (fromRealSlot[0] != toRealSlot[0])
			refreshTab(toRealSlot[0]);
		refreshItems();
	}

	public void insertItem(int fromSlot, int tab, int index) {
		if (index == fromSlot)
			return;
		Item[] bankTab = new Item[bankTabs[tab].length];
		for (int i = 0; i < bankTabs[tab].length; i++) {
			if ((i < index || (i > index && i > fromSlot)) && index < fromSlot)
				bankTab[i] = bankTabs[tab][i];
			else if ((i >= index || i < fromSlot) && fromSlot < index)
				bankTab[i] = bankTabs[tab][i];
			else if (i == (fromSlot < index ? index - 1 : index))
				bankTab[i] = bankTabs[tab][fromSlot];
			else
				bankTab[i] = bankTabs[tab][fromSlot < index ? i + 1 : i - 1];
		}
		bankTabs[tab] = bankTab;
		refreshItems();
		refreshTabs();
		refreshViewingTab();
	}

	public void openSetPin() { // TODO

	}

	public void openDepositBox() {
		player.getInterfaceManager().sendInterface(11);
		player.getInterfaceManager().closeInventory();
		player.getInterfaceManager().closeEquipment();
		final int lastGameTab = player.getInterfaceManager().openGameTab(9); // friends
		// tab
		sendBoxInterItems();
		player.getPackets().sendIComponentText(11, 13, "Bank Of " + Settings.SERVER_NAME + " - Deposit Box");
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				player.getInterfaceManager().sendInventory();
				player.getInventory().unlockInventoryOptions();
				player.getInterfaceManager().sendEquipment();
				player.getInterfaceManager().openGameTab(lastGameTab);
			}
		});
	}

	public void sendBoxInterItems() {
		player.getPackets().sendInterSetItemsOptionsScript(11, 17, 93, 6, 5, "Deposit-1", "Deposit-5", "Deposit-10",
				"Deposit-All", "Deposit-X", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(11, 17, 0, 27, 0, 1, 2, 3, 4, 5);
	}

	public void openBank2() {
		switch (player.getBankId()) {
		case 0:
			if (player.hasClanBank()) {
				player.getClanManager().getClan().getClanBank().openBank(player);
				player.getTemporaryAttributtes().remove(Key.VIEWING_BANK);
			}
			return;
		default:
			openBank();
			player.getTemporaryAttributtes().remove(Key.VIEWING_BANK);
		}
	}

	public void openBank() {
		player.setBankId(1);
		player.getTemporaryAttributtes().remove("clanBank");
		if (player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().getTutorialStage() < 21) {
			player.getDialogueManager().startDialogue("SimpleNPCMessage", 947,
					"You should come talk to me first before messing with your bank.");
			return;
		}
		if (player.hasPin && !player.enteredPin) {
			player.getPackets().sendRunScript(108, new Object[] { "Please Enter Your PIN" });
			player.getTemporaryAttributtes().put("Write_pin", Boolean.TRUE);
		} else {
			player.getPackets().sendConfig(638, -1);
			player.getPackets().sendConfig(638, 0);
			player.getInterfaceManager().sendInterface(762);
			player.getInterfaceManager().sendInventoryInterface(763);
			sendBankSize(player);
			refreshViewingTab();
			refreshTabs();
			unlockButtons();
			sendItems();
			refreshLastX();
		}
		if (!player.hasPin) {
			player.getPackets().sendGameMessage("Consider yourself about adding a security pin for your bank to keep them protected.");
		}
		if (player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().tutorialStage == 21) {
			player.getStatistics().tutorialStage = 22;
			DiccusTutorial.addPrayerHintIcon(player);
			player.getDialogueManager().startDialogue("SimpleNPCMessage", 947,
					"You're nearly finished. The Prayer instructor would like to have a chat with you in the nearby church.");
		}
	}

	public static void RemovePin(Player player, int value, boolean haspin) {
		if (haspin) {
			if (player.getPin() != value) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You failed, the digits doesn't not match your current PIN.");
			} else {
				if (player.getPin() == value) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You've removed your PIN.");
					player.hasPin = false;
				}
			}
		}
	}

	public static void WritePin(Player player, int value, boolean pinned) {
		if (pinned) {
			if (player.getPin() != value) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You failed, the digits doesn't not match your current PIN.");
			} else {
				if (player.getPin() == value) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You entered your PIN correctly, you can now access to your bank & money pouch.");
					player.enteredPin = true;
				}
			}
		}
	}

	public void refreshLastX() {
		player.getPackets().sendConfig(1249, lastX);
	}

	public void createTab() {
		int slot = bankTabs.length;
		Item[][] tabs = new Item[slot + 1][];
		System.arraycopy(bankTabs, 0, tabs, 0, slot);
		tabs[slot] = new Item[0];
		bankTabs = tabs;
	}

	public void destroyTab(int slot) {
		Item[][] tabs = new Item[bankTabs.length - 1][];
		System.arraycopy(bankTabs, 0, tabs, 0, slot);
		System.arraycopy(bankTabs, slot + 1, tabs, slot, bankTabs.length - slot - 1);
		bankTabs = tabs;
		if (currentTab != 0 && currentTab >= slot)
			currentTab--;
	}

	public boolean hasBankSpace() {
		return getBankSize() < MAX_BANK_SIZE;
	}

	public void withdrawItem(int[] slots, int quantity) {
		if (quantity < 1)
			return;
		if (slots == null)
			return;
		Item item = getItem(slots);
		if (item == null)
			return;
		if (item.getAmount() < quantity)
			item = new Item(item.getId(), item.getAmount());
		else
			item = new Item(item.getId(), quantity);
		boolean noted = false;
		ItemDefinitions defs = item.getDefinitions();
		if (withdrawNotes) {
			if (!defs.isNoted() && defs.getCertId() != -1) {
				item.setId(defs.getCertId());
				noted = true;
			} else
				player.getPackets().sendGameMessage("You cannot withdraw this item as a note.");
		}
		if (noted || defs.isStackable()) {
			if (player.getInventory().getItems().containsOne(item)) {
				int slot = player.getInventory().getItems().getThisItemSlot(item);
				Item invItem = player.getInventory().getItems().get(slot);
				if (invItem.getAmount() + item.getAmount() <= 0) {
					item.setAmount(Integer.MAX_VALUE - invItem.getAmount());
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
				}
			} else if (!player.getInventory().hasFreeSlots()) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (freeSlots == 0) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
			if (freeSlots < item.getAmount()) {
				item.setAmount(freeSlots);
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
			}
		}
		removeItem(slots, item.getAmount(), true, false);
		player.getInventory().addItemMoneyPouch(item);
	}

	public void withdrawItem(int bankSlot, int quantity) {
		if (quantity < 1)
			return;
		Item item = getItem(getRealSlot(bankSlot));
		if (item == null)
			return;
		if (item.getAmount() < quantity)
			item = new Item(item.getId(), item.getAmount());
		else
			item = new Item(item.getId(), quantity);
		boolean noted = false;
		ItemDefinitions defs = item.getDefinitions();
		if (item.getDefinitions().isLended()) {
			if (Wilderness.isAtDitch(player) || player.getControllerManager().getController() instanceof FfaZone) {
				player.getPackets().sendGameMessage("You can't withdraw lendable items at this area.");
				return;
			}
		}
		if (withdrawNotes) {
			if (!defs.isNoted() && defs.getCertId() != -1) {
				item.setId(defs.getCertId());
				noted = true;
			} else
				player.getPackets().sendGameMessage("You cannot withdraw this item as a note.");
		}
		if (noted || defs.isStackable()) {
			if (player.getInventory().getItems().containsOne(item)) {
				int slot = player.getInventory().getItems().getThisItemSlot(item);
				Item invItem = player.getInventory().getItems().get(slot);
				if (invItem.getAmount() + item.getAmount() <= 0) {
					item.setAmount(Integer.MAX_VALUE - invItem.getAmount());
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
			} else if (!player.getInventory().hasFreeSlots()) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
		} else {// um idk try now
			int freeSlots = player.getInventory().getFreeSlots();
			if (freeSlots == 0) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
			if (freeSlots < item.getAmount()) {
				item.setAmount(freeSlots);
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
			}
		}
		removeItem(bankSlot, item.getAmount(), true, false);
		if (Settings.DISCORD) {
			DiscordBot.sendBankWithdrawl(player, item);
		}
		player.getInventory().addItem(item);
	}

	public void sendExamine(int fakeSlot) {
		int[] slot = getRealSlot(fakeSlot);
		if (slot == null)
			return;
		Item item = bankTabs[slot[0]][slot[1]];
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void depositItem(int invSlot, int quantity, boolean refresh) {
		if (quantity < 1 || invSlot < 0 || invSlot > 27)
			return;
		Item item = player.getInventory().getItem(invSlot);
		if (item == null)
			return;
		int amt = player.getInventory().getItems().getNumberOf(item);
		if (amt < quantity)
			item = new Item(item.getId(), amt);
		else
			item = new Item(item.getId(), quantity);
		ItemDefinitions defs = item.getDefinitions();
		int originalId = item.getId();
		if (defs.isNoted() && defs.getCertId() != -1)
			item.setId(defs.getCertId());
		Item bankedItem = getItem(item.getId());
		if (item.getId() == 995) {
			int currentAmount = item.getAmount();
			int bankedAmount = bankedItem != null ? bankedItem.getAmount() : 0;
			long total = (long) currentAmount + (long) bankedAmount;
			if (total > Integer.MAX_VALUE) {
				player.getPackets().sendGameMessage("You cannot bank that much cash...");
				return;
			}
		}
		if (bankedItem != null) {
			if (bankedItem.getAmount() + item.getAmount() <= 0) {
				item.setAmount(Integer.MAX_VALUE - bankedItem.getAmount());
				player.getPackets().sendGameMessage("Not enough space in your bank.");
			} else if (bankedItem.getAmount() + item.getAmount() >= Integer.MAX_VALUE) {
				player.getPackets().sendGameMessage("You can't bank that " + item.getName());
				return;// should work
			}
		} else if (!hasBankSpace()) {
			player.getPackets().sendGameMessage("Not enough space in your bank.");
			return;
		}
		player.getInventory().deleteItem(invSlot, new Item(originalId, item.getAmount()));
		addItem(item, refresh);
	}

	public void addItem(Item item, boolean refresh) {
		addItem(item.getId(), item.getAmount(), refresh);
		if (Settings.DISCORD) {
			DiscordBot.sendBankDeposit(player, item);
		}
	}

	public int addItems(Item[] items, boolean refresh) {
		int space = (int) (MAX_BANK_SIZE - getBankSize());
		if (space != 0) {
			space = (space < items.length ? space : items.length);
			for (int i = 0; i < space; i++) {
				if (items[i] == null)
					continue;
				addItem(items[i], false);
			}
			if (refresh) {
				refreshTabs();
				refreshItems();
			}
		}
		return space;
	}

	public void addItem(int id, int quantity, boolean refresh) {
		addItem(id, quantity, currentTab, refresh);
	}

	public void addItem(int id, int quantity, int creationTab, boolean refresh) {
		int[] slotInfo = getItemSlot(id);
		if (slotInfo == null) {
			if (creationTab >= bankTabs.length)
				creationTab = bankTabs.length - 1;
			if (creationTab < 0) // fixed now, alex
				creationTab = 0;
			int slot = bankTabs[creationTab].length;
			Item[] tab = new Item[slot + 1];
			System.arraycopy(bankTabs[creationTab], 0, tab, 0, slot);
			tab[slot] = new Item(id, quantity);
			bankTabs[creationTab] = tab;
			if (refresh)
				refreshTab(creationTab);
		} else {
			Item item = bankTabs[slotInfo[0]][slotInfo[1]];
			bankTabs[slotInfo[0]][slotInfo[1]] = new Item(item.getId(), item.getAmount() + quantity);
		}
		if (refresh)
			refreshItems();
	}

	public boolean removeItem(int fakeSlot, int quantity, boolean refresh, boolean forceDestroy) {
		return removeItem(getRealSlot(fakeSlot), quantity, refresh, forceDestroy);
	}

	public boolean removeItem(int[] slot, int quantity, boolean refresh, boolean forceDestroy) {
		if (slot == null)
			return false;
		Item item = bankTabs[slot[0]][slot[1]];
		boolean destroyed = false;
		if (quantity >= item.getAmount()) {
			if (bankTabs[slot[0]].length == 1 && (forceDestroy || bankTabs.length != 1)) {
				destroyTab(slot[0]);
				if (refresh)
					refreshTabs();
				destroyed = true;
			} else {
				Item[] tab = new Item[bankTabs[slot[0]].length - 1];
				System.arraycopy(bankTabs[slot[0]], 0, tab, 0, slot[1]);
				System.arraycopy(bankTabs[slot[0]], slot[1] + 1, tab, slot[1], bankTabs[slot[0]].length - slot[1] - 1);
				bankTabs[slot[0]] = tab;
				if (refresh)
					refreshTab(slot[0]);
			}
		} else
			bankTabs[slot[0]][slot[1]] = new Item(item.getId(), item.getAmount() - quantity);
		if (refresh)
			refreshItems();
		return destroyed;
	}

	public Item getItem(int id) {
		for (int slot = 0; slot < bankTabs.length; slot++) {
			for (Item item : bankTabs[slot])
				if (item.getId() == id)
					return item;
		}
		return null;
	}

	public int[] getItemSlot(int id) {
		for (int tab = 0; tab < bankTabs.length; tab++) {
			for (int slot = 0; slot < bankTabs[tab].length; slot++)
				if (bankTabs[tab][slot].getId() == id)
					return new int[] { tab, slot };
		}
		return null;
	}

	public Item getItem(int[] slot) {
		if (slot == null)
			return null;
		return bankTabs[slot[0]][slot[1]];
	}

	public int getStartSlot(int tabId) {
		int slotId = 0;
		for (int tab = 1; tab < (tabId == 0 ? bankTabs.length : tabId); tab++)
			slotId += bankTabs[tab].length;

		return slotId;

	}

	public int[] getRealSlot(int slot) {
		for (int tab = 1; tab < bankTabs.length; tab++) {
			if (slot >= bankTabs[tab].length)
				slot -= bankTabs[tab].length;
			else
				return new int[] { tab, slot };
		}
		if (slot >= bankTabs[0].length)
			return null;
		return new int[] { 0, slot };
	}

	public void refreshViewingTab() {
		player.getPackets().sendConfigByFile(4893, currentTab + 1);
	}

	public void refreshTab(int slot) {
		if (slot == 0)
			return;
		player.getPackets().sendConfigByFile(4885 + (slot - 1), getTabSize(slot));
	}

	public void sendItems() {
		player.getPackets().sendItems(95, getContainerCopy());
	}

	public void refreshItems(int[] slots) {
		player.getPackets().sendUpdateItems(95, getContainerCopy(), slots);
	}

	public Item[] getContainerCopy() {
		if (lastContainerCopy == null)
			lastContainerCopy = generateContainer();
		return lastContainerCopy;
	}

	public void refreshItems() {
		refreshItems(generateContainer(), getContainerCopy());
	}

	public void refreshItems(Item[] itemsAfter, Item[] itemsBefore) {
		if (itemsBefore.length != itemsAfter.length) {
			lastContainerCopy = itemsAfter;
			sendItems();
			return;
		}
		int[] changedSlots = new int[itemsAfter.length];
		int count = 0;
		for (int index = 0; index < itemsAfter.length; index++) {
			if (itemsBefore[index] != itemsAfter[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		lastContainerCopy = itemsAfter;
		refreshItems(finalChangedSlots);
	}

	public int getBankSize() {
		int size = 0;
		for (int i = 0; i < bankTabs.length; i++)
			size += bankTabs[i].length;
		return size;
	}

	public void depositAllMoneyPouch(boolean banking) {
		int coinsInPouch = player.getMoneyPouch().getCoinsAmount();
		int coinsToDeposit = coinsInPouch;
		int space = 0;
		Item coinsInBank = getItem(995);
		int coinsCanPutIn = coinsInBank == null ? Integer.MAX_VALUE : Integer.MAX_VALUE - coinsInBank.getAmount();
		if (coinsInBank != null) {
			if (coinsCanPutIn < coinsToDeposit) {
				coinsToDeposit = (int) (coinsCanPutIn);
				player.sm("Not enough space in your bank.");
			}
		}
		if (coinsToDeposit != 0) {
			space = addItems(new Item[] { new Item(995, coinsToDeposit) }, banking);
			if (space != 0) {
				if (space < 1) {
					player.sm("Not enough space in your bank.");
					return;
				}
				player.getMoneyPouch().sendDynamicInteraction(coinsToDeposit, true, MoneyPouch.TYPE_REMOVE);
			}
		}
	}

	public Item[] generateContainer() {
		Item[] container = new Item[getBankSize()];
		int count = 0;
		for (int slot = 1; slot < bankTabs.length; slot++) {
			System.arraycopy(bankTabs[slot], 0, container, count, bankTabs[slot].length);
			count += bankTabs[slot].length;
		}
		System.arraycopy(bankTabs[0], 0, container, count, bankTabs[0].length);
		return container;
	}

	public void unlockButtons() {
		player.getPackets().sendIComponentSettings(762, 95, 0, 516, 2622718);
		player.getPackets().sendIComponentSettings(763, 0, 0, 27, 2425982);
	}

	private int collectableItem;

	public void addCollectableItem(int collectableItem) {
		this.collectableItem = collectableItem;
	}

	public int getCollectableItem() {
		return collectableItem;
	}

	public void switchWithdrawNotes() {
		withdrawNotes = !withdrawNotes;
	}

	public void switchInsertItems() {
		insertItems = !insertItems;
		player.getPackets().sendConfig(305, insertItems ? 1 : 0);
	}

	public void setCurrentTab(int currentTab) {
		if (currentTab >= bankTabs.length)
			return;
		this.currentTab = currentTab;
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
	}

	public void showBank(Item[] containerCopy) {
		if (containerCopy == null) {
			return;
		}
		player.getVarBitManager().sendVar(638, -1);
		player.getVarBitManager().sendVar(638, 0);
		player.getInterfaceManager().sendInterface(762);
		player.getPackets().sendHideIComponent(762, 119, true);
		player.getPackets().sendHideIComponent(762, 120, true);
		for (int i = 0; i < 8; i++)
			player.getPackets().sendHideIComponent(762, 33 + i, true);
		for (int i = 0; i < 6; i++)
			player.getPackets().sendHideIComponent(762, 15 + i, true);
		player.getPackets().sendHideIComponent(762, 46, true);
		player.getInterfaceManager().sendInventoryInterface(763);
		player.getPackets().sendItems(95, containerCopy);
		refreshViewingTab();
		refreshTabs();
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public boolean getInsertItems() {
		return insertItems;
	}

	public boolean getWithdrawNotes() {
		return withdrawNotes;
	}

	public boolean hasAnotherBank() {
		boolean canAccessClanBank = player.getClanManager() != null && player.getClanManager().getClan() != null;
		return canAccessClanBank;
	}

	public void switchBank() {
		if (hasAnotherBank()) {
			switch (player.getBankId()) {
			case 0:// bank 1
				openBank();
				return;
			case 1:// clan bank
				if (player.hasClanBank()) {
					player.getClanManager().getClan().getClanBank().openBank(player);
				}
				return;
			}
		}
	}

	public void sendBankSize(Player player) {
		player.getPackets().sendHideIComponent(762, 21, true);
		player.getPackets().sendHideIComponent(762, 41, true);
	}

	public static boolean handleButtons(Player player, int interfaceId, int componentId, int slotId, int packetId) {
		if (interfaceId == 762) {
			if (player.getTemporaryAttributtes().get(Key.VIEWING_BANK) != null) {
				return false;
			}
			if (player.getTemporaryAttributtes().get("clanBank") != null
					&& player.getTemporaryAttributtes().get("clanBank") == Boolean.TRUE) {
				if (player.getClanManager() == null || player.getClanManager().getClan() == null)
					return true;
				if (player.getClanManager().getClan().getMemberByName(player.getUsername()) != null) {
					if (player.getClanManager().getClan().getMemberByName(player.getUsername())
							.getRank() > Clan.RECRUIT) {
						if (componentId == 15) {
						}
						if (componentId == 19) {
							player.getBank().switchWithdrawNotes();
						}
						if (componentId == 33) {
							player.getClanManager().getClan().getClanBank().depositAllInventory(player, true);
						}
						if (componentId == 37) {
							player.getClanManager().getClan().getClanBank().depositAllEquipment(player, true);
						}
						if (componentId == 39) {
							player.getClanManager().getClan().getClanBank().depositAllBob(player, true);
						}
						if (componentId == 35) {
							player.getClanManager().getClan().getClanBank().depositAllMoneyPouch(player, true);
						}
						if (componentId == 46) {
							player.getBank().switchBank();
						}
						if (componentId >= 46 && componentId <= 64) {
							int tabId = 9 - ((componentId - 46) / 2);
							if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
								player.getBank().setCurrentTab(tabId);
							else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
								player.getBank().collapse(tabId);
						} else if (componentId == 95) {
							if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
								player.getClanManager().getClan().getClanBank().withdrawItem(player, slotId, 1);
							else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
								player.getClanManager().getClan().getClanBank().withdrawItem(player, slotId, 5);
							else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
								player.getClanManager().getClan().getClanBank().withdrawItem(player, slotId, 10);
							else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
								player.getClanManager().getClan().getClanBank(player).withdrawLastAmount(player,
										slotId);
							else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
								player.getTemporaryAttributtes().put("clanbank_item_X_Slot", slotId);
								player.getTemporaryAttributtes().put("clanbank_isWithdraw", Boolean.TRUE);
								player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
							} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
								player.getClanManager().getClan().getClanBank(player).withdrawItemButOne(player,
										slotId);
							else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
								player.getClanManager().getClan().getClanBank().withdrawItem(player, slotId,
										Integer.MAX_VALUE);
							else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
								player.getClanManager().getClan().getClanBank().sendExamine(player, slotId);
						} else if (componentId == 119) {
							if (player.getTemporaryAttributtes().get("viewingOtherBank") != null
									&& (Boolean) player.getTemporaryAttributtes().get("viewingOtherBank"))
								return true;
							Equipment.openEquipmentBonuses(player, true);
						}
						SerializableFilesManager.saveClan(player.getClanManager().getClan());
						return true;
					}
				} else {
					player.sm("You must be a higher rank than Recruit to withdraw from the clan bank.");
					return true;
				}
			}
			if (componentId == 15)
				player.getBank().switchInsertItems();
			else if (componentId == 19)
				player.getBank().switchWithdrawNotes();
			else if (componentId == 33)
				player.getBank().depositAllInventory(true);
			else if (componentId == 37)
				player.getBank().depositAllEquipment(true);
			else if (componentId == 39)
				player.getBank().depositAllBob(true);
			else if (componentId == 35)
				player.getBank().depositAllMoneyPouch(true);
			else if (componentId == 46) {
				player.getBank().switchBank();
			} else if (componentId >= 46 && componentId <= 64) {
				int tabId = 9 - ((componentId - 46) / 2);
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().setCurrentTab(tabId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().collapse(tabId);
			} else if (componentId == 95) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().withdrawItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().withdrawItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getBank().withdrawItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getBank().withdrawLastAmount(slotId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
					player.getBank().withdrawItemButOne(slotId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getBank().sendExamine(slotId);

			} else if (componentId == 119) {
				Equipment.openEquipmentBonuses(player, true);
			}
			return true;
		}

		// Bank Deposit
		if (interfaceId == 763) {
			if (player.getTemporaryAttributtes().get(Key.VIEWING_BANK) != null) {
				return false;
			}
			if (player.getTemporaryAttributtes().get("clanBank") != null
					&& player.getTemporaryAttributtes().get("clanBank") == Boolean.TRUE) {
				if (componentId == 0) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getClanManager().getClan().getClanBank().depositItem(player, slotId, 1, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getClanManager().getClan().getClanBank().depositItem(player, slotId, 5, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getClanManager().getClan().getClanBank().depositItem(player, slotId, 10, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getClanManager().getClan().getClanBank().depositLastAmount(player, slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("clanbank_item_X_Slot", slotId);
						player.getTemporaryAttributtes().remove("clanbank_isWithdraw");
						player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
						player.getClanManager().getClan().getClanBank().depositItem(player, slotId, Integer.MAX_VALUE,
								true);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
						player.getInventory().sendExamine(slotId);
					}
				}
			} else {
				if (componentId == 0) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getBank().depositItem(slotId, 1, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getBank().depositItem(slotId, 5, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getBank().depositItem(slotId, 10, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getBank().depositLastAmount(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
						player.getTemporaryAttributtes().remove("bank_isWithdraw");
						player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
						player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
						player.getInventory().sendExamine(slotId);
					}
				}
				return true;
			}
		}
		return false;
	}

}
