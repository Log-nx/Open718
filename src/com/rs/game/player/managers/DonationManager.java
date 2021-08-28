package com.rs.game.player.managers;

import java.io.Serializable;

import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.utils.Color;
import com.rs.utils.Utils;

public class DonationManager implements Serializable {

	private static final long serialVersionUID = 1L;
	public int amountDonated;
	private transient Player player;

	public DonationManager() {
		amountDonated = 0;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void increaseDonationAmount(int amount) {
		if (amount >= 0) {
			handleUpgrade(amountDonated, amountDonated + amount);
			amountDonated += amount;
			player.getPackets().sendGameMessage(Color.GREEN, "Thank you for your donation, your new total is " + Utils.formatNumber(amountDonated));
		}
	}
	
	public void setDonatedAmount(int amount) {
		if (amount >= 0) {
			handleUpgrade(amountDonated, amount);
			amountDonated = amount;
		}
	}

	public int getTotalDonated() {
		return amountDonated;
	}

	public boolean isDonator() {
		return amountDonated >= 10 || player.getRights() >= 2;
	}

	public boolean isExtremeDonator() {
		return amountDonated >= 15;
	}

	public boolean isLegendaryDonator() {
		return amountDonated >= 25;
	}

	public boolean isDivineDonator() {
		return amountDonated >= 50;
	}

	public boolean isHeroicDonator() {
		return amountDonated >= 100;
	}
	
	public boolean isImmortalDonator() {
		return amountDonated >= 200;
	}

	public void handleBond() {
		if (!(player.getInventory().containsItem(ItemIdentifiers.BOND, 1) || player.getInventory().containsItem(ItemIdentifiers.BOND_UNTRADEABLE, 1)))
			return;
		if (player.getInventory().containsItem(ItemIdentifiers.BOND_UNTRADEABLE, player.getInventory().getAmountOf(ItemIdentifiers.BOND_UNTRADEABLE))) {
			increaseDonationAmount(5 * player.getInventory().getAmountOf(ItemIdentifiers.BOND_UNTRADEABLE));
			player.getInventory().removeItemMoneyPouch(ItemIdentifiers.BOND_UNTRADEABLE, player.getInventory().getAmountOf(ItemIdentifiers.BOND_UNTRADEABLE));
		} else {
			increaseDonationAmount(5 * player.getInventory().getAmountOf(ItemIdentifiers.BOND));
			player.getInventory().removeItemMoneyPouch(ItemIdentifiers.BOND, player.getInventory().getAmountOf(ItemIdentifiers.BOND));
		}
	}

	public void handleKeys() {
		if (!(player.getInventory().containsItem(ItemIdentifiers.BOND, 1)
				|| player.getInventory().containsItem(ItemIdentifiers.BOND_UNTRADEABLE, 1)))
			return;
		if (player.getInventory().containsItem(ItemIdentifiers.BOND_UNTRADEABLE,
				player.getInventory().getAmountOf(ItemIdentifiers.BOND_UNTRADEABLE))) {
			player.getSquealOfFortune().setEarnedSpins(player.getSquealOfFortune().getEarnedSpins() + 50);
			player.getInventory().removeItemMoneyPouch(ItemIdentifiers.BOND_UNTRADEABLE, 1);
		} else {
			player.getSquealOfFortune().setEarnedSpins(player.getSquealOfFortune().getEarnedSpins() + 50);
			player.getInventory().removeItemMoneyPouch(ItemIdentifiers.BOND, 1);
		}
	}

	private void handleUpgrade(int prevAmount, int newAmount) {
		if (prevAmount == newAmount) {
			return;
		}
		if (!getDonatorTitle(prevAmount).equalsIgnoreCase(getDonatorTitle(newAmount))) {
			World.sendWorldMessage("<img=5><col=b7b82b>Donation: "+player.getDisplayName() + " has just upgraded " + (player.getAppearence().isMale() ? "his" : "her")
			+ " donator status from " + getDonatorTitle(prevAmount).toLowerCase() + " to " + getDonatorTitle(newAmount).toLowerCase() + "!", false, false);
		}
	}

	public String getDonatorTitle() {
		return getDonatorTitle(amountDonated);
	}

	public String getDonatorTitleColor() {
		return getDonatorTitleColor(amountDonated);
	}

	private String getDonatorTitle(int amount) {
		if (amount >= 5 && amount < 15)
			return "Regular";
		else if (amount >= 15 && amount < 25)
			return "Extreme";
		else if (amount >= 25 && amount < 50)
			return "Legendary";
		else if (amount >= 50 && amount < 100)
			return "Divine";
		else if (amount >= 100 && amount < 200)
			return "Heroic";
		else if (amount >= 200)
			return "Immortal";
		return "Not A Donator";
	}

	private String getDonatorTitleColor(int amount) {
		if (amount >= 10 && amount < 14)
			return "<col=A50B00>";
		else if (amount >= 15 && amount < 25)
			return "<col=A50B00>";
		else if (amount >= 25 && amount < 50)
			return "<col=0000FF>";
		else if (amount >= 50 && amount < 100)
			return "<col=0000FF>";
		else if (amount >= 100 && amount < 120)
			return "<col=6C21ED>";
		else if (amount >= 120 && amount < 150)
			return "<col=6C21ED>";
		else if (amount >= 150 && amount < 200)
			return "<col=6C21ED>";
		else if (amount >= 200)
			return "<col=65E0D5>";
		return "";
	}
}