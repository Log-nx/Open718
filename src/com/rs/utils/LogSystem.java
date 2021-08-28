package com.rs.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

	public class LogSystem {
		
		public static void dropLog(Player player, int i, int amount) {
			final String FILE_PATH = "data/logs/dropLogs/player/playerDrops/";
			DecimalFormat format = new DecimalFormat("#,###,###,###");
			try {
				if (player == null)
					return;
				BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername()+".txt", true));
				writer.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " dropped " + format.format(amount) + " " + ItemDefinitions.getItemDefinitions(i).getName() + " (" + i + ") at "+ player.getX() + ", " + player.getY() + ", " + player.getPlane());
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (final IOException e) {
				System.out.println("Error logging item drop by " +player.getUsername() + ".");
			}
		}
		
		public static void playerPickup(Player player, int i, int amount) {
			final String FILE_PATH = "data/logs/dropLogs/player/playerPickups/";
			DecimalFormat format = new DecimalFormat("#,###,###,###");
			try {
				if (player == null)
					return;
				BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername()+".txt", true));
				writer.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + "] " + "Player: " + player.getUsername()
				+ " IP: " + player.getLastIP()
				+ " - Picked up: " + i
				+ " - Amount: " + amount);
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (final IOException e) {
				System.out.println("Error logging item pickup by " +player.getUsername() + ".");
			}
		}

		public static void deathLogs(Player player, String ip, String name, Object o) {
			try {
				final String FILE_PATH = "data/logs/deaths/";
				BufferedWriter bf = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername()+ ".txt", true));
				bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + "] " + name + " - " + ip + " [ " + o + " ]");
				bf.newLine();
				bf.flush();
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static void npcDropLog(Player player, int i, int a, String name, String npc, int n) {
			try {
			final String FILE_PATH = "data/logs/dropLogs/npc/npcDrops/";
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername()+".txt", true));
			writer.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " has received " + a + " of the item " + name + "(" + i + ") from the NPC " + npc + "(" + n + ").");
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException er) {
			System.out.println("Error logging suggestion by " +player.getUsername() + ".");
		}
			}
		
		public static void npcDeathLog(Player player, String npc, int n) {
			try {
				final String FILE_PATH = "data/logs/dropLogs/npc/npcDeaths/";
				BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername()+".txt", true));
				writer.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " killed NPC " + npc + " (" + n + ") at "+ player.getX() + ", " + player.getY() + ", " + player.getPlane());
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (IOException e) {
			}
		}

		public static void playerCountLog(int amount) {
			try {
				final BufferedWriter bf = new BufferedWriter(new FileWriter(Settings.LOG_PATH + "playercount.txt", true));
				bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + " " + Calendar.getInstance().getTimeZone().getDisplayName() + "] " + Settings.SERVER_NAME + " currently has " + amount + " players online.");
				bf.newLine();
				bf.flush();
				bf.close();
			} catch (final IOException ignored) {
			}
		}

		public static void shopLog(Player player, int i, int a, boolean selling) {
			try {
				final BufferedWriter bf = new BufferedWriter(new FileWriter(Settings.LOG_PATH + "items/shop/" + player.getUsername() + ".txt", true));
				if (selling) {
					bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + " " + Calendar.getInstance().getTimeZone().getDisplayName() + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " sold " + a + " of " + i + "(" + ItemDefinitions.getItemDefinitions(i).getName() + ")" + ".");
				} else {
					bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + " " + Calendar.getInstance().getTimeZone().getDisplayName() + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " bought " + a + " of " + i + "(" + ItemDefinitions.getItemDefinitions(i).getName() + ")" + ".");
				}
				bf.newLine();
				bf.flush();
				bf.close();
			} catch (final IOException ignored) {
			}
		}

		public static void tradeLog(String ip, String name, Object o) {
			try {

				final BufferedWriter bf = new BufferedWriter(new FileWriter(Settings.LOG_PATH + "items/trades.txt", true));
				bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + " " + Calendar.getInstance().getTimeZone().getDisplayName() + "] " + name + " - " + ip + " [ " + o + " ]");
				bf.newLine();
				bf.flush();
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void deathLog(Player player, int i, int a, String name) {
			try {
				final BufferedWriter bf = new BufferedWriter(new FileWriter(Settings.LOG_PATH + "items/deaths.txt", true));
				bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + " " + Calendar.getInstance().getTimeZone().getDisplayName() + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " has lost " + a + " of the item " + name + "(" + i + ") when killed.");
				bf.newLine();
				bf.flush();
				bf.close();
			} catch (final IOException ignored) {
		}
	}
}