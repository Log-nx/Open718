package com.rs.cache.loaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.utils.Utils;

public final class ClientScriptMap {

	@SuppressWarnings("unused")
	private char aChar6337;
	@SuppressWarnings("unused")
	private char aChar6345;
	private String defaultStringValue;
	private int defaultIntValue;
	private HashMap<Long, Object> values;

	private static final ConcurrentHashMap<Integer, ClientScriptMap> interfaceScripts = new ConcurrentHashMap<Integer, ClientScriptMap>();

	public static void main(String[] args) throws IOException {

		Cache.init();

		System.out.println(Arrays.toString(Cache.RS3CACHE.getIndexes()[3].getTable().getValidArchiveIds()));
	}

	public static final ClientScriptMap getMap(int scriptId) {
		ClientScriptMap script = interfaceScripts.get(scriptId);
		if (script != null)
			return script;
		byte[] data = Cache.RS3CACHE.getIndexes()[17].getFile(
				scriptId >>> 0xba9ed5a8, scriptId & 0xff);
		script = new ClientScriptMap();
		if (data != null)
			script.readValueLoop(new InputStream(data));
		interfaceScripts.put(scriptId, script);
		return script;

	}

	public HashMap<Long, Object> getValues() {
		return values;
	}

	public Object getValue(long key) {
		if (values == null)
			return null;
		return values.get(key);
	}

	public long getKeyForValue(Object value) {
		for (Long key : values.keySet()) {
			if (values.get(key).equals(value))
				return key;
		}
		return -1;
	}

	public int getSize() {
		if(values == null)
			return 0;
		return values.size();
	}

	public int getIntValue(long key) {
		if (values == null)
			return defaultIntValue;
		Object value = values.get(key);
		if (value == null || !(value instanceof Integer))
			return defaultIntValue;
		return (Integer) value;
	}

	public String getStringValue(long key) {
		if (values == null)
			return defaultStringValue;
		Object value = values.get(key);
		if (value == null || !(value instanceof String))
			return defaultStringValue;
		return (String) value;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1)
			aChar6337 = Utils.method2782((byte) stream.readByte());
		else if (opcode == 2)
			aChar6345 = Utils.method2782((byte) stream.readByte());
		else if (opcode == 3)
			defaultStringValue = stream.readString();
		else if (opcode == 4)
			defaultIntValue = stream.readInt();
		else if (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 8) {
			int count = stream.readUnsignedShort();
			int loop = opcode == 7 || opcode == 8 ? stream.readUnsignedShort()
					: count;
			values = new HashMap<Long, Object>(Utils.getHashMapSize(count));
			for (int i = 0; i < loop; i++) {
				int key = opcode == 7 || opcode == 8 ? stream
						.readUnsignedShort() : stream.readInt();
				Object value = opcode == 5 || opcode == 7 ? stream.readString()
						: stream.readInt();
				values.put((long) key, value);
			}
		}
	}

	private ClientScriptMap() {
		defaultStringValue = "null";
	}


	  public int getDefaultIntValue() {
	return defaultIntValue;
    }
}
