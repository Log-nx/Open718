package com.rs.game.npc.instances;

import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.corp.CorporealBeast;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class InstanceLegioPrimus extends NPC {

	public InstanceLegioPrimus(int id, WorldTile tile) {
		super(id, tile, -1, true, true);	
		setForceMultiArea(true);
		setNoDistanceCheck(true);
	}	
	

	@Override
	public void sendDeath(Entity source) {
		final Player player = getMostDamageReceivedSourcePlayer();
		final NPCCombatDefinitions defs = getCombatDefinitions();
		Familiar familiar = (Familiar) source;
		final WorldTile tile = this;
		super.sendDeath(source);
		if (source instanceof Player)
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					if (player != null) {
						if (player.getTimer() != 0 || player.getTimer() > 0) {			
							new InstanceLegioPrimus(17149, tile);	
						}
						} else {
							new InstanceLegioPrimus(17149, tile);	
						}
					if (World.canMoveNPC(getPlane(), tile.getX() + 1,
							tile.getY(), 1))
						tile.moveLocation(1, 0, 0);
					else if (World.canMoveNPC(getPlane(), tile.getX() - 1,
							tile.getY(), 1))
						tile.moveLocation(-1, 0, 0);
					else if (World.canMoveNPC(getPlane(), tile.getX(),
							tile.getY() - 1, 1))
						tile.moveLocation(0, -1, 0);
					else if (World.canMoveNPC(getPlane(), tile.getX(),
							tile.getY() + 1, 1))
						tile.moveLocation(0, 1, 0);
					finish();
					stop();
				}
				loop++;
			}
		}, 0, player.getSpawnRate());		
		if (source instanceof Familiar)
			WorldTasksManager.schedule(new WorldTask() {
				int loop;

				@Override
				public void run() {
					if (loop == 0) {
					} else if (loop >= defs.getDeathDelay()) {
						reset();
						if (familiar.getOwner() != null) {
							if (familiar.getOwner().getTimer() != 0 || familiar.getOwner().getTimer() > 0) {			
								new InstanceLegioPrimus(17149, tile);	
							}
							} else {
								new InstanceLegioPrimus(17149, tile);	
							}
						if (World.canMoveNPC(getPlane(), tile.getX() + 1,
								tile.getY(), 1))
							tile.moveLocation(1, 0, 0);
						else if (World.canMoveNPC(getPlane(), tile.getX() - 1,
								tile.getY(), 1))
							tile.moveLocation(-1, 0, 0);
						else if (World.canMoveNPC(getPlane(), tile.getX(),
								tile.getY() - 1, 1))
							tile.moveLocation(0, -1, 0);
						else if (World.canMoveNPC(getPlane(), tile.getX(),
								tile.getY() + 1, 1))
							tile.moveLocation(0, 1, 0);
						finish();
						stop();
					}
					loop++;
				}
			}, 0, familiar.getOwner().getSpawnRate());	
	}



}