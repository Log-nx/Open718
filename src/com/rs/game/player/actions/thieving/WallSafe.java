package com.rs.game.player.actions.thieving;

import com.rs.game.Animation;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.content.activities.skillingtask.SkillTasks;
import com.rs.utils.Utils;

public class WallSafe extends Action {

	private boolean checked;

	public WallSafe(WorldObject safe) {
		@SuppressWarnings("unused")
		WorldObject safe1 = safe;
	}

	@Override
	public boolean start(Player player) {
		if (player.getSkills().getLevel(Skills.THIEVING) < 50) {
			player.sm("You must have a thieving level of at least 50 in order to crack a safe.");
			return false;
		}
		checked = false;
		return true;
	}

	@Override
	public boolean process(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (!checked) {
			player.getPackets().sendGameMessage("You attempt to pick the lock..." , true);
			player.setNextAnimation(new Animation(2247));
			checked = true;
			return 2;
		} else {
			Item loot = getLoot(player.getInventory().containsItem(ItemIdentifiers.STETHOSCOPE, 1));
			if (loot == null) {
				player.sm("You fail and trigger a trap!");
				player.setNextAnimation(new Animation(3170));
				if (player.getSkills().getLevel(Skills.HITPOINTS) <= 30)
					player.applyHit(new Hit(player, Utils.random(40), HitLook.REGULAR_DAMAGE));
				else
					player.applyHit(new Hit(player, Utils.random(100), HitLook.REGULAR_DAMAGE));
			} else {
				player.getSkillTasks().decreaseTask(SkillTasks.CRACK1);
				player.getSkillTasks().decreaseTask(SkillTasks.CRACK2);
				player.getSkillTasks().decreaseTask(SkillTasks.CRACK3);
				player.getSkillTasks().decreaseTask(SkillTasks.CRACK4);
				int amount = loot.getAmount();
				if (loot.getId() == 995)
					amount = loot.getAmount() * (80 + Utils.getRandom(80));
				player.getInventory().addItemMoneyPouch(new Item(loot.getId(), amount));
				player.getSkills().addXp(Skills.THIEVING, 70);
				player.getPackets().sendGameMessage("You successfully crack the safe!" , true);
			}
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		this.setActionDelay(player, 2);
	}

	private Item getLoot(boolean b) {
		Item item = null;
		int r = Utils.random(100);
		if (b)
			r += 10;
		if (r >= 20 && r <= 38)
			item = new Item(ItemIdentifiers.COINS, Utils.random(20, 40));
		else if (r >= 39 && r <= 50)
			item = new Item(ItemIdentifiers.UNCUT_SAPPHIRE, 1);
		else if (r >= 51 && r <= 58)
			item = new Item(ItemIdentifiers.UNCUT_EMERALD, 1);
		else if (r >= 59 && r <= 63)
			item = new Item(ItemIdentifiers.UNCUT_RUBY, 1);
		else if (r >= 20 && r <= 66)
			item = new Item(ItemIdentifiers.COINS, Utils.random(20, 40));
		else if (r >= 67 && r <= 80)
			item = new Item(ItemIdentifiers.UNCUT_SAPPHIRE, 1);
		else if (r >= 81 && r <= 90)
			item = new Item(ItemIdentifiers.UNCUT_EMERALD, 1);
		else if (r >= 91)
			item = new Item(ItemIdentifiers.UNCUT_RUBY, 1);
		return item;
	}

}