package com.rs.game.player.managers;

import com.rs.game.player.Player;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.utils.Utils;

public class WealthManager {
	
	public static void sendInterface(Player player) {
		long moneyPouch = player.getMoneyPouch().getCoinsAmount();
		long bankValue = player.getBankValue();
		long inventoryValue = player.getInventoryValue();
		long equipmentValue = player.getEquipmentValue();
		long totalValue = 0;
		long grandexchangeValue = GrandExchange.getTotalOfferValues(player);
		long collectionValue = GrandExchange.getTotalCollectionValue(player);
		player.closeInterfaces();
		player.getInterfaceManager().sendInterface(629);
		player.getPackets().sendIComponentText(629, 11, "Information Tab");
		player.getPackets().sendIComponentText(629, 12, "");
		player.getPackets().sendIComponentText(629, 41, "Money pouch:");
		player.getPackets().sendIComponentText(629, 54, Utils.formatNumber(moneyPouch));
		player.getPackets().sendIComponentText(629, 42, "Bank:");
		player.getPackets().sendIComponentText(629, 55, Utils.formatNumber(bankValue));
		player.getPackets().sendIComponentText(629, 43, "Inventory:");
		player.getPackets().sendIComponentText(629, 56, Utils.formatNumber(inventoryValue));
		player.getPackets().sendIComponentText(629, 44, "Equipment:");
		player.getPackets().sendIComponentText(629, 57, Utils.formatNumber(equipmentValue));
		player.getPackets().sendIComponentText(629, 45, "Grand Exchange");
		player.getPackets().sendIComponentText(629, 58, Utils.formatNumber(grandexchangeValue));
		player.getPackets().sendIComponentText(629, 46, "Collection Box:");
		player.getPackets().sendIComponentText(629, 59, Utils.formatNumber(collectionValue));
		player.getPackets().sendIComponentText(629, 47, "Total wealth:");
		player.getPackets().sendIComponentText(629, 60, Utils.formatNumber(totalValue));
		totalValue = bankValue + inventoryValue + equipmentValue + moneyPouch + collectionValue + grandexchangeValue;
		player.getPackets().sendIComponentText(629, 48, "");
		player.getPackets().sendIComponentText(629, 61, "");
		player.getPackets().sendIComponentText(629, 49, "");
		player.getPackets().sendIComponentText(629, 62, "");
		player.getPackets().sendIComponentText(629, 50, "");
		player.getPackets().sendIComponentText(629, 63, "");
		player.getPackets().sendIComponentText(629, 51, "");
		player.getPackets().sendIComponentText(629, 64, "");
		player.getPackets().sendIComponentText(629, 52, "");
		player.getPackets().sendIComponentText(629, 65, "");
		player.getPackets().sendIComponentText(629, 68, "Close");
		player.getPackets().sendHideIComponent(629, 69, true);
	}

}
