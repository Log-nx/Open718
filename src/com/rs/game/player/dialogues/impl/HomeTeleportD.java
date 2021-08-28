package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class HomeTeleportD extends Dialogue {

    @Override
    public void finish() {

    }

    @Override
    public void run(int interfaceId, int componentId)  {
	switch (stage) {
	case -1:
	    if (componentId == OPTION_1) {
	    Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
	    removeControler(player);
		end();
	    } else if (componentId == OPTION_2) {
		player.getInterfaceManager().sendInterface(1092);
		end();
	    } else {
		end();
	    }
	    break;
	}
    }
    public final static void removeControler(Player player) {
		player.getControllerManager().removeControllerWithoutCheck();
	}
    @Override
    public void start() {
	sendOptionsDialogue("What would you like to do?", 
		     "Teleport Home",
		"Open Lodestone Network", "Nevermind");
    }
}