package com.rs.game.player.appearance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.rs.cache.loaders.BodyDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.WorldThread;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.item.ItemsContainer;
import com.rs.game.item.WeightManager;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.actions.combat.CombatDefinitions;
import com.rs.game.player.actions.combat.PlayerCombat;
import com.rs.game.player.actions.firemaking.Bonfire;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.Wildstalker;
import com.rs.game.player.content.capes.HerbloreCape;
import com.rs.game.player.content.dungeoneering.DungeonController;
import com.rs.game.player.content.items.sof.MaskOfMourning;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.controllers.SorceressGarden;
import com.rs.game.player.dialogues.impl.Transportation;
import com.rs.game.player.managers.ChargesManager;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.utils.ItemExamines;
import com.rs.utils.Utils;

public final class Equipment implements Serializable {

	private static final long serialVersionUID = -4147163237095647617L;

	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_WEAPON = 3, SLOT_CHEST = 4,
			SLOT_SHIELD = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13,
			SLOT_AURA = 14;

	private ItemsContainer<Item> items;

	private transient Player player;
	private transient int equipmentHpIncrease;
	private transient double equipmentWeight;

	private ItemsContainer<Item> cosmeticItems;
	private transient ItemsContainer<Item> cosmeticPreviewItems;
	private List<Item> keepSakeItems;
	private List<SavedCosmetic> savedCosmetics;
	private int costumeColor;

	public Equipment() {
		items = new ItemsContainer<Item>(BodyDefinitions.getEquipmentContainerSize(), false);
		cosmeticItems = new ItemsContainer<>(BodyDefinitions.getEquipmentContainerSize(), false);
		keepSakeItems = new ArrayList<>(50);
		savedCosmetics = new ArrayList<>();
	}

	public void setPlayer(Player player) {
		if (cosmeticItems == null)
			cosmeticItems = new ItemsContainer<>(BodyDefinitions.getEquipmentContainerSize(), false);
		if (keepSakeItems == null)
			keepSakeItems = new ArrayList<>(50);
		if (savedCosmetics == null)
			savedCosmetics = new ArrayList<>();
		this.player = player;
		if (items.getSize() != BodyDefinitions.getEquipmentContainerSize()) {
			ItemsContainer<Item> tempItems = new ItemsContainer<Item>(BodyDefinitions.getEquipmentContainerSize(),
					false);
			for (int i = 0; i < items.getSize(); i++)
				tempItems.set(i, items.get(i));
			items = tempItems;
		}
	}

	public void init() {
		player.getPackets().sendItems(94, items);
		refresh(null);
	}

	public void refresh(int... slots) {
		if (slots != null) {
			if (player.getTemporaryAttributtes().get("Cosmetics") != null) {
				Item[] cosmetics = items.getItemsCopy();
				for (int i = 0; i < cosmetics.length; i++) {
					Item item = cosmetics[i];
					if (item == null)
						cosmetics[i] = new Item(0);
				}
				player.getPackets().sendUpdateItems(94, cosmetics, slots);
			} else
				player.getPackets().sendUpdateItems(94, items, slots);
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		refreshConfigs(slots == null);
	}

	public void reset() {
		items.reset();
		init();
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public Item getCosmeticItem(int slot) {
		return player.getCosmeticManager().getCosmetics().get(slot);
	}

	public void sendExamine(int slotId) {
		Item item = items.get(slotId);
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshConfigs(boolean init) {
		double hpIncrease = 0;
		for (int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			if (item == null) {
				continue;
			}
			refreshItems(item);
			int id = item.getId();
			if (index == Equipment.SLOT_HAT) {
				if (id == 20135 || id == 20137 // torva
						|| id == 20147 || id == 20149 // pernix
						|| id == 20159 || id == 20161 // virtus
				) {
					hpIncrease += 66;
				}

			} else if (index == Equipment.SLOT_CHEST) {
				if (id == 20139 || id == 20141 // torva
						|| id == 20151 || id == 20153 // pernix
						|| id == 20163 || id == 20165 // virtus
				) {
					hpIncrease += 200;
				}
			} else if (index == Equipment.SLOT_LEGS) {
				if (id == 20143 || id == 20145 // torva
						|| id == 20155 || id == 20157 // pernix
						|| id == 20167 || id == 20169 // virtus
				) {
					hpIncrease += 134;
				}
			}
		}
		if (player.getLastBonfire() > 0) {
			int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
			hpIncrease += maxhp * (Bonfire.getBonfireBoostMultiplier(player)
					* (player.getPerkManager().hasPerk(PlayerPerks.PYROMATIC) ? 1.5 : 1)) - maxhp;
		}
		if (player.getHpBoostMultiplier() != 0) {
			int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
			hpIncrease += maxhp * player.getHpBoostMultiplier();
		}
		if (hpIncrease != equipmentHpIncrease) {
			equipmentHpIncrease = (int) hpIncrease;
			if (!init) {
				player.refreshHitPoints();
			}
		}
	}

	public static boolean hideArms(Item item) {
		String name = item.getName().toLowerCase();
		if
		// temp old graphics fix, but bugs alil new ones
		(name.contains("d'hide body") || name.contains("dragonhide body") || name.equals("stripy pirate shirt")
				|| (name.contains("chainbody") && (name.contains("iron") || name.contains("bronze")
						|| name.contains("steel") || name.contains("black") || name.contains("mithril")
						|| name.contains("adamant") || name.contains("rune") || name.contains("white")))
				|| name.equals("leather body") || name.equals("hardleather body") || name.contains("studded body"))
			return false;
		return item.getDefinitions().getEquipType() == 6;
	}

	private boolean refreshItems(Item item) {
		int defenceLvl = player.getSkills().getLevelForXp(Skills.DEFENCE);
		int items50[] = { 30306, 30309, 30312, 30315, 30318, 30321, 28773, 28776, 28779, 28782, 28785, 28788, 28755,
				28758, 28761, 28764, 28767, 28770, 30288, 30291, 30294, 30297, 30300, 30303 };
		int items75[] = { 30307, 30310, 30313, 30316, 30319, 30322, 28774, 28777, 28780, 28783, 28786, 28789, 28756,
				28759, 28762, 28765, 28768, 28771, 30289, 30292, 30295, 30298, 30301, 30304 };
		if (defenceLvl >= 50 && defenceLvl < 75) {
			for (int itemId : items50) {
				if (item.getId() == itemId) {
					player.sendMessage("Your " + item.getName() + " upgraded to a higher tier.");
					item.setId(item.getId() + 1);
					return true;
				}
			}
		}
		if (defenceLvl >= 75) {
			for (int itemId : items75) {
				if (item.getId() == itemId) {
					player.sendMessage("Your " + item.getName() + " upgraded to a higher tier.");
					item.setId(item.getId() + 1);
					return true;
				}
			}
			for (int itemId : items50) {
				if (item.getId() == itemId) {
					player.sendMessage("Your " + item.getName() + " upgraded to a higher tier.");
					item.setId(item.getId() + 2);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean MonkeyGreeGree(Item item) {
		String name = item.getName().toLowerCase();
		if (name.contains("greegree"))
			return false;
		return item.getDefinitions().getEquipType() == 3;
	}

	public static boolean hideHair(Item item) {
		return item.getDefinitions().getEquipType() == 8;
	}

	public static boolean showBear(Item item) {
		String name = item.getName().toLowerCase();
		return !hideHair(item) || name.contains("horns") || name.contains("hat") || name.contains("afro")
				|| name.contains("bronze full") || name.contains("cowl") || name.contains("tattoo")
				|| name.contains("headdress") || name.contains("hood") || name.contains("bearhead")
				|| name.equals("santa hat") || name.contains("partyhat") || name.contains("sleeping")
				|| name.contains("coif") || name.contains("wig") || name.contains("bandana") || name.contains("mitre")
				|| name.contains("mask") && !name.contains("ween") && !name.contains("sirenic")
				|| name.contains("med helm") || name.contains("chicken head")
				|| name.contains("helm") && !name.contains("full") || name.contains("headwear") || item.getId() == 32386
				|| item.getId() == 27157;
	}

	public static int getItemSlot(int itemId) {
		return ItemDefinitions.getItemDefinitions(itemId).getEquipSlot();
	}

	public static boolean isTwoHandedWeapon(Item item) {
		return item.getDefinitions().getEquipType() == 5;
	}

	public static boolean isTwoHandedWeapon(ItemDefinitions defs) {
		return defs.getEquipType() == 5;
	}

	public boolean hasShield() {
		return items.get(5) != null;
	}

	public boolean hasOffHand() {
		Item item = items.get(SLOT_SHIELD);
		return items.get(5) != null && (item != null ? item.getDefinitions().getCombatMap() : null) != null;
	}

	public int getWeaponId() {
		return getWeaponId(false);
	}

	public int getWeaponId(boolean offhand) {
		Item item = !offhand ? items.get(SLOT_WEAPON) : items.get(SLOT_SHIELD);
		if (item == null)
			return -1;
		return item.getId();
	}

	public ItemDefinitions getWeaponDefs() {
		return new Item(getWeaponId()).getDefs();
	}

	public int getWeaponEndCombatEmote() {
		Item weapon = items.get(SLOT_WEAPON);
		if (weapon == null) {
			Item offhand = items.get(SLOT_SHIELD);
			if (offhand == null) {
				return -1;
			}
			int emote = offhand.getDefinitions().getCombatOpcode(2918);
			return emote == 0 ? 18025 : emote;
		}
		int emote = weapon.getDefinitions().getCombatOpcode(2918);
		return emote == 0 ? 18025 : emote;
	}

	public int getWeaponRenderEmote() {
		Item weapon = (cosmeticItems.get(3) != null && isCanDisplayCosmetic()) ? cosmeticItems.get(3)
				: items.get(SLOT_WEAPON);
		if (weapon == null)
			return 1426;
		if (player.getEquipment().hasTwoHandedWeapon() && weapon.getDefinitions().isMeleeTypeWeapon()
				&& player.getCombatDefinitions().isLegacyMode() && !isSpear(weapon))
			return 2574;
		return weapon.getDefinitions().getRenderAnimId();
	}

	public boolean isSpear(Item weapon) {
		return weapon.getDefinitions().getName().contains("spear") || weapon.getName().toLowerCase().equals("mizuyari");
	}

	public int getChestId() {
		Item item = items.get(SLOT_CHEST);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getHatId() {
		Item item = items.get(SLOT_HAT);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getShieldId() {
		Item item = items.get(SLOT_SHIELD);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getLegsId() {
		Item item = items.get(SLOT_LEGS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void removeAmmo(int ammoId, int amount) {
		if (amount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
			if (items.get(SLOT_WEAPON) == null)
				player.getAppearence().generateAppearenceData();
		} else if (amount == -2) {
			items.remove(SLOT_SHIELD, new Item(ammoId, 1));
			refresh(SLOT_SHIELD);
			if (items.get(SLOT_SHIELD) == null)
				player.getAppearence().generateAppearenceData();
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, amount));
			refresh(SLOT_ARROWS);
		}
	}

	public void removeAmmoRS3(int ammoId, int ammount, boolean primary) {
		if (ammount == -1) {
			items.remove(primary ? SLOT_WEAPON : SLOT_SHIELD, new Item(ammoId, 1));
			refresh(primary ? SLOT_WEAPON : SLOT_SHIELD);
			player.getAppearence().generateAppearenceData();
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, ammount));
			refresh(SLOT_ARROWS);
		}
	}

	public int getAuraId() {
		Item item = items.get(SLOT_AURA);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getCapeId() {
		Item item = items.get(SLOT_CAPE);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getRingId() {
		Item item = items.get(SLOT_RING);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getAmmoId() {
		Item item = items.get(SLOT_ARROWS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void deleteItem(int itemId, int amount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public int getBootsId() {
		Item item = items.get(SLOT_FEET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public boolean containsOneItem(int... itemIds) {
		for (int itemId : itemIds) {
			if (items.containsOne(new Item(itemId, 1))) {
				return true;
			}
		}
		return false;
	}

	public int getGlovesId() {
		Item item = items.get(SLOT_HANDS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}

	public int getEquipmentHpIncrease() {
		return equipmentHpIncrease;
	}

	public void setEquipmentHpIncrease(int hp) {
		this.equipmentHpIncrease = hp;
	}

	public boolean wearingArmour() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null || getItem(SLOT_AMULET) != null
				|| getItem(SLOT_WEAPON) != null || getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null || getItem(SLOT_FEET) != null;
	}

	public int getAmuletId() {
		Item item = items.get(SLOT_AMULET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public boolean hasTwoHandedWeapon() {
		Item weapon = items.get(SLOT_WEAPON);
		return weapon != null && isTwoHandedWeapon(weapon);
	}

	public ItemsContainer<Item> getItemsContainer() {
		return items;
	}

	public boolean wearingGloves() {
		return items.get(SLOT_HANDS) != null;
	}

	public long getEquipmentValue() {
		long value = 0;
		for (Item equipment : player.getEquipment().getItems().toArray()) {
			if (equipment == null) {
				continue;
			}
			long amount = equipment.getAmount();
			value += equipment.getDefinitions().getTipitPrice() * amount;
		}
		return value;
	}

	public static void openEquipmentBonuses(final Player player, boolean banking) {
		player.getTemporaryAttributtes().remove("Cosmeticsold");
		WeightManager.calculateWeight(player);
		player.stopAll();
		player.getInterfaceManager().sendInventoryInterface(670);
		player.getInterfaceManager().sendInterface(667);
		player.getPackets().sendUnlockIComponentOptionSlots(667, 9, 0, 14, 0);
		player.getVarBitManager().sendVarBit(8348, 1);
		player.getPackets().sendConfigByFile(4894, banking ? 1 : 0);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendInterSetItemsOptionsScript(670, 0, 93, 4, 7, "Equip", "Compare", "Stats", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(670, 0, 0, 27, 0, 1, 2, 3);
		player.getPackets().sendIComponentSettings(667, 14, 0, 13, 1030);
		Equipment.refreshEquipBonuses(player);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getVarBitManager().sendVarBit(8348, 1);
				player.getPackets().sendRunScriptBlank(2319);
			}
		});
		if (banking) {
			player.getTemporaryAttributtes().put("Banking", Boolean.TRUE);
			player.setCloseInterfacesEvent(() -> player.getTemporaryAttributtes().remove("Banking"));
		}
	}

	public static void refreshEquipBonuses(Player player) {
		player.getPackets().sendIComponentText(667, 28, "Stab: +" + player.getCombatDefinitions().getBonuses()[0]);
		player.getPackets().sendIComponentText(667, 29, "Slash: +" + player.getCombatDefinitions().getBonuses()[1]);
		player.getPackets().sendIComponentText(667, 30, "Crush: +" + player.getCombatDefinitions().getBonuses()[2]);
		player.getPackets().sendIComponentText(667, 31, "Magic: +" + player.getCombatDefinitions().getBonuses()[3]);
		player.getPackets().sendIComponentText(667, 32, "Range: +" + player.getCombatDefinitions().getBonuses()[4]);
		player.getPackets().sendIComponentText(667, 33, "Stab: +" + player.getCombatDefinitions().getBonuses()[5]);
		player.getPackets().sendIComponentText(667, 34, "Slash: +" + player.getCombatDefinitions().getBonuses()[6]);
		player.getPackets().sendIComponentText(667, 35, "Crush: +" + player.getCombatDefinitions().getBonuses()[7]);
		player.getPackets().sendIComponentText(667, 36, "Magic: +" + player.getCombatDefinitions().getBonuses()[8]);
		player.getPackets().sendIComponentText(667, 37, "Range: +" + player.getCombatDefinitions().getBonuses()[9]);
		player.getPackets().sendIComponentText(667, 38,
				"Summoning: +" + player.getCombatDefinitions().getBonuses()[10]);
		player.getPackets().sendIComponentText(667, 39, "Absorb Melee: +"
				+ player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 40, "Absorb Magic: +"
				+ player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 41, "Absorb Ranged: +"
				+ player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 42, "Strength: " + player.getCombatDefinitions().getBonuses()[14]);
		player.getPackets().sendIComponentText(667, 43,
				"Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
		player.getPackets().sendIComponentText(667, 44, "Prayer: +" + player.getCombatDefinitions().getBonuses()[16]);
		player.getPackets().sendIComponentText(667, 45,
				"Magic Damage: +" + player.getCombatDefinitions().getBonuses()[17] + "%");
		player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponStance());
	}

	/**
	 * Sets the attack render animation.
	 * 
	 * @param player The player to set the r'emote to.
	 */
	public int getWeaponStance() {
		Item originalWeapon = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
		boolean combatStance = player.getCombatDefinitions().isCombatStance();
		Item weapon = player.getCosmeticManager().getClothingOverrides()[SLOT_WEAPON] != 0
				? new Item(player.getCosmeticManager().getClothingOverrides()[Equipment.SLOT_WEAPON])
				: items.get(SLOT_WEAPON);
		if (originalWeapon != null && originalWeapon.getDefinitions().getAttackAnimation() != ItemDefinitions
				.getItemDefinitions(weapon.getId()).getAttackAnimation()) {
			weapon = originalWeapon;
		} else {
			new Item(player.getCosmeticManager().getClothingOverrides()[Equipment.SLOT_WEAPON]);
		}
		if (weapon == null) {
			Item offhand = player.getCosmeticManager().getClothingOverrides()[SLOT_SHIELD] != 0
					? items.get(player.getCosmeticManager().getClothingOverrides()[SLOT_SHIELD])
					: items.get(SLOT_SHIELD);
			if (offhand == null) {
				return combatStance ? 2688 : 2699;
			}
			int emote = offhand.getDefinitions().getCombatOpcode(combatStance ? 2955 : 2954);
			return emote == 0 ? combatStance ? 2688 : 2699 : emote;
		}
		int emote = weapon.getDefinitions().getCombatOpcode(combatStance ? 2955 : 2954);
		if (weapon.getId() == 4084) {
			return 1119;
		}
		return emote == 0 ? combatStance ? 2688 : 2699 : emote;
	}

	public boolean hasFarmingCape() {
		Item item = getItem(SLOT_CAPE);
		if (item == null)
			return false;
		return item.getName().toLowerCase().contains("farming cape");
	}

	public boolean hasHerbloreCape() {
		Item item = getItem(SLOT_CAPE);
		if (item == null)
			return false;
		return item.getName().toLowerCase().contains("herblore cape");
	}

	public double getEquipmentWeight() {
		return equipmentWeight;
	}

	public boolean hasRingOfWhispers() {
		Item item = getItem(SLOT_RING);
		if (item == null)
			return false;
		return item.getName().toLowerCase().contains("ring of whispers");
	}

	public ItemsContainer<Item> getCosmeticItems() {
		return cosmeticItems;
	}

	public boolean isCanDisplayCosmetic() {
		if (player.getControllerManager().getController() != null
				&& player.getControllerManager().getController() instanceof DuelControler)
			return false;
		if (player.getControllerManager().getController() != null
				&& player.getControllerManager().getController() instanceof DungeonController)
			return false;
		return !player.isCanPvp();
	}

	public List<Item> getKeepSakeItems() {
		if (keepSakeItems == null)
			keepSakeItems = new ArrayList<>(50);
		return keepSakeItems;
	}

	public boolean containsKeepSakeItem(int itemId) {
		for (Item item : keepSakeItems) {
			if (item == null)
				continue;
			if (item.getId() == itemId)
				return true;
		}
		return false;
	}

	public List<SavedCosmetic> getSavedCosmetics() {
		if (savedCosmetics == null)
			savedCosmetics = new ArrayList<>();
		return savedCosmetics;
	}

	public int getCostumeColor() {
		return costumeColor;
	}

	public void setCostumeColor(int costumeColor) {
		this.costumeColor = costumeColor;
		player.getAppearence().generateAppearenceData();
	}

	public void resetCosmetics() {
		cosmeticItems.reset();
	}

	public ItemsContainer<Item> getCosmeticPreviewItems() {
		return cosmeticPreviewItems;
	}

	public void setCosmeticPreviewItems(ItemsContainer<Item> cosmeticPreviewItems) {
		this.cosmeticPreviewItems = cosmeticPreviewItems;
	}

	public static final class SavedCosmetic implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3243926269985535095L;
		private ItemsContainer<Item> cosmeticItems;
		private String cosmeticName;

		public SavedCosmetic(String cosmeticName, ItemsContainer<Item> cosmeticItems) {
			this.cosmeticName = cosmeticName;
			this.cosmeticItems = cosmeticItems;
		}

		public ItemsContainer<Item> getCosmeticItems() {
			return cosmeticItems;
		}

		public String getCosmeticName() {
			return cosmeticName;
		}

	}
	
	public static void handleEquipment(Player player, int componentId, int slotId, int packetId, int itemId) {
		if (player.getInterfaceManager().containsInventoryInter())
			return;
		if (componentId == 6) {
			int hatId = player.getEquipment().getHatId();
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				ButtonHandler.sendRemove(player, Equipment.SLOT_HAT);
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				if (hatId == 24437 || hatId == 24439 || hatId == 24440 || hatId == 24441) {
					player.getDialogueManager().startDialogue("FlamingSkull", player.getEquipment().getItem(Equipment.SLOT_HAT), -1);
					return;
				}
				if (hatId == 20801 || hatId == 20802 || hatId == 20803 || hatId == 20804 || hatId == 20805 || hatId == 20806) {
					Wildstalker.sendKillsMsg(player);
					return;
				}
				if (hatId == ItemIdentifiers.MASK_OF_MOURNING || hatId == ItemIdentifiers.HELM_OF_KEENING) {
					MaskOfMourning.checkKills(player);
					return;
				}
				final Item hat = player.getEquipment().getItem(Equipment.SLOT_HAT);
				if (hat != null) {
					if (player.getCharges().checkCharges(hat)) {
						return;
					}
				}
			} 
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				if (hatId == ItemIdentifiers.MASK_OF_MOURNING || hatId == ItemIdentifiers.HELM_OF_KEENING) {
					MaskOfMourning.useTeleport(player);
					return;
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				if (hatId == ItemIdentifiers.MASK_OF_MOURNING || hatId == ItemIdentifiers.HELM_OF_KEENING) {
					MaskOfMourning.changeLooks(player, hatId, false);
					return;
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
				player.getEquipment().sendExamine(Equipment.SLOT_HAT);
			}
		}
		if (componentId == 9) {
			int capeId = player.getEquipment().getCapeId();
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				ButtonHandler.sendRemove(player, Equipment.SLOT_CAPE);
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				if (ItemDefinitions.getItemDefinitions(capeId).containsOption(0, "Boost")) {
					player.getPackets().sendGameMessage("You have been given a slight boost by your skillcape.", true);
					return;
				}
				if (capeId == 20763 || capeId == 24709) {
					player.getEmotesManager().useBookEmote(37);
				}
				if (capeId == 15345 || capeId == 15347 || capeId == 15349 || capeId == 19748) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2606, 3211, 0));
				}
				if (capeId == 20769 || capeId == 20771 || capeId == 32152 || capeId == 32153 || capeId == 32151 || capeId == 20767) {
					if (player.isLocked() || player.getControllerManager().getController() != null) {
						return;
					}
					Magic.sendTeleportSpell(player, 8939, 8941, 1576, 1577, 1, 0, new WorldTile(2276, 3313, 1), 4, false, Magic.ITEM_TELEPORT);
					FadingScreen.fade(player);
					FadingScreen.unfade(player, FadingScreen.fade(player, 600 / 2), new Runnable() {
						@Override
						public void run() {
							WorldObject objectdoor = new WorldObject(92278, 10, 3, 2275, 3303, 1);
							player.faceObject(objectdoor);
						}
					});
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				if (capeId == 32151 || capeId == 20767) {
					SkillCapeCustomizer.startCustomizing(player, itemId);
				}
				if (capeId == 9774 || capeId == 9775) {
					HerbloreCape.handleHerbloreCape(player);
				}
				if (capeId == 15347 || capeId == 15349 || capeId == 19748) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2669, 3376, 0));
				}
				if (capeId == 18508 || capeId == 18509) {
					player.getInterfaceManager().sendInterface(1412);
				}
				if (capeId == 9786 || capeId == 9787 || capeId == 31282) {
					player.getDialogueManager().startDialogue("SlayerCape");
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				if (capeId == 20769 || capeId == 20771 || capeId == 32152 || capeId == 32153 || capeId == 19748 || capeId == 15349 || capeId == 15347 || capeId == 15345) {
					if (player.isLocked() || player.getControllerManager().getController() != null) {
						player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
						return;
					}
					WorldTasksManager.schedule(new WorldTask() {
						int timer;

						@Override
						public void run() {
							if (timer == 0) {
								player.lock(2);
								player.setNextGraphics(new Graphics(2670));
								player.setNextAnimation(new Animation(3254));
							}
							if (timer == 1) {
								player.setNextWorldTile(new WorldTile(2674, 3375, 0));
							}
							if (timer == 2) {
								player.setNextGraphics(new Graphics(2671));
								player.setNextAnimation(new Animation(3255));
							}
							timer++;
						}
					}, 0, 1);
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
				int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
				if (capeId == 20769 || capeId == 20771 || capeId == 19748 || capeId == 28301 || capeId == 28302
						|| capeId == 32152 || capeId == 32153) {
					if (player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
						player.lock(3);
						player.setNextAnimation(new Animation(8502));
						player.setNextGraphics(new Graphics(1308));
						player.getSkills().set(Skills.SUMMONING, summonLevel);
						player.getPackets().sendGameMessage("You have recharged your Summoning points.", true);
					} else
						player.getPackets().sendGameMessage("You already have full Summoning points.");
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
				if (capeId == 32152 || capeId == 32153 || capeId == 20769 || capeId == 20771) {
					SkillCapeCustomizer.startCustomizing(player, capeId);
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
				player.getEquipment().sendExamine(Equipment.SLOT_CAPE);
			}
		} 
		if (componentId == 12) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				int amuletId = player.getEquipment().getAmuletId();
				final Item item = player.getEquipment().getItem(Equipment.SLOT_AMULET);
				if (item != null) {
					if (player.getCharges().checkCharges(item)) {
						return;
					}
				}
				if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
					if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3087, 3496, 0))) {
						Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
						if (amulet != null) {
							amulet.setId(amulet.getId() - 2);
							player.getEquipment().refresh(Equipment.SLOT_AMULET);
						}
					}
				} 
				if (amuletId == 1704 || amuletId == 10352) {
					player.getPackets().sendGameMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				int amuletId = player.getEquipment().getAmuletId();
				if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
					if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(2918, 3176, 0))) {
						Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
						if (amulet != null) {
							amulet.setId(amulet.getId() - 2);
							player.getEquipment().refresh(Equipment.SLOT_AMULET);
						}
					}
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				int amuletId = player.getEquipment().getAmuletId();
				if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
					if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3105, 3251, 0))) {
						Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
						if (amulet != null) {
							amulet.setId(amulet.getId() - 2);
							player.getEquipment().refresh(Equipment.SLOT_AMULET);
						}
					}
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
				int amuletId = player.getEquipment().getAmuletId();
				if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
					if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3293, 3163, 0))) {
						Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
						if (amulet != null) {
							amulet.setId(amulet.getId() - 2);
							player.getEquipment().refresh(Equipment.SLOT_AMULET);
						}
					}
				}
			} 
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				ButtonHandler.sendRemove(player, Equipment.SLOT_AMULET);
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
				player.getEquipment().sendExamine(Equipment.SLOT_AMULET);
			}
		} else if (componentId == 15) {
			int weaponId = player.getEquipment().getWeaponId();
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				ButtonHandler.sendRemove(player, Equipment.SLOT_WEAPON);
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				if (weaponId == 15484) {
					if (player.getEquipment().getWeaponId() == 15484) {
						player.getInterfaceManager().gazeOrbOfOculus();
						}
					}
				final Item item = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
				if (item != null) {
					if (player.getCharges().checkCharges(item)) {
						return;
					}
				}
				if (weaponId == 9044 || weaponId == 9046 || weaponId == 9048) {
					player.getDialogueManager().startDialogue("PharaohSceptre", weaponId);
				}
				if (weaponId == 14057) {
					player.setNextAnimation(new Animation(10532));
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				if (weaponId == 14057) {
					SorceressGarden.teleportToSocreressGarden(player, true);
				}
				if (weaponId == 9044 || weaponId == 9046 || weaponId == 9048) {
					player.getDialogueManager().startDialogue("PharaohSceptre", weaponId);
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				if (weaponId == 9044 || weaponId == 9046 || weaponId == 9048) {
					player.getDialogueManager().startDialogue("PharaohSceptre", weaponId);
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
				player.getEquipment().sendExamine(Equipment.SLOT_WEAPON);
			}
		}
		if (componentId == 18) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				final Item chest = player.getEquipment().getItem(Equipment.SLOT_CHEST);
				if (chest != null) {
					if (player.getCharges().checkCharges(chest)) {
						return;
					}
				}
			}
			ButtonHandler.sendRemove(player, Equipment.SLOT_CHEST);
		}
		if (componentId == 21) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				ButtonHandler.sendRemove(player, Equipment.SLOT_SHIELD);
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				if (player.getEquipment().getShieldId() == 11283) {
					int speed, gfxDelay;
					if (!(player.getActionManager().getAction() instanceof PlayerCombat)) {
						return;
					}
					Entity target = ((PlayerCombat) player.getActionManager().getAction()).getTarget();
					if (target != null) {
						if (player.withinDistance(target, 1)) {
							speed = 70;
						} else if (player.withinDistance(target, 5)) {
							speed = 90;
						} else if (player.withinDistance(target, 8)) {
							speed = 110;
						} else {
							speed = 130;
						}
						gfxDelay = speed + 10;
						if (player.getDfsDelay() - Utils.currentTimeMillis() > 0) {
							player.sm("You have to wait " + (TimeUnit.MILLISECONDS.toSeconds(player.getDfsDelay() - Utils.currentTimeMillis())) + " more seconds to activate your special attack.");
							return;
						}
						player.setDfsDelay(Utils.currentTimeMillis() + 45000);
						player.setNextAnimation(new Animation(6696));
						player.setNextGraphics(new Graphics(1165));
						World.sendProjectile(player, player, ((PlayerCombat) player.getActionManager().getAction()).getTarget(), 1166, 76, 50, speed, 43, 55, 0);
						target.setNextGraphics(new Graphics(1167, gfxDelay, 100));
						target.applyHit(new Hit(target, new Random().nextInt(player.getSkills().getCombatLevel() / 2), Hit.HitLook.MAGIC_DAMAGE));
					}
					return;
				}
				if (player.getEquipment().getShieldId() == 11284) {
					player.getPackets().sendGameMessage("You don't have any charges in your shield.");
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				if (ItemDefinitions.getItemDefinitions(itemId).containsOption(1, "Provoke")) {
					Entity target = player.getAttackedBy();
					if (target == null || !(target instanceof NPC)) {
						player.getPackets().sendGameMessage("You can't use provoke without a target.");
						return;
					}
					Long cd = (Long) player.getTemporaryAttributtes().get(Key.PROVOKE);
					if (cd != null && cd > WorldThread.LAST_CYCLE_CTM) {
						player.getPackets().sendGameMessage("You can't use provoke while it is on cooldown.");
						return;
					}
					player.getTemporaryAttributtes().put(Key.PROVOKE, WorldThread.LAST_CYCLE_CTM + 20);
					player.setNextAnimation(new Animation(18130));
					((NPC) target).setTarget(player);
				}
			}
		}
		if (componentId == 24) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				final Item legs = player.getEquipment().getItem(Equipment.SLOT_LEGS);
				if (legs != null) {
					if (player.getCharges().checkCharges(legs)) {
						return;
					}
				}
			}
			ButtonHandler.sendRemove(player, Equipment.SLOT_LEGS);
		}
		if (componentId == 27) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				final Item hands = player.getEquipment().getItem(Equipment.SLOT_HANDS);
				if (hands != null) {
					if (player.getCharges().checkCharges(hands)) {
						return;
					}
				}
			}
			ButtonHandler.sendRemove(player, Equipment.SLOT_HANDS);
		}
		if (componentId == 30) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				int glovesId = player.getEquipment().getGlovesId();
				if ((glovesId >= 24450 && glovesId <= 24454) || (glovesId >= 22358 && glovesId <= 22369))
					player.getCharges().checkPercentage("The gloves are " + ChargesManager.REPLACE + "% degraded.", glovesId, true);
			}
			ButtonHandler.sendRemove(player, Equipment.SLOT_FEET);
		}
		if (componentId == 33) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				int ringId = player.getEquipment().getRingId();
				if (ringId == 15707) {
					return;
				}
				if (itemId == 27477) {
					player.getDialogueManager().startDialogue("SixthAgeCircuit");
					return;
				}
				if (ringId == 15398) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1661, 5257, 0));
					return;
				}
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				int ringId = player.getEquipment().getRingId();
				if (ringId == 15707) {
					Magic.sendTeleportSpell(player, 13652, 13654, 2602, 2603, 1, 0, new WorldTile(3447, 3694, 0), 10, true, Magic.ITEM_TELEPORT);
					return;
				}
				if (ringId == 15398 && player.getStatistics().hasUnlockedFerociousRingUpgrade()) {
					player.getPackets().sendGameMessage("Coming Soon");
					return;
				}
			}
			ButtonHandler.sendRemove(player, Equipment.SLOT_RING);
		}
		if (componentId == 36) {
			ButtonHandler.sendRemove(player, Equipment.SLOT_ARROWS);
		}
		if (componentId == 45) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				ButtonHandler.sendRemove(player, Equipment.SLOT_AURA);
				player.getAuraManager().removeAura();
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
				player.getEquipment().sendExamine(Equipment.SLOT_AURA);
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				player.getAuraManager().activate();
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				player.getAuraManager().sendAuraRemainingTime();
			}
		}
		 if (componentId == 40) {
			String totalValue = Utils.formatNumber(player.getEquipmentValue() + player.getInventoryValue());
			player.stopAll();
			player.getInterfaceManager().sendInterface(17);
			String info = "The number of items kept on<br>death is normally 3.<br><br><br>Your gravestone will not<br>appear.<br><br>Carried wealth:<br>"
					+ totalValue + "<br><br>Risked wealth:<br>" + totalValue
					+ "<br><br>Your hub will be set to:<br>Home";
			player.getPackets().sendConfig2(1747, 201397392);
			player.getPackets().sendGlobalString(352, info);
		} else if (componentId == 37) {
			WeightManager.calculateWeight(player);
			Equipment.openEquipmentBonuses(player, false);
		} else if (componentId == 40) {
			player.stopAll();
			ButtonHandler.openItemsKeptOnDeath(player, false);
		} else if (componentId == 41) {
			player.stopAll();
			player.getInterfaceManager().sendInterface(1178);
			player.getToolbelt().refresh();
		}
	}

}
