package com.rs.game.player.content.prestige;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

public class PrestigeSystem {
	
	private static Item[] PRESTIGE_REWARDS = { new Item(1, 1) };
	
	public static int getLength() {
		return PRESTIGE_REWARDS.length;
	}
	
	private static void resetSkills(Player player) {
		for (int i = 0; i < 25; i++) {
			player.getSkills().set(i, 1);
			player.getSkills().setXp(i, 1);
			player.getSkills().init();
		}
		player.getSkills().set(3, 10);
		player.getSkills().setXp(3, 1154);
	}
	
	public static void handlePrestige(Player player) {
			if (player.isSquire()) {
				player.getBank().addItem(19864, 1, true);
			}
			if (player.isVeteran()) {
				player.getBank().addItem(19864, 5, true);
			}
			if (player.isLegend()) {
				player.getBank().addItem(19864, 10, true);
			}
			resetSkills(player);
			player.getInventory().addItem(PRESTIGE_REWARDS[Utils.random(getLength())]);
			player.setNextAnimation(new Animation(1914));
			player.setNextGraphics(new Graphics(92));
			player.prestigeLevel++;
			World.sendWorldMessage("<img=5><col=a55466>News: " + player.getDisplayName() + " has just prestiged, they have now prestiged " + player.getPrestigeLevel() + " times!", false, false);
		}
	
	public static void canPrestige(Player player) {
		for (int i = 0; i < 25;) {
			if (player.getSkills().getLevel(i) >= 99) {
				handlePrestige(player);
				return;
			} else {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 25715, "I'm sorry, you don't meet the requirements to do this.");
				return;
			}
		}
	}
	
	public void openPrestigeShops(Player player) {
	}
}
