package com.rs.game.npc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.javacord.api.entity.message.MessageBuilder;

import com.rs.GameServer;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.HeadIcon;
import com.rs.game.Hit;
import com.rs.game.Projectile;
import com.rs.game.SecondBar;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.instances.BossInstance;
import com.rs.game.Hit.HitLook;
import com.rs.game.RegionBuilder;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.combat.NPCCombat;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.combat.NPCvsNPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.pet.Pet;
import com.rs.game.npc.slayer.CorruptedCreature;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.herblore.HerbCleaning;
import com.rs.game.player.actions.herblore.HerbCleaning.Herbs;
import com.rs.game.player.actions.prayer.Burying;
import com.rs.game.player.actions.prayer.Burying.Bone;
import com.rs.game.player.actions.slayer.SlayerManager;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.activities.gungame.SurvivalEvent;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.events.GlobalEvents.Event;
import com.rs.game.player.content.activities.gungame.Survival;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.controllers.FightCaves;
import com.rs.game.player.controllers.FightKiln;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Color;
import com.rs.utils.Colors;
import com.rs.utils.DiscordBot;
import com.rs.utils.LogSystem;
import com.rs.utils.Logger;
import com.rs.utils.MapAreas;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;

public class NPC extends Entity implements Serializable {

	private static final long serialVersionUID = -4794678936277614443L;

	public int id;
	public int amount;
	private WorldTile respawnTile;
	private int mapAreaNameHash;
	private int waveId;
	protected boolean canBeAttackFromOutOfArea;
	private boolean randomwalk;
	private Integer[] lastTile;
	protected int[] bonuses;
	private boolean spawned;
	private transient NPCCombat combat;
	public WorldTile forceWalk;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private int capDamage;
	private int lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	public boolean isInfected;
	private int forceTargetDistance;
	private boolean forceFollowClose;
	private boolean intelligentRouteFinder;
	private boolean forceMultiAttacked;
	private boolean noClipWalking;
	private boolean noDistanceCheck;

	// npc masks
	private transient Transformation nextTransformation;
	// name changing masks
	private String name;
	private transient boolean changedName;
	private int combatLevel;
	private transient boolean changedCombatLevel;
	private transient boolean locked;
	private transient boolean refreshHeadIcon;
	private transient BossInstance bossInstance;

	private SecondBar secondBar;

	public NPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new WorldTile(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		this.setSpawned(spawned);
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk((getDefinitions().walkMask & 0x2) != 0 || forceRandomWalk(id));
		setBonuses();
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || refreshHeadIcon || nextTransformation != null || changedCombatLevel
				|| changedName;
	}

	public void transformIntoNPC(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
	}

	public void setNPC(int id) {
		this.id = id;
		setBonuses();
	}
	
	public void setBonuses() {
		bonuses = NPCBonuses.getBonuses(id);
		if (bonuses == null) {
			bonuses = new int[10];
			int level = getCombatLevel();
			for (int i = 0; i < bonuses.length; i++) {
				bonuses[i] = level;
			}
		}
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		nextTransformation = null;
		changedCombatLevel = false;
		changedName = false;
		refreshHeadIcon = false;
	}

	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}

	public boolean isIntelligentRouteFinder() {
		return intelligentRouteFinder;
	}

	public void setIntelligentRouteFinder(boolean intelligentRouteFinder) {
		this.intelligentRouteFinder = intelligentRouteFinder;
	}

	public void setCanBeAttackFromOutOfArea(boolean b) {
		canBeAttackFromOutOfArea = b;
	}
	
	public void setBossInstance(BossInstance instance) {
		bossInstance = instance;
	}

	public BossInstance getBossInstance() {
		return bossInstance;
	}

	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}

	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(id);
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
	}

	@Override
	public int getMaxHitpoints() {
		return getCombatDefinitions().getHitpoints();
	}

	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	public int getId() {
		return id;
	}

	public NPC(int id, int waveId, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super(tile);
		if (getName() != "null" || getName() != null) {
			this.id = id;
			this.waveId = waveId;
			this.respawnTile = new WorldTile(tile);
			this.mapAreaNameHash = mapAreaNameHash;
			this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
			this.setSpawned(spawned);
			combatLevel = -1;
			setHitpoints(getMaxHitpoints());
			setDirection(getRespawnDirection());
			setRandomWalk((getDefinitions().walkMask & 0x2) != 0 || forceRandomWalk(id));
			bonuses = NPCBonuses.getBonuses(id);
			combat = new NPCCombat(this);
			capDamage = -1;
			lureDelay = 12000;
			initEntity();
			World.addNPC(this);
			World.updateEntityRegion(this);
			loadMapRegions();
			checkMultiArea();
		}
	}

	public void processNPC() {
		if (isDead() || locked)
			return;
		/**
		 * Name changes
		 */
		if (getId() == 15158) {
			setName("" + Settings.SERVER_NAME + " Manager");
		}
		if (getId() == 22435) {
			setNextFaceWorldTile(new WorldTile(3203, 6939, 1));
		}
		if (getId() == 22436) {
			setNextFaceWorldTile(new WorldTile(3196, 6939, 1));
		}
		if (getId() == 945) {
			setName("" + Settings.SERVER_NAME + " Guide");
		}
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
				// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getFreezeDelay() < Utils.currentTimeMillis()) {
							if (((hasRandomWalk()) && World.getRotation(getPlane(), getX(), getY()) == 0) // temporary
									// fix
									&& Math.random() * 1000.0 < 100.0) {
								int moveX = (int) Math.round(Math.random() * 10.0 - 5.0);
								int moveY = (int) Math.round(Math.random() * 10.0 - 5.0);
								resetWalkSteps();
								if (getMapAreaNameHash() != -1) {
									if (!MapAreas.isAtArea(getMapAreaNameHash(), this)) {
										forceWalkRespawnTile();
										return;
									}
									addWalkSteps(getX() + moveX, getY() + moveY, 5);
								} else
									addWalkSteps(respawnTile.getX() + moveX, respawnTile.getY() + moveY, 5);
							}
						}
					}
				}
			}
		}
		if (isForceWalking()) {
			if (getFreezeDelay() < Utils.currentTimeMillis()) {
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps())
						addWalkSteps(forceWalk.getX(), forceWalk.getY(), getSize(), true);
					if (!hasWalkSteps()) {
						setNextWorldTile(new WorldTile(forceWalk));
						forceWalk = null;
					}
				} else
					forceWalk = null;
			}
		}
	}

	public void setNextNPCTransformation(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
		if (getCustomCombatLevel() != -1) {
			changedCombatLevel = true;
		}
		if (getCustomName() != null) {
			changedName = true;
		}
	}

	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
	}

	public int getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.anInt853 << 32 != 0 && definitions.respawnDirection > 0 && definitions.respawnDirection <= 8)
			return (4 + definitions.respawnDirection) << 11;
		return 0;
	}

	private static boolean forceRandomWalk(int npcId) {
		switch (npcId) {
		case 11226:
			return true;
		case 3341:
		case 3342:
		case 3343:
			return true;
		default:
			return false;
		}
	}

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final NPC target = this;
		if (hit.getDamage() > 0) {
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0) {
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
				}
				double heal;
				if (((Player) user).getPerkManager().hasPerk(PlayerPerks.SOUL_ABSORPTION)) {
					heal = 10.0;
				} else {
					heal = 5.0;
				}
				if (((Player) user).getEquipment().getAmuletId() == 31875 && Utils.random(100) >= 50) {
					heal -= Utils.random(1.25, 2.5); // We reduce since it divides
				}
				user.applyHit((int) (hit.getDamage() / heal), HitLook.HEALED_DAMAGE);
			}
		}, Utils.projectileTimeToCycles(World.sendProjectileNew(target, user, 2263, 11, 11, 20, 5, 0, 0).getEndTime()));
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (capDamage != -1 && hit.getDamage() > capDamage)
			hit.setDamage(capDamage);
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.getPrayer().hasPrayersOn()) {
				if (p2.getPrayer().usingPrayer(1, 18)) {
					sendSoulSplit(hit, p2);
				}
				if (hit.getDamage() == 0)
					return;
				if (!p2.getPrayer().isBoostedLeech()) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 19)) {
							p2.getPrayer().setBoostedLeech(true);
							return;
						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
							// att
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(0)) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(0);
									p2.getPackets().sendGameMessage(
											"Your curse drains Attack from the enemy, boosting your Attack.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2215, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
							}
						} else {
							if (p2.getPrayer().usingPrayer(1, 10)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(3)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(3);
										p2.getPackets().sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2231, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2232));
										}
									}, 1);
									return;
								}
							}
							if (p2.getPrayer().usingPrayer(1, 14)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(7)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(7);
										p2.getPackets().sendGameMessage(
												"Your curse drains Strength from the enemy, boosting your Strength.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2248, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2250));
										}
									}, 1);
									return;
								}
							}

						}
					}
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(1)) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(1);
									p2.getPackets().sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2217));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2218, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2219));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 11)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(4)) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(4);
									p2.getPackets().sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2236, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2238));
									}
								});
								return;
							}
						}
					}
					if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(2)) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(2);
									p2.getPackets().sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2220));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2221, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 12)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(5)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(5);
									p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2240, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2242));
									}
								}, 1);
								return;
							}
						}
					}

					// overall

					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Utils.getRandom(10) == 0) {
							if (p2.getPrayer().reachedMax(6)) {
								p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
							} else {
								p2.getPrayer().increaseLeechBonus(6);
								p2.getPackets().sendGameMessage("Your curse drains Defence from the enemy, boosting your Defence.", true);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							World.sendProjectile(p2, this, 2244, 35, 35, 20, 5, 0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									setNextGraphics(new Graphics(2246));
								}
							}, 1);
							return;
						}
					}
				}
			}
		}

	}

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		bonuses = NPCBonuses.getBonuses(id); // back to real bonuses
		forceWalk = null;
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		setFinished(true);
		World.updateEntityRegion(this);
		World.removeNPC(this);
	}

	public void setRespawnTask() {
		if (bossInstance != null && bossInstance.isFinished()) {
			return;
		}
		if (!hasFinished()) {
			reset();
			setLocation(respawnTile);
			finish();
		}
		long respawnDelay = (NPCDefinitions.getNPCDefinitions(id).getName().toLowerCase().contains("kingly") ? 300 : getCombatDefinitions().getRespawnDelay()) * 600;
		if (bossInstance != null) {
			respawnDelay /= bossInstance.getSettings().getSpawnSpeed();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (bossInstance != null && bossInstance.isFinished())
						return;
					spawn();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, respawnDelay, TimeUnit.MILLISECONDS);
	}

	public void deserialize() {
		if (combat == null)
			combat = new NPCCombat(this);
		spawn();
	}

	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		World.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}

	public NPCCombat getCombat() {
		return combat;
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		Player killer = getMostDamageReceivedSourcePlayer();
		String npcName = getDefinitions().name.toLowerCase();
		int id = getDefinitions().getId();
		resetWalkSteps();
		combat.removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (killer instanceof Player) {
						LogSystem.npcDeathLog(killer, npcName, id);
						increaseKillStatistics(killer, npcName);
						killer.getGamePointManager().increaseGamePoints((getMaxHitpoints() + getMaxHit()) / 15);
						killer.setAttackedByDelay(0);
						if (id == 86) {
							if (killer.getControllerManager().getController() instanceof DiccusTutorial && killer.getStatistics().getTutorialStage() == 18) {
								killer.getStatistics().tutorialStage = 19;
								killer.getDialogueManager().startDialogue("SimpleNPCMessage", 944, "Head back and talk to me when you're ready to move on.");
							}
						}
						if (id == 8164 && killer.getControllerManager().getController() instanceof Survival) {
							SurvivalEvent.applyReward(killer, killer.getBotKillstreak());
						}
						if (getName().toLowerCase().contains("banshee") && killer.getEquipment().getHatId() == ItemIdentifiers.MASK_OF_MOURNING) {
							killer.getStatistics().increaseBansheeKills();
						}
						if (getName().toLowerCase().contains("crawling hand") && killer.getEquipment().getHatId() == ItemIdentifiers.MASK_OF_BROKEN_FINGERS) {
							killer.getStatistics().increaseCrawlingHandKills();
						}
					}
					drop();
					reset();
					setLocation(respawnTile);
					finish();
					if (!isSpawned())
						setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	protected void increaseKillStatistics(Player killer, String name) {
		if (killer.increaseKillStatistics(name, false) != -1) {
			if (killer.isToggleKillcount()) {
				killer.getPackets().sendGameMessage(
						"You have slain a total of " + Colors.RED + killer.increaseKillStatistics(name, true) + ""
								+ "</col> x " + Colors.RED + name + "'s</col>.", true);
			} else
				killer.increaseKillStatistics(name, true);
		}
	}

	public void drop() {
		Drop[] drops = NPCDrops.getDrops(id);
		Player killer = getMostDamageReceivedSourcePlayer();
		if (drops == null)
			return;
		if (killer == null)
			return;
		if (killer.getControllerManager().getController() instanceof FightKiln || killer.getControllerManager().getController() instanceof FightCaves) {
			return;
		}
		if (isClueScrollNPC(getDefinitions().name)
				&& Utils.random(killer.getPerkManager().hasPerk(PlayerPerks.INVESTIGATOR) ? 285 : 300) <= 1) {
			if (!killer.getTreasureTrails().hasClueScrollItem()) {
				killer.getTreasureTrails().resetCurrentClue();
				int itemId = getCombatLevel() < 50 ? 2677 : getCombatLevel() < 150 ? 2801 : getCombatLevel() < 250 ? 2722 : 19043;
				World.updateGroundItem(new Item(itemId), new WorldTile(this), killer);
			}
		}
		if (killer.getPerkManager().hasPerk(PlayerPerks.HERB_LAW)) {
			int chance = getCombatLevel() >= 400 ? 5 : getCombatLevel() >= 200 ? 4 : getCombatLevel() >= 100 ? 3 : getCombatLevel() >= 50 ? 2 : 1;
			if (Utils.random(150) <= chance) {
				World.addGroundItem(new Item(34927, 1), killer);
				killer.getPackets().sendGameMessage(Color.ORANGE, "You've received a Herb Box drop thanks to your Herb Law Perk.", true);
			}
		}
		Player otherPlayer = killer.getSlayerManager().getSocialPlayer();
		SlayerManager manager = killer.getSlayerManager();
		if (manager.isValidTask(getName()))
			manager.checkCompletedTask(getDamageReceived(killer), otherPlayer != null ? getDamageReceived(otherPlayer) : 0);
		if (killer.getEquipment().getRingId() == 31869) {
			if (Utils.random(100) >= 50) {
				int toRestore = getMaxHitpoints() / 150;
				int special = killer.getCombatDefinitions().getSpecialAttackPercentage();
				if ((toRestore > 0 || toRestore <= 5) && special + toRestore <= 100) {
					killer.getCombatDefinitions().restoreSpecialAttack(special + toRestore);
				}
			}
		}
		boolean hasBonecrusher = killer.getInventory().containsOneItem(ItemIdentifiers.BONECRUSHER)
				|| killer.getEquipment().containsOneItem(ItemIdentifiers.BONECRUSHER);
		boolean hasHerbicide = killer.getInventory().containsOneItem(ItemIdentifiers.HERBICIDE)
				|| killer.getEquipment().containsOneItem(ItemIdentifiers.HERBICIDE);
		for (Drop drop : drops) {
			if (drop.getRate() == 100) {
				if (hasBonecrusher) {
					Bone bone = Bone.forId(drop.getItemId());
					if (bone != null) {
						final int maxPrayer = killer.getSkills().getLevelForXp(Skills.PRAYER) * 10;

						Burying.handlePrayerBonus(killer, bone, maxPrayer);
						killer.getSkills().addXp(Skills.PRAYER, bone.getExperience());
						continue;
					}
				}
				if (hasHerbicide) {
					final Herbs herb = HerbCleaning.getHerb(drop.getItemId());
					if (herb != null && killer.getSkills().getLevel(Skills.HERBLORE) >= herb.getLevel()) {
						killer.getSkills().addXp(Skills.HERBLORE, herb.getExperience() * 2);
						continue;
					}
				}
				sendDrop(killer, drop, getSize());
			}
		}
		Drop regularDrop = NPCDrops.generateRegularDrop(killer, drops, killer.getDropRateModifier());
		if (regularDrop != null) {
			if (hasBonecrusher) {
				Bone bone = Bone.forId(regularDrop.getItemId());
				if (bone != null) {
					final int maxPrayer = killer.getSkills().getLevelForXp(Skills.PRAYER) * 10;

					Burying.handlePrayerBonus(killer, bone, maxPrayer);
					killer.getSkills().addXp(Skills.PRAYER, bone.getExperience());
					return;
				}
			}
			if (hasHerbicide) {
				final Herbs herb = HerbCleaning.getHerb(regularDrop.getItemId());
				if (herb != null && killer.getSkills().getLevel(Skills.HERBLORE) >= herb.getLevel()) {
					killer.getSkills().addXp(Skills.HERBLORE, herb.getExperience() * 2);
					return;
				}
			}
			sendDrop(killer, regularDrop, getSize());
			NPCDrops.sendDropRateModifierMessage(killer, regularDrop);
		}
	}

	public void sendDrop(Player player, Drop drop2, int size) {
		boolean stackable = ItemDefinitions.getItemDefinitions(drop2.getItemId()).isStackable();
		Item item = stackable ? new Item(drop2.getItemId(), drop2.getMinAmount() + Utils.random(drop2.getExtraAmount())) : new Item(drop2.getItemId(), drop2.getMinAmount() + Utils.random(drop2.getExtraAmount()));
		FriendChatsManager friendsChat = player.getCurrentFriendChat();
		if (player.isToggleLootshare()) {
			List<Player> players = friendsChat.getPlayers();
			if (players == null || players.size() == 1) {
				sendDrop2(player, item, size);
			} else {
				CopyOnWriteArrayList<Player> playersWithLs = players.stream( ).filter( p -> p.isToggleLootshare() && withinDistance(p, 20) && getDamageReceived( p ) > 0 ).collect( Collectors.toCollection( CopyOnWriteArrayList::new ) );
				Player luckyPlayer = playersWithLs.get((int) (Math.random() * playersWithLs.size()));
				sendDrop2(luckyPlayer, item, size);
				luckyPlayer.getPackets().sendGameMessage("<col=00FF00>You received: " + item.getAmount() + " " + item.getName() + ".", true);
				for (Player p2 : players) {
					if (p2 == luckyPlayer  || !p2.isToggleLootshare())
						continue;
					p2.getPackets().sendGameMessage("<col=66FFCC>" + luckyPlayer.getDisplayName() + "</col> received: " + item.getAmount() + " " + item.getName() + ".", true);
					if (item.getDefinitions().getTipitPrice() >= 5) {
						int playeramount = playersWithLs.size();
						int dividedamount = (item.getDefinitions().getTipitPrice() / playeramount);
						p2.getInventory().addItemMoneyPouch(new Item(995, dividedamount));
						p2.getPackets().sendGameMessage(String.format("<col=115b0d>You received: %sx coins from a split from the %s.</col>", dividedamount, item.getName()), true);
					}
				}
			}
		} else {
			sendDrop2(player, item, size);
		}
	}

	public void sendDrop2(Player player, Item item, int size) {
		if (this instanceof CorruptedCreature) {
			if (player.getSophanemChest().add(item)) {
				return;
			}
		}
		boolean stackable = ItemDefinitions.getItemDefinitions(item.getId()).isStackable();
		final ItemDefinitions defs = item.getDefinitions();
		if (item.getId() == 995 && player.getMoneyPouch().getCoinsAmount() < Integer.MAX_VALUE) {
			player.getMoneyPouch().sendDynamicInteraction(item.getAmount(), false);
			return;
		}
		for (String itemName : Settings.RARE_DROPS) {
			String name = ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase();
			if (name.contains(itemName.toLowerCase()) && !name.contains("charm") && !name.contains("fragment")
					&& !name.contains("shard") && !name.contains("key") && !name.contains("brew")
					&& !name.contains("dart") && !name.contains("saradomin_fruit") && !name.contains("saradomin fruit")
					&& !name.contains("saradomin_wine") && !name.contains("saradomin wine")
					&& !name.contains("warpriest")) {
				if (Settings.DISCORD) {
					DiscordBot.sendRareDrop(player, item);
				}
				World.sendNews(player, player.getDisplayName() + " has received an " + ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase() + " drop!", 0);
				if (player.isToggleLootBeams())
					sendLootBeam(player, this, defs);
			}
		}
		if (Settings.DOUBLE_DROPS)
			item.setAmount(item.getAmount() * 2);
		else {
			if (!stackable && item.getAmount() > 1) {
				for (int i = 0; i < item.getAmount(); i++) {
					World.addGroundItem(item, new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player, true, 60);
				}
			} else {
				World.addGroundItem(item, new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player, true, 60);
			}
			if (player.isToggleLootBeams())
				sendLootBeam(player, this, defs);
		}
		player.getDropLogs().addDrop(item);
		LogSystem.npcDropLog(player, item.getId(), item.getAmount(), item.getName(), this.getName(), this.getId());
		return;
	}

	@Override
	public int getSize() {
		return getDefinitions().size;
	}

	public static int getLootBeamGFXMultiplier(ItemDefinitions drop) {
		if (drop.getValue() >= 50000 && drop.getValue() <= 999999)
			return 1;
		else if (drop.getValue() >= 1000000 && drop.getValue() <= 9999999)
			return 2;
		else if (drop.getValue() >= 10000000)
			return 3;
		return 0;
	}

	public static void sendLootBeam(Player player, NPC npc, ItemDefinitions drop) {
		if (drop.getValue() < player.getLootBeamValue())
			return;
		sendLootBeam(player, new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getPlane()), getLootBeamGFXMultiplier(drop));
	}

	public static void sendLootBeam(Player player, WorldTile tile, int lootbeamIndex) {
		long messageTime = Utils.currentTimeMillis() - player.lastLootbeamMessage;
		if (messageTime < (5 * 1000)) {
			
		} else {
			player.getPackets().sendGameMessage("<col=ff8c38>A golden beam shines over one of your items.");
			player.lastLootbeamMessage = Utils.currentTimeMillis();
		}
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.getPackets().sendGraphics(new Graphics(4422), new WorldTile(tile));
				} else if (loop == 20)
					player.getPackets().sendGraphics(new Graphics(-1), new WorldTile(tile));
				else if (loop == 21)
					stop();
				loop++;
			}
		}, 0, 1);
	}

	public int getMaxHit(int style) {
		int maxHit = bonuses[0];
		if (style == 1)
			maxHit = bonuses[1];
		else if (style == 2)
			maxHit = bonuses[2];
		return maxHit / 10;
	}

	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}

	public int getAttackSpeed() {
		Map<Integer, Object> data = getDefinitions().clientScriptData;
		if (data != null) {
			Integer speed = (Integer) data.get(14);
			if (speed != null)
				return speed;
		}
		return 4;
	}

	public int[] getBonuses() {
		return bonuses;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0;
	}

	public WorldTile getRespawnTile() {
		return respawnTile;
	}

	public boolean isUnderCombat() {
		return combat.underCombat();
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget() && !(combat.getTarget() instanceof Familiar))
			lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}

	public boolean isForceWalking() {
		return forceWalk != null;
	}

	public void setTarget(Entity entity) {
		if (isForceWalking()) // if force walk not gonna get target
			return;
		combat.setTarget(entity);
		lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public void removeTarget() {
		if (combat.getTarget() == null)
			return;
		combat.removeTarget();
	}

	public void forceWalkRespawnTile() {
		setForceWalk(respawnTile);
	}

	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	public boolean hasForceWalk() {
		return forceWalk != null;
	}

	private ArrayList<Entity> getNpcPossibleTargets(ArrayList<String> targets) {
		ArrayList<Entity> possibleTarget = new ArrayList<>();
		for (int regionId : getMapRegionsIds()) {
			final List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null || npc.isDead() || npc.hasFinished()
							|| !targets.contains(npc.getName().toLowerCase())
							|| !npc.withinDistance(this,
									(getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL
													? 64
													: 8))
							|| ((!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this
									&& (npc.getAttackedByDelay() > System.currentTimeMillis()
											|| npc.getFindTargetDelay() > System.currentTimeMillis()))
							|| !clipedProjectile(npc, false))
						continue;
					possibleTarget.add(npc);
				}
			}
		}
		return possibleTarget;
	}

	public ArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
		int size = getSize();
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			if (checkPlayers) {
				List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
				if (playerIndexes != null) {
					for (int playerIndex : playerIndexes) {
						Player player = World.getPlayers().get(playerIndex);
						if (player == null || player.isDead() || player.hasFinished() || !player.isRunning()
								|| player.getAppearence().isHidden()
								|| !Utils.isOnRange(getX(), getY(), size, player.getX(), player.getY(),
										player.getSize(), forceTargetDistance > 0 ? forceTargetDistance : 1)
								|| !forceMultiAttacked && (!isAtMultiArea() || !player.isAtMultiArea())
										&& player.getAttackedBy() != this
										&& (player.getAttackedByDelay() > Utils.currentTimeMillis()
												|| player.getFindTargetDelay() > Utils.currentTimeMillis())
								|| !clipedProjectile(player, false) || !forceAgressive && !Wilderness.isAtWild(this)
										&& player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2) {
							continue;
						}
						possibleTarget.add(player);
					}
				}
			}
			if (checkNPCs) {
				List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
				if (npcsIndexes != null) {
					for (int npcIndex : npcsIndexes) {
						NPC npc = World.getNPCs().get(npcIndex);
						if (npc == null || npc == this || npc.isDead() || npc.hasFinished()
								|| !Utils.isOnRange(getX(), getY(), size, npc.getX(), npc.getY(), npc.getSize(),
										forceTargetDistance > 0 ? forceTargetDistance : 1)
								|| !npc.getDefinitions().hasAttackOption()
								|| (!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this
										&& npc.getAttackedByDelay() > Utils.currentTimeMillis()
								|| !clipedProjectile(npc, false)) {
							continue;
						}
						possibleTarget.add(npc);
					}
				}
			}
		}
		return possibleTarget;
	}

	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playersIndexes != null) {
				for (int playerIndex : playersIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || player.isDead() || player.hasFinished() || !player.isRunning()
							|| !player.withinDistance(this, forceTargetDistance > 0 ? forceTargetDistance
									: (getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL
													? 64
													: 8))
							|| (!forceMultiAttacked && (!isAtMultiArea() || !player.isAtMultiArea())
									&& player.getAttackedBy() != this
									&& (player.getAttackedByDelay() > Utils.currentTimeMillis()
											|| player.getFindTargetDelay() > Utils.currentTimeMillis()))
							|| !clipedProjectile(player, false) || !forceAgressive && !Wilderness.isAtWild(this)
									&& player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2)
						continue;
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}

	private final static int NON_AGGRESSIVE = 0;
	private final static int NO_TARGET = 1;
	private final static int HAS_TARGET = 2;

	private int setNpcvnpc() {
		ArrayList<String> targets = NPCvsNPC.getSingleton().getTargetNames(getName());
		if (targets != null && !targets.isEmpty()) {
			ArrayList<Entity> possibleTarget = getNpcPossibleTargets(targets);
			if (!possibleTarget.isEmpty()) {
				Entity target = possibleTarget.get(Utils.getRandom(possibleTarget.size() - 1));
				setTarget(target);
				target.setAttackedBy(this);
				target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
				return HAS_TARGET;
			}
			return NO_TARGET;
		}
		return NON_AGGRESSIVE;
	}

	public boolean checkAgressivity() {
		switch (setNpcvnpc()) {
		case NON_AGGRESSIVE:
			break;
		case NO_TARGET:
			break;
		case HAS_TARGET:
			String text = NPCvsNPC.getSingleton().forceTalk(getId());
			if (!text.equals(""))
				setNextForceTalk(new ForceTalk(text));
			return true;
		}
		if (!forceAgressive) {
			NPCCombatDefinitions defs = getCombatDefinitions();
			if (defs.isAgressive() == false) {
				return false;
			}
		}
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!forceAgressive) {
			if (!getDefinitions().hasOption("attack"))
				return false;
			if (!getCombatDefinitions().isAgressive()) {
				if (!possibleTarget.isEmpty()) {
					for (Entity target : possibleTarget) {
						if (target == null || target.isDead() || target.hasFinished())
							continue;
						if (target instanceof Player) {
							Player player = (Player) target;
							continue;
						}
					}
				}
			}
		}
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Utils.random(possibleTarget.size()));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public boolean isCantInteract() {
		return cantInteract;
	}

	/**
	 * Head Icons.
	 */
	public HeadIcon[] getIcons() {
		return new HeadIcon[0];
	}

	public void requestIconRefresh() {
		refreshHeadIcon = true;
	}

	public boolean isRefreshHeadIcon() {
		return refreshHeadIcon;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract)
			combat.reset();
	}

	public int getCapDamage() {
		return capDamage;
	}

	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}

	public int getLureDelay() {
		return lureDelay;
	}

	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}

	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}

	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		this.cantFollowUnderCombat = canFollowUnderCombat;
	}

	public Transformation getNextTransformation() {
		return nextTransformation;
	}

	@Override
	public String toString() {
		return getDefinitions().name + " - " + id + " - " + getX() + " " + getY() + " " + getPlane();
	}

	public boolean isForceAgressive() {
		return forceAgressive;
	}

	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}

	public boolean isInfected() {
		return isInfected;
	}

	public void setInfected(boolean isInfected) {
		setName(getName() + " (Infected)");
		this.isInfected = isInfected;
	}

	public int getForceTargetDistance() {
		return forceTargetDistance;
	}

	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}

	public boolean isForceFollowClose() {
		return forceFollowClose;
	}

	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}

	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}

	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}

	public boolean hasRandomWalk() {
		return randomwalk;
	}

	public void setRandomWalk(boolean forceRandomWalk) {
		this.randomwalk = forceRandomWalk;
	}

	public String getCustomName() {
		return name;
	}

	public void setName(String string) {
		this.name = getDefinitions().name.equals(string) ? null : string;
		changedName = true;
	}

	public int getCustomCombatLevel() {
		return combatLevel;
	}

	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
	}

	public String getName() {
		return name != null ? name : getDefinitions().name;
	}

	public void setCombatLevel(int level) {
		combatLevel = getDefinitions().combatLevel == level ? -1 : level;
		changedCombatLevel = true;
	}

	public boolean hasChangedName() {
		return changedName;
	}

	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}

	public WorldTile getMiddleWorldTile() {
		int size = getSize();
		return new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane());
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public void setSecondBar(SecondBar secondBar) {
		this.secondBar = secondBar;
	}

	public boolean isNoDistanceCheck() {
		return noDistanceCheck;
	}

	public void setNoDistanceCheck(boolean noDistanceCheck) {
		this.noDistanceCheck = noDistanceCheck;
	}

	public boolean withinDistance(Player tile, int distance) {
		return super.withinDistance(tile, distance);
	}

	/**
	 * Gets the locked.
	 * 
	 * @return The locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the locked.
	 * 
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isClueScrollNPC(String npcName) {
		switch (npcName) {
		case "'Rum'-pumped crab":
		case "Aberrant spectre":
		case "Abyssal demon":
		case "Abyssal leech":
		case "Air elemental":
		case "Ancient mage":
		case "Ancient ranger":
		case "Ankou":
		case "Armoured zombie":
		case "Arrg":
		case "Aviansie":
		case "Bandit":
		case "Banshee":
		case "Barbarian":
		case "Barbarian woman":
		case "Basilisk":
		case "Black Guard":
		case "Black Guard Berserker":
		case "Black Guard crossbowdwarf":
		case "Black Heather":
		case "Black Knight":
		case "Black Knight Titan":
		case "Black demon":
		case "Black dragon":
		case "Blood reaver":
		case "Bloodveld":
		case "Blue dragon":
		case "Bork":
		case "Brine rat":
		case "Bronze dragon":
		case "Brutal green dragon":
		case "Catablepon":
		case "Cave bug":
		case "Cave crawler":
		case "Cave horror":
		case "Cave slime":
		case "Chaos Elemental":
		case "Chaos druid":
		case "Chaos druid warrior":
		case "Chaos dwarf":
		case "Chaos dwarf hand cannoneer":
		case "Chaos dwogre":
		case "Cockatrice":
		case "Cockroach drone":
		case "Cockroach soldier":
		case "Cockroach worker":
		case "Columbarium":
		case "Columbarium key":
		case "Commander Zilyana":
		case "Corporeal Beast":
		case "Crawling Hand":
		case "Cyclops":
		case "Cyclossus":
		case "Dagannoth":
		case "Dagannoth Prime":
		case "Dagannoth Rex":
		case "Dagannoth Supreme":
		case "Dagannoth guardian":
		case "Dagannoth spawn":
		case "Dark beast":
		case "Desert Lizard":
		case "Desert strykewyrm":
		case "Dried zombie":
		case "Dust devil":
		case "Dwarf":
		case "Earth elemental":
		case "Earth warrior":
		case "Elf warrior":
		case "Elite Black Knight":
		case "Elite Dark Ranger":
		case "Elite Khazard guard":
		case "Exiled Kalphite Queen":
		case "Exiled kalphite guardian":
		case "Exiled kalphite marauder":
		case "Ferocious barbarian spirit":
		case "Fire elemental":
		case "Fire giant":
		case "Flesh Crawler":
		case "Forgotten Archer":
		case "Forgotten Mage":
		case "Forgotten Warrior":
		case "Frog":
		case "Frost dragon":
		case "Ganodermic beast":
		case "Gargoyle":
		case "General Graardor":
		case "General malpractitioner":
		case "Ghast":
		case "Ghostly warrior":
		case "Giant Mole":
		case "Giant ant soldier":
		case "Giant ant worker":
		case "Giant rock crab":
		case "Giant skeleton":
		case "Giant wasp":
		case "Glacor":
		case "Glod":
		case "Gnoeals":
		case "Goblin statue":
		case "Gorak":
		case "Greater demon":
		case "Greater reborn mage":
		case "Greater reborn ranger":
		case "Greater reborn warrior":
		case "Green dragon":
		case "Grotworm":
		case "Haakon the Champion":
		case "Harold":
		case "Harpie Bug Swarm":
		case "Hill giant":
		case "Hobgoblin":
		case "Ice giant":
		case "Ice strykewyrm":
		case "Ice troll":
		case "Ice troll female":
		case "Ice troll male":
		case "Ice troll runt":
		case "Ice warrior":
		case "Icefiend":
		case "Iron dragon":
		case "Jelly":
		case "Jogre":
		case "Jungle horror":
		case "Jungle strykewyrm":
		case "K'ril Tsutsaroth":
		case "Kalphite Guardian":
		case "Kalphite King":
		case "Kalphite Queen":
		case "Kalphite Soldier":
		case "Kalphite Worker":
		case "Killerwatt":
		case "King Black Dragon":
		case "Kraka":
		case "Kree'arra":
		case "Kurask":
		case "Lanzig":
		case "Lesser demon":
		case "Lesser reborn mage":
		case "Lesser reborn ranger":
		case "Lesser reborn warrior":
		case "Lizard":
		case "Locust lancer":
		case "Locust ranger":
		case "Locust rider":
		case "Mature grotworm":
		case "Mighty banshee":
		case "Minotaur":
		case "Mithril dragon":
		case "Molanisk":
		case "Moss giant":
		case "Mountain troll":
		case "Mummy":
		case "Mutated bloodveld":
		case "Mutated jadinko male":
		case "Mutated zygomite":
		case "Nechryael":
		case "Nex":
		case "Ogre":
		case "Ogre statue":
		case "Ork statue":
		case "Otherworldly being":
		case "Ourg statue":
		case "Paladin":
		case "Pee Hat":
		case "Pirate":
		case "Pyrefiend":
		case "Queen Black Dragon":
		case "Red dragon":
		case "Rock lobster":
		case "Rockslug":
		case "Salarin the Twisted":
		case "Scabaras lancer":
		case "Scarab mage":
		case "Sea Snake Hatchling":
		case "Shadow warrior":
		case "Skeletal Wyvern":
		case "Skeletal miner":
		case "Skeleton":
		case "Skeleton fremennik":
		case "Skeleton thug":
		case "Skeleton warlord":
		case "Small Lizard":
		case "Soldier":
		case "Sorebones":
		case "Speedy Keith":
		case "Spiritual mage":
		case "Spiritual warrior":
		case "Steel dragon":
		case "Stick":
		case "Suqah":
		case "Terror dog":
		case "Thrower Troll":
		case "Thug":
		case "Tortured soul":
		case "Trade floor guard":
		case "Tribesman":
		case "Troll general":
		case "Troll spectator":
		case "Tstanon Karlak":
		case "Turoth":
		case "Tyras guard":
		case "TzHaar-Hur":
		case "TzHaar-Ket":
		case "TzHaar-Mej":
		case "TzHaar-Xil":
		case "Undead troll":
		case "Vampyre":
		case "Vyre corpse":
		case "Vyrelady":
		case "Vyrelord":
		case "Vyrewatch":
		case "Wallasalki":
		case "Warped terrorbird":
		case "Warped tortoise":
		case "Warrior":
		case "Water elemental":
		case "Waterfiend":
		case "Werewolf":
		case "White Knight":
		case "WildyWyrm":
		case "Yeti":
		case "Yuri":
		case "Zakl'n Gritch":
		case "Zombie":
		case "Zombie hand":
		case "Zombie swab":
			return true;
		}
		return false;
	}

	protected boolean isNoClipWalking() {
		return noClipWalking;
	}

	public void setNoClipWalking(boolean noClipWalking) {
		this.noClipWalking = noClipWalking;
	}

	public void removeClipping() {
		if (lastTile != null)
			RegionBuilder.entityClip(toString(), lastTile[0], lastTile[1], lastTile[2], lastTile[3],
					(this instanceof Familiar || this instanceof Pet) ? 1 : 0, false);
	}

	public boolean isFamiliar() {
		return this instanceof Familiar;
	}

	public Item sendDrop(Player player, Drop drop) {
		boolean stackable = ItemDefinitions.getItemDefinitions(drop.getItemId()).isStackable();
		Item item = stackable ? new Item(drop.getItemId(), drop.getMinAmount() + Utils.random(drop.getExtraAmount()))
				: new Item(drop.getItemId(), drop.getMinAmount() + Utils.random(drop.getExtraAmount()));
		final ItemDefinitions defs = item.getDefinitions();
		Player killer = getMostDamageReceivedSourcePlayer();
		for (String itemName : Settings.RARE_DROPS) {
			String name = ItemDefinitions.getItemDefinitions(drop.getItemId()).getName().toLowerCase();
			if (name.contains(itemName.toLowerCase()) && !name.contains("charm") && !name.contains("fragment")
					&& !name.contains("shard") && !name.contains("key") && !name.contains("brew")
					&& !name.contains("dart") && !name.contains("saradomin_fruit") && !name.contains("saradomin fruit")
					&& !name.contains("saradomin_wine") && !name.contains("saradomin wine")
					&& !name.contains("warpriest")) {
				if (!Settings.TEST && Settings.DISCORD)
					new MessageBuilder().append(player.getDisplayName() + " has received "
							+ ItemDefinitions.getItemDefinitions(drop.getItemId()).getName().toLowerCase() + " drop!")
							.send(GameServer.getDiscordBot().getAPI().getTextChannelById("441730708235485184").get());

				World.sendNews(player, player.getDisplayName() + " has received "
						+ ItemDefinitions.getItemDefinitions(drop.getItemId()).getName().toLowerCase() + " drop!", 0);
			}
		}
		int size = getSize();
		if (GlobalEvents.isActiveEvent(Event.DOUBLE_DROPS))
			item.setAmount(item.getAmount() * 2);
		else {
			if (!stackable && item.getAmount() > 1) {
				for (int i = 0; i < item.getAmount(); i++) {
					World.addGroundItem(new Item(item.getId(), 1),
							new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player, true, 60);
				}
			} else {
				World.addGroundItem(item, new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player,
						true, 60);
			}
		}
		return item;
	}

	public boolean switchTarget(boolean force) {
		if (force || (Utils.random(3) == 0 && (lastAttackedByTarget + 15000) >= Utils.currentTimeMillis())) {
			if (getPossibleTargets().size() != 0) {
				Random random = new Random();
				ArrayList<Entity> targets = getPossibleTargets();
				Entity target = targets.get(random.nextInt(targets.size()));
				setTarget(target);
				return true;
			}
		}
		return false;
	}
}
