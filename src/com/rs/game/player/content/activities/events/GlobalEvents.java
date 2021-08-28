package com.rs.game.player.content.activities.events;

import java.util.Random;

import org.javacord.api.entity.message.MessageBuilder;

import com.rs.GameServer;
import com.rs.Settings;
import com.rs.game.World;
import com.rs.utils.Color;

public class GlobalEvents {

	private static Event currentEvent;
	private static boolean modEvent;
	public static String mod = "";
	public static boolean started;

	public enum Event {
		BANDOS_KC("There is currently no kill count required to enter bandos!"),
		ARMADYL_KC("There is currently no kill count required to enter armadyl!"),
		ZAMORAK_KC("There is currently no kill count required to enter zamorak!"),
		SARADOMIN_KC("There is currently no kill count required to enter saradomin!"),
		CANNONBALL("Extra cannonballs can be made while smelting in a furnace!"),
		FASTER_SKILLING("Reduces the time to collect items from skilling!"), DOUBLE_DROPS("");

		Event(String description) {
			this.description = description;
		}

		private String description;

		public String getDescription() {
			return this.description;
		}

	}

	public static void generateRandomEvent() {
		currentEvent = Event.values()[new Random().nextInt(Event.values().length)];
		World.sendWorldMessage("<img=5>" + Color.ORANGE + "[Events] " + currentEvent.getDescription(), false, false);
		if (Settings.DISCORD)
			new MessageBuilder().append("[Events] " + currentEvent.getDescription()).send(GameServer.getDiscordBot().getAPI().getTextChannelById("534549522894815236").get());
	}

	public static Event getEvent() {
		return currentEvent;
	}

	public static boolean isActiveEvent(Event event) {
		return currentEvent == event;
	}

	public static boolean getModEvent() {
		return modEvent;
	}

	public static boolean setModEvent(boolean event) {
		return modEvent = event;
	}

}