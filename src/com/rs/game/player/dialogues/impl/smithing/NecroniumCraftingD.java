package com.rs.game.player.dialogues.impl.smithing;

import com.rs.game.player.Skills;
import com.rs.game.player.actions.smithing.rework.NecroniumCrafting;
import com.rs.game.player.actions.smithing.rework.NecroniumCrafting.Necronium;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.SkillsDialogue.ItemNameFilter;
import com.rs.game.player.dialogues.Dialogue;

public class NecroniumCraftingD extends Dialogue {
	
	private Necronium[] scale;

	@Override
	public void start() {
		scale = (Necronium[]) parameters[1];
		int count = 0;
		int[] ids = new int[scale.length];
		for (Necronium scale : scale)
			ids[count++] = scale.getProductItem().getId();
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "Which armour piece would you like to create?", 1, ids, new ItemNameFilter() {
			int count = 0;

			@Override
			public String rename(String name) {
				Necronium scale = Necronium.values()[count++];
				if (player.getSkills().getLevel(Skills.SMITHING) < scale.getLevelRequired()) {
					return name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + scale.getLevelRequired();
				}
				if (!player.getInventory().containsItems(scale.getItemsRequired()) && player.getSkills().getLevel(Skills.SMITHING) >= scale.getLevelRequired()) {
					return name = "<col=ff0000>" + name + "<br><col=ff0000>" + (scale.getItemsRequired()[0].getAmount() - player.getInventory().getAmountOf(scale.getItemsRequired()[0].getId())) + " " + scale.getItemsRequired()[0].getDefinitions().getName();
				}
				return name + "<br>" + scale.getItemsRequired()[0].getAmount() + " " + scale.getItemsRequired()[0].getDefinitions().getName();

			}
		});
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		int idx = SkillsDialogue.getItemSlot(componentId);
		if (idx > scale.length) {
			end();
			return;
		}
		player.getActionManager().setAction(new NecroniumCrafting(scale[idx], SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {  }
}