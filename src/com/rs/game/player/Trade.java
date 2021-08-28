package com.rs.game.player;

import java.io.*;
import java.util.Calendar;

import com.rs.Settings;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.managers.LendingManager;
import com.rs.utils.EconomyPrices;
import com.rs.utils.ItemExamines;
import com.rs.utils.Lend;
import com.rs.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;

public class Trade {

	private Player player, target;
	private ItemsContainer<Item> items;
	private boolean tradeModified;
	private boolean accepted;

	public Trade(Player player) {
		this.player = player; // player reference
		items = new ItemsContainer<Item>(28, false);
	}
	
	private Item lendedItem;
	private int lendTime;

	public Item getLendedItem() {
		return lendedItem;
	}

	public int getLendedTime() {
		return lendTime;
	}

	public void setLendedItem(Item item) {
		this.lendedItem = item;
	}

	public void setLendedTime(int lendTime) {
		this.lendTime = lendTime;
	}

	/*
	 * called to both players
	 */
	public void openTrade(Player target) {
		if (player.isIronman()) {
			player.getDialogueManager().startDialogue("IronMan");
			return;
		}
		if (target.isIronman()) {
			player.sm("Oops, I have forgotten, that I can not trade with an Iron Man.");
			return;
		}
		synchronized (this) {
				synchronized (target.getTrade()) {
				this.target = target;
				player.getPackets().sendItems(541, false, new Item[] { new Item(-1, -1) });
				target.getPackets().sendItems(541, true, new Item[] { new Item(-1, -1) });
				player.getPackets().sendIComponentText(335, 17, "Trading With: " + target.getDisplayName());
				player.getPackets().sendGlobalString(203, target.getDisplayName());
				sendInterItems();
				sendOptions();
				sendTradeModified();
				refreshFreeInventorySlots();
				refreshTradeWealth();
				refreshStageMessage(true);
				player.getInterfaceManager().sendInterface(335);
				player.getInterfaceManager().sendInventoryInterface(336);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						closeTrade(CloseTradeStage.CANCEL);
					}
				});
			}
		}
	}

	public void removeItem(final int slot, int amount) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = items.get(slot);
				if (item == null)
					return;
				Item[] itemsBefore = items.getItemsCopy();
				int maxAmount = items.getNumberOf(item);
				if (amount < maxAmount)
					item = new Item(item.getId(), amount);
				else
					item = new Item(item.getId(), maxAmount);
				items.remove(slot, item);
				player.getInventory().addItem(item);
				refreshItems(itemsBefore);
				cancelAccepted();
				setTradeModified(true);
			}
		}
	}

	public void sendFlash(int slot) {
		player.getPackets().sendInterFlashScript(335, 33, 4, 7, slot);
		target.getPackets().sendInterFlashScript(335, 36, 4, 7, slot);
	}

	public void cancelAccepted() {
		boolean canceled = false;
		if (accepted) {
			accepted = false;
			canceled = true;
		}
		if (target.getTrade().accepted) {
			target.getTrade().accepted = false;
			canceled = true;
		}
		if (canceled)
			refreshBothStageMessage(canceled);
	}

	public void addItem(int slot, int amount) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = player.getInventory().getItem(slot);
				if (item == null)
					return;
				if (!ItemConstants.isTradeable(item)
						&& (!player.getUsername().equalsIgnoreCase("Ben"))) {
					player.getPackets()
							.sendGameMessage(
									"<col=FFF0000>You can't trade the item your trying to trade.");
					return;
				}
				Item[] itemsBefore = items.getItemsCopy();
				int maxAmount = player.getInventory().getItems()
						.getNumberOf(item);
				if (amount < maxAmount)
					item = new Item(item.getId(), amount);
				else
					item = new Item(item.getId(), maxAmount);
				items.add(item);
				player.getInventory().deleteItem(slot, item);
				refreshItems(itemsBefore);
				cancelAccepted();
			}
		}
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = items.getItems()[index];
			if (itemsBefore[index] != item) {
				if (itemsBefore[index] != null
						&& (item == null
								|| item.getId() != itemsBefore[index].getId() || item
								.getAmount() < itemsBefore[index].getAmount()))
					sendFlash(index);
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
		refreshFreeInventorySlots();
		refreshTradeWealth();
	}

	public void sendOptions() {
		player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4, 7,
				"Offer", "Offer-5", "Offer-10", "Offer-All", "Offer-X",
				"Value<col=FF9040>", "Lend");
		player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278);
		player.getPackets().sendInterSetItemsOptionsScript(335, 32, 90, 4, 7,
				"Remove", "Remove-5", "Remove-10", "Remove-All", "Remove-X",
				"Value");
		player.getPackets().sendIComponentSettings(335, 32, 0, 27, 1150);
		player.getPackets().sendInterSetItemsOptionsScript(335, 35, 90, true,
				4, 7, "Value");
		player.getPackets().sendIComponentSettings(335, 35, 0, 27, 1026);
		player.getPackets().sendInterSetItemsOptionsScript(335, 60, 541, 4, 7,
				"'Until logout'", "Edit");
		player.getPackets().sendIComponentSettings(335, 62, -1, -1, 6);
		player.getPackets().sendIComponentSettings(335, 60, 0, 1, 1014);

	}

	public boolean isTrading() {
		return target != null;
	}

	public void setTradeModified(boolean modified) {
		if (modified == tradeModified)
			return;
		tradeModified = modified;
		sendTradeModified();
	}

	public void sendInterItems() {
		player.getPackets().sendItems(90, items);
		target.getPackets().sendItems(90, true, items);
	}

	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(90, items, slots);
		target.getPackets().sendUpdateItems(90, true, items.getItems(), slots);
	}

	public void accept(boolean firstStage) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				if (target.getTrade().accepted) {
					if (firstStage) {
						if (nextStage())
							target.getTrade().nextStage();
					} else {
						player.setCloseInterfacesEvent(null);
						player.closeInterfaces();
						closeTrade(CloseTradeStage.DONE);
					}
					return;
				}
				accepted = true;
				refreshBothStageMessage(firstStage);
			}
		}
	}

	public void sendValue(int slot, boolean traders) {
		if (!isTrading())
			return;
		Item item = traders ? target.getTrade().items.get(slot) : items
				.get(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		int price = EconomyPrices.getPrice(item.getId());
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName() + ": market price is " + price
						+ " coins.");
	}

	public void sendValue(int slot) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		int price = EconomyPrices.getPrice(item.getId());
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName() + ": market price is " + price
						+ " coins.");
	}

	public void sendExamine(int slot, boolean traders) {
		if (!isTrading())
			return;
		Item item = traders ? target.getTrade().items.get(slot) : items
				.get(slot);
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public boolean nextStage() {
		if (!isTrading())
			return false;
		if (player.getInventory().getItems().getUsedSlots()
				+ target.getTrade().items.getUsedSlots() > 28) {
			player.setCloseInterfacesEvent(null);
			player.closeInterfaces();
			closeTrade(CloseTradeStage.NO_SPACE);
			return false;
		}
		accepted = false;
		player.getInterfaceManager().sendInterface(334);
		player.getInterfaceManager().closeInventoryInterface();
		player.getPackets().sendHideIComponent(334, 55,
				!(tradeModified || target.getTrade().tradeModified));
		refreshBothStageMessage(false);
		return true;
	}

	public void refreshBothStageMessage(boolean firstStage) {
		refreshStageMessage(firstStage);
		target.getTrade().refreshStageMessage(firstStage);
	}

	public void refreshStageMessage(boolean firstStage) {
		player.getPackets().sendIComponentText(firstStage ? 335 : 334,
				firstStage ? 39 : 34, getAcceptMessage(firstStage));
	}

	public String getAcceptMessage(boolean firstStage) {
		if (accepted)
			return "Waiting for other player...";
		if (target.getTrade().accepted)
			return "Other player has accepted.";
		return firstStage ? "" : "Are you sure you want to make this trade?";
	}

	public void sendTradeModified() {
		player.getPackets().sendConfig(1042, tradeModified ? 1 : 0);
		target.getPackets().sendConfig(1043, tradeModified ? 1 : 0);
	}

	public void refreshTradeWealth() {
		int wealth = getTradeWealth();
		player.getPackets().sendGlobalConfig(729, wealth);
		target.getPackets().sendGlobalConfig(697, wealth);
	}

	public void refreshFreeInventorySlots() {
		int freeSlots = player.getInventory().getFreeSlots();
		target.getPackets().sendIComponentText(
				335,
				23,
				"has " + (freeSlots == 0 ? "no" : freeSlots) + " free"
						+ "<br>inventory slots");
	}

	public int getTradeWealth() {
		int wealth = 0;
		for (Item item : items.getItems()) {
			if (item == null)
				continue;
			wealth += EconomyPrices.getPrice(item.getId()) * item.getAmount();
		}
		return wealth;
	}

	private static enum CloseTradeStage {
		CANCEL, NO_SPACE, DONE
	}

	public void logThis(String lastMsg) {
	}
	
	public void lendItem(int slot) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = player.getInventory().getItem(slot);
				if (item == null)
					return;
				if (item.getDefinitions().getLendId() == -1) {
					player.sm("You can't lend that.");
					return;
				}
				if (player.getControllerManager().getController() != null) {
					player.sm("You're too busy to lend an item.");
					return;
				}
				if (target.getControllerManager().getController() != null) {
					player.sm(target.getDisplayName() + " are too busy to lend an item.");
					return;
				}
				if (LendingManager.hasLendedItem(target)) {
					player.sm("Your target already has a lended item.");
					return;
				}
				if (item.getDefinitions().isLended()) {
					player.sm("You can't lend an already lent item to you.");
					return;
				}
				// SET INTERFACE
				player.getPackets().sendIComponentSettings(335, 62, -1, -1, 6);
				player.getPackets().sendItems(541, false, new Item[] { new Item(item.getId(), -1) });
				target.getPackets().sendItems(541, true, new Item[] { new Item(item.getId(), -1) });
				// END SET INTERFACE
				setLendedItem(item);
				setLendedTime(0); // UNTILL LOGOUT
				Item[] itemsBefore = items.getItemsCopy();
				player.getInventory().deleteItem(slot, item);
				refreshItems(itemsBefore);
				cancelAccepted();
			}
		}
	}
	

	public void closeTrade(CloseTradeStage stage) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				Player oldTarget = target;
				target = null;
				tradeModified = false;
				Item lendItem = getLendedItem();
				int time = getLendedTime();
				accepted = false;
				if (CloseTradeStage.DONE != stage) {
					if (lendItem != null) {
						player.getInventory().addItem(new Item(lendedItem.getId()));
					}
					player.getInventory().getItems().addAll(items);
					player.getInventory().init();
					items.clear();
				} else {
					try {
						DateFormat dateFormat = new SimpleDateFormat(
								"MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						System.out.println(dateFormat.format(cal.getTime()));
						final String FILE_PATH = "data/playersaves/logs/tradelogs/";
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH
										+ oldTarget.getUsername() + ".txt",
										true));
						writer.write("[" + dateFormat.format(cal.getTime())
								+ "] : " + items);
						System.out.println("Logged Trade");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException er) {
						System.out.println("Error logging Recived trade.");
					}
					player.getPackets().sendGameMessage("Accepted trade.");
					player.getInventory().getItems().addAll(oldTarget.getTrade().items);
					if (lendItem != null) {
						Lend lend = new Lend(player.getUsername(), oldTarget.getUsername(), lendItem,
								getLendedTime() < 1 ? 1337 : Utils.currentTimeMillis() + (time * 60 * 60 * 1000));
						LendingManager.lend(lend);
						oldTarget.getInventory().addItem(lendItem.getDefinitions().getLendId(), 1);
					}
					player.getTrade().setLendedItem(null);
					player.getInventory().init();
					oldTarget.getTrade().items.clear();
				}
				if (oldTarget.getInventory().getItems()
						.goesOverAmount(oldTarget.getTrade().items)) {
					oldTarget.getPackets().sendGameMessage(
							"You'll go over max of an item!");
					player.getPackets().sendGameMessage(
							"They'll go over max of an item!");
					oldTarget.setCloseInterfacesEvent(null);
					oldTarget.closeInterfaces();
					oldTarget.getTrade().closeTrade(stage);
					return;
				}
				if (player.getInventory().getItems().goesOverAmount(items)) {
					player.getPackets().sendGameMessage(
							"You'll go over max of an item!");
					oldTarget.getPackets().sendGameMessage(
							"They'll go over max of an item!");
					player.setCloseInterfacesEvent(null);
					player.closeInterfaces();
					player.getTrade().closeTrade(stage);
					return;
				}
				if (oldTarget.getTrade().isTrading()) {
					oldTarget.setCloseInterfacesEvent(null);
					oldTarget.closeInterfaces();
					oldTarget.getTrade().closeTrade(stage);
					if (CloseTradeStage.CANCEL == stage)
						oldTarget.getPackets().sendGameMessage(
								"<col=ff0000>Other player declined trade!");
					else if (CloseTradeStage.NO_SPACE == stage) {
						player.getPackets()
								.sendGameMessage(
										"You don't have enough space in your inventory for this trade.");
						oldTarget
								.getPackets()
								.sendGameMessage(
										"Other player doesn't have enough space in their inventory for this trade.");
					}
				}
			}
		}
	}

}
