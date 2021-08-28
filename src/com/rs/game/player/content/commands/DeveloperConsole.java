package com.rs.game.player.content.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.JailControler;
import com.rs.utils.Utils;

public final class DeveloperConsole {

	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) throws NumberFormatException, Exception {
		if (command.length() == 0)
			return false;
		if (player.getControllerManager().getController() instanceof JailControler && player.getRights() < 1) {
			return false;
		}
		String[] cmd = command.split(" ");
		if (cmd.length == 0)
			return false;
		archiveLogs(player, cmd);
		if (AdministratorCommands.processCommand(player, cmd, console, clientCommand))
			return true;
		else if (ModeratorCommands.processCommand(player, cmd, console, clientCommand))
			return true;
		else if (PlayerCommands.processCommand(player, cmd, console, clientCommand))
			return true;
		return false;

	}
	
	public static void archiveLogs(Player player, String[] cmd) {
		try {
			String location = "";
			if (player.getRights() > 1) {
				location = "data/logs/commands/staff/" + player.getUsername() + ".txt";
			} else
				location = "data/logs/commands/" + player.getUsername() + ".txt";
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++)
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
			writer.write("[" + currentTime("dd MMMMM yyyy 'at' hh:mm:ss ") + "] - ::" + cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String currentTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
	

	public static void sendYell(Player player, String message, boolean staffYell) {
		if (player.getMuted() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
			return;
		}
		if (player.yellBanned != 0) {
			player.sm("You are currently banned from the yell channel.");
			return;
		}
		if (staffYell) {
			World.sendWorldMessage("[<col=ff0000>Staff Yell</col>] "
					+(player.getRights() > 1 ? "<img=1>" : (player.isSupporter() ? "": "<img=0>"))
					+ player.getDisplayName()+": <col=ff0000>"
					+message+".</col>", true, true);
			return;
		}
		if (message.length() > 100)
			message = message.substring(0, 100);
		if (message.toLowerCase().equals("nigger") && player.getRights() == 0) {
			player.getPackets().sendGameMessage("Shutup");
			return;
		}
		if (player.getRights() < 2) {
			String[] invalid = { "<euro", "<col", "<col=", "<shad", "<shad=", "<str>", "<u>" };
			for (String s : invalid)
				if (message.contains(s)) {
					player.getPackets().sendGameMessage("You cannot add additional code to the message.");
					return;
				}
			if (player.isGraphicDesigner())
				World.sendWorldMessage("<col=00ACE6>{"+player.prestigeLevel+"} Graphic Designer</shad></col> <img=14>" + player.getDisplayName() + ": <col=00ACE6><shad=000000>" + message + "", false, true);
			if (player.isCommunityManager())
				World.sendWorldMessage("<col=dfd500>{"+player.prestigeLevel+"} Community Manager</shad></col> <img=1>" + player.getDisplayName() + ": <col=dfd500>" + message + "", false, true);
			else if (player.isForumModerator())
				World.sendWorldMessage("<img=10><col=33CC00>{"+player.prestigeLevel+"} Forum Moderator</col> <img=10>" + player.getDisplayName() + ": <col=33CC00><shad=000000>" + message + "", false, true);				
			else if (player.isSupporter() && player.getRights() == 0)
				World.sendWorldMessage("<col=58ACFA><shad=2E2EFE>{"+player.prestigeLevel+"} Support Team</shad></col> "+player.getDisplayName()+": <col=58ACFA><shad=2E2EFE>"+message+"</shad></col>.", false, true);
			else if (player.isIronman() && player.getRights() == 0)
				if (player.getAppearence().isMale())
				World.sendWorldMessage("<col=a29e9e>{"+player.prestigeLevel+"} Ironman</col> <img=26>"+player.getDisplayName()+": <col=a29e9e>"+message+"</col>.", false, true);
				if (!player.getAppearence().isMale())
					World.sendWorldMessage("[<col=a29e9e>{"+player.prestigeLevel+"} Ironwoman</col> <img=26>"+player.getDisplayName()+": <col=a29e9e>"+message+"</col>.", false, true);
			else if (player.getDonationManager().isDonator() && player.getRights() == 0)
				World.sendWorldMessage("<col=317eff>{"+player.prestigeLevel+"} Donator</col> <img=17>" + player.getDisplayName() + ": <col=317eff>" + message + "</col>", false, true);
			else if (player.getDonationManager().isExtremeDonator() && player.getRights() == 0)
				World.sendWorldMessage("<col=317eff>{"+player.prestigeLevel+"} Extreme Donator</col> <img=24>" + player.getDisplayName() + ": <col=317eff>" + message + "</col>", false, true);
			else if (player.getDonationManager().isLegendaryDonator() && player.getRights() == 0)
				World.sendWorldMessage("<col=317eff>{"+player.prestigeLevel+"} Legendary Donator</col> <img=18>" + player.getDisplayName() + ": <col=317eff>" + message + "</col>", false, true);
			else if (player.getDonationManager().isDivineDonator() && player.getRights() == 0)
				World.sendWorldMessage("<col=317eff>{"+player.prestigeLevel+"} Divine Donator</col> <img=9>" + player.getDisplayName() + ": <col=317eff>" + message + "</col>", false, true);
			else if (player.getDonationManager().isHeroicDonator() && player.getRights() == 0)
				World.sendWorldMessage("<col=317eff>{"+player.prestigeLevel+"} Heroic Donator</col> <img=25>" + player.getDisplayName() + ": <col=317eff>" + message + "</col>", false, true);
			else if (player.getDonationManager().isImmortalDonator() && player.getRights() == 0)
				World.sendWorldMessage("<col=317eff>{"+player.prestigeLevel+"} Immortal Donator</col> <img=19>" + player.getDisplayName() + ": <col=317eff>" + message + "</col>", false, true);
			else if (player.getRights() == 1)
				World.sendWorldMessage("<col=cc0005>{"+player.prestigeLevel+"} Moderator</col> <img=0>" + player.getDisplayName() + ": <col=cc0005>" + message + "</col>", false, true);
			else if (player.getRights() == 2)
				World.sendWorldMessage("<col=cc0005>{"+player.prestigeLevel+"} Admin</col> <img=0>" + player.getDisplayName() + ": <col=cc0005>" + message + "</col>", false, true);
			} else if (player.getUsername().equalsIgnoreCase("BigFuckinChungus")) {
				World.sendWorldMessage("<col=cc0005>{"+player.prestigeLevel+"} Owner</col> <img=1>" + player.getDisplayName() + ": <col=cc0005>" + message + "</col>", false, true);
				return;		
		}
	}

	public static void max(Player player, double xp) {
		if (player != null) {
			for (int i = 0; i <= 26; i++) {
				if (i == 24 || i == 26)
					player.getSkills().set(i, 120);
				else
					player.getSkills().set(i, 99);
				player.getSkills().setXp(i, xp);
				player.getAppearence().generateAppearenceData();
			}
		}
	}

	public static void reset(Player player) {
		if (player != null) {
			for (int i = 0; i < 26; i++) {
				if (i == 24)
					player.getSkills().set(i, 1);
				else
					player.getSkills().set(i, 1);
				player.getSkills().setXp(i, 0);
				player.getAppearence().generateAppearenceData();
			}
		}
	}

	private DeveloperConsole() {

	}
}