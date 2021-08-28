package com.rs.game.npc.familiar;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;

import com.rs.game.player.actions.summoning.Summoning.Pouches;

public class Assassin extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6377458256826528627L;

	public Assassin(Player owner, Pouches pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getMaxHit() {
		return 10 + getOwner().getSkills().getAssassinLevel(Skills.ASSASSIN_CALL) * 3;
	}

	@Override
	public int getMaxHitpoints() {
		if (getOwner() == null)
			return -1;
		return 2500 + getOwner().getSkills().getAssassinLevel(Skills.ASSASSIN_CALL) * 15;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public String getSpecialDescription() {
		return "Attack your opponent.";
	}

	@Override
	public String getSpecialName() {
		return "Attack";
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (getHitpoints() == -1 || getBonuses() != null && getBonuses()[0] == 500) {
			setHitpoints(getMaxHitpoints());
			setCombatLevel(getOwner().getSkills().getAssassinLevel(Skills.ASSASSIN_CALL) * 5);
			final int skill = 30 + getOwner().getSkills().getAssassinLevel(Skills.ASSASSIN_CALL) * 4;
			setBonuses(new int[] { skill, skill, skill, skill, skill, skill, skill, skill, skill, skill });
		}
	}

	@Override
	public boolean submitSpecial(Object object) {
		return true;
	}
}
