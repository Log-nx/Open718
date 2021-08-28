package com.rs.game.player.actions.herblore;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.game.player.managers.CompletionistCapeManager.Requirement;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class HerbCleaning {

	public enum Herbs {

		GUAM(199, 2.5, 3, 249),

		MARRENTILL(201, 3.8, 5, 251),

		TARROMIN(203, 5, 11, 253),

		HARRALANDER(205, 6.3, 20, 255),

		RANARR(207, 7.5, 25, 257),

		TOADFLAX(3049, 8, 30, 2998),

		SPIRIT_WEED(12174, 7.8, 35, 12172),

		IRIT(209, 8.8, 40, 259),

		WERGALI(14836, 9.5, 41, 14854),

		AVANTOE(211, 10, 48, 261),

		KWUARM(213, 11.3, 54, 263),

		SNAPDRAGON(3051, 11.8, 59, 3000),

		CADANTINE(215, 12.5, 65, 265),

		LANTADYME(2485, 13.1, 67, 2481),

		DWARF_WEED(217, 13.8, 70, 267),

		TORSTOL(219, 15, 75, 269),
		
		FELLSTALK(21626,17 , 91 , 21624),
		
		SAGEWORT(17494, 2.5, 3, 17512),
		
		VALERIAN(17496, 3.5, 4, 17514),
		
		ALOE(17498, 4.5, 8, 17516),
		
		WORMWOOD(17500, 8.2, 34, 17518),
		
		MAGEBANE(17502, 8.4, 37, 17520),
		
		FEATHERFOIL(17504, 9.5, 41, 17522),
		
		WINTERS_GRIP(17506, 13.1, 67, 17524),
		
		LYCOPUS(17508, 13.8, 70, 17526),
		
		BUCKTHORN(17510, 14.7, 74, 17528);

		private int herbId;
		private int level;
		private int cleanId;
		private double xp;

		Herbs(int herbId, double xp, int level, int cleanId) {
			this.herbId = herbId;
			this.xp = xp;
			this.level = level;
			this.cleanId = cleanId;
		}

		public int getHerbId() {
			return herbId;
		}

		public double getExperience() {
			return xp;
		}

		public int getLevel() {
			return level;
		}

		public int getCleanId() {
			return cleanId;
		}
	}

	public static Herbs getHerb(int id) {
		for (final Herbs herb : Herbs.values())
			if (herb.getHerbId() == id)
				return herb;
		return null;
	}

	public static boolean clean(final Player player, Item item, final int slotId) {
		final Herbs herb = getHerb(item.getId());
		if (herb == null)
			return false;
		if (player.getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
			player.sm("You do not have the required level to clean this.");
			return true;
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				Item i;

				int cleanTimes = 0;

				for(int index = 0; index < player.getInventory().getItemsContainerSize(); index++) {
					if (player.getInventory().getItem(index) != null) {
						i = player.getInventory().getItem(index);
						if (i.getId() == herb.getHerbId()) {
							i.setId(herb.getCleanId());
							player.getInventory().refresh(index);
							handleTask(player);
							player.getCompCapeManager().increaseRequirement(Requirement.CLEAN_HERBS, 1);
							cleanTimes++;
						}
					}
				}
				player.getSkills().addXp(Skills.HERBLORE, cleanTimes * herb.getExperience());
				player.sm("You clean the herb.");

			}

		});
		return true;
	}

	public static boolean canCleanAndSetId(final Player player, final Item item, final int slotId) {
		final Herbs herb = getHerb(item.getId());
		if (herb == null)
			return false;
		if (player.getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
			player.sm("You do not have the required level to clean this.");
			return false;
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getSkills().addXp(Skills.HERBLORE, herb.getExperience());
				player.sm("You clean the herb.");
			}
		});
		return true;
	}
	
	private static void handleTask(Player player) {
		player.getSkillTasks().decreaseTask( SkillTasks.CLEAN1);
		player.getSkillTasks().decreaseTask(SkillTasks.CLEAN2);
		player.getSkillTasks().decreaseTask(SkillTasks.CLEAN3);
		player.getSkillTasks().decreaseTask(SkillTasks.CLEAN4);
	}

}
