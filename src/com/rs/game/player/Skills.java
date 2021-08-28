package com.rs.game.player;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.content.activities.XPWell;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.content.pet.SkillingPetsManager;
import com.rs.game.player.dialogues.impl.LevelUp;
import com.rs.utils.Utils;

public final class Skills implements Serializable {

	private static final long serialVersionUID = -7086829989489745985L;

	public static final double MAXIMUM_EXP = 500000000;

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6,
			COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13,
			MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20,
			CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23, DUNGEONEERING = 24, DIVINATION = 25, INVENTION = 26;

	public static final int ASSASSIN = 0, ASSASSIN_CALL = 1, FINAL_BLOW = 2, SWIFT_SPEED = 3, STEALTH_MOVES = 4;

	public static final String[] SKILL_NAME_ASSASSIN = { "Assassin", "Assassin Call", "Final Blow", "Swift Speed",
			"Stealth Moves" };

	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Constitution", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
			"Summoning", "Dungeoneering", "Divination", "Invention" };

	public String getSkillName(int skill) {
		switch (skill) {
		case ATTACK:
			return "Attack";
		case STRENGTH:
			return "Strength";
		case DEFENCE:
			return "Defence";
		case RANGE:
			return "Ranged";
		case HITPOINTS:
			return "Hitpoints";
		case PRAYER:
			return "Prayer";
		case AGILITY:
			return "Agility";
		case HERBLORE:
			return "Herblore";
		case THIEVING:
			return "Thieving";
		case CRAFTING:
			return "Crafting";
		case MINING:
			return "Mining";
		case SMITHING:
			return "Smithing";
		case FISHING:
			return "Fishing";
		case COOKING:
			return "Cooking";
		case FIREMAKING:
			return "Firemaking";
		case WOODCUTTING:
			return "Woodcutting";
		case SLAYER:
			return "Slayer";
		case FARMING:
			return "Farming";
		case CONSTRUCTION:
			return "Construction";
		case HUNTER:
			return "Hunter";
		case SUMMONING:
			return "Summoning";
		case DUNGEONEERING:
			return "Dungeoneering";
		case MAGIC:
			return "Magic";
		case FLETCHING:
			return "Fletching";
		case RUNECRAFTING:
			return "Runecrafting";
		case DIVINATION:
			return "Divination";
		case INVENTION:
			return "Invention";
		default:
			return "Null";
		}
	}

	public static int getSkillId(String name) {
		for (int i = 0; i < SKILLS.length; i++) {
			if (name.toLowerCase().equals(SKILLS[i].toLowerCase())) {
				return i;
			}
		}
		return -1;
	}

	public static final String[] SKILLS = { "Attack", "Defence", "Strength", "Constitution", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
			"Summoning", "Dungeoneering", "Divination", "Invention" };;

	private short level[];
	private double xp[];
	private double[] xpTracks;
	private boolean[] trackSkills;
	private byte[] trackSkillsIds;
	private boolean xpDisplay, xpPopup;
	private boolean[] enabledSkillsTargets;
	private boolean[] skillsTargetsUsingLevelMode;
	private int[] skillsTargetsValues;

	private transient int currentCounter;
	private transient Player player;

	public void passLevels(Player p) {
		this.level = p.getSkills().level;
		this.xp = p.getSkills().xp;
	}

	public Skills() {
		level = new short[27];
		xp = new double[27];
		assassinLevel = new short[5];
		assassinXp = new double[5];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
		xpPopup = true;
		xpTracks = new double[3];
		trackSkills = new boolean[3];
		trackSkillsIds = new byte[3];
		trackSkills[0] = true;
		if (enabledSkillsTargets == null)
			enabledSkillsTargets = new boolean[27];
		if (skillsTargetsUsingLevelMode == null)
			skillsTargetsUsingLevelMode = new boolean[27];
		if (skillsTargetsValues == null)
			skillsTargetsValues = new int[27];
		for (int i = 0; i < trackSkillsIds.length; i++)
			trackSkillsIds[i] = 30;
	}

	public void sendXPDisplay() {
		for (int i = 0; i < trackSkills.length; i++) {
			player.getPackets().sendConfigByFile(10444 + i, trackSkills[i] ? 1 : 0, true);
			player.getPackets().sendConfigByFile(10440 + i, trackSkillsIds[i] + 1, true);
			refreshCounterXp(i);
		}
	}

	public void setupXPCounter() {
		player.getInterfaceManager().sendXPDisplay(1214);
	}

	public void refreshCurrentCounter() {
		player.getPackets().sendConfig(2478, currentCounter + 1);
	}

	public void setCurrentCounter(int counter) {
		if (counter != currentCounter) {
			currentCounter = counter;
			refreshCurrentCounter();
		}
	}

	public void switchTrackCounter() {
		trackSkills[currentCounter] = !trackSkills[currentCounter];
		player.getPackets().sendConfigByFile(10444 + currentCounter, trackSkills[currentCounter] ? 1 : 0);
	}

	public void resetCounterXP() {
		xpTracks[currentCounter] = 0;
		refreshCounterXp(currentCounter);
	}

	public void setCounterSkill(int skill) {
		xpTracks[currentCounter] = 0;
		trackSkillsIds[currentCounter] = (byte) skill;
		player.getPackets().sendConfigByFile(10440 + currentCounter, trackSkillsIds[currentCounter] + 1);
		refreshCounterXp(currentCounter);
	}

	public void refreshCounterXp(int counter) {
		player.getPackets().sendConfig(counter == 0 ? 1801 : 2474 + counter, (int) (xpTracks[counter] * 10));
	}

	public void handleSetupXPCounter(int componentId) {
		if (componentId == 18) {
			player.getInterfaceManager().closeXPDisplay();
		} else if (componentId >= 22 && componentId <= 24) {
			setCurrentCounter(componentId - 22);
		} else if (componentId == 27) {
			switchTrackCounter();
		} else if (componentId == 61) {
			resetCounterXP();
		} else if (componentId >= 31 && componentId <= 57) {
			if (componentId == 33) {
				setCounterSkill(4);
			} else if (componentId == 34) {
				setCounterSkill(2);
			} else if (componentId == 35) {
				setCounterSkill(3);
			} else if (componentId == 42) {
				setCounterSkill(18);
			} else if (componentId == 49) {
				setCounterSkill(11);
			} else {
				setCounterSkill(componentId >= 56 ? componentId - 27 : componentId - 31);
			}
		}
	}

	public void sendInterfaces() {
		if (xpDisplay) {
			player.getInterfaceManager().sendXPDisplay();
		}
		if (xpPopup) {
			player.getInterfaceManager().sendXPPopup();
		}
	}

	public void switchXPDisplay() {
		xpDisplay = !xpDisplay;
		if (xpDisplay) {
			player.getInterfaceManager().sendXPDisplay();
		} else {
			player.getInterfaceManager().closeXPDisplay();
		}
	}

	public void switchXPPopup() {
		xpPopup = !xpPopup;
		player.getPackets().sendGameMessage("XP pop-ups are now " + (xpPopup ? "en" : "dis") + "abled.");
		if (xpPopup) {
			player.getInterfaceManager().sendXPPopup();
		} else {
			player.getInterfaceManager().closeXPPopup();
		}
	}

	public void restoreNewSkills() {
		level[25] = (short) getLevelForXp(25);
		refresh(25);
		level[26] = (short) getLevelForXp(26);
		refresh(26);
	}

	public void restoreSummoning() {
		level[23] = (short) getLevelForXp(23);
		refresh(23);
	}

	public void restoreSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		// temporary
		if (xpTracks == null) {
			xpPopup = true;
			xpTracks = new double[3];
			trackSkills = new boolean[3];
			trackSkillsIds = new byte[3];
			trackSkills[0] = true;
			for (int i = 0; i < trackSkillsIds.length; i++) {
				trackSkillsIds[i] = 30;
			}
		}
		enabledSkillsTargets = new boolean[27];
		skillsTargetsUsingLevelMode = new boolean[27];
		skillsTargetsValues = new int[27];

		if (level.length != 27) {//this is the correct way
			level = Arrays.copyOf(level, level.length);
			level[DIVINATION] = 1;
			level[INVENTION] = 1;
		}

		if (xp.length != 27) {
			xp = Arrays.copyOf(xp, xp.length);
			xp[DIVINATION] = 0;
			xp[INVENTION] = 0;
		}
	}

	public short[] getLevels() {
		return level;
	}

	public double[] getXp() {
		return xp;
	}

	public int getLevel(int skill) {
		return level[skill];
	}

	public double getXp(int skill) {
		return xp[skill];
	}

	public boolean hasRequiriments(int... skills) {
		for (int i = 0; i < skills.length; i += 2) {
			int skillId = skills[i];
			if (skillId == CONSTRUCTION || skillId == FARMING)
				continue;
			int skillLevel = skills[i + 1];
			if (getLevelForXp(skillId) < skillLevel)
				return false;

		}
		return true;
	}

	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}

	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}

	public void assassinSet(int skill, int newLevel) {
		assassinLevel[skill] = (short) newLevel;
	}

	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}

	public int getSummoningCombatLevel() {
		return getLevelForXp(Skills.SUMMONING) / 8;
	}

	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 120); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 120;
	}

	public void init() {
		if (enabledSkillsTargets == null)
			enabledSkillsTargets = new boolean[25];
		if (skillsTargetsUsingLevelMode == null)
			skillsTargetsUsingLevelMode = new boolean[25];
		if (skillsTargetsValues == null)
			skillsTargetsValues = new int[25];
		for (int skill = 0; skill < level.length; skill++)
			refresh(skill);
		sendXPDisplay();
	}

	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
		player.getAppearence().generateAppearenceData();
	}

	public int getCounterSkill(int skill) {
		switch (skill) {
		case ATTACK:
			return 0;
		case STRENGTH:
			return 1;
		case DEFENCE:
			return 4;
		case RANGE:
			return 2;
		case HITPOINTS:
			return 5;
		case PRAYER:
			return 6;
		case AGILITY:
			return 7;
		case HERBLORE:
			return 8;
		case THIEVING:
			return 9;
		case CRAFTING:
			return 10;
		case MINING:
			return 12;
		case SMITHING:
			return 13;
		case FISHING:
			return 14;
		case COOKING:
			return 15;
		case FIREMAKING:
			return 16;
		case WOODCUTTING:
			return 17;
		case SLAYER:
			return 19;
		case FARMING:
			return 20;
		case CONSTRUCTION:
			return 21;
		case HUNTER:
			return 22;
		case SUMMONING:
			return 23;
		case DUNGEONEERING:
			return 24;
		case MAGIC:
			return 3;
		case FLETCHING:
			return 18;
		case RUNECRAFTING:
			return 11;
		default:
			return -1;
		}

	}

	public short assassinLevel[];
	private double assassinXp[];

	public void setAssassin() {
		assassinLevel = new short[5];
		assassinXp = new double[5];
	}

	public int getAssassinLevel(int skill) {
		return assassinLevel[skill];
	}

	public double getAssassinXp(int skill) {
		return assassinXp[skill];
	}

	public int getAssassinLevelForXp(int skill) {
		double exp = assassinXp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	public void addAssassinXp(int skill, double exp) {
		if (player.isXpLocked())
			return;
		if (player.getAuraManager().usingWisdom())
			exp *= 1.025;
		if (World.getPlayers().size() >= 20 && World.getPlayers().size() < 30)
			exp *= 2;
		if (player.getGameMode() == 0) { // Squire
			exp *= 1000;
			if (player.isExtremeDonator())
				exp *= 1.4;
			else if (player.isDonator())
				exp *= 1.2;
		} else if (player.getGameMode() == 1) { // Knight
			exp *= 700;
		} else if (player.getGameMode() == 2) { // Legends
			exp *= 100;
		} else if (player.getGameMode() == 3) { // Extreme
			exp *= 40;
		}
		int oldLevel = getAssassinLevelForXp(skill);
		assassinXp[skill] += exp;
		if (assassinXp[skill] > MAXIMUM_EXP) {
			assassinXp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getAssassinLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			assassinLevel[skill] += levelDiff;
			player.getDialogueManager().startDialogue("AssassinLevelUp", skill);
		}
	}

	private double getXpRate(int skill, double exp) {
		return player.getXpRate();
	}

	public void addXp(int skill, double exp) {
		SkillingPetsManager.dropChance(skill, player, exp);
		player.getControllerManager().trackXP(skill, (int) exp);
		if (player.isXpLocked())
			return;
		exp *= getXpRate(skill, exp);
		if (player.getSaradominBlessing() > 1 && skill == FARMING)
			exp *= 1.1;
		if (player.getAuraManager().usingWisdom())
			exp *= 1.025;
		if (skill == World.SKILL_ID)
			exp *= (World.MODIFIER / 10);
		if (XPWell.isWellActive() && !World.isWeekend())
			exp *= 1.5;
		else if (World.isWeekend() && !XPWell.isWellActive())
			exp *= 2;
		else if (XPWell.isWellActive() && World.isWeekend())
			exp *= 3.5;
		if (Settings.DOUBLE_EXP)
			exp *= 2;
		if (skill == Skills.HERBLORE) {
			if (player.getPerkManager().hasPerk(PlayerPerks.GREEN_THUMB)) {
				exp *= 1.25;
			}
		}
		if (skill == Skills.PRAYER) {
			if (player.getPerkManager().hasPerk(PlayerPerks.PRAYER_BETRAYER)) {
				exp *= 1.25;
			}
		}
		if (skill == Skills.DIVINATION) {
			if (player.getPerkManager().hasPerk(PlayerPerks.MASTER_DIVINER)) {
				exp *= 1.25;
			}
		}
		if (skill == Skills.HUNTER) {
			if (player.getPerkManager().hasPerk(PlayerPerks.HUNTSMAN)) {
				exp *= 1.25;
			}
		}
		int oldLevel = getLevelForXp(skill);
		int oldXP = (int) xp[skill];
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| trackSkillsIds[i] == 29
								&& (skill == Skills.ATTACK || skill == Skills.DEFENCE || skill == Skills.STRENGTH
										|| skill == Skills.MAGIC || skill == Skills.RANGE || skill == Skills.HITPOINTS)
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		if (player.getRights() < 2) {
			if (oldXP < 104273167 && xp[skill] >= 104273167 && skill != DUNGEONEERING) {
				LevelUp.send104m(player, skill);
			}
			if (oldXP < 200000000 && xp[skill] >= 200000000) {
				LevelUp.send200m(player, skill);
			}
			if (oldXP < 300000000 && xp[skill] >= 300000000) {
				LevelUp.send300m(player, skill);
			}
			if (oldXP < 400000000 && xp[skill] >= 400000000) {
				LevelUp.send400m(player, skill);
			}
			if (oldXP < 500000000 && xp[skill] == MAXIMUM_EXP) {
				LevelUp.send500m(player, skill);
			}
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearence().generateAppearenceData();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 10);
				else if (skill == PRAYER)
					player.getPrayer().restorePrayer(levelDiff * 10);
			}
			player.getQuestManager().checkCompleted();
		}
		refresh(skill);
		if (skill != HITPOINTS) {
			if (player.getAuraManager().usingWisdom()) {
				player.getPackets().sendConfig(2044, (int) (exp * 10) / 4);
			} else if (Settings.DOUBLE_EXP || World.isWeekend()) {
				player.getPackets().sendConfig(2044, (int) (exp * 10) / 2);
			}
		}
	}

	public void addSkillXpRefresh(int skill, double xp) {
		this.xp[skill] += xp;
		level[skill] = (short) getLevelForXp(skill);
	}

	public void resetSkillNoRefresh(int skill) {
		xp[skill] = 0;
		level[skill] = 1;
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}

	public double addXpLamp(int skill, double exp) {
		player.getControllerManager().trackXP(skill, (int) exp);
		if (player.isXpLocked())
			return 0;
		exp *= 1;
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| (trackSkillsIds[i] == 29
								&& (skill == Skills.ATTACK || skill == Skills.DEFENCE || skill == Skills.STRENGTH
										|| skill == Skills.MAGIC || skill == Skills.RANGE || skill == Skills.HITPOINTS))
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearence().generateAppearenceData();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 10);
				else if (skill == PRAYER)
					player.getPrayer().restorePrayer(levelDiff * 10);
			}
			player.getQuestManager().checkCompleted();
		}
		refresh(skill);
		return exp;
	}

	public int getTotalLevel() {
		int total = 0;
		for (int id = 0; id < level.length; id++) {
			total += getLevelForXp(id);
		}
		return total;
	}

	public int getTotalLevel(Player player) {
		int totallevel = 0;
		for (int i = 0; i <= 26; i++) {
			totallevel += player.getSkills().getLevelForXp(i);
		}
		return totallevel;
	}

	public String getTotalXp(Player player) {
		double totalxp = 0;
		for (double xp : player.getSkills().getXp()) {
			totalxp += xp;
		}
		NumberFormat formatter = new DecimalFormat("#######");
		return formatter.format(totalxp);
	}

	public void summonXP(int skill, double exp) {
		player.getControllerManager().trackXP(skill, (int) exp);
		if (player.isXpLocked())
			return;
		exp *= 1.0;
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| (trackSkillsIds[i] == 29
								&& (skill == Skills.ATTACK || skill == Skills.DEFENCE || skill == Skills.STRENGTH
										|| skill == Skills.MAGIC || skill == Skills.RANGE || skill == Skills.HITPOINTS))
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearence().generateAppearenceData();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 10);
				else if (skill == PRAYER)
					player.getPrayer().restorePrayer(levelDiff * 10);
			}
			player.getQuestManager().checkCompleted();
		}
		refresh(skill);
	}

	public int getTargetIdByComponentId(int componentId) {
		switch (componentId) {
		case 150: // Attack
			return 0;
		case 9: // Strength
			return 1;
		case 40: // Range
			return 2;
		case 71: // Magic
			return 3;
		case 22: // Defence
			return 4;
		case 145: // Constitution
			return 5;
		case 58: // Prayer
			return 6;
		case 15: // Agility
			return 7;
		case 28: // Herblore
			return 8;
		case 46: // Theiving
			return 9;
		case 64: // Crafting
			return 10;
		case 84: // Runecrafting
			return 11;
		case 140: // Mining
			return 12;
		case 135: // Smithing
			return 13;
		case 34: // Fishing
			return 14;
		case 52: // Cooking
			return 15;
		case 130: // Firemaking
			return 16;
		case 125: // Woodcutting
			return 17;
		case 77: // Fletching
			return 18;
		case 90: // Slayer
			return 19;
		case 96: // Farming
			return 20;
		case 102: // Construction
			return 21;
		case 108: // Hunter
			return 22;
		case 114: // Summoning
			return 23;
		case 120: // Dungeoneering
			return 24;
		default:
			return -1;
		}
	}

	public int getSkillIdByTargetId(int targetId) {
		System.out.println(targetId);
		switch (targetId) {
		case 0: // Attack
			return ATTACK;
		case 1: // Strength
			return STRENGTH;
		case 2: // Range
			return RANGE;
		case 3: // Magic
			return MAGIC;
		case 4: // Defence
			return DEFENCE;
		case 5: // Constitution
			return HITPOINTS;
		case 6: // Prayer
			return PRAYER;
		case 7: // Agility
			return AGILITY;
		case 8: // Herblore
			return HERBLORE;
		case 9: // Thieving
			return THIEVING;
		case 10: // Crafting
			return CRAFTING;
		case 11: // Runecrafting
			return RUNECRAFTING;
		case 12: // Mining
			return MINING;
		case 13: // Smithing
			return SMITHING;
		case 14: // Fishing
			return FISHING;
		case 15: // Cooking
			return COOKING;
		case 16: // Firemaking
			return FIREMAKING;
		case 17: // Woodcutting
			return WOODCUTTING;
		case 18: // Fletching
			return FLETCHING;
		case 19: // Slayer
			return SLAYER;
		case 20: // Farming
			return FARMING;
		case 21: // Construction
			return CONSTRUCTION;
		case 22: // Hunter
			return HUNTER;
		case 23: // Summoning
			return SUMMONING;
		case 24: // Dungeoneering
			return DUNGEONEERING;
		default:
			return -1;
		}
	}

	public void refreshEnabledSkillsTargets() {

		int value = Utils.get32BitValue(enabledSkillsTargets, true);
		player.getPackets().sendConfig(1966, value);
	}

	public void refreshUsingLevelTargets() {
		int value = Utils.get32BitValue(skillsTargetsUsingLevelMode, true);
		player.getPackets().sendConfig(1968, value);
	}

	public void refreshSkillsTargetsValues() {
		for (int i = 0; i < 25; i++) {
			player.getPackets().sendConfig(1969 + i, skillsTargetsValues[i]);
		}
	}

	public void setSkillTargetEnabled(int id, boolean enabled) {
		enabledSkillsTargets[id] = enabled;
		refreshEnabledSkillsTargets();
	}

	public void setSkillTargetUsingLevelMode(int id, boolean using) {
		skillsTargetsUsingLevelMode[id] = using;
		refreshUsingLevelTargets();
	}

	public void setSkillTargetValue(int skillId, int value) {
		skillsTargetsValues[skillId] = value;
		refreshSkillsTargetsValues();
	}

	public void setSkillTarget(boolean usingLevel, int skillId, int target) {
		setSkillTargetEnabled(skillId, true);
		setSkillTargetUsingLevelMode(skillId, usingLevel);
		setSkillTargetValue(skillId, target);

	}

	public boolean hasLevel(int skill, int boost, int requiredLevel) {
		return hasLevel(skill, requiredLevel - boost);
	}

	public boolean hasLevel(int skill, int requiredLevel) {
		player.getPackets().sendGameMessage("You must have a " + getSkillName(skill).toLowerCase()
				+ " level of at least " + requiredLevel + " to do this.");
		return false;
	}
}
