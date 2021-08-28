package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.cache.loaders.GeneralRequirementMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.content.utils.ToyHorsey;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Inventory;
import com.rs.game.player.MysteryBox;
import com.rs.game.player.Player;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.actions.PlanksCutting;
import com.rs.game.player.actions.PlanksCutting.Planks;
import com.rs.game.player.actions.agility.MonkeyGreeGree;
import com.rs.game.player.actions.cooking.Foods;
import com.rs.game.player.actions.crafting.AraxCrafting;
import com.rs.game.player.actions.crafting.CrystalGlassBlowing;
import com.rs.game.player.actions.crafting.GemCutting;
import com.rs.game.player.actions.crafting.LeatherCrafting;
import com.rs.game.player.actions.crafting.NoxiousWeaponCrafting;
import com.rs.game.player.actions.crafting.SirenicScaleCrafting;
import com.rs.game.player.actions.crafting.TectonicEnergyCrafting;
import com.rs.game.player.actions.divination.DivineObject;
import com.rs.game.player.actions.divination.WeavingEnergy;
import com.rs.game.player.actions.crafting.JewellerySmithing;
import com.rs.game.player.actions.firemaking.Firemaking;
import com.rs.game.player.actions.fletching.BoltTipFletching;
import com.rs.game.player.actions.fletching.Fletching;
import com.rs.game.player.actions.fletching.Fletching.Fletch;
import com.rs.game.player.actions.herblore.FlaskDecanting;
import com.rs.game.player.actions.herblore.HerbCleaning;
import com.rs.game.player.actions.herblore.Herblore;
import com.rs.game.player.actions.herblore.PotionDecanting;
import com.rs.game.player.actions.hunter.TrapAction;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.actions.mining.MiningConstants;
import com.rs.game.player.actions.mining.MiningConstants.Geode;
import com.rs.game.player.actions.mining.orebox.OreBox;
import com.rs.game.player.actions.prayer.Burying.Bone;
import com.rs.game.player.actions.runecrafting.Runecrafting;
import com.rs.game.player.actions.slayer.Slayer;
import com.rs.game.player.actions.smithing.GodswordCreation;
import com.rs.game.player.actions.smithing.SpiritShieldCreation;
import com.rs.game.player.actions.summoning.Summoning;

import com.rs.game.player.actions.summoning.Summoning.Pouches;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.Dicing;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.ItemDyes;
import com.rs.game.player.content.MoneyPouch;
import com.rs.game.player.content.MysteryGift;
import com.rs.game.player.content.PolyporeDungeon;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.WeaponPoisoning;
import com.rs.game.player.content.Wildstalker;
import com.rs.game.player.content.creation.FuryAmulet;
import com.rs.game.player.content.creation.PolyporeStaff;
import com.rs.game.player.content.creation.SkillingOutfits;
import com.rs.game.player.content.creation.VineWhipCreation;
import com.rs.game.player.content.items.ArmourSets;
import com.rs.game.player.content.items.BirdNests;
import com.rs.game.player.content.items.Bonds;
import com.rs.game.player.content.items.BossPetManager;
import com.rs.game.player.content.items.HerbloreBox;
import com.rs.game.player.content.items.HoodedCapes;
import com.rs.game.player.content.items.ItemRecolor;
import com.rs.game.player.content.items.Lamps;
import com.rs.game.player.content.items.Portables;
import com.rs.game.player.content.items.Potions;
import com.rs.game.player.content.items.Potions.Pot;
import com.rs.game.player.content.items.ArmourSets.Sets;
import com.rs.game.player.content.items.sof.MaskOfBrokenFingers;
import com.rs.game.player.content.items.sof.MaskOfGranite;
import com.rs.game.player.content.items.sof.MaskOfMourning;
import com.rs.game.player.content.items.sof.MaskOfReflection;
import com.rs.game.player.content.overrides.CosmeticsHandler;
import com.rs.game.player.content.treasuretrails.Puzzles;
import com.rs.game.player.controllers.FightKiln;
import com.rs.game.player.controllers.minigames.Barrows;
import com.rs.game.player.dialogues.impl.CombinationsD.Combinations;
import com.rs.game.player.managers.LendingManager;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.Color;
import com.rs.utils.Colors;
import com.rs.utils.Lend;
import com.rs.utils.LogSystem;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class InventoryOptionsHandler {

	public static void handleItemOption1(Player player, final int slotId, final int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		for (Puzzles puzzle : Puzzles.values()) {
			if (item.getId() == puzzle.getUnsolvedPuzzleId()) {
				if (player.getPuzzleBox() == null)
					player.setPuzzleBox(puzzle.getFirstTileId());
				player.getPuzzleBox().openPuzzle();
				return;
			} else if (item.getId() == puzzle.getSolvedPuzzleId()) {
				if (player.getPuzzleBox() != null)// just in case.
					player.getPuzzleBox().openPuzzle();
				return;
			}

		}
		if (item.getId() == CosmeticsHandler.KEEP_SAKE_KEY) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You currently have " + player.getEquipment().getKeepSakeItems().size() + " items in your keepsake box.");
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_MOURNING || item.getId() == ItemIdentifiers.HELM_OF_KEENING) {
			MaskOfMourning.checkKills(player);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_BROKEN_FINGERS || item.getId() == ItemIdentifiers.HELM_OF_THE_DEAD_HAND) {
			MaskOfBrokenFingers.checkKills(player);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_GRANITE || item.getId() == ItemIdentifiers.HELM_OF_GROTESQUERY) {
			MaskOfGranite.checkKills(player);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_REFLECTION || item.getId() == ItemIdentifiers.HELM_OF_LITTLE_KINGS) {
			MaskOfReflection.checkKills(player);
			return;
		}
		Geode geode = Geode.getGeode(itemId);
		if (geode != null) {
			MiningConstants.openGeode(player, geode);
			return;
		}
		if (player.getTreasureTrails().useItem(item, slotId)) {
			return;
		}
		if (BossPetManager.unlockPet(player, item)) {
			return;
		}
		if (Bonds.redeemBond(player, item)) {
			return;
		}
		if (player.getOreBox().depositOres(item, item)) {
			return;
		}
		if (Fletching.isFletching(player, itemId)) {
			return;
		}
		if (TrapAction.isTrap(player, new WorldTile(player), itemId)) {
			return;
		}
		Sets set = ArmourSets.getSet(itemId);
		if (set != null) {
			ArmourSets.exchangeSet(player, slotId, set.getId());
			return;
		}
		Pouches pouches = Pouches.forId(itemId);
		if (pouches != null) {
			Summoning.spawnFamiliar(player, pouches);
			return;
		}
		if (Magic.useTabTeleport(player, itemId)) {
			return;
		}
		if (Magic.useScrollTeleport(player, itemId)) {
			return;
		}
		if (itemId >= 5070 && itemId <= 5074) {
			BirdNests.searchNest(player, itemId);
			return;
		}
		if (GemCutting.cutGem(player, itemId)) {
			return;
		}
		if (BoltTipFletching.boltFletch(player, itemId)) {
			return;
		}
		if (item.getName().toLowerCase().contains("logs")) {
			player.getDialogueManager().startDialogue("LogAction", itemId);
		}
		if (LeatherCrafting.handleItemOnItem(player, item, new Item(1733))) {
			return;
		}
		if (Foods.eat(player, item, slotId)) {
			return;
		}
		ItemDefinitions itemDef = ItemDefinitions.getItemDefinitions(itemId);
		if (itemDef.containsOption("Repair") || itemDef.containsOption("(broken)") || itemDef.containsOption("(deg)")
				|| itemDef.containsOption("(degraded)")) {
			player.getDialogueManager().startDialogue("RepairItem", item);
			return;
		}
		if ((item.getDefinitions().containsOption(0, "Grind") || (item.getDefinitions().containsOption(0, "Crush")))
				&& player.getInventory().containsItemToolBelt(233, 1)) {
			final int herblore = Herblore.isHerbloreSkill(item, new Item(233, 1));
			if (herblore > -1) {
				player.getDialogueManager().startDialogue("HerbloreD", herblore, item, new Item(233, 1));
				return;
			}
		}
		if (itemId >= 15086 && itemId <= 15100) {
			Dicing.handleRoll(player, itemId, false);
			return;
		}
		if (Potions.pot(player, item, slotId)) {
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.fillPouch(player, pouch);
			return;
		}
		if (Lamps.isSelectable(itemId) || Lamps.isSkillLamp(itemId) || Lamps.isOtherLamp(itemId)) {
			Lamps.processLampClick(player, slotId, itemId);
			return;
		}
		if (HerbCleaning.clean(player, item, slotId)) {
			return;
		}
		if (WeavingEnergy.handleWeaving(player, item)) {
			return;
		}
		Bone bone = Bone.forId(itemId);
		if (bone != null) {
			Bone.bury(player, slotId);
			return;
		}
		if (Magic.useTabTeleport(player, itemId)) {
			return;
		}
		if (item.getDefinitions().containsOption("Deploy")
				|| item.getDefinitions().containsOption("Place deposit box")) {
			int objectId = 0;
			Portables.placePortable(player, item, objectId);
			return;
		}
		switch (itemId) {
		case 19864:
			int amount = player.getInventory().getAmountOf(itemId);
			player.prestigePoints += amount;
			player.getInventory().deleteItem(itemId, amount);
			player.getPackets().sendGameMessage(Color.GREEN, "You now have a total of " + Utils.formatNumber(player.prestigePoints) + " prestige tokens.");
			return;
		case 3062:
			HerbloreBox.open(player);
			return;
		case 23193:
		case 32845:
			player.getActionManager().setAction(new CrystalGlassBlowing());
			return;
		case 31721:
		case 31722:
		case 31723:
		case 31724:
			player.getDialogueManager().startDialogue("NoxiousWeaponCraftingD", null, NoxiousWeaponCrafting.WEAPON);
			return;
		case 31718:
		case 31719:
		case 31720:
			AraxCrafting.handleSpiderLeg(player);
			return;
		case 29863:
			player.getDialogueManager().startDialogue("SirenicScaleCraftingD", null, SirenicScaleCrafting.ARMOUR);
			return;
		case 28627:
			player.getDialogueManager().startDialogue("TectonicEnergyCraftingD", null, TectonicEnergyCrafting.ARMOUR);
			return;
		case 995:
			player.getMoneyPouch().sendDynamicInteraction(item.getAmount(), false);
			return;
		case 2574:
			player.getTreasureTrails().useSextant();
			return;
		case 2798:
		case 3565:
		case 3576:
		case 19042:
			player.getTreasureTrails().openPuzzle(itemId);
			return;
		case 952:
			dig(player);
			return;
		case 22370:
			// Summoning.openDreadnipInterface(player);
			return;
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			ToyHorsey.play(player);
			return;
		case 6199:
			MysteryGift.OpenGift(player, item);
			return;
		case 18778:
			MysteryBox.openBox(player);
			return;
		}
		if (WeavingEnergy.dropDivine(player, item)) {
			return;
		}
		if (itemDef.getName().toLowerCase().contains("effigy")) {
			player.getDialogueManager().startDialogue("AncientEffigiesD", itemId);
			return;
		}
		if (itemId >= 23653 && itemId <= 23658) {
			FightKiln.useCrystal(player, itemId);
			return;
		}
		if (item.getDefinitions().getName().startsWith("Burnt")) {
			player.getDialogueManager().startDialogue("SimplePlayerMessage", "Ugh, this is inedible.");
			return;
		}
		if (player.getRights() >= 2) {
			Logger.log("ItemHandler", "Item option 1: " + itemId + ", slotId: " + slotId);
			player.getPackets().sendGameMessage("Item option 1: " + itemId + ", slotId " + slotId + ".", true);
		}
	}

	public static void handleItemOption2(final Player player, final int slotId, final int itemId, Item item) {
		if (Firemaking.isFiremaking(player, itemId)) {
			return;
		}
		if (item.getDefinitions().name.contains("wildstalker")) {
			Wildstalker.sendKillsMsg(player);
			return;
		}
		if (player.getOreBox().isOreBox(item)) {
			player.getOreBox().depositChestToBank(item);
			return;
		}
		Geode geode = Geode.getGeode(itemId);
		if (geode != null) {
			player.stopAll();
			MiningConstants.openGeode(player, geode, item.getAmount());
			return;
		}
		switch (itemId) {
		case 2801:
			if (player.getTreasureTrails().useDig()) {
				return;
			}
			return;
		case 6583:
		case 7927:
			JewellerySmithing.ringTransformation(player, itemId);
			return;
		case 19043:
			player.getTreasureTrails().useItem(item, slotId);
			return;
		case 5733:
			player.getDialogueManager().startDialogue("RottenPotato");
			return;
		case 4031:
			MonkeyGreeGree.getInstance().setTransformation(player, 0, 4363);
			return;
		case 19803:
			player.getEquipment().getItems().set(Equipment.SLOT_CHEST, new Item(itemId));
			player.getEquipment().refresh(slotId);
			return;
		case 19804:
			player.getEquipment().getItems().set(Equipment.SLOT_LEGS, new Item(itemId));
			player.getEquipment().refresh(slotId);
			return;
		case 995:
			if (player.isCanPvp()) {
				player.getPackets()
						.sendGameMessage("You cannot access your money pouch within a player-vs-player zone.");
				return;
			}
			player.getTemporaryAttributtes().put(Key.ADD_X_TO_POUCH, Boolean.TRUE);
			player.getPackets().sendInputIntegerScript("How many would you like to deposit? You have "
					+ Utils.formatNumber(player.getInventory().getAmountOf(995)) + " coins.");
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.emptyPouch(player, pouch);
			player.stopAll(false);
		} else if (itemId >= 15086 && itemId <= 15100) {
			Dicing.handleRoll(player, itemId, true);
			return;
		}
		if (ButtonHandler.sendWear(player, slotId, item.getId())) {
			Equipment.refreshEquipBonuses(player);
			return;
		}
		if (player.getRights() >= 2) {
			Logger.log("ItemHandler", "Item option 2: " + itemId + ", slotId: " + slotId);
			player.getPackets().sendGameMessage("Item option 2: " + itemId + ", slotId " + slotId + ".", true);
		}
	}

	public static void dig(final Player player) {
		player.resetWalkSteps();
		player.setNextAnimation(new Animation(830));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				if (Barrows.digIntoGrave(player))
					return;
				if (player.getX() == 3005 && player.getY() == 3376 || player.getX() == 2999 && player.getY() == 3375
						|| player.getX() == 2996 && player.getY() == 3377
						|| player.getX() == 2989 && player.getY() == 3378
						|| player.getX() == 2987 && player.getY() == 3387
						|| player.getX() == 2984 && player.getY() == 3387) {
					// mole
					player.setNextWorldTile(new WorldTile(1752, 5137, 0));
					player.getPackets()
							.sendGameMessage("You seem to have dropped down into a network of mole tunnels.");
					return;
				}
				if (player.getTreasureTrails().useDig()) {
					return;
				}
				player.getPackets().sendGameMessage("You find nothing.");
			}

		});
	}

	public static Item contains(int id1, Item item1, Item item2) {
		if (item1.getId() == id1)
			return item2;
		if (item2.getId() == id1)
			return item1;
		return null;
	}

	public static boolean contains(int id1, int id2, Item... items) {
		boolean containsId1 = false;
		boolean containsId2 = false;
		for (Item item : items) {
			if (item.getId() == id1)
				containsId1 = true;
			else if (item.getId() == id2)
				containsId2 = true;
		}
		return containsId1 && containsId2;
	}

	public static void handleItemOnItem(final Player player, InputStream stream) {
		int itemUsedWithId = stream.readShort();
		int toSlot = stream.readShortLE128();
		int hash1 = stream.readInt();
		int hash2 = stream.readInt();
		int interfaceId = hash1 >> 16;
		int interfaceId2 = hash2 >> 16;
		int fromSlot = stream.readShort();
		int itemUsedId = stream.readShortLE128();
		Item fromItem = player.getInventory().getItem(fromSlot);
		Item toItem = player.getInventory().getItem(toSlot);
		if ((interfaceId == 747 || interfaceId == 662) && interfaceId2 == Inventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(toSlot);
				}
			}
			return;
		}
		if (interfaceId == Inventory.INVENTORY_INTERFACE && interfaceId == interfaceId2
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28)
				return;
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != itemUsedId
					|| usedWith.getId() != itemUsedWithId)
				return;
			player.stopAll();
			if (!player.getControllerManager().canUseItemOnItem(itemUsed, usedWith))
				return;
			if (WeaponPoisoning.handleItemInteract(player, itemUsed, usedWith)) {
				return;
			}
			if (PolyporeStaff.createStaff(player, itemUsed, usedWith)) {
				return;
			}
			if (CosmeticsHandler.keepSakeItem(player, itemUsed, usedWith))
				return;
			Combinations combination = Combinations.isCombining(itemUsedId, itemUsedWithId);
			if (combination != null) {
				player.getDialogueManager().startDialogue("CombinationsD", combination);
				return;
			}
			if (PolyporeDungeon.handleItemOnItem(player, itemUsed, usedWith)) {
				return;
			}
			if (HoodedCapes.handleHooding(player, itemUsedId, itemUsedWithId)) {
				return;
			}
			if (ItemDyes.attachDyeOnItem(player, fromItem, toItem, fromSlot, toSlot)) {
				return;
			}
			if (ItemRecolor.itemRecolor(player, itemUsedId, itemUsedWithId)) {
				return;
			}
			if (ItemRecolor.itemRecolor(player, itemUsedWithId, itemUsedId)) {
				return;
			}
			if (FlaskDecanting.mixPot(player, fromItem, toItem, fromSlot, toSlot)) {
				return;
			}
			if (PotionDecanting.mixPot(player, fromItem, toItem, fromSlot, toSlot)) {
				return;
			}
			if (itemUsed.getId() == 1775 && usedWith.getId() == 1785
					|| usedWith.getId() == 1775 && itemUsed.getId() == 1785) {
				player.getDialogueManager().startDialogue("GlassblowingD");
				return;
			}
			Fletch fletch = Fletching.isFletching(usedWith, itemUsed);
			if (fletch != null) {
				player.getDialogueManager().startDialogue("FletchingD", fletch);
				return;
			}
			if (itemUsed.getId() == LeatherCrafting.NEEDLE.getId()
					|| usedWith.getId() == LeatherCrafting.NEEDLE.getId()) {
				if (LeatherCrafting.handleItemOnItem(player, itemUsed, usedWith)) {
					return;
				}
			}
			if (Firemaking.isFiremaking(player, itemUsed, usedWith)) {
				return;
			}
			if (contains(8794, Planks.PLANK.getPlank(), itemUsed, usedWith)) {
				PlanksCutting.cut(player, Planks.PLANK);
				return;
			}
			if (contains(8794, Planks.OAK_PLANK.getPlank(), itemUsed, usedWith)) {
				PlanksCutting.cut(player, Planks.OAK_PLANK);
				return;
			}
			if (contains(8794, Planks.TEAK_PLANK.getPlank(), itemUsed, usedWith)) {
				PlanksCutting.cut(player, Planks.TEAK_PLANK);
				return;
			}
			if (contains(8794, Planks.MOHAGANY_PLANK.getPlank(), itemUsed, usedWith)) {
				PlanksCutting.cut(player, Planks.MOHAGANY_PLANK);
				return;
			}
			if (FuryAmulet.createAmulet(player, itemUsed, usedWith)) {
				return;
			}
			if (GodswordCreation.handleGodSword(player, itemUsedId, itemUsedWithId)) {
				return;
			}
			if (contains(SpiritShieldCreation.HOLY_ELIXIR, SpiritShieldCreation.SPIRIT_SHIELD, itemUsed, usedWith)) {
				player.getPackets().sendGameMessage("The shield must be blessed at an altar.");
			} else if (contains(SpiritShieldCreation.BLESSED_SPIRIT_SHIELD, 13746, itemUsed, usedWith)
					|| contains(SpiritShieldCreation.BLESSED_SPIRIT_SHIELD, 13748, itemUsed, usedWith)
					|| contains(SpiritShieldCreation.BLESSED_SPIRIT_SHIELD, 13750, itemUsed, usedWith)
					|| contains(SpiritShieldCreation.BLESSED_SPIRIT_SHIELD, 13752, itemUsed, usedWith)) {
				SpiritShieldCreation.attachShield(player, itemUsed, false);
				return;
			}
			if (itemUsed.getName().equals("Whip vine") && usedWith.getName().contains("Abyssal whip")) {
				VineWhipCreation.combine(player, itemUsed.getId());
				return;
			}
			if (Settings.DEBUG) {
				Logger.log("ItemHandler", "Used:" + itemUsed.getId() + ", With:" + usedWith.getId());
			}
		}
	}

	public static void handleItemOption3(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (item.getId() == ItemIdentifiers.MASK_OF_MOURNING || item.getId() == ItemIdentifiers.HELM_OF_KEENING) {
			MaskOfMourning.useTeleport(player);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_BROKEN_FINGERS || item.getId() == ItemIdentifiers.HELM_OF_THE_DEAD_HAND) {
			MaskOfBrokenFingers.useTeleport(player);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_GRANITE || item.getId() == ItemIdentifiers.HELM_OF_GROTESQUERY) {
			MaskOfGranite.useTeleport(player);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_REFLECTION || item.getId() == ItemIdentifiers.HELM_OF_LITTLE_KINGS) {
			MaskOfReflection.useTeleport(player);
			return;
		}
		switch (itemId) {
		case 4155:
			player.getSlayerManager().checkKillsLeft();
			break;
		case 20767:
		case 20769:
		case 20771:
			SkillCapeCustomizer.startCustomizing(player, itemId);
			return;
		case 2801:
			player.getTreasureTrails().useSextant();
			return;
		case 19043:
			if (player.getTreasureTrails().useDig()) {
				return;
			}
			return;
		}
		if (player.getCharges().checkCharges(item)) {
			return;
		}
		if (SkillingOutfits.combineOutfit(player, item)) {
			return;
		}
		if (item.getDefinitions().containsOption("Check limit")) {
			player.sm(Colors.DARK_RED + "You've currently gathered " + DivineObject.checkPercentage(player)
					+ "% of your daily divine resources.");
			long timeVariation = Utils.currentTimeMillis() - player.lastGatherLimit;
			if (timeVariation < 24 * 60 * 60 * 1000) { // 24 hours
				long toWait = 24 * 60 * 60 * 1000 - (Utils.currentTimeMillis() - player.lastGatherLimit);
				player.sm(Colors.DARK_RED + "Your Divine resource limitation will reset in "
						+ Utils.millisecsToMinutes(toWait) + " minutes.");
				return;
			}
			player.gathered = 0;
			player.lastGatherLimit = Utils.currentTimeMillis();
			return;
		} else if (itemId == 15707)
			Magic.DaemonheimTeleport(player, 0, 0, new WorldTile(3449, 3698, 0));
		else if (itemId >= 15084 && itemId <= 15100)
			player.getDialogueManager().startDialogue("DiceBag", itemId);
		else if (itemId == 24437 || itemId == 24439 || itemId == 24440 || itemId == 24441)
			player.getDialogueManager().startDialogue("FlamingSkull", item, slotId);
		else if (Equipment.getItemSlot(itemId) == Equipment.SLOT_AURA)
			player.getAuraManager().sendTimeRemaining(itemId);
	}

	public static void handleItemOption4(Player player, int slotId, int itemId, Item item) {
		if (Settings.DEBUG) {
			System.out.println("Option 4; slotId: " + slotId + "; itemId: " + itemId + ".");
		}
	}

	public static void handleItemOption5(Player player, int slotId, int itemId, Item item) {
		if (Settings.DEBUG) {
			System.out.println("Option 5; slotId: " + slotId + "; itemId: " + itemId + ".");
		}
	}

	public static void handleItemOption6(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (item.getId() == ItemIdentifiers.MASK_OF_MOURNING || item.getId() == ItemIdentifiers.HELM_OF_KEENING) {
			MaskOfMourning.changeLooks(player, itemId, true);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_BROKEN_FINGERS || item.getId() == ItemIdentifiers.HELM_OF_THE_DEAD_HAND) {
			MaskOfBrokenFingers.changeLooks(player, itemId, true);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_GRANITE || item.getId() == ItemIdentifiers.HELM_OF_GROTESQUERY) {
			MaskOfGranite.changeLooks(player, itemId, true);
			return;
		}
		if (item.getId() == ItemIdentifiers.MASK_OF_REFLECTION || item.getId() == ItemIdentifiers.HELM_OF_LITTLE_KINGS) {
			MaskOfReflection.changeLooks(player, itemId, true);
			return;
		}
		if (player.getToolbelt().addItem(item)) {
			return;
		}
		if (player.getToolBeltNew().addItem(item)) {
			return;
		}
		if (Wildstalker.isHelmet(itemId)) {
			Wildstalker.handleHelmets(player, itemId);
		}
		if (player.getOreBox().isOreBox(item)) {
			player.getOreBox().getBoxContents(item);
			return;
		}
		Pot pot = Pot.getPot(item.getId());
		if (pot != null) {
			player.getPackets().sendGameMessage("You empty the contents of the " + item.getName().replace(" (4)", "").replace(" (3)", "").replace(" (2)", "").replace(" (1)", "") + " on the floor.");
			player.getInventory().getItem(slotId).setId(229);
			player.getInventory().refresh();
			return;
		}
		if (item.getDefinitions().containsOption("Powder") || item.getDefinitions().containsOption("Grind")) {
			int herbloreVersa = Herblore.isHerbloreSkill(new Item(233), item);
			if (herbloreVersa > -1) {
				player.getDialogueManager().startDialogue("HerbloreD", herbloreVersa, item, new Item(233));
				return;
			}
		} else if (itemId == 20801) {
			if (!(Wildstalker.getKills() == 10)) {
				player.getPackets().sendGameMessage("You need a total of 10 kills to upgrade this helmet.");
				return;
			}
			player.getInventory().deleteItem(20801, 1);
			player.getInventory().addItem(20802, 1);
			player.getPackets().sendGameMessage("You have upgraded your tier of this helmet.");
		} else if (itemId == 20802) {
			if (!(Wildstalker.getKills() == 25)) {
				player.getPackets().sendGameMessage("You need a total of 25 kills to upgrade this helmet.");
				return;
			}
			player.getInventory().deleteItem(20802, 1);
			player.getInventory().addItem(20803, 1);
			player.getPackets().sendGameMessage("You have upgraded your tier of this helmet.");
		} else if (itemId == 20803) {
			if (!(Wildstalker.getKills() == 75)) {
				player.getPackets().sendGameMessage("You need a total of 75 kills to upgrade this helmet.");
				return;
			}
			player.getInventory().deleteItem(20803, 1);
			player.getInventory().addItem(20804, 1);
			player.getPackets().sendGameMessage("You have upgraded your tier of this helmet.");
		} else if (itemId == 20804) {
			if (!(Wildstalker.getKills() == 100)) {
				player.getPackets().sendGameMessage("You need a total of 100 kills to upgrade this helmet.");
				return;
			}
			player.getInventory().deleteItem(20804, 1);
			player.getInventory().addItem(20805, 1);
			player.getPackets().sendGameMessage("You have upgraded your tier of this helmet.");
		} else if (itemId == 20805) {
			if (!(Wildstalker.getKills() == 200)) {
				player.getPackets().sendGameMessage("You need a total of 200 kills to upgrade this helmet.");
				return;
			}
			player.getInventory().deleteItem(20805, 1);
			player.getInventory().addItem(20806, 1);
			player.getPackets().sendGameMessage("You have upgraded your tier of this helmet.");
		} else if (itemId == 20806) {
			player.getPackets().sendGameMessage("You've now got all Wildstalker helmets.");
		} else if (itemId == 1438)
			Runecrafting.locate(player, 3127, 3405);
		else if (itemId == 1440)
			Runecrafting.locate(player, 3306, 3474);
		else if (itemId == 1442)
			Runecrafting.locate(player, 3313, 3255);
		else if (itemId == 1444)
			Runecrafting.locate(player, 3185, 3165);
		else if (itemId == 1446)
			Runecrafting.locate(player, 3053, 3445);
		else if (itemId == 1448)
			Runecrafting.locate(player, 2982, 3514);
		else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354 && itemId <= 10362)
			player.getDialogueManager().startDialogue("Transportation", "Edgeville", new WorldTile(3087, 3496, 0),
					"Karamja", new WorldTile(2918, 3176, 0), "Draynor Village", new WorldTile(3105, 3251, 0),
					"Al Kharid", new WorldTile(3293, 3163, 0), itemId);
		else if (itemId == 1704 || itemId == 10352)
			player.getPackets().sendGameMessage(
					"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		else if (itemId >= 3853 && itemId <= 3867)
			player.getDialogueManager().startDialogue("Transportation", "Burthrope Games Room",
					new WorldTile(2880, 3559, 0), "Barbarian Outpost", new WorldTile(2519, 3571, 0), "Gamers' Grotto",
					new WorldTile(2970, 9679, 0), "Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);

		if (Settings.DEBUG) {
			System.out.println("Option 6; slotId: " + slotId + "; itemId: " + itemId + ".");
		}
	}

	public static void handleItemOption7(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		LogSystem.dropLog(player, item.getId(), item.getAmount());
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		if (!player.getControllerManager().canDropItem(item))
			return;
		player.stopAll(false);
		if (LendingManager.isLendedItem(player, item)) {
			Lend lend = LendingManager.getLend(player);
			if (lend != null && lend.getItem().getDefinitions().getLendId() == item.getId()) {
				player.getDialogueManager().startDialogue("DiscardLend", lend);
			}
			return;
		}
		if (!ItemConstants.isTradeable(item) || item.getDefinitions().isDestroyItem()) {
			player.getDialogueManager().startDialogue("DestroyItemOption", slotId, item);
			return;
		}
		if (player.getPetManager().spawnPet(itemId, true)) {
			return;
		}
		if (player.getCharges().degradeCompletely(item)) {
			return;
		}
		if (player.isIronman()) {
			player.getInventory().deleteItem(slotId, item);
		}
		if (!player.isIronman()) {
			player.getInventory().deleteItem(slotId, item);
			World.addGroundItem(item, new WorldTile(player), player, true, 60);
		}
		player.getPackets().sendSound(2739, 0, 1);
		if (Settings.DEBUG) {
			System.out.println("Option 7; slotId: " + slotId + "; itemId: " + itemId + ".");
		}
	}

	public static void handleItemOption8(Player player, int slotId, int itemId, Item item) {
		if (Settings.DEBUG) {
			System.out.println("Option 5; slotId: " + slotId + "; itemId: " + itemId + ".");
		}
		if (player.isToggleItems()) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(itemId);
			player.getInterfaceManager().sendItemDrops(def);
			player.getInventory().sendExamine(slotId);
		} else
			player.getInventory().sendExamine(slotId);
	}

	public static void handleItemOnNPC(final Player player, final NPC npc, final Item item) {
		if (Settings.DEBUG) {
			System.out.println("Item On NPC; playerName: " + player.getDisplayName().toLowerCase() + "; npcId: "
					+ npc.getId() + "; itemId: " + item.getId() + ".");
		}
		if (item == null) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
					return;
				}
				if (npc == null || npc.isDead() || npc.hasFinished()
						|| !player.getMapRegionsIds().contains(npc.getRegionId()))
					return;
				if (npc instanceof Pet) {
					player.faceEntity(npc);
					player.getPetManager().eat(item.getId(), (Pet) npc);
					return;
				}
				if (npc.getId() == 20282 && item.getId() == 6739) {
					if (player.getInventory().containsItem(32622, 4000)) {
						player.getInventory().deleteItem(6739, 1);
						player.getInventory().deleteItem(32622, 4000);
						player.getInventory().addItem(32645, 1);
						player.getDialogueManager().startDialogue("SimpleMessage",
								"Lady Ithell has successfully created a crystal hatchet for you.");
						return;
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage",
								"Make sure you have a dragon hatchet and 4.000 harmonic dust with you.");
					}
				}
			}
		}));
	}

	public static void handleItemOnPlayer(final Player player, final Player usedOn, final Item item) {
		if (Settings.DEBUG) {
			System.out.println("Item On Player; playerName: " + player.getDisplayName().toLowerCase() + "; usedOn: "
					+ usedOn.getDisplayName().toLowerCase() + "; itemId: " + item.getId() + ".");
		}
		if (item == null || player.isLocked())
			return;
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(usedOn, new Runnable() {
			@Override
			public void run() {
				player.faceEntity(usedOn);
				if (usedOn.getInterfaceManager().containsScreenInter()) {
					player.sm(usedOn.getDisplayName() + " is busy.");
					return;
				}
				if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
					player.sm("You can't do this during combat.");
					return;
				}
				if (usedOn.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
					player.sm("You cannot send a request to a player in combat.");
					return;
				}
				switch (item.getId()) {
				case 20083:
					if (!player.getInventory().containsOneItem(20083)) {
						return;
					}
					player.getInventory().deleteItem(20083, 1);
					player.getInventory().addItem(20084, 1);
					usedOn.faceEntity(player);
					player.setNextAnimation(new Animation(15153));
					usedOn.setNextAnimation(new Animation(15153));
					break;
				case 4155:
					player.getSlayerManager().invitePlayer(usedOn);
					break;
				default:
					player.sm("Nothing interesting happens.");
					break;
				}
			}
		}));
	}
}
