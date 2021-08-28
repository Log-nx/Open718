package com.rs.game.player.actions.smithing.rework;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.actions.Action;

public class BaneCrafting extends Action {
	
	public enum Bane {
		BANE_HELMET(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 2) }, new Item(ItemIdentifiers.BANE_FULL_HELM)),
		BANE_PLATEBODY(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 5) }, new Item(ItemIdentifiers.BANE_PLATEBODY)),
		BANE_PLATELEGS(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 3) }, new Item(ItemIdentifiers.BANE_PLATELEGS)),
		BANE_GAUNTLETS(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 1) }, new Item(ItemIdentifiers.BANE_GAUNTLETS)),
		BANE_BOOTS(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 1) }, new Item(ItemIdentifiers.BANE_ARMOURED_BOOTS)),
		BANE_SQUARE_SHIELD(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 2) }, new Item(ItemIdentifiers.BANE_SQUARE_SHIELD)),
		BANE_LONGSWORD(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 2) }, new Item(ItemIdentifiers.BANE_LONGSWORD)),
		BANE_OFFHAND_LONGSWORD(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 2) }, new Item(ItemIdentifiers.BANE_OFF_HAND_LONGSWORD)),
		BANE_2H(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 4) }, new Item(ItemIdentifiers.BANE_2H_SWORD)),
		BANE_PICKAXE(new Item[] { new Item(ItemIdentifiers.BANE_BAR, 2) }, new Item(ItemIdentifiers.BANE_PICKAXE));
		
		private Item[] itemsRequired;
		private Item productItem;
	
		private Bane(Item[] itemsRequired, Item productItem) {
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
		    return getProductItem().getDefinitions().getCSOpcode(SmithingConstants.ITEM_CREATE_XP_OP) / 10.00;
		}
    }

    public Bane scale;
    public int ticks;

    public BaneCrafting(Bane scale, int ticks) {
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
    
    public static final Bane[] ITEMS = new Bane[] { Bane.BANE_HELMET, Bane.BANE_PLATEBODY, Bane.BANE_PLATELEGS, Bane.BANE_GAUNTLETS, Bane.BANE_BOOTS, Bane.BANE_SQUARE_SHIELD, Bane.BANE_LONGSWORD, Bane.BANE_OFFHAND_LONGSWORD, Bane.BANE_2H, Bane.BANE_PICKAXE };
}