package com.rs.game.player.actions.smithing;

import java.util.HashMap;
import java.util.Map;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.random.DwarvenMinerNPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.actions.smithing.Smithing.ForgingBar;
import com.rs.game.player.actions.smithing.rework.SmithingConstants;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.events.GlobalEvents.Event;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.managers.CompletionistCapeManager.Requirement;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class Smelting extends Action {
	
	public static final int[] OLD_ORES = { ItemIdentifiers.COPPER_ORE, ItemIdentifiers.TIN_ORE,
			ItemIdentifiers.IRON_ORE, ItemIdentifiers.COAL, ItemIdentifiers.SILVER_ORE,
			ItemIdentifiers.MITHRIL_ORE, ItemIdentifiers.ADAMANTITE_ORE, ItemIdentifiers.RUNITE_ORE};


	public enum SmeltingBar {

		BRONZE(new Item[] { new Item(436), new Item(438) }, new Item(2349), 0),

		IRON(new Item[] { new Item(440) }, new Item(2351), 1),

		SILVER(new Item[] { new Item(442) }, new Item(2355), 2),

		STEEL(new Item[] { new Item(440), new Item(453, 2) }, new Item(2353), 3),

		GOLD(new Item[] { new Item(444) }, new Item(2357), 4),

		MITHRIL(new Item[] { new Item(447), new Item(453, 4) }, new Item(2359), 5),

		ADAMANT(new Item[] { new Item(449), new Item(453, 6) }, new Item(2361), 6),

		RUNE(new Item[] { new Item(451), new Item(453, 8) }, new Item(2363), 7),

		CORRUPTED_ORE(new Item[] { new Item(32262) }, new Item(32262), 8),

		CANNON_BALLS(new Item[] { new Item(2353, 1), new Item(4, 1) }, new Item(2, 4), 9);

		private static Map<Integer, SmeltingBar> bars = new HashMap<Integer, SmeltingBar>();

		static {
			for (SmeltingBar bar : SmeltingBar.values()) {
				bars.put(bar.getButtonId(), bar);
			}
		}

		public static SmeltingBar forId(int buttonId) {
			return bars.get(buttonId);
		}
		
		private Item[] itemsRequired;
		private int buttonId;
		private Item producedBar;

		private SmeltingBar(Item[] itemsRequired, Item producedBar, int buttonId) {
			this.itemsRequired = itemsRequired;
			this.producedBar = producedBar;
			this.buttonId = buttonId;
		}

		public int getButtonId() {
			return buttonId;
		}

		public double getExperience() {
			return getProducedBar().getDefinitions().getCSOpcode(SmithingConstants.ITEM_CREATE_XP_OP) / 10;
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
	}
	
	public SmeltingBar bar;
	public WorldObject object;
	public int ticks;

	public Smelting(int slotId, WorldObject object, int ticks) {
		bar = SmeltingBar.forId(slotId);
		this.object = object;
		this.ticks = ticks;
	}

	public boolean isSuccessFull(Player player) {
		if (bar == SmeltingBar.IRON) {
			if (player.getEquipment().getItem(Equipment.SLOT_RING) != null
					&& player.getEquipment().getItem(Equipment.SLOT_RING).getId() == 2568) {
				return true;
			} else {
				return Utils.getRandom(100) <= (player.getSkills().getLevel(Skills.SMITHING) >= 45 ? 80 : 50);
			}
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (bar == null || player == null || object == null) {
			return false;
		}
		if (!player.getInventory().containsItem(bar.getItemsRequired()[0].getId(), bar.getItemsRequired()[0].getAmount())) {
			player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[0].getDefinitions().getName() + " to create a "
					+ bar.getProducedBar().getDefinitions().getName() + ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(bar.getItemsRequired()[1].getId(), bar.getItemsRequired()[1].getAmount())) {
				player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[1].getDefinitions().getName() + " to create a "
						+ bar.getProducedBar().getDefinitions().getName() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of at least "
					+ bar.getLevelRequired() + " to smelt " + bar.getProducedBar().getDefinitions().getName());
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
		int amount = bar.getProducedBar().getAmount();
		if (player.getPerkManager().hasPerk(PlayerPerks.ARCANE_SMELTING)) {
			player.setNextAnimation(new Animation(20292));
			player.setNextGraphics(new Graphics(4000));
		} else {
			player.setNextAnimation(new Animation(32626));
		}
		player.getSkills().addXp(Skills.SMITHING, getExp(player));
		for (Item required : bar.getItemsRequired()) {
			if (required.getId() == 4) {
				continue;
			}
			player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (isSuccessFull(player)) {
			if (bar != SmeltingBar.CORRUPTED_ORE) {
				player.getStatistics().addBarsSmelt();
				if (object.getId() == 97270 && Utils.random(100) > 90) {
					player.getPackets().sendGameMessage(Colors.ORANGE+"<shad=000000>You managed to make an extra bar from your " + "ore in this exceptional furnace! It has been sent directly to your bank.");
					player.getBank().addItem(bar.getProducedBar().getId(), 1, true);
				}
				if (bar == SmeltingBar.CANNON_BALLS) {
					if (GlobalEvents.isActiveEvent(Event.CANNONBALL)) {
						amount *= 2;
					}
					player.getCompCapeManager().increaseRequirement(Requirement.CANNON_BALLS, 1);
					for (int i = 0; i < 4; i++) {
						player.getSkillTasks().decreaseTask(SkillTasks.SCANNON1);
						player.getSkillTasks().decreaseTask(SkillTasks.SCANNON2);
						player.getSkillTasks().decreaseTask(SkillTasks.SCANNON3);
						player.getSkillTasks().decreaseTask(SkillTasks.SCANNON4);
					}
				} else if (bar == SmeltingBar.BRONZE) {
					if (player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().tutorialStage == 13) {
						player.getStatistics().tutorialStage = 14;
						Dialogue.sendNPCDialogueNoContinue(player, 948, Dialogue.NORMAL, "Great, now let me see that bar you smelted.");
					}
					player.getSkillTasks().decreaseTask(SkillTasks.SBRONZE1);
					player.getSkillTasks().decreaseTask(SkillTasks.SBRONZE2);
				} else if (bar == SmeltingBar.IRON) {
					player.getSkillTasks().decreaseTask(SkillTasks.SIRON1);
					player.getSkillTasks().decreaseTask(SkillTasks.SIRON2);
				} else if (bar == SmeltingBar.STEEL) {
					player.getSkillTasks().decreaseTask(SkillTasks.SSTEEL1);
					player.getSkillTasks().decreaseTask(SkillTasks.SSTEEL2);
				} else if (bar == SmeltingBar.SILVER) {
					player.getSkillTasks().decreaseTask(SkillTasks.SSILVER1);
					player.getSkillTasks().decreaseTask(SkillTasks.SSILVER2);
				} else if (bar == SmeltingBar.GOLD) {
					player.getSkillTasks().decreaseTask(SkillTasks.SGOLD1);
					player.getSkillTasks().decreaseTask(SkillTasks.SGOLD2);
					player.getSkillTasks().decreaseTask(SkillTasks.SGOLD3);
				} else if (bar == SmeltingBar.MITHRIL) {
					player.getSkillTasks().decreaseTask(SkillTasks.SMITHRIL1);
					player.getSkillTasks().decreaseTask(SkillTasks.SMITHRIL2);
					player.getSkillTasks().decreaseTask(SkillTasks.SMITHRIL3);
				} else if (bar == SmeltingBar.ADAMANT) {
					player.getSkillTasks().decreaseTask(SkillTasks.SADAMANT1);
					player.getSkillTasks().decreaseTask(SkillTasks.SADAMANT2);
				} else if (bar == SmeltingBar.RUNE) {
					player.getSkillTasks().decreaseTask(SkillTasks.SRUNE1);
					player.getSkillTasks().decreaseTask(SkillTasks.SRUNE2);

				}
				player.getPackets().sendGameMessage("You retrieve a bar of " + bar.getProducedBar().getDefinitions().getName().toLowerCase().replace(" bar", "") + "; " 	+ "bars smelt: " + Colors.RED + Utils.getFormattedNumber(player.getStatistics().getBarsSmelt()) + "</col>.", true);
				player.getInventory().addItem(bar.getProducedBar().getId(), amount);
			} else {
				player.getPackets().sendGameMessage("You've successfully smelt the Corrupted ore.", true);
			}
		} else {
			player.getPackets().sendGameMessage("The ore is too impure and you fail to refine it.", true);
		}
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
		if (!player.getInventory().containsItem(bar.getItemsRequired()[0].getId(),
				bar.getItemsRequired()[0].getAmount())) {
			player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[0].getDefinitions().getName() + " to create a "
					+ bar.getProducedBar().getDefinitions().getName() + ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(bar.getItemsRequired()[1].getId(),
					bar.getItemsRequired()[1].getAmount())) {
				player.getPackets().sendGameMessage("You need " + bar.getItemsRequired()[1].getDefinitions().getName() + " to create a "
						+ bar.getProducedBar().getDefinitions().getName() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
			player.getPackets().sendGameMessage("You need a Smithing level of at least " + bar.getLevelRequired() + " to smelt "
					+ bar.getProducedBar().getDefinitions().getName());
			return false;
		}
		if (bar != SmeltingBar.CORRUPTED_ORE) {
			player.getPackets().sendGameMessage("You place the required ore in the furnace and attempt to smelt it.", true);
		} else {
			player.getPackets().sendGameMessage(
					"You place the required ores and attempt to create a bar of "
							+ bar.getProducedBar().getDefinitions().getName().toLowerCase().replace(" bar", "") + ".",
					true);
		}
		return true;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	/**
	 * Calculates how much experience should the player receive.
	 * 
	 * @param player
	 *            The player smelting.
	 * @return the EXP as Double.
	 */
	private double getExp(Player player) {
		double xp = bar.getExperience();
		xp *= blackSmithSuit(player);
		if (object.getId() == 97270) {
			xp *= 1.1;
		}
		return xp;
	}

	/**
	 * XP modifier by wearing items.
	 * 
	 * @param player
	 *            The player.
	 * @return the XP modifier.
	 */
	public static double blackSmithSuit(Player player) {
		double xpBoost = 1.0;
		if (player.getEquipment().getHatId() == 25195) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getChestId() == 25196) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getLegsId() == 25197) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getBootsId() == 25198) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getGlovesId() == 25199) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getHatId() == 25195 && player.getEquipment().getChestId() == 25196
				&& player.getEquipment().getLegsId() == 25197 && player.getEquipment().getBootsId() == 25198
				&& player.getEquipment().getGlovesId() == 25199) {
			xpBoost *= 1.01;
		}
		return xpBoost;
	}
}