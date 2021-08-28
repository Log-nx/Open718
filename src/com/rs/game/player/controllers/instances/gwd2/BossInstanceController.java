package com.rs.game.player.controllers.instances.gwd2;

import com.rs.game.WorldTile;
import com.rs.game.instances.BossInstance;
import com.rs.game.instances.BossInstanceHandler;
import com.rs.game.instances.BossInstanceHandler.Boss;
import com.rs.game.player.controllers.Controller;
import com.rs.game.player.managers.MusicsManager;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class BossInstanceController extends Controller {

	protected BossInstance instance;

	@Override
	public void start() {

		instance = (BossInstance) getArguments()[0]; // cant save this as not
		// serializable would null
		// :L
		getArguments()[0] = instance.getBoss(); // gotta save reference for
		// which instance im at
	}

	@Override
	public void process() {

		if (instance != null && Utils.currentWorldCycle() % 120 == 0)
			instance.playMusic(player);
	}

	@Override
	public boolean login() {
		// shouldnt happen but better be safe
		if (getArguments() == null || getArguments().length == 0 || getArguments()[0] == null)
			return true;
		if (getArguments()[0] instanceof String) {
			getArguments()[0] = BossInstanceHandler.Boss.getBossByName((String) getArguments()[0]);
		}
		if (getArguments() == null || getArguments().length == 0 || getArguments()[0] == null)
			return true;
		Boss boss = (Boss) getArguments()[0];
		instance = BossInstanceHandler.joinInstance(player, boss, "", true);
		return instance == null; // if failed. remove
	}

	public BossInstance getInstance() {
		return instance;
	}

	@Override
	public boolean sendDeath() {
		player.lock(8);
		player.stopAll();
		player.getTemporaryAttributtes().put("dieing", Boolean.TRUE);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(player.getDeathAnimation());
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					instance.leaveInstance(player, BossInstance.DIED);
					removeController();
					if (instance.getSettings().isPracticeMode()) {
						player.reset();
						player.setNextWorldTile(instance.getBoss().getOutsideTile());
					} else {
						WorldTile graveStoneLoc = instance.getBoss().getGraveStoneTile();
						if (graveStoneLoc == null)
							graveStoneLoc = instance.isPublic() ? new WorldTile(player) : instance.getBoss().getOutsideTile();
							player.getControllerManager().startController("DeathEvent", graveStoneLoc, player.hasSkull());
					}
				} else if (loop == 4) {
					player.getMusicsManager().playMusic(MusicsManager.DEATH_MUSIC_EFFECT);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean logout() {
		if (instance == null) {
			removeController();
			return true;
		}
		instance.leaveInstance(player, BossInstance.LOGGED_OUT);
		if (!instance.isPublic())
			removeController();
		return false;
	}

	@Override
	public void forceClose() {
		if (instance != null)
			instance.leaveInstance(player, BossInstance.EXITED);
	}

	@Override
	public void magicTeleported(int type) {

		instance.leaveInstance(player, BossInstance.TELEPORTED);
		removeController();
	}
}