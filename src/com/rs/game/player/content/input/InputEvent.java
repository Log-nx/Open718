package com.rs.game.player.content.input;

/**
 * @author Tom
 *
 */
public abstract class InputEvent<T> {

	private String text;

	public InputEvent(String text) {
		this.text = text;
	}

	public abstract void onEvent(T value);

	public String getText() {
		return text;
	}

}