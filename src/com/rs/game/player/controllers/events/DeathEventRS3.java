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
import com.rs.game.player.CoordsEvent;
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

public class DeathEventRS3 extends Controller {

	private enum Stages {
		LOADING, RUNNING, DESTROYING
	}

	private int[] boundChuncks;
	private Stages stage;

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

	private void getReadyToRespawn() {
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
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(48, 80, boundChuncks[0], boundChuncks[1], 8, 8);
		player.setNextWorldTile(new WorldTile(boundChuncks[0] * 8 + 30, boundChuncks[1] * 8 + 34, 0));
		return false;
	}

	@Override
	public boolean logout() {
		destroyRoom();
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		destroyRoom();
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
	public void start() {
		loadRoom();
	}
}