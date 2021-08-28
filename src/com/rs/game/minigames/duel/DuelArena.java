package com.rs.game.minigames.duel;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.combat.PlayerCombat;
import com.rs.game.player.actions.cooking.Foods.Food;
import com.rs.game.player.content.Commands;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.items.Potions.Pot;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class DuelArena extends Controller {

	enum DuelStage {
		DECLINED, NO_SPACE, SECOND, DONE
	}

	public Player target;

	// private final Item[] FUN_WEAPONS = { new Item(4566) };

	public boolean ifFriendly, isDueling;

	private final WorldTile[] LOBBY_TELEPORTS = { new WorldTile(3367, 3275, 0),
			new WorldTile(3360, 3275, 0), new WorldTile(3358, 3270, 0),
			new WorldTile(3363, 3268, 0), new WorldTile(3370, 3268, 0),
			new WorldTile(3367, 3267, 0), new WorldTile(3376, 3275, 0),
			new WorldTile(3377, 3271, 0), new WorldTile(3375, 3269, 0),
			new WorldTile(3381, 3277, 0) };

	private void accept(boolean firstStage) {
		
		if (target == null || player == null)
			return;
		boolean accepted = (Boolean) player.getTemporaryAttributtes().get(
				"acceptedDuel");
		boolean targetAccepted = (Boolean) target.getTemporaryAttributtes()
				.get("acceptedDuel");
		DuelRules rules = player.getLastDuelRules();
		if (!rules.canAccept(player.getLastDuelRules().getStake()))
			return;
		if (accepted && targetAccepted) {
			if (firstStage) {
				if (nextStage())
					if (target != null) {
						((DuelArena) target.getControllerManager()
								.getController()).nextStage();
					}
			} else {
				player.setCloseInterfacesEvent(null);
				player.closeInterfaces();
				closeDuelInteraction(true, DuelStage.DONE);
			}
			return;
		}
		player.getTemporaryAttributtes().put("acceptedDuel", true);
		refreshScreenMessages(firstStage, ifFriendly);
	}

	public void addItem(int slot, int amount) {
		if (player.isIronman()) {
			player.getDialogueManager().startDialogue("IronMan");
			return;
		}
		if (target.isIronman()) {
			player.sm("Oops, I have forgotten, that I can not stake with an Iron Man.");
			return;
		}
		if (!hasTarget())
			return;
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item cannot be staked!");
			return;
		}
		Item[] itemsBefore = player.getLastDuelRules().getStake()
				.getItemsCopy();
		int maxAmount = player.getInventory().getItems().getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		player.getLastDuelRules().getStake().add(item);
		player.getInventory().deleteItem(slot, item);
		refreshItems(itemsBefore);
		refresh(slot);
		cancelAccepted();
	}

	 private void beginBattle(boolean started) {
	        if (started) {
	            boolean teleport = true;
	            if (!DuelControler.isAtDuelArena(player)) {
	                if (player.isOnSpotDuelingRequest()) {
	                	World.sendWorldMessage("[<col=ab451d>Dueling</col>] <col=ab451d>"+getduelstyle()+" duel has been started between " + player.getDisplayName() + " & " + target.getDisplayName() + "</col>.", false, false);
	                    
	                	//generateRegion();
	                    player.setOnSpotDueling(true);
	                    target.setOnSpotDueling(true);
	                    teleport = false;
	                }
	            }
	            WorldTile[] teleports = getPossibleWorldTiles();
	            int random = Utils.getRandom(1);
	            if (teleport) {
	            player.setNextWorldTile(random == 0 ? teleports[0] : teleports[1]);
	            target.setNextWorldTile(random == 0 ? teleports[1] : teleports[0]);
	            }
	        }
	        player.stopAll();
	        player.lock(2); // fixes mass click steps
	        player.reset();
	        isDueling = true;
	        player.getTemporaryAttributtes().put("startedDuel", true);
	        player.getTemporaryAttributtes().put("canFight", false);
	        player.setCanPvp(true);
	        player.getHintIconsManager().addHintIcon(target, 1, -1, false);
	        WorldTasksManager.schedule(new WorldTask() {
	            int count = 3;

	            @Override
	            public void run() {
	                if (count > 0)
	                    player.setNextForceTalk(new ForceTalk("" + count));
	                if (count == 0) {
	                    player.getTemporaryAttributtes().put("canFight", true);
	                    player.setNextForceTalk(new ForceTalk("FIGHT!"));
	                    this.stop();
	                }
	                count--;
	            }
	        }, 0, 2);
	    }


	@Override
	public boolean canAttack(Entity target) {
		if (player.getTemporaryAttributtes().get("canFight") == Boolean.FALSE) {
			player.getPackets().sendGameMessage("The duel hasn't started yet.",
					true);
			return false;
		}
		if (player.isDead() || target.isDead())
			return false;
		return true;
	}

	public void cancelAccepted() {
		boolean canceled = false;
		if ((Boolean) player.getTemporaryAttributtes().get("acceptedDuel")) {
			player.getTemporaryAttributtes().put("acceptedDuel", false);
			canceled = true;
		}
		if ((Boolean) target.getTemporaryAttributtes().get("acceptedDuel")) {
			target.getTemporaryAttributtes().put("acceptedDuel", false);
			canceled = true;
		}
		if (canceled)
			refreshScreenMessages(canceled, ifFriendly);
	}

	@Override
	public boolean canEat(Food food) {
		if (player.getLastDuelRules().getRule(4) && isDueling) {
			player.getPackets().sendGameMessage(
					"You cannot eat during this duel.", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		DuelRules rules = player.getLastDuelRules();
		if (isDueling) {
			if (rules.getRule(15) && twoHanded(itemId) == true) {
				player.getPackets().sendGameMessage(
						"You can't equip "
								+ ItemDefinitions.getItemDefinitions(itemId)
										.getName().toLowerCase()
								+ " during this duel.");
				return false;
			}
			if (rules.getRule(10 + slotId)) {
				player.getPackets().sendGameMessage(
						"You can't equip "
								+ ItemDefinitions.getItemDefinitions(itemId)
										.getName().toLowerCase()
								+ " during this duel.");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canHit(Entity target) {
		Player p2 = (Player) target;
		if (player.isDead() || p2.isDead())
			return false;
		return true;
	}

	@Override
	public boolean canMove(int dir) {
		if (player.getLastDuelRules().getRule(25) && isDueling) {
			player.getPackets().sendGameMessage(
					"You cannot move during this duel!", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canPot(Pot pot) {
		if (player.getLastDuelRules().getRule(3) && isDueling) {
			player.getPackets().sendGameMessage(
					"You cannot drink during this duel.", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canSummonFamiliar() {
		if (player.getLastDuelRules().getRule(24) && isDueling)
			return true;
		player.getPackets().sendGameMessage(
				"Summoning has been disabled during this duel!");
		return false;
	}

	@Override
	public boolean canUseCommands() {
		player.getPackets().sendGameMessage(
				"You can't use commands at the moment.");
		return false;
	}

	protected void closeDuelInteraction(boolean started, DuelStage duelStage) {
		Player oldTarget = target;
		if (duelStage != DuelStage.DONE) {
			target = null;
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getControllerManager()
							.startController("DuelControler");
				}
			});
			player.getInventory().getItems()
					.addAll(player.getLastDuelRules().getStake());
			player.getInventory().init();
			player.getLastDuelRules().getStake().clear();
		} else {
			removeEquipment();
			beginBattle(started);
		}
		if (oldTarget == null)
			return;
		Controller controler = oldTarget.getControllerManager().getController();
		if (controler == null || !(controler instanceof DuelArena))
			return;
		DuelArena targetConfiguration = (DuelArena) controler;
		if (controler instanceof DuelArena) {
			if (targetConfiguration.hasTarget()) {
				oldTarget.setCloseInterfacesEvent(null);
				oldTarget.closeInterfaces();
				if (duelStage != DuelStage.DONE)
					player.getControllerManager().removeControllerWithoutCheck();
				if (started)
					targetConfiguration.closeDuelInteraction(false, duelStage);
				if (duelStage == DuelStage.DONE)
					player.getPackets().sendGameMessage(
							"Your battle will begin shortly.");
				else if (duelStage == DuelStage.SECOND)
					player.getPackets()
							.sendGameMessage(
									"<col=ff0000>Please check if these settings are correct.");
				else if (duelStage == DuelStage.DECLINED)
					oldTarget.getPackets().sendGameMessage(
							"<col=ff0000>Other player declined the duel!");
				else if (duelStage == DuelStage.DECLINED) {
					oldTarget.getPackets().sendGameMessage(
							"You do not have enough space to continue!");
					oldTarget
							.getPackets()
							.sendGameMessage(
									"Other player does not have enough space to continue!");
				}
			}
		}
	}

	public void endDuel(Player victor, Player loser) {
		endDuel(victor, loser, true);
	}

	public void endDuel(final Player victor, final Player loser,
			boolean removeLoserControler) {
		if (player.isCanPvp() && target.isCanPvp()) {
			startEndingTeleport(victor);
			player.setOnSpotDueling(false);
	        target.setOnSpotDueling(false);
			startEndingTeleport(loser);
			sendFinishInterface(victor, loser);
			CopyOnWriteArrayList<Item> lostItems = new CopyOnWriteArrayList<Item>();
			if (player.getLastDuelRules().getStake() != null) {
				for (Item item : player.getLastDuelRules().getStake()
						.getItems()) {
					if (item == null)
						continue;
					lostItems.add(item);
				}
				for (Item item : player.getLastDuelRules().getStake()
						.getItems()) {
					if (item == null)
						continue;
					if (!victor.getInventory().addItem(item)) {
						World.addGroundItem(item, new WorldTile(player),
								player, true, 180);
						player.getPackets().sendGameMessage(
								"Items were dropped on the ground.");
					}
					ItemDefinitions defs = ItemDefinitions
							.getItemDefinitions(item.getId());
					String name = defs == null ? "" : defs.getName()
							.toLowerCase();
					if (item.getId() == 995 && item.getAmount() >= 50000000) {
						World.sendWorldMessage("<col=ff0000>Stake Log: "
								+ victor.getUsername() + " Won " + name
								+ ", amount: " + item.getAmount() + " from "
								+ loser.getUsername() + " in a stake.", true, false);
					}
					if (name.contains("partyhat") || name.contains("ticket")
							|| name.contains("arcane")
							|| name.contains("torva") || name.contains("santa")
							|| name.contains("bandos")
							|| name.contains("pernix")
							|| name.contains("virtus")
							|| name.contains("spirit") || name.contains("mask")
							|| name.contains("santa") || name.contains("rusty")
							|| name.contains("steadfast")
							|| name.contains("fury")
							|| name.contains("ragefire")
							|| name.contains(" (i)")
							|| name.contains("glaiven")
							|| name.contains("spirit")
							|| name.contains("saradomin's")
							|| name.contains("swift")
							|| name.contains("goliath")
							|| name.contains("dragonbone")
							|| name.contains("spellcaster")
							|| name.contains("primal")
							|| name.contains("bandos")
							|| name.contains("armadyl")
							|| name.contains("godsword")
							|| name.contains("claws")
							|| name.contains("Third-age")) {
						World.sendWorldMessage("<col=ff0000>Stake Log: "
								+ victor.getUsername() + " Won " + name
								+ ", amount: " + item.getAmount() + " from "
								+ loser.getUsername() + " in a stake.", true, false);
					}
				}
				CopyOnWriteArrayList<Item> wonItems = new CopyOnWriteArrayList<Item>();
				for (Item item : player.getLastDuelRules().getStake()
						.getItems()) {
					if (item == null)
						continue;
					wonItems.add(item);
				}
				SerializableFilesManager.savePlayer(victor);
				player.getLastDuelRules().resetStake();
				player.setOnSpotDueling(false);
		        target.setOnSpotDueling(false);
			}
			if (target.getLastDuelRules().getStake() != null) {
				for (Item item : target.getLastDuelRules().getStake()
						.getItems()) {
					if (item == null)
						continue;
					if (!victor.getInventory().addItem(item)) {
						World.addGroundItem(item, new WorldTile(player),
								player, true, 180);
						player.getPackets().sendGameMessage(
								"Items were dropped on the ground.");
					}
				}
				target.getLastDuelRules().resetStake();
			}
			World.sendWorldMessage("[<col=ab451d>Dueling</col>] <col=ab451d>" + victor.getDisplayName() + " has defeated " + loser.getDisplayName() + " in a "+getduelstyle()+" duel</col>.", false, false);
        //isDueling = false;
        victor.getPackets().sendGameMessage("<col=ff0000>Congratulations, you easily defeated " + loser.getDisplayName() + ".");
        //startEndingTeleport(victor);
        loser.getPackets().sendGameMessage("<col=ff0000>Oh dear, you have lost to " + victor.getDisplayName() + ".");
        //startEndingTeleport(loser);
	}
    if (loser.getControllerManager().getController() != null && removeLoserControler)
        loser.getControllerManager().removeControllerWithoutCheck();
    loser.setCanPvp(false);
    loser.getHintIconsManager().removeUnsavedHintIcon();
    loser.reset();
    loser.closeInterfaces();
    if (victor.getControllerManager().getController() != null)
        victor.getControllerManager().removeControllerWithoutCheck();
    victor.setCanPvp(false);
    victor.getHintIconsManager().removeUnsavedHintIcon();
    victor.reset();
    //victor.closeInterfaces();
    WorldTasksManager.schedule(new WorldTask() {


        @Override
        public void run() {
            victor.getControllerManager().startController("DuelControler");
            loser.getControllerManager().startController("DuelControler");
        }
    }, 2);
}

	       /* victor.getPackets().sendGameMessage("Congratulations, you easily defeated " + loser.getDisplayName() + ".");
            loser.getPackets().sendGameMessage("Oh dear, you have lost to " + victor.getDisplayName() + ".");
	        //victor.closeInterfaces();
			loser.getPackets().sendGameMessage(
					"You lost the duel to " + victor.getDisplayName() + ".");
			victor.getPackets().sendGameMessage(
					"You easily defeated " + loser.getDisplayName() + ".");
		}}*/
	

	private String getAcceptMessage(boolean firstStage) {
		if (target.getTemporaryAttributtes().get("acceptedDuel") == Boolean.TRUE)
			return "Other player has accepted.";
		else if (player.getTemporaryAttributtes().get("acceptedDuel") == Boolean.TRUE)
			return "Waiting for other player...";
		return firstStage ? "" : "Please look over the agreements to the duel.";
	}

	private WorldTile[] getPossibleWorldTiles() {
		final int arenaChoice = Utils.getRandom(2);
		WorldTile[] locations = new WorldTile[2];
		int[] arenaBoundariesX = { 3337, 3367, 3336 };
		int[] arenaBoundariesY = { 3246, 3227, 3208 };
		int[] maxOffsetX = { 14, 14, 16 };
		int[] maxOffsetY = { 10, 10, 10 };
		int finalX = arenaBoundariesX[arenaChoice]
				+ Utils.getRandom(maxOffsetX[arenaChoice]);
		int finalY = arenaBoundariesY[arenaChoice]
				+ Utils.getRandom(maxOffsetY[arenaChoice]);
		locations[0] = (new WorldTile(finalX, finalY, 0));
		if (player.getLastDuelRules().getRule(25)) {
			int direction = Utils.getRandom(1);
			if (direction == 0) {
				finalX--;
			} else {
				finalY++;
			}
		} else {
			finalX = arenaBoundariesX[arenaChoice]
					+ Utils.getRandom(maxOffsetX[arenaChoice]);
			finalY = arenaBoundariesY[arenaChoice]
					+ Utils.getRandom(maxOffsetY[arenaChoice]);
		}
		locations[1] = (new WorldTile(finalX, finalY, 0));
		return locations;
	}

	private WorldTile[] getPossibleWorldTilesSummoning() {
		final int arenaChoice = Utils.getRandom(2);
		WorldTile[] locations = new WorldTile[2];
		int[] arenaBoundariesX = { 3209, 3208, 3226 };
		int[] arenaBoundariesY = { 5168, 5176, 5176 };
		int[] maxOffsetX = { 1, 1, 1 };
		int[] maxOffsetY = { 1, 1, 1 };
		int finalX = arenaBoundariesX[arenaChoice]
				+ Utils.getRandom(maxOffsetX[arenaChoice]);
		int finalY = arenaBoundariesY[arenaChoice]
				+ Utils.getRandom(maxOffsetY[arenaChoice]);
		locations[0] = (new WorldTile(finalX, finalY, 0));
		if (player.getLastDuelRules().getRule(25)) {
			int direction = Utils.getRandom(1);
			if (direction == 0) {
				finalX--;
			} else {
				finalY++;
			}
		} else {
			finalX = arenaBoundariesX[arenaChoice]
					+ Utils.getRandom(maxOffsetX[arenaChoice]);
			finalY = arenaBoundariesY[arenaChoice]
					+ Utils.getRandom(maxOffsetY[arenaChoice]);
		}
		locations[1] = (new WorldTile(finalX, finalY, 0));
		return locations;
	}

	public Entity getTarget() {
		if (hasTarget())
			return target;
		return null;
	}

	public boolean hasTarget() {
		return target != null;
	}

	public boolean isDueling() {
		return isDueling;
	}

	public boolean isWearingTwoHandedWeapon() {
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == 4153 || weaponId == 11235 || weaponId == 861
				|| weaponId == 18353 || weaponId == 20171 || weaponId == 14484)
			return true;
		return false;
	}

	@Override
	public boolean keepCombating(Entity victim) {
		DuelRules rules = player.getLastDuelRules();
		boolean isRanging = PlayerCombat.isRanging(player) != 0;
		if (player.getTemporaryAttributtes().get("canFight") == Boolean.FALSE) {
			player.getPackets().sendGameMessage("The duel hasn't started yet.",
					true);
			return false;
		}
		if (target != victim)
			return false;
		if (player.getCombatDefinitions().getSpellId() > 0 && rules.getRule(2)
				&& isDueling) {
			player.getPackets().sendGameMessage(
					"You cannot use Magic in this duel!", true);
			return false;
		} else if (isRanging && rules.getRule(0) && isDueling) {
			player.getPackets().sendGameMessage(
					"You cannot use Range in this duel!", true);
			return false;
		} else if (!isRanging && rules.getRule(1)
				&& player.getCombatDefinitions().getSpellId() <= 0 && isDueling) {
			player.getPackets().sendGameMessage(
					"You cannot use Melee in this duel!", true);
			return false;
		}
		if (player.isDead() || victim.isDead())
			return false;
		return true;
	}

	@Override
	public boolean login() {
		startEndingTeleport(player);
		removeController();
		return true;
	}

	@Override
	public boolean logout() {
		if (isDueling)
			endDuel(target, player, false);
		else
			closeDuelInteraction(true, DuelStage.DECLINED);
		return isDueling ? false : true;
	}

	@Override
	public void magicTeleported(int type) {
		if (type != -1)
			return;
	}

	public boolean nextStage() {
		if (!hasTarget())
			return false;
		if (player.getInventory().getItems().getUsedSlots()
				+ target.getLastDuelRules().getStake().getUsedSlots() > 28) {
			player.setCloseInterfacesEvent(null);
			player.closeInterfaces();
			closeDuelInteraction(true, DuelStage.NO_SPACE);
			player.sm("You do not have enough space in your inventory for the stake!");
			return false;
		}
		player.getTemporaryAttributtes().put("acceptedDuel", false);
		openConfirmationScreen(false);
		player.getInterfaceManager().closeInventoryInterface();
		return true;
	}

	private void openConfirmationScreen(boolean ifFriendly) {
		player.getInterfaceManager().sendInterface(ifFriendly ? 639 : 626);
		refreshScreenMessage(false, ifFriendly);
	}

	 private void openDuelScreen(Player target, boolean ifFriendly) {
	        if (!ifFriendly) {
	            sendOptions(player);
	            player.getLastDuelRules().getStake().clear();
	        }
	        if (player.isOnSpotDuelingRequest())
	            target.setOnSpotDuelingRequest(true);
		player.getTemporaryAttributtes().put("acceptedDuel", false);
		player.getPackets().sendItems(134, false,
				player.getLastDuelRules().getStake());
		player.getPackets().sendItems(134, true,
				player.getLastDuelRules().getStake());
		player.getPackets().sendIComponentText(ifFriendly ? 637 : 631,
				ifFriendly ? 16 : 38,
				" " + Utils.formatPlayerNameForDisplay(target.getUsername()));
		player.getPackets().sendIComponentText(ifFriendly ? 637 : 631,
				ifFriendly ? 18 : 40,
				"" + (target.getSkills().getCombatLevel()));
		player.getPackets().sendConfig(286, 0);
		player.getTemporaryAttributtes().put("firstScreen", true);
		player.getInterfaceManager().sendInterface(ifFriendly ? 637 : 631);
		refreshScreenMessage(true, ifFriendly);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				closeDuelInteraction(true, DuelStage.DECLINED);
			}
		});
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		synchronized (this) {
			synchronized (target.getControllerManager().getController()) {
				DuelRules rules = player.getLastDuelRules();
				switch (interfaceId) {
				case 271:
					if (rules.getRule(5) && isDueling) {
						player.getPackets().sendGameMessage(
								"You can't use prayers in this duel.");
						return false;
					}
					return true;
				case 193:
				case 430:
				case 192:
					if (rules.getRule(2) && isDueling)
						return false;
					return true;
				case 884:
					if (componentId == 4) {
						if (rules.getRule(9) && isDueling) {
							player.getPackets()
									.sendGameMessage(
											"You can't use special attacks in this duel.");
							return false;
						}
					}
					return true;
				case 631:
					switch (componentId) {
					case 56: // no range
						rules.setRules(0);
						return false;
					case 57: // no melee
						rules.setRules(1);
						return false;
					case 58: // no magic
						rules.setRules(2);
						return false;
					case 59: // fun wep
						rules.setRules(8);
						return false;
					case 60: // no forfiet
						rules.setRules(7);
						return false;
					case 61: // no drinks
						rules.setRules(3);
						return false;
					case 62: // no food
						rules.setRules(4);
						return false;
					case 63: // no prayer
						rules.setRules(5);
						return false;
					case 64: // no movement
						rules.setRules(25);
						if (rules.getRule(6)) {
							rules.setRule(6, false);
							player.getPackets()
									.sendGameMessage(
											"You can't have movement without obstacles.");
						}
						return false;
					case 65: // obstacles
						rules.setRules(6);
						if (rules.getRule(25)) {
							rules.setRule(25, false);
							player.getPackets()
									.sendGameMessage(
											"You can't have obstacles without movement.");
						}
						return false;
					case 66: // enable summoning
						rules.setRules(24);
						return false;
					case 67:// no spec
						rules.setRules(9);
						return false;
					case 21:// no helm
						rules.setRules(10);
						return false;
					case 22:// no cape
						rules.setRules(11);
						return false;
					case 23:// no ammy
						rules.setRules(12);
						return false;
					case 31:// arrows
						rules.setRules(23);
						return false;
					case 24:// weapon
						rules.setRules(13);
						return false;
					case 25:// body
						rules.setRules(14);
						return false;
					case 26:// shield
						rules.setRules(15);
						return false;
					case 27:// legs
						rules.setRules(17);
						return false;
					case 28:// ring
						rules.setRules(19);
						return false;
					case 29: // bots
						rules.setRules(20);
						return false;
					case 30: // gloves
						rules.setRules(22);
						return false;
					case 107:
						closeDuelInteraction(true, DuelStage.DECLINED);
						return false;
					case 46:
						accept(true);
						return false;
					case 47:
						switch (packetId) {
						case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
							removeItem(slotId, 1);
							return false;
						case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
							removeItem(slotId, 5);
							return false;
						case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
							removeItem(slotId, 10);
							return false;
						case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
							Item item = player.getInventory().getItems()
									.get(slotId);
							if (item == null)
								return false;
							removeItem(slotId, player.getInventory().getItems()
									.getNumberOf(item));
							return false;
						case WorldPacketsDecoder.ACTION_BUTTON5_PACKET:
							player.getInventory().sendExamine(slotId);
							return false;
						}
						return false;
					}
				case 628:
					switch (packetId) {
					case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
						addItem(slotId, 1);
						return false;
					case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
						addItem(slotId, 5);
						return false;
					case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
						addItem(slotId, 10);
						return false;
					case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
						Item item = player.getInventory().getItems()
								.get(slotId);
						if (item == null)
							return false;
						addItem(slotId, player.getInventory().getItems()
								.getNumberOf(item));
						return false;
					case WorldPacketsDecoder.ACTION_BUTTON5_PACKET:
						player.getInventory().sendExamine(slotId);
						return false;
					}
				case 626:
					switch (componentId) {
					case 43:
						accept(false);
						return false;
					}
				case 637: // friendly
					switch (componentId) {
					case 25: // no range
						rules.setRules(0);
						return false;
					case 26: // no melee
						rules.setRules(1);
						return false;
					case 27: // no magic
						rules.setRules(2);
						return false;
					case 28: // fun wep
						rules.setRules(8);
						return false;
					case 29: // no forfiet
						rules.setRules(7);
						return false;
					case 30: // no drinks
						rules.setRules(3);
						return false;
					case 31: // no food
						rules.setRules(4);
						return false;
					case 32: // no prayer
						rules.setRules(5);
						return false;
					case 33: // no movement
						rules.setRules(25);
						if (rules.getRule(6)) {
							rules.setRule(6, false);
							player.getPackets()
									.sendGameMessage(
											"You can't have movement without obstacles.");
						}
						return false;
					case 34: // obstacles
						rules.setRules(6);
						if (rules.getRule(25)) {
							rules.setRule(25, false);
							player.getPackets()
									.sendGameMessage(
											"You can't have obstacles without movement.");
						}
						return false;
					case 35: // enable summoning
						rules.setRules(24);
						return false;
					case 36:// no spec
						rules.setRules(9);
						return false;
					case 43:// no helm
						rules.setRules(10);
						return false;
					case 44:// no cape
						rules.setRules(11);
						return false;
					case 45:// no ammy
						rules.setRules(12);
						return false;
					case 53:// arrows
						rules.setRules(23);
						return false;
					case 46:// weapon
						rules.setRules(13);
						return false;
					case 47:// body
						rules.setRules(14);
						return false;
					case 48:// shield
						rules.setRules(15);
						return false;
					case 49:// legs
						rules.setRules(17);
						return false;
					case 50:// ring
						rules.setRules(19);
						return false;
					case 51: // bots
						rules.setRules(20);
						return false;
					case 52: // gloves
						rules.setRules(22);
						return false;
					case 86:
						closeDuelInteraction(true, DuelStage.DECLINED);
						return false;
					case 21:
						accept(true);
						return false;
					}
				case 639:
					switch (componentId) {
					case 25:
						accept(false);
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		/*
		 * if(getWinner() == null) {
		 * player.getDialogueManager().startDialogue("ForfeitDialouge"); }
		 */
		return false;
	}

	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(134,
				player.getLastDuelRules().getStake(), slots);
		target.getPackets().sendUpdateItems(134, true,
				player.getLastDuelRules().getStake().getItems(), slots);
	}

	private void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = player.getLastDuelRules().getStake().getItems()[index];
			if (item != null)
				if (itemsBefore[index] != item) {
					changedSlots[count++] = index;
				}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	private void refreshScreenMessage(boolean firstStage, boolean ifFriendly) {
		player.getPackets().sendIComponentText(
				firstStage ? (ifFriendly ? 637 : 631)
						: (ifFriendly ? 639 : 626),
				firstStage ? (ifFriendly ? 20 : 41) : (ifFriendly ? 23 : 35),
				"<col=ff0000>" + getAcceptMessage(firstStage));
	}

	private void refreshScreenMessages(boolean firstStage, boolean ifFriendly) {
		refreshScreenMessage(firstStage, ifFriendly);
		if (!ifFriendly) {
			player.getPackets().sendIComponentText(626, 25, "");
			player.getPackets().sendIComponentText(626, 26, "");
		}
		if (target != null) {
			((DuelArena) target.getControllerManager().getController())
					.refreshScreenMessage(firstStage, ifFriendly);
		}
	}

	private void removeEquipment() {
		int slot = 0;
		for (int i = 10; i < 23; i++) {
			if (i == 14) {
				if (player.getEquipment().hasTwoHandedWeapon()
						|| isWearingTwoHandedWeapon() == true)
					ButtonHandler.sendRemove(target, 3);
			}
			if (player.getLastDuelRules().getRule(i)) {
				slot = i - 10;
				ButtonHandler.sendRemove(player, slot);
			}
		}
	}
	 public String getduelstyle() {
			if(!ifFriendly) {
				return "Stake";
			}else
				return "Friendly";
		}
	public void removeItem(final int slot, int amount) {
		if (!hasTarget())
			return;
		Item item = player.getLastDuelRules().getStake().get(slot);
		if (item == null)
			return;
		Item[] itemsBefore = player.getLastDuelRules().getStake()
				.getItemsCopy();
		int maxAmount = player.getLastDuelRules().getStake().getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		player.getLastDuelRules().getStake().remove(slot, item);
		player.getInventory().addItem(item);
		refreshItems(itemsBefore);
		cancelAccepted();
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				target.closeInterfaces();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					endDuel(target, player);
					this.stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	private void sendFinishInterface(Player player, Player loser) {
		player.getInterfaceManager().sendInterface(634);
		player.getPackets().sendIComponentText(634, 33,
				Utils.formatPlayerNameForDisplay(loser.getUsername()));
		player.getPackets().sendIComponentText(634, 32,
				Integer.toString(target.getSkills().getCombatLevel()));
		player.getPackets().sendIComponentText(634, 17, "Close");
		player.getPackets().sendInterSetItemsOptionsScript(634, 28, 136, 6, 4,
				"Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(634, 28, 0, 35,
				new int[] { 0, 1, 2, 3, 4, 5 });
		if (loser.getLastDuelRules().getStake() != null) {
			player.getPackets().sendItems(136,
					loser.getLastDuelRules().getStake());
		}
	}

	private void sendOptions(Player player) {
		player.getInterfaceManager().sendInventoryInterface(628);
		player.getPackets().sendUnlockIComponentOptionSlots(628, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(628, 0, 93, 4, 7,
				"Stake 1", "Stake 5", "Stake 10", "Stake All", "Stake X");
		player.getPackets().sendUnlockIComponentOptionSlots(631, 47, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(631, 0, 120, 4, 7,
				"Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X");
	}

	@Override
	public void start() {
		this.target = (Player) getArguments()[0];
		ifFriendly = (boolean) getArguments()[1];
		openDuelScreen(target, ifFriendly);
	}

	private void startEndingTeleport(Player player) {
        if (player.getDuelLocation() != null) {
            player.setNextWorldTile(player.getDuelLocation());
            player.setDuelLocation(null);
            return;
        }
        WorldTile tile = LOBBY_TELEPORTS[Utils.random(LOBBY_TELEPORTS.length)];
        WorldTile teleTile = tile;
        for (int trycount = 0; trycount < 10; trycount++) {
            teleTile = new WorldTile(tile, 2);
            if (World.canMoveNPC(tile.getPlane(), teleTile.getX(),
                    teleTile.getY(), player.getSize()))
                break;
            teleTile = tile;
        }
        player.setNextWorldTile(teleTile);
    }


	public boolean twoHanded(int itemId) {
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		String name = defs == null ? "" : defs.getName().toLowerCase();
		if (name.contains(" bow") || name.contains("godsword")
				|| name.contains("anchor") || name.contains("hand cannon")
				|| name.contains(" ket-om") || name.contains("2h")
				|| name.contains("claws") || name.contains("greataxe")
				|| name.contains("spear") || name.contains("katana")
				|| name.contains("zaryte") || name.contains("halberd")
				|| name.contains("maul") || name.contains("shortbow")
				|| name.contains("longbow") || name.contains("saradomin sword")
				|| name.contains("claws"))
			return true;
		return false;
	}
}