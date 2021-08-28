package com.rs.game.player.dialogues.impl;

import com.rs.game.item.Item;
import com.rs.game.player.Skills;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Utils;

public class CombatReseting extends Dialogue {

	private int npcId = 11508;

	private int skillResetCost;

	private boolean remove, alreadyMin;

	@Override
	public void finish() {
	}

	public boolean resetSkillToZero(int SKILLID) {
		int RESETSKILL = SKILLID == 3 ? 10 : 1;
		skillResetCost = player.getSkills().getLevel(SKILLID) * 10000;
		if (player.getSkills().getLevel(SKILLID) == RESETSKILL) {
			alreadyMin = true;
			return false;
		}
		if (player.getInventory().containsItem(995, skillResetCost)) {
			player.getInventory().deleteItem(new Item(995, skillResetCost));
			remove = true;
		}
		if (!remove) {
			return false;
		}
		stage = -2;
		player.getSkills().set(SKILLID, RESETSKILL);
		player.getSkills().setXp(SKILLID, Skills.getXPForLevel(RESETSKILL));
		player.getAppearence().generateAppearenceData();
		return true;
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			sendPlayerDialogue(9827,
					"I am fine with that. Let me choose what skill.");
			stage = 4;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				if (resetSkillToZero(Skills.ATTACK)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Attack</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}

			} else if (componentId == OPTION_2) {
				if (resetSkillToZero(Skills.STRENGTH)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Strength</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}

			} else if (componentId == OPTION_3) {
				if (resetSkillToZero(Skills.DEFENCE)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Defence</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}

			} else if (componentId == OPTION_4) {
				if (resetSkillToZero(Skills.RANGE)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Ranged</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}

			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Which skill do you wish to reset?",
						"I'd like to reset: <col=ff0000>Prayer", "I'd like to reset: <col=ff0000>Magic", "I'd like to reset: <col=ff0000>Constitution",
						"I'd like to reset: <col=ff0000>Summoning");
				stage = 2;
			}

		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				if (resetSkillToZero(Skills.PRAYER)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Prayer</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}
			} else if (componentId == OPTION_2) {
				if (resetSkillToZero(Skills.MAGIC)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Magic</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}
			} else if (componentId == OPTION_3) {
				if (resetSkillToZero(Skills.HITPOINTS)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Constitution</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}
			} else if (componentId == OPTION_4) {
				if (resetSkillToZero(Skills.SUMMONING)) {
					sendNPCDialogue(
							npcId,
							9827,
							"Thank you, your request has been completed, you have now reset your skill <col=ff0000>Summoning</col> for <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> coins.");
				} else if (!alreadyMin) {
					sendNPCDialogue(
							npcId,
							9827,
							"You need <col=ff0000>"
									+ Utils.getFormattedNumber(skillResetCost)
									+ "</col> to reset this skill.");
					stage = -2;
				} else {
					sendDialogue("Your skill is already at the lowest level, be please choose another skill to reset.");
					stage = -2;
				}
			}
		} else if (stage == -2) {
			end();
		} else if (stage == 4) {
			sendOptionsDialogue("Which skill do you wish to reset?",
					"I'd like to reset: <col=ff0000>Attack", "I'd like to reset: <col=ff0000>Strength", "I'd like to reset: <col=ff0000>Defence",
					"I'd like to reset: <col=ff0000>Ranged", "More Options");
			stage = 1;
		}
	}

	@Override
	public void start() {
		if (player.getEquipment().wearingArmour()) {
			sendDialogue("Please remove your armour first.");
			stage = -2;
		} else {
			sendNPCDialogue(npcId, 9827,
					"Are you sure you want to reset your skills? Think carefully before doing this, because this can't be undone! I'm only allowed to reset skills to level <col=ff0000>1</col>, and it'll only cost you 10,000 GP for one level (10K / Level).");
		}
	}

}