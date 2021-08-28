package com.rs.game.player.cutscenes;

import java.util.ArrayList;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.cutscenes.actions.CutsceneAction;
import com.rs.game.player.cutscenes.actions.LookCameraAction;
import com.rs.game.player.cutscenes.actions.PosCameraAction;

public class NexCutScene extends Cutscene {

	private final WorldTile dir;
	private final int selected;

	public NexCutScene(WorldTile dir, int selected) {
		this.dir = dir;
		this.selected = selected;
	}

	@Override
	public boolean hiddenMinimap() {
		return false;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		int xExtra = 0;
		int yExtra = 0;


		if (selected == 0 || selected == 1)
			yExtra -= 7;
		else
			xExtra += 7;

		ArrayList<CutsceneAction> actionsList = new ArrayList<>();
		actionsList.add(new PosCameraAction(getX(player, 2925 + xExtra), getY(
				player, 5203 + yExtra), 2500, -1));
		actionsList.add(new LookCameraAction(getX(player, dir.getX()), getY(
				player, dir.getY()), 2500, 3));
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
