package com.rs.game.npc.araxxor;

import com.rs.game.player.Player;

public interface AraxxorAttack {

	/**
	 * Starts the attack.
	 * @param npc The NPC.
	 * @param victim The victim.
	 * @return The next attack value.
	 */
	int attack(Araxxor npc, Player victim);
	/**
	 * Checks if Araxxor can use this attack.
	 * @param npc Araxxor
	 * @param victim The player.
	 * @return {@code True} if so.
	 */
	boolean canAttack(Araxxor npc, Player victim);
	
}
