package com.rs.game.npc.godwars.bandos;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.Controller;
import com.rs.game.player.controllers.GodWars;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class GodwarsBandosFaction extends NPC {

	public GodwarsBandosFaction(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setForceAgressive(true);
	}

	@Override
	public boolean checkAgressivity() {
		NPCCombatDefinitions defs = getCombatDefinitions();
		if (!defs.isAgressive())
			return false;
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playersIndexes != null) {
				for (int playerIndex : playersIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null
							|| player.isDead()
							|| !player.isRunning()
							|| !player
									.withinDistance(
											this,
											getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
													: getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
															: 8)
							|| ((!isAtMultiArea() || !player.isAtMultiArea())
									&& player.getAttackedBy() != this && player
									.getAttackedByDelay() > Utils
									.currentTimeMillis())
							|| !clipedProjectile(player, false)
							|| hasGodItem(player))
						continue;
					possibleTarget.add(player);
				}
			}
			final List<Integer> npcsIndexes = World.getRegion(regionId)
					.getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null
							|| npc == this
							|| npc instanceof GodwarsBandosFaction
							|| npc.isDead()
							|| npc.hasFinished()
							|| !npc.withinDistance(
									this,
									getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
													: 8)
							|| !npc.getDefinitions().hasAttackOption()
							|| ((!isAtMultiArea() || !npc.isAtMultiArea())
									&& npc.getAttackedBy() != this && npc
									.getAttackedByDelay() > Utils
									.currentTimeMillis())
							|| !clipedProjectile(npc, false))
						continue;
					possibleTarget.add(npc);
				}
			}
		}
		if (!possibleTarget.isEmpty()) {
			setTarget(possibleTarget
					.get(Utils.getRandom(possibleTarget.size() - 1)));
			return true;
		}
		return false;
	}

	private boolean hasGodItem(Player player) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null)
				continue; // shouldn't happen
			String name = item.getDefinitions().getName();
			// using else as only one item should count
			if (name.contains("Bandos mitre")
					|| name.contains("Bandos Full helm")
					|| name.contains("Bandos coif")
					|| name.contains("Torva full helm")
					|| name.contains("Pernix cowl")
					|| name.contains("Vitus mask"))
				return true;
			else if (name.contains("Bandos cloak"))
				return true;
			else if (name.contains("Bandos stole"))
				return true;
			else if (name.contains("Ancient mace")
					|| name.contains("Granite mace")
					|| name.contains("Bandos godsword")
					|| name.contains("Bandos crozier")
					|| name.contains("Zaryte Bow"))
				return true;
			else if (name.contains("Bandos body")
					|| name.contains("Bandos robe top")
					|| name.contains("Bandos chestplate")
					|| name.contains("Bandos platebody")
					|| name.contains("Torva platebody")
					|| name.contains("Pernix body")
					|| name.contains("Virtus robe top"))
				return true;
			else if (name.contains("Illuminated book of war")
					|| name.contains("Book of war")
					|| name.contains("Bandos kiteshield"))
				return true;
			else if (name.contains("Bandos robe legs")
					|| name.contains("Bandos tassets")
					|| name.contains("Bandos chaps")
					|| name.contains("Bandos platelegs")
					|| name.contains("Bandos plateskirt")
					|| name.contains("Torva platelegs")
					|| name.contains("Pernix chaps")
					|| name.contains("Virtus robe legs"))
				return true;
			else if (name.contains("Bandos vambraces"))
				return true;
			else if (name.contains("Bandos boots"))
				return true;
		}
		return false;
	}


	@Override
	public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (source instanceof Player) {
						Player player = (Player) source;
						Controller controller = player.getControllerManager()
								.getController();
						if (controller != null && controller instanceof GodWars) {
							player.bandos += 1;
							player.getPackets().sendIComponentText(601, 9, "" + player.bandos + "");
						}
					}
					drop();
					reset();
					setLocation(getRespawnTile());
					finish();
					if (!isSpawned())
						setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
}