package com.rs.game.player.content.activities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class XPWell {

	private static final int WELL_GOAL = 15000000;
	private static int wellAmount;

	public static void give(Player player) {
		if (isWellActive()) {
			player.getPackets().sendGameMessage("The XP well is already active! Go train!");
			return;
		}
		player.getPackets().sendInputIntegerScript("Progress: " + Utils.formatNumber(wellAmount) + " GP (" + ((wellAmount * 100) / WELL_GOAL) + "% of Goal); Goal: " + Utils.formatNumber(WELL_GOAL) + " GP");
		player.getTemporaryAttributtes().put("WellOfExperience", Boolean.TRUE);
	}

	public static int getWellAmount() {
		return wellAmount;
	}

	public static int getWellGoal() {
		return WELL_GOAL;
	}

	public static void addWellAmount(Player player, int amount) {
		if (player.getInventory().getCoinsAmount() < amount) {
			player.getPackets().sendGameMessage("You do not have enough coins to donate this amount.");
			return;
		} else if (amount > WELL_GOAL - wellAmount)
			amount = WELL_GOAL - wellAmount;
		player.getInventory().removeItemMoneyPouch(ItemIdentifiers.COINS,
				amount);
		wellAmount += amount;
		World.sendWorldMessage("<col=FF0000>" + player.getDisplayName() + " has contributed " + NumberFormat.getNumberInstance(Locale.US).format(amount) + " GP to the XP well! Progress now: " + ((wellAmount * 100) / WELL_GOAL) + "%!", false, false);
		World.sendWorldMessage("<col=FF0000> The XP well is currently active! The XP modifier is currently " + (World.isWeekend()? "3.5x." : "1.5x."), false, false);
		try {
			DateFormat dateFormat2 = new SimpleDateFormat(
					"MM/dd/yy HH:mm:ss");
			Calendar cal2 = Calendar.getInstance();
			final String FILE_PATH = Settings.LOGS_PATH+ "/well/";
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername() + ".txt", true));
			writer.write("[" + dateFormat2.format(cal2.getTime()) + ", IP: " + player.getSession().getIP() + ", contributions to well: " + NumberFormat.getNumberInstance(Locale.US).format(amount));
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException er) {
			er.printStackTrace();
		}
	}

	public static boolean isWellActive() {
		return wellAmount >= WELL_GOAL;
	}

	public static void addWellTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				wellAmount = 0;
				World.sendWorldMessage("<col=FF0000>The XP well has been reset!", false, false);
			}
		}, 2, 9, TimeUnit.HOURS);
	}
}