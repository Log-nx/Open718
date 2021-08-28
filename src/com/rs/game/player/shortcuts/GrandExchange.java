package com.rs.game.player.shortcuts;

import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class GrandExchange {
	
	/**
	 * 
	 * @author DikKutJoch
	 */

	public static void geshortcut(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int timer;
			@Override
			public void run() {
				if (timer == 0) {
					player.lock();
					player.addWalkSteps(3139, 3516);
				}
				else if (timer == 1) {
					player.setNextAnimation(new Animation(2589));
					
				}
				else if (timer == 2) {
					player.setNextAnimation(new Animation (2590));
					player.setNextWorldTile(new WorldTile(3142, 3514, 0));
				}
				else if (timer == 3) {
					player.setNextAnimation(new Animation(2591));
					player.setNextWorldTile(new WorldTile(3143, 3514, 0));
				
				}
				
				else if (timer == 4) {
					player.addWalkSteps(3144, 3514);
				}
				else if (timer == 5) {
					player.unlock();
				}
				timer ++;
			}
			
		
	
		}, 0, 1); 
	}



public static void geshortcut2(final Player player) {
	WorldTasksManager.schedule(new WorldTask() {
		int timer;
		@Override
		public void run() {
			if (timer == 0) {
				player.lock();
				player.addWalkSteps(3143, 3516);
			}
			else if (timer == 1) {
				player.setNextAnimation(new Animation(2589));
				
			}
			else if (timer == 2) {
				player.setNextAnimation(new Animation (2590));
				player.setNextWorldTile(new WorldTile(3140, 3514, 0));
			}
			else if (timer == 3) {
				player.setNextAnimation(new Animation(2591));
				player.setNextWorldTile(new WorldTile(3139, 3516, 0));
				
			}
			else if (timer == 4) {
				player.addWalkSteps(3138, 3516);
			}
			else if (timer == 5) {
				player.unlock();
			}
			timer ++;
		}
		
	

	}, 0, 1); 
}
}

