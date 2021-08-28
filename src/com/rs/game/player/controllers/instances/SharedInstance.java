package com.rs.game.player.controllers.instances;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;

public class SharedInstance extends Controller {

	private long minutes;
	private long seconds;
	private boolean instanceover;
	private Player leader;

	public static void enterTrioInstance(Player player) {
		player.getControllerManager().startController("SharedControler", 1);
	}


	@Override
	public void start() {
		player.setDestroyTimer(0);
		player.setisDestroytimer(false);
		leader = player.getLeaderName();
		player.setTimer(leader.getTimer());
		try {
		sendInterfaces();
		loadInstance(false);
		} catch (Throwable t) {
			Logger.handle(t);
		}
		instanceover = false;
		player.setSpawnRate(leader.getSpawnRate());
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
					player.setNextAnimation(new Animation(17768));
					player.setNextGraphics(new Graphics(3425));
				} else if (loop == 1) {
					player.sm("You lose 10 minutes from the timer because of your death.");
					leader.sm("A player has died, you lose 10 minutes from your timer. You may kick the player with the portal if needed.");
					leader.setTimer(leader.getTimer() - 600);
				} else if (loop == 3) {			
					player.reset();
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 2);
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
				if (instanceover == true) {
					this.cancel();
				}
				if (leader == null) {
					exitInstance(false);
					player.setNextWorldTile(player.getOutside());
				}
					minutes = TimeUnit.SECONDS.toMinutes(leader.getTimer());
					seconds = leader.getTimer() % 60;
				String String = java.lang.String.format("%02d:%02d", minutes, seconds);
				player.getPackets().sendIComponentText(800, 6, " " + String);
			}
    	}, 0, 500);				
	}			
	
	@Override
	public boolean login() {
		player.setNextWorldTile(player.getOutside());	
		exitInstance(false);
		return true;
	}
	
	
	public void loadInstance(final boolean login) {
		if (player.getHacker() == 2) {
			player.sm("Enter your pin.");
		} else {
		player.lock();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.unlock();
					}

				}, 1);
					CoresManager.fastExecutor.schedule(new TimerTask() {

						@Override
						public void run() {
							try {
								player.setForceMultiArea(leader.isForceMultiArea());
								player.setNextWorldTile(leader);
							} catch (Throwable t) {
								Logger.handle(t);
							}
						}
					}, 1500);
			}
			
		});
		
		}
	}

	@Override
	public void process() {
		if (leader.getTimer() <= 0 && player.getEnd() == false) {
			exitInstance(true);
		}
		if (player.getEnd() == true) {
			exitInstance(false);
			player.setNextWorldTile(player.getOutside());				
			player.setInstanceEnd(false);
		}
		if (player.getInstanceKick() == true) {
			player.setInstanceKick(false);
			exitInstance(false);
			player.setNextWorldTile(player.getOutside());			
		}
		if (player.getLeaderName() == null) {
		if (instanceover == false) {
		player.sm("Could not locate leader.");
		exitInstance(false);
		player.setNextWorldTile(player.getOutside());	
		}
		} 
		if (!player.getInterfaceManager().containsInterface(800) && instanceover == false) {
			sendInterfaces();
		}
	}
	
	public void exitInstance(boolean isTimeEnd) {
		player.setLeaderName(null);
		instanceover = true;
		player.setTimer(0);
		minutes = 0;
		seconds = 0;
		player.setForceMultiArea(false);
		player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 11 : 8);
		removeController();
		if (isTimeEnd == true) {
			player.setDestroyTimer(40);
			player.sm("You will be removed from the instance in 40 seconds.");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (player.isOutside() == true)
						player.setisDestroytimer(true);
					if (player.isDestroytimer() == true) {
						player.sm("The timer has stopped.");
						player.setisDestroytimer(false);
						this.stop();
						return;				
					}			
					if (player.getDestroyTimer() == 20) {
						player.sm("You will be removed from the instance in 20 seconds.");
					}	
					if (player.getDestroyTimer() == 10) {
						player.sm("You will be removed from the instance in 10 seconds.");
					}
					if (player.getDestroyTimer() == 0) {
						player.sm("You have been removed from the instance.");
					player.setNextWorldTile(player.getOutside());
					player.getControllerManager().startController("GodWars");
					this.stop();
					return;
					}
					player.setDestroyTimer(player.getDestroyTimer() - 1);	
				}
			}, 0, 1);
		}		
	}	

	@Override
	public void magicTeleported(int type) {
	}
	

	@Override
	public boolean logout() {
		return false;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 28779) {
			player.getDialogueManager().startDialogue("PartnerPortal");	
		}
		return true;
	}	

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.sm("You can't teleport out of the instance. To exit the instance, click the portal.");
		player.getInterfaceManager().closeChatBoxInterface();
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.sm("You can't teleport out of the instance. To exit the instance, click the portal.");
		player.getInterfaceManager().closeChatBoxInterface();
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.sm("You can't teleport out of the instance. To exit the instance, click the portal.");
		player.getInterfaceManager().closeChatBoxInterface();
		return false;
	}

}
