package com.rs.game.player.dialogues;

import java.util.HashMap;

import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class DialogueHandler {

	private static final HashMap<Object, Class<? extends Dialogue>> handledDialogues = new HashMap<>();

	@SuppressWarnings({ "unchecked" })
	public static void init() {
		try {
			Class<Dialogue>[] classes = Utils.getClasses("com.rs.game.player.dialogues.impl");
			for (Class<Dialogue> c : classes) {
				if (c.isAnonymousClass()) // next
					continue;
				if (handledDialogues.put(c.getSimpleName(), c) != null)
					Logger.log(DialogueHandler.class, "ERROR, double dialogue: " + c);
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static void reload() {
		handledDialogues.clear();
		init();
	}

	public static Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue)
			return (Dialogue) key;
		Class<? extends Dialogue> classD = handledDialogues.get(key);
		if (classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}

	private DialogueHandler() {

	}
}