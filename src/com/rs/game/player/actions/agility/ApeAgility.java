package com.rs.game.player.actions.agility;

import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class ApeAgility {

	
	public static void jumpStone(final Player player, WorldObject object) {
		if (player.getX() == 2755 && player.getY() == 2742) {
		WorldTasksManager.schedule(new WorldTask() {
			int timer;
			public void run() {	
			if (timer == 0) {
				player.setNextWorldTile(new WorldTile(2754, 2742, 0));
				player.sm("You jump on the stone");
			}
			else if (timer == 1) {
				player.setNextWorldTile(new WorldTile(2753, 2742, 0));
				player.sm("You made it to ther otherside of the river.");

		
			}timer++;
			}
			},0,1);
		}
		
	}
	
	
	public static void climbTree(final Player player, WorldObject object) {
			player.setNextWorldTile(new WorldTile(2752, 2741, 2));
			player.sm("You climb up the tree.");
		}
	
	
	public static void swingLadder(final Player player, WorldObject object) {
	}
}
