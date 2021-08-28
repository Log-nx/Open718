package com.rs.game.player.content.items;

import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.Color;

public class BossPetManager {
	
	public static boolean unlockPet(Player player, Item item) {
		if (item.getId() == 28626) { // vitalis
			if (player.getInventory().containsItem(28626, 1)) {
				player.getInventory().deleteItem(28626, 1);
				player.getInventory().addItem(28630, 1);
				player.getPackets().sendGameMessage(Color.GREEN, "As you touch the artefact, a familiar face appears..");

			}
			return true;
		}
		if (item.getId() == 33832) { // general awwdor
			if (player.getInventory().containsItem(33832, 1)) {
				player.getInventory().deleteItem(33832, 1);
				player.getInventory().addItem(33806, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33833) { // commander miniana
			if (player.getInventory().containsItem(33833, 1)) {
				player.getInventory().deleteItem(33833, 1);
				player.getInventory().addItem(33807, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33832) { // k'ril tinyroth
			if (player.getInventory().containsItem(33831, 1)) {
				player.getInventory().deleteItem(33831, 1);
				player.getInventory().addItem(33805, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33830) { // chick'ara
			if (player.getInventory().containsItem(33830, 1)) {
				player.getInventory().deleteItem(33830, 1);
				player.getInventory().addItem(33804, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33834) { // nexterminator
			if (player.getInventory().containsItem(33834, 1)) {
				player.getInventory().deleteItem(33834, 1);
				player.getInventory().addItem(33808, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33835) {
			if (player.getInventory().containsItem(33835, 1)) {
				player.getInventory().deleteItem(33835, 1);
				player.getInventory().addItem(33810, 1);
				boolean bank = false;
				if (!player.getInventory().addItem(33809, 1)) {
					bank = true;
					player.getBank().addItem(new Item(33809), true);
				}
				player.getPackets().sendGameMessage(Color.GREEN, "As you inspect the " + item.getName() + ", it magically transforms into two creatures "
								+ (bank ? ("(one of them was added to bank)") : "") + "!");

			}
			return true;
		}
		if (item.getId() == 33836) { // ellie
			if (player.getInventory().containsItem(33836, 1)) {
				player.getInventory().deleteItem(33836, 1);
				player.getInventory().addItem(33811, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33837) { // corporeal puppy
			if (player.getInventory().containsItem(33837, 1)) {
				player.getInventory().deleteItem(33837, 1);
				player.getInventory().addItem(33812, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33838) { // molly
			if (player.getInventory().containsItem(33838, 1)) {
				player.getInventory().deleteItem(33838, 1);
				player.getInventory().addItem(33813, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33839) { // shrimpy
			if (player.getInventory().containsItem(33839, 1)) {
				player.getInventory().deleteItem(33839, 1);
				player.getInventory().addItem(33814, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33840) { // kalphite grubling
			if (player.getInventory().containsItem(33840, 1)) {
				player.getInventory().deleteItem(33840, 1);
				player.getInventory().addItem(33815, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33841) { // kalphite grublet
			if (player.getInventory().containsItem(33841, 1)) {
				player.getInventory().deleteItem(33841, 1);
				if (player.getBank().containsItem(33816)) {
					player.getInventory().addItem(33817, 1);
				} else {
					player.getInventory().addItem(33816, 1);
				}
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33842) { // King black dragonling
			if (player.getInventory().containsItem(33842, 1)) {
				player.getInventory().deleteItem(33842, 1);
				player.getInventory().addItem(33818, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33843) { // legio primulus
			if (player.getInventory().containsItem(33843, 1)) {
				player.getInventory().deleteItem(33843, 1);
				player.getInventory().addItem(33819, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33844) { // legio secundulus
			if (player.getInventory().containsItem(33844, 1)) {
				player.getInventory().deleteItem(33844, 1);
				player.getInventory().addItem(33820, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33845) { // legio tertioulus
			if (player.getInventory().containsItem(33845, 1)) {
				player.getInventory().deleteItem(33845, 1);
				player.getInventory().addItem(33821, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33846) { // legio quartulus
			if (player.getInventory().containsItem(33846, 1)) {
				player.getInventory().deleteItem(33846, 1);
				player.getInventory().addItem(33822, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33847) { // legio quintulus
			if (player.getInventory().containsItem(33847, 1)) {
				player.getInventory().deleteItem(33847, 1);
				player.getInventory().addItem(33823, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33848) { // legio legio sextulus
			if (player.getInventory().containsItem(33848, 1)) {
				player.getInventory().deleteItem(33848, 1);
				player.getInventory().addItem(33824, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33849) { // queen black dragonling
			if (player.getInventory().containsItem(33849, 1)) {
				player.getInventory().deleteItem(33849, 1);
				player.getInventory().addItem(33825, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33850) { // prime hatchling
			if (player.getInventory().containsItem(33850, 1)) {
				player.getInventory().deleteItem(33850, 1);
				player.getInventory().addItem(33826, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33851) { // rex hatchling
			if (player.getInventory().containsItem(33851, 1)) {
				player.getInventory().deleteItem(33851, 1);
				player.getInventory().addItem(33827, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33852) { // supreme hatchling
			if (player.getInventory().containsItem(33852, 1)) {
				player.getInventory().deleteItem(33852, 1);
				player.getInventory().addItem(33828, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 33716) { // bombi
			if (player.getInventory().containsItem(33716, 1)) {
				player.getInventory().deleteItem(33716, 1);
				player.getInventory().addItem(33817, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 37191) { // ava
			if (player.getInventory().containsItem(37191, 1)) {
				player.getInventory().deleteItem(37191, 1);
				player.getInventory().addItem(37185, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 37190) { // nylessa
			if (player.getInventory().containsItem(37190, 1)) {
				player.getInventory().deleteItem(37190, 1);
				player.getInventory().addItem(37184, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 37189) { // greg
			if (player.getInventory().containsItem(37189, 1)) {
				player.getInventory().deleteItem(37189, 1);
				player.getInventory().addItem(37183, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 37188) { // lilwyr
			if (player.getInventory().containsItem(37188, 1)) {
				player.getInventory().deleteItem(37188, 1);
				player.getInventory().addItem(37182, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 37187) { // vindiddy
			if (player.getInventory().containsItem(37187, 1)) {
				player.getInventory().deleteItem(37187, 1);
				player.getInventory().addItem(37181, 1);
				sendMessage(player, item);

			}
			return true;
		}
		if (item.getId() == 37186) { // rawrvek
			if (player.getInventory().containsItem(37186, 1)) {
				player.getInventory().deleteItem(37186, 1);
				player.getInventory().addItem(37180, 1);
				sendMessage(player, item);

			}
			return true;
		}
		return false;
	}
	
	public static void sendMessage(Player player, Item item) {
		player.getPackets().sendGameMessage(Color.GREEN, "As you inspect the " + item.getName() + ", it magically transforms into a creature!");
		player.setNextGraphics(new Graphics(1935));
	}

}
