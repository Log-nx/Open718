package com.rs.game.player.controllers;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class MercArena extends Controller {

	public static Object lock = new Object();
	
	@Override
	public void start() {
		player.sm("Good luck, you've been teleported to the lair of the Mercenary Mage!");
		player.setNextWorldTile(new WorldTile(3210, 5478, 0));
	}
	
	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		removeController();
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		removeController();
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		removeController();
		return true;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		int objectId = object.getId();
		
		if (objectId == 77745) {
			Magic.sendNormalTeleportSpell(player, 1, 0, Settings.START_PLAYER_LOCATION);
			removeController();
			return true;
		}
		
		return true;
	}
	
	@Override
	public boolean logout() {
		removeController();
		player.setLocation(new WorldTile(Settings.START_PLAYER_LOCATION));
		return false;
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
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					removeController();
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}
	
}
