package com.rs.game.player.actions.thieving;

import java.util.List;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

/**
 * Handles the Thieving Skill
 * 
 * @author Dragonkk
 * 
 */
public class Thieving {

    public enum Stalls {
		VEGETABAL(0, 2, new int[] { 1957, 1965, 1942, 1982, 1550 }, 1, 2, 10, 34381), 
		CAKE(34384, 5, new int[] { 1891, 1897, 2309 }, 1, 2.5, 16, 34381), 
		CRAFTING(0, 5, new int[] { 1755, 1592, 1597 }, 1, 7, 16, 34381), 
		MONKEY_FOOD(0, 5, new int[] { 1963 }, 1, 7, 16, 34381), 
		MONKEY_GENERAL(0, 5, new int[] { 1931, 2347, 590 }, 1, 7, 16, 34381), 
		TEA_STALL(0, 5, new int[] { 712 }, 1, 7, 16, 34381), 
		SILK_STALL(34383, 20, new int[] { 950 }, 1, 8, 24, 34381), 
		WINE_STALL(14011, 22, new int[] { 1937, 1993, 1987, 1935, 7919 }, 1, 16, 27, 2046), 
		SEED_STALL(7053, 27, new int[] { 5096, 5097, 5098, 5099, 5100, 5101, 5102, 5103, 5105 }, 30, 11, 10, 2047), 
		FUR_STALL(34387, 35, new int[] { 6814, 958 }, 1, 15, 36, 34381), 
		FISH_STALL(0, 42, new int[] { 331, 359, 377 }, 1, 16, 42, 34381), 
		CROSSBOW_STALL(0, 49, new int[] { 877, 9420, 9440 }, 1, 11, 52, 34381), 
		SILVER_STALL(0, 50, new int[] { 442 }, 1, 30, 54, 34381), 
		SPICE_STALL(34386, 65, new int[] { 2007 }, 1, 80, 81, 34381), 
		MAGIC_STALL(0, 65, new int[] { 556, 557, 554, 555, 563 }, 30, 80, 100, 34381), 
		SCIMITAR_STALL(0, 65, new int[] { 1323 }, 1, 80, 100, 34381);

	private int[] item;
	private int level;
	private int amount;
	private int objectId;
	private int replaceObject;
	private double experience;
	private double seconds;

	Stalls(int objectId, int level, int[] item, int amount, double seconds, double experience, int replaceObject) {
	    this.objectId = objectId;
	    this.level = level;
	    this.item = item;
	    this.amount = amount;
	    this.seconds = seconds;
	    this.experience = experience;
	    this.replaceObject = replaceObject;
	}

	public int getAmount() {
	    return amount;
	}

	public double getExperience() {
	    return experience;
	}

	public int getItem(int count) {
	    return item[count];
	}

	public int getLevel() {
	    return level;
	}

	public int getObjectId() {
	    return objectId;
	}

	public int getReplaceObject() {
	    return replaceObject;
	}

	public double getTime() {
	    return seconds;
	}
}

public static void checkGuards(Player player) {
	NPC guard = null;
	int lastDistance = -1;
	for (int regionId : player.getMapRegionsIds()) {
	    List<Integer> npcIndexes = World.getRegion(regionId).getNPCsIndexes();
	    if (npcIndexes == null)
	    	continue;
	    for (int npcIndex : npcIndexes) {
	    	NPC npc = World.getNPCs().get(npcIndex);
	    	if (npc == null)
	    		continue;
	    	if (!isGuard(npc.getId()) || npc.isUnderCombat() || npc.isDead() 
	    			|| !npc.withinDistance(player, 4) || !npc.clipedProjectile(player, true))
		    continue;
	    	int distance = Utils.getDistance(npc.getX(), npc.getY(),
	    			player.getX(), player.getY());
	    	if (lastDistance == -1 || lastDistance > distance) {
	    		guard = npc;
	    		lastDistance = distance;
	    	}
	    }
	}
	if (guard != null && !player.getPerkManager().hasPerk(PlayerPerks.SNEAK_FACTOR)) {
	    guard.setNextForceTalk(new ForceTalk("Hey, what do you think you are doing!"));
	    player.getPackets().sendGameMessage("Purchase the Sleight of Hand perk to never get caught.", true);
	    guard.setTarget(player);
	}
}

private static int getIncreasedChance(Player player) {
	int chance = 0;
	if (Equipment.getItemSlot(Equipment.SLOT_HANDS) == 10075)
	    chance += 12;
	if (Equipment.getItemSlot(Equipment.SLOT_CAPE) == 15349)
	    chance += 15;
	return chance;
}

public static void handleStalls(final Player player, final WorldObject object) {
	if (player.getAttackedBy() != null && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
	    player.getPackets().sendGameMessage("You can't do this while you're under combat.");
	    return;
	}
	for (final Stalls stall : Stalls.values()) {
	    if (stall.getObjectId() == object.getId()) {
			final WorldObject emptyStall = new WorldObject(stall.getReplaceObject(), 10, object.getRotation(),
				object.getX(), object.getY(), object.getPlane());
			if (player.getSkills().getLevel(Skills.THIEVING) < stall.getLevel()) {
			    player.getPackets().sendGameMessage("You need a thieving level of " + stall.getLevel() + " to steal from this.", true);
			    return;
			}
			if (!player.getInventory().hasFreeSlots()) {
			    player.getPackets().sendGameMessage("Inventory full. To make more room, sell, drop or bank something.", true);
			    return;
			}
	
			player.setNextAnimation(new Animation(881));
			player.lock(2);
			
			WorldTasksManager.schedule(new WorldTask() {
			    
				boolean gaveItems;
	
			    @Override
				public void run() {
					if (!World.containsObjectWithId(object, object.getId()))
						return;
					if (!gaveItems) {
						player.getGamePointManager().addGamePointItem(stall.getItem(Utils.getRandom(stall.item.length - 1)), Utils.getRandom(stall.getAmount()));
					    player.getSkills().addXp(Skills.THIEVING, stall.getExperience());
				    	player.getStatistics().addTimesStolen();
				    	player.getPackets().sendGameMessage("You've successfully stolen from this stall; " + "times thieved: "+Colors.RED+Utils.getFormattedNumber(player.getStatistics().getTimesStolen())+"</col>.", true);
					    gaveItems = true;
						player.getSkillTasks().decreaseTask(SkillTasks.STALL1);
						player.getSkillTasks().decreaseTask(SkillTasks.STALL2);
						player.getSkillTasks().decreaseTask(SkillTasks.STALL3);
						player.getSkillTasks().decreaseTask(SkillTasks.STALL4);
					    checkGuards(player);
					    stop();
					} else {
					    World.spawnObjectTemporary(emptyStall, (int) (1500 * stall.getTime()));
					    stop();
					}
			    }
			}, 0, 0);
	    }
	}
}

public static boolean isGuard(int npcId) {
	if (npcId == 32 || npcId == 21 || npcId == 2256 || npcId == 23)
	    return true;
	else
	    return false;
}

public static boolean pickDoor(Player player, WorldObject object) {
	if (player.getTemporaryAttributtes().get("numbFingers") == null)
	    player.getTemporaryAttributtes().put("numbFingers", 0);
	int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
	int increasedChance = getIncreasedChance(player);
	int decreasedChance = (Integer) player.getTemporaryAttributtes().get("numbFingers");
	int level = Utils.getRandom(thievingLevel + (increasedChance - decreasedChance)) + 1;
	double ratio = level / (Utils.getRandom(45 + 5) + 1);
	if (Math.round(ratio * thievingLevel) < (player.getAttackedByDelay() > 0 ? 50 : 40)
		/ player.getAuraManager().getThievingAccurayMultiplier() && !player.getPerkManager().hasPerk(PlayerPerks.SNEAK_FACTOR)) {
	    player.getPackets().sendGameMessage("You fail to unlock the door and your hands begin to numb down.", true);
	    player.getPackets().sendGameMessage("Purchase the Sleight of Hand perk to never fail picking locks.", true);
	    player.getTemporaryAttributtes().put("numbFingers", decreasedChance + 1);
	    return false;
	}
	player.getPackets().sendGameMessage("You successfully unlock the door.");
	ObjectHandler.handleDoor(player, object, 1500 + Utils.getRandom(1000));
	return true;
}

/**
 * XP modifier by wearing items.
 * @param player The player.
 * @return the XP modifier.
 */
public static double outfitBoost(Player player) {
	double xpBoost = 1.0;
	if (player.getEquipment().getHatId() == 21482)
		xpBoost *= 1.01;
	if (player.getEquipment().getChestId() == 21480)
		xpBoost *= 1.01;
	if (player.getEquipment().getLegsId() == 21481)
		xpBoost *= 1.01;
	if (player.getEquipment().getBootsId() == 21483)
		xpBoost *= 1.01;
	if (player.getEquipment().getHatId() == 21482
			&& player.getEquipment().getChestId() == 21480
			&& player.getEquipment().getLegsId() == 21481
			&& player.getEquipment().getBootsId() == 21483)
		xpBoost *= 1.01;
	return xpBoost;
}
}