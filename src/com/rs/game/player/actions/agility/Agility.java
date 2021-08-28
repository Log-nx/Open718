package com.rs.game.player.actions.agility;

import com.rs.game.npc.random.AgilityPenguinNPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

public class Agility {
	
	public enum AgilityCourses {
		AGILITY_PYRAMID, APE_ATOLL, BARBARIAN_OUTPOST, BARBARIAN_OUTPOST_ADVANCED, GNOME_AGILITY, GNOME_AGILITY_ADVANCED, WILDERNESS_AGILITY, HEFIN_AGILITY;
	}

	public static boolean hasLevel(Player player, int level) {
		if (player.getSkills().getLevel(Skills.AGILITY) < level) {
			 player.getPackets()
			 .sendGameMessage(
					 "You need an agility level of "+level+" to use this obstacle.",
					 true);
			 return false;
		 }
		return true;
	}

    public static void checkAgilityRandom(Player player) {
    	if (Utils.random(50) == 0 || player.getStatistics().getLapsRan() % 100 == 0) {
    		new AgilityPenguinNPC(player, player);
    		player.sm("<col=ff0000>An Agility Instructor has appeared out of nowhere.");
    	}
    }
}
