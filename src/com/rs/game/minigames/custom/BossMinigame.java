package com.rs.game.minigames.custom;

import java.util.ArrayList;
import java.util.List;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

public class BossMinigame {
	
	private static List<Player> team = new ArrayList<Player>();
	
	public static void addPlayer(Player player) {
		if (team.size() == 1) {
			player.setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
			player.sm("You've been teleported to the home area");
		} else {
			team.add(player);
			player.getPackets().sendGameMessage("There are currently " + team.size() + " players in this team.");
			player.setNextWorldTile(new WorldTile(2972, 9696, 0));
		}
	}
	
	public static void removePlayer(Player player) {
		team.remove(player);
		player.getPackets().sendGameMessage("You have left the team.");
		player.setNextWorldTile(new WorldTile(2969, 9696, 0));
	}
	
	public static void teleportTeam(Player player) {
		for (Player p : team) 
			p.setNextWorldTile(new WorldTile(3049, 2979, 0));
	}
}