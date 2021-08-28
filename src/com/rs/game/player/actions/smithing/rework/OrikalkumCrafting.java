package com.rs.game.player.actions.smithing.rework;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.actions.Action;

public class OrikalkumCrafting extends Action {
	
	public enum Orikalkum {
		ORIKALKUM_HELMET(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 2) }, new Item(ItemIdentifiers.ORIKALKUM_FULL_HELM)),
		ORIKALKUM_PLATEBODY(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 5) }, new Item(ItemIdentifiers.ORIKALKUM_PLATEBODY)),
		ORIKALKUM_PLATELEGS(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 3) }, new Item(ItemIdentifiers.ORIKALKUM_PLATELEGS)),
		ORIKALKUM_GAUNTLETS(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 1) }, new Item(ItemIdentifiers.ORIKALKUM_GAUNTLETS)),
		ORIKALKUM_BOOTS(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 1) }, new Item(ItemIdentifiers.ORIKALKUM_ARMOURED_BOOTS)),
		ORIKALKUM_KITESHIELD(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 2) }, new Item(ItemIdentifiers.ORIKALKUM_KITESHIELD)),
		ORIKALKUM_WARHAMMER(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 2) }, new Item(ItemIdentifiers.ORIKALKUM_WARHAMMER)),
		ORIKALKUM_OFFHAND_WARHAMMER(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 2) }, new Item(ItemIdentifiers.ORIKALKUM_OFF_HAND_WARHAMMER)),
		ORIKALKUM_2H_WARHAMMER(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 4) }, new Item(ItemIdentifiers.ORIKALKUM_2H_WARHAMMER)),
		ORIKALKUM_PICKAXE(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 2) }, new Item(ItemIdentifiers.ORIKALKUM_PICKAXE)),
		ORIKALKUM_ARMOUR_SET(new Item[] { new Item(ItemIdentifiers.ORIKALKUM_BAR, 14) }, new Item(ItemIdentifiers.ORIKALKUM_ARMOUR_SET));
	
		private Item[] itemsRequired;
		private Item productItem;
	
		private Orikalkum(Item[] itemsRequired, Item productItem) {
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

    public Orikalkum scale;
    public int ticks;

    public OrikalkumCrafting(Orikalkum scale, int ticks) {
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
    
    public static final Orikalkum[] ITEMS = new Orikalkum[] { Orikalkum.ORIKALKUM_HELMET, Orikalkum.ORIKALKUM_PLATEBODY, Orikalkum.ORIKALKUM_PLATELEGS, Orikalkum.ORIKALKUM_GAUNTLETS, Orikalkum.ORIKALKUM_BOOTS, Orikalkum.ORIKALKUM_KITESHIELD, Orikalkum.ORIKALKUM_WARHAMMER, Orikalkum.ORIKALKUM_2H_WARHAMMER, Orikalkum.ORIKALKUM_OFFHAND_WARHAMMER, Orikalkum.ORIKALKUM_PICKAXE, Orikalkum.ORIKALKUM_ARMOUR_SET };
}