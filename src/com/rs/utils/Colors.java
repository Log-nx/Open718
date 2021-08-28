package com.rs.utils;

import com.rs.game.player.Player;

public final class Colors {

	public static String RED = "<col=ff0000>", GREEN = "<col=ff000>", WHITE = "<col=FFFFFF>",
			GOLDEN = "<col=ffc800>", ACHIEVEMENT = "<col=ff8c38>", PURPLE = "<col=9966ff>", DARK_BLUE = "<col=064273>",
			GREY = "<col=808080>", AQUA = "<col=00FFFF>", BLACKISH = "<col=212F3D>", BLUE = "<col=5DADE2>", YELLOW = "<col=FFFF00>", ORANGE = "<col=FFA500>", BROWN = "<col=BA4A00>",
			DARK_RED = "<col=B52100>",
			CYAN ="<col=00B2ED>", SP_BLUE = "<col=1402D7>"
			
					
			;
	
	public static final int regularDonor = 1, superDonor = 2, legendaryDonor = 3, immortalDonor = 4, divineDonor = 5;
	
	/**
	 * so this method basically colours the donor ranks.
	 */
	public static String donorColour(Player player, int theRank) {
		switch(theRank) {
		case regularDonor:
			return GREEN + "<img=10>" + "Regular Donator</col>";
		case superDonor:
			return SP_BLUE + "<img=11>" + "Super Donator</col>";
		case legendaryDonor:
			return WHITE + "<img=12>" + "Legendary Donator</col>";
		case immortalDonor:
			return PURPLE + "<img=13>" + "Immortal Donator</col>";
		case divineDonor:
			return GOLDEN + "<img=15>" + "<shad=FF0000>Divine Donator</col></shad>";
		}
		return "";
	}
	
}
