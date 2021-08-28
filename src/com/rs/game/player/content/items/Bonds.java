package com.rs.game.player.content.items;

import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;

public class Bonds {
		
	public static boolean redeemBond(Player player, Item item) {
		if (player.getInventory().contains(ItemIdentifiers.BOND_UNTRADEABLE)) {
			player.getDialogueManager().startDialogue("BondRedemption");
			return true;
		}
		return false;
	}

}
