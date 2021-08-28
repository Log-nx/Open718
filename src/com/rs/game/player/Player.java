package com.rs.game.player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.functors.InstanceofPredicate;

import com.everythingrs.hiscores.Hiscores;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.EffectsManager.Effect;
import com.rs.game.EffectsManager.EffectType;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.instances.InstanceSettings;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.clanwars.WarControler;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelRules;
import com.rs.game.minigames.hunger.HungerGamesControler;
import com.rs.game.npc.NPC;
import com.rs.game.npc.araxxor.MirrorBackSpider;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.FamiliarManager;
import com.rs.game.npc.godwars.zaros.Nex;
import com.rs.game.npc.others.GraveStone;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.QuickChatMessage;
import com.rs.game.player.VarsManager;
import com.rs.game.player.actions.assassin.Assassins;
import com.rs.game.player.actions.combat.CombatDefinitions;
import com.rs.game.player.actions.combat.PlayerCombat;
import com.rs.game.player.actions.construction.House;
import com.rs.game.player.actions.divination.DivineObject;
import com.rs.game.player.actions.farming.FarmingManager;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.actions.mining.orebox.OreBox;
import com.rs.game.player.actions.prayer.Prayer;
import com.rs.game.player.actions.slayer.SlayerManager;
import com.rs.game.player.actions.slayer.SlayerTask;
import com.rs.game.player.appearance.Appearence;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.appearance.LocalNPCUpdate;
import com.rs.game.player.appearance.LocalPlayerUpdate;
import com.rs.game.player.appearance.LogicPacket;
import com.rs.game.player.appearance.Titles;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.MoneyPouch;
import com.rs.game.player.content.Notes;
import com.rs.game.player.content.PlayerDesign;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.SquealOfFortune;
import com.rs.game.player.content.Wildstalker;
import com.rs.game.player.content.activities.DailyChallenges;
import com.rs.game.player.content.activities.DominionTower;
import com.rs.game.player.content.activities.PlayerOwnedPort;
import com.rs.game.player.content.activities.reaper.Reaper;
import com.rs.game.player.content.activities.skillingtask.SkillerTasks;
import com.rs.game.player.content.clans.ChatMessage;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.contracts.Contract;
import com.rs.game.player.content.contracts.ContractHandler;
import com.rs.game.player.content.custom.CosmeticManager;
import com.rs.game.player.content.custom.Options;
import com.rs.game.player.content.custom.Statistics;
import com.rs.game.player.content.dungeoneering.Dungeon;
import com.rs.game.player.content.dungeons.MetalBank;
import com.rs.game.player.content.dungeons.SophanemChest;
import com.rs.game.player.content.grandexchange.GrandExchangeManager;
import com.rs.game.player.content.input.InputEvent;
import com.rs.game.player.content.input.impl.InputIntegerEvent;
import com.rs.game.player.content.input.impl.InputLongStringEvent;
import com.rs.game.player.content.input.impl.InputNameEvent;
import com.rs.game.player.content.interfaces.LootBag;
import com.rs.game.player.content.interfaces.NewsDashboard;
import com.rs.game.player.content.items.Potions;
import com.rs.game.player.content.perks.GamePointRewards;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.player.content.pet.Pets;
import com.rs.game.player.content.slayer.CooperativeSlayer;
import com.rs.game.player.content.treasuretrails.PuzzleBox;
import com.rs.game.player.controllers.BarrelchestControler;
import com.rs.game.player.controllers.CorpBeastControler;
import com.rs.game.player.controllers.CrucibleControler;
import com.rs.game.player.controllers.DTControler;
import com.rs.game.player.controllers.FightCaves;
import com.rs.game.player.controllers.FightKiln;
import com.rs.game.player.controllers.GodWars;
import com.rs.game.player.controllers.NomadsRequiem;
import com.rs.game.player.controllers.QueenBlackDragonController;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.player.controllers.ZGDControler;
import com.rs.game.player.controllers.castlewars.CastleWarsPlaying;
import com.rs.game.player.controllers.castlewars.CastleWarsWaiting;
import com.rs.game.player.controllers.fightpits.FightPitsArena;
import com.rs.game.player.controllers.minigames.SophanemSlayerDungeon;
import com.rs.game.player.controllers.pestcontrol.PestControlGame;
import com.rs.game.player.controllers.pestcontrol.PestControlLobby;
import com.rs.game.player.managers.AchievementManager;
import com.rs.game.player.managers.ActionManager;
import com.rs.game.player.managers.AuraManager;
import com.rs.game.player.managers.ChargesManager;
import com.rs.game.player.managers.CompletionistCapeManager;
import com.rs.game.player.managers.ControllerManager;
import com.rs.game.player.managers.CutscenesManager;
import com.rs.game.player.managers.DialogueManager;
import com.rs.game.player.managers.DonationManager;
import com.rs.game.player.managers.EmotesManager;
import com.rs.game.player.managers.GamePointManager;
import com.rs.game.player.managers.HintIconsManager;
import com.rs.game.player.managers.InterfaceManager;
import com.rs.game.player.managers.LendingManager;
import com.rs.game.player.managers.MusicsManager;
import com.rs.game.player.managers.OwnedObjectManager;
import com.rs.game.player.managers.PlayerPerkManager;
import com.rs.game.player.managers.PriceCheckManager;
import com.rs.game.player.managers.QuestManager;
import com.rs.game.player.managers.TreasureTrails;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.Session;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.utils.Color;
import com.rs.utils.IsaacKeyPair;
import com.rs.utils.Lend;
import com.rs.utils.LogSystem;
import com.rs.utils.Logger;
import com.rs.utils.MachineInformation;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

public class Player extends Entity {

	public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1, RUN_MOVE_TYPE = 2;

	private static final long serialVersionUID = 2011932556974180375L;

	public int moneyPouchTrade;
	public boolean addedFromPouch;
	public long lastImpling;
	public int assassinPoints;
	private SophanemChest sophanemChest;
	private MetalBank metalBank;
	private OreBox oreBox;
	public boolean callAssassin;
	public boolean finalblow;
	public boolean finalBlowCoolDown;
	public boolean swiftness;
	public boolean swiftspeed;
	public boolean stealth;
	public boolean stealthMovesCoolDown;
	public int chest;
	public int cashcasket;
	public int rosTrips;
	private LootBag lootBag;
	public boolean firstReaperTask = true;
	public long lastReaperTask;
	public int reaperTask = 0;
	private Reaper reaperTasks;
	public int reaperPoints;
	private boolean groupAssignments, largerTasks;
	private int rerollCount;
	public int xpRate = 0;
	private int gStone;

	/**
	 * Server Message
	 */
	public void WorldMessageHandler() {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				int r3 = 0;
				r3 = Utils.random(10);
				if (r3 == 0) {
					getPackets().sendGameMessage(
							"<img=5><col=b7b82b>Information: Did you know that can have up to 500m in a skill?");
				} else if (r3 == 1) {
					getPackets().sendGameMessage(
							"<img=5><col=b7b82b>News: By donating you will get useful items & benefits, type ::donate!");
				} else if (r3 == 3) {
					getPackets().sendGameMessage(
							"<img=5><col=b7b82b>News: Feel free to ask any staff for help, as long as they're not busy!");
				} else if (r3 == 4) {
					getPackets().sendGameMessage(
							"<img=5><col=b7b82b>News: All are welcome to make suggestions as long as they are posted on the forums!");
				} else if (r3 == 5) {
					getPackets().sendGameMessage(
							"<img=5><col=b7b82b>News: Sign up on our forum and make sure you keep an eye on our updates!");
				} else if (r3 == 5) {
					getPackets().sendGameMessage(
							"<img=5><col=b7b82b>News: Don't forget that you can get daily rewards by voting for us, type ::vote!");
				}
			}
		}, 0, 500);
	}

	private boolean offhand = false;
	public boolean Ironman;

	public void setIronman(boolean Ironman) {
		this.Ironman = Ironman;
	}

	public boolean HardcoreIronman;

	public void setHardcoreI6ronman(boolean HardcoreIronman) {
		this.HardcoreIronman = HardcoreIronman;
	}

	public boolean isIronman() {
		return Ironman;
	}

	public boolean isHardcoreIronman() {
		return HardcoreIronman;
	}

	private int money;

	public void refreshMoneyPouch() {
		getPackets().sendRunScript(5560, money);
	}

	public double getMoneyInPouch() {
		return moneyPouch.getCoinsAmount();
	}

	public void setMoneyInPouch(int totalCash) {
		moneyPouch.setAmount(totalCash, false);
	}

	public void addMoneyToPouch(int amount) {
		moneyPouch.sendDynamicInteraction(amount, false);
	}

	public void removeMoneyFromPouch(int amount) {
		moneyPouch.sendDynamicInteraction(amount, true);
	}

	public MoneyPouch getMoneyPouch() {
		return moneyPouch;
	}

	public boolean isDestroytimer() {
		return isdestroytimer;
	}

	public void setisDestroytimer(boolean isdestroytimer) {
		this.isdestroytimer = isdestroytimer;
	}

	public int getInstanceBooth() {
		return instanceBooth;
	}

	public void setInstanceBooth(int instanceBooth) {
		this.instanceBooth = instanceBooth;
	}

	private transient boolean cantWalk;

	public boolean isCantWalk() {
		return cantWalk;
	}

	public void setCantWalk(boolean cantWalk) {
		this.cantWalk = cantWalk;
	}

	/**
	 * Drop Logs
	 */
	private DropLog dropLogs;

	public DropLog getDropLogs() {
		return dropLogs;
	}

	/*
	 * Saradomin Blessing
	 */
	public void addSaradominBlessing(long time) {
		saradominBlessing = time + Utils.currentTimeMillis();
	}

	public long getSaradominBlessing() {
		return saradominBlessing;
	}

	/*
	 * Juju Fishing
	 */

	public void addJujuFishing(long time) {
		jujuFishing = time + Utils.currentTimeMillis();
	}

	public long getJujuFishing() {
		return jujuFishing;
	}

	public int getPrestigeLevel() {
		return prestigeLevel;
	}
	/**
	 * Prestige System
	 */
	public int agilityPrestige, attackPrestige, defencePrestige, herblorePrestige, cookingPrestige, thievingPrestige,
			woodcuttingPrestige, firemakingPrestige, prayerPrestige, rangedPrestige, strengthPrestige, magicPrestige,
			runecraftingPrestige, slayerPrestige, hunterPrestige, craftingPrestige, fletchingPrestige, smithingPrestige,
			miningPrestige, farmingPrestige, summoningPrestige, constructionPrestige, dungeoneeringPrestige,
			fishingPrestige;

	public void prestigeShops() {
		if (prestigeLevel == 0) {
			getPackets().sendGameMessage("You need to have prestiged once to gain access to this store.");
		} else if (prestigeLevel == 1) {
			ShopsHandler.openShop(this, 21);
		} else if (prestigeLevel == 2) {
			ShopsHandler.openShop(this, 21);
		} else if (prestigeLevel == 3) {
			ShopsHandler.openShop(this, 21);
		} else if (prestigeLevel == 4) {
			ShopsHandler.openShop(this, 21);
		} else if (prestigeLevel == 5) {
			ShopsHandler.openShop(this, 21);
		}
	}

	private transient PuzzleBox puzzleBox;

	public PuzzleBox getPuzzleBox() {
		return puzzleBox;
	}

	public void setPuzzleBox(int puzzleId) {
		this.puzzleBox = new PuzzleBox(this, puzzleId);
	}

	/**
	 * Pvm
	 */
	private int pvmPoints;
	private int instancepin;
	private String instanceControler = "";

	public int getPvmPoints() {
		return pvmPoints;
	}

	public void setPvmPoints(int pvmPoints) {
		this.pvmPoints = pvmPoints;
	}

	/**
	 * Varbit manager.
	 */
	public transient VarBitManager VBM;

	public VarBitManager getVarBitManager() {
		return VBM;
	}

	/**
	 * Farming Methods
	 */

	/**
	 * Farming.
	 */
	public FarmingManager farmingManager;

	public FarmingManager getFarmingManager() {
		return farmingManager;
	}

	/**
	 * Lottery System
	 */
	private Item prize;

	public Item getPrize() {
		return prize;
	}

	public void setPrize(Item price) {
		this.prize = price;
	}

	public boolean isBurying = false;
	public boolean pure = false;
	public boolean completedTutorial = false;
	public boolean hasntCookedShrimpYet = true;
	public boolean hasntMadeCharacter = true;
	public boolean hasMinedTin = false;
	public boolean hasMinedCopper = false;

	public void setServerTutorial(boolean completedTutorial) {
		this.completedTutorial = completedTutorial;
	}

	public boolean WearGreeGree = false;
	public boolean transformed;

	public boolean isTransformed() {
		return transformed;
	}

	public void setTransformed(boolean transformed) {
		this.transformed = transformed;
	}

	// Kuradal
	private boolean talkedWithKuradal;

	public boolean isTalkedWithKuradal() {
		return talkedWithKuradal;
	}

	public void setTalkedWithKuradal(boolean talkedWithKuradal) {
		this.talkedWithKuradal = talkedWithKuradal;
	}
	// Imp Catcher Quest

	public boolean startedImpCatcher = false;
	public boolean inProgressImpCatcher = false;
	public boolean completedImpCatcher = false;

	// Doric's Quest
	public boolean startedDoricsQuest = false;
	public boolean inProgressDoricsQuest = false;
	public boolean completedDoricsQuest = false;

	/**
	 * Dungeoneering - NEW SYSTEM
	 */
	public int toks = 0;
	public int dungtime = 0;
	public int dungdeaths = 0;
	public boolean isInDungLobby = false;
	private transient Dungeon dungeon;

	public boolean hasPaidDeath;
	public boolean talkedToJade = false;

	public int prestigeLevel, prestigePoints;

	public boolean hasFamiliar() {
		return this.familiar != null;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public boolean Clothes = false;

	public boolean inInstance() {
		if (getControllerManager().toString() == null)
			return false;
		if (getControllerManager().toString().toLowerCase().contains("instance"))
			return true;
		return false;
	}

	// Clans
	public void kickPlayerFromClanChannel(String name) {
		if (clansManager == null)
			return;
		clansManager.kickPlayerFromChat(this, name);
	}

	public void sendClanChannelMessage(ChatMessage message) {
		if (clansManager == null)
			return;
		clansManager.sendMessage(this, message);
	}

	public void sendGuestClanChannelMessage(ChatMessage message) {
		if (guestClansManager == null)
			return;
		guestClansManager.sendMessage(this, message);
	}

	public void sendClanChannelQuickMessage(QuickChatMessage message) {
		if (clansManager == null)
			return;
		clansManager.sendQuickMessage(this, message);
	}

	public void sendGuestClanChannelQuickMessage(QuickChatMessage message) {
		if (guestClansManager == null)
			return;
		guestClansManager.sendQuickMessage(this, message);
	}

	public boolean inClanChat;
	public int getClanSize = 0;

	// Items On Death
	public boolean clickedButton;

	// Stars
	public boolean canTalkToSprite, taggedStar;

	// Shops
	public boolean isBuying;

	// Lodestones
	public boolean activatedDraynor, activatedFalador = false;

	// Lending
	public boolean isLendingItem = false;

	// Cooks Assistant Quest
	public boolean startedCooksAssistant = false;
	public boolean inProgressCooksAssistant = false;
	public boolean completedCooksAssistantQuest = false;
	public boolean hasGrainInHopper = false;
	public int questPoints = 0;

	// transient stuff
	private MoneyPouch moneyPouch;
	private transient String username;
	private transient Session session;
	private transient boolean clientLoadedMapRegion;
	protected transient int displayMode;
	protected transient int screenWidth;
	protected transient int screenHeight;
	private static final int lastlogged = 0;
	public int cluenoreward;
	private transient FadingScreen fadingScreen;
	private transient InterfaceManager interfaceManager;
	public boolean KarateFletching;
	private transient DialogueManager dialogueManager;
	protected transient HintIconsManager hintIconsManager;
	private transient ActionManager actionManager;
	protected transient CutscenesManager cutscenesManager;
	private transient PriceCheckManager priceCheckManager;
	private transient CoordsEvent coordsEvent;
	private transient FriendChatsManager currentFriendChat;
	private transient Trade trade;
	private transient DuelRules lastDuelRules;
	private transient IsaacKeyPair isaacKeyPair;
	private WorldTile getoutside;
	protected transient Pet pet;

	// used for packets logic
	private transient ConcurrentLinkedQueue<LogicPacket> logicPackets;
	public boolean toogleLootShare;

	// used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	private transient LocalNPCUpdate localNPCUpdate;

	private int temporaryMovementType;
	private boolean updateMovementType;

	// player stages
	protected transient boolean started;
	protected transient boolean running;

	private transient long packetsDecoderPing;
	protected transient boolean resting;
	private transient boolean canPvp;
	private transient boolean cantTrade;
	private transient long lockDelay; // used for doors and stuff like that
	protected transient long foodDelay;
	protected transient long potDelay;
	private transient long boneDelay;
	private transient Runnable closeInterfacesEvent;
	private transient long lastPublicMessage;
	private transient long polDelay;
	private transient List<Integer> switchItemCache;
	private transient boolean disableEquip;
	private transient MachineInformation machineInformation;
	private transient boolean spawnsMode;
	protected transient boolean castedVeng;
	private transient boolean invulnerable;
	private transient double hpBoostMultiplier;
	private transient boolean largeSceneView;

	// interface

	// saving stuff
	private GamePointManager gamePointManager;
	private DonationManager donationManager;
	private FamiliarManager familiarManager;
	public String password;
	private int rights;
	private int lootBeamValue = 1000;
	public boolean ferociousUpgrade;
	public Familiar familiar;
	private String displayName;
	private String discordName;
	private String realDiscordName;
	private Player leaderName;
	public long displayNameChange;
	private String lastIP;
	private long creationDate;
	protected Appearence appearence;
	private Inventory inventory;
	private Equipment equipment;
	protected Skills skills;
	protected CombatDefinitions combatDefinitions;
	public Prayer prayer;
	private PlayerPerkManager perkManager;
	private AchievementManager achievementManager;
	private Bank bank;
	protected ControllerManager controllerManager;
	private MusicsManager musicsManager;
	private EmotesManager emotesManager;
	public TreasureTrails treasureTrails;
	private FriendsIgnores friendsIgnores;
	private DominionTower dominionTower;
	private CompletionistCapeManager compCapeManager;
	private AuraManager auraManager;
	private QuestManager questManager;
	private PetManager petManager;
	private byte runEnergy;
	private boolean allowChatEffects;
	private boolean mouseButtons;
	private int privateChatSetup;
	private int friendChatSetup;
	protected int skullDelay;
	private int skullId;
	private boolean forceNextMapLoadRefresh;
	protected long poisonImmune;
	protected long fireImmune;
	private long superAntiFire;
	private long saradominBlessing;
	private long jujuFishing;
	private boolean killedQueenBlackDragon;
	private int runeSpanPoints;

	private int lastBonfire;
	public int lendMessage;
	private int[] pouches;
	private long displayTime;
	private long muted;
	private long jailed;
	private long banned;
	private boolean permBanned;
	private boolean filterGame;
	private boolean xpLocked;
	private boolean yellOff;
	private boolean worldMessageOff;

	// game bar status
	private int publicStatus;
	private int clanStatus;
	private int tradeStatus;
	private boolean reportOption;
	private int assistStatus;

	private boolean donator;
	public transient ArrayList<Player> joined;
	private boolean outside;
	private boolean extremeDonator;
	private long donatorTill;
	private long extremeDonatorTill;

	// Recovery ques. & ans.
	private String recovQuestion;
	private String recovAnswer;

	private String lastMsg;

	// Used for storing recent ips and password
	private ArrayList<String> passwordList = new ArrayList<String>();
	private ArrayList<String> ipList = new ArrayList<String>();

	// honor
	private int killCount, deathCount;
	private ChargesManager charges;
	// barrows
	private boolean[] killedBarrowBrothers;
	public int hiddenBrother;
	private int barrowsKillCount;

	// skill capes customizing
	private int[] maxedCapeCustomized;
	public int[] completionistCapeCustomized;

	/** Skilling pets */
	public List<Integer> collectedPets;

	// completionistcape reqs
	private boolean completedFightCaves;
	private boolean completedFightKiln;
	private boolean wonFightPits;

	// crucible
	private boolean talkedWithMarv;
	private int crucibleHighScore;

	private int overloadDelay;
	private int prayerRenewalDelay;

	public boolean godMode;

	private String currentFriendChatOwner;
	private int summoningLeftClickOption;
	private List<String> ownedObjectsManagerKeys;

	// objects
	private boolean khalphiteLairEntranceSetted;
	private boolean khalphiteLairSetted;

	// supportteam
	private boolean isSupporter;

	// Toggle news
	public boolean ToggleNews;

	// FairyRing
	public int firstColumn = 1, secondColumn = 1, thirdColumn = 1;

	// Daily Challenges
	private transient DailyChallenges dailychallenges;

	public DailyChallenges getDailyChallenges() {
		return dailychallenges;
	}

	// voting
	private boolean oldItemsLook;

	private String yellColor = "ff0000";

	private long voted;

	private boolean isGraphicDesigner;
	public boolean isCommunityManager;
	private boolean isForumModerator;

	private int spawnrate;
	private int hacker;
	private boolean permatrio;
	private int instanceBooth;
	private boolean permabank;
	private boolean permabandos;
	private boolean permasaradomin;
	private boolean permazamorak;
	private boolean permaarmadyl;
	private boolean permablink;
	private boolean permaeradicator;
	private boolean permacorp;
	private boolean permakq;
	private boolean permadagking;
	private boolean permanex;
	private boolean permalegioprimus;
	private boolean permakbd;
	private boolean permagradum;
	private boolean permawyrm;
	private boolean permanecrolord;
	private boolean permaavatar;
	private boolean permafear;
	private boolean permastq;
	private boolean permageno;
	private boolean permarajj;
	private int timer;
	private int destroytimer;
	private boolean isdestroytimer;
	private boolean instanceend;
	public int penguins;
	public int sinkholes;
	public int totalTreeDamage;
	public int barrowsLoot;
	public int domCount;
	public int castleWins;
	public int trollWins;
	public int summer;
	public int implingCount;
	public int pestWins;
	public int voteCount;
	public int spinsCount;
	public int houseMoney;
	public boolean killedQueenBlackDragon2;
	public int heroSteals;
	public int cutDiamonds;
	public int kuradalTasks;
	public int grenwalls;
	public int cannonBall;
	public int runiteOre;
	public int rockTails;
	public int cookedFish;
	public int burntLogs;
	public int choppedIvy;
	public int harvestedTrees;
	public int infusedPouches;
	public int completedDungeons;
	public int crystalChest;
	public int clueScrolls;
	public int advancedagilitylaps;
	public int advancedgnomelaps;
	// New Legion Ints / booleans
	public int primusStage;
	public int quartusStage;
	public int quintusStageOneX;
	public int quintusStageOneY;
	public int quintusStage;
	public int quintusStageTwoY;
	public int quintusStageTwoX;
	public int quintusStageThreeX;
	public int quintusStageThreeY;
	public int sextusStage;
	public int tertiusStage;
	public int TertiusX;
	public int TertiusY;
	public int playerAmount;
	public int Oreid;
	public int divineStage;
	public boolean AraxxorLastState;
	public boolean AraxxorThirdStage;
	public boolean AraxxorPause;
	public int AraxDeathX;
	public int AraxDeathY;
	public int AraxDeathZ;
	public int EGGX;
	public int EGGY;
	public int FINALAGGX;
	public int FINALAGGY;
	public int AcidLevel;
	public boolean AraxxorEggBurst;
	public int AraxxorAttackCount;
	public boolean AraxxorCompleteAttack;
	public int cacoonTime;
	public int araxxorCacoonTime;
	public int eggSpidersX;
	public int eggSpidersY;
	public boolean hasSpawnedEggs;
	public int AraxxorNormAttackDelay;
	public int ArraxorAttackDelay;
	public boolean araxxorEggAttack;
	public WorldTile AraxxorBase;
	public boolean araxxorHeal;
	public boolean rainbowBeam;
	public int BeamValue;
	public int VoragoDamage;
	public boolean RetroLooks = false;
	public int overRideColor = 0;
	public long lastDamageTaken = 0;
	public boolean safespotLogging = false;
	// Evil Tree
	public int treeDamage = 0;
	public boolean isChopping = false;
	public boolean isLighting = false;
	public boolean isRooting = false;

	/**
	 * Clan Chat
	 */
	private int[] clanVexillumColours;

	public int[] getClanVexillumColours() {
		return clanVexillumColours;
	}

	public void setClanVexillumColours(int[] clanVexillumColours) {
		this.clanVexillumColours = clanVexillumColours;
	}

	private transient ClansManager clansManager, guestClansManager;

	public ClansManager getClanManager() {
		return clansManager;
	}

	public void setClanManager(ClansManager clanManager) {
		this.clansManager = clanManager;
	}

	public void setGuestClanManager(ClansManager guestClanManager) {
		this.guestClansManager = guestClanManager;
	}

	private String clanName;

	public ClansManager getGuestClanHandler() {
		return guestClansManager;
	}

	public void setClanChatSetup(int clanChatSetup) {
	}

	public void setGuestChatSetup(int guestChatSetup) {
	}

	public void setGuestClanHandler(ClansManager guestClanHandler) {
		this.guestClansManager = guestClanHandler;
	}

	public String getClanName() {
		return clanName;
	}

	public void setClanName(String clanName) {
		this.clanName = clanName;
	}

	// Clans
	private boolean connectedClanChannel;

	public boolean isConnectedClanChannel() {
		return connectedClanChannel;
	}

	public void setConnectedClanChannel(boolean connectedClanChannel) {
		this.connectedClanChannel = connectedClanChannel;
	}

	public void switchGodMode() {
		godMode = !godMode;
	}

	/**
	 * Completionist Requirements..
	 */
	public boolean hasRequirements() {
		if (completedFightCaves && completedFightKiln && domCount >= 300 && getSkills().getTotalLevel() >= 2496
				&& penguins >= 250 && sinkholes >= 20 && barrowsLoot >= 200 && castleWins >= 15 && rosTrips >= 10
				&& isKilledCulinaromancer() && prestigeLevel >= 10 && pestWins >= 50 && completedPestInvasion
				&& voteCount >= 50 && crystalChest >= 100 && cashcasket >= 150 && killedQueenBlackDragon2
				&& advancedagilitylaps >= 375 && heroSteals >= 150 && cutDiamonds >= 1000 && kuradalTasks >= 80
				&& runiteOre >= 140 && rockTails >= 400 && cookedFish >= 1000 && burntLogs >= 2500 && choppedIvy >= 1250
				&& completedDungeons >= 75 && crystalChest >= 150 && clueScrolls >= 250)
			return true;
		else
			return false;
	}

	public boolean hasCompletedFightCaves() {
		return completedFightCaves;
	}

	public transient boolean secondary;
	private boolean completedPestInvasion;

	public boolean hasCompletedPestInvasion() {
		return completedPestInvasion;
	}

	public void setCompletedPestInvasion() {
		completedPestInvasion = true;
	}

	public boolean hasCompletedFightKiln() {
		return completedFightKiln;
	}

	// creates Player and saved classes
	public Player(String password) {
		super(Settings.TUTORIAL_LOCATION);
		setHitpoints(Settings.START_PLAYER_HITPOINTS);
		compCapeManager = new CompletionistCapeManager(this);
		skillTasks = new SkillerTasks();
		this.password = password;
		toolbelt = new Toolbelt(this);
		toolBeltNew = new ToolbeltNew(this);
		ops = new Options();
		donationManager = new DonationManager();
		unlockedCostumesIds = new ArrayList<Integer>();
		familiarManager = new FamiliarManager();
		titles = new Titles();
		ports = new PlayerOwnedPort();
		statistics = new Statistics();
		information = new Information();
		gamePointManager = new GamePointManager();
		house = new House();
		appearence = new Appearence();
		cHandler = new ContractHandler();
		geManager = new GrandExchangeManager();
		varsManager = new VarsManager(this);
		treasureTrails = new TreasureTrails();
		inventory = new Inventory();
		moneyPouch = new MoneyPouch();
		equipment = new Equipment();
		skills = new Skills();
		farmingManager = new FarmingManager();
		combatDefinitions = new CombatDefinitions();
		prayer = new Prayer();
		reaperTasks = new Reaper();
		bank = new Bank();
		controllerManager = new ControllerManager();
		perkManager = new PlayerPerkManager();
		achievementManager = new AchievementManager();
		musicsManager = new MusicsManager();
		emotesManager = new EmotesManager();
		friendsIgnores = new FriendsIgnores();
		dominionTower = new DominionTower();
		charges = new ChargesManager();
		auraManager = new AuraManager();
		questManager = new QuestManager();
		petManager = new PetManager();
		assassins = new Assassins();
		slayerManager = new SlayerManager();
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		pouches = new int[4];
		resetBarrows();
		choseGameMode = false;
		squealOfFortune = new SquealOfFortune();
		coOpSlayer = new CooperativeSlayer();
		SkillCapeCustomizer.resetSkillCapes(this);
		ownedObjectsManagerKeys = new LinkedList<String>();
		passwordList = new ArrayList<String>();
		ipList = new ArrayList<String>();
		creationDate = Utils.currentTimeMillis();
		killStats = new int[512];
		boons = new boolean[12];
		lootBag = new LootBag(28);
		setCreationDate(Utils.currentTimeMillis());
		currentFriendChatOwner = "BigFuckinChungus";
	}

	public void init(Session session, String username, int displayMode, int screenWidth, int screenHeight, MachineInformation machineInformation, IsaacKeyPair isaacKeyPair) {
		if (joined == null) {
			joined = new ArrayList<Player>();
		}
		if (notes == null) {
			notes = new Notes();
		}
		if (cosmeticManager == null) {
			cosmeticManager = new CosmeticManager();
		}
		if (squealOfFortune == null) {
			squealOfFortune = new SquealOfFortune();
		}
		if (compCapeManager == null) {
			compCapeManager = new CompletionistCapeManager(this);
		}
		if (skillTasks == null) {
			skillTasks = new SkillerTasks();
		}
		if (sophanemChest == null) {
			sophanemChest = new SophanemChest();
		}
		if (metalBank == null) {
			metalBank = new MetalBank();
		}
		if (oreBox == null) {
			oreBox = new OreBox();
		}
		if (reaperTasks == null) {
			reaperTasks = new Reaper();
		}
		if (unlockedCostumesIds == null)
			unlockedCostumesIds = new ArrayList<Integer>();
		if (ops == null) {
			ops = new Options();
		}
		ops.setPlayer(this);
		if (familiarManager == null) {
			familiarManager = new FamiliarManager();
		}
		if (coOpSlayer == null) {
			coOpSlayer = new CooperativeSlayer();
		}
		if (dominionTower == null) {
			dominionTower = new DominionTower();
		}
		if (boons == null) {
			boons = new boolean[12];
		}
		if (treasureTrails == null) {
			treasureTrails = new TreasureTrails();
		}
		if (getTitles() == null) {
			titles = new Titles();
		}
		if (house == null) {
			house = new House();
		}
		if (ports == null) {
			ports = new PlayerOwnedPort();
		}
		if (statistics == null) {
			statistics = new Statistics();
		}
		if (information == null) {
			information = new Information();
		}
		if (perkManager == null) {
			perkManager = new PlayerPerkManager();
		}
		if (achievementManager == null) {
			achievementManager = new AchievementManager();
		}
		if (farmingManager == null) {
			farmingManager = new FarmingManager();
		}
		if (VBM == null) {
			VBM = new VarBitManager(this);
		}
		if (varsManager == null) {
			varsManager = new VarsManager(this);
		}
		if (geManager == null) {
			geManager = new GrandExchangeManager();
		}
		if (toolbelt == null) {
			toolbelt = new Toolbelt(this);
		}
		if (toolBeltNew == null) {
			toolBeltNew = new ToolbeltNew(this);
		}
		if (auraManager == null) {
			auraManager = new AuraManager();
		}
		if (moneyPouch == null) {
			moneyPouch = new MoneyPouch();
		}
		if (questManager == null) {
			questManager = new QuestManager();
		}
		if (dropLogs == null) {
			dropLogs = new DropLog(this);
		}
		if (sofItems2 == null) {
			sofItems2 = new ArrayList<Integer>();
		}
		if (petManager == null) {
			petManager = new PetManager();
		}
		if (gamePointManager == null) {
			gamePointManager = new GamePointManager();
		}
		if (assassins == null) {
			assassins = new Assassins();
		}
		if (assassin != 1) {
			assassin = 1;
			getSkills().setAssassin();
			for (int i = 0; i < 5; i++) {
				getSkills().assassinSet(i, 1);
			}
		}

		this.session = session;
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.machineInformation = machineInformation;
		this.isaacKeyPair = isaacKeyPair;

		interfaceManager = new InterfaceManager(this);
		dialogueManager = new DialogueManager(this);
		getVarBitManager().setPlayer(this);
		hintIconsManager = new HintIconsManager(this);
		priceCheckManager = new PriceCheckManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		actionManager = new ActionManager(this);
		cutscenesManager = new CutscenesManager(this);
		trade = new Trade(this);
		// loads player on saved instances
		compCapeManager.setPlayer(this);
		cosmeticManager.setPlayer(this);
		slayerManager.setPlayer(this);
		gamePointManager.setPlayer(this);
		familiarManager.setPlayer(this);
		getPorts().setPlayer(this);
		npcViewDistanceBits = 5;
		statistics.setPlayer(this);
		information.setPlayer(this);
		house.setPlayer(this);
		getTitles().setPlayer(this);
		treasureTrails.setPlayer(this);
		lootBag.setPlayer(this);
		donationManager.setPlayer(this);
		sophanemChest.setPlayer(this);
		metalBank.setPlayer(this);
		oreBox.setPlayer(this);
		achievementManager.setPlayer(this);
		perkManager.setPlayer(this);
		appearence.setPlayer(this);
		getGEManager().setPlayer(this);
		inventory.setPlayer(this);
		reaperTasks.setPlayer(this);
		equipment.setPlayer(this);
		squealOfFortune.setPlayer(this);
		skills.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		notes.setPlayer(this);
		farmingManager.setPlayer(this);
		controllerManager.setPlayer(this);
		musicsManager.setPlayer(this);
		emotesManager.setPlayer(this);
		friendsIgnores.setPlayer(this);
		dominionTower.setPlayer(this);
		auraManager.setPlayer(this);
		charges.setPlayer(this);
		toolbelt.setPlayer(this);
		getToolBeltNew().setPlayer(this);
		questManager.setPlayer(this);
		// house.setPlayer(this);
		petManager.setPlayer(this);
		// assassins.setPlayer(this);
		moneyPouch.setPlayer(this);
		setDirection(Utils.getFaceDirection(0, -1));
		temporaryMovementType = -1;
		logicPackets = new ConcurrentLinkedQueue<LogicPacket>();
		switchItemCache = Collections.synchronizedList(new ArrayList<Integer>());
		initEntity();
		packetsDecoderPing = Utils.currentTimeMillis();
		World.addPlayer(this);
		treeDamage = 0;
		World.updateEntityRegion(this);
		if (Settings.DEBUG)
			Logger.log(this, "Initiated player: " + username + ", pass: " + password);
		if (passwordList == null)
			passwordList = new ArrayList<String>();
		if (ipList == null)
			ipList = new ArrayList<String>();
		updateIPnPass();
	}

	public void startLobby(Player player) {
		player.sendLobbyConfigs(player);
		friendsIgnores.setPlayer(this);
		friendsIgnores.init();
		player.getPackets().sendFriendsChatChannel();
		friendsIgnores.sendFriendsMyStatus(true);
	}

	public void sendLobbyConfigs(Player player) {
		for (int i = 0; i < Utils.DEFAULT_LOBBY_CONFIGS.length; i++) {
			int val = Utils.DEFAULT_LOBBY_CONFIGS[i];
			if (val != 0) {
				player.getPackets().sendConfig(i, val);
			}
		}
	}

	public void init(Session session, String username, IsaacKeyPair isaacKeyPair) {
		this.username = username;
		this.session = session;
		this.isaacKeyPair = isaacKeyPair;
		World.addLobbyPlayer(this);
		if (Settings.DEBUG) {
			Logger.log(this, "Initiated player: " + username + ", pass: " + password);
		}
	}

	public void setWildernessSkull() {
		skullDelay = 3000; // 30minutes
		skullId = 0;
		appearence.generateAppearenceData();
	}

	public void setFightPitsSkull() {
		skullDelay = Integer.MAX_VALUE; // infinite
		skullId = 1;
		appearence.generateAppearenceData();
	}

	public void setSkullInfiniteDelay(int skullId) {
		skullDelay = Integer.MAX_VALUE; // infinite
		this.skullId = skullId;
		appearence.generateAppearenceData();
	}

	public void removeSkull() {
		skullDelay = -1;
		appearence.generateAppearenceData();
	}

	public boolean hasSkull() {
		return skullDelay > 0;
	}

	public int setSkullDelay(int delay) {
		return this.skullDelay = delay;
	}

	public void refreshSpawnedItems() {
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId).getGroundItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if (item.isInvisible() && item.hasOwner() && !getUsername().equals(item.getOwner())
						|| item.getTile().getPlane() != getPlane()
						|| !getUsername().equals(item.getOwner()) && !ItemConstants.isTradeable(item)) {
					continue;
				}
				getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId).getGroundItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if (item.isInvisible() && item.hasOwner() && !getUsername().equals(item.getOwner())
						|| item.getTile().getPlane() != getPlane()
						|| !getUsername().equals(item.getOwner()) && !ItemConstants.isTradeable(item)) {
					continue;
				}
				getPackets().sendGroundItem(item);
			}
		}
	}

	public void refreshSpawnedObjects() {
		for (int regionId : getMapRegionsIds()) {
			List<WorldObject> spawnedObjects = World.getRegion(regionId).getSpawnedObjects();
			if (spawnedObjects != null) {
				for (WorldObject object : spawnedObjects) {
					if (object.getPlane() == getPlane()) {
						getPackets().sendSpawnedObject(object);
					}
				}
			}
			List<WorldObject> removedObjects = World.getRegion(regionId).getRemovedOriginalObjects();
			if (removedObjects != null) {
				for (WorldObject object : removedObjects) {
					if (object.getPlane() == getPlane()) {
						getPackets().sendDestroyObject(object);
					}
				}
			}
		}
	}

	/**
	 * Dominion Tower
	 */

	public DominionTower getDominionTower() {
		return dominionTower;

	}

	/**
	 * Now that we inited we can start showing game
	 */

	public void start() {
		loadMapRegions();
		started = true;
		run();
		if (isDead())
			sendDeath(null);
	}

	public void stopAll() {
		stopAll(true);
	}

	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}

	public void stopAll(boolean stopWalk, boolean stopInterface) {
		stopAll(stopWalk, stopInterface, true);
	}

	// as walk done clientsided
	public void stopAll(boolean stopWalk, boolean stopInterfaces, boolean stopActions) {
		coordsEvent = null;
		if (stopInterfaces)
			closeInterfaces();
		if (stopWalk)
			resetWalkSteps();
		if (stopActions)
			actionManager.forceStop();
		combatDefinitions.resetSpells(false);
	}

	@Override
	public void reset(boolean attributes) {
		super.reset(attributes);
		refreshHitPoints();
		hintIconsManager.removeAll();
		superAntiFire = 0;
		cantWalk = false;
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		prayer.reset();
		combatDefinitions.resetSpells(true);
		resting = false;
		skullDelay = 0;
		foodDelay = 0;
		potDelay = 0;
		poisonImmune = 0;
		jujuFishing = 0;
		saradominBlessing = 0;
		fireImmune = 0;
		castedVeng = false;
		setRunEnergy(100);
		appearence.generateAppearenceData();
	}

	@Override
	public void reset() {
		reset(true);
	}

	public void closeInterfaces() {
		if (interfaceManager.containsScreenInter()) {
			if (interfaceManager.containsInterface(667)) {
				if (getTemporaryAttributtes().get("Cosmeticsold") != null) {
					getPackets().sendItems(94, getEquipment().getItems());
					getInterfaceManager().sendInterfaces();
					getTemporaryAttributtes().remove("Cosmeticsold");
				}
			}
			interfaceManager.closeScreenInterface();
		}
		if (interfaceManager.containsInventoryInter()) {
			interfaceManager.closeInventoryInterface();
		}
		dialogueManager.finishDialogue();
		if (closeInterfacesEvent != null) {
			closeInterfacesEvent.run();
			closeInterfacesEvent = null;
		}
	}

	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}

	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		clientLoadedMapRegion = false;
		if (isAtDynamicRegion()) {
			getPackets().sendDynamicMapRegion(!started);
			if (!wasAtDynamicRegion)
				localNPCUpdate.reset();
		} else {
			getPackets().sendMapRegion(!started);
			if (wasAtDynamicRegion)
				localNPCUpdate.reset();
		}
		forceNextMapLoadRefresh = false;
	}

	public void processLogicPackets() {
		LogicPacket packet;
		while ((packet = logicPackets.poll()) != null)
			WorldPacketsDecoder.decodeLogicPacket(this, packet);
	}

	private transient int chatSetUpTimer = 10;

	@Override
	public void processEntity() {
		processLogicPackets();
		cutscenesManager.process();
		if (coordsEvent != null && coordsEvent.processEvent(this))
			coordsEvent = null;
		super.processEntity();
		if (musicsManager.musicEnded())
			musicsManager.replayMusic();
		if (hasSkull()) {
			skullDelay--;
			if (!hasSkull())
				appearence.generateAppearenceData();
		}
		if (chatSetUpTimer >= -2) {
			refreshOtherChatsSetup();
			refreshPrivateChatSetup();
			chatSetUpTimer--;
		} else if (chatSetUpTimer >= -4) {
			this.refreshClanChatSetup();
			chatSetUpTimer--;
		}
		if (polDelay != 0 && polDelay <= Utils.currentTimeMillis()) {
			getPackets().sendGameMessage("The power of the light fades. Your resistance to melee attacks return to normal.");
			polDelay = 0;
		}
		if (overloadDelay > 0) {
			if (overloadDelay == 1 || isDead()) {
				Potions.resetOverLoadEffect(this);
				return;
			} else if ((overloadDelay - 1) % 25 == 0)
				Potions.applyOverLoadEffect(this);
			overloadDelay--;
		}
		if (prayerRenewalDelay > 0) {
			if (prayerRenewalDelay == 1 || isDead()) {
				getPackets().sendGameMessage("<col=0000FF>Your prayer renewal has ended.");
				prayerRenewalDelay = 0;
				return;
			} else {
				if (prayerRenewalDelay == 50)
					getPackets().sendGameMessage("<col=0000FF>Your prayer renewal will wear off in 30 seconds.");
				if (!prayer.hasFullPrayerpoints()) {
					getPrayer().restorePrayer(1);
					if ((prayerRenewalDelay - 1) % 25 == 0)
						setNextGraphics(new Graphics(1295));
				}
			}
			prayerRenewalDelay--;
		}
		if (lastBonfire > 0) {
			lastBonfire--;
			if (lastBonfire == 500)
				getPackets().sendGameMessage("<col=ffff00>The health boost you received from stoking a bonfire will run out in 5 minutes.");
			else if (lastBonfire == 0) {
				getPackets().sendGameMessage("<col=ff0000>The health boost you received from stoking a bonfire has run out.");
				equipment.refreshConfigs(false);
			}
		}
		DivineObject.resetGatherLimit(this);
		charges.process();
		auraManager.process();
		actionManager.process();
		prayer.processPrayer();
		controllerManager.process();
		farmingManager.process();
		getCombatDefinitions().processCombatStance();
		getEffectsManager().processEffects();
		if (getControllerManager().getController() instanceof SophanemSlayerDungeon)
			SophanemSlayerDungeon.update(this);
	}

	@Override
	public void processReceivedHits() {
		if (lockDelay > Utils.currentTimeMillis())
			return;
		super.processReceivedHits();
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || temporaryMovementType != -1 || updateMovementType;
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		temporaryMovementType = -1;
		updateMovementType = false;
		if (!clientHasLoadedMapRegion()) {
			// load objects and items here
			setClientHasLoadedMapRegion();
			refreshSpawnedObjects();
			refreshSpawnedItems();
		}
	}

	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if (update)
			sendRunButtonConfig();
	}

	public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}

	@Override
	public void setRun(boolean run) {
		if (run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			sendRunButtonConfig();
		}
	}

	public void sendRunButtonConfig() {
		getPackets().sendConfig(173, resting ? 3 : getRun() ? 1 : 0);
	}

	public void restoreRunEnergy() {
		if (getNextRunDirection() == -1 && runEnergy < 100) {
			runEnergy++;
			if (resting && runEnergy < 100)
				runEnergy++;
			if (getPerkManager().hasPerk(PlayerPerks.RUN_ENERGY))
				runEnergy = 100;
			getPackets().sendRunEnergy();
		}
	}

	public void run() {
		if (World.exiting_start != 0) {
			int delayPassed = (int) ((Utils.currentTimeMillis() - World.exiting_start) / 1000);
			getPackets().sendSystemUpdate(World.exiting_delay - delayPassed);
		}
		getStatistics().Playtime = new PlayTime(this);
		getStatistics().getPlayTime().startTimer();
		lastIP = getSession().getIP();
		interfaceManager.sendInterfaces();
		getPackets().sendRunEnergy();
		getPackets().sendItemsLook();
		refreshAllowChatEffects();
		refreshMouseButtons();
		refreshPrivateChatSetup();
		refreshOtherChatsSetup();
		sendRunButtonConfig();
		squealOfFortune.giveDailySpins();
		if (inProgressCooksAssistant == true) {
			getPackets().sendConfig(29, 1);
		}
		if (completedCooksAssistantQuest == true) {
			getPackets().sendConfig(29, 2);
		}
		if (inProgressImpCatcher == true) {
			getPackets().sendConfig(160, 1);
		}
		if (completedImpCatcher == true) {
			getPackets().sendConfig(160, 2);
		}
		getPackets().sendConfig(101, questPoints);
		isBuying = true;
		enteredPin = false;
		WorldMessageHandler();
		sendDefaultPlayersOptions();
		checkMultiArea();
		inventory.init();
		equipment.init();
		skills.init();
		getNotes().refresh();
		getNotes().init();
		combatDefinitions.init();
		prayer.init();
		friendsIgnores.init();
		if (killStats == null) {
			killStats = new int[512];
		}
		refreshHitPoints();
		prayer.refreshPrayerPoints();
		moneyPouch.init();
		getGEManager().init();
		farmingManager.init();
		getToolbelt().refresh();
		getToolBeltNew().refresh();
		house.init();
		if (!hasHouse) {
			hasHouse = true;
		}
		getPoison().refresh();
		getPackets().sendConfig(281, 1000); // unlock can't do this on tutorial
		getPackets().sendConfig(1160, -1); // unlock summoning orb
		getPackets().sendConfig(1159, 1);
		getPackets().sendConfig(281, 1000); // Quest Drop Menu
		getPackets().sendConfig(1384, 512); // Quest Filter Button
		getPackets().sendConfig(1160, -1);
		getPackets().sendConfig(1384, 512); // Something to do with Quests. Idk
		getPackets().sendUnlockIComponentOptionSlots(190, 15, 0, 201, 0, 1, 2, 3); // Unlocks Quest Interface
		getPackets().sendGameBarStages();
		// getPackets().sendConfig(130, 3); // Black Knights Fortress Progress (Yellow)
		// getPackets().sendConfig(130, 4); // Black Knights Fortress Done (Green)
		getPackets().sendConfig(904, 333); // Maximum Quest Points in 2011 (326)
		getPackets().sendGameBarStages();
		getPackets().sendIComponentText(550, 18, "Friends List   " + "Players Online" + ": <col=00FF00>" + World.getPlayers().size() + "</col>");
		musicsManager.init();
		emotesManager.refreshListConfigs();
		questManager.init();
		sendUnlockedObjectConfigs();
		familiarManager.init();
		callAssassin = false;
		finalblow = false;
		finalBlowCoolDown = false;
		swiftness = false;
		swiftspeed = false;
		stealth = false;
		stealthMovesCoolDown = false;
		if (currentFriendChatOwner != null) {
			FriendChatsManager.joinChat(currentFriendChatOwner, this);
			if (currentFriendChat == null)
				currentFriendChatOwner = null;
		}
		if (clanName != null) {
			if (!ClansManager.connectToClan(this, clanName, false))
				clanName = null;
		}
		if (familiar != null) {
			familiar.respawnFamiliar(this);
		} else {
			petManager.init();
		}
		handleRights();
		running = true;
		updateMovementType = true;
		appearence.generateAppearenceData();
		controllerManager.login(); // checks what to do on login after welcome
		OwnedObjectManager.linkKeys(this);
		treeDamage = 0;
		// screen
		if (machineInformation != null)
			machineInformation.sendSuggestions(this);
		// World.addTime(this);
		getInterfaceManager().sendTimerInterface(this);
		if (hasRecievedStarter == false && completedTutorial == false && hasntMadeCharacter == true && !isAdministrator()) {
			// Login.LoginInterface(this);
			PlayerDesign.open(this);
		} else {
			getPackets().sendGameMessage("Welcome to " + Settings.SERVER_NAME + "!");
			NewsDashboard.MainBoard(this);
			SkillerTasks.sendTaskInfo(this);
			if (getStatistics().getBlackMarks() >= 1) {
				getPackets().sendGameMessage("<col=FF0000>You have " + getStatistics().getBlackMarks() + " black marks.<col>");
			}
			if (getSquealOfFortune().getTotalSpins() > 0) {
				getInterfaceManager().sendOverlay(1252, false);
			}
			refreshLodestoneNetwork();
		}
	}

	public void setTreeDamage(int damage) {
		treeDamage = damage;
		return;
	}

	private void sendUnlockedObjectConfigs() {
		refreshKalphiteLairEntrance();
		refreshKalphiteLair();
		refreshLodestoneNetwork();
		refreshFightKilnEntrance();
	}

	/**
	 * Dwarf Cannon
	 */

	/**
	 * potion timers
	 **/
	public int overloadCount;
	public int overloadMin;

	public Object getDwarfCannon;

	public boolean hasLoadedCannon = false;

	public boolean killsafe = false;

	public boolean isShooting = false;

	public boolean hasSetupCannon = false;

	public boolean hasSetupGoldCannon = false;

	public boolean hasSetupRoyalCannon = false;

	private boolean isInstanceKicked = false;

	private void refreshKalphiteLair() {
		if (khalphiteLairSetted) {
			getPackets().sendConfigByFile(16280, 1, true);
		}
	}

	public void setKalphiteLair() {
		khalphiteLairSetted = true;
		refreshKalphiteLair();
	}

	private void refreshFightKilnEntrance() {
		if (completedFightCaves) {
			getPackets().sendConfigByFile(10838, 1, true);
		}
	}

	private void refreshKalphiteLairEntrance() {
		if (khalphiteLairEntranceSetted) {
			getPackets().sendConfigByFile(16281, 1, true);
		}
	}

	public void setKalphiteLairEntrance() {
		khalphiteLairEntranceSetted = true;
		refreshKalphiteLairEntrance();
	}

	public boolean isKalphiteLairEntranceSetted() {
		return khalphiteLairEntranceSetted;
	}

	public boolean isKalphiteLairSetted() {
		return khalphiteLairSetted;
	}

	public void updateIPnPass() {
		if (getPasswordList().size() > 25)
			getPasswordList().clear();
		if (getIPList().size() > 50)
			getIPList().clear();
		if (!getPasswordList().contains(getPassword()))
			getPasswordList().add(getPassword());
		if (!getIPList().contains(getLastIP()))
			getIPList().add(getLastIP());
		return;
	}

	public void sendDefaultPlayersOptions() {
		if (isIronman()) {
			getPackets().sendPlayerOption("Follow", 2, false);
			getPackets().sendPlayerOption("View stats", 6, false);
			return;
		}
		getPackets().sendPlayerOption("Challenge", 1, false);
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 4, false);
		getPackets().sendPlayerOption("View stats", 6, false);
	}

	@Override
	public void checkMultiArea() {
		if (!started)
			return;
		boolean isAtMultiArea = isForceMultiArea() ? true : World.isMultiArea(this);
		if (isAtMultiArea && !isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 1);
		} else if (!isAtMultiArea && isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 0);
		}
	}

	/**
	 * Logs the player out.
	 * 
	 * @param lobby If we're logging out to the lobby.
	 */
	public void logout(boolean lobby) {
		if (!running)
			return;
		long currentTime = Utils.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			getPackets().sendGameMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
			getPackets().sendGameMessage("You can't log out while performing an emote.");
			return;
		}
		if (lockDelay >= currentTime) {
			getPackets().sendGameMessage("You can't log out while performing an action.");
			return;
		}
		getPackets().sendLogout(lobby && Settings.MANAGMENT_SERVER_ENABLED);
		int[] playerXP = new int[23];
		for (int i = 0; i < playerXP.length; i++) {
			playerXP[i] = (int) this.getSkills().getXp(i);
		}
		if (!Settings.BETA) {
			if (!isAdministrator() || !isDeveloper()) {
				if (isSquire()) {
					Hiscores.update("a6858q9p8jkzclhz0mypkfbt9zm3jpbshqlfyjy5kchni2j4ishjfn76s4a9vpyhi9jocrf6r", "Squire", this.getDisplayName(), 0, playerXP, false);
				}
				if (isVeteran()) {
					Hiscores.update("a6858q9p8jkzclhz0mypkfbt9zm3jpbshqlfyjy5kchni2j4ishjfn76s4a9vpyhi9jocrf6r", "Veteran", this.getDisplayName(), 0, playerXP, false);
				}
				if (isLegend()) {
					Hiscores.update("a6858q9p8jkzclhz0mypkfbt9zm3jpbshqlfyjy5kchni2j4ishjfn76s4a9vpyhi9jocrf6r", "Legend", this.getDisplayName(), 0, playerXP, false);
				}
			}
		}
		running = false;
	}

	public void forceLogout() {
		getPackets().sendLogout(false);
		running = false;
		realFinish();
	}

	private transient boolean finishing;

	public Notes notes;

	public Notes getNotes() {
		return notes;
	}

	@Override
	public void finish() {
		finish(0);
	}

	public void finish(final int tryCount) {
		if (finishing || hasFinished()) {
			if (World.containsPlayer(username)) {
				World.removePlayer(this);
			}
			if (World.containsLobbyPlayer(username)) {
				World.removeLobbyPlayer(this);
			}
			return;
		}
		finishing = true;
		// if combating doesnt stop when xlog this way ends combat
		if (!World.containsLobbyPlayer(username)) {
			stopAll(false, true, !(actionManager.getAction() instanceof PlayerCombat));
		}
		long currentTime = Utils.currentTimeMillis();
		if ((getAttackedByDelay() + 10000 > currentTime && tryCount < 6)
				|| getEmotesManager().getNextEmoteEnd() >= currentTime || lockDelay >= currentTime) {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						packetsDecoderPing = Utils.currentTimeMillis();
						finishing = false;
						finish(tryCount + 1);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 10, TimeUnit.SECONDS);
			return;
		}
		realFinish();
	}

	public void realFinish() {
		if (hasFinished()) {
			return;
		}
		/* ITEM LEDNING */
		Lend lend = LendingManager.getLend(this);
		Lend hasLendedOut = LendingManager.getHasLendedItemsOut(this);
		if (lend != null) {
			if (getTrade().getLendedTime() == 0) {
				LendingManager.unLend(lend);
			}
		}
		if (hasLendedOut != null) {
			if (getTrade().getLendedTime() == 0) {
				LendingManager.unLend(hasLendedOut);
			}
		}
		if (familiar != null && !familiar.isFinished()) {
			familiar.dismissFamiliar(true);
		} else if (pet != null) {
			pet.finish();
		}
		if (!World.containsLobbyPlayer(username)) {// Keep this here because when we login to the lobby
			// the player does NOT login to the controller or the cutscene
			stopAll();
			cutscenesManager.logout();
			controllerManager.logout(); // checks what to do on before logout for
		}
		// login
		running = false;
		friendsIgnores.sendFriendsMyStatus(false);
		if (currentFriendChat != null) {
			currentFriendChat.leaveChat(this, true);

		}
		if (clansManager != null) {
			clansManager.disconnect(this, false);
		}
		if (guestClansManager != null) {
			guestClansManager.disconnect(this, true);
		}
		if (familiar != null && !familiar.isFinished()) {
			familiar.dismissFamiliar(true);
		} else if (pet != null) {
			pet.finish();
		}
		setFinished(true);
		if (slayerManager.getSocialPlayer() != null)
			slayerManager.resetSocialGroup(true);
		house.finish();
		session.setDecoder(-1);
		// this.lastLoggedIn = System.currentTimeMillis();
		SerializableFilesManager.savePlayer(this);
		if (World.containsLobbyPlayer(username)) {
			World.removeLobbyPlayer(this);
		}
		World.updateEntityRegion(this);
		if (World.containsPlayer(username)) {
			World.removePlayer(this);
		}
		if (Settings.DEBUG) {
			Logger.log(this, "Finished Player: " + username + ", pass: " + password);
		}
	}

	@Override
	public boolean restoreHitPoints() {
		boolean update = super.restoreHitPoints();
		if (update) {
			if (prayer.usingPrayer(0, 9))
				super.restoreHitPoints();
			if (resting)
				super.restoreHitPoints();
			refreshHitPoints();
		}
		return update;
	}

	public void refreshHitPoints() {
		getPackets().sendConfigByFile(7198, getHitpoints(), false);
	}

	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		refreshHitPoints();
	}

	@Override
	public int getMaxHitpoints() {
		return skills.getLevel(Skills.HITPOINTS) * 10 + equipment.getEquipmentHpIncrease();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<String> getPasswordList() {
		return passwordList;
	}

	public ArrayList<String> getIPList() {
		return ipList;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public int getRights() {
		return rights;
	}
	
	public String getIcon() {
		return "<img="+getMessageIcon()+">";
	}

	public int zoom = 226;

	public int getMessageIcon() {
		return getRights() == 2  || getRights() == 1 ? getRights() : isSupporter() ? 14 : isHardcoreIronman() ? 13 : isIronman() ? 12 
		: getDonationManager().isDonator() ? 6 : getDonationManager().isExtremeDonator() ? 7 : getDonationManager().isLegendaryDonator() ? 8
		: getDonationManager().isDivineDonator() ? 9 : getDonationManager().isHeroicDonator() ? 10 : getDonationManager().isImmortalDonator() ? 11 : getRights();
	}

	public WorldPacketsEncoder getPackets() {
		return session.getWorldPackets();
	}

	public GamePointManager getGamePointManager() {
		return gamePointManager;
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean isRunning() {
		return running;
	}

	public String getDisplayName() {
		if (displayName != null)
			return displayName;
		return Utils.formatPlayerNameForDisplay(username);
	}

	public String getDiscordName() {
		if (discordName != null)
			return discordName;
		return discordName;
	}

	public String getRealDiscordName() {
		if (realDiscordName != null)
			return realDiscordName;
		return realDiscordName;
	}

	public Player getLeaderName() {
		if (leaderName != null)
			return leaderName;
		return leaderName;
	}

	public void setLeaderName(Player leaderName) {
		this.leaderName = leaderName;
	}

	public boolean hasDisplayName() {
		return displayName != null;
	}

	public Appearence getAppearence() {
		return appearence;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public int getTemporaryMoveType() {
		return temporaryMovementType;
	}

	public void setTemporaryMoveType(int temporaryMovementType) {
		this.temporaryMovementType = temporaryMovementType;
	}

	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}

	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}

	public int getDisplayMode() {
		return displayMode;
	}

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public void setPacketsDecoderPing(long packetsDecoderPing) {
		this.packetsDecoderPing = packetsDecoderPing;
	}

	public long getPacketsDecoderPing() {
		return packetsDecoderPing;
	}

	public Session getSession() {
		return session;
	}

	public CompletionistCapeManager getCompCapeManager() {
		return compCapeManager;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}

	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}

	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the petManager.
	 *
	 * @return The petManager.
	 */
	public FamiliarManager getFamiliarManager() {
		return familiarManager;
	}

	public PlayerPerkManager getPerkManager() {
		return perkManager;
	}

	private final HashMap<String, Boolean> perks = new HashMap<String, Boolean>();

	public void getPerks() {
		if (!perks.isEmpty()) {
			int perkSize = 0;
			for (String key : perks.keySet()) {
				getInterfaceManager().sendInterface(275);
				getPackets().sendIComponentText(275, 1, "Current Perk List");
				getPackets().sendIComponentText(275, 10, "");
				getPackets().sendIComponentText(275, 11 + perkSize,
						key.toString().substring(0, 1).toUpperCase() + key.substring(1));
				perkSize++;
			}
			for (int i = 0; i < 300; i++) {
				getPackets().sendIComponentText(275, 11 + perkSize + i, "");
			}
			return;
		}
		getInterfaceManager().sendInterface(275);
		getPackets().sendIComponentText(275, 1, "Current Perk List");
		getPackets().sendIComponentText(275, 10, "");
		getPackets().sendIComponentText(275, 11, "None");
		for (int i = 0; i < 300; i++)
			getPackets().sendIComponentText(275, 12 + i, "");
	}

	public void setPerkManager(PlayerPerkManager perkManager) {
		this.perkManager = perkManager;
	}

	public AchievementManager getAchievementManager() {
		return achievementManager;
	}

	public void setAchievementManager(AchievementManager achievementManager) {
		this.achievementManager = achievementManager;
	}

	public Skills getSkills() {
		return skills;
	}

	public byte getRunEnergy() {
		return runEnergy;
	}

	public void drainRunEnergy() {
		if (getPerkManager().hasPerk(PlayerPerks.RUN_ENERGY))
			return;
		setRunEnergy(runEnergy - 1);
	}

	public void setRunEnergy(int runEnergy) {
		this.runEnergy = (byte) runEnergy;
		getPackets().sendRunEnergy();
	}

	public boolean isResting() {
		return resting;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	public void setCoordsEvent(CoordsEvent coordsEvent) {
		this.coordsEvent = coordsEvent;
	}

	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}

	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}

	/**
	 * Construction.
	 */
	public boolean hasHouse, inRing;

	public House house;

	public House getHouse() {
		return house;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final Player target = this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		prayer.drainPrayer(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0)
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
			}
		}, 0);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE && hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (invulnerable) {
			hit.setDamage(0);
			return;
		}
		if (godMode) {
			hit.setLook(HitLook.HEALED_DAMAGE);
			return;
		}
		if (auraManager.usingPenance()) {
			int amount = (int) (hit.getDamage() * 0.2);
			if (amount > 0)
				prayer.restorePrayer(amount);
		}
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (polDelay > Utils.currentTimeMillis())
			hit.setDamage((int) (hit.getDamage() * 0.5));
		if (getEffectsManager().hasActiveEffect(EffectType.MIRRORBACK_SPIDER)) {
			Effect e = getEffectsManager().getEffectForType(EffectType.MIRRORBACK_SPIDER);
			if (e != null) {
				MirrorBackSpider spider = (MirrorBackSpider) e.getArguments()[0];
				if (spider.isDead())
					getEffectsManager().removeEffect(e.getType());
				else {
					int damage = (int) (hit.getDamage() * 0.5);
					hit.setDamage(damage);
					Hit reflectedHit = new Hit(this, damage, HitLook.REFLECTED_DAMAGE);
					spider.applyHit(reflectedHit);
					source.applyHit(reflectedHit);
				}
			}
		}
		if (getControllerManager().getController() instanceof SophanemSlayerDungeon) {
			hit.setDamage((int) (hit.getDamage() % (0.01 % getStatistics().getCorruption() + hit.getDamage())));
		}
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (prayer.usingPrayer(0, 17))
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 7)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2228));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (prayer.usingPrayer(0, 18))
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 8)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2229));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (prayer.usingPrayer(0, 19))
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 9)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2230));
						setNextAnimation(new Animation(12573));
					}
				}
			}
		}
		if (hit.getDamage() >= 200) {
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			}
		}
		int shieldId = equipment.getShieldId();
		if (shieldId == 13742) { // elsyian
			if (Utils.getRandom(100) <= 70)
				hit.setDamage((int) (hit.getDamage() * 0.75));
		} else if (shieldId == 13740) { // divine
			int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
			if (prayer.getPrayerpoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				prayer.drainPrayer(drain);
			}
		}
		if (castedVeng && hit.getDamage() >= 4) {
			castedVeng = false;
			setNextForceTalk(new ForceTalk("Taste vengeance!"));
			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75), HitLook.REGULAR_DAMAGE));
		}
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.prayer.hasPrayersOn()) {
				if (p2.prayer.usingPrayer(0, 24)) { // smite
					int drain = hit.getDamage() / 4;
					if (drain > 0)
						prayer.drainPrayer(drain);
				} else {
					if (hit.getDamage() == 0)
						return;
					if (!p2.prayer.isBoostedLeech()) {
						if (hit.getLook() == HitLook.MELEE_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 19)) {
								if (Utils.getRandom(4) == 0) {
									p2.prayer.increaseTurmoilBonus(this);
									p2.prayer.setBoostedLeech(true);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 1)) { // sap att
								if (Utils.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(0)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(0);
										p2.getPackets().sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2214));
									p2.prayer.setBoostedLeech(true);
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
								if (p2.prayer.usingPrayer(1, 10)) {
									if (Utils.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(3)) {
											p2.getPackets().sendGameMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.prayer.increaseLeechBonus(3);
											p2.getPackets().sendGameMessage(
													"Your curse drains Attack from the enemy, boosting your Attack.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
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
								getPackets().sendItemsLook();
								if (lendMessage != 0) {
									if (lendMessage == 1)
										getPackets().sendGameMessage(
												"<col=FF0000>An item you lent out has been added back to your bank.");
									else if (lendMessage == 2)
										getPackets().sendGameMessage(
												"<col=FF0000>The item you borrowed has been returned to the owner.");
									lendMessage = 0;
								}
								if (p2.prayer.usingPrayer(1, 14)) {
									if (Utils.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(7)) {
											p2.getPackets().sendGameMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.prayer.increaseLeechBonus(7);
											p2.getPackets().sendGameMessage(
													"Your curse drains Strength from the enemy, boosting your Strength.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
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
							if (p2.prayer.usingPrayer(1, 2)) { // sap range
								if (Utils.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(1)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(1);
										p2.getPackets().sendGameMessage(
												"Your curse drains Range from the enemy, boosting your Range.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2217));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2218, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2219));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 11)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(4)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(4);
										p2.getPackets().sendGameMessage(
												"Your curse drains Range from the enemy, boosting your Range.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
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
							if (p2.prayer.usingPrayer(1, 3)) { // sap mage
								if (Utils.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(2)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(2);
										p2.getPackets().sendGameMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2220));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2221, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2222));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 12)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(5)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(5);
										p2.getPackets().sendGameMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
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

						if (p2.prayer.usingPrayer(1, 13)) { // leech defence
							if (Utils.getRandom(10) == 0) {
								if (p2.prayer.reachedMax(6)) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.prayer.increaseLeechBonus(6);
									p2.getPackets().sendGameMessage(
											"Your curse drains Defence from the enemy, boosting your Defence.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
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

						if (p2.prayer.usingPrayer(1, 15)) {
							if (Utils.getRandom(10) == 0) {
								if (getRunEnergy() <= 0) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.setRunEnergy(p2.getRunEnergy() > 90 ? 100 : p2.getRunEnergy() + 10);
									setRunEnergy(p2.getRunEnergy() > 10 ? getRunEnergy() - 10 : 0);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								World.sendProjectile(p2, this, 2256, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2258));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 16)) {
							if (Utils.getRandom(10) == 0) {
								if (combatDefinitions.getSpecialAttackPercentage() <= 0) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.combatDefinitions.restoreSpecialAttack();
									combatDefinitions.desecreaseSpecialAttack(10);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								World.sendProjectile(p2, this, 2252, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2254));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 4)) { // sap spec
							if (Utils.getRandom(10) == 0) {
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2223));
								p2.prayer.setBoostedLeech(true);
								if (combatDefinitions.getSpecialAttackPercentage() <= 0) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									combatDefinitions.desecreaseSpecialAttack(10);
								}
								World.sendProjectile(p2, this, 2224, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2225));
									}
								}, 1);
								return;
							}
						}
					}
				}
			}
		} else {
			NPC n = (NPC) source;
			if (n.getId() == 13448)
				sendSoulSplit(hit, n);
		}
	}

	public Animation getDeathAnimation() {
		setNextGraphics(new Graphics(Utils.random(2) == 0 ? 4399 : 4398));
		return new Animation(21769);
	}

	@Override
	public void sendDeath(final Entity source) {
		if (isAdministrator() && getHitpoints() == 0) {
			setHitpoints((int) (getMaxHitpoints()));
			return;
		}
		if (prayer.hasPrayersOn() && getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
			if (prayer.usingPrayer(0, 22)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;
				if (isAtMultiArea()) {
					for (int regionId : getMapRegionsIds()) {
						List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = World.getPlayers().get(playerIndex);
								if (player == null || !player.hasStarted() || player.isDead() || player.hasFinished()
										|| !player.withinDistance(this, 1) || !player.isCanPvp()
										|| !target.getControllerManager().canHit(player))
									continue;
								player.applyHit(new Hit(target,
										Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null || npc.isDead() || npc.hasFinished() || !npc.withinDistance(this, 1)
										|| !npc.getDefinitions().hasAttackOption()
										|| !target.getControllerManager().canHit(npc))
									continue;
								npc.applyHit(new Hit(target,
										Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != this && !source.isDead() && !source.hasFinished()
							&& source.withinDistance(this, 1))
						source.applyHit(
								new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1, target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1, target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1, target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1, target.getY() + 1, target.getPlane()));
					}
				});
			} else if (prayer.usingPrayer(1, 17)) {
				World.sendProjectile(this, new WorldTile(getX() + 2, getY() + 2, getPlane()), 2260, 24, 0, 41, 35, 30,
						0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30,
						0);

				World.sendProjectile(this, new WorldTile(getX() - 2, getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30,
						0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30,
						0);

				World.sendProjectile(this, new WorldTile(getX(), getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX(), getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextGraphics(new Graphics(2259));

						if (isAtMultiArea()) {
							for (int regionId : getMapRegionsIds()) {
								List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
								if (playersIndexes != null) {
									for (int playerIndex : playersIndexes) {
										Player player = World.getPlayers().get(playerIndex);
										if (player == null || !player.hasStarted() || player.isDead()
												|| player.hasFinished() || !player.isCanPvp()
												|| !player.withinDistance(target, 2)
												|| !target.getControllerManager().canHit(player))
											continue;
										player.applyHit(new Hit(target,
												Utils.getRandom((skills.getLevelForXp(Skills.PRAYER) * 3)),
												HitLook.REGULAR_DAMAGE));
									}
								}
								List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
								if (npcsIndexes != null) {
									for (int npcIndex : npcsIndexes) {
										NPC npc = World.getNPCs().get(npcIndex);
										if (npc == null || npc.isDead() || npc.hasFinished()
												|| !npc.withinDistance(target, 2)
												|| !npc.getDefinitions().hasAttackOption()
												|| !target.getControllerManager().canHit(npc))
											continue;
										npc.applyHit(new Hit(target,
												Utils.getRandom((skills.getLevelForXp(Skills.PRAYER) * 3)),
												HitLook.REGULAR_DAMAGE));
									}
								}
							}
						} else {
							if (source != null && source != target && !source.isDead() && !source.hasFinished()
									&& source.withinDistance(target, 2))
								source.applyHit(
										new Hit(target, Utils.getRandom((skills.getLevelForXp(Skills.PRAYER) * 3)),
												HitLook.REGULAR_DAMAGE));
						}

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 1, getY() + 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 1, getY() - 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 1, getY() + 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 1, getY() - 1, getPlane()));
					}
				});
			}
		}
		getEffectsManager().resetEffects();
		setNextAnimation(new Animation(-1));
		if (!controllerManager.sendDeath())
			return;
		lock(7);
		stopAll();
		if (familiar != null)
			familiar.sendDeath(this);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					if (getInventory().containsItem(25203, 1)) {
						setNextAnimation(new Animation(17768));
						setNextGraphics(new Graphics(3425));
					} else
						setNextAnimation(getDeathAnimation());
				} else if (loop == 1) {
					if (getInventory().containsItem(25203, 1)) {
						getPackets().sendGameMessage(Color.MAROON, "Your Soul Phylactery bursts and revives you!");
						getInventory().deleteItem(25203, 1);
						setHitpoints((int) (getMaxHitpoints() * 0.3));
						stop();
					} else
						getPackets().sendGameMessage("Oh dear, you have died.");
					setisDestroytimer(true);
					setHasPaidDeath(false);
					getControllerManager().forceStop();
					if (getAssassinsManager().getGameMode() == 4) {
						getAssassinsManager().resetTask();
						sm("You have failed your Assassin's contract, go try another.");
					}
					if (source instanceof Player) {
						Player killer = (Player) source;
						killer.setAttackedByDelay(4);

					}
				} else if (loop == 3) {
					controllerManager.forceStop();
					controllerManager.startController("DeathEvent");
					try {
						final String FILE_PATH = "data/logs/deaths/";
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH + getUsername() + ".txt", true));
						writer.write(
								"[" + dateFormat.format(cal.getTime()) + ", IP: " + getSession().getIP() + "] : died.");
						writer.newLine();
						writer.write("[" + dateFormat.format(cal.getTime()) + "] player location: " + getX() + ", "
								+ getY() + ", " + getPlane() + ".");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (loop == 4) {
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public void sendItemsOnDeath(Player killer, WorldTile tile) {
		charges.die();
		auraManager.removeAura();
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<>();
		for (int i = 0; i < 14; i++) {
			if (equipment.getItem(i) != null && equipment.getItem(i).getId() != -1
					&& equipment.getItem(i).getAmount() != -1) {
				containedItems.add(new Item(equipment.getItem(i).getId(), equipment.getItem(i).getAmount()));
			}
		}
		for (int i = 0; i < 28; i++) {
			if (inventory.getItem(i) != null && inventory.getItem(i).getId() != -1
					&& inventory.getItem(i).getAmount() != -1) {
				containedItems.add(new Item(getInventory().getItem(i).getId(), getInventory().getItem(i).getAmount()));
			}
		}
		if (containedItems.isEmpty()) {
			return;
		}
		int keptAmount = 0;
		if (!(controllerManager.getController() instanceof CorpBeastControler)
				&& !(controllerManager.getController() instanceof CrucibleControler)) {
			keptAmount = hasSkull() ? 0 : 3;
			if (prayer.usingPrayer(0, 10) || prayer.usingPrayer(1, 0)) {
				keptAmount++;
			}
		}
		if (donator && Utils.random(2) == 0) {
			keptAmount += 1;
		}
		CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<>();
		Item lastItem = new Item(1, 1);
		for (int i = 0; i < keptAmount; i++) {
			for (Item item : containedItems) {
				int price = item.getDefinitions().getValue();
				if (price >= lastItem.getDefinitions().getValue()) {
					lastItem = item;
				}
			}
			keptItems.add(lastItem);
			containedItems.remove(lastItem);
			lastItem = new Item(1, 1);
		}
		inventory.reset();
		equipment.reset();
		for (Item item : keptItems) {
			if (item.getId() == -1) {
				continue;
			}
			getInventory().addItemMoneyPouch(item);
		}

		for (Item item : containedItems) {
			World.addGroundItem(item, tile, killer, true, 600000);
		}
	}

	public void sendItemsOnDeath(Player killer, WorldTile deathTile, WorldTile respawnTile, boolean wilderness, Integer[][] slots) {
		// charges.die(slots[1], slots[3]);
		auraManager.removeAura();
		Item[][] items = GraveStone.getItemsKeptOnDeath(this, slots);
		inventory.reset();
		equipment.reset();
		appearence.generateAppearenceData();

		for (Item item : items[0]) {
			inventory.addItemDrop(item.getId(), item.getAmount(), respawnTile);
		}
		if (items[1].length != 0) {
			if (wilderness) {
				LogSystem.deathLogs(this, getSession().getLocalAddress(), getUsername(), new String(
						" died and lost the following items in the wilderness " + Arrays.toString(items[1]) + "."));
				for (Item item : items[1]) {
					World.addGroundItem(item, deathTile, killer == null ? this : killer, true, 600000, 0);
				}
			} else {
				LogSystem.deathLogs(this, getSession().getLocalAddress(), getUsername(),
						new String(" died and lost the following items " + Arrays.toString(items[1]) + "."));
				new GraveStone(this, deathTile, items[1]);
			}
		}
	}

	public int getGraveStone() {
		return gStone;
	}

	public void setGraveStone(int graveStone) {
		this.gStone = graveStone;
	}

	private String lastKilled;

	public void increaseKillCount(Player killed) {
		if (lastKilled == killed.getUsername()) {
			sm("You haven't been awarded any pk points for killing; " + killed.getDisplayName() + " twice.");
			return;
		}
		killed.deathCount++;
		PkRank.checkRank(killed);
		lastKilled = killed.getUsername();
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		killCount++;
		if (getEquipment().getHatId() == Wildstalker.getId()) {
			Wildstalker.sendPoints();
		}
		sm("<shad=000000><col=ff0000>You have been awarded pk points for killing: " + killed.getDisplayName() + ".");
		PkRank.checkRank(this);
	}

	public void increaseKillCountSafe(Player killed) {
		PkRank.checkRank(killed);
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		killCount++;
		getPackets().sendGameMessage(
				"<col=ff0000>You have killed " + killed.getDisplayName() + ", you have now " + killCount + " kills.");
		PkRank.checkRank(this);
	}

	public void sendRandomJail(Player p) {
		p.resetWalkSteps();
		switch (Utils.getRandom(6)) {
		case 0:
			p.setNextWorldTile(new WorldTile(2669, 10387, 0));
			break;
		case 1:
			p.setNextWorldTile(new WorldTile(2669, 10383, 0));
			break;
		case 2:
			p.setNextWorldTile(new WorldTile(2669, 10379, 0));
			break;
		case 3:
			p.setNextWorldTile(new WorldTile(2673, 10379, 0));
			break;
		case 4:
			p.setNextWorldTile(new WorldTile(2673, 10385, 0));
			break;
		case 5:
			p.setNextWorldTile(new WorldTile(2677, 10387, 0));
			break;
		case 6:
			p.setNextWorldTile(new WorldTile(2677, 10383, 0));
			break;
		}
	}

	@Override
	public int getSize() {
		return appearence.getSize();
	}

	public boolean isCanPvp() {
		if (isTradeLocked())
			return false;
		return canPvp;
	}

	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearence.generateAppearenceData();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		getPackets().sendPlayerUnderNPCPriority(canPvp);
	}

	public Prayer getPrayer() {
		return prayer;
	}

	private boolean acceptAid, profanityFilter;

	public boolean isAcceptingAid() {
		return acceptAid;
	}

	public boolean isFilteringProfanity() {
		return profanityFilter;
	}

	public void switchAcceptAid() {
		acceptAid = !acceptAid;
		refreshAcceptAid();
	}

	public void switchIronman() {
		Ironman = !Ironman;
	}

	public void switchProfanityFilter() {
		profanityFilter = !profanityFilter;
		refreshProfanityFilter();
	}

	public void refreshAcceptAid() {
		getPackets().sendConfig(427, acceptAid ? 1 : 0);
	}

	public void refreshProfanityFilter() {
		getPackets().sendConfig(1438, profanityFilter ? 31 : 32);
	}

	public long getLockDelay() {
		return lockDelay;
	}

	public boolean isLocked() {
		return lockDelay >= Utils.currentTimeMillis();
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}

	public void lock(long time) {
		lockDelay = Utils.currentTimeMillis() + (time * 600);
	}

	public void unlock() {
		lockDelay = 0;
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay, final String message) {
		stopAll();
		lock(totalDelay);
		if (emoteId != -1)
			setNextAnimation(new Animation(emoteId));
		if (useDelay == 0)
			setNextWorldTile(dest);
		else {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (isDead())
						return;
					setNextWorldTile(dest);
					if (message != null)
						getPackets().sendGameMessage(message);
				}
			}, useDelay - 1);
		}
	}

	public Bank getBank() {
		return bank;
	}

	public Bank getBankT() {
		return bank;
	}

	public ControllerManager getControllerManager() {
		return controllerManager;
	}

	public boolean isUsingReportOption() {
		return reportOption;
	}

	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}

	public void switchReportOption() {
		reportOption = !reportOption;
		refreshReportOption();
	}

	public void refreshReportOption() {
		getPackets().sendConfig(1056, isUsingReportOption() ? 2 : 0);
	}

	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}

	public void refreshAllowChatEffects() {
		getPackets().sendConfig(171, allowChatEffects ? 0 : 1);
	}

	public void refreshMouseButtons() {
		getPackets().sendConfig(170, mouseButtons ? 0 : 1);
	}

	public void refreshPrivateChatSetup() {
		getPackets().sendConfig(287, privateChatSetup);
	}

	private transient VarsManager varsManager;

	public VarsManager getVarsManager() {
		return varsManager;
	}

	public void refreshOtherChatsSetup() {
		int value = friendChatSetup << 6;
		getPackets().sendConfig(1438, value);
		refreshProfanityFilter();
		getVarBitManager().forceSendVarBit(3612, this.getClanChatSetup());
		getVarBitManager().forceSendVarBit(9188, this.getFriendChatSetup());
	}

	private int getClanChatSetup() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setPrivateChatSetup(int privateChatSetup) {
		this.privateChatSetup = privateChatSetup;
	}

	public void setFriendChatSetup(int friendChatSetup) {
		this.friendChatSetup = friendChatSetup;
	}

	private void refreshClanChatSetup() {
		getVarBitManager().forceSendVarBit(3612, this.getClanChatSetup());
	}

	private int getFriendChatSetup() {
		return friendChatSetup;
	}

	public int getPrivateChatSetup() {
		return privateChatSetup;
	}

	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}

	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}

	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}

	/*
	 * do not use this, only used by pm
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setDiscordName(String discordName) {
		this.discordName = discordName;
	}

	public void setRealDiscordName(String realDiscordName) {
		this.realDiscordName = realDiscordName;
	}

	public void addPotDelay(long time) {
		potDelay = time + Utils.currentTimeMillis();
	}

	public long getPotDelay() {
		return potDelay;
	}

	public void addFoodDelay(long time) {
		foodDelay = time + Utils.currentTimeMillis();
	}

	public long getFoodDelay() {
		return foodDelay;
	}

	public long getBoneDelay() {
		return boneDelay;
	}

	public void addBoneDelay(long time) {
		boneDelay = time + Utils.currentTimeMillis();
	}

	public void addPoisonImmune(long time) {
		poisonImmune = time + Utils.currentTimeMillis();
		getPoison().reset();
	}

	public long getPoisonImmune() {
		return poisonImmune;
	}

	public void addFireImmune(long time) {
		fireImmune = time + Utils.currentTimeMillis();
	}

	public long getFireImmune() {
		return fireImmune;
	}

	@Override
	public void heal(int ammount, int extra) {
		super.heal(ammount, extra);
		refreshHitPoints();
	}

	public MusicsManager getMusicsManager() {
		return musicsManager;
	}

	public HintIconsManager getHintIconsManager() {
		return hintIconsManager;
	}

	public boolean isCastVeng() {
		return castedVeng;
	}

	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}

	public int getKillCount() {
		return killCount;
	}

	public int setKillCount(int killCount) {
		return this.killCount = killCount;
	}

	public int getDeathCount() {
		return deathCount;
	}

	public int setDeathCount(int deathCount) {
		return this.deathCount = deathCount;
	}

	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}

	public long getMuted() {
		return muted;
	}

	public void setMuted(long muted) {
		this.muted = muted;
	}

	public long getJailed() {
		return jailed;
	}

	public void setJailed(long jailed) {
		this.jailed = jailed;
	}

	public boolean isPermBanned() {
		return permBanned;
	}

	public void setPermBanned(boolean permBanned) {
		this.permBanned = permBanned;
	}

	public long getBanned() {
		return banned;
	}

	public void setBanned(long banned) {
		this.banned = banned;
	}

	public ChargesManager getCharges() {
		return charges;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}

	public int getHiddenBrother() {
		return hiddenBrother;
	}

	public boolean isDonator() {
		return donationManager.isDonator() || donationManager.getTotalDonated() >= 10;
	}

	public boolean isExtremeDonator() {
		return donationManager.isExtremeDonator() || donationManager.getTotalDonated() >= 15;
	}

	public boolean isGraphicDesigner() {
		return isGraphicDesigner;
	}

	public boolean isCommunityManager() {
		return isCommunityManager;
	}

	public void setCommunityManager(boolean isCommunityManager) {
		this.isCommunityManager = isCommunityManager;
	}

	public boolean isForumModerator() {
		return isForumModerator;
	}

	public void setGraphicDesigner(boolean isGraphicDesigner) {
		this.isGraphicDesigner = isGraphicDesigner;
	}

	public void setForumModerator(boolean isForumModerator) {
		this.isForumModerator = isForumModerator;
	}

	@SuppressWarnings("deprecation")
	public void makeDonator(int months) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + months);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeDonatorDays(int days) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setDate(date.getDate() + days);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeExtremeDonatorDays(int days) {
		if (extremeDonatorTill < Utils.currentTimeMillis())
			extremeDonatorTill = Utils.currentTimeMillis();
		Date date = new Date(extremeDonatorTill);
		date.setDate(date.getDate() + days);
		extremeDonatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public String getDonatorTill() {
		return (donator ? "never" : new Date(donatorTill).toGMTString()) + ".";
	}

	@SuppressWarnings("deprecation")
	public String getExtremeDonatorTill() {
		return (extremeDonator ? "never" : new Date(extremeDonatorTill).toGMTString()) + ".";
	}

	public void setDonator(boolean donator) {
		this.donator = donator;
	}

	public String getRecovQuestion() {
		return recovQuestion;
	}

	public void setRecovQuestion(String recovQuestion) {
		this.recovQuestion = recovQuestion;
	}

	public String getRecovAnswer() {
		return recovAnswer;
	}

	public void setRecovAnswer(String recovAnswer) {
		this.recovAnswer = recovAnswer;
	}

	public String getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	public int[] getPouches() {
		return pouches;
	}

	public EmotesManager getEmotesManager() {
		return emotesManager;
	}

	public String getLastIP() {
		return lastIP;
	}

	public String getLastHostname() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(getLastIP());
			String hostname = addr.getHostName();
			return hostname;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PriceCheckManager getPriceCheckManager() {
		return priceCheckManager;
	}

	public boolean isUpdateMovementType() {
		return updateMovementType;
	}

	public long getLastPublicMessage() {
		return lastPublicMessage;
	}

	public void setLastPublicMessage(long lastPublicMessage) {
		this.lastPublicMessage = lastPublicMessage;
	}

	public CutscenesManager getCutscenesManager() {
		return cutscenesManager;
	}

	public void kickPlayerFromFriendsChannel(String name) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.kickPlayerFromChat(this, name);
	}

	public void sendFriendsChannelMessage(String message) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.sendMessage(this, message);
	}

	public void sendFriendsChannelQuickMessage(QuickChatMessage message) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.sendQuickMessage(this, message);
	}

	public void sendPublicChatMessage(PublicChatMessage message) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player p = World.getPlayers().get(playerIndex);
				if (p == null || !p.hasStarted() || p.hasFinished()
						|| p.getLocalPlayerUpdate().getLocalPlayers()[getIndex()] == null)
					continue;
				p.getPackets().sendPublicMessage(this, message);
			}
		}
	}

	public int[] getCompletionistCapeCustomized() {
		return completionistCapeCustomized;
	}

	public void setCompletionistCapeCustomized(int[] skillcapeCustomized) {
		this.completionistCapeCustomized = skillcapeCustomized;
	}

	public int[] getMaxedCapeCustomized() {
		return maxedCapeCustomized;
	}

	public void setMaxedCapeCustomized(int[] maxedCapeCustomized) {
		this.maxedCapeCustomized = maxedCapeCustomized;
	}

	public void setSkullId(int skullId) {
		this.skullId = skullId;
	}

	public int getSkullId() {
		return skullId;
	}

	public boolean isFilterGame() {
		return filterGame;
	}

	public void setFilterGame(boolean filterGame) {
		this.filterGame = filterGame;
	}

	public final static void removeControler(Player player) {
		player.getControllerManager().removeControllerWithoutCheck();
	}

	public boolean lumby, canfis, draynor, port, alkarid, varrock, falador, burth, tav, adrougne, cath, seers, yanille,
			edge;

	/**
	 * LodeStones.
	 */
	public boolean[] lodestone;

	public void activateLodeStone(final WorldObject object, final Player p) {
		lock(5);
		WorldTasksManager.schedule(new WorldTask() {
			int count = 0;

			@Override
			public void run() {
				if (count == 0) {
					lodestone[object.getId() - 69827] = true;
					refreshLodestoneNetwork();
				}
				if (count == 1) {
					unlock();
					stop();
				}
				count++;
			}
		}, 0, 1);
	}

	public void refreshLodestoneNetwork() {
		if (lodestone == null || lodestone[9] != true) {
			lodestone = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true, true,
					true, true };
		}
		getPackets().sendConfigByFile(358, lodestone[0] ? 15 : 14);
		getPackets().sendConfigByFile(2448, lodestone[1] ? 190 : 189);
		for (int i = 10900; i < 10913; i++) {
			getPackets().sendConfigByFile(i, lodestone[i - 10900 + 2] ? 1 : -1);
		}
	}

	public void addLogicPacketToQueue(LogicPacket packet) {
		for (LogicPacket p : logicPackets) {
			if (p.getId() == packet.getId()) {
				logicPackets.remove(p);
				break;
			}
		}
		logicPackets.add(packet);
	}

	public void setPrayerRenewalDelay(int delay) {
		this.prayerRenewalDelay = delay;
	}

	public int getOverloadDelay() {
		return overloadDelay;
	}

	public void setOverloadDelay(int overloadDelay) {
		this.overloadDelay = overloadDelay;
	}

	public Trade getTrade() {
		return trade;
	}

	public void setTeleBlockDelay(long teleDelay) {
		getTemporaryAttributtes().put("TeleBlocked", teleDelay + Utils.currentTimeMillis());
	}

	public long getTeleBlockDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("TeleBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public void setPrayerDelay(long teleDelay) {
		getTemporaryAttributtes().put("PrayerBlocked", teleDelay + Utils.currentTimeMillis());
		prayer.closeAllPrayers();
	}

	public long getPrayerDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("PrayerBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public FriendChatsManager getCurrentFriendChat() {
		return currentFriendChat;
	}

	public void setCurrentFriendChat(FriendChatsManager currentFriendChat) {
		this.currentFriendChat = currentFriendChat;
	}

	public String getCurrentFriendChatOwner() {
		return currentFriendChatOwner;
	}

	public void setCurrentFriendChatOwner(String currentFriendChatOwner) {
		this.currentFriendChatOwner = currentFriendChatOwner;
	}

	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	public boolean canSpawn() {
		if (Wilderness.isAtWild(this) || getControllerManager().getController() instanceof FightPitsArena
				|| getControllerManager().getController() instanceof CorpBeastControler
				|| getControllerManager().getController() instanceof PestControlLobby
				|| getControllerManager().getController() instanceof PestControlGame
				|| getControllerManager().getController() instanceof ZGDControler
				|| getControllerManager().getController() instanceof GodWars
				|| getControllerManager().getController() instanceof DTControler
				|| getControllerManager().getController() instanceof DuelArena
				|| getControllerManager().getController() instanceof CastleWarsPlaying
				|| getControllerManager().getController() instanceof CastleWarsWaiting
				|| getControllerManager().getController() instanceof FightCaves
				|| getControllerManager().getController() instanceof FightKiln
				|| getControllerManager().getController() instanceof HungerGamesControler
				|| getControllerManager().getController() instanceof BarrelchestControler || FfaZone.inPvpArea(this)
				|| getControllerManager().getController() instanceof NomadsRequiem
				|| getControllerManager().getController() instanceof QueenBlackDragonController
				|| getControllerManager().getController() instanceof WarControler) {
			return false;
		}
		if (getControllerManager().getController() instanceof CrucibleControler) {
			CrucibleControler controler = (CrucibleControler) getControllerManager().getController();
			return !controler.isInside();
		}
		return true;
	}

	public long getPolDelay() {
		return polDelay;
	}

	public void addPolDelay(long delay) {
		polDelay = delay + Utils.currentTimeMillis();
	}

	public void setPolDelay(long delay) {
		this.polDelay = delay;
	}

	// Toolbelt
	public Toolbelt toolbelt;

	public Toolbelt getToolbelt() {
		return toolbelt;
	}

	public ToolbeltNew toolBeltNew;

	public ToolbeltNew getToolBeltNew() {
		return toolBeltNew;
	}

	public List<Integer> getSwitchItemCache() {
		return switchItemCache;
	}

	public AuraManager getAuraManager() {
		return auraManager;
	}

	public SquealOfFortune getSquealOfFortune() {
		return squealOfFortune;
	}

	public SlayerManager getSlayerManager() {
		return slayerManager;
	}

	public Assassins getAssassinsManager() {
		return assassins;
	}

	public void setSpins(int spins) {
		this.spins = spins;
	}

	public int getSpins() {
		return spins;
	}

	public int getMovementType() {
		if (getTemporaryMoveType() != -1)
			return getTemporaryMoveType();
		return getRun() ? RUN_MOVE_TYPE : WALK_MOVE_TYPE;
	}

	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
			ownedObjectsManagerKeys = new LinkedList<String>();
		return ownedObjectsManagerKeys;
	}

	/**
	 * Slayer
	 */
	public boolean hasTask = false;
	protected SlayerManager slayerManager;
	private Assassins assassins;
	private int assassin;
	public int slayerTaskAmount = 0;

	// Slayer
	/**
	 * The slayer task system instance
	 */
	public SlayerTask slayerTask;

	private SquealOfFortune squealOfFortune;

	// Squeal
	public int spins;
	protected int freeSpins = 0;

	/**
	 * @param task the task to set
	 */
	public void setTask(SlayerTask task) {
		this.task = task;
	}

	public void setRTask(Contract rtask) {
		Rtask = rtask;
	}

	// reaper
	private ContractHandler cHandler;
	private Contract cContracts;

	/**
	 * @return the task
	 */
	public SlayerTask getTask() {
		return task;
	}

	public void setContract(Contract contract) {
		cContracts = contract;
	}

	public Contract getContract() {
		return cContracts;
	}

	public Reaper getReaperTasks() {
		return reaperTasks;
	}

	public void setReaperTasks(Reaper reaperTasks) {
		this.reaperTasks = reaperTasks;
	}

	private int ReaperPoints;

	public int getReaperPoints() {
		return ReaperPoints;
	}

	public void setReaperPoints(int ReaperPoints) {
		this.ReaperPoints = ReaperPoints;
	}

	private int totalkills;

	public int getTotalKills() {
		return totalkills;
	}

	public void setTotalKills(int totalkills) {
		this.totalkills = totalkills;
	}

	private int totalcontract;

	public int getTotalContract() {
		return totalcontract;
	}

	public void setTotalContract(int totalcontract) {
		this.totalcontract = totalcontract;
	}

	public int slayer;

	public int increaseSlayer() {
		slayer++;
		return slayer;
	}

	private int slayerPoints;

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public void setSlayerPoints(int slayerPoints) {
		this.slayerPoints = slayerPoints;
	}

	private boolean tradeLocked;

	public boolean isTradeLocked() {
		return tradeLocked;
	}

	public void setTradeLock() {
		tradeLocked = !tradeLocked;
	}

	public boolean hasInstantSpecial(final int weaponId) {
		switch (weaponId) {
		case 4153:
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		case 1377:
		case 13472:
		case 35:// Excalibur
		case 8280:
		case 14632:
			return true;
		default:
			return false;
		}
	}

	public void performInstantSpecial(final int weaponId) {
		int specAmt = PlayerCombat.getSpecialAmmount(weaponId);
		if (combatDefinitions.hasRingOfVigour())
			specAmt *= 0.9;
		if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
			getPackets().sendGameMessage("You don't have enough power left.");
			combatDefinitions.desecreaseSpecialAttack(0);
			return;
		}
		if (this.getSwitchItemCache().size() > 0) {
			ButtonHandler.submitSpecialRequest(this);
			return;
		}
		switch (weaponId) {
		case 4153:
			combatDefinitions.setInstantAttack(true);
			combatDefinitions.switchUsingSpecialAttack();
			Entity target = (Entity) getTemporaryAttributtes().get("last_target");
			if (target != null && target.getTemporaryAttributtes().get("last_attacker") == this) {
				if (!(getActionManager().getAction() instanceof PlayerCombat)
						|| ((PlayerCombat) getActionManager().getAction()).getTarget() != target) {
					getActionManager().setAction(new PlayerCombat(target));
				}
			}
			break;
		case 1377:
		case 13472:
			setNextAnimation(new Animation(1056));
			setNextGraphics(new Graphics(246));
			setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
			int defence = (int) (skills.getLevelForXp(Skills.DEFENCE) * 0.90D);
			int attack = (int) (skills.getLevelForXp(Skills.ATTACK) * 0.90D);
			int range = (int) (skills.getLevelForXp(Skills.RANGE) * 0.90D);
			int magic = (int) (skills.getLevelForXp(Skills.MAGIC) * 0.90D);
			int strength = (int) (skills.getLevelForXp(Skills.STRENGTH) * 1.2D);
			skills.set(Skills.DEFENCE, defence);
			skills.set(Skills.ATTACK, attack);
			skills.set(Skills.RANGE, range);
			skills.set(Skills.MAGIC, magic);
			skills.set(Skills.STRENGTH, strength);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 35:// Excalibur
		case 8280:
		case 14632:
			setNextAnimation(new Animation(1168));
			setNextGraphics(new Graphics(247));
			setNextForceTalk(new ForceTalk("For Matrix!"));
			final boolean enhanced = weaponId == 14632;
			skills.set(Skills.DEFENCE, enhanced ? (int) (skills.getLevelForXp(Skills.DEFENCE) * 1.15D)
					: (skills.getLevel(Skills.DEFENCE) + 8));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 5;

				@Override
				public void run() {
					if (isDead() || hasFinished() || getHitpoints() >= getMaxHitpoints()) {
						stop();
						return;
					}
					heal(enhanced ? 80 : 40);
					if (count-- == 0) {
						stop();
						return;
					}
				}
			}, 4, 2);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			setNextAnimation(new Animation(12804));
			setNextGraphics(new Graphics(2319));// 2320
			setNextGraphics(new Graphics(2321));
			addPolDelay(60000);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		}
	}

	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}

	public boolean isEquipDisabled() {
		return disableEquip;
	}

	public void addDisplayTime(long i) {
		this.displayTime = i + Utils.currentTimeMillis();
	}

	public long getDisplayTime() {
		return displayTime;
	}

	public int getPublicStatus() {
		return publicStatus;
	}

	public void setPublicStatus(int publicStatus) {
		this.publicStatus = publicStatus;
	}

	public int getClanStatus() {
		return clanStatus;
	}

	public void setClanStatus(int clanStatus) {
		this.clanStatus = clanStatus;
	}

	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public int getAssistStatus() {
		return assistStatus;
	}

	public void setAssistStatus(int assistStatus) {
		this.assistStatus = assistStatus;
	}

	public boolean isSpawnsMode() {
		return spawnsMode;
	}

	public void setSpawnsMode(boolean spawnsMode) {
		this.spawnsMode = spawnsMode;
	}

	public IsaacKeyPair getIsaacKeyPair() {
		return isaacKeyPair;
	}

	public QuestManager getQuestManager() {
		return questManager;
	}

	public boolean isCompletedFightCaves() {
		return completedFightCaves;
	}

	public void setCompletedFightCaves() {
		if (!completedFightCaves) {
			completedFightCaves = true;
			refreshFightKilnEntrance();
		}
	}

	public boolean isCompletedFightKiln() {
		return completedFightKiln;
	}

	public void setCompletedFightKiln() {
		completedFightKiln = true;
	}

	public boolean isWonFightPits() {
		return wonFightPits;
	}

	public void setWonFightPits() {
		wonFightPits = true;
	}

	public boolean isCantTrade() {
		return cantTrade;
	}

	public DonationManager getDonationManager() {
		return donationManager;
	}

	public void setCantTrade(boolean canTrade) {
		this.cantTrade = canTrade;
	}

	public String getYellColor() {
		return yellColor;
	}

	public void setYellColor(String yellColor) {
		this.yellColor = yellColor;
	}

	private int ColorID;

	public void setColorID(int ColorID) {
		this.ColorID = ColorID;
	}

	public int getColorID() {
		return ColorID;
	}

	/**
	 * Gets the pet.
	 * 
	 * @return The pet.
	 */
	public Pet getPet() {
		return pet;
	}

	/**
	 * Sets the pet.
	 * 
	 * @param pet The pet to set.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public boolean isSupporter() {
		return isSupporter;
	}

	public void setSupporter(boolean isSupporter) {
		this.isSupporter = isSupporter;
	}

	/*
	 * Max Hit
	 */
	public int maxHitMelee;
	public int maxHitMagic;
	public int maxHitRanged;

	/**
	 * Game Modes System
	 */
	public int gameMode = 0;
	public int bandos = 0;
	public int saradomin = 0;
	public int armadyl = 0;
	public int zamorak = 0;

	public boolean choseGameMode;

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public int getGameMode() {
		return gameMode;
	}

	private int maxBotWave, botKillStreak;

	public int botKills;
	private int botPoints;

	public int getBotKills() {
		return botKills;
	}

	public void setBotKills(int value) {
		this.botKills = value;
	}

	public int getBotKillstreak() {
		return botKillStreak;
	}

	public void setBotKillstreak(int value) {
		this.botKillStreak = value;
	}

	public int getBP() {
		return botPoints;
	}

	public void setBP(int amount) {
		this.botPoints = amount;
	}

	public int getMaxBotWave() {
		return maxBotWave;
	}

	public void setMaxBotWave(int value) {
		this.maxBotWave = value;
	}

	/**
	 * RFD
	 */
	private boolean culinaromancer;

	public boolean isKilledCulinaromancer() {
		return culinaromancer;
	}

	private boolean dessourt;

	public boolean isKilledDessourt() {
		return dessourt;
	}

	private boolean flamBeed;

	public boolean isKilledFlambeed() {
		return flamBeed;
	}

	private boolean karamel;

	public boolean isKilledKaramel() {
		return karamel;
	}

	private boolean agrithNaNa;

	public boolean isKilledAgrithNaNa() {
		return agrithNaNa;
	}

	public void setKilledAgrithNaNa(boolean agrithNaNa) {
		this.agrithNaNa = agrithNaNa;
	}

	public void setKilledCulinaromancer(boolean culinaromancer) {
		this.culinaromancer = culinaromancer;
	}

	public void setKilledDessourt(boolean dessourt) {
		this.dessourt = dessourt;
	}

	public void setKilledFlamBeed(boolean flamBeed) {
		this.flamBeed = flamBeed;
	}

	public void setKilledKaramel(boolean karamel) {
		this.karamel = karamel;
	}

	private boolean talkedtoCook;

	public boolean hasTalkedtoCook() {
		return talkedtoCook;
	}

	public void setTalkedToCook() {
		talkedtoCook = true;
	}

	/**
	 * Gets the petManager.
	 * 
	 * @return The petManager.
	 */
	public PetManager getPetManager() {
		return petManager;
	}

	/**
	 * Sets the petManager.
	 * 
	 * @param petManager The petManager to set.
	 */
	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public boolean isXpLocked() {
		return xpLocked;
	}

	public void setXpLocked(boolean locked) {
		this.xpLocked = locked;
	}

	public int getLastBonfire() {
		return lastBonfire;
	}

	public void setLastBonfire(int lastBonfire) {
		this.lastBonfire = lastBonfire;
	}

	public boolean isYellOff() {
		return yellOff;
	}

	public void setYellOff(boolean yellOff) {
		this.yellOff = yellOff;
	}

	public boolean isWorldMessageOff() {
		return worldMessageOff;
	}

	public void setWorldMessageOff(boolean worldMessageOff) {
		this.worldMessageOff = worldMessageOff;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	public double getHpBoostMultiplier() {
		return hpBoostMultiplier;
	}

	public void setHpBoostMultiplier(double hpBoostMultiplier) {
		this.hpBoostMultiplier = hpBoostMultiplier;
	}

	/**
	 * Gets the killedQueenBlackDragon.
	 * 
	 * @return The killedQueenBlackDragon.
	 */
	public boolean isKilledQueenBlackDragon() {
		return killedQueenBlackDragon;
	}

	/**
	 * Sets the killedQueenBlackDragon.
	 * 
	 * @param killedQueenBlackDragon The killedQueenBlackDragon to set.
	 */
	public void setKilledQueenBlackDragon(boolean killedQueenBlackDragon) {
		this.killedQueenBlackDragon = killedQueenBlackDragon;
	}

	public boolean hasLargeSceneView() {
		return largeSceneView;
	}

	public void setLargeSceneView(boolean largeSceneView) {
		this.largeSceneView = largeSceneView;
	}

	public boolean isOldItemsLook() {
		return oldItemsLook;
	}

	public void switchItemsLook() {
		oldItemsLook = !oldItemsLook;
		getPackets().sendItemsLook();
	}

	/**
	 * Duel anywhere
	 */
	/**
	 * On Spot Duelling
	 */
	public boolean onSpotDuelingRequest;

	public boolean isOnSpotDuelingRequest() {
		return onSpotDuelingRequest;
	}

	public void setOnSpotDuelingRequest(boolean onSpotDuelingRequest) {
		this.onSpotDuelingRequest = onSpotDuelingRequest;
	}

	public boolean onSpotDueling;

	public boolean isOnSpotDueling() {
		return onSpotDueling;
	}

	public void setOnSpotDueling(boolean onSpotDueling) {
		this.onSpotDueling = onSpotDueling;
	}

	WorldTile duelLocation;

	public WorldTile getDuelLocation() {
		return duelLocation;
	}

	public void setDuelLocation(WorldTile duelLocation) {
		this.duelLocation = duelLocation;
	}

	public boolean withinArea(int a, int b, int c, int d) {
		return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
	}

	/**
	 * @return the runeSpanPoint
	 */
	public int getRuneSpanPoints() {
		return runeSpanPoints;
	}

	/**
	 * @param runeSpanPoint the runeSpanPoint to set
	 */
	public void setRuneSpanPoint(int runeSpanPoints) {
		this.runeSpanPoints = runeSpanPoints;
	}

	/**
	 * Adds points
	 * 
	 * @param points
	 */
	public void addRunespanPoints(int points) {
		this.runeSpanPoints += points;
	}

	public DuelRules getLastDuelRules() {
		return lastDuelRules;
	}

	public void setLastDuelRules(DuelRules duelRules) {
		this.lastDuelRules = duelRules;
	}

	private long shopVault;

	public long getVault() {
		return shopVault;
	}

	public void setVault(long value) {
		this.shopVault = value;
	}

	public boolean isTalkedWithMarv() {
		return talkedWithMarv;
	}

	public void setTalkedWithMarv() {
		talkedWithMarv = true;
	}

	public int getCrucibleHighScore() {
		return crucibleHighScore;
	}

	public void increaseCrucibleHighScore() {
		crucibleHighScore++;
	}

	public void setVoted(long ms) {
		voted = ms + Utils.currentTimeMillis();
	}

	public boolean hasVoted() {
		// disabled so that donators vote for the first 2 days of list reset ^^
		return isDonator() || voted > Utils.currentTimeMillis();
	}

	public int getLastLoggedIn() {
		return lastlogged;
	}

	public FadingScreen getFadingScreen() {
		return fadingScreen;
	}

	public Object getTemporaryAttribute(String string) {
		return null;
	}

	public int getFirstColumn() {
		return this.firstColumn;
	}

	public int getSecondColumn() {
		return this.secondColumn;
	}

	public int getThirdColumn() {
		return this.thirdColumn;
	}

	public void setFirstColumn(int i) {
		this.firstColumn = i;
	}

	public WorldTile getTile() {
		return new WorldTile(getX(), getY(), getPlane());
	}

	public void setOutside(WorldTile getoutside) {
		this.getoutside = getoutside;
	}

	public boolean isOutside() {
		return outside;
	}

	public void setOutside(boolean outside) {
		this.outside = outside;
	}

	public WorldTile getOutside() {
		return getoutside;
	}

	public void setSecondColumn(int i) {
		this.secondColumn = i;
	}

	public void setThirdColumn(int i) {
		this.thirdColumn = i;
	}

	public Options ops;

	public Options getOptions() {
		return ops;

	}

	public static String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount).toString();
	}

	public boolean hasPin;
	public boolean enteredPin;
	public int bankpin;

	public int getPin() {
		return bankpin;
	}

	public void setPin(int bankpin) {
		this.bankpin = bankpin;
	}

	private long dfsDelay;

	public long getDfsDelay() {
		return dfsDelay;
	}

	public void setDfsDelay(long dfsDelay) {
		this.dfsDelay = dfsDelay;
	}

	public void out(String text) {
		getPackets().sendGameMessage(text);
	}

	public void sendMessage(String message) {
		getPackets().sendGameMessage(message);
	}

	public void sm(String text) {
		getPackets().sendGameMessage(text);
	}

	/**
	 * Pest Control Game
	 */
	public int lander = 0;
	public int pestControlPoint = 0;
	private int pestPoints;
	private transient InputEvent<?> inputEvent;
	private int pestControlGames;

	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	public void setPestControlGames(int pestControlGames) {
		this.pestControlGames = pestControlGames;
	}

	public int getPestPoints() {
		return pestPoints;
	}

	public int getPestControlGames() {
		return pestControlGames;
	}

	/**
	 * Warrior Guild Points
	 */
	public int warriorsPoints;

	public void setwarriorsPoints(int warriorsPoints) {
		this.warriorsPoints = warriorsPoints;
	}

	public int getwarriorsPoints() {
		return warriorsPoints;
	}

	/**
	 * hunger related stuff..
	 */
	public boolean Hunger1;

	public int hungerNumber;

	public int hungerPoints;

	public int getHungerPoints() {
		return hungerPoints;
	}

	public void setHungerPoints(int hungerPoints) {
		this.hungerPoints = hungerPoints;
	}

	public boolean isHunger1() {
		return Hunger1;
	}

	public void hungerShops() {
		if (hungerNumber == 0) {
			getPackets().sendGameMessage("You must have a Hunger level of at least 5 to access the store.");
		} else if (hungerNumber == 5) {
			ShopsHandler.openShop(this, 69);
		}
	}

	public void setHunger1() {
		if (!Hunger1) {
			Hunger1 = true;
		}
	}

	// Player Informs
	public boolean informwelcome;
	public boolean informmessages;
	public boolean inform99s = true;
	public transient String instanceDialogue;

	// Comp cape
	public static int isMaxed1 = 1;
	public static int isCompletionist1 = 1;
	// Completionist Cape
	public int isCompletionist = 1;
	public int isMaxed = 1;
	/**
	 * Penguin
	 */
	public boolean penguin;
	public int penguinpoints;

	/**
	 * Max receiver, after getting maxed in combat.
	 */
	public boolean hasMaxCape = false;

	public void recieveMaxCape() {
		if (getSkills().getLevel(0) >= 99 && getSkills().getLevel(1) >= 99 && getSkills().getLevel(2) >= 99
				&& getSkills().getLevel(3) >= 99 && getSkills().getLevel(4) >= 99 && getSkills().getLevel(5) >= 99
				&& getSkills().getLevel(6) >= 99 && hasMaxCape == false) {
			getBank().addItem(20767, 1, true);
			getBank().addItem(20768, 1, true);
			sm("<col=008000>Congratulations, your Max Cape and Hood awaits you in your bank!");
			hasMaxCape = true;
		}
	}

	public int SkillPoints;

	public int getSkillPoints() {
		return SkillPoints;
	}

	public void setSkillPoints(int SkillPoints) {
		this.SkillPoints = SkillPoints;
	}

	public boolean isOwner() {
		for (String owner : Settings.OWNER) {
			if (getUsername().equalsIgnoreCase(owner)) {
				return true;
			}
		}
		for (String coOwner : Settings.CO_OWNER) {
			if (getUsername().equalsIgnoreCase(coOwner)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDeveloper() {
		for (String developer : Settings.DEVELOPER) {
			if (getUsername().equalsIgnoreCase(developer) || isOwner()) {
				return true;
			}
		}
		return false;
	}

	public boolean isAdministrator() {
		if (isOwner() || getRights() >= 2) {
			return true;
		}
		for (String cm : Settings.ADMINISTRATOR) {
			if (getUsername().equalsIgnoreCase(cm)) {
				return true;
			}
		}
		return false;
	}

	public boolean isModerator() {
		for (String mod : Settings.MODERATOR) {
			if (getUsername().equalsIgnoreCase(mod) || getRights() == 1) {
				return true;
			}
		}
		return false;
	}

	public void checkMovement(int x, int y, int plane) {
		Magic.teleControllerCheck(this, new WorldTile(x, y, plane));
	}

	public int checkTotalLevel(int checktotal) {
		checktotal = 0;
		for (int i = 0; i < 25; i++) {
			checktotal += getSkills().getLevel(i);
		}
		return checktotal;
	}

	private boolean capturedCastleWarsFlag;

	/**
	 * NPC Drops
	 */

	private int dropSortingType = 0;

	public int getDropSortingType() {
		return dropSortingType;
	}

	public void setDropSortingType(int dropSortingType) {
		this.dropSortingType = dropSortingType;
	}

	public boolean isToggleLootshare() {
		return toogleLootShare;
	}

	public void toggleLootshare() {
		toogleLootShare = !toogleLootShare;
		refreshToggleLootShare();
	}

	public void refreshToggleLootShare() {
		getPackets().sendConfig(1083, toogleLootShare ? 1 : 0);
	}

	private boolean toggleDrops;

	public boolean isToggleDrops() {
		return toggleDrops;
	}

	private boolean toggleKillcount;

	public boolean isToggleKillcount() {
		return toggleKillcount;
	}

	public void setToggleHealthBar(boolean toggleHealthBar) {
		this.toggleHealthBar = toggleHealthBar;
	}

	private boolean toggleHealthBar;

	public boolean isToggleHealthBar() {
		return toggleHealthBar;
	}

	/**
	 * Kill-statistics.
	 */
	int[] killStats = new int[512];

	/**
	 * Increases the statistics.
	 * 
	 * @param name The NPC name.
	 */
	public int increaseKillStatistics(String name, boolean add) {
		switch (name.toLowerCase()) {
		case "rock crab":
			if (add) {
				killStats[0]++;
			}
			return getKillStatistics(0);
		case "general graardor":
			if (add) {
				killStats[1]++;
			}
			return getKillStatistics(1);
		case "k'ril tsutsaroth":
			if (add) {
				killStats[2]++;
			}
			return getKillStatistics(2);
		case "kree'arra":
			if (add) {
				killStats[3]++;
			}
			return getKillStatistics(3);
		case "commander zilyana":
			if (add) {
				killStats[4]++;
			}
			return getKillStatistics(4);
		case "nex":
			if (add) {
				killStats[5]++;
			}
			return getKillStatistics(5);
		case "corporeal beast":
			if (add) {
				killStats[6]++;
			}
			return getKillStatistics(6);
		case "queen black dragon":
			if (add) {
				killStats[7]++;
			}
			return getKillStatistics(7);
		case "king black dragon":
			if (add) {
				killStats[8]++;
			}
			return getKillStatistics(8);
		case "bork":
			if (add) {
				killStats[9]++;
			}
			return getKillStatistics(9);
		case "chaos elemental":
			if (add) {
				killStats[10]++;
			}
			return getKillStatistics(10);
		case "crawling Hand":
			if (add) {
				killStats[11] += 1;
			}
			return getKillStatistics(11);
		case "abyssal demon":
			if (add) {
				killStats[12] += 1;
			}
			return getKillStatistics(12);
		case "ice strykewyrm":
			if (add) {
				killStats[13] += 1;
			}
			return getKillStatistics(13);
		case "jungle strykewyrm":
			if (add) {
				killStats[14] += 1;
			}
			return getKillStatistics(14);
		case "desert strykewyrm":
			if (add) {
				killStats[15] += 1;
			}
			return getKillStatistics(15);
		case "nechryael":
			if (add) {
				killStats[16] += 1;
			}
			return getKillStatistics(16);
		case "aberrant spectre":
			if (add) {
				killStats[17] += 1;
			}
			return getKillStatistics(17);
		case "hellhound":
			if (add) {
				killStats[18] += 1;
			}
			return getKillStatistics(18);
		case "mature grotworm":
			if (add) {
				killStats[19] += 1;
			}
			return getKillStatistics(19);
		case "tztok-jad":
			if (add) {
				killStats[20] += 1;
			}
			return getKillStatistics(20);
		case "greater demon":
			if (add) {
				killStats[21] += 1;
			}
			return getKillStatistics(21);
		case "mutated jadinko baby":
			if (add) {
				killStats[22] += 1;
			}
			return getKillStatistics(22);
		case "mutated jadinko male":
			if (add) {
				killStats[23] += 1;
			}
			return getKillStatistics(23);
		case "mutated jadinko guard":
			if (add) {
				killStats[24] += 1;
			}
			return getKillStatistics(24);
		case "blue dragon":
			if (add) {
				killStats[25] += 1;
			}
			return getKillStatistics(25);
		case "iron dragon":
			if (add) {
				killStats[26] += 1;
			}
			return getKillStatistics(26);
		case "steel dragon":
			if (add) {
				killStats[27] += 1;
			}
			return getKillStatistics(27);
		case "frost dragon":
			if (add) {
				killStats[28] += 1;
			}
			return getKillStatistics(28);
		case "glacor":
			if (add) {
				killStats[29] += 1;
			}
			return getKillStatistics(29);
		case "infernal mage":
			if (add) {
				killStats[30] += 1;
			}
			return getKillStatistics(30);
		case "ganodermic beast":
			if (add) {
				killStats[31] += 1;
			}
			return getKillStatistics(31);
		case "gargoyle":
			if (add) {
				killStats[32] += 1;
			}
			return getKillStatistics(32);
		case "jelly":
			if (add) {
				killStats[33] += 1;
			}
			return getKillStatistics(33);
		case "dark beast":
			if (add) {
				killStats[34] += 1;
			}
			return getKillStatistics(34);
		case "bloodveld":
			if (add) {
				killStats[35] += 1;
			}
			return getKillStatistics(35);
		case "black guard":
			if (add) {
				killStats[36] += 1;
			}
			return getKillStatistics(36);
		case "chaos dwarf hand cannoneer":
			if (add) {
				killStats[37] += 1;
			}
			return getKillStatistics(37);
		case "chaos dwogre":
			if (add) {
				killStats[38] += 1;
			}
			return getKillStatistics(38);
		case "pyrefiend":
			if (add) {
				killStats[39] += 1;
			}
			return getKillStatistics(39);
		case "cockatrice":
			if (add) {
				killStats[40] += 1;
			}
			return getKillStatistics(40);
		case "brutal green dragon":
		case "green dragon":
			if (add) {
				killStats[41] += 1;
			}
			return getKillStatistics(41);
		case "fungal rodent":
			if (add) {
				killStats[42] += 1;
			}
			return getKillStatistics(42);
		case "grifolaroo":
			if (add) {
				killStats[43] += 1;
			}
			return getKillStatistics(43);
		case "grifolapine":
			if (add) {
				killStats[44] += 1;
			}
			return getKillStatistics(44);
		case "mithril dragon":
			if (add) {
				killStats[45] += 1;
			}
			return getKillStatistics(45);
		case "bronze dragon":
			if (add) {
				killStats[46] += 1;
			}
			return getKillStatistics(46);
		case "moss giant":
			if (add) {
				killStats[47] += 1;
			}
			return getKillStatistics(47);
		case "fire giant":
			if (add) {
				killStats[48] += 1;
			}
			return getKillStatistics(48);
		case "hill giant":
			if (add) {
				killStats[49] += 1;
			}
			return getKillStatistics(49);
		case "turoth":
			if (add) {
				killStats[50] += 1;
			}
			return getKillStatistics(50);
		case "basilisk":
			if (add) {
				killStats[51] += 1;
			}
			return getKillStatistics(51);
		case "kurask":
			if (add) {
				killStats[52] += 1;
			}
			return getKillStatistics(52);
		case "black demon":
			if (add) {
				killStats[53] += 1;
			}
			return getKillStatistics(53);
		case "kalphite queen":
			if (add) {
				killStats[54] += 1;
			}
			return getKillStatistics(54);
		case "tormented demon":
			if (add) {
				killStats[55] += 1;
			}
			return getKillStatistics(55);
		case "baby blue dragon":
			if (add) {
				killStats[56] += 1;
			}
			return getKillStatistics(56);
		case "lesser demon":
			if (add) {
				killStats[57] += 1;
			}
			return getKillStatistics(57);
		case "skeleton":
			if (add) {
				killStats[58] += 1;
			}
			return getKillStatistics(58);
		case "man":
		case "farmer":
		case "woman":
			if (add) {
				killStats[59] += 1;
			}
			return getKillStatistics(59);
		case "waterfiend":
			if (add) {
				killStats[60] += 1;
			}
			return getKillStatistics(60);
		case "banshee":
			if (add) {
				killStats[61] += 1;
			}
			return getKillStatistics(61);
		case "dog":
		case "terror dog":
		case "wild dog":
			if (add) {
				killStats[62] += 1;
			}
			return getKillStatistics(62);
		case "cave crawler":
			if (add) {
				killStats[63] += 1;
			}
			return getKillStatistics(63);
		case "black dragon":
			if (add) {
				killStats[64] += 1;
			}
			return getKillStatistics(64);
		case "chaos druid":
			if (add) {
				killStats[65] += 1;
			}
			return getKillStatistics(65);
		case "black knight":
			if (add) {
				killStats[66] += 1;
			}
			return getKillStatistics(66);
		case "barrelchest":
			if (add) {
				killStats[67] += 1;
			}
			return getKillStatistics(67);
		case "dagannoth supreme":
			if (add) {
				killStats[68] += 1;
			}
			return getKillStatistics(68);
		case "dagannoth prime":
			if (add) {
				killStats[69] += 1;
			}
			return getKillStatistics(69);
		case "dagannoth rex":
			if (add) {
				killStats[70] += 1;
			}
			return getKillStatistics(70);
		case "araxxor":
			if (add) {
				killStats[71] += 1;
			}
			return getKillStatistics(71);
		case "vampyre":
			if (add) {
				killStats[72] += 1;
			}
			return getKillStatistics(72);
		case "werewolf":
			if (add) {
				killStats[73] += 1;
			}
			return getKillStatistics(73);
		case "goblin":
		case "hobgoblin":
			if (add) {
				killStats[74] += 1;
			}
			return getKillStatistics(74);
		case "imp":
			if (add) {
				killStats[75] += 1;
			}
			return getKillStatistics(75);
		case "icefiend":
			if (add) {
				killStats[76] += 1;
			}
			return getKillStatistics(76);
		case "ogre":
			if (add) {
				killStats[77] += 1;
			}
			return getKillStatistics(77);
		case "cyclops":
			if (add) {
				killStats[78] += 1;
			}
			return getKillStatistics(78);
		case "rorarius":
			if (add) {
				killStats[79] += 1;
			}
			return getKillStatistics(79);
		case "gladius":
			if (add) {
				killStats[80] += 1;
			}
			return getKillStatistics(80);
		case "capsarius":
			if (add) {
				killStats[81] += 1;
			}
			return getKillStatistics(81);
		case "scutarius":
			if (add) {
				killStats[82] += 1;
			}
			return getKillStatistics(82);
		case "legio primus":
			if (add) {
				killStats[83] += 1;
			}
			return getKillStatistics(83);
		case "legio secundus":
			if (add) {
				killStats[84] += 1;
			}
			return getKillStatistics(84);
		case "legio tertius":
			if (add) {
				killStats[85] += 1;
			}
			return getKillStatistics(85);
		case "legio quartus":
			if (add) {
				killStats[86] += 1;
			}
			return getKillStatistics(86);
		case "legio quintus":
			if (add) {
				killStats[87] += 1;
			}
			return getKillStatistics(87);
		case "legio sextus":
			if (add) {
				killStats[88] += 1;
			}
			return getKillStatistics(88);
		case "Giant Mole":
			if (add) {
				killStats[89] += 1;
			}
			return getKillStatistics(89);
		case "kalphite king":
			if (add) {
				killStats[90] += 1;
			}
			return getKillStatistics(90);
		case "ork":
			if (add) {
				killStats[91] += 1;
			}
			return getKillStatistics(91);
		case "aviansie":
			if (add) {
				killStats[92] += 1;
			}
			return getKillStatistics(92);
		case "vorago":
			if (add) {
				killStats[93] += 1;
			}
			return getKillStatistics(93);
		case "adamant dragon":
			if (add) {
				killStats[94] += 1;
			}
			return getKillStatistics(94);
		case "rune dragon":
			if (add) {
				killStats[95] += 1;
			}
			return getKillStatistics(95);
		case "edimmu":
			if (add) {
				killStats[96] += 1;
			}
			return getKillStatistics(96);
		case "nightmare":
			if (add) {
				killStats[97] += 1;
			}
			return getKillStatistics(97);
		case "sergeant strongstack":
			if (add) {
				killStats[98] += 1;
			}
			return getKillStatistics(98);
		case "sergeant steelwill":
			if (add) {
				killStats[99] += 1;
			}
			return getKillStatistics(99);
		case "sergeant grimspike":
			if (add) {
				killStats[100] += 1;
			}
			return getKillStatistics(100);
		}
		return -1;
	}

	/**
	 * Gets the kill statistics.
	 * 
	 * @param i The NPC id.
	 * @return the Statistic.
	 */
	public int getKillStatistics(int i) {
		return killStats[i];
	}

	public void setToggleKillcount(boolean toggleKillcount) {
		this.toggleKillcount = toggleKillcount;
	}

	public void setToggleDrops(boolean toggleDrops) {
		this.toggleDrops = toggleDrops;
	}

	private boolean toggleItems;

	public boolean isToggleItems() {
		return toggleItems;
	}

	public void setToggleItems(boolean toggleItems) {
		this.toggleItems = toggleItems;
	}

	private boolean lootBeams;

	public boolean isToggleLootBeams() {
		return lootBeams;
	}

	public void setToggleLootBeams(boolean lootBeams) {
		this.lootBeams = lootBeams;
	}

	private ItemsContainer<Item> npcDrops = new ItemsContainer<Item>(31, false);

	public ItemsContainer<Item> getNpcDrops() {
		return npcDrops;
	}

	public void setNpcDrops(ItemsContainer<Item> npcDrops) {
		this.npcDrops = npcDrops;
	}

	public boolean isCapturedCastleWarsFlag() {
		return capturedCastleWarsFlag;
	}

	public void setCapturedCastleWarsFlag() {
		capturedCastleWarsFlag = true;
	}

	public void increaseFinishedCastleWars() {
	}

	public void sendSimpleDialogue(Object... parameters) {
		getDialogueManager().startDialogue("SimpleMessage", parameters);
	}

	public int getBandos() {
		return bandos;
	}

	public void setBandos(int bandos) {
		this.bandos = bandos;
	}

	public int getZamorak() {
		return zamorak;
	}

	public void setZamorak(int zamorak) {
		this.zamorak = zamorak;
	}

	public int getSaradomin() {
		return saradomin;
	}

	public void setSaradomin(int saradomin) {
		this.saradomin = saradomin;
	}

	public int getArmadyl() {
		return armadyl;
	}

	public void setArmadyl(int armadyl) {
		this.armadyl = armadyl;
	}

	private byte frozenKeyCharges;

	public byte getFrozenKeyCharges() {
		return frozenKeyCharges;
	}

	public void setFrozenKeyCharges(byte charges) {
		frozenKeyCharges = charges;
	}

	public void setSpawnRate(int spawnrate) {
		this.spawnrate = spawnrate;
	}

	public int getSpawnRate() {
		return spawnrate;
	}

	public void setInstanceKick(boolean isInstanceKicked) {
		this.isInstanceKicked = isInstanceKicked;
	}

	public void setInstancePin(int instancepin) {
		this.instancepin = instancepin;
	}

	public int getInstancePin() {
		return instancepin;
	}

	public boolean getInstanceKick() {
		return isInstanceKicked;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getTimer() {
		return timer;
	}

	public void setDestroyTimer(int destroytimer) {
		this.destroytimer = destroytimer;
	}

	public int getDestroyTimer() {
		return destroytimer;
	}

	public void setInstanceEnd(boolean instanceend) {
		this.instanceend = instanceend;
	}

	public boolean getEnd() {
		return instanceend;
	}

	public void setPermaBandos(boolean permabandos) {
		this.permabandos = permabandos;
	}

	public String getInstanceControler() {
		return instanceControler;
	}

	public void setInstanceControler(String instanceControler) {
		this.instanceControler = instanceControler;
	}

	public boolean isPermaBank() {
		return permabank;
	}

	public void setPermaBank(boolean permabank) {
		this.permabank = permabank;
	}

	public boolean getPermaBandos() {
		return permabandos;
	}

	public void setPermaArmadyl(boolean permaarmadyl) {
		this.permaarmadyl = permaarmadyl;
	}

	public boolean getPermaArmadyl() {
		return permaarmadyl;
	}

	public void setPermaSaradomin(boolean permasaradomin) {
		this.permasaradomin = permasaradomin;
	}

	public boolean getPermaSaradomin() {
		return permasaradomin;
	}

	public void setPermaZamorak(boolean permazamorak) {
		this.permazamorak = permazamorak;
	}

	public boolean getPermaZamorak() {
		return permazamorak;
	}

	public void setPermaBlink(boolean permablink) {
		this.permablink = permablink;
	}

	public boolean getPermaBlink() {
		return permablink;
	}

	public void setPermaEradicator(boolean permaeradicator) {
		this.permaeradicator = permaeradicator;
	}

	public boolean getPermaEradicator() {
		return permaeradicator;
	}

	public void setPermaTrio(boolean permatrio) {
		this.permatrio = permatrio;
	}

	public boolean getPermaTrio() {
		return permatrio;
	}

	public void setPermaCorp(boolean permacorp) {
		this.permacorp = permacorp;
	}

	public void setPermaKQ(boolean permakq) {
		this.permakq = permakq;
	}

	public void setPermaDagKing(boolean permadagking) {
		this.permadagking = permadagking;
	}

	public void setPermaNex(boolean permanex) {
		this.permanex = permanex;
	}

	public boolean getPermaNex() {
		return permanex;
	}

	public void setPermaLegioPrimus(boolean permalegioprimus) {
		this.permalegioprimus = permalegioprimus;
	}

	public boolean getPermaLegioPrimus() {
		return permalegioprimus;
	}

	public boolean getPermaCorp() {
		return permacorp;
	}

	public boolean getPermaKQ() {
		return permakq;
	}

	public boolean getPermaDagKing() {
		return permadagking;
	}

	public void setPermaKBD(boolean permakbd) {
		this.permakbd = permakbd;
	}

	public boolean getPermaKBD() {
		return permakbd;
	}

	public void setPermaGradum(boolean permagradum) {
		this.permagradum = permagradum;
	}

	public boolean getPermaGradum() {
		return permagradum;
	}

	public void setPermaWyrm(boolean permawyrm) {
		this.permawyrm = permawyrm;
	}

	public boolean getPermaWyrm() {
		return permawyrm;
	}

	public void setPermaNecroLord(boolean permanecrolord) {
		this.permanecrolord = permanecrolord;
	}

	public boolean getPermaNecroLord() {
		return permanecrolord;
	}

	public void setPermaAvatar(boolean permaavatar) {
		this.permaavatar = permaavatar;
	}

	public boolean getPermaAvatar() {
		return permaavatar;
	}

	public void setPermaFear(boolean permafear) {
		this.permafear = permafear;
	}

	public boolean getPermaFear() {
		return permafear;
	}

	public void setPermaSTQ(boolean permastq) {
		this.permastq = permastq;
	}

	public boolean getPermaSTQ() {
		return permastq;
	}

	public void setPermaGeno(boolean permageno) {
		this.permageno = permageno;
	}

	public boolean getPermaGeno() {
		return permageno;
	}

	public void setPermaRajj(boolean permarajj) {
		this.permarajj = permarajj;
	}

	public boolean getPermaRajj() {
		return permarajj;
	}

	public void setHacker(int hacker) {
		this.hacker = hacker;
	}

	public int getHacker() {
		return hacker;
	}

	private List<Integer> uniqueSofItem;

	/*
	 * used for the unique items from sof
	 */
	public List<Integer> getUniqueSofItems() {
		if (uniqueSofItem == null) {
			uniqueSofItem = new ArrayList<Integer>();
		}
		return uniqueSofItem;
	}

	public List<Integer> sofItems2;

	public List<Integer> getSofItems2() {
		return sofItems2;
	}

	/** Skilling pets */
	public List<Integer> getCollectedPets() {
		return collectedPets;
	}

	public void setCollectedPets(List<Integer> collectedPets) {
		this.collectedPets = collectedPets;
	}

	/**
	 * Input event.
	 */

	public void setInputEvent(InputEvent<?> inputEvent) {
		this.inputEvent = inputEvent;
		if (inputEvent instanceof InputIntegerEvent) {
			getPackets().sendInputIntegerScript(inputEvent.getText());
		} else if (inputEvent instanceof InputNameEvent) {
			getPackets().sendInputNameScript(inputEvent.getText());
		} else if (inputEvent instanceof InputLongStringEvent) {
			getPackets().sendInputLongTextScript(inputEvent.getText());
		}
	}

	public InputEvent<?> getInputEvent() {
		return inputEvent;
	}

	public void resetInputEvent() {
		inputEvent = null;
	}
	public boolean hasAnswered;

	public long getTotalValue() {
		return getInventoryValue() + getEquipmentValue() + getBankValue() + getMoneyPouch().getCoinsAmount();
	}

	public long getInventoryValue() {
		long value = 0;
		for (Item inventory : getInventory().getItems().toArray()) {
			if (inventory == null)
				continue;
			long amount = inventory.getAmount();
			value += inventory.getDefinitions().getTipitPrice() * amount;
		}
		return value;
	}

	public long getEquipmentValue() {
		long value = 0;
		for (Item equipment : getEquipment().getItems().toArray()) {
			if (equipment == null)
				continue;
			long amount = equipment.getAmount();
			value += equipment.getDefinitions().getTipitPrice() * amount;
		}
		return value;
	}

	public long getBankValue() {
		long value = 0;
		for (Item bank : getBank().getContainerCopy()) {
			if (bank == null)
				continue;
			long amount = bank.getAmount();
			value += bank.getDefinitions().getTipitPrice() * amount;
		}
		return value;
	}

	public long getClanBankValue() {
		long value = 0;
		for (Item bank : getClanManager().getClan().getClanBank().getContainerCopy()) {
			if (bank == null)
				continue;
			long amount = bank.getAmount();
			value += bank.getDefinitions().getTipitPrice() * amount;
		}
		return value;
	}

	// Slayer
	private SlayerTask task;

	public Contract Rtask;

	public int tasksCompleted;

	private int taskStreak;

	public void setTaskStreak(int amount) {
		taskStreak = amount;
	}

	public int getTaskStreak() {
		return taskStreak;
	}

	/**
	 * Co-Op Slayer.
	 */
	public CooperativeSlayer coOpSlayer;

	public void getPartner() {
		sm("Your Slayer partner is: " + getSlayerPartner() + ".");
	}

	public boolean hasInvited, hasHost, hasGroup, hasOngoingInvite;

	private String slayerPartner = "";

	public String getSlayerPartner() {
		return slayerPartner;
	}

	public void setSlayerPartner(String partner) {
		slayerPartner = partner;
	}

	private String slayerHost = "";

	public String getSlayerHost() {
		return slayerHost;
	}

	public void setSlayerHost(String host) {
		slayerHost = host;
	}

	private String slayerInvite = "";

	public String getSlayerInvite() {
		return slayerInvite;
	}

	public void setSlayerInvite(String invite) {
		slayerInvite = invite;
	}

	// slayer masters
	public void setSlayerTaskAmount(int amount) {
		this.slayerTaskAmount = amount;
	}

	public int getSlayerTaskAmount() {
		return slayerTaskAmount;
	}

	private int master;

	public int getSlayerMaster() {
		return master;
	}

	public void setSlayerMaster(int masta) {
		this.master = masta;
	}

	/**
	 * Grand Exchange.
	 */
	public GrandExchangeManager geManager;

	public GrandExchangeManager getGEManager() {
		return geManager;
	}

	public int yellBanned;

	public boolean offHandAttack = false;
	public boolean rs3Combat = true;

	public void addSuperAntiFire(long time) {
		superAntiFire = time + Utils.currentTimeMillis();
	}

	public long getSuperAntiFire() {
		return superAntiFire;
	}

	/**
	 * Dungeoneering scrolls.
	 */
	private boolean augury, renewal, rigour, efficiency, life, cleansing;

	public void setAugury(boolean aug) {
		augury = aug;
	}

	public boolean hasAuguryActivated() {
		return augury;
	}

	public void setRenewal(boolean ren) {
		renewal = ren;
	}

	public boolean hasRenewalActivated() {
		return renewal;
	}

	public void setRigour(boolean rig) {
		rigour = rig;
	}

	public boolean hasRigourActivated() {
		return rigour;
	}

	public void setEfficiency(boolean eff) {
		efficiency = eff;
	}

	public boolean hasEfficiencyActivated() {
		return efficiency;
	}

	public void setLife(boolean life) {
		this.life = life;
	}

	public boolean hasLifeActivated() {
		return life;
	}

	public void setCleansing(boolean cleanse) {
		cleansing = cleanse;
	}

	public boolean hasCleansingActivated() {
		return cleansing;
	}

	public boolean consumeFish;

	/**
	 * For AFK'ing combat.
	 */
	public long toleranceTimer;

	private CosmeticManager cosmeticManager;

	public CosmeticManager getCosmeticManager() {
		return cosmeticManager;
	}

	public boolean emptyWarning;

	/**
	 * Sets the Tolerance Timer.
	 */
	public void setToleranceTimer() {
		toleranceTimer = System.currentTimeMillis();
	}

	public boolean hasOffhand() {
		return offhand;
	}

	public void setOffhand(boolean offhand) {
		this.offhand = offhand;
	}

	public LootBag getLootbag() {
		return lootBag;
	}

	/**
	 * Jadinko Lair
	 */
	private int favorPoints;

	public int getFavorPoints() {
		return favorPoints;
	}

	public void setFavorPoints(int points) {
		if (points + favorPoints >= 2000) {
			points = 2000;
			getPackets().sendGameMessage(
					"The offering stone is full! The jadinkos won't deposite any more rewards until you have taken some.");
		}
		this.favorPoints = points;
		refreshFavorPoints();
	}

	public void refreshFavorPoints() {
		getVarBitManager().sendVarBit(9511, favorPoints);
	}

	public TreasureTrails getTreasureTrails() {
		return treasureTrails;
	}

	public boolean containsOneItem(int... itemIds) {
		if (getInventory().containsOneItem(itemIds)) {
			return true;
		}
		if (getEquipment().containsOneItem(itemIds)) {
			return true;
		}
		Familiar familiar = getFamiliar();
		if (familiar != null
				&& (familiar.getBob() != null && familiar.getBob().containsOneItem(itemIds) || familiar.isFinished())) {
			return true;
		}
		return false;
	}

	private transient boolean pouchFilter;

	public void setPouchFilter(boolean pouchFilter) {
		this.pouchFilter = pouchFilter;
	}

	public boolean isPouchFilter() {
		return pouchFilter;
	}

	// Legendary Pets
	public long petLifeSaver = 0;
	public long petHighAlchemy = 0;
	public long petItemRepair = 0;
	public long petItemBank = 0;
	public long petItemForge = 0;
	public long petItemForageReset = 0;
	public int petForageItems = 0;
	public long petNPCExecute = 0;
	public boolean petVamp = false;
	public long petVampReset = 0;

	public int araxxiEnrage = 0;
	public long araxxiEnrageTimer = 0;

	public boolean hasLegendaryPet() {
		return getPet() != null && (getPet().getPetType() == Pets.BLOODPOUNCER
				|| getPet().getPetType() == Pets.SKYPOUNCER || getPet().getPetType() == Pets.BLAZEHOUND
				|| getPet().getPetType() == Pets.DRAGON_WOLF || getPet().getPetType() == Pets.WARBORN_BEHEMOTH
				|| getPet().getPetType() == Pets.PROTOTYPE_COLOSSUS || getPet().getPetType() == Pets.RORY_THE_REINDEER);
	}

	private List<Player> vorParty = new ArrayList<>();

	public void clearVoragoPartyMembers() {
		vorParty = new ArrayList<>();
	}

	public void addVoragoPartyMember(Player player) {
		if (vorParty.contains(player))
			return;
		vorParty.add(player);
	}

	public void removeVoragoPartyMember(Player player) {
		vorParty.remove(player);
	}

	public List<Player> getVoragoPartyMembers() {
		return vorParty;
	}

	public void eggBurst(int X, int Y, int Z) {
		if (AraxxorEggBurst) {
			sm("Araxxor Egg Burst false");
			World.sendGraphics(null, new Graphics(4996), new WorldTile(X + 3, Y, Z));
			World.sendGraphics(null, new Graphics(4996), new WorldTile(X + 2, Y, Z));
			World.sendGraphics(null, new Graphics(4996), new WorldTile(X + 2, Y + 1, Z));
			World.sendGraphics(null, new Graphics(4982), new WorldTile(X + 3, Y, Z));
			World.sendGraphics(null, new Graphics(4982), new WorldTile(X + 2, Y, Z));
			World.sendGraphics(null, new Graphics(4982), new WorldTile(X + 2, Y + 1, Z));
			AraxxorEggBurst = true;
			stopAll();
		}
	}

	public void setKilledAraxxor(boolean killedAraxxor) {
	}

	public boolean hefinLapReward;
	public boolean hasRecievedStarter;
	public long motherlodeMaw;
	public long lastOnslaughtKill;

	/**
	 * Player-owned titles.
	 */
	public Titles titles;

	public Titles getTitles() {
		return titles;
	}

	/**
	 * Prifddinas thieving.
	 */
	public int thievIorwerth, thievIthell, thievCadarn, thievAmlodd, thievTrahaearn, thievHefin, thievCrwys, thievMeilyr;
	public byte caughtIorwerth, caughtIthell, caughtCadarn, caughtAmlodd, caughtTrahaearn, caughtHefin, caughtCrwys, caughtMeilyr;

	/**
	 * Checks if the Player has access to Prifddinas.
	 * 
	 * @return true if has access.
	 */
	public boolean hasAccessToPrifddinas() {
		return getSkills().getTotalLevel(this) >= 2250 || getPerkManager().hasPerk(PlayerPerks.ELF_FRIEND);
	}

	public boolean hasItem(Item item) {
		if (getInventory().contains(item)) {
			return true;
		}
		if (getEquipment().getItemsContainer().contains(item)) {
			return true;
		}
		if (getBank().getItem(item.getId()) != null) {
			return true;
		}
		return false;
	}

	public void addItem(Item item) {
		if (!getInventory().hasFreeSlots()
				&& !(item.getDefinitions().isStackable() && getInventory().containsOneItem(item.getId()))) {
			if (!getBank().hasBankSpace()) {
				World.addGroundItem(item, this, this, true, 60);
			} else {
				if (item.getDefinitions().isNoted()) {
					item.setId(item.getDefinitions().getCertId());
				}
				getBank().addItem(item.getId(), item.getAmount(), true);
				sm(item.getName() + " has been added to your bank account.");
			}
		} else {
			getInventory().addItem(item);
		}
	}

	public boolean isMaxed() {
		if (getSkills().getLevelForXp(Skills.ATTACK) >= 99 && getSkills().getLevelForXp(Skills.STRENGTH) >= 99
				&& getSkills().getLevelForXp(Skills.DEFENCE) >= 99 && getSkills().getLevelForXp(Skills.RANGE) >= 99
				&& getSkills().getLevelForXp(Skills.PRAYER) >= 99 && getSkills().getLevelForXp(Skills.MAGIC) >= 99
				&& getSkills().getLevelForXp(Skills.RUNECRAFTING) >= 99
				&& getSkills().getLevelForXp(Skills.HITPOINTS) >= 99 && getSkills().getLevelForXp(Skills.AGILITY) >= 99
				&& getSkills().getLevelForXp(Skills.HERBLORE) >= 99 && getSkills().getLevelForXp(Skills.THIEVING) >= 99
				&& getSkills().getLevelForXp(Skills.CRAFTING) >= 99 && getSkills().getLevelForXp(Skills.FLETCHING) >= 99
				&& getSkills().getLevelForXp(Skills.SLAYER) >= 99 && getSkills().getLevelForXp(Skills.HUNTER) >= 99
				&& getSkills().getLevelForXp(Skills.MINING) >= 99 && getSkills().getLevelForXp(Skills.SMITHING) >= 99
				&& getSkills().getLevelForXp(Skills.FISHING) >= 99 && getSkills().getLevelForXp(Skills.COOKING) >= 99
				&& getSkills().getLevelForXp(Skills.FIREMAKING) >= 99
				&& getSkills().getLevelForXp(Skills.WOODCUTTING) >= 99
				&& getSkills().getLevelForXp(Skills.FARMING) >= 99
				&& getSkills().getLevelForXp(Skills.CONSTRUCTION) >= 99
				&& getSkills().getLevelForXp(Skills.SUMMONING) >= 99
				&& getSkills().getLevelForXp(Skills.DUNGEONEERING) >= 99
				&& getSkills().getLevelForXp(Skills.DIVINATION) >= 99) {
			return true;
		}
		return false;
	}

	/**
	 * Player Owned Port.
	 */
	public PlayerOwnedPort ports;

	public PlayerOwnedPort getPorts() {
		return ports;
	}
	
	public Statistics statistics;

	public Statistics getStatistics() {
		return statistics;
	}
	
	public Information information;

	public Information getInformation() {
		return information;
	}

	/**
	 * Divination things.
	 */
	public boolean[] boons;

	public int divine, gathered;
	public transient Player divines;
	public long lastGatherLimit, lastCreationTime;
	public boolean created;

	public boolean[] getBoons() {
		return boons;
	}

	public boolean getBoon(int index) {
		return boons[index];
	}

	public void setBoons(boolean[] boons) {
		this.boons = boons;
	}

	public boolean hasMoney(int amount) {
		int money = getInventory().getNumberOf(995) + getMoneyPouch().getCoinsAmount();
		return money >= amount;
	}

	/*
	 * Gamemodes
	 */
	private boolean isSquire;
	private boolean isLegend;
	private boolean isVeteran;

	public boolean isSquire() {
		return isSquire;
	}

	public boolean setSquire(boolean isSquire) {
		return this.isSquire = isSquire;

	}

	public boolean isLegend() {
		return isLegend;
	}

	public boolean setLegend(boolean isLegend) {
		return this.isLegend = isLegend;

	}

	public boolean isVeteran() {
		return isVeteran;
	}

	public boolean setVeteran(boolean isVeteran) {
		return this.isVeteran = isVeteran;

	}

	public String getXPMode() {
		if (isSquire()) {
			return "Squire";
		}
		if (isVeteran()) {
			return "Veteran";
		}
		if (isLegend()) {
			return "Legend";
		}
		return "Squire";
	}

	public double getDropRate() {
		if (isSquire())
			return 1.5 + getDonatorDropRate();
		if (isVeteran())
			return 2 + getDonatorDropRate();
		if (isLegend())
			return 2.5 + getDonatorDropRate();
		return 1;
	}

	public double getDonatorDropRate() {
		if (getDonationManager().isImmortalDonator())
			return 1.6;
		if (getDonationManager().isHeroicDonator())
			return 1.4;
		if (getDonationManager().isDivineDonator())
			return 1.2;
		if (getDonationManager().isLegendaryDonator())
			return 1.075;
		if (isExtremeDonator())
			return 1.05;
		return 1.025;
	}

	/**
	 * Skiller Task System
	 */
	private SkillerTasks skillTasks;
	private int taskPoints;

	public int getTaskPoints() {
		return taskPoints;
	}

	public void setTaskPoints(int amount) {
		this.taskPoints = amount;
	}

	public SkillerTasks getSkillTasks() {
		return skillTasks;
	}

	public long lastLootbeamMessage;

	public int getLootBeamValue() {
		return lootBeamValue;
	}

	public void setLootBeamValue(int amount) {
		this.lootBeamValue = amount;
	}

	public double getDropRateModifier() {
		double dropRateModifier = getDropRate();
		boolean hasROW = ItemDefinitions.getItemDefinitions(getEquipment().getRingId()).getName().toLowerCase().contains("wealth");
		boolean hasCompCape = ItemDefinitions.getItemDefinitions(getEquipment().getCapeId()).getName().toLowerCase().contains("completionist");
		if (hasROW)
			dropRateModifier += 0.05;
		if (hasCompCape)
			dropRateModifier += 0.07;
		return dropRateModifier;
	}

	public boolean hasGroupAssignments() {
		return groupAssignments;
	}

	public void toggleGroupAssignments() {
		groupAssignments = !groupAssignments;
	}

	public boolean hasLargerTasks() {
		return largerTasks;
	}

	public void toggleLargerTasks() {
		largerTasks = !largerTasks;
	}

	public int getRerollCount() {
		return rerollCount;
	}

	public void increaseRerollCount() {
		rerollCount++;
	}

	public void resetRerollCount() {
		rerollCount = 0;
	}

	public int setXpRate(int rate) {
		return xpRate = rate;
	}

	public int getXpRate() {
		return xpRate;
	}

	public boolean hasPaidDeath() {
		return hasPaidDeath;
	}

	public void switchWasAlwaysChatOnMode() {
		hasPaidDeath = !hasPaidDeath;
	}

	public void setHasPaidDeath(boolean hasPaidDeath) {
		this.hasPaidDeath = hasPaidDeath;
	}

	public void setHasTalkedToJade(boolean talkedToJade) {
		this.talkedToJade = talkedToJade;
	}

	public boolean containsItem(int id) {
		return getInventory().containsItemToolBelt(id) || getEquipment().getItems().containsOne(new Item(id))
				|| getBank().containsItem(id);
	}

	public boolean isAnIronMan() {
		if (isIronman())
			return true;
		if (isHardcoreIronman())
			return true;
		return false;
	}

	/* Home Selection */
	public boolean homeIsTaverly;
	public boolean homeIsLletya;
	public boolean homeIsLumby;
	public boolean homeIsVarrock;

	public void setTaverlyHome(boolean homeIsTaverly) {
		this.homeIsTaverly = homeIsTaverly;
	}

	public WorldTile getPlayerHomeLocation() {
		if (homeIsLletya) {
			new WorldTile(2321, 3171, 0);
		}
		if (homeIsLumby) {
			new WorldTile(Settings.START_PLAYER_LOCATION);
		}
		if (homeIsTaverly) {
			new WorldTile(3225, 3219, 0);
		}
		if (homeIsVarrock) {
			new WorldTile(3165, 3459, 0);
		}
		return Settings.START_PLAYER_LOCATION;
	}

	public void setLumbyHome(boolean homeIsLumby) {
		this.homeIsLumby = homeIsLumby;
	}

	public void setLletyaHome(boolean homeIsLletya) {
		this.homeIsLletya = homeIsLletya;
	}

	public boolean isLletyaHome() {
		return homeIsLletya;
	}

	public void setVarrockHome(boolean homeIsVarrock) {
		this.homeIsVarrock = homeIsVarrock;
	}

	public boolean isVarrockHome() {
		return homeIsVarrock;
	}

	public boolean isLumbyHome() {
		return homeIsLumby;
	}

	public boolean isTaverlyHome() {
		return homeIsTaverly;
	}

	public String getIronmanTitle(boolean yell) {
		if (yell) {
			if (isIronman())
				if (getAppearence().isMale())
					return "Ironman";
				else
					return "Ironwoman";
			if (isHardcoreIronman())
				if (getAppearence().isMale())
					return "Hardcore Ironman";
				else
					return "Hardcore Ironwoman";
		} else {
			if (isIronman())
				if (getAppearence().isMale())
					return "<col=5F6169>Ironman </col>";
				else
					return "<col=5F6169>Ironwoman </col>";
			if (isHardcoreIronman())
				if (getAppearence().isMale())
					return "<col=A30920>Hardcore Ironman </col>";
				else
					return "<col=A30920>Hardcore Ironwoman </col>";
		}
		return "";
	}

	private double weight;

	public boolean lookingAtEvents = false;

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	private int bankId;

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public int getBankId() {
		return bankId;
	}

	public boolean hasClanBank() {
		return getClanManager() != null && getClanManager().getClan() != null && getClanManager().getClan().getClanBank() != null;
	}

	public SophanemChest getSophanemChest() {
		return sophanemChest;
	}

	public MetalBank getMetalBank() {
		return metalBank;
	}

	public OreBox getOreBox() {
		return oreBox;
	}

	public ArrayList<Integer> unlockedCostumesIds;

	public boolean isLockedCostume(int itemId) {
		return !unlockedCostumesIds.contains(itemId);
	}

	public ArrayList<Integer> getUnlockedCostumesIds() {
		return unlockedCostumesIds;
	}

	private boolean showSearchOption;
	private boolean filterLocked;

	public boolean isShowSearchOption() {
		return showSearchOption;
	}

	public void setShowSearchOption(boolean showSearchOption) {
		this.showSearchOption = showSearchOption;
	}

	public boolean isFilterLocked() {
		return filterLocked;
	}

	public void setFilterLocked(boolean filterLocked) {
		this.filterLocked = filterLocked;
	}

	/* Barrows */
	
	public void resetBarrows() {
		hiddenBrother = -1;
		killedBarrowBrothers = new boolean[7];
		barrowsKillCount = 0;
	}
	
	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}
	
	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}

	public int setBarrowsKillCount(int barrowsKillCount) {
		return this.barrowsKillCount = barrowsKillCount;
	}
	
	public void handleRights() {
		if (isOwner() || isAdministrator() || isDeveloper()) {
			setRights(2);
		}
		if (isModerator()) {
			setRights(1);
		}
		for (String supporter : Settings.SUPPORTER) {
			if (getUsername().equalsIgnoreCase(supporter)) {
				setSupporter(true);
			}
		}
		for (String supporter : Settings.TRIAL_SUPPORTER) {
			if (getUsername().equalsIgnoreCase(supporter)) {
				setSupporter(true);
			}
		}
	}

	private boolean usedIceCooler;
	
	public void setUsedIceCooler(boolean usedIceCooler) {
		this.usedIceCooler = usedIceCooler;
	}
	
	public boolean hasUsedIceCooler() {
		return usedIceCooler;
	}
	
	private String lastBossInstanceKey;
	private InstanceSettings lastBossInstanceSettings;
	
	public void setLastBossInstanceKey(String lastBossInstanceKey) {
		this.lastBossInstanceKey = lastBossInstanceKey;
	}

	public InstanceSettings getLastBossInstanceSettings() {
		return lastBossInstanceSettings;
	}

	public void setLastBossInstanceSettings(InstanceSettings lastBossInstanceSettings) {
		this.lastBossInstanceSettings = lastBossInstanceSettings;
	}

	public String getLastBossInstanceKey() {
		return lastBossInstanceKey;
	}
	
	private transient int npcViewDistanceBits;

	public int getNPCViewDistanceBits() {
		return npcViewDistanceBits;
	}
	
	public void setNPCViewDistanceBits(int npcViewDistanceBits) {
		this.npcViewDistanceBits = npcViewDistanceBits;
		getLocalNPCUpdate().reset();
		loadMapRegions();
	}

	public boolean hasBankSkills() {
		if (getGamePointManager().hasReward(GamePointRewards.BANK_SKILLS)) {
			return true;
		}
		return false;
	}
	
}