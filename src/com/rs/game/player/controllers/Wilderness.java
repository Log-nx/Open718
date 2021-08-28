package com.rs.game.player.controllers;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceMovement;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.thieving.Thieving;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class Wilderness extends Controller {

	public static void checkBoosts(Player player) {
		boolean changed = false;
		int level = player.getSkills().getLevelForXp(Skills.ATTACK);
		int maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.ATTACK)) {
			player.getSkills().set(Skills.ATTACK, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.STRENGTH);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.STRENGTH)) {
			player.getSkills().set(Skills.STRENGTH, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.DEFENCE);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.DEFENCE)) {
			player.getSkills().set(Skills.DEFENCE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.RANGE);
		maxLevel = (int) (level + 5 + (level * 0.1));
		if (maxLevel < player.getSkills().getLevel(Skills.RANGE)) {
			player.getSkills().set(Skills.RANGE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.MAGIC);
		maxLevel = level + 5;
		if (maxLevel < player.getSkills().getLevel(Skills.MAGIC)) {
			player.getSkills().set(Skills.MAGIC, maxLevel);
			changed = true;
		}
		if (changed)
			player.getPackets().sendGameMessage("Your extreme potion bonus has been reduced.");
	}

	public static final boolean isAtDitch(WorldTile tile) {
		if (tile.getRegionId() == 10057)
			return true;
		return (tile.getX() >= 2940 && tile.getX() <= 3395
				&& tile.getY() <= 3524 && tile.getY() >= 3523 || tile.getX() >= 3084
				&& tile.getX() <= 3092
				&& tile.getY() >= 3929
				&& tile.getY() <= 3937);
	}

	public static final boolean isAtWild(WorldTile tile) {
		if (tile.getRegionId() == 12192)
			return true;
		return (tile.getX() >= 3011 && tile.getX() <= 3132
				&& tile.getY() >= 10052 && tile.getY() <= 10175)
				|| (tile.getX() >= 2940 && tile.getX() <= 3395
						&& tile.getY() >= 3525 && tile.getY() <= 4000)
				|| (tile.getX() >= 3264 && tile.getX() <= 3279
						&& tile.getY() >= 3279 && tile.getY() <= 3672)
				|| (tile.getX() >= 2756 && tile.getX() <= 2875
						&& tile.getY() >= 5512 && tile.getY() <= 5627)
				|| (tile.getX() >= 3158 && tile.getX() <= 3181
						&& tile.getY() >= 3679 && tile.getY() <= 3697)
				|| (tile.getX() >= 3280 && tile.getX() <= 3183
						&& tile.getY() >= 3885 && tile.getY() <= 3888)
				|| (tile.getX() >= 3012 && tile.getX() <= 3059
						&& tile.getY() >= 10303 && tile.getY() <= 10351);
	}

	public static boolean isDitch(int id) {
		return id >= 1440 && id <= 1444 || id >= 65076 && id <= 65087;
	}

	private boolean showingSkull;

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (player.isCanPvp() && !p2.isCanPvp()) {
				player.getPackets().sendGameMessage("That player is not in the wilderness.");
				return false;
			}
			if (canHit(target))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC)
			return true;
		Player p2 = (Player) target;
		if (Math.abs(player.getSkills().getCombatLevel() - p2.getSkills().getCombatLevel()) > getWildLevel()) {
			player.getPackets().sendGameMessage("The difference between your combat level and the combat level of " + p2.getDisplayName() + " is too great.");
			player.getPackets().sendGameMessage("You need to move deeper into the Wilderness to be available to attack.");
			return false;
		}
		return true;
	}

	@Override
	public void forceClose() {
		removeIcon();
	}

	public int getWildLevel() {
		if (player.getY() > 9900)
			return (player.getY() - 9912) / 8 + 1;
		return (player.getY() - 3520) / 8 + 1;
	}

	public static boolean isAtForinthryDungeon(WorldTile tile) {
		return (tile.getX() >= 3011 && tile.getX() <= 3132 && tile.getY() >= 10052 && tile.getY() <= 10175);
	}

	public boolean isAtWildSafe() {
		if (player.getRegionId() == 10057)
			return true;
		return (player.getX() >= 2940 && player.getX() <= 3395 && player.getY() <= 3524 && player.getY() >= 3523);
	}

	@Override
	public boolean keepCombating(Entity target) {
		if (target instanceof NPC)
			return true;
		if (!canAttack(target))
			return false;
		return true;
	}

	@Override
	public boolean login() {
		moved();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void moved() {
		boolean isAtWild = isAtWild(player);
		boolean isAtWildSafe = isAtWildSafe();
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.setCanPvp(true);
			showSkull();
			player.getAppearence().generateAppearenceData();
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			removeIcon();
		} else if (!isAtWildSafe && !isAtWild) {
			player.setCanPvp(false);
			removeIcon();
			removeController();
		} else if (Kalaboss.isAtKalaboss(player)) {
			removeIcon();
			player.setCanPvp(false);
			removeController();
			player.getControllerManager().startController("Kalaboss");
		}
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		if (getWildLevel() > 20 && player.getRegionId() != 10057) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		if (getWildLevel() > 20 && player.getRegionId() != 10057) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;

	}

	/*@Override
	public boolean processMoneyPouch() {
		player.getPackets().sendGameMessage(
				"You can't use the Add-to-pouch option in Wilderness.");
		return false;
	}*/

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (isDitch(object.getId())) {
			player.lock();
			player.setNextAnimation(new Animation(6132));
			final WorldTile toTile = new WorldTile(object.getRotation() == 1 || object.getRotation() == 3 ? object.getX() + 2 : player.getX(), object.getRotation() == 0 || object.getRotation() == 2 ? object.getY() - 1 : player.getY(), object.getPlane());

			player.setNextForceMovement(new ForceMovement(new WorldTile(player), 1, toTile, 2, object.getRotation() == 0 || object.getRotation() == 2 ? ForceMovement.SOUTH : ForceMovement.EAST));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(toTile);
					player.faceObject(object);
					removeIcon();
					removeController();
					player.resetReceivedDamage();
					player.unlock();
				}
			}, 2);
			return false;
		} else if (object.getId() == 2557 || object.getId() == 65717) {
			player.getPackets().sendGameMessage("It seems it is locked, maybe you should try something else.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {
		if (object.getId() == 2557 || object.getId() == 65717) {
			if (!player.getInventory().containsItem(1523, 1)) {
				player.getPackets().sendGameMessage("You need a lockpick to open that door.");
				return false;
			}
			Thieving.pickDoor(player, object);
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	public void removeIcon() {
		if (showingSkull) {
			showingSkull = false;
			player.setCanPvp(false);
			player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 11 : 0);
			player.getAppearence().generateAppearenceData();
			player.getEquipment().refresh(null);
		}
	}

	@Override
	public boolean sendDeath() {

		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					Player killer = player.getMostDamageReceivedSourcePlayer();
					if (killer != null) {
						killer.increaseKillCount(player);
						player.sendItemsOnDeath(killer, player);
					}
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					removeIcon();
					removeController();
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void sendInterfaces() {
		if (isAtWild(player))
			showSkull();
	}

	public void showSkull() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? 11 : 0, 381);
	}

	@Override
	public void start() {
		checkBoosts(player);
	}

}