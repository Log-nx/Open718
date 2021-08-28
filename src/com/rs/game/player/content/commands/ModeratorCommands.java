package com.rs.game.player.content.commands;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.TicketSystem;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class ModeratorCommands {

	static String name;
	static Player target;

	static boolean processCommand(final Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (!player.isModerator() && !Settings.BETA) {
			return false;
		}
		String name = "";
		Player target;
		switch (cmd[0].toLowerCase()) {
		case "teleto":
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
			else {
				player.setNextWorldTile(target);
			}
			return true;
			
		case "staffzone":
		case "sz":
			if (player.isLocked() || player.getControllerManager().getController() != null) {
				player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
				return true;
			}
			player.setNextWorldTile(new WorldTile(2667, 10396, 0));
			return true;

		case "unnull":
		case "sendhome":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if(target == null)
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
			else {
				target.unlock();
				target.getControllerManager().forceStop();
				if (target.getNextWorldTile() == null) //if controler wont tele the player
					target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
				player.getPackets().sendGameMessage("You have unnulled: "+target.getDisplayName()+".");
				return true; 
			}
			return true;

		case "ticket":
			TicketSystem.answerTicket(player);
			return true;

		case "finishticket":
			TicketSystem.removeTicket(player);
			return true;

		case "mute":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				target.setMuted(Utils.currentTimeMillis() + (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
				target.getPackets().sendGameMessage("You've been muted for " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
				player.getPackets().sendGameMessage("You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ") + target.getDisplayName()+".");
			} else {
				name = Utils.formatPlayerNameForProtocol(name);
				if(!SerializableFilesManager.containsPlayer(name)) {
					player.getPackets().sendGameMessage("Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
					return true;
				}
				target = SerializableFilesManager.loadPlayer(name);
				target.setUsername(name);
				target.setMuted(Utils.currentTimeMillis() + (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
				player.getPackets().sendGameMessage("You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ") + target.getDisplayName()+".");
				SerializableFilesManager.savePlayer(target);
			}
			return true;

		}
		return clientCommand;
	}
}
