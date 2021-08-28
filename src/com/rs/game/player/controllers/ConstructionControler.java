package com.rs.game.player.controllers;

import com.rs.game.RegionBuilder;
import com.rs.game.WorldTile;

public class ConstructionControler extends Controller {
	
	private int[] boundChuncks;
	
	@Override
	public void start() {
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8); 
		player.setNextWorldTile(new WorldTile(boundChuncks[0]*8 + 35, boundChuncks[1]*8 + 35,0));
		player.getPackets().sendGameMessage("Welcome to your house! Make a bed or wardrobe!");
	}
	boolean remove = true;
}