package com.rs.game.player.controllers;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceMovement;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.agility.Agility;
import com.rs.game.player.content.activities.events.GlobalEvents;
import com.rs.game.player.content.activities.events.GlobalEvents.Event;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Color;
import com.rs.utils.Utils;

/*
 * Handles everything Godwars dungeon related.
 */
public class GodWars extends Controller {
	
	private static final int EMPTY_SECTOR = -1;
	private static final int BANDOS = 0;
	private static final int ARMADYL = 1;
	private static final int SARADOMIN = 2;
	private static final int ZAMORAK = 3;
	private static final int ZAROS = 4;
	private static final int BANDOS_SECTOR = 4, ARMADYL_SECTOR = 5, SARADOMIN_SECTOR = 6, ZAMORAK_SECTOR = 7, ZAROS_PRE_CHAMBER = 8, ZAROS_SECTOR = 9;
	
	private static final WorldTile[] CHAMBER_TELEPORTS = {
			new WorldTile(2863, 5357, 0), new WorldTile(2857, 5357, 0), // bandos
			new WorldTile(2835, 5295, 0), new WorldTile(2835, 5294, 0), // armadyl
			new WorldTile(2923, 5256, 0), new WorldTile(2923, 5257, 0), // saradomin
			new WorldTile(2925, 5332, 0), new WorldTile(2925, 5333, 0), // zamorak
	};
	private int sector;

	@Override
	public void start() {
		setArguments(new Object[] { 0, 0, 0, 0, 0, 0 });
		sendInterfaces();
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean login() {
		sendInterfaces();
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 26425) {
		if (player.bandos >= 40 || player.getInventory().contains(ItemIdentifiers.BANDOSIAN_SOULSTONE) || player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT) || GlobalEvents.isActiveEvent(Event.BANDOS_KC)) {	
			player.getDialogueManager().startDialogue("Bandos");
			return false;
		} else
			player.getPackets().sendGameMessage("You need to have slain 40 Bandos Followers, you have slain "+player.bandos+".");
			return false;
		}
		if (object.getId() == 26426) {
			if (player.armadyl >= 40 || player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT) || GlobalEvents.isActiveEvent(Event.ARMADYL_KC)) {	
			player.getDialogueManager().startDialogue("Armadyl");
		    return false;
		} else
			 player.getPackets().sendGameMessage("You need to have slain 40 Armadyl Followers, you have slain "+player.armadyl+".");
			return false;
		}
		if (object.getId() == 26427) {
			if (player.saradomin >= 40 || player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT) || GlobalEvents.isActiveEvent(Event.SARADOMIN_KC)) {	
			player.getDialogueManager().startDialogue("Saradomin");
			return false;
		} else
			 player.getPackets().sendGameMessage("You need to have slain 40 Saradomin Followers, you have slain "+player.saradomin+".");
			 return false;
		}
		if (object.getId() == 26428) {
			if (player.zamorak >= 40 || player.getPerkManager().hasPerk(PlayerPerks.GODWARS_KILLCOUNT) || GlobalEvents.isActiveEvent(Event.ZAMORAK_KC)) {
			player.getDialogueManager().startDialogue("Zamorak");
			return false;
		} else
			 player.getPackets().sendGameMessage("You need to have slain 40 Zamorak Followers, you have slain "+player.zamorak+".");
			 return false;
		}
		if (object.getId() == 75462) {
			if (object.getX() == 2912 && (object.getY() == 5298 || object.getY() == 5299)) {
				useAgilityStones(player, object, new WorldTile(2915, object.getY(), 0), 15239);
			} else if (object.getX() == 2914 && (object.getY() == 5298 || object.getY() == 5299)) {
				useAgilityStones(player, object, new WorldTile(2911, object.getY(), 0), 3378);
			} else if ((object.getX() == 2919 || object.getX() == 2920) && object.getY() == 5278)
				useAgilityStones(player, object, new WorldTile(object.getX(), 5275, 0), 15239);
			 else if ((object.getX() == 2920 || object.getX() == 2919) && object.getY() == 5276)
				useAgilityStones(player, object, new WorldTile(object.getX(), 5279, 0), 3378);
			return false;
		}
		if (object.getId() == 75089) {
			if (player.getInventory().contains(20120) && player.getY() == 5280) {
				player.getPackets().sendGameMessage("You flash the key in front of the door");
				player.useStairs(1133, new WorldTile(2887, 5278, 0), 1, 2, "...and a strange force flings you in.");
			} else if (player.getInventory().contains(ItemIdentifiers.FROZEN_KEY) && player.getY() == 5277) {
				player.getPackets().sendGameMessage("You flash the key in front of the door");
				player.useStairs(1133, new WorldTile(2887, 5280, 0), 1, 2, "...and a strange force flings you out.");	
		} else
			player.getDialogueManager().startDialogue("SimpleMessage", "You try to push the door open, but it wont budge.... It looks like there is some kind of key hole.");
			return false;
		}
    	if (object.getId() == 75463) {
			player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
				@Override
				public void run() {
					if (player.getY() >= 5280) {
						if (player.getSkills().getLevel(Skills.RANGE) >= 70) {
							player.setNextWorldTile(new WorldTile(2872, 5272, 0));
							player.sm("You swing yourself over...");
						} else {
							player.sm("You must have 70 Ranged to enter the Armadyl GodWars.");
						}
					} else if (player.getX() == 2872 && player.getY() <= 5272) {
						player.setNextWorldTile(new WorldTile(2872, 5280, 0));
						player.sm("You swing yourself over...");
					}
				}
			}, true));
			return false;
		}
    	if (object.getId() == 26439) {
			if (!player.getSkills().hasRequiriments(Skills.HITPOINTS, 70))
				return false;
			final boolean withinZamorak = sector == ZAMORAK_SECTOR;
			final WorldTile tile = new WorldTile(2887, withinZamorak ? 5336
					: 5346, 0);
			player.lock();
			player.setNextWorldTile(object);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.setNextAnimation(new Animation(17454));
					player.setNextFaceWorldTile(tile);
					if (!withinZamorak) {
						sector = ZAMORAK_SECTOR;
					} else
						sector = EMPTY_SECTOR;
					sendInterfaces();
				}
			}, 1);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.unlock();
					player.resetReceivedDamage();
					player.setNextAnimation(new Animation(-1));
					player.setNextWorldTile(tile);
				}
			}, 5);
			return false;
    	}
    	if (object.getId() == 57256) {
			player.useStairs(-1, new WorldTile(2855, 5222, 0), 1, 2, "You climb down the stairs.");
			player.getPackets().sendHideIComponent(601, 17, false);
			player.getPackets().sendHideIComponent(601, 22, false);
			return false;
    	}
		return true;
		}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendOverlay(getInterface(), true);
		player.getPackets().sendIComponentText(601, 3, "Armadyl:");
		player.getPackets().sendIComponentText(601, 4, "Bandos:");
		player.getPackets().sendIComponentText(601, 5, "Saradomin:");
		player.getPackets().sendIComponentText(601, 6, "Zamorak:");
		player.getPackets().sendIComponentText(601, 8, "" + player.armadyl);
		player.getPackets().sendIComponentText(601, 9, "" + player.bandos);
		player.getPackets().sendIComponentText(601, 10, "" + player.saradomin);
		player.getPackets().sendIComponentText(601, 11, "" + player.zamorak);
	}

	private int getInterface() {
		return 601;
	}

	@Override
	public boolean sendDeath() {
		removeController();
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeController();
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		removeController();
		return true;
	}

	@Override
	public void forceClose() {
		reset();
	}

	@Override
	public boolean canHit(Entity entity) {
		return !(entity instanceof Familiar);
	}

	private void remove() {
		player.bandos -= 40;
		player.armadyl -= 40;
		player.saradomin -= 40;
		player.zamorak -= 40;
		player.getPackets().sendGameMessage(Color.MAROON, "The gods have taken some of your killcount.");
		player.getInterfaceManager().closeOverlay(false);
	}
	
	private void reset() {
		player.bandos = 0;
		player.armadyl = 0;
		player.saradomin = 0;
		player.zamorak = 0;
		player.getInterfaceManager().closeOverlay(false);
	}
	
	private static void useAgilityStones(final Player player,
			final WorldObject object, final WorldTile tile, final int emote) {
		if (!Agility.hasLevel(player, 70))
			return;
		player.faceObject(object);
		player.addWalkSteps(object.getX(), object.getY());
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.resetReceivedDamage();// Kinda unfair if not :)
				player.useStairs(emote, tile, 7, 7 + 1);
			}
		}, 1);
	}

}