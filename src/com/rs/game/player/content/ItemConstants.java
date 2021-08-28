
package com.rs.game.player.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.managers.QuestManager.Quests;

public class ItemConstants {

	public static boolean isDungItem(int itemId) {
		return (itemId >= 15750 && itemId <= 18329) || (itemId >= 19886 && itemId <= 19895)
				|| (itemId >= 19669 && itemId <= 19675) || (itemId >= 18330 && itemId <= 18374);
	}

	public static boolean isBadTestDropsItem(Player player, Item item) {
		return item.getId() == ItemIdentifiers.BONE_BEADS || item.getId() == ItemIdentifiers.DEVOTION
				|| item.getId() == ItemIdentifiers.TRANSFIGURE || item.getId() == ItemIdentifiers.SACRIFICE
				|| item.getId() == ItemIdentifiers.LONG_BONE || item.getId() == ItemIdentifiers.FEMUR_BONE
				|| item.getId() == ItemIdentifiers.THE_GLORY_OF_GENERAL_GRAARDOR
				|| item.getId() == ItemIdentifiers.CURVED_BONE || item.getId() == ItemIdentifiers.COURT_SUMMONS
				|| item.getId() == ItemIdentifiers.ARMADYLS_ASSAULT || item.getId() == ItemIdentifiers.ZILYANAS_NOTES
				|| item.getId() == ItemIdentifiers.RAZULEIS_TALE
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_CAPE)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_CAPE
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_BOOTS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_BOOTS
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_HELM)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_HELM
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_GREAVES)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_GREAVES
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ARMADYL_CUIRASS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ARMADYL_CUIRASS
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_CAPE)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_CAPE
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_BOOTS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_BOOTS
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_HELM)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_HELM
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_GREAVES)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_GREAVES
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_BANDOS_CUIRASS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_BANDOS_CUIRASS
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CAPE)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CAPE
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_BOOTS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_BOOTS
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_HELM)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_HELM
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_GREAVES)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_GREAVES
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CUIRASS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_SARADOMIN_CUIRASS
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CAPE)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CAPE
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_BOOTS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_BOOTS
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_HELM)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_HELM
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_GREAVES)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_GREAVES
				|| player.containsItem(ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CUIRASS)
						&& item.getId() == ItemIdentifiers.WARPRIEST_OF_ZAMORAK_CUIRASS;
	}

	public static boolean isDegradable(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		return name.contains("nightmare gauntlet") || name.contains("seren godbow") || name.contains("zaros godsword")
				|| name.contains("staff of sliske") || name.contains("primeval") || name.contains("tempest")
				|| name.contains("teralith") || name.contains("razorback") || name.contains("celestial handwrap")
				|| name.contains("ascension grips") || name.contains("flarefrost") || name.contains("emberkeen")
				|| name.contains("hailfire") || name.contains("tokkul-zo") || name.contains("amulet of souls")
				|| name.contains("deathtouch braclet") || name.contains("reaper necklace")
				|| name.contains("ring of death") || name.contains("rock-shell") || name.contains("superior seasinger")
				|| name.contains("superior death lotus") || name.contains("superior tetsu")
				|| name.contains("seasinger") || name.contains("death lotus") || name.contains("tetsu")
				|| name.contains("malevolent") || name.contains("vengeful") || name.contains("merciless")
				|| name.contains("sirenic") || name.contains("noxious") || name.contains("tectonic")
				|| name.contains("drygore") || name.contains("ascension") || name.contains("khopesh")
				|| name.contains("defender") || name.contains("lantern") || name.contains("repriser")
				|| name.contains("anti-dragon shield (mole)") || name.contains("royal crossbow")
				|| name.contains("lava whip") || name.contains("staff of darkness") || name.contains("strykebow")
				|| name.contains("anima core") || name.contains("vesta") || name.contains("statius")
				|| name.contains("morrigan") || name.contains("zuriel") || name.contains("crystal helm")
				|| name.contains("crystal body") || name.contains("crystal legs") || name.contains("crystal boots")
				|| name.contains("crystal glove") || name.contains("torva") || name.contains("virtus")
				|| name.contains("pernix") || name.contains("zaryte") || name.contains("crystal bow")
				|| name.contains("crystal dagger") || name.contains("crystal halberd") || name.contains("crystal staff")
				|| name.contains("crystal wand") || name.contains("crystal orb") || name.contains("crystal shield")
				|| name.contains("crystal deflector") || name.contains("crystal chakram") || name.contains("obsidian")
				|| name.contains("crystal ward") || name.contains("ahrim") || name.contains("dharok")
				|| name.contains("guthan") || name.contains("karil") || name.contains("torag") || name.contains("verac")
				|| name.contains("sunspear") || name.contains("leviathan") || name.contains("reefwalker")
				|| name.contains("skeletal") || name.contains("karil") || name.contains("linza")
				|| name.contains("akrisae") || name.contains("goliath glove") || name.contains("spellcaster glove")
				|| name.contains("swift glove") || name.contains("fungal") || name.contains("grifolic")
				|| name.contains("ripper claw") || name.contains("polypore") || name.contains("chaotic")
				|| name.contains("gravite") || name.contains("farseer") || name.contains("eagle-eye")
				|| name.contains("cywir elder") || name.contains("praesul") || name.contains("camel staff")
				|| name.contains("mizuyari") || name.contains("avaryss") || name.contains("nymora")
				|| name.contains("dragon rider lance") || name.contains("shadow glaive") || name.contains("primeval")
				|| name.contains("tempest") || name.contains("teralith") || name.contains("arcane blood necklace")
				|| name.contains("blood amulet of fury") || name.contains("brawler's blood necklace")
				|| name.contains("farsight blood necklace") || name.contains("decimation")
				|| name.contains("imperium core");
	}

	/*
	 * Default Item Charges
	 */

	public static int getItemDefaultCharges(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();

		if (name.contains("nightmare gauntlet"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("mizuyari"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("blade of avaryss") || name.contains("blade of nymora"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("arcane blood necklace") || name.contains("blood amulet of fury")
				|| name.contains("brawler's blood necklace"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("dragon rider lance"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("shadow glaive"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("camel staff") || name.contains("wyvern crossbow") || name.contains("ripper claw"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("decimation"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("seren godbow") || name.contains("zaros godsword") || name.contains("staff of sliske"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("superior leviathon") || name.contains("superior reefwalker"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("primeval") || name.contains("tempest") || name.contains("teralith"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("razorback") || name.contains("celestial handwrap") || name.contains("ascension grips")
				|| name.contains("flarefrost") || name.contains("emberkeen") || name.contains("hailfire"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("superior seasinger") || name.contains("superior death lotus")
				|| name.contains("superior tetsu"))
			return 72000 * Settings.getDegradeGearRate();

		if (name.contains("seasinger") || name.contains("death lotus") || name.contains("tetsu"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("(blood)") || name.contains("(barrows)") || name.contains("(shadow)")
				|| name.contains("(Third Age)"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("malevolent") || name.contains("vengeful") || name.contains("merciless"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("sirenic"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("drygore") || name.contains("kalphite"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("ascension") && name.contains("crossbow"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("siesmic"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("noxious"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("khopesh"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("defender"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("corrupt vesta") || name.contains("corrupt statius") || name.contains("corrupt zuriel")
				|| name.contains("corrupt morrigan"))
			return 1500 * Settings.getDegradeGearRate();

		if (name.contains("corrupt dragon"))
			return 2000 * Settings.getDegradeGearRate();

		if (name.contains("vesta") || name.contains("statius") || name.contains("zuriel") || name.contains("morrigan"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("attuned crystal helm") || name.contains("attuned crystal body")
				|| name.contains("attuned crystal legs") || name.contains("attuned crystal boots")
				|| name.contains("attuned crystal glove"))
			return 80000 * Settings.getDegradeGearRate();

		if (name.contains("crystal helm") || name.contains("crystal body") || name.contains("crystal legs")
				|| name.contains("crystal boots") || name.contains("crystal glove"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("torva") || name.contains("virtus") || name.contains("pernix") || name.contains("zaryte"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("crystal bow") || name.contains("crystal ward") || name.contains("crystal chakram")
				|| name.contains("crystal dagger") || name.contains("crystal halberd") || name.contains("crystal staff")
				|| name.contains("crystal wand") || name.contains("crystal orb") || name.contains("crystal deflector")
				|| name.contains("crystal shield"))
			return 50000 * Settings.getDegradeGearRate();

		if (name.contains("ahrim") || name.contains("dharok") || name.contains("guthan") || name.contains("karil")
				|| name.contains("torag") || name.contains("verac") || name.contains("karil") || name.contains("linza")
				|| name.contains("akrisae"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("goliath glove") || name.contains("spellcaster glove") || name.contains("swift glove"))
			return 30000 * Settings.getDegradeGearRate();

		if (name.contains("neem oil"))
			return 2000 * Settings.getDegradeGearRate();

		if (name.contains("obsidian"))
			return 4000 * Settings.getDegradeGearRate();

		if (name.contains("chaotic") || name.contains("gravite"))
			return 30000 * Settings.getDegradeGearRate();

		if (name.contains("fungal") || name.contains("polypore") || name.contains("grifolic"))
			return 30000 * Settings.getDegradeGearRate();

		if (name.contains("anti-dragon shield (mole)"))
			return 500 * Settings.getDegradeGearRate();

		if (name.contains("royal crossbow"))
			return 30000 * Settings.getDegradeGearRate();

		if (name.contains("lava whip") || name.contains("staff of darkness") || name.contains("strykebow"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("refined anima core"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("tokkul-zo"))
			return 4000 * Settings.getDegradeGearRate();

		if (name.contains("amulet of souls") || name.contains("reaper necklace") || name.contains("ring of death")
				|| name.contains("deathtouch bracelet"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("superior rock-shell") || name.contains("superior skeletal") || name.contains("leviathan")
				|| name.contains("reefwalker"))
			return 100000 * Settings.getDegradeGearRate();

		if (name.contains("cywir elder"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("praesul"))
			return 60000 * Settings.getDegradeGearRate();

		if (name.contains("imperium core"))
			return 30000 * Settings.getDegradeGearRate();
		return -1;
	}

	/*
	 * Item's That Don't Degrade On Death
	 */

	public static boolean itemDegradesInDeath(int id) {
		if (id == 22460 || id == 22464 || id == 22468 || id == 22472 || id == 22476 || id == 22480 || id == 22484
				|| id == 22488 || id == 22492)
			return false;// polypore
		if (id == 18349 || id == 18351 || id == 18353 || id == 18355 || id == 18357 || id == 18359 || id == 18361
				|| id == 18363 || id == 18365 || id == 18367 || id == 18369 || id == 18371 || id == 18373)
			return false;// chaotics
		return true;
	}

	/*
	 * What Degrades Item's Degrade Into When At 0 Charges
	 */

	public static int getItemDegrade(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		if (id == 11283)
			return 11284;
		if (name.contains("vesta") || name.contains("statius") || name.contains("zuriel") || name.contains("morrigan"))
			return -1;
		if (id == 30781) // anti-dragon shield (mole)
			return 11284;
		if (id == 24339) // royal crossbow
			return id + 1;
		if (id == 34150) // lava whip
			return 34151;
		if (id == 34155) // staff of darkness
			return 34153;
		if (id == 34158) // strykebow
			return 34156;
		if (id == 30580) // superior leviathan ring
			return id + 1;
		if (id == 30572) // superior reefwalker's cape
			return id + 1;
		if (id == 35987) // wyvern crossbow
			return id + 1;
		if (id == 36021) // camel staff
			return id + 1;
		if (name.contains("ripper claw")) // ripper claw
			return id + 1;
		if (name.contains("shadow glaive")) // shadow glaive
			return id + 1;
		if (name.contains("cywir elder")) // wand of cywir elder
			return id + 1;
		if (name.contains("amulet of souls")) // amulet of souls
			return id + 3;
		if (name.contains("deathtouch bracelet")) // deathtouch bracelet
			return id + 1;
		if (name.contains("reaper necklace")) // reaper necklace
			return id + 5;
		if (name.contains("ring of death")) // ring of death
			return id + 7;
		if (name.contains("dragon rider lance")) // dragon rider lance
			return id + 1;
		if (name.contains("blade of nymora") || name.contains("blade of avaryss")) // blade
																					// of
																					// nymora
																					// &
																					// blade
																					// of
																					// avaryss
			return id + 1;
		if (name.contains("mizuyari")) // mizuyari
			return id + 1;
		if (id == 35159) // achto primeval helm
			return id - 80;
		if (id == 35161) // achto primeval gloves
			return id - 79;
		if (id == 35163) // achto primeval boots
			return id - 78;
		if (id == 35165) // achto primeval body
			return id - 77;
		if (id == 35167) // achto primeval legs
			return id - 76;
		if (id == 35189) // achto tempest head
			return id - 60;
		if (id == 35191) // achto tempest gloves
			return id - 59;
		if (id == 35193) // achto tempest boots
			return id - 58;
		if (id == 35195) // achto tempest body
			return id - 57;
		if (id == 35197) // achto tempest legs
			return id - 56;
		if (id == 35174) // achto teralith helm
			return id - 70;
		if (id == 35176) // achto teralith gloves
			return id - 69;
		if (id == 35178) // achto teralith boots
			return id - 68;
		if (id == 35180) // achto teralith body
			return id - 67;
		if (id == 35182) // achto teralith legs
			return id - 66;
		if (name.contains("seren godbow") || name.contains("zaros godsword") || name.contains("staff of sliske"))
			return id + 3;
		if (name.contains("primeval") || name.contains("tempest") || name.contains("teralith"))
			return -1;
		if (name.contains("nightmare gauntlet"))
			return id + 1;
		if (name.contains("superior seasinger") || name.contains("superior death lotus")
				|| name.contains("superior tetsu"))
			return id + 9;
		if (name.contains("sea singer") || name.contains("death lotus") || name.contains("tetsu"))
			return id + 3;
		if (name.contains("razorback") || name.contains("celestial handwrap") || name.contains("ascension grips"))
			return -1;
		if (name.contains("hailfire"))
			return 21793;
		if (name.contains("flarefrost"))
			return 21790;
		if (name.contains("emberkeen"))
			return 21787;
		// Dyed T90
		if (name.contains("(blood)") || name.contains("(barrows)") || name.contains("(shadow)")
				|| name.contains("(Third Age)"))
			return id + 1;
		// ROTS
		if (name.contains("vengeful") || name.contains("merciless"))
			return id + 3;
		if (name.contains("malevolent") && !name.contains("energy"))
			return -1;
		// Sirenic
		if (name.contains("sirenic"))
			return -1;
		// Tectonic
		if (name.contains("tectonic"))
			return -1;
		// Siesmic
		if (id == 28617 || id == 28621)
			return id + 3;
		if (id == 33324 || id == 33327 || id == 33390 || id == 33393 || id == 33456 || id == 33459 || id == 36327
				|| id == 36330)
			return id + 2;
		if (name.contains("ancient defender"))
			return id + 1;
		if (name.contains("corrupted defender") || name.contains("kalphite defender"))
			return id + 1;
		if (id == 22444) // neem oil
			return 1935;
		// crystal armor
		if (id == 35402 || id == 35406 || id == 35410 || id == 35414 || id == 35418)
			return id + 1;
		if (name.contains("khopesh"))
			return id + 1;
		// noxious default
		if (id == 31727 || id == 31731 || id == 31735)
			return id + 1;
		// attuned crystal armor
		if (id == 35421 || id == 35424 || id == 35427 || id == 35430 || id == 35433)
			return id + 1;
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161
				|| id == 20165 || id == 20169)
			return id + 1;
		// dung items (chaotics)
		if (id == 18349 || id == 18351 || id == 18353 || id == 18355 || id == 18357 || id == 18359 || id == 18361
				|| id == 18363 || id == 18365 || id == 18367 || id == 18369 || id == 18371 || id == 18373)
			return id + 1;
		// new barrows equipment
		if (id == 4708 || id == 4710 || id == 4712 || id == 4714 || id == 4716 || id == 4718 || id == 4720 || id == 4722
				|| id == 4724 || id == 4726 || id == 4728 || id == 4730 || id == 4732 || id == 4734 || id == 4736
				|| id == 4738)
			return 4856 + (((id - 4708) / 2) * 6);
		if (id == 4745 || id == 4747 || id == 4749 || id == 4751 || id == 4753 || id == 4756 || id == 4759)
			return 4952 + (((id - 4745) / 2) * 6);
		if (id == 21736 || id == 21744 || id == 21752 || id == 21760)
			return id + 2;
		// barrows degraded
		if ((id >= 4856 && id <= 4859) || (id >= 4862 && id <= 4865) || (id >= 4868 && id <= 4871)
				|| (id >= 4874 && id <= 4877) || (id >= 4880 && id <= 4883) || (id >= 4886 && id <= 4889)
				|| (id >= 4892 && id <= 4895) || (id >= 4898 && id <= 4901) || (id >= 4904 && id <= 4907)
				|| (id >= 4910 && id <= 4913) || (id >= 4916 && id <= 4919) || (id >= 4922 && id <= 4925)
				|| (id >= 4928 && id <= 4931) || (id >= 4934 && id <= 4937) || (id >= 4940 && id <= 4943)
				|| (id >= 4946 && id <= 4949) || (id >= 4952 && id <= 4955) || (id >= 4958 && id <= 4961)
				|| (id >= 4964 && id <= 4967) || (id >= 4970 && id <= 4973) || (id >= 4976 && id <= 4979)
				|| (id >= 4982 && id <= 4985) || (id >= 4988 && id <= 4991) || (id >= 4994 && id <= 4997))
			return id + 1;
		// Crystal bow
		if (id == 4214 || id == 4215 || id == 4216 || id == 4217 || id == 4218 || id == 4219 || id == 4220 || id == 4221
				|| id == 4222)
			return id + 1;
		// Crystal shield
		if (id == 4225 || id == 4226 || id == 4227 || id == 4228 || id == 4229 || id == 4230 || id == 4231 || id == 4232
				|| id == 4233)
			return id + 1;
		if (id == 4223 || id == 4234)
			return 4207;
		// visor
		if (id == 22460 || id == 22472 || id == 22484)
			return 22452;
		if (id == 22464 || id == 22476 || id == 22488)
			return 22454;
		if (id == 22468 || id == 22480 || id == 22492)
			return 22456;
		if (id == 22496) // polypore staff
			return 22498; // stick
		if (id == 20173)
			return 20174;
		return -1;
	}

	/*
	 * When Wearing A Fully Charged Item, What It Turns Into
	 */

	public static int getDegradeItemWhenWear(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		if (id == 39248)// nightmare gauntlets
			return id + 2;

		if (id == 35420 || id == 35423 || id == 35426 || id == 35429 || id == 35432) // attuned
																						// crystal
																						// armor
																						// set
			return id + 1;

		if (id == 35400 || id == 35404 || id == 35408 || id == 35412 || id == 35416)// crystal
																					// armor
																					// set
			return id + 2;

		if (id == 36004)// ripper claw
			return id + 2;

		if (id == 36008)// off-hand ripper claw
			return id + 2;

		if (id == 36019)// camel staff
			return id + 2;

		if (id == 37090)// blade of nymora
			return id + 3;

		if (id == 37095)// blade of avaryss
			return id + 3;

		if (id == 37075)// shadow glaive
			return id + 3;

		if (id == 37080)// off-hand shadow glaive
			return id + 3;

		if (id == 37070)// dragon rider lance
			return id + 3;

		if (id == 37824)// mizuyari
			return id + 2;

		if (id == 37085)// wand of the cywir elder
			return id + 3;

		if (id == 35159) // achto primeval helm
			return id + 81;

		if (id == 35161) // achto primeval gloves
			return id + 80;

		if (id == 35163) // achto primeval boots
			return id + 79;

		if (id == 35165) // achto primeval body
			return id + 78;

		if (id == 35167) // achto primeval legs
			return id + 77;

		if (id == 35189) // achto tempest head
			return id + 61;

		if (id == 35191) // achto tempest gloves
			return id + 60;

		if (id == 35193) // achto tempest boots
			return id + 59;

		if (id == 35195) // achto tempest body
			return id + 58;

		if (id == 35197) // achto tempest legs
			return id + 57;

		if (id == 35174) // achto teralith helm
			return id + 71;

		if (id == 35176) // achto teralith gloves
			return id + 70;

		if (id == 35178) // achto teralith boots
			return id + 69;

		if (id == 35180) // achto teralith body
			return id + 68;

		if (id == 35182) // achto teralith legs
			return id + 67;

		if (id == 37632 || id == 37640 || id == 37636) // seren godbow, zaros
														// godsword, staff of
														// sliske
			return id + 2;

		if (id == 26337 || id == 26338 || id == 26339) // seasingers hood, top,
														// bottom
			return id + 3;

		if (id == 26346 || id == 26347 || id == 26348) // death lotus hood, top,
														// bottom
			return id + 3;

		if (id == 26325 || id == 26326 || id == 26327) // tetsu hood, top,
														// bottom
			return id + 3;

		if (id == 33879 || id == 33882) // tetsu weapons
			return id + 1;

		if (id == 33886 || id == 33889) // seasinger weapons
			return id + 1;

		if (id == 36153 || id == 36160) // corrupted/kalphite defender
			return id + 1;

		if (id == 30213 || id == 31189 || id == 31203 || id == 34981 || id == 34978 || id == 34984)// razorback/handwraps/ascension
																									// grips/flarefrost
																									// boots/emberkeen
																									// boots/hailfire
																									// boots
			return id + 2;

		if (id == 36276 || id == 36279 || id == 36282 || id == 36285 || id == 36288 || id == 36291 || id == 36294
				|| id == 36297 || id == 36300 || id == 36303 || id == 36306 || id == 36309 || id == 36312 || id == 36315
				|| id == 36318 || id == 36321 || id == 36324 || id == 36327 || id == 36330 || id == 36333 || id == 36336
				|| id == 36339) // blood dyed items
			return id + 1;

		if (id == 33366 || id == 33369 || id == 33372 || id == 33375 || id == 33378 || id == 33381 || id == 33384
				|| id == 33387 || id == 33390 || id == 33393 || id == 33396 || id == 33399 || id == 33402 || id == 33405
				|| id == 33408 || id == 33411 || id == 33414 || id == 33417 || id == 33420 || id == 33423 || id == 33426
				|| id == 33429) // shadow dyed items
			return id + 1;

		if (id == 33366 || id == 33369 || id == 33372 || id == 33375 || id == 33378 || id == 33381 || id == 33384
				|| id == 33387 || id == 33390 || id == 33393 || id == 33396 || id == 33399 || id == 33402 || id == 33405
				|| id == 33408 || id == 33411 || id == 33414 || id == 33417 || id == 33420 || id == 33423 || id == 33426
				|| id == 33429) // shadow dyed items
			return id + 1;

		if (id == 33432 || id == 33435 || id == 33438 || id == 33441 || id == 33444 || id == 33447 || id == 33450
				|| id == 33453 || id == 33456 || id == 33459 || id == 33462 || id == 33465 || id == 33468 || id == 33471
				|| id == 33474 || id == 33477 || id == 33480 || id == 33483 || id == 33486 || id == 33489 || id == 33492
				|| id == 33495) // third age dyed items
			return id + 1;

		if (id == 33300 || id == 33303 || id == 33306 || id == 33309 || id == 33312 || id == 33315 || id == 33318
				|| id == 33321 || id == 33324 || id == 33327 || id == 33330 || id == 33333 || id == 33336 || id == 33339
				|| id == 33342 || id == 33345 || id == 33348 || id == 33351 || id == 33354 || id == 33357 || id == 33360
				|| id == 33363) // barrows dyed items
			return id + 1;

		if (id == 29854 || id == 29857 || id == 29860) // sirenic helm, body,
														// legs
			return id + 2;

		if (id == 30005 || id == 30008 || id == 30011) // malevolent helm, body,
														// legs
			return id + 2;

		if (id == 28608 || id == 28611 || id == 28614) // tectonic helm, body,
														// legs
			return id + 2;

		if (id == 40312 || id == 40316) // khopesh
			return id + 2;

		if (id == 30579 || id == 30579) // superior leviathan & superior
										// reefwalker's cape
			return id + 1;

		if (id == 31725 || id == 31729 || id == 31733) // noxious weapons
			return id + 2;

		if (id == 28617 || id == 28621) // siesmic wand/singularity
			return id + 2;

		if (id == 30780) // anti-dragon shield (mole)
			return id + 1;

		if (id == 24338) // royal crossbow
			return id + 1;

		if (id == 31875 || id == 31878 || id == 31872 || id == 31869) // amulet
																		// of
																		// souls
			return id + 2;

		if (id == 13780 || id == 13783 || id == 31736) // non corrupt morrigans
			return id + 2;

		// Pvp armors
		if (id == 13958 || id == 13961 || id == 13964 || id == 13967 || id == 13970 || id == 13973 || id == 13908
				|| id == 13911 || id == 13914 || id == 13917 || id == 13920 || id == 13923 || id == 13941 || id == 13944
				|| id == 13947 || id == 13950 || id == 13958 || id == 13938 || id == 13926 || id == 13929 || id == 13932
				|| id == 13935)
			return id + 2; // When equipping it becomes Corrupted
		return -1;
	}

	// returns what it degrades into when start to combating(usualy first time)
	public static int getDegradeItemWhenCombating(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		// defenders
		if (id == 36160 || id == 36153)
			return id + 1;
		// khopesh
		if (id == 40312 || id == 40316)
			return id + 1;
		// Nex and Pvp
		if (id == 20135 || id == 20139 || id == 20143 || id == 20147 || id == 20151 || id == 20155 || id == 20159
				|| id == 20163 || id == 20167 || id == 20171 || id == 13858 || id == 13861 || id == 13864 || id == 13867
				|| id == 13870 || id == 13873 || id == 13876 || id == 13884 || id == 13887 || id == 13890 || id == 13893
				|| id == 13896 || id == 13905 || id == 13902 || id == 13899)
			return id + 2;
		if (id == 35400 || id == 35404 || id == 35408 || id == 35416 || id == 35412)
			return id + 2;
		// noxious default
		if (id == 31725 || id == 31729 || id == 31733)
			return id + 2;
		// noxious scythe dyed
		if (id == 33330 || id == 33396 || id == 33462 || id == 36333)
			return id + 1;
		// noxious staff dyed
		if (id == 33333 || id == 33393 || id == 33465 || id == 36336)
			return id + 1;
		// noxious longbow dyed
		if (id == 33336 || id == 33402 || id == 33468 || id == 36339)
			return id + 1;
		// polipore armors
		if (id == 22458 || id == 22462 || id == 22466 || id == 22470 || id == 22474 || id == 22478 || id == 22482
				|| id == 22486 || id == 22490)
			return id + 2;
		// Crystal bow new
		if (id == 4212)
			return id + 2;
		// Crystal bow shield new
		if (id == 4224)
			return id + 1;
		// polypore staff
		if (id == 22494)
			return id + 2;
		// new barrows equipment
		if (id == 4708 || id == 4710 || id == 4712 || id == 4714 || id == 4716 || id == 4718 || id == 4720 || id == 4722
				|| id == 4724 || id == 4726 || id == 4728 || id == 4730 || id == 4732 || id == 4734 || id == 4736
				|| id == 4738)
			return 4856 + (((id - 4708) / 2) * 6);
		if (id == 4745 || id == 4747 || id == 4749 || id == 4751 || id == 4753 || id == 4756 || id == 4759)
			return 4952 + (((id - 4745) / 2) * 6);
		if (id == 21736 || id == 21744 || id == 21752 || id == 21760)
			return id + 2;
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 4225 || id == 4226 || id == 4227 || id == 4228 || id == 4229 || id == 4230 || id == 4231 || id == 4232
				|| id == 4233 || id == 4234)
			return true;
		return false;
	}

	/*
	 * Item's That Remove A Charge Per Server Tick
	 */

	public static boolean itemDegradesWhileWearing(int id) {
		return false;
	}

	// removes a charge per ticket when wearing this and attacking
	public static boolean itemDegradesWhileCombating(int id) {
		if (isDegradable(id))
			return true;
		return false;
	}

	public static int getItemFixed(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		return -1;
	}

	public static int getDegradeItemOnDeath(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		if (name.contains("akrisae's hood")) {
			return 21742;
		}
		if (name.contains("akrisae's war mace")) {
			return 21750;
		}
		if (name.contains("akrisae's robe top")) {
			return 21758;
		}
		if (name.contains("akrisae's robe skirt")) {
			return 21766;
		}
		if (name.contains("dharok's helm")) {
			return 4884;
		}
		if (name.contains("dharok's greataxe")) {
			return 4890;
		}
		if (name.contains("dharok's platebody")) {
			return 4896;
		}
		if (name.contains("dharok's platelegs")) {
			return 4902;
		}
		if (name.contains("verac's helm")) {
			return 4980;
		}
		if (name.contains("verac's flail")) {
			return 4986;
		}
		if (name.contains("verac's brassard")) {
			return 4992;
		}
		if (name.contains("verac's plateskirt")) {
			return 4998;
		}
		if (name.contains("torag's helm")) {
			return 4956;
		}
		if (name.contains("torag's hammers")) {
			return 4962;
		}
		if (name.contains("torag's platebody")) {
			return 4968;
		}
		if (name.contains("torag's platelegs")) {
			return 4974;
		}
		if (name.contains("karil's coif")) {
			return 4932;
		}
		if (name.contains("karil's crossbow")) {
			return 4938;
		}
		if (name.contains("karil's top")) {
			return 4944;
		}
		if (name.contains("karil's skirt")) {
			return 4950;
		}
		if (name.contains("guthan's helm")) {
			return 4908;
		}
		if (name.contains("guthan's warspear")) {
			return 4914;
		}
		if (name.contains("guthan's platebody")) {
			return 4920;
		}
		if (name.contains("guthan's chainskirt")) {
			return 4926;
		}
		if (name.contains("ahrim's hood")) {
			return 4860;
		}
		if (name.contains("ahrim's staff")) {
			return 4866;
		}
		if (name.contains("ahrim's robe top")) {
			return 4872;
		}
		if (name.contains("ahrim's robe skirt")) {
			return 4878;
		}
		if (name.contains("torva full helm")) {
			return 20138;
		}
		if (name.contains("torva platebody")) {
			return 20142;
		}
		if (name.contains("torva platelegs")) {
			return 20146;
		}
		if (name.contains("pernix cowl")) {
			return 20150;
		}
		if (name.contains("pernix body")) {
			return 20154;
		}
		if (name.contains("pernix chaps")) {
			return 20158;
		}
		if (name.contains("virtus mask")) {
			return 20162;
		}
		if (name.contains("virtus robe top")) {
			return 20166;
		}
		if (name.contains("virtus robe legs")) {
			return 20170;
		}
		if (name.contains("zaryte bow")) {
			return 20174;
		}
		return -1;
	}

	public static int getItemDustOnDeath(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		if (name.contains("statius's")) {
			return 592;
		}
		if (name.contains("vesta's")) {
			return 592;
		}
		if (name.contains("morrigan's")) {
			return 592;
		}
		if (name.contains("zuriel's")) {
			return 592;
		}
		return -1;
	}

	public static boolean canWear(Item item, Player player) {
		return canWear(item, player, false);
	}

	public static boolean canWear(Item item, Player player, boolean keepSake) {
		switch (item.getDefinitions().getName().toLowerCase().replace("_", " ")) {
		case "charming imp":
		case "bonecrusher":
		case "herbicide":
			return true;
		}
		if (item.getId() == 18337) {
			if (!keepSake) 
			player.getPackets().sendGameMessage("You cannot wear that.");
			return false;
		}

		if (player.getRights() >= 2)
			return true;

		if (!item.getDefinitions().isWearItem())
			return false;

		if (item.getId() == 32055 || item.getId() == 32059) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need an Agility level of 99 to use this item.");
				return false;
			}

			if (player.getSkills().getLevel(Skills.DUNGEONEERING) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Dungoneering level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.SLAYER) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Slayer level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.THIEVING) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Thieving level of 99 to use this item.");
				return false;
			}
			return true;
		}

		if (item.getId() == 32052 || item.getId() == 32056) {
			if (player.getSkills().getLevel(Skills.FARMING) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Farming level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.FISHING) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Fishing level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.HUNTER) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Hunter level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.MINING) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Mining level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.WOODCUTTING) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need a Woodcutting level of 99 to use this item.");
				return false;
			}
			return true;
		}

		if (item.getId() == 32053 || item.getId() == 32057) {
			if (player.getSkills().getLevel(Skills.ATTACK) < 99) {
				if (!keepSake) 
				player.getPackets().sendGameMessage("You need an Attack level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.HITPOINTS) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Constitutation level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.DEFENCE) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Defence level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.MAGIC) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Magic level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.PRAYER) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Prayer level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.RANGE) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Ranged level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.STRENGTH) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Strength level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.SUMMONING) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Summoning level of 99 to use this item.");
				return false;
			}
			return true;
		}

		if (item.getId() == 32054 || item.getId() == 32058) {
			if (player.getSkills().getLevel(Skills.COOKING) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Cooking level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Construction level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.CRAFTING) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Crafting level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.FIREMAKING) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Firemaking level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.FLETCHING) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Fletching level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.HERBLORE) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Herblore level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.RUNECRAFTING) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Runecrafting level of 99 to use this item.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.SMITHING) < 99) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need a Smithing level of 99 to use this item.");
				return false;
			}

			return true;
		}

		/*
		 * Expert capes end
		 */

		/*
		 * 120 capes
		 */
		if (item.getId() == 31277 && player.getSkills().getXp(Skills.AGILITY) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31268 && player.getSkills().getXp(Skills.ATTACK) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31269 && player.getSkills().getXp(Skills.STRENGTH) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31270 && player.getSkills().getXp(Skills.DEFENCE) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31271 && player.getSkills().getXp(Skills.RANGE) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31272 && player.getSkills().getXp(Skills.PRAYER) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31273 && player.getSkills().getXp(Skills.MAGIC) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31274 && player.getSkills().getXp(Skills.RUNECRAFTING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31275 && player.getSkills().getXp(Skills.CONSTRUCTION) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}

		if (item.getId() == 31276 && player.getSkills().getXp(Skills.HITPOINTS) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}

		if (item.getId() == 31278 && player.getSkills().getXp(Skills.HERBLORE) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31279 && player.getSkills().getXp(Skills.THIEVING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31280 && player.getSkills().getXp(Skills.CRAFTING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31281 && player.getSkills().getXp(Skills.FLETCHING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31282 && player.getSkills().getXp(Skills.SLAYER) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31283 && player.getSkills().getXp(Skills.HUNTER) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31285 && player.getSkills().getXp(Skills.MINING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31286 && player.getSkills().getXp(Skills.SMITHING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31287 && player.getSkills().getXp(Skills.FISHING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31288 && player.getSkills().getXp(Skills.COOKING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31289 && player.getSkills().getXp(Skills.FIREMAKING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31290 && player.getSkills().getXp(Skills.WOODCUTTING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31291 && player.getSkills().getXp(Skills.FARMING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}
		if (item.getId() == 31292 && player.getSkills().getXp(Skills.SUMMONING) < 104273167) {
			if (!keepSake)
			player.getPackets().sendGameMessage("You need to have 104,273,167 xp in order to wear this item");
			return false;
		}

		/*
		 * End of 120 capes
		 */
		else if (item.getId() == 20767) {
			for (int skill = 0; skill < 25; skill++) {
				if (player.getSkills().getXp(skill) < 13034431 && player.getSkills().getXp(26) < 36073511) {
					if (!keepSake)
					player.getPackets().sendGameMessage("You must have the maximum level of each skill in order to use this cape.");
					return false;
				}

			}
		} else if (item.getId() == 6570 || item.getId() == 10566 || item.getId() == 10637) {
			if (!player.isCompletedFightCaves()) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need to complete at least once fight cave minigame to use this cape.");
				return false;
			}
		} else if (item.getId() == 23639 || item.getId() == 23659 || item.getId() == 31610 || item.getId() == 31611) {
			if (!player.isCompletedFightKiln()) {
				if (!keepSake)
				player.getPackets().sendGameMessage("You need to complete at least once fight kiln minigame to use this cape.");
				return false;
			}
		} else if (item.getId() == 15433 || item.getId() == 15435 || item.getId() == 15432 || item.getId() == 15434) {
			if (!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
				player.getPackets().sendGameMessage("You need to have completed Nomad's Requiem miniquest to use this cape.");
				return false;
			}
		}
		return true;
	}

	public static boolean isDestroy(Item item) {
		return item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended();
	}

	public static boolean isTradeable(Item item) {
		try {
			if (item.getAttributes() != null)
				return false;
			Scanner scanner = new Scanner(new File("data/items/untradables.txt"));
			ArrayList<Integer> itemIds = new ArrayList<Integer>();
			while (scanner.hasNextLine()) {
				itemIds.add(scanner.nextInt());
			}
			for (int i = 0; i < itemIds.size(); i++) {
				if (item.getId() == itemIds.get(i)) {
					return false;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static final int[][] REPAIR = {
			// Chaotic weapons
			{ 18349, 2000000, 50000 }, { 18351, 2000000, 50000 }, { 18353, 2000000, 50000 }, { 18355, 2000000, 50000 },
			{ 18357, 2000000, 50000 }, { 18359, 2000000, 50000 }, { 18361, 2000000, 50000 }, { 18363, 2000000, 50000 },
			{ 25991, 1000000, 50000 }, { 25993, 1000000, 50000 }, { 25995, 1000000, 50000 }, { 27069, 2000000, 50000 },
			{ 27071, 1000000, 50000 }, { 31463, 2000000, 50000 }, };

	private static final int ITEMS_RANGE = 500;

	private static final int[][] RECOLOR_PRICES = { { 2581, 2500 }, };

	public static boolean handleRecolorItem(Player player, Item item) {
		if (item.getId() == 2581 || item.getId() == 9470 || item.getId() == 15486)
			player.getDialogueManager().startDialogue("RecolorItemD", item);
		else
			player.getDialogueManager().startDialogue("SimpleNPCMessage", 13727, "You can't recolor that item.");
		return true;
	}

	public static boolean repairItem(Player player, int slot, boolean dungeoneering) {
		Item item = player.getInventory().getItem(slot);
		int[] prices = getRepairIdx(item.getId());
		if (prices == null && !isRepairable(item.getName()))
			return false;
		String itemName = item.getName().toLowerCase().replace(" (broken)", "").replace(" (damaged)", "")
				.replace(" 0", "").replace(" 25", "").replace(" 50", "").replace(" 75", "").replace(" 100", "");
		if (itemName.contains("(worn"))
			player.getPackets().sendGameMessage("You cannot repair this item.");
		for (int nextId = item.getId() - ITEMS_RANGE; nextId < item.getId() + ITEMS_RANGE; nextId++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(nextId);
			if (def == null || !def.getName().toLowerCase().contains(itemName))
				continue;
			prices = getRepairIdx(nextId);
			if (prices == null)
				return false;
			String indexName = item.getName().toLowerCase().replace(itemName, "").replace(" (", "").replace(")", "")
					.replace(" ", "");
			if (indexName.equals("")) {
				int charges = player.getCharges().getCharges(item.getId());
				if (charges == 0) {
					player.getPackets().sendGameMessage("The item doesn't have a dent in it.");
					return true;
				}
				double percentage = (double) charges / ItemConstants.getItemDefaultCharges(item.getId());
				percentage = 1.0 - percentage;
				prices[0] *= percentage;
			} else if (!indexName.equals("") && !indexName.equals("broken") && !itemName.equals("damaged"))
				prices[0] *= (1 - (Integer.parseInt(indexName) * .01));
			player.getDialogueManager().startDialogue("RepairD", slot, prices, nextId, dungeoneering);
			return true;
		}
		return false;
	}

	private static int[] getRepairIdx(int nextId) {
		for (int[] ids : REPAIR) {
			if (nextId == ids[0]) {
				if (ids.length == 3)
					return new int[] { ids[1], ids[2] };
				else
					return new int[] { ids[1] };
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static int[] getRecolorIdx(int nextId) {
		for (int[] ids : RECOLOR_PRICES) {
			if (nextId == ids[0])
				return new int[] { ids[1] };
		}
		return null;
	}

	private static boolean isRepairable(String itemName) {
		return itemName.endsWith(" (broken)") || itemName.endsWith(" (damaged)") || itemName.endsWith(" 0")
				|| itemName.endsWith(" 25") || itemName.endsWith(" 50") || itemName.endsWith(" 75")
				|| itemName.endsWith(" 100");
	}

	public static boolean degradeOnDrop(Item item) {
		if (item.getDefinitions().getName().toLowerCase().contains(" 100")
				|| item.getDefinitions().getName().toLowerCase().contains(" 75")
				|| item.getDefinitions().getName().toLowerCase().contains(" 50")
				|| item.getDefinitions().getName().toLowerCase().contains(" 25")) {
			return true;
		}
		return false;
	}

	public static boolean keptOnDeath(Item item) {
		if (item.getDefinitions().isLended()) {
			return true;
		}
		if (item.getId() == 19888) {
			return true;
		}
		if (item.getId() == 18839) {
			return true;
		}
		switch (item.getId()) {
		case 22899:
			return true;
		default:
			return false;
		}
	}
}