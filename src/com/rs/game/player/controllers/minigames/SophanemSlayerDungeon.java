package com.rs.game.player.controllers.minigames;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.Controller;

public class SophanemSlayerDungeon extends Controller {

	@Override
	public void start() {
		sendInterfaces();
	}
	
	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}
	
	@Override
	public boolean login() {
		sendInterfaces();
		return false; // so doesnt remove script
	}
	
	@Override 
	public void sendInterfaces() {
		player.getPackets().sendWindowsPane(746, 0); //childs, 17, 183
		player.getPackets().sendIComponentText(746, 17, "Corruption Info");
	}
	
	@Override
	public void magicTeleported(int type) {
		end();
		removeController();
	}
	
	@Override
	public boolean processMagicTeleport(WorldTile to) {
		end();
		removeController();
		return true;
	}
	
	@Override
	public boolean processItemTeleport(WorldTile to) {
		end();
		removeController();
		return true;
	}

	@Override
	public void forceClose() {
		end();
		removeController();
	}
	
	@Override
	public boolean sendDeath() {
		end();
		removeController();
		return true;
	}
	
	//Removes shit to do with the controler
	public void end() {
		player.getPackets().closeInterface(746, 0);
		player.getPackets().sendIComponentText(746, 183, "");
		player.getPackets().sendIComponentText(746, 17, "");
		player.getStatistics().setCorruption(0);	
	}
	
	//Updates corruption information.
	public static void update(Player player) {
		player.getPackets().sendIComponentText(746, 183, player.getStatistics().getCorruption() + "%");
	}

}

