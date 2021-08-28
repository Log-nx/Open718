package com.rs.game.player.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class RiseOfTheSix extends Controller {

	private int[] regionBase;
	public WorldTile base;

	public boolean spawned;
	protected NPC bossNPC;
	
	private int WaveId;
	
	public int Chest = 18804;
	public int Barrier = 31314;
	
	public int dharok = 2026;
	public int verac = 2030;
	public int guthan = 2027;
	public int torag = 2029;
	public int ahrim = 2025;
	public int karil = 2028;
	
	private static final Item[] COMMUM_REWARDS = {
		new Item(558, 17950),
		new Item(562, 7730),
		new Item(560, 3910),
		new Item(565, 1640),
		new Item(4740, 1880),
		new Item(1128,10),
		new Item(1514, 210),
		new Item(15271, 150),
		new Item(1748, 80),
		new Item(9245, 60),
		new Item(1392, 45),
		new Item(452, 34),
		new Item(5316, 4),
		new Item(5303, 9),
		new Item(5302, 10)
	};
	
	private static final Item[] RARE_REWARDS = {
		new Item(1149, 1),
		new Item(987, 1),
		new Item(985, 1),
		new Item(4708, 1),
		new Item(4710, 1),
		new Item(4712, 1),
		new Item(4714, 1),
		new Item(4716, 1),
		new Item(4718, 1),
		new Item(4720, 1),
		new Item(4722, 1),
		new Item(4724, 1),
		new Item(4726, 1),
		new Item(4728, 1),
		new Item(4730, 1),
		new Item(4732, 1),
		new Item(4734, 1),
		new Item(4736, 1),
		new Item(4738, 1),
		
	};
	 
	 Item[] BARROW_REWARDS = {
			 new Item(4745, 1),
				new Item(4747, 1),
				new Item(4749, 1),
				new Item(4751, 1),
				new Item(4753, 1),
				new Item(4755, 1),
				new Item(4757, 1),
	};

	@Override
	public void start() {
		// = RegionBuilder.findEmptyChunkBound(8, 8); 
		//RegionBuilder.copyAllPlanesMap(418, , regionChucks[0], regionChucks[1], 8);
		regionBase = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(418, 1176, regionBase[0], regionBase[1], 8, 8);
		player.setNextWorldTile(getWorldTile(14, 15));
		player.getInventory().deleteItem(29941, 1);
		player.sm("As you enter a Barrow totem gets destroyed...");
		player.sm("Enter the barrier to begin.");
		WaveId = 0;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == Chest) {
			lootChest();
		}
		if (object.getId() == Barrier) {
			passBarrier();
		}
		return false;
	}
	
	private boolean noSpaceOnInv;
	
	public void drop(Item item) {
		Item dropItem = new Item(item.getId(),Utils.random(item.getDefinitions().isStackable() ? item.getAmount()*Settings.DROP_RATE : item.getAmount()) + 1);
		if(!noSpaceOnInv && player.getInventory().addItem(dropItem)) 
			return;
		noSpaceOnInv = true;
		player.getBank().addItem(dropItem, false);
		player.getPackets().sendGameMessage("Your loot was placed into your bank.");
	}
	
	public void lootChest() {
		if (Utils.random(20) == 0)
			drop(BARROW_REWARDS[Utils.random(BARROW_REWARDS.length)]);
		if(Utils.random(10) == 0)
			drop(RARE_REWARDS[Utils.random(RARE_REWARDS.length)]);
		if(Utils.random(1) == 0) 
			drop(COMMUM_REWARDS[Utils.random(COMMUM_REWARDS.length)]);
		drop(new Item(995, 50000));
		drop(new Item(29940, 2));
		player.rosTrips++;
		player.sm("You managed to slay all the Barrows brothers and escape with some loot.");
		player.setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
		player.setForceMultiArea(false);
		removeController();
	}
	
	@Override
	public void process() {
		if(spawned) {
			List<Integer> npcsInArea = World.getRegion(player.getRegionId()).getNPCsIndexes();
			if(npcsInArea == null || npcsInArea.isEmpty()) {
				spawned = false;
				WaveId += 1;
				nextWave(WaveId);
				System.out.println("next");
			}
		}
		
	}
	private void passBarrier() {
		player.setNextWorldTile(getWorldTile(14, 21));
		player.setForceMultiArea(true);
		barrowsBros1();
		barrowsBros2();
		barrowsBros3();
		barrowsBros4();
		barrowsBros5();
		barrowsBros6();
	}
	
	public void barrowsBros1() { 
		bossNPC = new NPC(ahrim, getWorldTile(10, 27), -1, true, true);
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		spawned = true;
	}
	
	public void barrowsBros2() { 
		bossNPC = new NPC(dharok, getWorldTile(13, 29), -1, true, true);
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		spawned = true;
	}
	
	public void barrowsBros3() { 
		bossNPC = new NPC(guthan, getWorldTile(16, 29), -1, true, true);
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		spawned = true;
	}
	
	public void barrowsBros4() { 
		bossNPC = new NPC(karil, getWorldTile(19, 27), -1, true, true);
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		spawned = true;
	}
	public void barrowsBros5() { 
		bossNPC = new NPC(torag, getWorldTile(16, 26), -1, true, true);
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		spawned = true;
	}
	
	public void barrowsBros6() { 
		bossNPC = new NPC(verac, getWorldTile(13, 26), -1, true, true);
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		spawned = true;
	}
	
	private void nextWave(int waveid) {
		if(waveid == 1) {
			player.getPackets().sendGameMessage("Congratulations! You've defeated the Barrows Brothers.");
			player.getPackets().sendGameMessage("You now have access to the chest.");
			World.spawnObject(new WorldObject(Chest, 10, 0, getWorldTile(14, 32)));
			return;
		}
	}
	
	public WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile(regionBase[0] * 8 + mapX, regionBase[1] * 8
				+ mapY, 0);
	}
	
	@Override
	public boolean logout() {
		removeController();
		return true;
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
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
						player.getEquipment().init();
						player.getInventory().init();
					player.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
					player.reset();
					removeController();					
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
	public void magicTeleported(int type) {
		removeController();
	}
}
