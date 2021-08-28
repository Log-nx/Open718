package com.rs.game.player.managers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import com.rs.game.player.Player;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.utils.Color;

public class PlayerPerkManager implements Serializable {

	private static final long serialVersionUID = 2995657158069467668L;
	private Vector<PlayerPerks> playerPerks = new Vector<>();
	private Player player;
	
	public boolean hasPerk(PlayerPerks perk) {
		return playerPerks.contains(perk);
	}
	
	public boolean slayerBattleCry, arcaneCooking,
	samuraiCooking, arcaneFishing, deepSeaFishing,
	karateFletching, chiBlastMining, blastMining,
	strongArmMining, headButtMining, depthChargeFishing,
	strongArmWoodcutting, roundHouseWoodcutting, explosiveWoodcutting, crystalSingingWoodcutting,
	crystalSingingMining, strongArmBurial, arcaneSmelting, ironFistSmithing, masterFledger, avaSecret, investigator;
	
	public boolean unlockPerk(PlayerPerks perk) {
		if (playerPerks.contains(perk)) {
			return false;
		}
		player.getPackets().sendGameMessage(Color.GREEN, "You have unlocked the " + perk.toString().toLowerCase().replace("_", " ") + " perk!");
		playerPerks.add(perk);
		return true;
	}
	
	public boolean lockPerk(PlayerPerks perk) {
		if(!playerPerks.contains(perk)) {
			return false;
		}
		playerPerks.remove(perk);
		return true;
	}
	
	public Vector<PlayerPerks> getPlayerPerks() {
		return playerPerks;
	}
	
	public void setPlayerPerks(Vector<PlayerPerks> playerPerks) {
		this.playerPerks = playerPerks;
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
	public Player setPlayer(Player player) {
		return this.player = player;
	}

}
