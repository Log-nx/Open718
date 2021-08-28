package com.rs;

import java.util.concurrent.TimeUnit;

import com.alex.store.Index;
import com.rs.cache.Cache;
import com.rs.cache.loaders.BodyDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.instances.BossInstanceHandler;
import com.rs.game.item.WeightManager;
import com.rs.game.npc.combat.CombatScriptsHandler;
import com.rs.game.npc.combat.NPCvsNPC;
import com.rs.game.player.Player;
import com.rs.game.player.actions.fishing.FishingSpotsHandler;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.controllers.ControllerHandler;
import com.rs.game.player.cutscenes.CutscenesHandler;
import com.rs.game.player.dialogues.DialogueHandler;
import com.rs.game.player.managers.LendingManager;
import com.rs.game.worldlist.WorldList;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.BotRank;
import com.rs.utils.Censor;
import com.rs.utils.DTRank;
import com.rs.utils.DiscordBot;
import com.rs.utils.DisplayNames;
import com.rs.utils.IPBanL;
import com.rs.utils.ItemBonuses;
import com.rs.utils.ItemExamines;
import com.rs.utils.ItemWeights;
import com.rs.utils.Logger;
import com.rs.utils.MapArchiveKeys;
import com.rs.utils.MapAreas;
import com.rs.utils.MusicHints;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.NPCExamines;
import com.rs.utils.NPCSpawning;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ObjectSpawns;
import com.rs.utils.OnlineTime;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;

public final class GameServer {

	public static long currentTime;
	private static long ticks = GameServer.currentTime - Utils.currentTimeMillis();
	static int seconds = Math.abs((int) (ticks / 1000) % 60);
	static int minutes = Math.abs((int) ((ticks / (1000 * 60)) % 60));
	static int hours = Math.abs((int) ((ticks / (1000 * 60 * 60)) % 24));
	static int days = Math.abs((int) ((ticks / (1000 * 60 * 60 * 60)) % 24));
	private static DiscordBot discord;

	public static DiscordBot getDiscordBot() {
		return discord;
	}
	
	
	public static void main(String[] args) throws Exception {
		currentTime = Utils.currentTimeMillis();
		Settings.HOSTED = false;
		Settings.DEBUG = true;
		Logger.log("Diccus", "Initing Cache...");
		Cache.init();
		if (Settings.DISCORD) {
			Logger.log("Diccus", "Initiating Discord Bot...");
			 discord = new DiscordBot();
			 TimeUnit.SECONDS.sleep(1);
		}
		ItemsEquipIds.init();
		Huffman.init();
		BodyDefinitions.init();
		Logger.log("Diccus", "Initing Data Files...");
		DisplayNames.init();
		Logger.log("Loading the Grand Exchange...");
		GrandExchange.init();
		IPBanL.init();
		Logger.log("Diccus", "Preparing Lending Manager...");
		LendingManager.init();
		PkRank.init();
		ClansManager.init();
		NPCvsNPC.getSingleton().init();
		OnlineTime.init();
		DTRank.init();
		MapArchiveKeys.init();
		Censor.init();
		MapAreas.init();
		ObjectSpawns.init();
    	ObjectSpawns.addCustomSpawns();
		NPCExamines.init();
		NPCSpawns.init();
		NPCCombatDefinitionsL.init();
		NPCBonuses.init();
		NPCDrops.init();
		ItemExamines.init();
		ItemBonuses.init();
		WeightManager.init();
		ItemWeights.init();
		MusicHints.init();
		BotRank.init();
		NPCSpawning.spawnNPCS();
		ShopsHandler.init();
		Logger.log("Diccus", "Initing WorldList...");
		WorldList.init();
		Logger.log("Diccus", "Initing Fishing Spots...");
		FishingSpotsHandler.init();
		Logger.log("Diccus", "Initing NPC Combat Scripts...");
		CombatScriptsHandler.init();
		Logger.log("Diccus", "Initing Dialogues...");
		DialogueHandler.init();
		Logger.log("Diccus", "Initing Controllers...");
		ControllerHandler.init();
		Logger.log("Diccus", "Initing Cutscenes...");
		CutscenesHandler.init();
		Logger.log("Diccus", "Initing Friend Chat...");
		FriendChatsManager.init();
		Logger.log("Diccus", "Initing Cores Manager...");
		CoresManager.init();
		Logger.log("Diccus", "Initing World...");
		World.init();
		Logger.log("Diccus", "Initing Region Builder...");
		RegionBuilder.init();
		Logger.log("Diccus", "Initing Server Channel Handler...");
		BossInstanceHandler.init();
		Logger.log("Diccus", "Initiating Boss Instances...");
		try {
			ServerChannelHandler.init();
		} catch (Throwable e) {
			Logger.handle(e);
			Logger.log("Diccus", "Failed initing Server Channel Handler. Shutting down...");
			System.exit(1);
			return;
		}
		Logger.log("Diccus", "Server took " + (Utils.currentTimeMillis() - currentTime) + " milli seconds to launch.");
		addAccountsSavingTask();
		addCleanMemoryTask();
		addRecalculatePricesTask();
	}

	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < Settings.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 5, TimeUnit.MINUTES);
	}

	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					saveFiles();
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 30, 50, TimeUnit.SECONDS);
	}

	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			SerializableFilesManager.savePlayer(player);
			if (player.getClanManager() != null && player.getClanManager().getClan() != null) {
				SerializableFilesManager.saveClan(player.getClanManager().getClan());
			}
		}
		DisplayNames.save();
		IPBanL.save();
		PkRank.save();
		DTRank.save();
		OnlineTime.save();
		GrandExchange.save();
	}
	
	  private static void addRecalculatePricesTask() {
			CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					try {
						GrandExchange.recalcPrices();
					} catch (Throwable e) {
						Logger.handle(e);
					}

				}
			}, 1, 12, TimeUnit.HOURS);
		}

	  public static void cleanMemory(boolean force) {
	        if (force) {
	            ItemDefinitions.clearItemsDefinitions();
	            NPCDefinitions.clearNPCDefinitions();
	            ObjectDefinitions.clearObjectDefinitions();
	        }
	        for (Index index : Cache.RS3CACHE.getIndexes())
	            index.resetCachedFiles();
	        CoresManager.fastExecutor.purge();
	        System.gc();
	    }

	public static void shutdown() {
		try {
			closeServices();
		} finally {
			System.exit(0);
		}
	}

	public static void closeServices() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
		if (Settings.HOSTED) {
			try {
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}

	public static void restart() {
		closeServices();
		System.gc();
		try {
			Runtime.getRuntime().exec("java -server -Xms2048m -Xmx20000m -cp bin;/data/libs/netty-3.2.7.Final.jar;/data/libs/FileStore.jar GameServer false false true false");
			System.exit(0);
		} catch (Throwable e) {
			Logger.handle(e);
		}

	}

	private GameServer() {

	}

}
