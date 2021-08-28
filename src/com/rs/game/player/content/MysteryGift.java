package com.rs.game.player.content;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

 public class MysteryGift {
	
	public static void OpenGift(final Player player, final Item item) {
		WorldTasksManager.schedule(new WorldTask() {
			int tijd;

			public void run() {
				if (tijd == 0 && player.getInventory().containsItem(6199, 1)) {
					player.sm("You open the box.");				
					player.getInventory().deleteItem(item.getId(),1 );
					player.lock(3);
				}
				else if (tijd == 1) {
	
					player.getInventory().addItem(995, Utils.random(250000, 3000000));
					player.sm("And loot the reward.");
				}tijd++;
			}
		},0, 1);
	}

}
