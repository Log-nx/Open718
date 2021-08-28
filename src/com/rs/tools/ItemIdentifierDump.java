package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;

public class ItemIdentifierDump {

	public static List<String> complete = new ArrayList<String>();

	public static void main(String[] args) throws Throwable {
		Cache.init();
		File file = new File("data/itemIdentifierDump5.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
			ItemDefinitions prev = ItemDefinitions.getItemDefinitions(i - 1);
			if (def == null || prev == null)
				continue;
			String name = Normalizer.normalize(
					def.name.toUpperCase().replace(" ", "_")
							.replace("&", "AND").replace("+", "_PLUS")
							.replace("/", "").replace("'", "").replace("-", "")
							.replace("(", "").replace(")", "").replace("?", "")
							.replace("23", "TWO_THIRDS")
							.replace("13", "ONE_THIRD")
							.replace("12", "ONE_FORTH").replace("%", "PERCENT")
							.replace(".", "").replace(",", "").replace(":", "")
							.replace("[", "").replace("]", ""),
					Normalizer.Form.NFD);
			name = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
					.matcher(name).replaceAll("");
			if (def.name.equalsIgnoreCase(""))
				continue;
			else if (def.name.equalsIgnoreCase(prev.name))
				name += "_NOTED";
			if (complete.contains(name))
				continue;
			complete.add(name);
			writer.append("	public static final int " + name + " = " + def.id
					+ ";");
			writer.newLine();
			writer.flush();
			System.out.println("Completed the identifier for " + name + ".");
		}
		writer.close();
	}

}