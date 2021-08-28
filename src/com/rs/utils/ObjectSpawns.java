package com.rs.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;

public final class ObjectSpawns {

	private static BufferedReader in;

	public static final void init() {
		if (!new File("data/map/packedSpawns").exists())
			packObjectSpawns();
	}

	private static final void packObjectSpawns() {
		Logger.log("ObjectSpawns", "Packing object spawns...");
		if (!new File("data/map/packedSpawns").mkdir())
			throw new RuntimeException("Couldn't create packedSpawns directory.");
		try {
			in = new BufferedReader(new FileReader("data/map/unpackedSpawnsList.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ");
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid Object Spawn line: " + line);
				String[] splitedLine2 = splitedLine[0].split(" ");
				String[] splitedLine3 = splitedLine[1].split(" ");
				if (splitedLine2.length != 3 || splitedLine3.length != 4)
					throw new RuntimeException("Invalid Object Spawn line: " + line);
				int objectId = Integer.parseInt(splitedLine2[0]);
				int type = Integer.parseInt(splitedLine2[1]);
				int rotation = Integer.parseInt(splitedLine2[2]);

				WorldTile tile = new WorldTile(Integer.parseInt(splitedLine3[0]), Integer.parseInt(splitedLine3[1]),
						Integer.parseInt(splitedLine3[2]));
				addObjectSpawn(objectId, type, rotation, tile.getRegionId(), tile,
						Boolean.parseBoolean(splitedLine3[3]));
			}
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadObjectSpawns(int regionId) {
		File file = new File("data/map/packedSpawns/" + regionId + ".os");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int objectId = buffer.getShort() & 0xffff;
				int type = buffer.get() & 0xff;
				int rotation = buffer.get() & 0xff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				@SuppressWarnings("unused")
				boolean cliped = buffer.get() == 1;
				World.spawnObject(new WorldObject(objectId, type, rotation, x, y, plane));
			}
			channel.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final void addObjectSpawn(int objectId, int type, int rotation, int regionId, WorldTile tile,
			boolean cliped) {
		try {
			DataOutputStream out = new DataOutputStream(
					new FileOutputStream("data/map/packedSpawns/" + regionId + ".os", true));
			out.writeShort(objectId);
			out.writeByte(type);
			out.writeByte(rotation);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(cliped);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add's custom object spawns after world has initialized.
	 */
	public static void addCustomSpawns() {
		
		// Warbands Level 1
		World.spawnObject(new WorldObject(83301, 10, 0, 3029, 3589, 0), true);
		World.spawnObject(new WorldObject(83310, 10, 0, 3032, 3596, 0), true);
		World.spawnObject(new WorldObject(83319, 11, 2, 3038, 3594, 0), true);
		World.spawnObject(new WorldObject(83328, 10, 1, 3038, 3588, 0), true);
		World.spawnObject(new WorldObject(83336, 10, 1, 3047, 3592, 0), true);

		// Warbands Level 2
		World.spawnObject(new WorldObject(83301, 10, 0, 3299, 3768, 0), true);
		World.spawnObject(new WorldObject(83310, 10, 0, 3314, 3772, 0), true);
		World.spawnObject(new WorldObject(83319, 11, 2, 3311, 3767, 0), true);
		World.spawnObject(new WorldObject(83328, 10, 1, 3310, 3775, 0), true);
		World.spawnObject(new WorldObject(83336, 10, 1, 3313, 3762, 0), true);

		// Warbands Level 3
		World.spawnObject(new WorldObject(83301, 10, 0, 3124, 3836, 0), true);
		World.spawnObject(new WorldObject(83310, 10, 0, 3132, 3842, 0), true);
		World.spawnObject(new WorldObject(83319, 11, 2, 3138, 3839, 0), true);
		World.spawnObject(new WorldObject(83328, 10, 1, 3134, 3835, 0), true);
		World.spawnObject(new WorldObject(83336, 10, 1, 3135, 3849, 0), true);
		
		/*
		 * ---------- Donation Zone | Seren Stones ----------
		 */
		
		World.spawnObject(new WorldObject(92713, 10, 0, 3766, 4391, 1), true);
		World.spawnObject(new WorldObject(92713, 10, 0, 3766, 4392, 1), true);
		World.spawnObject(new WorldObject(92713, 10, 0, 3766, 4393, 1), true);
		World.spawnObject(new WorldObject(92713, 10, 0, 3764, 4394, 1), true);
		World.spawnObject(new WorldObject(92713, 10, 0, 3763, 4394, 1), true);
		World.spawnObject(new WorldObject(92713, 10, 0, 3762, 4394, 1), true);
		World.spawnObject(new WorldObject(92713, 10, 0, 3764, 4392, 1), true);
		
		/*
		 * ---------- Donation Zone |  ----------
		 */
		
		//Kethsi's world gate
		World.spawnObject(new WorldObject(89742, 10, 0, new WorldTile(2367, 3353, 0)), true);
		
		//Harps in prifddinas
		World.spawnObject(new WorldObject(94059, 10, 0, new WorldTile(2135, 3335, 1)), true);
		World.spawnObject(new WorldObject(94059, 10, 2, new WorldTile(2129, 3335, 1)), true);
		World.spawnObject(new WorldObject(94059, 10, 3, new WorldTile(2132, 3338, 1)), true);
		World.spawnObject(new WorldObject(94059, 10, 1, new WorldTile(2132, 3332, 1)), true);
		
		//Ashdale
		World.spawnObject(new WorldObject(66711, 10, 0, new WorldTile(2474, 2709, 2)), true); //lodestone
		World.spawnObject(new WorldObject(92120, 10, 0, new WorldTile(2504, 2678, 2)), true); //reaper portal
		World.spawnObject(new WorldObject(100248, 10, 3, new WorldTile(2495, 2722, 2)), true); //bank booth
		World.spawnObject(new WorldObject(100247, 10, 3, new WorldTile(2501, 2722, 2)), true); //bank booth
		World.spawnObject(new WorldObject(76274, 10, 3, new WorldTile(2500, 2722, 2)), true); //bank booth
		World.spawnObject(new WorldObject(76274, 10, 3, new WorldTile(2499, 2722, 2)), true); //bank booth
		World.spawnObject(new WorldObject(76274, 10, 3, new WorldTile(2498, 2722, 2)), true); //bank booth
		World.spawnObject(new WorldObject(76274, 10, 3, new WorldTile(2497, 2722, 2)), true); //bank booth
		World.spawnObject(new WorldObject(76274, 10, 3, new WorldTile(2496, 2722, 2)), true); //bank booth
		World.spawnObject(new WorldObject(70765, 10, 0, new WorldTile(2497, 2716, 2)), true); //fire
		World.spawnObject(new WorldObject(93249, 10, 1, new WorldTile(2517, 2722, 2)), true); //port portal
		World.spawnObject(new WorldObject(26945, 10, 0, new WorldTile(2490, 2724, 2)), true); //well of goodwill
		
		//Player ports
		World.spawnObject(new WorldObject(72695, 10, 0, new WorldTile(4070, 7279, 0)), true); //port portal
	}
}