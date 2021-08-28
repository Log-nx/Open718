package com.rs.game.player.content.creation;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.Color;

public class FuryAmulet {
	
	public static boolean createAmulet(Player player, Item itemUsed, Item usedWith) {
		if (itemUsed.getId() == 1759 || usedWith.getId() == 6573) {
            if (player.getInventory().containsItem(1759, 1)   && player.getInventory().containsItem(6573, 1)) {
                    player.getInventory().deleteItem(1759, 1);
                    player.getInventory().deleteItem(6573, 1);
                    player.getInventory().addItem(6585, 1);
					player.getPackets().sendGameMessage(Color.GREEN, "You have successfully made an amulet of Fury.");
            }
        }
		if (itemUsed.getId() == 6573 || usedWith.getId() == 1759) {
            if (player.getInventory().containsItem(6573, 1)  && player.getInventory().containsItem(1759, 1)) {
                    player.getInventory().deleteItem(1759, 1);
                    player.getInventory().deleteItem(6573, 1);
                    player.getInventory().addItem(6585, 1);
                    player.getPackets().sendGameMessage(Color.GREEN, "You have successfully made an amulet of Fury.");
            }
        }
		return false;
	}

}
