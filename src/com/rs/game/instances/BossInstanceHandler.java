package com.rs.game.instances;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.game.instances.impl.GodWars2Instance;

public class BossInstanceHandler {

	public static final Object LOCK = new Object();

	public static enum Boss {
		
		Vindicta(GodWars2Instance.class, 33236, 100000, 20, true, true, new WorldTile(3113, 6897, 1),
				new WorldTile(3111, 6895, 1), null, "GodWars2BossInstanceController", -1),

		TWIN_FURIES(GodWars2Instance.class, 33237, 100000, 20, true, true, new WorldTile(3115, 7060, 1),
				new WorldTile(3112, 7061, 1), null, "GodWars2BossInstanceController", -1),

		GREGOROVIC(GodWars2Instance.class, 33238, 100000, 20, true, true, new WorldTile(3286, 7063, 1),
				new WorldTile(3288, 7064, 1), null, "GodWars2BossInstanceController", -1),

		HELWYR(GodWars2Instance.class, 33239, 100000, 20, true, true, new WorldTile(3279, 6896, 1),
				new WorldTile(3280, 6895, 1), null, "GodWars2BossInstanceController", -1);

		private final Map<String, BossInstance> cachedInstances = Collections.synchronizedMap(new HashMap<String, BossInstance>());
		private Class<? extends BossInstance> instance;
		private int initialCost, maxPlayers, structure, musicId;
		private boolean hasHM, publicVersion;
		private WorldTile insideTile, outsideTile, graveStoneTile;
		private String controllerName;

		private Boss(Class<? extends BossInstance> instance, int structure, int initialCost, int maxPlayers,
				boolean hasHM, boolean publicVersion, WorldTile outsideTile, WorldTile insideTile,
				WorldTile graveStoneTile, String controllerName, int musicId) {
			this.instance = instance;
			this.initialCost = initialCost;
			this.maxPlayers = maxPlayers;
			this.hasHM = hasHM;
			this.publicVersion = publicVersion;
			this.insideTile = insideTile;
			this.outsideTile = outsideTile;
			this.graveStoneTile = graveStoneTile;
			this.controllerName = controllerName;
			this.musicId = musicId;
			this.structure = structure;
		}

		public Map<String, BossInstance> getCachedInstances() {
			return cachedInstances;
		}

		public String getControllerName() {
			return controllerName;
		}

		public WorldTile getInsideTile() {
			return insideTile;
		}

		public WorldTile getOutsideTile() {
			return outsideTile;
		}

		public WorldTile getGraveStoneTile() {
			return graveStoneTile;
		}

		public boolean isHasHM() {
			return hasHM;
		}

		public int getStructure() {
			return structure;
		}

		public int getMaxPlayers() {
			return maxPlayers;
		}

		public int getInitialCost() {
			return initialCost;
		}

		public int getMusicId() {
			return musicId;
		}

		public boolean hasPublicVersion() {
			return publicVersion;
		}

		public static Boss getBossByName(String name) {
			for (Boss b : Boss.values()) {
				if (b.name().equalsIgnoreCase(name))
					return b;
			}
			return null;
		}

	}

	public static void enterInstance(Player player, Boss boss) {
		enterInstance(player, boss, false);
	}

	public static void enterInstance(Player player, Boss boss, boolean old) {
		player.getDialogueManager().startDialogue(old ? "OLDBossInstanceD" : "BossInstanceD", boss);
	}

	private static void createInstance(Player player, Boss boss, int maxPlayers, int minCombat, int spawnSpeed,
			int protection, boolean practiceMode, boolean hardMode) {
		createInstance(player,
				new InstanceSettings(boss, maxPlayers, minCombat, spawnSpeed, protection, practiceMode, hardMode));
	}

	public static void createInstance(Player player, InstanceSettings settings) {
		synchronized (LOCK) {

			try {
				String key = player == null ? "" : player.getUsername();
				BossInstance instance = findInstance(settings.getBoss(), key);
				if (instance == null) {
					if (player == null && !settings.getBoss().publicVersion) {
						Logger.log(BossInstanceHandler.class, "Not a public instance. Can't create it.");
						return;
					}
					instance = settings.getBoss().instance.getDeclaredConstructor(Player.class, InstanceSettings.class)
							.newInstance(player, settings);
					settings.getBoss().cachedInstances.put(key, instance);
				} else {
					// recreating the instance but not gonna replace settings
					// since already exists(instead, increase time)
					settings.setCreationTime(Utils.currentTimeMillis());
					joinInstance(player, settings.getBoss(), key, false); // enter
					// the
					// instance
					// normally
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}

	/*
	 * login means reloging in a public instance(u cant login into private, but lets
	 * keep this in case rs lets u in future)
	 */
	public static BossInstance joinInstance(Player player, Boss boss, String key, boolean login) {
		synchronized (LOCK) {
			BossInstance instance = findInstance(boss, key);
			if (instance == null) { // not username
				Player owner = World.getPlayerByDisplayName(key);
				if (owner != null) {
					key = owner.getUsername();
					instance = findInstance(boss, key);
				}
			}

			if (instance == null) {
				if (key.equals("")) { // supposed to be public instance
					player.getPackets().sendGameMessage("This boss has no public instance.");
					return null;
				}
				player.getPackets().sendGameMessage("That player is offline, or has privacy mode enabled.");
				return null;
			}
			// loading
			if (!instance.isInstanceReady())
				return null;
			if (!key.equals("") && !player.getUsername().equals(key)) {
				if (instance.getSettings().getMinCombat() > player.getSkills().getCombatLevel()) {
					player.getPackets().sendGameMessage("Your combat level is too low to enter this session.");
					return null;
				}
				if (instance.getSettings().getProtection() == BossInstance.FRIENDS_ONLY) {
					Player owner = World.getPlayer(key);
					if (owner == null) {
						player.getPackets().sendGameMessage("That player is offline, or has privacy mode enabled.");
						return null;
					}
				}
				if (instance.getSettings().getMaxPlayers() - 1 <= instance.getPlayersCount()) {
					player.getPackets().sendGameMessage("This instance is full.");
					return null;
				}
			}
			instance.enterInstance(player, login);
			return instance;
		}
	}

	public static BossInstance findInstance(Boss boss, String key) {
		synchronized (LOCK) {
			return boss.cachedInstances.get(key);
		}
	}

	public static final void init() {
		for (Boss boss : Boss.values()) {
			if (!boss.publicVersion)
				continue;
			try {
				createInstance(null, boss, boss.maxPlayers, 1, BossInstance.STANDARD, BossInstance.FFA, false, false);
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}
}