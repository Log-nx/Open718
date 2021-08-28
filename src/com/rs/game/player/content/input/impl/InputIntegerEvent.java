package com.rs.game.player.content.input.impl;

import com.rs.game.player.content.input.InputEvent;

/**
 * @author Tom
 *
 */
public abstract class InputIntegerEvent extends InputEvent<Integer> {

	public InputIntegerEvent(String text) {
		super(text);
	}

}