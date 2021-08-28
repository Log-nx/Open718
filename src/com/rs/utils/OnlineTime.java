package com.rs.utils;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import com.rs.game.player.Player;

public final class OnlineTime implements Serializable {

	private static final long serialVersionUID = 5403480618483552509L;

	private String username;
	private int minutes;

	private static OnlineTime[] ranks;

	private static final String PATH = "data/onlineTime.ser";

	public OnlineTime(Player player) {
		this.username = player.getUsername();
		this.minutes = player.getStatistics().getOnlineTime();
	}

	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				ranks = (OnlineTime[]) SerializableFilesManager.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		ranks = new OnlineTime[300];
	}

	public static final void save() {
		try {
			SerializableFilesManager.storeSerializableClass(ranks, new File(PATH));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static void showRanks(Player player) {
		for (int i = 10; i < 310; i++)
			player.getPackets().sendIComponentText(275, i, "");
		for (int i = 0; i < 300; i++) {
			if (ranks[i] == null)
				break;
			String text;
			if (i >= 0 && i <= 2)
				text = "<shad=FFFF00><col=FF0000>";
			else if (i <= 9)
				text = "<shad=FFFF00><col=1500FF>";
			else if (i <= 49)
				text = "<shad=FFFF00><col=00610B>";
			else
				text = "<col=09FF00>";
			player.getPackets().sendIComponentText(275, i + 10, text + "Top " + (i + 1) + " - " + Utils.formatPlayerNameForDisplay(ranks[i].username) + " - Online time: " + ranks[i].minutes + "");
		}
		player.getPackets().sendIComponentText(275, 1, "Players online time!");
		player.getInterfaceManager().sendInterface(275);
	}

	public static void sort() {
		Arrays.sort(ranks, new Comparator<OnlineTime>() {
			@Override
			public int compare(OnlineTime arg0, OnlineTime arg1) {
				if (arg0 == null)
					return 1;
				if (arg1 == null)
					return -1;
				if (arg0.minutes < arg1.minutes)
					return 1;
				else if (arg0.minutes > arg1.minutes)
					return -1;
				else
					return 0;
			}

		});
	}

	public static void checkRank(Player player) {
		int kills = player.getKillCount();
		for (int i = 0; i < ranks.length; i++) {
			OnlineTime rank = ranks[i];
			if (rank == null)
				break;
			if (rank.username.equalsIgnoreCase(player.getUsername())) {
				ranks[i] = new OnlineTime(player);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks.length; i++) {
			OnlineTime rank = ranks[i];
			if (rank == null) {
				ranks[i] = new OnlineTime(player);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks.length; i++) {
			if (ranks[i].minutes < kills) {
				ranks[i] = new OnlineTime(player);
				sort();
				return;
			}
		}
	}

}
