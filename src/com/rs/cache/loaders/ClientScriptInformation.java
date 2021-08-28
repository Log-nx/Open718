package com.rs.cache.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.rs.Settings;
import com.rs.utils.Logger;

public class ClientScriptInformation {

    private static final HashMap<Integer, HashMap<Object, Object>> CLIENTDATA = new HashMap<>();

    private static int lastScript;

    public static void init() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(Settings.LOG_PATH + "data/maps/clientScriptMap.txt"));
            String line;
            Object key, value, script = null;
            while ((line = reader.readLine()) != null) {
                String intval = line.substring(9, line.indexOf(" "));
                try {
                    script = Integer.valueOf(intval);
                } catch (NumberFormatException e) {
                    Logger.handle(e);
                }
                CLIENTDATA.put((int) script, new HashMap<>());
                lastScript = (int) script;
                String lineData = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
                String[] data = lineData.split(", ");
                for (String aData : data) {
                    String[] mapdata = aData.split("=");
                    if (mapdata.length != 2)
                        continue;
                    key = mapdata[0];
                    value = mapdata[1];
                    if (key != null && value != null) {
                        HashMap<Object, Object> map = CLIENTDATA.get(script);
                        if (map != null)
                            map.put(key, value);
                    }
                }
            }
            reader.close();
            System.out.println(
                    "[ ClientScriptParser ] loaded ClientScript Data. ClientScriptSize:=" + CLIENTDATA.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, HashMap<Object, Object>> getMap() {
        return CLIENTDATA;
    }

    public static int getLastScript() {
        return lastScript;
    }
}