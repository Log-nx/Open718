package com.rs.game.player.content.custom;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.NPC;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.Player;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class CharmingImp {

	/**
	 * Representing the item.
	 */
	static Item item;

	/**
	 * The low-level charms.
	 */
	private static final Item[] LOW_CHARMS = { new Item(12158, Utils.random(1, 2)), new Item(12159, 1) };

	/**
	 * The mid-level charms.
	 */
	private static final Item[] MID_CHARMS = { new Item(12158, Utils.random(1, 3)), new Item(12159, Utils.random(1, 2)), new Item(12160, 1) };

	/**
	 * The high-level charms.
	 */
	private static final Item[] HIGH_CHARMS = { new Item(12158, Utils.random(1, 4)), new Item(12159, Utils.random(1, 3)), new Item(12160, Utils.random(1, 2)), new Item(12163, 1) };

	/**
	 * Handles the item.
	 * 
	 * @param player
	 *            The player.
	 */
	public static boolean handleCharmDrops(Player player, NPC npc, WorldTile tile) {
		Pet playerPet = player.getPet();
		if (Utils.random(player.getPerkManager().hasPerk(PlayerPerks.CHARM_COLLECTOR) ? 4 : 6) == 0) {
			item = null;
			if (npc.getCombatLevel() > 100) {
				item = HIGH_CHARMS[Utils.random(HIGH_CHARMS.length)];
			} else if (npc.getCombatLevel() > 50) {
				item = MID_CHARMS[Utils.random(MID_CHARMS.length)];
			} else {
				item = LOW_CHARMS[Utils.random(LOW_CHARMS.length)];
			}
			if (player.getInventory().containsOneItem(ItemIdentifiers.CHARMING_IMP)) {
				if (player.getInventory().addItem(item)) {
					return true;
				} else if (player.getPerkManager().hasPerk(PlayerPerks.CHARM_COLLECTOR)) {
					player.getBank().addItem(item.getId(), item.getAmount(), true);
					player.getPackets().sendGameMessage(Colors.ORANGE + "<shad=000000>Charm Collector: x " + item.getAmount() + " " + "of " + item.getName() + " " + (item.getAmount() == 1 ? "has" : "have") + " " + "been sent to your bank.", true);
				} else {
					dropItems(player, npc, tile, item, true);
					return true;
				}
			} else if (player.getPerkManager().hasPerk(PlayerPerks.CHARM_COLLECTOR)) {
				player.getBank().addItem(item.getId(), item.getAmount(), true);
				player.getPackets().sendGameMessage(Colors.ORANGE + "<shad=000000>Charm Collector: x" + item.getAmount() + " " + "of " + item.getName() + " " + (item.getAmount() == 1 ? "has" : "have") + " " + "been sent to your bank.", true);
			} else if (playerPet != null && player.getPetManager().isALegendaryPet()) {
				player.getInventory().addItem(item.getId(), item.getAmount());
			} else {
				dropItems(player, npc, tile, item, false);
				return true;
			}
		}
		return false;
	}

	/**
	 * Drops an item.
	 * 
	 * @param player
	 *            The player.
	 * @param npc
	 *            the NPC.
	 * @param item
	 *            The item.
	 * @param underPlayer
	 *            If under the player.
	 */
	private static void dropItems(Player player, NPC npc, WorldTile tile, Item item, boolean underPlayer) {
		World.addGroundItem(item, new WorldTile(underPlayer ? player : tile), player, true, 60);
	}
}