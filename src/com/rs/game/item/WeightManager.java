package com.rs.game.item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;

public class WeightManager {

	private static Map<Integer, Double> itemWeight = new HashMap<>();

	private static final int[] WEIGHT_REDUCERS = { 88, 10069, 10073, 10663,
			10071, 10074, 10664, 10553, 10554, 24210, 24211, 14938, 14939,
			24208, 24209, 14936, 14937, 24206, 24207, 24560, 24561, 24562,
			24563, 24654, 24801, 24802, 24803, 24804, 24805 };

	public static void init() {
		try (BufferedReader reader = new BufferedReader(new FileReader(
				"./data/items/weights.txt"))) {
			while (true) {
				String file = reader.readLine();
				if (file == null) {
					break;
				}
				if (file.startsWith("//")) {
					continue;
				}
				String[] values = file.split(" - ");
				int itemId = Integer.valueOf(values[0]);
				double weight = Double.parseDouble(values[1]);
				if (ItemDefinitions.getItemDefinitions(itemId).isNoted())
					continue;
				itemWeight.put(itemId,
						weight);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double calculateWeight(Player player) {
		for (int REDUCERS : WEIGHT_REDUCERS) {
			if (player.getEquipment().getItems().contains(new Item(REDUCERS))) {
				player.setWeight(player.getWeight() - getWeight(REDUCERS));
			}
		}
		for (Item item : player.getInventory().getItems().getItems()) {
			if (item == null || item.getId() <= 0)
				continue;
			player.setWeight(player.getWeight() + getWeight(item.getId()) * item.getAmount());
		}
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null || item.getId() <= 0)
				continue;
			player.setWeight(player.getWeight() + getWeight(item.getId()) * item.getAmount());
		}
		if (!player.hasStarted())
			player.getPackets().sendWeight(player.getWeight());
		return player.getWeight();
	}

	private static double getWeight(int itemId) {
		if (itemWeight.get(itemId) == null)
			return 0;
		return itemWeight.get(itemId);
	}

}