package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utils.Utils;

public class NPCListDumper {

	public static void main(String[] args) throws IOException {
		Cache.init();
		File file = new File("data/npcList900.txt");
		if (file.exists()) {
			file.delete();
		} else {
			file.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.flush();
		for (int id = 0; id < Utils.getNPCDefinitionsSize(); id++) {
			try {
				NPCDefinitions def = NPCDefinitions.getNPCDefinitions(id);
				if (NPCDefinitions.getNPCDefinitions(id).name.contains("null") || def.name == null )
				continue;
				writer.append(id + " - " + def.name);
				writer.newLine();
				System.out.println(id + " - " + def.name);
				writer.flush();
			} catch (Exception ex) {

			}
		}
		writer.close();
	}

}
