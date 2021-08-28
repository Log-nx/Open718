package com.rs.game.player.controllers.tutorial;

import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.actions.cooking.Cooking;
import com.rs.game.player.actions.cooking.Cooking.Cookables;
import com.rs.game.player.actions.WaterFilling;
import com.rs.game.player.actions.WaterFilling.Fill;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.controllers.Controller;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Color;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.ItemIdentifiers;

public class DiccusTutorial extends Controller {
	
	static int FINISHED = 26;

	private void remove() {
		removeController();
		Dialogue.closeNoContinueDialogue(player);
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		remove();
	}

	@Override
	public boolean sendDeath() {
		remove();
		return true;
	}
	
	public static void finishTutorial (Player player) {
		WorldTile home = player.getPlayerHomeLocation();
		if (player.isAnIronMan()) { 
			World.sendWorldMessage("<col=43dde5>Welcome, " + player.getIronmanTitle(false) + "<col=43dde5>" + player.getDisplayName() + " to the world of Diccus, they are playing the gamemode </col>" + player.getXPMode() + ".", false, false); 
		} else { 
			World.sendWorldMessage("<col=43dde5>Welcome, </col>" + player.getDisplayName() + "<col=43dde5> to the world of Diccus, they are playing the gamemode </col>" + player.getXPMode() + ".", false, false); 
		}
		giveStarter(player);
		player.setServerTutorial(true);
		player.getStatistics().tutorialStage = FINISHED;
		if (!player.homeIsLletya || !player.homeIsTaverly || !player.homeIsLumby || !player.homeIsVarrock) {
			player.setLumbyHome(true);
		}
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(home));
	}
	
	public static void addMagicHintIcon(Player player) {
		NPC target = World.findNPC(player, 946);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addGuideHintIcon(Player player) {
		NPC target = World.findNPC(player, 945);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addCombatHintIcon(Player player) {
		NPC target = World.findNPC(player, 944);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addExpertHintIcon(Player player) {
		NPC target = World.findNPC(player, 943);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addChefHintIcon(Player player) {
		NPC target = World.findNPC(player, 942);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addMiningHintIcon(Player player) {
		NPC target = World.findNPC(player, 948);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addQuestHintIcon(Player player) {
		NPC target = World.findNPC(player, 949);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addPrayerHintIcon(Player player) {
		NPC target = World.findNPC(player, 25280);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}
	
	public static void addBankHintIcon(Player player) {
		NPC target = World.findNPC(player, 947);
		if (target != null) {
			player.getHintIconsManager().addHintIcon(target, 1, -1, false);
		}
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		int id = object.getId();
		ObjectDefinitions definitions = object.getDefinitions();
		switch (definitions.name.toLowerCase()) {
		case "tree":
			if (player.getStatistics().tutorialStage < 2) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 943, "You should really come talk to me first.");
				return false;
			}
			break;
		}
		if (id == 110897) {
			if (player.getStatistics().tutorialStage == 0) {
				return false;
			} else
				player.setNextWorldTile(new WorldTile(3802, 4259, 0));
			return false;
		}
		if (id == 110907 || id == 110908) {
			if (player.getStatistics().tutorialStage < 6) {
				return false;
			} else
				player.setNextWorldTile(new WorldTile(3793, 4244, 0));
			return false;
		}
		if (id == 110898) {
			if (player.getStatistics().tutorialStage < 8) {
				return false;
			}
			else {
				player.setNextWorldTile(new WorldTile(3776, 4242, 0));
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 942, "Follow the path to the home of the quest guide.");
			}
			return false;
		}
		if (id == 110944) {
			Cookables cook = Cooking.getCook(player);
			if (player.getStatistics().tutorialStage < 7) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 942, "Hey! Don't mess with that!");
				return false;
			} else
				player.getDialogueManager().startDialogue("CookingD", cook, object);
			return false;
		}
		if (id == 110943) {
			if (player.getStatistics().tutorialStage < 7) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 942, "Hey! Don't mess with that!");
				return false;
			} else {
				Fill empty = WaterFilling.getEmpty(player);
				if (empty != null)
				player.getDialogueManager().startDialogue("WaterFillingD", empty, object);
				//WaterFilling.isFilling(player, empty.getEmpty(), false);
			}
			return false;
		}
		if (id == 110945) {
			if (player.getStatistics().tutorialStage == 9) {
				player.setNextAnimation(new Animation(828));
				player.setNextWorldTile(new WorldTile(3672, 4327, 0));
				player.getStatistics().tutorialStage = 10;
				addMiningHintIcon(player);
				return false;
			}
		}
		if (id == 113258) {
			if (player.getStatistics().tutorialStage < 12) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 948, "Hey! Don't mess with that!");
				return false;
			}
			return true;
		}
		if (id == 110954 || id == 113259) {
			if (player.getStatistics().tutorialStage < 13) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 948, "Hey! Don't mess with that!");
				return false;
			}
			return true;
		}
		if (id == 110948) {
			if (player.getStatistics().tutorialStage < 20) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 944, "I knew you were a coward, get back here.");
				return false;
			}
			player.setNextAnimation(new Animation(828));
			player.setNextWorldTile(new WorldTile(3815, 4277, 0));
			addBankHintIcon(player);
			return false;
		}
		if (id == 110899) {
			if (player.getStatistics().tutorialStage < 22) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 947, "You should talk to me before trying to leave.");
				return false;
			}
			player.setNextWorldTile(new WorldTile(3834, 4276, 0));
			addPrayerHintIcon(player);
			return false;
		}
		if (id == 110900) {
			if (player.getStatistics().tutorialStage < 23) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 25280, "You should talk to me before trying to leave.");
				return false;
			}
			player.setNextWorldTile(new WorldTile(3826, 4254, 0));
			addMagicHintIcon(player);
			return false;
		}
		return true;
	}

	@Override
	public boolean processNPCClick1(final NPC npc) {
		int id = npc.getId();
		switch (id) {
		case 327:
			if (player.getStatistics().tutorialStage < 4) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 943, "Hey! Don't scare them away!");
				return false;
			}
			break;
		case 945:
			if (player.getStatistics().tutorialStage == 1) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("TutorialGuide", 2);
				return false;
			}
			if (player.getStatistics().tutorialStage == 0) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("TutorialGuide", 1);
				return false;
			}
			break;
		case 943:
			if (player.getStatistics().tutorialStage == 1) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("SurvivalExpert", 1);
				return false;
			}
			if (player.getStatistics().tutorialStage == 2) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("SurvivalExpert", 2);
				return false;
			}
			if (player.getStatistics().tutorialStage == 3) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("SurvivalExpert", 3);
				return false;
			}
			if (player.getStatistics().tutorialStage == 4) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("SurvivalExpert", 4);
				return false;
			}
			if (player.getStatistics().tutorialStage == 5) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("SurvivalExpert", 5);
				return false;
			}
			if (player.getStatistics().tutorialStage == 6) {
				Dialogue.closeNoContinueDialogue(player);
				player.getDialogueManager().startDialogue("SurvivalExpert", 6);
				return false;
			}
			Dialogue.closeNoContinueDialogue(player);
			player.getDialogueManager().startDialogue("SurvivalExpert", 1);
			break;
		case 942:
			if (player.getStatistics().tutorialStage == 6) {
				player.getDialogueManager().startDialogue("MasterChef", 1);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 7) {
				player.getDialogueManager().startDialogue("MasterChef", 2);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 8) {
				player.getDialogueManager().startDialogue("MasterChef", 4);	
				return false;
			}
			break;
		case 948:
			if (player.getStatistics().tutorialStage == 10) {
				player.getDialogueManager().startDialogue("MiningInstructor", 1);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 11) {
				player.getDialogueManager().startDialogue("MiningInstructor", 2);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 12) {
				player.getDialogueManager().startDialogue("MiningInstructor", 3);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 13) {
				player.getDialogueManager().startDialogue("MiningInstructor", 4);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 14) {
				player.getDialogueManager().startDialogue("MiningInstructor", 5);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 15) {
				player.getDialogueManager().startDialogue("MiningInstructor", 6);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 16) {
				player.getDialogueManager().startDialogue("MiningInstructor", 7);	
				return false;
			}
			break;
		case 944:
			if (player.getStatistics().tutorialStage == 16) {
				player.getDialogueManager().startDialogue("CombatInstructor", 1);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 17) {
				player.getDialogueManager().startDialogue("CombatInstructor", 2);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 18) {
				player.getDialogueManager().startDialogue("CombatInstructor", 3);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 19) {
				player.getDialogueManager().startDialogue("CombatInstructor", 4);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 20) {
				player.getDialogueManager().startDialogue("CombatInstructor", 5);	
				return false;
			}
			break;
		case 947:
			if (player.getStatistics().tutorialStage == 20) {
				player.getDialogueManager().startDialogue("FinancialAdvisor", 1);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 21) {
				player.getDialogueManager().startDialogue("FinancialAdvisor", 2);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 22) {
				player.getDialogueManager().startDialogue("FinancialAdvisor", 3);	
				return false;
			}
			break;
		case 949:
			if (player.getStatistics().tutorialStage == 8) {
				player.getDialogueManager().startDialogue("QuestGuide", 1);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 9) {
				player.getDialogueManager().startDialogue("QuestGuide", 2);	
				return false;
			}
			break;
		case 25280:
			if (player.getStatistics().tutorialStage == 22) {
				player.getDialogueManager().startDialogue("PrayerInstructor", 1);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 23) {
				player.getDialogueManager().startDialogue("PrayerInstructor", 2);	
				return false;
			}
			break;
		case 946:
			if (player.getStatistics().tutorialStage == 23) {
				player.getDialogueManager().startDialogue("MagicInstructor", 1);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 24) {
				player.getDialogueManager().startDialogue("MagicInstructor", 2);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 25) {
				player.getDialogueManager().startDialogue("MagicInstructor", 3);	
				return false;
			}
			if (player.getStatistics().tutorialStage == 26) {
				player.getDialogueManager().startDialogue("MagicInstructor", 4);	
				return false;
			}
			break;
		}
		return true;
	}

	@Override
	public boolean processNPCClick2(final NPC npc) {
		return true;
	}

	@Override
	public void start() {
		if (player.getStatistics().tutorialStage == 0) {
			//closeInterfaces();
			player.setNextAnimation(new Animation(-1));
			player.setNextWorldTile(Settings.TUTORIAL_LOCATION);
			addGuideHintIcon(player);
			Dialogue.sendNPCDialogueNoContinue(player, 945, Dialogue.HAPPY, "Hello " + player.getDisplayName() + ", come talk to me.");
			return;
		}
		if (player.getStatistics().tutorialStage == 1) {
			addExpertHintIcon(player);
			return;
		}
		if (player.getStatistics().tutorialStage == 6) {
			addChefHintIcon(player);
			return;
		}
		if (player.getStatistics().tutorialStage == 9) {
			addQuestHintIcon(player);
			return;
		}
		if (player.getStatistics().tutorialStage == 10) {
			addMiningHintIcon(player);
			return;
		}
		if (player.getStatistics().tutorialStage == 16) {
			addCombatHintIcon(player);
			return;
		}
		if (player.getStatistics().tutorialStage == 20) {
			addBankHintIcon(player);
			return;
		}
		if (player.getStatistics().tutorialStage == 22) {
			addPrayerHintIcon(player);
			return;
		}
		if (player.getStatistics().tutorialStage == 23) {
			addMagicHintIcon(player);
			return;
		}
	}
	
	public static void giveStarter(Player player) {
		if (player.hasRecievedStarter == true)
			return;
		player.getInventory().addItem(ItemIdentifiers.IRON_SCIMITAR, 1);
		player.getInventory().addItem(ItemIdentifiers.BRONZE_SCIMITAR, 1);
		player.getInventory().addItem(ItemIdentifiers.OFFHAND_IRON_SCIMITAR, 1);
		player.getInventory().addItem(ItemIdentifiers.OFFHAND_BRONZE_SCIMITAR, 1);
		player.getInventory().addItem(ItemIdentifiers.SHORTBOW, 1);
		player.getInventory().addItem(ItemIdentifiers.BRONZE_ARROW, 250);
		player.getInventory().addItem(ItemIdentifiers.AMULET_OF_STRENGTH, 1);
		player.getInventory().addItem(ItemIdentifiers.IRON_ARMOUR_SET_LG, 1);
		player.getInventory().addItem(ItemIdentifiers.WATER_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.AIR_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.EARTH_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.BODY_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.DEATH_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.NATURE_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.CHAOS_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.LAW_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.COSMIC_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.SOUL_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.BLOOD_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.STAFF_OF_AIR, 1);
		player.getInventory().addItem(ItemIdentifiers.BLOOD_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.LOBSTER_NOTED, 100);
		player.getInventory().addItem(ItemIdentifiers.BLOOD_RUNE, 200);
		player.getInventory().addItem(ItemIdentifiers.IRON_CROSSBOW, 1);
		player.getInventory().addItem(ItemIdentifiers.IRON_BOLTS, 200);
		player.getMoneyPouch().sendDynamicInteraction(250000, false);
		player.getSquealOfFortune().setEarnedSpins(10);
		player.getStatistics().setRuneCoins(50);
		player.getPackets().sendGameMessage(Color.RED, "You have been given 10 treasure tunter keys, and 50 rune coins.");
		player.hasRecievedStarter = true;
	}
	
	public void closeInterfaces() {
		player.getInterfaceManager().closeCombatStyles();
		player.getInterfaceManager().closeEmotes();
		player.getInterfaceManager().closeMagicBook();
		player.getInterfaceManager().closePrayerBook();
		player.getInterfaceManager().closeEquipment();
		player.getInterfaceManager().closeSkills();
		player.getInterfaceManager().closeSquealOfFortuneTab();
		player.getInterfaceManager().closeTaskSystem();
		player.getInterfaceManager().closeInventory();
		player.getInterfaceManager().closeQuests();
		player.getInterfaceManager().closeFriendsList();
		player.getInterfaceManager().closeClanChat();
		player.getInterfaceManager().closeFriendsChat();
		player.getInterfaceManager().closeMusic();
		player.getInterfaceManager().closeNotes();
	}
}