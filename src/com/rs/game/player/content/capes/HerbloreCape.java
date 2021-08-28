package com.rs.game.player.content.capes;

import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;

public class HerbloreCape {
	
	public static void handleHerbloreCape(Player player) {
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_GUAM)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_GUAM);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_GUAM, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_GUAM));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_GUAM, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_TARROMIN)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_TARROMIN);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_TARROMIN, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_TARROMIN));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_TARROMIN, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_MARRENTILL)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_MARRENTILL);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_MARRENTILL, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_MARRENTILL));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_MARRENTILL, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_HARRALANDER)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_HARRALANDER);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_HARRALANDER, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_HARRALANDER));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_HARRALANDER, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_RANARR)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_RANARR);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_RANARR, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_RANARR));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_RANARR, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_TOADFLAX)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_TOADFLAX);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_TOADFLAX, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_TOADFLAX));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_TOADFLAX, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_SPIRIT_WEED)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_SPIRIT_WEED);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_SPIRIT_WEED, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_SPIRIT_WEED));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_SPIRIT_WEED, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_IRIT)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_IRIT);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_IRIT, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_IRIT));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_IRIT, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_WERGALI)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_WERGALI);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_WERGALI, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_WERGALI));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_WERGALI, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_AVANTOE)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_AVANTOE);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_AVANTOE, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_AVANTOE));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_AVANTOE, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_KWUARM)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_KWUARM);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_KWUARM, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_KWUARM));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_KWUARM, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_BLOODWEED)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_BLOODWEED);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_BLOODWEED, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_BLOODWEED));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_BLOODWEED, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_SNAPDRAGON)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_SNAPDRAGON);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_SNAPDRAGON, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_SNAPDRAGON));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_SNAPDRAGON, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_CADANTINE)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_CADANTINE);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_CADANTINE, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_CADANTINE));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_CADANTINE, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_LANTADYME)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_LANTADYME);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_LANTADYME, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_LANTADYME));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_LANTADYME, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_DWARF_WEED)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_DWARF_WEED);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_DWARF_WEED, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_DWARF_WEED));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_DWARF_WEED, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_TORSTOL)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_TORSTOL);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_TORSTOL, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_TORSTOL));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_TORSTOL, oldamount);
		}
		if (player.getInventory().containsOneItem(ItemIdentifiers.GRIMY_FELLSTALK)) {
			int oldamount = player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_FELLSTALK);
			player.getInventory().deleteItem(ItemIdentifiers.GRIMY_FELLSTALK, player.getInventory().getAmountOf(ItemIdentifiers.GRIMY_FELLSTALK));
			player.getInventory().addItem(ItemIdentifiers.CLEAN_FELLSTALK, oldamount);
		}
		return;
	}
}
