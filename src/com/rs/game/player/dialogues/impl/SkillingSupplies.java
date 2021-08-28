package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class SkillingSupplies extends Dialogue {

	@Override
    public void finish() {

    }
    @Override
    public void run(int interfaceId, int componentId)  {
	if (stage == -1) {
	    if (componentId == OPTION_1) {
	    	 ShopsHandler.openShop(player, 10); 
	    	 end();
	    }
	    if (componentId == OPTION_2) {
	    	 ShopsHandler.openShop(player, 9);
	    	 end();
	    }
	    if (componentId == OPTION_3) {
	    	stage = 2;
	    	sendOptionsDialogue("Hoods & Capes",
	    			"Hoods",
	    			"Capes",
	    			"Achievements Capes",
	    			"Master Capes",
	    			"Back");
	    }
	    if (componentId == OPTION_4) {
	    	ShopsHandler.openShop(player, 11); 
	    	 end();
	    }
	    if (componentId == OPTION_5) {
	    	stage = 1;
	    	sendOptionsDialogue("Skilling Supplies",
	    			"Hunter Supplies",
	    			"Herblore Supplies",
	    			"Woodcutting & Mining Supplies", 
	    			"Fishing Supplies",
	    			"Potions & Food");
	    }
	 } else if (stage == 2) {
		    if (componentId == OPTION_1) {
		    	 ShopsHandler.openShop(player, 17); 
		    	 end();
		    }
		    if (componentId == OPTION_2) {
		    	 ShopsHandler.openShop(player, 16);
		    	 end();
		    }
		    if (componentId == OPTION_3) {
		    	 ShopsHandler.openShop(player, 18);
		    	 end();
		    }
		    if (componentId == OPTION_4) {
		    	 ShopsHandler.openShop(player, 19);
		    	 end();
		    }
		    if (componentId == OPTION_5) {
		    	stage = -1;
		    	sendOptionsDialogue("Skilling Store", 
		    			"Outfits",
		    			"Tools",
		    			"Hoods & Capes",
		    			"Crafting Supplies");
		    }
	    } else if (stage == 1) {
		    if (componentId == OPTION_1) {
		    	 ShopsHandler.openShop(player, 12); 
		    	 end();
		    }
		    if (componentId == OPTION_2) {
		    	player.getDialogueManager().startDialogue("JatixShop");
		    }
		    if (componentId == OPTION_3) {
		    	 ShopsHandler.openShop(player, 14); 
		    	 end();
		    }
		    if (componentId == OPTION_4) {
		    	 ShopsHandler.openShop(player, 15); 
		    	 end();
		    }
		    if (componentId == OPTION_5) {
		    	 ShopsHandler.openShop(player, 29); 
		    	 end();
		    }
	} else if (stage == 3) {
	    end();
	}}

    @Override
    public void start() {
	sendOptionsDialogue("Skilling Store", 
			"Outfits",
			"Tools",
			"Hoods & Capes",
			"Crafting Supplies",
			"More Options");
    }

}