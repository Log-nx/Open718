package com.rs.game.player.actions.assassin;

import java.io.Serializable;
import java.util.Random;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.summoning.Summoning;

import com.rs.game.player.actions.summoning.Summoning.Pouches;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * @author Miles Black (bobismyname)
 */

public class Assassins implements Serializable {

	public enum Tasks {

		GLACOR(14301, 15, 35, 140, 130), 
		KING_BLACK_DRAGON(50, 40, 80, 100, 150), 
		BLINK(12878, 25, 30, 220, 280), 
		YK_LAGOR(11872, 5, 20, 350, 1200), 
		SUNFREET(15222, 40, 80, 280, 360), 
		WILDY_WYRM(3334, 15, 30, 220, 400), 
		CORPOREAL_BEAST(8133, 10, 20, 300, 660), 
		NEX(13447, 5, 10, 1000, 2700), 
		GENERAL_GRAARDOR(6260, 25, 50, 250, 300), 
		COMMANDER_ZILYANA(6247, 25, 50, 250,300), 
		KRIL_TSUTSAROTH(6203, 25, 50, 250, 300), 
		KREE_ARRA(6222,25, 50, 250, 300), 
		GIANT_MOLE(3340, 35, 50, 75, 400), 
		KALPHITE_KING(16697, 5, 20, 300, 660), 
		KALPHITE_QUEEN(3836, 25, 40, 120, 340),
		ARAXXI(19464, 5, 20, 400, 600),
		VORAGO(17182, 5, 20, 350, 660),
		QUEENBLACKDRAGON(15454, 15 , 30, 250 ,660),
		PRIMUS(17149, 5 , 15 , 400, 500),
		QUINTUS(17153, 5 , 15 , 400, 500),
		QUARTUS(17152 , 5 , 15 , 400 , 500),
		TERTIUS(17151, 5 , 15 , 400, 500),
		SEXDUS(17154, 5 , 15 , 400, 500),
		SECUNDUS(17150, 5 , 15 , 400, 500);




		private final int id;
		private final int min;
		private final int max;
		private final int xp;
		private final int speed;

		Tasks(int id, int min, int max, int xp, int speed) {
			this.id = id;
			this.min = min;
			this.max = max;
			this.xp = xp;
			this.speed = speed;
		}

		public int getId() {
			return id;
		}

		public int getMax() {
			return max;
		}

		public int getMin() {
			return min;
		}

		public int getSpeed() {
			return speed;
		}

		public int getXp() {
			return xp;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8035331642599855251L;
	private transient Player player;
	private Tasks currentTask;
	private int taskAmount;
	private int taskSpeed;
	private int taskMode;
	private int taskWeapon;
	private int killedTasks;
	public static int[] weapons = { 1319, 1333, 1303, 1215, 11694, 11696,
		11698, 11700, 11730, 4587, 1377, 1434, 9185, 4718 };

	public static int[] ranged = { 4734, 9181, 9183, 9185, 859, 861, 868,
		11230, 14684 };

	public void activateCallAssassin() {
		player.sm("You have successfully called an assassin.");
		final Pouches pouches = Pouches.forId(8422);
		Summoning.spawnFamiliar(player, pouches);
		player.callAssassin = true;
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 6000;

			@Override
			public void run() {
				if (loop == 1) {
					player.sm("Your ability has cooled down, you may now activate it again.");
					player.callAssassin = false;
				}
				loop--;
			}
		}, 0, 1);
	}

	public void activateFinalBlow() {
		player.sm("Your final blow ability will last for 10 minutes.");
		player.finalBlowCoolDown = true;
		player.finalblow = true;
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 1200;

			@Override
			public void run() {
				if (loop == 600) {
					player.sm("Your ability has run out, please let it cool down.");
					player.finalblow = false;
				} else if (loop == 1) {
					player.sm("Your ability has cooled down, you may now activate it again.");
					player.finalBlowCoolDown = false;
				}
				loop--;
			}
		}, 0, 1);
	}

	public void activateStealthMoves() {
		player.sm("Your stealth moves ability will last for 30 minutes.");
		player.stealthMovesCoolDown = true;
		player.stealth = true;
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 2400;

			@Override
			public void run() {
				if (loop == 1800) {
					player.sm("Your ability has run out, please let it cool down.");
					player.stealth = false;
				} else if (loop == 1) {
					player.sm("Your ability has cooled down, you may now activate it again.");
					player.stealthMovesCoolDown = false;
				}
				loop--;
			}
		}, 0, 1);
	}

	public void activateSwiftSpeed() {
		player.sm("Your swift speed ability will last for 10 minutes.");
		player.swiftspeed = true;
		player.swiftness = true;
		final int time = player.getSkills()
				.getAssassinLevel(Skills.SWIFT_SPEED) * 30;
		WorldTasksManager.schedule(new WorldTask() {
			int loop = time;

			@Override
			public void run() {
				if (loop == (time / 2)) {
					player.sm("Your ability has run out, please let it cool down.");
					player.swiftness = false;
				} else if (loop == 1) {
					player.sm("Your ability has cooled down, you may now activate it again.");
					player.swiftspeed = false;
				}
				loop--;
			}
		}, 0, 1);
	}

	public double addExp() {
		return currentTask.getXp();
	}

	public void completeTask() {
		this.taskAmount--;
		player.sm("You only have about " + taskAmount + " left to go!");
		player.getSkills().addAssassinXp(getGameMode(), addExp());
		if (taskAmount <= 0) {
			player.sm("You have successfully completed your task and have received an Assassin's Favor Point.");
			player.getSkills().addAssassinXp(0, addExp() * 5);
			//player.setAssassinTokens(player.getAssassinTokens() + (GlobalEvents.isActiveEvent(Event.ASSASSIN_BOOST) ? 2 : 1));
			resetTask();
		}
	}

	public int getAmount() {
		return taskAmount;
	}

	public int getGameMode() {
		return taskMode;
	}

	public int getKilledTasks() {
		return killedTasks;
	}

	public String getName() {
		final int i = getNpcId();
		final NPCDefinitions def = NPCDefinitions.getNPCDefinitions(i);
		return def.getName();
	}

	public int getNpcId() {
		return currentTask.getId();
	}

	public int getSpeed() {
		return taskSpeed;
	}

	public Tasks getTask() {
		return currentTask;
	}

	public void getTask(int mode) {
		if (currentTask == null) {
			final int pick = new Random().nextInt(Tasks.values().length);
			final Tasks task = Tasks.values()[pick];
			final int amount = Utils.random(task.getMin(), task.getMax());
			this.taskMode = mode;
			setCurrentTask(task, amount);
			if (mode == 1) {
				this.taskAmount = taskAmount * 5;
			} else if (mode == 2) {
				if (currentTask.getId() == 6222)
					this.taskWeapon = ranged[Utils.random(ranged.length)];
				else
					this.taskWeapon = weapons[Utils.random(weapons.length)];
			} else if (mode == 3) {
				this.taskSpeed = taskAmount * currentTask.getSpeed();
				speedTask();
				player.getInterfaceManager().sendCountDown();
			} else if (mode == 4) {
				this.killedTasks = taskAmount;
			}
		} else {
			player.sm("You already have a task.");
		}
	}

	public int getWeapon() {
		return taskWeapon;
	}

	public String getWeaponName() {
		final int i = taskWeapon;
		final ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
		return def.getName();
	}

	public void resetTask() {
		setCurrentTask(null, 0);
	}

	public void setCurrentTask(Tasks task, int amount) {
		this.currentTask = task;
		this.taskAmount = amount;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void speedTask() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop = taskSpeed;

			@Override
			public void run() {
				if (loop <= 0 || taskAmount <= 0) {
					stop();
					player.getInterfaceManager().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 34 : 0, 57);
					player.getInterfaceManager().closeOverlay(false);
				}
				loop--;
				taskSpeed--;
				if (taskAmount > 0)
					player.getPackets().sendIComponentText(57, 1, "You currently have " + taskSpeed + " seconds left to kill " + taskAmount + " " + getName() + ".");
			}
		}, 0, 1);
	}

}