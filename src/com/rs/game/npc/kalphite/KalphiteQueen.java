package com.rs.game.npc.kalphite;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.actions.combat.PlayerCombat;
import com.rs.game.player.controllers.instances.KalphiteQueenInstance;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class KalphiteQueen extends NPC {

	public KalphiteQueen(int id, WorldTile tile) {
		super(id, tile, -1, true, true);	
		setForceMultiArea(true);
		setNoDistanceCheck(true);
		setLureDelay(0);
		setIntelligentRouteFinder(true);
		setForceAgressive(true);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (getId() == 1158) {
		if (hit.getLook() == HitLook.RANGE_DAMAGE || hit.getLook() == HitLook.MAGIC_DAMAGE || hit.getSource() instanceof Familiar) {
			hit.setDamage(0);
		}
	}
		if (getId() == 1160) {
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				hit.setDamage(0);
				}
			}
		super.handleIngoingHit(hit);
	}
	
	@Override
	public void sendDeath(Entity source) {
		final Player player = getMostDamageReceivedSourcePlayer();
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		if (source instanceof Player)
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (getId() == 1158) {
						setCantInteract(true);
						setNextNPCTransformation(getId() == 16707 ? 16708 : 1160);
						setNextGraphics(new Graphics(getId() == 16707 ? 5037 : 5038));
						setNextAnimation(new Animation(24293));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								reset();
								setCantInteract(false);
							}
							
						}, 5);
					} else {
						drop();
						reset();
						setLocation(getRespawnTile());
						finish();
						if (!isSpawned())
							setRespawnTask();
						transformIntoNPC(1158);
						if (player.getControllerManager().getController() instanceof KalphiteQueenInstance && player.getTimer() != 0 || player.getTimer() > 0) {
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									new KalphiteQueen(1158, getRespawnTile());
								}
								
							}, player.getSpawnRate());
						}
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
}
