package com.rs.game.player.managers;

import java.io.Serializable;
import java.util.Vector;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.perks.GamePointRewards;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.utils.Color;
import com.rs.utils.Utils;

public class GamePointManager implements Serializable {

	private static final long serialVersionUID = 2995657158069467668L;
	private Vector<GamePointRewards> gamePointRewards = new Vector<>();
	private Player player;
	
	public boolean hasReward(GamePointRewards reward) {
		return gamePointRewards.contains(reward);
	}
	
	public boolean unlockReward(GamePointRewards reward, boolean silent) {
		if (gamePointRewards.contains(reward)) {
			return false;
		}
		if (!silent) {
			player.getPackets().sendGameMessage(Color.ORANGE, "Congratulations! You have unlocked the " + reward.toString().toLowerCase().replace("_", " ") + " reward!");
		}
		gamePointRewards.add(reward);
		return true;
	}
	
	public boolean lockReward(GamePointRewards reward) {
		if (!gamePointRewards.contains(reward)) {
			return false;
		}
		gamePointRewards.remove(reward);
		return true;
	}
	
	public Vector<GamePointRewards> getGamePointRewards() {
		return gamePointRewards;
	}
	
	public void setPlayerPerks(Vector<GamePointRewards> gamePointRewards) {
		this.gamePointRewards = gamePointRewards;
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
	public Player setPlayer(Player player) {
		return this.player = player;
	}
	
	public void increaseGamePoints(int amount) {
		player.getStatistics().increaseGamePoints(amount);
		player.getPackets().sendGameMessage("<col=FF8330><shad=00001A>You have received " + Utils.getFormattedNumber(amount) + " game points, you now have a total of " + Utils.getFormattedNumber(player.getStatistics().getGamePoints()) + " game points.", true);
	}
	
	public void addGamePointItem(int itemId, int amount) {
		if (player.hasBankSkills()) {
			if (ItemConstants.isTradeable(new Item(itemId))) {
				if (player.getBank().hasBankSpace()) {
					player.getPackets().sendGameMessage(String.format("<col=EE4000>The following item has been added to your bank: %sx %s.</col>", amount, ItemDefinitions.getItemDefinitions(itemId).getName().toLowerCase()), true);
					player.getBank().addItem(itemId, amount, true);
					return;
				} else {
					player.getPackets().sendGameMessage("Not enough space in your bank.");
				}
			}
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.getInventory().addItem(itemId, amount, true);
		}
	}
}