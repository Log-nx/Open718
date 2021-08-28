package com.rs.game.npc.pet;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.pet.PetDetails;
import com.rs.game.player.content.pet.Pets;
import com.rs.utils.Utils;

public final class Pet extends NPC {

	private static final long serialVersionUID = -2848843157767889742L;

	public final Player owner;
	
	public boolean isLegendaryPet;
	public long petLifeSaver = 0;
	public long petHighAlchemy = 0;
	public long petItemRepair = 0;
	public long petItemBank = 0;
	public long petItemForge = 0;
	public long petItemForageReset = 0;
	public long petNPCExecute = 0;
	public boolean petVamp = false;
	public long petVampReset = 0;
	public int forageCount = 0;
	public boolean foraging = false;
	public long BoBTime = 0;
	public boolean activeBoB = false;

	public long getPetItemForageReset() {
		return petItemForageReset;
	}

	private void setPetItemForageReset(long time) {
		this.petItemForageReset = time;
		owner.petItemForageReset = time;
	}

	public boolean isForging() {
		return foraging;
	}

	public void setForaging(boolean value) {
		foraging = value;
	}

	public long getPetItemForge() {
		return petItemForge;
	}

	public long getPetLifeSaver() {
		return petLifeSaver;
	}

	public long getPetHighAlchemy() {
		return petHighAlchemy;
	}

	public long getPetNPCExecute() {
		return petNPCExecute;
	}

	public boolean isVamp() {
		return petVamp;
	}
	
	
	public boolean isASkillingPet(Pet pet) {
		if (pet.getId() == Pets.MALCOLM.getBabyNpcId() || pet.getId() == Pets.SIFU.getBabyNpcId() || pet.getId() == Pets.GHOSTLY.getBabyItemId() 
				|| pet.getId() == Pets.MORTY.getBabyItemId() || pet.getId() == Pets.WALLACE.getBabyItemId() || pet.getId() == Pets.CRABBE.getBabyItemId()
				|| pet.getId() == Pets.KANGALI.getBabyItemId() || pet.getId() == Pets.SHAMINI.getBabyItemId() || pet.getId() == Pets.DOJO_MOJO.getBabyItemId()
				|| pet.getId() == Pets.BABY_YAGA.getBabyItemId() || pet.getId() == Pets.GEMI.getBabyItemId() || pet.getId() == Pets.RAMSAY.getBabyItemId()
				|| pet.getId() == Pets.WILLOW.getBabyItemId() || pet.getId() == Pets.GORDIE.getBabyItemId() || pet.getId() == Pets.BRAINS.getBabyItemId()
				|| pet.getId() == Pets.BERNIE.getBabyItemId() || pet.getId() == Pets.BUBBLES.getBabyItemId() || pet.getId() == Pets.FLO.getBabyItemId()
				|| pet.getId() == Pets.HEBRERT.getBabyItemId() || pet.getId() == Pets.ACE.getBabyItemId() || pet.getId() == Pets.MALCOLM.getBabyItemId()
				|| pet.getId() == Pets.NEWTON.getBabyItemId() || pet.getId() == Pets.ROCKY.getBabyItemId() || pet.getId() == Pets.SPARKY.getBabyItemId()
				|| pet.getId() == Pets.RUE.getBabyItemId() || pet.getId() == Pets.SMITHY.getBabyItemId() || pet.getId() == Pets.RALPH.getBabyItemId()
				|| pet.getId() == Pets.WOODY.getBabyItemId()) {
			return true;
		}
		return false;
	}

	public void setVamp(boolean value) {
		this.petVamp = value;
		owner.petVamp = value;
	}

	private final int[][] checkNearDirs;

	private final int itemId;

	private final PetDetails details;

	public static final transient int DRAGON_WOLF = 16735, WARBORN_BEHEMOTH = 17788, RORY_THE_REINDEER = 18816, BLOOD_POUNCER = 16668,
			SKY_POUNDER = 16665, PROTOTYPE_COLOSSUS = 17782, BLAZE_HOUND = 16662;

	private double growthRate;

	public static transient int MAX_LEVEL = 25;
	
	private final Pets pet;

	public Pet(int id, int itemId, Player owner, WorldTile tile, PetDetails details) {
		super(id, tile, -1, false);
		this.owner = owner;
		this.itemId = itemId;
		checkNearDirs = Utils.getCoordOffsetsNear(super.getSize());
		this.details = details;
		pet = Pets.forId(itemId);
		if (pet != null && owner.getPetManager().getPetName() != null) {
			setName(owner.getPetManager().getPetName());
		}
		if (pet == Pets.TROLL_BABY && owner.getPetManager().getTrollBabyName() != null) {
			setName(owner.getPetManager().getTrollBabyName());
		}
		setRun(true);
		sendMainConfigurations();
		sendFollowerDetails();
	}

	public void call() {
		int size = getSize();
		WorldTile teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final WorldTile tile = new WorldTile(new WorldTile(owner.getX() + checkNearDirs[0][dir], owner.getY() + checkNearDirs[1][dir], owner.getPlane()));
			if (World.canMoveNPC(tile.getPlane(), tile.getX(), tile.getY(), size)) {
				teleTile = tile;
				break;
			}
		}
		if (teleTile == null) {
			return;
		}
		setNextWorldTile(teleTile);
	}

	public PetDetails getDetails() {
		return details;
	}

	public double getGrowthRate() {
		return growthRate;
	}

	public int getItemId() {
		return itemId;
	}

	public void growNextStage() {
		if (details.getStage() == 3) {
			return;
		}
		if (pet == null) {
			return;
		}
		int npcId = pet.getNpcId(details.getStage() + 1);
		if (npcId < 1) {
			return;
		}
		details.setStage(details.getStage() + 1);
		int itemId = pet.getItemId(details.getStage());
		if (pet.getNpcId(details.getStage() + 1) > 0) {
			details.updateGrowth(-100.0);
		}
		owner.getPetManager().setItemId(itemId);
		owner.getPetManager().setNpcId(npcId);
		finish();
		Pet newPet = new Pet(npcId, itemId, owner, owner, details);
		newPet.growthRate = growthRate;
		owner.setPet(newPet);
		owner.sm("<col=ff0000>Your pet has grown larger.</col>");
	}

	public void lockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, true);
	}

	public void pickup() {
		owner.getInventory().addItem(itemId, 1);
		owner.setPet(null);
		owner.getPetManager().setNpcId(-1);
		owner.getPetManager().setItemId(-1);
		switchOrb(false);
		owner.getPackets().closeInterface(owner.getInterfaceManager().hasResizableScreen() ? 98 : 212);
		owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
		owner.getInterfaceManager().openGameTab(4);
		finish();
	}

	@Override
	public void processNPC() {
		unlockOrb();
		if (pet == Pets.TROLL_BABY || pet.getFood().length > 0) {
			details.updateHunger(0.025);
			owner.getPackets().sendConfigByFile(4286, (int) details.getHunger());
		}
		if (details.getHunger() >= 90.0 && details.getHunger() < 99.9) {
			owner.sm("<col=ff0000>Your pet is starving, feed it before it runs off.</col>");
		} else if (details.getHunger() == 100.0) {
			owner.getPetManager().setNpcId(-1);
			owner.getPetManager().setItemId(-1);
			owner.setPet(null);
			owner.getPetManager().removeDetails(itemId);
			owner.sm("Your pet has ran away to find some food!");
			switchOrb(false);
			owner.getPackets().closeInterface(owner.getInterfaceManager().hasResizableScreen() ? 98 : 212);
			owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
			finish();
			return;
		}
		if (growthRate > 0.000) {
			details.updateGrowth(growthRate);
			owner.getPackets().sendConfigByFile(4285, (int) details.getGrowth());
			if (details.getGrowth() == 100.0) {
				growNextStage();
			}
		}
		if (!withinDistance(owner, 12)) {
			call();
			return;
		}
		sendFollow();
	}

	private void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex())
			setNextFaceEntity(owner);
		if (isFrozen())
			return;
		int size = getSize();
		int targetSize = owner.getSize();
		if (Utils.colides(getX(), getY(), size, owner.getX(), owner.getY(), targetSize) && !owner.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + targetSize, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), owner.getY() + targetSize)) {
						resetWalkSteps();
						if (!addWalkSteps(getX(), owner.getY() - size)) {
							return;
						}
					}
				}
			}
			return;
		}
		resetWalkSteps();
		if (!clipedProjectile(owner, true) || !Utils.isOnRange(getX(), getY(), size, owner.getX(), owner.getY(), targetSize, 0))
			calcFollow(owner, 2, true, false);
	}

	public void sendFollowerDetails() {
		owner.getPackets().sendConfigByFile(4285, (int) details.getGrowth());
		owner.getPackets().sendConfigByFile(4286, (int) details.getHunger());
		final boolean res = owner.getInterfaceManager().hasResizableScreen();
		owner.getPackets().sendInterface(true, res ? 746 : 548, res ? 119 : 179, 662);
		unlock();
		owner.getPackets().sendGlobalConfig(168, 8);// tab id
	}

	public void sendMainConfigurations() {
		switchOrb(true);
		owner.getPackets().sendConfig(448, itemId);// configures
		owner.getPackets().sendConfig(1160, 243269632); // sets npc emote
		owner.getPackets().sendGlobalConfig(1436, 0);
		unlockOrb(); // temporary
	}

	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}

	public void switchOrb(boolean enable) {
		owner.getPackets().sendConfig(1174, enable ? getId() : 0);
		if (enable) {
			unlock();
			return;
		}
		lockOrb();
	}

	public void unlock() {
		owner.getPackets().sendHideIComponent(747, 9, false);
	}

	public void unlockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, false);
		Familiar.sendLeftClickOption(owner);
	}
	
	public double addExperience(double experience) {
		if (details.getExperience() > Skills.MAXIMUM_EXP) {
			return details.setExperience(Skills.MAXIMUM_EXP);
		}
		int oldLevel = getLevelForXp();
		details.setExperience(details.getExperience() + experience / 8);
		int newLevel = getLevelForXp();
		int levelDiff = newLevel - oldLevel;
		if (details.getLevel() > MAX_LEVEL - 1) {
			details.setLevel((short) MAX_LEVEL);
		}
		if (newLevel > oldLevel) {
			details.setLevel((short) (details.getLevel() + levelDiff));
			if (newLevel == MAX_LEVEL) {
				owner.getPackets().sendGameMessage("<col=E86100>Your " + getName() + " has reached it's max level!");
			} else {
				owner.getPackets().sendGameMessage("<col=E86100>Your " + getName() + " has reached level " + details.getLevel() + "!");
			}
			owner.setNextGraphics(new Graphics(1765));
			setNextGraphics(new Graphics(199));
		}
		return experience;
	}

	public int getLevelForXp() {
		double exp = details.getExperience();
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= MAX_LEVEL; lvl++) {
			points += Math.floor(lvl + MAX_LEVEL * 50.0 * Math.pow(MAX_LEVEL / 5.0, lvl / (MAX_LEVEL / 5.0)));
			output = (int) Math.floor(points / 4);
			if (output - 1 >= exp) {
				return lvl;
			}
		}
		return 25;
	}
	
	public Pets getPetType() {
		return pet;
	}
}