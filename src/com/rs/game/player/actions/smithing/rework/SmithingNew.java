package com.rs.game.player.actions.smithing.rework;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.player.Player;
import com.rs.game.WorldObject;
import com.rs.game.item.ItemIdentifiers;

public class SmithingNew {
	
    public enum ForgingBarNew {
    	
    	ELDER_RUNE(ItemIdentifiers.ELDER_RUNE_BAR),
		
    	BANE(ItemIdentifiers.BANE_BAR),
		
    	NECRONIUM(ItemIdentifiers.NECRONIUM_BAR), 
    	
		ORIKALKUM(ItemIdentifiers.ORIKALKUM_BAR);
	
		private static Map<Integer, ForgingBarNew> bars = new HashMap<Integer, ForgingBarNew>();
	
		static {
		    for (ForgingBarNew bar : ForgingBarNew.values()) {
				bars.put(bar.getBarId(), bar);
			}
		}
	
		public static ForgingBarNew forId(int id) {
		    return bars.get(id);
		}
		
		public static ForgingBarNew getBar(Player player) {
			for (ForgingBarNew bar : ForgingBarNew.values()) {
				if (player.getInventory().containsOneItem(bar.getBarId()))
					return bar;
			}
			return null;
		}
	
		private int barId;
	
		private ForgingBarNew(int barId) {
		    this.barId = barId;
		}
	
		public int getBarId() {
		    return barId;
		}
    }
    
	public static void sendSmithingInterface(Player player, ForgingBarNew bar, WorldObject object) {
		if (bar == ForgingBarNew.ORIKALKUM) {
			player.getDialogueManager().startDialogue("OrikalkumCraftingD", null, OrikalkumCrafting.ITEMS);
			return;
		}
		if (bar == ForgingBarNew.NECRONIUM) {
			player.getDialogueManager().startDialogue("NecroniumCraftingD", null, NecroniumCrafting.ITEMS);
			return;
		}
		if (bar == ForgingBarNew.BANE) {
			player.getDialogueManager().startDialogue("BaneCraftingD", null, BaneCrafting.ITEMS);
			return;
		}
		if (bar == ForgingBarNew.ELDER_RUNE) {
			player.getDialogueManager().startDialogue("ElderRuneCraftingD", null, ElderRuneCrafting.ITEMS);
			return;
		}
	}
    
}
