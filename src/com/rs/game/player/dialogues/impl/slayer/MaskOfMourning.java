package com.rs.game.player.dialogues.impl.slayer;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class MaskOfMourning extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Teleports", "Morytania Slayer Tower", "Morytania Slayer Tower (First Floor)", "Pollnivneach Slayer Dungeon (Mighty Banshees)");
		
	}
	
	@Override
	public void run(int interfaceId, int componentId) throws ClassNotFoundException {
		switch (stage) {
		case 1:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3419, 3524, 0));
			break;
		case 2:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3423, 3544, 0));
			break;
		case 3:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3357, 9353, 0));
			break;
			}
		}
	
	@Override
	public void finish() {
	}

}
