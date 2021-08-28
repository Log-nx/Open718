 package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.game.player.content.achievements.Achievement;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class AchievementD extends Dialogue {

	private int npcId;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 0:
			stage = 1;
			sendOptionsDialogue("Select an Option", 
					"Open Achievement System",
					"Achievement Store",
					"Nevermind");
			
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				 Achievement.sendInterface(player);
				 end();
				break;
			case OPTION_2:
				ShopsHandler.openShop(player, 2);
				end();
				break;
			case OPTION_3:
				end();
				break;
			}
			break;
		case 4:
			end();
			break;
		}

	}

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		stage = 0;
		sendNPCDialogue(
				npcId,
				9827,
				"Hello there young skiller, I am the skiller master of "+ Settings.SERVER_NAME +"..<br>In short, I am here to offer you my brand new achievements with rewards!");
	}
}