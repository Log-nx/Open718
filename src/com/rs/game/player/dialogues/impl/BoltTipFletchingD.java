package com.rs.game.player.dialogues.impl;

import com.rs.game.player.actions.fletching.BoltTipFletching;
import com.rs.game.player.actions.fletching.BoltTipFletching.BoltTips;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.dialogues.Dialogue;

/**
 * Handles the Bolt Tip Fletching skill dialogue.
 * @author BigFuckinChungus
 */
public class BoltTipFletchingD extends Dialogue {

	private BoltTips tips;

	@Override
	public void start() {
		this.tips = (BoltTips) parameters[0];
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.CUT, "Choose how many you wish to fletch,<br>then click on the item to begin.", player.getInventory().getItems().getNumberOf(tips.getGemName()), new int[] { tips.getGemName() }, null);

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		player.getActionManager().setAction(
				new BoltTipFletching(tips, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {  }
}