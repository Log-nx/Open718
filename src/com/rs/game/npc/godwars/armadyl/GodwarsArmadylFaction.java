package com.rs.game.npc.godwars.armadyl;

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
import com.rs.game.npc.godwars.zamorak.GodwarsZamorakFaction;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.Controller;
import com.rs.game.player.controllers.GodWars;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class GodwarsArmadylFaction extends NPC {

	public GodwarsArmadylFaction(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setForceAgressive(true);
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
							GodWars godController = (GodWars) Controller;
							player.armadyl += 1;
							player.getPackets().sendIComponentText(601, 8, "" + player.armadyl + "");
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
	
	@Override
	public boolean checkAgressivity() {
		NPCCombatDefinitions defs = getCombatDefinitions();
		if (!defs.isAgressive())
			return false;
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
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
							|| npc instanceof GodwarsArmadylFaction
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
			if (name.contains("Armadyl Helmet")
					|| name.contains("Armadyl mitre")
					|| name.contains("Armadyl full helm")
					|| name.contains("Armadyl coif")
					|| name.contains("Torva full helm")
					|| name.contains("Pernix cowl")
					|| name.contains("Virtus mask"))
				return true;
			else if (name.contains("Armadyl cloak"))
				return true;
			else if (name.contains("Armadyl pendant")
					|| name.contains("Armadyl stole"))
				return true;
			else if (name.contains("Armadyl godsword")
					|| name.contains("Armadyl crozier")
					|| name.contains("Zaryte Bow"))
				return true;
			else if (name.contains("Armadyl body")
					|| name.contains("Armadyl robe top")
					|| name.contains("Armadyl chestplate")
					|| name.contains("Armadyl platebody")
					|| name.contains("Torva platebody")
					|| name.contains("Pernix body")
					|| name.contains("Virtus robe top"))
				return true;
			else if (name.contains("Illuminated book of law")
					|| name.contains("Book of law")
					|| name.contains("Armadyl kiteshield"))
				return true;
			else if (name.contains("Armadyl robe legs")
					|| name.contains("Armadyl plateskirt")
					|| name.contains("Armadyl chaps")
					|| name.contains("Armadyl platelegs")
					|| name.contains("Armadyl Chainskirt")
					|| name.contains("Torva platelegs")
					|| name.contains("Pernix chaps")
					|| name.contains("Virtus robe legs"))
				return true;
			else if (name.contains("Armadyl vambraces"))
				return true;
		}
		return false;
	}
}