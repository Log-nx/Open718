package com.rs.game.npc.godwars.saradomin;

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
public class GodwarsSaradominFaction extends NPC {

	public GodwarsSaradominFaction(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setForceAgressive(true);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || player.isDead() || player.hasFinished() || !player.isRunning() || !player.withinDistance(this, getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4 : getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16 : 8) || ((!isAtMultiArea() || !player.isAtMultiArea()) && player.getAttackedBy() != this && player.getAttackedByDelay() > Utils.currentTimeMillis()) || !clipedProjectile(player, false) || hasGodItem(player))
						continue;
					possibleTarget.add(player);
				}
			}
			final List<Integer> npcsIndexes = World.getRegion(regionId)
					.getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null || npc == this || npc instanceof GodwarsSaradominFaction || npc.isDead() || npc.hasFinished() || !npc.withinDistance(this, getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4 : getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16 : 8) || !npc.getDefinitions().hasAttackOption() || ((!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this && npc.getAttackedByDelay() > Utils.currentTimeMillis()) || !clipedProjectile(npc, false))
						continue;
					possibleTarget.add(npc);
				}
			}
		}
		return possibleTarget;
	}

	private boolean hasGodItem(Player player) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null)
				continue; // shouldn't happen
			String name = item.getDefinitions().getName();
			// using else as only one item should count
			if (name.toLowerCase().contains("saradomin"))
				return true;
			if (name.contains("Saradomin coif") || name.contains("Citharede hood") || name.contains("Saradomin mitre") || name.contains("Saradomin full helm") || name.contains("Saradomin halo") || name.contains("Torva full helm") || name.contains("Pernix cowl") || name.contains("Virtus mask"))
				return true;
			else if (name.contains("Saradomin cape") || name.contains("Saradomin cloak"))
				return true;
			else if (name.contains("Holy symbol") || name.contains("Citharede symbol") || name.contains("Saradomin stole"))
				return true;
			else if (name.contains("Saradomin arrow"))
				return true;
			else if (name.contains("Saradomin godsword") || name.contains("Saradomin sword") || name.contains("Saradomin staff") || name.contains("Saradomin crozier") || name.contains("Zaryte Bow"))
				return true;
			else if (name.contains("Saradomin robe top") || name.contains("Saradomin d'hide") || name.contains("Citharede robe top") || name.contains("Monk's robe top") || name.contains("Saradomin platebody") || name.contains("Torva platebody") || name.contains("Pernix body") || name.contains("Virtus robe top"))
				return true;
			else if (name.contains("Illuminated holy book") || name.contains("Holy book") || name.contains(" Saradomin kiteshield"))
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
							player.saradomin += 1;
							player.getPackets().sendIComponentText(601, 10, "" + player.saradomin + "");
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