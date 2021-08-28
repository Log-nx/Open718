package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.utils.Utils;

public class ObjectListDumper {

	public static void main(String[] args) throws IOException {
		Cache.init();
		File file = new File("data/objectList900.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.flush();
		for (int id = 0; id < Utils.getObjectDefinitionsSize(); id++) {
			ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(id);
			if (def.name == null || def.name.contains("null"))
				continue;
			writer.append(id + " - " + def.name);
			writer.newLine();
			System.out.println(id + " - " + def.name);
			writer.flush();
		}
		writer.close();
	}

}
