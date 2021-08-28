package com.rs.game.player.content.activities.skillingtask;

import java.io.Serializable;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.agility.Agility.AgilityCourses;

public enum SkillTasks implements Serializable {

	// Agility
	APE1("Ape Atoll Laps Medium",
			"You must complete 30 laps around the Ape Atoll Agility Course.",
			SkillerTasks.MEDIUM, 30, Skills.AGILITY, 48), APE2(
			"Ape Atoll Laps Hard",
			"You must complete 60 laps around the Ape Atoll Agility Course.",
			SkillerTasks.HARD, 60, Skills.AGILITY, 48), APE3(
			"Ape Atoll Laps Elite",
			"You must complete 90 laps around the Ape Atoll Agility Course.",
			SkillerTasks.ELITE, 90, Skills.AGILITY, 48), BARB1(
			"Barbarian Laps Easy",
			"You must complete 15 laps around the Barbarian Agility Course.",
			SkillerTasks.EASY, 15, Skills.AGILITY, 35), BARB2(
			"Barbarian Laps Medium",
			"You must complete 40 laps around the Barbarian Agility Course.",
			SkillerTasks.MEDIUM, 40, Skills.AGILITY, 35), BARB3(
			"Barbarian Adv Laps Hard",
			"You must complete 80 laps around the Barbarian Adv Agility Course.",
			SkillerTasks.HARD, 80, Skills.AGILITY, 90), BARB4(
			"Barbarian Adv Laps Elite",
			"You must complete 100 laps around the Barbarian Adv Agility Course.",
			SkillerTasks.ELITE, 100, Skills.AGILITY, 90), GNOME1(
			"Gnome Laps Easy",
			"You must complete 15 laps around the Gnome Agility Course.",
			SkillerTasks.EASY, 15, Skills.AGILITY, 1), GNOME2(
			"Gnome Laps Medium",
			"You must complete 40 laps around the Gnome Agility Course.",
			SkillerTasks.MEDIUM, 40, Skills.AGILITY, 1), GNOME3(
			"Gnome Laps Hard",
			"You must complete 80 laps around the Gnome Adv Agility Course.",
			SkillerTasks.HARD, 80, Skills.AGILITY, 85), PYRAMID1(
			"Pyramid Laps Hard",
			"You must complete 50 laps around the Pyramid Agility Course.",
			SkillerTasks.HARD, 50, Skills.AGILITY, 30), PYRAMID2(
			"Pyramid Laps Elite",
			"You must complete 75 laps around the Pyramid Agility Course.",
			SkillerTasks.ELITE, 75, Skills.AGILITY, 30), WILD1(
			"Wilderness Laps Medium",
			"You must complete 30 laps around the Wilderness Agility Course.",
			SkillerTasks.MEDIUM, 30, Skills.AGILITY, 52), WILD2(
			"Wilderness Laps Hard",
			"You must complete 60 laps around the Wilderness Agility Course.",
			SkillerTasks.HARD, 60, Skills.AGILITY, 52), WILD3(
			"Wilderness Laps Elite",
			"You must complete 90 laps around the Wilderness Agility Course.",
			SkillerTasks.ELITE, 90, Skills.AGILITY, 52),

	// Cooking
	CANCHOVIES1("Cook Anchovies Easy",
			"You must successfully cook 300 Raw Anchovies.", SkillerTasks.EASY,
			300, Skills.COOKING, 1), CANCHOVIES2("Cook Anchovies Medium",
			"You must successfully cook 500 Raw Anchovies.",
			SkillerTasks.MEDIUM, 500, Skills.COOKING, 1), CHERRING1(
			"Cook Herring Easy", "You must successfully cook 280 Raw Herring.",
			SkillerTasks.EASY, 280, Skills.COOKING, 5), CHERRING2(
			"Cook Herring Mediun",
			"You must successfully cook 450 Raw Herring.", SkillerTasks.MEDIUM,
			450, Skills.COOKING, 5), CLOBSTER1("Cook Lobster Medium",
			"You must successfully cook 400 Raw Lobster.", SkillerTasks.MEDIUM,
			400, Skills.COOKING, 40), CLOBSTER2("Cook Lobster Hard",
			"You must successfully cook 750 Raw Lobster.", SkillerTasks.HARD,
			750, Skills.COOKING, 40), CROCKTAIL1("Cook Rocktail Hard",
			"You must successfully cook 650 Raw Rocktail.", SkillerTasks.HARD,
			650, Skills.COOKING, 93), CROCKTAIL2("Cook Rocktail Elite",
			"You must successfully cook 830 Raw Rocktail.", SkillerTasks.ELITE,
			830, Skills.COOKING, 93), CSALMON1("Cook Salmon Easy",
			"You must successfully cook 220 Raw Salmon.", SkillerTasks.EASY,
			220, Skills.COOKING, 25), CSALMON2("Cook Salmon Medium",
			"You must successfully cook 400 Raw Salmon.", SkillerTasks.MEDIUM,
			400, Skills.COOKING, 25), CSHARK1("Cook Shark Medium",
			"You must successfully cook 350 Raw Shark.", SkillerTasks.MEDIUM,
			350, Skills.COOKING, 80), CSHARK2("Cook Shark Hard",
			"You must successfully cook 700 Raw Shark.", SkillerTasks.HARD,
			700, Skills.COOKING, 80), CSHARK3("Cook Shark Elite",
			"You must successfully cook 900 Raw Shark.", SkillerTasks.ELITE,
			900, Skills.COOKING, 80), CSHRIMP1("Cook Shrimp Easy",
			"You must successfully cook 300 Raw Shrimp.", SkillerTasks.EASY,
			300, Skills.COOKING, 1), CSHRIMP2("Cook Shrimp Medium",
			"You must successfully cook 500 Raw Shrimp.", SkillerTasks.MEDIUM,
			500, Skills.COOKING, 1), CSWORD1("Cook Swordfish Medium",
			"You must successfully cook 400 Raw Swordfish.",
			SkillerTasks.MEDIUM, 400, Skills.COOKING, 45), CSWORD2(
			"Cook Swordfish Hard",
			"You must successfully cook 750 Raw Swordfish.", SkillerTasks.HARD,
			750, Skills.COOKING, 45), CTROUT1("Cook Trout Easy",
			"You must successfully cook 250 Raw Trout.", SkillerTasks.EASY,
			250, Skills.COOKING, 15), CTROUT2("Cook Trout Medium",
			"You must successfully cook 420 Raw Trout.", SkillerTasks.MEDIUM,
			420, Skills.COOKING, 15), CTUNA1("Cook Tuna Easy",
			"You must successfully cook 200 Raw Tuna.", SkillerTasks.EASY, 200,
			Skills.COOKING, 30), CTUNA2("Cook Tuna",
			"You must successfully cook 380 Raw Tuna.", SkillerTasks.MEDIUM,
			380, Skills.COOKING, 30),

	// Crafting
	AMULET1("Craft Amulet Easy", "You must successfully craft 300 Amulets.",
			SkillerTasks.EASY, 300, Skills.CRAFTING, 8), AMULET2(
			"Craft Amulet Medium", "You must successfully craft 720 Amulets.",
			SkillerTasks.MEDIUM, 720, Skills.CRAFTING, 8), AMULET3(
			"Craft Amulet Hard", "You must successfully craft 1,200 Amulets.",
			SkillerTasks.HARD, 1200, Skills.CRAFTING, 8), AMULET4(
			"Craft Amulet Elite", "You must successfully craft 1,450 Amulets.",
			SkillerTasks.ELITE, 1450, Skills.CRAFTING, 8), BRACELET1(
			"Craft Bracelet Easy",
			"You must successfully craft 300 Bracelets.", SkillerTasks.EASY,
			300, Skills.CRAFTING, 7), BRACELET2("Craft Bracelet Medium",
			"You must successfully craft 720 Bracelets.", SkillerTasks.MEDIUM,
			720, Skills.CRAFTING, 7), BRACELET3("Craft Bracelet Hard",
			"You must successfully craft 1,200 Bracelets.", SkillerTasks.HARD,
			1200, Skills.CRAFTING, 7), BRACELET4("Craft Bracelet Elite",
			"You must successfully craft 1,450 Bracelets.", SkillerTasks.ELITE,
			1450, Skills.CRAFTING, 7), DIAMOND1("Cut Diamond Medium",
			"You must successfully cut 480 Uncut Diamonds.",
			SkillerTasks.MEDIUM, 480, Skills.CRAFTING, 43), DIAMOND2(
			"Cut Diamond Hard",
			"You must successfully cut 700 Uncut Diamonds.", SkillerTasks.HARD,
			700, Skills.CRAFTING, 43), EMERALD1("Cut Emerald Easy",
			"You must successfully cut 230 Uncut Emeralds.", SkillerTasks.EASY,
			230, Skills.CRAFTING, 27), EMERALD2("Cut Emerald Medium",
			"You must successfully cut 560 Uncut Emeralds.",
			SkillerTasks.MEDIUM, 560, Skills.CRAFTING, 27), NECKLACE1(
			"Craft Neaklace Easy", "You must successfully craft 300 Necklace.",
			SkillerTasks.EASY, 300, Skills.CRAFTING, 6), NECKLACE2(
			"Craft Necklace Medium",
			"You must successfully craft 720 Necklace.", SkillerTasks.MEDIUM,
			720, Skills.CRAFTING, 6), NECKLACE3("Craft Necklace Hard",
			"You must successfully craft 1,200 Necklace.", SkillerTasks.HARD,
			1200, Skills.CRAFTING, 6), NECKLACE4("Craft Necklace Elite",
			"You must successfully craft 1,450 Necklace.", SkillerTasks.ELITE,
			1450, Skills.CRAFTING, 6), RING1("Craft Ring Easy",
			"You must successfully craft 300 Rings.", SkillerTasks.EASY, 300,
			Skills.CRAFTING, 5), RING2("Craft Ring Medium",
			"You must successfully craft 720 Rings.", SkillerTasks.MEDIUM, 720,
			Skills.CRAFTING, 5), RING3("Craft Ring Hard",
			"You must successfully craft 1,200 Rings.", SkillerTasks.HARD,
			1200, Skills.CRAFTING, 5), RING4("Craft Ring Elite",
			"You must successfully craft 1,450 Rings.", SkillerTasks.ELITE,
			1450, Skills.CRAFTING, 5), RUBY1("Cut Ruby Easy",
			"You must successfully cut 220 Uncut Rubies.", SkillerTasks.EASY,
			220, Skills.CRAFTING, 34), RUBY2("Cut Ruby Medium",
			"You must successfully cut 520 Uncut Rubies.", SkillerTasks.MEDIUM,
			520, Skills.CRAFTING, 34), RUBY3("Cut Ruby Hard",
			"You must successfully cut 750 Uncut Rubies.", SkillerTasks.HARD,
			750, Skills.CRAFTING, 34), SAPPHIRE1("Cut Sapphire Easy",
			"You must successfully cut 250 Uncut Sapphires.",
			SkillerTasks.EASY, 250, Skills.CRAFTING, 20), SAPPHIRE2(
			"Cut Sapphire Medium",
			"You must successfully cut 600 Uncut Sapphires.",
			SkillerTasks.MEDIUM, 600, Skills.CRAFTING, 20),

	// Farming
	FLOWER1("Harvest Flowers Easy", "You must harvest 30 Flowers.",
			SkillerTasks.EASY, 30, Skills.FARMING, 2), FLOWER2(
			"Harvest Flowers Medium", "You must harvest 75 Flowers.",
			SkillerTasks.MEDIUM, 75, Skills.FARMING, 2), FLOWER3(
			"Harvest Flowers Hard", "You must harvest 130 Flowers.",
			SkillerTasks.HARD, 130, Skills.FARMING, 2), FLOWER4(
			"Harvest Flowers Elite", "You must harvest 180 Flowers.",
			SkillerTasks.ELITE, 180, Skills.FARMING, 2), FRUIT1(
			"Harvest Fruit Easy", "You must harvest 30 Fruit.",
			SkillerTasks.EASY, 30, Skills.FARMING, 27), FRUIT2(
			"Harvest Fruit Medium", "You must harvest 75 Fruit.",
			SkillerTasks.MEDIUM, 75, Skills.FARMING, 27), FRUIT3(
			"Harvest Fruit Hard", "You must harvest 130 Fruit.",
			SkillerTasks.HARD, 130, Skills.FARMING, 27), FRUIT4(
			"Harvest Fruit Elite", "You must harvest 180 Fruit.",
			SkillerTasks.ELITE, 180, Skills.FARMING, 27), HERB1(
			"Harvest Herbs Easy", "You must harvest 30 Herbs.",
			SkillerTasks.EASY, 30, Skills.FARMING, 9), HERB2(
			"Harvest Herbs Medium", "You must harvest 75 Herbs.",
			SkillerTasks.MEDIUM, 75, Skills.FARMING, 9), HERB3(
			"Harvest Herbs Hard", "You must harvest 130 Herbs.",
			SkillerTasks.HARD, 130, Skills.FARMING, 9), HERB4(
			"Harvest Herbs Elite", "You must harvest 180 Herbs.",
			SkillerTasks.ELITE, 180, Skills.FARMING, 9), RAKE1(
			"Rake Weeds Easy", "You must rake any patch 50 times.",
			SkillerTasks.EASY, 50, Skills.FARMING, 1), RAKE2(
			"Rake Weeds Medium", "You must rake any patch 120 times.",
			SkillerTasks.MEDIUM, 120, Skills.FARMING, 1), VEG1(
			"Harvest Vegetables Easy", "You must harvest 30 Vegetables.",
			SkillerTasks.EASY, 30, Skills.FARMING, 1), VEG2(
			"Harvest Vegetables Medium", "You must harvest 75 Vegetables.",
			SkillerTasks.MEDIUM, 75, Skills.FARMING, 1), VEG3(
			"Harvest Vegetables Hard", "You must harvest 130 Vegetables.",
			SkillerTasks.HARD, 130, Skills.FARMING, 1), VEG4(
			"Harvest Vegetables Elite", "You must harvest 180 Vegetables.",
			SkillerTasks.ELITE, 180, Skills.FARMING, 1),

	// Firemaking
	FBON1("Fuel Bonfire Medium", "You must fuel 750 logs into a Bonfire.",
			SkillerTasks.MEDIUM, 750, Skills.FIREMAKING, 1), FBON2(
			"Fuel Bonfire Hard", "You must fuel 1,600 logs into a Bonfire.",
			SkillerTasks.HARD, 1600, Skills.FIREMAKING, 1), FBON3(
			"Fuel Bonfire Elite", "You must fuel 2,000 logs into a Bonfire.",
			SkillerTasks.ELITE, 2000, Skills.FIREMAKING, 1), FOAK1(
			"Burn Oak Easy", "You must successfully burn 300 Oak Logs.",
			SkillerTasks.EASY, 300, Skills.FIREMAKING, 15), FOAK2(
			"Burn Oak Medium", "You must successfully burn 720 Oak Logs.",
			SkillerTasks.MEDIUM, 720, Skills.FIREMAKING, 15), FMAGIC1(
			"Magic Logs Hard", "You must successfully burn 830 Magic Logs.",
			SkillerTasks.HARD, 830, Skills.FIREMAKING, 75), FMAGIC2(
			"Magic Logs Elite", "You must successfully burn 1,050 Magic Logs.",
			SkillerTasks.ELITE, 1050, Skills.FIREMAKING, 75), FMAPLE1(
			"Burn Maple Medium", "You must successfully burn 820 Maple Logs.",
			SkillerTasks.MEDIUM, 820, Skills.FIREMAKING, 45), FMAPLE2(
			"Burn Maple Hard", "You must successfully burn 1,560 Maple Logs.",
			SkillerTasks.HARD, 1560, Skills.FIREMAKING, 45), FNORMAL1(
			"Burn Normal Easy", "You must successfully burn 350 Regular Logs.",
			SkillerTasks.EASY, 350, Skills.FIREMAKING, 1), FNORMAL2(
			"Burn Normal Medium",
			"You must successfully burn 760 Regular Logs.",
			SkillerTasks.MEDIUM, 760, Skills.FIREMAKING, 1), FWILLOW1(
			"Burn Willow Easy", "You must successfully burn 400 Willow Logs.",
			SkillerTasks.EASY, 400, Skills.FIREMAKING, 30), FWILLOW2(
			"Burn Willow Medium",
			"You must successfully burn 950 Willow Logs.", SkillerTasks.MEDIUM,
			950, Skills.FIREMAKING, 30), FWILLOW3("Burn Willow Hard",
			"You must successfully burn 1,720 Willow Logs.", SkillerTasks.HARD,
			1720, Skills.FIREMAKING, 30), FYEW1("Burn Yew Medium",
			"You must successfully burn 700 Yew Logs.", SkillerTasks.MEDIUM,
			700, Skills.FIREMAKING, 60), FYEW2("Burn Yew Hard",
			"You must successfully burn 1,130 Yew Logs.", SkillerTasks.HARD,
			1130, Skills.FIREMAKING, 60), FYEW3("Burn Yew Elite",
			"You must successfully burn 1,510 Yew Logs.", SkillerTasks.ELITE,
			1510, Skills.FIREMAKING, 60),

	// Fishing
	FANCHOVIES1("Fish Anchovies Easy",
			"You must successfully catch 230 Raw Anchovies.",
			SkillerTasks.EASY, 230, Skills.FISHING, 15), FANCHOVIES2(
			"Fish Anchovies Medium",
			"You must successfully catch 480 Raw Anchovies.",
			SkillerTasks.MEDIUM, 480, Skills.FISHING, 15), FHERRING1(
			"Fish Herring Easy",
			"You must successfully catch 280 Raw Herring.", SkillerTasks.EASY,
			280, Skills.FISHING, 10), FHERRING2("Fish Herring Medium",
			"You must successfully catch 450 Raw Herring.",
			SkillerTasks.MEDIUM, 450, Skills.FISHING, 10), FLOBSTER1(
			"Fish Lobster Medium",
			"You must successfully catch 300 Raw Lobster.",
			SkillerTasks.MEDIUM, 300, Skills.FISHING, 40), FLOBSTER2(
			"Fish Lobster Hard",
			"You must successfully catch 650 Raw Lobster.", SkillerTasks.HARD,
			650, Skills.FISHING, 40), FROCKTAIL1("Fish Rocktail Hard",
			"You must successfully catch 400 Raw Rocktail.", SkillerTasks.HARD,
			400, Skills.FISHING, 90), FROCKTAIL2("Fish Rocktail Elite",
			"You must successfully catch 550 Raw Rocktail.",
			SkillerTasks.ELITE, 550, Skills.FISHING, 90), FSALMON1(
			"Fish Salmon Easy", "You must successfully catch 220 Raw Salmon.",
			SkillerTasks.EASY, 220, Skills.FISHING, 30), FSALMON2(
			"Fish Salmon Medium",
			"You must successfully catch 400 Raw Salmon.", SkillerTasks.MEDIUM,
			400, Skills.FISHING, 30), FSHARK1("Fish Shark Medium",
			"You must successfully catch 220 Raw Shark.", SkillerTasks.MEDIUM,
			220, Skills.FISHING, 76), FSHARK2("Fish Shark Hard",
			"You must successfully catch 380 Raw Shark.", SkillerTasks.HARD,
			380, Skills.FISHING, 76), FSHARK3("Fish Shark Elite",
			"You must successfully catch 500 Raw Shark.", SkillerTasks.ELITE,
			500, Skills.FISHING, 76), FSHRIMP1("Fish Shrimp Easy",
			"You must successfully catch 300 Raw Shrimp.", SkillerTasks.EASY,
			300, Skills.FISHING, 1), FSHRIMP2("Fish Shrimp Medium",
			"You must successfully catch 500 Raw Shrimp.", SkillerTasks.MEDIUM,
			500, Skills.FISHING, 1), FSWORD1("Fish Swordfish Medium",
			"You must successfully catch 320 Raw Swordfish.",
			SkillerTasks.MEDIUM, 320, Skills.FISHING, 50), FSWORD2(
			"Fish Swordfish Hard",
			"You must successfully catch 510 Raw Swordfish.",
			SkillerTasks.HARD, 510, Skills.FISHING, 50), FTROUT1(
			"Fish Trout Easy", "You must successfully catch 250 Raw Trout.",
			SkillerTasks.EASY, 250, Skills.FISHING, 20), FTROUT2(
			"Fish Trout Medium", "You must successfully catch 420 Raw Trout.",
			SkillerTasks.MEDIUM, 420, Skills.FISHING, 20), FTUNA1(
			"Fish Tuna Easy", "You must successfully catch 150 Raw Tuna.",
			SkillerTasks.EASY, 150, Skills.FISHING, 35), FTUNA2(
			"Fish Tuna Medium", "You must successfully catch 340 Raw Tuna.",
			SkillerTasks.MEDIUM, 340, Skills.FISHING, 35),

	// Fletching
	BOAK1("Fletch Oak Easy", "You must fletch 250 bows from an Oak Log.",
			SkillerTasks.EASY, 250, Skills.FLETCHING, 20), BOAK2(
			"Fletch Oak Medium", "You must fletch 670 bows from an Oak Log.",
			SkillerTasks.MEDIUM, 670, Skills.FLETCHING, 20), BMAGIC1(
			"Magic Logs Hard", "You must fletch 750 bows from a Magic Log.",
			SkillerTasks.HARD, 750, Skills.FLETCHING, 80), BMAGIC2(
			"Magic Logs Elite", "You must fletch 980 bows from a Magic Log.",
			SkillerTasks.ELITE, 980, Skills.FLETCHING, 80), BMAPLE1(
			"Fletch Maple Medium",
			"You must fletch 750 bows from a Maple Log.", SkillerTasks.MEDIUM,
			750, Skills.FLETCHING, 50), BMAPLE2("Fletch Maple Hard",
			"You must fletch 1,450 bows from a Maple Log.", SkillerTasks.HARD,
			1450, Skills.FLETCHING, 50), BNORMAL1("Fletch Normal Easy",
			"You must fletch 280 bows from a Regular Log.", SkillerTasks.EASY,
			280, Skills.FLETCHING, 1), BNORMAL2("Fletch Normal Medium",
			"You must fletch 700 bows from a Regular Log.",
			SkillerTasks.MEDIUM, 700, Skills.FLETCHING, 1), BRUNE1(
			"Fletch Rune Medium", "You must fletch 1,000 Rune Arrows.",
			SkillerTasks.MEDIUM, 1000, Skills.FLETCHING, 75), BRUNE2(
			"Fletch Rune Hard", "You must fletch 1,750 Rune Arrows.",
			SkillerTasks.HARD, 1750, Skills.FLETCHING, 75), BWILLOW1(
			"Fletch Willow Easy",
			"You must fletch 330 bows from a Willow Log.", SkillerTasks.EASY,
			330, Skills.FLETCHING, 35), BWILLOW2("Fletch Willow Medium",
			"You must fletch 800 bows from a Willow Log.", SkillerTasks.MEDIUM,
			800, Skills.FLETCHING, 35), BWILLOW3("Fletch Willow Hard",
			"You must fletch 1,500 bows from a Willow Log.", SkillerTasks.HARD,
			1500, Skills.FLETCHING, 35), BYEW1("Fletch Yew Medium",
			"You must fletch 600 bows from a Yew Log.", SkillerTasks.MEDIUM,
			600, Skills.FLETCHING, 65), BYEW2("Fletch Yew Hard",
			"You must fletch 1,050 bows from a Yew Log.", SkillerTasks.HARD,
			1050, Skills.FLETCHING, 65), BYEW3("Fletch Yew Elite",
			"You must fletch 1,450 bows from a Yew Log.", SkillerTasks.ELITE,
			1450, Skills.FLETCHING, 65),

	// Herblore
	CLEAN1("Clean Herbs Easy", "You must clean 400 Grimy Herbs.",
			SkillerTasks.EASY, 400, Skills.HERBLORE, 1), CLEAN2(
			"Clean Herbs Medium", "You must clean 1,000 Grimy Herbs.",
			SkillerTasks.MEDIUM, 1000, Skills.HERBLORE, 1), CLEAN3(
			"Clean Herbs Hard", "You must clean 1,750 Grimy Herbs.",
			SkillerTasks.HARD, 1750, Skills.HERBLORE, 1), CLEAN4(
			"Clean Herbs Elite", "You must clean 2,250 Grimy Herbs.",
			SkillerTasks.ELITE, 2250, Skills.HERBLORE, 1), POTION1(
			"Create Potions Easy", "You must create 250 Potions.",
			SkillerTasks.EASY, 250, Skills.HERBLORE, 1), POTION2(
			"Create Potions Medium", "You must create 600 Potions.",
			SkillerTasks.MEDIUM, 600, Skills.HERBLORE, 1), POTION3(
			"Create Potions Hard", "You must create 1,050 Potions.",
			SkillerTasks.HARD, 1050, Skills.HERBLORE, 1), POTION4(
			"Create Potions Elite", "You must create 1,300 Potions.",
			SkillerTasks.ELITE, 1300, Skills.HERBLORE, 1),

	// Hunter
	BIRD1("Bird Snare Easy", "You must catch 300 birds using a Bird Snare.",
			SkillerTasks.EASY, 300, Skills.HUNTER, 1), BIRD2(
			"Bird Snare Medium",
			"You must catch 800 birds using a Bird Snare.",
			SkillerTasks.MEDIUM, 800, Skills.HUNTER, 1), BIRD3(
			"Bird Snare Hard",
			"You must catch 1,450 birds using a Bird Snare.",
			SkillerTasks.HARD, 1450, Skills.HUNTER, 1), BIRD4(
			"Bird Snare Elite",
			"You must catch 1,675 birds using a Bird Snare.",
			SkillerTasks.ELITE, 1675, Skills.HUNTER, 1), BOX1("Box Trap Easy",
			"You must catch 300 animals using a Box Trap.", SkillerTasks.EASY,
			300, Skills.HUNTER, 27), BOX2("Box Trap Medium",
			"You must catch 800 animals using a Box Trap.",
			SkillerTasks.MEDIUM, 800, Skills.HUNTER, 27), BOX3("Box Trap Hard",
			"You must catch 1,450 animals using a Box Trap.",
			SkillerTasks.HARD, 1450, Skills.HUNTER, 27), BOX4("Box Trap Elite",
			"You must catch 1,675 animals using a Box Trap.",
			SkillerTasks.ELITE, 1675, Skills.HUNTER, 27), NET1(
			"Butterfly Net Easy",
			"You must catch 300 critters using a Butterfly Net.",
			SkillerTasks.EASY, 300, Skills.HUNTER, 15), NET2(
			"Butterfly Net Medium",
			"You must catch 800 critters using a Butterfly Net.",
			SkillerTasks.MEDIUM, 800, Skills.HUNTER, 15), NET3(
			"Butterfly Net Hard",
			"You must catch 1,450 critters using a Butterfly Net.",
			SkillerTasks.HARD, 1450, Skills.HUNTER, 15), NET4(
			"Butterfly Net Elite",
			"You must catch 1,675 critters using a Butterfly Net.",
			SkillerTasks.ELITE, 1675, Skills.HUNTER, 15),

	// Mining
	MADAMANT1("Mine Adamant Hard",
			"You must mine 400 ores from a Adamant Rock.", SkillerTasks.HARD,
			400, Skills.MINING, 70), MADAMANT2("Mine Adamant Elite",
			"You must mine 600 ores from a Adamant Rock.", SkillerTasks.ELITE,
			600, Skills.MINING, 70), MCOAL1("Mine Coal Easy",
			"You must mine 350 ores from a Coal Rock.", SkillerTasks.EASY, 350,
			Skills.MINING, 30), MCOAL2("Mine Coal Medium",
			"You must mine 850 ores from a Coal Rock.", SkillerTasks.MEDIUM,
			850, Skills.MINING, 30), MCOAL3("Mine Coal Hard",
			"You must mine 1,300 ores from a Coal Rock.", SkillerTasks.HARD,
			1300, Skills.MINING, 30), MCOPPER1("Mine Copper Easy",
			"You must mine 375 ores from a Copper Rock.", SkillerTasks.EASY,
			375, Skills.MINING, 1), MCOPPER2("Mine Copper Medium",
			"You must mine 900 ores from a Copper Rock.", SkillerTasks.MEDIUM,
			900, Skills.MINING, 1), MGEM1("Mine Gems Medium",
			"You must mine 500 gems from a Gem Rock.", SkillerTasks.MEDIUM,
			500, Skills.MINING, 40), MGEM2("Mine Gems Medium",
			"You must mine 1,250 gems from a Gem Rock.", SkillerTasks.MEDIUM,
			1250, Skills.MINING, 40), MGEM3("Mine Gems Medium",
			"You must mine 1,750 gems from a Gem Rock.", SkillerTasks.MEDIUM,
			1750, Skills.MINING, 40), MGEM4("Mine Gems Medium",
			"You must mine 2,200 gems from a Gem Rock.", SkillerTasks.MEDIUM,
			2200, Skills.MINING, 40), MESSENCE1("Mine Essence Medium",
			"You must mine 500 ores from an Essence Mine.",
			SkillerTasks.MEDIUM, 500, Skills.MINING, 1), MESSENCE2(
			"Mine Essence Medium",
			"You must mine 1,250 ores from an Essence Mine.",
			SkillerTasks.MEDIUM, 1250, Skills.MINING, 1), MESSENCE3(
			"Mine Essence Medium",
			"You must mine 1,750 ores from an Essence Mine.",
			SkillerTasks.MEDIUM, 1750, Skills.MINING, 1), MESSENCE4(
			"Mine Essence Medium",
			"You must mine 2,200 ores from an Essence Mine.",
			SkillerTasks.MEDIUM, 2200, Skills.MINING, 1), MGOLD1(
			"Mine Gold Easy", "You must mine 320 ores from a Gold Rock.",
			SkillerTasks.EASY, 320, Skills.MINING, 40), MGOLD2(
			"Mine Gold Medium", "You must mine 800 ores from a Gold Rock.",
			SkillerTasks.MEDIUM, 800, Skills.MINING, 40), MGOLD3(
			"Mine Gold Hard", "You must mine 1,120 ores from a Gold Rock.",
			SkillerTasks.HARD, 1120, Skills.MINING, 40), MIRON1(
			"Mine Iron Easy", "You must mine 350 ores from a Iron Rock.",
			SkillerTasks.EASY, 350, Skills.MINING, 15), MIRON2(
			"Mine Iron Medium", "You must mine 850 ores from a Iron Rock.",
			SkillerTasks.MEDIUM, 850, Skills.MINING, 15), MIRON3(
			"Mine Iron Hard", "You must mine 1,300 ores from a Iron Rock.",
			SkillerTasks.HARD, 1300, Skills.MINING, 15), MMITHRIL1(
			"Mine Mithril Medium",
			"You must mine 650 ores from a Mithril Rock.", SkillerTasks.MEDIUM,
			650, Skills.MINING, 55), MMITHRIL2("Mine Mithril Hard",
			"You must mine 1,050 ores from a Mithril Rock.", SkillerTasks.HARD,
			1050, Skills.MINING, 55), MRUNE1("Mine Rune Hard",
			"You must mine 250 ores from a Rune Rock.", SkillerTasks.HARD, 250,
			Skills.MINING, 85), MRUNE2("Mine Rune Elite",
			"You must mine 400 ores from a Rune Rock.", SkillerTasks.ELITE,
			400, Skills.MINING, 85), MSILVER1("Mine Silver Easy",
			"You must mine 350 ores from a Silver Rock.", SkillerTasks.EASY,
			350, Skills.MINING, 20), MSILVER2("Mine Silver Medium",
			"You must mine 850 ores from a Silver Rock.", SkillerTasks.MEDIUM,
			850, Skills.MINING, 20), MSILVER3("Mine Silver Hard",
			"You must mine 1,300 ores from a Silver Rock.", SkillerTasks.HARD,
			1300, Skills.MINING, 20), MTIN1("Mine Tin Easy",
			"You must mine 375 ores from a Tin Rock.", SkillerTasks.EASY, 375,
			Skills.MINING, 1), MTIN2("Mine Tin Medium",
			"You must mine 900 ores from a Tin Rock.", SkillerTasks.MEDIUM,
			900, Skills.MINING, 1),

	// Runecrafting
	CRAFT1("Craft Rune Easy", "You must craft 5,000 Runes.", SkillerTasks.EASY,
			5000, Skills.RUNECRAFTING, 1), CRAFT2("Craft Rune Medium",
			"You must craft 10,000 Runes.", SkillerTasks.MEDIUM, 10000,
			Skills.RUNECRAFTING, 1), CRAFT3("Craft Rune Hard",
			"You must craft 18,000 Runes.", SkillerTasks.HARD, 18000,
			Skills.RUNECRAFTING, 1), CRAFT4("Craft Rune Elite",
			"You must craft 25,000 Runes.", SkillerTasks.ELITE, 25000,
			Skills.RUNECRAFTING, 1),

	// Smithing
	SADAMANT1("Smelt Adamant Hard",
			"You must successfully smelt 650 Adamant Bars.", SkillerTasks.HARD,
			650, Skills.SMITHING, 70), SADAMANT2("Smelt Adamant Elite",
			"You must successfully smelt 1,000 Adamant Bars.",
			SkillerTasks.ELITE, 1000, Skills.SMITHING, 70), SBRONZE1(
			"Smelt Bronze Easy",
			"You must successfully smelt 350 Bronze Bars.", SkillerTasks.EASY,
			350, Skills.SMITHING, 1), SBRONZE2("Smelt Bronze Medium",
			"You must successfully smelt 800 Bronze Bars.",
			SkillerTasks.MEDIUM, 800, Skills.SMITHING, 1), SCANNON1(
			"Smelt Cannon Balls Easy",
			"You must successfully smelt 400 Cannon Balls.", SkillerTasks.EASY,
			400, Skills.SMITHING, 35), SCANNON2("Smelt Cannon Balls Medium",
			"You must successfully smelt 1,000 Cannon Balls.",
			SkillerTasks.MEDIUM, 1000, Skills.SMITHING, 35), SCANNON3(
			"Smelt Cannon Balls Hard",
			"You must successfully smelt 1,800 Cannon Balls.",
			SkillerTasks.HARD, 1800, Skills.SMITHING, 35), SCANNON4(
			"Smelt Cannon Balls Elite",
			"You must successfully smelt 2,250 Cannon Balls.",
			SkillerTasks.ELITE, 2250, Skills.SMITHING, 35), SGOLD1(
			"Smelt Gold Easy", "You must successfully smelt 350 Gold Bars.",
			SkillerTasks.EASY, 350, Skills.SMITHING, 40), SGOLD2(
			"Smelt Gold Medium", "You must successfully smelt 800 Gold Bars.",
			SkillerTasks.MEDIUM, 800, Skills.SMITHING, 40), SGOLD3(
			"Smelt Gold Hard", "You must successfully smelt 1,300 Gold Bars.",
			SkillerTasks.HARD, 1300, Skills.SMITHING, 40), SIRON1(
			"Smelt Iron Easy", "You must successfully smelt 325 Iron Bars.",
			SkillerTasks.EASY, 325, Skills.SMITHING, 15), SIRON2(
			"Smelt Iron Medium", "You must successfully smelt 750 Iron Bars.",
			SkillerTasks.MEDIUM, 750, Skills.SMITHING, 15), SMITHRIL1(
			"Smelt Mithril Medium",
			"You must successfully smelt 300 Mithril Bars.",
			SkillerTasks.MEDIUM, 300, Skills.SMITHING, 50), SMITHRIL2(
			"Smelt Mithril Hard",
			"You must successfully smelt 700 Mithril Bars.", SkillerTasks.HARD,
			700, Skills.SMITHING, 50), SMITHRIL3("Smelt Mithril Elite",
			"You must successfully smelt 1,100 Mithril Bars.",
			SkillerTasks.ELITE, 1100, Skills.SMITHING, 50), SRUNE1(
			"Smelt Rune Hard", "You must successfully smelt 400 Rune Bars.",
			SkillerTasks.HARD, 400, Skills.SMITHING, 85), SRUNE2(
			"Smelt Rune Elite", "You must successfully smelt 800 Rune Bars.",
			SkillerTasks.ELITE, 800, Skills.SMITHING, 85), SSILVER1(
			"Smelt Silver Easy",
			"You must successfully smelt 325 Silver Bars.", SkillerTasks.EASY,
			325, Skills.SMITHING, 20), SSILVER2("Smelt Silver Medium",
			"You must successfully smelt 750 Silver Bars.",
			SkillerTasks.MEDIUM, 750, Skills.SMITHING, 20), SSTEEL1(
			"Smelt Steel Easy", "You must successfully smelt 310 Steel Bars.",
			SkillerTasks.EASY, 310, Skills.SMITHING, 30), SSTEEL2(
			"Smelt Steel Medium",
			"You must successfully smelt 700 Steel Bars.", SkillerTasks.MEDIUM,
			700, Skills.SMITHING, 30),

	// Thieving
	CRACK1("Crack Safe Easy", "You must crack open 300 Wall Safes.",
			SkillerTasks.EASY, 300, Skills.THIEVING, 50), CRACK2(
			"Crack Safe Medium", "You must crack open 650 Wall Safes.",
			SkillerTasks.MEDIUM, 650, Skills.THIEVING, 50), CRACK3(
			"Crack Safe Hard", "You must crack open 900 Wall Safes.",
			SkillerTasks.HARD, 900, Skills.THIEVING, 50), CRACK4(
			"Crack Safe Elite", "You must crack open 1,125 Wall Safes.",
			SkillerTasks.ELITE, 1125, Skills.THIEVING, 50), POCKET1(
			"Pickpocket Easy", "You must steal from 300 People.",
			SkillerTasks.EASY, 300, Skills.THIEVING, 1), POCKET2(
			"Pickpocket Medium", "You must steal from 650 People.",
			SkillerTasks.MEDIUM, 650, Skills.THIEVING, 1), POCKET3(
			"Pickpocket Hard", "You must steal from 900 People.",
			SkillerTasks.HARD, 900, Skills.THIEVING, 1), POCKET4(
			"Pickpocket Elite", "You must steal from 1,125 People.",
			SkillerTasks.ELITE, 1125, Skills.THIEVING, 1), STALL1(
			"Thieve Stall Easy", "You must steal from 300 Stalls.",
			SkillerTasks.EASY, 300, Skills.THIEVING, 1), STALL2(
			"Thieve Stall Medium", "You must steal from 650 Stalls.",
			SkillerTasks.MEDIUM, 650, Skills.THIEVING, 1), STALL3(
			"Thieve Stall Hard", "You must steal from 900 Stalls.",
			SkillerTasks.HARD, 900, Skills.THIEVING, 1), STALL4(
			"Thieve Stall Elite", "You must steal from 1,125 Stalls.",
			SkillerTasks.ELITE, 1125, Skills.THIEVING, 1),

	// Woodcutting
	WIVY1("Chop Ivy Medium", "You must chop 800 pieces of Choking Ivy.",
			SkillerTasks.MEDIUM, 800, Skills.WOODCUTTING, 68), WIVY2(
			"Chop Ivy Hard", "You must chop 1,400 pieces of Choking Ivy.",
			SkillerTasks.HARD, 1400, Skills.WOODCUTTING, 68), WIVY3(
			"Chop Ivy Elite", "You must chop 1,850 pieces of Choking Ivy.",
			SkillerTasks.ELITE, 1850, Skills.WOODCUTTING, 68), WOAK1(
			"Chop Oak Easy", "You must chop 250 logs from an Oak Tree.",
			SkillerTasks.EASY, 250, Skills.WOODCUTTING, 15), WOAK2(
			"Chop Oak Medium", "You must chop 670 logs from an Oak Tree.",
			SkillerTasks.MEDIUM, 670, Skills.WOODCUTTING, 15), WMAGIC1(
			"Magic Logs Hard", "You must chop 750 logs from a Magic Tree.",
			SkillerTasks.HARD, 750, Skills.WOODCUTTING, 75), WMAGIC2(
			"Magic Logs Elite", "You must chop 980 logs from a Magic Tree.",
			SkillerTasks.ELITE, 980, Skills.WOODCUTTING, 75), WMAPLE1(
			"Chop Maple Medium", "You must chop 750 logs from a Maple Tree.",
			SkillerTasks.MEDIUM, 750, Skills.WOODCUTTING, 45), WMAPLE2(
			"Chop Maple Hard", "You must chop 1,450 logs from a Maple Tree.",
			SkillerTasks.HARD, 1450, Skills.WOODCUTTING, 45), WNORMAL1(
			"Chop Normal Easy", "You must chop 280 logs from a Regular Tree.",
			SkillerTasks.EASY, 280, Skills.WOODCUTTING, 1), WNORMAL2(
			"Chop Normal Medium",
			"You must chop 700 logs from a Regular Tree.", SkillerTasks.MEDIUM,
			700, Skills.WOODCUTTING, 1), WWILLOW1("Chop Willow Easy",
			"You must chop 330 logs from a Willow Tree.", SkillerTasks.EASY,
			330, Skills.WOODCUTTING, 30), WWILLOW2("Chop Willow Medium",
			"You must chop 800 logs from a Willow Tree.", SkillerTasks.MEDIUM,
			800, Skills.WOODCUTTING, 30), WWILLOW3("Chop Willow Hard",
			"You must chop 1,500 logs from a Willow Tree.", SkillerTasks.HARD,
			1500, Skills.WOODCUTTING, 30), WYEW1("Chop Yew Medium",
			"You must chop 600 logs from a Yew Tree.", SkillerTasks.MEDIUM,
			600, Skills.WOODCUTTING, 60), WYEW2("Chop Yew Hard",
			"You must chop 1,050 logs from a Yew Tree.", SkillerTasks.HARD,
			1050, Skills.WOODCUTTING, 60), WYEW3("Chop Yew Elite",
			"You must chop 1,450 logs from a Yew Tree.", SkillerTasks.ELITE,
			1450, Skills.WOODCUTTING, 60);

	private String assignment;
	private String description;
	private int difficulty;
	private int amount;
	private int skill;
	private int level;

	SkillTasks(String assignment, String description, int difficulty,
			int amount, int skill, int level) {
		this.assignment = assignment;
		this.description = description;
		this.difficulty = difficulty;
		this.amount = amount;
		this.skill = skill;
		this.level = level;
	}

	public String getAssignment() {
		return assignment;
	}

	public String getDescription() {
		return description;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getAmount() {
		return amount;
	}

	public int getSkill() {
		return skill;
	}

	public int getLevel() {
		return level;
	}

}