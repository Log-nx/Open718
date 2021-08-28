package com.rs.game.player.actions.crafting;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;

public class NoxiousWeaponCrafting extends Action {
	
	public enum Noxious {
		NOXIOUS_SCYTHE(91, 500, new Item[] { new Item(31721, 1), new Item(31722, 1) }, new Item(31725)),
		NOXIOUS_STAFF(91, 1500, new Item[] {new Item(31721, 1), new Item(31723, 1) }, new Item(31729)),
		NOXIOUS_LONGBOW(91, 1000, new Item[] {new Item(31721, 1), new Item(31724, 1) }, new Item(31733));
	
		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item energyProduce;
	
		private Noxious(int levelRequired, double experience, Item[] itemsRequired, Item energyProduce) {
		    this.levelRequired = levelRequired;
		    this.experience = experience;
		    this.itemsRequired = itemsRequired;
		    this.energyProduce = energyProduce;
		}
	
		public Item[] getItemsRequired() {
		    return itemsRequired;
		}
	
		public int getLevelRequired() {
		    return levelRequired;
		}
	
		public Item getProduceEnergy() {
		    return energyProduce;
		}
	
		public double getExperience() {
		    return experience;
		}
    }

    public Noxious scale;
    public int ticks;

    public NoxiousWeaponCrafting(Noxious scale, int ticks) {
		this.scale = scale;
		this.ticks = ticks;
    }

    @Override
    public boolean start(Player player) {
    	if (!process(player))
			return false;
		return true;
    }

    @Override
    public boolean process(Player player) {
		if (scale == null || player == null)
		    return false;
		if (ticks <= 0)
			return false;
		if (player.getSkills().getLevel(Skills.CRAFTING) < scale.getLevelRequired()) {
			player.getPackets().sendGameMessage("You need a Crafting level of at least " + scale.getLevelRequired() + " to create a " + scale.getProduceEnergy().getDefinitions().getName());
		    return false;
		}
    	int amount = scale.getItemsRequired()[0].getAmount();
		if (!player.getInventory().containsItem(scale.getItemsRequired()[0].getId(), amount)) {
			player.sm("You need "+(amount > 1 ? "x"+amount+" of" : "a")+" " + scale.getItemsRequired()[0].getDefinitions().getName() + " to create " + scale.getProduceEnergy().getDefinitions().getName() + ".");
		    return false;
		}
		if (scale.getItemsRequired().length > 0) {
	    	amount = scale.getItemsRequired()[1].getAmount();
		    if (!player.getInventory().containsItem(scale.getItemsRequired()[1].getId(), amount)) {
				player.sm("You need "+(amount > 1 ? "x"+amount+" of" : "a")+" " + scale.getItemsRequired()[1].getDefinitions().getName() + " to create " + scale.getProduceEnergy().getDefinitions().getName() + ".");
				return false;
		    }
		}
		return true;
    }

    @Override
    public int processWithDelay(Player player) {
		ticks--;
		double xp = scale.getExperience();
		for (Item required : scale.getItemsRequired())
		    player.getInventory().deleteItem(required.getId(), required.getAmount());
		int amount = scale.getProduceEnergy().getAmount();
		player.getInventory().addItem(scale.getProduceEnergy().getId(), amount);
		player.getSkills().addXp(Skills.CRAFTING, xp);
		player.getPackets().sendGameMessage("You make a "+scale.getProduceEnergy().getDefinitions().getName().toLowerCase()+".", true);
		if (scale.getProduceEnergy().getDefinitions().getId() == 31725)
			player.setNextAnimation(new Animation(24108));
		else if (scale.getProduceEnergy().getDefinitions().getId() == 31729)
			player.setNextAnimation(new Animation(24109));
		else if (scale.getProduceEnergy().getDefinitions().getId() == 31733)
			player.setNextAnimation(new Animation(24110));
		if (ticks > 0)
		    return 1;
		return -1;
    }

    @Override
    public void stop(Player player) {
    	setActionDelay(player, 3);
    }
    
    public static final Noxious[] WEAPON = new Noxious[] {
    		Noxious.NOXIOUS_SCYTHE,
    		Noxious.NOXIOUS_STAFF,
    		Noxious.NOXIOUS_LONGBOW };
}