package com.rs.game.player.content.interfaces;

import com.rs.game.player.Player;

public abstract class RSInterface {

	/**
	 * Called when the player trys to open this interface.
	 */
	public abstract void open();
	
	/**
	 * Called when the interface's buttons are clicked on.
	 * 
	 * @param button
	 * 				the button's id.
	 * @param packet TODO
	 * @param itemId TODO
	 * @param slotId TODO
	 * @return
	 */
	public abstract boolean onClick(int button, int packet, int itemId, int slotId);
	
	/**
	 * Called when the interface is closed.
	 */
	public abstract void onClose();
	
	/**
	 * Gets the ID for this interface.
	 * 
	 * @return 
	 * 			the interface's id.
	 */
	public abstract int getId();
	
	/**
	 * Refreshs the specified {@link RSInterface}
	 * @param player
	 * 				the {@link Player}
	 * @param rsInterface
	 * 				the {@link RSInterface} to refresh
	 */
	public void refresh(final Player player, RSInterface rsInterface) {
	}
	
}