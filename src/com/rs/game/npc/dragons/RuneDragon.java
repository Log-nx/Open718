package com.rs.game.npc.dragons;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.combat.Combat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class RuneDragon extends NPC {

	private static final long serialVersionUID = 8811721304905284108L;

	public RuneDragon(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(952);
		setLureDelay(0);
		setPhase(1);
	}

	private int phase;

	public void setPhase(int i) {
		this.phase = i;
	}

	public int getPhase() {
		return this.phase;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (getId() != 21136 && !isCantInteract() && !isUnderCombat() && getHitpoints() > 0) {
			setCantInteract(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (getPhase() == 2)
						setNextAnimation(new Animation(26528));
					transformIntoNPC(21136);
					setHitpoints(getMaxHitpoints());
					resetReceivedDamage();
					setCantInteract(false);
					stop();
				}
			}, 2);
		}
		if (getHitpoints() < 4705 && getPhase() == 1 && isUnderCombat()) {
			setNextAnimation(new Animation(26526));
			transformIntoNPC(21137);
			setPhase(2);
		}
		if (getHitpoints() < 2520 && getPhase() == 2 && isUnderCombat()) {
			setNextAnimation(new Animation(26528));
			transformIntoNPC(21140);
			setPhase(3);
		}
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (hit != null && hit.getDamage() > 0) {
			if (getPhase() == 1)
				hit.setDamage(hit.getDamage() / 2);
			else if (getPhase() == 2 || getPhase() == 3) {
				hit.setDamage((int) (hit.getDamage() * 0.9));
				if (getPhase() == 2) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE)
						hit.setDamage(0);
				}
			}
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public void sendDeath(final Entity source) {
		setNextNPCTransformation(21136);
		setNextAnimation(new Animation(-1));
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		resetCombat();
		
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0)
					setNextAnimation(new Animation(defs.getDeathEmote()));
				else if (loop == 3) {
					drop();
					reset();
					getCombat().removeTarget();
					setLocation(getRespawnTile());
					finish();
					setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
}