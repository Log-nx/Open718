package com.rs.game.player.actions.smithing.rework;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;

public class ElderRuneCrafting extends Action {
	
	public static final int ITEM_CREATE_XP_OP = 2697, SMITHING_LEVEL_REQUIREMENT_OP = 2645;
	
	public enum ElderRune {
		ELDER_RUNE_HELMET(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 2) }, new Item(ItemIdentifiers.ELDER_RUNE_FULL_HELM)),
		ELDER_RUNE_PLATEBODY(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 5) }, new Item(ItemIdentifiers.ELDER_RUNE_PLATEBODY)),
		ELDER_RUNE_PLATELEGS(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 3) }, new Item(ItemIdentifiers.ELDER_RUNE_PLATELEGS)),
		ELDER_RUNE_GAUNTLETS(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 1) }, new Item(ItemIdentifiers.ELDER_RUNE_GAUNTLETS)),
		ELDER_RUNE_BOOTS(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 1) }, new Item(ItemIdentifiers.ELDER_RUNE_ARMOURED_BOOTS)),
		ELDER_RUNE_ROUND_SHIELD(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 2) }, new Item(ItemIdentifiers.ELDER_RUNE_ROUND_SHIELD)),
		ELDER_RUNE_LONGSWORD(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 2) }, new Item(ItemIdentifiers.ELDER_RUNE_LONGSWORD)),
		ELDER_RUNE_OFFHAND_LONGSWORD(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 2) }, new Item(ItemIdentifiers.ELDER_RUNE_OFF_HAND_LONGSWORD)),
		ELDER_RUNE_2H(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 4) }, new Item(ItemIdentifiers.ELDER_RUNE_2H_SWORD)),
		ELDER_RUNE_PICKAXE(new Item[] { new Item(ItemIdentifiers.ELDER_RUNE_BAR, 2) }, new Item(ItemIdentifiers.ELDER_RUNE_PICKAXE));
	
		private Item[] itemsRequired;
		private Item productItem;
	
		private ElderRune(Item[] itemsRequired, Item productItem) {
		    this.itemsRequired = itemsRequired;
		    this.productItem = productItem;
		}
	
		public Item[] getItemsRequired() {
		    return itemsRequired;
		}
	
		public int getLevelRequired() {
		    return getProductItem().getDefinitions().getCSOpcode(SMITHING_LEVEL_REQUIREMENT_OP);
		}
	
		public Item getProductItem() {
		    return productItem;
		}
	
		public double getExperience() {
		    return ((double) getProductItem().getDefinitions().getCSOpcode(ITEM_CREATE_XP_OP) / 10.00);
		}
    }

    public ElderRune scale;
    public int ticks;

    public ElderRuneCrafting(ElderRune scale, int ticks) {
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
    
    public static final ElderRune[] ITEMS = new ElderRune[] { ElderRune.ELDER_RUNE_HELMET, ElderRune.ELDER_RUNE_PLATEBODY, ElderRune.ELDER_RUNE_PLATELEGS, ElderRune.ELDER_RUNE_GAUNTLETS, ElderRune.ELDER_RUNE_BOOTS, ElderRune.ELDER_RUNE_ROUND_SHIELD, ElderRune.ELDER_RUNE_LONGSWORD, ElderRune.ELDER_RUNE_OFFHAND_LONGSWORD, ElderRune.ELDER_RUNE_2H, ElderRune.ELDER_RUNE_PICKAXE };
}