package com.rs.game.npc.others;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class GraveStone extends NPC {// 652 - gravestone selection interface
	
	public static GraveStone getGraveStoneByUsername(String username) {
		for (final GraveStone stone : gravestones)
			if (stone.username.equals(username))
				return stone;
		return null;
	}
	
	/*
	 * the final items after swaping slots return keptItems, dropedItems as we
	 * reset inv and equipment all others will just disapear
	 */
	public static Item[][] getItemsKeptOnDeath(Player player, Integer[][] slots) {
		final ArrayList<Item> droppedItems = new ArrayList<Item>();
		final ArrayList<Item> keptItems = new ArrayList<Item>();
		for (final int i : slots[0]) { // items kept on death
			Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);
			if (item == null) // shouldn't
				continue;
			item = item.clone();
			if (item.getAmount() > 1) {
				droppedItems.add(new Item(item.getId(), item.getAmount() - 1));
				item.setAmount(1);
			}
			keptItems.add(item);
		}
		for (final int i : slots[1]) { // items dropped on death
			final Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);
			if (item == null) // shouldn't
				continue;
			droppedItems.add(item);
		}
		for (final int i : slots[2]) { // items protected by default
			final Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);
			if (item == null) // shouldnt
				continue;
			keptItems.add(item);
		}
		return new Item[][] { keptItems.toArray(new Item[keptItems.size()]),
				droppedItems.toArray(new Item[droppedItems.size()]) };
	}
	
	/*
	 * return arrays: items kept on death by default, items dropped on death by
	 * default, items protected by default, items lost by default
	 */
	public static Integer[][] getItemSlotsKeptOnDeath(final Player player,
			boolean atWilderness, boolean skulled, boolean protectPrayer) {
		final ArrayList<Integer> droppedItems = new ArrayList<Integer>();

		// so basically this was null if in wilderness, jut made it like the
		// others, which is ok
		final ArrayList<Integer> protectedItems = new ArrayList<Integer>();
		final ArrayList<Integer> lostItems = new ArrayList<Integer>();
		for (int i = 1; i < 44; i++) {
			final Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);

			if (item == null)
				continue;

			final int stageOnDeath = item.getDefinitions().getStageOnDeath();
			if (!atWilderness && stageOnDeath == 1
					|| !ItemConstants.isTradeable(item))
				protectedItems.add(i);
			else
				droppedItems.add(i);
		}
		int keptAmount = skulled ? 0 : 3;
		if (protectPrayer)
			keptAmount++;
		if (droppedItems.size() < keptAmount)
			keptAmount = droppedItems.size();
		Collections.sort(droppedItems, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				final Item i1 = o1 >= 16 ? player.getInventory().getItem(
						o1 - 16) : player.getEquipment().getItem(o1 - 1);
				final Item i2 = o2 >= 16 ? player.getInventory().getItem(
						o2 - 16) : player.getEquipment().getItem(o2 - 1);
				final int price1 = i1.getDefinitions().getPrice();
				final int price2 = i2.getDefinitions().getPrice();
				if (price1 > price2)
					return -1;
				if (price1 < price2)
					return 1;
				return 0;
			}
		});
		final Integer[] keptItems = new Integer[keptAmount];
		for (int i = 0; i < keptAmount; i++)
			keptItems[i] = droppedItems.remove(0);
		return new Integer[][] {
				keptItems,
				droppedItems.toArray(new Integer[droppedItems.size()]),
				protectedItems.isEmpty() ? new Integer[0] : protectedItems
						.toArray(new Integer[protectedItems.size()]),
						atWilderness ? new Integer[0] : lostItems
						.toArray(new Integer[lostItems.size()]) };
	}
	
	public static int getMaximumTicks(int graveStone) {
		switch (graveStone) {
			case 0:
				return 500;
			case 1:
			case 2:
				return 600;
			case 3:
				return 800;
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
				return 1000;
			case 12:
				return 1200;
			case 13:
				return 1500;
		}
		return 500;
	}

	public static int getNPCId(int currentGrave) {
		if (currentGrave == 13)
			return 13296;
		return 6565 + (currentGrave * 3);
	}

	private static final int GRAVE_STONE_INTERFACE = 266;
	private static final List<GraveStone> gravestones = new ArrayList<GraveStone>();
	private final String username;
	private int ticks;
	private final String inscription;

	private final List<FloorItem> FloorItems;

	private final int graveStone;

	private boolean blessed;

	private final int hintIcon;

	public GraveStone(Player player, WorldTile deathTile, Item[] items) {
		super(getNPCId(player.getGraveStone()), deathTile, -1, true);
		graveStone = player.getGraveStone();
		setDirection(Utils.getFaceDirection(0, -1));
		setNextAnimation(new Animation(graveStone == 1 ? 7396 : 7394));
		username = player.getUsername();
		ticks = (player.isDonator() ? 2 : 1) * getMaximumTicks(graveStone);
		inscription = getInscription(player.getDisplayName());
		FloorItems = new ArrayList<FloorItem>();
		for (final Item item : items) {
			final FloorItem i = World.addGroundItem(item, deathTile, player, true, -1, 1);
			if (i != null)
				FloorItems.add(i);
		}
		synchronized (gravestones) {
			final GraveStone oldStone = getGraveStoneByUsername(username);
			if (oldStone != null) {
				addLeftTime(false);
				oldStone.finish();
			}
			gravestones.add(this);
		}
		player.getPackets().sendRunScript(2434, ticks);
		hintIcon = player.getHintIconsManager().addHintIcon(this, 0, -1, true);
		player.getPackets().sendGlobalConfig(623, deathTile.getTileHash());
		player.getPackets().sendGlobalConfig(624, 0);
		player.getPackets().sendGlobalString(53, "Your gravestone marker");
		player.sm("Your items have been dropped at your gravestone, where they'll remain until it crumbles. Look at the world map to help find your gravestone.");
		player.sm("It looks like it'll survive another " + (ticks / 100)
				+ " minutes.");
	}

	public void addLeftTime(boolean clean) {
		if (clean) {
			for (final FloorItem item : FloorItems)
				World.turnPublic(item, 60);
		} else {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						for (final FloorItem item : FloorItems)
							World.turnPublic(item, 60);
					} catch (final Throwable e) {
						Logger.handle(e);
					}
				}
			}, (long) (ticks * 0.6), TimeUnit.SECONDS);
		}
	}

	public void decrementGrave(int stage, String message) {
		final Player player = getPlayer();
		if (player != null) {
			player.sm("<col=7E2217>" + message);
			player.getPackets().sendRunScript(2434, ticks);
		}
		if (stage == -1) {
			addLeftTime(true);
			finish();
		} else
			setNextNPCTransformation(getNPCId(graveStone) + stage);
	}

	public void demolish(Player demolisher) {
		if (!demolisher.getUsername().equals(username)) {
			demolisher
			.sm("It would be impolite to demolish someone else's gravestone.");

			return;
		}
		addLeftTime(true);
		finish();
		demolisher.sm("It looks like it'll survive another " + (ticks / 100)
				+ " minutes. You demolish it anyway.");
	}

	@Override
	public void finish() {
		synchronized (gravestones) {
			gravestones.remove(this);
		}
		final Player player = getPlayer();
		if (player != null) {
			player.getPackets().sendRunScript(2434, 0);
			player.getHintIconsManager().removeHintIcon(hintIcon);
			player.getPackets().sendGlobalConfig(623, -1);

		}
		super.finish();
	}

	private String getInscription(String username) {
		username = Utils.formatPlayerNameForDisplay(username);
		switch (graveStone) {
		case 0:
		case 1:
			return "In memory of <i>" + username + "</i>,<br>who died here.";
		case 2:
		case 3:
			return "In loving memory of our dear friend <i>" + username
					+ "</i>,who <br>died in this place @X@ minutes ago.";
		case 4:
		case 5:
			return "In your travels, pause awhile to remember <i>" + username
					+ "</i>,<br>who passed away at this spot.";
		case 6:
			return "<i>"
			+ username
			+ "</i>, <br>an enlightened servant of Saradomin,<br>perished in this place.";
		case 7:
			return "<i>"
			+ username
			+ "</i>, <br>a most bloodthirsty follower of Zamorak,<br>perished in this place.";
		case 8:
			return "<i>"
			+ username
			+ "</i>, <br>who walked with the Balance of Guthix,<br>perished in this place.";
		case 9:
			return "<i>"
			+ username
			+ "</i>, <br>a vicious warrior dedicated to Bandos,<br>perished in this place.";
		case 10:
			return "<i>"
			+ username
			+ "</i>, <br>a follower of the Law of Armadyl,<br>perished in this place.";
		case 11:
			return "<i>"
			+ username
			+ "</i>, <br>servant of the Unknown Power,<br>perished in this place.";
		case 12:
			return "Ye frail mortals who gaze upon this sight, forget not<br>the fate of <i>"
			+ username
			+ "</i>, once mighty, now surrendered to the inescapable grasp of destiny.<br><i>Requiescat in pace.</i>";
		case 13:
			return "Here lies <i>"
			+ username
			+ "</i>, friend of dwarves. Great in<br>life, glorious in death. His/Her name lives on in<br>song and story.";
		}
		return "Cabbage";
	}

	private Player getPlayer() {
		return World.getPlayer(username);
	}

	@Override
	public void processNPC() {
		ticks--;
		if (ticks == 0)
			decrementGrave(-1, "Your grave has collapsed!");
		else if (ticks == 50)
			decrementGrave(2, "Your grave is collapsing.");
		else if (ticks == 100)
			decrementGrave(1, "Your grave is about to collapse.");
	}

	/*
	 * if (item.getAmount() > 1) { item.setAmount(item.getAmount() - 1);
	 * count++; } else containedItems.remove(count);
	 */

	public void repair(Player blesser, boolean bless) {
		if (blesser.getSkills().getLevel(Skills.PRAYER) < (bless ? 70 : 2)) {
			blesser.sm("You need " + (bless ? 70 : 2) + " prayer to "
					+ (bless ? "bless" : "repair") + " a gravestone.");
			return;
		}
		if (blesser.getUsername().equals(username)) {
			blesser.sm("The gods don't seem to approve of people attempting to "
					+ (bless ? "bless" : "repair") + " their own gravestones.");
			return;
		}
		if (bless && blessed) {
			blesser.sm("This gravestone has already been blessed.");
			return;
		} else if (!bless && ticks > 100) {
			blesser.sm("This gravestone doesn't seem to need repair.");
			return;
		}
		ticks += bless ? 6000 : 500; // 5minutes, 1hour
		blessed = true;
		decrementGrave(0, blesser.getDisplayName() + "has "
				+ (bless ? "blessed" : "repaired")
				+ " your gravestone. It should survive another "
				+ (ticks / 100) + " minutes.");
		blesser.sm("You " + (bless ? "bless" : "repair") + " the grave.");
		blesser.lock(2);
		blesser.setNextAnimation(new Animation(645));
	}
	
	public void sendGraveInscription(final Player reader) {
		if (ticks <= 50) {// if the grave is almost broken
			reader.getInterfaceManager().sendInterface(GRAVE_STONE_INTERFACE);
			reader.getVarBitManager().sendVarBit(4191, graveStone == 0 ? 0 : 1);
			reader.getPackets().sendIComponentText(GRAVE_STONE_INTERFACE, 22,
					"The inscription is too unclear to read.");
		} else if (reader == getPlayer()) {
			reader.getDialogueManager().startDialogue(new Dialogue() {
				
				@Override
				public void start() {
					this.sendOptionsDialogue("What do you want to do?", "Put my items in my inventory", "Put my items in my bank", "Demolish my grave");
				}
				
				@Override
				public void run(int interfaceId, int componentId)  {
					end();
					switch(componentId) {
						case OPTION_1://inv
							{
								List<FloorItem> copy = new ArrayList<FloorItem>();
								copy.addAll(FloorItems);
								for(FloorItem item : copy) {
									World.removeGroundItem(reader, item);
									FloorItems.remove(item);
								}
							}
							break;
						case OPTION_2://bank
							if (!player.isDonator()) {
								player.getDialogueManager().startDialogue("SimpleMessage", "You need to be a donator to put these items in your bank.");
								return;
							}
							{
								List<FloorItem> copy = new ArrayList<FloorItem>();
								copy.addAll(FloorItems);
								for(FloorItem item : copy) {
									final int regionId = item.getTile().getRegionId();
									final Region region = World.getRegion(regionId);
									if (region.getGroundItemsSafe().contains(item)) {
										if (player.getBank().hasBankSpace()
												|| player.getBank().containsItem(item.getId(), 1)) {
											player.getBank().addItem(item, true);
											region.getGroundItemsSafe().remove(item);
											FloorItems.remove(item);
											player.getPackets().sendRemoveGroundItem(item);
										}
									}
								}
							}
							break;
						case OPTION_3:
							demolish(player);
							break;
					}
				}
				
				@Override
				public void finish() {
					
				} 
			});
		} else {
			reader.getInterfaceManager().sendInterface(GRAVE_STONE_INTERFACE);
			reader.getVarBitManager().sendVarBit(4191, graveStone == 0 ? 0 : 1);
			reader.getPackets().sendIComponentText(GRAVE_STONE_INTERFACE, 5,
					inscription);
		}
	}
	
	/**
	 * 
	 * @param FloorItem
	 * @return removed
	 */
	public boolean removeGraveItem(FloorItem FloorItem) {
		final int regionId = FloorItem.getTile().getRegionId();
		final Region region = World.getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(FloorItem))
			return false;
		region.getGroundItemsSafe().remove(FloorItem);
		for (final Player player : World.getPlayers()) {
			if (player == null
					|| !player.hasStarted()
					|| player.hasFinished()
					|| player.getPlane() != FloorItem.getTile()
							.getPlane()
					|| !player.getMapRegionsIds()
							.contains(regionId))
				continue;
			player.getPackets().sendRemoveGroundItem(FloorItem);
		}
		return true;
	}
}