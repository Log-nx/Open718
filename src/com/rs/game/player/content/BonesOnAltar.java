package com.rs.game.player.content;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.actions.cooking.Cooking.Cookables;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

@SuppressWarnings("unused")
public class BonesOnAltar extends Action {

	public enum Bones {

		BONES(new Item(526, 1), 5), WOLF_BONES(new Item(2859, 1), 8), BIG_BONES(new Item(532, 1), 15), JOGRE_BONES(
				new Item(3125, 1),
				15), BURNT_JOGRE_BONES(new Item(3127, 1), 16), BABYDRAGON_BONES(new Item(534, 1), 30), WYVERN_BONES(
						new Item(6812, 1), 50), DRAGON_BONES(new Item(536, 1), 72), OURG_BONES(new Item(4834, 1),
								140), ADAMANT_DRAGON_BONES(new Item(35008), 144), FROST_DRAGON_BONES(new Item(18830, 1),
										180), RUNE_DRAGON_BONES(new Item(35010, 1), 190), DAGANNOTH_BONES(
												new Item(6729, 1), 125), AIRUT_BONES(new Item(30209, 1), 132);

		private static Map<Short, Bones> bones = new HashMap<Short, Bones>();

		static {
			for (Bones bone : Bones.values()) {
				bones.put((short) bone.getBone().getId(), bone);
			}
		}

		public static Bones forId(short itemId) {
			return bones.get(itemId);
		}

		private Item item;
		private int xp;

		private Bones(Item item, int xp) {
			this.item = item;
			this.xp = xp;
		}

		public Item getBone() {
			return item;
		}

		public int getXP() {
			return xp;
		}
	}

	public static Bones isGood(Item item) {
		return Bones.forId((short) item.getId());
	}

	public final String MESSAGE = "The gods are very pleased with your offerings.";

	public final double MULTIPLIER = 2.5;
	private Bones bone;
	private Item item;
	private WorldObject object;

	private Animation USING = new Animation(896);

	public BonesOnAltar(WorldObject object, Item item) {
		this.item = item;
		this.object = object;
	}

	@Override
	public boolean process(Player player) {
		if (!World.containsObjectWithId(object, object.getId())) {
			return false;
		}
		if (!player.getInventory().containsItem(item.getId(), 1)) {
			return false;
		}
		if (!player.getInventory().containsItem(bone.getBone().getId(), 1)) {
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		player.closeInterfaces();
		if (player.getPerkManager().hasPerk(PlayerPerks.STRONG_ARM_BURIAL)) {
			player.setNextAnimation(new Animation(20294));
			player.setNextGraphics(new Graphics(4001));
		} else {
			player.setNextAnimation(USING);
			player.getPackets().sendGraphics(new Graphics(624), object);
		}
		player.getInventory().deleteItem(item.getId(), 1);
		player.getSkills().addXp(Skills.PRAYER,
				bone.getXP() * player.getAuraManager().getPrayerMultiplier() * MULTIPLIER);
		player.getStatistics().addBonesOffered();
		player.getPackets().sendGameMessage("The Gods are very pleased with your offerings; " + "bones offered: " + Colors.RED
				+ Utils.getFormattedNumber(player.getStatistics().getBonesOffered()) + "</col>.", true);
		player.getInventory().refresh();
		player.faceObject(object);
		return 3;
	}

	@Override
	public boolean start(Player player) {
		if ((bone = Bones.forId((short) item.getId())) == null) {
			return false;
		}
		player.faceObject(object);
		return true;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}