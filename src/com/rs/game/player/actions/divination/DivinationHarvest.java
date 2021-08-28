package com.rs.game.player.actions.divination;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.random.ChronicleFragmentNPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

/**
 * Used to handle Wisp harvesting.
 * @author BigFuckinChungus
 */
public class DivinationHarvest extends Action {

    private Wisp wisp;
    private WispInfo info;

    public DivinationHarvest(Player player, Object[] args) {
		wisp = (Wisp) args[0];
		info = (WispInfo) args[1];
    }

    public static boolean checkAll(Player player, WispInfo info) {
		if (player.getSkills().getLevel(Skills.DIVINATION) < info.getLevel()) {
			player.getPackets().sendGameMessage("You need a Divination level of " + info.getLevel() + " to harvest from this spring.");
		    return false;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPackets().sendGameMessage("Inventory full. To make room, sell, drop or bank something.", true);
		    return false;
		}
		return true;
    }

    public NPC getWisp() {
    	return wisp;
    }

    public WispInfo getInfo() {
    	return info;
    }

    @Override
    public boolean start(Player player) {
		if (!checkAll(player, info)) {
			return false;
		}
		player.setNextAnimation(new Animation(21231));
		player.faceEntity(wisp);
		return true;
    }

    @Override
    public boolean process(Player player) {
		if (wisp == null || wisp.isUsedUp()) {
			return false;
		}
		if (!checkAll(player, info)) {
			return false;
		}
		if (Utils.random(player.getPerkManager().hasPerk(PlayerPerks.MORE_DIVINATION) ? 300 : 400) == 0) {
			if (!player.hasItem(new Item(29293, 30))) {
				new ChronicleFragmentNPC(player, player);
				player.getPackets().sendGameMessage(Colors.RED+"<shad=000000>A chronicle escapes from the spring!");
			}
		}
		player.faceEntity(wisp);
		return true;
    }

    @Override
    public void stop(Player player) {
    	player.setNextAnimation(new Animation(21229));
    }
    
    private int getBoost(Player player) {
    	int boost = 5;
    	if (player.getPerkManager().hasPerk(PlayerPerks.MORE_DIVINATION)) {
			boost += 5;
		}
    	return boost + player.getAuraManager().getDivinationEnrichment();
    }

    @Override
    public int processWithDelay(Player player) {
    	double exp = info.getHarvestXp();
    	int roll = Utils.random(100);
    	Item energy = new Item(info.getEnergyId(), player.getBoon(info.ordinal()) ? Utils.random(1, 5) : Utils.random(1, 3));
    	if (hasElderDivinationOutfit(player)) {
    		if (roll <= 7) {
    			exp *= 2;
    			player.getInventory().addItem(energy);
    		}
    	}
    	if (hasDivinationOutfit(player)) {
    		if (roll <= 5) {
    			exp *= 2;
    			player.getInventory().addItem(energy);
    		}
    	}
		player.getInventory().addItem(energy);
		int chance = Utils.random(2);
		if (Utils.getRandom(100) >= 5) {
		    if (info != WispInfo.PALE && Utils.getRandom(100) <= getBoost(player)) {
		    	
		    	player.getSkills().addXp(Skills.DIVINATION, exp * 2);
		    	player.setNextGraphics(new Graphics(4236));
		    	if (chance != 2) {
		    		if (hasDivinationOutfit(player) && roll <= 5) {
			    		player.getInventory().addItem(info.getEnrichedMemoryId(), 1);
			    		player.getStatistics().addMemoriesCollected();
		    		}
		    		if (hasElderDivinationOutfit(player) && roll <= 7) {
			    		player.getInventory().addItem(info.getEnrichedMemoryId(), 1);
			    		player.getStatistics().addMemoriesCollected();
		    		}
		    		player.getInventory().addItem(info.getEnrichedMemoryId(), 1);
		    		player.getStatistics().addMemoriesCollected();
		    		player.getPackets().sendGameMessage("You've harvested an enriched memory; memories harvested: "
		    				+ Colors.RED+Utils.getFormattedNumber(player.getStatistics().getMemoriesCollected())+"</col>.", true);
		    	}
		    } else {
		    	player.getSkills().addXp(Skills.DIVINATION, exp);
		    	player.setNextGraphics(new Graphics(4235));
		    	if (chance != 2) {
		    		if (hasDivinationOutfit(player) && roll <= 5) {
			    		player.getInventory().addItem(info.getMemoryId(), 1);
			    		player.getStatistics().addMemoriesCollected();
		    		}
		    		if (hasElderDivinationOutfit(player) && roll <= 7) {
			    		player.getInventory().addItem(info.getMemoryId(), 1);
			    		player.getStatistics().addMemoriesCollected();
		    		}
		    		player.getInventory().addItem(info.getMemoryId(), 1);
		    		player.getStatistics().addMemoriesCollected();
		    		player.getPackets().sendGameMessage("You've harvested a memory; memories harvested: "
		    				+ Colors.RED+Utils.getFormattedNumber(player.getStatistics().getMemoriesCollected())+"</col>.", true);
		    	}
		    }
		} else {
			player.getSkills().addXp(Skills.DIVINATION, exp);
		}
		return 2;
    }
    
    /**
     * Checks if the player has one of the Divination outfits.
     * @param player The player to check.
     * @return if has an outfit.
     */
    public static boolean hasDivinationOutfit(Player player) {
    	if (player.getEquipment().getHatId() == 35963
    			&& player.getEquipment().getChestId() == 35964
    			&& player.getEquipment().getLegsId() == 35965
    	    	&& player.getEquipment().getGlovesId() == 35966
    			&& player.getEquipment().getBootsId() == 35967) {
			return true;
		}
    	if (player.getEquipment().getHatId() == 35968
    			&& player.getEquipment().getChestId() == 35969
    			&& player.getEquipment().getLegsId() == 35970
    	    	&& player.getEquipment().getGlovesId() == 35971
    			&& player.getEquipment().getBootsId() == 35972) {
			return true;
		}
    	if (player.getEquipment().getHatId() == 35973
    			&& player.getEquipment().getChestId() == 35974
    			&& player.getEquipment().getLegsId() == 35975
    	    	&& player.getEquipment().getGlovesId() == 35976
    			&& player.getEquipment().getBootsId() == 35977) {
			return true;
		}
    	return false;
    }
    
    /**
     * Checks if the player has one of the Divination outfits.
     * @param player The player to check.
     * @return if has an outfit.
     */
    public static boolean hasElderDivinationOutfit(Player player) {
    	if (player.getEquipment().getHatId() == 35978
    			&& player.getEquipment().getChestId() == 35979
    			&& player.getEquipment().getLegsId() == 35980
    	    	&& player.getEquipment().getGlovesId() == 35981
    			&& player.getEquipment().getBootsId() == 35982) {
			return true;
		}
    	return false;
    }
}