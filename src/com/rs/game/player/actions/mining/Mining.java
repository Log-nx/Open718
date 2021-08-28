package com.rs.game.player.actions.mining;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.npc.random.LiquidGoldNymph;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.events.GlobalEvents.Event;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.content.perks.GamePointRewards;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.managers.CompletionistCapeManager.Requirement;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public final class Mining extends MiningBase {
	
	public static boolean coalHarmonized = false;
	public static boolean mithrilHarmonized = false;
	public static boolean adamantHarmonized = false;
	public static boolean runeHarmonized = false;

    public static enum RockDefinitions {
    	
		CLAY(1, 5, 434, 10, 1), 
		COPPER_ORE(1, 17.5, 436, 10, 1), 
		TIN_ORE(1, 17.5, 438, 15, 1), 
		IRON_ORE(15, 35, 440, 15, 1),
		BLURITE_ORE(10, 17.5, 668, 15, 1),
		SANDSTONE_ORE(35, 30, 6971, 30, 1), 
		SILVER_ORE(20, 40, 442, 25, 1), 
		COAL_ORE(30, 50, 453, 50, 10), 
		GRANITE_ORE(45, 50, 6979, 50, 10), 
		GOLD_ORE(40, 60, 444, 80, 20), 
		MITHRIL_ORE(55, 80, 447, 100, 20),
		LUMINITE(40, 112.48, ItemIdentifiers.LUMINITE, 100, 20),
		ORICHALCITE_ORE(60, 436.8, ItemIdentifiers.ORICHALCITE_ORE, 100, 20),
		DRAKOLITH(60, 436.8, ItemIdentifiers.DRAKOLITH, 100, 20),
		ADAMANT_ORE(70, 95, 449, 130, 25),
		NECRITE_ORE(70, 416, ItemIdentifiers.NECRITE_ORE, 130, 25),
		PHASMATITE(70, 416, ItemIdentifiers.PHASMATITE, 130, 25),
		RUNITE_ORE(85, 125, 451, 150, 30), 
		LRC_COAL_ORE(77, 50, 453, 50, 10), 
		LRC_GOLD_ORE(80, 60, 444, 40, 10),
		BANITE_ORE(80, 557.6, ItemIdentifiers.BANE_ORE, 40, 10),
		RED_SANDSTONE(81, 15, 23194, 41, 11),
		GEM_ROCK(40, 60, 1625, 90, 10),
		CRYSTAL_SANDSTONE(81, 15, 32847, 41, 11),
		SEREN_STONE(89, 296.7, 32262, 150, 25),
		HARMONIZED_COAL_ORE(30, 50, 453, 15, 15),
		HARMONIZED_MITHRIL_ORE(55, 80, 447, 15, 15),
		HARMONIZED_ADAMANT_ORE(70, 95, 449, 15, 15),
		HARMONIZED_RUNITE_ORE(85, 125, 451, 15, 15),
		MENAPHOS_SANDSTONE(35, 30, 6971, 30, 5),
		LIGHT_ANIMICA(90, 672, ItemIdentifiers.LIGHT_ANIMICA, 150, 25),
		DARK_ANIMICA(90, 672, ItemIdentifiers.DARK_ANIMICA, 150, 25),
		
		;

    	private int level;
    	private double xp;
    	private int oreId;
    	private int oreBaseTime;
    	private int oreRandomTime;

    	private RockDefinitions(int level, double xp, int oreId, int oreBaseTime, int oreRandomTime) {
    		this.level = level;
    		this.xp = xp;
    		this.oreId = oreId;
    		this.oreBaseTime = oreBaseTime;
    		this.oreRandomTime = oreRandomTime;
    	}

    	public int getLevel() {
    		return level;
    	}

    	public int getOreBaseTime() {
    		return oreBaseTime;
    	}

    	public int getOreId() {
    		return oreId;
    	}

    	public int getOreRandomTime() {
    		return oreRandomTime;
    	}

    	public double getXp() {
    		return xp;
    	}
    }

    private WorldObject rock;
	PickAxeDefinitions pickaxeDefs;
    private RockDefinitions definitions;

    public Mining(WorldObject rock, RockDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
    }

    private void addOre(Player player) {
    	double xpBoost = 0;
    	int idSome = 0;
    	if (definitions == RockDefinitions.GRANITE_ORE) {
    		idSome = Utils.getRandom(2) * 2;
    		if (idSome == 2)
    			xpBoost += 10;
    		else if (idSome == 4)
    			xpBoost += 25;
    	} else if (definitions == RockDefinitions.SANDSTONE_ORE) {
    		idSome = Utils.getRandom(3) * 2;
    		xpBoost += idSome / 2 * 10;
    	} else if (definitions == RockDefinitions.COPPER_ORE && player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().tutorialStage == 11) {
			player.hasMinedTin = true;
			if (player.hasMinedCopper) {
				player.getStatistics().tutorialStage = 12;
				Dialogue.sendNPCDialogueNoContinue(player, 948, Dialogue.NORMAL, "Great, now let me see those ores you mined.");
			}
    	} 
		if (definitions == RockDefinitions.TIN_ORE && player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().tutorialStage == 11) {
			player.hasMinedCopper = true;
			if (player.hasMinedTin) {
				player.getStatistics().tutorialStage = 12;
				Dialogue.sendNPCDialogueNoContinue(player, 948, Dialogue.NORMAL, "Great, now let me see those ores you mined.");
			}
		}
    	double totalXp = (definitions.getXp() + xpBoost) * miningSuit(player);
    	player.getSkills().addXp(Skills.MINING, totalXp);
    	if (definitions == RockDefinitions.GEM_ROCK) {
    		if (rock.getId() == 93024 || rock.getId() == 93025) {
    			if (Utils.random(10000) == 0) {
    	    		player.getStatistics().addOresMined();
    	    		player.getGamePointManager().addGamePointItem(6571, 1);
    				player.getPackets().sendGameMessage("You mine a rare uncut onyx gem; ores mined: " + Colors.RED+Utils.getFormattedNumber(player.getStatistics().getOresMined())+"</col>.", true);
    				return;
    			}
    		}
			int gem = Utils.random(5);
			switch (gem) {
			case 0:
	    		player.getGamePointManager().addGamePointItem(1625, 1);
				break;
			case 1:
	    		player.getGamePointManager().addGamePointItem(1627, 1);
				break;
			case 2:
	    		player.getGamePointManager().addGamePointItem(1623, 1);
				break;
			case 3:
	    		player.getGamePointManager().addGamePointItem(1621, 1);
				break;
			case 4:
	    		player.getGamePointManager().addGamePointItem(1619, 1);
				break;
			case 5:
	    		player.getGamePointManager().addGamePointItem(1617, 1);
				break;
			}
    		player.getStatistics().addOresMined();
			player.getPackets().sendGameMessage("You mine a gem; ores mined: " + Utils.getFormattedNumber(player.getStatistics().getOresMined())+".", true);
			return;
			}
    		String oreName = ItemDefinitions.getItemDefinitions(definitions.getOreId() + idSome).getName().toLowerCase();
    		player.getStatistics().addOresMined();
			handleSkillTasks(player);
    		player.getPackets().sendGameMessage("You mine some " + oreName + "; ores mined: " + Colors.RED+ Utils.getFormattedNumber(player.getStatistics().getOresMined())+"</col>.", true);
    		if (handlePerk(player)) {
    		} else
    			player.getGamePointManager().addGamePointItem(definitions.getOreId() + idSome, 1);
    		handleGeodes(player);
    	}

    private boolean checkAll(Player player) {
		player.closeInterfaces();
		if (pickaxeDefs == null) {
			player.getPackets().sendGameMessage("You do not have a pickaxe or do not have the required level to use the pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
		    return false;
		if (!player.getInventory().hasFreeSlots()) {
		    player.getPackets().sendGameMessage("Inventory full. To make more room, sell, drop or bank something.");
		    return false;
		}
		return true;
    }

    private boolean checkRock(Player player) {
    	return World.containsObjectWithId(rock, rock.getId());
    }

    private int getMiningDelay(Player player) {
		int summoningBonus = 0;
		if (player.getFamiliar() != null) {
		    if (player.getFamiliar().getId() == 7342 || player.getFamiliar().getId() == 7342)
		    	summoningBonus += 10;
		    else if (player.getFamiliar().getId() == 6832 || player.getFamiliar().getId() == 6831)
		    	summoningBonus += 1;
		}
		int mineTimer = definitions.getOreBaseTime() - (player.getSkills().getLevel(Skills.MINING) + summoningBonus) - Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + definitions.getOreRandomTime())
		    mineTimer = 1 + Utils.getRandom(definitions.getOreRandomTime());
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		if (player.getPerkManager().hasPerk(PlayerPerks.QUARRY_MASTER)) {
			mineTimer /= 1.33;
		}
		if (hasGolemOutfit(player)) {
			mineTimer /= 1.07;
		}
		if (GlobalEvents.isActiveEvent(Event.FASTER_SKILLING)) {
			mineTimer /= 2;
		}
		if (player.getGamePointManager().hasReward(GamePointRewards.MORE_SKILLS)) {
			mineTimer /= 2;
		}
		return mineTimer;
    }

    private boolean hasMiningLevel(Player player) {
    	int level = hasRingOfSilence(player) ? definitions.getLevel() - 3 : definitions.getLevel();
		if (level > player.getSkills().getLevel(Skills.MINING)) {
		    player.getPackets().sendGameMessage("You need a mining level of " + definitions.getLevel() + " to mine this rock.");
		    return false;
		}
		return true;
    }
    
    private boolean hasRingOfSilence(Player player) {
    	if (player.getEquipment().hasRingOfWhispers()) {
    		return true;
    	}
    	return false;
    }

    /**
     * XP modifier by wearing items.
     * @param player The player.
     * @return the XP modifier.
     */
    private double miningSuit(Player player) {
    	double xpBoost = 1.0;
    	if (player.getEquipment().getHatId() == 20789)
    		xpBoost *= 1.01;
    	if (player.getEquipment().getChestId() == 20791)
    		xpBoost *= 1.01;
    	if (player.getEquipment().getLegsId() == 20790)
    		xpBoost *= 1.01;
    	if (player.getEquipment().getBootsId() == 20788)
    		xpBoost *= 1.01;
    	if (player.getEquipment().getHatId() == 20789
    			&& player.getEquipment().getChestId() == 20791
    			&& player.getEquipment().getLegsId() == 20790
    			&& player.getEquipment().getBootsId() == 20788)
    		xpBoost *= 1.01;
    	if (hasGolemOutfit(player))
    		xpBoost *= 1.07;
    	return xpBoost;
    }
    
    /**
     * Checks if the player has any of the 4 golem outfits.
     * @param player The player to check.
     * @return if has the full outfit.
     */
    private boolean hasGolemOutfit(Player player) {
    	if (player.getEquipment().getHatId() == 31575
    			&& player.getEquipment().getChestId() == 31576
    			&& player.getEquipment().getLegsId() == 31577
    			&& player.getEquipment().getGlovesId() == 31578
    			&& player.getEquipment().getBootsId() == 31579)
    	    return true;
    	if (player.getEquipment().getHatId() == 31580
    			&& player.getEquipment().getChestId() == 31581
    			&& player.getEquipment().getLegsId() == 31582
    			&& player.getEquipment().getGlovesId() == 31583
    			&& player.getEquipment().getBootsId() == 31584)
    	    return true;
    	if (player.getEquipment().getHatId() == 31585
    			&& player.getEquipment().getChestId() == 31586
    			&& player.getEquipment().getLegsId() == 31587
    			&& player.getEquipment().getGlovesId() == 31588
    			&& player.getEquipment().getBootsId() == 31589)
    	    return true;
    	if (player.getEquipment().getHatId() == 31590
    			&& player.getEquipment().getChestId() == 31591
    			&& player.getEquipment().getLegsId() == 31592
    			&& player.getEquipment().getGlovesId() == 31593
    			&& player.getEquipment().getBootsId() == 31594)
    	    return true;
    	return false;
    }

    @Override
    public boolean process(Player player) {
    	setAnimationAndGFX(player);
    	if (Utils.random(500) == 0) {
    		new LiquidGoldNymph(player, player);
    		player.getPackets().sendGameMessage("<col=ff0000>A Liquid Gold Nymph emerges from the rock.");
    	}
    	player.faceObject(rock);
    	return checkRock(player);
    }

    @Override
    public int processWithDelay(Player player) {
		addOre(player);
		if (definitions == RockDefinitions.RUNITE_ORE)
			player.getCompCapeManager().increaseRequirement(Requirement.RUNITE_MINING, 1);
		if (!player.getInventory().hasFreeSlots()) {
		    player.setNextAnimation(new Animation(-1));
		    player.getPackets().sendGameMessage("Inventory full. To make more room, sell, drop or bank something.");
		    return -1;
		}
		return getMiningDelay(player);
    }
    
	private void handleSkillTasks( Player player ) {
		if (definitions == RockDefinitions.COPPER_ORE) {
			player.getSkillTasks().decreaseTask( SkillTasks.MCOPPER1);
			player.getSkillTasks().decreaseTask(SkillTasks.MCOPPER2);
		} else if (definitions == RockDefinitions.TIN_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MTIN1);
			player.getSkillTasks().decreaseTask(SkillTasks.MTIN2);
		} else if (definitions == RockDefinitions.SILVER_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MSILVER1);
			player.getSkillTasks().decreaseTask(SkillTasks.MSILVER2);
			player.getSkillTasks().decreaseTask(SkillTasks.MSILVER3);
		} else if (definitions == RockDefinitions.IRON_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MIRON1);
			player.getSkillTasks().decreaseTask(SkillTasks.MIRON2);
			player.getSkillTasks().decreaseTask(SkillTasks.MIRON3);
		} else if (definitions == RockDefinitions.COAL_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MCOAL1);
			player.getSkillTasks().decreaseTask(SkillTasks.MCOAL2);
			player.getSkillTasks().decreaseTask(SkillTasks.MCOAL3);
		} else if (definitions == RockDefinitions.GOLD_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MGOLD1);
			player.getSkillTasks().decreaseTask(SkillTasks.MGOLD2);
			player.getSkillTasks().decreaseTask(SkillTasks.MGOLD3);
		} else if (definitions == RockDefinitions.MITHRIL_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MMITHRIL1);
			player.getSkillTasks().decreaseTask(SkillTasks.MMITHRIL2);
		} else if (definitions == RockDefinitions.ADAMANT_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MADAMANT1);
			player.getSkillTasks().decreaseTask(SkillTasks.MADAMANT2);
		} else if (definitions == RockDefinitions.RUNITE_ORE) {
			player.getSkillTasks().decreaseTask(SkillTasks.MRUNE1);
			player.getSkillTasks().decreaseTask(SkillTasks.MRUNE2);
		} else if (definitions == RockDefinitions.GEM_ROCK) {
			player.getSkillTasks().decreaseTask(SkillTasks.MGEM1);
			player.getSkillTasks().decreaseTask(SkillTasks.MGEM2);
			player.getSkillTasks().decreaseTask(SkillTasks.MGEM3);
			player.getSkillTasks().decreaseTask(SkillTasks.MGEM4);
		}
	}

    @Override
    public boolean start(Player player) {
		pickaxeDefs = getPickAxeDefinitions(player);
		if (!checkAll(player))
		    return false;
		player.getPackets().sendGameMessage("You swing your pickaxe at the rock..", true);
		setActionDelay(player, getMiningDelay(player));
		return true;
    }
    
    public boolean handlePerk(Player player) {
		String oreName = ItemDefinitions.getItemDefinitions(definitions.getOreId()).getName().toLowerCase();
    	if (definitions.getOreId() != -1) {
    		Item bar = null;
    		if (Utils.random(100) >= 90) {
	    		if (definitions == RockDefinitions.TIN_ORE || definitions == RockDefinitions.COPPER_ORE)
	    			bar = new Item(2349);
	    		if (definitions == RockDefinitions.IRON_ORE)
	    			bar = new Item(2351);
	    		if (definitions == RockDefinitions.SILVER_ORE)
	    			bar = new Item(2355);
	    		if (definitions == RockDefinitions.GOLD_ORE || definitions == RockDefinitions.LRC_GOLD_ORE)
	    			bar = new Item(2357);
	    		if (definitions == RockDefinitions.MITHRIL_ORE)
	    			bar = new Item(2359);
	    		if (definitions == RockDefinitions.ADAMANT_ORE)
	    			bar = new Item(2361);
	    		if (definitions == RockDefinitions.RUNITE_ORE)
	    			bar = new Item(2363);
    		}
    		if (bar != null && player.getPerkManager().hasPerk(PlayerPerks.QUARRY_MASTER)) {
    			player.getGamePointManager().addGamePointItem(bar.getId(), 1);
    			player.getPackets().sendGameMessage("The "+ oreName +" turned into a "+bar.getName().toLowerCase() +" thanks to your perk.", true);
    			player.getSkills().addXp(Skills.SMITHING, Utils.random(0.1, 3.0));
    		}
    	}
		return false;
    }
    
    public void handleGeodes(Player player) {
    	if (definitions == RockDefinitions.COPPER_ORE || definitions == RockDefinitions.TIN_ORE || definitions == RockDefinitions.IRON_ORE 
    		|| definitions == RockDefinitions.COAL_ORE || definitions == RockDefinitions.MITHRIL_ORE 
    		|| definitions == RockDefinitions.ADAMANT_ORE  || definitions == RockDefinitions.LUMINITE || definitions == RockDefinitions.RUNITE_ORE) {
    		if (Utils.random(50) == 1) {
    			player.getGamePointManager().addGamePointItem(ItemIdentifiers.SEDIMENTARY_GEODE, 1);
    		}
    	}
    	if (definitions == RockDefinitions.ORICHALCITE_ORE || definitions == RockDefinitions.DRAKOLITH || definitions == RockDefinitions.NECRITE_ORE 
        	|| definitions == RockDefinitions.PHASMATITE || definitions == RockDefinitions.BANITE_ORE 
        	|| definitions == RockDefinitions.LIGHT_ANIMICA  || definitions == RockDefinitions.DARK_ANIMICA) {
        	if (Utils.random(100) == 1) {
    			player.getGamePointManager().addGamePointItem(ItemIdentifiers.IGNEOUS_GEODE, 1);
        	}
        }
    	if (definitions == RockDefinitions.ORICHALCITE_ORE || definitions == RockDefinitions.DRAKOLITH || definitions == RockDefinitions.NECRITE_ORE 
            || definitions == RockDefinitions.PHASMATITE || definitions == RockDefinitions.BANITE_ORE 
            || definitions == RockDefinitions.LIGHT_ANIMICA  || definitions == RockDefinitions.DARK_ANIMICA) {
            if (Utils.random(200) == 1) {
    			player.getGamePointManager().addGamePointItem(ItemIdentifiers.METAMORPHIC_GEODE, 1);
            }
        }
    }
}