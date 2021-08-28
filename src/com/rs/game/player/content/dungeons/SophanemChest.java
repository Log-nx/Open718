package com.rs.game.player.content.dungeons;

import java.io.Serializable;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Bank;
import com.rs.game.player.Player;

public class SophanemChest implements Serializable {

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
	 * If this chest is collecting loot.
	 */
	private boolean collecting;
	
	/**
	 * Items that arnt collected, but drop on the ground.
	 */
	private static final int[] DROPPED_ITEMS = { 
			40310, //Key to the crossing
			40334, //Corrupted gem
			40312, //Khopesh of the Kharidian
			995,
	};
	
	/**
	 * Constructor
	 */
	public SophanemChest() {
		container = new ItemsContainer<Item>(30, true);
		collecting = true;
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
	
	/**
	 * Toggles collecting of loot into this container.
	 */
	public void toggleCollecting() {
		this.collecting = !this.collecting;
		player.sendMessage("<col=00ffff>Collection of loot has now been " + (collecting ? "enabled" : "disabled") + ".</col>");
	}
	
	/**
	 * Is the chest collecting loot?
	 * @return {@value true} if the chest is collecting loot.
	 */
	public boolean isCollecting() {
		return collecting;
	}
	
	/**
	 * Adds loot to the chest container.
	 * @param items {@link Item} the loot.
	 * @return {@value true} if loot was added to chest container.
	 */
	public boolean add(Item... items) {
		//Checks for drops that can't be collected by this chest.
		for (Item item : items) {			
			for (int id : DROPPED_ITEMS) {
				if (item.getId() == id)
					return false;
			}					
		}
		//If player isn't using chest, dont collect items in it.
		if (!isCollecting())
			return false;	
		
		if (!(container.freeSlots() >= items.length)) {
			player.sendMessage("<col=ff0000>Your treasure chest is currently full. Please empty it to add additional loot.</col>");
			return false;
		}
		
		for (Item item : items)
			container.add(item);
		
		return true;
	}
	
	/**
	 * Deposits all the chest contents to their bank.
	 * @return {@value true} if the items were deposited.
	 */
	public boolean depositChestToBank() {
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
	public void getChestContents() {
		player.sendMessage("-------------------Chest Contents-------------------");
		for (Item item : container.getItems()) {
			if (item == null)
				continue;
			player.sendMessage("" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
		}
		player.sendMessage("----------------------------------------------------");
	}

}