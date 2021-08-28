package com.rs.game.player.controllers;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.minigames.ZarosGodwars;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Skills;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class ZGDControler extends Controller {

	@Override
	public void start() {
		ZarosGodwars.addPlayer(player);
		sendInterfaces();
	}

	@Override
	public boolean logout() {
		ZarosGodwars.removePlayer(player);
		return false; // so doesnt remove script
	}

	@Override
	public boolean login() {
		ZarosGodwars.addPlayer(player);
		sendInterfaces();
		return false; // so doesnt remove script
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasResizableScreen() ? 34 : 8, 601);
	}

	@Override
	public boolean sendDeath() {
		remove();
		removeController();
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		remove();
		removeController();
	}

	@Override
	public void forceClose() {
		remove();
	}

	public void remove() {
		ZarosGodwars.removePlayer(player);
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasResizableScreen() ? 34 : 8);
	}
	
	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 57225) {
			player.getDialogueManager().startDialogue("NexExit");
			return false;
		}
		return true;
		}
}
