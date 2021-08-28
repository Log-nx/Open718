package com.rs.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.message.MessageBuilder;

import com.rs.GameServer;
import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.minigames.GodWarsBosses;
import com.rs.game.minigames.ZarosGodwars;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.clanwars.RequestController;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.npc.NPC;
import com.rs.game.npc.agoroth.Agoroth;
import com.rs.game.npc.araxxi.Araxxi;
import com.rs.game.npc.corp.CorporealBeast;
import com.rs.game.npc.dagannoth.DagannothKing;
import com.rs.game.npc.dragons.KingBlackDragon;
import com.rs.game.npc.dragons.RuneDragon;
import com.rs.game.npc.glacor.Glacor;
import com.rs.game.npc.godwars.GodWarMinion;
import com.rs.game.npc.godwars.armadyl.GodwarsArmadylFaction;
import com.rs.game.npc.godwars.armadyl.KreeArra;
import com.rs.game.npc.godwars.bandos.GeneralGraardor;
import com.rs.game.npc.godwars.bandos.GodwarsBandosFaction;
import com.rs.game.npc.godwars.saradomin.CommanderZilyana;
import com.rs.game.npc.godwars.saradomin.GodwarsSaradominFaction;
import com.rs.game.npc.godwars.zamorak.GodwarsZamorakFaction;
import com.rs.game.npc.godwars.zamorak.KrilTstsaroth;
import com.rs.game.npc.godwars.zaros.Nex;
import com.rs.game.npc.godwars.zaros.NexMinion;
import com.rs.game.npc.godwars2.Helwyr;
import com.rs.game.npc.kalphite.KalphiteKing;
import com.rs.game.npc.kalphite.KalphiteQueen;
import com.rs.game.npc.legio.Primus;
import com.rs.game.npc.nomad.FlameVortex;
import com.rs.game.npc.nomad.Nomad;
import com.rs.game.npc.others.Bork;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.npc.others.Lucien;
import com.rs.game.npc.others.MasterOfFear;
import com.rs.game.npc.others.MercenaryMage;
import com.rs.game.npc.others.PestMonsters;
import com.rs.game.npc.others.Revenant;
import com.rs.game.npc.others.TormentedDemon;
import com.rs.game.npc.others.TrainingDummy;
import com.rs.game.npc.random.HunterTrapNPC;
import com.rs.game.npc.slayer.AbyssalDemon;
import com.rs.game.npc.slayer.Airut;
import com.rs.game.npc.slayer.CorruptedCreature;
import com.rs.game.npc.slayer.DesertLizard;
import com.rs.game.npc.slayer.Jadinko;
import com.rs.game.npc.slayer.Strykewyrm;
import com.rs.game.npc.sorgar.Elemental;
import com.rs.game.npc.vorago.Vorago;
import com.rs.game.npc.wilderness.ChaosElemental;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.divination.Wisp;
import com.rs.game.player.actions.divination.WispInfo;
import com.rs.game.player.actions.hunter.TrapAction.HunterNPC;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.LivingRockCavern;
import com.rs.game.player.content.Lottery;
import com.rs.game.player.content.PenguinEvent;
import com.rs.game.player.content.activities.XPWell;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.ports.JohnStrum;
import com.rs.game.player.content.activities.warbands.Warbands;
import com.rs.game.player.content.activities.warbands.Warbands.WarbandEvent;
import com.rs.game.player.content.custom.SuggestionManager;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.items.Portables;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.content.trivia.TriviaBot;
import com.rs.game.player.controllers.JadinkoLair;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.player.managers.GamePointManager;
import com.rs.game.player.managers.OwnedObjectManager;
import com.rs.game.route.Flags;
import com.rs.utils.AntiFlood;
import com.rs.utils.Colors;
import com.rs.utils.Logger;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.utils.Utils.EntityDirection;

public final class World {

	public static int exiting_delay;
	public static long exiting_start;
	public static int lotteryAmount;
	private static final EntityList<Player> players = new EntityList<Player>(Settings.PLAYERS_LIMIT);

	private static final EntityList<NPC> npcs = new EntityList<NPC>(Settings.NPCS_LIMIT);
	private static final Map<Integer, Region> regions = Collections.synchronizedMap(new HashMap<Integer, Region>());

	public static final void init() {
		addTriviaBotTask();
       // addHeatMapsTask();
		addSkillerDreamTask();
		penguinHS();
		addSuggestionManagerTask();
		addRestoreRunEnergyTask();
		addDrainPrayerTask();
		addRestoreHitPointsTask();
		addRestoreSkillsTask();
		addRestoreSpecialAttackTask();
		addRestoreShopItemsTask();
		addSummoningEffectTask();
		addOwnedObjectsTask();
		LivingRockCavern.init();
		JadinkoLair.init();
		XPWell.addWellTask();
		addWarbandsEventManager();
		addGlobalEventsTask();
	}
	
	public static final NPC spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, EntityDirection faceDirection) {
	NPC returnValue = spawnNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	returnValue.setDirection(faceDirection.getValue());
	return returnValue;
	}
	
	public static List<WorldTile> restrictedTiles = new ArrayList<WorldTile>();
	
	public static void deleteObject(WorldTile tile){
		restrictedTiles.add(tile);
	}
	
	public static void addHeatMapsTask() {
	com.everythingrs.service.Service.scheduledService.scheduleAtFixedRate(new Runnable() {
		@Override
		public void run() {
			com.everythingrs.heatmaps.Heatmap.getMap().clear();
			for (Player player :  World.getPlayers()) {
				if (player != null) {
					com.everythingrs.heatmaps.Heatmap.getMap().put(player.getUsername(),
							new com.everythingrs.heatmaps.Heatmap(player.getUsername(), player.getX(), player.getY(),
									player.getPlane()));
				}
			}
			com.everythingrs.heatmaps.Heatmap.update("a6858q9p8jkzclhz0mypkfbt9zm3jpbshqlfyjy5kchni2j4ishjfn76s4a9vpyhi9jocrf6r");
		}
	}, 0, 5, java.util.concurrent.TimeUnit.SECONDS);
	}
	
	private static void addGlobalEventsTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
			GlobalEvents.generateRandomEvent();
		}, Utils.random(60, 90), 45, TimeUnit.MINUTES);
	}
	
	/**
	 * Skill id for skill id. MODIFIER is divided by ten, so that it's rounded to
	 * the nearest tenth.
	 */
	public static int SKILL_ID = 0;
	public static double MODIFIER = 10;

	private static void addSkillerDreamTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				MODIFIER = 10 + Utils.random(5);
				String skillName = Skills.SKILL_NAME[SKILL_ID];
				if (MODIFIER == 10) {
					sendWorldMessage("<img=7><col=FFA500>[Skiller's Dream] The xp bonus for " + skillName + " is no longer active.", false, false);
				} else {
					SKILL_ID = Utils.random(0, 25);
					skillName = Skills.SKILL_NAME[World.SKILL_ID];
					int god = Utils.random(2);
					String godName = "Zamorak";
					switch (god) {
					case 0:
						godName = "Guthix";
						break;
					case 1:
						godName = "Saradomin";
						break;
					case 2:
						godName = "Zamorak";
						break;
					}
					sendWorldMessage("<img=7><col=FFA500>[Skiller's Dream] " + godName + " has granted everyone a " + (MODIFIER / 10) + "X xp bonus for " + skillName + "!", false, false);
					if (Settings.DISCORD)
						new MessageBuilder().append("[Skiller's Dream]  " + godName + " has granted everyone a " + (MODIFIER / 10) + "X xp bonus for " + skillName + "!").send(GameServer.getDiscordBot().getAPI().getTextChannelById("534549522894815236").get());
				}
			}
		}, 1, 4, TimeUnit.HOURS);
	}
	
	public static void penguinHS() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player players : World.getPlayers()) {
						if (players == null)
							continue;
						players.penguin = false;
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8104)
							continue;
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8105)
							continue;
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8107)
							continue;
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8108)
							continue;
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8109)
							continue;
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8110)
							continue;
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 14766)
							continue;
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 14415)
							continue;
						n.sendDeath(n);
					}
					PenguinEvent.startEvent();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 3600000);
	}
	
	private static void addWarbandsEventManager() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (Warbands.warband == null) {
					int random = Utils.random(WarbandEvent.values().length);
					if (WarbandEvent.getEvent(random) != null)
						Warbands.warband = new Warbands(random);
					return;
				}
				Warbands.warband.finish();
			}
		}, Utils.random(45, 60), 180, TimeUnit.MINUTES);
	}

	private static void addOwnedObjectsTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					OwnedObjectManager.processAll();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	
	
	private static void addRestoreShopItemsTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					ShopsHandler.restoreShops();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30, TimeUnit.SECONDS);
	}

	private static final void addSummoningEffectTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.getFamiliar() == null || player.isDead()
								|| !player.hasFinished())
							continue;
						if (player.getFamiliar().getOriginalId() == 6814) {
							player.heal(20);
							player.setNextGraphics(new Graphics(1507));
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 15, TimeUnit.SECONDS);
	}

	private static final void addRestoreSpecialAttackTask() {

		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead()
								|| !player.isRunning())
							continue;
						player.getCombatDefinitions().restoreSpecialAttack();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30000);
	}

	private static boolean checkAgility;

	private static final void addTriviaBotTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new TimerTask() {
			@Override
			public void run() {
				try {
					TriviaBot.Run();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 5, 5, TimeUnit.MINUTES);
	}
	
	private static final void addSuggestionManagerTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new TimerTask() {
			@Override
			public void run() {
				try {
					SuggestionManager.process();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 300, TimeUnit.SECONDS);
	}

	private static final void addRestoreRunEnergyTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null
								|| player.isDead()
								|| !player.isRunning()
								|| (checkAgility && player.getSkills()
										.getLevel(Skills.AGILITY) < 70))
							continue;
						player.restoreRunEnergy();
					}
					checkAgility = !checkAgility;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1000);
	}

	private static final void addDrainPrayerTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead()
								|| !player.isRunning())
							continue;
						player.getPrayer().processPrayerDrain();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 600);
	}

	private static final void addRestoreHitPointsTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead()
								|| !player.isRunning())
							continue;
						player.restoreHitPoints();
					}
					for (NPC npc : npcs) {
						if (npc == null || npc.isDead() || npc.hasFinished())
							continue;
						npc.restoreHitPoints();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 6000);
	}

	private static final void addRestoreSkillsTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || !player.isRunning())
							continue;
						int ammountTimes = player.getPrayer().usingPrayer(0, 8) ? 2
								: 1;
						if (player.isResting())
							ammountTimes += 1;
						boolean berserker = player.getPrayer()
								.usingPrayer(1, 5);
						for (int skill = 0; skill < 25; skill++) {
							if (skill == Skills.SUMMONING)
								continue;
							for (int time = 0; time < ammountTimes; time++) {
								int currentLevel = player.getSkills().getLevel(
										skill);
								int normalLevel = player.getSkills()
										.getLevelForXp(skill);
								if (currentLevel > normalLevel) {
									if (skill == Skills.ATTACK
											|| skill == Skills.STRENGTH
											|| skill == Skills.DEFENCE
											|| skill == Skills.RANGE
											|| skill == Skills.MAGIC) {
										if (berserker
												&& Utils.getRandom(100) <= 15)
											continue;
									}
									player.getSkills().set(skill,
											currentLevel - 1);
								} else if (currentLevel < normalLevel)
									player.getSkills().set(skill,
											currentLevel + 1);
								else
									break;
							}
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30000);

	}

	public static final Map<Integer, Region> getRegions() {
		// synchronized (lock) {
		return regions;
		// }
	}

	public static final Region getRegion(int id) {
		return getRegion(id, false);
	}

	public static final Region getRegion(int id, boolean load) {
		// synchronized (lock) {
		Region region = regions.get(id);
		if (region == null) {
			region = new Region(id);
			regions.put(id, region);
		}
		if(load)
			region.checkLoadMap();
		return region;
		// }
	}

	public static final void addNPC(NPC npc) {
		npcs.add(npc);
	}

	public static final void removeNPC(NPC npc) {
		npcs.remove(npc);
	}

	public static final NPC spawnNPC(int id, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		NPC n = null;
		if (id == 6142 || id == 6144 || id == 6145 || id == 6143)
			n = new PestMonsters(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id >= 5533 && id <= 5558)
			n = new Elemental(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 17161)
			n = new Vorago(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id >= 21136 && id <= 21143)
			n = new RuneDragon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 13820 || id == 13821 || id == 13822)
			n = new Jadinko(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		else if (id == 5079)
			n = new HunterTrapNPC(HunterNPC.GREY_CHINCHOMPA, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5080)
			n = new HunterTrapNPC(HunterNPC.RED_CHINCHOMPA, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 22997)
			n = new HunterTrapNPC(HunterNPC.TORTLE, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 7010 || id == 7011)
			n = new HunterTrapNPC(HunterNPC.GRENWALL, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 19443)
			n = new HunterTrapNPC(HunterNPC.SKILLCHOMPAS, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5081)
			n = new HunterTrapNPC(HunterNPC.FERRET, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6916)
			n = new HunterTrapNPC(HunterNPC.GECKO, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 7272)
			n = new HunterTrapNPC(HunterNPC.MONKEY, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 7272)
			n = new HunterTrapNPC(HunterNPC.RACCOON, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5073)
			n = new HunterTrapNPC(HunterNPC.CRIMSON_SWIFT, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5075)
			n = new HunterTrapNPC(HunterNPC.GOLDEN_WARBLER, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5076)
			n = new HunterTrapNPC(HunterNPC.COPPER_LONGTAIL, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5074)
			n = new HunterTrapNPC(HunterNPC.CERULEAN_TWITCH, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5072)
			n = new HunterTrapNPC(HunterNPC.TROPICAL_WAGTAIL, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 7031)
			n = new HunterTrapNPC(HunterNPC.WIMPY_BIRD, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 5088)
			n = new HunterTrapNPC(HunterNPC.BARB_TAILED_KEBBIT, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 7134)
			n = new Bork(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 14301)
			n = new Glacor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		else if (id == 9441)
			n = new FlameVortex(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id >= 8832 && id <= 8834)
			n = new LivingRock(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 3200)
			n = new ChaosElemental(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 16070)
			n = new TrainingDummy(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id >= 2881 && id <= 2883)
			n = new DagannothKing(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id >= 13465 && id <= 13481)
			n = new Revenant(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 1158 || id == 1160 || id == 16707 || id == 16708)
			n = new KalphiteQueen(id, tile);
		else if (id >= 8528 && id <= 8532)
			n = new Nomad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6215 || id == 6211 || id == 3406 || id == 6216|| id == 6214 || id == 6215|| id == 6212 || id == 6219 || id == 6221 || id == 6218)
			n = new GodwarsZamorakFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id >= 6254 && id <= 6259)
			n = new GodwarsSaradominFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6246 || id == 6236 || id == 6232 || id == 6240 || id == 6241 || id == 6242 || id == 6235 || id == 6234 || id == 6243 || id == 6236 || id == 6244 || id == 6237 || id == 6246 || id == 6238 || id == 6239 || id == 6230)
			n = new GodwarsArmadylFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6281 || id == 6282 || id == 6275 || id == 6279|| id == 9184 || id == 6268 || id == 6270 || id == 6274 || id == 6277 || id == 6276 || id == 6278)
			n = new GodwarsBandosFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6261 || id == 6263 || id == 6265)
			n = GodWarsBosses.graardorMinions[(id - 6261) / 2] = new GodWarMinion(id, 0, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6260)
			n = new GeneralGraardor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6222)
			n = new KreeArra(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6223 || id == 6225 || id == 6227)
			n = GodWarsBosses.armadylMinions[(id - 6223) / 2] = new GodWarMinion(id, 0, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6203)
			n = new KrilTstsaroth(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6204 || id == 6206 || id == 6208)
			n = GodWarsBosses.zamorakMinions[(id - 6204) / 2] = new GodWarMinion(id, 0, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 50 || id == 2642)
			n = new KingBlackDragon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id >= 9462 && id <= 9467)
			n = new Strykewyrm(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		else if (id == 6248 || id == 6250 || id == 6252)
			n = GodWarsBosses.commanderMinions[(id - 6248) / 2] = new GodWarMinion(id, 0, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 6247)
			n = new CommanderZilyana(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 8133)
			n = new CorporealBeast(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 17149)
			n = new Primus(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 19332)
			n = new Agoroth(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 13447)
		n = ZarosGodwars.nex = new Nex(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		else if (id == 19464)
		n = new Araxxi(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned, null);
		else if (id == 13451)
		n = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 13452)
		n = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 13453)
		n = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 13454)
		n = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 14256)
			n = new Lucien(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 16697 || id == 16698 || id == 16699)
			n = new KalphiteKing(id, tile);
		else if (id == 8335)
			n = new MercenaryMage(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 8349 || id == 8450 || id == 8451)
			n = new TormentedDemon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 15149)
			n = new MasterOfFear(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 1615)
			n = new AbyssalDemon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		else if (id == 18621)
			n = new Airut(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		else if (id == 2805 || id == 2806 || id == 2808)
			n = new DesertLizard(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		else if (id == 21136)
			n = new RuneDragon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (id == 16554)
			n = new JohnStrum(id, tile);
		else if (CorruptedCreature.isCorruptedCreature(id))
			n = new CorruptedCreature(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		else if (WispInfo.forNpcId(id) != null) {
			n = new Wisp(id, tile);
		}
		else 
			n = new NPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		return n;
	}

	public static final NPC spawnNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		return spawnNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	/*
	 * check if the entity region changed because moved or teled then we update
	 * it
	 */
	public static final void updateEntityRegion(Entity entity) {
		if (entity.hasFinished()) {
			if (entity instanceof Player)
				getRegion(entity.getLastRegionId()).removePlayerIndex(entity.getIndex());
			else 
				getRegion(entity.getLastRegionId()).removeNPCIndex(entity.getIndex());
			return;
		}
		int regionId = entity.getRegionId();
		if (entity.getLastRegionId() != regionId) { // map region entity at
			// changed
			if (entity instanceof Player) {
				if (entity.getLastRegionId() > 0)
					getRegion(entity.getLastRegionId()).removePlayerIndex(
							entity.getIndex());
				Region region = getRegion(regionId);
				region.addPlayerIndex(entity.getIndex());
				Player player = (Player) entity;
				int musicId = region.getRandomMusicId();
				if (musicId != -1)
					player.getMusicsManager().checkMusic(musicId);
				player.getControllerManager().moved();
				if(player.hasStarted())
					checkControlersAtMove(player);
			} else {
				if (entity.getLastRegionId() > 0)
					getRegion(entity.getLastRegionId()).removeNPCIndex(
							entity.getIndex());
				getRegion(regionId).addNPCIndex(entity.getIndex());
			}
			entity.checkMultiArea();
			entity.setLastRegionId(regionId);
		} else {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				player.getControllerManager().moved();
				if(player.hasStarted())
					checkControlersAtMove(player);
			}
			entity.checkMultiArea();
		}
	}

	private static void checkControlersAtMove(Player player) {
        if (!(player.getControllerManager().getController() instanceof RequestController) && RequestController.inWarRequest(player))
            player.getControllerManager().startController("clan_wars_request");
        else if (DuelControler.isAtDuelArena(player))
            player.getControllerManager().startController("DuelControler");
        else if (FfaZone.inArea(player))
            player.getControllerManager().startController("clan_wars_ffa");
    }

	/*
	 * checks clip
	 */
	public static boolean canMoveNPC(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++)
			for (int tileY = y; tileY < y + size; tileY++)
				if (getMask(plane, tileX, tileY) != 0)
					return false;
		return true;
	}

	/*
	 * checks clip
	 */
	public static boolean isNotCliped(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++)
			for (int tileY = y; tileY < y + size; tileY++)
				if ((getMask(plane, tileX, tileY) & 2097152) != 0)
					return false;
		return true;
	}

	public static int getMask(int plane, int x, int y) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return -1;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getMask(tile.getPlane(), baseLocalX, baseLocalY);
	}

	public static void setMask(int plane, int x, int y, int mask) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		region.setMask(tile.getPlane(), baseLocalX, baseLocalY, mask);
	}

	public static int getRotation(int plane, int x, int y) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return 0;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getRotation(tile.getPlane(), baseLocalX, baseLocalY);
	}

	private static int getClipedOnlyMask(int plane, int x, int y) {
		WorldTile tile = new WorldTile(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return -1;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region
				.getMaskClipedOnly(tile.getPlane(), baseLocalX, baseLocalY);
	}

	public static final boolean checkProjectileStep(int plane, int x, int y,
			int dir, int size) {
		int xOffset = Utils.DIRECTION_DELTA_X[dir];
		int yOffset = Utils.DIRECTION_DELTA_Y[dir];

		if (size == 1) {
			int mask = getClipedOnlyMask(plane, x
					+ Utils.DIRECTION_DELTA_X[dir], y
					+ Utils.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0)
				return (mask & 0x42240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (mask & 0x60240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (mask & 0x40a40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (mask & 0x48240000) == 0;
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0)
				return (getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) == 0
				&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (getClipedOnlyMask(plane, x + 2, y) & 0x60e40000) == 0
				&& (getClipedOnlyMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) == 0
				&& (getClipedOnlyMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (getClipedOnlyMask(plane, x, y + 2) & 0x4e240000) == 0
				&& (getClipedOnlyMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (getClipedOnlyMask(plane, x - 1, y) & 0x4fa40000) == 0
				&& (getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) == 0
				&& (getClipedOnlyMask(plane, x, y - 1) & 0x63e40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (getClipedOnlyMask(plane, x + 1, y - 1) & 0x63e40000) == 0
				&& (getClipedOnlyMask(plane, x + 2, y - 1) & 0x60e40000) == 0
				&& (getClipedOnlyMask(plane, x + 2, y) & 0x78e40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
				&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0
				&& (getClipedOnlyMask(plane, x, y + 2) & 0x7e240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (getClipedOnlyMask(plane, x + 1, y + 2) & 0x7e240000) == 0
				&& (getClipedOnlyMask(plane, x + 2, y + 2) & 0x78240000) == 0
				&& (getClipedOnlyMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getClipedOnlyMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getClipedOnlyMask(plane, x + (size - 1), y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0
					|| (getClipedOnlyMask(plane, sizeOffset - 1 + x,
							y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x + size, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + size, sizeOffset
							+ (-1 + y)) & 0x78e40000) != 0
							|| (getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x - 1, y + size) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
					|| (getClipedOnlyMask(plane, -1 + (x + sizeOffset),
							y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x + size, y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
					|| (getClipedOnlyMask(plane, x + size, y
							+ sizeOffset) & 0x78e40000) != 0)
						return false;
			}
		}
		return true;
	}

	public static final boolean checkWalkStep(int plane, int x, int y, int dir,
			int size) {
		int xOffset = Utils.DIRECTION_DELTA_X[dir];
		int yOffset = Utils.DIRECTION_DELTA_Y[dir];
		int rotation = getRotation(plane, x + xOffset, y + yOffset);
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				int fakeChunckX = xOffset;
				int fakeChunckY = yOffset;
				xOffset = fakeChunckY;
				yOffset = 0 - fakeChunckX;
			}
		}

		if (size == 1) {
			int mask = getMask(plane, x + Utils.DIRECTION_DELTA_X[dir], y
					+ Utils.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0)
				return (mask & 0x42240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (mask & 0x60240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (mask & 0x40a40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (mask & 0x48240000) == 0;
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0
						&& (getMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0
						&& (getMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0
						&& (getMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0
						&& (getMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0)
				return (getMask(plane, x - 1, y) & 0x43a40000) == 0
				&& (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (getMask(plane, x + 2, y) & 0x60e40000) == 0
				&& (getMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (getMask(plane, x, y - 1) & 0x43a40000) == 0
				&& (getMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (getMask(plane, x, y + 2) & 0x4e240000) == 0
				&& (getMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (getMask(plane, x - 1, y) & 0x4fa40000) == 0
				&& (getMask(plane, x - 1, y - 1) & 0x43a40000) == 0
				&& (getMask(plane, x, y - 1) & 0x63e40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (getMask(plane, x + 1, y - 1) & 0x63e40000) == 0
				&& (getMask(plane, x + 2, y - 1) & 0x60e40000) == 0
				&& (getMask(plane, x + 2, y) & 0x78e40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (getMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
				&& (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0
				&& (getMask(plane, x, y + 2) & 0x7e240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (getMask(plane, x + 1, y + 2) & 0x7e240000) == 0
				&& (getMask(plane, x + 2, y + 2) & 0x78240000) == 0
				&& (getMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getMask(plane, x + (size - 1), y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getMask(plane, x - 1, y - 1) & 0x43a40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0
					|| (getMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getMask(plane, x + size, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0
					|| (getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getMask(plane, x - 1, y + size) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
					|| (getMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getMask(plane, x + size, y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
					|| (getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			}
		}
		return true;
	}

	public static final boolean containsPlayer(String username) {
		for (Player p2 : players) {
			if (p2 == null)
				continue;
			if (p2.getUsername().equals(username))
				return true;
		}
		return false;
	}

	public static Player getPlayer(Player player2) {
		for (Player player : getPlayers()) {
			if (player == null)
				continue;
			if (player.getUsername().equals(player2))
				return player;
		}
		return null;
	}
	public static Player getPlayer(String username) {
		for (Player player : getPlayers()) {
			if (player == null)
				continue;
			if (player.getUsername().equals(username))
				return player;
		}
		return null;
	}
	public static final Player getPlayerByDisplayName(String username) {
		String formatedUsername = Utils.formatPlayerNameForDisplay(username);
		for (Player player : getPlayers()) {
			if (player == null)
				continue;
			if (player.getUsername().equalsIgnoreCase(formatedUsername)
					|| player.getDisplayName().equalsIgnoreCase(formatedUsername))
				return player;
		}
		return null;
	}

	public static final EntityList<Player> getPlayers() {
		return players;
	}

	public static final EntityList<NPC> getNPCs() {
		return npcs;
	}

	private World() {

	}

	public static final void safeShutdown(final boolean restart, int delay) {
		if (exiting_start != 0)
			return;
		exiting_start = Utils.currentTimeMillis();
		exiting_delay = delay;
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			player.getPackets().sendSystemUpdate(delay);
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					Lottery.INSTANCE.cancelLottery();
					for (Player player : World.getPlayers()) {
						if (player == null || !player.hasStarted())
							continue;
						player.realFinish();
					}
					GrandExchange.savePrices();
					GameServer.saveFiles();
					if (restart)
						GameServer.restart();
					else
						GameServer.shutdown();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, delay, TimeUnit.SECONDS);
	}

	public static final boolean isSpawnedObject(WorldObject object) {
		return getRegion(object.getRegionId()).getSpawnedObjects().contains(object);
	}

	public static final boolean isSpawnedObject(Player player, WorldObject object) {
		return getRegion(player.getRegionId()).getSpawnedObjects().contains(object);
	}

	public static final void spawnObject(WorldObject object) {
		getRegion(object.getRegionId()).spawnObject(object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion(), false);
	}

	public static final void spawnObject(Player player, WorldObject object) {
		getRegion(player.getRegionId()).spawnObject(player, object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion(), false);
	}

	public static final void unclipTile(WorldTile tile) {
		getRegion(tile.getRegionId()).unclip(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion());
	}

	public static final void removeObject(WorldObject object, boolean removeClip) {
		getRegion(object.getRegionId()).removeObject(object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion(), removeClip);
	}

	public static final void removeObject(WorldObject object) {
		getRegion(object.getRegionId()).removeObject(object, object.getPlane(), object.getXInRegion(), object.getYInRegion());
	}

	public static final void removeObject(Player player, WorldObject object, boolean removeClip) {
		getRegion(player.getRegionId()).removeObject(player, object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion(), removeClip);
	}

	public static final void removeObject(Player player, WorldObject object) {
		getRegion(player.getRegionId()).removeObject(player, object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion());
	}

	public static final void spawnObjectTemporary(final WorldObject object, long time) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (!World.isSpawnedObject(object)) {
						return;
					}
					removeObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static final void spawnObjectTemporary(final Player player, final WorldObject object, long time) {
		spawnObject(player, object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (!World.isSpawnedObject(player, object)) {
						return;
					}
					removeObject(player, object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}
	
	public static final void spawnObjectTemporary(final WorldObject object, long time,
			final boolean checkObjectInstance, boolean checkObjectBefore) {
		final WorldObject before = checkObjectBefore ? World.getObjectWithType(object, object.getType()) : null;
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (checkObjectInstance && World.getObjectWithId(object, object.getId()) != object) {
						return;
					}
					if (before != null) {
						spawnObject(before);
					}
					else {
						removeObject(object);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static final boolean removeObjectTemporary(final WorldObject object, long time, boolean removeClip) {
		removeObject(object, removeClip);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawnObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
		return true;
	}
	
	public static final boolean removeObjectTemporary(final WorldObject object, long time) {
		removeObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawnObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
		return true;
	}

	public static final boolean removeObjectTemporary(final Player player, final WorldObject object, long time, boolean removeClip) {
		removeObject(player, object, removeClip);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawnObject(player, object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
		return true;
	}

	public static final void spawnTempGroundObject(final WorldObject object, final int replaceId, long time,
			final boolean removeClip) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					removeObject(object, removeClip);
					addGroundItem(new Item(replaceId), object, null, false, 180);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, time, TimeUnit.MILLISECONDS);
	}

	public static final WorldObject getStandartObject(WorldTile tile) {
		return getRegion(tile.getRegionId()).getStandartObject(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion());
	}

	public static final WorldObject getObjectWithType(WorldTile tile, int type) {
		return getRegion(tile.getRegionId()).getObjectWithType(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion(), type);
	}

	public static final WorldObject getObjectWithSlot(WorldTile tile, int slot) {
		return getRegion(tile.getRegionId()).getObjectWithSlot(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion(), slot);
	}

	public static final boolean containsObjectWithId(WorldTile tile, int id) {
		return getRegion(tile.getRegionId()).containsObjectWithId(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion(), id);
	}

	public static final WorldObject getObjectWithId(WorldTile tile, int id) {
		return getRegion(tile.getRegionId()).getObjectWithId(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion(), id);
	}
	
	public static final void addGroundItem(final Item item, final WorldTile tile) {
		// adds item, not invisible, no owner, no time to disappear
		addGroundItem(item, tile, null, false, -1, 2, -1);
	}

	public static final void addGroundItem(final Item item, final WorldTile tile, final Player owner, boolean invisible, long hiddenTime) {
		addGroundItem(item, tile, owner, invisible, hiddenTime, 2, 60);
	}

	public static final FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner, boolean invisible, long hiddenTime, int type) {
		return addGroundItem(item, tile, owner, invisible, hiddenTime, type, 60);
	}

	public static final void turnPublic(FloorItem floorItem, int publicTime) {
		if (!floorItem.isInvisible())
			return;
		int regionId = floorItem.getTile().getRegionId();
		final Region region = getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(floorItem))
			return;
		Player realOwner = floorItem.hasOwner() ? World.getPlayer(floorItem.getOwner()) : null;
		if (!ItemConstants.isTradeable(floorItem)) {
			region.getGroundItemsSafe().remove(floorItem);
			if (realOwner != null) {
				if (realOwner.getMapRegionsIds().contains(regionId))
					realOwner.getPackets().sendRemoveGroundItem(floorItem);
			}
			return;
		}
		floorItem.setInvisible(false);
		for (Player player : getPlayers()) {
			if (player == null || player == realOwner || !player.hasStarted() || player.hasFinished()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			if ((floorItem.getName().toLowerCase().contains("toktz-xil-ul")
					|| floorItem.getName().toLowerCase().contains("throwing")
					|| floorItem.getName().toLowerCase().contains("javelin")
					|| floorItem.getName().toLowerCase().contains("darts")
					|| floorItem.getName().toLowerCase().contains("stake")
					|| floorItem.getName().toLowerCase().contains("arrow")
					|| floorItem.getName().toLowerCase().contains("bolts"))
					&& player.getEquipment().containsOneItem(26777, 26778, ItemIdentifiers.AVAS_ALERTER,
							ItemIdentifiers.AVAS_ACCUMULATOR, ItemIdentifiers.AVAS_ATTRACTOR,
							ItemIdentifiers.COMPLETIONIST_CAPE, ItemIdentifiers.COMPLETIONIST_CAPE_T)) {
				player.getInventory().addItem(floorItem.getId(), floorItem.getAmount());
				removeGroundItem(floorItem, publicTime);
				return;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
		// disapears after this time
		if (publicTime != -1)
			removeGroundItem(floorItem, publicTime);
	}

	@Deprecated
	public static final void addGroundItemForever(Item item, final WorldTile tile) {
		int regionId = tile.getRegionId();
		final FloorItem floorItem = new FloorItem(item, tile, true);
		final Region region = getRegion(tile.getRegionId());
		region.getGroundItemsSafe().add(floorItem);
		for (Player player : players) {
			if (player == null || player.hasFinished() || player.getPlane() != floorItem.getTile().getPlane()
					|| !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
	}

	/*
	 * type 0 - if not tradeable type 1 - if destroyable type 2 - no
	 */
	public static final FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner,
			boolean invisible, long hiddenTime, int type, final int publicTime) {
		if (type != 2) {
			if ((type == 0 && !ItemConstants.isTradeable(item)) || type == 1 && ItemConstants.isDestroy(item)) {

				int price = GrandExchange.getPrice(item.getId());
				if (price <= 0)
					return null;
				item.setId(995);
				item.setAmount(price);
			}
		}
		final FloorItem floorItem = new FloorItem(item, tile, owner, owner != null, invisible);
		floorItem.setAttributes(item.getAttributes());
		final Region region = getRegion(tile.getRegionId());
		region.getGroundItemsSafe().add(floorItem);
		if (invisible) {
			if (owner != null)
				owner.getPackets().sendGroundItem(floorItem);
			// becomes visible after x time
			if (hiddenTime != -1) {
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							turnPublic(floorItem, publicTime);
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}
				}, hiddenTime, TimeUnit.SECONDS);
			}
		} else {
			// visible
			int regionId = tile.getRegionId();
			for (Player player : getPlayers()) {
				if (player == null || !player.hasStarted() || player.hasFinished()
						|| !player.getMapRegionsIds().contains(regionId))
					continue;
				player.getPackets().sendGroundItem(floorItem);
			}
			// disapears after this time
			if (publicTime != -1)
				removeGroundItem(floorItem, publicTime);
		}
		return floorItem;
	}

	public static final void updateGroundItem(Item item, final WorldTile tile, final Player owner) {
		final FloorItem floorItem = World.getRegion(tile.getRegionId()).getGroundItem(item.getId(), tile, owner);
		if ((item.getName().toLowerCase().contains("toktz-xil-ul") || item.getName().toLowerCase().contains("throwing")
				|| item.getName().toLowerCase().contains("javelin") || item.getName().toLowerCase().contains("darts")
				|| item.getName().toLowerCase().contains("stake") || item.getName().toLowerCase().contains("arrow")
				|| item.getName().toLowerCase().contains("bolts"))
				&& owner.getEquipment().containsOneItem(26777, 26778, ItemIdentifiers.AVAS_ALERTER,
						ItemIdentifiers.AVAS_ACCUMULATOR, ItemIdentifiers.AVAS_ATTRACTOR,
						ItemIdentifiers.COMPLETIONIST_CAPE, ItemIdentifiers.COMPLETIONIST_CAPE_T)) {
			owner.getInventory().addItem(item);
			return;
		}
		if (floorItem == null) {
			addGroundItem(item, tile, owner, true, 360);
			return;
		}
		owner.getPackets().sendRemoveGroundItem(floorItem);
		floorItem.setAmount(floorItem.getAmount() + item.getAmount());
		owner.getPackets().sendGroundItem(floorItem);

	}
	
	private static final void removeGroundItem(final FloorItem floorItem, long publicTime) {
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					int regionId = floorItem.getTile().getRegionId();
					Region region = getRegion(regionId);
					if (!region.getGroundItemsSafe().contains(floorItem))
						return;
					region.getGroundItemsSafe().remove(floorItem);
					for (Player player : World.getPlayers()) {
						if (player == null || !player.hasStarted() || player.hasFinished()
								|| !player.getMapRegionsIds().contains(regionId))
							continue;
						player.getPackets().sendRemoveGroundItem(floorItem);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, publicTime, TimeUnit.SECONDS);
	}

	/*
	 * used for dung
	 */
	public static final boolean removeGroundItem(final FloorItem floorItem) {
		int regionId = floorItem.getTile().getRegionId();
		Region region = getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(floorItem))
			return false;
		region.getGroundItemsSafe().remove(floorItem);
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			player.getPackets().sendRemoveGroundItem(floorItem);
		}
		return true;
	}

	public static final boolean removeGroundItem(Player player, final FloorItem floorItem, boolean add) {
		int regionId = floorItem.getTile().getRegionId();
		Region region = getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(floorItem))
			return false;
		if (add && (player.getInventory().getFreeSlots() == 0 && (!floorItem.getDefinitions().isStackable()
				|| !player.getInventory().containsItem(floorItem.getId(), 1))) && floorItem.getId() != 995) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		region.getGroundItemsSafe().remove(floorItem);
		if (add) {
			Item t = new Item(floorItem.getId(), floorItem.getAmount());
			t.setAttributes(floorItem.getAttributes());
			player.getInventory().addItemMoneyPouch(t);
		}
		if (floorItem.isInvisible()) {
			player.getPackets().sendRemoveGroundItem(floorItem);
			return true;
		} else {
			for (Player p2 : World.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished() || !p2.getMapRegionsIds().contains(regionId))
					continue;
				System.out.println("Removed Drop");
				p2.getPackets().sendRemoveGroundItem(floorItem);
			}
			if (floorItem.isForever()) {
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							addGroundItemForever(floorItem, floorItem.getTile());
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}
				}, 60, TimeUnit.SECONDS);
			}
			return true;
		}
	}

	public static final boolean removeGroundItem(Player player, FloorItem floorItem) {
		return removeGroundItem(player, floorItem, true);
	}

	public static final void spawnTemporaryObject(final WorldObject object, long time) {
		spawnTemporaryObject(object, time, false);
	}

	public static final void spawnTemporaryObject(final WorldObject object, long time, final boolean clip) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (!World.isSpawnedObject(object)) {
						return;
					}
					removeObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static final void sendObjectAnimation(WorldObject object, Animation animation) {
		sendObjectAnimation(null, object, animation);
	}

	public static final void sendObjectAnimation(Entity creator, WorldObject object, Animation animation) {
		if (creator == null) {
			for (Player player : World.getPlayers()) {
				if (player == null || player.hasFinished() || !player.withinDistance(object)) {
					continue;
				}
				player.getPackets().sendObjectAnimation(object, animation);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
				if (playersIndexes == null) {
					continue;
				}
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || player.hasFinished() || !player.withinDistance(object)) {
						continue;
					}
					player.getPackets().sendObjectAnimation(object, animation);
				}
			}
		}
	}

	public static final void sendGraphics(Entity creator, Graphics graphics, WorldTile tile) {
		if (creator == null) {
			for (Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted() || player.hasFinished() || !player.withinDistance(tile))
					continue;
				player.getPackets().sendGraphics(graphics, tile);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
				if (playersIndexes == null)
					continue;
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted() || player.hasFinished()
							|| (!(creator instanceof Helwyr) && !player.withinDistance(tile)))
						continue;
					player.getPackets().sendGraphics(graphics, tile);
				}
			}
		}
	}
	
	public static final void sendProjectile(Entity shooter, Entity receiver, int gfxId, int startHeight, int endHeight,
			int speed, int delay, int curve) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null

						|| player.hasFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				int size = shooter.getSize();
				int distance = Utils.getDistance(shooter, receiver);
				int startOffsetDistance = distance > 2 ? 0 : 11;
				int startOffsetDistance2 = 0;
				player.getPackets().sendProjectile(receiver,
						new WorldTile(shooter.getCoordFaceX(size), shooter.getCoordFaceY(size), shooter.getPlane()),
						receiver, gfxId, startHeight, endHeight, speed, delay, curve,
						receiver instanceof Player ? startOffsetDistance : startOffsetDistance2, size);
			}
		}
	}

	public static final void sendProjectile(Entity shooter, WorldTile startTile, WorldTile receiver, int gfxId,
			int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null

						|| player.hasFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				player.getPackets().sendProjectile(null, startTile, receiver, gfxId, startHeight, endHeight, speed,
						delay, curve, startDistanceOffset, shooter.getSize());
			}
		}
	}

	public static final void sendProjectile(WorldTile shooter, Entity receiver, int gfxId, int startHeight,
			int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : receiver.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null

						|| player.hasFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				player.getPackets().sendProjectile(receiver, shooter, receiver, gfxId, startHeight, endHeight, speed,
						delay, curve, startDistanceOffset, 1);
			}
		}
	}

	public static final void sendProjectile(Entity shooter, WorldTile receiver, int gfxId, int startHeight,
			int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null

						|| player.hasFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				player.getPackets().sendProjectile(null, shooter, receiver, gfxId, startHeight, endHeight, speed, delay,
						curve, startDistanceOffset, shooter.getSize());
			}
		}
	}

	public static final void sendProjectile(Entity shooter, Entity receiver, int gfxId, int startHeight, int endHeight,
			int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || player.hasFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				int size = shooter.getSize();
				player.getPackets().sendProjectile(receiver, shooter, receiver, gfxId, startHeight, endHeight, speed,
						delay, curve, startDistanceOffset, size);
			}
		}
	}
	
	public static final Projectile sendProjectileNew(WorldTile from, WorldTile to, int graphicId, int startHeight,
			int endHeight, int startTime, double speed, int angle, int slope) {
		return sendProjectile(from, to, false, false, 0, graphicId, startHeight, endHeight, startTime, speed, angle,
				slope);
	}
	
	public static final Projectile sendProjectile(WorldTile from, WorldTile to, boolean adjustFlyingHeight,
			boolean adjustSenderHeight, int senderBodyPartId, int graphicId, int startHeight, int endHeight,
			int startTime, double speed, int angle, int slope) {
		int fromSizeX, fromSizeY;
		if (from instanceof Entity)
			fromSizeX = fromSizeY = ((Entity) from).getSize();
		else if (from instanceof WorldObject) {
			ObjectDefinitions defs = ((WorldObject) from).getDefinitions();
			fromSizeX = defs.getSizeX();
			fromSizeY = defs.getSizeY();
		} else
			fromSizeX = fromSizeY = 1;
		int toSizeX, toSizeY;
		if (to instanceof Entity)
			toSizeX = toSizeY = ((Entity) to).getSize();
		else if (to instanceof WorldObject) {
			ObjectDefinitions defs = ((WorldObject) to).getDefinitions();
			toSizeX = defs.getSizeX();
			toSizeY = defs.getSizeY();
		} else
			toSizeX = toSizeY = 1;

		Projectile projectile = new Projectile(from, to, adjustFlyingHeight, adjustSenderHeight, senderBodyPartId,
				graphicId, startHeight, endHeight, startTime,
				startTime + (speed == -1
						? Utils.getProjectileTimeSoulsplit(from, fromSizeX, fromSizeY, to, toSizeX, toSizeY)
						: Utils.getProjectileTimeNew(from, fromSizeX, fromSizeY, to, toSizeX, toSizeY, speed)),
				slope, angle);
		getRegion(from.getRegionId()).addProjectile(projectile);
		return projectile;
	}

	public static final boolean isMultiArea(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 3462 && destX <= 3511 && destY >= 9481 && destY <= 9521 && tile.getPlane() == 0 // kalphite lair
				|| destX >= 4540 && destX <= 4799 && destY >= 5052 && destY <= 5183 && tile.getPlane() == 0 // thzaar city
				|| destX >= 1721 && destX <= 1791 && destY >= 5123 && destY <= 5249
				|| destX >= 2250 && destX <= 2280 && destY >= 4670 && destY <= 4720
				|| destX >= 2987 && destX <= 3006 && destY >= 3912 && destY <= 3937
				|| destX >= 2895 && destX <= 2937 && destY >= 4430 && destY <= 4472
				|| destX >= 2245 && destX <= 2295 && destY >= 4675 && destY <= 4720
				|| destX >= 2450 && destX <= 3520 && destY >= 9450 && destY <= 9550
				|| destX >= 3006 && destX <= 3071 && destY >= 3602 && destY <= 3710
				|| destX >= 3134 && destX <= 3192 && destY >= 3519 && destY <= 3646
				|| destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375// wild
				|| destX >= 1790 && destX <= 1987 && destY >= 3194 && destY <= 3273 // soul wars
				|| destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230 // godwars
				|| destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699
				|| destX >= 3136 && destX <= 3327 && destY >= 3519 && destY <= 3607 // WILDY
				|| destX >= 3190 && destX <= 3327 && destY >= 3648 && destY <= 3839
				|| destX >= 3200 && destX <= 3390 && destY >= 3840 && destY <= 3967
				|| destX >= 2992 && destX <= 3007 && destY >= 3912 && destY <= 3967
				|| destX >= 2946 && destX <= 2959 && destY >= 3816 && destY <= 3831
				|| destX >= 3008 && destX <= 3199 && destY >= 3856 && destY <= 3903
				|| destX >= 3008 && destX <= 3071 && destY >= 3600 && destY <= 3711
				|| destX >= 3072 && destX <= 3327 && destY >= 3608 && destY <= 3647
				|| destX >= 2624 && destX <= 2690 && destY >= 2550 && destY <= 2619
				|| destX >= 2371 && destX <= 2422 && destY >= 5062 && destY <= 5117
				|| destX >= 2896 && destX <= 2927 && destY >= 3595 && destY <= 3630
				|| destX >= 2892 && destX <= 2932 && destY >= 4435 && destY <= 4464
				|| destX >= 2256 && destX <= 2287 && destY >= 4680 && destY <= 4711
				|| destX >= 2863 && destX <= 2878 && destY >= 5350 && destY <= 5372 
				|| KingBlackDragon.atKBD(tile) // KBD
				|| TormentedDemon.atTD(tile) // Tormented demon's area
				|| Bork.atBork(tile) // Bork's area
				|| destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400// corp
				|| destX >= 3195 && destX <= 3327 && destY >= 3520 && destY <= 3970
				|| destX >= 2376 && 5127 >= destY && destX <= 2422 & 5168 <= destY
				|| destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168 // pits
				|| destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752 // torms
				|| destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135 // castlewars
				|| destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532 // castlewars
				|| destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631 // Risk																		// ffa.
				|| destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631 // Safe
				|| destX >= 4160 && destY >= 5695 && destX <= 4223 && destY <= 5760 // Glacors
				|| destX >= 2654 && destY >= 3706 && destX <= 2723 && destY <= 3754 
				|| destX >= 4481 && destY >= 6207 && destX <= 4607 && destY <= 6333 
				|| destX >= 2691 && destY >= 9410 && destX <= 2748 && destY <= 9538 
				|| destX >= 999 && destY >= 558 && destX <= 1209 && destY <= 765 
				|| destX >= 2955 && destY >= 1735 && destX <= 2997 && destY <= 1783 
				|| destX >= 3009 && destY >= 5955 && destX <= 3135 && destY <= 6136
				|| destX >= 1600 && destX <= 1663 && destY >= 5249 && destY <= 5320
				|| destX >= 3218 && destY >= 5079 && destX <= 3246 && destY <= 5159;
	}

	public static final boolean isPvpArea(WorldTile tile) {
		return Wilderness.isAtWild(tile);
	}

	public static void sendWorldMessage(String message, boolean forStaff, boolean isYell) {
		for (Player p : World.getPlayers()) {
			if (p == null || !p.isRunning() || forStaff && p.getRights() == 0) {
				continue;
			}
			if (isYell) {
				if (p.isYellOff() && p.getRights() < 1) {
					continue;
				}
			}
			if (!isYell) {
				if (p.isWorldMessageOff() && p.getRights() < 1) {
					continue;
				}
			}
			p.getPackets().sendGameMessage(message);
		}
	}

	public static void addItemsAll(Item item) {
		for (Player p : World.getPlayers()) {
			if (p == null) {
				continue;
			}
			p.getInventory().addItemDrop(item.getId(), item.getAmount());
		}
	}

	public static final void sendProjectile(WorldObject object, WorldTile startTile, WorldTile endTile, int gfxId,
			int startHeight, int endHeight, int speed, int delay, int curve, int startOffset) {
		for (Player pl : getPlayers()) {
			if (pl == null || !pl.withinDistance(object, 20))
				continue;
			pl.getPackets().sendProjectile(null, startTile, endTile, gfxId, startHeight, endHeight, speed, delay, curve,
					startOffset, 1);
		}
	}
	
	private static final EntityList<Player> lobbyPlayers = new EntityList<Player>(Settings.PLAYERS_LIMIT);

	public static final Player getLobbyPlayerByDisplayName(String username) {
		String formatedUsername = Utils.formatPlayerNameForDisplay(username);
		for (Player player : getLobbyPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equalsIgnoreCase(formatedUsername) || player.getDisplayName().equalsIgnoreCase(formatedUsername)) {
				return player;
			}
		}
		return null;
	}

	public static final EntityList<Player> getLobbyPlayers() {
		return lobbyPlayers;
	}
		
	public static final void addPlayer(Player player) {
		players.add(player);
		if (World.containsLobbyPlayer(player.getUsername())) {
			World.removeLobbyPlayer(player);
			AntiFlood.remove(player.getSession().getIP());
		}
		AntiFlood.add(player.getSession().getIP());
	}

	public static final void addLobbyPlayer(Player player) {
		lobbyPlayers.add(player);
		AntiFlood.add(player.getSession().getIP());
	}

	public static final boolean containsLobbyPlayer(String username) {
		for (Player p2 : lobbyPlayers) {
			if (p2 == null) {
				continue;
			}
			if (p2.getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	public static void removeLobbyPlayer(Player player) {
		for (Player p : lobbyPlayers) {
			if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
				if (player.getCurrentFriendChat() != null) {
					player.getCurrentFriendChat().leaveChat(player, true);
				}
				lobbyPlayers.remove(p);
			}
		}
		AntiFlood.remove(player.getSession().getIP());
	}

	public static void removePlayer(Player player) {
		for (Player p : players) {
			if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
				players.remove(p);
			}
		}
		AntiFlood.remove(player.getSession().getIP());
	}
	public static int checkWildernessPlayers() {
		int pkers = 0;
		for (Player pker : getPlayers()) {
			if (pker == null) {
				continue;
			}
			if (!Wilderness.isAtWild(pker)) {
				continue;
			} else {
				pkers++;
			}
		}
		return pkers;
	}
	
	public static int checkStakers() {
		int stakers = 0;
		for (Player staker : getPlayers()) {
			if (staker == null) {
				continue;
			}
			if (staker.getRegionId() != 13363) {
				continue;
			} else {
				stakers++;
			}
		}
		return stakers;
	}

	public static int checkStaffOnline() {
		int staffs = 0;
		for (Player staff : getPlayers()) {
			if (staff == null) {
				continue;
			}
			if (staff.getRights() == 0) {
				continue;
			} else {
				staffs++;
			}
		}
		return staffs;
	}
	public static int getIdFromName(String playerName) {
		for (Player p : players) {
			if (p == null) {
				continue;
			}
			if (p.getUsername().equalsIgnoreCase(
					Utils.formatPlayerNameForProtocol(playerName))) {
				return p.getIndex();
			}
		}
		return 0;
	}

	public static final void sendLootbeam(Player player, WorldTile tile) {
		if (player == null || player.hasFinished() || !player.withinDistance(tile)) {
			return;
		}
		player.getPackets().sendGraphics(new Graphics(4422), tile);
		player.getPackets().sendGameMessage(Colors.ORANGE + "<shad=000000>A golden beam shines over one of your items.", true);
	}

	public static boolean isTileFree(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (!isFloorFree(plane, tileX, tileY) || !isWallsFree(plane, tileX, tileY)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isFloorFree(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (!isFloorFree(plane, tileX, tileY)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isFloorFree(int plane, int x, int y) {
		return (getMask(plane, x, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ)) == 0;
	}

	public static boolean isWallsFree(int plane, int x, int y) {
		return (getMask(plane, x, y) & (Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_NORTHWEST
				| Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST | Flags.WALLOBJ_EAST | Flags.WALLOBJ_NORTH
				| Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST)) == 0;
	}
	
	private static int dayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static boolean isWeekend() {
		return dayOfWeek() == 1 ? true : dayOfWeek() == 6 ? true : dayOfWeek() == 7 ? true : false;
	}

	/**
	 * Finds an NPC in the world by its NPC ID.
	 * 
	 * @param id
	 *            The ID to find.
	 * @return The NPC.
	 */
	public static NPC findNPC(int id) {
		NPC npc = null;
		for (NPC n : getNPCs()) {
			if (n == null) {
				continue;
			}
			if (n.getId() == id) {
				npc = n;
			}
		}
		return npc;
	}

	/**
	 * Finds the NPC by it's id.
	 * 
	 * @param player
	 *            The player searching.
	 * @param id
	 *            The NPC ID to search for.
	 * @return If NPC found.
	 */
	public static NPC findNPC(Player player, int id) {
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id) {
				continue;
			}
			if (npc.getRegionId() == player.getRegionId()) {
				return npc;
			}
		}
		return null;
	}

	public static void sendWarbandsNews(String message, int type) {
		String m = "<shad=000>News: " + message + "</shad></col>";
		if (type == 0)
			m = "<img=7><col=D80000>" + m;
		else if (type == 1)
			m = "<img=6><col=ba5409>" + m;
		else if (type == 2)
			m = "<img=5><col=079209>" + m;
		else if (type == 3)
			m = "<img=7><col=68b484>" + m;
		else if (type == 4)
			m = "<img=7><col=00c5ff>" + message;

		for (Player p : World.getPlayers()) {
			if (p == null || !p.isRunning())
				continue;
			p.getPackets().sendGameMessage(m, true);
		}
	}

	public static final void spawnBurningWeb(final WorldObject object, long time) {
		spawnTemporaryObject(object, time, false);
	}

	public static final WorldObject getRealObject(WorldTile tile, int slot) {
		return getRegion(tile.getRegionId()).getRealObject(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion(),
				slot);
	}
	
	public static final void spawnTemporaryDivineObject(final WorldObject object, long time, final Player player) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (!World.isSpawnedObject(object)) {
						return;
					}
					removeObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, time, TimeUnit.MILLISECONDS);
	}
	
	public static final void spawnPortable(final WorldObject object, long time, final Player player) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (!World.isSpawnedObject(object)) {
						return;
					}
					Portables.removePortable(player, object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, time, TimeUnit.MILLISECONDS);
	}

	public static final void spawnObject(WorldObject object, boolean clip) {
		getRegion(object.getRegionId()).spawnObject(object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion(), false);
	}

	public static void sendNews(String message, int type) {
		sendNews(null, message, type);
	}

	/*
	 * 0 - all worlds 1 - just this world 2 - friend 3 - game news
	 */
	public static void sendNews(Player from, String message, int type) {
		String m = "<shad=000>News: " + message + "</shad></col>";
		if (type == 0)
			m = "<img=5><col=D80000>" + m;
		else if (type == 1)
			m = "<img=6><col=ba5409>" + m;
		else if (type == 2)
			m = "<img=5><col=079209>" + m;
		else if (type == 3)
			m = "<img=7><col=68b484>" + m;
		else if (type == 4)
			m = "<img=7><col=00c5ff>Lottery: " + message;
		for (Player p : World.getPlayers()) {
			if (p == null || !p.isRunning() || (type == 2 && p != from))
				continue;
			p.getPackets().sendGameMessage(m, true);
		}
	}

	public static List<Player> getLocalPlayers(WorldTile location) {
		List<Player> localPlayers = new ArrayList<Player>();
		for (Player n : getPlayers())
			if (n != null)
				if (!localPlayers.contains(n))
					if (n.withinDistance(location, 14))
						localPlayers.add(n);
		return localPlayers;
	}
}
