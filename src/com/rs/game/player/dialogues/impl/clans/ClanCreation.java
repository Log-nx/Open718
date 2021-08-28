package com.rs.game.player.dialogues.impl.clans;

import com.rs.game.player.dialogues.Dialogue;

public class ClanCreation extends Dialogue {

    @Override
    public void start() {
			sendDialogue("You don't seem to be in a clan, would you like to make one?");

    }

    @Override
    public void run(int interfaceId, int componentId)  {
    	switch (stage) {
		case -1:
			if (player.isAnIronMan()) {
				sendOptionsDialogue("Would you like to create a clan for only ironmen?", "Yes", "No");
				stage = 1;
			}
			if (!player.isAnIronMan()) {
				player.getTemporaryAttributtes().put("setclan", Boolean.TRUE);
				player.getPackets().sendInputNameScript("Enter the clan name you'd like to have.");
				end();
			}
			break;
		case 1:
			switch (componentId) {
			case OPTION1:
				stage = 2;
				break;
			case OPTION2:
				stage = 3;
				break;
			}
		case 2:
			player.getTemporaryAttributtes().put("makingIronOnlyClan", true);
			player.getTemporaryAttributtes().put("setclan", Boolean.TRUE);
			player.getPackets().sendInputNameScript("Enter the clan name you'd like to have.");
			end();
			break;
		case 3:
			sendPlayerDialogue(NORMAL, "No thanks, a normal clan seems better to me.");
			player.getTemporaryAttributtes().put("setclan", Boolean.TRUE);
			player.getPackets().sendInputNameScript("Enter the clan name you'd like to have.");
			end();
			break;
		}

    }

    @Override
    public void finish() {

    }

}
