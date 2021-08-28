package com.rs.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils.EntityDirection;

public class NPCSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {
		
		/**
		 * Objects of the Fishing Colony Home.
		 */
		World.deleteObject(new WorldTile(2340, 3691, 0));
		World.deleteObject(new WorldTile(2341, 3692, 0));
		World.deleteObject(new WorldTile(2336, 3691, 0));
		World.deleteObject(new WorldTile(2338, 3690, 0));
		World.deleteObject(new WorldTile(2339, 3690, 0));
		World.deleteObject(new WorldTile(2340, 3690, 0));
		World.deleteObject(new WorldTile(2341, 3690, 0));
		World.deleteObject(new WorldTile(2338, 3690, 0));
		World.deleteObject(new WorldTile(2339, 3690, 0));
		World.deleteObject(new WorldTile(2340, 3690, 0));
		World.deleteObject(new WorldTile(2341, 3689, 0));
		World.deleteObject(new WorldTile(2338, 3689, 0));
		World.deleteObject(new WorldTile(2339, 3689, 0));
		World.deleteObject(new WorldTile(2340, 3689, 0));
		World.deleteObject(new WorldTile(2341, 3689, 0));
		World.deleteObject(new WorldTile(2338, 3688, 0));
		World.deleteObject(new WorldTile(2339, 3688, 0));
		World.deleteObject(new WorldTile(2340, 3688, 0));
		World.deleteObject(new WorldTile(2344, 3688, 0));
		World.deleteObject(new WorldTile(2333, 3692, 0));
		World.deleteObject(new WorldTile(2330, 3692 , 0));
		World.deleteObject(new WorldTile(2329, 3693 , 0));
		World.deleteObject(new WorldTile(2328, 3693 , 0));
		World.deleteObject(new WorldTile(2327, 3693 , 0));
		World.deleteObject(new WorldTile(2327, 3691 , 0));
		World.deleteObject(new WorldTile(2327, 3689 , 0));
		World.deleteObject(new WorldTile(2327, 3688 , 0));
		World.deleteObject(new WorldTile(2327, 3687 , 0));
		World.deleteObject(new WorldTile(2327, 3686 , 0));
		World.deleteObject(new WorldTile(2329, 3686 , 0));
		World.deleteObject(new WorldTile(2332, 3687, 0));
		World.deleteObject(new WorldTile(2332, 3686, 0));
		/**
		 * Ending..
		 */
		World.deleteObject(new WorldTile(2690, 3714, 0));
		World.deleteObject(new WorldTile(2689, 3715, 0));
		World.deleteObject(new WorldTile(2689, 3716, 0));
		World.deleteObject(new WorldTile(3100, 3508, 0));
		World.deleteObject(new WorldTile(3100, 3507, 0));
		World.deleteObject(new WorldTile(3099, 3507, 0));
		World.deleteObject(new WorldTile(3098, 3507, 0));
		World.deleteObject(new WorldTile(3097, 3507, 0));
		//World.deleteObject(new WorldTile(3096, 3507, 0));
		World.deleteObject(new WorldTile(3095, 3507, 0));
		World.deleteObject(new WorldTile(3094, 3507, 0));
		World.deleteObject(new WorldTile(3093, 3507, 0));
		World.deleteObject(new WorldTile(3092, 3507, 0));
		World.deleteObject(new WorldTile(3091, 3507, 0));
		World.deleteObject(new WorldTile(3091, 3508, 0));
		World.deleteObject(new WorldTile(3091, 3510, 0));
		World.deleteObject(new WorldTile(3091, 3511, 0));
		World.deleteObject(new WorldTile(3092, 3511, 0));
		World.deleteObject(new WorldTile(3094, 3510, 0));
		World.deleteObject(new WorldTile(3095, 3510, 0));
		World.deleteObject(new WorldTile(3096, 3511, 0));
		World.deleteObject(new WorldTile(3099, 3512, 0));
		World.deleteObject(new WorldTile(3100, 3512, 0));
		World.deleteObject(new WorldTile(3100, 3513, 0));
		World.deleteObject(new WorldTile(3098, 3513, 0));
		World.deleteObject(new WorldTile(3097, 3513, 0));
		World.deleteObject(new WorldTile(3096, 3513, 0));
		World.deleteObject(new WorldTile(3093, 3513, 0));
		World.deleteObject(new WorldTile(3092, 3513, 0));
		World.deleteObject(new WorldTile(3091, 3513, 0));
		World.deleteObject(new WorldTile(3083, 3500, 0)); 
		World.deleteObject(new WorldTile(3090, 3505, 0)); 
		World.deleteObject(new WorldTile(3089, 3505, 0)); 
		World.deleteObject(new WorldTile(3091, 3499, 0));
		World.deleteObject(new WorldTile(3098, 3499, 0));
		World.deleteObject(new WorldTile(3092, 3488, 0));
		World.deleteObject(new WorldTile(3083, 3492, 0));
		World.deleteObject(new WorldTile(3084, 3492, 0));
		World.deleteObject(new WorldTile(3084, 3493, 0));
		World.deleteObject(new WorldTile(3083, 3493, 0));
		/**
		 * Custom spawns..
		 * Custom made NPCs spawns, can be added here.
		 */
		World.spawnObject(new WorldObject(635, 10, 2, 2339, 3679, 0));
		World.spawnObject(new WorldObject(4706, 10, 2, 2342, 3679, 0));
		World.spawnObject(new WorldObject(4705, 10, 2, 2345, 3679, 0));
		World.spawnObject(new WorldObject(13190, 10, 2, 2329, 3685, 0));
		World.spawnObject(new WorldObject(35470, 10, 1, 2351, 3683, 0));
		World.spawnObject(new WorldObject(2563, 10, 2, 2332, 3694, 0));
		World.spawnObject(new WorldObject(782, 10, 2, 2332, 3687, 0));//bank booth
		World.spawnObject(new WorldObject(782, 10, 2, 2331, 3687, 0));//bank booth
		World.spawnObject(new WorldObject(782, 10, 2, 2330, 3687, 0));//bank booth
		World.spawnObject(new WorldObject(782, 10, 2, 2329, 3687, 0));//bank booth
		World.spawnObject(new WorldObject(782, 10, 2, 2328, 3687, 0));//bank booth
		World.spawnObject(new WorldObject(782, 10, 2, 2327, 3687, 0));//bank booth
		World.spawnNPC(13955, new WorldTile(2354, 3685, 0), 1, false, EntityDirection.WEST);
		World.spawnNPC(2538, new WorldTile(2353, 3694, 0), 1, false, EntityDirection.WEST);
		World.spawnNPC(15839, new WorldTile(2328, 3686, 0), 1, false, EntityDirection.NORTH);
		World.spawnNPC(15839, new WorldTile(2331, 3686, 0), 1, false, EntityDirection.NORTH);
		World.spawnNPC(9085, new WorldTile(2351, 3690, 0), 1, false, EntityDirection.WEST);
		World.spawnNPC(519, new WorldTile(2337, 3677, 0), 1, false, EntityDirection.WEST);
		World.spawnNPC(2676, new WorldTile(2327, 3672, 0), 1, false, EntityDirection.NORTH);
		World.spawnNPC(11508, new WorldTile(2329, 3672, 0), 1, false, EntityDirection.NORTH);
		World.spawnNPC(12, new WorldTile(2331, 3672, 0), 1, false, EntityDirection.NORTH);
		World.spawnNPC(3709, new WorldTile(2328, 3680, 0), 1, false, EntityDirection.EAST);
		World.spawnNPC(8977, new WorldTile(2323, 3682, 0), 1, false, EntityDirection.EAST);
		World.spawnNPC(15158, new WorldTile(2342, 3695, 0), 1, false, EntityDirection.SOUTH);
		World.spawnNPC(529, new WorldTile(2334, 3680, 0), 1, false, EntityDirection.NORTH);
		/*
		 * Worldwide 
		 */
		World.spawnNPC(945, new WorldTile(3093, 3111, 0), 1, false, EntityDirection.SOUTH);
		World.spawnNPC(3709, new WorldTile(3089, 3510, 0), 1, false, EntityDirection.WEST);
		World.spawnObject(new WorldObject(12356, 10, 0, 1863, 5317, 0));
		World.spawnObject(new WorldObject(12309, 10, 0, 1866, 5327, 0));
		World.spawnObject(new WorldObject(540, 10, -1, 2984, 3369, 0));
		World.spawnObject(new WorldObject(782, 10, 1, 3093, 3512, 0));//bank booth
	
		}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/npcspawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
						.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
						.parseInt(spawn[3]), faceDir = Integer
						.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new WorldTile(x, y, z), -1, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					WorldTile spawnLoc = new WorldTile(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}