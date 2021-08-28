package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;
public class BossTeleports extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId)  {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				player.getDialogueManager().startDialogue("GodwarsBosses");
			}
			if (componentId == OPTION_2) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2966, 4383, 2));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3479, 9488, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(1195, 6499, 0));
				end();
			}
			if (componentId == OPTION_5) {
				stage = 2;
				sendOptionsDialogue("Select an Option", "Tormented demons",
						"Bork", "Barrelchest", "Dagannoth kings", "More Options");
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2569, 5737, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3114, 5528, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3803, 2844, 0));
				end();
			}
			if (componentId == OPTION_4) {
						if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
									player.getPackets().sendGameMessage("You need at least a summoning level of 60 to use this teleport.");
									player.getControllerManager().removeControllerWithoutCheck();
								} else {
								player.lock();
								player.getControllerManager().startController("QueenBlackDragonControler");
								
								}
							}
			if (componentId == OPTION_5) {
					stage = 4;
					sendOptionsDialogue("Select an Option", "Elvarg", "King Black Dragon", "Wildy Wyrm", 
							"Back to Start");
				}
			} else if (stage == 4) {
				if (componentId == OPTION_1) {
					Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2853, 9647, 0));
					end();
				}
				if (componentId == OPTION_2) {
								  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3067, 10255, 0)); //kbd
								  end();
				}
					  if (componentId == OPTION_3) {
						  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3170, 3872, 0)); //wildy
							end();
							closeInterface(player);
					  }
					  if (componentId == OPTION_4) {
					stage = -1;
					sendOptionsDialogue("Select an Option", "Godwars Dungeon",
							"Corporeal Beast", "Kalphite Queen", "Queen Black Dragon",
							"More Options");
				}}}
		
	private static void closeInterface(Player player) {
		player.closeInterfaces();
	}

	@Override
	public void start() {
		if (player.isLocked()) {
			end();
			return;
		}
		sendOptionsDialogue("Select an Option", "Godwars Dungeon",
				"Corporeal Beast", "Kalphite Queen", "Queen Black Dragon",
				"More Options");
	}

}