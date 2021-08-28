package com.rs.game.player.dialogues.impl.slayer;

import com.rs.game.player.content.interfaces.SophanemChestInterface;
import com.rs.game.player.dialogues.Dialogue;

public class SophanemChest extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("<col=ff0000>This will completely delete this chest.</col>", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
			case OPTION_1:
				player.getSophanemChest().getContainer().clear();
				break;				
			case OPTION_2:
				break;
		}
		player.getInterfaceManager().sendInterface(new SophanemChestInterface(player));
		end();
	}

	@Override
	public void finish() {

	}

}