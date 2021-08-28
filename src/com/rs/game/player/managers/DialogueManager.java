package com.rs.game.player.managers;

import java.io.IOException;

import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.dialogues.DialogueHandler;

public class DialogueManager {

    private Player player;
    private Dialogue lastDialogue;

    public DialogueManager(Player player) {
    	this.player = player;
    }
    
    public void startDialogue(Object key, Object... parameters) {
		if (!player.getControllerManager().useDialogueScript(key))
			return;
		if (lastDialogue != null)
			lastDialogue.finish();
		lastDialogue = DialogueHandler.getDialogue(key);
		if (lastDialogue == null)
			return;
		lastDialogue.parameters = parameters;
		lastDialogue.setPlayer(player);
		lastDialogue.start();
	}

	public boolean continueDialogue(String input) {
		if (lastDialogue == null)
			return false;
		return lastDialogue.run(input);
	}

	public boolean continueDialogue(int input) {
		if (lastDialogue == null)
			return false;
		return lastDialogue.run(input);
	}

	public void continueDialogue(int interfaceId, int componentId) throws ClassNotFoundException {
		if (lastDialogue == null)
			return;
		lastDialogue.run(interfaceId, componentId);
	}
    public void finishDialogue() {
		if (lastDialogue == null)
		    return;
		lastDialogue.finish();
		lastDialogue = null;
		if (player.getInterfaceManager().containsChatBoxInter())
		    player.getInterfaceManager().closeChatBoxInterface();
	}
}
