package com.rs.cache.loaders.rs2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.rs.utils.Logger;

public class NPCDefinitionsRs2 {

    private static final HashMap<Integer, HashMap<Object, Object>> DATA_MAP = new HashMap<>();

    public static void init() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("./data/rs2/Rs2npcdefs.txt"));
            String line;
            Object key, value, npcdef = null;
            while ((line = reader.readLine()) != null) {
                String intval = line.substring(6, line.indexOf(" "));
                try {
                    npcdef = Integer.valueOf(intval);
                } catch (NumberFormatException e) {
                    Logger.handle(e);
                }
                DATA_MAP.put((int) npcdef, new HashMap<>());
                String lineData = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
                String[] data = lineData.split(", ");
                for (int i = 0; i < data.length; i++) {
                    String[] mapdata = data[i].split("=");
                    if (mapdata.length != 2)
                        continue;
                    key = mapdata[0];
                    value = mapdata[1];
                    if (key != null && value != null) {
                        HashMap<Object, Object> map = DATA_MAP.get(npcdef);
                        if (map != null)
                            map.put(key, value);
                    }
                }
            }
            reader.close();
            System.out.println("[ NpcDefs RS2 ] loaded NpcDefs -> Size:=" + DATA_MAP.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, HashMap<Object, Object>> getMap() {
        return DATA_MAP;
    }
}