package com.rs.game.player.content.overrides;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class CosmeticsHandler {

	public static final int KEEP_SAKE_KEY = 25430;
	public static final int DEFAULT_PRICE_FULL_OUTFIT = 50;
	public static final int DEFAULT_PRICE_SINGLE_PIECE = 10;

	public static void openCosmeticsHandler(final Player player) {
		player.stopAll();
		player.getTemporaryAttributtes().put("Cosmetics", Boolean.TRUE);
		player.getPackets().sendHideIComponent(667, 84, true);
		player.getPackets().sendHideIComponent(667, 22, true);
		player.getPackets().sendHideIComponent(667, 23, true);
		for (int i = 0; i < Utils.getInterfaceDefinitionsComponentsSize(667); i++)
			player.getPackets().sendIComponentText(667, i, "");
		player.getPackets().sendIComponentText(667, 25, "Instructions:");
		player.getPackets().sendIComponentText(667, 28, "Click \"continue\" on a slot");
		player.getPackets().sendIComponentText(667, 29, "to view costumes in that");
		player.getPackets().sendIComponentText(667, 30, "slot.");
		player.getPackets().sendIComponentText(667, 26, "Custom slots:");
		player.getPackets().sendIComponentText(667, 33, "Arrows slot -> Wings.");
		player.getPackets().sendIComponentText(667, 35, "Ring slot -> Full/Saved ");
		player.getPackets().sendIComponentText(667, 36, "Outfits.");
		player.getPackets().sendIComponentText(667, 38, "Aura slot -> Gazes.");
		player.getPackets().sendConfigByFile(8348, 1);
		player.getPackets().sendConfigByFile(4894, 0);
		Item[] cosmetics = player.getEquipment().getItems().getItemsCopy();
		for (int i = 0; i < cosmetics.length; i++) {
			cosmetics[i] = new Item(0);
		}
		player.getPackets().sendItems(94, cosmetics);
		player.getPackets().sendUnlockIComponentOptionSlots(667, 9, 0, 14, true, 0, 1, 2, 3);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendConfigByFile(8348, 1);
				player.getPackets().sendRunScriptBlank(2319);
			}
		});
		player.getInterfaceManager().sendInterface(667);
		player.setCloseInterfacesEvent(() -> {
			player.getDialogueManager().finishDialogue();
			player.getTemporaryAttributtes().remove("Cosmetics");
			for (int i = 0; i < 15; i++) {
				player.getEquipment().refresh(i);
			}
		});
	}

	public static boolean keepSakeItem(Player player, Item itemUsed, Item itemUsedWith) {
		if (itemUsed.getId() != KEEP_SAKE_KEY && itemUsedWith.getId() != KEEP_SAKE_KEY)
			return false;
		if (itemUsed.getId() == KEEP_SAKE_KEY && itemUsedWith.getId() == KEEP_SAKE_KEY)
			return false;
		Item keepSakeKey = itemUsed.getId() == KEEP_SAKE_KEY ? itemUsed : itemUsedWith;
		Item keepSakeItem = itemUsed.getId() == KEEP_SAKE_KEY ? itemUsedWith : itemUsed;
		if (keepSakeItem == null || keepSakeKey == null)
			return false;
		if (player.getEquipment().getKeepSakeItems().size() >= 50) {
			player.getPackets().sendGameMessage("You can only keep sake 50 items.");
			return false;
		}
		int equipSlot = keepSakeItem.getDefinitions().getEquipSlot();
		if (equipSlot == Equipment.SLOT_ARROWS || equipSlot == Equipment.SLOT_AURA
				|| equipSlot == Equipment.SLOT_RING) {
			player.getPackets().sendGameMessage(
					"You can only keep sake items that goes into head, cape, neck, body, legs, gloves, main hand, off-hand, or boots slots.");
			return false;
		}
		if (equipSlot == -1) {
			player.getPackets().sendGameMessage("You can't keep sake this item as its not wearable.");
			return false;
		}
		if (!ItemConstants.canWear(keepSakeItem, player, true)) {
			player.getPackets().sendGameMessage("You don't have enough requirments to keep sake this item.");
			return false;
		}
		if (keepSakeItem.getDefinitions().isBindItem() || keepSakeItem.getDefinitions().isLended()
				|| keepSakeItem.getDefinitions().isStackable())
			return false;
		String name = keepSakeItem.getName().toLowerCase();
		if (name.contains("broken")) {
			player.getPackets().sendGameMessage("You can't keep sake broken items.");
			return false;
		}
		for (Item item : player.getEquipment().getKeepSakeItems()) {
			if (item == null)
				continue;
			if (item.getId() == keepSakeItem.getId()) {
				player.getPackets().sendGameMessage("You already have that item in your keepsake box.");
				return false;
			}
		}
		player.stopAll();
		player.getDialogueManager().startDialogue(new Dialogue() {

			@Override
			public void start() {
				sendOptionsDialogue("DO YOU WANT TO KEEP SAKE THIS ITEM?",
						"Yes, keep sake this item.(You won't be able to retrieve key)", "No, I would like to keep it.");
			}

			@Override
			public void run(int interfaceId, int componentId) {
				if (componentId == OPTION_1) {
					player.getEquipment().getKeepSakeItems().add(keepSakeItem);
					player.getPackets().sendGameMessage("You have added " + keepSakeItem.getName()
							+ " to keepsakes. It will appear along with other in cosmetic list.");
					player.getInventory().deleteItem(KEEP_SAKE_KEY, 1);
					player.getInventory().deleteItem(keepSakeItem);
				}
				end();
			}

			@Override
			public void finish() {

			}

		});
		return true;
	}

	public static boolean isRestrictedItem(Player player, Cosmetic... cosmetics) {
		for (Cosmetic costume : cosmetics) {
			if (isRestrictedItem(player, costume.getItemId()))
				return true;
		}
		return false;
	}

	public static boolean isRestrictedItem(Player player, int itemId) {
		if (player.getRights() == 2) {
			return false;
		}
		for (Cosmetic cosmetic : Cosmetics.HIDE_ALL.getCosmetics()) {
			if (itemId == cosmetic.getItemId())
				return false;
		}
		for (Item item : player.getEquipment().getKeepSakeItems()) {
			if (item == null)
				continue;
			if (item.getId() == itemId)
				return false;
		}
		return getEarnedMessageRequirement(player, itemId) != null || player.isLockedCostume(itemId);
	}

	public static String getEarnedMessageRequirement(Player player, int itemId) {
		
		 switch (itemId) {
		 
		 }
		 
		return null;
	}

	public static void UnlockCostume(Player player, Cosmetics cosmetic) {
		boolean unlocked = false;
		for (Cosmetic costume : cosmetic.getCosmetics()) {
			if (player.getUnlockedCostumesIds().contains(costume.getItemId()))
				continue;
			player.getUnlockedCostumesIds().add(costume.getItemId());
			unlocked = true;
		}
		if (unlocked)
			player.getPackets().sendGameMessage("<col=00ff00>You have unlocked " + cosmetic.getName() + "!");
	}

	public static void previewCosmetic(Player player, Cosmetics costume, final int page, final String keyWord) {
		ItemsContainer<Item> cosmeticPreviewItems = new ItemsContainer<>(19, false);
		player.getTemporaryAttributtes().put("Cosmetics", Boolean.TRUE);
		player.getPackets().sendHideIComponent(667, 4, true);
		player.getPackets().sendHideIComponent(667, 9, true);
		player.getPackets().sendHideIComponent(667, 24, true);
		player.getPackets().sendHideIComponent(667, 84, true);
		for (Cosmetic cosmetic : costume.getCosmetics()) {
			cosmeticPreviewItems.set(cosmetic.getSlot(), new Item(cosmetic.getItemId()));
		}
		player.getEquipment().setCosmeticPreviewItems(cosmeticPreviewItems);
		player.getAppearence().generateAppearenceData();
		player.getInterfaceManager().sendInterface(667);
		player.setCloseInterfacesEvent(() -> {
			player.getEquipment().setCosmeticPreviewItems(null);
			player.getAppearence().generateAppearenceData();
			player.getTemporaryAttributtes().remove("Cosmetics");
			openCosmeticsStore(player, keyWord, page);
		});
	}

	public static void openCosmeticsStore(Player player, int page) {
		CosmeticsHandler.openCosmeticsStore(player, null, page);
	}

	public static void openCosmeticsStore(Player player, String keyWord, int page) {
		player.getDialogueManager().startDialogue("CosmeticStoreD", page, keyWord);
	}

	public enum Cosmetics {
		 
        HIDE_ALL(new Cosmetic("Hide helmet", 27146, 0), new Cosmetic("Hide cape", 27147, 1), new Cosmetic("Hide necklace", 30038, 2), new Cosmetic("Hide torso", 30039, 4), new Cosmetic("Hide legs", 30040, 7), new Cosmetic("Hide gloves", 30042, 9), new Cosmetic("Hide boots", 30041, 10), new Cosmetic("Hide effects", 35865, 14)),
 
        MOD_ROY_OUTFIT(new Cosmetic("Executioner Cowl", 28001, 0), new Cosmetic("Arrav (Cursed) Sword", 34315, 3), new Cosmetic("Executioner Robe Top", 28003, 4), new Cosmetic("Arrav (Cursed) Off-hand Sword", 34316, 5), new Cosmetic("Executioner Robe Bottom", 28007, 7), new Cosmetic("Executioner Gloves", 28005, 9), new Cosmetic("Executioner Boots", 28009, 10), new Cosmetic("Infernal Gaze", 29092, 14)),
 
        MOD_RAVEN_OUTFIT(new Cosmetic("Aviansie Skyguard Head", 31536, 0), new Cosmetic("Ravensworn Cape", 38462, 1), new Cosmetic("Cursed Reaver Wings", 30611, 18), new Cosmetic("Death Scythe", 39559, 3), new Cosmetic("null", 2591, 4), new Cosmetic("Executioner Robe Bottom", 28007, 7), new Cosmetic("Cursed Reaver Grasps", 30610, 9), new Cosmetic("Golem of Justice Boots", 30479, 10)),
 
        CABARET_OUTFIT(new Cosmetic("Cabaret Hat", 24583, 0), new Cosmetic("Cabaret Jacket", 24585, 4), new Cosmetic("Cabaret Legs", 24587, 7), new Cosmetic("Cabaret Gloves", 24591, 9), new Cosmetic("Cabaret Shoes", 24589, 10)),
 
        COLOSSEUM_OUTFIT(new Cosmetic("Colosseum Head", 24595, 0), new Cosmetic("Colosseum Jacket", 24597, 4), new Cosmetic("Colosseum Legs", 24599, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Colosseum Shoes", 24601, 10)),
 
        FELINE_OUTFIT(new Cosmetic("Feline Ears", 24605, 0), new Cosmetic("Feline Tail", 24613, 1), new Cosmetic("Feline Jacket", 24607, 4), new Cosmetic("Feline Legs", 24609, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Feline Shoes", 24611, 10)),
 
        GOTHIC_OUTFIT(new Cosmetic("Gothic Cape", 24623, 1), new Cosmetic("Gothic Jacket", 24617, 4), new Cosmetic("Gothic Legs", 24619, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Gothic Shoes", 24621, 10)),
 
        SWASHBUCKLER_OUTFIT(new Cosmetic("Swashbuckler Mask", 24627, 0), new Cosmetic("Swashbuckler Cape", 24635, 1), new Cosmetic("Swashbuckler Jacket", 24629, 4), new Cosmetic("Swashbuckler Legs", 24631, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Swashbuckler Shoes", 24633, 10)),
 
        ASSASSIN_OUTFIT(new Cosmetic("Assassin Hood", 24639, 0), new Cosmetic("Assassin Cape", 24649, 1), new Cosmetic("Assassin Scimitar", 24651, 3), new Cosmetic("Assassin Jacket", 24641, 4), new Cosmetic("Assassin Off-hand Scimitar", 26029, 5), new Cosmetic("Assassin Legs", 24643, 7), new Cosmetic("Assassin Gloves", 24647, 9), new Cosmetic("Assassin Shoes", 24645, 10)),
 
        BEACHWEAR_OUTFIT(new Cosmetic("Beachwear Head", 24827, 0), new Cosmetic("Beachwear Shirt", 24828, 4), new Cosmetic("Beachwear Shorts", 24830, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Beachwear Sandals", 24832, 10)),
 
        MONARCH_OUTFIT(new Cosmetic("Monarch Crown", 25074, 0), new Cosmetic("Monarch Doublet", 25076, 4), new Cosmetic("Monarch Legs", 25080, 7), new Cosmetic("Monarch Gloves", 25078, 9), new Cosmetic("Monarch Shoes", 25082, 10)),
 
        NOBLE_OUTFIT(new Cosmetic("Noble Hat", 25086, 0), new Cosmetic("Noble Jacket", 25088, 4), new Cosmetic("Noble Legs", 25092, 7), new Cosmetic("Noble Gloves", 25090, 9), new Cosmetic("Noble Shoes", 25094, 10)),
 
        SERVANT_OUTFIT(new Cosmetic("Servant Hat", 25098, 0), new Cosmetic("Servant Amulet", 25100, 2), new Cosmetic("Servant Jacket", 25102, 4), new Cosmetic("Servant Legs", 25106, 7), new Cosmetic("Servant Gloves", 25104, 9), new Cosmetic("Servant Shoes", 25108, 10)),
 
        FOX_OUTFIT(new Cosmetic("Fox Ears", 25136, 0), new Cosmetic("Fox Tail", 25142, 1), new Cosmetic("Fox Jacket", 25138, 4), new Cosmetic("Fox Legs", 25140, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Fox Shoes", 25144, 10)),
 
        WOLF_OUTFIT(new Cosmetic("Wolf Ears", 25148, 0), new Cosmetic("Wolf Tail", 25154, 1), new Cosmetic("Wolf Jacket", 25150, 4), new Cosmetic("Wolf Legs", 25152, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Wolf Shoes", 25156, 10)),
 
        PANDA_OUTFIT(new Cosmetic("Panda Ears", 25160, 0), new Cosmetic("Panda Tail", 25166, 1), new Cosmetic("Panda Jacket", 25162, 4), new Cosmetic("Panda Legs", 25164, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Panda Shoes", 25168, 10)),
 
        DWARVEN_WARSUIT_OUTFIT(new Cosmetic("Dwarven Warsuit Helm", 25273, 0), new Cosmetic("Dwarven Warsuit Longsword", 25283, 3), new Cosmetic("Dwarven Warsuit Chest", 25275, 4), new Cosmetic("Dwarven Warsuit Shield Off-hand", 25287, 5), new Cosmetic("Dwarven Warsuit Legwear", 25277, 7), new Cosmetic("Dwarven Warsuit Gauntlets", 25279, 9), new Cosmetic("Dwarven Warsuit Boots", 25281, 10)),
 
        KRILS_BATTLEGEAR_OUTFIT(new Cosmetic("K'ril's Battlegear Helm", 25374, 0), new Cosmetic("K'ril's Battlegear Chest Armour", 25376, 4), new Cosmetic("K'ril's Battlegear Leg Armour", 25378, 7), new Cosmetic("K'ril's Battlegear Gauntlets", 25380, 9), new Cosmetic("K'ril's Battlegear Boots", 25382, 10)),
 
        KRILS_GODCRUSHER_OUTFIT(new Cosmetic("K'ril's Godcrusher Helm", 25386, 0), new Cosmetic("K'ril's Godcrusher Winged Cape", 25396, 1), new Cosmetic("K'ril's Godcrusher Chest Armour", 25388, 4), new Cosmetic("K'ril's Godcrusher Leg Armour", 25390, 7), new Cosmetic("K'ril's Godcrusher Gauntlets", 25392, 9), new Cosmetic("K'ril's Godcrusher Boots", 25394, 10)),
 
        ARIANE_OUTFIT(new Cosmetic("Ariane Tiara", 26043, 0), new Cosmetic("Ariane Staff", 26053, 3), new Cosmetic("Ariane Robe Top", 26045, 4), new Cosmetic("Ariane Robe Bottom", 26047, 7), new Cosmetic("Ariane Bracers", 26049, 9), new Cosmetic("Ariane Boots", 26051, 10)),
 
        OZAN_OUTFIT(new Cosmetic("Ozan Cape", 26071, 1), new Cosmetic("Ozan Bow", 26073, 3), new Cosmetic("Ozan Tunic", 26063, 4), new Cosmetic("Ozan Breeches", 26065, 7), new Cosmetic("Ozan Gloves", 26067, 9), new Cosmetic("Ozan Boots", 26069, 10)),
 
        TOKHAAR_BRUTE_OUTFIT(new Cosmetic("TokHaar (Brute) Helm", 26158, 0), new Cosmetic("TokHaar (Brute) Chest Armour", 26160, 4), new Cosmetic("TokHaar (Brute) Leg Armour", 26162, 7), new Cosmetic("TokHaar (Brute) Gauntlets", 26164, 9), new Cosmetic("TokHaar (Brute) Boots", 26166, 10)),
 
        TOKHAAR_VETERAN_OUTFIT(new Cosmetic("TokHaar (Veteran) Helm", 26170, 0), new Cosmetic("TokHaar (Veteran) Chest Armour", 26172, 4), new Cosmetic("TokHaar (Veteran) Leg Armour", 26174, 7), new Cosmetic("TokHaar (Veteran) Gauntlets", 26176, 9), new Cosmetic("TokHaar (Veteran) Boots", 26178, 10)),
 
        TOKHAAR_WARLORD_OUTFIT(new Cosmetic("TokHaar (Warlord) Helm", 26182, 0), new Cosmetic("TokHaar (Warlord) Chest Armour", 26184, 4), new Cosmetic("TokHaar (Warlord) Leg Armour", 26186, 7), new Cosmetic("TokHaar (Warlord) Gauntlets", 26188, 9), new Cosmetic("TokHaar (Warlord) Boots", 26190, 10)),
 
        EASTERN_CAPTAIN_OUTFIT(new Cosmetic("Eastern Captain Tricorne", 26402, 0), new Cosmetic("Eastern Captain Coat", 26404, 4), new Cosmetic("Eastern Captain Trousers", 26406, 7), new Cosmetic("Eastern Captain Gloves", 26408, 9), new Cosmetic("Eastern Captain Boots", 26410, 10)),
 
        EASTERN_CREW_OUTFIT(new Cosmetic("Eastern Crew Hat", 26414, 0), new Cosmetic("Eastern Crew Shirt", 26416, 4), new Cosmetic("Eastern Crew Trousers", 26418, 7), new Cosmetic("Eastern Crew Gloves", 26420, 9), new Cosmetic("Eastern Crew Boots", 26422, 10)),
 
        WESTERN_CAPTAIN_OUTFIT(new Cosmetic("Western Captain Hat", 26390, 0), new Cosmetic("Western Captain Coat", 26392, 4), new Cosmetic("Western Captain Trousers", 26394, 7), new Cosmetic("Western Captain Gloves", 26396, 9), new Cosmetic("Western Captain Boots", 26398, 10)),
 
        WESTERN_CREW_OUTFIT(new Cosmetic("Western Crew Hat", 26426, 0), new Cosmetic("Western Crew Shirt", 26428, 4), new Cosmetic("Western Crew Trousers", 26430, 7), new Cosmetic("Western Crew Gloves", 26432, 9), new Cosmetic("Western Crew Boots", 26434, 10)),
 
        PALADIN_OUTFIT(new Cosmetic("Paladin Gauntlets", 26472, 9), new Cosmetic("Paladin Boots", 26470, 10), new Cosmetic("Paladin Chestplate", 26466, 4), new Cosmetic("Paladin Legplates", 26468, 7)),
 
        PALADIN_HERO_OUTFIT(new Cosmetic("Paladin (Hero) Helm", 26464, 0), new Cosmetic("Paladin Chestplate", 26466, 4), new Cosmetic("Paladin Legplates", 26468, 7), new Cosmetic("Paladin Gauntlets", 26472, 9), new Cosmetic("Paladin Boots", 26470, 10)),
 
        KALPHITE_SENTINEL_OUTFIT(new Cosmetic("Kalphite Sentinel Helm", 27075, 0), new Cosmetic("Kalphite Sentinel Cape", 27076, 1), new Cosmetic("Kalphite Sentinel Chest Amour", 27077, 4), new Cosmetic("Kalphite Sentinel Leg Armour", 27079, 7), new Cosmetic("Kalphite Sentinel Gauntlets", 27078, 9), new Cosmetic("Kalphite Sentinel Boots", 27080, 10)),
 
        KALPHITE_EMISSARY_OUTFIT(new Cosmetic("Kalphite Emissary Antennae", 27083, 0), new Cosmetic("Kalphite Emissary Wing Cape", 27084, 1), new Cosmetic("Kalphite Emissary Robe Top", 27085, 4), new Cosmetic("Kalphite Emissary Robe Bottom", 27087, 7), new Cosmetic("Kalphite Emissary Gloves", 27086, 9), new Cosmetic("Kalphite Emissary Boots", 27088, 10)),
 
        SHADOW_CAT_OUTFIT(new Cosmetic("Shadow Cat Ears", 27174, 0), new Cosmetic("Shadow Cat Tail", 27178, 1), new Cosmetic("Shadow Cat Jacket", 27175, 4), new Cosmetic("Shadow Cat Legs", 27176, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Shadow Cat Shoes", 27177, 10)),
 
        SHADOW_HUNTER_OUTFIT(new Cosmetic("Shadow Hunter Hood", 27181, 0), new Cosmetic("Shadow Hunter Cape", 27186, 1), new Cosmetic("Shadow Hunter Jacket", 27182, 4), new Cosmetic("Shadow Hunter Legs", 27183, 7), new Cosmetic("Shadow Hunter Gloves", 27185, 9), new Cosmetic("Shadow Hunter Shoes", 27184, 10)),
 
        SHADOW_SENTINEL_OUTFIT(new Cosmetic("Shadow Sentinel Helm", 27189, 0), new Cosmetic("Shadow Sentinel Chestplate", 27190, 4), new Cosmetic("Shadow Sentinel Legwear", 27191, 7), new Cosmetic("Shadow Sentinel Gauntlets", 27192, 9), new Cosmetic("Shadow Sentinel Boots", 27193, 10)),
 
        SHADOW_DEMON_OUTFIT(new Cosmetic("Shadow Demon Helm", 27205, 0), new Cosmetic("Shadow Demon Cape", 27210, 1), new Cosmetic("Shadow Demon Chestplate", 27206, 4), new Cosmetic("Shadow Demon Leg Armour", 27207, 7), new Cosmetic("Shadow Demon Gauntlets", 27208, 9), new Cosmetic("Shadow Demon Boots", 27209, 10)),
 
        SHADOW_KNIGHT_OUTFIT(new Cosmetic("Shadow Knight Helmet", 27198, 0), new Cosmetic("Shadow Knight Chestplate", 27199, 4), new Cosmetic("Shadow Knight Legs", 27200, 7), new Cosmetic("Shadow Knight Gloves", 27202, 9), new Cosmetic("Shadow Knight Boots", 27201, 10)),
 
        DEMONFLESH_GREATER_OUTFIT(new Cosmetic("Demonflesh (Greater) Mask", 27120, 0), new Cosmetic("Demonflesh (Greater) Cape", 27130, 1), new Cosmetic("Demonflesh (Greater) Torso", 27122, 4), new Cosmetic("Demonflesh (Greater) Legs", 27124, 7), new Cosmetic("Demonflesh (Greater) Gloves", 27126, 9), new Cosmetic("Demonflesh (Greater) Boots", 27128, 10)),
 
        DEMONFLESH_LESSER_OUTFIT(new Cosmetic("Demonflesh (Lesser) Mask", 27134, 0), new Cosmetic("Demonflesh (Lesser) Torso", 27136, 4), new Cosmetic("Demonflesh (Lesser) Legs", 27138, 7), new Cosmetic("Demonflesh (Lesser) Gloves", 27140, 9), new Cosmetic("Demonflesh (Lesser) Boots", 27142, 10)),
 
        DRAGON_WOLF_OUTFIT(new Cosmetic("Dragon Wolf Helmet", 27220, 0), new Cosmetic("Dragon Wolf Tunic", 27222, 4), new Cosmetic("Dragon Wolf Leggings", 27224, 7), new Cosmetic("Dragon Wolf Gloves", 27226, 9), new Cosmetic("Dragon Wolf Boots", 27228, 10)),
 
        WAR_ROBES_GUTHIX_OUTFIT(new Cosmetic("War Robes (Guthix) Hood", 27419, 0), new Cosmetic("War Robes (Guthix) Top", 27421, 4), new Cosmetic("War Robes (Guthix) Bottom", 27423, 7), new Cosmetic("War Robes (Guthix) Gloves", 27425, 9), new Cosmetic("War Robes (Guthix) Boots", 27427, 10)),
 
        WAR_ROBES_SARADOMIN_OUTFIT(new Cosmetic("War Robes (Saradomin) Hood", 27431, 0), new Cosmetic("War Robes (Saradomin) Top", 27433, 4), new Cosmetic("War Robes (Saradomin) Bottom", 27435, 7), new Cosmetic("War Robes (Saradomin) Gloves", 27437, 9), new Cosmetic("War Robes (Saradomin) Boots", 27439, 10)),
 
        WAR_ROBES_ZAMORAK_OUTFIT(new Cosmetic("War Robes (Zamorak) Hood", 27443, 0), new Cosmetic("War Robes (Zamorak) Top", 27445, 4), new Cosmetic("War Robes (Zamorak) Bottom", 27447, 7), new Cosmetic("War Robes (Zamorak) Gloves", 27449, 9), new Cosmetic("War Robes (Zamorak) Boots", 27451, 10)),
 
        WAR_ROBES_ZAROS_OUTFIT(new Cosmetic("War Robes (Zaros) Hood", 27455, 0), new Cosmetic("War Robes (Zaros) Top", 27457, 4), new Cosmetic("War Robes (Zaros) Bottom", 27459, 7), new Cosmetic("War Robes (Zaros) Gloves", 27461, 9), new Cosmetic("War Robes (Zaros) Boots", 27463, 10)),
 
        ROBES_OF_SORROW_OUTFIT(new Cosmetic("Robes of Sorrow Cowl", 27557, 0), new Cosmetic("Robes of Sorrow Top", 27558, 4), new Cosmetic("Robes of Sorrow Bottom", 27560, 7), new Cosmetic("Robes of Sorrow Gauntlets", 27559, 9), new Cosmetic("Robes of Sorrow Boots", 27561, 10)),
 
        VESTMENTS_OF_SORROW_OUTFIT(new Cosmetic("Vestments of Sorrow Hood", 27565, 0), new Cosmetic("Vestments of Sorrow Top", 27566, 4), new Cosmetic("Vestments of Sorrow Bottom", 27568, 7), new Cosmetic("Vestments of Sorrow Gloves", 27567, 9), new Cosmetic("Vestments of Sorrow Footwear", 27569, 10)),
 
        ROBES_OF_REMEMBRANCE_OUTFIT(new Cosmetic("Robes of Remembrance Garland", 27572, 0), new Cosmetic("Robes of Remembrance Top", 27573, 4), new Cosmetic("Robes of Remembrance Bottom", 27575, 7), new Cosmetic("Robes of Remembrance Bracers", 27574, 9), new Cosmetic("Robes of Remembrance Sandals", 27576, 10)),
 
        VESTMENTS_OF_REMEMBRANCE_OUTFIT(new Cosmetic("Vestments of Remembrance Wreath", 27580, 0), new Cosmetic("Vestments of Remembrance Top", 27581, 4), new Cosmetic("Vestments of Remembrance Bottom", 27583, 7), new Cosmetic("Vestments of Remembrance Handwraps", 27582, 9), new Cosmetic("Vestments of Remembrance Footwraps", 27584, 10)),
 
        SKYPOUNCER_OUTFIT(new Cosmetic("Skypouncer Headpiece", 27549, 0), new Cosmetic("Skypouncer Cape", 27554, 1), new Cosmetic("Skypouncer Chestpiece", 27550, 4), new Cosmetic("Skypouncer Legwear", 27552, 7), new Cosmetic("Skypouncer Gloves", 27551, 9), new Cosmetic("Skypouncer Boots", 27553, 10)),
 
        EXECUTIONER_OUTFIT(new Cosmetic("Executioner Cowl", 28001, 0), new Cosmetic("Executioner Robe Top", 28003, 4), new Cosmetic("Executioner Robe Bottom", 28007, 7), new Cosmetic("Executioner Gloves", 28005, 9), new Cosmetic("Executioner Boots", 28009, 10)),
 
        ELEMENTAL_FLAMEHEART_OUTFIT(new Cosmetic("Elemental (Flameheart) Headgear", 28049, 0), new Cosmetic("Elemental (Flameheart) Chest", 28050, 4), new Cosmetic("Elemental (Flameheart) Legs", 28052, 7), new Cosmetic("Elemental (Flameheart) Gloves", 28051, 9), new Cosmetic("Elemental (Flameheart) Boots", 28053, 10)),
 
        ELEMENTAL_STONEHEART_OUTFIT(new Cosmetic("Elemental (Stoneheart) Helm", 28056, 0), new Cosmetic("Elemental (Stoneheart) Chestplate", 28057, 4), new Cosmetic("Elemental (Stoneheart) Greaves", 28059, 7), new Cosmetic("Elemental (Stoneheart) Gauntlets", 28058, 9), new Cosmetic("Elemental (Stoneheart) Boots", 28060, 10)),
 
        ELEMENTAL_STORMHEART_OUTFIT(new Cosmetic("Elemental (Stormheart) Headgear", 28063, 0), new Cosmetic("Elemental (Stormheart) Robe Top", 28064, 4), new Cosmetic("Elemental (Stormheart) Robe Bottom", 28066, 7), new Cosmetic("Elemental (Stormheart) Wraps", 28065, 9), new Cosmetic("Elemental (Stormheart) Boots", 28067, 10)),
 
        ELEMENTAL_ICEHEART_OUTFIT(new Cosmetic("Elemental (Iceheart) Headgear", 28070, 0), new Cosmetic("Elemental (Iceheart) Robe Top", 28071, 4), new Cosmetic("Elemental (Iceheart) Robe Bottom", 28073, 7), new Cosmetic("Elemental (Iceheart) Wraps", 28072, 9), new Cosmetic("Elemental (Iceheart) Boots", 28074, 10)),
 
        COLOSSUS_OUTFIT(new Cosmetic("Colossus Helm", 28838, 0), new Cosmetic("Colossus Cape", 28843, 1), new Cosmetic("Colossus Cuirass", 28839, 4), new Cosmetic("Colossus Greaves", 28840, 7), new Cosmetic("Colossus Gauntlets", 28841, 9), new Cosmetic("Colossus Boots", 28842, 10)),
 
        COLOSSUS_VETERAN_OUTFIT(new Cosmetic("Colossus (Veteran) Helm", 28846, 0), new Cosmetic("Colossus (Veteran) Cape", 28851, 1), new Cosmetic("Colossus (Veteran) Cuirass", 28847, 4), new Cosmetic("Colossus (Veteran) Greaves", 28848, 7), new Cosmetic("Colossus (Veteran) Gauntlets", 28849, 9), new Cosmetic("Colossus (Veteran) Boots", 28850, 10)),
 
        TITAN_OUTFIT(new Cosmetic("Titan Helm", 28854, 0), new Cosmetic("Titan Cuirass", 28855, 4), new Cosmetic("Titan Greaves", 28856, 7), new Cosmetic("Titan Gauntlets", 28857, 9), new Cosmetic("Titan Boots", 28858, 10)),
 
        TITAN_VETERAN_OUTFIT(new Cosmetic("Titan (Veteran) Helm", 28861, 0), new Cosmetic("Titan (Veteran) Cuirass", 28862, 4), new Cosmetic("Titan (Veteran) Greaves", 28863, 7), new Cosmetic("Titan (Veteran) Gauntlets", 28864, 9), new Cosmetic("Titan (Veteran) Boots", 28865, 10)),
 
        BEHEMOTH_OUTFIT(new Cosmetic("Behemoth Helm", 28868, 0), new Cosmetic("Behemoth Cape", 28873, 1), new Cosmetic("Behemoth Cuirass", 28869, 4), new Cosmetic("Behemoth Greaves", 28870, 7), new Cosmetic("Behemoth Gauntlets", 28871, 9), new Cosmetic("Behemoth Boots", 28872, 10)),
 
        BEHEMOTH_VETERAN_OUTFIT(new Cosmetic("Behemoth (Veteran) Helm", 28876, 0), new Cosmetic("Behemoth (Veteran) Cape", 28881, 1), new Cosmetic("Behemoth (Veteran) Cuirass", 28877, 4), new Cosmetic("Behemoth (Veteran) Greaves", 28878, 7), new Cosmetic("Behemoth (Veteran) Gauntlets", 28879, 9), new Cosmetic("Behemoth (Veteran) Boots", 28880, 10)),
 
        BEAST_OUTFIT(new Cosmetic("Beast Helm", 28884, 0), new Cosmetic("Beast Cuirass", 28885, 4), new Cosmetic("Beast Greaves", 28886, 7), new Cosmetic("Beast Gauntlets", 28887, 9), new Cosmetic("Beast Boots", 28888, 10)),
 
        BEAST_VETERAN_OUTFIT(new Cosmetic("Beast (Veteran) Helm", 28891, 0), new Cosmetic("Beast (Veteran) Cuirass", 28892, 4), new Cosmetic("Beast (Veteran) Greaves", 28893, 7), new Cosmetic("Beast (Veteran) Gauntlets", 28894, 9), new Cosmetic("Beast (Veteran) Boots", 28895, 10)),
 
        LINZA_OUTFIT(new Cosmetic("Linza Hammer", 28958, 3), new Cosmetic("Linza Leather Vest", 28950, 4), new Cosmetic("Linza Off-hand Hammer", 28960, 5), new Cosmetic("Linza Apron", 28952, 7), new Cosmetic("Linza Gloves", 28954, 9), new Cosmetic("Linza Boots", 28956, 10)),
 
        SIR_OWEN_OUTFIT(new Cosmetic("Sir Owen Longsword", 28979, 3), new Cosmetic("Sir Owen Cuirass", 28971, 4), new Cosmetic("Sir Owen Shield Off-hand", 28989, 5), new Cosmetic("Sir Owen Cuisses", 28973, 7), new Cosmetic("Sir Owen Gauntlets", 28975, 9), new Cosmetic("Sir Owen Sabatons", 28977, 10)),
 
        DERVISH_OUTFIT(new Cosmetic("Dervish Hat", 29009, 0), new Cosmetic("Dervish Robe", 29010, 4), new Cosmetic("Dervish Legs", 29011, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Dervish Boots", 29012, 10)),
 
        EASTERN_OUTFIT(new Cosmetic("Eastern Hair", 29015, 0), new Cosmetic("Eastern Robes", 29016, 4), new Cosmetic("Eastern Legs", 29017, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Eastern Sandals", 29018, 10)),
 
        TRIBAL_OUTFIT(new Cosmetic("Tribal Circlet", 29021, 0), new Cosmetic("Tribal Top", 29022, 4), new Cosmetic("Tribal Legs", 29023, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Tribal Shoes", 29024, 10)),
 
        SAMBA_OUTFIT(new Cosmetic("Samba Headdress", 29027, 0), new Cosmetic("Samba Top", 29028, 4), new Cosmetic("Samba Loincloth", 29029, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Samba Sandals", 29030, 10)),
 
        THEATRICAL_OUTFIT(new Cosmetic("Theatrical Hat", 29033, 0), new Cosmetic("Theatrical Tunic", 29034, 4), new Cosmetic("Theatrical Legs", 29035, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Theatrical Shoes", 29036, 10)),
 
        PHARAOH_OUTFIT(new Cosmetic("Pharaoh Hat", 29039, 0), new Cosmetic("Pharaoh Top", 29040, 4), new Cosmetic("Pharaoh Shendyt", 29041, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Pharaoh Sandals", 29042, 10)),
 
        WUSHANKO_OUTFIT(new Cosmetic("Wushanko Hat", 29045, 0), new Cosmetic("Wushanko Top", 29046, 4), new Cosmetic("Wushanko Legs", 29047, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Wushanko Shoes", 29048, 10)),
 
        SILKEN_OUTFIT(new Cosmetic("Silken Turban", 29051, 0), new Cosmetic("Silken Top", 29052, 4), new Cosmetic("Silken Legs", 29053, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Silken Boots", 29054, 10)),
 
        COLONIST_OUTFIT(new Cosmetic("Colonist Hat", 29057, 0), new Cosmetic("Colonist Top", 29058, 4), new Cosmetic("Colonist Legs", 29059, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Colonist Boots", 29060, 10)),
 
        HIGHLAND_OUTFIT(new Cosmetic("Highland War Paint", 29063, 0), new Cosmetic("Highland Top", 29064, 4), new Cosmetic("Highland Kilt", 29065, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Highland Boots", 29066, 10)),
 
        FEATHERED_SERPENT_OUTFIT(new Cosmetic("Feathered Serpent Headdress", 29069, 0), new Cosmetic("Feathered Serpent Body", 29070, 4), new Cosmetic("Feathered Serpent Legs", 29071, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Feathered Serpent Boots", 29072, 10)),
 
        MUSKETEER_OUTFIT(new Cosmetic("Musketeer Hat", 29075, 0), new Cosmetic("Musketeer Body", 29076, 4), new Cosmetic("Musketeer Legs", 29077, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Musketeer Boots", 29078, 10)),
 
        ELVEN_OUTFIT(new Cosmetic("Elven Wig", 29081, 0), new Cosmetic("Elven Top", 29082, 4), new Cosmetic("Elven Legs", 29083, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Elven Shoes", 29084, 10)),
 
        WEREWOLF_OUTFIT(new Cosmetic("Werewolf Mask", 29087, 0), new Cosmetic("Werewolf Torso", 29088, 4), new Cosmetic("Werewolf Legs", 29090, 7), new Cosmetic("Werewolf Claws", 29089, 9), new Cosmetic("Werewolf Paws", 29091, 10)),
 
        AMBASSADOR_OF_ORDER_OUTFIT(new Cosmetic("Ambassador of Order Circlet", 29561, 0), new Cosmetic("Ambassador of Order Cloak", 29571, 1), new Cosmetic("Ambassador of Order Robe Top", 29563, 4), new Cosmetic("Ambassador of Order Robe Bottom", 29565, 7), new Cosmetic("Ambassador of Order Gloves", 29567, 9), new Cosmetic("Ambassador of Order Shoes", 29569, 10)),
 
        ENVOY_OF_ORDER_OUTFIT(new Cosmetic("Envoy of Order Circlet", 29575, 0), new Cosmetic("Envoy of Order Robe Top", 29577, 4), new Cosmetic("Envoy of Order Robe Bottom", 29579, 7), new Cosmetic("Envoy of Order Gloves", 29581, 9), new Cosmetic("Envoy of Order Shoes", 29583, 10)),
 
        AMBASSADOR_OF_CHAOS_OUTFIT(new Cosmetic("Ambassador of Chaos Cap", 29587, 0), new Cosmetic("Ambassador of Chaos Cloak", 29597, 1), new Cosmetic("Ambassador of Chaos Robe Top", 29589, 4), new Cosmetic("Ambassador of Chaos Robe Bottom", 29591, 7), new Cosmetic("Ambassador of Chaos Gloves", 29593, 9), new Cosmetic("Ambassador of Chaos Shoes", 29595, 10)),
 
        ENVOY_OF_CHAOS_OUTFIT(new Cosmetic("Envoy of Chaos Cap", 29601, 0), new Cosmetic("Envoy of Chaos Robe Top", 29603, 4), new Cosmetic("Envoy of Chaos Robe Bottom", 29605, 7), new Cosmetic("Envoy of Chaos Gloves", 29607, 9), new Cosmetic("Envoy of Chaos Shoes", 29609, 10)),
 
        AURORA_OUTFIT(new Cosmetic("Aurora Helm", 28428, 0), new Cosmetic("Aurora Longsword", 28429, 3), new Cosmetic("Aurora Cuirass", 28431, 4), new Cosmetic("Aurora Off-hand Longsword", 28430, 5), new Cosmetic("Aurora Greaves", 28432, 7), new Cosmetic("Aurora Gauntlets", 28433, 9), new Cosmetic("Aurora Boots", 28434, 10)),
 
        TEMPLAR_OUTFIT(new Cosmetic("Templar Helm", 28941, 0), new Cosmetic("Templar Cuirass", 28942, 4), new Cosmetic("Templar Greaves", 28943, 7), new Cosmetic("Templar Gauntlets", 28944, 9), new Cosmetic("Templar Boots", 28945, 10)),
 
        SUPERHERO_OUTFIT(new Cosmetic("Superhero Mask", 29421, 0), new Cosmetic("Superhero Cape", 29424, 1), new Cosmetic("Superhero Top", 29419, 4), new Cosmetic("Superhero Legs", 29420, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        SUPERIOR_HERO_OUTFIT(new Cosmetic("Superior Hero Mask", 29431, 0), new Cosmetic("Superior Hero Cape", 29436, 1), new Cosmetic("Superior Hero Top", 29432, 4), new Cosmetic("Superior Hero Legs", 29433, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        DULCIN_OUTFIT(new Cosmetic("Dulcin Helm", 29461, 0), new Cosmetic("Dulcin Cuirass", 29462, 4), new Cosmetic("Dulcin Gown", 29463, 7), new Cosmetic("Dulcin Gauntlets", 29464, 9), new Cosmetic("Dulcin Boots", 29465, 10)),
 
        RAVENSKULL_OUTFIT(new Cosmetic("Ravenskull Cowl", 29766, 0), new Cosmetic("Ravenskull Talon", 29776, 3), new Cosmetic("Ravenskull Raiment", 29768, 4), new Cosmetic("Ravenskull Off-hand Talon", 29778, 5), new Cosmetic("Ravenskull Garb", 29770, 7), new Cosmetic("Ravenskull Bracers", 29772, 9), new Cosmetic("Ravenskull Treads", 29774, 10)),
 
        DEATHLESS_REGENT_OUTFIT(new Cosmetic("Deathless Regent Headguard", 29782, 0), new Cosmetic("Deathless Regent Shroud", 29792, 1), new Cosmetic("Deathless Regent Mace 'Pain'", 29794, 3), new Cosmetic("Deathless Regent Breastplate", 29784, 4), new Cosmetic("Deathless Regent Off-hand Mace 'Agony'", 29796, 5), new Cosmetic("Deathless Regent Legplates", 29786, 7), new Cosmetic("Deathless Regent Gauntlets", 29788, 9), new Cosmetic("Deathless Regent Boots", 29790, 10)),
 
        ANCIENT_MUMMY_OUTFIT(new Cosmetic("Ancient Mummy Burial Mask", 29958, 0), new Cosmetic("Ancient Mummy Wraps", 29962, 4), new Cosmetic("Ancient Mummy Greaves", 29960, 7), new Cosmetic("Ancient Mummy Handguards", 29964, 9), new Cosmetic("Ancient Mummy Sandals", 29966, 10)),
 
        OGRE_INFILTRATOR_OUTFIT(new Cosmetic("Ogre Infiltrator Mask", 29976, 0), new Cosmetic("Ogre Infiltrator Sword-club", 29986, 3), new Cosmetic("Ogre Infiltrator Torso", 29978, 4), new Cosmetic("Ogre Infiltrator Stubs", 29980, 7), new Cosmetic("Ogre Infiltrator Hands", 29982, 9), new Cosmetic("Ogre Infiltrator Stompers", 29984, 10)),
 
        DRAKEWING_OUTFIT(new Cosmetic("Drakewing Head", 29990, 0), new Cosmetic("Drakewing Wings", 30000, 18), new Cosmetic("Drakewing Staff", 30002, 3), new Cosmetic("Drakewing Torso", 29992, 4), new Cosmetic("Drakewing Legs", 29994, 7), new Cosmetic("Drakewing Claws", 29996, 9), new Cosmetic("Drakewing Feet", 29998, 10)),
 
        REPLICA_INFINITY_OUTFIT(new Cosmetic("Replica Infinity Hat", 30147, 0), new Cosmetic("Replica Infinity Top", 30149, 4), new Cosmetic("Replica Infinity Bottoms", 30151, 7), new Cosmetic("Replica Infinity Gloves", 30153, 9), new Cosmetic("Replica Infinity Boots", 30155, 10)),
 
        REPLICA_INFINITY_AIR_OUTFIT(new Cosmetic("Replica Infinity (Air) Hat", 30159, 0), new Cosmetic("Replica Infinity (Air) Top", 30161, 4), new Cosmetic("Replica Infinity (Air) Bottoms", 30163, 7), new Cosmetic("Replica Infinity Gloves", 30153, 9), new Cosmetic("Replica Infinity Boots", 30155, 10)),
 
        REPLICA_INFINITY_EARTH_OUTFIT(new Cosmetic("Replica Infinity (Earth) Hat", 30167, 0), new Cosmetic("Replica Infinity (Earth) Top", 30169, 4), new Cosmetic("Replica Infinity (Earth) Bottoms", 30171, 7), new Cosmetic("Replica Infinity Gloves", 30153, 9), new Cosmetic("Replica Infinity Boots", 30155, 10)),
 
        REPLICA_INFINITY_FIRE_OUTFIT(new Cosmetic("Replica Infinity (Fire) Hat", 30175, 0), new Cosmetic("Replica Infinity (Fire) Top", 30177, 4), new Cosmetic("Replica Infinity (Fire) Bottoms", 30179, 7), new Cosmetic("Replica Infinity Gloves", 30153, 9), new Cosmetic("Replica Infinity Boots", 30155, 10)),
 
        REPLICA_INFINITY_WATER_OUTFIT(new Cosmetic("Replica Infinity (Water) Hat", 30183, 0), new Cosmetic("Replica Infinity (Water) Top", 30185, 4), new Cosmetic("Replica Infinity (Water) Bottoms", 30187, 7), new Cosmetic("Replica Infinity Gloves", 30153, 9), new Cosmetic("Replica Infinity Boots", 30155, 10)),
 
        REPLICA_DRAGON_OUTFIT(new Cosmetic("Replica Dragon Full Helm", 30191, 0), new Cosmetic("Replica Dragon Platebody", 30193, 4), new Cosmetic("Replica Dragon Platelegs", 30194, 7), new Cosmetic("Replica Dragon Gauntlets", 30195, 9), new Cosmetic("Replica Dragon Boots", 30196, 10)),
 
        REPLICA_DRAGON_SP_OUTFIT(new Cosmetic("Replica Dragon (sp) Full Helm", 30199, 0), new Cosmetic("Replica Dragon (sp) Platebody", 30201, 4), new Cosmetic("Replica Dragon (sp) Platelegs", 30202, 7), new Cosmetic("Replica Dragon Gauntlets", 30195, 9), new Cosmetic("Replica Dragon Boots", 30196, 10)),
 
        REPLICA_DRAGON_OR_OUTFIT(new Cosmetic("Replica Dragon (or) Full Helm", 30205, 0), new Cosmetic("Replica Dragon (or) Platebody", 30207, 4), new Cosmetic("Replica Dragon (or) Platelegs", 30208, 7), new Cosmetic("Replica Dragon Gauntlets", 30195, 9), new Cosmetic("Replica Dragon Boots", 30196, 10)),
 
        FROSTWALKER_OUTFIT(new Cosmetic("Frostwalker Hood", 30417, 0), new Cosmetic("Frostwalker Cape", 30427, 1), new Cosmetic("Frostwalker Tunic", 30419, 4), new Cosmetic("Frostwalker Leggings", 30421, 7), new Cosmetic("Frostwalker Gloves", 30423, 9), new Cosmetic("Frostwalker Boots", 30425, 10)),
 
        GLAD_TIDINGS_OUTFIT(new Cosmetic("Glad Tidings Tiara", 30433, 0), new Cosmetic("Glad Tidings Cape", 30445, 1), new Cosmetic("Glad Tidings Shirt", 30437, 4), new Cosmetic("Glad Tidings Bottoms", 30439, 7), new Cosmetic("Glad Tidings Handwraps", 30441, 9), new Cosmetic("Glad Tidings Boots", 30443, 10)),
 
        GOLEM_OF_STRENGTH_OUTFIT(new Cosmetic("Golem of Strength Helm", 30461, 0), new Cosmetic("Golem of Strength Cape", 30466, 1), new Cosmetic("Golem of Strength Chestplate", 30462, 4), new Cosmetic("Golem of Strength Legplates", 30463, 7), new Cosmetic("Golem of Strength Gauntlets", 30464, 9), new Cosmetic("Golem of Strength Boots", 30465, 10)),
 
        CONSTRUCT_OF_STRENGTH_OUTFIT(new Cosmetic("Construct of Strength Helm", 30469, 0), new Cosmetic("Construct of Strength Chestplate", 30470, 4), new Cosmetic("Construct of Strength Legplates", 30471, 7), new Cosmetic("Construct of Strength Gauntlets", 30472, 9), new Cosmetic("Construct of Strength Boots", 30473, 10)),
 
        GOLEM_OF_JUSTICE_OUTFIT(new Cosmetic("Golem of Justice Helm", 30476, 0), new Cosmetic("Golem of Justice Cape", 30481, 1), new Cosmetic("Golem of Justice Chestplate", 30477, 4), new Cosmetic("Golem of Justice Legplates", 30478, 7), new Cosmetic("Golem of Justice Gauntlets", 30480, 9), new Cosmetic("Golem of Justice Boots", 30479, 10)),
 
        CONSTRUCT_OF_JUSTICE_OUTFIT(new Cosmetic("Construct of Justice Helm", 30484, 0), new Cosmetic("Construct of Justice Chestplate", 30485, 4), new Cosmetic("Construct of Justice Legplates", 30487, 7), new Cosmetic("Construct of Justice Gauntlets", 30486, 9), new Cosmetic("Construct of Justice Boots", 30488, 10)),
 
        BLESSED_SENTINEL_OUTFIT(new Cosmetic("Blessed Sentinel Hood", 30597, 0), new Cosmetic("Blessed Sentinel Wings", 30602, 18), new Cosmetic("Blessed Sentinel Lance", 30603, 3), new Cosmetic("Blessed Sentinel Cuirass", 30598, 4), new Cosmetic("Blessed Sentinel Robe", 30599, 7), new Cosmetic("Blessed Sentinel Gloves", 30601, 9), new Cosmetic("Blessed Sentinel Boots", 30600, 10)),
 
        CURSED_REAVER_OUTFIT(new Cosmetic("Cursed Reaver Cowl", 30606, 0), new Cosmetic("Cursed Reaver Wings", 30611, 18), new Cosmetic("Cursed Reaver Scythe", 30612, 3), new Cosmetic("Cursed Reaver Garb Top", 30607, 4), new Cosmetic("Cursed Reaver Garb Bottom", 30608, 7), new Cosmetic("Cursed Reaver Grasps", 30610, 9), new Cosmetic("Cursed Reaver Boots", 30609, 10)),
 
        REPLICA_GWD_VIRTUS_OUTFIT(new Cosmetic("Replica GWD (Virtus) Mask", 30617, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Replica GWD (Virtus) Robe Top", 30618, 4), new Cosmetic("Replica GWD (Virtus) Robe Legs", 30619, 7)),
 
        REPLICA_GWD_TORVA_OUTFIT(new Cosmetic("Replica GWD (Torva) Full Helm", 30622, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Replica GWD (Torva) Platebody", 30623, 4), new Cosmetic("Replica GWD (Torva) Platelegs", 30624, 7)),
 
        REPLICA_GWD_PERNIX_OUTFIT(new Cosmetic("Replica GWD (Pernix) Cowl", 30627, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Replica GWD (Pernix) Body", 30628, 4), new Cosmetic("Replica GWD (Pernix) Chaps", 30629, 7)),
 
        CIRCUS_CLOWN_OUTFIT(new Cosmetic("Circus (Clown) Hat", 30952, 0), new Cosmetic("Circus (Clown) Tambourine", 30956, 3), new Cosmetic("Circus (Clown) Shirt", 30953, 4), new Cosmetic("Circus (Clown) Leggings", 30954, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Circus (Clown) Shoes", 30955, 10)),
 
        CIRCUS_RINGMASTER_OUTFIT(new Cosmetic("Circus (Ringmaster) Hat", 30959, 0), new Cosmetic("Circus (Ringmaster) Mega-phonus", 30963, 3), new Cosmetic("Circus (Ringmaster) Shirt", 30960, 4), new Cosmetic("Circus (Ringmaster) Pants", 30961, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Circus (Ringmaster) Boots", 30962, 10)),
 
        CIRCUS_ACROBAT_OUTFIT(new Cosmetic("Circus (Acrobat) Hat", 30967, 0), new Cosmetic("Circus (Acrobat) Shirt", 30964, 4), new Cosmetic("Circus (Acrobat) Pants", 30965, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Circus (Acrobat) Shoes", 30966, 10)),
 
        CIRCUS_AUDIENCE_OUTFIT(new Cosmetic("Circus (Audience) Hat", 30974, 0), new Cosmetic("Circus (Audience) Shirt", 30980, 4), new Cosmetic("Circus (Audience) Pants", 30983, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Circus (Audience) Shoes", 30986, 10)),
 
        CIRCUS_SPECTATOR_OUTFIT(new Cosmetic("Circus (Spectator) Hat", 30976, 0), new Cosmetic("Circus (Spectator) Shirt", 30981, 4), new Cosmetic("Circus (Spectator) Pants", 30984, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Circus (Spectator) Shoes", 30987, 10)),
 
        CIRCUS_CROWD_OUTFIT(new Cosmetic("Circus (Crowd) Hat", 30978, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Circus (Crowd) Shirt", 30982, 4), new Cosmetic("Circus (Crowd) Pants", 30985, 7)),
 
        SHADOW_ARIANE_OUTFIT(new Cosmetic("Shadow Ariane Tiara", 31128, 0), new Cosmetic("Shadow Ariane Robe Top", 31129, 4), new Cosmetic("Shadow Ariane Robe Bottom", 31130, 7), new Cosmetic("Shadow Ariane Bracers", 31131, 9), new Cosmetic("Shadow Ariane Boots", 31132, 10)),
 
        SHADOW_OZAN_OUTFIT(new Cosmetic("Shadow Ozan Cape", 31139, 1), new Cosmetic("Shadow Ozan Tunic", 31135, 4), new Cosmetic("Shadow Ozan Breeches", 31136, 7), new Cosmetic("Shadow Ozan Gloves", 31137, 9), new Cosmetic("Shadow Ozan Boots", 31138, 10)),
 
        SHADOW_LINZA_OUTFIT(new Cosmetic("Shadow Linza Gloves", 31113, 9), new Cosmetic("Shadow Linza Boots", 31114, 10), new Cosmetic("Shadow Linza Leather Vest", 31111, 4), new Cosmetic("Shadow Linza Apron", 31112, 7)),
 
        SHADOW_SIR_OWEN_OUTFIT(new Cosmetic("Shadow Sir Owen Gauntlets", 31120, 9), new Cosmetic("Shadow Sir Owen Boots", 31121, 10), new Cosmetic("Shadow Sir Owen Cuirass", 31118, 4), new Cosmetic("Shadow Sir Owen Cuisses", 31119, 7)),
 
        REPLICA_METAL_OUTFIT(new Cosmetic("Replica Metal Full Helm", 31211, 0), new Cosmetic("Replica Metal Platebody", 31212, 4), new Cosmetic("Replica Metal Kiteshield Off-hand", 31215, 5), new Cosmetic("Replica Metal Platelegs", 31213, 7), new Cosmetic("Replica Metal Gloves", 31216, 9), new Cosmetic("Replica Metal Boots", 31217, 10)),
 
        REPLICA_METAL_T_OUTFIT(new Cosmetic("Replica Metal (t) Full Helm", 31219, 0), new Cosmetic("Replica Metal (t) Platebody", 31220, 4), new Cosmetic("Replica Metal (t) Kiteshield Off-hand", 31223, 5), new Cosmetic("Replica Metal (t) Platelegs", 31221, 7), new Cosmetic("Replica Metal Gloves", 31216, 9), new Cosmetic("Replica Metal Boots", 31217, 10)),
 
        REPLICA_METAL_G_OUTFIT(new Cosmetic("Replica Metal (g) Full Helm", 31225, 0), new Cosmetic("Replica Metal (g) Platebody", 31226, 4), new Cosmetic("Replica Metal (g) Kiteshield Off-hand", 31229, 5), new Cosmetic("Replica Metal (g) Platelegs", 31227, 7), new Cosmetic("Replica Metal Gloves", 31216, 9), new Cosmetic("Replica Metal Boots", 31217, 10)),
 
        REPLICA_GWD_ARMADYL_OUTFIT(new Cosmetic("Replica GWD (Armadyl) Helmet", 31232, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Replica GWD (Armadyl) Chestplate", 31233, 4), new Cosmetic("Replica GWD (Armadyl) Chainskirt", 31234, 7)),
 
        REPLICA_GWD_BANDOS_OUTFIT(new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Replica GWD (Bandos) Boots", 31239, 10), new Cosmetic("Replica GWD (Bandos) Chestplate", 31237, 4), new Cosmetic("Replica GWD (Bandos) Tassets", 31238, 7)),
 
        HIKER_OUTFIT(new Cosmetic("Hiker Cap", 31296, 0), new Cosmetic("Hiker Backpack", 31300, 1), new Cosmetic("Hiker Jacket", 31297, 4), new Cosmetic("Hiker Leggings", 31298, 7), new Cosmetic("Hiker Gloves", 31301, 9), new Cosmetic("Hiker Boots", 31299, 10)),
 
        AVIANSIE_SKYGUARD_OUTFIT(new Cosmetic("Aviansie Skyguard Head", 31536, 0), new Cosmetic("Aviansie Skyguard Wings", 31543, 18), new Cosmetic("Aviansie Skyguard Longbow", 31541, 3), new Cosmetic("Aviansie Skyguard Mail", 31537, 4), new Cosmetic("Aviansie Skyguard Tassets", 31538, 7), new Cosmetic("Aviansie Skyguard Gauntlets", 31540, 9), new Cosmetic("Aviansie Skyguard Talons", 31539, 10)),
 
        VYREWATCH_SKYSHADOW_OUTFIT(new Cosmetic("Vyrewatch Skyshadow Hood", 31546, 0), new Cosmetic("Vyrewatch Skyshadow Wings", 31551, 18), new Cosmetic("Vyrewatch Skyshadow Staff", 31552, 3), new Cosmetic("Vyrewatch Skyshadow Vest", 31547, 4), new Cosmetic("Vyrewatch Skyshadow Bottoms", 31548, 7), new Cosmetic("Vyrewatch Skyshadow Wrist Wraps", 31549, 9), new Cosmetic("Vyrewatch Skyshadow Boots", 31550, 10)),
 
        REPLICA_VOID_KNIGHT_OUTFIT(new Cosmetic("Replica Void Knight Top", 31698, 4), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Replica Void Knight Bottom", 31699, 7)),
 
        VANQUISHER_OUTFIT(new Cosmetic("Vanquisher Skull", 31833, 0), new Cosmetic("Vanquisher Cuirass", 31834, 4), new Cosmetic("Vanquisher Cuisses", 31835, 7), new Cosmetic("Vanquisher Gauntlets", 31837, 9), new Cosmetic("Vanquisher Greaves", 31836, 10)),
 
        ZAROSIAN_SHADOW_OUTFIT(new Cosmetic("Zarosian Shadow Hood", 31975, 0), new Cosmetic("Zarosian Shadow Robe Top", 31976, 4), new Cosmetic("Zarosian Shadow Robe Bottom", 31977, 7), new Cosmetic("Zarosian Shadow Gauntlets", 31978, 9), new Cosmetic("Zarosian Shadow Boots", 31979, 10)),
 
        ZAROSIAN_PRAETOR_OUTFIT(new Cosmetic("Zarosian Praetor Mask", 31982, 0), new Cosmetic("Zarosian Praetor Splint Mail", 31983, 4), new Cosmetic("Zarosian Praetor Robe", 31984, 7), new Cosmetic("Zarosian Praetor Gauntlets", 31985, 9), new Cosmetic("Zarosian Praetor Boots", 31986, 10)),
 
        ELVEN_WARRIOR_OUTFIT(new Cosmetic("Elven Warrior Helmet", 32315, 0), new Cosmetic("Elven Warrior Armour", 32316, 4), new Cosmetic("Elven Warrior Tassets", 32317, 7), new Cosmetic("Elven Warrior Gloves", 32319, 9), new Cosmetic("Elven Warrior Boots", 32318, 10)),
 
        ELVEN_RANGER_OUTFIT(new Cosmetic("Elven Ranger Helmet", 32322, 0), new Cosmetic("Elven Ranger Armour", 32323, 4), new Cosmetic("Elven Ranger Tassets", 32324, 7), new Cosmetic("Elven Ranger Gloves", 32326, 9), new Cosmetic("Elven Ranger Boots", 32325, 10)),
 
        ELVEN_MAGE_OUTFIT(new Cosmetic("Elven Mage Helmet", 32329, 0), new Cosmetic("Elven Mage Robe Top", 32330, 4), new Cosmetic("Elven Mage Robe Bottom", 32331, 7), new Cosmetic("Elven Mage Gloves", 32333, 9), new Cosmetic("Elven Mage Boots", 32332, 10)),
 
        NEX_OUTFIT(new Cosmetic("Nex Head", 32543, 0), new Cosmetic("Nex Tail", 32549, 1), new Cosmetic("Nex Wings", 32548, 18), new Cosmetic("Nex Armour", 32544, 4), new Cosmetic("Nex Legs", 32545, 7), new Cosmetic("Nex Gloves", 32547, 9), new Cosmetic("Nex Boots", 32546, 10)),
 
        NEX_ATTUNED_OUTFIT(new Cosmetic("Nex (Attuned) Head", 32551, 0), new Cosmetic("Nex (Attuned) Tail", 32557, 1), new Cosmetic("Nex (Attuned) Wings", 32556, 18), new Cosmetic("Nex (Attuned) Armour", 32552, 4), new Cosmetic("Nex (Attuned) Legs", 32553, 7), new Cosmetic("Nex (Attuned) Gloves", 32555, 9), new Cosmetic("Nex (Attuned) Boots", 32554, 10)),
 
        KING_BLACK_DRAGON_OUTFIT(new Cosmetic("King Black Dragon Head", 32560, 0), new Cosmetic("King Black Dragon Tail", 32566, 1), new Cosmetic("King Black Dragon Wings", 32565, 18), new Cosmetic("King Black Dragon Armour", 32561, 4), new Cosmetic("King Black Dragon Legs", 32562, 7), new Cosmetic("King Black Dragon Gloves", 32564, 9), new Cosmetic("King Black Dragon Boots", 32563, 10)),
 
        KING_BLACK_DRAGON_ATTUNED_OUTFIT(new Cosmetic("King Black Dragon (Attuned) Head", 32568, 0), new Cosmetic("King Black Dragon (Attuned) Tail", 32574, 1), new Cosmetic("King Black Dragon (Attuned) Wings", 32573, 18), new Cosmetic("King Black Dragon (Attuned) Armour", 32569, 4), new Cosmetic("King Black Dragon (Attuned) Legs", 32570, 7), new Cosmetic("King Black Dragon (Attuned) Gauntlets", 32572, 9), new Cosmetic("King Black Dragon (Attuned) Boots", 32571, 10)),
 
        SNOWVERLOAD_OUTFIT(new Cosmetic("Snowverload Head", 33593, 0), new Cosmetic("Snowverload Ice Sickle", 33597, 3), new Cosmetic("Snowverload Body", 33594, 4), new Cosmetic("Snowverload Legs", 33595, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("null", 33633, 10), new Cosmetic("Snowverload Snowstormer", 33596, 14)),
 
        SAMURAI_OUTFIT(new Cosmetic("Samurai Helmet", 33637, 0), new Cosmetic("Samurai Flag", 33642, 1), new Cosmetic("Samurai Armour", 33638, 4), new Cosmetic("Samurai Tassets", 33639, 7), new Cosmetic("Samurai Gauntlets", 33641, 9), new Cosmetic("Samurai Boots", 33640, 10)),
 
        OMENS_OUTFIT(new Cosmetic("Omens Helm", 33711, 0), new Cosmetic("Maul of Omens", 33709, 3), new Cosmetic("Omens Torso", 33712, 4), new Cosmetic("Omens Legs", 33713, 7), new Cosmetic("Omens Gloves", 33715, 9), new Cosmetic("Omens Boots", 33714, 10)),
 
        REPLICA_BARROWS_AHRIM_OUTFIT(new Cosmetic("Replica Barrows (Ahrim) Hood", 33674, 0), new Cosmetic("Replica Barrows (Ahrim) Staff", 33677, 3), new Cosmetic("Replica Barrows (Ahrim) Robe Top", 33675, 4), new Cosmetic("Replica Barrows (Ahrim) Robe Skirt", 33676, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        REPLICA_BARROWS_DHAROK_OUTFIT(new Cosmetic("Replica Barrows (Dharok) Helm", 33680, 0), new Cosmetic("Replica Barrows (Dharok) Greataxe", 33683, 3), new Cosmetic("Replica Barrows (Dharok) Platebody", 33681, 4), new Cosmetic("Replica Barrows (Dharok) Platelegs", 33682, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        REPLICA_BARROWS_GUTHAN_OUTFIT(new Cosmetic("Replica Barrows (Guthan) Helm", 33686, 0), new Cosmetic("Replica Barrows (Guthan) Warspear", 33689, 3), new Cosmetic("Replica Barrows (Guthan) Platebody", 33687, 4), new Cosmetic("Replica Barrows (Guthan) Chainskirt", 33688, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        REPLICA_BARROWS_KARIL_OUTFIT(new Cosmetic("Replica Barrows (Karil) Coif", 33692, 0), new Cosmetic("Replica Barrows (Karil) Crossbow", 33695, 3), new Cosmetic("Replica Barrows (Karil) Top", 33693, 4), new Cosmetic("Replica Barrows (Karil) Skirt", 33694, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        REPLICA_BARROWS_TORAG_OUTFIT(new Cosmetic("Replica Barrows (Torag) Helm", 33698, 0), new Cosmetic("Replica Barrows (Torag) Hammer", 33701, 3), new Cosmetic("Replica Barrows (Torag) Platebody", 33699, 4), new Cosmetic("Replica Barrows (Torag) Off-hand Hammer", 33702, 5), new Cosmetic("Replica Barrows (Torag) Platelegs", 33700, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        REPLICA_BARROWS_VERAC_OUTFIT(new Cosmetic("Replica Barrows (Verac) Helm", 33705, 0), new Cosmetic("Replica Barrows (Verac) Flail", 33708, 3), new Cosmetic("Replica Barrows (Verac) Brassard", 33706, 4), new Cosmetic("Replica Barrows (Verac) Plateskirt", 33707, 7), new Cosmetic("Bare hands", 39760, 9)),
 
        WARM_WINTER_OUTFIT(new Cosmetic("Warm Winter Hood", 33755, 0), new Cosmetic("Warm Winter Coat", 33756, 4), new Cosmetic("Warm Winter Coat Bottom", 33757, 7), new Cosmetic("Warm Winter Gloves", 33759, 9), new Cosmetic("Warm Winter Boots", 33758, 10)),
 
        NOMAD_OUTFIT(new Cosmetic("Nomad Gorget", 34099, 0), new Cosmetic("Nomad Cape", 34104, 1), new Cosmetic("Nomad Spear", 34105, 3), new Cosmetic("Nomad Chestplate", 34100, 4), new Cosmetic("Nomad Tassets", 34101, 7), new Cosmetic("Nomad Gloves", 34103, 9), new Cosmetic("Nomad Boots", 34102, 10)),
 
        GAMEBLAST_2015_OUTFIT(new Cosmetic("GameBlast 2015 Bandana", 34114, 0), new Cosmetic("GameBlast 2015 Cape", 34119, 1), new Cosmetic("GameBlast 2015 Amulet", 34120, 2), new Cosmetic("GameBlast 2015 Torso", 34115, 4), new Cosmetic("GameBlast 2015 Legs", 34116, 7), new Cosmetic("GameBlast 2015 Gloves", 34118, 9), new Cosmetic("GameBlast 2015 Boots", 34117, 10)),
 
        DARK_LORD_OUTFIT(new Cosmetic("Dark Lord Head", 34222, 0), new Cosmetic("Dark Lord Body", 34223, 4), new Cosmetic("Dark Lord Legs", 34224, 7), new Cosmetic("Dark Lord Hands", 34226, 9), new Cosmetic("Dark Lord Feet", 34225, 10)),
 
        NEW_VARROCK_CULTIST_OUTFIT(new Cosmetic("New Varrock Cultist Hood", 34319, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("New Varrock Cultist Top", 34320, 4), new Cosmetic("New Varrock Cultist Robes", 34321, 7)),
 
        NEW_VARROCK_ZOMBIE_OUTFIT(new Cosmetic("New Varrock Zombie Head", 34323, 0), new Cosmetic("New Varrock Zombie Torso", 34324, 4), new Cosmetic("New Varrock Zombie Legs", 34325, 7), new Cosmetic("New Varrock Zombie Hands", 34326, 9), new Cosmetic("New Varrock Zombie Feet", 34327, 10)),
 
        ARRAV_NEW_VARROCK_OUTFIT(new Cosmetic("Arrav (New Varrock) Helmet", 34329, 0), new Cosmetic("Arrav (New Varrock) Gorget", 34330, 4), new Cosmetic("Arrav (New Varrock) Platelegs", 34331, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Arrav (New Varrock) Greaves", 34332, 10)),
 
        ARRAV_OUTFIT(new Cosmetic("Arrav Helmet", 34304, 0), new Cosmetic("Arrav Gorget", 34305, 4), new Cosmetic("Arrav Platelegs", 34306, 7), new Cosmetic("Arrav Wrist Guards", 34308, 9), new Cosmetic("Arrav Greaves", 34307, 10)),
 
        ARRAV_CURSED_OUTFIT(new Cosmetic("Arrav (Cursed) Helmet", 34310, 0), new Cosmetic("Arrav (Cursed) Sword", 34315, 3), new Cosmetic("Arrav (Cursed) Gorget", 34311, 4), new Cosmetic("Arrav (Cursed) Off-hand Sword", 34316, 5), new Cosmetic("Arrav (Cursed) Platelegs", 34312, 7), new Cosmetic("Arrav (Cursed) Wrist Guards", 34314, 9), new Cosmetic("Arrav (Cursed) Greaves", 34313, 10)),
 
        VITALITY_SUIT_OUTFIT(new Cosmetic("Vitality Suit Helmet", 34535, 0), new Cosmetic("Vitality Suit Chest", 34536, 4), new Cosmetic("Vitality Suit Legs", 34537, 7), new Cosmetic("Vitality Suit Gloves", 34539, 9), new Cosmetic("Vitality Suit Feet", 34538, 10)),
 
        VITALITY_SUIT_INACTIVE_OUTFIT(new Cosmetic("Vitality Suit (Inactive) Helmet", 34948, 0), new Cosmetic("Vitality Suit (Inactive) Chest", 34949, 4), new Cosmetic("Vitality Suit (Inactive) Legs", 34950, 7), new Cosmetic("Vitality Suit (Inactive) Gloves", 34952, 9), new Cosmetic("Vitality Suit (Inactive) Feet", 34951, 10)),
 
        DIVING_SUIT_OUTFIT(new Cosmetic("Diving Suit Oyster Hunter Helmet", 34678, 0), new Cosmetic("Diving Suit Torso", 34681, 4), new Cosmetic("Diving Suit Legs", 34682, 7), new Cosmetic("Diving Suit Gloves", 34684, 9), new Cosmetic("Diving Suit Boots", 34683, 10)),
 
        LAVA_OUTFIT(new Cosmetic("Lava Hands", 34813, 9), new Cosmetic("Lava Feet", 34814, 10), new Cosmetic("Lava Top", 34811, 4), new Cosmetic("Lava Legs", 34812, 7)),
 
        COGWHEEL_OUTFIT(new Cosmetic("Cogwheel Helmet", 34790, 0), new Cosmetic("Cogwheel Chest", 34791, 4), new Cosmetic("Cogwheel Legs", 34792, 7), new Cosmetic("Cogwheel Gloves", 34794, 9), new Cosmetic("Cogwheel Boots", 34793, 10)),
 
        KETHSI_OUTFIT(new Cosmetic("Kethsi Helmet", 34999, 0), new Cosmetic("Kethsi Chestplate", 35000, 4), new Cosmetic("Kethsi Robes", 35001, 7), new Cosmetic("Kethsi Bracers", 35002, 9), new Cosmetic("Kethsi Boots", 35003, 10)),
 
        BEACH_SWIMMING_OUTFIT(new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Bare feet", 39759, 10), new Cosmetic("Beach Swimming Top", 35100, 4), new Cosmetic("Beach Swimming Bottoms", 35101, 7)),
 
        V_OUTFIT(new Cosmetic("V Helmet", 35231, 0), new Cosmetic("V Cape", 35236, 1), new Cosmetic("V Broadsword", 35237, 3), new Cosmetic("V Chestpiece", 35232, 4), new Cosmetic("V Legguards", 35233, 7), new Cosmetic("V Gauntlets", 35235, 9), new Cosmetic("V Sabatons", 35234, 10)),
 
        BARBARIAN_OUTFIT(new Cosmetic("Barbarian Helmet", 35369, 0), new Cosmetic("Barbarian Maul", 35374, 3), new Cosmetic("Barbarian Cuirass", 35370, 4), new Cosmetic("Barbarian Greaves", 35371, 7), new Cosmetic("Barbarian Cuffs", 35373, 9), new Cosmetic("Barbarian Boots", 35372, 10)),
 
        DESERT_TREASURE_FAREED_OUTFIT(new Cosmetic("Desert Treasure (Fareed) Helm", 35673, 0), new Cosmetic("Desert Treasure (Fareed) Top", 35674, 4), new Cosmetic("Desert Treasure (Fareed) Bottoms", 35675, 7), new Cosmetic("Desert Treasure (Fareed) Gauntlets", 35676, 9), new Cosmetic("Desert Treasure (Fareed) Boots", 35677, 10)),
 
        DESERT_TREASURE_KAMIL_OUTFIT(new Cosmetic("Desert Treasure (Kamil) Helm", 35679, 0), new Cosmetic("Desert Treasure (Kamil) Top", 35680, 4), new Cosmetic("Desert Treasure (Kamil) Bottoms", 35681, 7), new Cosmetic("Desert Treasure (Kamil) Gauntlets", 35682, 9), new Cosmetic("Desert Treasure (Kamil) Boots", 35683, 10)),
 
        SPIRIT_HUNTER_OUTFIT(new Cosmetic("Spirit Hunter Skull Helmet", 35842, 0), new Cosmetic("Spirit Hunter Bow", 35847, 3), new Cosmetic("Spirit Hunter Flesh Jacket", 35843, 4), new Cosmetic("Spirit Hunter Bottoms", 35844, 7), new Cosmetic("Spirit Hunter Cuffs", 35846, 9), new Cosmetic("Spirit Hunter Greaves", 35845, 10)),
 
        REVENANT_OUTFIT(new Cosmetic("Revenant Helmet", 35853, 0), new Cosmetic("Revenant Staff", 35858, 3), new Cosmetic("Revenant Cuirass", 35854, 4), new Cosmetic("Revenant Greaves", 35855, 7), new Cosmetic("Revenant Cuffs", 35857, 9), new Cosmetic("Revenant Boots", 35856, 10)),
 
        COUNT_DRAYNOR_OUTFIT(new Cosmetic("Count Draynor Cape", 35949, 1), new Cosmetic("Count Draynor Top", 35947, 4), new Cosmetic("Count Draynor Bottoms", 35948, 7), new Cosmetic("Count Draynor Hands", 35950, 9), new Cosmetic("Count Draynor Shoes", 35951, 10)),
 
        RAPTOR_OUTFIT(new Cosmetic("Raptor Helmet", 36026, 0), new Cosmetic("Raptor Chestpiece", 36027, 4), new Cosmetic("Raptor Shield Off-hand", 36031, 5), new Cosmetic("Raptor Legguards", 36028, 7), new Cosmetic("Raptor Gauntlets", 36030, 9), new Cosmetic("Raptor Sabatons", 36029, 10)),
 
        RAPTOR_ADVANCED_OUTFIT(new Cosmetic("Raptor (Advanced) Helmet", 36035, 0), new Cosmetic("Raptor (Advanced) Chestpiece", 36036, 4), new Cosmetic("Raptor (Advanced) Shield Off-hand", 36040, 5), new Cosmetic("Raptor (Advanced) Legguards", 36037, 7), new Cosmetic("Raptor (Advanced) Gauntlets", 36039, 9), new Cosmetic("Raptor (Advanced) Sabatons", 36038, 10)),
 
        MAHJARRAT_OUTFIT(new Cosmetic("Mahjarrat Head", 36148, 0), new Cosmetic("Mahjarrat Torso", 36140, 4), new Cosmetic("Mahjarrat Legs", 36144, 7), new Cosmetic("Mahjarrat Hands", 36142, 9), new Cosmetic("Mahjarrat Feet", 36146, 10)),
 
        MAHJARRAT_SKELETAL_OUTFIT(new Cosmetic("Mahjarrat (Skeletal) Head", 36150, 0), new Cosmetic("Mahjarrat Torso", 36140, 4), new Cosmetic("Mahjarrat Legs", 36144, 7), new Cosmetic("Mahjarrat Hands", 36142, 9), new Cosmetic("Mahjarrat Feet", 36146, 10)),
 
        SHADOW_DRAGOON_OUTFIT(new Cosmetic("Shadow Dragoon Helm", 36344, 0), new Cosmetic("Shadow Dragoon Spear 'Fury's Remorse'", 36349, 3), new Cosmetic("Shadow Dragoon Chestplate", 36345, 4), new Cosmetic("Shadow Dragoon Legplates", 36346, 7), new Cosmetic("Shadow Dragoon Gauntlets", 36348, 9), new Cosmetic("Shadow Dragoon Boots", 36347, 10)),
 
        GROTESQUE_TIER_1_OUTFIT(new Cosmetic("Grotesque (Tier 1) Helm", 36739, 0), new Cosmetic("Grotesque (Tier 1) Cape", 36744, 1), new Cosmetic("Grotesque (Tier 1) Wings", 36745, 18), new Cosmetic("Grotesque (Tier 1) Chest", 36740, 4), new Cosmetic("Grotesque (Tier 1) Legs", 36741, 7), new Cosmetic("Grotesque (Tier 1) Gloves", 36742, 9), new Cosmetic("Grotesque (Tier 1) Boots", 36743, 10)),
 
        GROTESQUE_TIER_2_OUTFIT(new Cosmetic("Grotesque (Tier 2) Helm", 36746, 0), new Cosmetic("Grotesque (Tier 2) Cape", 36751, 1), new Cosmetic("Grotesque (Tier 2) Wings", 36752, 18), new Cosmetic("Grotesque (Tier 2) Chest", 36747, 4), new Cosmetic("Grotesque (Tier 2) Legs", 36748, 7), new Cosmetic("Grotesque (Tier 2) Gloves", 36749, 9), new Cosmetic("Grotesque (Tier 2) Boots", 36750, 10)),
 
        GROTESQUE_TIER_3_OUTFIT(new Cosmetic("Grotesque (Tier 3) Helm", 36753, 0), new Cosmetic("Grotesque (Tier 3) Cape", 36758, 1), new Cosmetic("Grotesque (Tier 3) Wings", 36759, 18), new Cosmetic("Grotesque (Tier 3) Chest", 36754, 4), new Cosmetic("Grotesque (Tier 3) Legs", 36755, 7), new Cosmetic("Grotesque (Tier 3) Gloves", 36756, 9), new Cosmetic("Grotesque (Tier 3) Boots", 36757, 10)),
 
        MASQUERADE_OUTFIT(new Cosmetic("Masquerade Head", 36782, 0), new Cosmetic("Masquerade Top", 36786, 4), new Cosmetic("Masquerade Bottoms", 36783, 7), new Cosmetic("Masquerade Gloves", 36784, 9), new Cosmetic("Masquerade Boots", 36785, 10)),
 
        ELITE_MAMMOTH_OUTFIT(new Cosmetic("Elite Mammoth Helmet", 36883, 0), new Cosmetic("Elite Mammoth 2h Tusk Sword", 36888, 3), new Cosmetic("Elite Mammoth Torso", 36884, 4), new Cosmetic("Elite Mammoth Legs", 36885, 7), new Cosmetic("Elite Mammoth Gauntlets", 36887, 9), new Cosmetic("Elite Mammoth Boots", 36886, 10)),
 
        ANCIENT_OUTFIT(new Cosmetic("Ancient Tiara", 36903, 0), new Cosmetic("Ancient Robe Top", 36905, 4), new Cosmetic("Ancient Robe Bottom", 36906, 7), new Cosmetic("Ancient Cuffs", 36907, 9), new Cosmetic("Ancient Shoes", 36908, 10)),
 
        ROGUE_OUTFIT(new Cosmetic("Rogue Hood", 36947, 0), new Cosmetic("Rogue Top", 36949, 4), new Cosmetic("Rogue Bottom", 36950, 7), new Cosmetic("Rogue Wrist Guards", 36951, 9), new Cosmetic("Rogue Sandals", 36952, 10)),
 
        NAVIGATOR_OUTFIT(new Cosmetic("Navigator Goggles", 37137, 0), new Cosmetic("Navigator Tunic Top", 37140, 4), new Cosmetic("Navigator Tunic Bottom", 37141, 7), new Cosmetic("Navigator Cuffs", 37142, 9), new Cosmetic("Navigator Knee High Boots", 37143, 10)),
 
        ROYAL_EASTERN_OUTFIT(new Cosmetic("Royal Eastern Fascinator", 37215, 0), new Cosmetic("Royal Eastern Wrap Top", 37217, 4), new Cosmetic("Royal Eastern Wrap Bottom", 37218, 7), new Cosmetic("Royal Eastern Gloves", 37219, 9), new Cosmetic("Royal Eastern Geta", 37220, 10)),
 
        VAMPYRE_HUNTER_OUTFIT(new Cosmetic("Vampyre Hunter Hat", 37204, 0), new Cosmetic("Vampyre Hunter Amulet", 37209, 2), new Cosmetic("Vampyre Hunter Torso", 37205, 4), new Cosmetic("Vampyre Hunter Legs", 37206, 7), new Cosmetic("Vampyre Hunter Cuffs", 37208, 9), new Cosmetic("Vampyre Hunter Boots", 37207, 10)),
 
        INVESTIGATOR_UNIFORM_OUTFIT(new Cosmetic("Investigator Uniform Fedora", 37325, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Investigator Uniform Coat", 37326, 4), new Cosmetic("Investigator Uniform Trousers", 37327, 7)),
 
        LION_OUTFIT(new Cosmetic("Lion Mane", 37378, 0), new Cosmetic("Lion Tail", 37383, 1), new Cosmetic("Lion Belly", 37379, 4), new Cosmetic("Lion Legs", 37380, 7), new Cosmetic("Lion Claws", 37381, 9), new Cosmetic("Lion Paws", 37382, 10)),
 
        GRIFFIN_OUTFIT(new Cosmetic("Griffin Crown", 37388, 0), new Cosmetic("Griffin Claws", 37391, 9), new Cosmetic("Griffin Talons", 37392, 10), new Cosmetic("Griffin Wings", 37393, 18), new Cosmetic("Griffin Mantle", 37389, 4), new Cosmetic("Griffin Limbs", 37390, 7)),
 
        SCORPION_OUTFIT(new Cosmetic("Scorpion Prosoma", 37511, 0), new Cosmetic("Scorpion Stinger", 37516, 1), new Cosmetic("Scorpion Carapace", 37512, 4), new Cosmetic("Scorpion Tibia", 37513, 7), new Cosmetic("Scorpion Palps", 37514, 9), new Cosmetic("Scorpion Tarsus", 37515, 10)),
 
        CABBAGEMANCER_OUTFIT(new Cosmetic("Cabbagemancer Staff", 37604, 3), new Cosmetic("Cabbagemancer Robe Top", 37605, 4), new Cosmetic("Cabbagemancer Robe Bottom", 37606, 7), new Cosmetic("Cabbagemancer Gloves", 37608, 9), new Cosmetic("Cabbagemancer Boots", 37607, 10)),
 
        DEATH_LOTUS_ROGUE_OUTFIT(new Cosmetic("Death Lotus Rogue Hood", 39005, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Death Lotus Rogue Chestplate", 39006, 4), new Cosmetic("Death Lotus Rogue Chaps", 39007, 7)),
 
        GU_RONIN_OUTFIT(new Cosmetic("Gu Ronin Helm", 37859, 0), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Gu Ronin Body", 37860, 4), new Cosmetic("Gu Ronin Platelegs", 37861, 7)),
 
        SEASINGER_ACOLYTE_OUTFIT(new Cosmetic("Seasinger Acolyte Hood", 38867, 0), new Cosmetic("Seasinger Acolyte Robe Top", 38868, 4), new Cosmetic("Seasinger Acolyte Robe Bottoms", 38869, 7), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Bare feet", 39759, 10)),
 
        FLOURISHING_FAIRY_OUTFIT(new Cosmetic("Flourishing Fairy Petal Coronet", 37853, 0), new Cosmetic("Flourishing Fairy Petal Gloves", 37857, 9), new Cosmetic("Flourishing Fairy Petal Boots", 37856, 10), new Cosmetic("Flourishing Fairy Petal Wings", 37858, 18), new Cosmetic("Flourishing Fairy Petal Top", 37854, 4), new Cosmetic("Flourishing Fairy Petal Bottoms", 37855, 7)),
 
        SATYR_OUTFIT(new Cosmetic("Satyr Horned Headwear", 38068, 0), new Cosmetic("Satyr Armguards", 38072, 9), new Cosmetic("Satyr Hooves", 38071, 10), new Cosmetic("Satyr Tendril Wings", 38073, 18), new Cosmetic("Satyr Leafy Top", 38069, 4), new Cosmetic("Satyr Hock Legs", 38070, 7)),
 
        BEACH_SUMMER_FUN_OUTFIT(new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Bare feet", 39759, 10), new Cosmetic("Beach Summer Fun Top", 37993, 4), new Cosmetic("Beach Summer Fun Bottoms", 37994, 7)),
 
        CRYSTAL_PEACOCK_OUTFIT(new Cosmetic("Crystal Peacock Helmet", 38155, 0), new Cosmetic("Crystal Peacock Body", 38156, 4), new Cosmetic("Crystal Peacock Legs", 38157, 7), new Cosmetic("Crystal Peacock Gloves", 38158, 9), new Cosmetic("Crystal Peacock Boots", 38159, 10)),
 
        SUNFURY_TIER_1_OUTFIT(new Cosmetic("Sunfury (Tier 1) Helm", 38139, 0), new Cosmetic("Sunfury (Tier 1) Cape", 38149, 1), new Cosmetic("Sunfury (Tier 1) Chest", 38141, 4), new Cosmetic("Sunfury (Tier 1) Shield Off-hand", 38151, 5), new Cosmetic("Sunfury (Tier 1) Legs", 38143, 7), new Cosmetic("Sunfury (Tier 1) Gauntlets", 38145, 9), new Cosmetic("Sunfury (Tier 1) Boots", 38147, 10)),
 
        SUNFURY_TIER_2_OUTFIT(new Cosmetic("Sunfury (Tier 2) Helm", 38140, 0), new Cosmetic("Sunfury (Tier 2) Cape", 38150, 1), new Cosmetic("Sunfury (Tier 2) Chest", 38142, 4), new Cosmetic("Sunfury (Tier 2) Shield Off-hand", 38152, 5), new Cosmetic("Sunfury (Tier 2) Legs", 38144, 7), new Cosmetic("Sunfury (Tier 2) Gauntlets", 38146, 9), new Cosmetic("Sunfury (Tier 2) Boots", 38148, 10)),
 
        GOSSAMER_OUTFIT(new Cosmetic("Gossamer Feathered Headdress", 38193, 0), new Cosmetic("Gossamer Silk Gloves", 38197, 9), new Cosmetic("Gossamer Pumps", 38196, 10), new Cosmetic("Gossamer Wings", 38198, 18), new Cosmetic("Gossamer Robes Top", 38194, 4), new Cosmetic("Gossamer Robes Bottom", 38195, 7)),
 
        GORAJAN_TRAILBLAZER_FROZEN_OUTFIT(new Cosmetic("Gorajan Trailblazer (Frozen) Head", 38546, 0), new Cosmetic("Gorajan Trailblazer (Frozen) Body", 38547, 4), new Cosmetic("Gorajan Trailblazer (Frozen) Legs", 38548, 7), new Cosmetic("Gorajan Trailblazer (Frozen) Hands", 38549, 9), new Cosmetic("Gorajan Trailblazer (Frozen) Feet", 38550, 10)),
 
        GORAJAN_TRAILBLAZER_FURNISHED_OUTFIT(new Cosmetic("Gorajan Trailblazer (Furnished) Head", 38551, 0), new Cosmetic("Gorajan Trailblazer (Furnished) Body", 38552, 4), new Cosmetic("Gorajan Trailblazer (Furnished) Legs", 38553, 7), new Cosmetic("Gorajan Trailblazer (Furnished) Hands", 38554, 9), new Cosmetic("Gorajan Trailblazer (Furnished) Feet", 38555, 10)),
 
        GORAJAN_TRAILBLAZER_ABANDONED_OUTFIT(new Cosmetic("Gorajan Trailblazer (Abandoned) Head", 38556, 0), new Cosmetic("Gorajan Trailblazer (Abandoned) Body", 38557, 4), new Cosmetic("Gorajan Trailblazer (Abandoned) Legs", 38558, 7), new Cosmetic("Gorajan Trailblazer (Abandoned) Hands", 38559, 9), new Cosmetic("Gorajan Trailblazer (Abandoned) Feet", 38560, 10)),
 
        GORAJAN_TRAILBLAZER_OCCULT_OUTFIT(new Cosmetic("Gorajan Trailblazer (Occult) Head", 38561, 0), new Cosmetic("Gorajan Trailblazer (Occult) Body", 38562, 4), new Cosmetic("Gorajan Trailblazer (Occult) Legs", 38563, 7), new Cosmetic("Gorajan Trailblazer (Occult) Hands", 38564, 9), new Cosmetic("Gorajan Trailblazer (Occult) Feet", 38565, 10)),
 
        GORAJAN_TRAILBLAZER_WARPED_OUTFIT(new Cosmetic("Gorajan Trailblazer (Warped) Head", 38566, 0), new Cosmetic("Gorajan Trailblazer (Warped) Body", 38567, 4), new Cosmetic("Gorajan Trailblazer (Warped) Legs", 38568, 7), new Cosmetic("Gorajan Trailblazer (Warped) Hands", 38569, 9), new Cosmetic("Gorajan Trailblazer (Warped) Feet", 38570, 10)),
 
        PRIVATEER_OUTFIT(new Cosmetic("Privateer Tricorn", 38857, 0), new Cosmetic("Privateer Cutlass", 38862, 3), new Cosmetic("Privateer Tunic", 38858, 4), new Cosmetic("Privateer Cargo Pants", 38859, 7), new Cosmetic("Privateer Gloves", 38861, 9), new Cosmetic("Privateer Cuffed Boots", 38860, 10)),
 
        IRONMAN_OUTFIT(new Cosmetic("Ironman Helm", 38943, 0), new Cosmetic("Ironman Body", 38945, 4), new Cosmetic("Ironman Legs", 38947, 7), new Cosmetic("Ironman Gauntlets", 38948, 9), new Cosmetic("Ironman Boots", 38949, 10)),
 
        IRONMAN_HARDCORE_OUTFIT(new Cosmetic("Ironman (Hardcore) Helm", 38944, 0), new Cosmetic("Ironman (Hardcore) Body", 38946, 4), new Cosmetic("Ironman Legs", 38947, 7), new Cosmetic("Ironman Gauntlets", 38948, 9), new Cosmetic("Ironman Boots", 38949, 10)),
 
        SPOOKY_SPIDER_OUTFIT(new Cosmetic("Spooky Spider Head", 38893, 0), new Cosmetic("Spooky Spider Gloves", 38896, 9), new Cosmetic("Spooky Spider Boots", 38897, 10), new Cosmetic("Spooky Spider Wings", 38898, 18), new Cosmetic("Spooky Spider Top", 38894, 4), new Cosmetic("Spooky Spider Bottoms", 38895, 7)),
 
        FALLEN_NIHIL_OUTFIT(new Cosmetic("Fallen Nihil Headpiece", 39041, 0), new Cosmetic("Fallen Nihil Gauntlets", 39045, 9), new Cosmetic("Fallen Nihil Greaves", 39044, 10), new Cosmetic("Fallen Nihil Wings", 39046, 18), new Cosmetic("Fallen Nihil Chestpiece", 39042, 4), new Cosmetic("Fallen Nihil Leg-guards", 39043, 7)),
 
        CAPTAIN_DEATHBEARD_OUTFIT(new Cosmetic("Captain Deathbeard Hat", 39519, 0), new Cosmetic("Captain Deathbeard Doublet", 39520, 4), new Cosmetic("Captain Deathbeard Breeches", 39521, 7), new Cosmetic("Captain Deathbeard Hook", 39523, 9), new Cosmetic("Captain Deathbeard Feet", 39522, 10)),
 
        CAPTAIN_DEATHBEARD_PHANTOM_OUTFIT(new Cosmetic("Captain Deathbeard (Phantom) Hat", 39525, 0), new Cosmetic("Captain Deathbeard (Phantom) Doublet", 39526, 4), new Cosmetic("Captain Deathbeard (Phantom) Breeches", 39527, 7), new Cosmetic("Captain Deathbeard (Phantom) Hook", 39529, 9), new Cosmetic("Captain Deathbeard (Phantom) Feet", 39528, 10)),
 
        NAUTILUS_OUTFIT(new Cosmetic("Nautilus Crown", 39169, 0), new Cosmetic("Nautilus Necklace", 41302, 2), new Cosmetic("Nautilus Trident", 39176, 3), new Cosmetic("Nautilus Torso", 39170, 4), new Cosmetic("Nautilus Gown", 39171, 7), new Cosmetic("Nautilus Gloves", 39172, 9), new Cosmetic("Nautilus Boots", 39173, 10)),
 
        LEGATUS_MAXIMUS_OUTFIT(new Cosmetic("Legatus Maximus Gauntlets", 39257, 9), new Cosmetic("Legatus Maximus Boots", 39258, 10), new Cosmetic("Legatus Maximus Platebody", 39255, 4), new Cosmetic("Legatus Maximus Platelegs", 39256, 7)),
 
        MENAPHITE_ANCIENT_OUTFIT(new Cosmetic("Menaphite Ancient Headpiece", 39291, 0), new Cosmetic("Menaphite Ancient Cloak", 39296, 1), new Cosmetic("Menaphite Ancient Chestplate", 39292, 4), new Cosmetic("Menaphite Ancient Legguards", 39293, 7), new Cosmetic("Menaphite Ancient Gauntlets", 39295, 9), new Cosmetic("Menaphite Ancient Boots", 39294, 10)),
 
        SHADOW_GORILLA_OUTFIT(new Cosmetic("Shadow Gorilla Head", 39564, 0), new Cosmetic("Shadow Gorilla Top", 39565, 4), new Cosmetic("Shadow Gorilla Bottoms", 39566, 7), new Cosmetic("Shadow Gorilla Gloves", 39568, 9), new Cosmetic("Shadow Gorilla Boots", 39567, 10)),
 
        BATTLE_ROBES_CRUOR_OUTFIT(new Cosmetic("Battle Robes (Cruor) Mask", 39609, 0), new Cosmetic("Battle Robes (Cruor) Banner", 47722, 1), new Cosmetic("Battle Robes (Cruor) Top", 39610, 4), new Cosmetic("Battle Robes (Cruor) Legs", 39611, 7), new Cosmetic("Battle Robes (Cruor) Gloves", 39613, 9), new Cosmetic("Battle Robes (Cruor) Footwear", 39612, 10)),
 
        BATTLE_ROBES_FUMUS_OUTFIT(new Cosmetic("Battle Robes (Fumus) Mask", 39615, 0), new Cosmetic("Battle Robes (Fumus) Banner", 47723, 1), new Cosmetic("Battle Robes (Fumus) Top", 39616, 4), new Cosmetic("Battle Robes (Fumus) Legs", 39617, 7), new Cosmetic("Battle Robes (Fumus) Gloves", 39619, 9), new Cosmetic("Battle Robes (Fumus) Footwear", 39618, 10)),
 
        BATTLE_ROBES_GLACIES_OUTFIT(new Cosmetic("Battle Robes (Glacies) Mask", 39603, 0), new Cosmetic("Battle Robes (Glacies) Banner", 47721, 1), new Cosmetic("Battle Robes (Glacies) Top", 39604, 4), new Cosmetic("Battle Robes (Glacies) Legs", 39605, 7), new Cosmetic("Battle Robes (Glacies) Gloves", 39607, 9), new Cosmetic("Battle Robes (Glacies) Footwear", 39606, 10)),
 
        BATTLE_ROBES_UMBRA_OUTFIT(new Cosmetic("Battle Robes (Umbra) Mask", 39597, 0), new Cosmetic("Battle Robes (Umbra) Banner", 47720, 1), new Cosmetic("Battle Robes (Umbra) Top", 39598, 4), new Cosmetic("Battle Robes (Umbra) Legs", 39599, 7), new Cosmetic("Battle Robes (Umbra) Gloves", 39601, 9), new Cosmetic("Battle Robes (Umbra) Footwear", 39600, 10)),
 
        DESERT_TREASURE_DAMIS_LIGHT_OUTFIT(new Cosmetic("Desert Treasure (Damis Light) Helm", 39774, 0), new Cosmetic("Desert Treasure (Damis Light) Top", 39775, 4), new Cosmetic("Desert Treasure (Damis Light) Bottoms", 39776, 7), new Cosmetic("Desert Treasure (Damis Light) Gauntlets", 39777, 9), new Cosmetic("Desert Treasure (Damis Light) Boots", 39778, 10)),
 
        DESERT_TREASURE_DAMIS_DARK_OUTFIT(new Cosmetic("Desert Treasure (Damis Dark) Helm", 39779, 0), new Cosmetic("Desert Treasure (Damis Dark) Top", 39780, 4), new Cosmetic("Desert Treasure (Damis Dark) Bottoms", 39781, 7), new Cosmetic("Desert Treasure (Damis Dark) Gauntlets", 39782, 9), new Cosmetic("Desert Treasure (Damis Dark) Boots", 39783, 10)),
 
        REPLICA_VOID_KNIGHT_ELITE_OUTFIT(new Cosmetic("Replica Void Knight (Elite) Top", 31702, 4), new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Replica Void Knight (Elite) Bottom", 31703, 7)),
 
        CHRISTMAS_WARSUIT_OUTFIT(new Cosmetic("null", 26450, 0), new Cosmetic("null", 26452, 4), new Cosmetic("null", 26454, 7), new Cosmetic("null", 26456, 9), new Cosmetic("null", 26458, 10)),
 
        TROPICAL_ISLANDER_OUTFIT(new Cosmetic("Tropical Islander Headdress", 24806, 0), new Cosmetic("Tropical Islander Lei Necklace", 24815, 2), new Cosmetic("Tropical Islander Top", 24807, 4), new Cosmetic("Tropical Islander Grass Skirt", 24809, 7), new Cosmetic("Tropical Islander Bracelets", 24813, 9), new Cosmetic("Tropical Islander Ankle Cuffs", 24811, 10)),
 
        GILLY_OUTFIT(new Cosmetic("Gilly Hat", 35549, 0), new Cosmetic("Gilly Top", 35547, 4), new Cosmetic("Gilly Trousers", 35548, 7), new Cosmetic("Gilly Gloves", 35545, 9), new Cosmetic("Gilly Boots", 35546, 10)),
 
        GOEBIE_WARPAINT_BLUE_OUTFIT(new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Bare feet", 39759, 10), new Cosmetic("Goebie Warpaint (Blue) Top", 35878, 4), new Cosmetic("Goebie Warpaint (Blue) Bottom", 35879, 7)),
 
        GOEBIE_WARPAINT_RED_OUTFIT(new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Bare feet", 39759, 10), new Cosmetic("Goebie Warpaint (Red) Top", 35880, 4), new Cosmetic("Goebie Warpaint (Red) Bottom", 35881, 7)),
 
        GOEBIE_WARPAINT_YELLOW_OUTFIT(new Cosmetic("Bare hands", 39760, 9), new Cosmetic("Bare feet", 39759, 10), new Cosmetic("Goebie Warpaint (Yellow) Top", 35882, 4), new Cosmetic("Goebie Warpaint (Yellow) Bottom", 35883, 7)),
 
        FURIES_AGENT_OUTFIT(new Cosmetic("Furies Agent Horns", 39796, 0), new Cosmetic("Furies Agent Gauntlets", 39800, 9), new Cosmetic("Furies Agent Greaves", 39799, 10), new Cosmetic("Furies Agent Wings", 39801, 18), new Cosmetic("Furies Agent Chest", 39797, 4), new Cosmetic("Furies Agent Trousers", 39798, 7)),
 
        LUNARFURY_TIER_1_OUTFIT(new Cosmetic("Lunarfury (Tier 1) Helm", 39869, 0), new Cosmetic("Lunarfury (Tier 1) Cape", 39879, 1), new Cosmetic("Lunarfury (Tier 1) Shieldbow", 39883, 3), new Cosmetic("Lunarfury (Tier 1) Chest", 39871, 4), new Cosmetic("Lunarfury (Tier 1) Legs", 39873, 7), new Cosmetic("Lunarfury (Tier 1) Gloves", 39875, 9), new Cosmetic("Lunarfury (Tier 1) Boots", 39877, 10)),
 
        LUNARFURY_TIER_2_OUTFIT(new Cosmetic("Lunarfury (Tier 2) Helm", 39870, 0), new Cosmetic("Lunarfury (Tier 2) Cape", 39880, 1), new Cosmetic("Lunarfury (Tier 2) Shieldbow", 39884, 3), new Cosmetic("Lunarfury (Tier 2) Chest", 39872, 4), new Cosmetic("Lunarfury (Tier 2) Legs", 39874, 7), new Cosmetic("Lunarfury (Tier 2) Gloves", 39876, 9), new Cosmetic("Lunarfury (Tier 2) Boots", 39878, 10)),
 
        FAYRE_MENOWIN_OUTFIT(new Cosmetic("Fayre (Menowin) Hat", 39970, 0), new Cosmetic("Fayre (Menowin) Coat", 39971, 4), new Cosmetic("Fayre (Menowin) Legs", 39972, 7), new Cosmetic("Fayre (Menowin) Gloves", 39974, 9), new Cosmetic("Fayre (Menowin) Boots", 39973, 10)),
 
        FAYRE_VENTURER_OUTFIT(new Cosmetic("Fayre (Venturer) Head", 39987, 0), new Cosmetic("Fayre (Venturer) Body", 39988, 4), new Cosmetic("Fayre (Venturer) Legs", 39989, 7), new Cosmetic("Fayre (Venturer) Gloves", 39991, 9), new Cosmetic("Fayre (Venturer) Boots", 39990, 10)),
 
        FAYRE_DANCER_OUTFIT(new Cosmetic("Fayre (Dancer) Headband", 39993, 0), new Cosmetic("Fayre (Dancer) Body", 39994, 4), new Cosmetic("Fayre (Dancer) Legs", 39995, 7), new Cosmetic("Fayre (Dancer) Gloves", 39997, 9), new Cosmetic("Fayre (Dancer) Boots", 39996, 10)),
 
        FAYRE_FORTUNE_TELLER_OUTFIT(new Cosmetic("Fayre (Fortune Teller) Headband", 39981, 0), new Cosmetic("Fayre (Fortune Teller) Top", 39982, 4), new Cosmetic("Fayre (Fortune Teller) Legs", 39983, 7), new Cosmetic("Fayre (Fortune Teller) Gloves", 39985, 9), new Cosmetic("Fayre (Fortune Teller) Boots", 39984, 10)),
 
        ABYSSAL_KNIGHT_OUTFIT(new Cosmetic("Abyssal Knight Helmet", 40164, 0), new Cosmetic("Abyssal Knight Platebody", 40163, 4), new Cosmetic("Abyssal Knight Platelegs", 40165, 7), new Cosmetic("Abyssal Knight Gauntlets", 40166, 9), new Cosmetic("Abyssal Knight Boots", 40167, 10)),
 
        DEFENDER_OF_THE_MIND_OUTFIT(new Cosmetic("Defender of the Mind helmet", 40175, 0), new Cosmetic("Defender of the Mind cape", 40183, 1), new Cosmetic("Defender of the Mind chestplate", 40176, 4), new Cosmetic("Aegis of the Mind Off-hand", 40180, 5), new Cosmetic("Defender of the Mind legguards", 40177, 7), new Cosmetic("Defender of the Mind gauntlets", 40178, 9), new Cosmetic("Defender of the Mind sabatons", 40179, 10)),
 
        MENAPHOS_FACTION_IMPERIAL_OUTFIT(new Cosmetic("Menaphos Faction (Imperial) Head", 40229, 0), new Cosmetic("Menaphos Faction (Imperial) Body", 40230, 4), new Cosmetic("Menaphos Faction (Imperial) Legs", 40231, 7)),
 
        MENAPHOS_FACTION_MERCHANT_OUTFIT(new Cosmetic("Menaphos Faction (Merchant) Head", 40232, 0), new Cosmetic("Menaphos Faction (Merchant) Body", 40233, 4), new Cosmetic("Menaphos Faction (Merchant) Legs", 40234, 7)),
 
        MENAPHOS_FACTION_WORKER_OUTFIT(new Cosmetic("Menaphos Faction (Worker) Head", 40235, 0), new Cosmetic("Menaphos Faction (Worker) Body", 40236, 4), new Cosmetic("Menaphos Faction (Worker) Legs", 40237, 7)),
 
        MENAPHOS_FACTION_PORT_OUTFIT(new Cosmetic("Menaphos Faction (Port) Head", 40238, 0), new Cosmetic("Menaphos Faction (Port) Body", 40239, 4), new Cosmetic("Menaphos Faction (Port) Legs", 40240, 7)),
 
        BEACH_SAND_OUTFIT(new Cosmetic("Beach Sand Outfit helmet", 40725, 0), new Cosmetic("Beach Sand Cape", 34930, 1), new Cosmetic("Beach Sand Outfit chestplate", 40726, 4), new Cosmetic("Beach Sand Outfit legguards", 40727, 7), new Cosmetic("Beach Sand Outfit gauntlets", 40728, 9), new Cosmetic("Beach Sand Outfit sabatons", 40729, 10)),
 
        OUTFIT_OF_HEARTS(new Cosmetic("Crown of Hearts", 40935, 0), new Cosmetic("Cape of Hearts", 40942, 1), new Cosmetic("Necklace of Hearts", 40943, 2), new Cosmetic("Top of Hearts", 40937, 4), new Cosmetic("Dress of Hearts", 40939, 7), new Cosmetic("Gloves of Hearts", 40940, 9), new Cosmetic("Boots of Hearts", 40941, 10)),
 
        WARDEN_OF_THE_MIND_OUTFIT(new Cosmetic("Warden of the Mind coif", 41120, 0), new Cosmetic("Warden of the Mind cape", 41126, 1), new Cosmetic("Staff of the Mind", 41125, 3), new Cosmetic("Warden of the Mind robe top", 41121, 4), new Cosmetic("Warden of the Mind robe bottom", 41122, 7), new Cosmetic("Warden of the Mind gloves", 41123, 9), new Cosmetic("Warden of the Mind boots", 41124, 10)),
 
        DUSKWING_OUTFIT(new Cosmetic("Duskwing Helmet", 41130, 0), new Cosmetic("Duskwing Gloves", 41133, 9), new Cosmetic("Duskwing Boots", 41134, 10), new Cosmetic("Duskwing Wings", 41135, 18), new Cosmetic("Duskwing Body", 41131, 4), new Cosmetic("Duskwing Legs", 41132, 7)),
 
        NOVTUMBERFEST_OUTFIT(new Cosmetic("NovtumberFest Hat", 41148, 0), new Cosmetic("NovtumberFest Boots", 41154, 10), new Cosmetic("NovtumberFest Jacket", 41150, 4), new Cosmetic("NovtumberFest Embroidered Shorts", 41152, 7)),
 
        HEADLESS_RIDER_OUTFIT(new Cosmetic("Headless Rider Mask", 41305, 0), new Cosmetic("Headless Rider Coat", 41306, 4), new Cosmetic("Headless Rider Legs", 41307, 7)),
 
        HELLION_OUTFIT(new Cosmetic("Hellion Headpiece", 41443, 0), new Cosmetic("Hellion Cape", 41448, 1), new Cosmetic("Hellion Blade", 41449, 3), new Cosmetic("Hellion Chestplate", 41444, 4), new Cosmetic("Hellion Shield Off-hand", 41450, 5), new Cosmetic("Hellion Legguards", 41445, 7), new Cosmetic("Hellion Gauntlets", 41446, 9), new Cosmetic("Hellion Boots", 41447, 10)),
 
        SURVIVAL_OUTFIT(new Cosmetic("Survival Hood", 41532, 0), new Cosmetic("Survival Torso", 41533, 4), new Cosmetic("Survival Trousers", 41534, 7), new Cosmetic("Survival Gloves", 41535, 9), new Cosmetic("Survival Boots", 41536, 10)),
 
        CHEER_HUNTER_OUTFIT(new Cosmetic("Cheer Hunter Head", 41553, 0), new Cosmetic("Cheer Hunter Cape", 41558, 1), new Cosmetic("Cheer Hunter Chestwrap", 41554, 4), new Cosmetic("Cheer Hunter Leggings", 41555, 7), new Cosmetic("Cheer Hunter Handwraps", 41556, 9), new Cosmetic("Cheer Hunter Boots", 41557, 10)),
 
        OCEANS_ARCHER_OUTFIT(new Cosmetic("Ocean's Archer Head", 41597, 0), new Cosmetic("Ocean's Archer Bow", 41602, 3), new Cosmetic("Ocean's Archer Body", 41598, 4), new Cosmetic("Ocean's Archer Legs", 41599, 7), new Cosmetic("Ocean's Archer Hands", 41600, 9), new Cosmetic("Ocean's Archer Feet", 41601, 10)),
 
        FACELESS_ASSASSIN_BLUE_OUTFIT(new Cosmetic("Faceless Assassin head (blue)", 41608, 0), new Cosmetic("Faceless Assassin Cape (blue)", 41613, 1), new Cosmetic("Faceless Assassin top (blue)", 41609, 4), new Cosmetic("Faceless Assassin legs (blue)", 41610, 7), new Cosmetic("Faceless Assassin hands (blue)", 41611, 9), new Cosmetic("Faceless Assassin feet (blue)", 41612, 10)),
 
        FACELESS_ASSASSIN_GREEN_OUTFIT(new Cosmetic("Faceless Assassin head (green)", 41690, 0), new Cosmetic("Faceless Assassin Cape (green)", 41695, 1), new Cosmetic("Faceless Assassin top (green)", 41691, 4), new Cosmetic("Faceless Assassin legs (green)", 41692, 7), new Cosmetic("Faceless Assassin hands (green)", 41693, 9), new Cosmetic("Faceless Assassin feet (green)", 41694, 10)),
 
        FACELESS_ASSASSIN_RED_OUTFIT(new Cosmetic("Faceless Assassin head (red)", 41697, 0), new Cosmetic("Faceless Assassin Cape (red)", 41702, 1), new Cosmetic("Faceless Assassin top (red)", 41698, 4), new Cosmetic("Faceless Assassin legs (red)", 41699, 7), new Cosmetic("Faceless Assassin hands (red)", 41700, 9), new Cosmetic("Faceless Assassin feet (red)", 41701, 10)),
 
        FACELESS_ASSASSIN_BLACK_OUTFIT(new Cosmetic("Faceless Assassin head (black)", 41704, 0), new Cosmetic("Faceless Assassin Cape (black)", 41709, 1), new Cosmetic("Faceless Assassin top (black)", 41705, 4), new Cosmetic("Faceless Assassin legs (black)", 41706, 7), new Cosmetic("Faceless Assassin hands (black)", 41707, 9), new Cosmetic("Faceless Assassin feet (black)", 41708, 10)),
 
        AGENT_OF_THE_ELDEST_OUTFIT(new Cosmetic("Agent of the Eldest Helm", 41717, 0), new Cosmetic("Agent of the Eldest Body", 41715, 4), new Cosmetic("Agent of the Eldest Greaves", 41716, 7), new Cosmetic("Agent of the Eldest Gloves", 41713, 9), new Cosmetic("Agent of the Eldest Boots", 41714, 10)),
 
        AMARE_OUTFIT(new Cosmetic("Amare Wreath", 41746, 0), new Cosmetic("Amare Cape", 41752, 1), new Cosmetic("Amare Wings", 41751, 18), new Cosmetic("Amare Top", 41747, 4), new Cosmetic("Amare Bottom", 41748, 7), new Cosmetic("Amare Bracelets", 41750, 9), new Cosmetic("Amare Pumps", 41749, 10)),
 
        OUTFIT_OF_SPADES(new Cosmetic("Crown of Spades", 42224, 0), new Cosmetic("Cape of Spades", 42231, 1), new Cosmetic("Necklace of Spades", 42232, 2), new Cosmetic("Top of Spades", 42225, 4), new Cosmetic("Dress of Spades", 42227, 7), new Cosmetic("Gloves of Spades", 42229, 9), new Cosmetic("Boots of Spades", 42230, 10)),
 
        SOLITE_ARMOUR_OUTFIT(new Cosmetic("Solite helm", 42320, 0), new Cosmetic("Solite cape", 42325, 1), new Cosmetic("Solstice blade", 42333, 3), new Cosmetic("Solite chestplate", 42321, 4), new Cosmetic("Solstice shield Off-hand", 42334, 5), new Cosmetic("Solite platelegs", 42322, 7), new Cosmetic("Solite mail gloves", 42324, 9), new Cosmetic("Solite boots", 42323, 10)),
 
        LUNITE_ARMOUR_OUTFIT(new Cosmetic("Lunite helm", 42327, 0), new Cosmetic("Lunite cape", 42332, 1), new Cosmetic("Solstice blade", 42333, 3), new Cosmetic("Lunite chestplate", 42328, 4), new Cosmetic("Solstice shield Off-hand", 42334, 5), new Cosmetic("Lunite platelegs", 42329, 7), new Cosmetic("Lunite mail gloves", 42331, 9), new Cosmetic("Lunite boots", 42330, 10)),
 
        IWI_OUTFIT_PINK(new Cosmetic("Iwi mask (pink)", 42493, 0), new Cosmetic("Iwi chest piece (pink)", 42494, 4), new Cosmetic("Iwi shin guards (pink)", 42495, 7), new Cosmetic("Iwi hand wraps (pink)", 42496, 9), new Cosmetic("Iwi foot wraps (pink)", 42497, 10)),
 
        IWI_OUTFIT_BLUE(new Cosmetic("Iwi mask (blue)", 42499, 0), new Cosmetic("Iwi chest piece (blue)", 42500, 4), new Cosmetic("Iwi shin guards (blue)", 42501, 7), new Cosmetic("Iwi hand wraps (blue)", 42502, 9), new Cosmetic("Iwi foot wraps (blue)", 42503, 10)),
 
        IWI_OUTFIT_ORANGE(new Cosmetic("Iwi mask (orange)", 42505, 0), new Cosmetic("Iwi chest piece (orange)", 42506, 4), new Cosmetic("Iwi shin guards (orange)", 42507, 7), new Cosmetic("Iwi hand wraps (orange)", 42508, 9), new Cosmetic("Iwi foot wraps (orange)", 42509, 10)),
 
        OUTFIT_OF_DIAMONDS(new Cosmetic("Crown of Diamonds", 42848, 0), new Cosmetic("Cape of Diamonds", 42855, 1), new Cosmetic("Necklace of Diamonds", 42856, 2), new Cosmetic("Top of Diamonds", 42849, 4), new Cosmetic("Dress of Diamonds", 42851, 7), new Cosmetic("Gloves of Diamonds", 42853, 9), new Cosmetic("Boots of Diamonds", 42854, 10)),
 
        OUTFIT_OF_CLUBS(new Cosmetic("Crown of Clubs", 42864, 0), new Cosmetic("Cape of Clubs", 42871, 1), new Cosmetic("Necklace of Clubs", 42872, 2), new Cosmetic("Top of Clubs", 42865, 4), new Cosmetic("Dress of Clubs", 42867, 7), new Cosmetic("Gloves of Clubs", 42869, 9), new Cosmetic("Boots of Clubs", 42870, 10)),
 
        TWISTED_JESTER_OUTFIT(new Cosmetic("Twisted Jester Hat", 42880, 0), new Cosmetic("Twisted Jester Cape", 42885, 1), new Cosmetic("Twisted Jester Coat", 42881, 4), new Cosmetic("Twisted Jester Bottoms", 42882, 7), new Cosmetic("Twisted Jester Gloves", 42883, 9), new Cosmetic("Twisted Jester Boots", 42884, 10)),
 
        OCEANS_WARRIOR_OUTFIT(new Cosmetic("Ocean's Warrior Head", 42706, 0), new Cosmetic("Ocean's Warrior Body", 42707, 4), new Cosmetic("Ocean's Warrior Legs", 42708, 7), new Cosmetic("Ocean's Warrior Hands", 42709, 9), new Cosmetic("Ocean's Warrior Feet", 42710, 10)),
 
        OCEANS_MAGE_OUTFIT(new Cosmetic("Ocean's Mage Head", 42714, 0), new Cosmetic("Ocean's Mage Body", 42715, 4), new Cosmetic("Ocean's Mage Legs", 42716, 7), new Cosmetic("Ocean's Mage Hands", 42717, 9), new Cosmetic("Ocean's Mage Feet", 42718, 10)),
 
        HYDRO_SUIT_OUTFIT(new Cosmetic("Hydro Head", 42908, 0), new Cosmetic("Hydro Cape", 42916, 1), new Cosmetic("Hydro Body", 42909, 4), new Cosmetic("Hydro Legs", 42910, 7), new Cosmetic("Hydro Hands", 42911, 9), new Cosmetic("Hydro Feet", 42912, 10)),
 
        STORMBORN_ARMOUR_OUTFIT(new Cosmetic("Stormborn Helmet", 42949, 0), new Cosmetic("Stormborn Chestplate", 42950, 4), new Cosmetic("Stormborn Greaves", 42951, 7), new Cosmetic("Stormborn Gauntlets", 42952, 9), new Cosmetic("Stormborn Boots", 42953, 10)),
 
        ACOLYTE_OF_SEIRYU_OUTFIT(new Cosmetic("Acolyte of Seiryu headdress", 43105, 0), new Cosmetic("Acolyte of Seiryu jacket", 43107, 4), new Cosmetic("Acolyte of Seiryu trousers", 43109, 7), new Cosmetic("Acolyte of Seiryu handwraps", 43111, 9), new Cosmetic("Acolyte of Seiryu footwraps", 43113, 10)),
 
        SAKADAGAMI_OUTFIT(new Cosmetic("Sakadagami headdress", 43095, 0), new Cosmetic("Sakadagami jacket", 43097, 4), new Cosmetic("Sakadagami trousers", 43099, 7), new Cosmetic("Sakadagami handwraps", 43101, 9), new Cosmetic("Sakadagami footwraps", 43103, 10)),
 
        UMBRAL_OUTFIT(new Cosmetic("Umbral Helm", 43266, 0), new Cosmetic("Umbral Chestplate", 43267, 4), new Cosmetic("Umbral Legs", 43268, 7), new Cosmetic("Umbral Gloves", 43269, 9), new Cosmetic("Umbral Feet", 43270, 10)),
 
        BEACH_ARMOUR_OUTFIT(new Cosmetic("Beach Armour Helm", 43301, 0), new Cosmetic("Beach Armour Boots", 43302, 10), new Cosmetic("Beach Armour Torso", 43304, 4), new Cosmetic("Beach Armour Legs", 43303, 7)),
 
        PLAGUE_DOCTOR_OUTFIT_GREEN(new Cosmetic("Plague Doctor herbed mask (green)", 44344, 0), new Cosmetic("Plague Doctor waxed armour (green)", 44345, 4), new Cosmetic("Plague Doctor leather britches (green)", 44346, 7), new Cosmetic("Plague Doctor miasma guards (green)", 44347, 9), new Cosmetic("Plague Doctor bloused boots (green)", 44348, 10)),
 
        PLAGUE_DOCTOR_OUTFIT_CYAN(new Cosmetic("Plague Doctor herbed mask (cyan)", 44354, 0), new Cosmetic("Plague Doctor waxed armour (cyan)", 44355, 4), new Cosmetic("Plague Doctor leather britches (cyan)", 44356, 7), new Cosmetic("Plague Doctor miasma guards (cyan)", 44357, 9), new Cosmetic("Plague Doctor bloused boots(cyan)", 44358, 10)),
 
        PLAGUE_DOCTOR_OUTFIT_PURPLE(new Cosmetic("Plague Doctor herbed mask (purple)", 44349, 0), new Cosmetic("Plague Doctor waxed armour (purple)", 44350, 4), new Cosmetic("Plague Doctor leather britches (purple)", 44351, 7), new Cosmetic("Plague Doctor miasma guards (purple)", 44352, 9), new Cosmetic("Plague Doctor bloused boots (purple)", 44353, 10)),
 
        BLACK_NECRONIUM_OUTFIT(new Cosmetic("Black Necronium Full Helm", 44436, 0), new Cosmetic("Black Necronium Battleaxe", 44441, 3), new Cosmetic("Black Necronium Platebody", 44440, 4), new Cosmetic("Off-hand Black Necronium Battleaxe", 44442, 5), new Cosmetic("Black Necronium Platelegs", 44439, 7), new Cosmetic("Black Necronium Gauntlets", 44438, 9), new Cosmetic("Black Necronium Armoured Boots", 44437, 10)),
 
        VALKYRIE_OUTFIT(new Cosmetic("Valkyrie head", 44461, 0), new Cosmetic("Valkyrie wings", 44466, 18), new Cosmetic("Valkyrie Lance", 44467, 3), new Cosmetic("Valkyrie body", 44462, 4), new Cosmetic("Valkyrie legs", 44463, 7), new Cosmetic("Valkyrie hands", 44464, 9), new Cosmetic("Valkyrie feet", 44465, 10)),
 
        SLIME_HUNTER_OUTFIT(new Cosmetic("Slime Hunter helmet", 44505, 0), new Cosmetic("Slime Hunter chestplate", 44506, 4), new Cosmetic("Slime Hunter legplates", 44507, 7), new Cosmetic("Slime Hunter gloves", 44508, 9), new Cosmetic("Slime Hunter boots", 44509, 10)),
 
        NECTURION_OUTFIT(new Cosmetic("Necturion Helm", 44709, 0), new Cosmetic("Necturion Scimitar", 44715, 3), new Cosmetic("Necturion Platebody", 44710, 4), new Cosmetic("Necturion Shield Off-hand", 44714, 5), new Cosmetic("Necturion Platelegs", 44711, 7), new Cosmetic("Necturion Gauntlets", 44712, 9), new Cosmetic("Necturion Boots", 44713, 10)),
 
        NECTURION_MASTERWORK_OUTFIT(new Cosmetic("Necturion (Masterwork) Helm", 44718, 0), new Cosmetic("Necturion (Masterwork) Scimitar", 44724, 3), new Cosmetic("Necturion (Masterwork) Platebody", 44719, 4), new Cosmetic("Necturion (Masterwork) Shield Off-hand", 44723, 5), new Cosmetic("Necturion (Masterwork) Platelegs", 44720, 7), new Cosmetic("Necturion (Masterwork) Gauntlets", 44721, 9), new Cosmetic("Necturion (Masterwork) Boots", 44722, 10)),
 
        INVICTUM_OUTFIT(new Cosmetic("Invictum Helm", 44727, 0), new Cosmetic("Invictum Scimitar", 44733, 3), new Cosmetic("Invictum Platebody", 44728, 4), new Cosmetic("Invictum Shield Off-hand", 44732, 5), new Cosmetic("Invictum Platelegs", 44729, 7), new Cosmetic("Invictum Gauntlets", 44730, 9), new Cosmetic("Invictum Boots", 44731, 10)),
 
        INVICTUM_MASTERWORK_OUTFIT(new Cosmetic("Invictum (Masterwork) Helm", 44736, 0), new Cosmetic("Invictum (Masterwork) Scimitar", 44742, 3), new Cosmetic("Invictum (Masterwork) Platebody", 44737, 4), new Cosmetic("Invictum (Masterwork) Shield Off-hand", 44741, 5), new Cosmetic("Invictum (Masterwork) Platelegs", 44738, 7), new Cosmetic("Invictum (Masterwork) Gauntlets", 44739, 9), new Cosmetic("Invictum (Masterwork) Boots", 44740, 10)),
 
        AETHERIUM_OUTFIT(new Cosmetic("Aetherium Helm", 44745, 0), new Cosmetic("Aetherium Scimitar", 44751, 3), new Cosmetic("Aetherium Platebody", 44746, 4), new Cosmetic("Aetherium Shield Off-hand", 44750, 5), new Cosmetic("Aetherium Platelegs", 44747, 7), new Cosmetic("Aetherium Gauntlets", 44748, 9), new Cosmetic("Aetherium Boots", 44749, 10)),
 
        AETHERIUM_MASTERWORK_OUTFIT(new Cosmetic("Aetherium (Masterwork) Helm", 44754, 0), new Cosmetic("Aetherium (Masterwork) Scimitar", 44760, 3), new Cosmetic("Aetherium (Masterwork) Platebody", 44755, 4), new Cosmetic("Aetherium (Masterwork) Shield Off-hand", 44759, 5), new Cosmetic("Aetherium (Masterwork) Platelegs", 44756, 7), new Cosmetic("Aetherium (Masterwork) Gauntlets", 44757, 9), new Cosmetic("Aetherium (Masterwork) Boots", 44758, 10)),
 
        AMARE_PURPLE_OUTFIT(new Cosmetic("Amare Wreath (purple)", 47373, 0), new Cosmetic("Amare Cape (purple)", 47378, 1), new Cosmetic("Amare Wings (purple)", 47379, 18), new Cosmetic("Amare Top (purple)", 47374, 4), new Cosmetic("Amare Bottom (purple)", 47375, 7), new Cosmetic("Amare Bracelets (purple)", 47377, 9), new Cosmetic("Amare Pumps (purple)", 47376, 10)),
 
        BONE_MASTER_OUTFIT(new Cosmetic("Bone Master Helm", 47575, 0), new Cosmetic("Bone Master Chest", 47576, 4), new Cosmetic("Bone Master Chaps", 47577, 7), new Cosmetic("Bone Master Gloves", 47578, 9), new Cosmetic("Bone Master Boots", 47579, 10)),
 
        THE_RISEN_OUTFIT(new Cosmetic("The Risen Crown", 47727, 0), new Cosmetic("The Risen Spirit Breaker Sword", 47732, 3), new Cosmetic("The Risen Pauldrons", 47728, 4), new Cosmetic("The Risen Surcoat", 47729, 7), new Cosmetic("The Risen Bracers", 47730, 9), new Cosmetic("The Risen Manacles", 47731, 10)),
 
        COTTONTAIL_KNIGHT_ARMOUR_OUTFIT(new Cosmetic("Cottontail knight helmet", 47745, 0), new Cosmetic("Cottontail Knight chestplate", 47746, 4), new Cosmetic("Cottontail Knight legplates", 47747, 7), new Cosmetic("Cottontail Knight gauntlets", 47748, 9), new Cosmetic("Cottontail Knight boots", 47749, 10)),
 
        IMPERIAL_CORONATION_OUTFIT(new Cosmetic("Imperial Coronation Helmet", 47784, 0), new Cosmetic("Imperial Coronation Cuirass", 47786, 4), new Cosmetic("Imperial Coronation Plate Legs", 47788, 7), new Cosmetic("Imperial Coronation Gauntlets", 47792, 9), new Cosmetic("Imperial Coronation Sabatons", 47790, 10)),
 
        DRAGONKIN_IMPERIAL_SET_OUTFIT(new Cosmetic("Dragonkin Imperial helmet", 47839, 0), new Cosmetic("Dragonkin Imperial chestplate", 47840, 4), new Cosmetic("Dragonkin Imperial legplates", 47841, 7), new Cosmetic("Dragonkin Imperial gauntlets", 47842, 9), new Cosmetic("Dragonkin Imperial boots", 47843, 10)),
 
        VARROCK_AGENT_OUTFIT(new Cosmetic("Varrock Agent Cowl", 47897, 0), new Cosmetic("Varrock Agent Tunic", 47898, 4), new Cosmetic("Varrock Agent Faulds", 47899, 7), new Cosmetic("Varrock Agent Invisible Gloves", 47901, 9), new Cosmetic("Varrock Agent Boots", 47900, 10)),
 
        REVOLUTIONARY_MASK_AND_HAT(new Cosmetic("Revolutionary Mask and Hat", 24567, 0)),
 
        REVOLUTIONARY_MASK(new Cosmetic("Revolutionary Mask", 24569, 0)),
 
        REVOLUTIONARY_HAT(new Cosmetic("Revolutionary Hat", 24571, 0)),
 
        SUNGLASS_MONOCLES(new Cosmetic("Sunglass Monocles", 24822, 0)),
 
        WOODLAND_CROWN(new Cosmetic("Woodland Crown", 25170, 0)),
 
        KALPHITE_GREATHELM(new Cosmetic("Kalphite Greathelm", 25174, 0)),
 
        SEAWEED_HAIR(new Cosmetic("Seaweed Hair", 24819, 0)),
 
        BOOK_OF_FACES(new Cosmetic("Book of Faces", 23664, 0)),
 
        SCARECROW_MASK(new Cosmetic("Scarecrow Mask", 27602, 0)),
 
        TURKEY_HAT(new Cosmetic("Turkey Hat", 27604, 0)),
 
        FLAMING_SKULL_RED(new Cosmetic("Flaming Skull (Red)", 27606, 0)),
 
        FLAMING_SKULL_GREEN(new Cosmetic("Flaming Skull (Green)", 27610, 0)),
 
        FLAMING_SKULL_BLUE(new Cosmetic("Flaming Skull (Blue)", 27608, 0)),
 
        FLAMING_SKULL_PURPLE(new Cosmetic("Flaming Skull (Purple)", 27612, 0)),
 
        CROWN_OF_SUPREMACY(new Cosmetic("Crown of Supremacy", 28822, 0)),
 
        CROWN_OF_LEGENDS(new Cosmetic("Crown of Legends", 28823, 0)),
 
        SINISTER_CLOWN_FACE(new Cosmetic("Sinister Clown Face", 29762, 0)),
 
        CHRISTMAS_PUDDING_HEAD(new Cosmetic("Christmas Pudding Head", 30359, 0)),
 
        SNOWMAN_HEAD(new Cosmetic("Snowman Head", 30361, 0)),
 
        RUNEFEST_2011_HOOD(new Cosmetic("RuneFest 2011 Hood", 29944, 0)),
 
        VALKYRIE_HELMET(new Cosmetic("Valkyrie Helmet", 30613, 0)),
 
        DR_NABANIKS_OLD_TRILBY(new Cosmetic("Dr Nabanik's Old Trilby", 31028, 0)),
 
        HELM_OF_ZAROS(new Cosmetic("Helm of Zaros", 31039, 0)),
 
        CREST_OF_SEREN(new Cosmetic("Crest of Seren", 31040, 0)),
 
        REPLICA_VOID_KNIGHT_MELEE_HELM(new Cosmetic("Replica Void Knight Melee Helm", 31706, 0)),
 
        REPLICA_VOID_KNIGHT_MAGE_HELM(new Cosmetic("Replica Void Knight Mage Helm", 31707, 0)),
 
        REPLICA_VOID_KNIGHT_RANGER_HELM(new Cosmetic("Replica Void Knight Ranger Helm", 31708, 0)),
 
        OXFAM_REINDEER_ANTLERS(new Cosmetic("Oxfam Reindeer Antlers", 33587, 0)),
 
        LAVA_HOOD(new Cosmetic("Lava Hood", 33654, 0)),
 
        MORTAR_BOARD(new Cosmetic("Mortar Board", 34244, 0)),
 
        DIVING_SUIT_MERMAID_HUNTER_HELMET(new Cosmetic("Diving Suit Mermaid Hunter Helmet", 34679, 0)),
 
        DIVING_SUIT_SALVAGE_HUNTER_HELMET(new Cosmetic("Diving Suit Salvage Hunter Helmet", 34680, 0)),
 
        BEACH_COCONUT_HAT(new Cosmetic("Beach Coconut Hat", 34931, 0)),
 
        BEACH_SNORKEL(new Cosmetic("Beach Snorkel", 35087, 0)),
 
        BEACH_BUCKET_HEAD(new Cosmetic("Beach Bucket Head", 35088, 0)),
 
        BEACH_CLAWDIA_HAT(new Cosmetic("Beach Clawdia Hat", 35089, 0)),
 
        ZAROS_MORION(new Cosmetic("Zaros Morion", 35784, 0)),
 
        MASQUERADE_MASK(new Cosmetic("Masquerade Mask", 36781, 0)),
 
        GEM_CROWN(new Cosmetic("Gem Crown", 37505, 0)),
 
        CANNONBALL_HEAD(new Cosmetic("Cannonball Head", 37724, 0)),
 
        BEACH_LIFEGUARD_CHAIR_HAT(new Cosmetic("Beach Lifeguard Chair Hat", 37988, 0)),
 
        LIGHTNING_ROD_HAT(new Cosmetic("Lightning Rod Hat", 38985, 0)),
 
        DEVOTION_SPRITE_HEAD(new Cosmetic("Devotion Sprite Head", 39664, 0)),
 
        BUNNY_HEAD(new Cosmetic("Bunny Head", 39912, 0)),
 
        FAYRE_PICKAXE_HAT(new Cosmetic("Fayre Pickaxe Hat", 40000, 0)),
 
        PANTHEON_APMEKEN_MASK(new Cosmetic("Pantheon (Apmeken) Mask", 40205, 0)),
 
        PANTHEON_CRONDIS_MASK(new Cosmetic("Pantheon (Crondis) Mask", 40209, 0)),
 
        PANTHEON_HET_MASK(new Cosmetic("Pantheon (Het) Mask", 40211, 0)),
 
        PANTHEON_SCABARAS_MASK(new Cosmetic("Pantheon (Scabaras) Mask", 40207, 0)),
 
        PHARAOH_MASK(new Cosmetic("Pharaoh Mask", 40215, 0)),
 
        CAT_HAT(new Cosmetic("Cat Hat", 40271, 0)),
 
        PYRAMID_HAT(new Cosmetic("Pyramid Hat", 40976, 0)),
 
        NOVTUMBERFEST_BEER_GOGGLES(new Cosmetic("NovtumberFest Beer Goggles", 41157, 0)),
 
        CLOCKWORK_CROWN(new Cosmetic("Clockwork crown", 41343, 0)),
 
        RAINBOW_HALO(new Cosmetic("Rainbow Halo", 41405, 0)),
 
        CHRISTMAS_PRESENT_HELMET(new Cosmetic("Christmas Present Helmet", 41518, 0)),
 
        RABID_JACK_HAT(new Cosmetic("Rabid Jack hat", 42348, 0)),
 
        FAYRE_EASTER_EGG_HAT(new Cosmetic("Fayre Easter Egg Hat", 42408, 0)),
 
        FAYRE_FACE_PAINT__CAT(new Cosmetic("Fayre Face Paint - Cat", 42409, 0)),
 
        FAYRE_FACE_PAINT__BUTTERFLY(new Cosmetic("Fayre Face Paint - Butterfly", 42410, 0)),
 
        FAYRE_FACE_PAINT__FLOWER(new Cosmetic("Fayre Face Paint - Flower", 42411, 0)),
 
        SEERS_HEADBAND_1(new Cosmetic("Seer's headband 1", 42481, 0)),
 
        SEERS_HEADBAND_2(new Cosmetic("Seer's headband 2", 42482, 0)),
 
        SEERS_HEADBAND_3(new Cosmetic("Seer's headband 3", 42483, 0)),
 
        SEERS_HEADBAND_4(new Cosmetic("Seer's headband 4", 42484, 0)),
 
        BEACH_VANILLA_HAIR(new Cosmetic("Beach Vanilla Hair", 43305, 0)),
 
        BEACH_CHOCOLATE_HAIR(new Cosmetic("Beach Chocolate Hair", 43306, 0)),
 
        BEACH_STRAWBERRY_HAIR(new Cosmetic("Beach Strawberry Hair", 43307, 0)),
 
        BEACH_MINT_HAIR(new Cosmetic("Beach Mint Hair", 43308, 0)),
 
        SHADOW_GEM_CROWN(new Cosmetic("Shadow Gem Crown", 44146, 0)),
 
        HELM_OF_TRIALS(new Cosmetic("Helm of Trials", 44161, 0)),
 
        WREATH_CROWN(new Cosmetic("Wreath crown", 44528, 0)),
 
        SHADOW_GEM_HELM(new Cosmetic("Shadow Gem Helm", 47359, 0)),
 
        ELF_EARS_UNADORNED(new Cosmetic("Elf Ears (Unadorned)", 47875, 0)),
 
        ELF_EARS_ANADLAU(new Cosmetic("Elf Ears (Anadlau)", 47876, 0)),
 
        ELF_EARS_GRISIAL(new Cosmetic("Elf Ears (Grisial)", 47877, 0)),
 
        ELF_EARS_TORRI(new Cosmetic("Elf Ears (Torri)", 47878, 0)),
 
        ELF_EARS_TINCIAL(new Cosmetic("Elf Ears (Tincial)", 47879, 0)),
 
        COMPLETIONIST_HOOD(new Cosmetic("Completionist Hood", 47886, 0)),
 
        COMPLETIONIST_HOOD_T(new Cosmetic("Completionist Hood (t)", 47889, 0)),
 
        SQUID_TENTACLE_CAPE(new Cosmetic("Squid Tentacle Cape", 24817, 1)),
 
        RUNEFEST_2013_CAPE(new Cosmetic("RuneFest 2013 Cape", 29943, 1)),
 
        BRASSICAN_CLOAK(new Cosmetic("Brassican Cloak", 31182, 1)),
 
        MARIMBAN_CLOAK(new Cosmetic("Marimban Cloak", 31183, 1)),
 
        GODLESS_CLOAK(new Cosmetic("Godless Cloak", 31184, 1)),
 
        ENHANCED_FIRE_CAPE(new Cosmetic("Enhanced Fire Cape", 31603, 1)),
 
        LOOTS_CAPE(new Cosmetic("Loots Cape", 31618, 1)),
 
        JERRODS_CAPE(new Cosmetic("Jerrod's Cape", 34195, 1)),
 
        VITALITY_CAPE(new Cosmetic("Vitality Cape", 34943, 1)),
 
        VITALITY_INACTIVE_CAPE(new Cosmetic("Vitality (Inactive) Cape", 34953, 1)),
 
        BEACH_DRAGON_RING(new Cosmetic("Beach Dragon Ring", 35097, 1)),
 
        CLAN_CLOAK(new Cosmetic("Clan Cloak", 36350, 1)),
 
        GAMEBLAST_2016_CAPE(new Cosmetic("GameBlast 2016 Cape", 36855, 1)),
 
        DARKSCAPE(new Cosmetic("DarksCape", 37199, 1)),
 
        GEM_CAPE(new Cosmetic("Gem Cape", 37504, 1)),
 
        SKILLING_BACKPACK(new Cosmetic("Skilling Backpack", 37699, 1)),
 
        SKULLS_CAPE(new Cosmetic("Skulls Cape", 37722, 1)),
 
        BEACH_TOWEL_CAPE(new Cosmetic("Beach Towel Cape", 37986, 1)),
 
        BEACH_SHARK_FIN(new Cosmetic("Beach Shark Fin", 37987, 1)),
 
        BEACH_SHADOW_DRAKE_RING(new Cosmetic("Beach Shadow Drake Ring", 37990, 1)),
 
        BEACH_DUCK_RING(new Cosmetic("Beach Duck Ring", 37991, 1)),
 
        GEM_SACK(new Cosmetic("Gem Sack", 39685, 1)),
 
        FAYRE_FORTUNE_CAPE(new Cosmetic("Fayre Fortune Cape", 40001, 1)),
 
        CHRISTMAS_PRESENT_SACK(new Cosmetic("Christmas Present Sack", 41519, 1)),
 
        CHRISTMAS_TREE_CAPE(new Cosmetic("Christmas Tree Cape", 41520, 1)),
 
        PALAPA_CAPE(new Cosmetic("Palapa Cape", 40272, 1)),
 
        ARDOUGNE_CLOAK_1(new Cosmetic("Ardougne cloak 1", 42461, 1)),
 
        ARDOUGNE_CLOAK_2(new Cosmetic("Ardougne cloak 2", 42462, 1)),
 
        ARDOUGNE_CLOAK_3(new Cosmetic("Ardougne cloak 3", 42463, 1)),
 
        ARDOUGNE_CLOAK_4(new Cosmetic("Ardougne cloak 4", 42464, 1)),
 
        BEACH_GOEBIE_BACKPACK(new Cosmetic("Beach Goebie Backpack", 43296, 1)),
 
        CLOAK_OF_MIGHT(new Cosmetic("Cloak of Might", 43404, 1)),
 
        CLOAK_OF_TALENT(new Cosmetic("Cloak of Talent", 43406, 1)),
 
        CLOAK_OF_PROFICIENCY(new Cosmetic("Cloak of Proficiency", 43408, 1)),
 
        SHADOW_GEM_CAPE(new Cosmetic("Shadow Gem Cape", 44145, 1)),
 
        SHADOW_GEM_SACK(new Cosmetic("Shadow Gem Sack", 44147, 1)),
 
        SNOW_CAPE(new Cosmetic("Snow cape", 44526, 1)),
 
        JORMUNGAND_CAPE(new Cosmetic("Jormungand Cape", 47333, 1)),
 
        PARTY_WYVERN_CAPE(new Cosmetic("Party Wyvern Cape", 47581, 1)),
 
        PHOENIX_WING_BACKPACK(new Cosmetic("Phoenix Wing Backpack", 47671, 1)),
 
        COMPLETIONIST_CAPE(new Cosmetic("Completionist Cape", 47885, 1)),
 
        COMPLETIONIST_CAPE_HOODED(new Cosmetic("Completionist Cape (hooded)", 47887, 1)),
 
        COMPLETIONIST_CAPE_T(new Cosmetic("Completionist Cape (t)", 47888, 1)),
 
        COMPLETIONIST_CAPE_HOODEDT(new Cosmetic("Completionist Cape (hooded)(t)", 47890, 1)),
 
        BUNNY_TAIL(new Cosmetic("Bunny Tail", 25172, 1)),
 
        INARI_TAIL(new Cosmetic("Inari Tail", 31827, 1)),
 
        SKELETAL_TAIL(new Cosmetic("Skeletal Tail", 31829, 1)),
 
        DEVOTION_SPRITE_TAIL(new Cosmetic("Devotion Sprite Tail", 39665, 1)),
 
        REPLICA_VOID_KNIGHT_SEAL(new Cosmetic("Replica Void Knight Seal", 31710, 2)),
 
        ZAROSIAN_MARK(new Cosmetic("Zarosian Mark", 31987, 2)),
 
        GAMEBLAST_2016_AMULET(new Cosmetic("GameBlast 2016 Amulet", 36854, 2)),
 
        GEM_NECKLACE(new Cosmetic("Gem Necklace", 37503, 2)),
 
        RAINBOW_AMULET(new Cosmetic("Rainbow Amulet", 39159, 2)),
 
        NOVTUMBERFEST_GINGERBREAD_NECKLACE(new Cosmetic("NovtumberFest Gingerbread Necklace", 41156, 2)),
 
        RUDOLPH_NECKLACE(new Cosmetic("Rudolph Necklace", 41521, 2)),
 
        DESERT_AMULET_1(new Cosmetic("Desert amulet 1", 42465, 2)),
 
        DESERT_AMULET_2(new Cosmetic("Desert amulet 2", 42466, 2)),
 
        DESERT_AMULET_3(new Cosmetic("Desert amulet 3", 42467, 2)),
 
        DESERT_AMULET_4(new Cosmetic("Desert amulet 4", 42468, 2)),
 
        SHADOW_GEM_NECKLACE(new Cosmetic("Shadow Gem Necklace", 44144, 2)),
 
        SEABORNE_DAGGER(new Cosmetic("Seaborne Dagger", 24904, 3)),
 
        JOUSTING_LANCE_RAPIER(new Cosmetic("Jousting Lance (Rapier)", 25112, 3)),
 
        BRUTAL_RAPIER(new Cosmetic("Brutal Rapier", 26015, 3)),
 
        SIR_OWEN_SHORTSWORD(new Cosmetic("Sir Owen Shortsword", 28983, 3)),
 
        SHADOW_SIR_OWEN_SHORTSWORD(new Cosmetic("Shadow Sir Owen Shortsword", 31124, 3)),
 
        PROTO_PACK_DAGGER(new Cosmetic("Proto Pack Dagger", 32400, 3)),
 
        DEATH_LOTUS_SAI(new Cosmetic("Death Lotus Sai", 35311, 3)),
 
        MAZCAB_POKER(new Cosmetic("Mazcab Poker", 35874, 3)),
 
        SHADOW_GLAIVE_DAGGER(new Cosmetic("Shadow Glaive Dagger", 37113, 3)),
 
        ORNATE_DAGGER(new Cosmetic("Ornate Dagger", 37296, 3)),
 
        ORNATE_SCIMITAR(new Cosmetic("Ornate Scimitar", 37298, 3)),
 
        MANTICORE_DAGGER(new Cosmetic("Manticore Dagger", 37655, 3)),
 
        BEACH_CORAL_SWORD(new Cosmetic("Beach Coral Sword", 38005, 3)),
 
        BEACH_CORAL_DAGGER(new Cosmetic("Beach Coral Dagger", 38007, 3)),
 
        CRYSTAL_PEACOCK_SWORD(new Cosmetic("Crystal Peacock Sword", 38441, 3)),
 
        FAE_FAIRY_DAGGER(new Cosmetic("Fae Fairy Dagger", 38417, 3)),
 
        FAE_FAIRY_SHORTSWORD(new Cosmetic("Fae Fairy Shortsword", 38419, 3)),
 
        PANTHEON_SCABARAS_WAS(new Cosmetic("Pantheon (Scabaras) Was", 40208, 3)),
 
        SUNDOWN_DAGGER(new Cosmetic("Sundown Dagger", 41619, 3)),
 
        TWISTED_JESTER_DAGGER(new Cosmetic("Twisted Jester Dagger", 42886, 3)),
 
        TIDAL_SHORTSWORD(new Cosmetic("Tidal Shortsword", 42896, 3)),
 
        PLAGUE_DOCTOR_LEECHING_SYRINGE(new Cosmetic("Plague Doctor leeching syringe", 44359, 3)),
 
        VALKYRIE_SWORD(new Cosmetic("Valkyrie Sword", 44469, 3)),
 
        HUGINN_BLADE(new Cosmetic("Huginn Blade", 47327, 3)),
 
        SHADOW_GEM_BATTLEAXE(new Cosmetic("Shadow Gem Battleaxe", 47360, 3)),
 
        BRUTISH_DAGGER(new Cosmetic("Brutish Dagger", 48147, 3)),
 
        PACTBREAKER_LONGSWORD(new Cosmetic("Pactbreaker Longsword", 24573, 3)),
 
        KRILS_CLEAVER(new Cosmetic("K'ril's Cleaver", 25398, 3)),
 
        BRUTAL_LONGSWORD(new Cosmetic("Brutal Longsword", 26019, 3)),
 
        PALADIN_BLADE(new Cosmetic("Paladin Blade", 26476, 3)),
 
        RUNEFEST_2010_FLAGSTAFF_OF_FESTIVITIES(new Cosmetic("RuneFest 2010 Flagstaff of Festivities", 29945, 3)),
 
        SHADOW_SIR_OWEN_LONGSWORD(new Cosmetic("Shadow Sir Owen Longsword", 31122, 3)),
 
        FIRESTORM_BLADE(new Cosmetic("Firestorm Blade", 31363, 3)),
 
        SILVERLIGHT(new Cosmetic("Silverlight", 34511, 3)),
 
        SILVERLIGHT_DYED(new Cosmetic("Silverlight (Dyed)", 34513, 3)),
 
        DARKLIGHT(new Cosmetic("Darklight", 34515, 3)),
 
        BEACH_STICK_OF_ROCK(new Cosmetic("Beach Stick of Rock", 35083, 3)),
 
        KING_RADDALLIN_SWORD(new Cosmetic("King Raddallin Sword", 35939, 3)),
 
        SWORD_2001(new Cosmetic("2001 Sword", 36130, 3)),
 
        SWORD_2008(new Cosmetic("2008 Sword", 36132, 3)),
 
        SWORD_2011(new Cosmetic("2011 Sword", 36134, 3)),
 
        SWORD_2014(new Cosmetic("2014 Sword", 36136, 3)),
 
        VANNAKAS_SWORD(new Cosmetic("Vannaka's Sword", 36253, 3)),
 
        SHARD_OF_HAVOC(new Cosmetic("Shard of Havoc", 36822, 3)),
 
        DRAGON_RIDER_BLADE(new Cosmetic("Dragon Rider Blade", 37115, 3)),
 
        MANTICORE_SWORD(new Cosmetic("Manticore Sword", 37657, 3)),
 
        ICE_SWORD(new Cosmetic("Ice Sword", 39318, 3)),
 
        ANGER_SWORD(new Cosmetic("Anger Sword", 41219, 3)),
 
        CRESCENT_BLADE(new Cosmetic("Crescent Blade", 41614, 3)),
 
        PURIFIED_SWORD(new Cosmetic("Purified Sword", 41732, 3)),
 
        THUNDERS_EDGE(new Cosmetic("Thunder's Edge", 42130, 3)),
 
        IWI_SWORD(new Cosmetic("Iwi Sword", 42510, 3)),
 
        NATURES_BALANCE(new Cosmetic("Nature's Balance", 42522, 3)),
 
        OCEANS_WARRIOR_SWORD(new Cosmetic("Ocean's Warrior Sword", 42712, 3)),
 
        DEFENDERS_BLADE(new Cosmetic("Defender's Blade", 42698, 3)),
 
        UMBRAL_FLAMEBLADE_SWORD(new Cosmetic("Umbral Flameblade Sword", 43271, 3)),
 
        HUGINN_AND_MUNINN(new Cosmetic("Huginn and Muninn", 44192, 3)),
 
        WILDERNESS_SWORD_1(new Cosmetic("Wilderness sword 1", 37904, 3)),
 
        WILDERNESS_SWORD_2(new Cosmetic("Wilderness sword 2", 37905, 3)),
 
        WILDERNESS_SWORD_3(new Cosmetic("Wilderness sword 3", 37906, 3)),
 
        WILDERNESS_SWORD_4(new Cosmetic("Wilderness sword 4", 37907, 3)),
 
        JORMUNGAND_SWORD(new Cosmetic("Jormungand Sword", 47331, 3)),
 
        BRUTISH_SCIMITAR(new Cosmetic("Brutish Scimitar", 48149, 3)),
 
        BRUTAL_MACE(new Cosmetic("Brutal Mace", 26011, 3)),
 
        SHADOW_LINZA_HAMMER(new Cosmetic("Shadow Linza Hammer", 31115, 3)),
 
        BEACH_SPADE(new Cosmetic("Beach Spade", 34935, 3)),
 
        BARBARIAN_WARHAMMER(new Cosmetic("Barbarian Warhammer", 35375, 3)),
 
        MAZCAB_CUDGEL(new Cosmetic("Mazcab Cudgel", 35876, 3)),
 
        TURKEY_DRUMSTICK(new Cosmetic("Turkey Drumstick", 36077, 3)),
 
        ORNATE_MACE(new Cosmetic("Ornate Mace", 37294, 3)),
 
        MANTICORE_MACE(new Cosmetic("Manticore Mace", 37653, 3)),
 
        FAE_FAIRY_MACE(new Cosmetic("Fae Fairy Mace", 38415, 3)),
 
        ZOMBIE_HAND_FLAIL(new Cosmetic("Zombie Hand Flail", 38980, 3)),
 
        FAYRE_HOOKADUCK_FLAIL(new Cosmetic("Fayre Hook-a-Duck Flail", 42412, 3)),
 
        MACE_OF_CLUBS(new Cosmetic("Mace of Clubs", 42877, 3)),
 
        BEACH_SANDY_CLUB(new Cosmetic("Beach Sandy Club", 43309, 3)),
 
        BRUTISH_MACE(new Cosmetic("Brutish Mace", 48151, 3)),
 
        BRUTISH_MAUL(new Cosmetic("Brutish Maul", 48155, 3)),
 
        SCORCHING_AXE(new Cosmetic("Scorching Axe", 24900, 3)),
 
        ORNATE_BATTLEAXE(new Cosmetic("Ornate Battleaxe", 37303, 3)),
 
        FAE_FAIRY_BATTLEAXE(new Cosmetic("Fae Fairy Battleaxe", 38424, 3)),
 
        URAEUS(new Cosmetic("Uraeus", 40214, 3)),
 
        CHICKAXE(new Cosmetic("Chick-Axe", 47795, 3)),
 
        PARASOL_2H_SWORD(new Cosmetic("Parasol 2h Sword", 24824, 3)),
 
        BLAZING_FLAMBERGE(new Cosmetic("Blazing Flamberge", 24886, 3)),
 
        SIR_OWEN_GREATSWORD(new Cosmetic("Sir Owen Greatsword", 28987, 3)),
 
        SWORD_OF_EDICTS(new Cosmetic("Sword of Edicts", 27597, 3)),
 
        CIRCUS_AUDIENCE_GIANTS_HAND(new Cosmetic("Circus (Audience) Giant's Hand", 30949, 3)),
 
        SHADOW_SIR_OWEN_GREATSWORD(new Cosmetic("Shadow Sir Owen Greatsword", 31126, 3)),
 
        REPLICA_GWD_ARMADYL_GODSWORD(new Cosmetic("Replica GWD (Armadyl) Godsword", 31241, 3)),
 
        REPLICA_GWD_BANDOS_GODSWORD(new Cosmetic("Replica GWD (Bandos) Godsword", 31240, 3)),
 
        REPLICA_GWD_SARADOMIN_GODSWORD(new Cosmetic("Replica GWD (Saradomin) Godsword", 31242, 3)),
 
        REPLICA_GWD_ZAMORAK_GODSWORD(new Cosmetic("Replica GWD (Zamorak) Godsword", 31243, 3)),
 
        SAMURAI_KATANA_OWARI(new Cosmetic("Samurai Katana 'Owari'", 33634, 3)),
 
        NEFARIOUS_EDGE(new Cosmetic("Nefarious Edge", 33747, 3)),
 
        ARRAVS_SWORD(new Cosmetic("Arrav's Sword", 34333, 3)),
 
        VITALITY_2H_SWORD(new Cosmetic("Vitality 2h Sword", 34944, 3)),
 
        VITALITY_INACTIVE_2H_SWORD(new Cosmetic("Vitality (Inactive) 2h Sword", 34954, 3)),
 
        KING_RADDALLIN_2H_SWORD(new Cosmetic("King Raddallin 2h Sword", 35937, 3)),
 
        KING_RADDALLIN_2H_SWORD_AND_STANCE(new Cosmetic("King Raddallin 2h Sword and Stance", 35938, 3)),
 
        ORNATE_2H_SWORD(new Cosmetic("Ornate 2h Sword", 37300, 3)),
 
        BRASSICA_PRIME_GODSWORD(new Cosmetic("Brassica Prime Godsword", 37602, 3)),
 
        MANTICORE_2H_SWORD(new Cosmetic("Manticore 2h Sword", 37659, 3)),
 
        FAE_FAIRY_2H_SWORD(new Cosmetic("Fae Fairy 2h Sword", 38421, 3)),
 
        SHARD_OF_CHAOS(new Cosmetic("Shard of Chaos", 38995, 3)),
 
        NAUTILUS_2H_SWORD(new Cosmetic("Nautilus 2h Sword", 39174, 3)),
 
        HELLION_2H_BLADE(new Cosmetic("Hellion 2h Blade", 41573, 3)),
 
        PURIFIED_GREATSWORD(new Cosmetic("Purified Greatsword", 41734, 3)),
 
        THUNDERS_GREATBLADE(new Cosmetic("Thunder's Greatblade", 42129, 3)),
 
        OCEANS_WARRIOR_SWORD_2H(new Cosmetic("Ocean's Warrior Sword (2h)", 42711, 3)),
 
        DEFENDERS_GREATBLADE(new Cosmetic("Defender's Greatblade", 42700, 3)),
 
        TIDAL_KHOPESH(new Cosmetic("Tidal Khopesh", 42914, 3)),
 
        UMBRAL_2H_FLAMEBLADE_SWORD(new Cosmetic("Umbral 2h Flameblade Sword", 43273, 3)),
 
        BEACH_SANDY_2H_SWORD(new Cosmetic("Beach Sandy 2h Sword", 43311, 3)),
 
        BEACH_SHOVEL_SWORD_2H(new Cosmetic("Beach Shovel Sword (2h)", 43297, 3)),
 
        BLACK_NECRONIUM_TWO_HANDED_GREATAXE(new Cosmetic("Black Necronium Two Handed Greataxe", 44443, 3)),
 
        JORMUNGAND_SWORD_2H(new Cosmetic("Jormungand Sword 2h", 47335, 3)),
 
        CARROT_LANCE(new Cosmetic("Carrot lance", 47750, 3)),
 
        BRUTISH_2H_SWORD(new Cosmetic("Brutish 2h Sword", 48154, 3)),
 
        EXECUTIONER_AXE(new Cosmetic("Executioner Axe", 28011, 3)),
 
        ICYENIC_GREATHAMMER(new Cosmetic("Icyenic Greathammer", 28739, 3)),
 
        INFERNAL_GREATHAMMER(new Cosmetic("Infernal Greathammer", 28740, 3)),
 
        GOLDEN_SCYTHE(new Cosmetic("Golden Scythe", 29946, 3)),
 
        KYZAJ_BLOODIED(new Cosmetic("Kyzaj (Bloodied)", 31519, 3)),
 
        KYZAJ_HONOURABLE(new Cosmetic("Kyzaj (Honourable)", 31520, 3)),
 
        EGG_ON_A_FORK(new Cosmetic("Egg on a Fork", 34503, 3)),
 
        CAULDRON_MAUL(new Cosmetic("Cauldron Maul", 35954, 3)),
 
        ORNATE_HALBERD(new Cosmetic("Ornate Halberd", 37301, 3)),
 
        ORNATE_MAUL(new Cosmetic("Ornate Maul", 37302, 3)),
 
        YAK_SCYTHE(new Cosmetic("Yak Scythe", 37485, 3)),
 
        MANTICORE_MAUL(new Cosmetic("Manticore Maul", 37661, 3)),
 
        MANTICORE_HALBERD(new Cosmetic("Manticore Halberd", 37660, 3)),
 
        CRYSTAL_PEACOCK_BATTLEAXE(new Cosmetic("Crystal Peacock Battleaxe", 38443, 3)),
 
        FAE_FAIRY_HALBERD(new Cosmetic("Fae Fairy Halberd", 38422, 3)),
 
        FAE_FAIRY_MAUL(new Cosmetic("Fae Fairy Maul", 38423, 3)),
 
        LUNARFURY_TIER_1_MAUL(new Cosmetic("Lunarfury (Tier 1) Maul", 39881, 3)),
 
        LUNARFURY_TIER_2_MAUL(new Cosmetic("Lunarfury (Tier 2) Maul", 39882, 3)),
 
        FAYRE_MALLET(new Cosmetic("Fayre Mallet", 40003, 3)),
 
        ANGER_BATTLEAXE(new Cosmetic("Anger Battleaxe", 41220, 3)),
 
        ANGER_MAUL(new Cosmetic("Anger Maul", 41221, 3)),
 
        RIP_AXE(new Cosmetic("RIP Axe", 41530, 3)),
 
        PURIFIED_HALBERD(new Cosmetic("Purified Halberd", 41739, 3)),
 
        RAINBOW_SCYTHE(new Cosmetic("Rainbow Scythe", 42444, 3)),
 
        CRYPT_SCYTHE(new Cosmetic("Crypt Scythe", 42944, 3)),
 
        BEACH_SANDY_MAUL(new Cosmetic("Beach Sandy Maul", 43312, 3)),
 
        WARS_GREATAXE(new Cosmetic("War's Greataxe", 44212, 3)),
 
        SHIPWRECKER_TRIDENT(new Cosmetic("Shipwrecker Trident", 24902, 3)),
 
        JOUSTING_LANCE_SPEAR(new Cosmetic("Jousting Lance (Spear)", 25110, 3)),
 
        VALKYRIE_SPEAR(new Cosmetic("Valkyrie Spear", 30614, 3)),
 
        TUSKA_SARADOMIN_SPEAR(new Cosmetic("Tuska (Saradomin) Spear", 34878, 3)),
 
        TUSKA_ARMADYL_SPEAR(new Cosmetic("Tuska (Armadyl) Spear", 34879, 3)),
 
        TUSKA_ZAMORAK_SPEAR(new Cosmetic("Tuska (Zamorak) Spear", 34880, 3)),
 
        TUSKA_GODLESS_SPEAR(new Cosmetic("Tuska (Godless) Spear", 34881, 3)),
 
        DEATH_LOTUS_GLAIVE(new Cosmetic("Death Lotus Glaive", 35313, 3)),
 
        SUNSPEAR_MELEE(new Cosmetic("Sunspear (Melee)", 37263, 3)),
 
        ANGER_SPEAR(new Cosmetic("Anger Spear", 41222, 3)),
 
        PIKE_OF_SPADES(new Cosmetic("Pike of Spades", 42233, 3)),
 
        VANQUISH_MELEE(new Cosmetic("Vanquish (Melee)", 44176, 3)),
 
        BRUTISH_HALBERD(new Cosmetic("Brutish Halberd", 48153, 3)),
 
        DAGGERFIST_CLAW(new Cosmetic("Daggerfist Claw", 24898, 3)),
 
        BRUTAL_CLAW(new Cosmetic("Brutal Claw", 26007, 3)),
 
        CRAB_CLAW(new Cosmetic("Crab Claw", 29466, 3)),
 
        SUPERHERO_CLAW(new Cosmetic("Superhero Claw", 29425, 3)),
 
        AVIANSIE_CLAW(new Cosmetic("Aviansie Claw", 30337, 3)),
 
        ORKISH_CLAW(new Cosmetic("Orkish Claw", 30339, 3)),
 
        PROTO_PACK_CLAW(new Cosmetic("Proto Pack Claw", 32396, 3)),
 
        HELWYR_CLAW(new Cosmetic("Helwyr Claw", 37117, 3)),
 
        BEACH_CLAWDIA_CLAW(new Cosmetic("Beach Clawdia Claw", 37999, 3)),
 
        FAE_FAIRY_CLAW(new Cosmetic("Fae Fairy Claw", 38426, 3)),
 
        STEIN(new Cosmetic("Stein", 41159, 3)),
 
        EVENTIDE_RIPPER(new Cosmetic("Eventide Ripper", 41617, 3)),
 
        CLAW_OF_DIAMONDS(new Cosmetic("Claw of Diamonds", 42858, 3)),
 
        RAZOR_WHIP(new Cosmetic("Razor Whip", 24892, 3)),
 
        FLAMING_LASH(new Cosmetic("Flaming Lash", 24894, 3)),
 
        BRUTAL_WHIP(new Cosmetic("Brutal Whip", 26005, 3)),
 
        BEACH_BUNTING_WHIP(new Cosmetic("Beach Bunting Whip", 35082, 3)),
 
        ORNATE_WHIP(new Cosmetic("Ornate Whip", 37305, 3)),
 
        BEACH_CONGER_EEL_WHIP(new Cosmetic("Beach Conger Eel Whip", 37998, 3)),
 
        PANTHEON_CRONDIS_NEKHAKHA(new Cosmetic("Pantheon (Crondis) Nekhakha", 40210, 3)),
 
        WHIP_OF_HEARTS(new Cosmetic("Whip of Hearts", 40947, 3)),
 
        SHOCK_EYE_STAFF(new Cosmetic("Shock Eye Staff", 24577, 3)),
 
        ICYENIC_STAFF(new Cosmetic("Icyenic Staff", 28737, 3)),
 
        INFERNAL_STAFF(new Cosmetic("Infernal Staff", 28738, 3)),
 
        SHADOW_ARIANE_STAFF(new Cosmetic("Shadow Ariane Staff", 31133, 3)),
 
        THE_BURNING_TRUTH(new Cosmetic("The Burning Truth", 31357, 3)),
 
        NEFARIOUS_SPIRE(new Cosmetic("Nefarious Spire", 33749, 3)),
 
        NOMAD_STAFF(new Cosmetic("Nomad Staff", 34106, 3)),
 
        VITALITY_STAFF(new Cosmetic("Vitality Staff", 34945, 3)),
 
        VITALITY_INACTIVE_STAFF(new Cosmetic("Vitality (Inactive) Staff", 34955, 3)),
 
        DEATH_LOTUS_DRAGON_STAFF(new Cosmetic("Death Lotus Dragon Staff", 35316, 3)),
 
        BROOM_STAFF(new Cosmetic("Broom Staff", 35953, 3)),
 
        ORNATE_STAFF(new Cosmetic("Ornate Staff", 37309, 3)),
 
        MANTICORE_STAFF(new Cosmetic("Manticore Staff", 37665, 3)),
 
        CRYSTAL_PEACOCK_STAFF(new Cosmetic("Crystal Peacock Staff", 38444, 3)),
 
        FAE_FAIRY_STAFF(new Cosmetic("Fae Fairy Staff", 38431, 3)),
 
        SHARD_OF_SUFFERING(new Cosmetic("Shard of Suffering", 38997, 3)),
 
        PRIVATEER_SERPENT_SCEPTRE(new Cosmetic("Privateer Serpent Sceptre", 38864, 3)),
 
        ICE_STAFF(new Cosmetic("Ice Staff", 39320, 3)),
 
        SUNSPEAR_MAGIC(new Cosmetic("Sunspear (Magic)", 37261, 3)),
 
        MYSTICAL_STAFF(new Cosmetic("Mystical Staff", 39860, 3)),
 
        LUNARFURY_TIER_1_STAFF(new Cosmetic("Lunarfury (Tier 1) Staff", 39885, 3)),
 
        LUNARFURY_TIER_2_STAFF(new Cosmetic("Lunarfury (Tier 2) Staff", 39886, 3)),
 
        PHARAOH_SCEPTRE(new Cosmetic("Pharaoh Sceptre", 40216, 3)),
 
        PURIFIED_STAFF(new Cosmetic("Purified Staff", 41738, 3)),
 
        STORM_SCEPTRE(new Cosmetic("Storm Sceptre", 42132, 3)),
 
        STAFF_OF_SPADES(new Cosmetic("Staff of Spades", 42234, 3)),
 
        IWI_STAFF(new Cosmetic("Iwi Staff", 42513, 3)),
 
        OCEANS_MAGE_STAFF(new Cosmetic("Ocean's Mage Staff", 42719, 3)),
 
        TIDAL_STAFF(new Cosmetic("Tidal Staff", 42915, 3)),
 
        CRYPT_STAFF(new Cosmetic("Crypt Staff", 42945, 3)),
 
        BEACH_BUCKET_STAFF(new Cosmetic("Beach Bucket staff", 43298, 3)),
 
        VANQUISH_MAGIC(new Cosmetic("Vanquish (Magic)", 44177, 3)),
 
        STAFF_OF_FAMINE(new Cosmetic("Staff of Famine", 44213, 3)),
 
        LUCIENS_STAFF(new Cosmetic("Lucien's staff", 44215, 3)),
 
        PLAGUE_DOCTOR_LANCING_CANE(new Cosmetic("Plague Doctor lancing cane", 44361, 3)),
 
        MERETHIELS_STAVE(new Cosmetic("Merethiel's stave", 42769, 3)),
 
        VALKYRIE_STAFF(new Cosmetic("Valkyrie Staff", 44468, 3)),
 
        JORMUNGAND_STAFF(new Cosmetic("Jormungand Staff", 47330, 3)),
 
        SHADOW_GEM_STAFF(new Cosmetic("Shadow Gem Staff", 47361, 3)),
 
        THE_RISEN_SHADOW_SCREAM_STAFF(new Cosmetic("The Risen Shadow Scream Staff", 47733, 3)),
 
        UNTAMED_STAFF(new Cosmetic("Untamed Staff", 48156, 3)),
 
        SHATTERSTORM_WAND(new Cosmetic("Shatterstorm Wand", 24890, 3)),
 
        AVIANSIE_WAND(new Cosmetic("Aviansie Wand", 30345, 3)),
 
        ORKISH_WAND(new Cosmetic("Orkish Wand", 30346, 3)),
 
        SPITEFUL_SPARK(new Cosmetic("Spiteful Spark", 31359, 3)),
 
        REVENANT_WAND(new Cosmetic("Revenant Wand", 35859, 3)),
 
        SHARD_OF_ENERGY(new Cosmetic("Shard of Energy", 36826, 3)),
 
        ORNATE_WAND(new Cosmetic("Ornate Wand", 37308, 3)),
 
        MANTICORE_WAND(new Cosmetic("Manticore Wand", 37664, 3)),
 
        BEACH_ICE_LOLLY_WAND(new Cosmetic("Beach Ice Lolly Wand", 37996, 3)),
 
        CRYSTAL_PEACOCK_WAND(new Cosmetic("Crystal Peacock Wand", 38445, 3)),
 
        FAE_FAIRY_WAND(new Cosmetic("Fae Fairy Wand", 38430, 3)),
 
        ZOMBIE_BONE_WAND(new Cosmetic("Zombie Bone Wand", 38982, 3)),
 
        WAND_OF_HEARTS(new Cosmetic("Wand of Hearts", 40945, 3)),
 
        RAINBOW_WAND(new Cosmetic("Rainbow Wand", 41402, 3)),
 
        PURIFIED_WAND(new Cosmetic("Purified Wand", 41736, 3)),
 
        WAND_OF_DIAMONDS(new Cosmetic("Wand of Diamonds", 42857, 3)),
 
        WAND_OF_CLUBS(new Cosmetic("Wand of Clubs", 42873, 3)),
 
        OCEANS_MAGE_WAND(new Cosmetic("Ocean's Mage Wand", 42720, 3)),
 
        WAND_OF_THE_MIND(new Cosmetic("Wand of the Mind", 42701, 3)),
 
        UNTAMED_WAND(new Cosmetic("Untamed Wand", 48157, 3)),
 
        FIREBRAND_BOW(new Cosmetic("Firebrand Bow", 24888, 3)),
 
        BARBED_BOW(new Cosmetic("Barbed Bow", 27614, 3)),
 
        ICYENIC_BOW(new Cosmetic("Icyenic Bow", 28741, 3)),
 
        INFERNAL_BOW(new Cosmetic("Infernal Bow", 28742, 3)),
 
        SHADOW_OZAN_BOW(new Cosmetic("Shadow Ozan Bow", 31140, 3)),
 
        NEFARIOUS_REACH(new Cosmetic("Nefarious Reach", 33751, 3)),
 
        ORNATE_SHORTBOW(new Cosmetic("Ornate Shortbow", 37313, 3)),
 
        ORNATE_SHIELDBOW(new Cosmetic("Ornate Shieldbow", 37314, 3)),
 
        MANTICORE_SHORTBOW(new Cosmetic("Manticore Shortbow", 37669, 3)),
 
        MANTICORE_SHIELDBOW(new Cosmetic("Manticore Shieldbow", 37670, 3)),
 
        CRYSTAL_PEACOCK_SHORTBOW(new Cosmetic("Crystal Peacock Shortbow", 38447, 3)),
 
        FAE_FAIRY_SHORTBOW(new Cosmetic("Fae Fairy Shortbow", 38435, 3)),
 
        FAE_FAIRY_SHIELDBOW(new Cosmetic("Fae Fairy Shieldbow", 38436, 3)),
 
        ZOMBIE_SPINE_BOW(new Cosmetic("Zombie Spine Bow", 38981, 3)),
 
        SHARD_OF_DESPITE(new Cosmetic("Shard of Despite", 38996, 3)),
 
        ICE_BOW(new Cosmetic("Ice Bow", 39317, 3)),
 
        LONGBOW_OF_HEARTS(new Cosmetic("Longbow of Hearts", 40946, 3)),
 
        RAINBOW_BOW(new Cosmetic("Rainbow Bow", 40993, 3)),
 
        DUSK_WARBOW(new Cosmetic("Dusk Warbow", 41616, 3)),
 
        PURIFIED_SHORTBOW(new Cosmetic("Purified Shortbow", 41735, 3)),
 
        STORM_STRIKE(new Cosmetic("Storm Strike", 42133, 3)),
 
        LONGBOW_OF_SPADES(new Cosmetic("Longbow of Spades", 42235, 3)),
 
        LONGBOW_OF_DIAMONDS(new Cosmetic("Longbow of Diamonds", 42860, 3)),
 
        LONGBOW_OF_CLUBS(new Cosmetic("Longbow of Clubs", 42876, 3)),
 
        TIDAL_SHIELDBOW(new Cosmetic("Tidal Shieldbow", 42913, 3)),
 
        CRYPT_SHIELDBOW(new Cosmetic("Crypt Shieldbow", 42946, 3)),
 
        PESTILENCES_BOW(new Cosmetic("Pestilence's Bow", 44214, 3)),
 
        SHADOW_GEM_BOW(new Cosmetic("Shadow Gem Bow", 47362, 3)),
 
        THE_RISEN_SOUL_PIERCER_BOW(new Cosmetic("The Risen Soul Piercer Bow", 47734, 3)),
 
        FERAL_SHORTBOW(new Cosmetic("Feral Shortbow", 48160, 3)),
 
        FERAL_SHIELDBOW(new Cosmetic("Feral Shieldbow", 48165, 3)),
 
        QUICKFIRE_CROSSBOW(new Cosmetic("Quick-Fire Crossbow", 24575, 3)),
 
        DWARVEN_WARSUIT_CROSSBOW(new Cosmetic("Dwarven Warsuit Crossbow", 25285, 3)),
 
        BRUTAL_CROSSBOW(new Cosmetic("Brutal Crossbow", 26001, 3)),
 
        PROTO_PACK_CROSSBOW(new Cosmetic("Proto Pack Crossbow", 32392, 3)),
 
        SPIRIT_HUNTER_CROSSBOW(new Cosmetic("Spirit Hunter Crossbow", 35848, 3)),
 
        SHARD_OF_MALICE(new Cosmetic("Shard of Malice", 36824, 3)),
 
        ENERGISED_ARM_CANNON(new Cosmetic("Energised Arm Cannon", 36942, 3)),
 
        MANTICORE_CROSSBOW(new Cosmetic("Manticore Crossbow", 37671, 3)),
 
        BEACH_INK_SHOOTER(new Cosmetic("Beach Ink Shooter", 38002, 3)),
 
        OCEANS_ARCHER_CROSSBOW(new Cosmetic("Ocean's Archer Crossbow", 41603, 3)),
 
        PURIFIED_CROSSBOW(new Cosmetic("Purified Crossbow", 41740, 3)),
 
        JORMUNGAND_BOW(new Cosmetic("Jormungand Bow", 47329, 3)),
 
        FERAL_CROSSBOW(new Cosmetic("Feral Crossbow", 48161, 3)),
 
        WILDFIRE(new Cosmetic("Wildfire", 31369, 3)),
 
        VITALITY_CROSSBOW(new Cosmetic("Vitality Crossbow", 34946, 3)),
 
        VITALITY_INACTIVE_CROSSBOW(new Cosmetic("Vitality (Inactive) Crossbow", 34956, 3)),
 
        PUMPKIN_LAUNCHER(new Cosmetic("Pumpkin Launcher", 35955, 3)),
 
        VAMPYRE_HUNTER_STAKE_LAUNCHER(new Cosmetic("Vampyre Hunter Stake Launcher", 37210, 3)),
 
        ORNATE_CROSSBOW(new Cosmetic("Ornate Crossbow", 37315, 3)),
 
        ORNATE_2H_CROSSBOW(new Cosmetic("Ornate 2h Crossbow", 37312, 3)),
 
        MANTICORE_2H_CROSSBOW(new Cosmetic("Manticore 2h Crossbow", 37668, 3)),
 
        BEACH_WATER_BALLOON_LAUNCHER(new Cosmetic("Beach Water Balloon Launcher", 37997, 3)),
 
        FAE_FAIRY_CROSSBOW(new Cosmetic("Fae Fairy Crossbow", 38437, 3)),
 
        FAE_FAIRY_2H_CROSSBOW(new Cosmetic("Fae Fairy 2h Crossbow", 38434, 3)),
 
        PRIVATEER_REPEATER_CROSSBOW(new Cosmetic("Privateer Repeater Crossbow", 38863, 3)),
 
        NAUTILUS_2H_CROSSBOW(new Cosmetic("Nautilus 2h Crossbow", 39175, 3)),
 
        PURIFIED_2H_CROSSBOW(new Cosmetic("Purified 2h Crossbow", 41742, 3)),
 
        BEACH_PUFFERFISH_LAUNCHER(new Cosmetic("Beach Pufferfish Launcher", 43299, 3)),
 
        VANQUISH_RANGED(new Cosmetic("Vanquish (Ranged)", 44178, 3)),
 
        VALKYRIE_2H_CROSSBOW(new Cosmetic("Valkyrie 2H Crossbow", 44471, 3)),
 
        SLIME_LAUNCHER(new Cosmetic("Slime Launcher", 44487, 3)),
 
        FERAL_2H_CROSSBOW(new Cosmetic("Feral 2h Crossbow", 48168, 3)),
 
        SUPERHERO_LIGHTNING(new Cosmetic("Superhero Lightning", 29428, 3)),
 
        AVIANSIE_THROWING_STAR(new Cosmetic("Aviansie Throwing Star", 30341, 3)),
 
        ORKISH_THROWING_AXE(new Cosmetic("Orkish Throwing Axe", 30343, 3)),
 
        SUNFLARE_THROWING_AXE(new Cosmetic("Sunflare Throwing Axe", 31366, 3)),
 
        NOMAD_JAVELIN(new Cosmetic("Nomad Javelin", 34107, 3)),
 
        BEACH_THROWING_STARFISH(new Cosmetic("Beach Throwing Starfish", 35085, 3)),
 
        DEATH_LOTUS_ACID_FLASK(new Cosmetic("Death Lotus Acid Flask", 35314, 3)),
 
        ORNATE_THROWING_STAR(new Cosmetic("Ornate Throwing Star", 37310, 3)),
 
        MANTICORE_THROWING_AXE(new Cosmetic("Manticore Throwing Axe", 37666, 3)),
 
        OAR(new Cosmetic("Oar", 37723, 3)),
 
        CRYSTAL_PEACOCK_THROWING_STAR(new Cosmetic("Crystal Peacock Throwing Star", 38448, 3)),
 
        FAE_FAIRY_THROWING_STAR(new Cosmetic("Fae Fairy Throwing Star", 38432, 3)),
 
        SUNSPEAR_RANGED(new Cosmetic("Sunspear (Ranged)", 37265, 3)),
 
        PRESENT_HAMMER(new Cosmetic("Present Hammer", 41524, 3)),
 
        RABID_JACK_SWORD(new Cosmetic("Rabid Jack sword", 42349, 3)),
 
        IWI_JAVELIN(new Cosmetic("Iwi Javelin", 42514, 3)),
 
        FERAL_THROWING_KNIFE(new Cosmetic("Feral Throwing Knife", 48163, 3)),
 
        FERAL_THROWING_AXE(new Cosmetic("Feral Throwing Axe", 48166, 3)),
 
        REPLICA_DRAGON_CHAINBODY(new Cosmetic("Replica Dragon Chainbody", 30635, 4)),
 
        GAMEBLAST_2014_TUNIC(new Cosmetic("GameBlast 2014 Tunic", 30887, 4)),
 
        CIRCUS_FIREMAKER_TABARD(new Cosmetic("Circus (Firemaker) Tabard", 30988, 4)),
 
        ABLEGAMERS_TUNIC(new Cosmetic("AbleGamers Tunic", 32527, 4)),
 
        YOUNGMINDS_TUNIC(new Cosmetic("YoungMinds Tunic", 32529, 4)),
 
        DONATEGAMES_TUNIC(new Cosmetic("DonateGames Tunic", 32531, 4)),
 
        TUNIC_15TH_ANNIVERSARY(new Cosmetic("15th Anniversary Tunic", 36267, 4)),
 
        GAMEBLAST_2018_TUNIC(new Cosmetic("GameBlast 2018 Tunic", 41778, 4)),
 
        VARROCK_ARMOUR_1(new Cosmetic("Varrock armour 1", 42485, 4)),
 
        VARROCK_ARMOUR_2(new Cosmetic("Varrock armour 2", 42486, 4)),
 
        VARROCK_ARMOUR_3(new Cosmetic("Varrock armour 3", 42487, 4)),
 
        VARROCK_ARMOUR_4(new Cosmetic("Varrock armour 4", 42488, 4)),
 
        PLATEBODY_OF_TRIALS(new Cosmetic("Platebody of Trials", 44164, 4)),
 
        CHRISTMAS_JUMPER_PENGUIN(new Cosmetic("Christmas jumper (Penguin)", 44530, 4)),
 
        CHRISTMAS_JUMPER_CHINCHOMPA(new Cosmetic("Christmas jumper (Chinchompa)", 44532, 4)),
 
        CHRISTMAS_JUMPER_GUTHIX(new Cosmetic("Christmas jumper (Guthix)", 44534, 4)),
 
        CHRISTMAS_JUMPER_KBD(new Cosmetic("Christmas jumper (KBD)", 44536, 4)),
 
        SEABORNE_OFFHAND_DAGGER(new Cosmetic("Seaborne Off-hand Dagger", 26037, 5)),
 
        JOUSTING_LANCE_OFFHAND_RAPIER(new Cosmetic("Jousting Lance (Off-hand Rapier)", 26031, 5)),
 
        BRUTAL_OFFHAND_RAPIER(new Cosmetic("Brutal Off-hand Rapier", 26017, 5)),
 
        SIR_OWEN_OFFHAND_SHORTSWORD(new Cosmetic("Sir Owen Off-hand Shortsword", 28985, 5)),
 
        SHADOW_SIR_OWEN_OFFHAND_SHORTSWORD(new Cosmetic("Shadow Sir Owen Off-hand Shortsword", 31125, 5)),
 
        PROTO_PACK_OFFHAND_DAGGER(new Cosmetic("Proto Pack Off-hand Dagger", 32402, 5)),
 
        DEATH_LOTUS_OFFHAND_SAI(new Cosmetic("Death Lotus Off-hand Sai", 35312, 5)),
 
        MAZCAB_OFFHAND_POKER(new Cosmetic("Mazcab Off-hand Poker", 35875, 5)),
 
        SHADOW_GLAIVE_OFFHAND_DAGGER(new Cosmetic("Shadow Glaive Off-hand Dagger", 37114, 5)),
 
        ORNATE_OFFHAND_DAGGER(new Cosmetic("Ornate Off-hand Dagger", 37297, 5)),
 
        ORNATE_OFFHAND_SCIMITAR(new Cosmetic("Ornate Off-hand Scimitar", 37299, 5)),
 
        MANTICORE_OFFHAND_DAGGER(new Cosmetic("Manticore Off-hand Dagger", 37656, 5)),
 
        BEACH_OFFHAND_CORAL_SWORD(new Cosmetic("Beach Off-hand Coral Sword", 38006, 5)),
 
        BEACH_OFFHAND_CORAL_DAGGER(new Cosmetic("Beach Off-hand Coral Dagger", 38008, 5)),
 
        CRYSTAL_PEACOCK_OFFHAND_SWORD(new Cosmetic("Crystal Peacock Off-hand Sword", 38442, 5)),
 
        FAE_FAIRY_OFFHAND_DAGGER(new Cosmetic("Fae Fairy Off-hand Dagger", 38418, 5)),
 
        FAE_FAIRY_OFFHAND_SHORTSWORD(new Cosmetic("Fae Fairy Off-hand Shortsword", 38420, 5)),
 
        SUNDOWN_DAGGER_OFFHAND_OFFHAND(new Cosmetic("Sundown Dagger Offhand Off-hand", 41620, 5)),
 
        TWISTED_JESTER_OFFHAND_DAGGER(new Cosmetic("Twisted Jester Off-hand Dagger", 42887, 5)),
 
        TIDAL_SHORTSWORD_OFFHAND(new Cosmetic("Tidal Shortsword Off-hand", 42897, 5)),
 
        PLAGUE_DOCTOR_OFFHAND_LEECHING_SYRINGE(new Cosmetic("Plague Doctor off-hand leeching syringe", 44360, 5)),
 
        VALKYRIE_SWORD_OH_OFFHAND(new Cosmetic("Valkyrie Sword OH Off-hand", 44470, 5)),
 
        MUNINN_BLADE_OFFHAND(new Cosmetic("Muninn Blade Off-hand", 47328, 5)),
 
        BRUTISH_OFFHAND_DAGGER(new Cosmetic("Brutish Off-hand Dagger", 48148, 5)),
 
        PACTBREAKER_OFFHAND_LONGSWORD(new Cosmetic("Pactbreaker Off-hand Longsword", 26025, 5)),
 
        DWARVEN_WARSUIT_OFFHAND_LONGSWORD(new Cosmetic("Dwarven Warsuit Off-hand Longsword", 25997, 5)),
 
        KRILS_OFFHAND_CLEAVER(new Cosmetic("K'ril's Off-hand Cleaver", 26023, 5)),
 
        BRUTAL_OFFHAND_LONGSWORD(new Cosmetic("Brutal Off-hand Longsword", 26021, 5)),
 
        PALADIN_OFFHAND_BLADE(new Cosmetic("Paladin Off-hand Blade", 26478, 5)),
 
        SIR_OWEN_OFFHAND_LONGSWORD(new Cosmetic("Sir Owen Off-hand Longsword", 28981, 5)),
 
        SHADOW_SIR_OWEN_OFFHAND_LONGSWORD(new Cosmetic("Shadow Sir Owen Off-hand Longsword", 31123, 5)),
 
        FIRESTORM_OFFHAND_BLADE(new Cosmetic("Firestorm Off-hand Blade", 31365, 5)),
 
        SILVERLIGHT_OFFHAND(new Cosmetic("Silverlight Off-hand", 34512, 5)),
 
        SILVERLIGHT_DYED_OFFHAND(new Cosmetic("Silverlight (Dyed) Off-hand", 34514, 5)),
 
        DARKLIGHT_OFFHAND(new Cosmetic("Darklight Off-hand", 34516, 5)),
 
        BEACH_OFFHAND_STICK_OF_ROCK(new Cosmetic("Beach Off-hand Stick of Rock", 35084, 5)),
 
        KING_RADDALLIN_OFFHAND_SWORD(new Cosmetic("King Raddallin Off-hand Sword", 35940, 5)),
 
        OFFHAND_SWORD_2001(new Cosmetic("2001 Off-hand Sword", 36131, 5)),
 
        OFFHAND_SWORD_2008(new Cosmetic("2008 Off-hand Sword", 36133, 5)),
 
        OFFHAND_SWORD_2011(new Cosmetic("2011 Off-hand Sword", 36135, 5)),
 
        OFFHAND_SWORD_2014(new Cosmetic("2014 Off-hand Sword", 36137, 5)),
 
        SHARD_OF_HAVOC_OFFHAND(new Cosmetic("Shard of Havoc Off-hand", 36823, 5)),
 
        DRAGON_RIDER_OFFHAND_BLADE(new Cosmetic("Dragon Rider Off-hand Blade", 37116, 5)),
 
        MANTICORE_OFFHAND_SWORD(new Cosmetic("Manticore Off-hand Sword", 37658, 5)),
 
        ICE_OFFHAND_SWORD(new Cosmetic("Ice Off-hand Sword", 39392, 5)),
 
        OFFHAND_HELLION_BLADE(new Cosmetic("Off-hand Hellion Blade", 41572, 5)),
 
        CRESCENT_BLADE_OFFHAND_OFFHAND(new Cosmetic("Crescent Blade Offhand Off-hand", 41615, 5)),
 
        PURIFIED_OFFHAND_SWORD(new Cosmetic("Purified off-hand Sword", 41733, 5)),
 
        THUNDERS_EDGE_OFFHAND(new Cosmetic("Thunder's Edge off-hand", 42131, 5)),
 
        IWI_SWORD_OFFHAND_OFFHAND(new Cosmetic("Iwi Sword offhand Off-hand", 42511, 5)),
 
        OFFHAND_NATURES_BALANCE(new Cosmetic("Off-hand Nature's Balance", 42523, 5)),
 
        RABID_JACK_SWORD_OFFHAND(new Cosmetic("Rabid Jack sword off-hand", 42350, 5)),
 
        DEFENDERS_BLADE_OFFHAND_OFFHAND(new Cosmetic("Defender's Blade Offhand Off-hand", 42699, 5)),
 
        OCEANS_WARRIOR_SWORD_OFFHAND(new Cosmetic("Ocean's Warrior Sword Off-hand", 42713, 5)),
 
        OFFHAND_UMBRAL_FLAMEBLADE_SWORD(new Cosmetic("Off-hand Umbral Flameblade Sword", 43272, 5)),
 
        OFFHAND_HUGINN_AND_MUNINN(new Cosmetic("Off-hand Huginn and Muninn", 44193, 5)),
 
        JORMUNGAND_SWORD_OFFHAND_OFFHAND(new Cosmetic("Jormungand Sword Offhand Off-hand", 47332, 5)),
 
        BRUTISH_OFFHAND_SCIMITAR(new Cosmetic("Brutish Off-hand Scimitar", 48150, 5)),
 
        BRUTAL_OFFHAND_MACE(new Cosmetic("Brutal Off-hand Mace", 26013, 5)),
 
        SHADOW_LINZA_OFFHAND_HAMMER(new Cosmetic("Shadow Linza Off-hand Hammer", 31116, 5)),
 
        BARBARIAN_OFFHAND_WARHAMMER(new Cosmetic("Barbarian Off-hand Warhammer", 35376, 5)),
 
        MAZCAB_OFFHAND_CUDGEL(new Cosmetic("Mazcab Off-hand Cudgel", 35877, 5)),
 
        TURKEY_DRUMSTICK_OFFHAND(new Cosmetic("Turkey Drumstick Off-hand", 36078, 5)),
 
        ORNATE_OFFHAND_MACE(new Cosmetic("Ornate Off-hand Mace", 37295, 5)),
 
        MANTICORE_OFFHAND_MACE(new Cosmetic("Manticore Off-hand Mace", 37654, 5)),
 
        FAE_FAIRY_OFFHAND_MACE(new Cosmetic("Fae Fairy Off-hand Mace", 38416, 5)),
 
        MACE_OF_CLUBS_OFFHAND(new Cosmetic("Mace of Clubs off-hand", 42878, 5)),
 
        BEACH_OFFHAND_SANDY_CLUB(new Cosmetic("Beach Off-hand Sandy Club", 43310, 5)),
 
        BRUTISH_OFFHAND_MACE(new Cosmetic("Brutish Off-hand Mace", 48152, 5)),
 
        SCORCHING_OFFHAND_AXE(new Cosmetic("Scorching Off-hand Axe", 26035, 5)),
 
        ORNATE_OFFHAND_BATTLEAXE(new Cosmetic("Ornate Off-hand Battleaxe", 37304, 5)),
 
        FAE_FAIRY_OFFHAND_BATTLEAXE(new Cosmetic("Fae Fairy Off-hand Battleaxe", 38425, 5)),
 
        DAGGERFIST_OFFHAND_CLAW(new Cosmetic("Daggerfist Off-hand Claw", 26033, 5)),
 
        BRUTAL_OFFHAND_CLAW(new Cosmetic("Brutal Off-hand Claw", 26009, 5)),
 
        CRAB_OFFHAND_CLAW(new Cosmetic("Crab Off-hand Claw", 29467, 5)),
 
        SUPERHERO_OFFHAND_CLAW(new Cosmetic("Superhero Off-hand Claw", 29426, 5)),
 
        AVIANSIE_OFFHAND_CLAW(new Cosmetic("Aviansie Off-hand Claw", 30338, 5)),
 
        ORKISH_OFFHAND_CLAW(new Cosmetic("Orkish Off-hand Claw", 30340, 5)),
 
        PROTO_PACK_OFFHAND_CLAW(new Cosmetic("Proto Pack Off-hand Claw", 32398, 5)),
 
        HELWYR_OFFHAND_CLAW(new Cosmetic("Helwyr Off-hand Claw", 37118, 5)),
 
        BEACH_OFFHAND_CLAWDIA_CLAW(new Cosmetic("Beach Off-hand Clawdia Claw", 38000, 5)),
 
        FAE_FAIRY_OFFHAND_CLAW(new Cosmetic("Fae Fairy Off-hand Claw", 38427, 5)),
 
        OFFHAND_STEIN(new Cosmetic("Off-hand Stein", 41160, 5)),
 
        PRESENT_OFFHAND_HAMMER_OFFHAND(new Cosmetic("Present Offhand Hammer Off-hand", 41525, 5)),
 
        EVENTIDE_RIPPER_OFFHAND_OFFHAND(new Cosmetic("Eventide Ripper Offhand Off-hand", 41618, 5)),
 
        CLAW_OF_DIAMONDS_OFFHAND(new Cosmetic("Claw of Diamonds off-hand", 42859, 5)),
 
        QUICKFIRE_OFFHAND_CROSSBOW(new Cosmetic("Quick-Fire Off-hand Crossbow", 26027, 5)),
 
        DWARVEN_WARSUIT_OFFHAND_CROSSBOW(new Cosmetic("Dwarven Warsuit Off-hand Crossbow", 25999, 5)),
 
        BRUTAL_OFFHAND_CROSSBOW(new Cosmetic("Brutal Off-hand Crossbow", 26003, 5)),
 
        PROTO_PACK_OFFHAND_CROSSBOW(new Cosmetic("Proto Pack Off-hand Crossbow", 32394, 5)),
 
        SPIRIT_HUNTER_OFFHAND_CROSSBOW(new Cosmetic("Spirit Hunter Off-hand Crossbow", 35849, 5)),
 
        SHARD_OF_MALICE_OFFHAND(new Cosmetic("Shard of Malice Off-hand", 36825, 5)),
 
        ENERGISED_OFFHAND_ARM_CANNON(new Cosmetic("Energised Off-hand Arm Cannon", 36943, 5)),
 
        ORNATE_OFFHAND_CROSSBOW(new Cosmetic("Ornate Off-hand Crossbow", 37316, 5)),
 
        MANTICORE_OFFHAND_CROSSBOW(new Cosmetic("Manticore Off-hand Crossbow", 37672, 5)),
 
        BEACH_OFFHAND_INK_SHOOTER(new Cosmetic("Beach Off-hand Ink Shooter", 38003, 5)),
 
        FAE_FAIRY_OFFHAND_CROSSBOW(new Cosmetic("Fae Fairy Off-hand Crossbow", 38438, 5)),
 
        OCEANS_ARCHER_OFFHAND_CROSSBOW(new Cosmetic("Ocean's Archer Off-hand Crossbow", 41604, 5)),
 
        PURIFIED_OFFHAND_CROSSBOW(new Cosmetic("Purified off-hand Crossbow", 41741, 5)),
 
        FERAL_OFFHAND_CROSSBOW(new Cosmetic("Feral Off-hand Crossbow", 48162, 5)),
 
        SUPERHERO_OFFHAND_LIGHTNING(new Cosmetic("Superhero Off-hand Lightning", 29429, 5)),
 
        AVIANSIE_OFFHAND_THROWING_STAR(new Cosmetic("Aviansie Off-hand Throwing Star", 30342, 5)),
 
        ORKISH_OFFHAND_THROWING_AXE(new Cosmetic("Orkish Off-hand Throwing Axe", 30344, 5)),
 
        SUNFLARE_OFFHAND_THROWING_AXE(new Cosmetic("Sunflare Off-hand Throwing Axe", 31368, 5)),
 
        BEACH_OFFHAND_THROWING_STARFISH(new Cosmetic("Beach Off-hand Throwing Starfish", 35086, 5)),
 
        DEATH_LOTUS_OFFHAND_ACID_FLASK(new Cosmetic("Death Lotus Off-hand Acid Flask", 35315, 5)),
 
        ORNATE_OFFHAND_THROWING_STAR(new Cosmetic("Ornate Off-hand Throwing Star", 37311, 5)),
 
        MANTICORE_OFFHAND_THROWING_AXE(new Cosmetic("Manticore Off-hand Throwing Axe", 37667, 5)),
 
        CRYSTAL_PEACOCK_OFFHAND_THROWING_STAR(new Cosmetic("Crystal Peacock Off-hand Throwing Star", 38449, 5)),
 
        FAE_FAIRY_OFFHAND_THROWING_STAR(new Cosmetic("Fae Fairy Off-hand Throwing Star", 38433, 5)),
 
        FERAL_OFFHAND_THROWING_KNIFE(new Cosmetic("Feral Off-hand Throwing Knife", 48164, 5)),
 
        FERAL_OFFHAND_THROWING_AXE(new Cosmetic("Feral Off-hand Throwing Axe", 48167, 5)),
 
        DEMONFLESH_BOOK_OFFHAND(new Cosmetic("Demonflesh Book Off-hand", 27144, 5)),
 
        HATEFUL_HEART_OFFHAND(new Cosmetic("Hateful Heart Off-hand", 31361, 5)),
 
        TEDDY_OFFHAND(new Cosmetic("Teddy Off-hand", 33522, 5)),
 
        NECROFELINOMICON_OFFHAND(new Cosmetic("Necrofelinomicon Off-hand", 34334, 5)),
 
        TUSKA_CALIBRATION_DEVICE_OFFHAND(new Cosmetic("Tuska Calibration Device Off-hand", 34850, 5)),
 
        REVENANT_ORB_OFFHAND(new Cosmetic("Revenant Orb Off-hand", 35860, 5)),
 
        SHARD_OF_FOCUS_OFFHAND(new Cosmetic("Shard of Focus Off-hand", 36827, 5)),
 
        DRACCLES_OFFHAND(new Cosmetic("Draccles Off-hand", 37232, 5)),
 
        ORNATE_ORB_OFFHAND(new Cosmetic("Ornate Orb Off-hand", 37306, 5)),
 
        ORNATE_BOOK_OFFHAND(new Cosmetic("Ornate Book Off-hand", 37307, 5)),
 
        MANTICORE_BOOK_OFFHAND(new Cosmetic("Manticore Book Off-hand", 37663, 5)),
 
        MANTICORE_ORB_OFFHAND(new Cosmetic("Manticore Orb Off-hand", 37662, 5)),
 
        BEACHBALL_ORB_OFFHAND(new Cosmetic("Beachball Orb Off-hand", 38004, 5)),
 
        CRYSTAL_PEACOCK_BOOK_OFFHAND(new Cosmetic("Crystal Peacock Book Off-hand", 38446, 5)),
 
        FAE_FAIRY_ORB_OFFHAND(new Cosmetic("Fae Fairy Orb Off-hand", 38428, 5)),
 
        FAE_FAIRY_BOOK_OFFHAND(new Cosmetic("Fae Fairy Book Off-hand", 38429, 5)),
 
        ZOMBIE_BRAIN_ORB_OFFHAND(new Cosmetic("Zombie Brain Orb Off-hand", 38983, 5)),
 
        FAYRE_CRYSTAL_BALL_OFFHAND(new Cosmetic("Fayre Crystal Ball Off-hand", 40002, 5)),
 
        PANTHEON_APMEKEN_ANKH_OFFHAND(new Cosmetic("Pantheon (Apmeken) Ankh Off-hand", 40206, 5)),
 
        PANTHEON_HET_HEKA_OFFHAND(new Cosmetic("Pantheon (Het) Heka Off-hand", 40212, 5)),
 
        ORB_OF_HEARTS_OFFHAND(new Cosmetic("Orb of Hearts Off-hand", 40944, 5)),
 
        GOD_BOOK_MAGIC_OFFHAND(new Cosmetic("God Book (Magic Off-hand)", 41029, 5)),
 
        ILLUMINATED_GOD_BOOK_MAGIC_OFFHAND(new Cosmetic("Illuminated God Book (Magic Off-hand)", 41030, 5)),
 
        GOD_BOOK_SHIELD_OFFHAND(new Cosmetic("God Book (Shield) Off-hand", 41031, 5)),
 
        ILLUMINATED_GOD_BOOK_SHIELD_OFFHAND(new Cosmetic("Illuminated God Book (Shield) Off-hand", 41032, 5)),
 
        RAINBOW_POT_OF_GOLD_OFFHAND(new Cosmetic("Rainbow Pot of Gold Off-hand", 41399, 5)),
 
        PURIFIED_ORB_OFFHAND(new Cosmetic("Purified Orb Off-hand", 41737, 5)),
 
        ORB_OF_CLUBS_OFFHAND(new Cosmetic("Orb of Clubs Off-hand", 42874, 5)),
 
        OCEANS_MAGE_ORB_OFFHAND(new Cosmetic("Ocean's Mage Orb Off-hand", 42721, 5)),
 
        ORB_OF_THE_MIND_OFFHAND(new Cosmetic("Orb of the Mind Off-hand", 42702, 5)),
 
        ERETHDORS_GRIMOIRE_OFFHAND(new Cosmetic("Erethdor's grimoire Off-hand", 44138, 5)),
 
        UNTAMED_ORB_OFFHAND(new Cosmetic("Untamed Orb Off-hand", 48158, 5)),
 
        UNTAMED_BOOK_OFFHAND(new Cosmetic("Untamed Book Off-hand", 48159, 5)),
 
        SOLARIUS_SHIELD_OFFHAND(new Cosmetic("Solarius Shield Off-hand", 24896, 5)),
 
        PALADIN_SHIELD_OFFHAND(new Cosmetic("Paladin Shield Off-hand", 26474, 5)),
 
        SUPERHERO_SHIELD_OFFHAND(new Cosmetic("Superhero Shield Off-hand", 29427, 5)),
 
        AVIANSIE_SHIELD_OFFHAND(new Cosmetic("Aviansie Shield Off-hand", 30347, 5)),
 
        ORKISH_SHIELD_OFFHAND(new Cosmetic("Orkish Shield Off-hand", 30348, 5)),
 
        REPLICA_DRAGONFIRE_SHIELD_OFFHAND(new Cosmetic("Replica Dragonfire Shield Off-hand", 30630, 5)),
 
        SHADOW_SIR_OWEN_SHIELD_OFFHAND(new Cosmetic("Shadow Sir Owen Shield Off-hand", 31127, 5)),
 
        REPLICA_VOID_KNIGHT_DEFLECTOR_OFFHAND(new Cosmetic("Replica Void Knight Deflector Off-hand", 31709, 5)),
 
        SHIELD_OF_ARRAV_OFFHAND(new Cosmetic("Shield of Arrav Off-hand", 34328, 5)),
 
        ARRAV_CURSED_SHIELD_OFFHAND(new Cosmetic("Arrav (Cursed) Shield Off-hand", 34317, 5)),
 
        BEACH_BUCKET_OFFHAND(new Cosmetic("Beach Bucket Off-hand", 34934, 5)),
 
        BEACH_SURFBOARD_SHIELD_OFFHAND(new Cosmetic("Beach Surfboard Shield Off-hand", 37995, 5)),
 
        BEACH_SHELL_SHIELD_OFFHAND(new Cosmetic("Beach Shell Shield Off-hand", 38001, 5)),
 
        ICE_SHIELD_OFFHAND(new Cosmetic("Ice Shield Off-hand", 39319, 5)),
 
        ATEN_OFFHAND(new Cosmetic("Aten Off-hand", 40213, 5)),
 
        SHIELD_OF_HEARTS_OFFHAND(new Cosmetic("Shield of Hearts Off-hand", 40948, 5)),
 
        NOVTUMBERFEST_PRETZEL_SHIELD_OFFHAND(new Cosmetic("NovtumberFest Pretzel Shield Off-hand", 41158, 5)),
 
        CLOCKWORK_SHIELD_OFFHAND(new Cosmetic("Clockwork shield Off-hand", 41342, 5)),
 
        WREATH_SHIELD_OFFHAND(new Cosmetic("Wreath Shield Off-hand", 41522, 5)),
 
        ZODIAC_SHIELD_OFFHAND(new Cosmetic("Zodiac shield Off-hand", 41760, 5)),
 
        GAMEBLAST_SHIELD_OFFHAND(new Cosmetic("Gameblast Shield Off-hand", 41776, 5)),
 
        SHIELD_OF_SPADES_OFFHAND(new Cosmetic("Shield of Spades Off-hand", 42236, 5)),
 
        FALADOR_SHIELD_1_OFFHAND(new Cosmetic("Falador shield 1 Off-hand", 14577, 5)),
 
        FALADOR_SHIELD_2_OFFHAND(new Cosmetic("Falador shield 2 Off-hand", 14578, 5)),
 
        FALADOR_SHIELD_3_OFFHAND(new Cosmetic("Falador shield 3 Off-hand", 14579, 5)),
 
        FALADOR_SHIELD_4_OFFHAND(new Cosmetic("Falador shield 4 Off-hand", 19749, 5)),
 
        IWI_SHIELD_OFFHAND(new Cosmetic("Iwi Shield Off-hand", 42512, 5)),
 
        SHIELD_OF_DIAMONDS_OFFHAND(new Cosmetic("Shield of Diamonds Off-hand", 42861, 5)),
 
        SHIELD_OF_CLUBS_OFFHAND(new Cosmetic("Shield of Clubs Off-hand", 42875, 5)),
 
        BLACK_NECRONIUM_KITESHIELD_OFFHAND(new Cosmetic("Black Necronium Kiteshield Off-hand", 44444, 5)),
 
        NECTURION_OFFHAND_SCIMITAR(new Cosmetic("Necturion Off-hand Scimitar", 44716, 5)),
 
        NECTURION_MASTERWORK_OFFHAND_SCIMITAR(new Cosmetic("Necturion (Masterwork) Off-hand Scimitar", 44725, 5)),
 
        INVICTUM_OFFHAND_SCIMITAR(new Cosmetic("Invictum Off-hand Scimitar", 44734, 5)),
 
        INVICTUM_MASTERWORK_OFFHAND_SCIMITAR(new Cosmetic("Invictum (Masterwork) Off-hand Scimitar", 44743, 5)),
 
        AETHERIUM_OFFHAND_SCIMITAR(new Cosmetic("Aetherium Off-hand Scimitar", 44752, 5)),
 
        AETHERIUM_MASTERWORK_OFFHAND_SCIMITAR(new Cosmetic("Aetherium (Masterwork) Off-hand Scimitar", 44761, 5)),
 
        REPLICA_DRAGON_PLATESKIRT(new Cosmetic("Replica Dragon Plateskirt", 30632, 7)),
 
        REPLICA_DRAGON_SP_PLATESKIRT(new Cosmetic("Replica Dragon (sp) Plateskirt", 30633, 7)),
 
        REPLICA_DRAGON_OR_PLATESKIRT(new Cosmetic("Replica Dragon (or) Plateskirt", 30634, 7)),
 
        CIRCUS_FIREMAKER_TROUSERS(new Cosmetic("Circus (Firemaker) Trousers", 30989, 7)),
 
        REPLICA_METAL_PLATESKIRT(new Cosmetic("Replica Metal Plateskirt", 31214, 7)),
 
        REPLICA_METAL_T_PLATESKIRT(new Cosmetic("Replica Metal (t) Plateskirt", 31222, 7)),
 
        REPLICA_METAL_G_PLATESKIRT(new Cosmetic("Replica Metal (g) Plateskirt", 31228, 7)),
 
        MORYTANIA_LEGS_1(new Cosmetic("Morytania legs 1", 42477, 7)),
 
        MORYTANIA_LEGS_2(new Cosmetic("Morytania legs 2", 42478, 7)),
 
        MORYTANIA_LEGS_3(new Cosmetic("Morytania legs 3", 42479, 7)),
 
        MORYTANIA_LEGS_4(new Cosmetic("Morytania legs 4", 42480, 7)),
 
        PLATELEGS_OF_TRIALS(new Cosmetic("Platelegs of Trials", 44167, 7)),
 
        REPLICA_VOID_KNIGHT_GLOVES(new Cosmetic("Replica Void Knight Gloves", 31711, 9)),
 
        JAS_HANDS(new Cosmetic("Jas Hands", 34301, 9)),
 
        KARAMJA_GLOVES_1(new Cosmetic("Karamja gloves 1", 42473, 9)),
 
        KARAMJA_GLOVES_2(new Cosmetic("Karamja gloves 2", 42474, 9)),
 
        KARAMJA_GLOVES_3(new Cosmetic("Karamja gloves 3", 42475, 9)),
 
        KARAMJA_GLOVES_4(new Cosmetic("Karamja gloves 4", 42476, 9)),
 
        BRACER_OF_TRIALS(new Cosmetic("Bracer of Trials", 44170, 9)),
 
        JADINKO_SLIPPERS(new Cosmetic("Jadinko Slippers", 34502, 10)),
 
        ELF_SHOES(new Cosmetic("Elf Shoes", 41517, 10)),
 
        FREMENNIK_SEA_BOOTS_1(new Cosmetic("Fremennik sea boots 1", 42469, 10)),
 
        FREMENNIK_SEA_BOOTS_2(new Cosmetic("Fremennik sea boots 2", 42470, 10)),
 
        FREMENNIK_SEA_BOOTS_3(new Cosmetic("Fremennik sea boots 3", 42471, 10)),
 
        FREMENNIK_SEA_BOOTS_4(new Cosmetic("Fremennik sea boots 4", 42472, 10)),
 
        BOOTS_OF_TRIALS(new Cosmetic("Boots of Trials", 44173, 10)),
 
        SERENE_GAZE(new Cosmetic("Serene Gaze", 29094, 14)),
 
        VERNAL_GAZE(new Cosmetic("Vernal Gaze", 29096, 14)),
 
        NOCTURNAL_GAZE(new Cosmetic("Nocturnal Gaze", 29098, 14)),
 
        DIVINE_GAZE(new Cosmetic("Divine Gaze", 29100, 14)),
 
        ABYSSAL_GAZE(new Cosmetic("Abyssal Gaze", 29102, 14)),
 
        BLAZING_GAZE(new Cosmetic("Blazing Gaze", 29104, 14)),
 
        MYSTICAL_GAZE(new Cosmetic("Mystical Gaze", 29106, 14)),
 
        RAINBOW_GAZE(new Cosmetic("Rainbow Gaze", 47823, 14)),
 
        FLAMING_SWORD_ENCHANTMENT(new Cosmetic("Flaming Sword Enchantment", 41810, 14)),
 
        PHOENIX_AURA(new Cosmetic("Phoenix Aura", 47673, 14)),
 
        RAINBOW_AURA(new Cosmetic("Rainbow Aura", 47821, 14)),
 
        SKELETAL_WINGS(new Cosmetic("Skeletal Wings", 30044, 18)),
 
        BUTTERFLY_WINGS(new Cosmetic("Butterfly Wings", 30046, 18)),
 
        ZAMORAK_WINGS(new Cosmetic("Zamorak Wings", 30048, 18)),
 
        ICYENIC_WINGS(new Cosmetic("Icyenic Wings", 30050, 18)),
 
        DRAGONFLY_WINGS(new Cosmetic("Dragonfly Wings", 30893, 18)),
 
        ARMADYL_WINGS(new Cosmetic("Armadyl Wings", 30895, 18)),
 
        CRYSTALLINE_WINGS(new Cosmetic("Crystalline Wings", 30897, 18)),
 
        PARADOX_WINGS(new Cosmetic("Paradox Wings", 30899, 18)),
 
        DWARVEN_WINGS(new Cosmetic("Dwarven Wings", 31823, 18)),
 
        BLADE_WINGS(new Cosmetic("Blade Wings", 31825, 18)),
 
        LAVA_WINGS(new Cosmetic("Lava Wings", 33655, 18)),
 
        BLOODBLADE_WINGS(new Cosmetic("Bloodblade Wings", 33853, 18)),
 
        ETHEREAL_WINGS_LAW(new Cosmetic("Ethereal Wings (Law)", 34123, 18)),
 
        ETHEREAL_WINGS_BLOOD(new Cosmetic("Ethereal Wings (Blood)", 34124, 18)),
 
        ETHEREAL_WINGS_DEATH(new Cosmetic("Ethereal Wings (Death)", 34125, 18)),
 
        ETHEREAL_WINGS_INFINITY(new Cosmetic("Ethereal Wings (Infinity)", 34126, 18)),
 
        GEMSTONE_WINGS_SAPPHIRE(new Cosmetic("Gemstone Wings (Sapphire)", 34129, 18)),
 
        GEMSTONE_WINGS_EMERALD(new Cosmetic("Gemstone Wings (Emerald)", 34130, 18)),
 
        GEMSTONE_WINGS_RUBY(new Cosmetic("Gemstone Wings (Ruby)", 34131, 18)),
 
        GEMSTONE_WINGS_MAGIC(new Cosmetic("Gemstone Wings (Magic)", 34132, 18)),
 
        FREEFALL_WINGS(new Cosmetic("Freefall Wings", 34133, 18)),
 
        SILVER_BLADED_WINGS(new Cosmetic("Silver Bladed Wings", 34135, 18)),
 
        DRAKAN_WINGS(new Cosmetic("Drakan Wings", 35684, 18)),
 
        KING_RADDALLIN_BANNER(new Cosmetic("King Raddallin Banner", 35956, 18)),
 
        KING_RADDALLIN_BANNER_BLESSED(new Cosmetic("King Raddallin Banner (Blessed)", 35957, 18)),
 
        WINGS_15TH_ANNIVERSARY_CRACKER(new Cosmetic("15th Anniversary Cracker Wings", 36268, 18)),
 
        FURY_WINGS(new Cosmetic("Fury Wings", 37112, 18)),
 
        DRYAD_WINGS(new Cosmetic("Dryad Wings", 37195, 18)),
 
        RUNIC_ESSENCE_WINGS(new Cosmetic("Runic Essence Wings", 37196, 18)),
 
        BEACH_CLAWDIA_WINGS(new Cosmetic("Beach Clawdia Wings", 37989, 18)),
 
        INFLORESCENT_WINGS(new Cosmetic("Inflorescent Wings", 38519, 18)),
 
        DECAYING_WINGS(new Cosmetic("Decaying Wings", 38520, 18)),
 
        ECHO_WINGS_GLOWING(new Cosmetic("Echo Wings (Glowing)", 39514, 18)),
 
        ECHO_WINGS_LUSTROUS(new Cosmetic("Echo Wings (Lustrous)", 39515, 18)),
 
        ECHO_WINGS_INCANDESCENT(new Cosmetic("Echo Wings (Incandescent)", 39516, 18)),
 
        TUMEKEN_BANNER(new Cosmetic("Tumeken Banner", 40348, 18)),
 
        TUMEKEN_BANNER_ANOINTED(new Cosmetic("Tumeken Banner (Anointed)", 40349, 18)),
 
        TUMEKEN_BANNER_BLESSED(new Cosmetic("Tumeken Banner (Blessed)", 40350, 18));
 
        private String name;
        private int price;
        private Cosmetic[] cosmetics;
 
        private Cosmetics(Cosmetic... cosmetics) {// For Default Price
            this(DEFAULT_PRICE_FULL_OUTFIT, cosmetics);
        }
 
        private Cosmetics(int price, Cosmetic... cosmetics) {
            this.name = Utils.formatPlayerNameForDisplay(toString());
            this.cosmetics = cosmetics;
            this.price = price;
        }
 
        private Cosmetics(Cosmetic cosmetic) {// For Default Price
            this(DEFAULT_PRICE_SINGLE_PIECE, cosmetic);
        }
 
        private Cosmetics(int price, Cosmetic cosmetic) {
            this.name = cosmetic.getName();
            this.price = price;
            this.cosmetics = new Cosmetic[] { cosmetic };
        }
 
        public String getName() {
            return name;
        }
 
        public int getPrice() {
            return price;
        }
 
        public Cosmetic[] getCosmetics() {
            return cosmetics;
        }
 
    }

	public static final class Cosmetic {

		private String name;
		private int itemId;
		private int slot;

		public Cosmetic(String name, int itemId, int slot) {
			this.name = name;
			this.itemId = itemId;
			this.slot = slot;
		}

		public String getName() {
			return name;
		}

		public int getItemId() {
			return itemId;
		}

		public int getSlot() {
			return slot;
		}

	}

}
