package com.rs.utils;

import java.io.IOException;
import java.util.HashMap;

public final class ItemWeights {
	
	private static final HashMap<Integer, Double> itemWeights = new HashMap<Integer, Double>();
	
	public static final void init() {
		try {
			for (String lines : FileUtilities
					.readFile("./data/items/weights.txt")) {
				String[] data = lines.split(" - ");
				int itemId = Integer.parseInt(data[0].trim());
				double weight = Double.parseDouble(data[1].trim());
				if (weight == 0)
					continue;
				itemWeights.put(itemId, weight);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static double getItemWeight(int itemId) {
		Double weight = itemWeights.get(itemId);
		return weight == null ? 0 : weight;
	}
	
	private ItemWeights() {
		
	}
}