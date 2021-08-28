package com.rs.game.player;

import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.World;

public class PlayTime {
	 
	private transient Player player;
	
	public PlayTime(Player player) {
		this.player = player;
	}
	public void startTimer() {
		
	CoresManager.fastExecutor.schedule(new TimerTask() {
			int timer = 10;
			@Override
			public void run() {
				if (timer == 1) {
					player.getStatistics().setPlayPoints(player.getStatistics().getPlayPoints() + 1);
					player.getPackets().sendIComponentText(506, 0, "Playtime: " + player.getStatistics().getPlayPoints() + "m");
					timer = 60;
				}
				if (player.getStatistics().getPlayPoints() == 18000) {
					player.getBank().addItem(20763,1, true);
					player.getBank().addItem(20764,1, true);
					World.sendWorldMessage("<col=ff0000>" + player.getUsername() + "has been awarded the veteran cape for having a playtime of 300 hours.", false, false);
				}
				if (timer > 0) {
					timer--;
				}
			}
		}, 0L, 1000L);
	}
}