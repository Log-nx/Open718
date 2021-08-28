package com.rs.game.player.actions.crafting;

import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

/**
 * Handles the Robust Glass Machine object.
 * @author BigFuckinChungus
 */
public class RobustGlassCrafting extends Action {

	private boolean checkAll(Player player) {
		if (!player.getInventory().containsItem(23194, 1) && !player.getInventory().containsItem(32847, 1))
			return false;
		return true;
	}

	public static boolean addSandstone(Player player) {
		player.getActionManager().setAction(new RobustGlassCrafting());
		return false;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player))
			return true;
		return false;

	}

	@Override
	public boolean process(Player player) {
		if (checkAll(player))
			return true;
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		WorldObject object = new WorldObject(94067, 10, 0, 2139, 3350, 1);
		WorldObject object2 = new WorldObject(67968, 10, 0, 2584, 2854, 0);
		if (player.getInventory().containsItem(23194, 1)) {
			player.getInventory().deleteItem(23194, 1);
			player.getInventory().addItem(23193, 1);
			player.getPackets().sendGameMessage("You add the red sandstone to the machine and make robust glass.", true);
			player.setNextAnimation(new Animation(25120));
			if (player.getX() == 2582 && player.getY() == 2852)
				World.sendObjectAnimation(object2, new Animation(25041));
			else
				World.sendObjectAnimation(object, new Animation(25041));
			return 1;
		} else if (player.getInventory().containsItem(32847, 1)) {
			player.getInventory().deleteItem(32847, 1);
			player.getInventory().addItem(32845, 1);
			player.getPackets().sendGameMessage("You add the crystal-flecked sandstone to the machine and make crystal glass.", true);
			player.setNextAnimation(new Animation(25120));
			if (player.getX() == 2582 && player.getY() == 2852)
				World.sendObjectAnimation(object2, new Animation(25041));
			else
				World.sendObjectAnimation(object, new Animation(25041));
			return 1;
		}
		return -1;
	}

	@Override
	public void stop(final Player player) {
		player.getEmotesManager().setNextEmoteEnd(-1);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextAnimation(new Animation(-1));
				player.getAppearence().setRenderEmote(-1);
				player.getPackets().sendGameMessage("You've ran out of sandstone.", true);
				this.stop();
			}
		}, 1);
	}
}