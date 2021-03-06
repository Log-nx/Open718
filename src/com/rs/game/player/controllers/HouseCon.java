package com.rs.game.player.controllers;

import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;

public class HouseCon extends Controller {
	
	private int[] boundChuncks;
	
	@Override
	public void start() {
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8); 
		player.setNextWorldTile(new WorldTile(boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 34, 0));
		World.spawnObject(new WorldObject(13405, 10, 0, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 35, 0));
		player.closeInterfaces();
	}
	
	boolean remove = true;
	
	@Override
	public boolean processObjectClick5(WorldObject object) {
		//house.previewRoom(player, boundChuncks, new RoomReference(Room.GARDEN, 4, 5, 0, 0), remove = !remove);
		return true;
	}

}
