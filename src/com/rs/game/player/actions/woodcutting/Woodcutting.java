package com.rs.game.player.actions.woodcutting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.others.NatureSpiritNPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.actions.firemaking.Firemaking;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.events.GlobalEvents.Event;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.content.items.BirdNests;
import com.rs.game.player.content.perks.GamePointRewards;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public final class Woodcutting extends Action {

	public static enum TreeDefinitions {

		NORMAL(1, 25, 1511, 20, 4, 1341, 8, 0),

		EVERGREEN(1, 25, 1511, 20, 4, 57931, 8, 0),

		DEAD(1, 25, 1511, 20, 4, 12733, 8, 0),

		FRUIT_TREES(1, 25, -1, 20, 4, 1341, 8, 0),

		OAK(15, 37.5, 1521, 30, 4, 1341, 15, 15),

		WILLOW(30, 67.5, 1519, 60, 4, 1341, 51, 15),

		MAPLE(45, 100, 1517, 83, 16, 31057, 72, 10),

		MAHOGANY(50, 105, 6332, 83, 16, 31057, 72, 10),

		YEW(60, 175, 1515, 85, 13, 1341, 94, 10),

		IVY(68, 200, -1, 90, 14, 46319, 58, 10),

		MAGIC(75, 250, 1513, 100, 15, 37824, 121, 10),

		TEAK(35, 70, 6333, 60, 4, -1, 51, 15),

		CURSED_MAGIC(82, 250, 1513, 150, 21, 37822, 121, 10),

		BLOOD(85, 250, 24121, 153, 21, -1, 500, 6),

		JADE_ROOT_HEALTHY(83, 100, 21349, 30, 10, -1, 180, 5),

		JADE_ROOT_MUTATED(83, 100, 21358, 30, 10, -1, 180, 5),

		DONOR_TREE(1, 25, -1, 80, 10, -1, -1, -1),

		TANGLE_GUM_VINE(1, 35, 17682, 20, 4, 49706, 8, 5),

		SEEPING_ELM_TREE(10, 60, 17684, 25, 4, 49708, 12, 5),

		BLOOD_SPINDLE_TREE(20, 85, 17686, 35, 4, 49710, 16, 5),

		UTUKU_TREE(30, 115, 17688, 60, 4, 49712, 51, 5),

		SPINEBEAM_TREE(40, 145, 17690, 76, 16, 49714, 68, 5),

		BOVISTRANGLER_TREE(50, 175, 17692, 85, 16, 49716, 75, 5),

		THIGAT_TREE(60, 210, 17694, 95, 16, 49718, 83, 10),

		CORPESTHORN_TREE(70, 245, 17696, 111, 16, 49720, 90, 10),

		ENTGALLOW_TREE(80, 285, 17698, 120, 17, 49722, 94, 10),
		
		MUTATED_VINE(83, 140, 21358, 83, 16, 12288, 72, 10),

		CURLY_VINE(83, 140, -1, 83, 16, 12279, 72, 0),

		STRAIT_VINE(83, 140, -1, 83, 16, 12277, 72, 0),

		GRAVE_CREEPER_TREE(90, 330, 17700, 150, 21, 49724, 121, 10),
		
		STRAIT_VINE_COLLECTABLE(83, 140, 21349, 83, 16, 12283, 72, 0),;

		private int level;
		private double xp;
		private int logsId;
		private int logBaseTime;
		private int logRandomTime;
		private int stumpId;
		private int respawnDelay;
		private int randomLifeProbability;

		private TreeDefinitions(int level, double xp, int logsId, int logBaseTime, int logRandomTime, int stumpId,
				int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.logsId = logsId;
			this.logBaseTime = logBaseTime;
			this.logRandomTime = logRandomTime;
			this.stumpId = stumpId;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		public int getLevel() {
			return level;
		}

		public int getLogBaseTime() {
			return logBaseTime;
		}

		public int getLogRandomTime() {
			return logRandomTime;
		}

		public int getLogsId() {
			return logsId;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getStumpId() {
			return stumpId;
		}

		public double getXp() {
			return xp;
		}
	}

	private WorldObject tree;
	private TreeDefinitions definitions;

	private int emoteId;
	private boolean usingBeaver = false;
	private int axeTime;

	private boolean usedDeplateAurora;

	public Woodcutting(WorldObject tree, TreeDefinitions definitions) {
		this.tree = tree;
		this.definitions = definitions;
	}

	private static double woodcuttingSet(Player player) {
		double xpBoost = 1.0;
		if (player.getEquipment().getChestId() == 10939) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getLegsId() == 10940) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getHatId() == 10941) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getBootsId() == 10933) {
			xpBoost *= 1.01;
		}
		if (player.getEquipment().getChestId() == 10939 && player.getEquipment().getLegsId() == 10940
				&& player.getEquipment().getHatId() == 10941 && player.getEquipment().getBootsId() == 10933) {
			xpBoost *= 1.01;
		}
		if (Wilderness.isAtWild(player) && player.getEquipment().getGlovesId() == 13850) {
			xpBoost *= 1.01;
		}
		return xpBoost;
	}

	private static int infAdze;

	public static void addLog(TreeDefinitions definitions, boolean dungeoneering, boolean usingBeaver, Player player) {
		if (definitions == TreeDefinitions.DONOR_TREE) {
			int log = Utils.random(8);
			double xp = 25;
			Item item = null;
			switch (log) {
			case 0: /** Normal **/
				item = new Item(1511);
				break;
			case 1: /** Oak **/
				item = new Item(1521);
				xp = 37.5;
				break;
			case 2: /** Willow **/
				item = new Item(1519);
				xp = 67.5;
				break;
			case 3: /** Maple **/
				item = new Item(1517);
				xp = 100;
				break;
			case 4: /** Mahogany **/
				item = new Item(6332);
				xp = 105;
				break;
			case 5: /** Yew **/
				item = new Item(1515);
				xp = 175;
				break;
			case 6: /** Magic **/
				item = new Item(1513);
				xp = 250;
				break;
			case 7: /** Teak **/
			case 8: /** Teak **/
				item = new Item(6333);
				xp = 70;
				break;
			}
			BirdNests.dropNest(player);
			player.getSkills().addXp(Skills.WOODCUTTING, xp * woodcuttingSet(player));
			player.getStatistics().addLogsChopped();
			player.getPackets().sendGameMessage("You get some " + item.getName().toLowerCase() + "; total chopped: "
					+ Colors.RED + Utils.getFormattedNumber(player.getStatistics().getLogsChopped()) + "</col>.", true);

			if (player.getEquipment().getWeaponId() == 13661) {
				infAdze = Utils.random(3);
			} else {
				infAdze = 0;
			}
			if (infAdze == 1 && player.getEquipment().getWeaponId() == 13661) {
				player.getStatistics().addLogsBurned();
				player.getSkills().addXp(Skills.FIREMAKING, Firemaking.increasedExperience(player, xp + Utils.random(2, 5)));
				player.getPackets().sendGameMessage("The adze's heat instantly incinerates the " + item.getName().toLowerCase() + "; " + "logs burned: " + Colors.RED + Utils.getFormattedNumber(player.getStatistics().getLogsBurned()) + "</col>.", true);
				World.sendProjectile(player, player, new WorldTile(player.getX(), player.getY() - 3, 0), 1776, 30, 0, 15, 0, 0, 0);
				infAdze = 0;
			} else {
				player.getGamePointManager().addGamePointItem(item.getId(), 1);
			}
			return;
		}
		player.getSkills().addXp(Skills.WOODCUTTING, definitions.getXp() * woodcuttingSet(player));
		if (!(definitions == TreeDefinitions.JADE_ROOT_HEALTHY || definitions == TreeDefinitions.JADE_ROOT_MUTATED)) {
			BirdNests.dropNest(player);
		}
		if (definitions == TreeDefinitions.NORMAL) {
			player.getSkillTasks().decreaseTask(SkillTasks.WNORMAL1);
			player.getSkillTasks().decreaseTask(SkillTasks.WNORMAL2);
			if (player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().tutorialStage == 2) {
				player.getStatistics().tutorialStage = 3;
				player.getDialogueManager().startDialogue("SurvivalExpert", 7);
			}
		} else if (definitions == TreeDefinitions.OAK) {
			player.getSkillTasks().decreaseTask(SkillTasks.WOAK1);
			player.getSkillTasks().decreaseTask(SkillTasks.WOAK2);
		} else if (definitions == TreeDefinitions.WILLOW) {
			player.getSkillTasks().decreaseTask(SkillTasks.WWILLOW1);
			player.getSkillTasks().decreaseTask(SkillTasks.WWILLOW2);
			player.getSkillTasks().decreaseTask(SkillTasks.WWILLOW3);
		} else if (definitions == TreeDefinitions.MAPLE) {
			player.getSkillTasks().decreaseTask(SkillTasks.WMAPLE1);
			player.getSkillTasks().decreaseTask(SkillTasks.WMAPLE2);
		} else if (definitions == TreeDefinitions.YEW) {
			player.getSkillTasks().decreaseTask(SkillTasks.WYEW1);
			player.getSkillTasks().decreaseTask(SkillTasks.WYEW2);
			player.getSkillTasks().decreaseTask(SkillTasks.WYEW3);
		} else if (definitions == TreeDefinitions.MAGIC) {
			player.getSkillTasks().decreaseTask(SkillTasks.WMAGIC1);
			player.getSkillTasks().decreaseTask(SkillTasks.WMAGIC2);
		} else if (definitions == TreeDefinitions.IVY) {
			player.getPackets().sendGameMessage("You successfully cut an ivy vine.");
			player.getSkillTasks().decreaseTask(SkillTasks.WIVY1);
			player.getSkillTasks().decreaseTask(SkillTasks.WIVY2);
			player.getSkillTasks().decreaseTask(SkillTasks.WIVY3);
			player.choppedIvy++;
		}
		if (definitions == TreeDefinitions.IVY) {
			player.getPackets().sendGameMessage("You succesfully cut an ivy vine.", true);
		} else {
			String logName = ItemDefinitions.getItemDefinitions(definitions.getLogsId()).getName().toLowerCase();
			player.getStatistics().addLogsChopped();
			player.getPackets().sendGameMessage("You get some " + logName + "; total chopped: " + Colors.RED + Utils.getFormattedNumber(player.getStatistics().getLogsChopped()) + "</col>.", true);

			if (player.getEquipment().getWeaponId() == 13661) {
				infAdze = Utils.random(3);
			} else {
				infAdze = 0;
			}
			if (infAdze == 1 && player.getEquipment().getWeaponId() == 13661 && !(definitions == TreeDefinitions.IVY)) {
				player.getStatistics().addLogsBurned();
				player.getSkills().addXp(Skills.FIREMAKING, Firemaking.increasedExperience(player, definitions.getXp()));
				player.getPackets().sendGameMessage("The adze's heat instantly incinerates the " + logName + "; " + "logs burned: " + Utils.getFormattedNumber(player.getStatistics().getLogsBurned()) + ".", true);
				World.sendProjectile(player, player, new WorldTile(player.getX(), player.getY() - 3, 0), 1776, 30, 0, 15, 0, 0, 0);
				infAdze = 0;
			} else {
				player.getGamePointManager().addGamePointItem(definitions.getLogsId(), 1);
			}
		}
	}

	private boolean checkAll(Player player) {
		if (player.getInterfaceManager().containsScreenInter() || player.getInterfaceManager().containsInventoryInter()) {
			player.getPackets().sendGameMessage("Please finish what you're doing before doing this action.");
			return false;
		}
		if (!hasAxe(player)) {
			player.getPackets().sendGameMessage("You need a hatchet to chop down this tree.");
			return false;
		}
		if (!setAxe(player)) {
			player.getPackets().sendGameMessage("You don't have the required level to use that axe.");
			return false;
		}
		if (!hasWoodcuttingLevel(player)) {
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage("Inventory full. To make more room, sell, drop or bank something.");
			return false;
		}
		return true;
	}

	private boolean checkTree(Player player) {
		return World.containsObjectWithId(tree, tree.getId());
	}

	private int getWoodcuttingDelay(Player player) {
		int summoningBonus = player.getFamiliar() != null ? player.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807 ? 10 : 0 : 0;
		int wcTimer = definitions.getLogBaseTime() - (player.getSkills().getLevel(8) + summoningBonus) - Utils.getRandom(axeTime);
		if (wcTimer < 1 + definitions.getLogRandomTime()) {
			wcTimer = 1 + Utils.getRandom(definitions.getLogRandomTime());
		}
		if (player.getGamePointManager().hasReward(GamePointRewards.MORE_SKILLS))
			wcTimer -= 10;
		if (player.getPerkManager().hasPerk(PlayerPerks.BANK_LOGS))
			wcTimer -= 10;
		if (GlobalEvents.isActiveEvent(Event.FASTER_SKILLING))
			wcTimer /= 2;
		wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();
		return wcTimer;
	}

	private boolean hasAxe(Player player) {
		if (player.getInventory().containsOneItem(1351, 1349, 1353, 1355, 1357, 1361, 1359, 6739, 13661, 32645)) {
			return true;
		}
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1) {
			return false;
		}
		switch (weaponId) {
		case 1351:// Bronze Axe
		case 1349:// Iron Axe
		case 1353:// Steel Axe
		case 1361:// Black Axe
		case 1355:// Mithril Axe
		case 1357:// Adamant Axe
		case 1359:// Rune Axe
		case 6739:// Dragon Axe
		case 13661: // Inferno adze
		case 32645: // Crystal hatchet
			return true;
		default:
			return false;
		}
	}

	private boolean hasWoodcuttingLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.getPackets().sendGameMessage("You need a woodcutting level of " + definitions.getLevel() + " to chop down this tree.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (player.getPerkManager().hasPerk(PlayerPerks.ROUNDHOUSE_WOODCUTTING)) {
			player.setNextAnimation(new Animation(17304));
			player.setNextGraphics(new Graphics(3301));
		} else if (player.getPerkManager().hasPerk(PlayerPerks.STRONG_ARM_WOODCUTTING)) {
			player.setNextAnimation(new Animation(20302));
			player.setNextGraphics(new Graphics(4006));
		} else {
			player.setNextAnimation(new Animation(usingBeaver ? 1 : emoteId));
		}
		if (Utils.random(750) == 0) {
			new NatureSpiritNPC(player, player);
			player.getPackets().sendGameMessage("<col=ff0000>A Nature Spirit emerges from the tree.");
		}
		return checkTree(player);
	}

	@Override
	public int processWithDelay(Player player) {
		boolean dungeoneering = definitions.ordinal() > 18;
		addLog(definitions, dungeoneering, usingBeaver, player);
		if (!usedDeplateAurora && 1 + Math.random() < player.getAuraManager().getChanceNotDepleteMN_WC()) {
			usedDeplateAurora = true;
		} else if (Utils.getRandom(definitions.getRandomLifeProbability()) == 0
				&& definitions != TreeDefinitions.DONOR_TREE) {
			long time = definitions.respawnDelay * 600;
			World.spawnTemporaryObject(new WorldObject(definitions.getStumpId(), tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getPlane()), time);
			if (tree.getPlane() < 3 && definitions != TreeDefinitions.IVY) {
				WorldObject object = World.getStandartObject(new WorldTile(tree.getX() - 1, tree.getY() - 1, tree.getPlane() + 1));
				if (object == null) {
					object = World.getStandartObject(new WorldTile(tree.getX(), tree.getY() - 1, tree.getPlane() + 1));
					if (object == null) {
						object = World.getStandartObject(new WorldTile(tree.getX() - 1, tree.getY(), tree.getPlane() + 1));
						if (object == null) {
							object = World.getStandartObject(new WorldTile(tree.getX(), tree.getY(), tree.getPlane() + 1));
						}
					}
				}

				if (object != null) {
					World.removeObjectTemporary(object, time, true);
				}
			}
			player.setNextAnimation(new Animation(-1));
			return -1;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage("Inventory full. To make more room, sell, drop or bank something.");
			return -1;
		}
		return getWoodcuttingDelay(player);
	}

	private boolean setAxe(Player player) {
		int level = player.getSkills().getLevel(8);
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId != -1) {
			switch (weaponId) {
			case 32645: // crystal axe
				if (level >= 71) {
					emoteId = 25169;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 25172;
					}
					axeTime = 14;
					return true;
				}
				break;
			case 6739: // dragon axe
				if (level >= 61) {
					emoteId = 2846;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 17092;
					}
					axeTime = 13;
					return true;
				}
				break;
			case 13661: // inferno adze
				if (level >= 61) {
					emoteId = 10251;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 17093;
					}
					axeTime = 13;
					return true;
				}
				break;
			case 1359: // rune axe
				if (level >= 41) {
					emoteId = 867;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 17085;
					}
					axeTime = 10;
					return true;
				}
				break;
			case 1357: // adam axe
				if (level >= 31) {
					emoteId = 869;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 17086;
					}
					axeTime = 7;
					return true;
				}
				break;
			case 1355: // mit axe
				if (level >= 21) {
					emoteId = 871;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 17087;
					}
					axeTime = 5;
					return true;
				}
				break;
			case 1361: // black axe
				if (level >= 11) {
					emoteId = 873;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 17088;
					}
					axeTime = 4;
					return true;
				}
				break;
			case 1353: // steel axe
				if (level >= 6) {
					emoteId = 875;
					if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
						emoteId = 17089;
					}
					axeTime = 3;
					return true;
				}
				break;
			case 1349: // iron axe
				emoteId = 877;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17090;
				}
				axeTime = 2;
				return true;
			case 1351: // bronze axe
				emoteId = 879;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17091;
				}
				axeTime = 1;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(32645)) {
			if (level >= 71) {
				emoteId = 25169;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 25172;
				}
				axeTime = 14;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(6739)) {
			if (level >= 61) {
				emoteId = 2846;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17092;
				}
				axeTime = 13;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(13661)) {
			if (level >= 61) {
				emoteId = 10251;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17093;
				}
				axeTime = 13;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1359)) {
			if (level >= 41) {
				emoteId = 867;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17085;
				}
				axeTime = 10;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1357)) {
			if (level >= 31) {
				emoteId = 869;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17086;
				}
				axeTime = 7;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1355)) {
			if (level >= 21) {
				emoteId = 871;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17087;
				}
				axeTime = 5;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1361)) {
			if (level >= 11) {
				emoteId = 873;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17088;
				}
				axeTime = 4;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1353)) {
			if (level >= 6) {
				emoteId = 875;
				if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
					emoteId = 17089;
				}
				axeTime = 3;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1349)) {
			emoteId = 877;
			if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
				emoteId = 17090;
			}
			axeTime = 2;
			return true;
		}
		if (player.getInventory().containsOneItem(1351)) {
			emoteId = 879;
			if (player.getPerkManager().hasPerk(PlayerPerks.LUMBERJACK_WOODCUTTING)) {
				emoteId = 17091;
			}
			axeTime = 1;
			return true;
		}
		return false;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		player.getPackets().sendGameMessage(usingBeaver ? "Your beaver uses its strong teeth to chop down the tree..."
				: "You swing your hatchet at the " + (TreeDefinitions.IVY == definitions ? "ivy" : "tree") + "...",
				true);
		setActionDelay(player, getWoodcuttingDelay(player));
		return true;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	public enum HatchetDefinitions {

		BRONZE(1351, 1, 1, 879, 480),

		IRON(1349, 5, 2, 877, 482),

		STEEL(1353, 5, 3, 875, 484),

		BLACK(1361, 11, 4, 873, 0),

		MITHRIL(1355, 21, 5, 871, 486),

		ADAMANT(1357, 31, 7, 869, 488),

		RUNE(1359, 41, 10, 867, 490),

		DRAGON(6739, 61, 13, 2846, 0),

		INFERNO(13661, 61, 13, 10251, 0),

		CRYSTAL(32645, 71, 14, 32645, 0);

		private int itemId, levelRequried, axeTime, emoteId, axeHead;

		private HatchetDefinitions(int itemId, int levelRequried, int axeTime, int emoteId, int axeHead) {
			this.itemId = itemId;
			this.levelRequried = levelRequried;
			this.axeTime = axeTime;
			this.emoteId = emoteId;
			this.axeHead = axeHead;
		}

		public int getItemId() {
			return itemId;
		}

		public int getLevelRequried() {
			return levelRequried;
		}

		public int getAxeTime() {
			return axeTime;
		}

		public int getEmoteId() {
			return emoteId;
		}

		public int getAxeHeadForItem() {
			return axeHead;
		}
	}

	public static HatchetDefinitions getHatchet(Player player, boolean dungeoneering) {
		for (int i = dungeoneering ? 10 : HatchetDefinitions.values().length - 1; i >= (dungeoneering ? 0 : 11); i--) {
			HatchetDefinitions def = HatchetDefinitions.values()[i];
			if (player.getInventory().containsItemToolBelt(def.itemId) || player.getEquipment().getWeaponId() == def.itemId) {
				if (player.getSkills().getLevel(Skills.WOODCUTTING) >= def.getLevelRequried())
					return def;
			}
		}
		return null;
	}
}