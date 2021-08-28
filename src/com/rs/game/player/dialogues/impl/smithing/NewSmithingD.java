package com.rs.game.player.dialogues.impl.smithing;

import com.rs.game.WorldObject;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.smithing.rework.NewOres;
import com.rs.game.player.actions.smithing.rework.SmeltingNew.SmeltingBarNew;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.SkillsDialogue.ItemNameFilter;
import com.rs.game.player.dialogues.Dialogue;

public class NewSmithingD extends Dialogue {

	private WorldObject object;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		int[] ids = new int[SmeltingBarNew.values().length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = SmeltingBarNew.values()[i].getProducedBar().getId();
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "How many bars you would like to smelt?<br>Choose a number, then click the bar to begin.", 28, ids, new ItemNameFilter() {
							int count = 0;

							@Override
							public String rename(String name) {
								SmeltingBarNew bar = SmeltingBarNew.values()[count++];
								if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired())
									name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + bar.getLevelRequired();
								return name;

							}
						});
	}

	@Override
	public void run(int interfaceId, int componentId)  {
		NewOres.handleSmithing(player, componentId);
	}

	@Override
	public void finish() {
	}
}
