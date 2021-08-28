package com.rs.game.npc.combat.impl.bosses;

import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.godwars2.Helwyr;
import com.rs.game.npc.godwars2.Helwyr.HelwyrAttacks;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class HelwyrCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 22438 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		if (!(target instanceof Player)) {
			npc.switchTarget(true);
			return 0;
		}
		Helwyr helwyr = (Helwyr) npc;
		int attackProgress = helwyr.getAttackProgress();
		HelwyrAttacks attack = Helwyr.attacks[attackProgress];
		if (!Utils.isOnRange(helwyr, target, helwyr.getAttackDistance(attack)))
			return 0;
		helwyr.setAttackProgress(attackProgress + 1 >= Helwyr.attacks.length ? 0 : attackProgress + 1);
		return attack.sendAttack(helwyr, (Player) target);
	}

}
