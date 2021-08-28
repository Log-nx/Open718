package com.rs.game.player.controllers;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class VoragoController extends Controller {
	@Override
	public void start() {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3374, 3513, 0));
		player.sm("Beware, as soon as you enter the gate you are in danger!");
		player.sm("Don't worry, you won't lose any of your items.");
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
	public boolean logout() {
		removeController();
		player.setLocation(new WorldTile(Settings.START_PLAYER_LOCATION));
		return false;
	}
	@Override
	public boolean sendDeath() {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					removeController();
				}
			}, 6);
			return true;
		}
	
}
	
/*	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("You are removed from the dungeon.");
				} else if (loop == 3) {
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setNextWorldTile(Main.START_PLAYER_LOCATION);
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
*/

