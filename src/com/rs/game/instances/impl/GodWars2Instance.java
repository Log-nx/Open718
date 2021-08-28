package com.rs.game.instances.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.game.Animation;
import com.rs.game.EffectsManager.EffectType;
import com.rs.game.instances.BossInstance;
import com.rs.game.instances.BossInstanceHandler;
import com.rs.game.instances.InstanceSettings;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.godwars2.Helwyr;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import static com.rs.game.RegionBuilder.destroyRegion;

public class GodWars2Instance extends BossInstance {

	public static final WorldTile MIN_FIGHT_AREA = new WorldTile(3090, 6866, 1),
			MAX_FIGHT_AREA = new WorldTile(3110, 6894, 1);

	private List<Player> playersInBattle;
	private boolean battleStarted;
	private NPC boss;
	private GodWars2BossInfo info;
	private WorldObject bank;

	public GodWars2Instance(Player owner, InstanceSettings settings) {
		super(owner, settings);
		playersInBattle = new CopyOnWriteArrayList<Player>();
		switch (getBoss()) {
		case Vindicta:
			info = GodWars2BossInfo.Vindicta;
			break;
		case TWIN_FURIES:
			info = GodWars2BossInfo.TWIN_FURIES;
			break;
		case GREGOROVIC:
			info = GodWars2BossInfo.GREGOROVIC;
			break;
		case HELWYR:
			info = GodWars2BossInfo.HELWYR;
			break;
		default:
			break;
		}
	}

	@Override
	public int[] getMapPos() {
		switch (getBoss()) {
		case Vindicta:
			return new int[] { 384, 857 };
		case TWIN_FURIES:
			return new int[] { 384, 880 };
		case GREGOROVIC:
			return new int[] { 407, 879 };
		case HELWYR:
			return new int[] { 407, 856 };
		default:
			return null;
		}
	}

	@Override
	public int[] getMapSize() {
		return new int[] { 1, 1 };
	}

	@Override
	public void loadMapInstance() {
		switch (getBoss()) {
		case Vindicta:
			bank = new WorldObject(4483, 11, 1, getTile(new WorldTile(3119, 6896, 1)));
			World.spawnObject(bank);
			break;
		case TWIN_FURIES:
			bank = new WorldObject(4483, 11, 2, getTile(new WorldTile(3118, 7056, 1)));
			World.spawnObject(bank);
			break;
		case GREGOROVIC:
			bank = new WorldObject(4483, 11, 3, getTile(new WorldTile(3286, 7064, 1)));
			World.spawnObject(bank);
			break;
		case HELWYR:
			bank = new WorldObject(4483, 11, 0, getTile(new WorldTile(3279, 6899, 1)));
			World.spawnObject(bank);
			break;
		default:
			break;
		}
	}

	@Override
	public void enterInstance(Player player, boolean login) {
		synchronized (BossInstanceHandler.LOCK) {
			Logger.log("God Wars 2", player.getUsername() + player.getSession().getIP() + " entered Instance: " + getInstanceName() + ".");
			if (!login || !isPublic())
				player.useStairs(-1, getTile(player.getTile()), 0, 2);
			getPlayers().remove(player);
			getPlayers().add(player);
			playMusic(player);
			if (!isPublic()) {
				player.getPackets()
						.sendGameMessage("Welcome to this session agaisnt <col=00FFFF>" + getInstanceName()
								+ "</col>. This arena will expire in " + (getSettings().getTimeRemaining() / 60000)
								+ " minutes.");
				player.getPackets()
						.sendGameMessage("Respawn Speed: "
								+ (getSettings().getSpawnSpeed() == BossInstance.FASTEST ? "Fastest"
										: getSettings().getSpawnSpeed() == BossInstance.FAST ? "Fast" : "Standard")
								+ ".");
			}
			if (!login)
				player.getControllerManager().startController(getSettings().getBoss().getControllerName(), this);
			player.setLastBossInstanceKey(getOwner() == null ? null : getOwner().getUsername());
			player.setNPCViewDistanceBits(7);
			if (isPublic())
				enterBattle(player);
		}
	}

	@Override
	public String getInstanceName() {
		return getSettings().getBoss().name().replace("_", " ")
				+ (getSettings().isHardMode() ? " (Challenge Mode)" : "");
	}

	@Override
	public void leaveInstance(Player player, int type) {
		synchronized (BossInstanceHandler.LOCK) {
			player.stopAll();
			player.getEffectsManager().removeEffect(EffectType.HELWYR_BLEED);
			player.lock();
			player.resetCombat();
			player.setNPCViewDistanceBits(5);
			if (getPlayers().size() <= 1 && !this.isPublic()) {
				destroyRegion(player.getRegionId());
			}
			if (type == EXITED)
				player.useStairs(-1, getSettings().getBoss().getOutsideTile(), 0, 2);
			else if (type == LOGGED_OUT)
				player.setLocation(getSettings().getBoss().getOutsideTile());
			player.getMusicsManager().reset();
			getPlayers().remove(player);
			playersInBattle.remove(player);
			if (getPlayers().isEmpty() || (isPublic() && playersInBattle.isEmpty())) {
				finish();
			}
			player.unlock();
		}
	}

	public void enterBattle(Player player) {
		playersInBattle.add(player);
		player.useStairs(-1, getTile(getSettings().getBoss().getInsideTile()), 0, 2);
		if (!battleStarted)
			startBattle();
	}

	public void leaveBattle(Player player) {
		playersInBattle.remove(player);
		player.stopAll();
		player.useStairs(-1, getTile(getSettings().getBoss().getOutsideTile()), 0, 2);
		if (playersInBattle.isEmpty()) { // public version
			if (boss != null && !boss.hasFinished()) {
				boss.finish();
			}
			boss = null;
			battleStarted = false;
		}
	}

	private void startBattle() {
		battleStarted = true;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (playersInBattle.isEmpty() || boss != null)
					return;
				switch (getBoss()) {
				case HELWYR:
					boss = new Helwyr(22438, getTile(info.getSpawnTile()), GodWars2Instance.this);
					boss.setNextAnimation(new Animation(28200));
					boss.setNextGraphics(new Graphics(6085));
					break;
				default:
					break;
				}
			}
		}, info.getSpawnTicks());
	}

	public void updateInterface(boolean sendInterface) {
		for (Player player : playersInBattle) {
			if (player == null || player.isDead())
				continue;
			updateInterface(player, sendInterface);
		}
	}

	public void removeInterfaces() {
		for (Player player : playersInBattle) {
			if (player == null || player.isDead())
				continue;
			player.getInterfaceManager().removeScreenInterface();
		}
	}

	public void updateInterface(Player player, boolean sendInterface) {
		if (info == null || boss == null)
			return;
	}

	@Override
	public void finish() {
		if (boss != null && !boss.hasFinished())
			boss.finish();
		boss = null;
		battleStarted = false;
		if (!isPublic() && bank != null)
			World.removeObject(bank);
		super.finish();
	}

	public boolean isPlayerInsideBattle(Player player) {
		synchronized (BossInstanceHandler.LOCK) {
			return playersInBattle.contains(player);
		}
	}

	public int getPlayersInBattleCount() {
		return playersInBattle.size();
	}

	public List<Player> getPlayersInBattle() {
		return playersInBattle;
	}

	public boolean isBattleStarted() {
		return battleStarted;
	}

	public NPC getBossNPC() {
		return boss;
	}

	public GodWars2BossInfo getBossInfo() {
		return info;
	}

	public static enum GodWars2BossInfo {
		Vindicta(33228, 10, new WorldTile(3102, 6877, 1)),

		TWIN_FURIES(33226, 10, new WorldTile(3099, 7077, 1)),

		GREGOROVIC(33225, 10, new WorldTile(3299, 7076, 1)),

		HELWYR(33227, 10, new WorldTile(3290, 6881, 1));

		private int dataId;
		private int spawnTicks;
		private WorldTile spawnTile;

		private GodWars2BossInfo(int dataId, int spawnTicks, WorldTile spawnTile) {
			this.dataId = dataId;
			this.spawnTicks = spawnTicks;
			this.spawnTile = spawnTile;
		}

		public int getDataId() {
			return dataId;
		}

		public int getSpawnTicks() {
			return spawnTicks;
		}

		public WorldTile getSpawnTile() {
			return spawnTile;
		}

	}

}