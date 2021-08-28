package com.rs.game.player.actions.smithing;

import com.rs.game.player.Player;

public class GodswordCreation {

	public static boolean handleGodSword(Player player, int usedWith, int itemUsed) {
		if (!player.getInventory().containsItem(usedWith, 1)) {
			return false;
		}
		if (!player.getInventory().containsItem(itemUsed, 1)) {
			return false;
		}
		if (itemUsed == 11702 && usedWith == 11690 || itemUsed == 11690 && usedWith == 11702) {
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().addItemMoneyPouch(11694, 1);
			player.sm("You join the hilt and godsword blade into an Armadyl Godsword!");
			return true;
		} else if (itemUsed == 11704 && usedWith == 11690 || usedWith == 11704 && itemUsed == 11690) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11696, 1);
			player.sm("You join the hilt and godsword blade into a Bandos Godsword!");
			return true;
		} else if (itemUsed == 11706 && usedWith == 11690 || usedWith == 11706 && itemUsed == 11690) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11698, 1);
			player.sm("You join the hilt and godsword blade into a Saradomin Godsword!");
			return true;
		} else if (itemUsed == 11708 && usedWith == 11690 || usedWith == 11708 && itemUsed == 11690) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11700, 1);
			player.sm("You join the hilt and godsword blade into a Zamorak Godsword!");
			return true;
		} else if (itemUsed == 11710 && usedWith == 11712 || usedWith == 11710 && itemUsed == 11712) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11686, 1);
			player.sm("you join the shards together!");
			return true;
		} else if (itemUsed == 11712 && usedWith == 11714 || usedWith == 11712 && itemUsed == 11714) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11692, 1);
			player.sm("You join the shards together!");
			return true;
		} else if (itemUsed == 11686 && usedWith == 11714 || usedWith == 11686 && itemUsed == 11714) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11690, 1);
			player.sm("You join the shards together!");
			return true;
		} else if (itemUsed == 11710 && usedWith == 11692 || usedWith == 11710 && itemUsed == 11692) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11690, 1);
			player.sm("You join the shards together!");
			return true;
		} else if (itemUsed == 11712 && usedWith == 11688 || usedWith == 11712 && itemUsed == 11688) {
			player.getInventory().deleteItem(itemUsed, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItemMoneyPouch(11690, 1);
			player.sm("You join the shards together!");
			return true;
		}
		return false;
	}
}