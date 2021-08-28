package com.rs.game.player.content.input.impl;

import com.rs.game.player.content.input.InputEvent;

/**
 * @author Tom
 *
 */
public abstract class InputNameEvent extends InputEvent<String> {

	public InputNameEvent(String text) {
		super(text);
	}

}