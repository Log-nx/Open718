package com.rs.game.npc.familiar;

import java.io.Serializable;

import com.rs.game.player.Player;
import com.rs.game.player.actions.summoning.Summoning.Pouches;

/**
 * The familiar manager.
 * 
 * 
 */
public final class FamiliarManager implements Serializable {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 7377270578500291237L;

	/**
	 * The player.
	 */
	private transient Player player;

	private boolean hadFamiliar;
	@SuppressWarnings("unused")
	private int ticks;
	@SuppressWarnings("unused")
	private int specialEnergy;
	@SuppressWarnings("unused")
	private BeastOfBurden bob;
	@SuppressWarnings("unused")
	private Pouches pouch;

	/**
	 * Constructs a new {@code FamiliarManager} {@code Object}.
	 */
	public FamiliarManager() {
		/*
		 * empty.
		 */
	}

	/**
	 * Spawns a pet.
	 * 
	 * @param itemId
	 *            The item id.
	 * @param deleteItem
	 *            If the item should be removed.
	 * @return {@code True} if we were dealing with a pet item id.
	 */
	public void spawn() {

	}
	
	/**
	 * Initializes the pet manager.
	 */
	public void init() {
		if (hadFamiliar) {
			spawn();
			hadFamiliar = false;
		}
	}

	/**
	 * Gets the player.
	 * 
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player.
	 * 
	 * @param player
	 *            The player to set.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setInformation() {
	}
}