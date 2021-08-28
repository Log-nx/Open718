package com.rs.game.player.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.araxxor.Araxxor;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * @author Miles/Danny (bobismyname), Nosz, homeboy927
 */

public class AraxxorController extends Controller {
	
	/**
	 * Rewards for the Rise of the Six
	 */
	private Araxxor npc;
	private static final Item[] commonRewards = { new Item(29759, Utils.random(3, 10)), new Item(1128, Utils.random(6, 10)),
			new Item(1514, Utils.random(190, 210)), new Item(15271, Utils.random(120, 150)), new Item(1748, Utils.random(60, 80)), 
			new Item(9245, Utils.random(50, 60)), new Item(1392, Utils.random(35, 45)), new Item(452, Utils.random(30, 34)), 
			new Item(5316, Utils.random(2, 4)), new Item(5303, Utils.random(6, 10)), new Item(5302, Utils.random(6, 10)) };
	private static final Item[] rareRewards = { new Item(30014, 1), new Item(30018, 1), new Item(30022, 1) };
	
	
	
	/**
	 * Dynamic region chunk data.
	 */
	private int[] chunks;
	private WorldTile base;
	
	/**
	 * Minigame coordinates.
	 */
	private static final WorldTile EXIT = new WorldTile(2208, 3373, 1);
	private static final WorldTile LOBBY = new WorldTile(2208, 3373, 1);
	private static final WorldTile GRAVEYARD = new WorldTile(2208, 3373, 1);

	@SuppressWarnings("unused")
	private boolean processNPCs;

	public void coordsPrint() {
		WorldTasksManager.schedule(new WorldTask() {
			int time;
	@Override
	public void run() {
		time++;
		if(time == 2) {
			player.sm("Region coords: "+getX()+" : "+getY()+"");
			player.sm("Normal coords: "+player.getX()+" : "+player.getY()+"");
			time = 0;
				}
			}
		}, 0, 0);
	}
	@Override
	public void start() {
		//player.sendMessage("being Started");
		//coordsPrint();
		//player.lock();
		player.araxxorHeal = false;
		player.AraxxorEggBurst = false;
		player.AraxxorThirdStage = false;
		player.AraxxorLastState = false;
		player.hasSpawnedEggs = false;
		boolean isHost = (boolean) getArguments()[0];
		Player host = (Player) getArguments()[1];
		processNPCs = false;
		player.setForceMultiArea(true);
		player.getInventory().deleteItem(30004, 1);
		if (isHost) {
			chunks = RegionBuilder.findEmptyChunkBound(32, 32);
			base = new WorldTile(chunks[0] << 3, chunks[1] << 3, 1);
			player.AraxxorBase = base;
			RegionBuilder.copyAllPlanesMap(550, 780, chunks[0], chunks[1], 32, 32);
			
			
			
			npc = new Araxxor(player, base.transform(105, 21, 0), base);
			npc.xDifference = base.getX();
			npc.setForceFollowClose(true);
			player.setNextWorldTile(base.transform(90, 25, 0));
			player.AraxxorNormAttackDelay = 0;
			player.ArraxorAttackDelay  = 0;
			player.araxxorEggAttack = false;
			player.AraxxorCompleteAttack = true;
			player.AraxxorAttackCount = 0;
			player.araxxorCacoonTime = 0;
			/*
	  Other variables.
	 */
			@SuppressWarnings("unused")
			int regionId = player.getRegionId();
			player.setForceMultiArea(true);
			// 104  // 32 
			// 111 // 21
			// 107 // 8
			World.spawnObject(new WorldObject(91504, 10, 0, base.transform(104, 32, 0))); // path 1 web ( perfect coords )
			World.spawnObject(new WorldObject(91509, 10, 0, base.transform(111, 21, 0))); // path 2 web ( perfect coords )
			World.spawnObject(new WorldObject(91511, 10, 0, base.transform(107, 8, 0))); // path 3 web ( perfect coords )	
			spawnWestBurn();
			spawnMidBurn();
			spawnEastBurn();

			
			// Ramp
			World.spawnObject(new WorldObject(91520, 10, 0, base.transform(135, 20, 0))); // Huge Ramp in center of map ( stage 0 no acid )

			
			//player.setLargeSceneView(true);
			WorldTasksManager.schedule(new WorldTask() {
				int stage;

				@Override
				public void run() {
					if (stage == 60) {
						processNPCs = true;
						stop();
					}
					stage++;
				}

			}, 0, 1);
		} else {
			player.setNextWorldTile(host.getWorldTile());
	/*		WorldTasksManager.schedule(new WorldTask() {
				int stage;

				@Override
				public void run() {
					if (stage == 60) {
						processNPCs = true;
						stop();
					}
					stage++;
				}

			}, 0, 1);*/
		}
	}
	private void spawnWestBurn() {
		WorldTasksManager.schedule(new WorldTask() {
			int time;
	@Override
	public void run() {
		time++;
		if(time == 9) {
		    for (int i = 0; i < 12; i++) {
		    World.spawnObject(new WorldObject(91667, 10, 0, base.transform(109-i, 31+i, 0)));
		 
			time = 0;
			stop();
		    }
				}
			}
		}, 0, 0);
}
	private void spawnMidBurn() {
		WorldTasksManager.schedule(new WorldTask() {
			int time;
	@Override
	public void run() {
		time++;
		if(time == 9) {
		    for (int i = 0; i < 12; i++) {
		    World.spawnObject(new WorldObject(91668, 10, 0, base.transform(112, 20+i, 0)));
		 
			time = 0;
			stop();
		    }
				}
			}
		}, 0, 0);
}
	private void spawnEastBurn() {
			WorldTasksManager.schedule(new WorldTask() {
				int time;
		@Override
		public void run() {
			time++;
			if(time == 9) {
			    for (int i = 0; i < 12; i++) {
			    World.spawnObject(new WorldObject(91669, 10, 0, base.transform(101+i, 5+i, 0)));
			 
				time = 0;
				stop();
			    }
					}
				}
			}, 0, 0);
	}
		
	
	public int getX() {
		return chunks[0] << 3;
	}

	public int getY() {
		return chunks[1] << 3;
	}
	
	@Override
	public void process() {
	}
	
	public static void receiveDrop(Player killer, WorldTile dropTile) {
		if (Utils.random(500) == 1)
			World.addGroundItem(rareRewards[Utils.random(0, rareRewards.length - 1)], dropTile, killer, true, 180);
		else
			World.addGroundItem(commonRewards[Utils.random(0, commonRewards.length - 1)], dropTile, killer, true, 180);
		World.addGroundItem(new Item(30027, 1), dropTile, killer, true, 180);	
	}
	
	@Override
	public boolean processObjectClick2(WorldObject object){
		int objectId = object.getId();
		switch (objectId) {
		case 91673:
			player.setNextWorldTile(LOBBY);
			//npc.openRewardChest(true);
			return false;
		}
		return false;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		int objectId = object.getId();
		switch (objectId) {
		
		case 91669:
			World.spawnBurningWeb(new WorldObject(91506, 10, 0, player.AraxxorBase.transform(40, 32, 0)), 60000); // 1 minute just like rs ( may need adjusted for server )
			for (int i = 0; i < 12; i++) { // increase 12 to 13 if a burn oject is left over 
				World.removeObject(new WorldObject(91669, 10, 0, player.AraxxorBase.transform(39, 34+i, 0))); // burn option
				World.spawnTemporaryObject(new WorldObject(91666, 10, 0, player.AraxxorBase.transform(39, 34+i, 0)), 60000); // burn option
			}
			return true;
		case 91673:
			if(npc.canLoot){
				player.getDialogueManager().startDialogue("AraxxiReward", npc);
			}else{
				player.sm("An error occurred, you cannot receive loot.");
			}
			//npc.openRewardChest(true);
			return false;
		
		case 91670:
			player.setNextAnimation(new Animation (10743));
			player.setNextWorldTile(player.AraxxorBase.transform(224-64, 86-64, 0));
			player.AraxxorThirdStage = true;
			return true;
		}
	/*if (object.getId() == 91669) { // path 3
			World.spawnBurningWeb(new WorldObject(91507, 10, 0, base.transform(107, 8, 0)), 60000); // 1 minute just like rs ( may need adjusted for server )
			for (int i = 0; i < 12; i++) { // increase 12 to 13 if a burn oject is left over 
				World.removeObject(new WorldObject(91669, 10, 0, base.transform(101+i, 5+i, 0))); // burn option
				World.spawnTemporaryObject(new WorldObject(91666, 10, 0, base.transform(101+i, 5+i, 0)), 60000); // burn option
				
				World.removeObject(new WorldObject(91667, 10, 0, base.transform(109-i, 31+i, 0))); // burn option
				World.spawnObject(new WorldObject(91666, 10, 0, base.transform(109-i, 31+i, 0)), true); // burn option
				World.removeObject(new WorldObject(91668, 10, 0, base.transform(112, 20+i, 0))); // burn option
				World.spawnObject(new WorldObject(91666, 10, 0, base.transform(112, 20+i, 0)), true); // burn option
			}
			return true;
		}*/
	if (object.getId() == 91668) { // path 2
		World.spawnBurningWeb(new WorldObject(91506, 10, 0, base.transform(111, 21, 0)), 60000); // 1 minute just like rs ( may need adjusted for server )
		for (int i = 0; i < 12; i++) { // increase 12 to 13 if a burn oject is left over 
			World.removeObject(new WorldObject(91668, 10, 0, base.transform(112, 20+i, 0))); // burn option
			World.spawnTemporaryObject(new WorldObject(91666, 10, 0, base.transform(112, 20+i, 0)), 60000); // burn option
			
			World.removeObject(new WorldObject(91669, 10, 0, base.transform(101+i, 5+i, 0))); // burn option
			World.spawnObject(new WorldObject(91666, 10, 0, base.transform(101+i, 5+i, 0))); // burn option
			World.removeObject(new WorldObject(91667, 10, 0, base.transform(109-i, 31+i, 0))); // burn option
			World.spawnObject(new WorldObject(91666, 10, 0, base.transform(109-i, 31+i, 0))); // burn option
		} 
		return true;
	}
	/*if (object.getId() == 91667) { // path 1
		World.spawnBurningWeb(new WorldObject(91505, 10, 0, base.transform(104, 32, 0)), 60000); // 1 minute just like rs ( may need adjusted for server )
		for (int i = 0; i < 12; i++) { // increase 12 to 13 if a burn oject is left over 
			World.removeObject(new WorldObject(91667, 10, 0, base.transform(109-i, 31+i, 0))); // burn option
			World.spawnTemporaryObject(new WorldObject(91666, 10, 0, base.transform(109-i, 31+i, 0)), 60000); // burn option
			
			World.removeObject(new WorldObject(91669, 10, 0, base.transform(101+i, 5+i, 0))); // burn option
			World.spawnObject(new WorldObject(91666, 10, 0, base.transform(101+i, 5+i, 0)), true); // burn option
			World.removeObject(new WorldObject(91668, 10, 0, base.transform(112, 20+i, 0))); // burn option
			World.spawnObject(new WorldObject(91666, 10, 0, base.transform(112, 20+i, 0)), true);
			
		}
		return true;
	}*/
		return true;
	
	}
	public static boolean handleObject(Player player, int objectId) {
		switch (objectId) {
	
		case 91669:
			World.spawnBurningWeb(new WorldObject(91506, 10, 0, player.AraxxorBase.transform(40, 32, 0)), 60000); // 1 minute just like rs ( may need adjusted for server )
			for (int i = 0; i < 12; i++) { // increase 12 to 13 if a burn oject is left over 
				World.removeObject(new WorldObject(91669, 10, 0, player.AraxxorBase.transform(39, 34+i, 0))); // burn option
				World.spawnTemporaryObject(new WorldObject(91666, 10, 0, player.AraxxorBase.transform(39, 34+i, 0)), 60000); // burn option
			}
			return true;
		case 91673:
			player.getDialogueManager().startDialogue("AraxxiReward");
			//npc.openRewardChest(true);
			return false;
		
		case 91670:
			player.setNextAnimation(new Animation (10743));
			player.setNextWorldTile(player.AraxxorBase.transform(224-64, 86-64, 0));
			player.AraxxorThirdStage = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		if (player.getFamiliar() != null) {
			player.getFamiliar().sendDeath(player);
		}
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					npc.finish();
					player.sm("Oh dear, you have died.");
					if (player.getAssassinsManager().getGameMode() == 4) {
						player.getAssassinsManager().resetTask();
						player.sm("You have failed your Assassin's contract, go try another.");
					}
				} else if (loop == 3) {
					player.reset();
					player.getControllerManager().startController("DeathEvent", GRAVEYARD, player.hasSkull());
				} else if (loop == 4) {
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}
	
	public void destroyMap() {
		//if()
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(chunks[0], chunks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}
	
	public void deleteNPCs(int regionId) {
		List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
		if (npcsIndexes != null) {
			for (int npcIndex : npcsIndexes) {
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null/* || npc.isDead() */|| npc.hasFinished()) {
					continue;
				}
				for (int i = 19457; i <= 19457; i++) {
					if (npc.getId() == i) {
						npc.finish();
					}
				}
			}
		}
	}
	
	public void endGame() {
		destroyMap();
		player.setForceMultiArea(false);
		player.getControllerManager().forceStop();
		player.setNextWorldTile(EXIT);
		player.stopAll();
	}
	
	private void endGame(WorldTile tile) {
		destroyMap();
		player.setForceMultiArea(false);
		player.getControllerManager().forceStop();
		Magic.sendNormalTeleportSpell(player, 0, 0, tile);
		player.stopAll();
	}
	
	@Override
	public boolean login() {
		endGame();
		return false;
	}
	
	@Override
	public boolean logout() {
		endGame();
		return false;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (npc == null) {
			return true;
		}
		switch (interfaceId) {
		case 1284:
			switch (componentId) {
			case 8:
				player.getBank().addItems(npc.getRewards().toArray(), false);
				npc.getRewards().clear();
				player.getPackets().sendGameMessage("All the items were moved to your bank.");
				break;
			case 9:
				npc.getRewards().clear();
				player.getPackets().sendGameMessage("All the items were removed from the chest.");
				break;
			case 10:
				for (int slot = 0; slot < npc.getRewards().toArray().length; slot++) {
					Item item = npc.getRewards().get(slot);
					if (item == null) {
						continue;
					}
					boolean added = true;
					if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
						added = player.getInventory().addItem(item);
						if (added) {
							npc.getRewards().toArray()[slot] = null;
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (!player.getInventory().addItem(single)) {
								added = false;
								break;
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.getPackets().sendGameMessage("You only had enough space in your inventory to accept some of the items.");
						break;
					}
				}
				break;
			case 7:
				Item item = npc.getRewards().get(slotId);
				if (item == null) {
					return true;
				}
				switch (packetId) {
				case 52:
					player.getPackets().sendGameMessage("It's a " + item.getDefinitions().getName());
					return false;
				case 4:
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 64:
					player.getBank().addItems(new Item[] {npc.getRewards().toArray()[slotId]}, false);
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 61:
					boolean added = true;
					if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
						added = player.getInventory().addItem(item);
						if (added) {
							npc.getRewards().toArray()[slotId] = null;
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (!player.getInventory().addItem(single)) {
								added = false;
								break;
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.getPackets().sendGameMessage("You only had enough space in your inventory to accept some of the items.");
						break;
					}
					break;
				default:
					return true;
				}
				break;
			default:
				return true;
			}
			npc.openRewardChest(false);
			return false;
		}
		return true;
	}
	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		endGame(toTile);
		return true;
	}
	
	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		endGame(toTile);
		return true;
	}
	
	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		endGame(toTile);
		return true;
	}

}