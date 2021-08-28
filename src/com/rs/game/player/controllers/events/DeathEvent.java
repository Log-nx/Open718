package com.rs.game.player.controllers.events;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.RegionBuilder;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.others.GraveStone;
import com.rs.game.player.Player;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class DeathEvent extends Controller {

	private enum Stages {
		LOADING, RUNNING, DESTROYING
	}

	public static int getCurrentHub(WorldTile tile) {
		int nearestHub = -1;
		int distance = 0;
		for (int i = 0; i < HUBS.length; i++) {
			final int d = Utils.getDistance(HUBS[i], tile);
			if (nearestHub == -1 || d < distance) {
				distance = d;
				nearestHub = i;
			}
		}
		return nearestHub;
	}

	public static final WorldTile[] HUBS = {
		// Lumbridge
		new WorldTile(3222, 3219, 0)
		// Varrock
		, new WorldTile(3212, 3422, 0)
		// EDGEVILLE
		, new WorldTile(3094, 3502, 0)
		// FALADOR
		, new WorldTile(2965, 3386, 0)
		// SEERS VILLAGE
		, new WorldTile(2725, 3491, 0)
		// ARDOUDGE
		, new WorldTile(2662, 3305, 0)
		// YANNILE
		, new WorldTile(2605, 3093, 0)
		// KELDAGRIM
		, new WorldTile(2845, 10210, 0)
		// DORGESH-KAN (Home)
		, Settings.RESPAWN_PLAYER_LOCATION
		// LYETYA
		, new WorldTile(2341, 3171, 0)
		// ETCETERIA
		, new WorldTile(2614, 3894, 0)
		// DAEMONHEIM
		, new WorldTile(3450, 3718, 0)
		// CANIFIS
		, new WorldTile(3496, 3489, 0)
		// THZAAR AREA
		, new WorldTile(4651, 5151, 0)
		// BURTHORPE
		, new WorldTile(2889, 3528, 0)
		// ALKARID
		, new WorldTile(3275, 3166, 0)
		// DRAYNOR VILLAGE
		, new WorldTile(3079, 3250, 0) };

	// 3796 - 0 - Lumbridge Castle - {1=Falador Castle, 2=Camelot, 3=Soul Wars,
	// 4=Burthorpe}
	private static final WorldTile[] RESPAWN_LOCATIONS = { Settings.RESPAWN_PLAYER_LOCATION };

	private int[] boundChuncks;
	private Stages stage;
	private Integer[][] slots;
	private int currentHub;

	@Override
	public boolean canEquip(int slotId, int itemId) {
		return false;
	}

	@Override
	public boolean canPlayerOption1(Player target) {
		return false;
	}

	private void destroyRoom() {
		if (stage != Stages.RUNNING)
			return;
		stage = Stages.DESTROYING;
		CoresManager.slowExecutor.schedule(() -> RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8), 1200, TimeUnit.MILLISECONDS);
	}

	@Override
	public void forceClose() {
		destroyRoom();
	}

	private WorldTile getDeathTile() {
		if (getArguments() == null || getArguments().length < 2)
			return Settings.RESPAWN_PLAYER_LOCATION;
		return (WorldTile) getArguments()[0];
	}

	private int getProtectSlots() {
		return player.getVarBitManager().getBitValue(9227);
	}

	private void getReadyToRespawn() {
		slots = GraveStone.getItemSlotsKeptOnDeath(player, false, hadSkull(), player.getPrayer().isProtectingItem());
		WorldTile respawnTile = Settings.RESPAWN_PLAYER_LOCATION;
		player.setHitpoints(player.getMaxHitpoints());
		player.setCloseInterfacesEvent(null);
		Magic.sendObjectTeleportSpell(player, true, respawnTile);
		if (!player.hasPaidDeath()) {
		slots = GraveStone.getItemSlotsKeptOnDeath(player, false, false, player.getPrayer().isProtectingItem());
		synchronized (slots) {
			player.sendItemsOnDeath(null, Settings.RESPAWN_PLAYER_LOCATION, Settings.RESPAWN_PLAYER_LOCATION, false, slots);
			}
		}
		player.getTemporaryAttributtes().remove(Key.TALKED_TO_REAPTER);
		FadingScreen.fade(player);
		FadingScreen.unfade(player, FadingScreen.fade(player, 2), new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	public WorldTile getRespawnHub(Player player) {
		return HUBS[getCurrentHub(player)];
	}

	private boolean hadSkull() {
		return !(getArguments() == null || getArguments().length < 2) && (boolean) getArguments()[1];
	}

	private void loadRoom() {
		stage = Stages.LOADING;
		player.lock(); // locks player
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
					RegionBuilder.copyAllPlanesMap(48, 80, boundChuncks[0], boundChuncks[1], 8, 8);
					player.reset();
					player.setNextWorldTile(new WorldTile(boundChuncks[0] * 8 + 30, boundChuncks[1] * 8 + 34, 0));
					player.unlock();
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.setNextAnimation(new Animation(-1));
							player.getMusicsManager().playMusic(683);
							player.getPackets().sendBlackOut(2);
							player.setNextFaceWorldTile(new WorldTile(boundChuncks[0] * 8 + 30, boundChuncks[1] * 8 + 35, 0));
							sendInterfaces();
							stage = Stages.RUNNING;
						}

					}, 1);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		});
	}

	@Override
	public boolean login() {
		loadRoom();
		return false;
	}

	@Override
	public boolean logout() {
		player.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
		destroyRoom();
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		destroyRoom();
		player.getPackets().sendBlackOut(0);
		player.getInterfaceManager().sendCombatStyles();
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getInterfaceManager().sendSkills();
		player.getInterfaceManager().sendInventory();
		player.getInventory().unlockInventoryOptions();
		player.getInterfaceManager().sendEquipment();
		player.getInterfaceManager().sendPrayerBook();
		player.getPrayer().unlockPrayerBookButtons();
		player.getInterfaceManager().sendMagicBook();
		player.getInterfaceManager().sendEmotes();
		player.getEmotesManager().unlockEmotesBook();
		removeController();
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 45802) {
			player.getDialogueManager().startDialogue("ReaperDialogue",
					player.hasPaidDeath() ? 2 : 1);
			return false;
		}
		if (object.getId() == 83634) {
			if (player.hasPaidDeath()) {
				player.getBank().openBank();
				return false;
			} else 
				player.getPackets().sendGameMessage("You may not access the bank unless you have paid for your items.");
			return true;
		}
		if (object.getId() == 96782) {
			if (player.getTemporaryAttributtes().get(Key.TALKED_TO_REAPTER) == null) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 8867, "Come talk to me first.");
				return false;
		}
			getReadyToRespawn();
			return false;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().closeCombatStyles();
		player.getInterfaceManager().closeSkills();
		player.getInterfaceManager().closeInventory();
		player.getInterfaceManager().closeEquipment();
		player.getInterfaceManager().closePrayerBook();
		player.getInterfaceManager().closeMagicBook();
		player.getInterfaceManager().closeEmotes();
	}

	public void sendProtectedItems() {
		for (int i = 0; i < getProtectSlots(); i++)
			player.getPackets().sendConfigByFile(9222 + i,
					i >= slots[0].length ? -1 : slots[0][i]);
	}

	@Override
	public void start() {
		loadRoom();
	}
}