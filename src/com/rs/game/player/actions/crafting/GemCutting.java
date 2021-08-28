package com.rs.game.player.actions.crafting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.managers.CompletionistCapeManager.Requirement;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class GemCutting extends Action {

	/**
	 * Enum for gems
	 * 
	 * @author Raghav
	 * 
	 */
	public enum Gem {
		OPAL(1625, 1609, 15.0, 1, 886),

		JADE(1627, 1611, 20, 13, 886),

		RED_TOPAZ(1629, 1613, 25, 16, 887),

		SAPPHIRE(1623, 1607, 50, 20, 888),

		EMERALD(1621, 1605, 67, 27, 889),

		RUBY(1619, 1603, 85, 34, 887),

		DIAMOND(1617, 1601, 107.5, 43, 890),

		DRAGONSTONE(1631, 1615, 137.5, 55, 885),

		ONYX(6571, 6573, 197.5, 79, 2717),
		
		HYDRIX(31853, 31855, 167.5, 67, 2717),

		LIMESTONE(3211, 3420, 10, 12, 2717),
		
		GRAY_SHELL_ROUND(3345, 3327, 35.5, 15, -1),

		RED_AND_BLACK_SHELL_ROUND(3347, 3329, 35.5, 15, -1),

		OCHRE_SHELL_ROUND(3349, 3331, 35.5, 15, -1),

		BLUE_SHELL_ROUND(3351, 3333, 35.5, 15, -1),

		BROKEN_SHELL_ROUND(3353, 3335, 35.5, 15, -1),

		GRAY_SHELL_POINTY(3355, 3337, 35.5, 15, -1),

		RED_AND_BLACK_SHELL_POINTY(3357, 3339, 35.5, 15, -1),

		OCHRE_SHELL_POINTY(3359, 3341, 35.5, 15, -1),

		BLUE_SHELL_POINTY(3361, 3343, 35.5, 15, -1);

		private double experience;
		private int levelRequired;
		private int uncut, cut;

		private int emote;

		Gem(int uncut, int cut, double experience, int levelRequired, int emote) {
			this.uncut = uncut;
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

		public int getUncut() {
			return uncut;
		}

		public int getCut() {
			return cut;
		}

		public int getEmote() {
			return emote;
		}

	}

	public static void cut(Player player, Gem gem) {
		if (player.getInventory().getItems().getNumberOf(new Item(gem.getUncut(), 1)) <= 1) // contains
																							// just
			// 1 lets start
			player.getActionManager().setAction(new GemCutting(gem, 1));
		else
			player.getDialogueManager().startDialogue("GemCuttingD", gem);
	}

	public static boolean cutGem(Player player, int itemId) {
		for (Gem g : Gem.values()) {
			if (g.getUncut() == itemId) {
				if (player.getInventory().getItems().getNumberOf(new Item(g.getUncut(), 1)) <= 1) // contains
					player.getActionManager().setAction(new GemCutting(g, 1));
				else
					player.getDialogueManager().startDialogue("GemCuttingD", g);
				return true;
			}
		}
		return false;
	}

	private Gem gem;
	private int quantity;

	public GemCutting(Gem gem, int quantity) {
		this.gem = gem;
		this.quantity = quantity;
	}

	public boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < gem.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a crafting level of " + gem.getLevelRequired() + " to cut that gem.");
			return false;
		}
		if (!player.getInventory().containsOneItem(gem.getUncut())) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You don't have any " + ItemDefinitions.getItemDefinitions(gem.getUncut()).getName().toLowerCase() + " to cut.");
			return false;
		}
		if (!(player.getInventory().containsItemToolBelt(1755, 1) || player.getInventory().containsItemToolBelt(32642, 1))) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You must have a chisel in order to cut this.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			player.setNextAnimation(new Animation(gem.getEmote()));
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
		player.getInventory().deleteItem(gem.getUncut(), 1);
		player.getInventory().addItem(gem.getCut(), 1);
		if (gem == Gem.SAPPHIRE) {
			player.getSkillTasks().decreaseTask(SkillTasks.SAPPHIRE1);
			player.getSkillTasks().decreaseTask(SkillTasks.SAPPHIRE2);
		} else if (gem == Gem.EMERALD) {
			player.getSkillTasks().decreaseTask(SkillTasks.EMERALD1);
			player.getSkillTasks().decreaseTask(SkillTasks.EMERALD2);
		} else if (gem == Gem.RUBY) {
			player.getSkillTasks().decreaseTask(SkillTasks.RUBY1);
			player.getSkillTasks().decreaseTask(SkillTasks.RUBY2);
			player.getSkillTasks().decreaseTask(SkillTasks.RUBY3);
		} else if (gem == Gem.DIAMOND) {
			player.getSkillTasks().decreaseTask(SkillTasks.DIAMOND1);
			player.getSkillTasks().decreaseTask(SkillTasks.DIAMOND2);
			player.cutDiamonds++;
			player.getCompCapeManager().increaseRequirement(Requirement.GEM_CUTTING, 1);
		}
		player.getSkills().addXp(Skills.CRAFTING, gem.getExperience());
		player.getStatistics().addItemsMade();
		player.getPackets().sendGameMessage("You cut the " + ItemDefinitions.getItemDefinitions(gem.getUncut()).getName().toLowerCase()
				+ "; items crafted: " + Colors.RED + Utils.getFormattedNumber(player.getStatistics().getItemsMade()) + "</col>.", true);
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(new Animation(gem.getEmote()));
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}