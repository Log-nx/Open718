package com.rs.game.player.content.items.sof;

import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.appearance.Equipment;
import com.rs.utils.Utils;

public class MaskOfReflection {
	
	static int MASK = ItemIdentifiers.MASK_OF_REFLECTION;
	
	static int UPGRADED_MASK = ItemIdentifiers.HELM_OF_LITTLE_KINGS;
	
	public static void checkKills(Player player) {
		player.getPackets().sendGameMessage("You have a total of " + Utils.formatNumber(player.getStatistics().getBasiliskKills()) + " basilisk kills.");
	}

	public static void useTeleport(Player player) {
		player.getDialogueManager().startDialogue("MaskOfReflection");
	}
	
	public static void changeLooks(Player player, int itemId, boolean inventory) {
		if (player.getStatistics().getBasiliskKills() >= 500) {
			if (inventory) {
				if (itemId == MASK) {
					player.getInventory().deleteItem(MASK, 1);
					player.getInventory().add(UPGRADED_MASK, 1);
				}
				if (itemId == UPGRADED_MASK) {
					player.getInventory().deleteItem(UPGRADED_MASK, 1);
					player.getInventory().add(MASK, 1);
				}
			} else {
				if (player.getEquipment().getHatId() == MASK) {
					player.getEquipment().getItems().set(Equipment.SLOT_HAT, new Item(UPGRADED_MASK));
				}
				if (player.getEquipment().getHatId() == UPGRADED_MASK) {
					player.getEquipment().getItems().set(Equipment.SLOT_HAT, new Item(MASK));
				}
			}
		}
	}
}
