package com.rs.game.player.dialogues.impl.araxxi;

import com.rs.game.npc.araxxi.Araxxi;
import com.rs.game.player.dialogues.Dialogue;

public final class AraxxiReward extends Dialogue {

	 /**
	  * The NPC.
	  */
	 private Araxxi npc;
	 
	 @Override
	 public void start() {
	  npc = (Araxxi) parameters[0];
	  super.sendDialogue("You search araxxi's body.");

	 }

	 @Override
	 public void run(int interfaceId, int componentId)  {
	  npc.openRewardChest(player);
	  super.end();
	 }

	 @Override
	 public void finish() { }
	 
	}
