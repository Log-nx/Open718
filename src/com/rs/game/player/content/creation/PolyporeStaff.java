package com.rs.game.player.content.creation;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Color;

public class PolyporeStaff {
	
	public static boolean createStaff(Player player, Item usedWith, Item itemUsed) {
		if (!player.getInventory().containsItem(usedWith.getId(), 1)) {
			return false;
		}
		if (!player.getInventory().containsItem(itemUsed.getId(), 1)) {
			return false;
		}
		if (usedWith.getId() == 22498 && itemUsed.getId() == 554 || usedWith.getId() == 554 && itemUsed.getId() == 22498 || usedWith.getId() == 22498 && itemUsed.getId() == 22448 || usedWith.getId() == 22448 && itemUsed.getId() == 22498) {
			if (!player.getInventory().containsItem(22448, 3000) && !player.getInventory().containsItem(554, 15000)) {
				player.getPackets().sendGameMessage(Color.RED, "You need to have 3,000 spores and 15,000 fire runes to create a Polypore staff.");
				return false;
			}
			player.setNextAnimation(new Animation(15434));
			player.setNextGraphics(new Graphics(2032));
			player.getSkills().addXp(Skills.FARMING, 300);
			player.getInventory().deleteItem(22448, 3000);
			player.getInventory().deleteItem(554, 15000);
			player.getInventory().deleteItem(22498, 1);
			player.getInventory().add(ItemIdentifiers.POLYPORE_STAFF, 1);
			return true;
		}
		return false;
	}

}
