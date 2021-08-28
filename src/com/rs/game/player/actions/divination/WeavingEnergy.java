package com.rs.game.player.actions.divination;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.actions.divination.WeavingEnergy.Energy;

/**
 * Handles Weaving Divination energy.
 * @author BigFuckinChungus.
 */
public class WeavingEnergy extends Action {
	
	    public enum Energy {
	
		/**
		 * Pale Energy Products
		 */
	    													//Energy          //Item required         //Produce
		DIVINE_CRAYFISH_BUBBLE(1, 1.0, new Item[] { new Item(29313, 5), new Item(13435, 20) }, new Item(31080)),
		DIVINE_BRONZE_ROCK(3, 1.2, new Item[] {new Item(29313, 20), new Item(436, 20)}, new Item(29294)),
		DIVINE_KEBBIT_BURROW(4, 1.3, new Item[] {new Item(29313, 25), new Item(9986, 20)}, new Item(29300)),
		DIVINE_TREE(7, 1.8, new Item[] { new Item(29313, 5), new Item(1511, 20)}, new Item(29304)), 
		
		/**
		 * Flicking Energy Products
		 */
		DIVINE_HERB_PATCH_I(12, 3.1, new Item[] { new Item(29314, 5), new Item(249, 10)}, new Item(29310)), 
		DIVINE_IRON_ROCK(19, 4, new Item[] { new Item(29314, 20), new Item(440, 15)}, new Item(29295)), 
		
		/**
		 * Bright Energy Products
		 */
		DIVINE_OAK_TREE(21, 5.1, new Item[] { new Item(29315, 15), new Item(1521, 20)}, new Item(29305)),
		DIVINE_BIRD_SNARE(24, 5.4, new Item[] { new Item(29315, 30), new Item(9978, 20)}, new Item(29301)), 
		
		/**
		 * Glowing Energy Products
		 */
		DIVINE_WILLOW_TREE(31, 7.2, new Item[] { new Item(29316, 20), new Item(1519, 20)}, new Item(29306)),
		DIVINE_COAL_ROCK(31, 7.2, new Item[] { new Item(29316, 30), new Item(453, 20)}, new Item(29296)),
		DIVINE_DEADFALL_TRAP(34, 7.5, new Item[] { new Item(29316, 45), new Item(10113, 24)}, new Item(29302)),
		
		/**
		 * Sparkling Energy Products
		 */
		DIVINE_LOBSTER_BUBBLE(41, 10, new Item[] { new Item(29317, 70), new Item(377, 10)}, new Item(31084)),
		DIVINE_MAPLE_TREE(44, 9.3, new Item[] { new Item(29317, 25), new Item(377, 20)}, new Item(29307)),
		
		/**
		 * Gleaming Energy Products
		 */
		DIVINE_HERB_PATCH_II(51, 11.3, new Item[] { new Item(29318, 20), new Item(259, 20)}, new Item(29311)),
		DIVINE_SWORDFISH_BUBBLE(53, 13, new Item[] { new Item(29318, 70), new Item(371, 20)}, new Item(31085)),

		/**
		 * Vibrant Energy Products
		 */
		DIVINE_MITHRIL_ROCK(61, 13.1, new Item[] { new Item(29319, 30), new Item(447, 20)}, new Item(29297)),
		DIVINE_YEW_TREE(62, 13.2, new Item[] { new Item(29319, 30), new Item(1515, 20)}, new Item(29308)),
		DIVINE_BOX_TRAP(64, 13.4, new Item[] { new Item(29319, 45), new Item(10034, 20)}, new Item(29303)),

		/**
		 * Lustrous Energy Products
		 */
		DIVINE_ADAMANTITE_ROCK(73, 15.3, new Item[] { new Item(29320, 40), new Item(449, 25)}, new Item(29298)),
		DIVINE_SIMULACRUM_II(92, 16, new Item[] { new Item(29320, 100), new Item(29323, 100) }, new Item(31311)),

		/**
		 * Brilliant Energy Products
		 */
		DIVINE_HERB_PATCH_III(82, 17.5, new Item[] { new Item(29321, 10), new Item(265, 5)}, new Item(29312)),
		DIVINE_MAGIC_TREE(83, 18, new Item[] { new Item(29321, 40), new Item(1515, 5)}, new Item(29309)),
		
		/**
		 * Radiant Energy Products
		 */
		DIVINE_SIMULACRUM_I(75, 16, new Item[] { new Item(29322, 100), new Item(29320, 100) }, new Item(31310)),
		DIVINE_CAVEFISH_BUBBLE(89, 20, new Item[] { new Item(29322, 70), new Item(15264, 15)}, new Item(29309)),
		
		/**
		 * Luminous Energy Products
		 */
		DIVINE_ROCKTAIL_BUBBLE(91, 21, new Item[] { new Item(29323, 80), new Item(15270, 10)}, new Item(31088)),
		DIVINE_RUNITE_ROCK(94, 22, new Item[] { new Item(29323, 80), new Item(451, 6)}, new Item(29299)),
		;

		public static Energy getEnergy(int id) {
		    for (Energy ener : Energy.values()) {
				for (Item item : ener.getItemsRequired())
				    if (item.getId() == id)
				    	return ener;
		    }
		    return null;
		}
	
		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item energyProduce;
	
		private Energy(int levelRequired, double experience, Item[] itemsRequired, Item energyProduce) {
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

    public Energy energy;
    public int ticks;

    public WeavingEnergy(Energy bar, int ticks) {
		this.energy = bar;
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
		if (energy == null || player == null)
		    return false;
		if (ticks <= 0)
			return false;
		if (player.created && player.getRights() < 2) {
			player.getPackets().sendGameMessage("You've already created one divine location today.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.DIVINATION) < energy.getLevelRequired()) {
			player.getPackets().sendGameMessage("You need a Divination level of at least " + energy.getLevelRequired() + " to create a " + energy.getProduceEnergy().getDefinitions().getName());
		    return false;
		}
    	int amount = energy.getItemsRequired()[0].getAmount();
		if (!player.getInventory().containsItem(energy.getItemsRequired()[0].getId(), amount)) {
			player.getPackets().sendGameMessage("You need "+(amount > 1 ? "x"+amount+" of" : "a")+" " + energy.getItemsRequired()[0].getDefinitions().getName() + " to create a " + energy.getProduceEnergy().getDefinitions().getName() + ".");
		    return false;
		}
		if (energy.getItemsRequired().length > 0) {
	    	amount = energy.getItemsRequired()[1].getAmount();
		    if (!player.getInventory().containsItem(energy.getItemsRequired()[1].getId(), amount)) {
				player.getPackets().sendGameMessage("You need "+(amount > 1 ? "x"+amount+" of" : "a")+" " + energy.getItemsRequired()[1].getDefinitions().getName() + " to create a " + energy.getProduceEnergy().getDefinitions().getName() + ".");
				return false;
		    }
		}
		return true;
    }

    @Override
    public int processWithDelay(Player player) {
		ticks--;
		player.created = true;
		player.setNextAnimation(new Animation(21225));
		player.setNextGraphics(new Graphics(4249));
		double xp = energy.getExperience();
		player.getSkills().addXp(Skills.DIVINATION, xp);
		for (Item required : energy.getItemsRequired())
		    player.getInventory().deleteItem(required.getId(), required.getAmount());
		int amount = energy.getProduceEnergy().getAmount();
		player.getInventory().addItem(energy.getProduceEnergy().getId(), amount);
		player.getPackets().sendGameMessage("You weave the energy into a " + energy.getProduceEnergy().getDefinitions().getName().toLowerCase() + ".", true);
		if (ticks > 0)
		    return 1;
		return -1;
    }

    @Override
    public void stop(Player player) {
    	setActionDelay(player, 3);
    }
    
	public static boolean handleWeaving(final Player player, Item item) {
	Energy energy = Energy.getEnergy(item.getId());
	if (energy != null) {
		if (item.getId() == 29313) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.PALE);
		} else if (item.getId() == 29314) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.FLICKERING);
		} else if (item.getId() == 29315) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.BRIGHT);
		} else if (item.getId() == 29316) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.GLOWING);
		} else if (item.getId() == 29317) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.SPARKLING);
		} else if (item.getId() == 29318) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.GLEAMING);
		} else if (item.getId() == 29319) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.VIBRANT);
		} else if (item.getId() == 29320) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.LUSTROUS);
		} else if (item.getId() == 29321) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.BRILLIANT);
		} else if (item.getId() == 29322) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.RADIANT);
		} else if (item.getId() == 29323) {
			player.getDialogueManager().startDialogue("WeavingD", null, WeavingEnergy.LUMINOUS);
		}
		return true;
		}
		return false;
	}
	
	public static boolean dropDivine(final Player player, Item item) {
		int itemId = item.getId();
		if (itemId == 29294) {
			DivineObject.placeDivine(player, itemId, 87285, 34107, 1, 14);
		} else if (itemId == 29295) {
			DivineObject.placeDivine(player, itemId, 87286, 57572, 15, 14);
		} else if (itemId == 29296) {
			DivineObject.placeDivine(player, itemId, 87287, 87266, 30, 14);
		} else if (itemId == 29297) {
			DivineObject.placeDivine(player, itemId, 87288, 87267, 55, 14);
		} else if (itemId == 29298) {
			DivineObject.placeDivine(player, itemId, 87289, 87268, 70, 14);
		} else if (itemId == 29299) {
			DivineObject.placeDivine(player, itemId, 87290, 87269, 85, 14);
		}
		// divine trees
		else if (itemId == 29304) {
			DivineObject.placeDivine(player, itemId, 87295, 87274, 1, 8);
		} else if (itemId == 29305) {
			DivineObject.placeDivine(player, itemId, 87296, 87275, 15, 8);
		} else if (itemId == 29306) {
			DivineObject.placeDivine(player, itemId, 87297, 87276, 30, 8);
		} else if (itemId == 29307) {
			DivineObject.placeDivine(player, itemId, 87298, 87277, 45, 8);
		} else if (itemId == 29308) {
			DivineObject.placeDivine(player, itemId, 87299, 87278, 60, 8);
		} else if (itemId == 29309) {
			DivineObject.placeDivine(player, itemId, 87300, 87279, 75, 8);
		}
		// herblore patch (farming)
		else if (itemId == 29310) {
			DivineObject.placeDivine(player, itemId, 87301, 87280, 9, Skills.FARMING);
		} else if (itemId == 29311) {
			DivineObject.placeDivine(player, itemId, 87302, 87281, 44, Skills.FARMING);
		} else if (itemId == 29312) {
			DivineObject.placeDivine(player, itemId, 87303, 87282, 67, Skills.FARMING);
		}
		// hunting
		else if (itemId == 29300) {
			DivineObject.placeDivine(player, itemId, 87291, 87270, 1, 21);
		} else if (itemId == 29301) {
			DivineObject.placeDivine(player, itemId, 87292, 87271, 1, 21);
		} else if (itemId == 29302) {
			DivineObject.placeDivine(player, itemId, 87293, 87272, 23, 21);
		} else if (itemId == 29303) {
			DivineObject.placeDivine(player, itemId, 87294, 87273, 53, 21);
		}
		// fishing
		else if (itemId == 31080) {
			DivineObject.placeDivine(player, itemId, 90232, 90223, 1, Skills.FISHING);
		} else if (itemId == 31081) {
			DivineObject.placeDivine(player, itemId, 90233, 90224, 10, Skills.FISHING);
		} else if (itemId == 31082) {
			DivineObject.placeDivine(player, itemId, 90234, 90225, 20, Skills.FISHING);
		} else if (itemId == 31083) {
			DivineObject.placeDivine(player, itemId, 90235, 90226, 30, Skills.FISHING);
		} else if (itemId == 31084) {
			DivineObject.placeDivine(player, itemId, 90236, 90227, 40, Skills.FISHING);
		} else if (itemId == 31085) {
			DivineObject.placeDivine(player, itemId, 90237, 90228, 50, Skills.FISHING);
		} else if (itemId == 31086) {
			DivineObject.placeDivine(player, itemId, 90238, 90229, 76, Skills.FISHING);
		} else if (itemId == 31087) {
			DivineObject.placeDivine(player, itemId, 90239, 90230, 85, Skills.FISHING);
		} else if (itemId == 31088) {
			DivineObject.placeDivine(player, itemId, 90240, 90231, 90, Skills.FISHING);
		}
		// div not being used
		else if (itemId == 31310) {
			DivineObject.placeDivine(player, itemId, 66526, 66528, 75, Skills.DIVINATION);
		} else if (itemId == 31311) {
			DivineObject.placeDivine(player, itemId, 66529, 66531, 86, Skills.DIVINATION);
		}
		return true;
	}
    
    /**
     * Pale energy products
     */
    public static final Energy[] PALE = new Energy[] {
    		Energy.DIVINE_CRAYFISH_BUBBLE,
    		Energy.DIVINE_BRONZE_ROCK,
    		Energy.DIVINE_KEBBIT_BURROW,
    		Energy.DIVINE_TREE };

    /**
     * Flickering energy products
     */
    public static final Energy[] FLICKERING = new Energy[] {
    		Energy.DIVINE_HERB_PATCH_I,
    		Energy.DIVINE_IRON_ROCK };

    /**
     * Bright energy products
     */
    public static final Energy[] BRIGHT = new Energy[] {
    		Energy.DIVINE_OAK_TREE,
    		Energy.DIVINE_BIRD_SNARE };

    /**
     * Glowing energy products
     */
    public static final Energy[] GLOWING = new Energy[] {
    		Energy.DIVINE_WILLOW_TREE,
    		Energy.DIVINE_COAL_ROCK,
    		Energy.DIVINE_DEADFALL_TRAP };

    /**
     * Sparkling energy products
     */
    public static final Energy[] SPARKLING = new Energy[] {
    		Energy.DIVINE_LOBSTER_BUBBLE,
    		Energy.DIVINE_MAPLE_TREE };

    /**
     * Gleaming energy products
     */
    public static final Energy[] GLEAMING = new Energy[] {
    		Energy.DIVINE_HERB_PATCH_II,
    		Energy.DIVINE_SWORDFISH_BUBBLE };

    /**
     * Vibrant energy products
     */
    public static final Energy[] VIBRANT = new Energy[] {
    		Energy.DIVINE_MITHRIL_ROCK,
    		Energy.DIVINE_YEW_TREE,
    		Energy.DIVINE_BOX_TRAP };

    /**
     * Lustrous energy products
     */
    public static final Energy[] LUSTROUS = new Energy[] {
    		Energy.DIVINE_ADAMANTITE_ROCK,
    		Energy.DIVINE_SIMULACRUM_II };

    /**
     * Brilliant energy products
     */
    public static final Energy[] BRILLIANT = new Energy[] {
    		Energy.DIVINE_HERB_PATCH_III,
    		Energy.DIVINE_MAGIC_TREE };

    /**
     * Radiant energy products
     */
    public static final Energy[] RADIANT = new Energy[] {
    		Energy.DIVINE_CAVEFISH_BUBBLE,
    		Energy.DIVINE_SIMULACRUM_I };

    /**
     * Luminous energy products
     */
    public static final Energy[] LUMINOUS = new Energy[] {
    		Energy.DIVINE_ROCKTAIL_BUBBLE,
    		Energy.DIVINE_RUNITE_ROCK };
}