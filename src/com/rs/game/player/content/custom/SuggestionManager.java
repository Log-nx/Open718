package com.rs.game.player.content.custom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class SuggestionManager {
	
	
	//public static long lastSuggestionTime;
	
	private static final int MAXIMUM_SUGGESTIONS = 2;
	
	private static int suggestionNumber = 0;
	
	private static List<String> mutedPlayerNames = new ArrayList<>();
	private static List<String> playerNames = new ArrayList<>();
	private static List<String> suggestions = new ArrayList<>();
	private static String currentSuggestion, currentName;
	private static int agree, disagree;

	public static void banFromSuggestions(String username) {
		mutedPlayerNames.add(username);
	}
	
	public static boolean bannedFromSuggestions(String username) {
		return mutedPlayerNames.contains(username);
	}
	
	public static boolean canAddSuggestion(String playerName) {
		int numberOfSuggestions = 0;
		for(String name : playerNames) {
			if (name.equals(playerName)) {
				numberOfSuggestions++;
			}
		}
		return numberOfSuggestions <= MAXIMUM_SUGGESTIONS;
	}
	
	public static void addSuggestion(String playerName, String suggestion) {
		playerNames.add(playerName);
		suggestions.add(suggestion);
	}
	
	public static void addAgree(Player player) {
		if (currentSuggestion == null) {
			player.sm("You can't vote yet.");
			return;
		}
		Integer sN = (Integer) player.getTemporaryAttributtes().get("suggestionNumber");
		if (sN != null && sN == suggestionNumber) {
			player.sm("You've already voted on this suggestion.");
			return;
		}
		player.sm("You've agreed with this suggestion.");
		player.getTemporaryAttributtes().put("suggestionNumber", suggestionNumber);
		agree += 1;
	}
	
	public static void addDisagree(Player player) {
		if (currentSuggestion == null) {
			player.sm("You can't vote yet.");
			return;
		}
		Integer sN = (Integer) player.getTemporaryAttributtes().get("suggestionNumber");
		if (sN != null && sN == suggestionNumber) {
			player.sm("You've already voted on this suggestion.");
			return;
		}
		player.sm("You've disagreed with this suggestion.");
		player.getTemporaryAttributtes().put("suggestionNumber", suggestionNumber);
		disagree += 1;
	}
	
	private static void sendResults() {
		Player player = World.getPlayerByDisplayName(currentName);
		if (player == null)
			return;
		player.sm("<col=FFAD5C><shad=FF6600>[Suggestions] Your suggestion had "+agree+" agrees and "+disagree+" disagrees.");
		sendStaffMessage("<col=FFAD5C><shad=FF6600>[Suggestions] This suggestion had "+agree+" agrees and "+disagree+" disagrees.");
	}
	
	private static void resetVotes() {
		agree = 0;
		disagree = 0;
	}
	
	public static void process() {
		if (currentSuggestion != null) {
			sendResults();
			save();
		}
		resetVotes();
		if (suggestions.isEmpty()) {
			currentSuggestion = null;
			currentName = null;
		} else {
			String suggestion = suggestions.remove(0);
			currentSuggestion = suggestion;
			String name = playerNames.remove(0);
			currentName = name;
			sendWorldMessage("<col=FFAD5C><shad=FF6600>[Suggestions] "+Utils.formatPlayerNameForDisplay(name)+" submitted the suggestion: '"+suggestion+"'", 
							"<col=FFAD5C><shad=FF6600>[Suggestions] Somebody submitted the suggestion: '"+suggestion+"'");
			World.sendWorldMessage("<col=FFAD5C><shad=FF6600>[Suggestions] Use ::agree to agree and ::disagree to disagree.", false, false);
		}
		suggestionNumber++;
	}
	
	public static void sendWorldMessage(String staffMessage, String playerMessage) {
		for (Player p : World.getPlayers()) {
			if (p == null || !p.isRunning() || p.isYellOff())
				continue;
			boolean staff = (p.isSupporter() || p.getRights() >= 1);
			p.sm(staff ? staffMessage : playerMessage);
		}
	}
	
	private static void sendStaffMessage(String staffMessage) {
		for (Player p : World.getPlayers()) {
			if (p == null || !p.isRunning() || p.isYellOff())
				continue;
			boolean staff = (p.isSupporter() || p.getRights() >= 1);
			if (!staff)
				continue;
			p.sm(staffMessage);
		}
	}
	
	public static String getLine() {
		return agree + " - " + disagree + " - " + currentSuggestion + " - " + currentName;
	}
	
	public static void save() {
		try {
			File log = new File("data/suggestions.txt");
			if (!log.exists()) {
				log.createNewFile();
			}
			BufferedWriter bf = new BufferedWriter(new FileWriter("data/suggestions.txt", true));
			bf.newLine();
			bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + "] " + getLine());
			bf.flush();
			bf.close();
		} catch (IOException ignored) {
		}
	}
}