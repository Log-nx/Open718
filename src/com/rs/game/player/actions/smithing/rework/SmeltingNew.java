package com.rs.game.player.actions.smithing.rework;

import java.util.HashMap;
import java.util.Map;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.npc.random.DwarvenMinerNPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.actions.smithing.Smelting;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.item.ItemIdentifiers;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class SmeltingNew extends Action {
	
	public static final int[] NEW_ORES = { ItemIdentifiers.ORICHALCITE_ORE, ItemIdentifiers.DRAKOLITH, ItemIdentifiers.NECRITE_ORE, ItemIdentifiers.PHASMATITE, ItemIdentifiers.DRAGONBANE_ORE,
			ItemIdentifiers.BANE_ORE, ItemIdentifiers.LIGHT_ANIMICA, ItemIdentifiers.DARK_ANIMICA, ItemIdentifiers.ABYSSALBANE_ORE, ItemIdentifiers.BASILISKBANE_ORE, ItemIdentifiers.WALLASALKIBANE_ORE};
	
	public enum SmeltingBarNew {

		ORICHALCITE_ORE(new Item[] { new Item(ItemIdentifiers.ORICHALCITE_ORE, 1), new Item(ItemIdentifiers.DRAKOLITH, 1) }, new Item(ItemIdentifiers.ORIKALKUM_BAR, 1), 0),
		
		NECRONIUM_ORE(new Item[] { new Item(ItemIdentifiers.NECRITE_ORE, 1), new Item(ItemIdentifiers.PHASMATITE, 1) }, new Item(ItemIdentifiers.NECRONIUM_BAR, 1), 1), 
		
		BANE_ORE(new Item[] { new Item(ItemIdentifiers.BANE_ORE, 2) }, new Item(ItemIdentifiers.BANE_BAR, 1), 2), 
		
		ELDER_RUNE_ORE(new Item[] { new Item(ItemIdentifiers.RUNE_BAR, 1), new Item(ItemIdentifiers.LIGHT_ANIMICA, 1), new Item(ItemIdentifiers.DARK_ANIMICA, 1),}, new Item(ItemIdentifiers.ELDER_RUNE_BAR, 1), 3),
		
		ABYSSAL_BANE_ORE(new Item[] { new Item(ItemIdentifiers.ABYSSALBANE_ORE, 1), }, new Item(ItemIdentifiers.ABYSSALBANE_BAR, 1), 4),
		
		BASILISKBANE_ORE(new Item[] { new Item(ItemIdentifiers.BASILISKBANE_ORE, 1), }, new Item(ItemIdentifiers.BASILISKBANE_BAR, 1), 5),
		
		DRAGONBANE_ORE(new Item[] { new Item(ItemIdentifiers.DRAGONBANE_ORE, 1), }, new Item(ItemIdentifiers.DRAGONBANE_BAR, 1), 6),
		
		WALLASALKIBANE_ORE(new Item[] { new Item(ItemIdentifiers.WALLASALKIBANE_ORE, 1), }, new Item(ItemIdentifiers.WALLASALKIBANE_BAR, 1), 7);

		private static Map<Integer, SmeltingBarNew> bars = new HashMap<Integer, SmeltingBarNew>();

		static {
			for (SmeltingBarNew bar : SmeltingBarNew.values()) {
				bars.put(bar.getButtonId(), bar);
			}
		}

		public static SmeltingBarNew forId(int buttonId) {
			return bars.get(buttonId);
		}

		private Item[] itemsRequired;
		private int buttonId;
		private Item producedBar;

		private SmeltingBarNew(Item[] itemsRequired, Item producedBar, int buttonId) {
			this.itemsRequired = itemsRequired;
			this.producedBar = producedBar;
			this.buttonId = buttonId;
		}

		public int getButtonId() {
			return buttonId;
		}

		public double getExperience() {
		    return ((double) getProducedBar().getDefinitions().getCSOpcode(SmithingConstants.ITEM_CREATE_XP_OP) / 10.00);
		}

		public Item[] getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return getProducedBar().getDefinitions().getCSOpcode(SmithingConstants.SMITHING_LEVEL_REQUIREMENT_OP);
		}

		public Item getProducedBar() {
			return producedBar;
		}
		
		public static SmeltingBarNew getBarByProduce(int id) {
			for (SmeltingBarNew bar : SmeltingBarNew.values()) {
				if (bar.getProducedBar().getId() == id) {
					if (Settings.PRINTS)
						System.err.println(bar);
					return bar;
				}
			}
			for (SmeltingBarNew bar : SmeltingBarNew.values()) {
				switch (bar.getProducedBar().getId()) {
				case 995:
				case 6577:
				case 31869:
				case 31871:
				case 31878:
				case 31875:
					return bar;
				}
			}
			return null;
		}

		public static SmeltingBarNew getBar(int id) {
			for (SmeltingBarNew bar : SmeltingBarNew.values()) {
				for (Item item : bar.getItemsRequired())
					if (item.getId() == id)
						return bar;
			}
			return null;
		}

		public static SmeltingBarNew getBar(Player player) {
			for (SmeltingBarNew bar : SmeltingBarNew.values()) {
				for (Item item : bar.getItemsRequired())
					if (!player.getInventory().containsItems(new Item(item.getId())))
						return bar;
			}
			return null;
		}
	}

	public SmeltingBarNew bar;
	public WorldObject object;
	public int ticks;

	public SmeltingNew(int slotId, WorldObject object, int ticks) {
		bar = SmeltingBarNew.forId(slotId);
		this.object = object;
		this.ticks = ticks;
	}

	@Override
	public boolean process(Player player) {
		if (bar == null || player == null || object == null) {
			return false;
		}
		if (!player.getInventory().containsItem(bar.getItemsRequired()[0].getId(),
				bar.getItemsRequired()[0].getAmount())) {
			player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[0].getDefinitions().getName().toLowerCase() + " to create a " + bar.getProducedBar().getDefinitions().getName().toLowerCase() + ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(bar.getItemsRequired()[1].getId(), bar.getItemsRequired()[1].getAmount())) {
				player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[1].getDefinitions().getName().toLowerCase() + " to create a " + bar.getProducedBar().getDefinitions().getName().toLowerCase() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of at least " + bar.getLevelRequired() + " to smelt " + bar.getProducedBar().getDefinitions().getName().toLowerCase());
			return false;
		}
		if (Utils.random(500) == 0) {
			new DwarvenMinerNPC(player, player);
			player.getPackets().sendGameMessage("<col=ff0000>A Dwarven Miner appears from nowhere.");
		}
		player.faceObject(object);
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int amount = 1;
		if (player.getPerkManager().hasPerk(PlayerPerks.ARCANE_SMELTING)) {
			player.setNextAnimation(new Animation(20292));
			player.setNextGraphics(new Graphics(4000));
		} else {
			player.setNextAnimation(new Animation(32626));
		}
		player.getSkills().addXp(Skills.SMITHING, getExp(player));
		for (Item required : bar.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		player.getStatistics().addBarsSmelt();
		player.getPackets().sendGameMessage("You retrieve a bar of " + bar.getProducedBar().getDefinitions().getName().toLowerCase().replace(" bar", "") + "; " 	+ "bars smelt: " + Colors.RED + Utils.getFormattedNumber(player.getStatistics().getBarsSmelt()) + "</col>.", true);
		player.getInventory().addItem(bar.getProducedBar().getId(), amount);
		if (ticks > 0) {
			return 1;
		}
		return -1;
	}

	@Override
	public boolean start(Player player) {
		if (bar == null || player == null || object == null) {
			return false;
		}
		if (!player.getInventory().containsItem(bar.getItemsRequired()[0].getId(), bar.getItemsRequired()[0].getAmount())) {
			player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[0].getDefinitions().getName().toLowerCase() + " to create a " + bar.getProducedBar().getDefinitions().getName().toLowerCase() + ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(bar.getItemsRequired()[1].getId(), bar.getItemsRequired()[1].getAmount())) {
				player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[1].getDefinitions().getName().toLowerCase() + " to create a " + bar.getProducedBar().getDefinitions().getName().toLowerCase() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
			player.getPackets().sendGameMessage("You need a Smithing level of at least " + bar.getLevelRequired() + " to smelt " + bar.getProducedBar().getDefinitions().getName().toLowerCase());
			return false;
		}
		player.getPackets().sendGameMessage("You place the required ores and attempt to create a bar of " + bar.getProducedBar().getDefinitions().getName().toLowerCase().replace(" bar", "") + ".", true);
		return true;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	private double getExp(Player player) {
		double xp = bar.getExperience();
		xp *= Smelting.blackSmithSuit(player);
		if (object.getId() == 97270) {
			xp *= 1.1;
		}
		return xp;
	}
}