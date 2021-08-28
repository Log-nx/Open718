package com.rs.game.player.content.dungeons;

import java.io.Serializable;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Bank;
import com.rs.game.player.Player;
import com.rs.utils.Color;
import com.rs.utils.Utils;

public class MetalBank implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -8752068490060348315L;

	/**
	 * Items in the chest currently.
	 */
	private ItemsContainer<Item> container;
	
	/**
	 * The player
	 */
	private Player player;

	/**
	 * Constructor
	 */
	public MetalBank() {
		container = new ItemsContainer<Item>(30, true);
	}
	
	/**
	 * Sets the player.
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Get the chest container.
	 * @return
	 */
	public ItemsContainer<Item> getContainer() {
		return container;
	}
	
	public boolean add(Item... items) {		
		if (!(container.freeSlots() >= items.length)) {
			player.getPackets().sendGameMessage(Color.RED, "Your metal bank is currently full. Please empty it to add additional ores.");
			return false;
		}
		
		for (Item item : items) {
			container.add(item);
			player.getPackets().sendGameMessage(Color.GREEN, "You have added " + item.getAmount() + " " + item.getName().toLowerCase() + "s to your metal bank.");
			player.getPackets().sendGameMessage(Color.GREEN, "You now have a total of " + getContainer().getNumberOf(item) + " " + item.getName().toLowerCase() + "s in your metal bank.");
			player.getInventory().deleteItem(item);
		}
		
		return true;
	}
	
	public boolean containsItem(int itemId, int amount) {
		return container.contains(new Item(itemId, amount));
	}
	
	public boolean depositMetalToBank() {
		if (!(Bank.MAX_BANK_SIZE - player.getBank().getBankSize() >= container.getSize())) {
			player.sendMessage("You don't have enough room in your bank to do this.");
			return false;
		}
		for (Item item : getContainer().getItems()) {
			if (item == null)
				continue;		
			ItemDefinitions def = item.getDefinitions();	
			if (def.isNoted() && def.getCertId() != -1)
				item.setId(def.getCertId());		
			player.getBank().addItem(item, true);			
		}	
		container.clear();
		return true;
	}
	
	/**
	 * Shows the player the chest contents.
	 */
	public void getBankContents() {
		player.sendMessage("-------------------Bank Contents-------------------");
		Color color = Color.WHITE;
		for (Item item : container.getItems()) {
			if (item == null)
				continue;
			if (item.getId() == ItemIdentifiers.BRONZE_BAR) {
				color = Color.BROWN;
			}
			if (item.getId() == ItemIdentifiers.IRON_BAR) {
				color = Color.GRAY;
			}
			if (item.getId() == ItemIdentifiers.STEEL_BAR) {
				color = Color.SILVER;
			}
			if (item.getId() == ItemIdentifiers.MITHRIL_BAR) {
				color = Color.PURPLE;
			}
			player.getPackets().sendGameMessage(color, "" + Utils.formatNumber(item.getAmount()) + " " + item.getDefinitions().getName() + "'s.");
		}
		player.sendMessage("----------------------------------------------------");
	}

}