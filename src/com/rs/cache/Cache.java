package com.rs.cache;

import com.alex.io.OutputStream;
import com.alex.store.Store;
import com.alex.util.whirlpool.Whirlpool;
import com.rs.Settings;
import com.rs.cache.loaders.rs2.ItemDefinitionsRs2;
import com.rs.cache.loaders.rs2.NPCDefinitionsRs2;
import com.rs.cache.loaders.rs3.MapInformationParser;
import com.rs.utils.Utils;

import java.io.IOException;

public final class Cache {

	public static Store RS3CACHE;

	private Cache() {

	}

	public static void init() throws IOException {
		MapInformationParser.init();
		ItemDefinitionsRs2.init();
		NPCDefinitionsRs2.init();
		RS3CACHE = new Store(Settings.CACHE_PATH_RS3);
	}

	public static final byte[] generateUkeysFile() {
		OutputStream stream = new OutputStream();
		stream.writeByte(RS3CACHE.getIndexes().length);
		for (int index = 0; index < RS3CACHE.getIndexes().length; index++) {
			if (RS3CACHE.getIndexes()[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				stream.writeBytes(new byte[64]);
				continue;
			}
			stream.writeInt(RS3CACHE.getIndexes()[index].getCRC());
			stream.writeInt(RS3CACHE.getIndexes()[index].getTable().getRevision());
			stream.writeBytes(RS3CACHE.getIndexes()[index].getWhirlpool());
		}
		byte[] archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archive, 0, archive.length);
		OutputStream hashStream = new OutputStream(65);
		hashStream.writeByte(0);
		hashStream.writeBytes(Whirlpool.getHash(archive, 0, archive.length));
		byte[] hash = new byte[hashStream.getOffset()];
		hashStream.setOffset(0);
		hashStream.getBytes(hash, 0, hash.length);
		hash = Utils.cryptRSA(hash, Settings.GRAB_SERVER_PRIVATE_EXPONENT, Settings.GRAB_SERVER_MODULUS);
		stream.writeBytes(hash);
		archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archive, 0, archive.length);
		return archive;
	}

}
