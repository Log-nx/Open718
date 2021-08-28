package com.rs.game.player.content.timers;

import com.rs.game.player.Player;
import com.rs.game.player.content.items.Potions;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class AntiFireTimer {
    
    public static void startTime(final Player p) {
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
            	if (Potions.antiFireTimer.secondsRemaining() >= 0) {
                p.getPackets().sendIComponentText(3000, 7, ""+AntiFireTimer.getTime(p)+"");
            	} else {
				p.getPackets().sendHideIComponent(3000, 2, true);
			    p.getPackets().sendIComponentText(3000, 7, "");	
				}
            }
        }, 0, 1);
    }
    
    public static String getTime(Player player) {
        if (Potions.antiFireTimer.secondsRemaining() == 0) {
        	player.getPackets().sendHideIComponent(3000, 2, true);
			player.getPackets().sendIComponentText(3000, 7, "");
        }
        return Potions.antiFireTimer.secondsRemaining()+ "s";
    }
}
