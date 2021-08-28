package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

public class GodwarsBosses extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2859, 5357, 0)); //bandos
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2835, 5291, 0)); //Arma
				end();
			}
		    if (componentId == OPTION_3) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2923, 5261, 0)); //sara
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2925, 5336, 0)); //zammy
				end();
			}
			if (componentId == OPTION_5) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2905, 5203, 0)); //nex
				end();
			}}}

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Bandos", "Armadyl",
				"Saradomin", "Zamorak", "Nex");
	}

}