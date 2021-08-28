package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class RunespanPortalD extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == 1) {
			if (componentId == OPTION_1) {
			/*	player.getPackets().sendGameMessage(
						"That option isn't yet working.", true);
				end();*/
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3039, 4834, 0));
				end();
			} else if (componentId == OPTION_2) {
				teleportPlayer(3994, 6105, 1);
				end();
			} else if (componentId == OPTION_3) {
				end();
			}}
	}
	private void teleportPlayer(int placeX, int placeY, int placePlane) {
		player.setNextWorldTile(new WorldTile(placeX, placeY, placePlane));
			player.stopAll();
		player.getControllerManager().startController("RuneSpanControler");
	}
	
	@Override
	public void start() {
		sendOptionsDialogue("Where would you like to travel to?",
				"The Runecrafting Abyss",
				"Entrance into the Runespan",
				"Close, I gotta go..");
		stage = 1;
	}

}




