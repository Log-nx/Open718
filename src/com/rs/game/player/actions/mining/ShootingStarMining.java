package com.rs.game.player.actions.mining;

import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.npc.random.LiquidGoldNymph;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.activities.events.ShootingStar;
import com.rs.utils.Utils;

public class ShootingStarMining extends MiningBase {

	private WorldObject rock;

	public ShootingStarMining(WorldObject rock) {
		this.rock = rock;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You swing your pickaxe at the rock.");
		setActionDelay(player, getMiningDelay());
		return true;
	}

	private int getMiningDelay() {
		return ShootingStar.getStarSize() * 2;
	}

	private boolean checkAll(Player player) {
		player.closeInterfaces();
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
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		int level = ShootingStar.getLevel();
		if (level > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage("You need a mining level of " + level + " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		setAnimationAndGFX(player);
		if (Utils.random(500) == 0) {
    		new LiquidGoldNymph(player, player);
    		player.sm("<col=ff0000>A Liquid Gold Nymph emerges from the rock.");
    	}
		player.faceObject(rock);
		return checkRock(player);
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(ShootingStar.STARDUST, 1)) {
			player.setNextAnimation(new Animation(-1));
			player.sm("Inventory full. To make more room, sell, drop or bank something.");
			return -1;
		}
		return getMiningDelay();
	}

	private void addOre(Player player) {
		player.getSkills().addXp(Skills.MINING, ShootingStar.getXP());
		if (!player.getInventory().containsItem(ShootingStar.STARDUST, 200)) {
			player.getGamePointManager().addGamePointItem(ShootingStar.STARDUST, 1);
		}
		player.getPackets().sendGameMessage("You mine some stardust.", true);
		ShootingStar.reduceStarLife();
	}

    private boolean checkRock(Player player) {
    	return World.containsObjectWithId(rock, rock.getId());
    }
}
