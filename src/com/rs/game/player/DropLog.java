package com.rs.game.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class DropLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Item> drops;

	private Player player;
	
	public boolean FIRST_TIME_USE = true;
	public boolean LOCKED_DROP_LOGS = false;
	public int minmumValue = 0;

	public void displayInterface() {
		int itemsSize = 13; 
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendIComponentText(275, 1, player.getDisplayName() + "'s Drops");
		player.getPackets().sendIComponentText(275, 10, " ");
		for (Item items : drops) {
			player.getPackets().sendIComponentText(275, itemsSize, (items.getDefinitions().getValue() > 10000000 ? "<col=fff>" : "<col=f00>") + items.getDefinitions().getName() + " : " + items.getAmount()); // Sendin
			itemsSize++; 
		}
	}
	
	public void displayInterface(Player target, Player player) {
		int itemsSize = 13;
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
			player.getPackets().sendIComponentText(275, 1, target.getDisplayName() + "'s Drops");
			player.getPackets().sendIComponentText(275, 10, " ");
			for (Item items : drops) {
				player.getPackets().sendIComponentText(275, itemsSize, (items.getDefinitions().getValue() > 10000000 ? "<col=fff>" : "<col=f00>") + items.getDefinitions().getName() + " : " + items.getAmount());
				itemsSize++;
			}
		}
	}

	public void addDrop(Item item) {
		if (item.getDefinitions().getValue() < player.getDropLogs().getMinmumValue()) {
			return;
		}
		for (Item items : drops) {
			if (items.getId() == item.getId()) { 
				items.setAmount(items.getAmount() + item.getAmount()); 
				return;
			}
		}
		drops.add(item);
	}

	public void removeDrop(Item item) {
		drops.remove(item);
	}

	public DropLog(Player player) {
		if (drops == null) {
			drops = new ArrayList<Item>();
		}
		this.player = player;
	}
	
	public int getMinmumValue() {
		return minmumValue;
	}

	public void setMinmumValue(int minmumValue) {
		this.minmumValue = minmumValue;
	}

}
