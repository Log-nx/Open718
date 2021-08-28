package com.rs.game.npc;

import java.util.List;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;

public class DropPredictor { 
	 
	public static Item[] loots = null; 
 
	public static void addItems(Drop drop) { 
		Item item = ItemDefinitions.getItemDefinitions(drop.getItemId()).isStackable() 
				? new Item(drop.getItemId(), (drop.getMinAmount() * 1) + Utils.getRandom(drop.getExtraAmount() * 1)) 
				: new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getExtraAmount())); 
		for (int i = 0; i < loots.length; i++) { 
			if (loots[i] != null && loots[i].getId() == item.getId()) { 
				loots[i].setAmount(loots[i].getAmount() + item.getAmount()); 
				break; 
			} 
			if (loots[i] == null) { 
				loots[i] = item; 
				break; 
			} 
		} 
	} 
 
	public static void generateDrop(Player player, int npcId, int amount) { 
		Drop[] drops = NPCDrops.getDrops(npcId); 
		if (drops == null) 
			return; 
		for (Drop drop : drops) { 
			if (drop == null) 
				continue; 
			if (drop.getRate() == 100) 
				addItems(drop);
		} 
		Drop regularDrop = NPCDrops.generateRegularDrop(player, drops, player.getDropRateModifier(), true); 
		if (regularDrop != null) 
			addItems(regularDrop); 
	} 
 
	public static void predictDrops(Player player, int npcId, int amount) { 
		loots = new Item[100]; 
		for (int i = 0; i < amount; i++) { 
			generateDrop(player, npcId, amount); 
		} 
		sendInterface(player, npcId, amount); 
	} 
 
	public static void sendInterface(Player player, int npcId, int amount) { 
		player.getInterfaceManager().sendInterface(275); 
		player.getPackets().sendIComponentText(275, 1, "<img=5><col=A50B00>Drops for " 
				+ NPCDefinitions.getNPCDefinitions(npcId).getName() + " killed " + amount + " times<img=5>"); 
		int i = 0; 
		for (int index = 0; index < loots.length; index++) { 
			if (loots[index] != null) { 
				player.getPackets().sendIComponentText(275, 10 + i, 
						getColorForPrice(loots[index].getDefinitions().getPrice()) + loots[index].getName() + " (" 
								+ loots[index].getAmount() + ")"); 
				i++; 
			} 
		} 
		for (int i2 = i; i2 < 90; i2++) { 
			player.getPackets().sendIComponentText(275, 10 + i2, " "); 
		} 
	} 
 
	public static String getColorForPrice(int price) { 
		if (price > 1000000) { 
			return "<col=CC0000><shad=660000>"; 
		} else if (price > 500000) { 
			return "<col=FF6600><shad=B24700>"; 
		} else if (price > 250000) { 
			return "<col=FFFF00><shad=474700>"; 
		} 
		return "<col=000000>"; 
	} 
 
	public static void findNPCs(Player player, int itemId) { 
		List<Integer> npcIds = NPCDrops.getNPCIds(itemId); 
		player.getInterfaceManager().sendInterface(275); 
		player.getPackets().sendIComponentText(275, 1, 
				"<img=5><col=A50B00>NPCs for " + ItemDefinitions.getItemDefinitions(itemId).getName() + "<img=5>"); 
		int i = 0; 
		for (int index = 0; index < npcIds.size(); index++) { 
			if (npcIds.get(index) != null) { 
				NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(npcIds.get(index)); 
				if (defs.combatLevel > 0) { 
					player.getPackets().sendIComponentText(275, 10 + i, 
							"(Level: " + defs.combatLevel + ") " + defs.getName() + " (Id: " + defs.getId() + ")"); 
					i++; 
					if (i > 90) 
						break; 
				} 
			} 
		} 
		for (int i2 = i; i2 < 90; i2++) { 
			player.getPackets().sendIComponentText(275, 10 + i2, " "); 
		} 
	} 
 
}