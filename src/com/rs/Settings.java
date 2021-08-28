package com.rs;

import java.math.BigInteger;
import java.util.ArrayList;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;

public final class Settings {

	/*
	 * Update list via an interface.
	 */
	
	public static final String LATEST_UPDATES = ("Cosmetic Overrides are here!");
	
	/*
	 * General client and server settings.
	 */
	public static final String SERVER_NAME = "Diccus";
	public static final int PORT_ID = 43594;
	public static final String CACHE_PATH = "YOU NEED 718 - 905 CACHE";
	public static final int RECEIVE_DATA_LIMIT = 7500;
	public static final int PACKET_SIZE_LIMIT = 7500;
	public static final int CLIENT_BUILD = 718;
	public static final int CUSTOM_CLIENT_BUILD = 1;

	/*
	 * Link settings
	 */
	public static final String VOTE_LINK = "";
	public static final String HIGHSCORES_LINK = "";
	public static final String FORUMS_LINK = "";
	public static final String GUIDES_LINK = "";
	public static final String RULES_LINK = "";
	public static final String DONATE_LINK = "";

	/**
	 * Launching settings
	 */
	public static boolean DEBUG = true;
	public static boolean HOSTED;
	public static boolean ECONOMY;

	/**
	 * If the use of the managment server is enabled.
	 */
	public static boolean MANAGMENT_SERVER_ENABLED = true;

	/**
	 * Graphical User Interface settings
	 */
	public static final String GUI_SIGN = "Diccus Console";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Player settings
	 */
	public static final int START_PLAYER_HITPOINTS = 100;
	public static final WorldTile START_PLAYER_LOCATION = new WorldTile(2343, 3692, 0);
	public static final WorldTile RESPAWN_PLAYER_LOCATION = new WorldTile(2339, 3689, 0);
	public static final WorldTile TUTORIAL_LOCATION = new WorldTile(3798, 4259, 0);
	public static final String START_CONTROLER = "";
	public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000;
	public static final int DROP_RATE = 1;

	/* Double Experience */
	public static boolean DOUBLE_EXP;

	public static void switchDoubleExperience() {
		DOUBLE_EXP = !DOUBLE_EXP;
	}

	/**
	 * World settings
	 */
	public static final int WORLD_CYCLE_TIME = 600; // the speed of world in ms

	/**
	 * Music & Emote settings
	 */
	public static final int AIR_GUITAR_MUSICS_COUNT = 50;

	/**
	 * Quest settings
	 */
	public static final int QUESTS = 183;

	/**
	 * Memory settings
	 */
	public static final int PLAYERS_LIMIT = 2000;
	public static final int LOCAL_PLAYERS_LIMIT = 250;
	public static final int NPCS_LIMIT = Short.MAX_VALUE;
	public static final int LOCAL_NPCS_LIMIT = 250;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000; // 30mb

	/**
	 * Game constants
	 */
	public static final int[] MAP_SIZES = { 104, 120, 136, 168, 72 };

	public static final String GRAB_SERVER_TOKEN = "hAJWGrsaETglRjuwxMwnlA/d5W6EgYWx";

	public static final int[] GRAB_SERVER_KEYS = { 1441, 78700, 44880, 39771, 363186, 44375, 0, 16140, 55059, 271148,
			1238496, 216189, 668056, 1199765, 1388168, 41072, 25367, 17247, 1244, 91561, 2285, 119, 882901, 1818764,
			8657, 24326 };

	public static final BigInteger GRAB_SERVER_PRIVATE_EXPONENT = new BigInteger(
			"89322430671018348298861742515764482233068288200783343847484544509571962628065714778039511997210645035862085108962875360569135872634557671666240517045834668947577049456705382892479750010806366850719644085927056756904313310072649000562778157741036154113587273566438466744696882894671672580846715090382197655153");
	public static final BigInteger GRAB_SERVER_MODULUS = new BigInteger(
			"115364171193790857703771988634011723234901294835045976897948378959143482150356556557719793819072464058398074055260823414177708406772382518337315585709021299516318648240434549165809373987442991535785582009103643721314234447691079015056437405560926382379415396001459145525973260251222089696997044675536959288419");

	public static final BigInteger PRIVATE_EXPONENT = new BigInteger(
			"66851234167926024380953989339831673274959129365599608132765057938509769881257204176228996571543884170458905350931714006603627296876867252430110992145127045541093512308052351616147593807209595540403658444011070264203511111233188267283254233133340511625089309299466023592024757080082019901427343980837171609453");
	public static final BigInteger MODULUS = new BigInteger(
			"135252348768665077635741721957353390282499196160692171709845448155963164532706244870728853403799325066507124378230226927138021243954534903297384746464211154793581403970982194545379291592214853590086028609334242657579723419696938265964747992773207383237448008221192605273035890497659188786240923114771728636709");

	public static final String LOG_PATH = "/data/logs/";
	public static final String LOGS_PATH = "data/logs/";
	public static final String DATA_PATH = "/data/";

	/* Special ranks settings */
	public static final String[] MAIN_ACESSS = { "BigFuckinChungus" };

	/* Server limits */
	public static final int SV_RECEIVE_DATA_LIMIT = 10000; // bytes
	public static final int SV_PACKET_SIZE_LIMIT = 10000; // bytes
	public static final long SV_MAX_PACKETS_DECODER_PING_DELAY = 30000; // 30
																		// seconds
	public static final int SV_PLAYERS_LIMIT = 1000;
	public static final int SV_LOCAL_PLAYERS_LIMIT = 250;
	public static final int SV_NPCS_LIMIT = Short.MAX_VALUE;
	public static final int SV_LOCAL_NPCS_LIMIT = 250;

	/**
	 * Dungeoneering Related
	 */
	public static final int DUNG_XP_RATE = 10;

	public static final WorldTile RESPAWN_DUNGEONEERING_LOCATION = new WorldTile(3454, 3726, 0);

	public static final String[] RARE_DROPS = { "pernix", "torva", "virtus", "abyssal", "dark bow", "bandos",
			"steadfast", "glaiven", "ragefire", "spirit shield", "dragon claw", "berserker ring", "warrior ring",
			"archers' ring", "seers' ring", "hilt", "saradomin", "armadyl", "subjugation", "drygore", "draconic visage",
			"ascension", "tetsu", "death lotus", "seasinger's", "spider leg", "araxxi", "araxyte egg", "dragon pickaxe",
			"partyhat", "party hat", "cracker", "dragon hatchet", "spectral", "arcane", "divine", "elysian",
			"wand of treachery", "abyssal whip", "whip vine", "seismic", "crest", "anima", "dragon rider lance",
			"shadow glaive", "cywir elders", "nymora", "avaryss", "zamorakian spear", "zaryte", "celestial handwrap",
			"razorback gauntlet", "blood necklace", "tetsu", "singer", "death lotus" };

	/**
	 * Donator settings
	 */
	public static String[] DONATOR_ITEMS = { "primal", "promethium", "(i)", "thok's", "trident" };

	public static final String[] BOTS = { "Jens", "Michal", "Rickard", "Yan", "Nils", "Scramble", "Empty" };

	public static String[] EXTREME_DONATOR_ITEMS = {};

	public static String[] QUEST_ITEMS = { "greegree" };

	public static final double[] SOF_CHANCES = new double[] { 1.0D, 0.35D, 0.05D, 0.001D };
	public static final int[] SOF_COMMON_CASH_AMOUNTS = new int[] { 100, 250, 500, 1000, 5000 };
	public static final int[] SOF_UNCOMMON_CASH_AMOUNTS = new int[] { 10000, 25000, 50000, 100000, 500000 };
	public static final int[] SOF_RARE_CASH_AMOUNTS = new int[] { 1000000, 2500000, 5000000, 10000000, 50000000 };
	public static final int[] SOF_JACKPOT_CASH_AMOUNTS = new int[] { 100 * 1000000, 250 * 1000000, 500 * 1000000, 1000 * 1000000 };
	public static final int[] SOF_UNCOMMON_LAMPS = new int[] { 23714, 23718, 23722, 23726, 23730, 23738, 23734, 23742, 23746, 23750, 23754, 23758, 23762, 23766, 23770, 23779, 23775, 23787, 23783, 23795, 23791, 23803, 23799, 23811, 23807, 23815 };
	public static final int[] SOF_RARE_LAMPS = new int[] { 23715, 23719, 23723, 23727, 23731, 23739, 23735, 23743,
			23747, 23751, 23755, 23759, 23763, 23767, 23771, 23780, 23776, 23788, 23784, 23796, 23792, 23804, 23800,
			23812, 23808, 23816 };
	public static final int[] SOF_JACKPOT_LAMPS = new int[] { 23716, 23720, 23724, 23728, 23732, 23740, 23736, 23744,
			23748, 23752, 23756, 23760, 23764, 23768, 23773, 23781, 23777, 23789, 23785, 23797, 23793, 23805, 23801,
			23813, 23809, 23817 };
	public static final int[] SOF_COMMON_OTHERS = new int[] { 1965, 1925, 618, 1955, 1514, 15271, 52, 2364, 18831, 8779,
			26743, 15273, 8779, 15273, 2002 };
	public static final int[] SOF_UNCOMMON_OTHERS = new int[] { 24154, 24154, 24155 };
	public static final int[] SOF_RARE_OTHERS = new int[] { 995, 995, 995, 995, 23665, 23666, 23667, 23668, 23669,
			23670, 23671, 23672, 23673, 23674, 23675, 23676, 23677, 23678, 23679, 23680, 23681, 23682, 23691, 23692,
			23693, 23694, 23695, 23696, 23687, 23688, 23689, 23684, 23686, 23685, 23697, 23690, 23699, 23700, 23683,
			23698, 27370, 27371, 27372, 26743 };
	public static final int[] SOF_JACKPOT_OTHERS = new int[] { 995, 995, 995, 995, 18349, 18351, 18353, 18355, 18333,
			18335, 21777, 22494, 23659, 27359, 27358, 27356 };
	public static final int[] SOF_COMMON_LAMPS = new int[] { 23713, 23717, 23721, 23725, 23729, 23737, 23733, 23741,
			23745, 23749, 23753, 23757, 23761, 23765, 23769, 23778, 23774, 23786, 23782, 23794, 23790, 23802, 23798,
			23810, 23806, 23814 };
	public static final int[] SOF_ONLY_ONCE = new int[] { 28024, 28025, 28029, 27616, 27618, 27620, 27622, 27624, 28686,
			28688, 28690, 28692, 28694, 31089, 31091, 31095, 31097, 31099, 23679, 23680, 23681, 23682, 23691, 23692,
			23693, 23694, 23695, 23696, 23687, 23688, 23689, 23684, 23686, 23685, 23697, 23690, 23699, 23700, 23683,
			23698, 34036, 34034, };
	public static final boolean SQUEAL_OF_FORTUNE_ENABLED = true;
	public static final boolean TEST = false;
	public static final boolean FARMING_SPEED_INCREASE = true;
	public static final int WARNING_AMOUNT = Integer.MAX_VALUE;
	/**
	 * Item settings
	 */
	public static String[] EARNED_ITEMS = { "tokkul", "castle wars ticket", "(class", "sacred clay", "magic carpet" };

	public static String[] UNWEARABLE_ITEMS = { "enchanted gem" };

	public static String[] REMOVING_ITEMS = { "(class", "sacred clay", "dominion", "sled" };

	private Settings() {

	}

    public static final String[] OWNER = { "BigFuckinChungus" };
    public static final String[] CO_OWNER = { };
    public static final String[] DEVELOPER = { "BigFuckinChungus" };
    public static final String[] COMMUNITY_MANAGER = { };
    public static final String[] ADMINISTRATOR = { "Kipo" };
    public static final String[] MODERATOR = { };
    public static final String[] TRIAL_SUPPORTER = { };
    public static final String[] SUPPORTER = { };

	public static String CACHE_PATH_RS3 = "C:/Users/Nathan/Desktop/Runescape/Caches/718-905/cache/";
	public static final String CACHE_PATH_RS2 = "C:/Users/Nathan/Desktop/Runescape/Caches/718-905/cache/"; // I forgot dean changed those a long time ago

	public static boolean isOwner(Player player) {
		player.getUsername().contentEquals("BigFuckinChungus");
		return false;
	}

	public static int getDegradeGearRate() {
		return 1;
	}

	public static final boolean DOUBLE_DROPS = false;
	public static final boolean PRINTS = true;
	public static final boolean DISCORD = true;
	public static final boolean TUTORIAL = true;
	public static final boolean BETA = false;

	public static String[] VORAGO_ROTATION_NAMES = { "Ceiling Collapse", "Scopulus", "Vitalis", "Green Bomb", "Team Split", "The End" };
	public static int VORAGO_ROTATION;
	public static boolean VORAGO_JUMP_DISABLED = true;
	public static int DAYS_TO_CHANGE_ROTATION = 3;
	public static String VORAGO_RELEASE_DATE = "2017-10-30";
}
