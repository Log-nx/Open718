package com.rs.game.player.dialogues.impl.slayer;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class MaskOfBrokenFingers extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Teleports", "Taverley Slayer Dungeon", "Slayer Tower (Outside)", "Slayer Tower", "Meiyerditch dungeon");
		
	}
	
	@Override
	public void run(int interfaceId, int componentId) throws ClassNotFoundException {
		switch (stage) {
		case 1:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2218, 4532, 0));
			break;
		case 2:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3419, 3524, 0));
			break;
		case 3:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3423, 3544, 0));
			break;
		case 4:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3610, 9736, 0));
			break;
			}
		}
	
	@Override
	public void finish() {
	}

}
