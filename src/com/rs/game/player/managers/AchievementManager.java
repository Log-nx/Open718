package com.rs.game.player.managers;

import java.io.Serializable;
import java.util.Vector;

import org.javacord.api.entity.message.MessageBuilder;

import com.rs.GameServer;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.content.achievements.Achievements;

public class AchievementManager implements Serializable {

	private static final long serialVersionUID = 2995657158069467669L;
	private Vector<Achievements> achievements = new Vector<>();
	private Player player;
	
	public boolean hasAchievement(Achievements achievement) {
		return achievements.contains(achievement);
	}
	
	public boolean unlockAchievement(Achievements achievement) {
		if (achievements.contains(achievement)) {
			return false;
		}
		achievements.add(achievement);
		World.sendNews(player, "[Achievements] " + player.getDisplayName() + " has just successfully completed the " + achievement.toString().toLowerCase().replace("_", " ") + " achievement!", 0);
		new MessageBuilder().append("[Achievements] "+ player.getDisplayName() + " has just successfully completed the " + achievement.toString().toLowerCase().replace("_", " ") + " achievement!").send(GameServer.getDiscordBot().getAPI().getTextChannelById("534549522894815236").get());
		return true;
	}
	
	public boolean lockAchievement(Achievements achievement) {
		if(!achievements.contains(achievement)) {
			return false;
		}
		achievements.remove(achievement);
		return true;
	}
	
	public Vector<Achievements> getAchievements() {
		return achievements;
	}
	
	public void setAchievements(Vector<Achievements> achievement) {
		this.achievements = achievement;
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
	public Player setPlayer(Player player) {
		return this.player = player;
	}

}
