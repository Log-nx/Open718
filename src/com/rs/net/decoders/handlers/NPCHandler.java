package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.minigames.pest.CommendationExchange;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.others.GraveStone;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.npc.pet.Pet;
import com.rs.game.npc.slayer.DesertLizard;
import com.rs.game.npc.slayer.Strykewyrm;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Player;
import com.rs.game.player.actions.divination.Wisp;
import com.rs.game.player.actions.fishing.Fishing;
import com.rs.game.player.actions.fishing.Fishing.FishingSpots;
import com.rs.game.player.actions.mining.LivingMineralMining;
import com.rs.game.player.actions.mining.MiningBase;
import com.rs.game.player.actions.runecrafting.SiphonActionCreatures;
import com.rs.game.player.actions.slayer.SlayerTask;
import com.rs.game.player.actions.thieving.PickPocketAction;
import com.rs.game.player.actions.thieving.PickPocketableNPC;
import com.rs.game.player.content.PlayerDesign;
import com.rs.game.player.content.PlayerLook;
import com.rs.game.player.content.custom.RandomEventNPC;
import com.rs.game.player.content.dungeoneering.DungeonRewardShop;
import com.rs.game.player.content.interfaces.ViewNPCDrops;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.player.content.prestige.PrestigeSystem;
import com.rs.game.player.content.prifiddinas.PrifddinasCity;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.NPCExamines;
import com.rs.utils.ShopsHandler;

public class NPCHandler {

	public static void handleExamine(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		if (forceRun)
			player.setRun(forceRun);
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		if (player.isToggleDrops() && npc.getDefinitions().hasAttackOption()) {
			ViewNPCDrops.sendInterface(player, npc.getId());
		}
		if (npc instanceof Pet) {
			Pet pet = (Pet) npc;
			if (pet.isASkillingPet(pet)) {
				PetManager.sendXPMessage(player, pet);
				return;
			}
		}
		player.getPackets().sendNPCMessage(npc, NPCExamines.getExamine(npc));
		if (Settings.DEBUG)
			Logger.log("NPCHandler", "examined npc: " + npcIndex+", "+npc.getId());
	}
	
	public static void handleOption1(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				player.faceEntity(npc);
				if (!player.getControllerManager().processNPCClick1(npc))
					return;
				FishingSpots spot = FishingSpots.forId(npc.getId() | 1 << 24);
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return; // its a spot, they wont face us
				}
				npc.faceEntity(player);
				if (npc instanceof Familiar) {
					if (npc.getDefinitions().hasOption("store")) {
						player.getFamiliar().getBob().open();
					}
					if (npc.getDefinitions().hasOption("interact") && npc.getId() != 6873) {
						if (player.getFamiliar() != npc) {
							player.getPackets().sendGameMessage("That isn't your familiar.", true);
							return;
						}
						player.getDialogueManager().startDialogue("SimplePlayerMessage", "Why would I talk to a familiar? That's just weird.");
					}
					return;
				}
				if (PrifddinasCity.handleNPCOption1(player, npc)) {
					return;
				}
				if (npc instanceof Wisp) {
					Wisp wisp = (Wisp) npc;
					wisp.harvest(player);
					return;
				}
				if (RandomEventNPC.handleNPC(player, npc)) {
					return;
				}
				if (npc.getId() == 529) {
					ShopsHandler.openShop(player, 1);
					return;
				}
				if (npc instanceof GraveStone) {
					final GraveStone grave = (GraveStone) npc;
					grave.sendGraveInscription(player);
					return;
				}
				if (npc.getName().toLowerCase().contains("estate agent")) {
					player.getDialogueManager().startDialogue("EstateAgent", npc.getId());
					return;
				}
				if (npc.getName().toLowerCase().contains("grand exchange")) {
					if (player.withinDistance(npc, 2)) {
						player.getGEManager().openGrandExchange();
						npc.faceEntity(player);
						player.faceEntity(npc);
						return;
					}
				}
				if (npc.getDefinitions().name.toLowerCase().contains("bank")) {
					if (player.withinDistance(npc, 2)) {
						npc.faceEntity(player);
						player.faceEntity(npc);
						player.getBank().openBank();
						return;
					}
				}
				if (SiphonActionCreatures.siphon(player, npc))  {
					return;
				}
				switch (npc.getId()) {
				case 25715:
					if (player.talkedToJade == false) {
						player.getDialogueManager().startDialogue("Prestige", 1);
					}
					if (player.talkedToJade == true) {
						player.getDialogueManager().startDialogue("Prestige", 2);
					}
					return;
				case 17143:
					player.getDialogueManager().startDialogue("Ocellus", npc.getId(), (byte) 1);
					return;
				case 526:
				case 529:
					ShopsHandler.openShop(player, 1);
					return;
				case 3705:
					player.getDialogueManager().startDialogue("Max", npc.getId());
					return;
				case 9462:
				case 9464:
				case 9466:
					Strykewyrm.handleStomping(player, npc);
					break;
				case 8837:
				case 8838:
				case 8839:
					player.getActionManager().setAction(new LivingMineralMining((LivingRock) npc));
					return;
				case 15264:
					player.getDialogueManager().startDialogue("EvilChicken", npc.getId());
					return;
				case 9044:
					player.getDialogueManager().startDialogue("AssassinMaster", npc.getId());
					return;
				case 519:
					player.getDialogueManager().startDialogue("AchievementD", npc.getId()); 
					return;
				case 606:
					player.getDialogueManager().startDialogue("SuggestionD", npc.getId());
					return;
				case 5141:
					player.getDialogueManager().startDialogue("UgiD", npc);
					return;
				case 2824:
					player.getDialogueManager().startDialogue("EllisD", npc.getId()); 
					return;
				case 9085:
					player.getDialogueManager().startDialogue("Kuradal", npc.getId()); 
					return;
				case 17065:
					player.getDialogueManager().startDialogue("Quercus", npc.getId(), 1);
					return;
				case 15513:
				case 11303:
				case 11304:
				case 11305:
				case 11306:
				case 11307:
					player.getDialogueManager().startDialogue("ServantDialogue", npc.getId());
					return;
				case 945:
					PlayerDesign.open(player);
					player.getHintIconsManager().removeUnsavedHintIcon();	
					return;
				}
				if (npc instanceof Pet) {
					Pet pet = (Pet) npc;
					if (pet != player.getPet()) {
						player.getPackets().sendGameMessage("This isn't your pet.");
						return;
					}
					if (pet.getDefinitions().hasOption("Pickup") || pet.getDefinitions().hasOption("Pick-up"))
					player.setNextAnimation(new Animation(827));
					pet.pickup();
				}
				else {
					player.getDialogueManager().startDialogue("SimpleNPCMessage", npc.getId(), "I do not have any features added to me quite yet. However my id is " + npc.getId() + ", if I'm missing something important just show a developer.");
					if (Settings.DEBUG)
						Logger.log("Option 1 : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
				}
			}
		}));
	}
		public static void handleOption2(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if(forceRun)
			player.setRun(forceRun);
		if (npc.getDefinitions().name.contains("Banker")
				|| npc.getDefinitions().name.contains("banker")) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2))
				return;
			npc.faceEntity(player);
			player.getBank().openBank();
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				player.faceEntity(npc);
				FishingSpots spot = FishingSpots.forId(npc.getId() | (2 << 24));
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return;
				}
				PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
				if (pocket != null) {
					player.getActionManager().setAction(new PickPocketAction(npc, pocket));
					return;
				}
				if (npc instanceof Familiar) {
					if (npc.getDefinitions().hasOption("store")) {
						if (player.getFamiliar() != npc) {
							player.sm("That isn't your familiar.");
							return;
						}
						player.getFamiliar().store();
					} else if (npc.getDefinitions().hasOption("cure")) {
						if (player.getFamiliar() != npc) {
							player.sm("That isn't your familiar.");
							return;
						}
						if (!player.getPoison().isPoisoned()) {
							player.sm("Your arent poisoned or diseased.");
							return;
						} else {
							player.getFamiliar().drainSpecial(2);
							player.addPoisonImmune(120);
						}
					}
					return;
				}
				if (npc instanceof GraveStone) {
					final GraveStone grave = (GraveStone) npc;
					grave.repair(player, false);
					return;
				}
				if (npc instanceof DesertLizard) {
					((DesertLizard) npc).useIceCooler(player);
				}
				if (PrifddinasCity.handleNPCOption2(player, npc)) {
					return;
				}
				npc.faceEntity(player);
				if (!player.getControllerManager().processNPCClick2(npc))
					return;
				switch (npc.getId()) {
				case 25715:
					PrestigeSystem.canPrestige(player);
					break;
				case 529:
					ShopsHandler.openShop(player, 1); 
					break;
				case 3797:
					player.getDialogueManager().startDialogue("RepairSquire");
					break;
				case 9711:
					DungeonRewardShop.openRewardShop(player); 
					break;
				case 2824:
					player.getDialogueManager().startDialogue("TanningD", npc.getId()); 
					break;
				case 9085:
					player.getSlayerManager().getTask();
					break;
				}
				switch (npc.getDefinitions().name.toLowerCase()) {
				case "void knight":
					CommendationExchange.openExchangeShop(player);
					break;
				}
				if (npc instanceof Pet) {
					if (npc != player.getPet()) {
						player.getPackets().sendGameMessage("This isn't your pet.");
						return;
					}
					Pet pet = player.getPet();
					player.getPackets().sendMessage(99, "Pet [id=" + pet.getId() 
							+ ", hunger=" + pet.getDetails().getHunger()
							+ ", growth=" + pet.getDetails().getGrowth()
							+ ", stage=" + pet.getDetails().getStage() + "].", player);
				}
				else {
					if (Settings.DEBUG)
						System.out.println("Option 2 : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
						Logger.log("Option 2 : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
				}
			}
		}));
	}

	public static void handleOption3(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if(forceRun)
			player.setRun(forceRun);
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!player.getControllerManager().processNPCClick3(npc))
					return;
				player.faceEntity(npc);
				if (npc.getId() >= 8837 && npc.getId() <= 8839) {
					MiningBase.propect(player, "You examine the remains...", "The remains contain traces of living minerals.");
					return;
				}
				if (npc instanceof GraveStone) {
					final GraveStone grave = (GraveStone) npc;
					grave.repair(player, true);
					return;
				}
				if (PrifddinasCity.handleNPCOption3(player, npc)) {
					return;
				}
				if (npc.getDefinitions().name.toLowerCase().contains("banker") || npc.getId() == 15194) {
					if (player.withinDistance(npc, 2)) {
						npc.faceEntity(player);
						player.getGEManager().openCollectionBox();
						player.faceEntity(npc);
						return;
					}
				}
				npc.faceEntity(player);
				 if (npc.getId() == 548)
					PlayerLook.openThessaliasMakeOver(player);
			}
		}));
		if (Settings.DEBUG)
			System.out.println("Option 3 : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
			Logger.log("Option 3 : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
	}

	public static void handleOption4(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		player.stopAll(false);
		if (forceRun) {
			player.setRun(forceRun);
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!player.getControllerManager().processNPCClick4(npc)) {
					return;
				}
				if (npc instanceof GraveStone) {
					final GraveStone grave = (GraveStone) npc;
					grave.demolish(player);
					return;
				}
				player.faceEntity(npc);
				npc.faceEntity(player);
				if (npc.getId() == 9085) {
					SlayerTask.openSlayerShop(player);
					return;
				}
				if (PrifddinasCity.handleNPCOption4(player, npc)) {
					return;
				}
				if (npc.getName().toLowerCase().contains("grand exchange")) {
					if (player.withinDistance(npc, 2)) {
						player.getGEManager().openItemSets();
						npc.faceEntity(player);
						player.faceEntity(npc);
						return;
					}
				}
			}
		}));
			if (Settings.DEBUG)
				System.out.println("Option 4 : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
				Logger.log("Option 4 : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
	}
}
