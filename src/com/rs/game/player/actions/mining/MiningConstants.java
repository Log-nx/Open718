package com.rs.game.player.actions.mining;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.utils.Utils;

public class MiningConstants {
	
	public static enum Geode {
		SEDIMENTARY_GEODE(44816, new double[] { 12183, 1, 50 }, new double[] { 1625, 1, 20 },
				new double[] { 1627, 1, 12.5 }, new double[] { 1629, 1, 7.5 }, new double[] { 1623, 1, 5 },
				new double[] { 1621, 1, 2.5 }, new double[] { 1619, 1, 1.5 }, new double[] { 1617, 1, 1 }),

		IGNEOUS_GEODE(44817, new double[] { 12183, 1, 50 }, new double[] { 1625, 1, 5 }, new double[] { 1627, 1, 6 },
				new double[] { 1629, 1, 7 }, new double[] { 1623, 1, 8 }, new double[] { 1621, 1, 9 },
				new double[] { 1619, 1, 8 }, new double[] { 1617, 1, 5 }, new double[] { 1631, 1, 2 }),

		METAMORPHIC_GEODE(44818, new double[] { 36918, 1, 7 }, new double[] { 31867, 25, 50, 7 },
				new double[] { 11732, 1, 0.2 }, new double[] { 2513, 1, 0.2 }, new double[] { 11335, 1, 0.2 },
				new double[] { 1149, 1, 0.2 }, new double[] { 24365, 1, 0.2 }, new double[] { 4087, 1, 0.2 },
				new double[] { 4585, 1, 0.2 }, new double[] { 1187, 1, 0.2 }, new double[] { 45985, 1, 10, 7 },
				new double[] { 45986, 1, 7 }, new double[] { 6571, 1, 0.7 }, new double[] { 32262, 100, 300, 7 },
				new double[] { 7158, 1, 0.2 }, new double[] { 1377, 1, 0.2 },

				new double[] { 14484, 1, 0.2 }, new double[] { 3204, 1, 0.2 }, new double[] { 31377, 1, 0.2 },
				new double[] { 1305, 1, 0.2 }, new double[] { 1434, 1, 0.2 }, new double[] { 4587, 1, 0.2 },
				new double[] { 1249, 1, 0.2 }, new double[] { 29534, 1, 0.2 }, new double[] { 25776, 1, 0.2 },

				new double[] { 25555, 1, 0.2 }, new double[] { 25740, 1, 0.2 }, new double[] { 25689, 1, 0.2 },
				new double[] { 25758, 1, 0.2 }, new double[] { 29537, 1, 0.2 });

		private int id;
		private double[][] rewards;

		private Geode(int id, double[]... rewards) {
			this.id = id;
			this.rewards = rewards;
		}

		public int getId() {
			return id;
		}

		public double[][] getRewards() {
			return rewards;
		}

		public static Geode getGeode(int id) {
			for (Geode geode : Geode.values())
				if (geode.id == id)
					return geode;
			return null;
		}

	}

	public static Item generateReward(Geode geode) {
		List<Item> possibleRewards = new ArrayList<Item>();
		while (possibleRewards.isEmpty()) {
			double roll = Math.random() * 100;
			for (double[] rewards : geode.rewards) {
				Item reward = new Item((int) rewards[0],
						rewards.length == 4 ? Utils.random((int) rewards[1], (int) rewards[2] + 1) : (int) rewards[1]);
				double chance = rewards.length == 4 ? rewards[3] : rewards[2];
				if (roll <= chance)
					possibleRewards.add(reward);
			}
		}
		return possibleRewards.get(Utils.random(possibleRewards.size()));
	}

	public static void openGeode(Player player, Geode geode) {
		openGeode(player, geode, 1);
	}

	public static void openGeode(Player player, Geode geode, int amount) {
		if (amount == 1) {
			if (player.getInventory().getAmountOf(geode.id) > 1 && player.getInventory().getFreeSlots() == 0) {
				player.getPackets().sendGameMessage("You dont have enough space to do that.");
				return;
			}
			Item reward = MiningConstants.generateReward(geode);
			player.getInventory().deleteItem(geode.id, 1);
			player.getInventory().addItem(reward);
			player.getDialogueManager().startDialogue("SimpleItemMessage",	reward.getDefinitions().isNoted() ? reward.getDefinitions().certId : reward.getId(), "You break your geode open to find:<br>" + reward.getName());
			return;
		}
		player.getActionManager().setAction(new Action() {

			@Override
			public boolean start(Player player) {
				if (!checkAll(player))
					return false;
				return true;
			}

			public boolean checkAll(Player player) {
				if (player.getInventory().getAmountOf(geode.id) > 1 && player.getInventory().getFreeSlots() == 0)
					return false;
				if (player.getInventory().getAmountOf(geode.id) == 0)
					return false;
				return true;
			}

			@Override
			public boolean process(Player player) {
				return checkAll(player);
			}

			@Override
			public int processWithDelay(Player player) {
				if (!checkAll(player))
					return -1;
				Item reward = MiningConstants.generateReward(geode);
				player.getInventory().deleteItem(geode.id, 1);
				player.getInventory().addItem(reward);
				player.getDialogueManager().startDialogue("SimpleItemMessage", reward.getId(), "You break your geode open to find:<br> " + reward.getName());
				return 2;
			}

			@Override
			public void stop(Player player) {
				setActionDelay(player, 2);
			}

		});
	}
}
