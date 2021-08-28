package com.rs.utils;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.util.logging.ExceptionLogger;

import com.rs.GameServer;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.impl.varrock.Iffie;
import com.rs.utils.Utils;

public class DiscordBot {

	private static DiscordApi api;
	public String game;
	final String token = "NDYwMTYzMzQ0MTY3MzM3OTg0.DrFZTQ.YkTrPvb3JXrwhm214wm9KZq8zmM";
	final String serverId = "479296807797653516";

	public DiscordBot() {
		new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {
			DiscordBot.api = api;
			api.addMessageCreateListener(message -> {
				if (message.getMessageAuthor().isYourself() || message.getMessageAuthor().isBotUser() || message.getReadableMessageContent().length() == 0)
					return;
				if (message.getReadableMessageContent().charAt(0) != '!') {
					if (message.getChannel().getId() == 504469472116211712L) {
						World.sendWorldMessage("<col=fdff00>" + "<img=" + getRoleId(message) + ">" + "<col=" + getRoleColour(message) + ">" + getRoleName(message) + message.getMessageAuthor().getName() + "</col>" + ": " + message.getReadableMessageContent().toString() + "</col>", false, false);
						return;
					} else {
						return;
					}
				}
				Player player;
				long messageId = message.getMessageId();
				String[] cmd = message.getReadableMessageContent().substring(1).split(" ");
				try {
					String location = "";
					location = "data/logs/commands/discord/" + message.getMessageAuthor().getDiscriminatedName() +".txt";
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
				switch (cmd[0]) {
				case "kick":
					if (message.getMessageAuthor().canManageServer() == true) {
						Player target = World.getPlayer(cmd[1].toLowerCase());

						if (target == null) {
							message.getChannel().sendMessage("Player is offline.");
							return;
						}
						target.finish();
						message.getChannel().sendMessage(cmd[1] + " has been kicked.");
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "unnull":
				case "sendhome":
					if (message.getMessageAuthor().canManageServer() == true) {
						Player target = World.getPlayer(cmd[1].toLowerCase());

						if (target == null) {
							message.getChannel().sendMessage("Player is offline.");
							return;
						}
						target.unlock();
						target.getControllerManager().forceStop();
						if (target.getNextWorldTile() == null)
							target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
						message.getChannel().sendMessage(cmd[1] + " has been sent home.");
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "bxp":
					if (message.getMessageAuthor().canManageServer() == true) {
						boolean bxp;
						if (Settings.DOUBLE_EXP == true)
							bxp = false;
						else
							bxp = true;
						Settings.DOUBLE_EXP = bxp;
						message.getChannel().sendMessage("Bonus XP is " + (Settings.DOUBLE_EXP ? "activated." : "deactivated."));
						return;
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "searchitem":
					if (message.getMessageAuthor().isServerAdmin()) {
						String itemName = "";
						for (int i = 1; i < cmd.length; i++)
							itemName += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						message.getChannel().sendMessage("Searching for items containing name: " + itemName);
						for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
							if (ItemDefinitions.getItemDefinitions(i).getName().toLowerCase().contains(itemName.toLowerCase())) {
								message.getChannel().sendMessage("Result found: " + i + " - " + ItemDefinitions.getItemDefinitions(i).getName() + " " + (ItemDefinitions.getItemDefinitions(i).isNoted() ? "(noted)" : ""));
							}
						}
						return;
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "gb":
				case "givebonds":
					if (message.getMessageAuthor().isServerAdmin()) {
						int bond = ItemIdentifiers.BOND_UNTRADEABLE;
						int amount = Integer.parseInt(cmd[2]);
						Player target = World.getPlayer(cmd[1].toLowerCase());
						if (target == null) {
							message.getChannel().sendMessage("Player is offline.");
							return;
						}
						target.getBank().addItem(bond, amount, true);
						target.getPackets().sendGameMessage("You have been given x" + amount  + " bonds by " + message.getMessageAuthor().getDiscriminatedName() + ". It is in your bank.");
						message.getChannel().sendMessage("You have sent x" + amount + " bonds to " + target.getDisplayName() + "'s bank.");
						return;
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "giveitem":
					if (message.getMessageAuthor().isServerAdmin()) {
						int itemId = Integer.parseInt(cmd[1]);
						int itemAmount = Integer.parseInt(cmd[2]);
						String name = "";
						for (int i = 3; i < cmd.length; i++)
							name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						Player target = World.getPlayerByDisplayName(name);
						if (target == null) {
							message.getChannel().sendMessage("Player is offline.");
							return;
						}
						target.getInventory().addItemMoneyPouch(itemId, itemAmount);
						target.getPackets().sendGameMessage("You have been given a " + ItemDefinitions.getItemDefinitions(itemId).getName() + " by " + message.getMessageAuthor().getDiscriminatedName() + ".");
						message.getChannel().sendMessage("You have sent " + ItemDefinitions.getItemDefinitions(itemId).getName() + " to " + target.getDisplayName() + ".");
						return;
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "takeitem":
					if (message.getMessageAuthor().isServerAdmin()) {
						int itemId = Integer.parseInt(cmd[1]);
						int itemAmount = Integer.parseInt(cmd[2]);
						String name = "";
						for (int i = 3; i < cmd.length; i++)
							name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						Player target = World.getPlayerByDisplayName(name);
						if (target == null) {
							message.getChannel().sendMessage("Player is offline.");
							return;
						}
						target.getInventory().removeItemMoneyPouch(itemId, itemAmount);
						target.getPackets().sendGameMessage("You have had " + ItemDefinitions.getItemDefinitions(itemId).getName() + " taken by " + message.getMessageAuthor().getDiscriminatedName() + ".");
						message.getChannel().sendMessage("You have taken " + ItemDefinitions.getItemDefinitions(itemId).getName() + " from " + target.getDisplayName() + ".");
						return;
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "givebank":
					if (message.getMessageAuthor().isServerAdmin()) {
						int itemId = Integer.parseInt(cmd[1]);
						int itemAmount = Integer.parseInt(cmd[2]);
						String name = "";
						for (int i = 3; i < cmd.length; i++)
							name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						Player target = World.getPlayerByDisplayName(name);
						if (target == null) {
							message.getChannel().sendMessage("Player is offline.");
							return;
						}
						target.getBank().addItem(itemId, itemAmount, true);
						target.getPackets().sendGameMessage("You have been given " + ItemDefinitions.getItemDefinitions(itemId).getName() + " by " + message.getMessageAuthor().getDiscriminatedName() + ". It is in your bank.");
						message.getChannel().sendMessage("You have sent " + ItemDefinitions.getItemDefinitions(itemId).getName() + " to " + target.getDisplayName() + "'s bank.");
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "takebank":
					if (message.getMessageAuthor().isServerAdmin()) {
						int itemId = Integer.parseInt(cmd[1]);
						int itemAmount = Integer.parseInt(cmd[2]);
						String name = "";
						for (int i = 3; i < cmd.length; i++)
							name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

						Player target = World.getPlayerByDisplayName(name);

						if (target == null) {
							message.getChannel().sendMessage("Player is offline.");
							return;
						}

						target.getBank().removeItem(target.getBank().getItemSlot(itemId), itemAmount, true, true);
						target.getPackets().sendGameMessage("You have had " + ItemDefinitions.getItemDefinitions(itemId).getName() + " taken from your bank by " + message.getMessageAuthor().getDiscriminatedName() + ". It is in your bank.");
						message.getChannel().sendMessage("You have taken " + ItemDefinitions.getItemDefinitions(itemId).getName() + " from " + target.getDisplayName() + "'s bank.");
					} else {
						message.getChannel().sendMessage("You don't have permission to do that.");
					}
					break;
				case "players":
					String list = "Players Online: \n";
					if (World.getPlayers().size() == 0)
						list += "none" + "\n";
					for (Player p : World.getPlayers()) {
						String username = p.getUsername();
						if (username != null)
							list += (getMessageIcon(username) + username.toUpperCase().charAt(0) + username.substring(1, username.length()) + "\n");
					}
					list += "There " + (World.getPlayers().size() == 1 ? "is" : "are") + " currently " + World.getPlayers().size() + " " + (World.getPlayers().size() == 1 ? "person" : "people") + " playing on Diccus!";
					message.getChannel().sendMessage(list);
					break;
				case "staff":
					List<String> moderators = new ArrayList<>(), administrators = new ArrayList<>(), developers = new ArrayList<>(), owners = new ArrayList<>();
					for (Player p : World.getPlayers()) {
						if (p.isOwner())
							owners.add(p.getDisplayName().substring(0, 1).toUpperCase() + p.getDisplayName().substring(1));
						else if (p.isDeveloper())
							developers.add(p.getDisplayName().substring(0, 1).toUpperCase() + p.getDisplayName().substring(1));
						else if (p.getRights() == 1)
							moderators.add(p.getDisplayName().substring(0, 1).toUpperCase() + p.getDisplayName().substring(1));
						else if (p.getRights() == 2)
							administrators.add(p.getDisplayName().substring(0, 1).toUpperCase() + p.getDisplayName().substring(1));
					}
					int staffAmt = moderators.size() + administrators.size() + developers.size() + owners.size();
					message.getChannel().sendMessage("There " + (World.getPlayers().size() == 1 ? "is" : "are")
							+ " currently " + staffAmt + " staff " + (staffAmt == 1 ? "member" : "members")
							+ " on Diccus!\n" + "Owners: "
							+ owners.stream().map(Object::toString).collect(Collectors.joining(", ")).toString() + "\n"
							+ "Developers: "
							+ developers.stream().map(Object::toString).collect(Collectors.joining(", ")).toString()
							+ "\n" + "Administrators: "
							+ administrators.stream().map(Object::toString).collect(Collectors.joining(", ")).toString()
							+ "\n" + "Moderators: "
							+ moderators.stream().map(Object::toString).collect(Collectors.joining(", ")).toString());
					break;
				case "wealth":
					player = World.getPlayer(cmd[1].toLowerCase());
					if (player == null)
						message.getChannel().sendMessage("This player is currently offline.");
					else {
						message.getChannel().sendMessage("Current wealth of " + (player.getDisplayName().substring(0, 1).toUpperCase() + player.getDisplayName().substring(1))
						+ "\n" + "Inventory: " + Utils.formatNumber(player.getInventoryValue()) + "gp\n" + "Money Pouch: " + Utils.formatNumber(player.getMoneyPouch().getCoinsAmount())
						+ "gp\n" + "Equipment: " + Utils.formatNumber(player.getEquipmentValue()) + "gp\n" + "Bank: " + Utils.formatNumber(player.getBankValue()) + "gp\n" + "Total: " + Utils.formatNumber(player.getTotalValue()) + "gp");
					}
					break;
				case "online":
					player = World.getPlayerByDisplayName(cmd[1].toLowerCase());
					if (player == null)
						message.getChannel().sendMessage("This player is currently offline.");
					else
						message.getChannel().sendMessage("This player is currently online.");
					break;
				case "reportbug":
					if (message.getChannel().getId() == 585347882673111080L) {
					String bug = "";
					for (int i = 1; i < cmd.length; i++)
						bug += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(Color.ORANGE);
					eb.setThumbnail(message.getMessageAuthor().getAvatar());
					eb.addField("Submitter", message.getMessageAuthor().getDiscriminatedName(), false);
					eb.addField("Bug Report", String.valueOf(bug), false);
					eb.setFooter("ID: " + messageId);
					eb.setTimestamp(Instant.now());
					new MessageBuilder().setEmbed(eb).send(GameServer.getDiscordBot().getAPI().getTextChannelById("535991973715247114").get());
					}
					break;
				case "commands":
					message.getChannel().sendMessage("Current Diccus commands:\n" + "!players\n" + "!staff\n" + "!events\n" + "!online <player_name>\n" + "!wealth <player_name>\n" + "!stats <player_name>");
					break;
				case "link":
					player = World.getPlayer(cmd[1].toLowerCase());
					if (player.getDiscordName() != null) {
						return;
					}
					if (player != null) {
						player.setDiscordName(message.getMessageAuthor().getDiscriminatedName());
						player.getDialogueManager().startDialogue("Discord", 1);
					}
					break;
				case "approve":
					long id = messageId;
					message.deleteMessage(String.valueOf(id));
					break;
				default:
					message.getChannel().sendMessage("Invalid command, use .commands for a list of commands");
					break;
				}
				message.getMessageAuthor().getMessage().delete();
			});
		})
				.exceptionally(ExceptionLogger.get());
	}

	public void shutdown() {
		api.disconnect();
	}

	public DiscordApi getAPI() {
		return api;
	}

	private String getMessageIcon(String players) {
		String icon = "";
		String id = "";
		int iconId = World.getPlayerByDisplayName(players).getMessageIcon();
		switch (iconId) {
		case 1:
			icon = "mod_silver";
			id = "507965693669670912";
			break;
		case 2:
			icon = "mod_gold";
			id = "507965660697985029";
			break;
		case 4:
			icon = "vip";
			id = "507965770857447439";
			break;
		case 5:
			icon = "mod_vip";
			id = "507965798631866368";
			break;
		case 6:
			icon = "ironman";
			id = "507965712510484480";
			break;
		case 8:
			icon = "hcim";
			id = "507965742327791648";
			break;
		}
		if (icon != "")
			return "<:" + icon + ":" + id + "> ";
		else
			return "";
	}

	public int getRoleId(MessageCreateEvent message) {
		int iconId = 3;
		if (!message.getMessageAuthor().canManageRolesOnServer())
			iconId = 3;
		if (message.getMessageAuthor().isServerAdmin())
			iconId = 1;

		return iconId;
	}

	public String getRoleName(MessageCreateEvent message) {
		String roleName = "[Member] ";
		if (!message.getMessageAuthor().canManageRolesOnServer())
			roleName = "[Member] ";
		else
			roleName = "[Diccus Staff] ";
		if (message.getMessageAuthor().isServerAdmin() && !message.getMessageAuthor().getName().contains("BigFuckinChungus"))
			roleName = "[Diccus Founder] ";
		return roleName;
	}

	public String getRoleColour(MessageCreateEvent message) {
		String roleColour = "418363";
		if (!message.getMessageAuthor().canManageRolesOnServer())
			roleColour = "418363";
		else
			roleColour = "7c53dd";
		if (message.getMessageAuthor().isServerAdmin())
			roleColour = "ecbfbf";
		return roleColour;
	}
	
	public static String currentTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	public static String setTrueDiscordName(String trueDiscordName) {
		return trueDiscordName;
	}
	
	public static void sendMessage(String message, String channel) {
		new MessageBuilder().append(message).send(GameServer.getDiscordBot().getAPI().getTextChannelById(channel).get());;
	}
	
	public static void sendRareDrop(Player player, Item item) {
		sendMessage(player.getDisplayName() + " has received a " + ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase() + " drop!", "504474120956936211");
	}
	
	public static void sendBankWithdrawl(Player player, Item item) {
		sendMessage(player.getDisplayName() + " has removed " + ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase() + " | Amount: " + item.getAmount() + " | IP: " + player.getSession().getIP(), "572240283442348035");
	}
	
	public static void sendBankDeposit(Player player, Item item) {
		sendMessage(player.getDisplayName() + " has banked " + ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase() + " | Amount: " + item.getAmount() + " | IP: " + player.getSession().getIP(), "572240283442348035");
	}
}
