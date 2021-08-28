package com.rs.cache.loaders;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.alex.io.OutputStream;
import com.rs.cache.Cache;
import com.rs.io.InputStream;

public final class GeneralRequirementMap {

	private HashMap<Long, Object> values;
	private int id;

	private static final ConcurrentHashMap<Integer, GeneralRequirementMap> maps = new ConcurrentHashMap<Integer, GeneralRequirementMap>();

	public static final GeneralRequirementMap getMap(int scriptId) {
		GeneralRequirementMap script = maps.get(scriptId);
		if (script != null)
			return script;
		byte[] data = Cache.RS3CACHE.getIndexes()[2].getFile(26, scriptId);
		script = new GeneralRequirementMap();
		if (data != null)
			script.readValueLoop(new InputStream(data));
		script.id = scriptId;
		maps.put(scriptId, script);
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
		if (values == null)
			return 0;
		return values.size();
	}

	public int getIntValue(long key) {
		if (values == null)
			return 0;
		Object value = values.get(key);
		if (value == null || !(value instanceof Integer))
			return 0;
		return (Integer) value;
	}

	public String getStringValue(long key) {
		if (values == null)
			return "";
		Object value = values.get(key);
		if (value == null || !(value instanceof String))
			return "";
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
		if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (values == null)
				values = new HashMap<Long, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				values.put((long) key, value);
			}
		}
	}

	public byte[] encode() {
		if (values == null || values.isEmpty())
			throw new RuntimeException("Values of client script map cant be empty");
		OutputStream stream = new OutputStream();
		stream.writeByte(249);
		stream.writeByte(values.size());
		for (Entry<Long, Object> set : values.entrySet()) {
			if (set == null)
				continue;
			boolean stringValue = set.getValue() instanceof String;
			stream.writeByte(stringValue ? 1 : 0);
			stream.write24BitInt(Integer.valueOf(set.getKey() + ""));
			if (stringValue)
				stream.writeString((String) set.getValue());
			else
				stream.writeInt((int) set.getValue());
		}
		// end
		stream.writeByte(0);

		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}

	public String toString() {
		if (values == null)
			return "null";
		String toString = "[Id=" + id + "], ";
		for (long key : values.keySet()) {
			if (values.get(key) == null)
				continue;
			Object object = values.get(key);
			if (object instanceof String) {
				toString += "[key=" + key + ", text=" + object + "], ";
			} else if (object instanceof Integer) {
				toString += "[key=" + key + ", Int=" + object + "], ";
			}
		}
		return toString;
	}

	private GeneralRequirementMap() {
	}
}