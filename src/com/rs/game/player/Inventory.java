package com.rs.game.player;

import java.io.Serializable;
import java.util.List;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.item.WeightManager;
import com.rs.game.player.controllers.Wilderness;
import com.rs.utils.ItemExamines;
import com.rs.utils.Utils;

public final class Inventory implements Serializable {

	private static final long serialVersionUID = 8842800123753277093L;

	private ItemsContainer<Item> items;

	private transient Player player;
    private transient double inventoryWeight;

	public static final int INVENTORY_INTERFACE = 679;

	public Inventory() {
		items = new ItemsContainer<Item>(28, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		player.getPackets().sendItems(93, items);
	}

	public void unlockInventoryOptions() {
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, 4554126);
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 28, 55, 2097152);
	}

	public void reset() {
		items.reset();
		init(); // as all slots reseted better just send all again
	}

	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(93, items, slots);
	}
	
	public boolean addItemMoneyPouch(int id, int amount) {
		if (id == 995)
			return player.getMoneyPouch().sendDynamicInteraction(amount, false);
		return addItem(new Item(id, amount));
	}

	public boolean addItemMoneyPouch(Item item) {
		if (item.getId() == 995)
			return player.getMoneyPouch().sendDynamicInteraction(item.getAmount(), false);
		return addItem(item);
	}

	public void addItemMoneyPouchDrop(int id, int amt) {
		addItemMoneyPouchDrop(new Item(id, amt));
	}

	public boolean addItemMoneyPouchDrop(Item item) {
		if (item.getId() == 995)
			return player.getMoneyPouch().sendDynamicInteraction(item.getAmount(), false);
		return addItemDrop(item.getId(), item.getAmount());
	}
	
	public boolean removeItemMoneyPouch(int id, int amount) {
		if (id == 995)
			return player.getMoneyPouch().sendDynamicInteraction(amount, true);
		return removeItems(new Item(id, amount));
	}
	
	public boolean removeItemMoneyPouch(Item item) {
		if (item.getId() == 995)
			return player.getMoneyPouch().sendDynamicInteraction(item.getAmount(), true);
		return removeItems(item);
	}
	
	public boolean addItemDrop(int itemId, int amount) {
		return addItemDrop(itemId, amount, new WorldTile(player));
	}

	public boolean addItemDrop(int itemId, int amount, WorldTile tile) {
		if (itemId < 0 || amount < 0 || !Utils.itemExists(itemId) || !player.getControllerManager().canAddInventoryItem(itemId, amount))
			return false;
		if (itemId == 995)
			return player.getMoneyPouch().sendDynamicInteraction(amount, false);
		final Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(new Item(itemId, amount)))
			World.addGroundItem(new Item(itemId, amount), tile);
		else
			refreshItems(itemsBefore);
		return true;
	}

    public boolean addItem(int itemId, int amount) {
		if (itemId < 0 || amount < 0 || !Utils.itemExists(itemId) || !player.getControllerManager().canAddInventoryItem(itemId, amount)) {
			return false;
		}
		Item[] itemsBefore = items.getItemsCopy();
		int amount2 = getNumberOf(itemId);
		int amount3 = Integer.MAX_VALUE - amount2;
		boolean coinsToPouch = true;
		if (itemId == 995) {
		    if (player.getInventory().getNumberOf(995) + amount < 0) {
			    player.sendMessage("Inventory full. To make more room, sell, drop or bank something.");
			    return true;
		    }
		    if (!items.add(new Item(itemId, amount))) {
				items.add(new Item(itemId, items.getFreeSlots()));
			    player.sendMessage("Inventory full. To make more room, sell, drop or bank something.");
				refreshItems(itemsBefore);
				return false;
		    }
		    refreshItems(itemsBefore);
		    return true;
		}
		if (amount + amount2 < 0) {
		    if (itemId == 995) {
				if (player.getMoneyPouch().getCoinsAmount() + amount < 0) {
				    player.sendMessage(Utils.getFormattedNumber(amount) + " coins has been added to the ground.");
				    World.addGroundItem(new Item(995, amount), new WorldTile(player), player, true, 60);
				    return true;
				}
				player.getMoneyPouch().sendDynamicInteraction(amount, false);
				amount = 0;
				return true;
		    }
		    return false;
		}
		if (itemId == 995) {
		    if (player.getMoneyPouch().getCoinsAmount() + amount < 0) {
				coinsToPouch = false;
			}
		    if (player.getMoneyPouch().getCoinsAmount() + amount < Integer.MAX_VALUE && coinsToPouch && !Wilderness.isAtWild(player)) {
		    	player.getMoneyPouch().sendDynamicInteraction(amount, false);
		    	return true;
		    }
		    return false;
		}
		if (itemId == 995 && player.getInventory().getNumberOf(995) + amount < 0) {
		    player.getMoneyPouch().sendDynamicInteraction(amount, false);
		    return true;
		}
		if (amount3 <= 0) {
		    player.sendMessage("Inventory full. To make more room, sell, drop or bank something.");
		    return false;
		}
		if (!items.add(new Item(itemId, amount))) {
		    items.add(new Item(itemId, items.getFreeSlots()));
		    player.sendMessage("Inventory full. To make more room, sell, drop or bank something.");
		    refreshItems(itemsBefore);
		    return false;
		}
		refreshItems(itemsBefore);
		return true;
    }

	public void addItem(int itemId, int amount, boolean dropIfInvFull) {
		if (itemId < 0 || amount < 0 || !Utils.itemExists(itemId) || !player.getControllerManager().canAddInventoryItem(itemId, amount))
			return;
		final Item[] itemsBefore = items.getItemsCopy();
		int numberToDrop;
		if (!items.add(new Item(itemId, amount))) {
			numberToDrop = amount - items.getFreeSlots();
			items.add(new Item(itemId, items.getFreeSlots()));
			player.sm("Not enough space in your inventory.");
			World.addGroundItem(new Item(itemId, numberToDrop), player, player, false, 60);
			refreshItems(itemsBefore);
			return;
		}
		refreshItems(itemsBefore);
	}

	public boolean addItem(Item item) {
		if (item.getId() < 0 || item.getAmount() < 0 || !Utils.itemExists(item.getId()) || !player.getControllerManager().canAddInventoryItem(item.getId(), item.getAmount()))
			return false;
		final Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(item)) {
			items.add(new Item(item.getId(), items.getFreeSlots()));
			player.sm("Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}

    public void deleteItem(int itemId, int amount) {
		if (!player.getControllerManager().canDeleteInventoryItem(itemId, amount)) {
			return;
		}
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
    }

    public void deleteItem(int slot, Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(), item.getAmount())) {
			return;
		}
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(slot, item);
		refreshItems(itemsBefore);
    }

    public void deleteItem(Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(), item.getAmount())) {
			return;
		}
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(item);
		refreshItems(itemsBefore);
    }

	public boolean removeItems(Item... list) {
		for (Item item : list)  {
			if(item == null)
				continue;
				deleteItem(item);
		}
		return true;
	}

	/*
	 * No refresh needed its client to who does it :p
	 */
	public void switchItem(int fromSlot, int toSlot) {
		Item[] itemsBefore = items.getItemsCopy();
		Item fromItem = items.get(fromSlot);
		Item toItem = items.get(toSlot);
		items.set(fromSlot, toItem);
		items.set(toSlot, fromItem);
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		WeightManager.calculateWeight(player);
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

    public ItemsContainer<Item> getItems() {
    	return items;
    }

	public boolean hasFreeSlots() {
		return items.getFreeSlot() != -1;
	}

	public int getFreeSlots() {
		return items.getFreeSlots();
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public int getItemsContainerSize() {
		return items.getSize();
	}

	public boolean containsItem(int itemId, int ammount) {
		return items.contains(new Item(itemId, ammount));
	}

	public boolean containsOneItem(int... itemIds) {
		for (int itemId : itemIds) {
		    if (items.containsOne(new Item(itemId, 1)) || player.getToolbelt().contains(itemId) || player.getToolBeltNew().contains(itemId)) {
				return true;
			}
		}
		return false;
    }

	public void sendExamine(int slotId) {
		if (slotId >= getItemsContainerSize())
			return;
		Item item = items.get(slotId);
		if (item == null)
			return;
		player.getPackets().sendInventoryMessage(0, slotId, ItemExamines.getExamine(item));
	}
	
	public void refresh() {
		player.getPackets().sendItems(93, items);
	}
	
    public int getAmountOf(int itemId) {
    	return items.getNumberOf(itemId);
    }
	
	public int getNumberOf(int itemId) {
		return items.getNumberOf(itemId);
	}

	public boolean containsItemToolBelt(int id) {
		return containsOneItem(id) || player.getToolbelt().contains(id) || player.getToolBeltNew().contains(id);
	}

	public boolean containsItemToolBelt(int id, int amount) {
		return containsItem(id, amount) || player.getToolbelt().contains(id);
	}

		    public boolean add(int id, int amount) {
				if (id > Utils.getItemDefinitionsSize() || amount < 0)
					return false;
				Item item = new Item(id, amount);
				if (!item.isNote()) {
					if (amount > getFreeSlots() && !item.isStackable()) {
						if (item.hasNote()) {
							id = item.getNotedId();
							amount = item.getAmount();
						} else {
							amount = getFreeSlots();
						}
					}
				} else {
					if (amount > getFreeSlots() && !item.isStackable())
						amount = getFreeSlots();
					id = item.getId();
				}
				return addItem(id, amount);
			}

			public boolean contains(Item item) {
		if (containsItem(item.getId(), item.getAmount()))
			return true;
		return false;
	}
	
	public boolean contains(int... itemIds) {
		int count = 0;
		for (int itemId : itemIds) {
			if (items.containsOne(new Item(itemId, 1))) {
				count++;
			}
		}
		if (count >= itemIds.length)
			return true;
		return false;
	}

	public boolean containsCoins(int amount) {
		return items.contains(new Item(995, amount));
	}

	public int getCoinsAmount() {
		final int coins = items.getNumberOf(995) + player.getMoneyPouch().getCoinsAmount();
		return coins < 0 ? Integer.MAX_VALUE : coins;
	}

	public boolean containsItems(List<Item> list) {
		for (Item item : list)
			if (!items.contains(item))
				return false;
		return true;
	}
	
	public boolean removeItems(List<Item> list) {
		for (final Item item : list) {
			if (item == null)
				continue;
			deleteItem(item);
		}
		return true;
	}

	public boolean containsItems(Item... item) {
		for (int i = 0; i < item.length; i++)
			if (!items.contains(item[i]))
				return false;
		return true;
	}

	public boolean containsItems(int[] itemIds, int[] amounts) {
		int size = itemIds.length > amounts.length ? amounts.length : itemIds.length;
		for (int i = 0; i < size; i++)
			if (!items.contains(new Item(itemIds[i], amounts[i])))
				return false;
		return true;
	}

	public void replaceItem(int id, int amount, int slot) {
		Item item = items.get(slot);
		if (item == null) {
			return;
		}
		if (id == -1) {
			items.set(slot, null);
		} else {
		    item.setId(id);
		    item.setAmount(amount);
		}
		refresh(slot);
	}

    public boolean containsItem(Item item) {
    	return items.contains(item);
    }
	

}
