package com.rs.game.player.content.activities.skillingtask;

import java.io.Serializable;
import java.util.ArrayList;

import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class SkillerTasks implements Serializable {
	private static final long serialVersionUID = 3535856484888897359L;

	public final static int EASY = 0;
	public final static int MEDIUM = 1;
	public final static int HARD = 2;
	public final static int ELITE = 3;

	public SkillTasks task;
	private int taskAmount;
	private transient Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Adds all tasks to an array that they meet the requirements then picks a
	 * random task from that list.
	 * 
	 * @param tier difficulty
	 */
	public SkillTasks getNewTask(int tier) {
		ArrayList<SkillTasks> tasks = new ArrayList<>();
		for (SkillTasks t : SkillTasks.values())
			if (t.getDifficulty() == tier)
				tasks.add(t);
		setCurrentTask(tasks.get(Utils.random(tasks.size() - 1)));
		setTaskAmount(task.getAmount());
		return task;
	}

	/**
	 * Set's the current task to be completed. if null, no task
	 * 
	 * @param task
	 */
	public void setCurrentTask(SkillTasks task) {
		this.task = task;
	}

	/**
	 * Returns the current assigned task
	 * 
	 * @return task
	 */
	public SkillTasks getCurrentTask() {
		return task;
	}

	/**
	 * Sets the amount of the task to be completed. Decreases by 1 until ask is
	 * 0, at which points its completed.
	 * 
	 * @param amount
	 */
	private void setTaskAmount(int amount) {
		this.taskAmount = amount;
	}

	public static void sendTaskInfo(Player player) {
		if (player.getSkillTasks().getCurrentTask() != null) {
			player.getPackets().sendGameMessage("Current Skiller Task: " + player.getSkillTasks().getCurrentTask().getAssignment());
			player.getPackets().sendGameMessage("Amount Left: " +player. getSkillTasks().getTaskAmount());
		if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.EASY)
			player.getPackets().sendGameMessage("Task Difficulty: Easy");
		else if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.MEDIUM)
			player.getPackets().sendGameMessage("Task Difficulty: Medium");
		else if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.HARD)
			player.getPackets().sendGameMessage("Task Difficulty: Hard");
		else if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.ELITE)
			player.getPackets().sendGameMessage("Task Difficulty: Elite");
		}
	}
	
	/**
	 * Returns the current task amount.
	 * 
	 * @return taskAmount
	 */
	public int getTaskAmount() {
		return taskAmount;
	}

	/**
	 * Checks if player has a task, if returns null, means there is no task.
	 * 
	 * @return
	 */
	public boolean hasTask() {
		return task != null;
	}

	/**
	 * Checks if their task is completed.
	 * 
	 * @return true if they have a task, and the amount remaining is 0,
	 *         otherwise false
	 */
	public boolean isCompleted() {
		return hasTask() && taskAmount == 0;
	}

	/**
	 * Decreases the amount remaining for the task, if it hits 0 player will
	 * receive a message only one time.
	 */
	public void decreaseTask(SkillTasks stask) {
		if (!hasTask()|| taskAmount == 0|| !task.getAssignment().equalsIgnoreCase(stask.getAssignment())) {
			return;
		}
		taskAmount--;
		if (taskAmount == 0) {
			if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.EASY) {
				player.setTaskPoints(player.getTaskPoints() + 1);
				player.getInventory().addItemMoneyPouch(ItemIdentifiers.COINS, 100000);
			} else if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.MEDIUM) {
				player.setTaskPoints(player.getTaskPoints() + 3);
				player.getInventory().addItemMoneyPouch(ItemIdentifiers.COINS, 300000);
			} else if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.HARD) {
				player.setTaskPoints(player.getTaskPoints() + 7);
				player.getInventory().addItemMoneyPouch(ItemIdentifiers.COINS, 700000);
			} else if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.ELITE) {
				player.setTaskPoints(player.getTaskPoints() + 10);
				player.getInventory().addItemMoneyPouch(ItemIdentifiers.COINS, 1000000);
			}
			player.getSkillTasks().setCurrentTask(null);
			player.sm("<col=00FFFF>Your task has been completed! Return to Max for another assignment!</col>");
		}
	}

}