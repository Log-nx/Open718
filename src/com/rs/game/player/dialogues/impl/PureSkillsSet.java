package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.Skills;
import com.rs.game.player.dialogues.Dialogue;

public class PureSkillsSet extends Dialogue {

	@Override
	public void start() {
		sendDialogue("When you press the Continue button, "
				+ "you agree to your following skills will be increased / decreased to level <u>99</u> (<u>13m EXP</u>) "
				+ "attack, strength, defence, constitution, ranged, magic & 52 prayer. "
				+ "It's your own risk.<br> <u>No experience will be given back.</u>");
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
					"Yes, it is my own choice!", "No way, I don't want to lose my experiences!");
		} else if (stage == 0) {
			if (componentId == OPTION_1) 
				player.getSkills().set(0, 99); //Attack
			player.getSkills().set(2, 99); //Strength
			player.getSkills().set(3, 99); //Hitpoints
			player.getSkills().set(4, 99); //Ranged
			player.getSkills().set(5, 52); //Prayer
			player.getSkills().set(6, 99); //Magic
			player.getSkills().setXp(0, Skills.getXPForLevel(99));
			player.getSkills().setXp(2, Skills.getXPForLevel(99));
			player.getSkills().setXp(3, Skills.getXPForLevel(99));
			player.getSkills().setXp(4, Skills.getXPForLevel(99));
			player.getSkills().setXp(5, Skills.getXPForLevel(52));
			player.getSkills().setXp(6, Skills.getXPForLevel(99));
			player.pure = true;
			end();
		}
	}

	@Override
	public void finish() {

	}

}
