package com.rs.net.decoders.handlers.chat;

import com.rs.game.player.content.clans.ChatMessage;


public class PublicChatMessage extends ChatMessage {

	  private int effects;

	  public PublicChatMessage(String message, int effects) {
		  super(message);
		  this.effects = effects;
	  }

	  public int getEffects() {
		  return effects;
	  }

}