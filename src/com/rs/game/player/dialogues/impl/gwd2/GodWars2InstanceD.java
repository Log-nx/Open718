package com.rs.game.player.dialogues.impl.gwd2;

import com.rs.game.instances.BossInstance;
import com.rs.game.instances.BossInstanceHandler;
import com.rs.game.instances.BossInstanceHandler.Boss;
import com.rs.game.instances.InstanceSettings;
import com.rs.game.item.Item;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Utils;

public class GodWars2InstanceD extends Dialogue {

	private Boss boss;
	private int speed;

	@Override
	public void start() {
		boss = (Boss) parameters[0];
		sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Enter an encounter", "Start/Join custom encounter");
	}

	private void sendCustomEncounter() {
		stage = 0;
		sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Start", "Join", "Rejoin");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			switch (componentId) {
			case OPTION_1:
				BossInstanceHandler.joinInstance(player, boss, "", false);
				end();
				break;
			case OPTION_2:
				sendCustomEncounter();
				break;
			}
			break;
		case 0:
			switch (componentId) {
			case OPTION_1:
				stage = 1;
				player.setLastBossInstanceSettings(new InstanceSettings(boss)); // the
				// settings
				sendOptionsDialogue("CHOOSE BOSS SPAWN SPEED", "Standard (" + boss.getInitialCost() + " GP).",
						"Fast (" + boss.getInitialCost() * 2 + " GP).",
						"Fastest(" + boss.getInitialCost() * 3 + " GP).");
				break;
			case OPTION_2:
				end();
				player.getTemporaryAttributtes().put(Key.JOIN_BOSS_INSTANCE, boss);
				player.getPackets().sendInputNameScript("Enter the name of a player in a battle you wish to join.");
				break;
			default:
				String key = player.getLastBossInstanceKey();
				if (key == null) {
					stage = -2;
					sendDialogue("You do not have a battle to rejoin.");
					return;
				}
				if (BossInstanceHandler.findInstance(boss, key) == null) {

					if (key.equals(player.getUsername()) && player.getLastBossInstanceSettings() != null
							&& player.getLastBossInstanceSettings().getBoss() == boss
							&& player.getLastBossInstanceSettings().hasTimeRemaining()) {
						end();
						BossInstanceHandler.createInstance(player, player.getLastBossInstanceSettings());
						return;
					}

					stage = -2;
					sendDialogue("You do not have a battle to rejoin.");
					return;
				}
				end();
				BossInstanceHandler.joinInstance(player, boss, key, false);
				// You do not have a battle to rejoin.
				break;
			}
			break;
		case 1:
			speed = componentId == OPTION_1 ? BossInstance.STANDARD
					: componentId == OPTION_2 ? BossInstance.FAST : BossInstance.FASTEST;
			stage = 2;
			sendOptionsDialogue("CHALLENGE MODE?", "Yes.", "No.");
			break;
		case 2:
			if (player.getLastBossInstanceSettings() == null) {
				end();
				return;
			}
			end();
			startInstance(componentId == OPTION_1);
			break;
		case -2:
			end();
			break;
		}
	}

	public void startInstance(boolean hardMode) {
		InstanceSettings settings = player.getLastBossInstanceSettings();
		if (settings == null)
			return;
		settings.setHardMode(hardMode);
		settings.setSpawnSpeed(speed);
		settings.setMaxPlayers(settings.getBoss().getMaxPlayers());
		settings.setMinCombat(1);
		settings.setProtection(BossInstance.FFA);
		int initialCost = settings.getBoss().getInitialCost();
		if (speed == BossInstance.FAST)
			initialCost *= 2;
		else if (speed == BossInstance.FASTEST)
			initialCost *= 3;
		if (player.getInventory().getCoinsAmount() < initialCost) {
			player.getPackets().sendGameMessage("You don't have enough coins to start this battle.");
			player.setLastBossInstanceSettings(null);
			return;
		}
		if (initialCost > 0)
			player.getInventory().removeItemMoneyPouch(new Item(995, initialCost));
		settings.setCreationTime(Utils.currentTimeMillis());
		BossInstanceHandler.createInstance(player, settings);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
