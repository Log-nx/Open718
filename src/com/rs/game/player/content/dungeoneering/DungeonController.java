package com.rs.game.player.content.dungeoneering;

import java.util.List;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.mining.MiningBase;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

public class DungeonController extends Controller {
	
	Dungeon dungeon;
	
	@Override
	public void start() {
		dungeon = (Dungeon) getArguments()[0];
		setArguments(null);
		//sendItems(player);
		//dungeon.loadItems();
		//showDeaths();
		//player.getInventory().addItem(18201, 2500*(dungeon.getDungType()+1));
	/*	if (player.getDungBinds() != null) {
			if (player.getDungBinds()[0] != null) {
				player.getInventory().addItem(player.getDungBinds()[0]);
			}
			if (player.getDungBinds()[1] != null) {
				player.getInventory().addItem(player.getDungBinds()[1]);
			}
			if (player.getDungBinds()[2] != null) {
				player.getInventory().addItem(player.getDungBinds()[2]);
			}
		}*/
	}

	public void showDeaths() {
		player.getInterfaceManager()
				.sendTab(
						player.getInterfaceManager().hasResizableScreen() ? 10
								: 8, 945);
	}

	@Override
	public void sendInterfaces() {
		showDeaths();
	}

	public static void stopController(Player p) {
		p.getControllerManager().getController().removeController();
	}
	
	public void openStairs() {
		WorldTile pos;
		
		switch(dungeon.getDungType()) {
		case 0:
			pos = dungeon.getTileFromRegion(0, 8, 7, -1);
			dungeon.endLadder = new WorldObject(3784, 10, 3, pos.getX(), pos.getY(), 0);
			break;
		case 1:
			pos = dungeon.getTileFromRegion(0, 8, 7, -5);
			dungeon.endLadder = new WorldObject(3786, 10, 3, pos.getX(), pos.getY(), 0);
			break;
		case 2:
			pos = dungeon.getTileFromRegion(0, 8, 7, -1);
			dungeon.endLadder = new WorldObject(49700, 10, 3, pos.getX(), pos.getY(), 0);
			break;
		case 3:
			pos = dungeon.getTileFromRegion(0, 8, 7, -1);
			dungeon.endLadder = new WorldObject(3808, 10, 3, pos.getX(), pos.getY(), 0);
			break;
		case 4:
			pos = dungeon.getTileFromRegion(0, 8, 7, -1);
			dungeon.endLadder = new WorldObject(55484, 10, 3, pos.getX(), pos.getY(), 0);
			break;
		default:
			pos = dungeon.getTileFromRegion(0, 8, 8, 0);
			dungeon.endLadder = new WorldObject(55484, 10, 3, pos.getX(), pos.getY(), 0);
			break;
		}
		
		World.spawnObject(dungeon.endLadder);
		dungeon.openedStairs = true;
	}
	
	@Override
	public void process() {
		if (dungeon != null) {
			if (dungeon.boss.hasFinished() && !dungeon.openedStairs) {
				openStairs();
			}
		} else {
			player.setDungeon(null);
			player.setNextWorldTile(DungeonConstants.DAMONHEIM_LOBBY);
			removeController();
		}
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					dungeon.putPlayerAtStart();
					player.setNextAnimation(new Animation(-1));
					increaseDeaths();
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	public void increaseDeaths() {
		if (dungeon.deaths == 15)
			return;
		dungeon.deaths++;
		dungeon.refreshDeaths();
	}

	@Override
	public boolean login() {
		//player.removeDungItems();
		player.setLocation(Settings.RESPAWN_DUNGEONEERING_LOCATION);
		//player.set(DungeonConstants.DAMONHEIM_LOBBY);
		if (dungeon != null)
			dungeon.end(false);
		return false;
	}
	
	@Override
	public void forceClose() {
		//player.removeDungItems();
		player.setLocation(Settings.RESPAWN_DUNGEONEERING_LOCATION);
		//player.set(DungeonConstants.DAMONHEIM_LOBBY);
		if (dungeon != null)
			dungeon.end(false);
	}

	@Override
	public boolean logout() {
		//player.removeDungItems();
		player.setLocation(Settings.RESPAWN_DUNGEONEERING_LOCATION);
		//player.set(DungeonConstants.DAMONHEIM_LOBBY);
		if (dungeon != null)
			dungeon.end(false);
		return true;
	}

	/*@Override
	public boolean logout() {
		player.stopAll();
		player.setLocation(Main.RESPAWN_DUNGEONEERING_LOCATION);
		removeControler();
		return false;
	}*/

	@Override
	public boolean canUseCommands() {
		player.getPackets().sendGameMessage(
				"You can't use commands at the moment.");
		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You may not teleport in a dungeon. You may leave at any time from the home room.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage","You may not teleport in a dungeon. You may leave at any time from the home room.");
		return false;
	}
	
	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
            player.getDialogueManager().startDialogue("SimpleMessage", "You may not teleport in a dungeon. You may leave at any time from the home room.");
	return false;

	}
	
	@Override
	public boolean processNPCClick1(NPC npc) {
		switch(npc.getId()) {
		case 11226:
			player.getDialogueManager().startDialogue("DungeonExitD", player);
			return true;
			default:
				player.getPackets().sendGameMessage("NPC not added to dungeoneering controller.");
				return true;
		}
	}
	
	@Override
	public boolean processNPCClick2(NPC npc) {
		switch(npc.getId()) {
		case 11226:
			player.getDialogueManager().startDialogue("DungeonExitD", player);
			return true;
			default:
				player.getPackets().sendGameMessage("NPC not added to dungeoneering controller.");
				return true;
		}
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (dungeon != null)
			dungeon.handleObjects(object);
		return false;
	}
	
	/*public void sendItems(Player player) {
			player.getInventory().addItem(17239, 1);
			player.getInventory().addItem(16669, 1);
			player.getInventory().addItem(16935, 1);
			player.getInventory().addItem(15753, 1);
			player.getInventory().addItem(17217, 1);
			player.getInventory().addItem(16845, 1);
			player.getInventory().addItem(17785, 98);
			player.getInventory().addItem(17780, 121);
			player.getInventory().addItem(17175, 1);
			player.getInventory().addItem(17321, 1);
			player.getInventory().addItem(16319, 1);
			player.getInventory().addItem(16427, 100);
		}*/

}