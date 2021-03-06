package com.rs.game.player.actions.firemaking;

import java.util.ArrayList;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.random.FireSpirit;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class Bonfire extends Action {

    public static enum Log {

		LOG(1511, 3098, 1, 40, 6), 
		OAK(1521, 3099, 15, 50, 12), 
		WILLOW(1519, 3101, 30, 80.5, 18), 
		TEAK(6333, 3099, 35, 105, 25), 
		ARCTIN_PINE(10810, 3099, 42, 125, 30), 
		MAPLE(1517, 3100, 45, 150, 36), 
		MAHOGANY(6332, 3099, 50, 150, 40), 
		EUCALYPTUS(12582, 3099, 58, 193.5, 49), 
		YEWS(1515, 3111, 60, 200, 54), 
		MAGIC(1513, 3135, 75, 230, 60);
	    	
		private int logId, gfxId, level, boostTime;
		double xp;
	
		private Log(int logId, int gfxId, int level, double xp, int boostTime) {
		    this.logId = logId;
		    this.gfxId = gfxId;
		    this.level = level;
		    this.xp = xp;
		    this.boostTime = boostTime;
		}
	
		public int getLogId() {
		    return logId;
		}
    }

    public static boolean addLog(Player player, WorldObject object, Item item) {
		for (Log log : Log.values())
		    if (log.logId == item.getId()) {
		    	player.getActionManager().setAction(new Bonfire(log, object));
		    	return true;
		    }
		return false;
    }

    public static void addLogs(Player player, WorldObject object) {
		ArrayList<Log> possiblities = new ArrayList<Log>();
		for (Log log : Log.values())
		    if (player.getInventory().containsItem(log.logId, 1))
		    	possiblities.add(log);
		Log[] logs = possiblities.toArray(new Log[possiblities.size()]);
		if (logs.length == 0)
		    player.sm("You do not have any logs to add to this fire.");
		else if (logs.length == 1)
		    player.getActionManager().setAction(new Bonfire(logs[0], object));
		else
		    player.getDialogueManager().startDialogue("BonfireD", logs, object);
    }

    public static double getBonfireBoostMultiplier(Player player) {
		int fmLvl = player.getSkills().getLevel(Skills.FIREMAKING);
		if (fmLvl >= 90)
		    return 1.1;
		if (fmLvl >= 80)
		    return 1.09;
		if (fmLvl >= 70)
		    return 1.08;
		if (fmLvl >= 60)
		    return 1.07;
		if (fmLvl >= 50)
		    return 1.06;
		if (fmLvl >= 40)
		    return 1.05;
		if (fmLvl >= 30)
		    return 1.04;
		if (fmLvl >= 20)
		    return 1.03;
		if (fmLvl >= 10)
		    return 1.02;
		return 1.01;
    }

    private Log log;

    private WorldObject object;

    private int count;

    public Bonfire(Log log, WorldObject object) {
		this.log = log;
		this.object = object;
    }

    private boolean checkAll(Player player) {
	    if (!World.containsObjectWithId(object, object.getId()))
		    return false;
		if (!player.getInventory().containsItem(log.logId, 1))
		    return false;
		if (player.getSkills().getLevel(Skills.FIREMAKING) < log.level) {
		    player.getDialogueManager().startDialogue("SimpleMessage",
			    "You need level " + log.level + " Firemaking to add these logs to a bonfire.");
		    return false;
		}
		return true;
    }

    @Override
    public boolean process(Player player) {
		if (checkAll(player)) {
		    if (Utils.random(player.getPerkManager().hasPerk(PlayerPerks.PYROMATIC) ? 375 : 500) == 0) {
		    	new FireSpirit(new WorldTile(object, 1), player);
		    	player.sm("<col=ff0000>A fire spirit emerges from the bonfire.");
		    }
		    return true;
		}
		return false;
    }

    @Override
    public int processWithDelay(Player player) {
    	
		player.closeInterfaces();
		player.getStatistics().addLogsBurned();
		player.getInventory().deleteItem(log.logId, 1);
		player.getSkills().addXp(Skills.FIREMAKING, Firemaking.increasedExperience(player, log.xp) 
									* (player.getPerkManager().hasPerk(PlayerPerks.PYROMATIC) ? 1.15 : 1));
		if (log == Log.LOG) {
			player.getSkillTasks().decreaseTask(SkillTasks.FNORMAL1);
			player.getSkillTasks().decreaseTask(SkillTasks.FNORMAL2);
		} else if (log == Log.OAK) {
			player.getSkillTasks().decreaseTask(SkillTasks.FOAK1);
			player.getSkillTasks().decreaseTask(SkillTasks.FOAK2);
		} else if (log == Log.WILLOW) {
			player.getSkillTasks().decreaseTask(SkillTasks.FWILLOW1);
			player.getSkillTasks().decreaseTask(SkillTasks.FWILLOW2);
			player.getSkillTasks().decreaseTask(SkillTasks.FWILLOW3);
		} else if (log == Log.MAPLE) {
			player.getSkillTasks().decreaseTask(SkillTasks.FMAPLE1);
			player.getSkillTasks().decreaseTask(SkillTasks.FMAPLE2);
		} else if (log == Log.YEWS) {
			player.getSkillTasks().decreaseTask(SkillTasks.FYEW1);
			player.getSkillTasks().decreaseTask(SkillTasks.FYEW2);
			player.getSkillTasks().decreaseTask(SkillTasks.FYEW3);
		} else if (log == Log.MAGIC) {
			player.getSkillTasks().decreaseTask(SkillTasks.FMAGIC1);
			player.getSkillTasks().decreaseTask(SkillTasks.FMAGIC2);
		}
		player.getSkillTasks().decreaseTask(SkillTasks.FBON1);
		player.getSkillTasks().decreaseTask(SkillTasks.FBON2);
		player.getSkillTasks().decreaseTask(SkillTasks.FBON3);
		player.setNextAnimation(new Animation(16703));
		player.setNextGraphics(new Graphics(log.gfxId));
		player.getPackets().sendGameMessage("You add a log to the fire; logs burned " + Colors.RED+Utils.getFormattedNumber(player.getStatistics().getLogsBurned())+"</col>.", true);
		if (count++ == 4 && player.getLastBonfire() == 0) {
		    player.setLastBonfire(log.boostTime * 100);
		    int percentage = (int) (getBonfireBoostMultiplier(player) * 100 - 100);
		    player.sm("<col=00ff00>The bonfire's warmth increases your maximum health by "  + percentage + "%. This will last " + log.boostTime + " minutes.");
		    player.getEquipment().refreshConfigs(false);
		}
		player.faceObject(object);
		return 6;
    }

    @Override
    public boolean start(Player player) {
		if (checkAll(player)) {
		    player.getAppearence().setRenderEmote(2498);
		    return true;
		}
		return false;
    }

    @Override
    public void stop(final Player player) {
		player.getEmotesManager().setNextEmoteEnd(2400);
		WorldTasksManager.schedule(new WorldTask() {
	
		    @Override
		    public void run() {
		    	player.setNextAnimation(new Animation(16702));
		    	player.getAppearence().setRenderEmote(-1);
		    }
		}, 3);
    }

	public static Log getLog(int logId) {
		for (Log log : Log.values()) {
			if (log.getLogId() == logId)
				return log;
		}
		return null;
	}
}