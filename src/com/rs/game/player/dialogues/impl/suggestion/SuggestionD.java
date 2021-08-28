package com.rs.game.player.dialogues.impl.suggestion;

import com.rs.game.player.content.custom.SuggestionManager;
import com.rs.game.player.dialogues.Dialogue;

public class SuggestionD extends Dialogue {

	int npcId;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue("What would you like to say?",
					"I want to make a suggestion", "How do suggestions work?",
					"Who are you?", "Who am I?", "Goodbye");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				suggest();
			} else if (componentId == OPTION_2) {
				stage = 4;
				sendNPCDialogue(
						npcId,
						Dialogue.CALM,
						"Players submit a suggestion. Then, the suggestion is voted on with ::agree and ::disagree. Each suggestion will be considered by the Staff and Developerment team.");
			} else if (componentId == OPTION_3) {
				stage = 1;
				sendNPCDialogue(
						npcId,
						Dialogue.CALM,
						"I'm the guy who's here to try. I won't make you cry, I'll make you buy. So try to tell me that my eye is shy.");
			} else if (componentId == OPTION_4) {
				stage = 2;
				sendNPCDialogue(npcId, Dialogue.CALM,
						"You're the one who can change Diccus! You're the chosen one!");
			} else if (componentId == OPTION_5) {
				stage = 3;
				sendPlayerDialogue(Dialogue.CALM, "Goodbye!");
			} else
				end();
		} else if (stage == 1) {
			stage = -1;
			sendPlayerDialogue(Dialogue.CALM, "I agree with you 100%!");
		} else if (stage == 2) {
			stage = -1;
			sendPlayerDialogue(Dialogue.CALM,
					"You're right! In fact... I'm gonna make a suggestion right now!");
		} else if (stage == 3) {
			stage = -2;
			sendNPCDialogue(npcId, Dialogue.CALM, "Okay...");
		} else if (stage == 4) {
			stage = 5;
			sendNPCDialogue(npcId, Dialogue.CALM,
					"Your name will be anonymous. Players will only see your suggestion.");
		} else if (stage == 5) {
			stage = 6;
			sendPlayerDialogue(Dialogue.CALM,
					"What if my suggestion isn't good?");
		} else if (stage == 6) {
			stage = -1;
			sendNPCDialogue(npcId, Dialogue.CALM,
					"I guess we'll have to find out if it's good, won't we?");
		} else
			end();
	}

	@Override
	public boolean run(String suggestion) {
		if (suggestion.length() <= 8) {
			stage = -2;
			sendNPCDialogue(npcId, Dialogue.CALM, "That's a really short suggestion...");
			return true;
		}
		player.getTemporaryAttributtes().put("suggestion", (System.currentTimeMillis() + (1000 * 60 * 5)));
		SuggestionManager.addSuggestion(player.getUsername(), suggestion);
		player.getInterfaceManager().closeChatBoxInterface();
		stage = -2;
		sendNPCDialogue(npcId, Dialogue.HAPPY, "Thanks for your suggestion!");
		return true;
	}

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		stage = -1;
		sendNPCDialogue(npcId, 9827, "Hello " + player.getDisplayName());
	}

	// sendPlayerDialogue(9827, "I don't want anything to do with you.");

	public void suggest() {
		// check playing time
		if (SuggestionManager.bannedFromSuggestions(player.getUsername())) {
			stage = -2;
			sendNPCDialogue(npcId, Dialogue.CALM,
					"You can't make anymore suggestions!");
			return;
		}
		final Long time = (Long) player.getTemporaryAttributtes().get("suggestion");
		if (time != null) {
			if (System.currentTimeMillis() < time) {
				stage = -2;
				sendNPCDialogue(
						npcId,
						Dialogue.CALM,
						"You've already suggested something in the last 5 minutes. Thanks for your time though.");
				return;
			}
		}
		if (!SuggestionManager.canAddSuggestion(player.getUsername())) {
			stage = -2;
			sendNPCDialogue(
					npcId,
					Dialogue.CALM,
					"You've already make enough suggestions. We need to let other players make suggestions as well.");
			return;
		}
		player.getPackets().sendInputLongTextScript("Enter your suggestion:");
		return;
	}

}