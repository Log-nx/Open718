package com.rs.game.player.actions;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;

public class PlanksCutting extends Action {

	public enum Planks {
		
		PLANK(1511, 960, 5, 1, 9031), //OPAL

		OAK_PLANK(1521, 8778, 10, 15, 9031), //JADE

		TEAK_PLANK(1515, 8780, 17, 60, 9031), //RED

		MOHAGANY_PLANK(1513, 8782, 35, 75, 9031); //SAPPHIRE

		private double experience;
		private int levelRequired;
		private int plank, cut;
		private int emote;

		private Planks(int plank, int cut, double experience, int levelRequired,
				int emote) {
			this.plank = plank;
			this.cut = cut;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.emote = emote;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public int getPlank() {
			return plank;
		}

		public int getCut() {
			return cut;
		}

		public int getEmote() {
			return emote;
		}

	}

	public static void cut(Player player, Planks planks) {
		if (player.getInventory().getItems()
				.getNumberOf(new Item(planks.getPlank(), 1)) <= 1) // contains just
			// 1 lets start
			player.getActionManager().setAction(new PlanksCutting(planks, 1));
		else
			player.getDialogueManager().startDialogue("PlanksCuttingD", planks);
	}

	private Planks planks;
	private int quantity;

	public PlanksCutting(Planks planks, int quantity) {
		this.planks = planks;
		this.quantity = quantity;
	}

	public boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.CONSTRUCTION) < planks
				.getLevelRequired()) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a construction level of " + planks.getLevelRequired()
					+ " to cut that planks.");
			return false;
		}
		if (!player.getInventory().containsOneItem(planks.getPlank())) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You don't have any "
							+ ItemDefinitions
							.getItemDefinitions(planks.getPlank())
							.getName().toLowerCase() + " to cut.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			player.setNextAnimation(new Animation(planks.getEmote()));
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		player.getInventory().deleteItem(planks.getPlank(), 1);
		player.getInventory().addItem(planks.getCut(), 1);
		player.getSkills().addXp(Skills.CONSTRUCTION, planks.getExperience());
		player.getPackets().sendGameMessage(
				"You cut the "
						+ ItemDefinitions.getItemDefinitions(planks.getPlank())
						.getName().toLowerCase() + ".", true);
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(new Animation(planks.getEmote())); // start the
		player.setNextGraphics(new Graphics(56));
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}
