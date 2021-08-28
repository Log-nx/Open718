package com.rs.game.player.actions.mining;

import com.rs.game.Animation;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.npc.random.LiquidGoldNymph;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.events.GlobalEvents.Event;
import com.rs.game.player.content.perks.GamePointRewards;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.utils.Utils;

public class LivingMineralMining extends MiningBase {

    private LivingRock rock;

    public LivingMineralMining(LivingRock rock) {
	this.rock = rock;
    }

    private void addOre(Player player) {
    	double xp = 1.0;
    	xp *= miningSuit(player);
    	player.getSkills().addXp(Skills.MINING, xp);
    	player.getGamePointManager().addGamePointItem(15263, Utils.random(5, 25));
    	player.getPackets().sendGameMessage("You manage to mine some living minerals.", true);
    }

    private boolean checkAll(Player player) {
    if (getPickAxeDefinitions(player) == null) {
    	player.getPackets().sendGameMessage("You do not have a pickaxe or do not have the required level to use the pickaxe.");
    	return false;
    }
	if (!hasMiningLevel(player))
	    return false;
	if (!player.getInventory().hasFreeSlots()) {
	    player.sm("Inventory full. To make more room, sell, drop or bank something.");
	    return false;
	}
	if (!rock.canMine(player)) {
	    player.getPackets() .sendGameMessage(  "You must wait at least one minute before you can mine a living rock creature that someone else defeated.");
	    return false;
	}
	return true;
    }

    private boolean checkRock(Player player) {
	return !rock.hasFinished();
    }

    private int getMiningDelay(Player player) {
		int oreBaseTime = 50;
		int oreRandomTime = 20;
		int mineTimer = oreBaseTime - player.getSkills().getLevel(Skills.MINING) - Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + oreRandomTime)
		    mineTimer = 1 + Utils.getRandom(oreRandomTime);
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		if (player.getPerkManager().hasPerk(PlayerPerks.QUARRY_MASTER)) {
			mineTimer /= 1.33;
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
	if (73 > player.getSkills().getLevel(Skills.MINING)) {
	    player.getPackets().sendGameMessage( "You need a mining level of 73 to mine this rock.");
	    return false;
	}
	return true;
    }

    @Override
    public boolean process(Player player) {
    	setAnimationAndGFX(player);
		if (Utils.random(250) == 0) {
			new LiquidGoldNymph(player, player);
			player.sm("<col=ff0000>A Liquid Gold Nymph emerges from the rock.");
		}
		return checkRock(player);
    }

    @Override
    public int processWithDelay(Player player) {
	addOre(player);
	rock.takeRemains();
	player.setNextAnimation(new Animation(-1));
	return -1;
    }

    @Override
    public boolean start(Player player) {
	if (!checkAll(player))
	    return false;
	setActionDelay(player, getMiningDelay(player));
	return true;
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
    	return xpBoost;
    }
}