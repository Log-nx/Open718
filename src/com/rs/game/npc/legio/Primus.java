package com.rs.game.npc.legio;

import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;

public class Primus extends NPC {
	
	public Primus(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		if (hit.getLook() == HitLook.MAGIC_DAMAGE
				|| hit.getLook() == HitLook.MELEE_DAMAGE) {
			hit.setDamage(hit.getDamage() / 25);
		}
		int distance = Utils.getDistance(hit.getSource().getX(), hit
				.getSource().getY(), this.getX(), this.getY());
		double damageReduction = 1.3;
		if (distance >= 3)
			damageReduction = 1;
		if (distance >= 6)
			damageReduction = 0.75;
		if (distance >= 8)
			damageReduction = 0.5;
		hit.setDamage((int) (hit.getDamage() * damageReduction));
		super.handleIngoingHit(hit);
	}

}
