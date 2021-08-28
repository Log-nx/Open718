package com.rs.game.player.content.clans;

import com.rs.game.player.content.clans.Censor;
import com.rs.net.decoders.handlers.chat.QuickChatMessage;
import com.rs.utils.Utils;



public class ChatMessage {

    private String message;
    private String filteredMessage;

    public ChatMessage(String message) {
	if (!(this instanceof QuickChatMessage)) {
	    filteredMessage = Censor.getFilteredMessage(message);
	    this.message = Utils.fixChatMessage(message);
	} else
	    this.message = message;
    }

    public String getMessage(boolean filtered) {
	return filtered ? filteredMessage : message;
    }
}