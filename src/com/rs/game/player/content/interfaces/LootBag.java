package com.rs.game.player.content.interfaces;

import java.io.Serializable;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.content.ItemConstants;
import com.rs.utils.ItemSetsKeyGenerator;

public class LootBag implements Serializable {
 
    private static final int ITEMS_KEY = ItemSetsKeyGenerator.generateKey();
 
    /**
     * 
     */
    private static final long serialVersionUID = -2090871604834210257L;
 
    private transient Player player;
 
    private ItemsContainer<Item> lootbag;
 
    public LootBag(int size) {
        lootbag = new ItemsContainer<Item>(size, false);
    }
 
    public void setPlayer(Player player) {
        this.player = player;
    }
 
    public void open() {
        player.getInterfaceManager().sendInterface(671);
        player.getPackets().sendIComponentText(671, 14, "Lootbag");
        player.getPackets().sendIComponentModel(671, 29, 15);
        player.getInterfaceManager().sendInventoryInterface(665);
        sendInterItems();
        sendOptions();
    }
 
    public void dropContents() {
        WorldTile WorldTile = new WorldTile(player.getLocalX());
        for (int i = 0; i < lootbag.getSize(); i++) {
            Item item = lootbag.get(i);
            if (item != null)
                World.addGroundItem(item, WorldTile, player, false, -1);
        }
        lootbag.reset();
    }
 
    public void takeContents() {
        int freeSpots = player.getInventory().getFreeSlots();
        if (freeSpots == 0) {
            player.sm("You have no more free spots.");
            return;
        }
        System.out.println(lootbag.getSize());
        if (slotsUsed() == 0) {
            player.sm("You have no items to withdraw.");
            return;
        }
        Item[] itemsBefore = lootbag.getItemsCopy();
        if (freeSpots > slotsUsed()) {
            for (int i = 0; i < lootbag.getSize(); i++) {
                Item item = lootbag.get(i);
                if (item != null) {
                    if (!player.getInventory().addItem(item))
                        break;
                    lootbag.remove(i, item);
                }
            }
            lootbag.shift();
            refreshItems(itemsBefore);
        } else {
            for (int i = 0; i < freeSpots; i++) {
                Item item = lootbag.get(i);
                if (item != null) {
                    if (!player.getInventory().addItem(item))
                        break;
                    lootbag.remove(i, item);
                }
            }
            lootbag.shift();
            refreshItems(itemsBefore);
        }
 
    }
 
    public void removeItem(int slot, int amount) {
        Item item = lootbag.get(slot);
        if (item == null)
            return;
        Item[] itemsBefore = lootbag.getItemsCopy();
        int maxAmount = lootbag.getNumberOf(item);
        if (amount < maxAmount)
            item = new Item(item.getId(), amount);
        else
            item = new Item(item.getId(), maxAmount);
        int freeSpace = player.getInventory().getFreeSlots();
        if (!item.getDefinitions().isStackable()) {
            if (freeSpace == 0) {
                player.getPackets().sendGameMessage("Not enough space in your inventory.");
                return;
            }
            if (freeSpace < item.getAmount()) {
                item.setAmount(freeSpace);
                player.getPackets().sendGameMessage("Not enough space in your inventory.");
            }
        } else {
            if (freeSpace == 0 && !player.getInventory().containsItem(item.getId(), 1)) {
                player.getPackets().sendGameMessage("Not enough space in your inventory.");
                return;
            }
        }
        lootbag.remove(slot, item);
        lootbag.shift();
        player.getInventory().addItem(item);
        refreshItems(itemsBefore);
    }
 
    public void addItem(int slot, int amount) {
        Item item = player.getInventory().getItem(slot);
        if (item == null)
            return;
        if (!ItemConstants.isTradeable(item)) {
            player.getPackets().sendGameMessage("You cannot store this item.");
            return;
        }
        Item[] itemsBefore = lootbag.getItemsCopy();
        int maxAmount = player.getInventory().getItems().getNumberOf(item);
        if (amount < maxAmount)
            item = new Item(item.getId(), amount);
        else
            item = new Item(item.getId(), maxAmount);
        int freeSpace = lootbag.getFreeSlots();
        if (!item.getDefinitions().isStackable()) {
            if (freeSpace == 0) {
                player.getPackets().sendGameMessage("You don't have enough space in your lootbag.");
                return;
            }
 
            if (freeSpace < item.getAmount()) {
                item.setAmount(freeSpace);
                player.getPackets().sendGameMessage("You don't have enough space in your lootbag.");
            }
        } else {
            if (freeSpace == 0 && !lootbag.containsOne(item)) {
                player.getPackets().sendGameMessage("You don't have enough space in your lootbag.");
                return;
            }
        }
        lootbag.add(item);
        lootbag.shift();
        player.getInventory().deleteItem(slot, item);
        refreshItems(itemsBefore);
    }
 
    public void refreshItems(Item[] itemsBefore) {
        int[] changedSlots = new int[itemsBefore.length];
        int count = 0;
        for (int index = 0; index < itemsBefore.length; index++) {
            Item item = lootbag.getItems()[index];
            if (itemsBefore[index] != item) {
                changedSlots[count++] = index;
            }
 
        }
        int[] finalChangedSlots = new int[count];
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
        refresh(finalChangedSlots);
    }
 
    public void refresh(int... slots) {
        player.getPackets().sendUpdateItems(ITEMS_KEY, lootbag, slots);
    }
 
    public void sendOptions() {
        player.getPackets().sendUnlockIComponentOptionSlots(665, 0, 0, 27, 0, 1, 2, 3, 4, 5);
        player.getPackets().sendInterSetItemsOptionsScript(665, 0, 93, 4, 7, "Store", "Store-5", "Store-10",
                "Store-All", "Store-X", "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(671, 27, 0, ITEMS_KEY, 0, 1, 2, 3, 4, 5);
        player.getPackets().sendInterSetItemsOptionsScript(671, 27, ITEMS_KEY, 6, 5, "Withdraw", "Withdraw-5",
                "Withdraw-10", "Withdraw-All", "Withdraw-X", "Examine");
    }
 
    public void sendInterItems() {
        player.getPackets().sendItems(ITEMS_KEY, lootbag);
        player.getPackets().sendItems(93, player.getInventory().getItems());
    }
 
    public ItemsContainer<Item> getLootbagItems() {
        return lootbag;
    }
     
    public int slotsUsed() {
        return 28 - lootbag.getFreeSlots();
    }
}
