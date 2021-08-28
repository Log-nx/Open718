package com.rs.game.npc.combat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class NPCvsNPC {

	private static final NPCvsNPC INSTANCE = new NPCvsNPC();

	public static NPCvsNPC getSingleton() {
		return INSTANCE;
	}

	private final List<String> lines = new ArrayList<>( );
	private final String path = "./data/npcs/npcvnpc.ini";
	private final File map = new File(path);
	
	public void init() {
		try {
			Logger.log("NPC vs NPC", "Loading NPCs");
			BufferedReader reader = new BufferedReader(new FileReader(map));
			String s;
			while ((s = reader.readLine()) != null) {
				if (s.startsWith("//"))
					continue;
				lines.add(s);
			}
			reader.close();
			Logger.log("NPC vs NPC", "Loaded NPC vs NPC, There are "
					+ lines.size() + " NPCs");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void save() {
		BufferedWriter bf;
		try {
			clearMapFile();
			bf = new BufferedWriter(new FileWriter(path, true));
			for (String line : lines) {
				bf.write(line);
				bf.newLine();
			}
			bf.flush();
			bf.close();
		} catch (IOException e) {
			System.err.println("Error saving Npcvnpc map!");
		}
	}

	private void clearMapFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(map);
			writer.print("");
			writer.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public boolean addTarget(String npcName, String target) {
		boolean found = false;
		for (String line : lines) {
			if (line.startsWith(npcName)) {
				lines.remove(line);
				lines.add(line+","+target);
				found = true;
				break;
			}
		}
		if (!found)
			lines.add(npcName+","+target);
		save();
		return found;
	}
	
	public boolean removeTarget(String npcName, String target) {
		for (String line : lines) {
			String[] info = line.split(",");
			if (getAttacker(info).equals(npcName)) {
				lines.remove(line);
				String newLine = line.replace(","+npcName, "");
				if (newLine.contains(","))
					lines.add(newLine);
				save();
				return true;
			}
		}
		return false;
	}
	
	public boolean removeTargets(String npcName) {
		for (String line : lines) {
			String[] info = line.split(",");
			if (getAttacker(info).equals(npcName)) {
				lines.remove(line);
				save();
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<String> getTargetNames(String npcName) {
		ArrayList<String> targets = new ArrayList<>( );
		for (String line : lines) {
			String[] info = line.split(",");
			if (getAttacker(info).equalsIgnoreCase(npcName)) {
				for (int i = 1; i < info.length; i++) {
					String target = getTarget(info, i);
					if (!target.equals(""))
						targets.add(target.toLowerCase());
				}
				return targets;
			}
		}
		return null;
	}
	
	private static String getAttacker(String[] names) {
		return names[0];
	}
	
	private static String getTarget( String[] names, int index ) {
		return names[index];
	}
	
	public String forceTalk(int id) {
		int random = Utils.getRandom(2);
		switch(id) {
			case 20:
			case 21:
			case 23:
				return "What do you think you're doing?";
			case 8:
				if (random == 0) {
					return "Hand over the money!";
				} else if (random == 1) {
					return "Oo shiny, it's mine now!";
				}
			break;
			case 3267:
			case 3265:
				if (random == 0) {
					return "Come here human. Me want to poke you.";
				} else if (random == 1) {
					return "You humans have no chance to defeat goblin people!";
				}
			break;
			case 7:
				if (random == 0) {
					return "Gonna have a chicken for supper tonight.";
				} else if (random == 1) {
					return "I need some more feathers.";
				}
			break;
		}
		return "";
	}

}
