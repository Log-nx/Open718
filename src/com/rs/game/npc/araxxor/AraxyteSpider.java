package com.rs.game.npc.araxxor;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class AraxyteSpider extends NPC {
	
	private static final long serialVersionUID = 6552804569726523937L;
	
	public Entity owner;

	/**
	 * Constructs a new object.
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public AraxyteSpider(int id, WorldTile tile) {
		super(id, tile, -1, false);
	}
	
	public AraxyteSpider(Entity owner, int id, WorldTile tile) {
		super(id, tile, -1, false);
		this.owner = owner;
	}
	
	@Override
	public void sendDeath(Entity source) {
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
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

}