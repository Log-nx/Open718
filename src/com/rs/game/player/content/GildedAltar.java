package com.rs.game.player.content;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;

public class GildedAltar {

	public enum Bones {

		BONES(526, 25),
		BURNT_BONES(528, 25),
		WOLF_BONES(2859, 25),
		MONKEY_BONES(3183, 25),
		BAT_BONES(530, 25),
		BIG_BONES(532, 75),
		JOGRE_BONES(3125, 75),
		ZOGRE_BONES(4812, 115),
		BABYDRAGON_BONES(534, 150),
		WYVERN_BONES(6812, 250),
		DRAGON_BONES(536, 360),
		FAYRG_BONES(4830, 420),
		RAURG_BONES(4832, 475),
		OURG_BONES(4834, 700),
		FROST_DRAGON_BONES(18830, 900),
		DAGANNOTH_BONES(6729, 675);

		private int id;
		private double experience;

		public static Bones forId(int itemid) {
			for (Bones bone : Bones.values()) {
				if (bone.id == itemid) {
					return bone;
				}
			}
			return null;
		}

		private Bones(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}

		public static boolean stopOfferGod = false;

		public static void offerprayerGod(final Player player, final Bones bone) {
			final int x = player.getX();
			final int y = player.getY();
			
			
			if (bone == null || player.isBurying)
				return;
			
			player.isBurying = true;
			
			WorldTasksManager.schedule(new WorldTask() {
				@Override 
				public void run() {
					try {
						if (x != player.getX() || y != player.getY()) {
							stop();
							player.isBurying = false;
							return;
						}
						if (!player.getInventory().containsItem(bone.getId(), 1)) {
							stop();
							player.isBurying = false;
							return;
						}
						player.getPackets().sendGameMessage("The gods are very pleased with your offering.");
						player.setNextAnimation(new Animation(896));
						player.setNextGraphics(new Graphics(624));
						player.getInventory().deleteItem(new Item(bone.getId(), 1));
						double xp = bone.getExperience() * player.getAuraManager().getPrayerMultiplier();
						player.getSkills().addXp(Skills.PRAYER, xp * 2.5);
						player.getInventory().refresh();
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 0, 3);
		}
	}
}
