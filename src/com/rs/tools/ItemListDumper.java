package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;

public class ItemListDumper {

	public static void main(String[] args) {
		try {
			new ItemListDumper();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ItemListDumper() throws IOException {
		Cache.init();
		File file = new File("data/itemList900.txt");
		if (file.exists()) {
			file.delete();
		} else {
			file.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.flush();
		for (int id = 0; id < Utils.getItemDefinitionsSize(); id++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(id);
			if (def.getName().contains("null") || def.name == null|| def.getName().contains("Null"))
				continue;
			writer.append(id+" - "+def.getName());
			writer.newLine();
			writer.flush();
			System.out.println(id + " - " + def.name);
		}
		writer.close();
	}

	  public static int convertInt(String str) {
	    try {
	      int i = Integer.parseInt(str);
	      return i; } catch (NumberFormatException e) {
	    }
	    return 0;
	  }

}
