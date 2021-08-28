package com.rs.game.player.controllers.instances;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.instances.godwars.bandos.InstanceBandos;
import com.rs.game.npc.instances.godwars.bandos.InstanceGrimspike;
import com.rs.game.npc.instances.godwars.bandos.InstanceSteelwill;
import com.rs.game.npc.instances.godwars.bandos.InstanceStrongstack;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;

public class BandosInstance extends Controller {


	private int[] boundChuncks;
	private long minutes;
	private long seconds;
	private boolean instanceover;
	private int count;
	private WorldTile ORIGINAL_LOCATION =  new WorldTile(2861, 5357, 0);

	@Override
	public void start() {
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(357, 668, boundChuncks[0], boundChuncks[1], 8);	
		player.setDestroyTimer(0);
		player.setisDestroytimer(false);
		instanceover = false;
		try { 
		loadInstance();
		sendInterfaces();
		} catch (Throwable t) {
			Logger.handle(t);
		}
		player.setTimer(3600);
	}
		 	
	private void finishTimer() {
		player.setTimer(0);
		player.setInstanceBooth(0);
		minutes = 0;
		seconds = 0;
	}
	
	
    private void startTimer() {
    	WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (instanceover)
					this.stop();
				if (player.getTimer() == 0 || player.getTimer() < 1) {
					player.sm("Your timer has ended! The NPCs will no longer spawn.");
					finishTimer();
					for (Player players: World.getPlayers()) {
						 if (players.getLeaderName() == player) {
	                    	players.setTimer(0);
	                    	players.setInstanceBooth(0);
	                    	}
	                }
					player.setInstanceBooth(0);
					player.setTimer(0);
					this.stop();
					exitInstance(true); 
					player.getControllerManager().startController("GodWars");
					return;
				}
				if (count == 1) {
					try {
					World.spawnObject(new WorldObject(28779, 10, 1, getWorldTileX(16), getWorldTileY(18), 0));
					} catch (Throwable t) {
						Logger.handle(t);
					}
					}
				if (player.getEnd() == true) {
					for (Player players: World.getPlayers()) {
	                    if (players.getLeaderName() == player) {
	                    	players.setInstanceEnd(true);
	        				players.setNextWorldTile(ORIGINAL_LOCATION);
	    					players.sm("The leader has disbanded the group!");
	                }
					}
					player.setInstanceEnd(false);
					player.setNextWorldTile(ORIGINAL_LOCATION);
					exitInstance(false);
					player.getControllerManager().startController("GodWars");
					CoresManager.slowExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
						}
					}, 1200, TimeUnit.MILLISECONDS);
					this.stop();
					return;
				} else
				player.setTimer(player.getTimer() - 1);
				if (player.getInstanceBooth() > 0)
				player.setInstanceBooth(player.getTimer());
				for (Player players: World.getPlayers()) {
					 if (players.getLeaderName() == player) {
                    	players.setTimer(player.getTimer());
                    	}
                }		
				count++;
				seconds = player.getTimer() % 60;
				minutes = TimeUnit.SECONDS.toMinutes(player.getTimer());			
			}
    	}, 0, 1);
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
					player.sm("You lose 10 minutes from the timer because of your death.");
					player.setTimer(player.getTimer() - 600);
				} else if (loop == 3) {			
					player.reset();
					player.setNextWorldTile(getWorldTile(16, 18));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}	
	
	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
				player.forceLogout();
			return false;
		}
		return true;
	}
	
	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? 11 : 8, 800);	
		player.getPackets().sendIComponentText(800, 7, "Timer:");
    	CoresManager.fastExecutor.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (instanceover)
					this.cancel();
			 String String = java.lang.String.format("%02d:%02d", minutes, seconds);
				player.getPackets().sendIComponentText(800, 6, " " + String);
			}
    	}, 0, 400);				
	}			
	
	@Override
	public boolean login() {
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(357, 668, boundChuncks[0], boundChuncks[1], 8);	
		player.setNextWorldTile(ORIGINAL_LOCATION);
		loadInstance();
		count = 0;
		sendInterfaces();
		return false;
	}
	
	public void startWave() {
		try {
		new InstanceBandos(6260, getSpawnTile(0));
		new InstanceStrongstack(6261, getSpawnTile(0));
		new InstanceSteelwill(6263, getSpawnTile(0));
		new InstanceGrimspike(6265, getSpawnTile(0));
		} catch (Throwable t) {
			Logger.handle(t);
		}
	}
	
	
	public void loadInstance() {
		if (player.getHacker() == 2) {
			player.sm("Enter your pin.");
		} else {
		player.lock();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				player.sm("Processing, please wait....");
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {	
						try {
						player.setForceMultiArea(true);
						player.unlock();
						player.setNextWorldTile(getWorldTile(15, 18));					
						World.spawnObject(new WorldObject(28779, 10, 1, getWorldTileX(16), getWorldTileY(18), 0));						
						startWave();
						startTimer();
						player.sm("Instance loaded. If you don't see a portal, or the room is white, please relog.");
						} catch (Throwable t) {
							Logger.handle(t);
						}
					}
				}, 1);	
			}
			
		});
		
		}
	}

	public WorldTile getSpawnTile(int num) {
		switch (num) {
		case 0:
			return getWorldTile(9, 19);
		case 1:
			return getWorldTile(15, 25);
		case 2:
			return getWorldTile(22, 18);
		default:
			return getWorldTile(32, 30);
		}
	}




	@Override
	public void process() {
		if (!player.getInterfaceManager().containsInterface(800)) 
			sendInterfaces();
	}
	
	public void exitInstance(boolean isTimeEnd) {
		player.setInstancePin(0);
		instanceover = true;
		player.setTimer(0);
		minutes = 0;
		seconds = 0;
		finishTimer();
		player.setForceMultiArea(false);
		player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 11 : 8);
		removeController();
		if (isTimeEnd) {
			player.setDestroyTimer(60);
			for (Player players: World.getPlayers()) {
				 if (players.getLeaderName() == player) {
               	players.sm("You will be removed from the instance in 60 seconds.");
               	}
           }
			player.sm("You will be removed from the instance in 60 seconds.");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (player.getDestroyTimer() == 40) {
						player.sm("You will be removed from the instance in 40 seconds.");
					for (Player players: World.getPlayers()) {
						 if (players.getLeaderName() == player) {
							 players.sm("You will be removed from the instance in 40 seconds.");
		               	}
		           }
					}
					if (player.isDestroytimer() == true) {
						player.setisDestroytimer(false);
						RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
						this.stop();
						return;				
					}					
					if (player.getDestroyTimer() == 20) {
						player.sm("You will be removed from the instance in 20 seconds.");
					for (Player players: World.getPlayers()) {
						 if (players.getLeaderName() == player) {
							 players.sm("You will be removed from the instance in 20 seconds.");
		               	}
		           }
					}	
					if (player.getDestroyTimer() == 10) {
						player.sm("You will be removed from the instance in 10 seconds.");
					for (Player players: World.getPlayers()) {
						 if (players.getLeaderName() == player) {
							 players.sm("You will be removed from the instance in 10 seconds.");
		               	}
		           }
					}
					if (player.getDestroyTimer() == 0) {
						player.sm("You have been removed from the instance.");
					for (Player players: World.getPlayers()) {
						 if (players.getLeaderName() == player) {
								player.sm("You have been removed from the instance.");
		               	}
		           }
					RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
					player.setNextWorldTile(ORIGINAL_LOCATION);
					player.getControllerManager().startController("GodWars");
					this.stop();
					return;
					}
					player.setDestroyTimer(player.getDestroyTimer() - 1);	
					seconds = player.getTimer() % 60;
					minutes = TimeUnit.SECONDS.toMinutes(player.getTimer());
				}
			}, 0, 1);
		}
		}	

	@Override
	public void magicTeleported(int type) {
	}

	public WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile(boundChuncks[0] * 8 + mapX, boundChuncks[1] * 8
				+ mapY, 0);
	}
	public int getWorldTileX(int mapX) {
		return boundChuncks[0] * 8 + mapX;
	}	
	public int getWorldTileY(int mapY) {
		return boundChuncks[1] * 8 + mapY;
	}	
	

	@Override
	public boolean logout() {
		for (Player players: World.getPlayers()) {
			 if (players.getLeaderName() == player) {
            		players.setLeaderName(null);
            	}
        }
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder
						.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
		return false;

	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 28779) {
			player.getDialogueManager().startDialogue("TrioPortal");		
		}
		return true;
	}	

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.sm("You can't teleport out of the instance. To end the instance, click the portal.");
		player.getInterfaceManager().closeChatBoxInterface();
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.sm("You can't teleport out of the instance. To end the instance, click the portal.");
		player.getInterfaceManager().closeChatBoxInterface();
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.sm("You can't teleport out of the instance. To end the instance, click the portal.");
		player.getInterfaceManager().closeChatBoxInterface();
		return false;
	}
}
