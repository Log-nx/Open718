package com.rs.utils;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.Drop;
import com.rs.game.player.Player;
import com.rs.game.player.actions.slayer.Slayer.SlayerTask;

public class NPCDrops {

	private final static String PACKED_PATH = "data/npcs/packedDrops.d";
	private static HashMap<Integer, Drop[]> npcDrops;

	public static final void init() {
		loadPackedNPCDrops();
	}

	public static Drop[] getDrops(int npcId) {
		return npcDrops.get(npcId);
	}

	public static Drop[] getDrops(String npcName) {
		for (Entry<Integer, Drop[]> npcDrop : npcDrops.entrySet()) {
			int npcId = npcDrop.getKey();
			NPCDefinitions def = NPCDefinitions.getNPCDefinitions(npcId);
			if (def.getName().equalsIgnoreCase(Utils.formatPlayerNameForDisplay(npcName)))
				return npcDrop.getValue();
		}
		return null;
	}

	private Map<Integer, ArrayList<Drop>> dropMapx = null;

	public Map<Integer, ArrayList<Drop>> getDropArray() {

		if (dropMapx == null)
			dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		// dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		for (int i : npcDrops.keySet()) {
			int npcId = i;
			ArrayList<Drop> temp = new ArrayList<Drop>();
			for (Drop mainDrop : npcDrops.get(npcId)) {
				temp.add(mainDrop);
			}
			dropMapx.put(i, temp);
		}

		return dropMapx;
	}

	public void insertDrop(int npcID, Drop d) {
		loadPackedNPCDrops();
		Drop[] oldDrop = npcDrops.get(npcID);
		if (oldDrop == null) {
			npcDrops.put(npcID, new Drop[] { d });
		} else {
			int length = oldDrop.length;
			Drop destination[] = new Drop[length + 1];
			System.arraycopy(oldDrop, 0, destination, 0, length);
			destination[length] = d;
			npcDrops.put(npcID, destination);
		}
	}

	private static void loadPackedNPCDrops() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			int dropSize = buffer.getShort() & 0xffff;
			npcDrops = new HashMap<Integer, Drop[]>(dropSize);
			for (int i = 0; i < dropSize; i++) {
				int npcId = buffer.getShort() & 0xffff;
				Drop[] drops = new Drop[buffer.getShort() & 0xffff];
				for (int d = 0; d < drops.length; d++) {
					if (buffer.get() == 0)
						drops[d] = new Drop(buffer.getShort() & 0xffff, buffer.getDouble(), buffer.getInt(),
								buffer.getInt(), false);
					else
						drops[d] = new Drop(0, 0, 0, 0, true);

				}
				npcDrops.put(npcId, drops);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public HashMap<Integer, Drop[]> getDropMap() {
		return npcDrops;
	}

	public static List<Integer> getNPCIds(int itemId) {
		List<Integer> list = new ArrayList<Integer>();
		for (Entry<Integer, Drop[]> entry : npcDrops.entrySet()) {
			int npcId = entry.getKey();
			Drop[] drops = entry.getValue();
			for (Drop drop : drops) {
				if (drop.getItemId() == itemId) {
					list.add(npcId);
				}
			}
		}
		return list;
	}

	private static double VERY_RARE_THRESHOLD = 0.5, RARE_THRESHOLD = 2, UNCOMMON = 40.00;

	public static Drop generateRegularDrop(Player player, Drop[] drops, double dropRateModifier) {
		return generateRegularDrop(player, drops, dropRateModifier, false);
	}

	public static Drop generateRegularDrop(Player player, Drop[] drops, double dropRateModifier, boolean test) {
		List<Drop[]> sortedDropsNew = new ArrayList<Drop[]>(4);
		for (int i = 0; i < 4; i++) {
			List<Drop> sortedDrops = new ArrayList<Drop>();
			for (Drop drop : drops) {
				if (drop.getRate() == 100.00 || drop.getItemId() <= 0 || drop.isFromRareTable() || drop == null)
					continue;
				String dropName = ItemDefinitions.getItemDefinitions(drop.getItemId()).getName().toLowerCase();
				SlayerTask task = player.getSlayerManager().getCurrentTask();
				if (!test) {
					if (dropName.contains("perfect_chitin") && !player.getInventory().containsItem(36157, 1)
							&& !player.getEquipment().containsOneItem(36157)
							&& !player.getInventory().containsItem(36179, 1)
							&& !player.getEquipment().containsOneItem(36179, 1)
							&& !player.getInventory().containsItem(36171, 1)
							&& !player.getEquipment().containsOneItem(36171))
						continue;
					if (dropName.contains("ancient_emblem") && !player.getInventory().containsItem(36153, 1)
							&& !player.getEquipment().containsOneItem(36153))
						continue;
					if (drop.getItemId() == 31203 && task != SlayerTask.ASCENSION_MEMBERS
							&& task != SlayerTask.LEGIO_PRIMUS && task != SlayerTask.LEGIO_QUINTUS
							&& task != SlayerTask.LEGIO_QUARTUS && task != SlayerTask.LEGIO_SECUNDUS
							&& task != SlayerTask.LEGIO_SEXTUS && task != SlayerTask.LEGIO_TERTIUS)
						continue;
					if (drop.getItemId() == 30213 && task != SlayerTask.AIRUT)
						continue;
					if (drop.getItemId() == 31189 && task != SlayerTask.CELESTIAL_DRAGON)
						continue;
					if ((drop.getItemId() == ItemIdentifiers.RAGEFIRE_GLAND
							|| drop.getItemId() == ItemIdentifiers.STEADFAST_SCALE
							|| drop.getItemId() == ItemIdentifiers.GLAIVEN_WINGTIP) && task != SlayerTask.RUNE_DRAGON)
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_BOOTS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_BOOTS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_CAPE && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_CAPE))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_CUIRASS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_CUIRASS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_GAUNTLETS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_GAUNTLETS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_GREAVES && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_GREAVES))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_HELM && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_HELM))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_BOOTS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_BOOTS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_CAPE && player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_CAPE))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_CUIRASS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_CUIRASS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_GAUNTLETS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_GAUNTLETS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_HELM && player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_HELM))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_GREAVES && player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_GREAVES))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_BOOTS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_BOOTS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CAPE && player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CAPE))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CUIRASS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CUIRASS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_GAUNTLETS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_GAUNTLETS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_HELM && player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_HELM))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_GREAVES && player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_GREAVES))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_BOOTS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_BOOTS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CAPE && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CAPE))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CUIRASS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CUIRASS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_GAUNTLETS && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_GAUNTLETS))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_HELM && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_HELM))
						continue;
					if (drop.getItemId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_GREAVES && player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_GREAVES))
						continue;
					if (dropName.contains("kaigi's") || dropName.contains("goblin book") || dropName.contains("devotion") 
							|| dropName.contains("transfigure") || dropName.contains("sacrifice") || dropName.contains("long bone") 
							|| dropName.contains("femur bone") || dropName.contains("the glory of general graardor") || dropName.contains("curved bone") 
							|| dropName.contains("court summon") || dropName.contains("long bone") || dropName.contains("armadyl's assault")
							|| dropName.contains("zilyana's note") || dropName.contains("razulei's tale") || dropName.contains("bone bead"))
						continue;
					if (drop.getItemId() > Utils.getItemDefinitionsSize()) 
						continue; 
				}
				if (i == 3 && drop.getRate() > UNCOMMON) {
					sortedDrops.add(drop);
				} else if (i == 2 && drop.getRate() > RARE_THRESHOLD && drop.getRate() <= UNCOMMON) {
					sortedDrops.add(drop);
				} else if (i == 1 && drop.getRate() > VERY_RARE_THRESHOLD && drop.getRate() <= RARE_THRESHOLD) {
					sortedDrops.add(drop);
				} else if (i == 0 && drop.getRate() > 0.00 && drop.getRate() <= VERY_RARE_THRESHOLD) {
					sortedDrops.add(drop);
				}
			}
			sortedDropsNew.add(sortedDrops.isEmpty() ? new Drop[0] : sortedDrops.stream().toArray(Drop[]::new));
		}
		double uncommonDropRate = UNCOMMON;
		double rareDropRate = RARE_THRESHOLD * (1.00 + (dropRateModifier / 1.50));
		double veryrareDropRate = VERY_RARE_THRESHOLD * (1.00 + (dropRateModifier / 1.50));
		double roll = Math.random() * 100;
		List<Drop> possibleDrops = null;
		if (sortedDropsNew.get(0).length > 0 && roll <= veryrareDropRate) {
			while (true) {
				roll = Math.random() * 100;
				possibleDrops = asList(sortedDropsNew.get(0));
				Iterator<Drop> itr = possibleDrops.iterator();
				while (itr.hasNext()) {
					Drop drop = itr.next();
					if (roll > drop.getRate() * (1.00 + (dropRateModifier / (1.50))))
						itr.remove();
				}
				if (!possibleDrops.isEmpty())
					return possibleDrops.get(Utils.random(possibleDrops.size()));
			}
		}
		if (sortedDropsNew.get(1).length > 0 && roll <= rareDropRate) {
			while (true) {
				roll = Math.random() * 100;
				possibleDrops = asList(sortedDropsNew.get(1));
				Iterator<Drop> itr = possibleDrops.iterator();
				while (itr.hasNext()) {
					Drop drop = itr.next();
					if (roll > drop.getRate() * (1.00 + (dropRateModifier / (1.50))))
						itr.remove();
				}
				if (!possibleDrops.isEmpty())
					return possibleDrops.get(Utils.random(possibleDrops.size()));
			}
		}
		if (sortedDropsNew.get(2).length > 0 && roll <= uncommonDropRate) {
			while (true) {
				roll = Math.random() * 100;
				possibleDrops = asList(sortedDropsNew.get(2));
				Iterator<Drop> itr = possibleDrops.iterator();
				while (itr.hasNext()) {
					Drop drop = itr.next();
					if (roll > drop.getRate())
						itr.remove();
				}
				if (!possibleDrops.isEmpty())
					return possibleDrops.get(Utils.random(possibleDrops.size()));
			}
		}
		possibleDrops = asList(sortedDropsNew.get(3));
		if (possibleDrops.isEmpty())
			return null;
		return possibleDrops.get(Utils.random(possibleDrops.size()));
	}

	private static List<Drop> asList(Drop[] dropsNew) {
		if (dropsNew.length == 0)
			return new ArrayList<Drop>();
		List<Drop> drops = new ArrayList<Drop>();
		for (Drop drop : dropsNew)
			drops.add(drop);
		return drops;
	}

	public static void sendDropRateModifierMessage(Player player, Drop drop) {
		if (drop.getRate() > RARE_THRESHOLD)
			return;
		boolean hasROW = ItemDefinitions.getItemDefinitions(player.getEquipment().getRingId()).getName().toLowerCase()
				.contains("wealth");
		boolean hasCompCape = ItemDefinitions.getItemDefinitions(player.getEquipment().getCapeId()).getName()
				.toLowerCase().contains("completionist");
		if (hasCompCape && hasROW) {
			player.getPackets()
					.sendGameMessage("<col=ff7000>Your ring of wealth & "
							+ ItemDefinitions.getItemDefinitions(player.getEquipment().getCapeId()).getName()
							+ " affected your luck greatly!", true);
			return;
		}
		if (hasROW)
			player.getPackets().sendGameMessage("<col=ff7000>Your ring of wealth shines more brightly!", true);
		else if (hasCompCape)
			player.getPackets()
					.sendGameMessage("<col=ff7000>Your "
							+ ItemDefinitions.getItemDefinitions(player.getEquipment().getCapeId()).getName()
							+ " affected your luck slightly!", true);
	}

}