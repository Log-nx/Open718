package com.rs.game.player.dialogues.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.dialogues.Dialogue;

public class WildstalkerD extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"It's called a Wildstalker helmet, It's for all those who're willing to increase their kill counts. Anyway, would you like to get one?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Woow, sounds great.. I'd like to get one." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
			
		} else if (stage == 1) {
			if (!player.getInventory().hasFreeSlots()) {
				World.addGroundItem(new Item(20801, 1), 
						new WorldTile(player.getX(), player.getY(), player.getPlane()));
			} else {
				player.getInventory().addItem(20801, 1);
			}
			end();
			
		} else if (stage == 99) {
			end();
			
		}
	}

	@Override
	public void finish() {

	}
}
