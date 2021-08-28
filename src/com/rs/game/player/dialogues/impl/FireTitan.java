package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class FireTitan extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case 1:
			switch (componentId) {
			case OPTION_1:
				player.getPackets().sendGameMessage(
						"Why would I talk to a familiar? That's just weird.");
				end();
				break;
			case OPTION_2:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3038,
						3824, 0));
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Chat", "Teleport");
	}
}