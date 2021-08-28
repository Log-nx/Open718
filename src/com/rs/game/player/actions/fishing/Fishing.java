package com.rs.game.player.actions.fishing;

import java.util.HashMap;
import java.util.Map;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.NPC;
import com.rs.game.npc.random.RiverTrollNPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.events.GlobalEvents.Event;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.content.perks.GamePointRewards;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class Fishing extends Action {

    public enum Fish {

		ANCHOVIES(321, 15, 40),
	
		BASS(363, 46, 100),
	
		COD(341, 23, 45),
	
		CAVE_FISH(15264, 85, 300),
	
		HERRING(345, 10, 30),
	
		LOBSTER(377, 40, 90),
	
		MACKEREL(353, 16, 20),
	
		MANTA(389, 81, 46),
	
		MONKFISH(7944, 62, 120),
	
		PIKE(349, 25, 60),
	
		SALMON(331, 30, 70),
	
		SARDINES(327, 5, 20),
	
		SEA_TURTLE(395, 79, 38),
	
		SEAWEED(401, 30, 0),
	
		OYSTER(407, 30, 0),
	
		SHARK(383, 76, 165),
	
		SHRIMP(317, 1, 10),
	
		SWORDFISH(371, 50, 100),
	
		TROUT(335, 20, 50),
	
		TUNA(359, 35, 80),
	
		CAVEFISH(15264, 85, 300),
	
		ROCKTAIL(15270, 90, 380),
	
		SMALL_URCHIN(35721, 93, 385),
	
		MEDIUM_URCHIN(35722, 95, 390),
	
		LARGE_URCHIN(35723, 97, 395);
	
		private final int id, level;
		private final double xp;
	
		private Fish(int id, int level, double xp) {
		    this.id = id;
		    this.level = level;
		    this.xp = xp;
		}
	
		public int getId() {
		    return id;
		}
	
		public int getLevel() {
		    return level;
		}
	
		public double getXp() {
		    return xp;
		}
    }

    public enum FishingSpots {
    	
    	CAVEFISH_SHOAL(8841, 1, 307, 313, new Animation(622), Fish.CAVE_FISH),

		ROCKTAIL_SHOAL(8842, 1, 307, 15263, new Animation(622), Fish.ROCKTAIL),

		NET(327, 1, 303, -1, new Animation(621), Fish.SHRIMP, Fish.ANCHOVIES),

		BAIT(327, 2, 307, -1, new Animation(622), Fish.SARDINES, Fish.HERRING),

		LURE(328, 1, 309, 314, new Animation(622), Fish.TROUT, Fish.SALMON),

		LURE2(329, 1, 309, 314, new Animation(622), Fish.TROUT, Fish.SALMON),

		BAIT2(328, 2, 307, 313, new Animation(622), Fish.PIKE),

		BAIT3(329, 2, 307, 313, new Animation(622), Fish.PIKE, Fish.CAVE_FISH),

		CAGE(6267, 1, 301, -1, new Animation(619), Fish.LOBSTER),

		CAGE2(312, 1, 301, -1, new Animation(619), Fish.LOBSTER),

		HARPOON(312, 2, 311, -1, new Animation(618), Fish.TUNA, Fish.SWORDFISH),

		BIG_NET(313, 1, 305, -1, new Animation(620), Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED, Fish.OYSTER),

		HARPOON2(313, 2, 311, -1, new Animation(618), Fish.SHARK),

		NET2(952, 1, 303, -1, new Animation(621), Fish.MONKFISH),

		LURE_PRIFF_I(21778, 1, 307, 313, new Animation(622), Fish.SMALL_URCHIN),

		LURE_PRIFF_II(21779, 1, 307, 313, new Animation(622), Fish.MEDIUM_URCHIN),

		LURE_PRIFF_III(21780, 1, 307, 313, new Animation(622), Fish.LARGE_URCHIN);

		private final Fish[] fish;
		private final int id, option, tool, bait;
		private final Animation animation;
	
		static final Map<Integer, FishingSpots> spot = new HashMap<Integer, FishingSpots>();
	
		static {
		    for (FishingSpots spots : FishingSpots.values()) {
				spot.put(spots.id | spots.option << 24, spots);
			}
		}
	
		public static FishingSpots forId(int id) {
		    return spot.get(id);
		}
	
		private FishingSpots(int id, int option, int tool, int bait,
			Animation animation, Fish... fish) {
		    this.id = id;
		    this.tool = tool;
		    this.bait = bait;
		    this.animation = animation;
		    this.fish = fish;
		    this.option = option;
		}
	
		public Animation getAnimation() {
		    return animation;
		}
	
		public int getBait() {
		    return bait;
		}
	
		public Fish[] getFish() {
		    return fish;
		}
	
		public int getId() {
		    return id;
		}
	
		public int getOption() {
		    return option;
		}
	
		public int getTool() {
		    return tool;
		}
    }

    private FishingSpots spot;

    private NPC npc;
    private WorldTile tile;
    private int fishId;

    private final int[] BONUS_FISH = { 341, 349, 401, 407 };

    private boolean multipleCatch;

    public Fishing(FishingSpots spot, NPC npc) {
		this.spot = spot;
		this.npc = npc;
		tile = new WorldTile(npc);
    }

    private void addFish(Player player) {
		Item fish = new Item(spot.getFish()[fishId].getId(), 
				(player.getPerkManager().hasPerk(PlayerPerks.MASTER_FISHERMAN) ? 2 : 1) * (multipleCatch ? 2 : 1));
		
		player.getPackets().sendGameMessage(getMessage(player, fish), true);
		if (!player.getPerkManager().hasPerk(PlayerPerks.MASTER_FISHERMAN)) {
			if (hasFurySharkOutfit(player) && Utils.random(100) < 10) {
				player.getPackets().sendGameMessage(Colors.ORANGE+"<shad=000000>Your Fury shark outfit has saved you some bait.", true);
			} else {
				player.getInventory().deleteItem(spot.getBait(), 1);
			}
		}
		double totalXp = spot.getFish()[fishId].getXp();
		if (fish.getId() == ItemIdentifiers.RAW_SHRIMPS && player.getControllerManager().getController() instanceof DiccusTutorial && player.getStatistics().tutorialStage == 4) {
			player.getStatistics().tutorialStage = 5;
			Dialogue.sendNPCDialogueNoContinue(player, 943, Dialogue.NORMAL, "Now you have caught some shrimp, let's cook it! You'll cook your shrimp on a fire. If your fire's gone out, chop a tree to get some logs and make a new fire. Then use the shrimp on the fire.");
		}
		handleSkillTask( player );
		player.getSkills().addXp(Skills.FISHING, totalXp * fishingSuit(player) * 
				(player.getPerkManager().hasPerk(PlayerPerks.MASTER_FISHERMAN) ? 2 : 1) * (multipleCatch ? 2 : 1));

		if ((hasSharkOutfit(player) || hasFurySharkOutfit(player)) && player.consumeFish) {
			player.setNextGraphics(new Graphics(5420));
		} else {
			player.getGamePointManager().addGamePointItem(fish.getId(), fish.getAmount());
		}
		
		if (player.getFamiliar() != null) {
		    if (Utils.getRandom(50) == 0 && getSpecialFamiliarBonus(player.getFamiliar().getId()) > 0) {
		    	player.getInventory().addItem(new Item(BONUS_FISH[Utils.random(BONUS_FISH.length)]));
		    	player.getSkills().addXp(Skills.FISHING, 5.5);
		    }
		}
		fishId = getRandomFish(player);
		if (Utils.getRandom(50) == 0 && FishingSpotsHandler.moveSpot(npc)) {
			player.setNextAnimation(new Animation(-1));
		}
    }

    private boolean checkAll(Player player) {
		if (player.getInterfaceManager().containsScreenInter()
			|| player.getInterfaceManager().containsInventoryInter()) {
		    player.getPackets().sendGameMessage("Please finish what you're doing before doing this action.");
		    return false;
		}
		if (player.getSkills().getLevel(Skills.FISHING) < spot.getFish()[fishId].getLevel()) {
		    player.getDialogueManager().startDialogue("SimpleMessage",
			    "You need a fishing level of "  + spot.getFish()[fishId].getLevel() + " to fish here.");
		    return false;
		}
		if (!player.getInventory().containsOneItem(spot.getBait()) && spot.getBait() != -1 && !player.getPerkManager().hasPerk(PlayerPerks.MASTER_FISHERMAN)) {
			 player.getPackets().sendGameMessage("You don't have " + new Item(spot.getBait()).getDefinitions()
						    .getName().toLowerCase() + " to fish here.");
		    return false;
		}
		if (!player.getInventory().containsOneItem(spot.getTool())) {
			if (!hasSharkOutfit(player) && !hasFurySharkOutfit(player)) {
				player.getPackets().sendGameMessage("You don't have the required tool to use this Fishing spot.");
				return false;
			}
		}
		if (!player.getInventory().hasFreeSlots()) {
		    player.setNextAnimation(new Animation(-1));
		    player.getPackets().sendGameMessage("Inventory full. To make more room, sell, drop or bank something.");
		    return false;
		}
		if (tile.getX() != npc.getX() || tile.getY() != npc.getY()) {
			return false;
		}
		return true;
    }

    private int getFishingDelay(Player player) {
		int playerLevel = player.getSkills().getLevel(Skills.FISHING);
		int fishLevel = spot.getFish()[fishId].getLevel();
		int modifier = spot.getFish()[fishId].getLevel();
		int randomAmt = Utils.random(4);
		double cycleCount = 1, otherBonus = 0;
		if (player.getFamiliar() != null) {
			otherBonus = getSpecialFamiliarBonus(player.getFamiliar().getId());
		}
		cycleCount = Math.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10)
				/ modifier * 0.25 - randomAmt * 4);
		if (cycleCount < 1) {
			cycleCount = 1;
		}
		int delay = (int) cycleCount + 1;
		delay /= player.getAuraManager().getFishingAccurayMultiplier();
		if (hasSharkOutfit(player)) {
			delay /= 1.05;
		}
		if (hasFurySharkOutfit(player)) {
			delay /= 1.1;
		}
		if (player.getPerkManager().hasPerk(PlayerPerks.MORE_FISH)) {
			delay /= 1.25;
		}
		if (GlobalEvents.isActiveEvent(Event.FASTER_SKILLING)) {
			delay /= 2;
		}
		return delay;
    }

    private String getMessage(Player player, Item fish) {
    	player.getStatistics().addFishCaught(player.getPerkManager().hasPerk(PlayerPerks.MASTER_FISHERMAN));
		if (spot.getFish()[fishId] == Fish.ANCHOVIES
			|| spot.getFish()[fishId] == Fish.SHRIMP) {
			return "You manage to catch some "
			    + fish.getDefinitions().getName().toLowerCase() + "; fish caught: "
	        		+Colors.RED+Utils.getFormattedNumber(player.getStatistics().getFishCaught())+"</col>.";
		} else if (multipleCatch) {
			return "Your quick reactions allow you to catch two "
			    + fish.getDefinitions().getName().toLowerCase() + "; fish caught: "
	        		+Colors.RED+Utils.getFormattedNumber(player.getStatistics().getFishCaught())+"</col>.";
		} else {
			return "You manage to catch a "
			    + fish.getDefinitions().getName().toLowerCase() + "; fish caught: "
	        		+Colors.RED+Utils.getFormattedNumber(player.getStatistics().getFishCaught())+"</col>.";
		}
    }

    private int getRandomFish(Player player) {
		int random = Utils.random(spot.getFish().length);
		int difference = player.getSkills().getLevel(Skills.FISHING) - spot.getFish()[random].getLevel();
		if (difference < -1) {
			return random = 0;
		}
		if (random < -1) {
			return random = 0;
		}
		return random;
    }

    private int getSpecialFamiliarBonus(int id) {
		switch (id) {
		case 6796:
		case 6795:// rock crab
		    return 1;
		}
		return -1;
    }

    @Override
    public boolean process(Player player) {
		if (Utils.random(500) == 0) {
			new RiverTrollNPC(player, player);
			player.getPackets().sendGameMessage("<col=ff0000>A River Troll emerges from the fishing spot.");
		}
		return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
		addFish(player);
		startFishingAnimation(player);
		player.setNextFaceEntity(npc);
		if (Utils.random(100) == 0) {
			player.getPackets().sendGameMessage("You feel too exhausted from Fishing and rest for a little..");
		    player.setNextAnimation(new Animation(-1));
			return -1;
		}
		return getFishingDelay(player);
    }

    @Override
    public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		fishId = getRandomFish(player);
		if (spot.getFish()[fishId] == Fish.TUNA || spot.getFish()[fishId] == Fish.SHARK
				|| spot.getFish()[fishId] == Fish.SWORDFISH) {
		    if (Utils.getRandom(50) <= 5) {
		    	if (player.getSkills().getLevel(Skills.AGILITY) >= spot.getFish()[fishId].getLevel()) {
					multipleCatch = true;
				}
		    }
		}
		startFishingAnimation(player);
		player.getPackets().sendGameMessage("You attempt to capture a fish...", true);
		setActionDelay(player, getFishingDelay(player));
		return true;
    }

    @Override
    public void stop(final Player player) {
    	player.setNextFaceEntity(null);
    	setActionDelay(player, 3);
    }
    
    /**
     * Starts the fishing action animation.
     * @param player The player starting.
     */
    private void startFishingAnimation(Player player) {
    	if ((hasSharkOutfit(player) || hasFurySharkOutfit(player)) && player.consumeFish) {
			player.setNextAnimationNoPriority(new Animation(26017));
		} else {
			if (player.getPerkManager().hasPerk(PlayerPerks.DEEP_SEA_FISHING)) {
				player.setNextAnimation(new Animation(17084));
			} else if (player.getPerkManager().hasPerk(PlayerPerks.ARCANE_FISHING)) {
				player.setNextAnimation(new Animation(20298));
				npc.setNextGraphics(new Graphics(4007));
			} else {
				player.setNextAnimation(spot.getAnimation());
			}
    	}
    }
    
    /**
     * XP modifier by wearing items.
     * @param player The player.
     * @return the XP modifier.
     */
    private double fishingSuit(Player player) {
    	double xpBoost = 1.0;
    	if (hasSharkOutfit(player)) {
			xpBoost *= 1.05;
		}
    	if (hasFurySharkOutfit(player)) {
			xpBoost *= 1.07;
		}
    	if (player.getEquipment().getHatId() == 24427) {
			xpBoost *= 1.01;
		}
    	if (player.getEquipment().getChestId() == 24428) {
			xpBoost *= 1.01;
		}
    	if (player.getEquipment().getLegsId() == 24429) {
			xpBoost *= 1.01;
		}
    	if (player.getEquipment().getBootsId() == 24430) {
			xpBoost *= 1.01;
		}
    	if (player.getEquipment().getHatId() == 24427
    			&& player.getEquipment().getChestId() == 24428
    			&& player.getEquipment().getLegsId() == 24429
    			&& player.getEquipment().getBootsId() == 24430) {
			xpBoost *= 1.01;
		}
    	return xpBoost;
    }
    
    /**
     * Shark Outfits.
     * http://runescape.wikia.com/wiki/Shark_outfit
     */
    private boolean hasSharkOutfit(Player player) {
    	if (player.getEquipment().getHatId() == 34200
    			&& player.getEquipment().getChestId() == 34201
    			&& player.getEquipment().getLegsId() == 34202
    			&& player.getEquipment().getGlovesId() == 34203
    			&& player.getEquipment().getBootsId() == 34204) {
			return true;
		}
    	if (player.getEquipment().getHatId() == 34205
    			&& player.getEquipment().getChestId() == 34206
    			&& player.getEquipment().getLegsId() == 34207
    			&& player.getEquipment().getGlovesId() == 34208
    			&& player.getEquipment().getBootsId() == 34209) {
			return true;
		}
    	if (player.getEquipment().getHatId() == 34210
    			&& player.getEquipment().getChestId() == 34211
    			&& player.getEquipment().getLegsId() == 34212
    			&& player.getEquipment().getGlovesId() == 34213
    			&& player.getEquipment().getBootsId() == 34214) {
			return true;
		}
    	return false;
    }
    
    private boolean hasFurySharkOutfit(Player player) {
    	if (player.getEquipment().getHatId() == 34215
    			&& player.getEquipment().getChestId() == 34216
    			&& player.getEquipment().getLegsId() == 34217
    			&& player.getEquipment().getGlovesId() == 34218
    			&& player.getEquipment().getBootsId() == 34219) {
			return true;
		}
    	return false;
    }
    
	private void handleSkillTask( Player player ) {
		if (spot.getFish()[fishId] == Fish.ANCHOVIES) {
			player.getSkillTasks().decreaseTask( SkillTasks.FANCHOVIES1);
			player.getSkillTasks().decreaseTask(SkillTasks.FANCHOVIES2);
		} else if (spot.getFish()[fishId] == Fish.HERRING) {
			player.getSkillTasks().decreaseTask(SkillTasks.FHERRING1);
			player.getSkillTasks().decreaseTask(SkillTasks.FHERRING2);
		} else if (spot.getFish()[fishId] == Fish.LOBSTER) {
			player.getSkillTasks().decreaseTask(SkillTasks.FLOBSTER1);
			player.getSkillTasks().decreaseTask(SkillTasks.FLOBSTER2);
		} else if (spot.getFish()[fishId] == Fish.ROCKTAIL) {
			player.getSkillTasks().decreaseTask(SkillTasks.FROCKTAIL1);
			player.getSkillTasks().decreaseTask(SkillTasks.FROCKTAIL2);
		} else if (spot.getFish()[fishId] == Fish.SALMON) {
			player.getSkillTasks().decreaseTask(SkillTasks.FSALMON1);
			player.getSkillTasks().decreaseTask(SkillTasks.FSALMON2);
		} else if (spot.getFish()[fishId] == Fish.SHARK) {
			player.getSkillTasks().decreaseTask(SkillTasks.FSHARK1);
			player.getSkillTasks().decreaseTask(SkillTasks.FSHARK2);
			player.getSkillTasks().decreaseTask(SkillTasks.FSHARK3);
		} else if (spot.getFish()[fishId] == Fish.SHRIMP) {
			player.getSkillTasks().decreaseTask(SkillTasks.FSHRIMP1);
			player.getSkillTasks().decreaseTask(SkillTasks.FSHRIMP2);
		} else if (spot.getFish()[fishId] == Fish.SWORDFISH) {
			player.getSkillTasks().decreaseTask(SkillTasks.FSWORD1);
			player.getSkillTasks().decreaseTask(SkillTasks.FSWORD2);
		} else if (spot.getFish()[fishId] == Fish.TROUT) {
			player.getSkillTasks().decreaseTask(SkillTasks.FTROUT1);
			player.getSkillTasks().decreaseTask(SkillTasks.FTROUT2);
		} else if (spot.getFish()[fishId] == Fish.TUNA) {
			player.getSkillTasks().decreaseTask(SkillTasks.FTUNA1);
			player.getSkillTasks().decreaseTask(SkillTasks.FTUNA2);
		}
	}
}