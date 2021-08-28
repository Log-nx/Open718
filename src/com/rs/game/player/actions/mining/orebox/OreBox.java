package com.rs.game.player.actions.mining.orebox;

import java.io.Serializable;
import java.util.Objects;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Bank;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Color;

public class OreBox implements Serializable {

	private static final long serialVersionUID = -7752068490060348315L;

	private ItemsContainer<Item> container;
	
	private Player player;
	
	public static int BRONZE = ItemIdentifiers.BRONZE_ORE_BOX, IRON = ItemIdentifiers.IRON_ORE_BOX, STEEL = ItemIdentifiers.STEEL_ORE_BOX, 
			MITHRIL = ItemIdentifiers.MITHRIL_ORE_BOX, ADAMANT = ItemIdentifiers.ADAMANT_ORE_BOX, RUNE = ItemIdentifiers.RUNE_ORE_BOX,
			ORIKALKUM = ItemIdentifiers.ORIKALKUM_ORE_BOX, NECRONIUM = ItemIdentifiers.NECRONIUM_ORE_BOX, BANE = ItemIdentifiers.BANE_ORE_BOX,
			ELDER_RUNE = ItemIdentifiers.ELDER_RUNE_ORE_BOX;

	public OreBox() {
		container = new ItemsContainer<Item>(30, true);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public ItemsContainer<Item> getContainer() {
		return container;
	}
	
	public boolean add(Item oreBox, Item... items) {		
		if (!(container.freeSlots() >= items.length)) {
			player.getPackets().sendGameMessage(Color.RED, "Your " + oreBox.getName().toLowerCase() + " is currently full. Please empty it to add additional ores.");
			return false;
		}
		
		for (Item item : items) {
			container.add(item);
			player.getPackets().sendGameMessage(Color.GREEN, "You added x" + item.getAmount() + " " +  item.getName().toLowerCase() + " to the " + oreBox.getName().toLowerCase() + ".");
			player.getInventory().deleteItem(item);
		}
		
		return true;
	}
	
	public boolean containsItem(int itemId, int amount) {
		return container.contains(new Item(itemId, amount));
	}
	
	public void getBoxContents(Item oreBox) {
		String title = oreBox.getName().toUpperCase();
		player.sendMessage("------------------" + title + "------------------");
		Color color = Color.WHITE;
		for (Item item : container.getItems()) {
			if (item == null) {
				continue;
			}
			if (oreBox.getId() == BRONZE) {
				if (item.getId() != ItemIdentifiers.TIN_ORE && item.getId() !=  ItemIdentifiers.COPPER_ORE) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == IRON) {
				if (item.getId() != ItemIdentifiers.IRON_ORE) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == STEEL) {
				if (item.getId() != ItemIdentifiers.SILVER_ORE && item.getId() != ItemIdentifiers.COAL) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == MITHRIL) {
				if (item.getId() != ItemIdentifiers.MITHRIL_ORE) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == ADAMANT) {
				if (item.getId() != ItemIdentifiers.ADAMANTITE_ORE && item.getId() != ItemIdentifiers.LUMINITE && item.getId() != ItemIdentifiers.GOLD_ORE) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == RUNE) {
				if (item.getId() != ItemIdentifiers.RUNITE_ORE) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == ORIKALKUM) {
				if (item.getId() != ItemIdentifiers.ORICHALCITE_ORE && item.getId() != ItemIdentifiers.DRAKOLITH) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == NECRONIUM) {
				if (item.getId() != ItemIdentifiers.NECRITE_ORE && item.getId() != ItemIdentifiers.PHASMATITE) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == BANE) {
				if (item.getId() != ItemIdentifiers.BANE_ORE) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
			if (oreBox.getId() == ELDER_RUNE) {
				if (item.getId() != ItemIdentifiers.LIGHT_ANIMICA && item.getId() != ItemIdentifiers.DARK_ANIMICA) {
					continue;
				}
				player.getPackets().sendGameMessage(color, "" + item.getAmount() + "x " + item.getDefinitions().getName() + ".");
			}
		}
		player.sendMessage("----------------------------------------------------");
	}
	
	public boolean isOreBox(Item oreBox) {
		if (oreBox.getId() == BRONZE || oreBox.getId() == IRON || oreBox.getId() == STEEL || oreBox.getId() == MITHRIL 
			|| oreBox.getId() == ADAMANT || oreBox.getId() == RUNE || oreBox.getId() == ORIKALKUM || oreBox.getId() == NECRONIUM
			|| oreBox.getId() == BANE || oreBox.getId() == ELDER_RUNE) {
			return true;
		}
		return false;
	}
	
	public boolean depositOres(Item orebox, Item item) {
		if (orebox.getId() == BRONZE) {
			if (player.getInventory().contains(ItemIdentifiers.COPPER_ORE)) {
				item = new Item(ItemIdentifiers.COPPER_ORE, player.getInventory().getAmountOf(ItemIdentifiers.COPPER_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.COPPER_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.COPPER_ORE, player.getInventory().getAmountOf(ItemIdentifiers.COPPER_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.COPPER_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.COPPER_ORE_NOTED));
			}
			if (player.getInventory().contains(ItemIdentifiers.TIN_ORE)) {
				item = new Item(ItemIdentifiers.TIN_ORE, player.getInventory().getAmountOf(ItemIdentifiers.TIN_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.TIN_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.TIN_ORE, player.getInventory().getAmountOf(ItemIdentifiers.TIN_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.TIN_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.TIN_ORE_NOTED));
			}
			return true;
		}
		if (orebox.getId() == IRON) {
			if (player.getInventory().contains(ItemIdentifiers.IRON_ORE)) {
				item = new Item(ItemIdentifiers.IRON_ORE, player.getInventory().getAmountOf(ItemIdentifiers.IRON_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.IRON_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.IRON_ORE, player.getInventory().getAmountOf(ItemIdentifiers.IRON_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.IRON_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.TIN_ORE_NOTED));
			}
			return true;
		}
		if (orebox.getId() == STEEL) {
			if (player.getInventory().contains(ItemIdentifiers.COAL)) {
				item = new Item(ItemIdentifiers.COAL, player.getInventory().getAmountOf(ItemIdentifiers.COAL));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.COAL_NOTED)) {
				item = new Item(ItemIdentifiers.COAL, player.getInventory().getAmountOf(ItemIdentifiers.COAL_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.COAL_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.COAL_NOTED));
			}
			if (player.getInventory().contains(ItemIdentifiers.SILVER_ORE)) {
				item = new Item(ItemIdentifiers.SILVER_ORE, player.getInventory().getAmountOf(ItemIdentifiers.SILVER_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.SILVER_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.SILVER_ORE, player.getInventory().getAmountOf(ItemIdentifiers.SILVER_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.SILVER_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.SILVER_ORE_NOTED));
			}
			return true;
		}
		if (orebox.getId() == MITHRIL) {
			if (player.getInventory().contains(ItemIdentifiers.MITHRIL_ORE)) {
				item = new Item(ItemIdentifiers.MITHRIL_ORE, player.getInventory().getAmountOf(ItemIdentifiers.MITHRIL_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.MITHRIL_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.MITHRIL_ORE, player.getInventory().getAmountOf(ItemIdentifiers.MITHRIL_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.MITHRIL_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.MITHRIL_ORE_NOTED));
			}
			return true;
		}
		if (orebox.getId() == ADAMANT) {
			if (player.getInventory().contains(ItemIdentifiers.ADAMANTITE_ORE)) {
				item = new Item(ItemIdentifiers.ADAMANTITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.ADAMANTITE_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.ADAMANTITE_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.ADAMANTITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.ADAMANTITE_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.ADAMANTITE_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.ADAMANTITE_ORE_NOTED));
			}
			if (player.getInventory().contains(ItemIdentifiers.LUMINITE)) {
				item = new Item(ItemIdentifiers.LUMINITE, player.getInventory().getAmountOf(ItemIdentifiers.LUMINITE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.LUMINITE_NOTED)) {
				item = new Item(ItemIdentifiers.LUMINITE, player.getInventory().getAmountOf(ItemIdentifiers.LUMINITE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.LUMINITE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.LUMINITE_NOTED));
			}
			if (player.getInventory().contains(ItemIdentifiers.GOLD_ORE)) {
				item = new Item(ItemIdentifiers.GOLD_ORE, player.getInventory().getAmountOf(ItemIdentifiers.GOLD_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.GOLD_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.GOLD_ORE, player.getInventory().getAmountOf(ItemIdentifiers.GOLD_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.GOLD_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.GOLD_ORE_NOTED));
			}
			return true;
		}
		if (orebox.getId() == RUNE) {
			if (player.getInventory().contains(ItemIdentifiers.RUNITE_ORE)) {
				item = new Item(ItemIdentifiers.RUNITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.RUNITE_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.RUNITE_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.RUNITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.RUNITE_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.RUNITE_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.RUNITE_ORE_NOTED));
			}
			return true;
		}
		if (orebox.getId() == ORIKALKUM) {
			if (player.getInventory().contains(ItemIdentifiers.ORICHALCITE_ORE)) {
				item = new Item(ItemIdentifiers.ORICHALCITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.ORICHALCITE_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.ORICHALCITE_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.ORICHALCITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.ORICHALCITE_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.ORICHALCITE_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.ORICHALCITE_ORE_NOTED));
			}
			if (player.getInventory().contains(ItemIdentifiers.DRAKOLITH)) {
				item = new Item(ItemIdentifiers.DRAKOLITH, player.getInventory().getAmountOf(ItemIdentifiers.DRAKOLITH));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.DRAKOLITH_NOTED)) {
				item = new Item(ItemIdentifiers.DRAKOLITH, player.getInventory().getAmountOf(ItemIdentifiers.DRAKOLITH_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.DRAKOLITH_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.DRAKOLITH_NOTED));
			}
			return true;
		}
		if (orebox.getId() == NECRONIUM) {
			if (player.getInventory().contains(ItemIdentifiers.NECRITE_ORE)) {
				item = new Item(ItemIdentifiers.NECRITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.NECRITE_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.NECRITE_ORE_NOTED)) {
				item = new Item(ItemIdentifiers.NECRITE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.NECRITE_ORE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.NECRITE_ORE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.NECRITE_ORE_NOTED));
			}
			if (player.getInventory().contains(ItemIdentifiers.PHASMATITE)) {
				item = new Item(ItemIdentifiers.PHASMATITE, player.getInventory().getAmountOf(ItemIdentifiers.PHASMATITE));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.PHASMATITE_NOTED)) {
				item = new Item(ItemIdentifiers.PHASMATITE, player.getInventory().getAmountOf(ItemIdentifiers.PHASMATITE_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.PHASMATITE_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.PHASMATITE_NOTED));
			}
			return true;
		}
		if (orebox.getId() == BANE) {
			if (player.getInventory().contains(ItemIdentifiers.BANE_ORE)) {
				item = new Item(ItemIdentifiers.BANE_ORE, player.getInventory().getAmountOf(ItemIdentifiers.BANE_ORE));
				add(orebox, item);
			}
			if (player.getInventory().contains(26886)) {
				item = new Item(ItemIdentifiers.BANE_ORE, player.getInventory().getAmountOf(26886));
				add(orebox, item);
				player.getInventory().deleteItem(26886, player.getInventory().getAmountOf(26886));
			}
			return true;
		}
		if (orebox.getId() == ELDER_RUNE) {
			if (player.getInventory().contains(ItemIdentifiers.LIGHT_ANIMICA)) {
				item = new Item(ItemIdentifiers.LIGHT_ANIMICA, player.getInventory().getAmountOf(ItemIdentifiers.LIGHT_ANIMICA));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.LIGHT_ANIMICA_NOTED)) {
				item = new Item(ItemIdentifiers.LIGHT_ANIMICA, player.getInventory().getAmountOf(ItemIdentifiers.LIGHT_ANIMICA_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.LIGHT_ANIMICA_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.LIGHT_ANIMICA_NOTED));
			}
			if (player.getInventory().contains(ItemIdentifiers.DARK_ANIMICA)) {
				item = new Item(ItemIdentifiers.DARK_ANIMICA, player.getInventory().getAmountOf(ItemIdentifiers.DARK_ANIMICA));
				add(orebox, item);
			}
			if (player.getInventory().contains(ItemIdentifiers.DARK_ANIMICA_NOTED)) {
				item = new Item(ItemIdentifiers.DARK_ANIMICA, player.getInventory().getAmountOf(ItemIdentifiers.DARK_ANIMICA_NOTED));
				add(orebox, item);
				player.getInventory().deleteItem(ItemIdentifiers.DARK_ANIMICA_NOTED, player.getInventory().getAmountOf(ItemIdentifiers.DARK_ANIMICA_NOTED));
			}
			return true;
		}
		return false;
	}
	
	public boolean depositChestToBank(Item oreBox) {
		if (player.getDonationManager().isDonator() || player.getRights() >= 1) {
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
			if (oreBox.getId() == BRONZE) {
				if (item.getId() != ItemIdentifiers.COPPER_ORE && item.getId() != ItemIdentifiers.TIN_ORE) {
					continue;
				}
			}
			if (oreBox.getId() == IRON) {
				if (item.getId() != ItemIdentifiers.IRON_ORE) {
					continue;
				}
			}
			if (oreBox.getId() == STEEL) {
				if (item.getId() != ItemIdentifiers.SILVER_ORE && item.getId() != ItemIdentifiers.COAL) {
					continue;
				}
			}
			if (oreBox.getId() == MITHRIL) {
				if (item.getId() != ItemIdentifiers.MITHRIL_ORE) {
					continue;
				}
			}
			if (oreBox.getId() == ADAMANT) {
				if (item.getId() != ItemIdentifiers.ADAMANTITE_ORE && item.getId() != ItemIdentifiers.LUMINITE && item.getId() != ItemIdentifiers.GOLD_ORE) {
					continue;
				}
			}
			if (oreBox.getId() == RUNE) {
				if (item.getId() != ItemIdentifiers.RUNITE_ORE) {
					continue;
				}
			}
			if (oreBox.getId() == ORIKALKUM) {
				if (item.getId() != ItemIdentifiers.ORICHALCITE_ORE && item.getId() != ItemIdentifiers.DRAKOLITH) {
					continue;
				}
			}
			if (oreBox.getId() == NECRONIUM) {
				if (item.getId() != ItemIdentifiers.NECRITE_ORE && item.getId() != ItemIdentifiers.PHASMATITE) {
					continue;
				}
			}
			if (oreBox.getId() == BANE) {
				if (item.getId() != ItemIdentifiers.BANE_ORE) {
					continue;
				}
			}
			if (oreBox.getId() == ELDER_RUNE) {
				if (item.getId() != ItemIdentifiers.LIGHT_ANIMICA && item.getId() != ItemIdentifiers.DARK_ANIMICA) {
					continue;
				}
			}
			player.getBank().addItem(item, true);		
			container.remove(item);
		}	
		return true;
	} else {
		player.getDialogueManager().startDialogue("SimpleMessage", "You need to be a donator to do this.");
	}
		return false;
	}
}