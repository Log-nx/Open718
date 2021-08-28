package com.rs.game.npc.familiar;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import com.rs.game.player.actions.summoning.Summoning.Pouches;

public class Packyak extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1397015887332756680L;

	public Packyak(Player owner, Pouches pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, false);
	}

	@Override
	public int getBOBSize() {
		return 30;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public String getSpecialDescription() {
		return "Use special move on an item in your inventory to send it to your bank.";
	}

	@Override
	public String getSpecialName() {
		return "Winter Storage";
	}

	@Override
	public boolean submitSpecial(Object object) {
		int slotId = (Integer) object;
		if (!getOwner().getBank().hasBankSpace() || getOwner().isLocked()) {
			getOwner().getPackets().sendGameMessage(
					"You are too busy to send items to your bank.");
			return false;
		}
		if (getOwner().getBank().hasBankSpace() && !getOwner().isBurying) {
			getOwner().getBank().depositItem(slotId, 1, true);
			getOwner().getPackets().sendGameMessage(
					"Your Pack Yak has sent an item to your bank.");
			getOwner().setNextGraphics(new Graphics(1316));
			getOwner().setNextAnimation(new Animation(7660));
		}
		return true;
	}
}
