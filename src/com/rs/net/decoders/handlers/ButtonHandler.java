package com.rs.net.decoders.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.item.WeightManager;
import com.rs.game.minigames.Crucible;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.minigames.pest.CommendationExchange;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.others.GraveStone;
import com.rs.game.player.Bank;
import com.rs.game.player.Inventory;
import com.rs.game.player.KeptOnDeath;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.actions.FightPitsViewingOrb;
import com.rs.game.player.actions.HomeTeleport;
import com.rs.game.player.actions.Rest;
import com.rs.game.player.actions.combat.PlayerCombat;
import com.rs.game.player.actions.construction.House;
import com.rs.game.player.actions.magic.EnchantBolt;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.actions.runecrafting.Runecrafting;
import com.rs.game.player.actions.slayer.SlayerTask;
import com.rs.game.player.actions.smithing.Smithing.ForgingInterface;
import com.rs.game.player.actions.summoning.Summoning;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.AdventurersLog;
import com.rs.game.player.content.FairyRing;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.LoyaltyStore;
import com.rs.game.player.content.PlayerDesign;
import com.rs.game.player.content.PlayerLook;
import com.rs.game.player.content.Shop;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.SpiritTree;
import com.rs.game.player.content.SummoningScroll;
import com.rs.game.player.content.TicketSystem;
import com.rs.game.player.content.Wildstalker;
import com.rs.game.player.content.achievements.Achievement;
import com.rs.game.player.content.achievements.AgilityLocations;
import com.rs.game.player.content.achievements.GnomeAgilityAchievements;
import com.rs.game.player.content.clans.Clan;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.dungeoneering.DungeonRewardShop;
import com.rs.game.player.content.interfaces.AccountManager;
import com.rs.game.player.content.interfaces.NewsDashboard;
import com.rs.game.player.content.interfaces.QuestTab;
import com.rs.game.player.content.interfaces.SophanemChestInterface;
import com.rs.game.player.content.interfaces.ViewNPCDrops;
import com.rs.game.player.content.items.ArmourSets;
import com.rs.game.player.content.items.sof.MaskOfMourning;
import com.rs.game.player.content.teleports.TeleportSystem;
import com.rs.game.player.controllers.FightCaves;
import com.rs.game.player.controllers.FightKiln;
import com.rs.game.player.controllers.ImpossibleJad;
import com.rs.game.player.controllers.PestInvasion;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.impl.LevelUp;
import com.rs.game.player.dialogues.impl.Transportation;
import com.rs.game.player.dialogues.impl.quests.CooksAssistant;
import com.rs.game.player.dialogues.impl.quests.DoricsQuest;
import com.rs.game.player.dialogues.impl.quests.ImpCatcherQuest;
import com.rs.game.player.managers.EmotesManager;
import com.rs.game.player.managers.InterfaceManager;
import com.rs.game.player.managers.PriceCheckManager;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class ButtonHandler {

	public static void handleButtons(final Player player, InputStream stream, int packetId)
			throws ClassNotFoundException, IOException {

		int interfaceHash = stream.readIntV2();
		int interfaceId = interfaceHash >> 16;
		if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
			return;
		}
		if (!World.containsLobbyPlayer(player.getUsername())) {
			if (player.isDead() || !player.getInterfaceManager().containsInterface(interfaceId)) {
				return;
			}
		}
		final int componentId = interfaceHash - (interfaceId << 16);
		if (componentId != 65535 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
			return;
		}
		final int slotId2 = stream.readUnsignedShort128();
		final int slotId = stream.readUnsignedShortLE128();
		final int itemId = stream.readInt();
		if (!player.getControllerManager().processButtonClick(interfaceId, componentId, slotId, packetId)) {
			return;
		}
		if (interfaceId == 1253 || interfaceId == 1252 || interfaceId == 1139) {
			player.getSquealOfFortune().processClick(packetId, interfaceId, componentId, slotId);
		}
		if (interfaceId == 644 || interfaceId == 645) {
			ArmourSets.handleButtons(player, interfaceId, slotId, slotId2, packetId);
			return;
		}
		if (interfaceId == 1082 && player.getTemporaryAttributtes().get("CosmeticsStore") != null) {
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
			return;
		}
		if (interfaceId == 34) {
			switch (componentId) {
			case 35:
			case 37:
			case 39:
			case 41:
				player.getNotes().colour((componentId - 35) / 2);
				player.getPackets().sendHideIComponent(34, 16, true);
				break;
			case 3:
				player.getPackets().sendInputLongTextScript("Add note:");
				player.getTemporaryAttributtes().put("entering_note", Boolean.TRUE);
				break;
			case 9:
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					if (player.getNotes().getCurrentNote() == slotId) {
						player.getNotes().removeCurrentNote();
					} else {
						player.getNotes().setCurrentNote(slotId);
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					player.getPackets().sendInputLongTextScript("Edit note:");
					player.getNotes().setCurrentNote(slotId);
					player.getTemporaryAttributtes().put("editing_note", Boolean.TRUE);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					player.getNotes().setCurrentNote(slotId);
					player.getPackets().sendHideIComponent(34, 16, false);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					player.getNotes().delete(slotId);
					break;
				}
				break;
			case 8:
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.getNotes().delete();
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					player.getNotes().deleteAll();
					break;
				}
				break;
			}
		}
		if (interfaceId == 72) {
			TeleportSystem.handleInterface(player, componentId);
			return;
		}
		if (interfaceId == DungeonRewardShop.REWARD_SHOP) {
			DungeonRewardShop.handleButtons(player, componentId, slotId, packetId);
			return;
		}
		if (interfaceId == 432) {
			EnchantBolt.handleEnchant(player, componentId);
			return;
		}
		if (SlayerTask.handleShop(player, interfaceId, componentId)) {
			return;
		}
		if (interfaceId == 205) {
			NewsDashboard.handleButtons(player, componentId);
			return;
		}
		if (AccountManager.handleAccount(player, interfaceId, componentId)) {
			return;
		}
		if (House.createRoom(player, interfaceId, componentId, slotId)) {
			return;
		}
		if (interfaceId == 105 || interfaceId == 107 || interfaceId == 109 || interfaceId == 449) {
			player.getGEManager().handleButtons(interfaceId, componentId, slotId, packetId);
		}
		if (interfaceId == 1310) {
			player.coOpSlayer.handleInviteButtons(player, interfaceId, componentId);
		}
		if (interfaceId == 1309) {
			player.coOpSlayer.handleCoOpSlayerInterface(player, componentId);
		}
		if (interfaceId == 1284) {
			SophanemChestInterface.onClick(player, componentId, packetId, itemId, slotId);
		}
		if (interfaceId == 1312 || interfaceId == 960 || interfaceId == 1263 || interfaceId == 668) {
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
			if (Settings.DEBUG) {
				Logger.log("Continue dialogue - interface: " + interfaceId + "; component: " + componentId + ".");
			}
			return;
		}
		/**
		 * Achievement System
		 */
		if (interfaceId == 825 && player.getTemporaryAttributtes().get("AchievementInterface") != null) {
			Achievement.handleInterface(player, componentId);
			return;
		}
		if (interfaceId == 1082) {
			player.getTitles().handleShop(componentId);
			return;
		}
		if (interfaceId == 825 && player.getTemporaryAttributtes().get("AgilityLocations") != null) {
			AgilityLocations.handleInterface(player, componentId);
			return;
		}
		if (interfaceId == 825 && player.getTemporaryAttributtes().get("GnomeAgilityAchievements") != null) {
			GnomeAgilityAchievements.handleInterface(player, componentId);
			return;
		}
		if (PriceCheckManager.handleInterface(player, interfaceId, componentId, slotId, packetId)) {
			return;
		}
		if (InterfaceManager.handleFrameInterface(player, interfaceId, componentId, packetId)) {
			return;
		}
		if (interfaceId == 1019) {
			if (componentId == 16) {
				player.getTemporaryAttributtes().put(Key.REPORT_BUG, Boolean.TRUE);
				player.getPackets().sendInputLongTextScript("Bug Description:");
			}
			if (componentId == 18)
				TicketSystem.requestTicket(player);
		} else if (interfaceId == 734) {
			if (componentId == 21) {
				FairyRing.confirmRingHash(player);
			} else {
				FairyRing.handleDialButtons(player, componentId);
			}
			return;
		}
		if (interfaceId == 735) {
			if (componentId >= 14 && componentId <= 14 + 64) {
				FairyRing.sendRingTeleport(player, componentId - 14);
			}
			return;
		}
		if (interfaceId == 17) {
			KeptOnDeath.handleButtons(player, null, interfaceId, componentId);
		}
		if (interfaceId == 506) {
			QuestTab.handleButtons(player, componentId);
		}
		if (interfaceId == 629 && (componentId == 68 || componentId == 67)) {
			player.closeInterfaces();
			return;
		}
		if (interfaceId == 1312 || interfaceId == 960 || interfaceId == 1263 || interfaceId == 668) {
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
		}
		if (interfaceId == 1011) {
			CommendationExchange.handleButtonOptions(player, componentId);
		}
		if (interfaceId == 182) {
			if (player.getInterfaceManager().containsInventoryInter())
				return;
			if (componentId == 6 || componentId == 13)
				if (!player.hasFinished())
					player.logout(componentId == 6);
		}
		if (interfaceId == 1165) {
		}
		if (interfaceId == 880 || interfaceId == 747 || interfaceId == 662) {
			Familiar.handleFamiliar(player, componentId, interfaceId);
		}
		if (interfaceId == 19) {
			if (componentId == 21) {
				player.getCosmeticManager().handleButton(componentId, packetId);
			}
		}
		if (interfaceId == 190 && componentId == 15 && slotId == 1) {
			if (player.startedCooksAssistant == false) {
				CooksAssistant.handleQuestStartInterface(player);
			}
			if (player.inProgressCooksAssistant == true) {
				CooksAssistant.handleProgressQuestInterface(player);
			}
			if (player.completedCooksAssistantQuest == true) {
				CooksAssistant.handleQuestCompletionTabInterface(player);
			}
		}
		if (interfaceId == 190 && componentId == 15 && slotId == 3) {
			if (!player.startedDoricsQuest) {
				DoricsQuest.handleQuestStartInterface(player);
			}
			if (player.inProgressDoricsQuest) {
				DoricsQuest.handleProgressQuestInterface(player);
			}
			if (player.completedDoricsQuest) {
				DoricsQuest.handleQuestCompletionTabInterface(player);
			}
		} else if (interfaceId == 190 && componentId == 15 && slotId == 7) {
			if (player.startedImpCatcher == false) {
				ImpCatcherQuest.handleQuestStartInterface(player);
			}
			if (player.inProgressImpCatcher == true) {
				ImpCatcherQuest.handleProgressQuestInterface(player);
			}
			if (player.completedImpCatcher == true) {
				ImpCatcherQuest.handleQuestCompletionTabInterface(player);
			}
		} else if (interfaceId == 309)
			PlayerLook.handleHairdresserSalonButtons(player, componentId, slotId);
		else if (interfaceId == 1089) {
			if (componentId == 30)
				player.getTemporaryAttributtes().put("clanflagselection", slotId);
		} else if (componentId == 26) {
			Integer flag = (Integer) player.getTemporaryAttributtes().remove("clanflagselection");
			player.stopAll();
			if (flag != null)
				ClansManager.setClanFlagInterface(player, flag);
		} else if (interfaceId == 398) {
			if (componentId == 19)
				player.getInterfaceManager().sendSettings();
			if (componentId == 21)
				player.getInterfaceManager().sendSettings();
			else if (componentId == 15 || componentId == 1)
				player.getHouse().setBuildMode(componentId == 15);
			else if (componentId == 27 || componentId == 28)
				player.getHouse().setArriveInPortal(componentId == 25);
			else if (componentId == 29)
				player.getHouse().expelGuests();
			else if (componentId == 31)
				House.leaveHouse(player);
		}
		if (interfaceId == 1096 || interfaceId == 1105 || interfaceId == 1110) {
			ClansManager.handleButtons(player, interfaceId, componentId, slotId);
		}
		if (interfaceId == 729) {
			PlayerLook.handleThessaliasMakeOverButtons(player, componentId, slotId);
		} else if (interfaceId == 187) {
			if (componentId == 1) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getMusicsManager().playAnotherMusic(slotId / 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getMusicsManager().sendHint(slotId / 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getMusicsManager().addToPlayList(slotId / 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getMusicsManager().removeFromPlayList(slotId / 2);
			} else if (componentId == 4)
				player.getMusicsManager().addPlayingMusicToPlayList();
			else if (componentId == 10)
				player.getMusicsManager().switchPlayListOn();
			else if (componentId == 11)
				player.getMusicsManager().clearPlayList();
			else if (componentId == 13)
				player.getMusicsManager().switchShuffleOn();
		} else if (interfaceId == 275) {
			if (componentId == 14) {
				// player.getPackets().sendOpenURL(Settings.WEBSITE_LINK);
			}
		} else if ((interfaceId == 590 && componentId == 8) || interfaceId == 464) {
			player.getEmotesManager()
					.useBookEmote(interfaceId == 464 ? componentId : EmotesManager.getId(slotId, packetId));
		} else if (interfaceId == 192) {
			if (componentId == 2)
				player.getCombatDefinitions().switchDefensiveCasting();
			else if (componentId == 7)
				player.getCombatDefinitions().switchShowCombatSpells();
			else if (componentId == 9)
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			else if (componentId == 11)
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			else if (componentId == 13)
				player.getCombatDefinitions().switchShowSkillSpells();
			else if (componentId >= 15 & componentId <= 17)
				player.getCombatDefinitions().setSortSpellBook(componentId - 15);
			else
				Magic.processNormalSpell(player, componentId, packetId);
		} else if (interfaceId == 334) {
			if (componentId == 22)
				player.closeInterfaces();
			else if (componentId == 21)
				player.getTrade().accept(false);
		} else if (interfaceId == 335) {
			if (componentId == 18)
				player.getTrade().accept(true);
			else if (componentId == 20)
				player.closeInterfaces();
			else if (componentId == 32) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().removeItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getTrade().removeItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getTrade().removeItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getTrade().removeItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("trade_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("trade_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getTrade().sendValue(slotId, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getTrade().sendExamine(slotId, false);
			} else if (componentId == 35) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().sendValue(slotId, true);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getTrade().sendExamine(slotId, true);
			}
		} else if (interfaceId == 336) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().addItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getTrade().addItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getTrade().addItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getTrade().addItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("trade_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("trade_isRemove");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == 90) {
					player.getTrade().lendItem(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getTrade().sendValue(slotId);
				} else if (packetId == 90) {
					// Lend
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 300) {
			ForgingInterface.handleIComponents(player, componentId);
		} else if (interfaceId == 206) {
			if (componentId == 15) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getPriceCheckManager().removeItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("pc_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				}
			}
		} else if (interfaceId == 1028) {
			PlayerDesign.handle(player, componentId, slotId);
		} else if (interfaceId == 666) {
			if (componentId == 16) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					SummoningScroll.createScroll(player, slotId2, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					SummoningScroll.createScroll(player, slotId2, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					SummoningScroll.createScroll(player, slotId2, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					SummoningScroll.createScroll(player, slotId2, 28);
				}
			} else if (componentId == 18 && packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				Summoning.sendInterface(player);
			}
		} else if (interfaceId == 672) {
			if (componentId == 19) {
				SummoningScroll.sendInterface(player);
			}
			if (componentId == 16) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					Summoning.createPouch(player, slotId2, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					Summoning.createPouch(player, slotId2, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					Summoning.createPouch(player, slotId2, 10);
				}

			}
		} else if (interfaceId == 665) {
			if (player.getFamiliar() == null || player.getFamiliar().getBob() == null)
				return;
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getFamiliar().getBob().addItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bob_isRemove");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 671) {
			if (player.getTemporaryAttributtes().get("drops") == Boolean.FALSE) {
				if (player.getFamiliar() == null || player.getFamiliar().getBob() == null)
					return;
			}
			if (componentId == 27) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {

					if (player.getTemporaryAttributtes().get("drops") != null) {
						ViewNPCDrops.sendDropRateMessage(player, player.getNpcDrops().get(slotId),
								(int) player.getTemporaryAttributtes().get("drops"), slotId);
					} else
						player.getFamiliar().getBob().removeItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {

					if (player.getTemporaryAttributtes().get("drops") != null) {
						ViewNPCDrops.sendPriceCheckMessage(player, player.getNpcDrops().get(slotId));
					} else
						player.getFamiliar().getBob().removeItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {

					if (player.getTemporaryAttributtes().get("drops") != null) {
						player.getPackets().sendGameMessage(ItemExamines.getExamine(player.getNpcDrops().get(slotId)),
								true);
					} else
						player.getFamiliar().getBob().removeItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getFamiliar().getBob().removeItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("bob_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				}
			} else if (componentId == 29) {
				if (player.getTemporaryAttributtes().get("drops") != null) {
					ViewNPCDrops.changeSortType(player, (int) player.getTemporaryAttributtes().get("drops"));
				} else
					player.getFamiliar().takeBob();
			}
		} else if (interfaceId == 916) {
			SkillsDialogue.handleSetQuantityButtons(player, componentId);
		} else if (interfaceId == 193) {
			if (componentId == 5)
				player.getCombatDefinitions().switchShowCombatSpells();
			else if (componentId == 7)
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			else if (componentId >= 9 && componentId <= 11)
				player.getCombatDefinitions().setSortSpellBook(componentId - 9);
			else if (componentId == 18)
				player.getCombatDefinitions().switchDefensiveCasting();
			else
				Magic.processAncientSpell(player, componentId, packetId);
		} else if (interfaceId == 430) {
			if (componentId == 5)
				player.getCombatDefinitions().switchShowCombatSpells();
			else if (componentId == 7)
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			else if (componentId == 9)
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			else if (componentId >= 11 & componentId <= 13)
				player.getCombatDefinitions().setSortSpellBook(componentId - 11);
			else if (componentId == 20)
				player.getCombatDefinitions().switchDefensiveCasting();
			else
				Magic.processLunarSpell(player, componentId, packetId);
		} else if (interfaceId == 261) {
			if (player.getInterfaceManager().containsInventoryInter())
				return;
			if (componentId == 6) {
				player.switchReportOption();
			}
			if (componentId == 15) {
				player.switchAcceptAid();
			}
			if (componentId == 26) {
				AdventurersLog.open(player);
			}
			if (componentId == 11) {
				player.switchProfanityFilter();
			}
			if (componentId == 22) {
				if (player.getInterfaceManager().containsScreenInter()) {
					player.getPackets().sendGameMessage(
							"Please close the interface you have open before setting your graphic options.");
					return;
				}
				player.stopAll();
				player.getInterfaceManager().sendInterface(742);
			} else if (componentId == 12)
				player.switchAllowChatEffects();
			else if (componentId == 13) { // chat setup
				player.getInterfaceManager().sendSettings(982);
			} else if (componentId == 14)
				player.switchMouseButtons();
			else if (componentId == 24) // audio options
				player.getInterfaceManager().sendSettings(429);
			else if (componentId == 16) // house options
				player.getInterfaceManager().sendSettings(398);
		} else if (interfaceId == 429) {
			if (componentId == 18)
				player.getInterfaceManager().sendSettings();
		} else if (interfaceId == 982) {
			if (componentId == 5)
				player.getInterfaceManager().sendSettings();
			else if (componentId == 41)
				player.setPrivateChatSetup(player.getPrivateChatSetup() == 0 ? 1 : 0);
			else if (componentId >= 49 && componentId <= 66)
				player.setPrivateChatSetup(componentId - 48);
			else if (componentId >= 72 && componentId <= 91)
				player.setFriendChatSetup(componentId - 72);
		} else if (interfaceId == 271) {
			if (player.getControllerManager().getController() instanceof ImpossibleJad) {
				player.getPackets().sendGameMessage("You're not allowed to use prayer in here.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (componentId == 8 || componentId == 42)
						player.getPrayer().switchPrayer(slotId);
					else if (componentId == 43 && player.getPrayer().isUsingQuickPrayer())
						player.getPrayer().switchSettingQuickPrayer();
				}
			});
		} else if (interfaceId == 320) {
			if (packetId == 14) {
				player.stopAll();
				int lvlupSkill = -1;
				int skillMenu = -1;

				switch (componentId) {
				case 150: // Attack
					skillMenu = 1;
					if (player.getTemporaryAttributtes().remove("leveledUp[0]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 1);
					} else {
						lvlupSkill = 0;
						player.getPackets().sendConfig(1230, 10);
					}
					break;
				case 9: // Strength
					skillMenu = 2;
					if (player.getTemporaryAttributtes().remove("leveledUp[2]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 2);
					} else {
						lvlupSkill = 2;
						player.getPackets().sendConfig(1230, 20);
					}
					break;
				case 22: // Defence
					skillMenu = 5;
					if (player.getTemporaryAttributtes().remove("leveledUp[1]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 5);
					} else {
						lvlupSkill = 1;
						player.getPackets().sendConfig(1230, 40);
					}
					break;
				case 40: // Ranged
					skillMenu = 3;
					if (player.getTemporaryAttributtes().remove("leveledUp[4]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 3);
					} else {
						lvlupSkill = 4;
						player.getPackets().sendConfig(1230, 30);
					}
					break;
				case 58: // Prayer
					if (player.getTemporaryAttributtes().remove("leveledUp[5]") != Boolean.TRUE) {
						skillMenu = 7;
						player.getPackets().sendConfig(965, 7);
					} else {
						lvlupSkill = 5;
						player.getPackets().sendConfig(1230, 60);
					}
					break;
				case 71: // Magic
					if (player.getTemporaryAttributtes().remove("leveledUp[6]") != Boolean.TRUE) {
						skillMenu = 4;
						player.getPackets().sendConfig(965, 4);
					} else {
						lvlupSkill = 6;
						player.getPackets().sendConfig(1230, 33);
					}
					break;
				case 84: // Runecrafting
					if (player.getTemporaryAttributtes().remove("leveledUp[20]") != Boolean.TRUE) {
						skillMenu = 12;
						player.getPackets().sendConfig(965, 12);
					} else {
						lvlupSkill = 20;
						player.getPackets().sendConfig(1230, 100);
					}
					break;
				case 102: // Construction
					skillMenu = 22;
					if (player.getTemporaryAttributtes().remove("leveledUp[21]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 22);
					} else {
						lvlupSkill = 21;
						player.getPackets().sendConfig(1230, 698);
					}
					break;
				case 145: // Hitpoints
					skillMenu = 6;
					if (player.getTemporaryAttributtes().remove("leveledUp[3]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 6);
					} else {
						lvlupSkill = 3;
						player.getPackets().sendConfig(1230, 50);
					}
					break;
				case 15: // Agility
					skillMenu = 8;
					if (player.getTemporaryAttributtes().remove("leveledUp[16]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 8);
					} else {
						lvlupSkill = 16;
						player.getPackets().sendConfig(1230, 65);
					}
					break;
				case 28: // Herblore
					skillMenu = 9;
					if (player.getTemporaryAttributtes().remove("leveledUp[15]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 9);
					} else {
						lvlupSkill = 15;
						player.getPackets().sendConfig(1230, 75);
					}
					break;
				case 46: // Thieving
					skillMenu = 10;
					if (player.getTemporaryAttributtes().remove("leveledUp[17]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 10);
					} else {
						lvlupSkill = 17;
						player.getPackets().sendConfig(1230, 80);
					}
					break;
				case 64: // Crafting
					skillMenu = 11;
					if (player.getTemporaryAttributtes().remove("leveledUp[12]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 11);
					} else {
						lvlupSkill = 12;
						player.getPackets().sendConfig(1230, 90);
					}
					break;
				case 77: // Fletching
					skillMenu = 19;
					if (player.getTemporaryAttributtes().remove("leveledUp[9]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 19);
					} else {
						lvlupSkill = 9;
						player.getPackets().sendConfig(1230, 665);
					}
					break;
				case 90: // Slayer
					skillMenu = 20;
					if (player.getTemporaryAttributtes().remove("leveledUp[18]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 20);
					} else {
						lvlupSkill = 18;
						player.getPackets().sendConfig(1230, 673);
					}
					break;
				case 108: // Hunter
					skillMenu = 23;
					if (player.getTemporaryAttributtes().remove("leveledUp[22]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 23);
					} else {
						lvlupSkill = 22;
						player.getPackets().sendConfig(1230, 689);
					}
					break;
				case 140: // Mining
					skillMenu = 13;
					if (player.getTemporaryAttributtes().remove("leveledUp[14]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 13);
					} else {
						lvlupSkill = 14;
						player.getPackets().sendConfig(1230, 110);
					}
					break;
				case 135: // Smithing
					skillMenu = 14;
					if (player.getTemporaryAttributtes().remove("leveledUp[13]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 14);
					} else {
						lvlupSkill = 13;
						player.getPackets().sendConfig(1230, 115);
					}
					break;
				case 34: // Fishing
					skillMenu = 15;
					if (player.getTemporaryAttributtes().remove("leveledUp[10]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 15);
					} else {
						lvlupSkill = 10;
						player.getPackets().sendConfig(1230, 120);
					}
					break;
				case 52: // Cooking
					skillMenu = 16;
					if (player.getTemporaryAttributtes().remove("leveledUp[7]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 16);
					} else {
						lvlupSkill = 7;
						player.getPackets().sendConfig(1230, 641);
					}
					break;
				case 130: // Firemaking
					skillMenu = 17;
					if (player.getTemporaryAttributtes().remove("leveledUp[11]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 17);
					} else {
						lvlupSkill = 11;
						player.getPackets().sendConfig(1230, 649);
					}
					break;
				case 125: // Woodcutting
					skillMenu = 18;
					if (player.getTemporaryAttributtes().remove("leveledUp[8]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 18);
					} else {
						lvlupSkill = 8;
						player.getPackets().sendConfig(1230, 660);
					}
					break;
				case 96: // Farming
					skillMenu = 21;
					if (player.getTemporaryAttributtes().remove("leveledUp[19]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 21);
					} else {
						lvlupSkill = 19;
						player.getPackets().sendConfig(1230, 681);
					}
					break;
				case 114: // Summoning
					skillMenu = 24;
					if (player.getTemporaryAttributtes().remove("leveledUp[23]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 24);
					} else {
						lvlupSkill = 23;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
				case 120: // Dung
					skillMenu = 25;
					if (player.getTemporaryAttributtes().remove("leveledUp[24]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 25);
					} else {
						lvlupSkill = 24;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
				}

				player.getInterfaceManager().sendScreenInterface(317, 1218);
				player.getPackets().sendInterface(false, 1218, 1, 1217);
				if (lvlupSkill != -1) {
					LevelUp.switchFlash(player, lvlupSkill, false);
				}
				if (skillMenu != -1) {
					player.getTemporaryAttributtes().put("skillMenu", skillMenu);
				}
			} else if (packetId == 67 || packetId == 5) { // set level target,
															// set xp target
				int skillId = player.getSkills().getTargetIdByComponentId(componentId);
				boolean usingLevel = packetId == 67;
				player.getTemporaryAttributtes().put(usingLevel ? "levelSkillTarget" : "xpSkillTarget", skillId);
				player.getPackets().sendInputIntegerScript(
						"Please enter target " + (usingLevel ? "level" : "xp") + " you want to set: ");

			} else if (packetId == 55) { // clear target
				int skillId = player.getSkills().getTargetIdByComponentId(componentId);
				player.getSkills().setSkillTargetEnabled(skillId, false);
				player.getSkills().setSkillTargetValue(skillId, 0);
				player.getSkills().setSkillTargetUsingLevelMode(skillId, false);
			}
		} else if (interfaceId == 1218) {
			if ((componentId >= 33 && componentId <= 55) || componentId == 120 || componentId == 151
					|| componentId == 189)
				player.getPackets().sendInterface(false, 1218, 1, 1217); // seems to fix
		} else if (interfaceId == 499) {
			int skillMenu = -1;
			if (player.getTemporaryAttributtes().get("skillMenu") != null)
				skillMenu = (Integer) player.getTemporaryAttributtes().get("skillMenu");
			if (componentId >= 10 && componentId <= 25)
				player.getPackets().sendConfig(965, ((componentId - 10) * 1024) + skillMenu);
			else if (componentId == 29)
				// close inter
				player.stopAll();

		} else if (interfaceId == 387) {
			Equipment.handleEquipment(player, componentId, slotId, packetId, itemId);
		} else if (interfaceId == 1265) {
			Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
			if (shop == null)
				return;
			Integer slot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
			boolean isBuying = player.getTemporaryAttributtes().get("shop_buying") != null;
			if (componentId == 20) {
				player.getTemporaryAttributtes().put("ShopSelectedSlot", slotId);
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					shop.sendInfo(player, slotId, isBuying);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					shop.handleShop(player, slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					shop.handleShop(player, slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					shop.handleShop(player, slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					shop.handleShop(player, slotId, 50);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
					player.getPackets().sendGameMessage("You aren't allowed to buy all of that item.");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					shop.handleShop(player, slotId, 500);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					shop.sendExamine(player, slotId);
			} else if (componentId == 201) {
				if (slot == null)
					return;
				if (isBuying)
					shop.buy(player, slot, shop.getAmount());
				else {
					shop.sell(player, slot, shop.getAmount());
					player.getPackets().sendConfig(2563, 0);
					player.getPackets().sendConfig(2565, 1);
				}
			} else if (componentId == 208) {
				shop.setAmount(player, shop.getAmount() + 5);
			} else if (componentId == 15) {
				shop.setAmount(player, shop.getAmount() + 1);
			} else if (componentId == 214) {
				if (shop.getAmount() <= 1) {
					return;
				}
				shop.setAmount(player, shop.getAmount() - 1);
			} else if (componentId == 217) {
				if (shop.getAmount() < 6) {
					return;
				}
				shop.setAmount(player, shop.getAmount() - 5);
			} else if (componentId == 220) {
				shop.setAmount(player, 1);
			} else if (componentId == 211) {
				if (slot == null)
					return;
				Item item = isBuying ? shop.getMainStock()[slot] : player.getInventory().getItem(slot);
				if (item == null) {
					return;
				}
				shop.setAmount(player, item.getAmount());
			} else if (componentId == 29) {
				player.getPackets().sendConfig(2561, 93);
				player.getTemporaryAttributtes().remove("shop_buying");
				shop.setAmount(player, 1);
			} else if (componentId == 28) {
				player.getTemporaryAttributtes().put("shop_buying", true);
				shop.setAmount(player, 1);
			}
			return;

		} else if (interfaceId == 1266) {
			Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					shop.sendValue(player, slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					shop.sell(player, slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					shop.sell(player, slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					shop.sell(player, slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					shop.sell(player, slotId, 50);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 640) {
			if (componentId == 18 || componentId == 22) {
				player.getTemporaryAttributtes().put("WillDuelFriendly", true);
				player.getPackets().sendConfig(283, 67108864);
			} else if (componentId == 19 || componentId == 21) {
				if (player.isIronman()) {
					player.getDialogueManager().startDialogue("IronMan");
					return;
				}
				player.getTemporaryAttributtes().put("WillDuelFriendly", false);
				player.getPackets().sendConfig(283, 134217728);
			} else if (componentId == 20) {
				DuelControler.challenge(player);
			}
		} else if (interfaceId == 1188) {
			if (componentId == 4 && player.choseGameMode == false)
				;
			return;
		} else if (interfaceId == 1143) {// Loyalty shop
			LoyaltyStore.handleButtons(componentId, slotId, 0, packetId, player);
		} else if (interfaceId == 650) {
			if (componentId == 15) {
				player.stopAll();
				player.setNextWorldTile(new WorldTile(2974, 4384, player.getPlane()));
				player.getControllerManager().startController("CorpBeastControler");
			} else if (componentId == 16)
				player.closeInterfaces();
		} else if (interfaceId == 667) {
			if (player.getTemporaryAttributtes().get("Cosmetics") != null)
				return;
			if (player.getTemporaryAttributtes().get("Cosmeticsold") != null
					&& (Boolean) player.getTemporaryAttributtes().get("Cosmeticsold")) {
				if (componentId == 9) {

					switch (slotId) {

					case Equipment.SLOT_HAT:
					case Equipment.SLOT_CAPE:
					case Equipment.SLOT_AMULET:
					case Equipment.SLOT_CHEST:
					case Equipment.SLOT_LEGS:
					case Equipment.SLOT_WEAPON:
					case 9:
					case Equipment.SLOT_FEET:
					case 14:
						player.getDialogueManager().startDialogue("Cosmetic", slotId);
						break;

					case Equipment.SLOT_RING:
					case Equipment.SLOT_ARROWS:
					case Equipment.SLOT_SHIELD:
						player.getDialogueManager().startDialogue("SimpleMessage", "You can't customize this slot.");
						break;

					}
				}
				return;
			}
			if (componentId == 14) {
				if (slotId >= 14)
					return;
				Item item = player.getEquipment().getItem(slotId);
				if (item == null)
					return;
				if (packetId == 3)
					player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
				else if (packetId == 216) {
					sendRemove(player, slotId);
					Equipment.refreshEquipBonuses(player);
				}
			} else if (componentId == 46 && player.getTemporaryAttributtes().remove("Banking") != null) {
				player.getBank().openBank();
			} else if (componentId == 9) {
				sendRemove(player, slotId);
				Equipment.refreshEquipBonuses(player);
			}
		} else if (interfaceId == 670) {
			if (componentId == 0) {
				if (slotId >= player.getInventory().getItemsContainerSize())
					return;
				Item item = player.getInventory().getItem(slotId);
				if (item == null)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					if (sendWear(player, slotId, item.getId()))
						Equipment.refreshEquipBonuses(player);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == Inventory.INVENTORY_INTERFACE) { // inventory
			if (componentId == 0) {
				if (slotId > 27 || player.getInterfaceManager().containsInventoryInter())
					return;
				Item item = player.getInventory().getItem(slotId);
				if (item == null || item.getId() != slotId2)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					InventoryOptionsHandler.handleItemOption1(player, slotId, slotId2, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					InventoryOptionsHandler.handleItemOption2(player, slotId, slotId2, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					InventoryOptionsHandler.handleItemOption3(player, slotId, slotId2, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					InventoryOptionsHandler.handleItemOption4(player, slotId, slotId2, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					InventoryOptionsHandler.handleItemOption5(player, slotId, slotId2, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
					InventoryOptionsHandler.handleItemOption6(player, slotId, slotId2, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
					InventoryOptionsHandler.handleItemOption7(player, slotId, slotId2, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					InventoryOptionsHandler.handleItemOption8(player, slotId, slotId2, item);
			}
		} else if (interfaceId == 742) {
			if (componentId == 46) // close
				player.stopAll();
		} else if (interfaceId == 743) {
			if (componentId == 20) // close
				player.stopAll();
		} else if (interfaceId == 741) {
			if (componentId == 9) // close
				player.stopAll();
		} else if (interfaceId == 749) {
			if (player.getControllerManager().getController() instanceof ImpossibleJad) {
				player.getPackets().sendGameMessage("You're not allowed to use prayer in here.");
				return;
			}
			if (componentId == 4) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) // activate
					player.getPrayer().switchQuickPrayers();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) // switch
					player.getPrayer().switchSettingQuickPrayer();
			}
		} else if (interfaceId == 750) {
			if (componentId == 4) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.toogleRun(player.isResting() ? false : true);
					if (player.isResting())
						player.stopAll();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (player.isResting()) {
						player.stopAll();
						return;
					}
					long currentTime = Utils.currentTimeMillis();
					if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an emote.");
						return;
					}
					if (player.getLockDelay() >= currentTime) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an action.");
						return;
					}
					player.stopAll();
					player.getActionManager().setAction(new Rest());
				}
			}
			return;
		} // Deposit Box
		if (interfaceId == 11) {
			if (componentId == 17) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().depositItem(slotId, 1, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().depositItem(slotId, 5, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getBank().depositItem(slotId, 10, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			} else if (componentId == 18)
				player.getBank().depositAllInventory(false);
			else if (componentId == 20)
				player.getBank().depositAllEquipment(false);
			return;
		}

		// Bank
		if (Bank.handleButtons(player, interfaceId, componentId, slotId, packetId)) {
			return;
		}

		// Combat Style tab
		if (interfaceId == 884) {
			if (componentId == 4) {
				int weaponId = player.getEquipment().getWeaponId();
				if (player.hasInstantSpecial(weaponId)) {
					player.performInstantSpecial(weaponId);
					return;
				}
				submitSpecialRequest(player);
			} else if (componentId >= 7 && componentId <= 10)
				player.getCombatDefinitions().setAttackStyle(componentId - 7);
			else if (componentId == 11)
				player.getCombatDefinitions().switchAutoRelatie();
			return;

		} else if (interfaceId == 755) {
			if (componentId == 44)
				player.getPackets().sendWindowsPane(player.getInterfaceManager().hasResizableScreen() ? 746 : 548, 2);
			else if (componentId == 42) {
				player.getHintIconsManager().removeAll();// TODO find hintIcon index
				player.getPackets().sendConfig(1159, 1);
			}
		} else if (interfaceId == 20)
			SkillCapeCustomizer.handleSkillCapeCustomizer(player, componentId);
		else if (interfaceId == 1056) {
			if (componentId == 173)
				player.getInterfaceManager().sendInterface(917);
		} else if (interfaceId == 751) {
			if (componentId == 26) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getFriendsIgnores().setPrivateStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getFriendsIgnores().setPrivateStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getFriendsIgnores().setPrivateStatus(2);
			} else if (componentId == 32) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setFilterGame(false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setFilterGame(true);
			} else if (componentId == 29) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setPublicStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setPublicStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setPublicStatus(2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.setPublicStatus(3);
			} else if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getFriendsIgnores().setFriendsChatStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getFriendsIgnores().setFriendsChatStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getFriendsIgnores().setFriendsChatStatus(2);
			} else if (componentId == 23) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setClanStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setClanStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setClanStatus(2);
			} else if (componentId == 20) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setTradeStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setTradeStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setTradeStatus(2);
			} else if (componentId == 14) {
				player.getInterfaceManager().sendInterface(594);
			} else if (componentId == 17) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setAssistStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setAssistStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setAssistStatus(2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					// ASSIST XP Earned/Time
				}
			}
		} else if (interfaceId == 1163 || interfaceId == 1164 || interfaceId == 1168 || interfaceId == 1170
				|| interfaceId == 1173)
			player.getDominionTower().handleButtons(interfaceId, componentId);
		else if (interfaceId == 900)
			PlayerLook.handleMageMakeOverButtons(player, componentId);
		else if (interfaceId == 1108 || interfaceId == 1109)
			player.getFriendsIgnores().handleFriendChatButtons(interfaceId, componentId, packetId);
		else if (interfaceId == 1079)
			player.closeInterfaces();
		else if (interfaceId == 374) {
			if (componentId >= 5 && componentId <= 9)
				player.setNextWorldTile(new WorldTile(FightPitsViewingOrb.ORB_TELEPORTS[componentId - 5]));
			else if (componentId == 15)
				player.stopAll();
		} else if (interfaceId == 1089) {
			if (componentId == 30)
				player.getTemporaryAttributtes().put("clanflagselection", slotId);
			else if (componentId == 26) {
				Integer flag = (Integer) player.getTemporaryAttributtes().remove("clanflagselection");
				player.stopAll();
				if (flag != null)
					ClansManager.setClanFlagInterface(player, flag);
			}
		}
		if (interfaceId == 864) {
			SpiritTree.handleButtons(player, slotId);
		}
		if (interfaceId == 1092) {
			if (componentId == 60) {
				player.getInterfaceManager().closeScreenInterface();
				return;
			}
			String[] lodestoneNames = new String[] { "Lunar Isle", "Al Kharid", "Ardougne", "Burthorpe", "Catherby",
					"Draynor Village", "Edgeville", "Falador", "Lumbridge", "Port Sarim", "Seer's Village", "Taverley",
					"Varrock", "Yannile" };
			if (componentId != 7 && !player.lodestone[componentId - 38]) {
				player.getPackets().sendGameMessage("You'll need to activate the " + lodestoneNames[componentId - 39]
						+ " lodestone before you can homeport there!");
				return;
			} else if (componentId == 7 && !player.lodestone[0]) {
				player.getPackets().sendGameMessage(
						"You'll need to activate the Bandit Camp lodestone before you can homeport there!");
				return;
			}
			player.stopAll();
			WorldTile destTile = null;
			switch (componentId) {
			case 47:
				destTile = HomeTeleport.LUMBRIDGE_LODE_STONE;
				break;
			case 42:
				destTile = HomeTeleport.BURTHORPE_LODE_STONE;
				break;
			case 39:
				destTile = HomeTeleport.LUNAR_ISLE_LODE_STONE;
				break;
			case 7:
				destTile = HomeTeleport.BANDIT_CAMP_LODE_STONE;
				break;
			case 50:
				destTile = HomeTeleport.TAVERLY_LODE_STONE;
				break;
			case 40:
				destTile = HomeTeleport.ALKARID_LODE_STONE;
				break;
			case 51:
				destTile = HomeTeleport.VARROCK_LODE_STONE;
				break;
			case 45:
				destTile = HomeTeleport.EDGEVILLE_LODE_STONE;
				break;
			case 46:
				destTile = HomeTeleport.FALADOR_LODE_STONE;
				break;
			case 48:
				destTile = HomeTeleport.PORT_SARIM_LODE_STONE;
				break;
			case 44:
				destTile = HomeTeleport.DRAYNOR_VILLAGE_LODE_STONE;
				break;
			case 41:
				destTile = HomeTeleport.ARDOUGNE_LODE_STONE;
				break;
			case 43:
				destTile = HomeTeleport.CATHERBAY_LODE_STONE;
				break;
			case 52:
				destTile = HomeTeleport.YANILLE_LODE_STONE;
				break;
			case 49:
				destTile = HomeTeleport.SEERS_VILLAGE_LODE_STONE;
				break;
			}
			if (destTile != null) {
				player.getActionManager().setAction(new HomeTeleport(destTile));
			} else if (componentId == 7 && !player.lodestone[0]) {
				player.getPackets().sendGameMessage(
						"You'll need to activate the Bandit Camp lodestone before you can homeport there!");
				return;
			}
		} else if (interfaceId == 1214)
			player.getSkills().handleSetupXPCounter(componentId);
		else if (interfaceId == 1292) {
			if (componentId == 12)
				Crucible.enterArena(player);
			else if (componentId == 13)
				player.closeInterfaces();
		}
		if (Settings.DEBUG)
			Logger.log("ButtonHandler", "InterfaceId " + interfaceId + ", componentId " + componentId + ", slotId "
					+ slotId + ", slotId2 " + slotId2 + ", PacketId: " + packetId);
	}

	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15)
			return;
		player.stopAll(false, false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null || !player.getInventory().addItemDrop(item.getId(), item.getAmount()))
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
		if (Runecrafting.isTiara(item.getId()))
			player.getPackets().sendConfig(491, 0);
		if (slotId == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
	}

	public static boolean sendWear(Player player, int slotId, int itemId) {
		if (player.hasFinished() || player.isDead())
			return false;
		player.stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		String itemName = item.getDefinitions() == null ? "" : item.getDefinitions().getName().toLowerCase();
		if (item == null || item.getId() != itemId)
			return false;
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getAppearence().isMale())) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		if (item.getId() == 1205) {
			if (player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().getTutorialStage() == 17) {
				player.getStatistics().tutorialStage = 18;
				player.getDialogueManager().startDialogue("CombatInstructor", 3);
			}
		}
		for (String strings : Settings.UNWEARABLE_ITEMS) {
			if (itemName.contains(strings)) {
				player.getPackets().sendGameMessage("You cannot wear this item.");
				return true;
			}
		}
		for (String strings : Settings.DONATOR_ITEMS) {
			if (itemName.contains(strings) && !player.isDonator()) {
				player.getPackets().sendGameMessage("You need to be a donator to equip " + itemName + ".");
				return true;
			}
		}
		for (String strings : Settings.EXTREME_DONATOR_ITEMS) {
			if (itemName.contains(strings) && !player.isExtremeDonator()) {
				player.getPackets().sendGameMessage("You need to be a extreme donator to equip " + itemName + ".");
				return true;
			}
		}
		for (String strings : Settings.EARNED_ITEMS) {
			if (itemName.contains(strings) && player.getRights() <= 1) {
				player.getPackets().sendGameMessage("You must earn " + itemName + ".");
				return true;
			}
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		if (!ItemConstants.canWear(item, player))
			return true;
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage("Not enough free space in your inventory.");
			return true;
		}
		HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
					continue;
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequiriments) {
						player.getPackets().sendGameMessage("You are not high enough level to use this item.");
					}
					hasRequiriments = false;
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " "
							+ name + " level of " + level + ".");
				}
			}
		}
		if (!hasRequiriments)
			return true;
		if (!player.getControllerManager().canEquip(targetSlot, itemId))
			return false;
		player.stopAll(false, false);
		player.getInventory().deleteItem(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().addItemDrop(player.getEquipment().getItem(5).getId(),
						player.getEquipment().getItem(5).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().addItemDrop(player.getEquipment().getItem(3).getId(),
						player.getEquipment().getItem(3).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId()
						|| !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
				player.getInventory().refresh(slotId);
			} else
				player.getInventory().addItem(new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
			player.getEquipment().getItems().set(targetSlot, null);
		}
		if (targetSlot == Equipment.SLOT_AURA)
			player.getAuraManager().removeAura();
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendSound(2240, 0, 1);
		if (targetSlot == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		return true;
	}

	public static void submitSpecialRequest(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getCombatDefinitions().switchUsingSpecialAttack();
						}
					}, 0);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 200);
	}

	public static void openItemsKeptOnDeath(Player player, boolean wildInter) {
		player.getInterfaceManager().sendInterface(17);
		if (Wilderness.isAtWild(player) || wildInter) {
			sendItemsKeptOnDeath(player, Wilderness.isAtWild(player));
			return;
		} else {
			sendItemsKeptOnDeath(player, false);
			return;
		}
	}

	public static void sendItemsKeptOnDeath(Player player, boolean wilderness) {
		boolean skulled = player.hasSkull();
		Integer[][] slots = GraveStone.getItemSlotsKeptOnDeath(player, wilderness, skulled,
				player.getPrayer().isProtectingItem());
		Item[][] items = GraveStone.getItemsKeptOnDeath(player, slots);
		long riskedWealth = 0;
		long carriedWealth = 0;
		for (Item item : items[1])
			carriedWealth = riskedWealth += item.getDefinitions().getPrice() * item.getAmount();
		for (Item item : items[0])
			carriedWealth += item.getDefinitions().getPrice() * item.getAmount();
		if (slots[0].length > 0) {
			for (int i = 0; i < slots[0].length; i++)
				player.getPackets().sendConfigByFile(9222 + i, slots[0][i], true);
			player.getPackets().sendConfigByFile(9227, slots[0].length, true);
		} else {
			player.getPackets().sendConfigByFile(9222, -1, true);
			player.getPackets().sendConfigByFile(9227, 1, true);
		}
		player.getVarBitManager().setVarBit(9226, wilderness ? 1 : 0);
		player.getPackets().sendConfigByFile(9226, wilderness ? 1 : 0, true);
		player.getPackets().sendConfigByFile(9229, skulled ? 1 : 0, true);
		StringBuffer text = new StringBuffer();
		text.append("The number of items kept on").append("<br>").append("death is normally 3.").append("<br>")
				.append("<br>").append("<br>");
		if (wilderness) {
			text.append("Your gravestone will not").append("<br>").append("appear.");
		} else {
			int time = GraveStone.getMaximumTicks(player.getGraveStone());
			int seconds = (int) (time * 0.6);
			int minutes = seconds / 60;
			seconds -= minutes * 60;

			text.append("Gravestone:").append("<br>")
					.append(ClientScriptMap.getMap(1099).getStringValue(player.getGraveStone())).append("<br>")
					.append("<br>").append("Initial duration:").append("<br>")
					.append(minutes + ":" + (seconds < 10 ? "0" : "") + seconds).append("<br>");
		}
		text.append("<br>").append("<br>").append("Carried wealth:").append("<br>")
				.append(carriedWealth > Integer.MAX_VALUE ? "Too high!" : Utils.getFormattedNumber((int) carriedWealth))
				.append("<br>").append("<br>").append("Risked wealth:").append("<br>")
				.append(riskedWealth > Integer.MAX_VALUE ? "Too high!" : Utils.getFormattedNumber((int) riskedWealth))
				.append("<br>").append("<br>");
		if (wilderness) {
			text.append("Your hub will be set to:").append("<br>").append("Edgeville.");
		} else {
			text.append("Current hub: Home");
		}
		player.getPackets().sendGlobalString(352, text.toString());
	}

	public static void sendWear(Player player, int[] slotIds) {
		if (player.hasFinished() || player.isDead())
			return;
		boolean worn = false;
		Item[] copy = player.getInventory().getItems().getItemsCopy();
		for (int slotId : slotIds) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				continue;
		}
		player.getInventory().refreshItems(copy);
		if (worn) {
			player.getAppearence().generateAppearenceData();
			player.getPackets().sendSound(2240, 0, 1);
		}
	}
}