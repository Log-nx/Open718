package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.game.player.dialogues.Dialogue;

/** 
 * @author Enth
 */
public class WebLinks extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
			sendOptionsDialogue("Quick Links",
					"Website",
					"Community",
					"Voting Page",
					"Upgrade Your Account");
			stage = 1;
	}
	}

	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				//player.getPackets().sendOpenURL(Settings.WEBSITE_LINK);
				stage = 2;
			}
			if (componentId == OPTION_2) {	
				player.getPackets().sendOpenURL(Settings.FORUMS_LINK);
				stage = 2;
				}
			if (componentId == OPTION_3) {
				player.getPackets().sendOpenURL(Settings.VOTE_LINK);
				stage = 2;
			}
			if (componentId == OPTION_4) {
				player.getPackets().sendOpenURL(Settings.DONATE_LINK);
				stage = 2;
			}
		}
		if (stage == 2) {
			end();
			player.getInterfaceManager().closeChatBoxInterface();
		}
	}
	
	@Override
	public void finish() {

	}

}