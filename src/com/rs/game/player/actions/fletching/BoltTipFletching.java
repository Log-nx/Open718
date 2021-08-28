package com.rs.game.player.actions.fletching;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.utils.Utils;

/**
 * Handles cutting Gems into Bolt Tips.
 * @author BigFuckinChungus
 */
public class BoltTipFletching extends Action {

	public enum BoltTips {
		
		OPAL(1609, 45, 15.0, 11, 12, 886),

		JADE(1611, 9187, 20, 26, 12, 886),

		PEARL(411, 46, 20, 41, 6, 886),

		PEARLS(413, 46, 20, 41, 24, 886),

		RED_TOPAZ(1613, 9188, 25, 48, 12, 887),

		SAPPHIRE(1607, 9189, 50, 56, 12, 888),

		EMERALD(1605, 9190, 67, 58, 12, 889),

		RUBY(1603, 9191, 85, 63, 12, 887),

		DIAMOND(1601, 9192, 107.5, 65, 12, 890),

		DRAGONSTONE(1615, 9193, 137.5, 71, 12, 885),

		ONYX(6573, 9194, 167.5, 73, 24, 2717),

		HYDRIX(31855, 31867, 10.6, 80, 36, 2717); //For 896

		private double experience;
		private int levelRequired, amount, emote;
		private int gemName, tipName;

		private BoltTips(int gemName, int tipName, double experience, int levelRequired, int amount, int emote) {
			this.gemName = gemName;
			this.tipName = tipName;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.amount = amount;
			this.emote = emote;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}
		
		public int getGemName() {
			return gemName;
		}

		public int getTipName() {
			return tipName;
		}

		public int getAmount() {
			return amount;
		}

		public int getEmote() {
			return emote;
		}

	}
	
	public static boolean boltFletch(Player player, int itemId) {
		for (BoltTips b : BoltTips.values()) {
			if (b.getGemName() == itemId) {
				if (player.getInventory().getItems()
						.getNumberOf(new Item(itemId, 1)) <= 1) // contains
																			// just
					// 1 lets start
					player.getActionManager().setAction(new BoltTipFletching(getBoltTips(itemId), 1));
				else
					player.getDialogueManager().startDialogue("BoltTipFletchingD", getBoltTips(itemId));
				return true;
			}
		}
		return false;
	}
	
	public static BoltTips getBoltTips(int id) {
		for (BoltTips b : BoltTips.values()) {
			if (b.getGemName() == id)
				return b;
		}
		return null;
	}

	private BoltTips tips;
	private int quantity;

	public BoltTipFletching(BoltTips tips, int quantity) {
		this.tips = tips;
		this.quantity = quantity;
	}

	public boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.FLETCHING) < tips.getLevelRequired()) {
			player.getPackets().sendGameMessage("You need a Fletching level of " + tips.getLevelRequired() + " to cut that gem into Bolt Tips.");
			return false;
		}
		if (!player.getInventory().containsOneItem(tips.getGemName())) {
			player.getPackets().sendGameMessage("You don't have any " + ItemDefinitions.getItemDefinitions(tips.getGemName()).getName().toLowerCase() + " to cut into Bolt Tips.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			player.setNextAnimation(new Animation(tips.getEmote()));
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
		player.getInventory().deleteItem(tips.getGemName(), 1);
		player.getInventory().addItemDrop(tips.getTipName(), tips.getAmount());
		player.getSkills().addXp(Skills.FLETCHING, tips.getExperience() / 4);
		player.getPackets().sendGameMessage("You cut the "+ ItemDefinitions.getItemDefinitions(
				tips.getGemName()).getName().toLowerCase() + " into "
				+ ItemDefinitions.getItemDefinitions(tips.getTipName()).getName().toLowerCase());
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(new Animation(tips.getEmote()));
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}