package com.rs.game.player.actions.prayer;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.player.controllers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class Burying {

	/**
	 * Handles Bone Burying.
	 * @author BigFuckinChungus
	 */
    public enum Bone {
    	
    	BONES(526, 5), 
    	WOLF_BONES(2859, 8), 
    	BIG_BONES(532, 15), 
    	JOGRE_BONES(3125, 15), 
    	BURNT_JOGRE_BONES(3127, 16),
    	BABYDRAGON_BONES(534, 30), 
    	WYVERN_BONES(6812, 50), 
    	DRAGON_BONES(536, 72), 
    	OURG_BONES(4834, 140), 
    	ADAMANT_DRAGON_BONES(35008, 144), 
    	FROST_DRAGON_BONES(18830, 180), 
    	RUNE_DRAGON_BONES(35010, 190), 
    	DAGANNOTH_BONES(6729, 125), 
    	AIRUT_BONES(30209, 132.5);

    	private int id;
    	private double experience;

    	private static Map<Integer, Bone> bones = new HashMap<Integer, Bone>();

    	static {
    		for (Bone bone : Bone.values()) {
    			bones.put(bone.getId(), bone);
    		}
    	}

    	private static final Animation BURY_ANIMATION = new Animation(827);

    	public static void bury(final Player player, int inventorySlot) {
    		final Item item = player.getInventory().getItem(inventorySlot);
    		if (item == null || Bone.forId(item.getId()) == null) {
				return;
			}
    		if (player.isLocked()) {
				return;
			}
    		final Bone bone = Bone.forId(item.getId());
    		final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
    		final double xp = increasedExperience(player, bone.getExperience());
    		player.lock(1);
    		player.getPackets().sendSound(2738, 0, 1);
    		if (player.getPerkManager().hasPerk(PlayerPerks.STRONG_ARM_BURIAL)) {
				player.setNextAnimation(new Animation(20294));
				player.setNextGraphics(new Graphics(4001));
			} else {
				player.setNextAnimation(BURY_ANIMATION);
			}
    		player.getPackets().sendGameMessage("You dig a hole in the ground...", true);
    		player.getInventory().deleteItem(item.getId(), 1);
    		WorldTasksManager.schedule(new WorldTask() {
    			@Override
    			public void run() {
    				player.getStatistics().addBonesOffered();
    				player.getPackets().sendGameMessage("You bury the " + itemDef.getName().toLowerCase()+"; "
    						+ "bones offered: "+Colors.RED+Utils.getFormattedNumber(player.getStatistics().getBonesOffered())+"</col>.", true);
					final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
					handlePrayerBonus(player, bone, maxPrayer);
    				player.getSkills().addXp(Skills.PRAYER, xp);
    				if (player.getAuraManager().getPrayerRestoration() != 0) {
						player.getPrayer().restorePrayer((int) ((int) Math.floor(player.getSkills()
    						.getLevelForXp(Skills.PRAYER)) * player.getAuraManager().getPrayerRestoration()));
					}
    				/** Twisted Bird Skull necklace **/
    				if (player.getEquipment().getAmuletId() == 19886) {
						player.getPrayer().restorePrayer((int) ((int) bone.getExperience() * 0.5));
					} else if (player.getEquipment().getAmuletId() == 19887) {
						player.getPrayer().restorePrayer((int) ((int) bone.getExperience() * 0.75));
					} else if (player.getEquipment().getAmuletId() == 19888) {
						player.getPrayer().restorePrayer((int) bone.getExperience());
					}
    				stop();
    			}
    		}, 1);
    	}

    	public static Bone forId(int id) {
    		return bones.get(id);
    	}

    	public static double increasedExperience(Player player, double totalXp) {
    		if (Wilderness.isAtWild(player) && player.getEquipment().getGlovesId() == 13848) {
				totalXp *= 1.030;
			}
    		totalXp *= player.getAuraManager().getPrayerMultiplier();
		    return totalXp;
    	}

    	private Bone(int id, double experience) {
    		this.id = id;
    		this.experience = experience;
    	}

    	public double getExperience() {
    		return experience;
    	}

    	public int getId() {
    		return id;
    	}
    }

	public static void handlePrayerBonus(Player player, Bone bone, int maxPrayer) {
		if (player.getEquipment().containsOneItem(19886)) {
			switch (bone.getId()) {
			case 526:
			case 528:
			case 530:
			case 532:
			case 534:
			case 6812:
			case 536:
			case 4843:
			case 18830:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(50);
				}
				break;
			}
			player.getPackets().sendGameMessage("Your twisted bird necklace boosts your prayer points.", true);
		}
		if (player.getEquipment().containsOneItem(19886) && player.getEquipment().containsOneItem(32339)) {
			switch (bone.getId()) {
			case 20264:
			case 20266:
			case 20268:
			case 32945:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(50);
				}
				break;
			}
			player.getPackets().sendGameMessage("Your twisted bird necklace boosts your prayer points.", true);
		}
		if (player.getEquipment().containsOneItem(19888)) {
			switch (bone.getId()) {
			case 526:
			case 528:
			case 530:
			case 20264:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(50);
				}
				break;
			case 532:
			case 534:
			case 3125:
			case 6812:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(100);
				}
				break;
			case 536:
			case 6729:
			case 4834:
			case 4835:
			case 14793:
			case 14794:
			case 18832:
			case 18830:
			case 18831:
			case 30209:
			case 20268:
			case 32945:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(150);
				}
				break;
			}
			player.getPackets().sendGameMessage("Your demon horn necklace boosts your prayer points.", true);
		}
		if (player.getEquipment().containsOneItem(19888) && player.getEquipment().containsOneItem(32339)) {
			switch (bone.getId()) {
			case 20264:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(50);
				}
				break;
			case 20266:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(100);
				}
				break;
			case 20268:
			case 32945:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(150);
				}
				break;
			}
			player.getPackets().sendGameMessage("Your demon horn necklace boosts your prayer points.", true);
		}
		if (player.getEquipment().containsOneItem(19887)) {
			switch (bone.getId()) {
			case 526:
			case 528:
			case 530:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(50);
				}
				break;
			case 532:
			case 534:
			case 6812:
			case 536:
			case 4843:
			case 18830:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(100);
				}
				break;
			}
			player.getPackets().sendGameMessage("Your split dragontooth necklace boosts your prayer points.", true);
		}
		if (player.getEquipment().containsOneItem(19887) && player.getEquipment().containsOneItem(32339)) {
			switch (bone.getId()) {
			case 20264:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(50);
				}
				break;
			case 20266:
			case 20268:
			case 32945:
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.getPrayer().restorePrayer(50);
				}
				break;
			}
			player.getPackets().sendGameMessage("Your split dragontooth necklace boosts your prayer points.", true);
		}
	}
}