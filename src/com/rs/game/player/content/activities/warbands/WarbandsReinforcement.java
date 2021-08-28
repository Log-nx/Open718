package com.rs.game.player.content.activities.warbands;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.pet.Pet;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class WarbandsReinforcement extends NPC {

	private static final long serialVersionUID = -6657891026470205329L;
	public Warbands warband;

	public WarbandsReinforcement(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setForceAgressive(true);
		setForceMultiArea(true);
	}

	@Override
	public void processEntity() {
		super.processEntity();
		if (Warbands.warband == null || this.warband != null && this.warband != Warbands.warband)
			sendDeath(this);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> npcIndexes = World.getRegion(regionId).getNPCsIndexes();
			if (npcIndexes != null) {
				for (int npcIndex : npcIndexes) {
					NPC n = World.getNPCs().get(npcIndex);
					if (n == null || n.isDead() || n.hasFinished()
							|| !n.withinDistance(this, 64)
							|| !clipedProjectile(n, false)
							|| Warbands.warband != null
							&& n instanceof WarbandsReinforcement
							|| n instanceof Pet || n instanceof Familiar)
						continue;
					possibleTarget.add(n);
				}
			}
		}
		return possibleTarget;
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
					setNextAnimation(new Animation(defs != null ? defs.getDeathEmote() : 836));
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
}