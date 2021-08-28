package com.rs.game.minigames.hunger;


import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;


public class HungerGamesControler extends Controller {
	private boolean forfeit = false;

	@Override
	public void start() {
		player.getBank().depositAllBob(false);
		player.getBank().depositAllEquipment(false);
		player.getBank().depositAllInventory(false);
		HungerGames.addLobbyPlayer(player);
		player.sm(HungerGames.getLobbyPlayers().size() == 1 ? "There is currently <col=ff0000>1</col> player in the lobby."
				: "There are currently <col=ff0000>"
						+ HungerGames.getLobbyPlayers().size()
						+ "</col> players in the lobby.");
		//player.sm("Check ;;hgsize to check the size of the of the lobby again.");
		HungerGames.setStartTime(Utils.currentTimeMillis());
	}

	@Override
	public void process() {
		HungerGames.processCage();
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			if (forfeit == false && HungerGames.isGameStarted() && HungerGames.getGamePlayers().contains(player)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"If you log out now, you'll forfeit the game!");
                                forfeit = true;
			} else {
				HungerGames.getGamePlayers().remove(player);
				player.getEquipment().reset();
				player.setCanPvp(false);
				player.getAppearence().generateAppearenceData();
				player.getEquipment().init();
				player.setNextWorldTile(HungerGames.LOBBY);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.forceLogout();
					}
				}, 3);
			}
			return false;
		}
		if (interfaceId == 192 || interfaceId == 193 || interfaceId == 430) {
			player.sm("Magic has been disabled during this game!");
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave the game!");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave the game!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave the game!");
		return false;
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		switch(npc.getId()) {
		case 3812:
			player.getDialogueManager().startDialogue("HungerLeaving", player);
			return true;
			default:
				player.getPackets().sendGameMessage("Hasn't been added..");
				return true;
		}
	}
	
	@Override
	public boolean canAttack(Entity target) {
		Integer tempCastSpell = (Integer) player.getTemporaryAttributtes().get("tempCastSpell");
		if (tempCastSpell != null) {
			player.sm("You're trying to auto cast in the game, unfortunate magic has been disabled.");
			player.getCombatDefinitions().setAutoCastSpell(0);
			player.getCombatDefinitions().refreshAutoCastSpell();
			return false;
		}
		return true;
	}

	@Override
	public boolean login() {
		if(HungerGames.getLobbyPlayers().contains(player)) {
			HungerGames.getGamePlayers().remove(player);
			player.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
			return true;
		}
		player.getEquipment().reset();
		player.setCanPvp(false);
		player.getAppearence().generateAppearenceData();
		player.getEquipment().init();
		player.setNextWorldTile(HungerGames.LOBBY);
		return true;
	}

	@Override
	public boolean logout() {
		if(HungerGames.getLobbyPlayers().contains(player)) {
			HungerGames.getGamePlayers().remove(player);
			player.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
			return true;
		}
		HungerGames.getGamePlayers().remove(player);
		player.getEquipment().reset();
		player.getEquipment().init();
		player.setNextWorldTile(HungerGames.LOBBY);
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("You've lost!");
				} else if (loop == 3) {
					player.reset();
					player.getEquipment().reset();
					player.getAppearence().generateAppearenceData();
					HungerGames.getGamePlayers().remove(player);																
					player.setNextWorldTile(HungerGames.LOBBY);
					player.getControllerManager().startController("HungerGames"); //TODO
					player.setCanPvp(false);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

}
