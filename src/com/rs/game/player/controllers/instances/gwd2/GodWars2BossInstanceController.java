package com.rs.game.player.controllers.instances.gwd2;

import com.rs.game.EffectsManager.EffectType;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.instances.BossInstance;
import com.rs.game.instances.BossInstanceHandler.Boss;
import com.rs.game.instances.impl.GodWars2Instance;
import com.rs.game.instances.impl.GodWars2Instance.GodWars2BossInfo;
import com.rs.game.npc.godwars2.Helwyr;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class GodWars2BossInstanceController extends BossInstanceController {

	private boolean sentInterfaces;

	@Override
	public void process() {
		if (!sentInterfaces && getGodWars2Instance().isPlayerInsideBattle(player)
				&& getGodWars2Instance().getBossNPC() != null && !getGodWars2Instance().getBossNPC().hasFinished())
			//sendInterfaces();
		super.process();
	}

	@Override
	public void sendInterfaces() {
		sentInterfaces = true;
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
			}
		}, 5);
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 101909 || object.getId() == 101906 || object.getId() == 101901
				|| object.getId() == 101897) {// vindicta
			// threshold
			if (getInstance().isPublic())
				return true;
			sendLeaveConfirmation();
			return false;
		}
		if (object.getId() == 101910 || object.getId() == 101907 || object.getId() == 101902
				|| object.getId() == 101898) {// vindicta
			// barrier
			if (getInstance().isPublic() || (getGodWars2Instance().isPlayerInsideBattle(player))) {
				sendLeaveConfirmation();
				return false;
			}
			getGodWars2Instance().enterBattle(player);
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		if (!getGodWars2Instance().isPublic() || (getGodWars2Instance().isPlayerInsideBattle(player))) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You can't leave just like that!");
			return false;
		}
		getGodWars2Instance().leaveInstance(player, BossInstance.TELEPORTED);
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		if (!getGodWars2Instance().isPublic() || (getGodWars2Instance().isPlayerInsideBattle(player))) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You can't leave just like that!");
			return false;
		}
		getGodWars2Instance().leaveInstance(player, BossInstance.TELEPORTED);
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		if (!getGodWars2Instance().isPublic() || (getGodWars2Instance().isPlayerInsideBattle(player))) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You can't leave just like that!");
			return false;
		}
		getGodWars2Instance().leaveInstance(player, BossInstance.TELEPORTED);
		return true;
	}

	public GodWars2Instance getGodWars2Instance() {
		return (GodWars2Instance) getInstance();
	}

	@Override
	public boolean login() {
		Boss boss = getArguments()[0] != null && getArguments()[0] instanceof Boss ? (Boss) getArguments()[0]
				: getGodWars2Instance() != null && getGodWars2Instance().getBoss() != null
						? getGodWars2Instance().getBoss()
						: null;
		if (boss != null)
			player.setNextWorldTile(new WorldTile(boss.getOutsideTile()));
		removeController();
		return false;
	}

	@Override
	public boolean logout() {
		player.getEffectsManager().removeEffect(EffectType.HELWYR_BLEED);
		getGodWars2Instance().leaveInstance(player, BossInstance.LOGGED_OUT);
		removeController();
		return false;
	}

	@Override
	public void moved() {
		if (getGodWars2Instance() == null)
			return;
		if (getGodWars2Instance().getBossInfo() == GodWars2BossInfo.HELWYR) {
			Helwyr helwyr = getGodWars2Instance().getBossNPC() == null ? null
					: (Helwyr) getGodWars2Instance().getBossNPC();
			if (!getGodWars2Instance().isPlayerInsideBattle(player) || helwyr == null || helwyr.hasFinished()) {
				player.getTemporaryAttributtes().remove("mushroom");
				return;
			}
			if (helwyr.getMushrooms().isEmpty())
				return;
			boolean isUnderAnyMushroom = false;
			for (WorldObject mushroom : helwyr.getMushrooms()) {
				if (mushroom == null || mushroom.getId() != 101900)
					continue;
				if (Utils.isOnRange(mushroom, player, 1, 1, 1)) {
					isUnderAnyMushroom = true;
					break;
				}
			}
			if (!isUnderAnyMushroom)
				player.getTemporaryAttributtes().remove("mushroom");
		}
	}

	public void sendLeaveConfirmation() {
		player.getDialogueManager().startDialogue(new Dialogue() {
			@Override
			public void start() {
				sendDialogue("Are you sure you want to leave"
						+ (getGodWars2Instance().isPlayerInsideBattle(player) ? " this battle" : " this instance")
						+ "?",
						getGodWars2Instance().isPlayerInsideBattle(player)
								? "<col=ff0000>If you leave this battle and there are no players inside, the fight will be reset"
								: "<col=ff0000>If you leave this instance and there are no players inside it will be removed.");
			}

			@Override
			public void run(int interfaceId, int componentId) {
				switch (stage) {
				case -1:
					stage = 0;
					sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes i want to leave.", "Nervermind.");
					break;
				case 0:
					if (componentId == OPTION_1) {
						if (!getGodWars2Instance().isPublic() && getGodWars2Instance().isPlayerInsideBattle(player)) {
							getGodWars2Instance().leaveBattle(player);
							sentInterfaces = false;
						} else {
							removeController();
							getGodWars2Instance().leaveInstance(player, BossInstance.EXITED);
						}
					}
					end();
					break;
				}
			}

			@Override
			public void finish() {
			}
		});
	}

}