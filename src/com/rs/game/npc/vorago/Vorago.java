package com.rs.game.npc.vorago;

import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class Vorago extends NPC {

    private VoragoMinion minion;

    public Vorago(int id, WorldTile tile, int mapAreaNameHash,
                  boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        this.setCapDamage(1000);
        setLureDelay(3000);
        getCombatDefinitions().setHitpoints(50000);
		setHitpoints(50000);
        setForceTargetDistance(10);
        setForceFollowClose(true);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
    }

    public void spawnVoragoMinion() {
        if (minion != null)
            return;
        setCapDamage(0);
        minion = new VoragoMinion(this);
    }

    public void removeVoragoMinion() {
        if (minion == null)
            return;
        minion.finish();
        setCapDamage(1000);
        minion = null;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        Entity target = getCombat().getTarget();
        if (target != null && Utils.isOnRange(target.getX(), target.getY(), target.getSize(), getX(), getY(), getSize(), 4) && Utils.random(40) == 0)
            sendTeleport(Utils.random(2) == 0 ? target : this);
        if (isDead())
            return;
    }

    private void sendTeleport(Entity entity) {
        int entitySize = entity.getSize();
        for (int c = 0; c < 10; c++) {
            int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
            if (World.checkWalkStep(entity.getPlane(), entity.getX(), entity.getY(), dir, entitySize)) {
                entity.setNextGraphics(new Graphics(409));
                entity.setNextWorldTile(new WorldTile(getX() + Utils.DIRECTION_DELTA_X[dir], getY() + Utils.DIRECTION_DELTA_Y[dir], getPlane()));
                break;
            }
        }
    }

    @Override
    public void sendDeath(Entity source) {
        super.sendDeath(source);
        if (minion != null)
            minion.sendDeath(source);
    }
}
