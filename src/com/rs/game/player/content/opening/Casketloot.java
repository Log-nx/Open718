package com.rs.game.player.content.opening;

import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Casketloot {
	

	
	public static void openingCasket(Player player) {	
	/**
	 * Integers
	 */
	int [] COMMON = {11818, 11820 , 1187, 1127, 1079, 11732, 1837};
	int [] UNCOMMON  ={4151, 4151};
	int [] RARE = {1289, };
	int [] SUPER_RARE = {4151, 10828 , 1215, 1149};
	int [] MEGA_RARE = {4151, 4151};

	/**
	 * Process
	 */
		int rarity = Utils.getRandom(1000);
		if (rarity > 0 && rarity < 600) {		
			int length = COMMON.length;
			length--;	
			int reward = Utils.getRandom(length);
			player.getInventory().addItemDrop(COMMON[reward], 1);
			player.getInventory().deleteItem(405, 1);
			player.sm("You obtained a common item.");
		}
		if (rarity > 600 && rarity < 850) {
			int length = UNCOMMON.length;
			length--;
			int reward = Utils.getRandom(length);
			player.getInventory().addItemDrop(UNCOMMON[reward], 1);
			player.getInventory().deleteItem(405, 1);
			player.sm("You obtained a uncommon item");
		}
		if (rarity > 850 && rarity < 950) {
			int length = RARE.length;
			length--; 	
			int reward = Utils.getRandom(length);
			player.getInventory().addItemDrop(RARE[reward], 1);
			player.getInventory().deleteItem(405, 1);
			player.sm("You obtained a rare item");
		}
		if (rarity > 950 && rarity < 995) {
			int length = SUPER_RARE.length;
			length--;
			int reward = Utils.getRandom(length);
			player.getInventory().addItemDrop(SUPER_RARE[reward], 1);
			player.getInventory().deleteItem(405, 1);
			player.sm("You obtained a super rare item");
		}
		if (rarity > 995 && rarity < 1000) {
			int length = MEGA_RARE.length;
			length--;
			int reward = Utils.getRandom(length);
			player.getInventory().addItemDrop(MEGA_RARE[reward], 1);
			player.getInventory().deleteItem(405, 1);
			player.sm("You obtained a mega rare item!");
		}
	}
	}