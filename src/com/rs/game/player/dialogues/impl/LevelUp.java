package com.rs.game.player.dialogues.impl;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Colors;


public final class LevelUp extends Dialogue {
	
		public enum Configs {
			ATTACK(1, 0),
			STRENGTH(2, 2),
			DEFENCE(5, 1),
			HITPOINTS(6, 3),
			RANGE(3, 4),
			MAGIC(4, 6),
			PRAYER(7, 5),
			AGILITY(8, 16),
			HERBLORE(9, 15),
			THIEVING(10, 17),
			CRAFTING(11, 12),
			RUNECRAFTING(12, 20),
			MINING(13, 14),
			SMITHING(14, 13),
			FISHING(15, 10),
			COOKING(16, 7),
			FIREMAKING(17, 11),
			WOODCUTTING(18, 8),
			FLETCHING(19, 9),
			SLAYER(20, 18),
			FARMING(21, 19),
			CONSTRUCTION(22, 22),
			HUNTER(23, 21),
			SUMMONING(24, 23),
			DUNGEONEERING(25, 24),
			DIVINATION(26, 25),
			INVENTION(27,26);
			
		private int configId;
		private int skillId;
	
		private Configs(int configId, int skillId) {
			this.configId = configId;
			this.skillId = skillId;
		}
			
		public int getConfigId() {
			return configId;
		}
	
		private static Map<Integer, Configs> configs = new HashMap<Integer, Configs>();
	
		public static Configs levelup(int skill) {
			return configs.get(skill);
		}
	
		static {
			for (Configs config : Configs.values()) {
				configs.put(config.skillId, config);
			}
		}
	}
		
	    /**
	     * Skill level-up Music effect ID's.
	     */
	    public static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = { 30, 38, 66, 48,
		    58, 56, 52, 34, 70, 44, 42, 40, 36, 64, 54, 46, 28, 68, 61, 10, 60,
		    50, 32, 301, 417, 42, 42
		};
	
		@Override
		public void start() {
			int skill = (Integer) parameters[0];
			final int level = player.getSkills().getLevelForXp(skill);
			player.getSkills();
			final int xp = Skills.getXPForLevel(skill);
			player.getTemporaryAttributtes().put("leveledUp", skill);
			player.getTemporaryAttributtes().put("leveledUp[" + skill + "]", Boolean.TRUE);
			player.setNextGraphics(new Graphics(199));
			if (level == 99 || level == 120)
				player.setNextGraphics(new Graphics(1765));
			final String name = Skills.SKILL_NAME[skill];
			player.sm("You've just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level! You are now level " + level + ".");
			player.getPackets().sendConfigByFile(4757, getIconValue(skill));
			player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? 69 : 47, 1216);
			final Configs levelup = Configs.levelup(skill);
			player.getPackets().sendGlobalConfig(1756, levelup.getConfigId());
			player.getPackets().sendConfigByFile(4757, levelup.getConfigId());
			switchFlash(player, skill, true);
			final int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
			if (musicEffect != -1)
				player.getPackets().sendMusicEffect(musicEffect);
			if (player.getRights() < 2 && (level == 99 || level == 120))
				sendNews(player, skill, level);
			if (xp == 200000000)
				send200m(player, skill);
			if (xp == 300000000)
				send300m(player, skill);
			if (xp == 400000000)
				send300m(player, skill);
			if (xp == 500000000)
				send300m(player, skill);
		}
	
	public static void sendNews(Player player, int skill, int level) {
		boolean reachedAll = true;
		for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
			if (player.getSkills().getLevelForXp(i) < 99) {
				reachedAll = false;
				break;
			}
		}
		if (!reachedAll) {
			if (player.inform99s == true) {
			String gameMode = "";
			if (player.gameMode == 3) {
				gameMode = "Veteran";
			} else if (player.gameMode == 2) {
				gameMode = "Legend";
			} else if (player.gameMode == 1) {
				gameMode = "Knight";
			} else if (player.gameMode == 0) {
				gameMode = "Squire";
			}
			World.sendWorldMessage("<img=10><col=ff8c38>News: " + player.getDisplayName() + " has achieved " + level + " " + Skills.SKILL_NAME[skill] + " using the game mode "+gameMode+".", false, false);
			}
			return;
		}
		player.getBank().addItem(20767, 1, true);
		player.getBank().addItem(20768, 1, true);
		player.isMaxed = 1;
		player.sm("You've finally completed the requirements to get the Max Cape & Hood.");
		player.sm("The set has been placed into your bank.");
		if (player.inform99s == true) {
			String gameMode = "";
			if (player.gameMode == 3) {
				gameMode = "Extreme";
			} else if (player.gameMode == 2) {
				gameMode = "Legend";
			} else if (player.gameMode == 1) {
				gameMode = "Knight";
			} else if (player.gameMode == 0) {
				gameMode = "Squire";
			}
		World.sendWorldMessage("<img=5><col=ff0000>News: " + player.getDisplayName() + " has just achieved at least level 99 in all skills on "+gameMode+".", false, false);
	}
}




    public static int getIconValue(int skill) {
    	if (skill == Skills.ATTACK)
    	    return 1;
    	if (skill == Skills.STRENGTH)
    	    return 2;
    	if (skill == Skills.RANGE)
    	    return 3;
    	if (skill == Skills.MAGIC)
    	    return 4;
    	if (skill == Skills.DEFENCE)
    	    return 5;
    	if (skill == Skills.HITPOINTS)
    	    return 6;
    	if (skill == Skills.PRAYER)
    	    return 7;
    	if (skill == Skills.AGILITY)
    	    return 8;
    	if (skill == Skills.HERBLORE)
    	    return 9;
    	if (skill == Skills.THIEVING)
    	    return 10;
    	if (skill == Skills.CRAFTING)
    	    return 11;
    	if (skill == Skills.RUNECRAFTING)
    	    return 12;
    	if (skill == Skills.MINING)
    	    return 13;
    	if (skill == Skills.SMITHING)
    	    return 14;
    	if (skill == Skills.FISHING)
    	    return 15;
    	if (skill == Skills.COOKING)
    	    return 16;
    	if (skill == Skills.FIREMAKING)
    	    return 17;
    	if (skill == Skills.WOODCUTTING)
    	    return 18;
    	if (skill == Skills.FLETCHING)
    	    return 19;
    	if (skill == Skills.SLAYER)
    	    return 20;
    	if (skill == Skills.FARMING)
    	    return 21;
    	if (skill == Skills.CONSTRUCTION)
    	    return 22;
    	if (skill == Skills.HUNTER)
    	    return 23;
    	if (skill == Skills.SUMMONING)
    	    return 24;
    	else if (skill == Skills.DUNGEONEERING)
    	    return 25;
    	return 26;
    }

    public static void switchFlash(Player player, int skill, boolean on) {
    	int id = 0;
    	if (skill == Skills.ATTACK)
    	    id = 3267;
    	else if (skill == Skills.STRENGTH)
    	    id = 3268;
    	else if (skill == Skills.DEFENCE)
    	    id = 3269;
    	else if (skill == Skills.RANGE)
    	    id = 3270;
    	else if (skill == Skills.PRAYER)
    	    id = 3271;
    	else if (skill == Skills.MAGIC)
    	    id = 3272;
    	else if (skill == Skills.HITPOINTS)
    	    id = 3273;
    	else if (skill == Skills.AGILITY)
    	    id = 3274;
    	else if (skill == Skills.HERBLORE)
    	    id = 3275;
    	else if (skill == Skills.THIEVING)
    	    id = 3276;
    	else if (skill == Skills.CRAFTING)
    	    id = 3277;
    	else if (skill == Skills.FLETCHING)
    	    id = 3278;
    	else if (skill == Skills.MINING)
    	    id = 3279;
    	else if (skill == Skills.SMITHING)
    	    id = 3280;
    	else if (skill == Skills.FISHING)
    	    id = 3281;
    	else if (skill == Skills.COOKING)
    	    id = 3282;
    	else if (skill == Skills.FIREMAKING)
    	    id = 3283;
    	else if (skill == Skills.WOODCUTTING)
    	    id = 3284;
    	else if (skill == Skills.RUNECRAFTING)
    	    id = 3285;
    	else if (skill == Skills.SLAYER)
    	    id = 3286;
    	else if (skill == Skills.FARMING)
    	    id = 3287;
    	else if (skill == Skills.CONSTRUCTION)
    	    id = 3288;
    	else if (skill == Skills.HUNTER)
    	    id = 3289;
    	else if (skill == Skills.SUMMONING)
    	    id = 3290;
    	else if (skill == Skills.DUNGEONEERING)
    	    id = 3291;
		player.getPackets().sendConfigByFile(id, on ? 1 : 0);
    }
	
    /**
     * Sends the Mastery cape World announcement.
     * @param player The player.
     * @param skill The skill ID.
     */
    public static void send104m(Player player, int skill) {
		sendMessage(player, skill, 104273167, player.getXPMode());
		player.sm("<col=825200><shad=000000>Well done! You've achieved 104,273,167 XP in this skill! You can now purchase a Mastery Cape from the Wise Old Man.");
		return;
	}
    
	public static void send200m(Player player, int skill) {
		sendMessage(player, skill, 200000000, player.getXPMode());
		return;
	}
	
	public static void send300m(Player player, int skill) {
		sendMessage(player, skill, 300000000, player.getXPMode());
		return;
	}
	public static void send400m(Player player, int skill) {
		sendMessage(player, skill, 400000000, player.getXPMode());
		return;
	}
	public static void send500m(Player player, int skill) {
		sendMessage(player, skill, 500000000, player.getXPMode());
		return;
	}
	public static void send600m(Player player, int skill) {
		sendMessage(player, skill, 600000000, player.getXPMode());
		return;
	}
	public static void send700m(Player player, int skill) {
		sendMessage(player, skill, 700000000, player.getXPMode());
		return;
	}
	public static void send800m(Player player, int skill) {
		sendMessage(player, skill, 800000000, player.getXPMode());
		return;
	}
	public static void send900m(Player player, int skill) {
		sendMessage(player, skill, 900000000, player.getXPMode());
		return;
	}
	public static void send1b(Player player, int skill) {
		sendMessage(player, skill, 1000000000, player.getXPMode());
		return;
	}
	
	public static void sendMessage(Player player, int skill, int exp, String gameMode) {
    	player.getPackets().sendMusicEffect(320);
		player.setNextGraphics(new Graphics(1765));
		World.sendWorldMessage("<img=5>" + Colors.ORANGE + "<shad=000000>News: " + player.getDisplayName() + " has achieved " + exp + " experience in the " + Skills.SKILL_NAME[skill] + " skill on "+gameMode+" mode!", false, false);
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		end();
	}

	@Override
	public void finish() {
	}
}