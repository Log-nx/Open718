package com.rs.game.player.controllers.pestcontrol;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.minigames.pest.Lander;
import com.rs.game.player.controllers.Controller;
import com.rs.utils.Utils;

public final class PestControlLobby extends Controller {

	private int landerId;

	@Override
	public boolean canSummonFamiliar() {
		player.getPackets()
				.sendGameMessage(
						"You feel it's best to keep your Familiar away during this game.");
		return false;
	}

	@Override
	public void forceClose() {
		player.getInterfaceManager().closeOverlay(false);
		Lander.getLanders()[landerId].exitLander(player);
	}

	@Override
	public boolean logout() {
		Lander.getLanders()[landerId].remove(player);// to stop the timer in the
														// lander and prevent
														// future errors
		return false;
	}

	@Override
	public void magicTeleported(int teleType) {
		player.getControllerManager().forceStop();
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getControllerManager().forceStop();
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getControllerManager().forceStop();
		return true;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		case 14314:
		case 25629:
		case 25630:
			player.getDialogueManager().startDialogue("LanderD");
			return true;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		player.getPackets().sendIComponentText(407, 3, Utils.fixChatMessage(Lander.getLanders()[landerId].toString()));
		final int minutesLeft = (Lander.getLanders()[landerId].getTimer().getMinutes());
		player.getPackets().sendIComponentText(407, 13, "Next Departure: " + minutesLeft + " minutes " + (!(minutesLeft % 2 == 0) ? " 30 seconds" : ""));
		player.getPackets().sendIComponentText(407, 14, "Player's Ready: " + Lander.getLanders()[landerId].getPlayers().size());
		player.getPackets().sendIComponentText(407, 15, "(Need 5 to 25 players)");
		player.getPackets().sendIComponentText(407, 16, "Commendations: " + player.getPestPoints());
		player.getInterfaceManager().sendOverlay(407, false);
	}

	@Override
	public void start() {
		this.landerId = (Integer) getArguments()[0];
	}
}