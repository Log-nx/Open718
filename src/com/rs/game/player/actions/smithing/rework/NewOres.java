package com.rs.game.player.actions.smithing.rework;

import com.rs.game.player.Player;

public class NewOres {
	
	public static void handleSmithing(Player player, int componentId) {
		switch (componentId) {
		case 18:
			player.getDialogueManager().startDialogue("ElderRuneCraftingD", null, NecroniumCrafting.ITEMS);
			break;
		case 17:
			player.getDialogueManager().startDialogue("ElderRuneCraftingD", null, NecroniumCrafting.ITEMS);
			break;
		case 16:
			player.getDialogueManager().startDialogue("BaneCraftingD", null, BaneCrafting.ITEMS);
			break;
		case 15:
			player.getDialogueManager().startDialogue("NecroniumCraftingD", null, NecroniumCrafting.ITEMS);
			break;
		case 14:
			player.getDialogueManager().startDialogue("OrikalkumCraftingD", null, OrikalkumCrafting.ITEMS);
			break;
		}
	}
}
