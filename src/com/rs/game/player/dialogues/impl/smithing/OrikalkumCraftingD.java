package com.rs.game.player.dialogues.impl.smithing;

import com.rs.game.player.Skills;
import com.rs.game.player.actions.smithing.rework.OrikalkumCrafting;
import com.rs.game.player.actions.smithing.rework.OrikalkumCrafting.Orikalkum;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.SkillsDialogue.ItemNameFilter;
import com.rs.game.player.dialogues.Dialogue;

public class OrikalkumCraftingD extends Dialogue {
	
	private Orikalkum[] scale;

	@Override
	public void start() {
		scale = (Orikalkum[]) parameters[1];
		int count = 0;
		int[] ids = new int[scale.length];
		for (Orikalkum scale : scale)
			ids[count++] = scale.getProductItem().getId();
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "Which armour piece would you like to create?", 1, ids, new ItemNameFilter() {
			int count = 0;

			@Override
			public String rename(String name) {
				Orikalkum scale = Orikalkum.values()[count++];
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
		player.getActionManager().setAction(new OrikalkumCrafting(scale[idx], SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {  }
}
