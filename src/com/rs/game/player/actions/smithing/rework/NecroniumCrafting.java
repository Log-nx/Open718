package com.rs.game.player.actions.smithing.rework;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.actions.Action;

public class NecroniumCrafting extends Action {
	
	public enum Necronium {
		NECRONIUM_HELMET(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 2) }, new Item(ItemIdentifiers.NECRONIUM_FULL_HELM)),
		NECRONIUM_PLATEBODY(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 5) }, new Item(ItemIdentifiers.NECRONIUM_PLATEBODY)),
		NECRONIUM_PLATELEGS(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 3) }, new Item(ItemIdentifiers.NECRONIUM_PLATELEGS)),
		NECRONIUM_GAUNTLETS(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 1) }, new Item(ItemIdentifiers.NECRONIUM_GAUNTLETS)),
		NECRONIUM_BOOTS(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 1) }, new Item(ItemIdentifiers.NECRONIUM_ARMOURED_BOOTS)),
		NECRONIUM_KITESHIELD(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 2) }, new Item(ItemIdentifiers.NECRONIUM_KITESHIELD)),
		NECRONIUM_WARHAMMER(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 2) }, new Item(ItemIdentifiers.NECRONIUM_BATTLEAXE)),
		NECRONIUM_OFFHAND_WARHAMMER(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 2) }, new Item(ItemIdentifiers.NECRONIUM_OFF_HAND_BATTLEAXE)),
		NECRONIUM_2H_BATTLEAXE(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 4) }, new Item(ItemIdentifiers.NECRONIUM_2H_GREATAXE)),
		NECRONIUM_PICKAXE(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 2) }, new Item(ItemIdentifiers.NECRONIUM_PICKAXE)),
		NECRONIUM_ARMOUR_SET(new Item[] { new Item(ItemIdentifiers.NECRONIUM_BAR, 14) }, new Item(ItemIdentifiers.NECRONIUM_ARMOUR_SET));
	
		private Item[] itemsRequired;
		private Item productItem;
	
		private Necronium(Item[] itemsRequired, Item productItem) {
		    this.itemsRequired = itemsRequired;
		    this.productItem = productItem;
		}
	
		public Item[] getItemsRequired() {
		    return itemsRequired;
		}
	
		public int getLevelRequired() {
		    return getProductItem().getDefinitions().getCSOpcode(SmithingConstants.SMITHING_LEVEL_REQUIREMENT_OP);
		}
	
		public Item getProductItem() {
		    return productItem;
		}
	
		public double getExperience() {
		    return getProductItem().getDefinitions().getCSOpcode(SmithingConstants.ITEM_CREATE_XP_OP) / 10;
		}
    }

    public Necronium scale;
    public int ticks;

    public NecroniumCrafting(Necronium scale, int ticks) {
		this.scale = scale;
		this.ticks = ticks;
    }

    @Override
    public boolean start(Player player) {
    	if (!process(player))
			return false;
		return true;
    }

    @Override
    public boolean process(Player player) {
		if (scale == null || player == null)
		    return false;
		if (ticks <= 0)
			return false;
		if (player.getSkills().getLevel(Skills.SMITHING) < scale.getLevelRequired()) {
			player.getPackets().sendGameMessage("You need a Smithing level of at least " + scale.getLevelRequired() + " to create a " + scale.getProductItem().getDefinitions().getName());
		    return false;
		}
    	int amount = scale.getItemsRequired()[0].getAmount();
		if (!player.getInventory().containsItem(scale.getItemsRequired()[0].getId(), amount)) {
			player.sm("You need "+(amount > 1 ? "x"+amount+" of" : "a")+" " + scale.getItemsRequired()[0].getDefinitions().getName() + " to create " + scale.getProductItem().getDefinitions().getName() + ".");
		    return false;
		}
		return true;
    }

    @Override
    public int processWithDelay(Player player) {
		ticks--;
		double xp = scale.getExperience();
		for (Item required : scale.getItemsRequired())
		    player.getInventory().deleteItem(required.getId(), required.getAmount());
		int amount = scale.getProductItem().getAmount();
		player.getInventory().addItem(scale.getProductItem().getId(), amount);
		player.getSkills().addXp(Skills.SMITHING, xp);
		player.getPackets().sendGameMessage("You make a "+ scale.getProductItem().getDefinitions().getName().toLowerCase() + ".", true);
		player.setNextAnimation(new Animation(22143));
		if (ticks > 0)
		    return 1;
		return -1;
    }

    @Override
    public void stop(Player player) {
    	setActionDelay(player, 3);
    }
    
    public static final Necronium[] ITEMS = new Necronium[] { Necronium.NECRONIUM_HELMET, Necronium.NECRONIUM_PLATEBODY, Necronium.NECRONIUM_PLATELEGS, Necronium.NECRONIUM_GAUNTLETS, Necronium.NECRONIUM_BOOTS, Necronium.NECRONIUM_KITESHIELD, Necronium.NECRONIUM_WARHAMMER, Necronium.NECRONIUM_OFFHAND_WARHAMMER, Necronium.NECRONIUM_2H_BATTLEAXE, Necronium.NECRONIUM_PICKAXE, Necronium.NECRONIUM_ARMOUR_SET };
}