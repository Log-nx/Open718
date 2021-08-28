package com.rs.game.npc.godwars.zamorak;

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
public class GodwarsZamorakFaction extends NPC {

	public GodwarsZamorakFaction(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(6000);// Lurable boss
	}

	@Override
	public boolean checkAgressivity() {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		if (!defs.isAgressive())
			return false;
		final ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (final int regionId : getMapRegionsIds()) {
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
							|| npc instanceof GodwarsZamorakFaction
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
		for (final Item item : player.getEquipment().getItems().getItems()) {
			if (item == null)
				continue; // shouldn't happen
			final String name = item.getDefinitions().getName();
			if (name.contains("Zamorak coif") || name.contains("Zamorak mitre")
					|| name.contains("Zamorak full helm")
					|| name.contains("Zamorak halo")
					|| name.contains("Torva full helm")
					|| name.contains("Pernix cowl")
					|| name.contains("Virtus mask"))
				return true;
			else if (name.contains("Zamorak cape")
					|| name.contains("Zamorak cloak"))
				return true;
			else if (name.contains("Unholy symbol")
					|| name.contains("Zamorak stole"))
				return true;
			else if (name.contains("Illuminated unholy book")
					|| name.contains("Unholy book")
					|| name.contains("Zamorak kiteshield"))
				return true;
			else if (name.contains("Zamorak arrows"))
				return true;
			else if (name.contains("Zamorak godsword")
					|| name.contains("Zamorakian spear")
					|| name.contains("Zamorak staff")
					|| name.contains("Zamorak crozier")
					|| name.contains("Zaryte Bow"))
				return true;
			else if (name.contains("Zamorak robe top")
					|| name.contains("Zamorak d'hide")
					|| name.contains("Zamorak platebody")
					|| name.contains("Torva platebody")
					|| name.contains("Pernix body")
					|| name.contains("Virtus robe top"))
				return true;
			else if (name.contains("Zamorak robe legs")
					|| name.contains("Zamorak robe bottom ")
					|| name.contains("Zamorak chaps")
					|| name.contains("Zamorak platelegs")
					|| name.contains("Zamorak plateskirt")
					|| name.contains("Torva platelegs")
					|| name.contains("Pernix chaps")
					|| name.contains("Virtus robe legs"))
				return true;
			else if (name.contains("Zamorak vambraces"))
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
						Controller Controller = player.getControllerManager()
								.getController();
						if (Controller != null && Controller instanceof GodWars) {
							player.zamorak += 1;
							player.getPackets().sendIComponentText(601, 11, "" + player.zamorak + "");
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
