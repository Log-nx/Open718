package com.rs.game.player.content.creation;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class SkillingOutfits {
	
	public static boolean combineOutfit(final Player player, Item item) {
	int itemId = item.getId();
	/**
	 * Golem Outfits
	 */
	if (itemId == 31575 || itemId == 31580 || itemId == 31585) {
		Item[] items = { new Item(31575), new Item(31580), new Item(31585) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need a sapphire golem head, "
							+ "an emerald golem head and a ruby golem head in order to combine them into "
							+ "a magic golem head.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(31590));
			return false;
	}
	if (itemId == 31576 || itemId == 31581 || itemId == 31586) {
		Item[] items = { new Item(31576), new Item(31581), new Item(31586) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need a sapphire golem torso, "
							+ "an emerald golem torso and a ruby golem torso in order to combine them into "
							+ "a magic golem torso.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(31591));
			return false;
	}
	if (itemId == 31577 || itemId == 31582 || itemId == 31587) {
		Item[] items = { new Item(31577), new Item(31582), new Item(31587) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need sapphire golem legs, "
							+ "emerald golem legs and ruby golem legs in order to combine them into "
							+ "magic golem legs.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(31592));
			return false;
	}
	if (itemId == 31578 || itemId == 31583 || itemId == 31588) {
		Item[] items = { new Item(31578), new Item(31583), new Item(31588) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need sapphire golem gloves, "
							+ "emerald golem gloves and ruby golem gloves in order to combine them into "
							+ "magic golem gloves.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(31593));
			return false;
	}
	if (itemId == 31579 || itemId == 31584 || itemId == 31589) {
		Item[] items = { new Item(31579), new Item(31584), new Item(31589) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need sapphire golem boots, "
							+ "emerald golem boots and ruby golem boots in order to combine them into "
							+ "magic golem boots.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(31594));
			return false;
	}
	/**
	 * Ethereal Outfits
	 */
	if (itemId == 32342 || itemId == 32347 || itemId == 32352) {
		Item[] items = { new Item(32342), new Item(32347), new Item(32352) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need a law ethereal hood, "
							+ "a blood ethereal hood and a death ethereal head in order to combine them into "
							+ "an infinity ethereal head.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(32357));
			return false;
	}
	if (itemId == 32343 || itemId == 32348 || itemId == 32353) {
		Item[] items = { new Item(32343), new Item(32348), new Item(32353) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need a law ethereal body, "
							+ "a blood ethereal body and a death ethereal body in order to combine them into "
							+ "an infinity ethereal body.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(32358));
			return false;
	}
	if (itemId == 32344 || itemId == 32349 || itemId == 32354) {
		Item[] items = { new Item(32344), new Item(32349), new Item(32354) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need law ethereal legs, "
							+ "blood ethereal legs and death ethereal legs in order to combine them into "
							+ "infinity ethereal legs.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(32359));
			return false;
	}
	if (itemId == 32345 || itemId == 32350 || itemId == 32355) {
		Item[] items = { new Item(32345), new Item(32350), new Item(32355) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need law ethereal hands, "
							+ "blood ethereal hands and death ethereal hands in order to combine them into "
							+ "infinity ethereal hands.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(32360));
			return false;
	}
	if (itemId == 32346 || itemId == 32351 || itemId == 32356) {
		Item[] items = { new Item(32346), new Item(32351), new Item(32356) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need law ethereal feet, "
							+ "blood ethereal feet and death ethereal feet in order to combine them into "
							+ "infinity ethereal feet.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(32361));
			return false;
	}
	/**
	 * Shark Outfits
	 */
	if (itemId == 34200 || itemId == 34205 || itemId == 34210) {
		Item[] items = { new Item(34200), new Item(34205), new Item(34210) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You'll need a regular shark head, "
					+ "a burnt shark head and a tiger shark head to combine them into a fury shark head.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(34215));
			return false;
	}
	if (itemId == 34201 || itemId == 34206 || itemId == 34211) {
		Item[] items = { new Item(34201), new Item(34206), new Item(34211) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You'll need a regular shark body, "
					+ "a burnt shark body and a tiger shark body to combine them into a fury shark body.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(34216));
			return false;
	}
	if (itemId == 34202 || itemId == 34207 || itemId == 34212) {
		Item[] items = { new Item(34202), new Item(34207), new Item(34212) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You'll need regular shark legs, "
					+ "burnt shark legs and tiger shark legs to combine them into fury shark legs.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(34217));
			return false;
	}
	if (itemId == 34203 || itemId == 34208 || itemId == 34213) {
		Item[] items = { new Item(34203), new Item(34208), new Item(34213) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You'll need regular shark hands, "
					+ "burnt shark hands and tiger shark hands to combine them into fury shark hands.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(34218));
			return false;
	}
	if (itemId == 34204 || itemId == 34209 || itemId == 34214) {
		Item[] items = { new Item(34204), new Item(34209), new Item(34214) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You'll need regular shark feet, "
					+ "burnt shark feet and tiger shark feet to combine them into fury shark feet.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(34219));
			return false;
	}
	/**
	 * Divine simulacrum Outfits
	 */
	if (itemId == 35963 || itemId == 35968 || itemId == 35973) {
		Item[] items = { new Item(35963), new Item(35968), new Item(35973) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need a divination energy head, "
							+ "a divination chronicle head and a divination memory head in order to combine them into "
							+ "an elder divination head.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(35978));
			return false;
	}
	if (itemId == 35964 || itemId == 35969 || itemId == 35974) {
		Item[] items = { new Item(35964), new Item(35969), new Item(35974) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need a divination energy body, "
							+ "a divination chronicle body and a divination memory body in order to combine them into "
							+ "an elder divination body.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(35979));
		return false;
	}
	if (itemId == 35965 || itemId == 35970 || itemId == 35975) {
		Item[] items = { new Item(35965), new Item(35970), new Item(35975) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need divination energy legs, "
							+ "divination chronicle legs and divination memory legs in order to combine them into "
							+ "elder divination legs.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(35980));
		return false;
	}
	if (itemId == 35966 || itemId == 35971 || itemId == 35976) {
		Item[] items = { new Item(35966), new Item(35971), new Item(35976) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need divination energy hands, "
							+ "divination chronicle hands and divination memory hands in order to combine them into "
							+ "elder divination hands.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(35981));
		return false;
	}
	if (itemId == 35967 || itemId == 35972 || itemId == 35977) {
		Item[] items = { new Item(35967), new Item(35972), new Item(35977) };
		if (!player.getInventory().containsItems(items)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You'll need divination energy feet, "
							+ "divination chronicle feet and divination memory feet in order to combine them into "
							+ "elder divination feet.");
			return false;
		}
		player.getInventory().removeItems(items);
		player.getInventory().addItem(new Item(35982));
		return false;
		}
	return false;
	}

}
