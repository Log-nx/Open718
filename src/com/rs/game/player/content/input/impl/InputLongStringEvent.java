package com.rs.game.player.content.input.impl;

import com.rs.game.player.content.input.InputEvent;

/**
 * @author Tom
 *
 */
public abstract class InputLongStringEvent extends InputEvent<String> {

	public InputLongStringEvent(String text) {
		super(text);
	}

}