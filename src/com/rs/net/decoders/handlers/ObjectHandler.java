package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.Crucible;
import com.rs.game.minigames.FightPits;
import com.rs.game.minigames.custom.BossMinigame;
import com.rs.game.minigames.pest.Lander;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.actions.CowMilkingAction;
import com.rs.game.player.actions.WaterFilling;
import com.rs.game.player.actions.WaterFilling.Fill;
import com.rs.game.player.actions.agility.ApeAgility;
import com.rs.game.player.actions.agility.BarbarianOutpostAgility;
import com.rs.game.player.actions.agility.GnomeAgility;
import com.rs.game.player.actions.agility.WildernessAgility;
import com.rs.game.player.actions.cooking.Cooking;
import com.rs.game.player.actions.cooking.Cooking.Cookables;
import com.rs.game.player.actions.crafting.JewellerySmithing;
import com.rs.game.player.actions.crafting.RobustGlassCrafting;
import com.rs.game.player.actions.divination.impl.DivineFishing;
import com.rs.game.player.actions.divination.impl.DivineHerblore;
import com.rs.game.player.actions.divination.impl.DivineHunting;
import com.rs.game.player.actions.divination.impl.DivineMining;
import com.rs.game.player.actions.divination.impl.DivineSimulacrum;
import com.rs.game.player.actions.farming.FarmingManager.SpotInfo;
import com.rs.game.player.actions.firemaking.Bonfire;
import com.rs.game.player.actions.hunter.TrapAction;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.actions.mining.EssenceMining;
import com.rs.game.player.actions.mining.Mining;
import com.rs.game.player.actions.mining.MiningBase;
import com.rs.game.player.actions.mining.EssenceMining.EssenceDefinitions;
import com.rs.game.player.actions.mining.Mining.RockDefinitions;
import com.rs.game.player.actions.objects.House;
import com.rs.game.player.actions.objects.Varrock;
import com.rs.game.player.actions.runecrafting.Runecrafting;
import com.rs.game.player.actions.runecrafting.SiphonActionNodes;
import com.rs.game.player.actions.smithing.Smelting;
import com.rs.game.player.actions.smithing.Smithing.ForgingBar;
import com.rs.game.player.actions.smithing.Smithing.ForgingInterface;
import com.rs.game.player.actions.smithing.rework.NewOres;
import com.rs.game.player.actions.smithing.rework.SmeltingNew;
import com.rs.game.player.actions.smithing.rework.SmeltingNew.SmeltingBarNew;
import com.rs.game.player.actions.smithing.rework.SmithingConstants;
import com.rs.game.player.actions.smithing.rework.SmithingNew;
import com.rs.game.player.actions.smithing.rework.SmithingNew.ForgingBarNew;
import com.rs.game.player.actions.summoning.Summoning;
import com.rs.game.player.actions.thieving.Thieving;
import com.rs.game.player.actions.woodcutting.Woodcutting;
import com.rs.game.player.actions.woodcutting.Woodcutting.TreeDefinitions;
import com.rs.game.player.content.BonesOnAltar;
import com.rs.game.player.content.ClueScrolls;
//import com.rs.game.player.content.CompletionistCape;
import com.rs.game.player.content.FairyRing;
import com.rs.game.player.content.PartyRoom;
import com.rs.game.player.content.PolyporeDungeon;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.RepairItems.BrokenItems;
import com.rs.game.player.content.SpiritTree;
import com.rs.game.player.content.activities.XPWell;
import com.rs.game.player.content.activities.events.ShootingStar;
import com.rs.game.player.content.activities.events.WorldEvents;
import com.rs.game.player.content.activities.warbands.LootingAction;
import com.rs.game.player.content.activities.warbands.Warbands;
import com.rs.game.player.content.BonesOnAltar.Bones;
import com.rs.game.player.content.dungeons.ResourceDungeons;
import com.rs.game.player.content.interfaces.SophanemChestInterface;
import com.rs.game.player.content.opening.CrystalChest;
import com.rs.game.player.content.prifiddinas.PrifddinasCity;
import com.rs.game.player.controllers.Falconry;
import com.rs.game.player.controllers.FightCaves;
import com.rs.game.player.controllers.FightKiln;
import com.rs.game.player.controllers.NomadsRequiem;
import com.rs.game.player.controllers.PestInvasion;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.player.controllers.instances.BandosInstance;
import com.rs.game.player.controllers.minigames.Barrows;
import com.rs.game.player.dialogues.impl.MiningGuildDwarf;
import com.rs.game.player.dialogues.impl.quests.CooksAssistant;
import com.rs.game.player.managers.QuestManager.Quests;
import com.rs.game.player.shortcuts.AncientCavern;
import com.rs.game.player.shortcuts.GrandExchange;
import com.rs.game.player.shortcuts.KuradalDungeon;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.PkRank;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

public final class ObjectHandler {

	private ObjectHandler() {

	}

	public static void handleOption(final Player player, InputStream stream, int option) {
		if (!player.clientHasLoadedMapRegion() || player.isDead()) {
			return;
		}
		if (player.isLocked() || player.getEmotesManager().getNextEmoteEnd() >= Utils.currentTimeMillis()) {
			return;
		}

		boolean forceRun = stream.readUnsignedByte128() == 1;
		final int id = stream.readIntLE();
		int x = stream.readUnsignedShortLE();
		int y = stream.readUnsignedShortLE128();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId)) {
			return;
		}
		WorldObject mapObject = World.getObjectWithId(tile, id);
		if (mapObject == null || mapObject.getId() != id) {
			return;
		}
		final WorldObject object = mapObject;
		player.stopAll();
		if (forceRun) {
			player.setRun(forceRun);
		}
		switch (option) {
		case 1:
			handleOption1(player, object);
			break;
		case 2:
			handleOption2(player, object);
			break;
		case 3:
			handleOption3(player, object);
			break;
		case 4:
			handleOption4(player, object);
			break;
		case 5:
			handleOption5(player, object);
			break;
		case -1:
			handleOptionExamine(player, object);
			break;
		}
	}

	private static void handleOption1(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		final int x = object.getX();
		final int y = object.getY();
		final int plane = object.getPlane();
		if (SiphonActionNodes.siphion(player, object))
			return;
		if (id == 75463) {
			player.getControllerManager().processObjectClick1(object);
			return;
		}
		if (id == 31299) { //market signpost
			CrystalChest.openRewardsInterface(player);
			return;
		}
		if (id == 67968 || id == 94067) {
			RobustGlassCrafting.addSandstone(player);
			return;
		}
		if (id == 1816) {
			player.getDialogueManager().startDialogue("KingBlackDragon");
			return;
		}
		if (id == 3219) {
			player.getPorts().enterPorts();
			return;
		}
		if (PrifddinasCity.handleObjectOption1(player, object)) {
			return;
		}
		/**
		 * Araxxor objects.
		 */
		if (id == 91553) { //climb rope - up
			player.useStairs(828, new WorldTile(3700, 3419, 0), 1, 2);
			return;
		}
		if (id == 91557) { //enter cave - down
			player.useStairs(-1, new WorldTile(4512, 6289, 1), 0, 0);
			return;
		}
		if (id == 91661) { //cross gap
			if (player.getX() >= 4511) {
				player.useStairs(1603, new WorldTile(4506, player.getY(), 1), 1, 1);
				return;
			}
			player.useStairs(1603, new WorldTile(4511, player.getY(), 1), 1, 1);
			return;
		}
		if (id == 91500) { //webbed entrance
			player.getDialogueManager().startDialogue("AraxHyveD");
			return;
		}
		/**
		 * Godwars 2:
		 */
		if (id == 101865) {
			player.setCoordsEvent(new CoordsEvent(object, () -> {
				player.getDialogueManager().startDialogue("WoodenLiftD");
			}, true));
			return;
		}
		if (id == 101766) {
			player.setCoordsEvent(new CoordsEvent(object, () -> {
				WorldObject lift = new WorldObject(101865, 10, 3, new WorldTile(3198, 6935, 1));
				player.setNextWorldTile(new WorldTile(3198, 6940, 1));
				player.setNextAnimation(new Animation(26446));
				player.faceObject(lift);
			}, true));
			return;
		}
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControllerManager().processObjectClick1(object))
					return;
				if (player.getFarmingManager( ).isFarming( id, null, 1 ))
					return;
				if (House.isObject(object)) {
					House.HandleObject(player, object);
					return;
				}
				if (TrapAction.isTrap(player, object, id) || TrapAction.isTrap(player, object)) {
					return;
				}
				if (id == 82049) {
					if (player.isKalphiteLairSetted())
						player.useStairs(19507, new WorldTile(3420, 9510, 0), 4, 5, null);
					else if (player.getInventory().containsItem(954, 1)) {
						player.getInventory().deleteItem(954, 1);
						player.setKalphiteLair();
					} else
						player.getPackets().sendGameMessage("You need a rope to climb down.");
				} else if (id == 48803) {
					if (player.isKalphiteLairEntranceSetted())
						player.getDialogueManager().startDialogue("KalphiteQueen");
					else
						player.getPackets().sendGameMessage("You need a rope to climb down.");
				}
				if (id == 82014) {
					player.getDialogueManager().startDialogue("KalphiteKing");
				}
				if (Warbands.warband != null) {
					if (Warbands.warband.objectHasResources(object)) {
						LootingAction.loot(player, object);
						return;
					}
				}
				if (object.getDefinitions().name.equalsIgnoreCase("furnace")) {
					ForgingBar bar = ForgingBar.getBar(player);
					if (!player.getInventory().containsOneItem(SmeltingNew.NEW_ORES) && player.getInventory().containsOneItem(Smelting.OLD_ORES)) {
						player.getDialogueManager().startDialogue("SmeltingD", object);
						return;
					}
					if (player.getInventory().containsOneItem(Smelting.OLD_ORES) && player.getInventory().containsOneItem(SmeltingNew.NEW_ORES)) {
						player.getDialogueManager().startDialogue("SmeltingChoice", object);
						return;
					}
					if (!player.getInventory().containsOneItem(Smelting.OLD_ORES) && player.getInventory().containsOneItem(SmeltingNew.NEW_ORES)) {
						player.getDialogueManager().startDialogue("SmeltingNewD", object);
						return;
					}
					if (bar == ForgingBar.STEEL) {
						player.getActionManager().setAction(new Smelting(SkillsDialogue.getItemSlot(23), object, player.getInventory().getAmountOf(bar.getBarId())));
						return;
					}
					return;
				}
				if (object.getId() == 109138) { //Sophanem slayer dungeon entrance
					player.setNextWorldTile(new WorldTile(2384, 6793, 3));
					player.getControllerManager().startController("SophanemSlayerDungeon");
				}
				if (object.getId() == 109139) { //Sophanem slayer dungeon exit
					player.setNextWorldTile(new WorldTile(3289, 2709, 0));
					player.getControllerManager().forceStop();
				}	
				if (object.getId() == 109141) { //Font of el
					player.sendMessage("The font cleanses all of your corruption.");
					player.setNextAnimation(new Animation(725));
					player.getStatistics().setCorruption(0);
				}
				if (object.getId() == 92278) {
					player.getPrayer().restorePrayer(player.getSkills().getLevel(Skills.PRAYER) * 10);
					player.getSkills().restoreSummoning();
				}
				if (object.getId() == 109140) { //Slayer dungeon chest
					player.getInterfaceManager().sendInterface(new SophanemChestInterface(player));
				}
				switch (id) {
				case 96780:
					WorldObject objectdoor = new WorldObject(92278, 10, 3, 2275, 3303, 1);
					player.faceObject(objectdoor);
					player.getControllerManager().startController("DeathEventRS3");
					break;
				case 91688:
					WorldEvents.sendNoticeboard(player);
					break;
				case 90223:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_CRAYFISH, x, y, plane, object));
					break;
				case 90224:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_HERRING, x, y, plane, object));
					break;
				case 90225:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_TROUT ,x,y,plane, object));
					break;
				case 90226:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_SALMON ,x,y,plane, object));
					break;
				case 90227:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_LOBSTER ,x,y,plane, object));
					break;
				case 90228:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_SWORDFISH ,x,y,plane, object));
					break;
				case 90229:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_SHARK ,x,y,plane, object));
					break;
				case 90230:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_CAVEFISH ,x,y,plane, object));
					break;
				case 90231:
				    player.getActionManager().setAction(new DivineFishing(DivineFishing.DivineFishingSpots.DIVINE_ROCKTAIL ,x,y,plane, object));
					break;
				case 87280:
				    player.getActionManager().setAction(new DivineHerblore(DivineHerblore.DivineHerbSpots.DIVINE_HERB_I ,x,y,plane, object));
					break;
				case 87281:
				    player.getActionManager().setAction(new DivineHerblore(DivineHerblore.DivineHerbSpots.DIVINE_HERB_II ,x,y,plane, object));
					break;
				case 87282:
				    player.getActionManager().setAction(new DivineHerblore(DivineHerblore.DivineHerbSpots.DIVINE_HERB_III ,x,y,plane, object));
					break;
				case 87270:
				    player.getActionManager().setAction(new DivineHunting(DivineHunting.DivineHuntingSpots.DIVINE_KEBBIT_BURROW ,x,y,plane, object));
					break;
				case 87271:
				    player.getActionManager().setAction(new DivineHunting(DivineHunting.DivineHuntingSpots.DIVINE_BIRD_SNARE ,x,y,plane, object));
					break;
				case 87272:
				    player.getActionManager().setAction(new DivineHunting(DivineHunting.DivineHuntingSpots.DIVINE_DEADFALL_TRAP ,x,y,plane, object));
					break;
				case 87273:
				    player.getActionManager().setAction(new DivineHunting(DivineHunting.DivineHuntingSpots.DIVINE_BOX_TRAP,x, y, plane, object));
					break;
				case 66528:
				    player.getActionManager().setAction(new DivineSimulacrum(DivineSimulacrum.DivineSimulacrumSpots.DIVINE_SIMULACRUM_I,x, y, plane, object));
					break;
				case 66531:
				    player.getActionManager().setAction(new DivineSimulacrum(DivineSimulacrum.DivineSimulacrumSpots.DIVINE_SIMULACRUM_II,x, y, plane, object));
					break;
				case 102758:
					if (Lander.canEnter(player, 0))
					break;
				case 102760:
					if (Lander.canEnter(player, 1))
					break;
				case 102761:
					if (Lander.canEnter(player, 2))
					break;
				case 2563:
					player.getDialogueManager().startDialogue("CapeStand");
					break;
				case 44379:
					player.setNextWorldTile(new WorldTile(3380, 3513, 0));
					break;
				case 540:
					player.getDialogueManager().startDialogue("ConstructionTable");
					break;
				case 39468:
					player.setNextWorldTile(new WorldTile(1745, 5325, 0));
					break;
				case 84726:
					player.getDialogueManager().startDialogue("LegioPrimus");
					break;
				case 26341:
					player.useStairs(828, new WorldTile(2882, 5311, 0), 0, 0);
					player.getControllerManager().startController("GodWars");
					break;
				case 25337:
					player.setNextWorldTile(new WorldTile(1694, 5296, 1));
					break;
				case 25338:
					player.setNextWorldTile(new WorldTile(1772, 5366, 0));
					break;
				case 57225:
					player.getDialogueManager().startDialogue("NexEntrance");
					break;
				case 15482:
				case 15487:
					  player.getDialogueManager().startDialogue("HousePortal");
					break;
				case 38279:
					player.getDialogueManager().startDialogue("RunespanPortalD");
					break;
				case 66115:
				case 66116:
					player.resetWalkSteps();
					player.setNextAnimation(new Animation(830));
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.unlock();
							if (Barrows.digIntoGrave(player))
								return;
							player.getPackets().sendGameMessage("You find nothing.");
						}
					});
					break;
				case 12277:
					player.getActionManager().setAction( new Woodcutting(object, TreeDefinitions.STRAIT_VINE_COLLECTABLE));
					break;
				case 12291:
					player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MUTATED_VINE));
					break;
				case 22119:
					player.getControllerManager().startController("BarrelchestControler");
					break;
				case 12328:
					player.useStairs( 3527, new WorldTile( 3012, 9275, 0 ), 5, 6 );
					player.setNextForceMovement( new ForceMovement( player, 3, object, 2, ForceMovement.WEST ) );
					WorldTasksManager.schedule( new WorldTask( ) {

						@Override
						public void run( ) {
							player.setNextFaceWorldTile(new WorldTile( 3012, 9274, 0));
							player.setNextAnimation(new Animation(11043));
							player.getControllerManager().startController("JadinkoLair");
						}
					}, 4 );
					break;
				case 1317:
				case 68793:
					player.getDialogueManager().startDialogue("SpiritTreeDialogue", object.getId());
					break;
				case 12309:
					ShopsHandler.openShop(player, player.isKilledCulinaromancer() ? 42 : player.isKilledDessourt() ? 41 : player.isKilledKaramel() ? 40 : player.isKilledFlambeed() ? 39 : player.isKilledAgrithNaNa() ? 38 : 37);
					break;
				case 19222:
					Falconry.beginFalconry(player);
					break;
				}
				if (id == 12290 || id == 12272) {
				if (id == 12290)
					player.setFavorPoints( 1 - player.getFavorPoints( ) );
				player.getActionManager( ).setAction( new Woodcutting( object, TreeDefinitions.STRAIT_VINE ) );
			}
			else if (id == 64360 && x == 4629 && y == 5453)
				PolyporeDungeon.useStairs(player, new WorldTile(4629, 5451, 2), true);
			else if (id == 64361 && x == 4629 && y == 5452)
				PolyporeDungeon.useStairs(player, new WorldTile(4629, 5454, 3), false);
			else if (id == 64359 && x == 4632 && y == 5443)
				PolyporeDungeon.useStairs(player, new WorldTile(4632, 5443, 1), true);
			else if (id == 64361 && x == 4632 && y == 5442)
				PolyporeDungeon.useStairs(player, new WorldTile(4632, 5444, 2), false);
			else if (id == 64359 && x == 4632 && y == 5409)
				PolyporeDungeon.useStairs(player, new WorldTile(4632, 5409, 2), true);
			else if (id == 64361 && x == 4633 && y == 5409)
				PolyporeDungeon.useStairs(player, new WorldTile(4631, 5409, 3), false);
			else if (id == 64359 && x == 4642 && y == 5389)
				PolyporeDungeon.useStairs(player, new WorldTile(4642, 5389, 1), true);
			else if (id == 64361 && x == 4643 && y == 5389)
				PolyporeDungeon.useStairs(player, new WorldTile(4641, 5389, 2), false);
			else if (id == 64359 && x == 4652 && y == 5388)
				PolyporeDungeon.useStairs(player, new WorldTile(4652, 5388, 0), true);
			else if (id == 64362 && x == 4652 && y == 5387)
				PolyporeDungeon.useStairs(player, new WorldTile(4652, 5389, 1), false);
			else if (id == 64359 && x == 4691 && y == 5469)
				PolyporeDungeon.useStairs(player, new WorldTile(4691, 5469, 2), true);
			else if (id == 64361 && x == 4691 && y == 5468)
				PolyporeDungeon.useStairs(player, new WorldTile(4691, 5470, 3), false);
			else if (id == 64359 && x == 4689 && y == 5479)
				PolyporeDungeon.useStairs(player, new WorldTile(4689, 5479, 1), true);
			else if (id == 64361 && x == 4689 && y == 5480)
				PolyporeDungeon.useStairs(player, new WorldTile(4689, 5478, 2), false);
			else if (id == 64359 && x == 4698 && y == 5459)
				PolyporeDungeon.useStairs(player, new WorldTile(4698, 5459, 2), true);
			else if (id == 64361 && x == 4699 && y == 5459)
				PolyporeDungeon.useStairs(player, new WorldTile(4697, 5459, 3), false);
			else if (id == 64359 && x == 4705 && y == 5460)
				PolyporeDungeon.useStairs(player, new WorldTile(4704, 5461, 1), true);
			else if (id == 64361 && x == 4705 && y == 5461)
				PolyporeDungeon.useStairs(player, new WorldTile(4705, 5459, 2), false);
			else if (id == 64359 && x == 4718 && y == 5467)
				PolyporeDungeon.useStairs(player, new WorldTile(4718, 5467, 0), true);
			else if (id == 64361 && x == 4718 && y == 5466)
				PolyporeDungeon.useStairs(player, new WorldTile(4718, 5468, 1), false);
			else if (id >= 15477 && id <= 15482)
				player.getDialogueManager().startDialogue("HousePortal");
				if (ClueScrolls.objectSpot(player, object))
					return;
				if (CastleWars.handleObjects(player, id))
					return;
				if (ResourceDungeons.handleObjects(player, id))
					return;
				if (Varrock.isObject(object))
					Varrock.HandleObject(player, object);
				if (AncientCavern.climbDown(player, object))
					return;
				if (object.getId() == 49916 && player.getX() == 2969 && player.getY() == 9696)
					BossMinigame.addPlayer(player);
				if (id == 12349 || id == 12350 || id == 12356) {
					if (player.isKilledCulinaromancer()) {
						player.getDialogueManager().startDialogue("RfdPortal", true);
						return;
					}
					player.getControllerManager().startController("ImpossibleJadControler");
					return;
				}
				if (id >= 15477 && id <= 15482) {
					if (player.hasHouse) {
					player.getHouse().setBuildMode(false);
					player.getHouse().enterMyHouse();
					return;
				}
				player.sm("You must first purchase a house from the Estate Agent.");
				}
				// Below's method empty's the compost
				else if (object.getId() == 7836 || object.getId() == 7837) {
					if (player.getInventory().containsItem(6055, 1)) {
						player.setNextAnimation(new Animation(2292));
						player.getInventory().deleteItem(6055, 1);
						player.sm("You empty your weeds into the compost bin.");
						player.getPackets().sendConfigByFile(740, 15, true);
						player.getSkills().addXp(Skills.FARMING, 1);
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage",
								"You can only empty weeds into the compost bin.");
					}

				}
				if (object.getId() == 49916 && player.getX() == 2972 && player.getY() == 9696)
					BossMinigame.removePlayer(player);

				if (object.getId() == 11162)

				if (object.getId() == 11163)

				if (object.getId() == 9311)
					GrandExchange.geshortcut(player);
				
				if (object.getId() == 9312)
					GrandExchange.geshortcut2(player);

				if (object.getId() == 47232)
					player.setNextWorldTile(new WorldTile(1661, 5257, 0));

				if (object.getId() == 47231)
					player.setNextWorldTile(new WorldTile(1735, 5313, 1));

				if (object.getId() == 47237) {
					KuradalDungeon.crossGap(player, object);
				}

				if (object.getId() == 47233) {
					KuradalDungeon.crossGaps(player, object);
				}
				/**
				 * Crystal Chest
				 */

				if (object.getId() == 35470 && player.getInventory().containsItem(989, 1) && player.getInventory().hasFreeSlots()) {
					CrystalChest.openChest(object, player);
					player.getInventory().deleteItem(989, 1);
				} else if (object.getId() == 35470) {
					if (!player.getInventory().hasFreeSlots()) {
						player.sm("Unfortunately, you must at least have an available slot.");
					} else if (object.getId() == 35470) {
						if (!player.getInventory().containsItem(989, 1)) {
							player.sm("This requires you to have a crystal key to open the chest.");
						}
					}
				} else if (id == 70794)
					player.setNextWorldTile(new WorldTile(1340, 6488, 0));
				else if (id == 70796)
					player.setNextWorldTile(new WorldTile(1090, 6360, 0));
				else if (id == 70797)
					player.setNextWorldTile(new WorldTile(1090, 6597, 0));
				else if (id == 70798)
					player.setNextWorldTile(new WorldTile(1340, 6497, 0));
				else if (id == 70799) {
					if (player.getSkills().getLevelForXp(Skills.AGILITY) < 60) {
						player.getPackets().sendGameMessage("You need an Agility level of 60 to do this.");
						return;
					}
					player.setNextWorldTile(new WorldTile(1178, 6356, 0));
				} else if (id == 70795) {
					if (player.getSkills().getLevelForXp(Skills.AGILITY) < 60) {
						player.getPackets().sendGameMessage("You need an Agility level of 60 to use this shorcut.");
						return;
					}
					player.getDialogueManager().startDialogue("GrotDungeonAgility");
				}
				else if (id == 69827) {
				player.activateLodeStone(object, player);
			} else if (id == 69828) {
				player.activateLodeStone(object, player);
			} else if (id == 69829) {
				player.activateLodeStone(object, player);
			} else if (id == 69830) {
				player.activateLodeStone(object, player);
			} else if (id == 69831) {
				player.activateLodeStone(object, player);
			} else if (id == 69832) {
				player.activateLodeStone(object, player);
			} else if (id == 69833) {
				player.activateLodeStone(object, player);
			} else if (id == 69834) {
				player.activateLodeStone(object, player);
			} else if (id == 69835) {
				player.activateLodeStone(object, player);
			} else if (id == 69837) {
				player.activateLodeStone(object, player);
			} else if (id == 69838) {
				player.activateLodeStone(object, player);
			} else if (id == 69839) {
				player.activateLodeStone(object, player);
			} else if (id == 69840) {
				player.activateLodeStone(object, player);
			} else if (id == 69841) {
				player.activateLodeStone(object, player);
			}
				// End Lodestones

				if (id == 47236) {
					KuradalDungeon.handleBarriers(player, object);

				} else if (id == 2350 && (object.getX() == 3352 && object.getY() == 3417 && object.getPlane() == 0))
					player.useStairs(832, new WorldTile(3177, 5731, 0), 1, 2);
				else if (id == 2353 && (object.getX() == 3177 && object.getY() == 5730 && object.getPlane() == 0))
					player.useStairs(828, new WorldTile(3353, 3416, 0), 1, 2);
				else if (id == 11554 || id == 11552)
					player.getPackets().sendGameMessage("That rock is currently unavailable.");
				else if (id == 38279)
					player.getDialogueManager().startDialogue("RunespanPortalD");

				else if (id == 2491)
					player.getActionManager()
							.setAction(new EssenceMining(object, player.getSkills().getLevel(Skills.MINING) < 30
									? EssenceDefinitions.Rune_Essence : EssenceDefinitions.Pure_Essence));
				else if (id == 2478)
					Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2, 22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77,
							88, 9, 99, 10);
				else if (id == 2479)
					Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14, 2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98,
							8);
				else if (id == 2480)
					Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2, 38, 3, 57, 4, 76, 5, 95, 6);
				else if (id == 2481)
					Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26, 2, 52, 3, 78, 4);
				else if (id == 2482)
					Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2, 70, 3);
				else if (id == 2483)
					Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46, 2, 92, 3);
				else if (id == 2484)
					Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
				else if (id == 2487)
					Runecrafting.craftEssence(player, 562, 35, 8.5, true, 74, 2);
				else if (id == 17010)
					Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82, 2);
				else if (id == 2486)
					Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
				else if (id == 2485)
					Runecrafting.craftEssence(player, 563, 50, 9.5, true);
				else if (id == 2488)
					Runecrafting.craftEssence(player, 560, 65, 10, true);
				else if (id == 30624)
					Runecrafting.craftEssence(player, 565, 77, 10.5, true);
				else if (id == 2452) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.AIR_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1438, 1))
						Runecrafting.enterAirAltar(player);
				} else if (id == 2455) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.EARTH_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1440, 1))
						Runecrafting.enterEarthAltar(player);
				} else if (id == 2456) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.FIRE_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1442, 1))
						Runecrafting.enterFireAltar(player);
				} else if (id == 2454) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.WATER_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1444, 1))
						Runecrafting.enterWaterAltar(player);
				} else if (id == 2457) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.BODY_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1446, 1))
						Runecrafting.enterBodyAltar(player);
				} else if (id == 2453) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.MIND_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1448, 1))
						Runecrafting.enterMindAltar(player);
				} else if (id == 47721) {
					CooksAssistant.handleCowMilking(player);
				} else if (id == 2718) {
					if (!player.getInventory().containsItem(1931, 1) && player.hasGrainInHopper == true) {
						player.getPackets().sendGameMessage("You need a pot to fill the flour with.");
					} else if (player.getInventory().containsItem(1931, 1) && player.hasGrainInHopper == true) {
						player.getPackets().sendGameMessage("You operate the controls and fill your pot with flour.");
						player.getInventory().deleteItem(1931, 1);
						player.getInventory().addItem(1933, 1);
						player.hasGrainInHopper = false;
					} else {
						player.getPackets().sendGameMessage("You see no reason why you need to operate the controls.");
					}
				} else if (id == 47120) { // zaros altar
					// recharge if needed
					if (player.getPrayer().getPrayerpoints() < player.getSkills().getLevelForXp(Skills.PRAYER) * 10) {
						player.lock(12);
						player.setNextAnimation(new Animation(12563));
						player.getPrayer()
								.setPrayerpoints((int) ((player.getSkills().getLevelForXp(Skills.PRAYER) * 10) * 1.15));
						player.getPrayer().refreshPrayerPoints();
					}
					player.getDialogueManager().startDialogue("ZarosAltar");
				} else if (id == 19222)
					Falconry.beginFalconry(player);
				else if (id == 36786)
					player.getDialogueManager().startDialogue("Banker", 4907);
				else if (id == 42377 || id == 42378)
					player.getDialogueManager().startDialogue("Banker", 2759);
				else if (id == 42217 || id == 782 || id == 34752)
					player.getDialogueManager().startDialogue("Banker", 553);
				else if (id == 57437)
					player.getBank().openBank();
				else if (id == 42425 && object.getX() == 3220 && object.getY() == 3222) { // zaros
																							// portal
					player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5,
							"And you find yourself into a digsite.");
					player.addWalkSteps(3222, 3223, -1, false);
					player.getPackets().sendGameMessage("You examine portal and it aborves you...");
				} else if (id == 9356) {
					player.lock(3);
					FightCaves.enterFightCaves(player);
					return;
				}
				if (id == 68107)
					FightKiln.enterFightKiln(player, false);
				else if (id == 68223)
					FightPits.enterLobby(player, false);
				else if (id == 26898)
					PestInvasion.enterPestInvasion(player);
				else if (id == 46500 && object.getX() == 3351 && object.getY() == 3415) { // zaros
																							// portal
					player.useStairs(-1, new WorldTile(Settings.RESPAWN_PLAYER_LOCATION.getX(),
							Settings.RESPAWN_PLAYER_LOCATION.getY(), Settings.RESPAWN_PLAYER_LOCATION.getPlane()), 2, 3,
							"You found your way back to home.");
					player.addWalkSteps(3351, 3415, -1, false);
				} else if (id == 9293) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
						player.getPackets().sendGameMessage("You need an agility level of 70 to use this obstacle.",
								true);
						return;
					}
					int x = player.getX() == 2886 ? 2892 : 2886;
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							player.setNextAnimation(new Animation(844));
							if (count++ == 1)
								stop();
						}

					}, 0, 0);
					player.setNextForceMovement(
							new ForceMovement(new WorldTile(x, 9799, 0), 3, player.getX() == 2886 ? 1 : 3));
					player.useStairs(-1, new WorldTile(x, 9799, 0), 3, 4);
				} else if (id == 29370 && (object.getX() == 3150 || object.getX() == 3153) && object.getY() == 9906) { // edgeville
																														// dungeon
																														// cut
					if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
						player.getPackets().sendGameMessage("You need an agility level of 53 to use this obstacle.");
						return;
					}
					final boolean running = player.getRun();
					player.setRunHidden(false);
					player.lock(8);
					player.addWalkSteps(x == 3150 ? 3155 : 3149, 9906, -1, false);
					player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
					WorldTasksManager.schedule(new WorldTask() {
						boolean secondloop;

						@Override
						public void run() {
							if (!secondloop) {
								secondloop = true;
								player.getAppearence().setRenderEmote(295);
							} else {
								player.getAppearence().setRenderEmote(-1);
								player.setRunHidden(running);
								player.getSkills().addXp(Skills.AGILITY, 7);
								stop();
							}
						}
					}, 0, 5);
				}
				// start forinthry dungeon
				else if (id == 18341 && object.getX() == 3036 && object.getY() == 10172)
					player.useStairs(-1, new WorldTile(3039, 3765, 0), 0, 1);
				else if (id == 20599 && object.getX() == 3038 && object.getY() == 3761)
					player.useStairs(-1, new WorldTile(3037, 10171, 0), 0, 1);
				else if (id == 18342 && object.getX() == 3075 && object.getY() == 10057)
					player.useStairs(-1, new WorldTile(3071, 3649, 0), 0, 1);
				else if (id == 20600 && object.getX() == 3072 && object.getY() == 3648)
					player.useStairs(-1, new WorldTile(3077, 10058, 0), 0, 1);
				// nomads requiem
				else if (id == 18425 && !player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM))
					NomadsRequiem.enterNomadsRequiem(player);
				else if (id == 42219) {
					player.useStairs(-1, new WorldTile(1886, 3178, 0), 0, 1);
					if (player.getQuestManager().getQuestStage(Quests.NOMADS_REQUIEM) == -2) // for
																								// now,
																								// on
																								// future
																								// talk
																								// with
																								// npc
																								// +
																								// quest
																								// reqs
						player.getQuestManager().setQuestStageAndRefresh(Quests.NOMADS_REQUIEM, 0);
				} else if (id == 8689)
					player.getActionManager().setAction(new CowMilkingAction());
				else if (id == 42220)
					player.useStairs(-1, new WorldTile(3082, 3475, 0), 0, 1);
				// start falador mininig
				else if (id == 30942 && object.getX() == 3019 && object.getY() == 3450)
					player.useStairs(828, new WorldTile(3020, 9850, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3019 && object.getY() == 9850)
					player.useStairs(833, new WorldTile(3018, 3450, 0), 1, 2);
				else if (id == 31002 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS))
					player.useStairs(833, new WorldTile(2998, 3452, 0), 1, 2);
				else if (id == 31012 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS))
					player.useStairs(828, new WorldTile(2996, 9845, 0), 1, 2);
				else if (id == 30943 && object.getX() == 3059 && object.getY() == 9776)
					player.useStairs(-1, new WorldTile(3061, 3376, 0), 0, 1);
				else if (id == 30944 && object.getX() == 3059 && object.getY() == 3376)
					player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
				else if (id == 2112 && object.getX() == 3046 && object.getY() == 9756) {
				if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
					player.getDialogueManager().startDialogue("SimpleNPCMessage",
							MiningGuildDwarf.getClosestDwarfID(player),
							"Sorry, but you need level 60 Mining to go in there.");
					return;
				}
				handleDoor(player, object);
			} else if (id == 2113) {
				if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
					player.getDialogueManager().startDialogue("SimpleNPCMessage",
							MiningGuildDwarf.getClosestDwarfID(player),
							"Sorry, but you need level 60 Mining to go in there.");
					return;
				}
				player.useStairs(-1, new WorldTile(3021, 9739, 0), 0, 1);
				} else if (id == 2113) {
					if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage",
								MiningGuildDwarf.getClosestDwarfID(player),
								"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					player.useStairs(-1, new WorldTile(3021, 9739, 0), 0, 1);
				} else if (id == 6226 && object.getX() == 3019 && object.getY() == 9740)
					player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3019 && object.getY() == 9738)
					player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3018 && object.getY() == 9739)
					player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3020 && object.getY() == 9739)
					player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
				else if (id == 30963)
					player.getBank().openBank();
				else if (id == 6045)
					player.getPackets().sendGameMessage("You search the cart but find nothing.");
				else if (id == 5906) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
						player.getPackets().sendGameMessage("You need an agility level of 42 to use this obstacle.");
						return;
					}
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							if (count == 0) {
								player.setNextAnimation(new Animation(2594));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 2) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2),
										object.getY(), 0);
								player.setNextWorldTile(tile);
							} else if (count == 5) {
								player.setNextAnimation(new Animation(2590));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 7) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5),
										object.getY(), 0);
								player.setNextWorldTile(tile);
							} else if (count == 10) {
								player.setNextAnimation(new Animation(2595));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 12) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6),
										object.getY(), 0);
								player.setNextWorldTile(tile);
							} else if (count == 14) {
								stop();
								player.unlock();
							}
							count++;
						}

					}, 0, 0);
					// Wilderness course start
				} else if (id == 64698)
					WildernessAgility.walkAcrossLogBalance(player, object);
				else if (id == 64699)
					WildernessAgility.jumpSteppingStones(player, object);
				else if (id == 65362)
					WildernessAgility.enterWildernessPipe(player, object.getX(), object.getY());
				else if (id == 65734)
					WildernessAgility.climbUpWall(player, object);
				else if (id == 64696)
					WildernessAgility.swingOnRopeSwing(player, object);
				else if (id == 65365)
					WildernessAgility.enterWildernessCourse(player);
				else if (id == 65367)
					WildernessAgility.exitWildernessCourse(player);
				// BarbarianOutpostAgility start
				else if (id == 20210)
					BarbarianOutpostAgility.enterObstaclePipe(player, object);
				else if (id == 43526)
					BarbarianOutpostAgility.swingOnRopeSwing(player, object);
				else if (id == 43595 && x == 2550 && y == 3546)
					BarbarianOutpostAgility.walkAcrossLogBalance(player, object);
				else if (id == 20211 && x == 2538 && y == 3545)
					BarbarianOutpostAgility.climbObstacleNet(player, object);
				else if (id == 2302 && x == 2535 && y == 3547)
					BarbarianOutpostAgility.walkAcrossBalancingLedge(player, object);
				else if (id == 1948)
					BarbarianOutpostAgility.climbOverCrumblingWall(player, object);
				else if (id == 43533)
					BarbarianOutpostAgility.runUpWall(player, object);
				else if (id == 43597)
					BarbarianOutpostAgility.climbUpWall(player, object);
				else if (id == 43587)
					BarbarianOutpostAgility.fireSpringDevice(player, object);
				else if (id == 43527)
					BarbarianOutpostAgility.crossBalanceBeam(player, object);
				else if (id == 43531)
					BarbarianOutpostAgility.jumpOverGap(player, object);
				else if (id == 43532)
					BarbarianOutpostAgility.slideDownRoof(player, object);

				// rock living caverns
				else if (id == 45077) {
					player.lock();
					if (player.getX() != object.getX() || player.getY() != object.getY())
						player.addWalkSteps(object.getX(), object.getY(), -1, false);
					WorldTasksManager.schedule(new WorldTask() {

						private int count;

						@Override
						public void run() {
							if (count == 0) {
								player.setNextFaceWorldTile(new WorldTile(object.getX() - 1, object.getY(), 0));
								player.setNextAnimation(new Animation(12216));
								player.unlock();
							} else if (count == 2) {
								player.setNextWorldTile(new WorldTile(3651, 5122, 0));
								player.setNextFaceWorldTile(new WorldTile(3651, 5121, 0));
								player.setNextAnimation(new Animation(12217));
							} else if (count == 3) {
								// TODO find emote
								// player.getPackets().sendObjectAnimation(new
								// WorldObject(45078, 0, 3, 3651, 5123, 0), new
								// Animation(12220));
							} else if (count == 5) {
								player.unlock();
								stop();
							}
							count++;
						}

					}, 1, 0);
				} else if (id == 45076)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_GOLD_ORE));
				else if (id == 5999)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_COAL_ORE));
				else if (id == 45078)
					player.useStairs(2413, new WorldTile(3012, 9832, 0), 2, 2);
				// champion guild
				else if (id == 24357 && object.getX() == 3188 && object.getY() == 3355)
					player.useStairs(-1, new WorldTile(3189, 3354, 1), 0, 1);
				else if (id == 24359 && object.getX() == 3188 && object.getY() == 3355)
					player.useStairs(-1, new WorldTile(3189, 3358, 0), 0, 1);
			    else if (id == 1805 && object.getX() == 3191 && object.getY() == 3363)
				handleDoor(player, object);
				// start of varrock dungeon
				else if (id == 29355 && object.getX() == 3230 && object.getY() == 9904) // varrock
																						// dungeon
																						// climb
																						// to
																						// bear
					player.useStairs(828, new WorldTile(3229, 3503, 0), 1, 2);
				else if (id == 24264)
					player.useStairs(833, new WorldTile(3229, 9904, 0), 1, 2);
				else if (id == 24366)
					player.useStairs(828, new WorldTile(3237, 3459, 0), 1, 2);
				else if (id == 882 && object.getX() == 3237 && object.getY() == 3458)
					player.useStairs(833, new WorldTile(3237, 9858, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3097 && object.getY() == 9867) // edge
																						// dungeon
																						// climb
					player.useStairs(828, new WorldTile(3096, 3468, 0), 1, 2);
				else if (id == 26934)
					player.useStairs(833, new WorldTile(3097, 9868, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3088 && object.getY() == 9971)
					player.useStairs(828, new WorldTile(3087, 3571, 0), 1, 2);
				else if (id == 65453)
					player.useStairs(833, new WorldTile(3089, 9971, 0), 1, 2);
				else if (id == 12389 && object.getX() == 3116 && object.getY() == 3452)
					player.useStairs(833, new WorldTile(3117, 9852, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3116 && object.getY() == 9852)
					player.useStairs(833, new WorldTile(3115, 3452, 0), 1, 2);

				/**
				 * Ape Atoll Agility
				 */

				else if (id == 12570)
					ApeAgility.climbTree(player, object);
				else if (id == 12573)
					ApeAgility.swingLadder(player, object);
				else if (id == 12568)
					ApeAgility.jumpStone(player, object);

				/**
				 * Gnome agility
				 */
				else if (id == 69526)
					GnomeAgility.walkGnomeLog(player);
				else if (id == 69383)
					GnomeAgility.climbGnomeObstacleNet(player);
				else if (id == 69508)
					GnomeAgility.climbUpGnomeTreeBranch(player);
				else if (id == 2312)
					GnomeAgility.walkGnomeRope(player);
				else if (id == 4059)
					GnomeAgility.walkBackGnomeRope(player);
				else if (id == 69507)
					GnomeAgility.climbDownGnomeTreeBranch(player);
				else if (id == 69384)
					GnomeAgility.climbGnomeObstacleNet2(player);
				else if (id == 69377 || id == 69378)
					GnomeAgility.enterGnomePipe(player, object.getX(), object.getY());
				else if (Wilderness.isDitch(id)) {// wild ditch
					player.getDialogueManager().startDialogue("WildernessDitch", object);
				} else if (id == 42611) {// Magic Portal
					player.getDialogueManager().startDialogue("MagicPortal");
				} else if (object.getDefinitions().name.equalsIgnoreCase("Obelisk") && object.getY() > 3525) {

				} else if (id == 27254) {// Edgeville portal
					player.getPackets().sendGameMessage("You enter the portal...");
					player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3, "..and are transported to Edgeville.");
					player.addWalkSteps(1598, 4506, -1, false);

				} else if (id == 1317 || id == 68973) {
					player.getDialogueManager().startDialogue("SpiritTreeDialogue", object.getId());

				} else if (id == 68974) {
					player.getDialogueManager().startDialogue("MainSpiritTreeDialogue", object.getId());

				} else if (id == 48496) {
					if (/*
						 * player.getInventory().getFreeSlots() == 28 &&
						 * !player.getEquipment().wearingArmour() &&
						 */!player.hasFamiliar()) {
						player.getDialogueManager().startDialogue("DungFloorSelectD", player);
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage",
								"You're not allowed to bring any familiars into the dungeoneering.");
					}
					return;
				} else if (id == 12202) {// mole entrance
					if (!player.getInventory().containsItem(952, 1)) {
						player.getPackets().sendGameMessage("You need a spade to dig this.");
						return;
					}
					if (player.getX() != object.getX() || player.getY() != object.getY()) {
						player.lock();
						player.addWalkSteps(object.getX(), object.getY());
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								InventoryOptionsHandler.dig(player);
							}

						}, 1);
					} else
						InventoryOptionsHandler.dig(player);
				} else if (id == 12230 && object.getX() == 1752 && object.getY() == 5136) {// mole
																							// exit
					player.setNextWorldTile(new WorldTile(2986, 3316, 0));
				} else if (id == 15522) {// portal sign
					if (player.withinDistance(new WorldTile(1598, 4504, 0), 1)) {// PORTAL
						// 1
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Edgeville");
						player.getPackets().sendIComponentText(327, 14, "This portal will take you to edgeville. There "
								+ "you can multi pk once past the wilderness ditch.");
					}
					if (player.withinDistance(new WorldTile(1598, 4508, 0), 1)) {// PORTAL
						// 2
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Mage Bank");
						player.getPackets().sendIComponentText(327, 14, "This portal will take you to the mage bank. "
								+ "The mage bank is a 1v1 deep wilderness area.");
					}
					if (player.withinDistance(new WorldTile(1598, 4513, 0), 1)) {// PORTAL
						// 3
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Magic's Portal");
						player.getPackets().sendIComponentText(327, 14,
								"This portal will allow you to teleport to areas that "
										+ "will allow you to change your magic spell book.");
					}
				} else if (id == 38811 || id == 37929) {// corp beast
					if (object.getX() == 2971 && object.getY() == 4382)
						player.getInterfaceManager().sendInterface(650);
					else if (object.getX() == 2918 && object.getY() == 4382) {
						player.stopAll();
						player.setNextWorldTile(
								new WorldTile(player.getX() == 2921 ? 2917 : 2921, player.getY(), player.getPlane()));
					}
				} else if (id == 37928 && object.getX() == 2883 && object.getY() == 4370) {
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3214, 3782, 0));
					player.getControllerManager().startController("Wilderness");
				} else if (id == 38815 && object.getX() == 3209 && object.getY() == 3780 && object.getPlane() == 0) {
					if (player.getSkills().getLevelForXp(Skills.WOODCUTTING) < 37
							|| player.getSkills().getLevelForXp(Skills.MINING) < 45
							|| player.getSkills().getLevelForXp(Skills.SUMMONING) < 23
							|| player.getSkills().getLevelForXp(Skills.FIREMAKING) < 47
							|| player.getSkills().getLevelForXp(Skills.PRAYER) < 55) {
						player.getPackets().sendGameMessage(
								"You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
						return;
					}
					player.stopAll();
					player.setNextWorldTile(new WorldTile(2885, 4372, 2));
					player.getControllerManager().forceStop();
					// TODO all reqs, skills not added
				} else if (id == 48803) {
					if (player.isKalphiteLairEntranceSetted())
						player.getDialogueManager().startDialogue("KalphiteQueen");
					else
						player.getPackets().sendGameMessage("You need a rope to climb down.");
				} else if (id == 82049 && player.isKalphiteLairEntranceSetted()) {
					player.setNextWorldTile(new WorldTile(3483, 9510, 2));
				} else if (id == 3829) {
					if (object.getX() == 3483 && object.getY() == 9510) {
						player.useStairs(828, new WorldTile(3226, 3108, 0), 1, 2);
					}
				} else if (id == 3832)

				{
					if (object.getX() == 3508 && object.getY() == 9494) {
						player.useStairs(828, new WorldTile(3445, 9497, 0), 1, 2);
					}
				} else if (id == 10177 && x == 2546 && y == 10143) {
					player.getDialogueManager().startDialogue("ClimbEmoteStairs", new WorldTile(2544, 3741, 0),
							new WorldTile(1798, 4407, 3), "Go up the stairs.", "Go down the stairs.", 828);
				} else if (id == 9369) {
					player.getControllerManager().startController("FightPits");
				} else if (id == 9319) {

					player.setNextAnimation(new Animation(828));

					if (object.getX() == 3447 && object.getY() == 3576 && object.getPlane() == 1) {
						player.setNextWorldTile(new WorldTile(3446, 3576, 2));
					}
					if (object.getX() == 3422 && object.getY() == 3550 && object.getPlane() == 0) {
						player.setNextWorldTile(new WorldTile(3422, 3551, 1));

					}
					player.stopAll();
				} else if (id == 9320) {
					player.setNextAnimation(new Animation(828));
					if (object.getX() == 3447 && object.getY() == 3576 && object.getPlane() == 2) {
						player.setNextWorldTile(new WorldTile(3446, 3576, 1));
					}
					if (object.getX() == 3422 && object.getY() == 3550 && object.getPlane() == 1) {
						player.setNextWorldTile(new WorldTile(3422, 3551, 0));
					}
					player.stopAll();
				} else if (id == 54019 || id == 54020 || id == 55301)
					PkRank.showRanks(player);
				else if (id == 1817 && object.getX() == 2273 && object.getY() == 4680) { // kbd
																							// lever
					Magic.pushLeverTeleport(player, new WorldTile(3067, 10254, 0));
				} else if (id == 1816 && object.getX() == 3067 && object.getY() == 10252) { // kbd
																							// out
																							// lever
					Magic.pushLeverTeleport(player, new WorldTile(2273, 4681, 0));
				} else if (id == 32015 && object.getX() == 3069 && object.getY() == 10256) { // kbd
																								// stairs
					player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
					player.getControllerManager().startController("Wilderness");
				} else if (id == 1765 && object.getX() == 3017 && object.getY() == 3849) { // kbd
																							// out
																							// stairs
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3069, 10255, 0));
					player.getControllerManager().forceStop();
				} else if (id == 14315) {
					if (Lander.canEnter(player, 0))
						return;
				} else if (id == 25631) {
					if (Lander.canEnter(player, 1))
						return;
				} else if (id == 25632) {
					if (Lander.canEnter(player, 2))
						return;
				} else if (id == 5959) {
					Magic.pushLeverTeleport(player, new WorldTile(2539, 4712, 0));
				} else if (id == 5960) {
					Magic.pushLeverTeleport(player, new WorldTile(3089, 3957, 0));
				} else if (id == 1814) {
					Magic.pushLeverTeleport(player, new WorldTile(3155, 3923, 0));
				} else if (id == 1815) {
					Magic.pushLeverTeleport(player, new WorldTile(2561, 3311, 0));
				} else if (id == 62675)
					player.getCutscenesManager().play("DTPreview");
				else if ((id == 10193 && x == 1798 && y == 4406) || (id == 8930 && x == 2542 && y == 3740))
					player.useStairs(-1, new WorldTile(2545, 10143, 0), 0, 1);
				else if (id == 10195/* && x == 1808 && y == 4405 */)
					player.setNextWorldTile(new WorldTile(1810, 4405, 2));
				else if (id == 10196 && x == 1809 && y == 4405)
					player.useStairs(-1, new WorldTile(1807, 4405, 3), 0, 1);
				else if (id == 10198 && x == 1823 && y == 4404)
					player.useStairs(-1, new WorldTile(1825, 4404, 3), 0, 1);
				else if (id == 10197 && x == 1824 && y == 4404)
					player.useStairs(-1, new WorldTile(1823, 4404, 2), 0, 1);
				else if (id == 10199 && x == 1834 && y == 4389)
					player.useStairs(-1, new WorldTile(1834, 4388, 2), 0, 1);
				else if (id == 10200 && x == 1834 && y == 4388)
					player.useStairs(-1, new WorldTile(1834, 4390, 3), 0, 1);
				else if (id == 10201 && x == 1811 && y == 4394)
					player.useStairs(-1, new WorldTile(1810, 4394, 1), 0, 1);
				else if (id == 10202 && x == 1810 && y == 4394)
					player.useStairs(-1, new WorldTile(1812, 4394, 2), 0, 1);
				else if (id == 10203 && x == 1799 && y == 4388)
					player.useStairs(-1, new WorldTile(1799, 4386, 2), 0, 1);
				else if (id == 10204 && x == 1799 && y == 4387)
					player.useStairs(-1, new WorldTile(1799, 4389, 1), 0, 1);
				else if (id == 10205 && x == 1797 && y == 4382)
					player.useStairs(-1, new WorldTile(1797, 4382, 1), 0, 1);
				else if (id == 10206 && x == 1798 && y == 4382)
					player.useStairs(-1, new WorldTile(1796, 4382, 2), 0, 1);
				else if (id == 10207 && x == 1802 && y == 4369)
					player.useStairs(-1, new WorldTile(1800, 4369, 2), 0, 1);
				else if (id == 10208 && x == 1801 && y == 4369)
					player.useStairs(-1, new WorldTile(1802, 4369, 1), 0, 1);
				else if (id == 10209 && x == 1826 && y == 4362)
					player.useStairs(-1, new WorldTile(1828, 4362, 1), 0, 1);
				else if (id == 10210 && x == 1827 && y == 4362)
					player.useStairs(-1, new WorldTile(1825, 4362, 2), 0, 1);
				else if (id == 10211 && x == 1863 && y == 4371)
					player.useStairs(-1, new WorldTile(1863, 4373, 2), 0, 1);
				else if (id == 10212 && x == 1863 && y == 4372)
					player.useStairs(-1, new WorldTile(1863, 4370, 1), 0, 1);
				else if (id == 10213 && x == 1864 && y == 4388)
					player.useStairs(-1, new WorldTile(1864, 4389, 1), 0, 1);
				else if (id == 10214 && x == 1864 && y == 4389)
					player.useStairs(-1, new WorldTile(1864, 4387, 2), 0, 1);
				else if (id == 10215 && x == 1890 && y == 4407)
					player.useStairs(-1, new WorldTile(1890, 4408, 0), 0, 1);
				else if (id == 10216 && x == 1890 && y == 4408)
					player.useStairs(-1, new WorldTile(1890, 4406, 1), 0, 1);
				else if (id == 62681)
					player.getDominionTower().viewScoreBoard();
				else if (id == 10230)
					player.getDialogueManager().startDialogue("DagannothKings");
				else if (id == 62678 || id == 62679)
					player.getDominionTower().openModes();
				else if (id == 62688)
					player.getDialogueManager().startDialogue("DTClaimRewards");
				else if (id == 62677)
					player.getDominionTower().talkToFace();
				else if (id == 62680)
					player.getDominionTower().openBankChest();
				else if (id == 48797)
					player.useStairs(-1, new WorldTile(3877, 5526, 1), 0, 1);
				else if (id == 48798)
					player.useStairs(-1, new WorldTile(3246, 3198, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5543)
					player.useStairs(-1, new WorldTile(3861, 5543, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
				else if (id == 48677 && x == 3858 && y == 5543)
					player.useStairs(-1, new WorldTile(3856, 5543, 1), 0, 1);
				else if (id == 48677 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3856, 5533, 1), 0, 1);
				else if (id == 48679)
					player.useStairs(-1, new WorldTile(3875, 5527, 1), 0, 1);
				else if (id == 48688)
					player.useStairs(-1, new WorldTile(3972, 5565, 0), 0, 1);
				else if (id == 48683)
					player.useStairs(-1, new WorldTile(3868, 5524, 0), 0, 1);
				else if (id == 48682)
					player.useStairs(-1, new WorldTile(3869, 5524, 0), 0, 1);
				else if (id == 62676) { // dominion exit
					player.useStairs(-1, new WorldTile(3374, 3093, 0), 0, 1);
				} else if (id == 62674) { // dominion entrance
					player.useStairs(-1, new WorldTile(3744, 6405, 0), 0, 1);
				} else if (id == 3192) {
					PkRank.showRanks(player);
				} else if (id == 65349) {
					player.useStairs(-1, new WorldTile(3044, 10325, 0), 0, 1);
				} else if (id == 32048 && object.getX() == 3043 && object.getY() == 10328) {
					player.useStairs(-1, new WorldTile(3045, 3927, 0), 0, 1);
				} else if (id == 26194) {
					player.getDialogueManager().startDialogue("PartyRoomLever");
				} else if (id == 61190 || id == 61191 || id == 61192 || id == 61193) {
					if (objectDef.containsOption(0, "Chop down"))
						player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL));
				} else if (id == 20573)
					player.getControllerManager().startController("RefugeOfFear");
				// crucible
				else if (id == 67050)
					player.useStairs(-1, new WorldTile(3359, 6110, 0), 0, 1);
				else if (id == 67053)
					player.useStairs(-1, new WorldTile(3120, 3519, 0), 0, 1);
				else if (id == 67051)
					player.getDialogueManager().startDialogue("Marv", false);
				else if (id == 67052)
					Crucible.enterCrucibleEntrance(player);
				else {
					switch (objectDef.name.toLowerCase()) {
					case "obelisk":
						if (objectDef.containsOption(0, "Infuse-pouch") || objectDef.containsOption(0, "Infuse") || objectDef.containsOption(0, "Infuse pouch"))
							Summoning.sendInterface(player);
						return;
					case "trapdoor":
					case "manhole":
						if (objectDef.containsOption(0, "Open")) {
							WorldObject openedHole = new WorldObject(object
									.getId() + 1, object.getType(), object
									.getRotation(), object.getX(), object
									.getY(), object.getPlane());
							player.faceObject(openedHole);
							World.spawnObjectTemporary(openedHole, 60000);
						} else {
							player.sm("It won't budge!");
						}
						break;
					case "fairy ring":
						FairyRing.openRingInterface(player, object, false);
						break;
					case "closed chest":
						if (objectDef.containsOption(0, "Open")) {
							player.setNextAnimation(new Animation(536));
							player.lock(2);
							WorldObject openedChest = new WorldObject(object
									.getId() + 1, object.getType(), object
									.getRotation(), object.getX(), object
									.getY(), object.getPlane());
							player.faceObject(openedChest);
							World.spawnObjectTemporary(openedChest, 60000);
						}
						break;
					case "open chest":
						if (objectDef.containsOption(0, "Search"))
							player.getPackets().sendGameMessage("You search the chest but find nothing.");
						break;
					case "spiderweb":
						if (object.getRotation() == 2) {
							player.lock(2);
							if (Utils.getRandom(1) == 0) {
								player.addWalkSteps(player.getX(),
										player.getY() < y ? object.getY() + 2 : object.getY() - 1, -1, false);
								player.getPackets().sendGameMessage("You squeeze though the web.");
							} else
								player.getPackets().sendGameMessage(
										"You fail to squeeze though the web; perhaps you should try again.");
						}
						break;
					case "web":
						if (objectDef.containsOption(0, "Slash")) {
							slashWeb(player, object);
						}
						break;
					case "anvil":
					case "portable forge":
						if (objectDef.containsOption(0, "Smith")) {
							ForgingBar bar = ForgingBar.getBar(player);
							ForgingBarNew barNew = ForgingBarNew.getBar(player);
							if (bar == ForgingBar.DRACONIC_VISAGE) {
								player.getDialogueManager().startDialogue("DFSSmithingD");
								return;
							}
							if (bar != null && barNew != null) {
								player.getDialogueManager().startDialogue("ChooseBarsD", object);
								return;
							}
							if (bar != null) {
								ForgingInterface.sendSmithingInterface(player, bar, object);
							}
							if (barNew != null) {
								SmithingNew.sendSmithingInterface(player, barNew, object);
							}
						}
						break;
					case "crashed star":
						if (objectDef.containsOption(0, "Mine")) {
							ShootingStar.mine(player, object);
						}
						break;
					case "range":
					case "grill":
					case "cooking range":
					case "stove":
						Cookables cook = Cooking.getCook(player);
						if (cook != null) {
							if (cook.isFireOnly() && !object.getDefinitions().name.equals("Fire")) {
								player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on a fire.");
								return;
							} else if (cook.isSpitRoast() && object.getId() != 11363) {
								player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on an iron spit.");
								return;
							} else if (player.getSkills().getLevel(Skills.COOKING) < cook.getLvl()) {
								player.getDialogueManager().startDialogue("SimpleMessage", "You need a cooking level of " + cook.getLvl() + " to cook this food.");
								return;
							}
						}
						player.getDialogueManager().startDialogue("CookingD", cook, object);
						player.getTemporaryAttributtes().remove(Key.USING_PORTABLE_RANGE);
						break;
					case "portable range":
						if (player.isAnIronMan()) {
							return;
						}
						cook = Cooking.getCook(player);
						if (cook != null) {
							if (cook.isFireOnly() && !object.getDefinitions().name.equals("Fire")) {
								player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on a fire.");
								return;
							} else if (cook.isSpitRoast() && object.getId() != 11363) {
								player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on an iron spit.");
								return;
							} else if (player.getSkills().getLevel(Skills.COOKING) < cook.getLvl()) {
								player.getDialogueManager().startDialogue("SimpleMessage", "You need a cooking level of " + cook.getLvl() + " to cook this food.");
								return;
							}
						}
						player.getDialogueManager().startDialogue("CookingD", cook, object);
						player.getTemporaryAttributtes().put(Key.USING_PORTABLE_RANGE, true);
						break;
					case "tin rock":
					case "tin ore rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.TIN_ORE));
						break;
					case "gold rock":
					case "gold ore rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.GOLD_ORE));
						break;
					case "iron rock":
					case "iron ore rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.IRON_ORE));
						break;
					case "silver rock":
					case "silver ore rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.SILVER_ORE));
						break;
					case "coal rock":
					case "coal rocks":
						if ((objectDef.id == 93018 || objectDef.id == 93019) && Mining.coalHarmonized) {
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(new Mining(object, RockDefinitions.HARMONIZED_COAL_ORE));

							return;
						} else if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.COAL_ORE));
						break;
					case "clay rock":
					case "clay rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.CLAY));
						break;
					case "soft clay":
					case "soft clay rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.CLAY));
						break;
					case "copper rock":
					case "copper ore rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.COPPER_ORE));
						break;
					case "blurite rock":
					case "blurite ore rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.BLURITE_ORE));
						break;
					case "adamantite rock":
					case "adamantite ore rocks":
						if ((objectDef.id == 93021 || objectDef.id == 93020) && Mining.adamantHarmonized) {
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(new Mining(object, RockDefinitions.HARMONIZED_ADAMANT_ORE));

							return;
						} else if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.ADAMANT_ORE));
						break;
					case "runite rock":
					case "runite ore rocks":
						if ((objectDef.id == 93023 || objectDef.id == 93022) && Mining.runeHarmonized) {
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(new Mining(object, RockDefinitions.HARMONIZED_RUNITE_ORE));

							return;
						} else if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.RUNITE_ORE));
						break;
					case "gem rock":
					case "gem rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.GEM_ROCK));
						break;
					case "granite rock":
					case "granite rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.GRANITE_ORE));
						break;
					case "sandstone rock":
					case "sandstone rocks":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.SANDSTONE_ORE));
						break;
					case "mithril rock":
					case "mithril ore rocks":
						if ((objectDef.id == 93017 || objectDef.id == 93016) && Mining.mithrilHarmonized) {
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(new Mining(object, RockDefinitions.HARMONIZED_MITHRIL_ORE));

							return;
						} else if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.MITHRIL_ORE));
						break;
					case "divine runite rock":
						player.getActionManager().setAction(new DivineMining(object, DivineMining.RockDefinitions.DIVINE_RUNE_ORE));
						break;
					case "divine adamantite rock":
						player.getActionManager().setAction(new DivineMining(object, DivineMining.RockDefinitions.DIVINE_ADAMANTITE_ORE));
						break;
					case "divine mithril rock":
						player.getActionManager().setAction(new DivineMining(object, DivineMining.RockDefinitions.DIVINE_MITHRIL_ORE));
						break;
					case "divine coal rock":
						player.getActionManager().setAction(new DivineMining(object, DivineMining.RockDefinitions.DIVINE_COAL_ORE));
						break;
					case "divine iron rock":
						player.getActionManager().setAction(new DivineMining(object, DivineMining.RockDefinitions.DIVINE_IRON_ORE));
						break;
					case "divine bronze rock":
						player.getActionManager().setAction(new DivineMining(object, DivineMining.RockDefinitions.DIVINE_BRONZE_ORE));
						break;
					case "seren stone":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.SEREN_STONE));
						break;
					case "luminite rock":
					case "luminite vein":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.LUMINITE));
						break;
					case "orichalcite rock":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.ORICHALCITE_ORE));
						break;
					case "drakolith rock":
					case "drakolith vein":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.DRAKOLITH));
						break;
					case "necrite rock":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.NECRITE_ORE));
						break;
					case "phasmatite rock":
					case "phasmatite vein":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.PHASMATITE));
						break;
					case "banite rock":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.BANITE_ORE));
						break;
					case "light animica rock":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.LIGHT_ANIMICA));
						break;
					case "dark animica rock":
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(new Mining(object, RockDefinitions.DARK_ANIMICA));
						break;
					case "portable bank deposit box":
					case "bank deposit box":
						if (objectDef.containsOption(0, "Deposit"))
							player.getBank().openDepositBox();
						break;
					case "portable crafter":
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(0, "Bank") || objectDef.containsOption(0, "Use"))
							player.getBank().openBank();
						break;
					// Woodcutting start
					case "tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL));
						break;
					case "evergreen":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.EVERGREEN));
						break;
					case "dead tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.DEAD));
						break;
					case "oak":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.OAK));
						break;
					case "willow":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.WILLOW));
						break;
					case "maple tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAPLE));
						break;
					case "ivy":
						if (objectDef.containsOption(0, "Chop"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.IVY));
						break;
					case "yew":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.YEW));
						break;
					case "magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC));
						break;
					case "cursed magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.CURSED_MAGIC));
						break;
					// Woodcutting end
					case "gate":
					case "city gate":
					case "large door":
					case "metal door":
					case "church door":
					case "long hall door":
						if (objectDef.containsOption(0, "Open"))
							if (!handleGate(player, object))
								handleDoor(player, object);
						break;
					case "door":
					case "big door":
						if (object.getType() == 0
								&& (objectDef.containsOption(0, "Open") || objectDef.containsOption(0, "Unlock")))
							if (player.getControllerManager().getController() instanceof BandosInstance) {
								player.getPackets()
										.sendGameMessage("As much as you regret this fight, this door is locked.");
							} else
								handleDoor(player, object);
						break;
					case "ladder":
						handleLadder(player, object, 1);
						break;
					case "staircase":
						handleStaircases(player, object, 1);
						break;
					case "small obelisk":
						if (objectDef.containsOption(0, "Renew-points")) {
							int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
							if (player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
								player.lock(3);
								player.setNextAnimation(new Animation(8502));
								player.getSkills().set(Skills.SUMMONING, summonLevel);
								player.getPackets().sendGameMessage("You have recharged your Summoning points.", true);
							} else
								player.getPackets().sendGameMessage("You already have full Summoning points.");
						}
						break;
					case "bandos altar":
					case "altar":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage("You pray to the gods...", true);
								player.setNextAnimation(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(maxPrayer);
										player.getPackets().sendGameMessage("...and recharged your prayer.", true);
									}
								}, 2);
							} else
								player.getPackets().sendGameMessage("You already have full prayer.");
							if (id == 6552)
								player.getDialogueManager().startDialogue("AncientAltar");
						}
						break;
					case "fountain":
					case "portable well":
					case "well":
					case "sink":
					case "water barrel":
					case "water barrels":
					case "pump":
					case "water trough":
						Fill empty = WaterFilling.getEmpty(player);
						if (empty != null)
						player.getDialogueManager().startDialogue("WaterFillingD", empty, object);
						break;
					 default:
						 if (player.getRights() == 2) {
							 player.getPackets().sendGameMessage("Option 1 on Object : " + id);
						}
					 break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler",
							"Option 1 on Object : " + id + ", " + object.getX() + ", " + object.getY() + ", "
									+ object.getPlane() + ", " + object.getType() + ", " + object.getRotation() + ", "
									+ object.getDefinitions().name);
			}
		}));
	}

	private static void handleOption2(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControllerManager().processObjectClick2(object))
					return;
				if (object.getDefinitions().name.equalsIgnoreCase("furnace")) {
					ForgingBar bar = ForgingBar.getBarInventory(player);
					if (bar != null) {
						player.getMetalBank().add(new Item(bar.getBarId(), player.getInventory().getAmountOf(bar.getBarId())));
						return;
					}
					player.getMetalBank().getBankContents();
					return;
				}
				if (player.getFarmingManager().isFarming(id, null, 2))
				return;
				else if (id == 17010)
					player.getDialogueManager().startDialogue("LunarAltar");
				Animation THIEVING_ANIMATION = new Animation(881);
				if (id == 635) {
					if (player.getInventory().getFreeSlots() < 1) {
						player.getPackets().sendGameMessage("Not enough space in your inventory.");
						return;
					}
					player.applyHit(new Hit(player, 5, HitLook.REGULAR_DAMAGE, 1));
					player.setNextAnimation(THIEVING_ANIMATION);
					player.lock(1);
					player.getInventory().addItem(1243, 1);
					player.getSkills().addXp(17, 25);

					return;
				} else if (id == 4706) {
					if (player.getInventory().getFreeSlots() < 1) {
						player.getPackets().sendGameMessage("Not enough space in your inventory.");
						return;
					}
					if (player.getSkills().getLevel(Skills.THIEVING) >= 55) {
						player.applyHit(new Hit(player, 5, HitLook.REGULAR_DAMAGE, 1));
						player.setNextAnimation(THIEVING_ANIMATION);
						player.lock(1);
						player.getInventory().addItem(1245, 1);
						player.getSkills().addXp(17, 40);
					} else {
						player.getPackets().sendGameMessage("You must at least have a minimum Thieving level of 55.");
					}
					return;
				} else if (id == 4705) {
					if (player.getInventory().getFreeSlots() < 1) {
						player.getPackets().sendGameMessage("Not enough space in your inventory.");
						return;
					}
					if (player.getSkills().getLevel(Skills.THIEVING) >= 85) {
						player.applyHit(new Hit(player, 5, HitLook.REGULAR_DAMAGE, 1));
						player.setNextAnimation(THIEVING_ANIMATION);
						player.lock(1);
						player.getInventory().addItem(1247, 1);
						player.getSkills().addXp(17, 70);
					} else {
						player.getPackets().sendGameMessage("You must at least have a minimum Thieving level of 85.");
					}
					return;
				} else if (id == 62677)
					player.getDominionTower().openRewards();
				else if (id == 70795)
					player.setNextWorldTile(new WorldTile(1206, 6507, 0));
				else if (id == 62688)
					player.getDialogueManager().startDialogue("SimpleMessage", "You have a Dominion Factor of " + player.getDominionTower().getDominionFactor() + ".");
				else if (id == 34384 || id == 34383 || id == 14011 || id == 7053 || id == 34387 || id == 34386 || id == 34385)
					Thieving.handleStalls(player, object);
				else if (id == 2418)
					PartyRoom.openPartyChest(player);
				else if (id == 1317 || id == 68973) {
					SpiritTree.sendSpiritTreeTeleport(player, SpiritTree.MAIN_SPIRIT_TREE);

				} else if (id == 68974) {
					SpiritTree.sendSpiritTreeInterface(player);

				} else if (id == 67051) {
					player.getDialogueManager().startDialogue("Marv", true);
					return;
				}
				if (id == 87989) {
					XPWell.give(player);
					return;
				}
				if (PrifddinasCity.handleObjectOption2(player, object)) {
					return;
				}
				else {
					switch (objectDef.name.toLowerCase()) {
					case "cabbage":
						if (objectDef.containsOption(1, "Pick") && player.getInventory().addItem(1965, 1)) {
							player.setNextAnimation(new Animation(827));
							player.lock(2);
							World.removeObjectTemporary(object, 60000, false);
						}
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(1, "Bank"))
							player.getBank().openBank();
						break;
					case "gates":
					case "gate":
					case "metal door":
						if (object.getType() == 0 && objectDef.containsOption(1, "Open"))
							handleGate(player, object, id);
						break;
					case "door":
					case "big door":
						if (object.getType() == 0 && objectDef.containsOption(1, "Open"))
							handleDoor(player, object);
						break;
					case "ladder":
						handleLadder(player, object, 2);
						break;
					case "staircase":
						handleStaircases(player, object, 2);
						break;
					case "portable bank deposit box":
					case "bank deposit box":
						if (objectDef.containsOption(1, "Deposit-All")) {
							player.getBank().depositAllEquipment(true);
							player.getBank().depositAllInventory(true);
							player.getBank().depositAllMoneyPouch(true);
						}
						break;
					default:
					player.getPackets().sendGameMessage("Option 2 on Object : " + id);
					 break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "Option 2 on Object : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane());
			}
		}));
	}

	private static void handleOption3(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControllerManager().processObjectClick3(object))
					return;
				if (player.getFarmingManager().isFarming(id, null, 3))
					return;
				if (PrifddinasCity.handleObjectOption3(player, object)) {
					return;
				}
				if (object.getDefinitions().name.equalsIgnoreCase("furnace")) {
					player.getMetalBank().depositMetalToBank();
					return;
				}
				switch (objectDef.name.toLowerCase()) {
				case "gate":
				case "metal door":
					if (object.getType() == 0 && objectDef.containsOption(2, "Open"))
						handleGate(player, object, id);
					break;

				case "door":
				case "big door":
					if (object.getType() == 0 && objectDef.containsOption(2, "Open"))
						handleDoor(player, object);
					break;
				case "ladder":
					handleLadder(player, object, 3);
					break;
				case "staircase":
					handleStaircases(player, object, 3);
					break;
				}
				if (player.getFarmingManager().isFarming(id, null, 3)) {
					return;
				}
				if (id >= 15477 && id <= 15482) {
					if (player.hasHouse) {
						player.getHouse().setBuildMode(true);
						player.getHouse().enterMyHouse();
						return;
					}
					player.sm("You must first purchase a house from the Estate Agent.");
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 3 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane() + ", ");
			}
		}));
	}

	private static void handleOption4(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControllerManager().processObjectClick4(object))
					return;
				// living rock Caverns
				if (id == 45076)
					MiningBase.propect(player, "This rock contains a large concentration of gold.");
				else if (id == 5999)
					MiningBase.propect(player, "This rock contains a large concentration of coal.");
				else {
					switch (objectDef.name.toLowerCase()) {
					default:
						player.getPackets().sendGameMessage("Option 4 on Object : " + id);
						break;
					}
				}
				if (id >= 15477 && id <= 15482) {
				player.getTemporaryAttributtes().put("joinguesthouse", Boolean.TRUE);
				player.getPackets().sendInputNameScript("Please enter the name of the player:");
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "Option 4 on ID : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane() + ", ");
			}
		}));
	}

	private static void handleOption5(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControllerManager().processObjectClick5(object))
					return;
				if (id == -1) {
					// unused
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "fire":
						Cookables cook = Cooking.getCook(player);
						if (cook != null) {
							player.getDialogueManager().startDialogue("CookingD", cook, object);
							return;
						}
						if (objectDef.containsOption(4, "Use")) {
							Bonfire.addLogs(player, object);
						}
						break;
					case "door hotspot":
						player.getInterfaceManager().sendInterface(402);
						return;
					default:
						player.getPackets().sendGameMessage("Option 5 on Object : " + id);
						 break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "Option 5 on ID : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane() + ", ");
			}
		}));
	}

	private static void handleOptionExamine(final Player player, final WorldObject object) {
		if (player.getUsername().equalsIgnoreCase("tyler")) {
			int offsetX = object.getX() - player.getX();
			int offsetY = object.getY() - player.getY();
			System.out.println("Offsets" + offsetX + " , " + offsetY);
		}
		player.getPackets().sendGameMessage("It's an " + object.getDefinitions().name + ".");
		if (Settings.DEBUG)
			if (Settings.DEBUG)
				Logger.log("ObjectHandler",
						"examined object id : " + object.getId() + ", " + object.getX() + ", " + object.getY() + ", "
								+ object.getPlane() + ", " + object.getType() + ", " + object.getRotation() + ", "
								+ object.getDefinitions().name);
	}

	private static void slashWeb(Player player, WorldObject object) {

		if (Utils.getRandom(1) == 0) {
			World.spawnTemporaryObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), 60000, true);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else
			player.getPackets().sendGameMessage("You fail to cut through the web.");
	}

	private static boolean handleGate(Player player, WorldObject object) {
		return handleGate(player, object, 60000);
	}

	public static boolean handleGate(Player player, WorldObject object, long delay) {
		if (World.isSpawnedObject(object))
			return false;
		if (object.getRotation() == 0) {
			boolean south = true;
			WorldObject otherDoor = World.getObjectWithType(
					new WorldTile(object.getX(), object.getY() + 1, object.getPlane()), object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(object.getX(), object.getY() - 1, object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (World.removeObjectTemporary(object, delay) && World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			WorldObject otherDoor = World.getObjectWithType(
					new WorldTile(object.getX(), object.getY() + 1, object.getPlane()), object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(object.getX(), object.getY() - 1, object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (World.removeObjectTemporary(object, delay) && World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			WorldObject otherDoor = World.getObjectWithType(
					new WorldTile(object.getX() - 1, object.getY(), object.getPlane()), object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(object.getX() + 1, object.getY(), object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1, object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(), otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (World.removeObjectTemporary(object, delay) && World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			WorldObject otherDoor = World.getObjectWithType(
					new WorldTile(object.getX() - 1, object.getY(), object.getPlane()), object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(object.getX() + 1, object.getY(), object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (World.removeObjectTemporary(object, delay) && World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		}
		return false;
	}

	public static boolean handleDoor(Player player, WorldObject object, long timer) {
		if (World.isSpawnedObject(object)) {
			return false;
		}
		WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), (object.getRotation() + 1) & 0x3, object.getX(), object.getY(), object.getPlane());
		World.spawnObjectTemporary(openedDoor, timer);
		return false;
	}
	
	private static boolean handleDoor(Player player, WorldObject object) {
		return handleDoor(player, object, 60000);
	}

	private static boolean handleStaircases(Player player, WorldObject object, int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue("ClimbNoEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player.getPlane() + 1),
					new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else
			return false;
		return false;
	}

	private static boolean handleLadder(Player player, WorldObject object, int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue("ClimbEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player.getPlane() + 1),
					new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), "Climb up the ladder.",
					"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}

	public static void handleItemOnObject(final Player player, final WorldObject object, final int interfaceId, final Item item) {
		final int itemId = item.getId();
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.faceObject(object);
				if (itemId == -1) {
					return;
				}
				switch (objectDef.name.toLowerCase()) {
				case "altar":
					Bones bone = BonesOnAltar.isGood(item);
					if (bone != null) {
						player.getActionManager().setAction(new BonesOnAltar(object, bone.getBone()));
					} else {
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					}
					break;
				case "anvil":
					ForgingBar bar = ForgingBar.forId(itemId);
					ForgingBarNew barNew = ForgingBarNew.forId(itemId);
					if (bar == ForgingBar.DRACONIC_VISAGE || itemId == 1540) {
						player.getDialogueManager().startDialogue("DFSSmithingD");
						return;
					}
					if (itemId == 14472 || itemId == 14474 || itemId == 14476) {
						player.getDialogueManager().startDialogue("DPlateBodySmithingD");
						return;
					}
					if (barNew != null) {
						SmithingNew.sendSmithingInterface(player, barNew, object);
					}
					if (bar != null) {
						ForgingInterface.sendSmithingInterface(player, bar, object);
					}
					break;
				case "fire":
					Cookables cook = Cooking.isCookingSkill(item);
					if (Bonfire.addLog(player, object, item)) {
						return;
					} else if (cook != null) {
						player.getDialogueManager().startDialogue("CookingD", cook, object);
						return;
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage",
								"You can't use that item on the fire.");
					}
					break;
				case "furnace":
					if (itemId == 2357) {
						JewellerySmithing.openInterface(player);
						return;
					}
					if (itemId == 2355) {
						player.getDialogueManager().startDialogue("SilverCraftingD", object);
						return;
					}
					return;
				case "bank booth":
				case "deposit box":
					if (player.getOreBox().isOreBox(item)) {
						player.getOreBox().depositChestToBank(item);
						return;
					}
					break;
				case "fountain":
				case "well":
				case "sink":
				case "water barrel":
				case "water barrels":
				case "pump":
				case "water trough":
					WaterFilling.isFilling(player, itemId, false);
					break;
				case "range":
				case "grill":
				case "cooking range":
				case "stove":
				case "portable range":
					cook = Cooking.getCook(player);
					if (cook != null) {
						if (cook.isFireOnly() && !object.getDefinitions().name.equals("Fire")) {
							player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on a fire.");
							return;
						} else if (cook.isSpitRoast() && object.getId() != 11363) {
							player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on an iron spit.");
							return;
						} else if (player.getSkills().getLevel(Skills.COOKING) < cook.getLvl()) {
							player.getDialogueManager().startDialogue("SimpleMessage", "You need a cooking level of " + cook.getLvl() + " to cook this food.");
							return;
						}
					}
					player.getTemporaryAttributtes().remove(Key.USING_PORTABLE_RANGE);
					player.getDialogueManager().startDialogue("CookingD", cook, object);
					break;
				}
				if (itemId == 1438 && object.getId() == 2452) {
					Runecrafting.enterAirAltar(player);
				}
				if (PrifddinasCity.handleItemOnObject(player, object, item)) {
					return;
				}
				if (player.getFarmingManager().isFarming(object.getId(), item, 0)) {
					return;
				}
				if (itemId == 1440 && object.getId() == 2455) {
					Runecrafting.enterEarthAltar(player);
				}
				if (SpotInfo.getInfo(object.getId()) != null && player.getFarmingManager().getSpot(SpotInfo.getInfo(object.getId())) != null && !player.getFarmingManager().getSpot(SpotInfo.getInfo(object.getId())).isEmpty() && itemId == 952) {
					player.getFarmingManager().clearFarmingPatch(player.getFarmingManager().getSpot(SpotInfo.getInfo(object.getId())));
					return;
				}
				if (object.getId() == 35470 && player.getInventory().containsItem(989, 1) && player.getInventory().hasFreeSlots()) {
					CrystalChest.openChest(object, player);
					player.getInventory().deleteItem(989, 1);
				}
				if (object.getId() == 13715) {
					if (BrokenItems.forBrokenId(item.getId()) == null && BrokenItems.forDegradedId(item.getId()) == null) {
						player.getDialogueManager().startDialogue("SimpleMessage", "You cant repair this item.");
						return;
					}
					player.getDialogueManager().startDialogue("Repair", item.getId());
					return;
				}
				if (object.getId() == 35470) {
					if (!player.getInventory().hasFreeSlots()) {
						player.sm("Unfortunately, you must at least have an available slot.");
					} else if (object.getId() == 35470) {
						if (!player.getInventory().containsItem(989, 1)) {
							player.sm("This requires you to have a crystal key to open the chest.");
						}
					}
				}
				if (itemId == 1442 && object.getId() == 2456) {
					Runecrafting.enterFireAltar(player);
				}
				if (itemId == 1444 && object.getId() == 2454) {
					Runecrafting.enterWaterAltar(player);
				}
				if (itemId == 1446 && object.getId() == 2457) {
					Runecrafting.enterBodyAltar(player);
				}
				if (itemId == 1448 && object.getId() == 2453) {
					Runecrafting.enterMindAltar(player);
				}
				if (object.getId() == 733 || object.getId() == 64729) {
					slashWeb(player, object);
				}
				if (item.getId() == 1947 && object.getId() == 70034) {
					if (player.hasGrainInHopper == true) {
						player.getPackets().sendGameMessage("You already have grain placed in the hopper. Try using the hopper controls.");
					} else if (player.hasGrainInHopper == false) {
						player.hasGrainInHopper = true;
						player.getPackets().sendGameMessage("You place the grain into the hopper.");
						player.getInventory().deleteItem(1947, 1);
					}
				}
				if (object.getId() == 48803 && itemId == 954) {
					if (player.isKalphiteLairEntranceSetted())
						return;
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLairEntrance();
				}
				if (object.getId() == 82049 && itemId == 954) {
					if (player.isKalphiteLairSetted())
						return;
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLair();
				}
				if (Settings.DEBUG) {
					System.out.println("Item on object: " + object.getId());
				}
			}
		}));
	}
}
