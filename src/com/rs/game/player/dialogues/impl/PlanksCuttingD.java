package com.rs.game.player.dialogues.impl;

import com.rs.game.player.actions.PlanksCutting;
import com.rs.game.player.actions.PlanksCutting.Planks;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.dialogues.Dialogue;

public class PlanksCuttingD extends Dialogue {

	private Planks planks;

	@Override
	public void start() {
		this.planks = (Planks) parameters[0];
		SkillsDialogue
				.sendSkillsDialogue(
						player,
						SkillsDialogue.CUT,
						"Choose how many you wish to cut,<br>then click on the item to begin.",
						player.getInventory().getItems()
								.getNumberOf(planks.getPlank()),
						new int[] { planks.getPlank() }, null);

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		player.getActionManager().setAction(
				new PlanksCutting(planks, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}
