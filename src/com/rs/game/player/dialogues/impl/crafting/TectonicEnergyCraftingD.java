package com.rs.game.player.dialogues.impl.crafting;

import com.rs.game.player.Skills;
import com.rs.game.player.actions.crafting.TectonicEnergyCrafting;
import com.rs.game.player.actions.crafting.TectonicEnergyCrafting.Tectonic;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.SkillsDialogue.ItemNameFilter;
import com.rs.game.player.dialogues.Dialogue;

public class TectonicEnergyCraftingD extends Dialogue {
	
	private Tectonic[] scale;

	@Override
	public void start() {
		scale = (Tectonic[]) parameters[1];
		int count = 0;
		int[] ids = new int[scale.length];
		for (Tectonic scale : scale)
			ids[count++] = scale.getProduceEnergy().getId();
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "Which armour piece would you like to create?", 
				1, ids, new ItemNameFilter() {
			int count = 0;

			@Override
			public String rename(String name) {
				Tectonic scale = Tectonic.values()[count++];
				if (player.getSkills().getLevel(Skills.RUNECRAFTING) < scale.getLevelRequired())
					name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + scale.getLevelRequired();
				return name;

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
		player.getActionManager().setAction(new TectonicEnergyCrafting(scale[idx], SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {  }
}