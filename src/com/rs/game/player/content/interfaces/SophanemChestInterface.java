package com.rs.game.player.content.interfaces;

import java.util.Objects;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.ItemExamines;

public class SophanemChestInterface {

	//The player
	private Player player;
	
	//Container key for items container
	private static final int containerKey = 100;
	
	//Interface text
	private static final String 
			title = "Sophanem Chest",
			bank = "Bank All", 
			take = "Take All",
			abandon = "Abandon All";
	
	//Constructor
	public SophanemChestInterface(Player player) {
		this.player = player;
	}
	
	public void open() {
		player.getPackets().sendIComponentText(getId(), 28, title);
		player.getPackets().sendIComponentText(getId(), 43, take);
		player.getPackets().sendIComponentText(getId(), 42, abandon);
		player.getPackets().sendIComponentText(getId(), 4, bank);
		refreshContainer(player);
	}

	public static boolean onClick(Player player, int button, int packet, int itemId, int slotId) {
		Item item = player.getSophanemChest().getContainer().get(slotId);
		int freeSlots = player.getInventory().getFreeSlots();
		switch (button) {
		
			case 7: //Click item
				switch (packet) {
				
					case 5: //Discard
						player.getSophanemChest().getContainer().remove(item);
						refreshContainer(player);
						break;
				
					case 14: //Take	
						if (freeSlots <= 0 && !(item.getDefinitions().isStackable() && player.getInventory().contains(item.getId()))) {
							player.sendMessage("You don't have enough inventory space to withdrawl " + (item.getAmount() > 1 ? "these" : "this") + ".");
							return true;
						}
						if (player.getInventory().addItem(item)) {
							player.getSophanemChest().getContainer().remove(item);
						} else {
							player.getSophanemChest().getContainer().remove(new Item(item.getId(), freeSlots));
						}
						refreshContainer(player);
						break;
					
					case 55: //Examine
						player.sendMessage(ItemExamines.getExamine(item));
						break;
						
					case 67: //Bank
						player.getBank().addItem(item, false);
						player.getSophanemChest().getContainer().remove(item);
						refreshContainer(player);
						break;
				}
				break;

			case 8: //Bank all
				player.getSophanemChest().depositChestToBank();
				refreshContainer(player);
				break;
				
			case 9: //Abandon all
				player.getDialogueManager().startDialogue("SophanemChest"); //Confirm destroy for items
				refreshContainer(player);
				break;
				
			case 10: //Take all
				for (Item i : player.getSophanemChest().getContainer().getItemsCopy()) {				
					if (Objects.isNull(i))
						continue;
					if (player.getInventory().addItem(i)) {
						player.getSophanemChest().getContainer().remove(i);
					} else {
						player.getSophanemChest().getContainer().remove(new Item(i.getId(), freeSlots));
					}		
				}
				refreshContainer(player);
				break;
		}
		return true;
	}

	public static int getId() {
		return 1284;
	}
	
	/**
	 * Refreshes the container and shifts all the items together.
	 */
	private static void refreshContainer(Player player) {
		player.getPackets().sendInterSetItemsOptionsScript(getId(), 7, 100, 8, 3, "Take", "Bank", "Discard", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(getId(), 7, 0, 10, 0, 1, 2, 3);
		player.getSophanemChest().getContainer().shift();
		player.getPackets().sendItems(containerKey, player.getSophanemChest().getContainer());	
	}

}