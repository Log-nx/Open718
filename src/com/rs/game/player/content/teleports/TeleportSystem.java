package com.rs.game.player.content.teleports;

import com.rs.Settings;
import com.rs.game.player.Player;
import com.rs.utils.Logger;

public class TeleportSystem {
	
	public static void removeAttributes(Player player, String attribute) {
		player.getTemporaryAttributtes().remove("NPCTeleports");
		player.getTemporaryAttributtes().remove("LowLevelTrainingTeleports");
		player.getTemporaryAttributtes().remove("MediumLevelTrainingTeleports");
		player.getTemporaryAttributtes().remove("HighLevelTrainingTeleports");
		player.getTemporaryAttributtes().remove("LowLevelSlayerTeleports");
		player.getTemporaryAttributtes().remove("LowLevelSlayerTeleportsPage2");
		player.getTemporaryAttributtes().remove("HighLevelSlayerTeleportsPage2");
		player.getTemporaryAttributtes().remove("HighLevelSlayerTeleports");
		player.getTemporaryAttributtes().remove("WyrmTeleports");
		player.getTemporaryAttributtes().remove("ChromaticDragonTeleports");
		player.getTemporaryAttributtes().remove("MetalDragonTeleports");
		player.getTemporaryAttributtes().remove("DragonTeleports");
		player.getTemporaryAttributtes().remove("MinigameTeleports");
		player.getTemporaryAttributtes().remove("MinigameTeleportsPage2");
		player.getTemporaryAttributtes().remove("DonationZoneTeleports");
		player.getTemporaryAttributtes().remove("LowLevelBossTeleports");
		player.getTemporaryAttributtes().remove("MediumLevelBossTeleports");
		player.getTemporaryAttributtes().remove("HighLevelBossTeleports");
		player.getTemporaryAttributtes().put(attribute, true);
		
	}
	
	public static void sendInterface(Player player, int interfaceId) {
		player.getInterfaceManager().sendInterface(72);
		player.getPackets().sendIComponentText(72, 55, "Diccus Teleport System");
		player.getPackets().sendIComponentText(72, 31, "Low-Level Training");
		player.getPackets().sendIComponentText(72, 32, "Medium-Level Training");
		player.getPackets().sendIComponentText(72, 33, "High-Level Training");
		player.getPackets().sendIComponentText(72, 34, "Low-Level Slayer Teleports");
		player.getPackets().sendIComponentText(72, 35, "Minigame Teleports");
		player.getPackets().sendIComponentText(72, 36, "Low-Level Boss Locations");
		player.getPackets().sendIComponentText(72, 37, "Medium-Level Boss Locations");
		player.getPackets().sendIComponentText(72, 38, "High-Level Boss Locations");
		player.getPackets().sendIComponentText(72, 39, "High-Level Slayer Locations");
		player.getPackets().sendIComponentText(72, 40, "Donation Zones");
	}
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			player.getInterfaceManager().sendLowLevelTrainingTeleports();
			break;
		case 67:
			player.getInterfaceManager().sendMediumLevelTrainingTeleports();
			break;
		case 66:
			player.getInterfaceManager().sendHighLevelTrainingTeleports();
			break;
		case 65:
			player.getInterfaceManager().sendLowLevelSlayerTeleports();
			break;
		case 64:
			player.getInterfaceManager().sendMinigameTeleports();
			break;
		case 73:
			player.getInterfaceManager().sendLowLevelBossTeleports();
			break;
		case 72:
			player.getInterfaceManager().sendMediumLevelBossTeleports();
			break;
		case 71:
			player.getInterfaceManager().sendHighLevelBossTeleports();
			break;
		case 70:
			player.getInterfaceManager().sendHighLevelSlayerTeleports();
			break;
		case 69:
			player.getInterfaceManager().sendDonationZoneTeleports();
			break;
		default:
			player.getPackets().sendGameMessage("This button does not have an action set.");
			break;
		}
	}
	
	public static void handleInterface(Player player, int componentId) {
		if (player.getTemporaryAttributtes().get("NPCTeleports") != null) {
			player.getInterfaceManager().sendLowLevelTrainingTeleports();
			TeleportSystem.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("LowLevelTrainingTeleports") != null) {
			LowLevelTrainingTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("MediumLevelTrainingTeleports") != null) {
			MediumLevelTrainingTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("HighLevelTrainingTeleports") != null) {
			HighLevelTrainingTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("LowLevelSlayerTeleports") != null) {
			LowLevelSlayerTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("LowLevelSlayerTeleportsPage2") != null) {
			LowLevelSlayerTeleportsPage2.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("HighLevelSlayerTeleportsPage2") != null) {
			HighLevelSlayerTeleportsPage2.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("WyrmTeleports") != null) {
			StrykewyrmTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("ChromaticDragonTeleports") != null) {
			ChromaticDragonTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("MetalDragonTeleports") != null) {
			MetalDragonTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("DragonTeleports") != null) {
			DragonTypeTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("MinigameTeleports") != null) {
			MinigameTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("MinigameTeleportsPage2") != null) {
			MinigameTeleportsPage2.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("LowLevelBossTeleports") != null) {
			LowLevelBossTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("MediumLevelBossTeleports") != null) {
			MediumLevelBossTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("HighLevelBossTeleports") != null) {
			HighLevelBossTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("HighLevelSlayerTeleports") != null) {
			HighLevelSlayerTeleports.handleButtons(player, componentId);
		}
		else if (player.getTemporaryAttributtes().get("DonationZoneTeleports") != null) {
			DonationZoneTeleports.handleButtons(player, componentId);
		}
		if (player.isAdministrator() || Settings.BETA)
		player.getPackets().sendGameMessage("Component: " + componentId + ".");
	}
}