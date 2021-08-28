package com.rs.game.player.dialogues.impl.araxxi;

import com.rs.game.WorldTile;
import com.rs.game.player.controllers.AraxyteHyveController;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Colors;

public class AraxHyveD extends Dialogue {

    @Override
    public void start() {
		sendDialogue(Colors.DARK_RED+"Beyond this point is the Araxyte hive.", 
				Colors.DARK_RED+"There is no way out other than death or Victory.",
				Colors.DARK_RED+"Only those who can endure dangerous encounters should proceed.");
    }

    @Override
    public void run(int interfaceId, int componentId)  {
    	switch (stage) {
    	case -1:
    		int players = AraxyteHyveController.getPlayers().size();
    		stage = 0;
    		sendOptionsDialogue("There "+(players == 1 ? "is" : "are")+" currently " + players + " "
    				+ (players == 1 ? "player" : "players")+" fighting", "Climb down.", "Stay here.");
    		break;
    	case 0:
    		if (componentId == OPTION_1) {
	    		player.setNextWorldTile(new WorldTile(4582, 6265, 1));
	    		player.getControllerManager().startController("AraxyteHyveController");
 		    }
 		    end();
    		break;
    	}
    }

    @Override
    public void finish() {  }

}
