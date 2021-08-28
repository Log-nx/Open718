package com.rs.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.rs.Settings;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.ServerChannelHandler;

public final class Logger {

	public static void handle(Throwable throwable) {
		System.out.println("ERROR! THREAD NAME: "
				+ Thread.currentThread().getName());
		throwable.printStackTrace();
	}

	public static void debug(long processTime) {
		log(Logger.class, "---DEBUG--- start");
		log(Logger.class, "WorldProcessTime: " + processTime);
		log(Logger.class,
				"WorldRunningTasks: " + WorldTasksManager.getTasksCount());
		log(Logger.class,
				"ConnectedChannels: "
						+ ServerChannelHandler.getConnectedChannelsSize());
		log(Logger.class, "---DEBUG--- end");
	}

	public static void log(Object classInstance, Object message) {
		log(classInstance.getClass().getSimpleName(), message);
	}

	public static void log(String className, Object message) {
		String text = "[" + className + "]" + " " + message.toString();
		System.out.println(text);
	}

	private Logger() {

	}
	
    public static void log(String message) {
    	System.out.println("[" + date() + "] -> " + message);
    }
    
    public static String date() {
		try {
		    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		    Date now = new Date();
		    String parsed = format.format(now);
		    return parsed;
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return "";
    }

	/**
	 * Logs when the player claims their G.E. items.
	 */
	public static void logGrandExchange(Player player, Item item) {
		if (Settings.DEBUG)
			return;
		String FILE_PATH = Settings.LOGS_PATH + "/grandexchangelogs/";
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername() + ".txt", true));
			writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: " + player.getSession().getIP()
					+ "] : collected an item: " + item.getAmount() + " " + "x " + item.getName() + ".");
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			Logger.log(player, e);
		}
	}

}
