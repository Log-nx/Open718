package com.rs.game.player.content.commands;

import org.javacord.api.entity.message.MessageBuilder;

import com.everythingrs.donate.Donation;
import com.rs.GameServer;
import com.rs.Settings;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.minigames.hunger.HungerGamesControler;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.content.DonatorZone;
import com.rs.game.player.content.TicketSystem;
import com.rs.game.player.content.activities.events.WorldEvents;
import com.rs.game.player.content.activities.skillingtask.SkillerTasks;
import com.rs.game.player.content.custom.ItemSearch;
import com.rs.game.player.content.interfaces.TopWealth;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.content.trivia.TriviaBot;
import com.rs.game.player.controllers.FightCaves;
import com.rs.game.player.controllers.FightKiln;
import com.rs.game.player.controllers.PestInvasion;
import com.rs.game.player.controllers.instances.ArmadylInstance;
import com.rs.game.player.controllers.instances.BandosInstance;
import com.rs.game.player.controllers.instances.BlinkInstance;
import com.rs.game.player.controllers.instances.CorpInstance;
import com.rs.game.player.controllers.instances.SaradominInstance;
import com.rs.game.player.controllers.instances.ZamorakInstance;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.managers.CompletionistCapeManager.Requirement;
import com.rs.game.player.managers.WealthManager;
import com.rs.utils.Color;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class PlayerCommands {

	static boolean processCommand(final Player player, String[] cmd, boolean console, boolean clientCommand) {
		String name;
		String message;
		switch (cmd[0].toLowerCase()) {
		case "compcape":
		case "reqs":
			player.getCompCapeManager().sendInterface();
			return true;
		case "cosmetics":
			player.getDialogueManager().startDialogue("CosmeticsManagerD");
			return true;
		case "savecurrentcostume":
		case "savecurrentcosmetic":
			player.getTemporaryAttributtes().put("SaveCosmetic", Boolean.TRUE);
			player.getPackets().sendInputNameScript("Enter the name you want for your current costume: ");
			return true;
		case "events":
			WorldEvents.sendInterface(player);
			return true;
		case "setyellcolor":
		case "changeyellcolor":
		case "yellcolor":
			if (!player.getDonationManager().isExtremeDonator() && player.getRights() == 0) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You've to be a extreme donator to use this feature.");
				return true;
			}
			player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
			player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
			return true;

		case "divination":
			player.getPackets().sendGameMessage(Color.SILVER, "You have a total of " + Utils.formatNumber(player.getSkills().getXp(Skills.DIVINATION)) + " divination xp!");
			return true;

		case "invention":
			player.getPackets().sendGameMessage(Color.SILVER, "You have a total of " + Utils.formatNumber(player.getSkills().getXp(Skills.INVENTION)) + " invention xp!");
			return true;

		case "sheathe":
			player.getCombatDefinitions().switchSheathe();
			return true;

		case "topwealth":
			TopWealth.displayTopWealth(player);
			return true;

		case "dz":
		case "donatorzone":
			if (player.getDonationManager().isDonator()) {
				DonatorZone.enterDonatorzone(player);
			}
			return true;
		case "recanswer":
			if (player.getRecovQuestion() == null) {
				player.getPackets().sendGameMessage("Please set your recovery question first.");
				return true;
			}
			if (player.getRecovAnswer() != null && player.getRights() < 2) {
				player.getPackets().sendGameMessage("You can only set recovery answer once.");
				return true;
			}
			message = "";
			for (int i = 1; i < cmd.length; i++)
				message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			player.setRecovAnswer(message);
			player.getPackets().sendGameMessage(
					"Your recovery answer has been set to - " + Utils.fixChatMessage(player.getRecovAnswer()));
			return true;

		case "recquestion":
			if (player.getRecovQuestion() != null && player.getRights() < 2) {
				player.getPackets().sendGameMessage("You already have a recovery question set.");
				return true;
			}
			message = "";
			for (int i = 1; i < cmd.length; i++)
				message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			player.setRecovQuestion(message);
			player.getPackets().sendGameMessage(
					"Your recovery question has been set to - " + Utils.fixChatMessage(player.getRecovQuestion()));
			return true;

		case "empty":
			player.getDialogueManager().startDialogue("EmptyD");
			return true;

		case "ticket":
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
				return true;
			}
			TicketSystem.requestTicket(player);
			return true;

		case "setpin":
			if (cmd.length < 2) {
				player.getPackets().sendGameMessage("To set yourself a PIN, do the following ::Setpin (digits).");
				return true;
			}
			try {
				player.hasPin = true;
				player.setPin(Integer.valueOf(cmd[1]));
				player.getPackets().sendGameMessage("You have now entered your pin to your bank, and the code is as follows: " + player.getPin() + ".");
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage("An error appeared, please try again.");
			}
			return true;

		case "rfd":
			if (player.canSpawn() || player.getTeleBlockDelay() > Utils.currentTimeMillis() || player.isDead()
					|| player.isLocked()
					|| player.getControllerManager().getController() instanceof HungerGamesControler
					|| player.getControllerManager().getController() instanceof FightCaves
					|| player.getControllerManager().getController() instanceof FightKiln
					|| player.getControllerManager().getController() instanceof PestInvasion) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(1866, 5322, 0));
				Player.removeControler(player);
				player.getInterfaceManager().closeOverlay(false);
				return true;
			}
			player.getPackets().sendGameMessage("You are unable to teleport at this time.");
			return true;

		case "resetzoom":
			player.zoom = 226;
			player.getPackets().sendGlobalConfig(184, 0);
			return true;
			
		case "sof":
			player.getTemporaryAttributtes().put("SOF", Boolean.TRUE);
			player.getPackets().sendInputIntegerScript("How many spins would you like to spend? Spins: " + Utils.formatNumber(player.getSquealOfFortune().getTotalSpins()));
			return true;

		case "home":
			if (player.getTeleBlockDelay() > Utils.currentTimeMillis() || player.isDead() || player.isLocked()
					|| player.getControllerManager().getController() instanceof FightCaves
					|| player.getControllerManager().getController() instanceof ArmadylInstance
					|| player.getControllerManager().getController() instanceof BandosInstance
					|| player.getControllerManager().getController() instanceof SaradominInstance
					|| player.getControllerManager().getController() instanceof ZamorakInstance
					|| player.getControllerManager().getController() instanceof BlinkInstance
					|| player.getControllerManager().getController() instanceof CorpInstance
					|| player.getControllerManager().getController() instanceof FightKiln
					|| player.getControllerManager().getController() instanceof PestInvasion) {
				player.getPackets().sendGameMessage("You are unable to teleport at this time.");
			} else if (player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().getTutorialStage() < 26) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 945, "I'm sorry, you cannot leave yet.");
			} else if (player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().getTutorialStage() == 26) {
				DiccusTutorial.finishTutorial(player);
			} else
				Magic.sendCustomTeleport(player, player.getPlayerHomeLocation());
			player.getControllerManager().forceStop();
			player.getInterfaceManager().closeOverlay(false);
			return true;

		case "toggledrops":
			player.setToggleDrops(!player.isToggleDrops());
			player.out("You have " + (player.isToggleDrops() ? "enabled" : "disabled")
					+ " right click examine to view NPC's drops.");
			return true;

		case "players":
			if (World.getPlayers().size() < 280) {
				player.getInterfaceManager().sendInterface(275);
				int number = 0;
				for (int i = 0; i < 310; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}
				for (Player p5 : World.getPlayers()) {
					if (p5 == null)
						continue;
					number++;
					String titles = "";
					if (!(p5.getDonationManager().isDonator()) && p5.getRights() == 0) {
						titles = "Player";
					}
					if (p5.getDonationManager().isDonator()) {
						titles = "<img=17>Donator "; // <col=109a00>
					}
					if (p5.getDonationManager().isExtremeDonator()) {
						titles = "<img=24>Extreme "; // <col=FF0000>
					}
					if (p5.getDonationManager().isLegendaryDonator()) {
						titles = "<img=18>Legendary "; // <col=FF0000>
					}
					if (p5.getDonationManager().isHeroicDonator()) {
						titles = "<img=25>Heroic "; // <col=FF0000>
					}
					if (p5.getDonationManager().isImmortalDonator()) {
						titles = "<img=19>Immortal "; // <col=FF0000>
					}
					if (p5.getDonationManager().isDivineDonator()) {
						titles = "<img=9>Immortal "; // <col=FF0000>
					}
					if (p5.isIronman()) {
						if (player.getAppearence().isMale())
							titles = "<img=26>Ironman";
						else
							titles = "<img=26>Ironman";
					}
					if (p5.getRights() == 1) {
						titles = "<img=0>Mod"; // <col=07d8bc>
					}
					if (p5.getRights() == 2) {
						titles = "<img=1>Admin"; // <col=FFCC00>
					}
					if (p5.isSupporter()) {
						titles = "<img=12>Support "; // <col=58ACFA><shad=2E2EFE>
					}
					if (p5.isForumModerator()) {
						titles = "<img=11>Forum Manager"; // <col=58ACFA><shad=2E2EFE>
					}
					if (p5.isCommunityManager()) {
						titles = "<img=1>Community Manager "; // <col=FF0000>
					}
					if (p5.isGraphicDesigner()) {
						titles = "<img=14>Graphic Designer "; // <col=FF0000>
					}
					if (p5.getRights() == 2 && p5.getDisplayName().equalsIgnoreCase("")
							&& p5.getDisplayName().equalsIgnoreCase("") && p5.getAppearence().isHidden() == true) {
						player.getPackets().sendIComponentText(275, (13 + number), " ");
					} else {
						String gameMode = "";
						if (p5.gameMode == 3) {
							gameMode = "Extreme";
						} else if (p5.gameMode == 2) {
							gameMode = "Legend";
						} else if (p5.gameMode == 1) {
							gameMode = "Knight";
						} else if (p5.gameMode == 0) {
							gameMode = "Squire";
						}
						player.getPackets().sendIComponentText(275, +13 + number,
								"(" + p5.getIndex() + ") " + p5.getDisplayName() + " - " + titles + "  - " + gameMode
										+ " Mode - " + p5.getSkills().getCombatLevelWithSummoning() + " Combat");
					}
				}
				player.getPackets().sendIComponentText(275, 1, "Players Online");
				player.getPackets().sendIComponentText(275, 10, "");
				player.getPackets().sendIComponentText(275, 12, "There are currently " + number + " players online!");
				player.getPackets().sendIComponentText(275, 11, "");
			}
			player.getPackets().sendGameMessage("There are currently " + World.getPlayers().size() + " players playing "
					+ Settings.SERVER_NAME + ".");
			return true;

		case "dungtokens":
			player.setNextForceTalk(new ForceTalk("<col=ffffff>I have [</col><col=358987>"
					+ player.getStatistics().getDungeoneeringTokens() + "</col><col=ffffff>] Dungeoneering tokens."));
			return true;
			
		case "rc":
		case "runecoin":
		case "runecoins":
			player.getPackets().sendGameMessage("Rune Coins: " + Colors.GREEN + Utils.formatNumber(player.getStatistics().getRuneCoins()));
			return true;

		case "bank":
			if (!player.getDonationManager().isExtremeDonator() || !player.isAdministrator()) {
				player.getPackets().sendGameMessage("You do not have the privileges to use this.");
				return true;
			}
			if (player.getControllerManager().getController() instanceof FightCaves
					|| player.getControllerManager().getController() instanceof ArmadylInstance
					|| player.getControllerManager().getController() instanceof BandosInstance
					|| player.getControllerManager().getController() instanceof SaradominInstance
					|| player.getControllerManager().getController() instanceof ZamorakInstance
					|| player.getControllerManager().getController() instanceof BlinkInstance
					|| player.getControllerManager().getController() instanceof CorpInstance
					|| player.getControllerManager().getController() instanceof FightKiln
					|| player.getControllerManager().getController() instanceof PestInvasion) {
				player.getPackets().sendGameMessage("You may not open the bank here.");
				return true;
			}
			player.stopAll();
			player.getBank().openBank();
			return true;

		case "title":
			if (!player.getDonationManager().isDonator()) {
				player.getPackets().sendGameMessage("You do not have the privileges to use this.");
				return true;
			}
			try {
				if (Integer.valueOf(cmd[1]) > 56) {
					player.out("You can only use titles: 1-56.");
				} else {
					player.getAppearence().setTitle(Integer.valueOf(cmd[1]));
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage("Use: ::title id");
			}
			return true;

		case "claimvote":
		case "reward":
			final String playerName = player.getUsername();
			final String id = cmd[1];
			final String rewardAmount = "1";
			com.everythingrs.vote.Vote.service.execute(new Runnable() {
				@Override
				public void run() {
					try {
						com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward(
								"a6858q9p8jkzclhz0mypkfbt9zm3jpbshqlfyjy5kchni2j4ishjfn76s4a9vpyhi9jocrf6r", playerName,
								id, rewardAmount);
						if (reward[0].message != null) {
							player.getPackets().sendGameMessage(reward[0].message);
							return;
						}
						player.getInventory().addItem(new Item(reward[0].reward_id, reward[0].give_amount));
						player.getPackets().sendGameMessage("Thank you for your support in voting! You have "
								+ reward[0].vote_points + " vote points left.");
						player.getCompCapeManager().increaseRequirement(Requirement.VOTES, 1);
					} catch (Exception e) {
						player.getPackets()
								.sendGameMessage("Voting services are currently offline, please check back shortly.");
						e.printStackTrace();
					}
				}

			});
			return true;

		case "taskinfo":
			SkillerTasks.sendTaskInfo(player);
			return true;

		case "searchitem":
			String itemName = "";
			for (int i = 1; i < cmd.length; i++) {
				itemName += cmd[i] + (i == cmd.length - 1 ? "" : " ");
			}
			ItemSearch.searchForItem(player, itemName);
			return true;

		case "wealth":
			WealthManager.sendInterface(player);
			return true;

		case "donated":
			name = player.getUsername();
			new java.lang.Thread() {
				public void run() {
					try {
						Donation[] donations = Donation.donations(
								"a6858q9p8jkzclhz0mypkfbt9zm3jpbshqlfyjy5kchni2j4ishjfn76s4a9vpyhi9jocrf6r", name);
						if (donations.length == 0) {
							player.getPackets().sendGameMessage(
									"You currently don't have any items waiting. You must donate first!");
							return;
						}
						if (donations[0].message != null) {
							player.getPackets().sendGameMessage(donations[0].message);
							return;
						}
						for (com.everythingrs.donate.Donation donate : donations) {
							if (donate.product_id == 1) {
								player.getPerkManager().unlockPerk(PlayerPerks.CHARGE_BEFRIENDER);
								player.getDonationManager().increaseDonationAmount(25);
							}
							if (donate.product_id == 2) {
								player.getPerkManager().unlockPerk(PlayerPerks.GODWARS2_SPECIALIST);
								player.getDonationManager().increaseDonationAmount(25);
							}
							if (donate.product_id == 3) {
								player.getPerkManager().unlockPerk(PlayerPerks.INVESTIGATOR);
								player.getDonationManager().increaseDonationAmount(15);
							}
							if (donate.product_id == 4) {
								player.getPerkManager().unlockPerk(PlayerPerks.GODWARS_SPECIALIST);
								player.getDonationManager().increaseDonationAmount(15);
							}
							if (donate.product_id == 5) {
								player.getPerkManager().unlockPerk(PlayerPerks.SNEAK_FACTOR);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 6) {
								player.getPerkManager().unlockPerk(PlayerPerks.THE_NINTH_BROTHER);
								player.getDonationManager().increaseDonationAmount(10);
							}
							if (donate.product_id == 7) {
								player.getPerkManager().unlockPerk(PlayerPerks.OVERCLOCKED);
								player.getDonationManager().increaseDonationAmount(10);
							}
							if (donate.product_id == 8) {
								player.getPerkManager().unlockPerk(PlayerPerks.SOUL_SIPHONER);
								player.getDonationManager().increaseDonationAmount(15);
							}
							if (donate.product_id == 9) {
								player.getPerkManager().unlockPerk(PlayerPerks.PRAYER_BETRAYER);
								player.getDonationManager().increaseDonationAmount(10);
							}
							if (donate.product_id == 10) {
								player.getPerkManager().unlockPerk(PlayerPerks.GREEN_THUMB);
								player.getDonationManager().increaseDonationAmount(3);
							}
							if (donate.product_id == 11) {
								player.getPerkManager().unlockPerk(PlayerPerks.HERB_LAW);
								player.getDonationManager().increaseDonationAmount(3);
							}
							if (donate.product_id == 12) {
								player.getPerkManager().unlockPerk(PlayerPerks.MASTER_FISHERMAN);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 13) {
								player.getPerkManager().unlockPerk(PlayerPerks.MASTER_HUNTSMAN);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 14) {
								player.getPerkManager().unlockPerk(PlayerPerks.QUARRY_MASTER);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 15) {
								player.getPerkManager().unlockPerk(PlayerPerks.MASTER_DIVINER);
								player.getDonationManager().increaseDonationAmount(10);
							}
							if (donate.product_id == 16) {
								player.getPerkManager().unlockPerk(PlayerPerks.CERTIFIED_CHEF);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 17) {
								player.getPerkManager().unlockPerk(PlayerPerks.FAMILIAR_MASTER);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 18) {
								player.getPerkManager().unlockPerk(PlayerPerks.CRANKED);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 19) {
								player.getPerkManager().unlockPerk(PlayerPerks.DECAY_TOTEM);
								player.getDonationManager().increaseDonationAmount(15);
							}
							if (donate.product_id == 20) {
								player.getPerkManager().unlockPerk(PlayerPerks.KEY_EXPERT);
								player.getDonationManager().increaseDonationAmount(5);
							}
							if (donate.product_id == 21) {
								player.getBank().addItem(ItemIdentifiers.BOND_UNTRADEABLE, donate.product_amount, true);
								player.getDonationManager().increaseDonationAmount(5);
							} else
								player.getInventory().addItem(donate.product_id, donate.product_amount);
						}
						player.getPackets().sendGameMessage(Color.GREEN, "Thank you for donating!");
					} catch (Exception e) {
						player.getPackets().sendGameMessage(Color.RED, "Donation Services are currently offline. Please check back shortly");
						e.printStackTrace();
					}
				}
			}.start();
			return true;

		case "vote":
			player.getPackets().sendOpenURL(Settings.VOTE_LINK);
			return true;

		case "donate":
		case "donates":
			player.getPackets().sendOpenURL(Settings.DONATE_LINK);
			return true;

		case "forums":
			player.getPackets().sendOpenURL(Settings.FORUMS_LINK);
			return true;

		case "rules":
			player.getPackets().sendOpenURL(Settings.RULES_LINK);
			return true;

		case "guides":
			player.getPackets().sendOpenURL(Settings.GUIDES_LINK);
			return true;

		case "hs":
		case "hiscore":
		case "highscore":
			player.getPackets().sendOpenURL(Settings.HIGHSCORES_LINK);
			return true;

		case "mode":
			if (player.isSquire()) {
				player.setNextForceTalk(new ForceTalk("<col=ffffff>I am playing on the Game-Mode [</col><col=358987>Squire</col><col=ffffff>]."));
			} else if (player.isVeteran()) {
				player.setNextForceTalk(new ForceTalk("<col=ffffff>I am playing on the Game-Mode [</col><col=358987>Knight</col><col=ffffff>]."));
			} else if (player.isVeteran()) {
				player.setNextForceTalk(new ForceTalk("<col=ffffff>I am playing on the Game-Mode [</col><col=358987>Legend</col><col=ffffff>]."));
			}
			return true;

		case "lockxp":
			player.setXpLocked(player.isXpLocked() ? false : true);
			player.getPackets().sendGameMessage("You have " + (player.isXpLocked() ? "UNLOCKED" : "LOCKED") + " your xp.");
			return true;

		case "hideyell":
			player.setYellOff(!player.isYellOff());
			player.getPackets().sendGameMessage("You have turned " + (player.isYellOff() ? "off" : "on") + " yell.");
			return true;

		case "changepass":
			message = "";
			for (int i = 1; i < cmd.length; i++)
				message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			if (message.length() > 15 || message.length() < 5) {
				player.getPackets().sendGameMessage("You cannot set your password to over 15 chars.");
				return true;
			}
			player.getPackets().sendGameMessage("You changed your password! Your password is " + cmd[1] + ".");
			player.setPassword(message);
			return true;

		case "yell":
			message = "";
			for (int i = 1; i < cmd.length; i++)
				message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			DeveloperConsole.sendYell(player, Utils.fixChatMessage(message), false);
			if (Settings.DISCORD) {
				new MessageBuilder().append(player.getDisplayName() + ":  " + Utils.fixChatMessage(message))
						.send(GameServer.getDiscordBot().getAPI().getTextChannelById("510261180573417493").get());
			}
			return true;

		case "commanager":
			if (player.isCommunityManager) {
				player.getAppearence().setTitle(9876);
			}
			return true;

		case "answer":
			if (cmd.length >= 2) {
				String answer = cmd[1];
				if (cmd.length == 3) {
					answer = cmd[1] + " " + cmd[2];
				}
				TriviaBot.verifyAnswer(player, answer);
			} else {
				player.getPackets().sendGameMessage("Syntax is ::" + cmd[0] + " <answer input>.");
			}
			return true;

		case "removepin":
			if (player.hasPin) {
				player.getPackets().sendRunScript(108, new Object[] { "Please Enter Your PIN" });
				player.getTemporaryAttributtes().put("Remove_pin", Boolean.TRUE);
			} else {
				player.getPackets().sendGameMessage("You can't do this, you do not have a PIN.");
			}
			return true;
		}
		return clientCommand;
	}
}