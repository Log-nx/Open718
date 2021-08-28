package com.rs.game.npc.vorago;

import java.util.ArrayList;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class VoragoMinion extends NPC {

    private Vorago beast;
    private Entity target;

    public VoragoMinion(Vorago beast) {
        super(8834, beast, -1, true, true);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
        this.beast = beast;
        changeTarget = 2;
    }

    private int changeTarget;
    private int delay;

    @Override
    public void processNPC() {
        if (isDead() || hasFinished())
            return;
        if (delay > 0) {
            delay--;
            return;
        }
        if (changeTarget > 0) {
            if (changeTarget == 1) {
                ArrayList<Entity> possibleTarget = beast.getPossibleTargets();
                if (possibleTarget.isEmpty()) {
                    finish();
                    beast.removeVoragoMinion();
                    return;
                }
                target = possibleTarget.get(Utils.getRandom(possibleTarget
                        .size() - 1));
                setNextWorldTile(new WorldTile(target));
            }
            changeTarget--;
            return;
        }
        if (target == null || target.getX() != getX()
                || target.getY() != getY() || target.getPlane() != getPlane()) {
            changeTarget = 5;
            return;
        }
        int damage = Utils.getRandom(200) + 50;
        target.applyHit(new Hit(this, Utils.random(1, 131), HitLook.MAGIC_DAMAGE));
        beast.heal(damage);
        delay = getPoison().isPoisoned() ? 10 : 3;
        if (target instanceof Player) {
            Player player = (Player) target;
            player.getPackets()
                    .sendGameMessage(
                            "The living rock patriarch steals some life from you for its master.");
        }
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public void sendDeath(Entity source) {
        super.sendDeath(source);
        beast.removeVoragoMinion();
    }

}
