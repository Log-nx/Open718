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
import com.rs.game.npc.araxxi.Araxxi;
import com.rs.game.npc.araxxor.Araxxor;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class AraxxiController extends Controller{


	private static WorldTile EXIT = new WorldTile(3700, 3418, 0), LOBBY =new WorldTile(3700, 3418, 0) , GRAVEYARD = new WorldTile(3700, 3418, 0);


	@SuppressWarnings("unused")
	private int regionId;
	@SuppressWarnings("unused")
	private boolean processNPCs;
	private Player host;
	int time;
	private int[] chunks;
	private boolean canPro;
	private WorldTile base;

	private Araxxi araxxi;

	@Override
	public void start() {
		host = player;
		processNPCs = false;
		player.setForceMultiArea(true);
		chunks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(572, 780, chunks[0], chunks[1], 8);
		player.setNextWorldTile(new WorldTile(getX()+15, getY()+17, 1));
		WorldTile tile = new WorldTile(getX() + 14, getY() + 25, 1);
		araxxi = new Araxxi(19464, tile, 0, false, false, host);
		if(player.araxxiEnrage != 0 && (Utils.currentTimeMillis()- player.araxxiEnrageTimer> 43200000)){
			player.sm("Your Enrage level has reset!");
			player.araxxiEnrage = 0;
		}
		//araxxi.setForceFollowClose(true);
		araxxi.playerEnrageLevel = player.araxxiEnrage;
		//set araxxi's enrage multipliers
		araxxi.EnrageNumbers();
		player.sm("<br><br>--Enrage Level " + player.araxxiEnrage + "--");
		player.sm("Damage Multiplier: " + araxxi.damageMulti);
		player.sm("Healing Multiplier: " + araxxi.healingMulti);
		player.sm("Max Minions: " + araxxi.minionNumber);
		player.sm("Total HP: " + araxxi.startingHp);
		araxxi.setHitpoints(araxxi.startingHp);
		araxxi.setNoClipWalking(true);
		araxxi.removeClipping();
		regionId = player.getRegionId();
		player.setForceMultiArea(true);

		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 5) {
					canPro = true;
					stop();
				}
				stage++;
			}

		}, 0, 1);


	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (araxxi == null) {
			return true;
		}
		switch (interfaceId) {
			case 1284:
				switch (componentId) {
					case 8:
						player.getBank().addItems(araxxi.getRewards().toArray(), true);
						araxxi.getRewards().clear();
						player.getPackets().sendGameMessage("All the items were moved to your bank.");
						break;
					case 9:
						araxxi.getRewards().clear();
						player.getPackets().sendGameMessage("All the items were removed from the chest.");
						break;
					case 10:
						for (int slot = 0; slot < araxxi.getRewards().toArray().length; slot++) {
							Item item = araxxi.getRewards().get(slot);
							if (item == null) {
								continue;
							}
							boolean added = true;
							if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
								added = player.getInventory().addItem(item);
								if (added) {
									araxxi.getRewards().toArray()[slot] = null;
								}
							} else {
								for (int i = 0; i < item.getAmount(); i++) {
									Item single = new Item(item.getId());
									if (!player.getInventory().addItem(single)) {
										added = false;
										break;
									}
									araxxi.getRewards().remove(single);
								}
							}
							if (!added) {
								player.getPackets().sendGameMessage("You only had enough space in your inventory to accept some of the items.");
								break;
							}
						}
						break;
					case 7:
						Item item = araxxi.getRewards().get(slotId);
						if (item == null) {
							return true;
						}
						switch (packetId) {
							case 52:
								player.getPackets().sendGameMessage("It's a " + item.getDefinitions().getName());
								return false;
							case 4:
								araxxi.getRewards().toArray()[slotId] = null;
								break;
							case 64:
								player.getBank().addItems(new Item[] {araxxi.getRewards().toArray()[slotId]}, true);
								araxxi.getRewards().toArray()[slotId] = null;
								break;
							case 61:
								boolean added = true;
								if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
									added = player.getInventory().addItem(item);
									if (added) {
										araxxi.getRewards().toArray()[slotId] = null;
									}
								} else {
									for (int i = 0; i < item.getAmount(); i++) {
										Item single = new Item(item.getId());
										if (!player.getInventory().addItem(single)) {
											added = false;
											break;
										}
										araxxi.getRewards().remove(single);
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
				player.getInterfaceManager().closeScreenInterface();
				return false;
		}
		player.getInterfaceManager().closeScreenInterface();
		return true;
	}

	private boolean canFinish(){
		if(player.getControllerManager().getController() == this)
			return true;
		return false;
	}

	@Override
	public void process() {
		if(canPro){
			int regId = player.getRegionId();
			@SuppressWarnings("unused")
			List<Integer> npcs = World.getRegion(regId).getNPCsIndexes();
			if (araxxi.isFinished) {
				canPro = false;
				WorldTasksManager.schedule(new WorldTask() {
					int stage;

					@Override
					public void run() {

						if (stage == 1) {
							player.sm("You have 60 seconds before the room collapses.");

						} else if (stage == 30) {
							player.sm("You have 30 seconds before the room collapses.");

						} else if (stage == 60) {
							if(canFinish()){
								endGame();
								completeGame();
							}

							stop();
						}
						stage++;
					}

				}, 0, 1);
			}
		}
	}

	public void completePlayer(Player player){
		player.getControllerManager().forceStop();
		player.setNextWorldTile(LOBBY);
	}

	public void completeGame() {
		destroyMap();
		int rId = player.getRegionId();
		deleteNPCs(rId);
	}

	public void endGame() {
		player.setForceMultiArea(false);
		player.getControllerManager().forceStop();
		player.setNextWorldTile(EXIT);
		player.stopAll();
	}

	public void endGame(WorldTile tile) {
		player.setForceMultiArea(false);
		player.getControllerManager().forceStop();
		Magic.sendNormalTeleportSpell(player, 0, 0, tile);
		player.stopAll();
	}

	public void cleanupGame() {
		destroyMap();
		int rId = player.getRegionId();
		deleteNPCs(rId);
	}

	private void deleteNPCs(int regionId) {
		List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
		if (npcsIndexes != null) {
			for (int npcIndex : npcsIndexes) {
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null/* || npc.isDead() */|| npc.hasFinished()) {
					continue;
				}
				if (npc.getId() == 17182) {
					npc.finish();
				}

			}
		}
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

	@Override
	public boolean sendDeath() {
		final int rId = player.getRegionId();
		player.lock(7);
		player.stopAll();
		if (player.getFamiliar() != null) {
			player.getFamiliar().sendDeath(player);
		}

		final WorldTile graveTile = GRAVEYARD;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.sm("Oh dear, you have died.");
					if (player.getAssassinsManager().getGameMode() == 4) {
						player.getAssassinsManager().resetTask();
						player.sm("You have failed your Assassin's contract, go try another.");
					}
				} else if (loop == 3) {
					player.reset();
					player.getControllerManager().startController("DeathEvent", graveTile, player.hasSkull());
				} else if (loop == 4) {
					if (host.getVoragoPartyMembers().isEmpty()){
						destroyMap();
						deleteNPCs(rId);
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	public int getX() {
		return chunks[0] << 3;
	}

	public int getY() {
		return chunks[1] << 3;
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

	@Override
	public boolean processObjectClick2(WorldObject object){
		int objectId = object.getId();
		switch (objectId) {
			case 91673:
				player.setNextWorldTile(LOBBY);
				endGame();
				completeGame();

				return false;
		}
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int objectId = object.getId();
		switch (objectId) {
			case 91673:
				player.getDialogueManager().startDialogue("AraxxiReward", araxxi);
				//npc.openRewardChest(true);
				return true;

		}

		return true;

	}


}