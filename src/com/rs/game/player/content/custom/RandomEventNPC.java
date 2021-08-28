package com.rs.game.player.content.custom;

import com.rs.game.npc.NPC;
import com.rs.game.npc.others.NatureSpiritNPC;
import com.rs.game.npc.others.WizardFinixNPC;
import com.rs.game.npc.random.ChronicleFragmentNPC;
import com.rs.game.npc.random.CookNPC;
import com.rs.game.npc.random.DwarvenMinerNPC;
import com.rs.game.npc.random.EliNPC;
import com.rs.game.npc.random.FireSpirit;
import com.rs.game.npc.random.LiquidGoldNymph;
import com.rs.game.npc.random.RiverTrollNPC;
import com.rs.game.player.Player;

public class RandomEventNPC {
	
	public static boolean handleNPC(Player player, final NPC npc) {
		if (npc.getId() == 14 && npc instanceof LiquidGoldNymph) {
			LiquidGoldNymph goldNymph = (LiquidGoldNymph) npc;
			goldNymph.giveReward(player);
			return true;
		}
		if (npc.getId() == 15419 && npc instanceof WizardFinixNPC) {
			WizardFinixNPC wizard = (WizardFinixNPC) npc;
			wizard.giveReward(player);
			return true;
		}
		if (npc.getId() == 17347 && npc instanceof EliNPC) {
			EliNPC miner = (EliNPC) npc;
			miner.giveReward(player);
			return true;
		}
		if (npc.getId() == 2551 && npc instanceof DwarvenMinerNPC) {
			DwarvenMinerNPC miner = (DwarvenMinerNPC) npc;
			miner.giveReward(player);
			return true;
		}
		if (npc.getId() == 11454 && npc instanceof RiverTrollNPC) {
			RiverTrollNPC penguin = (RiverTrollNPC) npc;
			penguin.giveReward(player);
			return true;
		}
		if (npc.getId() == 5910 && npc instanceof CookNPC) {
			CookNPC penguin = (CookNPC) npc;
			penguin.giveReward(player);
			return true;
		}
		if (npc.getId() == 1051 && npc instanceof NatureSpiritNPC) {
			NatureSpiritNPC spirit = (NatureSpiritNPC) npc;
			spirit.giveReward(player);
			return true;
		}
		if (npc.getId() == 15451 && npc instanceof FireSpirit) {
			FireSpirit spirit = (FireSpirit) npc;
			spirit.giveReward(player);
			return true;
		}
		if (npc.getId() == 18204 && npc instanceof ChronicleFragmentNPC) {
			ChronicleFragmentNPC chronicle = (ChronicleFragmentNPC) npc;
			chronicle.giveReward(player);
			return true;
		}
		return false;
	}

}
