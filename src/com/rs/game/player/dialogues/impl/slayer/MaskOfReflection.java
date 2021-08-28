package com.rs.game.player.dialogues.impl.slayer;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class MaskOfReflection extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Teleports", "Fremennik Slayer Dungeon", "Pollnivneach Slayer Dungeon");
		
	}
	
	@Override
	public void run(int interfaceId, int componentId) throws ClassNotFoundException {
		switch (stage) {
		case 1:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2749, 9995, 0));
			break;
		case 2:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3315, 4333, 0));
			break;
			}
		}
	
	@Override
	public void finish() {
	}

}
