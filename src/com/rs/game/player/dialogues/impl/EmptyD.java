package com.rs.game.player.dialogues.impl;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.managers.LendingManager;
import com.rs.utils.Colors;
import com.rs.utils.Lend;

public class EmptyD extends Dialogue {

	@Override
	public void start() {
		if (!player.emptyWarning)
			sendDialogue(Colors.RED+"Warning: this will delete all items in your inventory!");
		else {
			finish();
			handleEmpty();
		}
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		switch (stage) {
		case -1:
			sendOptionsDialogue("Empty inventory?", "Yes.", "No.", "Yes (and never ask again).");
			stage = 0;
			break;
		case 0:
			switch (componentId) {
			case OPTION_1:
				finish();
				handleEmpty();
				break;
			case OPTION_2:
				finish();
				break;
			case OPTION_3:
				sendDialogue(Colors.RED+"WARNING</col>: Next time you issue the empty command your items will " + "automatically be removed without a warning and on accidents - your items will NOT be returned.");
				player.emptyWarning = true;
				stage = 1;
				break;
			}
			break;
		case 1:
			finish();
			handleEmpty();
			break;
		}
	}

	@Override
	public void finish() { player.getInterfaceManager().closeChatBoxInterface(); }
	
	/**
	 * Handles the actual inventory deletion.
	 */
	private void handleEmpty() {
		Lend lend = LendingManager.getLend(player);
		if (lend != null) {
			Player lender = World.getPlayer(lend.getLendee());
			if (lender != null && 
					lender.getInventory().containsOneItem(lend.getItem().getDefinitions().getLendId()))
				LendingManager.unLend(lend);
		}
	    player.getInventory().reset();
	}
}
