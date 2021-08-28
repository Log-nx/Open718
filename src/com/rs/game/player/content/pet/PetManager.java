package com.rs.game.player.content.pet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.item.Item;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.ItemConstants;
import com.rs.utils.Color;
import com.rs.utils.Utils;

/**
 * The pet manager.
 * @author Emperor
 *
 */
public final class PetManager implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -3379270918966667109L;

	/**
	 * The pet details mapping, sorted by item id.
	 */
	private final Map<Integer, PetDetails> petDetails = new HashMap<Integer, PetDetails>();
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * The current NPC id.
	 */
	private int npcId;
	
	/**
	 * The current item id.
	 */
	private int itemId;
	
	/**
	 * The troll baby's name (if any).
	 */
	private String trollBabyName;
	
	private String petName;
	
	/**
	 * Constructs a new {@code PetManager} {@code Object}.
	 */
	public PetManager() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Spawns a pet.
	 * @param itemId The item id.
	 * @param deleteItem If the item should be removed.
	 * @return {@code True} if we were dealing with a pet item id.
	 */
	public boolean spawnPet(int itemId, boolean deleteItem) {
		Pets pets = Pets.forId(itemId);
		if (pets == null)
			return false;
		else if (player.getPet() != null || player.getFamiliar() != null) {
			player.getPackets().sendGameMessage("You already have a follower.");
			return true;
		} else if (!hasRequirements(pets)) {
			return true;
		}
		int baseItemId = pets.getBabyItemId();
		PetDetails details = petDetails.get(baseItemId);
		if (details == null) {
			details = new PetDetails(pets.getGrowthRate() == 0.0 ? 100.0 : 0.0);
			petDetails.put(baseItemId, details);
		}
		int id = pets.getItemId(details.getStage());
		if (itemId != id) {
			player.getPackets().sendGameMessage("This is not the right pet, grow the pet correctly.");
			return true;
		}
		int npcId = pets.getNpcId(details.getStage());
		if (npcId > 0) {
			Pet pet = new Pet(npcId, itemId, player, player, details);
			this.npcId = npcId;
			this.itemId = itemId;
			pet.setGrowthRate(pets.getGrowthRate());
			player.setPet(pet);
			if (deleteItem) {
				player.setNextAnimation(new Animation(827));
				player.getInventory().deleteItem(itemId, 1);
				player.getPet().sendFollowerDetails();
			}
			player.getPet().sendFollowerDetails();
			return true;
		}
		return true;
	}
	
	/**
	 * Checks if the player has the requirements for the pet.
	 * @param pet The pet.
	 * @return {@code True} if so.
	 */
	private boolean hasRequirements(Pets pet) {
		switch (pet) {
		case TZREK_JAD:
			if (!player.isExtremeDonator()) {
				player.getPackets().sendGameMessage("You need to be Extreme donator to use this pet.");
				return false;
			}
			if (!player.isCompletedFightCaves()) {
				player.getPackets().sendGameMessage("You need to complete at least one fight cave minigame to use this pet.");
				return false;
			}
			if (!player.isWonFightPits()) {
				player.getPackets().sendGameMessage("You need to win at least one fight pits minigame to use this pet.");
				return false;
			}
			return true;
		case GIANT_WOLPERTINGER:
			return player.getRights() > 1;
		}
		return true;
	}
	
	public void init() {
		if (npcId > 0 && itemId > 0) {
			spawnPet(itemId, false);
		}
	}
	
	public void eat(int foodId, Pet npc) {
		if (npc != player.getPet()) {
			player.getPackets().sendGameMessage("This isn't your pet!");
			return;
		}
		Pets pets = Pets.forId(itemId);
		if (pets == null) {
			return;
		}
		if (pets == Pets.TROLL_BABY) {
			if (!ItemConstants.isTradeable(new Item(foodId))) {
				player.getPackets().sendGameMessage("Your troll baby won't eat this item.");
				return;
			}
			if (trollBabyName == null) {
				trollBabyName = ItemDefinitions.getItemDefinitions(foodId).getName();
				npc.setName(trollBabyName);
				npc.setNextForceTalk(new ForceTalk("YUM! Me likes " + trollBabyName + "!"));
			}
			player.getInventory().deleteItem(foodId, 1);
			player.getPackets().sendGameMessage("Your pet happily eats the " + ItemDefinitions.getItemDefinitions(foodId).getName() + ".");
			return;
		}
		for (int food : pets.getFood()) {
			if (food == foodId) {
				player.getInventory().deleteItem(food, 1);
				player.getPackets().sendGameMessage("Your pet happily eats the " + ItemDefinitions.getItemDefinitions(food).getName() + ".");
				player.setNextAnimation(new Animation(827));
				npc.getDetails().updateHunger(-15.0);
				return;
			}
		}
		player.getPackets().sendGameMessage("Nothing interesting happens.");
	}
	
	public void removeDetails(int itemId) {
		Pets pets = Pets.forId(itemId);
		if (pets == null) {	
			return;
		}
		petDetails.remove(pets.getBabyItemId());
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getTrollBabyName() {
		return trollBabyName;
	}

	public String getPetName() {
		return petName;
	}
	
	public void setTrollBabyName(String trollBabyName) {
		this.trollBabyName = trollBabyName;
	}
	
	public void setPetName(String petName) {
		this.petName = petName;
	}

	public boolean isALegendaryPet() {
		switch (getNpcId()) {
		case Pet.BLAZE_HOUND:
		case Pet.BLOOD_POUNCER:
		case Pet.DRAGON_WOLF:
		case Pet.PROTOTYPE_COLOSSUS:
		case Pet.RORY_THE_REINDEER:
		case Pet.SKY_POUNDER:
		case Pet.WARBORN_BEHEMOTH:
			return true;
		}
		return false;
	}

	public static void sendXPMessage(Player player, Pet pet) {
		if (pet.getId() == Pets.MALCOLM.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.INVENTION))+" experience in Invention!");
			else if (pet.getId() == Pets.WILLOW.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.DIVINATION))+" experience in Divination!");
			else if (pet.getId() == Pets.DOJO_MOJO.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.AGILITY))+" experience in Agility!");
			else if (pet.getId() == Pets.BABY_YAGA.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.CONSTRUCTION))+" experience in Construction!");
			else if (pet.getId() == Pets.RAMSAY.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.COOKING))+" experience in Cooking!");
			else if (pet.getId() == Pets.GEMI.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.CRAFTING))+" experience in Crafting!");
			else if (pet.getId() == Pets.GORDIE.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.DUNGEONEERING))+" experience in Dungeoneering!");
			else if (pet.getId() == Pets.BRAINS.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.FARMING))+" experience in Farming!");
			else if (pet.getId() == Pets.BERNIE.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.FIREMAKING))+" experience in Firemaking!");
			else if (pet.getId() == Pets.BUBBLES.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.FISHING))+" experience in Fishing!");
			else if (pet.getId() == Pets.FLO.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.FLETCHING))+" experience in Farming!");
			else if (pet.getId() == Pets.HEBRERT.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.HERBLORE))+" experience in Herblore!");
			else if (pet.getId() == Pets.ACE.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.HUNTER))+" experience in Hunter!");
			else if (pet.getId() == Pets.ROCKY.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.MINING))+" experience in Mining!");
			else if (pet.getId() == Pets.RUE.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.RUNECRAFTING))+" experience in Runecrafting!");
			else if (pet.getId() == Pets.CRABBE.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.SLAYER))+" experience in Slayer!");
			else if (pet.getId() == Pets.SMITHY.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.SMITHING))+" experience in Smithing!");
			else if (pet.getId() == Pets.RALPH.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.THIEVING))+" experience in Thieving!");
			else if (pet.getId() == Pets.WOODY.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.WOODCUTTING))+" experience in Woodcutting!");
			else if (pet.getId() == Pets.SIFU.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.ATTACK))+" experience in Attack!");
			else if (pet.getId() == Pets.KANGALI.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.STRENGTH))+" experience in Strength!");
			else if (pet.getId() == Pets.WALLACE.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.DEFENCE))+" experience in Defence!");
			else if (pet.getId() == Pets.MORTY.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.HITPOINTS))+" experience in Hitpoints!");
			else if (pet.getId() == Pets.SPARKY.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.RANGE))+" experience in Range!");
			else if (pet.getId() == Pets.GHOSTLY.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.PRAYER))+" experience in Prayer!");
			else if (pet.getId() == Pets.NEWTON.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.MAGIC))+" experience in Magic!");
			else if (pet.getId() == Pets.SHAMINI.getBabyNpcId())
			player.getPackets().sendGameMessage(Color.YELLOW, pet.owner.getDisplayName() + " has "+Utils.formatNumber(pet.owner.getSkills().getXp(Skills.SUMMONING))+" experience in Summoning!");	
	}
	
}