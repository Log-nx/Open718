package com.rs.game.player.dialogues.impl.slayer;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class MaskOfGranite extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Teleports", "Slayer Tower (Outside)", "Slayer Tower (Top Floor)", "Chaos Tunnels", "Kuradel's Dungeon (Gargoyle)");
		
	}
	
	@Override
	public void run(int interfaceId, int componentId) throws ClassNotFoundException {
		switch (stage) {
		case 1:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3419, 3524, 0));
			break;
		case 2:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3436, 3543, 3));
			break;
		case 3:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3184, 5471, 0));
			break;
		case 4:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1610, 5289, 0));
			break;
			}
		}
	
	@Override
	public void finish() {
	}

}
