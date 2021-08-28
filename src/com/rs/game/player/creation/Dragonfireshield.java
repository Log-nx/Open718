package com.rs.game.player.creation;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.appearance.Equipment;

public class Dragonfireshield {
	
	public static int hammer = 2347; // ItemId of the hammer
	public static int visage = 11286; // ItemId of the Draconic Visage
	public static int antishield = 1540; // ItemId of the Anti-Dragon Shield
	public static int dfs = 11283; // ItemId of the Dragonfire Shield
	public static int XP = 15; //
	public static int SMITHING = 13;
	
	
	public static void dfsCheck(Player player) {
		if (player.getInventory().containsItem(hammer, 1) && player.getInventory().containsItem(visage, 1) && player.getInventory().containsItem(antishield,1)) {
			shieldCreation(player);
		}
	}
	
	public static  void shieldCreation(Player player) {
		if (player.getInventory().containsItem(antishield, 1) && player.getInventory().containsItem(visage, 1)) {
			if(Smithing.hasLevel(player, 90)) {	
				player.setNextAnimation(new Animation(898));
				player.getInventory().deleteItem(antishield, 1);
				player.getInventory().deleteItem(visage, 1);
				player.getInventory().addItem(dfs, 1);
				player.getSkills().addXp(SMITHING, XP);
				player.getDialogueManager().startDialogue("ItemMessage", "You made a Dragonfire shield.", 11283);
				}
			}
		}
	
	public static void chargeDFS(Player player, boolean fully) {
		int shieldId = player.getEquipment().getShieldId();
		if (shieldId != 11284 && shieldId != 11283)
			return;
		if (shieldId == ItemIdentifiers.DRAGONFIRE_SHIELD) {
			player.getEquipment().getItem(Equipment.SLOT_SHIELD)
					.setId(ItemIdentifiers.DRAGONFIRE_SHIELD);
			player.getEquipment().refresh(Equipment.SLOT_SHIELD);
			player.getAppearence().generateAppearenceData();
		}
		if (player.getCharges().getCharges(ItemIdentifiers.DRAGONFIRE_SHIELD) == 50) {
			player.getPackets().sendGameMessage("Your dragonfire shield is already full.", true);
			return;
		}
		player.getCharges().addCharges(11283, fully ? 50 : 1, Equipment.SLOT_SHIELD);
		player.getCombatDefinitions().refreshBonuses();
		player.setNextAnimationNoPriority(new Animation(6695));
		player.setNextGraphics(new Graphics(1164));
		player.getPackets().sendGameMessage("Your dragonfire shield glows more brightly.", true);
	}
	
}
