package com.rs.game.player.dialogues.impl.dungeoneering;

import com.rs.game.player.Skills;
import com.rs.game.player.content.dungeoneering.Dungeon;
import com.rs.game.player.dialogues.Dialogue;

public class DungFloorSelectD extends Dialogue {

	@Override
	public void start() {
		sendDialogue("Select the type of dungeongeering floor that will suit your dungeoneering level.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (player.hasFamiliar()) { //player.getInventory().getFreeSlots() == 28)|| player.getEquipment().wearingArmour() ||
			player.getPackets().sendGameMessage("You're not allowed to bring any familiars.");
			end();
			return; 
		}
		if (stage == 1) {
			sendOptionsDialogue("Floor Selection", "Easy - Level 1 Dungeoneering", 
												   "Medium - Level 35 Dungeoneering", 
												   "Large - Level 50 Dungeoneering", 
												   "Hard - Level 75 Dungeoneering", 
												   "Extreme - Level 95 Dungeoneering");
			stage = 2;
		} else if (stage == 2) {
			switch(componentId) {
			case 11:
				player.setDungeon(new Dungeon(player, 0));
				end();
				break;
			case 13:
				if (player.getSkills().getLevelForXp(Skills.DUNGEONEERING) > 34) {
					player.setDungeon(new Dungeon(player, 1));
				} else {
					player.getPackets().sendGameMessage("You need at least level 35 in dungeoneering to access the Medium floor.");
				}
				end();
				break;
			case 14:
				if (player.getSkills().getLevelForXp(Skills.DUNGEONEERING) > 49) {
					player.setDungeon(new Dungeon(player, 2));
				} else {
					player.getPackets().sendGameMessage("You need at least level 50 in dungeoneering to access the Large floor.");
				}
				end();
				break;
			case 15:
				if (player.getSkills().getLevelForXp(Skills.DUNGEONEERING) > 74) {
					player.setDungeon(new Dungeon(player, 3));
				} else {
					player.getPackets().sendGameMessage("You need at least level 75 in dungeoneering to access the Hard floor.");
				}
				end();
				break;
			case 16:
				if (player.getSkills().getLevelForXp(Skills.DUNGEONEERING) > 94) {
					player.setDungeon(new Dungeon(player, 4));
				} else {
					player.getPackets().sendGameMessage("You need at least level 95 in dungeoneering to access the Extreme floor.");
				}
				end();
				break;
			default:
				end();
			}
		}
	}

	@Override
	public void finish() {

	}
}