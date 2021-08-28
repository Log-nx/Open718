package com.rs.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.npc.Drop;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;

public class DropSimulator {
	
/*
 * @Author Kethsi www.kethsi.com
 * 
 */
	
	private static final Map<Integer, Integer> map = new HashMap<>();
	
	private static void log(String string) {
		String location = "data/dropSimulation.txt";
		try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
		writer.write(string);
		System.out.println(string);
		writer.newLine();
		writer.flush();
		writer.close();
	} catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		Cache.init();
		NPCDrops.init();
		int npcId = 1158;
		int kills = 2000;
		for (int i = 0; i < kills; i++) {
			Drop[] drops = NPCDrops.getDrops(npcId);
			if (drops == null) {
			    return;
			}
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100) {
					int previousValue = map.get(drop.getItemId()) != null ?  map.get(drop.getItemId()) : 0;
					map.put(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getExtraAmount()) + previousValue);
				} else {
					if ((Utils.getRandomDouble(99) + 1) <= drop.getRate()) {
						possibleDrops[possibleDropsCount++] = drop;
					}
				}
			}
			if (possibleDropsCount > 0) {
				Drop drop2 = possibleDrops[Utils.getRandom(possibleDropsCount - 1)];
				int previousValue = map.get(drop2.getItemId()) != null ?  map.get(drop2.getItemId()) : 0;
				map.put(drop2.getItemId(), drop2.getMinAmount() + Utils.getRandom(drop2.getExtraAmount()) + previousValue);
			}
		}
		log("[NPC] ID: "+npcId+" Name: "+NPCDefinitions.getNPCDefinitions(npcId).name);
		log("[Kills] "+kills);
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			//System.out.println(ItemDefinitions.getItemDefinitions(entry.getKey()).name+" - "+entry.getValue());
			log(ItemDefinitions.getItemDefinitions(entry.getKey()).name+" - "+Utils.formatNumber(entry.getValue()));
		}
	}

}
