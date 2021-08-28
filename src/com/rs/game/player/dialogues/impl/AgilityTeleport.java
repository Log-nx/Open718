package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class AgilityTeleport extends Dialogue {

	@Override
    public void finish() {

    }
    @Override
    public void run(int interfaceId, int componentId)  {
	if (stage == -1) {
	    if (componentId == OPTION_1) {
	    	 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2470, 3438, 0));
	    	 end();
	    }
	    if (componentId == OPTION_2) {
	    	 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2552, 3561, 0));
	    	 end();
	    }
	    if (componentId == OPTION_3) {
	    	Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2998, 3912, 0));
	    	end();
	    }
	    if (componentId == OPTION_4) {
	    	 end();
	    }
	} else if (stage == 3) {
	    end();
	}}

    @Override
    public void start() {
	sendOptionsDialogue("Agility Teleports", 
			"Gnome",
			"Barbarian",
			"Wilderness",
			"Nevermind");
    }

}