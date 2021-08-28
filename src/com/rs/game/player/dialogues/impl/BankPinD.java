package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;

public class BankPinD extends Dialogue {


	@Override
    public void finish() {

    }

    @Override
    public void run(int interfaceId, int componentId)  {
	if (stage == -1) {
	    if (componentId == OPTION_1) {
	    	if (player.hasPin) {
	    		player.getPackets().sendGameMessage("You already have a pin, contact a staff member if you have forgotten it.");
	    		end();
				}
			 	else {
			 		end();
			 		player.getPackets().sendGameMessage("Please type this format: '::Setpin (digits)' in the chatbox.");
		    	stage = 2;
				
	    }
	    if (componentId == OPTION_2) {
	    	if (player.hasPin) {
	    		player.getPackets().sendRunScript(108, new Object[] { "Please Enter Your PIN" });
				player.getTemporaryAttributtes().put("Remove_pin", Boolean.TRUE);
				end();
				}
			 	else {
				end();
				player.getPackets().sendGameMessage("You currently have no pin, it may be it has already been removed.");
				stage = 2;
	    }
	    if (componentId == OPTION_3) {
	    	end();
	    }
	} else if (stage == 2) {
	    end();
	}
	    end();
	    }}}

    @Override
    public void start() {
	sendOptionsDialogue("Select an Option", 
		"How can I make myself a PIN?",
		"How do I remove my PIN?", 
		"Close, nevermind.");
    }

}