package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class PkTeleports extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				
				 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3359, 3671, 0));
				end();
			}
			if (componentId == OPTION_2) {
				
				 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3071, 3649, 0));
				end();
			}
			if (componentId == OPTION_3) {
				 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3337, 3889, 0));
				end();
			}
		}
	}

	@Override
	public void start() {
		if (player.isLocked()) {
			end();
			return;
		}
		sendOptionsDialogue("Where'd you like to go?", "East Dragons",
				"Revanants",
				"Newgates ");
	}

}