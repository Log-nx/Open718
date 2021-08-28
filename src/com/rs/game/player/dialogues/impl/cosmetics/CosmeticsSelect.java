package com.rs.game.player.dialogues.impl.cosmetics;

import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.dialogues.Dialogue;

public class CosmeticsSelect extends Dialogue {

	private int slotId;
	private int choosenOption;

	@Override
	public void start() {
		slotId = (int) this.parameters[0];
		choosenOption = -1;
		if (slotId == Equipment.SLOT_RING) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Full outfits", "Saved costumes", "Reset costume",
					"Nevermind");
			return;
		}
		if (slotId == Equipment.SLOT_ARROWS)
			slotId = 18;
		if (player.getEquipment().getCosmeticItems().get(slotId) != null) {
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Remove current costume", "Nevermind");
		} else
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Search outfits by name.", "View all outfits.", "Cancel.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (player.getEquipment().getCosmeticItems().get(slotId) != null) {
			if (componentId == OPTION_1)
				player.getEquipment().getCosmeticItems().set(slotId, null);
			player.getAppearence().generateAppearenceData();
			end();
			return;
		}
		switch (stage) {
		case -1:
			switch (componentId) {
			case OPTION_1:
				end();
				player.getTemporaryAttributtes().put("CosmeticsKeyWord", slotId);
				player.getTemporaryAttributtes().put("CosmeticsChoosenOption", choosenOption);
				player.getPackets().sendInputNameScript("Enter the name of the outfit you would like to search for: ");
				break;
			case OPTION_2:
				end();
				if (choosenOption != -1)
					player.getDialogueManager().startDialogue("CosmeticsD", slotId, null, choosenOption);
				else
					player.getDialogueManager().startDialogue("CosmeticsD", slotId);
				break;
			case OPTION_3:
				end();
				break;
			}
			break;
		case 0:
			if (componentId == OPTION_3 || componentId == OPTION_4) {
				end();
				if (componentId == OPTION_3) {
					player.getEquipment().resetCosmetics();
					player.getAppearence().generateAppearenceData();
					player.getPackets().sendGameMessage("Your current costume has been reset.");
				}
				return;
			}
			stage = -1;
			choosenOption = componentId == OPTION_1 ? 0 : 1;
			if (componentId == OPTION_2 && player.getEquipment().getSavedCosmetics().isEmpty()) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You don't have any saved costumes. To save your current costume do ::savecurrentcostume or ::savecurrentcosmetic .");
				return;
			}
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Search outfits by name.", "View all outfits.", "Cancel.");
			break;
		}

	}

	@Override
	public void finish() {
	}
}
