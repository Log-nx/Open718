package com.rs.game.player;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.World;
import com.rs.utils.Utils;

public class MysteryBox {

	private static final int[] RANDOM_REWARDS = {
	18349, 18351, 18353, 18355, 18357, 18359,
	7927, 1464, 24365, 25320, 25321, 11724, 11720,
	18768};
	private static final int[] SUPER_REWARDS = {
	1053, 1055, 1057, 1037, 1419, 15426, 14595, 14603,
	18349, 18351, 18353, 18355, 18357, 18359,
	7927, 1464, 24365, 25320, 25321, 11724, 11720,
	18768};
	private static final int[] VERY_SUPER_REWARDS = {
	18349, 18351, 18353, 18355, 18357, 18359,
	7927, 1464, 24365, 25320, 25321, 11724, 11720,
	1038, 1040, 1042, 1044, 1046, 1048, 1050,
	18768};
	public static final int BOX = 18768;

	public static int getReward;

	public static boolean canOpen(Player player) {
		if (player.getInventory().containsItem(18768, 1))
			return true;
		return false;
	}

	public static void openBox(Player player) {
		if (canOpen(player)) {
			int randomReward = SUPER_REWARDS[Utils.random(SUPER_REWARDS.length - 1)];
			getReward = randomReward;
			if (Utils.random(3) == 0) {
				randomReward = RANDOM_REWARDS[Utils.random(RANDOM_REWARDS.length - 1)];
				getReward = randomReward;
			} else if (Utils.random(20) < 5) {
				randomReward = VERY_SUPER_REWARDS[Utils.random(VERY_SUPER_REWARDS.length - 1)];
				getReward = randomReward;
			} else if (Utils.random(300) < 5) {
				randomReward = SUPER_REWARDS[Utils.random(SUPER_REWARDS.length - 1)];
				getReward = randomReward;
			}
			ItemDefinitions defs = ItemDefinitions.getItemDefinitions(randomReward);
			String name = defs == null ? "" : defs.getName().toLowerCase();
			player.getInventory().deleteItem(BOX, 1);
			player.getInventory().addItem(randomReward, 1);
			player.getDialogueManager().startDialogue("SimpleMessage", "You've received  <shad=000000><col=8000ff>" + name + "</col></shad> from the Mystery Box.");
				World.sendWorldMessage("<shad=000000><col=8000ff>Mystery Box</col></shad>: " + player.getDisplayName() + " received <shad=000000><col=8000ff>" + name + "</col></shad> from the Mystery Box.", false, false);
		}
	}
}