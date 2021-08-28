package com.rs.game.npc.araxxor;

import com.rs.game.Animation;
import com.rs.game.EffectsManager.EffectType;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.npc.araxxi.AraxyteNPC;
import com.rs.utils.Utils;

public class MirrorBackSpider extends AraxyteSpider {

	private static final long serialVersionUID = 7517172726993286421L;

	/**
	 * Constructs a new object.
	 * @param id
	 * @param tile
	 */
	public MirrorBackSpider(Entity owner, int id, WorldTile tile) {
		super(id, tile);
		this.owner = owner;
	}

	/**
	 * Sets the owner of the spider.
	 * @param owner
	 * 			{@link Entity} the owner.
	 */
	public void setOwner(Entity owner) {
		this.owner = owner;
		playAnimation();
	}
	
	@Override
	public void processNPC() {
		if (owner == null) {
			finish();
			return;
		} else if (owner.isDead() || owner.hasFinished()) {
			owner.getEffectsManager().removeEffect(EffectType.MIRRORBACK_SPIDER);
			return;
		} else if (!withinDistance(owner, 12)) {
			setNextWorldTile(owner);
			return;
		} else if (!getCombat().process())
			sendFollow();
	}
	
	private void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex())
			setNextFaceEntity(owner);
		int size = getSize();
		int targetSize = owner.getSize();
		if (Utils.colides(getX(), getY(), size, owner.getX(), owner.getY(), targetSize) && !owner.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + targetSize, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), owner.getY() + targetSize)) {
						resetWalkSteps();
						if (!addWalkSteps(getX(), owner.getY() - size)) {
							return;
						}
					}
				}
			}
			return;
		}
		resetWalkSteps();
	}

	private void playAnimation() {
		setNextAnimation(new Animation(24054));
		setNextGraphics(new Graphics(4982));
	}

}