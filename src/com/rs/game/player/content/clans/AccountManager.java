package com.rs.game.player.content.clans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import com.rs.game.player.Player;
import com.rs.utils.Logger;
import com.rs.utils.Utils;



public class AccountManager {

	 private static final String CLAN_PATH = "data/clans/accountManager/clansManager/";
    


	public synchronized static boolean containsClan(String name) {
		return new File(CLAN_PATH + name + ".c").exists();
	}

	public synchronized static Clan loadClan(String name) {
		try {
			return (Clan) loadSerializedFile(new File(CLAN_PATH + name + ".c"));
		}
		catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
		    
	public synchronized static void saveClan(Clan clan) {
		try {
			storeSerializableClass(clan, new File(CLAN_PATH + clan.getClanName() + ".c"));
		}
		catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public synchronized static void deleteClan(Clan clan) {
		try {
			new File(CLAN_PATH + clan.getClanName() + ".c").delete();
		}
		catch (Throwable t) {
			Logger.handle(t);
		
		}
	}
	public static final Object loadSerializedFile(File f) throws IOException,
	ClassNotFoundException {
if (!f.exists())
	return null;
ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
Object object = in.readObject();
in.close();
return object;
}

public static final void storeSerializableClass(Serializable o, File f)
	throws IOException {

ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
out.writeObject(o);
out.close();
}


	private AccountManager() {

	}

}
