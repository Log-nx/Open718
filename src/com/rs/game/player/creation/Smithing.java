package com.rs.game.player.creation;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;

public class Smithing {

	public static boolean hasLevel(Player player, int level) {
		if (player.getSkills().getLevel(Skills.SMITHING) < level) {
			 player.getPackets()
			 .sendGameMessage(
					 "You need a smithing level of "+level+" to smith this.",
					 true);
			 return false;
		 }
		return true;
	}
	
}