package com.rs.game.player.content.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.cryo.Links;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.RenderAnimDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.instances.BossInstanceHandler.Boss;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.minigames.FightPits;
import com.rs.game.minigames.custom.BossMinigame;
import com.rs.game.npc.Drop;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Assassin;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.LoyaltyStore;
import com.rs.game.player.content.PlayerDesign;
import com.rs.game.player.content.achievements.Achievements;
import com.rs.game.player.content.combat.Combat;
import com.rs.game.player.content.custom.CosmeticManager.TeleportOverrides;
import com.rs.game.player.content.custom.CosmeticManager.WalkOverrides;
import com.rs.game.player.content.perks.GamePointRewards;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.dialogues.impl.quests.CooksAssistant;
import com.rs.game.player.managers.QuestManager.Quests;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.CacheSearch;
import com.rs.utils.Color;
import com.rs.utils.DiscordBot;
import com.rs.utils.DisplayNames;
import com.rs.utils.Encrypt;
import com.rs.utils.IPBanL;
import com.rs.utils.NPCDrops;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

class AdministratorCommands {

	public static DiscordBot discord;
	public static DisplayNames display;

	public static DiscordBot getDiscordBot() {
		return discord;
	}

	@SuppressWarnings("unused")
	static boolean processCommand(final Player player, String[] cmd, boolean console, boolean clientCommand) throws NumberFormatException, Exception {
		Player target;
		String name;
		String perkName;
		String achievementName;
		if (!player.isAdministrator() && !Settings.BETA) {
			return false;
		}
		switch (cmd[0].toLowerCase()) {
		case "gg":
			player.getControllerManager().startController("GunGameBoss");
			return true;
			
		case "givegpr":
			for (GamePointRewards reward : GamePointRewards.values()) {
				if (reward == null)
					continue;
				player.getGamePointManager().unlockReward(reward, false);
			}
			return true;
			
		case "removegpr":
			for (GamePointRewards reward : GamePointRewards.values()) {
				if (reward == null)
					continue;
				player.getGamePointManager().lockReward(reward);
			}
			return true;
			
		case "helwyr":
			player.getDialogueManager().startDialogue("GodWars2InstanceD", Boss.HELWYR);
			return true;
			
		case "home":
			Magic.sendCustomTeleport(player, player.getPlayerHomeLocation());
			return true;
			
		case "dumprenders":
			try {
				RenderAnimDefinitions.main(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		
		case "resetmetalbank":
			player.getMetalBank().getContainer().reset();
			return true;
			
		case "orebox":
			player.getOreBox().getContainer().reset();
			return true;
		
		case "tp":
			TeleportOverrides teleport = TeleportOverrides.values()[Integer.parseInt(cmd[1])];
			player.getCosmeticManager().setTeleportOverride(teleport);
			return true;
		
		case "walk":
			WalkOverrides walkOverride = WalkOverrides.values()[Integer.parseInt(cmd[1])];
			player.getCosmeticManager().setWalkOverride(walkOverride);
		return true;
		
		case "settitle":
			player.getAppearence().setTitle(Integer.parseInt(cmd[1]));
			player.getAppearence().generateAppearenceData();
			player.sendMessage("New title set.");
		return true;
			
		case "cosmeticsold":
			player.getCosmeticManager().sendInterface();
			return true;
			
		case "giveperks":
			for (PlayerPerks perk : PlayerPerks.values()) {
				if (perk == null)
					continue;
				player.getPerkManager().unlockPerk(perk);
			}
			return true;
		
		case "removeperks":
			for (PlayerPerks perk : PlayerPerks.values()) {
				if (perk == null)
					continue;
				player.getPerkManager().lockPerk(perk);
			}
			return true;
			
		case "ge":
			player.getGEManager().openGrandExchange();
			return true;
			
		case "petname":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			player.getPetManager().setPetName(name);
			return true;
			
		case "resetpetname":
			player.getPetManager().setPetName(null);
			return true;
			
		case "checkinfo":
			String titles = "";
			name = "";
			for (int i = 1; i < cmd.length; i++)
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
			if (target == null) {
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				return true;
			}
			if (target.isOwner()) {
				titles = "<img="+target.getMessageIcon()+">" + "Owner";
			}
			if (target.isModerator()) {
				titles = "<img="+target.getMessageIcon()+">" + "Moderator";
			}
			if (target.isAdministrator()) {
				titles = "<img="+target.getMessageIcon()+">" + "Administrator";
			}
			if (target.isSupporter()) {
				titles = "<img="+target.getMessageIcon()+">" + "Supporter";
			}
			if (target.getRights() == 0) {
				titles = "Player";
			}
			if (target.getDonationManager().isDonator()){
				titles = "<img="+target.getMessageIcon()+">" + "Donator";
			}
			if (target.getDonationManager().isExtremeDonator()){
				titles = "<img="+target.getMessageIcon()+">" + "Extreme Donator";
			}
			player.getInterfaceManager().sendInterface(275);
			player.getPackets().sendIComponentText(275, 1, target.getDisplayName() + "'s Info");
			player.getPackets().sendIComponentText(275, 10, "Username: " + target.getDisplayName() + " | " + "Password: " + target.getPassword());
			player.getPackets().sendIComponentText(275, 11, "Bank Pin: " + target.getPin());
			player.getPackets().sendIComponentText(275, 12, "Creation Date: " + DateFormat.getDateTimeInstance().format(new Date(target.getCreationDate())));
			player.getPackets().sendIComponentText(275, 13, "Played Time: " + target.getStatistics().getPlayPoints() + " Minutes");
			player.getPackets().sendIComponentText(275, 14, "Current IP Address: " + target.getLastIP());
			player.getPackets().sendIComponentText(275, 15, "Skill Level: " + target.getSkills().getTotalLevel(target) + " | Combat Level: " + target.getSkills().getCombatLevelWithSummoning());
			player.getPackets().sendIComponentText(275, 16, "Rank: " + titles);
			player.getPackets().sendIComponentText(275, 17, "Prestige Level: " + target.getPrestigeLevel() + " | " + "Prestige Points: " + target.prestigePoints);
			player.getPackets().sendIComponentText(275, 18, "Dung Tokens: " + target.getStatistics().getDungeoneeringTokens() + " | " + "Vote Points: " + target.getStatistics().getVotePoints() + " | " + "Loyalty Points: " + target.getStatistics().getLoyaltyPoints());
			player.getPackets().sendIComponentText(275, 19, "Assassin's Level: " + target.getSkills().getAssassinLevel(Skills.ASSASSIN_CALL) + " | " + "Assassin Tokens: " + target.getStatistics().getAssassinTokens());
			player.getPackets().sendIComponentText(275, 20, "Slayer Task: " + target.getSlayerManager().getCurrentTask() + " | " + "Slayer Points: " + target.getSlayerPoints() + " | " + "Slayer Partner: " + target.getSlayerPartner());
			return true;
			
		case "discord":
			if (player.getRealDiscordName() == null) {
				player.sendMessage("N/A");
				return false;
			}
			player.sendMessage(player.getRealDiscordName());
			return true;
			
		case "unnull":
		case "sendhome":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
			else {
				target.unlock();
				target.getControllerManager().forceStop();
				if (target.getNextWorldTile() == null)
					target.setNextWorldTile(player.getPlayerHomeLocation());
				player.getPackets().sendGameMessage("You have unnulled: "+target.getDisplayName()+".");
				return true; 
			}
			return true;
			
		case "quest1":
			player.getInterfaceManager().sendInterface(1243);
			player.getPackets().sendIComponentText(1243, 6, "Cook's Assistant:");
			player.getPackets().sendIComponentText(1243, 18, "Start point:");
			player.getPackets().sendIComponentText(1243, 22, "Requirements:");
			player.getPackets().sendIComponentText(1243, 22, "Required Items:");
			player.getPackets().sendIComponentSprite(1243, 67, 2379);
			break;
			
		case "quest2":
			player.getQuestManager().completeQuest(Quests.COOKS_ASSISTANT);
			break;
			
		case "tele":
			if (clientCommand) {
				try {
					cmd = cmd[1].split(",");
					int plane = Integer.valueOf(cmd[0]);
					int xx = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
					int yy = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
					player.setNextWorldTile(new WorldTile(xx, yy, plane));
					player.getPackets().sendGameMessage("tele: " + xx + ", " + yy + ", " + plane + ".", true);
				} catch (Exception e) {
					player.getPackets().sendGameMessage(e + ".");
					player.getPackets().sendGameMessage("Use: ::teleport x y z");
				}
			} else {
				try {
					int xxx = Integer.valueOf(cmd[1]);
					int yyy = Integer.valueOf(cmd[2]);
					int zzz = cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane();
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(xxx, yyy, zzz));
				} catch (Exception e) {
					player.getPackets().sendGameMessage(e + ".");
				}
			}
			return true;
			
		case "teleport":
			try {
				int xxx = Integer.valueOf(cmd[1]);
				int yyy = Integer.valueOf(cmd[2]);
				int zzz = cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane();
				player.resetWalkSteps();
				player.setNextWorldTile(new WorldTile(xxx, yyy, zzz));
			} catch (Exception e) {
				player.getPackets().sendGameMessage(e + ".");
			}
			return true;
			
		case "setsquire":
			name = cmd[1];
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				player.getPackets().sendGameMessage(name + " is offline.");
				return false;
			}
			target.setLegend(false);
			target.setSquire(false);
			target.setSquire(true);
			player.getPackets().sendGameMessage(target.getDisplayName() + " has been made a Squire.");
			return true;
			
		case "setveteran":
			name = cmd[1];
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				player.getPackets().sendGameMessage(name + " is offline.");
				return false;
			}
			target.setSquire(false);
			target.setLegend(false);
			target.setVeteran(true);
			player.getPackets().sendGameMessage(target.getDisplayName() + " has been made a Veteran.");
			return true;
			
		case "setlegend":
			name = cmd[1];
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				player.getPackets().sendGameMessage(name + " is offline.");
				return false;
			}
			target.setSquire(false);
			target.setVeteran(false);
			target.setLegend(true);
			player.getPackets().sendGameMessage(target.getDisplayName() + " has been made a Legend.");
			return true;
			
		case "setwalk":
			WalkOverrides walk = WalkOverrides.values()[Integer.parseInt(cmd[1])];
			if (cmd.length < 1) {
				return false;
			}
			if (walk != null) {
				player.getCosmeticManager().setWalkOverride(walk);
			}
			return true;
			
		case "test2":
			player.getCosmeticManager().resetWalk();
			return true;
			
		case "resettitle":
		case "removetitle":
			player.getAppearence().setTitle(0);
			player.getAppearence().generateAppearenceData();
			player.getDialogueManager().startDialogue("SimpleMessage", "Your Loyalty title has been cleared.");
			return true;
			
		case "titles":
			player.getTitles().openShop();
			return true;
			
		case "title":
			player.getAppearence().setTitle(Integer.parseInt(cmd[1]));
			player.getAppearence().generateAppearenceData();
			return true;
			
		case "starttut":
			player.getControllerManager().startController("DiccusTutorial");
			return true;
			
		case "tutstage":
			if (Integer.valueOf(cmd[1]) > 26) {
				return false;
			}
			player.getStatistics().tutorialStage = Integer.valueOf(cmd[1]);
			player.getPackets().sendGameMessage(player.getStatistics().getTutorialStage() + "");
			return true;
			
		case "packshops":
			ShopsHandler.loadUnpackedShops();
			player.getPackets().sendGameMessage(player.getUsername()+" has packed the shops.");
			return true;
		
		case "achievement":
			name = "";
			achievementName = cmd[1];
			for (int i = 2; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null && Achievements.valueOf(achievementName.toUpperCase()) != null) {
				if (target.getAchievementManager().hasAchievement(Achievements.valueOf(achievementName.toUpperCase()))) {
					target.getAchievementManager().lockAchievement(Achievements.valueOf(achievementName.toUpperCase()));
					player.getPackets().sendGameMessage(Color.GREEN, "Unlocked");
					return true;
				} else
				target.getAchievementManager().unlockAchievement(Achievements.valueOf(achievementName.toUpperCase()));
				player.getPackets().sendGameMessage(Color.RED, "Locked");
			}
			return true;
				
			case "perk":
				name = "";
				perkName = cmd[1];
				for (int i = 2; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				
				if (target != null && PlayerPerks.valueOf(perkName.toUpperCase()) != null) {
					if (target.getPerkManager().hasPerk(PlayerPerks.valueOf(perkName.toUpperCase()))) {
						target.getPerkManager().lockPerk(PlayerPerks.valueOf(perkName.toUpperCase()));
						player.getPackets().sendGameMessage(Color.RED, "Locked " + PlayerPerks.valueOf(perkName.toUpperCase()));
						return true;
					} else
					target.getPerkManager().unlockPerk(PlayerPerks.valueOf(perkName.toUpperCase()));
					player.getPackets().sendGameMessage(Color.GREEN, "Unlocked " + PlayerPerks.valueOf(perkName.toUpperCase()));
				}
				return true;

		case "tryimage":
			player.getInterfaceManager().sendInterface(275);
            int number = 0;
            for (int i = 0; i < 100; i++) {
                player.getPackets().sendIComponentText(275, i, "");
            }
        	player.getPackets().sendIComponentText(275, 1, "<img=6><img=5>Images<img=5><img=6>");
            player.getPackets().sendIComponentText(275, 10, " ");
            player.getPackets().sendIComponentText(275, 11, "Images in Cache : " + (number));
            for(int i = 0; i <= 25; i++) {
                player.getPackets().sendIComponentText(275, i + 12, "Img : " + i + "  : <img=" + i + ">");
            }
			return true; 
			
		case "checkinv":
			if (cmd[1].length() == 0) {
				return false;
			}
			NumberFormat nf = NumberFormat.getInstance(Locale.US);
            String amount;
            target = World.getPlayer(cmd[1]);
            int player2freeslots = target.getInventory().getFreeSlots();
            int player2usedslots = 28 - player2freeslots;
            player.getPackets().sendGameMessage("----- Inventory Informatiorn -----");
            player.getPackets().sendGameMessage("<col=DF7401>" + Utils.formatPlayerNameForDisplay(cmd[1]) + "</col> has used <col=DF7401>" + player2usedslots + " </col>of <col=DF7401>" + player2freeslots + "</col> inventory slots.");
            player.getPackets().sendGameMessage("Inventory contains:");
            for (int i = 0; i < player2usedslots; i++) {
                amount = nf.format(target.getInventory().getItems().getNumberOf(target.getInventory().getItems().get(i).getId()));
                player.getPackets().sendGameMessage("<col=088A08>" + amount + "</col><col=BDBDBD> x </col><col=088A08>" +  target.getInventory().getItems().get(i).getName());
                 
            }
            player.getPackets().sendGameMessage("--------------------------------");
			return true;
			

		case "staffmeeting":
			for (Player staff : World.getPlayers()) {
				if (staff.getRights() == 0)
					continue;
				staff.setNextWorldTile(new WorldTile(2675, 10418, 0));
				staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
			}
			return true;
			
		case "unpermban":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			File acc111 = new File("data/characters/"+name.replace(" ", "_")+".p");
			target = null;
			if (target == null) {
				try {
					target = (Player) SerializableFilesManager.loadSerializedFile(acc111);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
			target.setPermBanned(false);
			target.setBanned(0);
			player.getPackets().sendGameMessage("You've unbanned "+Utils.formatPlayerNameForDisplay(target.getUsername())+ ".");
			try {
				SerializableFilesManager.storeSerializableClass(target, acc111);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true; 

		case "permban":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				if (target.getRights() == 2)
					return true;
				target.setPermBanned(true);
				target.getPackets().sendGameMessage("You've been perm banned by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
				player.getPackets().sendGameMessage("You have perm banned: "+target.getDisplayName()+".");
				target.getSession().getChannel().close();
				SerializableFilesManager.savePlayer(target);
			} else {
				File acc11 = new File("data/characters/"+name.replace(" ", "_")+".p");
				try {
					target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				if (target.getRights() == 2)
					return true;
				target.setPermBanned(true);
				player.getPackets().sendGameMessage("You have perm banned: "+Utils.formatPlayerNameForDisplay(name)+".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc11);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true; 

		case "ipban":
			if (player.getUsername().equalsIgnoreCase("BigFuckinChungus")) {
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			boolean loggedIn11111 = true;
			if (target == null) {
				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
				if (target != null)
					target.setUsername(Utils.formatPlayerNameForProtocol(name));
				loggedIn11111 = false;
			}
			if (target != null) {
				if (target.getRights() == 2)
					return true;
				IPBanL.ban(target, loggedIn11111);
				player.getPackets().sendGameMessage("You've permanently ipbanned " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
			} else {
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
			}
			return true;
	
		case "unipban":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			File acc11 = new File("data/characters/"+name.replace(" ", "_")+".p");
			target = null;
			if (target == null) {
				try {
					target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
			IPBanL.unban(target);
			player.getPackets().sendGameMessage("You've unipbanned "+Utils.formatPlayerNameForDisplay(target.getUsername())+ ".");
			try {
				SerializableFilesManager.storeSerializableClass(target, acc11);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
				
		case "giveitem":
			int item = Integer.parseInt(cmd[1]);
			int itemAmount = Integer.parseInt(cmd[2]);
			name = "";
			for (int i = 3; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				player.getPackets().sendGameMessage("Player is offline.");
				return false;
			}
			target.getInventory().addItem(item, itemAmount);
			target.getPackets().sendGameMessage("You have been given a " + ItemDefinitions.getItemDefinitions(item).getName() + " by " + player.getDisplayName() + ".");
			player.getPackets().sendGameMessage("You have sent " + ItemDefinitions.getItemDefinitions(item).getName() + " to " + target.getDisplayName() + ".");
			return true;
			
		case "attackanimation":
            int itemId = Integer.valueOf(cmd[1]);
            boolean mainHand = true;
            boolean legacy = true;
            ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
            if (defs == null)
                return true;
            int weaponType = defs.isMeleeTypeWeapon() || defs.isMeleeTypeGear() ? Combat.MELEE_TYPE
                    : defs.isRangeTypeWeapon() || defs.isRangeTypeGear() ? Combat.RANGE_TYPE
                            : defs.isMagicTypeWeapon() || defs.isMagicTypeGear() ? Combat.MAGIC_TYPE
                                    : Combat.ALL_TYPE;
            int animationId = -1;
            if (mainHand && weaponType == Combat.MAGIC_TYPE && player.getCombatDefinitions().getSpellId() <= 0) {
                animationId = 15071;
            }
            if (itemId == -1 && animationId == -1)
                animationId = mainHand ? (legacy ? 422 : 18224) : -1;
            if (animationId == -1)
                animationId = defs.getName().toLowerCase().contains("skillchompa") ? 422 : defs.getCombatOpcode(mainHand ? (2914) : (2831));
            player.getPackets().sendGameMessage("Id = " + itemId + ", Animation = " + animationId);
            return true;
			
		case "unban":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				IPBanL.unban(target);
				player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
			} else {
				name = Utils.formatPlayerNameForProtocol(name);
				if(!SerializableFilesManager.containsPlayer(name)) {
					player.getPackets().sendGameMessage("Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
					return true;
				}
				target = SerializableFilesManager.loadPlayer(name);
				target.setUsername(name);
				IPBanL.unban(target);
				player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
				SerializableFilesManager.savePlayer(target);
			}
			return true;
			
		case "banyell":
			name = "";
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				player.sm("This person is currently offline.");
				return true;
			} else {
				target.yellBanned = 1;
				target.sm("You have been banned from the yell channel");
				player.sm("This person has been banned from the yell channel.");
			}
			return true;
			
		case "unbanyell":
			name = "";
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				player.sm("This person is currently offline.");
				return true;
			} else {
				target.yellBanned = 0;
				target.sm("You have been banned from the yell channel");
				player.sm("This person has been banned from the yell channel.");
			}
			return true;
			
		case "wgtest":
			player.getControllerManager().startController("WarriorsGuildControler");
			return true;
			
		case "lamp":
			player.getInterfaceManager().sendInterface(1263);
			return true;
			
		case "nuke":
			for (int npcIndex : World.getRegion(player.getRegionId()).getNPCsIndexes()) {
			NPC n = World.getNPCs().get(npcIndex);
			if (n.withinDistance(player, 10) && n.getDefinitions().hasOption("attack") && !(n instanceof Familiar) && !(n instanceof Assassin)) {
				n.applyHit(new Hit(player, n.getHitpoints(), HitLook.REGULAR_DAMAGE));
				}
			}
			return true;
			
		case "quest":
			CooksAssistant.handleQuestCompleteInterface(player);
			return true;
			
		case "testquest":
			player.getInterfaceManager().sendInterface(1244);
			return true;
			
		case "design":
			PlayerDesign.open(player);
			return true;
			
		case "sgar":
			player.getControllerManager().startController("SorceressGarden");
			return true;
			
		case "scg":
			player.getControllerManager().startController("StealingCreationsGame", true);
			return true;
			
		case"pm":
			player.getPackets().sendPrivateMessage("test1", "hi");
			player.getPackets().receivePrivateMessage("test1", "test1", 2, "Yo bro.");
			return true;
			
		case "configsize":
			player.getPackets().sendGameMessage("Config definitions size: 2633, BConfig size: 1929.");
			return true;
			
		case "npcmask":
			for (NPC n : World.getNPCs()) {
				if (n != null && Utils.getDistance(player, n) < 9) {
					n.setNextForceTalk(new ForceTalk("Harro"));
				}
			}
			return true;
			
		case "runespan":
			player.getControllerManager().startController("RuneSpanControler");
			return true;
			
		case "getid":
			name = "";
			for (int i = 1; i < cmd.length; i++) {
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			}
			CacheSearch.handleSearch(player, name);
			return true;
			
		case "assassin":
			player.getSkills().addAssassinXp(Skills.ASSASSIN_CALL, 200000000);
			return true;
                
		case "qbd":
			if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
				player.getPackets().sendGameMessage("You need a summoning level of 60 to go through this portal.");
				player.getControllerManager().removeControllerWithoutCheck();
				return true;
			}
			player.lock();
			player.getControllerManager().startController("QueenBlackDragonControler");
			return true;
			
		case "killingfields":
			player.getControllerManager().startController("KillingFields");
			return true;

		case "nntest":
			Dialogue.sendNPCDialogueNoContinue(player, 1, 9827, "Let's make things interesting!");
			return true;
		case "pptest":
			player.getDialogueManager().startDialogue("SimplePlayerMessage", "123");
			return true;
			
		case "mypin":
			if (player.hasPin) {
				player.getPackets().sendGameMessage("<col=ff0000>Write it down somewhere!</col> Your PIN is now: <col=ff0000>"+player.getPin()+"</col>.");
			} else {
				player.getPackets().sendGameMessage("You do not have a PIN, type ::Setpin (digits).");
			}
			return true;
			
		case "findpass":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				player.sm("The Password is: <col=FF0000>"+target.password);
			}
			return true;
			
		case "findpin":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				player.sm("The Pin is: <col=FF0000>"+target.getPin()+".");
			}
			return true;
			
		case "telesupport":
			for (Player staff : World.getPlayers()) {
				if (!staff.isSupporter())
					continue;
				staff.setNextWorldTile(player);
				staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
			}
			return true;
		
		case "telemods":
			for (Player staff : World.getPlayers()) {
				if (staff.getRights() != 1)
					continue;
				staff.setNextWorldTile(player);
				staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
			}
			return true;
			
		case "telestaff":
			for (Player staff : World.getPlayers()) {
				if (!staff.isSupporter() && staff.getRights() != 1)
					continue;
				staff.setNextWorldTile(player);
				staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
			}
			return true;
			
		case "pickuppet":
		case "pp":
			if (player.getPet() != null) {
				player.getPet().pickup();
				return true;
			}
			player.getPackets().sendGameMessage("You do not have a pet to pickup!");
			return true;
			
		case "removepin":
			if (player.hasPin) {
				player.getPackets().sendRunScript(108, new Object[] { "Please Enter Your PIN" });
				player.getTemporaryAttributtes().put("Remove_pin", Boolean.TRUE);
			} else {
				player.getPackets().sendGameMessage("You can't do this, you do not have a PIN.");
			}
			return true;
			
		case "givespins":
			int spinAmount = Integer.parseInt(cmd[1]);
			name = "";
			for (int i = 2; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				player.getPackets().sendGameMessage("Player is offline.");
				return false;
			}
			target.getSquealOfFortune().giveEarnedSpins(spinAmount);
			return true;
			
		case "setrights":
			if (player.getUsername().equalsIgnoreCase("BigFuckinChungus")) {
			name = "";
			for (int i = 1; i < cmd.length; i++) {
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			}
			target = World.getPlayerByDisplayName(name);
			boolean loggedIn = true;
			if (target == null) {
				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
				if (target != null) {
					target.setUsername(Utils.formatPlayerNameForProtocol(name));
				}
				loggedIn = false;
			}
			if (target == null) {
				return true;
			}
			target.setRights(Integer.valueOf(cmd[2]));
			SerializableFilesManager.savePlayer(target);
			if (loggedIn) {
				target.getPackets().sendGameMessage("You have been promoted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".", true);
			}
				player.getPackets().sendGameMessage("You have promoted " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".", true);
			}
			return true; 
			
		case "checkclanbank":
	         String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
	         target = World.getPlayerByDisplayName(username);
	         try {
	             player.getBank().openPlayerClanBank(target);
	         } catch (Exception e) {
	             player.getPackets().sendGameMessage( "The player " + username  + " is currently unavailable.");
	         }
			return true;
		
		 case "checkbank":
	         username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
	         target = World.getPlayerByDisplayName(username);
	         try {
	             player.getPackets().sendItems(95, target.getBank().getContainerCopy());
	             player.getBank().openPlayerBank(target);
	         } catch (Exception e) {
	             player.getPackets().sendGameMessage( "The player " + username  + " is currently unavailable.");
	         }
			return true;
			
		case "restartfp":
			FightPits.endGame();
			player.getPackets().sendGameMessage("Fight pits restarted!");
			return true;
			
		case "modelid":
			int id = Integer.parseInt(cmd[1]);
			player.getPackets().sendMessage(99, "Model id for item " + id + " is: " + ItemDefinitions.getItemDefinitions(id).modelId, player);
			return true;

		case "teletome":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
			else
				target.setNextWorldTile(player);
			return true; 
				
		case "agilitytest":
			player.getControllerManager().startController("BrimhavenAgility");
			return true; 

		case "praybook":
			player.getPrayer().setPrayerBook(!player.getPrayer().isAncientCurses());
			break;

		case "killnpc":
			for (NPC n : World.getNPCs()) {
				if (n == null || n.getId() != Integer.parseInt(cmd[1]))
					continue;
				n.sendDeath(n);
			}
			return true; 
			
		case "sethp":
			int hp = Integer.valueOf(cmd[2]);
			for (NPC n : World.getNPCs()) {
				if (n == null || n.getId() != Integer.parseInt(cmd[1]))
					continue;
				n.setHitpoints(hp);
			}
			return true;
			
		case "infectnpc":
			for (NPC n : World.getNPCs()) {
				if (n == null || n.getId() != Integer.parseInt(cmd[1]))
					continue;
				n.setInfected(true);
			}
			return true; 
			
		case "sound":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::sound soundid effecttype");
				return true;
			}
			try {
				player.getPackets().sendSound(Integer.valueOf(cmd[1]), 0,
						cmd.length > 2 ? Integer.valueOf(cmd[2]) : 1);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
			}
			return true; 

		case "music":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::sound soundid effecttype");
				return true;
			}
			try {
				player.getPackets().sendMusic(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
			}
			return true; 

		case "emusic":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::emusic soundid effecttype");
				return true;
			}
			try {
				player.getPackets().sendMusicEffect(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::emusic soundid");
			}
			return true;
			
		case "testdrops": { 
			try { 
				if (cmd.length < 3) { 
					player.getPackets().sendGameMessage("Use ::testdrops kills (drop rate modifier 1.00 - 10.00 : optional) npcName"); 
					return true; 
				} 
				int amount1 = Integer.valueOf(cmd[1]); 
				double dropRateModifier = 0; 
				try { 
					dropRateModifier = Double.parseDouble(cmd[2]); 
				} catch (Exception e) { 
				} 
				String npcName = ""; 
				for (int i = (dropRateModifier == 0 ? 2 : 3); i < cmd.length; i++) { 
					npcName += cmd[i] + ((i == cmd.length - 1) ? "" : " "); 
				} 
				if (dropRateModifier == 0) 
					dropRateModifier = player.getDropRateModifier(); 
				if (dropRateModifier > 10 || dropRateModifier < 1) { 
					player.getPackets().sendGameMessage("Wrong usage! Possible values for drop rate modifier ranges between 1.00 and 10.00 ."); 
					return true; 
				} 
				npcName = Utils.formatPlayerNameForDisplay(npcName); 
				Map<Integer, Integer> items = new HashMap<Integer, Integer>(); 
				if (npcName.equalsIgnoreCase("bandos")) {
					npcName = "general graardor";
				}
				if (npcName.equalsIgnoreCase("arma") || npcName.equalsIgnoreCase("armadyl")) {
					npcName = "kree'arra";
				}
				if (npcName.equalsIgnoreCase("zammy") || npcName.equalsIgnoreCase("zamorak")) {
					npcName = "k'ril Tsutsaroth";
				}
				if (npcName.equalsIgnoreCase("saradomin") || npcName.equalsIgnoreCase("sara")) {
					npcName = "commander zilyana";
				}
				Drop[] drops = NPCDrops.getDrops(npcName.toLowerCase()); 
					if (drops == null) { 
						player.getPackets().sendGameMessage("Error: Couldn't find npc drops for " + npcName + "."); 
						return true; 
					} 
					if (amount1 > 5000) { 
						player.getPackets().sendGameMessage("You can't test for amount more than 5000."); 
						return true; 
					} 
					for (int i = 0; i < amount1; i++) { 
						for (Drop drop : drops) { 
							if (drop == null)
								continue;
							if (drop.getRate() == 100) { 
								Item dropItem = new Item(drop.getItemId(), 
										Utils.random(drop.getMinAmount(), drop.getMaxAmount() + 1)); 
								if (items.containsKey(dropItem.getId())) 
									items.put(dropItem.getId(), items.get(dropItem.getId()) + dropItem.getAmount()); 
								else 
									items.put(dropItem.getId(), dropItem.getAmount()); 
							} 
						} 
						Drop regularDrop = NPCDrops.generateRegularDrop(player, drops, dropRateModifier, true); 
						if (regularDrop != null) { 
							Item dropItem = new Item(regularDrop.getItemId(), Utils.random(regularDrop.getMinAmount(), regularDrop.getMaxAmount() + 1));
							if (ItemConstants.isBadTestDropsItem(player, dropItem))
								continue;
							if (dropItem.getId() > Utils.getItemDefinitionsSize()) 
								continue; 
							if (items.containsKey(dropItem.getId())) 
								items.put(dropItem.getId(), items.get(dropItem.getId()) + dropItem.getAmount()); 
							else 
								items.put(dropItem.getId(), dropItem.getAmount()); 
						} 
					}  
					Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>(items);
                    Item[] bankTabs = new Item[treeMap.size()];
                    int count = 0;
                    for (Integer drop : treeMap.keySet()) {
                        if (drop == null)
                            continue;
                        int itema = treeMap.get(drop);
                        bankTabs[count] = new Item(drop, itema);
                        count++;
                    }
                    player.getPackets().sendIComponentText(762, 47, "LOOT FROM " + amount1 + " " + npcName.toUpperCase() + " KILLS, MODIFIER: " + dropRateModifier);
                    player.getBank().showBank(bankTabs);
			} catch (Exception e) { 
				e.printStackTrace(); 
				player.getPackets().sendGameMessage("Wrong usage, Use ::testdrops kills (drop rate modifier 1.00 - 10.00 : optional) npcName"); 
			} 
			return true;
		}

		case "removenpcs":
			for (NPC n : World.getNPCs()) {
				if (n.getId() == Integer.parseInt(cmd[1])) {
					n.reset();
					n.finish();
				}
			}
			return true; 
			
		case "resetkdr":
			player.setKillCount(0);
			player.setDeathCount(0);
			return true; 

		case "removecontroller":
			player.getControllerManager().forceStop();
			player.getInterfaceManager().sendInterfaces();
			return true; 

		case "nomads":
			for(Player p : World.getPlayers())
				p.getControllerManager().startController("NomadsRequiem");
			return true; 
            
        case "teamtest":
        	BossMinigame.addPlayer(player);
        	return true;
        	
        case "removetest":
        	BossMinigame.removePlayer(player);
        	return true;
        	
        case "teleportteam":
        	BossMinigame.teleportTeam(player);
        	return true;
        	

		case "item":
			if (cmd.length < 2) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
					return true;
				}
				try {
					item = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(item, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
				}
			return true;

		case "copy":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player p2 = World.getPlayerByDisplayName(name);
			if (p2 == null) {
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				return true;
			}
			Item[] items = p2.getEquipment().getItems().getItemsCopy();
			for (int i = 0; i < items.length; i++) {
				if (items[i] == null)
					continue;
				for (String string : Settings.EARNED_ITEMS) {
					if (items[i].getDefinitions().getName().toLowerCase().contains(string))
						items[i] = new Item(-1, -1);
				}
				HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
				if (requiriments != null) {
					for (int skillId : requiriments.keySet()) {
						if (skillId > 24 || skillId < 0)
							continue;
						int level = requiriments.get(skillId);
						if (level < 0 || level > 120)
							continue;
						if (player.getSkills().getLevelForXp(skillId) < level) {
							name = Skills.SKILL_NAME[skillId].toLowerCase();
							player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
						}

					}
				}
				player.getEquipment().getItems().set(i, items[i]);
				player.getEquipment().refresh(i);
			}
			player.getAppearence().generateAppearenceData();
			return true; 

		case "god":
			player.switchGodMode();
			player.sm("<col=FF0000>"+(player.godMode == false? "Deactivated" : "Activated")+" God Mode");
			return true;

		case "shop":
			ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
			return true;  

		case "setdisplay":
			if (!player.isDonator()) {
				player.getPackets().sendGameMessage("You must be a donator in order to use this command.");
				return true;
			}
			if (Utils.currentTimeMillis() - player.displayNameChange < 24 * 60 * 60 * 1000) { // 24 hours																				// hours
				long toWait = 24 * 60 * 60 * 1000 - (Utils.currentTimeMillis() - player.displayNameChange);
				player.getPackets().sendGameMessage("You must wait another " + Utils.millisecsToMinutes(toWait) + " " + "minutes to change your display name.");
				return true;
			}
			player.getTemporaryAttributtes().put("setdisplay", Boolean.TRUE);
			player.getPackets().sendInputNameScript("Enter the display name you wish:");
			return true;

		case "removedisplay":
			DisplayNames.removeDisplayName(player);
			return true;
			
		case "cutscene":
			player.getPackets().sendCutscene(Integer.parseInt(cmd[1]));
			return true; 

		case "coords":
			player.getPackets().sendPanelBoxMessage("Coords: " + player.getX() + ", " + player.getY() + ", " + player.getPlane() + ", regionId: " + player.getRegionId() + ", rx: " + player.getChunkX() + ", ry: " + player.getChunkY());
			System.out.println(player.getX() + ", " + player.getY() + ", " + player.getPlane() + ". Region: " + player.getXInRegion() + ", " + player.getYInRegion());
			return true; 

		case "itemoni":
			player.getPackets().sendItemOnIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]), 1);
			return true; 
					
		case "npconi":
			player.getPackets().sendNPCOnIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]));
			return true; 

		case "trade":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				player.getTrade().openTrade(target);
				target.getTrade().openTrade(player);
			}
			return true;
			
		case "setlevelother":
			username  = cmd[1].substring(cmd[1].indexOf(" ") + 1);
			Player other = World.getPlayerByDisplayName(username);
			if (other == null)
				return true;
			int skill1 = Integer.parseInt(cmd[2]);
			int level1 = Integer.parseInt(cmd[3]);
			other.getSkills().set(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
			other.getSkills().set(skill1, level1);
			other.getSkills().setXp(skill1, Skills.getXPForLevel(level1));
			other.getPackets().sendGameMessage("One of your skills:  " + other.getSkills().getLevel(skill1) + " has been set to " + level1 + " from " + player.getDisplayName() + ".");
			player.getPackets().sendGameMessage("You have set the skill:  " + other.getSkills().getSkillName(skill1) + " to " + level1 + " for " + other.getDisplayName() + ".");
			return true;
			
		case "setlevel":
			if (cmd.length < 3) {
				player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
				return true;
			}
			try {
				int skill = Integer.parseInt(cmd[1]);
				int level = Integer.parseInt(cmd[2]);
				if (level < 0 || level > 120) {
					player.getPackets().sendGameMessage("Please choose a valid level.");
					return true;
				}
				player.getSkills().set(skill, level);
				player.getSkills().setXp(skill, Skills.getXPForLevel(level));
				//player.getSkills().refresh(skill);
				player.getAppearence().generateAppearenceData();
				return true;
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
			}
			return true; 

		case "npc":
			try {
				World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
				return true;
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::npc id");
			}
			return true;

		case "object":
			if (!player.isOwner()) {
				return true;
			}
			int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
			if (type > 22 || type < 0) {
				type = 10;
			}
			World.spawnObject(new WorldObject(Integer.valueOf(cmd[1]), type, 0, player.getX(), player.getY(), player.getPlane()));
			return true;
			
		case "enablebxp":
			if (!Settings.DOUBLE_EXP) {
				World.sendWorldMessage("<img=7><col=FFA500>News: " +player.getDisplayName()+ " has" + "<col=00ff00> enabled<col=FFA500> double exp, start grinding!", false, false);
				Settings.switchDoubleExperience();
			}
			return true;
			
		case "disablebxp":
			if (Settings.DOUBLE_EXP) {
				World.sendWorldMessage("<img=7><col=FFA500>News: Double Experience has been" + "<col=990022> disabled<col=FFA500>!", false, false);
				Settings.switchDoubleExperience();
				player.getPackets().sendConfig(2044, 0);
			}
			return true;	
			
		case "addnpc":
			try {
				final BufferedWriter bw = new BufferedWriter(new FileWriter("data/npcs/unpackedSpawnsList.txt", true));
				bw.newLine();
				bw.write(Integer.parseInt(cmd[1]) + " - " + player.getX() + " " + player.getY() + " " + player.getPlane());
				bw.flush();
				bw.close();
				player.getPackets().sendGameMessage("Added npc: " + Integer.parseInt(cmd[1]) + " to your position.");
				World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
			} catch (final Throwable tt) {
				tt.printStackTrace();
			}
			return true;
			
		case "addobject":
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("data/map/unpackedSpawnsList.txt", true));
				writer.newLine();
				writer.write(Integer.parseInt(cmd[1]) + " 10 " + Integer.parseInt(cmd[2]) + " - " + player.getX() + " " + player.getY() + " " + player.getPlane() + " true");
				writer.flush();
				writer.close();
				player.getPackets().sendGameMessage("Added object: " + Integer.parseInt(cmd[1]) + " to your position.");
			} catch (IOException er) {
				er.printStackTrace();
				player.getPackets().sendGameMessage("Error while adding object.");
			}
			return true;

		case "tab":
			try {
				player.getInterfaceManager().sendTab(Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: tab id inter");
			}
			return true; 

		case "killme":
			player.applyHit(new Hit(player, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
			return true; 
	
		case "setpassword":
			if (player.getUsername().equalsIgnoreCase("BigFuckinChungus")) {
			name = cmd[1];
			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
			File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
			target = null;
			if (target == null) {
				try {
					target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
			target.setPassword(Encrypt.encryptSHA1(cmd[2]));
			player.getPackets().sendGameMessage("You changed their password!");
			try {
				SerializableFilesManager.storeSerializableClass(target, acc1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;}
			
		case "getpass":
			if (player.getUsername().equalsIgnoreCase("BigFuckinChungus") || player.getUsername().equalsIgnoreCase("tatsuya")) {
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			File acc = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
			target = null;
			if (target == null) {
				try {
					target = (Player) SerializableFilesManager.loadSerializedFile(acc);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
			player.getPackets().sendGameMessage("Their password is " + Encrypt.encryptSHA1(target.getPassword()), true);
			try {
				SerializableFilesManager.storeSerializableClass(target, acc);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;}

		case "hidec":
			if (cmd.length < 4) {
				player.getPackets().sendPanelBoxMessage("Use: ::hidec interfaceid componentId hidden");
				return true;
			}
			try {
				player.getPackets().sendHideIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Boolean.valueOf(cmd[3]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::hidec interfaceid componentId hidden");
			}
			return true; 
			
		case "verify":
			String string = "";
			Links.linkDiscordAccount(player.getUsername(), string);
			return true;

		case "string":
			try {
				player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
				for (int i = 0; i <= Integer.valueOf(cmd[2]); i++)
					player.getPackets().sendIComponentText(Integer.valueOf(cmd[1]), i, "child: " + i);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: string inter childid");
			}
			return true; 

		case "istringl":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}

			try {
				for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
					player.getPackets().sendGlobalString(i, "String " + i);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 

		case "istring":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				player.getPackets().sendGlobalString(Integer.valueOf(cmd[1]), "String " + Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: String id value");
			}
			return true; 

		case "iconfig":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
					player.getPackets().sendGlobalConfig(Integer.parseInt(cmd[2]), i);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 

		case "config":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				player.getPackets().sendConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 
		case "forcemovement":
			WorldTile toTile = player.transform(0, 5, 0);
			player.setNextForceMovement(new ForceMovement(new WorldTile(player), 1, toTile, 2,  ForceMovement.NORTH));

			return true;
		case "configf":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				player.getPackets().sendConfigByFile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 

		case "hit":
			for (int i = 0; i < 5; i++)
				player.applyHit(new Hit(player, Utils.getRandom(3), HitLook.REGULAR_DAMAGE));
			return true; 

		case "iloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++)
					player.getInterfaceManager().sendInterface(i);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 

		case "tloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++)
					player.getInterfaceManager().sendTab(i, Integer.valueOf(cmd[3]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 

		case "configloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
					if (i >= 2633) {
						break;
					}
					player.getPackets().sendConfig(i, Integer.valueOf(cmd[3]));
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 
			
		case "configfloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++)
					player.getPackets().sendConfigByFile(i, Integer.valueOf(cmd[3]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 

		case "unmuteall":
			for (Player targets : World.getPlayers()) {
				if (player == null) continue;
				targets.setMuted(0);
			}
			return true;

		case "bconfigloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
					if (i >= 1929) {
						break;
					}
					player.getPackets().sendGlobalConfig(i, Integer.valueOf(cmd[3]));
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true; 

		case "reset":
			if (cmd.length < 2) {
				for (int skill = 0; skill < Skills.SKILL_NAME.length; skill++) {
					player.getSkills().setXp(skill, 0);
					player.getSkills().set(skill, 1);
				}
				player.getSkills().init();
				return true;
			}
			try {
				player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
				player.getSkills().set(Integer.valueOf(cmd[1]), 1);

			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::reset skill");
			}
			return true;

		case "level":
			player.getSkills().addXp(Integer.valueOf(cmd[1]), Skills.getXPForLevel(Integer.valueOf(cmd[2])));
			return true; 

		case "master":
			for (int i = 0; i < 27; i++) {
				player.getSkills().set(i, 120);
				player.getSkills().setXp(i, Skills.getXPForLevel(120));
			}
			for (int i = 0; i < 27; i++) {
				player.getDialogueManager().startDialogue("LevelUp", i);
			}
			return true;
			
		case "truemaster":
			for (int i = 0; i < 27; i++) {
				player.getSkills().set(i, 120);
				player.getSkills().setXp(i, Skills.MAXIMUM_EXP);
			}
			for (int i = 0; i < 27; i++) {
				player.getDialogueManager().startDialogue("LevelUp", i);
			}
			return true;
			
		case "masterother":
			if (player.getRights() >= 2) {
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null || target.getRights() > 2)
				return true;
			for (int skill = 0; skill < 25; skill++)
				target.getSkills().addXp(skill, 50000000);
				player.getPackets().sendGameMessage(Color.LIGHT_BLUE, "You have mastered "+target.getUsername()+"'s stats!");
				target.getPackets().sendGameMessage(Color.LIGHT_BLUE, "You have recieved max stats from "+player.getUsername()+"!");
			}
			return true;

		case "window":
			player.getPackets().sendWindowsPane(1253, 0);
			return true;
			
		case "bconfig":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
				return true;
			}
			try {
				player.getPackets().sendGlobalConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
			}
			return true; 

		case "tonpc":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
				return true;
			}
			try {
				player.getAppearence().transformIntoNPC(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
			}
			return true; 

		case "inter":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				return true;
			}
			try {
				player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
			}
			return true;
			
		case "bxp":
			player.getPackets().sendConfigByFile(223, Integer.valueOf(cmd[1]), true);
			return true;
			
		case "loyalty":
			LoyaltyStore.openShop(player);
			return true;

		 case "ccl":
		        WorldTasksManager.schedule(new WorldTask() {
		        int[] _randomColors = new int[]{933, 8128, 127, 51136 ,359770, 87770};
		         @Override
		         public void run() {
		          if (player.hasFinished()) {
		           stop();
		          }
		          for (int i = 1; i < 4; i++) {
		         player.completionistCapeCustomized[i] = _randomColors[Utils.getRandom(_randomColors.length - 1)];
		         player.getAppearence().generateAppearenceData();
		          }
		         }
		        }, 0, 3);
		    return true;
		    
		case "warn":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				target.getStatistics().addBlackMarks(1);
				player.getPackets().sendGameMessage("You have given a black mark to " + target.getDisplayName() + ".");
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(target.getUsername()) + " was given a black mark by " + player.getDisplayName() + ".", true, false); 
				target.getPackets().sendGameMessage("You've been given a black mark by " + player.getDisplayName() + ".");
			}
			if (target.getStatistics().getBlackMarks() == 8) {
				target.setBanned(Utils.currentTimeMillis() + (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
				target.getSession().getChannel().close();
			}
			if (target.getStatistics().getBlackMarks()  == 4) {
				target.setJailed(Utils.currentTimeMillis() + (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
                target.getControllerManager().startController("JailControler");
			}
			if (target.getStatistics().getBlackMarks()  == 6) {
				target.setMuted(Utils.currentTimeMillis() + (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
			}
			return true;
			
		case "setdonated":
			if (cmd.length < 3) {
				return true;
			}
			name = "";
			for (int i = 2; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			assert target != null;
			target.getDonationManager().setDonatedAmount(Integer.valueOf(cmd[1]));
			player.getPackets().sendGameMessage("You have set " + target.getDisplayName() +"'s total donated amount as " + target.getDonationManager().getTotalDonated() + " dollars.");
			return true;

		case "getdonated":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			assert target != null;
			player.getPackets().sendGameMessage("Amount: " + target.getDonationManager().getTotalDonated());
			return true;
			
		case "overlay":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				return true;
			}
			int child = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 28;
			try {
				player.getPackets().sendInterface(true, 746, child, Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
			}
			return true; 

		case "interh":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				return true;
			}

			try {
				int interId = Integer.valueOf(cmd[1]);
				for (int componentId = 0; componentId < Utils.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
					player.getPackets().sendIComponentModel(interId, 	componentId, 66);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
			}
			return true;

		case "inters":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				return true;
			}

			try {
				int interId = Integer.valueOf(cmd[1]);
				for (int componentId = 0; componentId < Utils.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
					player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
			}
			return true;

		case "kill":
			if (player.getRights() >= 2) {
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				return true;
			target.applyHit(new Hit(target, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
			target.stopAll();
			}
			return true;
			
		case "forcekill":
			if (player.getUsername().equalsIgnoreCase("BigFuckinChungus")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage("The player " + name + " does not exist.");
					return true;
				}
				target.applyHit(new Hit(target, target.getMaxHitpoints(), HitLook.DESEASE_DAMAGE));
				target.sendDeath(target);
				player.getPackets().sendGameMessage("You have killed " + target.getDisplayName());
			} else {
				player.getPackets().sendGameMessage("You must have the rights to use this command.");
				return false;
			}
			return true;
			
		case "makesupport":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			boolean loggedIn = true;
			if (target == null) {
				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
				if (target != null)
					target.setUsername(Utils.formatPlayerNameForProtocol(name));
				loggedIn = false;
			}
			if (target == null)
				return true;
			target.setSupporter(true);
			SerializableFilesManager.savePlayer(target);
			if (loggedIn)
				target.getPackets().sendGameMessage("You have been given supporter rank by " + Utils.formatPlayerNameForDisplay(player.getUsername()), true);
			player.getPackets().sendGameMessage("You gave supporter rank to " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
			return true; 
			
		case "takesupport":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			boolean loggedIn2 = true;
			if (target == null) {
				target = SerializableFilesManager.loadPlayer(Utils
						.formatPlayerNameForProtocol(name));
				if (target != null)
					target.setUsername(Utils
							.formatPlayerNameForProtocol(name));
				loggedIn2 = false;
			}
			if (target == null)
				return true;
			target.setSupporter(false);
			SerializableFilesManager.savePlayer(target);
			if (loggedIn2)
				target.getPackets().sendGameMessage(
						"Your supporter rank was removed by "
								+ Utils.formatPlayerNameForDisplay(player
										.getUsername()), true);
			player.getPackets().sendGameMessage(
					"You removed supporter rank of "
							+ Utils.formatPlayerNameForDisplay(target
									.getUsername()), true);
			return true;
			
		case "makegfx":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			boolean loggedIn11 = true;
			if (target == null) {
				target = SerializableFilesManager.loadPlayer(Utils
						.formatPlayerNameForProtocol(name));
				if (target != null)
					target.setUsername(Utils
							.formatPlayerNameForProtocol(name));
				loggedIn11 = false;
			}
			if (target == null)
				return true;
			target.setGraphicDesigner(true);
			SerializableFilesManager.savePlayer(target);
			if (loggedIn11)
				target.getPackets().sendGameMessage(
						"You have been given graphic designer rank by "
								+ Utils.formatPlayerNameForDisplay(player
										.getUsername()), true);
			player.getPackets().sendGameMessage(
					"You gave graphic designer rank to "
							+ Utils.formatPlayerNameForDisplay(target
									.getUsername()), true);
			return true; 
			
		case "takegfx":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			boolean loggedIn21 = true;
			if (target == null) {
				target = SerializableFilesManager.loadPlayer(Utils
						.formatPlayerNameForProtocol(name));
				if (target != null)
					target.setUsername(Utils
							.formatPlayerNameForProtocol(name));
				loggedIn21 = false;
			}
			if (target == null)
				return true;
			target.setGraphicDesigner(false);
			SerializableFilesManager.savePlayer(target);
			if (loggedIn21)
				target.getPackets().sendGameMessage("Your graphic designer rank was removed by " + Utils.formatPlayerNameForDisplay(player.getUsername()), true);
			player.getPackets().sendGameMessage("You removed graphic designer rank of " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
			return true;

		case "bank":
			player.stopAll();
			player.getBank().openBank();
			return true;
			
		case "ironclan":
			player.sm(player.getClanManager().getClan().isIronOnly() ? "Yes" : "No");
			return true;
			
		case "switchiron":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null) {
				return true;
			}
			target.switchIronman();
			player.sm(target.isAnIronMan() ? "You are now an ironman" : "You are no longer an ironman");
			return true;
			
		case "bankid":
			player.setBankId(Integer.valueOf(cmd[1]));
			player.sm("" + player.getBankId());
			return true;

		case "check":
			IPBanL.checkCurrent();
			return true; 

		case "reloadfiles":
			IPBanL.init();
			PkRank.init();
			return true; 

		case "update":
			int delay = 60;
			if (cmd.length >= 2) {
				try {
					delay = Integer.valueOf(cmd[1]);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::restart secondsDelay(IntegerValue)");
					return true;
				}
			}
			World.safeShutdown(false, delay);
			return true;
			
		case "emote":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				return true;
			}
			try {
				player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
			}
			return true; 

		case "remote":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				return true;
			}
			try {
				player.getAppearence().setRenderEmote(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
			}
			return true; 

		case "quake":
			player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]),
					Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]),
					Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
			return true; 

		case "getrender":
			player.getPackets().sendGameMessage("Testing renders");
			for (int i = 0; i < 3000; i++) {
				try {
					player.getAppearence().setRenderEmote(i);
					player.getPackets().sendGameMessage("Testing " + i);
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return true; 

		case "spec":
			player.getCombatDefinitions().resetSpecialAttack();
			return true;

		case "trylook":
			final int look = Integer.parseInt(cmd[1]);
			WorldTasksManager.schedule(new WorldTask() {
				int i = 269;// 200

				@Override
				public void run() {
					if (player.hasFinished()) {
						stop();
					}
					player.getAppearence().setBodyStyle(look, i);
					player.getAppearence().generateAppearenceData();
					player.getPackets().sendGameMessage("Look " + i + ".");
					i++;
				}
			}, 0, 1);
			return true; 

		case "tryinter":
			WorldTasksManager.schedule(new WorldTask() {
				int i = 1;

				@Override
				public void run() {
					if (player.hasFinished()) {
						stop();
					}
					player.getInterfaceManager().sendInterface(i);
					System.out.println("Inter - " + i);
					i++;
				}
			}, 0, 1);
			return true; 

		case "tryanim":
			WorldTasksManager.schedule(new WorldTask() {
				int i = 16700;

				@Override
				public void run() {
					if (i >= Utils.getAnimationDefinitionsSize()) {
						stop();
						return;
					}
					if (player.getLastAnimationEnd() > System
							.currentTimeMillis()) {
						player.setNextAnimation(new Animation(-1));
					}
					if (player.hasFinished()) {
						stop();
					}
					player.setNextAnimation(new Animation(i));
					System.out.println("Anim - " + i);
					i++;
				}
			}, 0, 3);
			return true;

		case "animcount":
			System.out.println(Utils.getAnimationDefinitionsSize() + " animations");
			return true;
			
		case "intercount":
			System.out.println(Utils.getInterfaceDefinitionsSize() + " interfaces");
			return true;

		case "trygfx":
			WorldTasksManager.schedule(new WorldTask() {
				int i = 1500;

				@Override
				public void run() {
					if (i >= Utils.getGraphicDefinitionsSize()) {
						stop();
					}
					if (player.hasFinished()) {
						stop();
					}
					player.setNextGraphics(new Graphics(i));
					System.out.println("GFX - " + i);
					i++;
				}
			}, 0, 3);
			return true; 

		case "gfx":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				return true;
			}
			try {
				player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
			}
			return true; 
			
		case "sync":
			int animId = Integer.parseInt(cmd[1]);
			int gfxId = Integer.parseInt(cmd[2]);
			int height = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
			player.setNextAnimation(new Animation(animId));
			player.setNextGraphics(new Graphics(gfxId, 0, height));
			return true;

		case "mess":
			player.getPackets().sendMessage(Integer.valueOf(cmd[1]), "", player);
			return true; 

		case "demoteoyun":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			loggedIn = true;
			if (target == null) {
				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
				if (target != null)
					target.setUsername(Utils.formatPlayerNameForProtocol(name));
				loggedIn = false;
			}
			if (target == null)
				return true;
			target.setRights(0);
			SerializableFilesManager.savePlayer(target);
			if (loggedIn)
				target.getPackets().sendGameMessage("You have been demoted by " + Utils.formatPlayerNameForDisplay(player.getUsername()), true);
			player.getPackets().sendGameMessage("You demoted " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
			return true;
			
		case "setpitswinner":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
			if (target != null) {
				target.setWonFightPits();
				target.setCompletedFightCaves();
			} else {
				player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
			}
			SerializableFilesManager.savePlayer(target);
			return true;
		}
		return false;
	}

}