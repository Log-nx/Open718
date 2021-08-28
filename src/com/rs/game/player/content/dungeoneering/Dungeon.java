package com.rs.game.player.content.dungeoneering;

import java.io.Serializable;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.actions.smithing.Smithing;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class Dungeon implements Serializable {

	private static final long serialVersionUID = -3866335496921765212L;
	
	public static final int FROZEN = 0;
	public static final int ABANDONED = 1;
	public static final int FURNISHED = 2;
	public static final int OCCULT = 3;
	public static final int WARPED = 4;

	public WorldObject endLadder;
	
	public NPC monster1;
	public NPC monster2;
	public NPC boss;
	public NPC smuggler;
	public NPC fish;
	
	public boolean openedStairs = false;
	
	private Player player;
	private int dungType;
	private int mapChunks[];
	public int deaths = 0;

	
	public Dungeon(Player player, int dungType) {
		this.player = player;
		this.dungType = dungType;
		bindChunksToEmptyMap();
		loadRooms(dungType);
		putPlayerAtStart();
		player.loadMapRegions();
		loadNPCs();
		player.getControllerManager().startController("DungeonController", this);
		player.stopAll();
		player.reset();
	}
	
	public void refreshDeaths() {
		player.getPackets().sendConfigByFile(7554, deaths);
	}
	
	private void loadNPCs() {
		smuggler = new NPC(11226, getTileFromRegion(0, 0, 7, 8), 0, false, true);
		//fish = new NPC(11226, getTileFromRegion(0, 0, 5, 17), 0, false, true);
		monster1 = new NPC(DungeonConstants.MONSTER1[dungType], getTileFromRegion(0, 2, 8, 7), -1, true, true);
		monster2 = new NPC(DungeonConstants.MONSTER2[dungType], getTileFromRegion(0, 4, 8, 7), -1, true, true);
		boss = new NPC(DungeonConstants.BOSS[dungType], getTileFromRegion(0, 6, 6, 6), -1, true, true);
	}

	/*public void loadItems() {
		int i = Utils.getRandom(DungeonConstants.randomPlatebody.length - 1);
		int i1 = Utils.getRandom(DungeonConstants.randomPlatelegs.length - 1);;
		int i2 = Utils.getRandom(DungeonConstants.randomRapier.length - 1);
		int i3 = Utils.getRandom(DungeonConstants.randomFullHelm.length - 1);
		int i4 = Utils.getRandom(DungeonConstants.randomFood.length - 1);
		int i5 = Utils.getRandom(DungeonConstants.randomFood.length - 1);
		int i6 = Utils.getRandom(DungeonConstants.randomFood.length - 1);
		World.addGroundItem(new Item(DungeonConstants.randomPlatebody[i], 1), getTileFromRegion(0, 0, 6, 5));
		World.addGroundItem(new Item(DungeonConstants.randomPlatelegs[i1], 1), getTileFromRegion(0, 0, 5, 5));
		World.addGroundItem(new Item(DungeonConstants.randomRapier[i2], 1), getTileFromRegion(0, 0, 4, 5));
		World.addGroundItem(new Item(DungeonConstants.randomFullHelm[i3], 1), getTileFromRegion(0, 0, 4, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i4], 1), getTileFromRegion(0, 0, 6, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i5], 1), getTileFromRegion(0, 0, 6, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i6], 1), getTileFromRegion(0, 0, 6, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i4], 1), getTileFromRegion(0, 0, 5, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i5], 1), getTileFromRegion(0, 0, 5, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i6], 1), getTileFromRegion(0, 0, 5, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i4], 1), getTileFromRegion(0, 0, 4, 4));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i5], 1), getTileFromRegion(0, 0, 4, 5));
		World.addGroundItem(new Item(DungeonConstants.randomFood[i6], 1), getTileFromRegion(0, 0, 4, 5));
	}*/
	public int getXpForDungeon() {
		int initialXp = (100 + (dungType * 15));
		initialXp *= (dungType + 1);
		if (deaths > 0)
			initialXp /= deaths;
		return initialXp;
	}
	
	public void end(boolean nextDung) {
		RegionBuilder.destroyMap(mapChunks[0], mapChunks[1], 4, 8);
		RegionBuilder.destroyMap(mapChunks[0], mapChunks[1]+2, 4, 8);
		RegionBuilder.destroyMap(mapChunks[0], mapChunks[1]+4, 4, 8);
		RegionBuilder.destroyMap(mapChunks[0], mapChunks[1]+6, 4, 8);
		player.getControllerManager().removeControllerWithoutCheck();
		if (!nextDung) {
			player.setNextWorldTile(DungeonConstants.DAMONHEIM_LOBBY);
			player.closeInterfaces();
		}
		if (!monster1.hasFinished())
			monster1.finish();
		if (!monster2.hasFinished())
			monster2.finish();
		if (!boss.hasFinished())
			boss.finish();
		if (!smuggler.hasFinished())
			smuggler.finish();
		if (endLadder != null)
			World.removeObject(endLadder, false);
		refreshDeaths();
		//player.removeDungItems();
		player.setDungeon(null);
		player.setForceNextMapLoadRefresh(true);
	}
	
	public void handleObjects(WorldObject object) {
		ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(object.getId());
		
		switch (defs.name.toLowerCase()) {
		case "dungeon exit":
			player.getDialogueManager().startDialogue("DungeonExit", player);
			break;
			
		case "boss door":
			player.setNextWorldTile(new WorldTile(player.getX(), player.getY()-3, player.getPlane()));
			break;
			
		case "ladder":
			if (boss.hasFinished() && monster1.hasFinished() && monster2.hasFinished()) {
				player.getDialogueManager().startDialogue("DungeonCompleteD", this);
			} else {
				player.getPackets().sendGameMessage("You have not killed the monster, do not waste time.");
			}
			break;
			
		case "door":
			if (player.getY() == getTileFromRegion(0, 2, 0, 0).getY()+1) {
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY()-3, player.getPlane()));
			}
			
			if (player.getY() == getTileFromRegion(0, 2, 0, 0).getY()-2) {
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY()+3, player.getPlane()));
			}
			
			if (player.getY() == getTileFromRegion(0, 4, 0, 0).getY()-2) {
				if (monster1.hasFinished()) {
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY()+3, player.getPlane()));
				} else {
					player.getPackets().sendGameMessage("You will have to kill the monster in this room before you can enter the another room.");
				}
			}
			
			if (player.getY() == getTileFromRegion(0, 6, 0, 0).getY()-2) {
				if (monster2.hasFinished()) {
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY()+3, player.getPlane()));
				} else {
					player.getPackets().sendGameMessage("You will have to kill the monster in this room before you can enter the another room.");
				}
			}
			
			if (player.getY() == getTileFromRegion(0, 2, 0, 0).getY()+1) {
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY()-3, player.getPlane()));
			}
			
			if (player.getY() == getTileFromRegion(0, 4, 0, 0).getY()+1) {
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY()-3, player.getPlane()));
			}
			break;
			
		default:
			player.getPackets().sendGameMessage("You should rather use the time to complete this floor.");//Object not handled: ID: "+object.getId()+" Name: "+defs.name);
			break;
		}
	}
	
	public WorldTile getTileFromRegion(int offsetX, int offsetY, int tileOffsetX, int tileOffsetY) {
		return new WorldTile((mapChunks[0] << 3) + offsetX * 8+tileOffsetX, (mapChunks[1] << 3) + offsetY * 8+tileOffsetY, 0);
	}
	
	public void putPlayerAtStart() {
		player.setNextWorldTile(getTileFromRegion(0, 0, 8, 8));
	}
	
	public void bindChunksToEmptyMap() {
		setMapChunks(RegionBuilder.findEmptyChunkBound(14, 24));
	}
	
	public void loadRooms(int dungType) {
		switch(dungType) {
		case FROZEN:
			RegionBuilder.copy2RatioSquare(DungeonConstants.FROZEN_ROOMS[0][0], DungeonConstants.FROZEN_ROOMS[0][1], mapChunks[0], mapChunks[1], 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.FROZEN_ROOMS[1][0], DungeonConstants.FROZEN_ROOMS[1][1], mapChunks[0], mapChunks[1]+2, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.FROZEN_ROOMS[1][0], DungeonConstants.FROZEN_ROOMS[1][1], mapChunks[0], mapChunks[1]+4, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.FROZEN_ROOMS[2][0], DungeonConstants.FROZEN_ROOMS[2][1], mapChunks[0], mapChunks[1]+6, 0);
			Logger.log("Dungeoneering", "Easy floor loaded by "+player.getDisplayName());
			player.getPackets().sendGameMessage(" ");
			player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
			player.getPackets().sendGameMessage("Participant: <col=8e28f4>"+player.getDisplayName()+"");
			player.getPackets().sendGameMessage("Dungeon Floor: <col=8e28f4>Easy");
			player.getPackets().sendGameMessage(" ");
			break;
		case ABANDONED:
			RegionBuilder.copy2RatioSquare(DungeonConstants.ABANDONED_ROOMS[0][0], DungeonConstants.ABANDONED_ROOMS[0][1], mapChunks[0], mapChunks[1], 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.ABANDONED_ROOMS[1][0], DungeonConstants.ABANDONED_ROOMS[1][1], mapChunks[0], mapChunks[1]+2, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.ABANDONED_ROOMS[1][0], DungeonConstants.ABANDONED_ROOMS[1][1], mapChunks[0], mapChunks[1]+4, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.ABANDONED_ROOMS[2][0], DungeonConstants.ABANDONED_ROOMS[2][1], mapChunks[0], mapChunks[1]+6, 0);
			Logger.log("Dungeoneering", "MEdium floor loaded by "+player.getDisplayName());
			player.getPackets().sendGameMessage(" ");
			player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
			player.getPackets().sendGameMessage("Participant: <col=8e28f4>"+player.getDisplayName()+"");
			player.getPackets().sendGameMessage("Dungeon Floor: <col=8e28f4>Medium");
			player.getPackets().sendGameMessage(" ");
			break;
		case FURNISHED:
			RegionBuilder.copy2RatioSquare(DungeonConstants.FURNISHED_ROOMS[0][0], DungeonConstants.FURNISHED_ROOMS[0][1], mapChunks[0], mapChunks[1], 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.FURNISHED_ROOMS[1][0], DungeonConstants.FURNISHED_ROOMS[1][1], mapChunks[0], mapChunks[1]+2, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.FURNISHED_ROOMS[1][0], DungeonConstants.FURNISHED_ROOMS[1][1], mapChunks[0], mapChunks[1]+4, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.FURNISHED_ROOMS[2][0], DungeonConstants.FURNISHED_ROOMS[2][1], mapChunks[0], mapChunks[1]+6, 0);
			Logger.log("Dungeoneering", "Large floor loaded by "+player.getDisplayName());
			player.getPackets().sendGameMessage(" ");
			player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
			player.getPackets().sendGameMessage("Participant: <col=8e28f4>"+player.getDisplayName()+"");
			player.getPackets().sendGameMessage("Dungeon Floor: <col=8e28f4>Large");
			player.getPackets().sendGameMessage(" ");
			break;
		case OCCULT:
			RegionBuilder.copy2RatioSquare(DungeonConstants.OCCULT_ROOMS[0][0], DungeonConstants.OCCULT_ROOMS[0][1], mapChunks[0], mapChunks[1], 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.OCCULT_ROOMS[1][0], DungeonConstants.OCCULT_ROOMS[1][1], mapChunks[0], mapChunks[1]+2, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.OCCULT_ROOMS[1][0], DungeonConstants.OCCULT_ROOMS[1][1], mapChunks[0], mapChunks[1]+4, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.OCCULT_ROOMS[2][0], DungeonConstants.OCCULT_ROOMS[2][1], mapChunks[0], mapChunks[1]+6, 0);
			Logger.log("Dungeoneering", "Hard floor loaded by "+player.getDisplayName());
			player.getPackets().sendGameMessage(" ");
			player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
			player.getPackets().sendGameMessage("Participant: <col=8e28f4>"+player.getDisplayName()+"");
			player.getPackets().sendGameMessage("Dungeon Floor: <col=8e28f4>Hard");
			player.getPackets().sendGameMessage(" ");
			break;
		default:
			RegionBuilder.copy2RatioSquare(DungeonConstants.WARPED_ROOMS[0][0], DungeonConstants.WARPED_ROOMS[0][1], mapChunks[0], mapChunks[1], 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.WARPED_ROOMS[1][0], DungeonConstants.WARPED_ROOMS[1][1], mapChunks[0], mapChunks[1]+2, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.WARPED_ROOMS[1][0], DungeonConstants.WARPED_ROOMS[1][1], mapChunks[0], mapChunks[1]+4, 2);
			RegionBuilder.copy2RatioSquare(DungeonConstants.WARPED_ROOMS[2][0], DungeonConstants.WARPED_ROOMS[2][1], mapChunks[0], mapChunks[1]+6, 0);
			Logger.log("Dungeoneering", "Extreme floor loaded by "+player.getDisplayName());
			player.getPackets().sendGameMessage(" ");
			player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
			player.getPackets().sendGameMessage("Participant: <col=8e28f4>"+player.getDisplayName()+"");
			player.getPackets().sendGameMessage("Dungeon Floor: <col=8e28f4>Extreme");
			player.getPackets().sendGameMessage(" ");
			break;
		}
	}

	public int[] getMapChunks() {
		return mapChunks;
	}

	public void setMapChunks(int mapChunks[]) {
		this.mapChunks = mapChunks;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getDungType() {
		return dungType;
	}

	public void setDungType(int dungType) {
		this.dungType = dungType;
	}
}