package com.rs.game.player.content.custom;

import com.rs.game.player.Player;
/**
 * 
 * @author DikKutJoch
 *
 */

public class LunarSpells {
	public static final int NATURE_RUNE = 561;
	public static final int ASTRAL_RUNE = 9075;
	public static final int EARTH_RUNE = 557;
	
	public static void PlankMaking(Player player) {
		if (player.getInventory().containsItem(EARTH_RUNE, 1) && player.getInventory().containsItem(ASTRAL_RUNE, 1 ) && player.getInventory().containsItem(NATURE_RUNE, 1)) {
			player.sm("WORKING");
		
		} else {
			player.sm("You do not have the required runes to cast this spell");
		}
	}

}
