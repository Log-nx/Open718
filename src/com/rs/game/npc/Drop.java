package com.rs.game.npc;

import com.rs.cache.loaders.ItemDefinitions;

public class Drop {

	public static Drop create(int itemId, double rate, int minAmount, int maxAmount, boolean rare) {
		return new Drop(itemId, rate, minAmount, maxAmount, rare);
	}

	private int itemId, minAmount, maxAmount;
	private double rate;
	private boolean rare;

	public Drop(int itemId, double rate, int minAmount, int maxAmount, boolean rare) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.rare = rare;
	}

	public Drop(int itemId, double rate, int minAmount) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = minAmount;
		this.rare = false;
	}

	public int getExtraAmount() {
		return maxAmount - minAmount;
	}

	public int getItemId() {
		return itemId;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public double getRate() {
		String dropName = ItemDefinitions.getItemDefinitions(getItemId()).getName().toLowerCase();
		if (dropName.contains("signet"))
			rate = 2.00;
		if (dropName.contains("scroll (hard)"))
			rate = 1.0;
		if (dropName.contains("scroll (elite)"))
			rate = 0.3;
		if (dropName.contains("scroll (master)"))
			rate = 0.01;
		return rate;
	}

	public boolean isFromRareTable() {
		return rare;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setMaxAmount(int amount) {
		this.maxAmount = amount;
	}

	public void setMinAmount(int amount) {
		this.minAmount = amount;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "DropNew [rate=" + rate + "]";
	}

}