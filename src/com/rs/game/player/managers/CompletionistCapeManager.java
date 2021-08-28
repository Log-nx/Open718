package com.rs.game.player.managers;

import java.io.Serializable;
import java.util.HashMap;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Color;

public class CompletionistCapeManager implements Serializable {

	private static final long serialVersionUID = 3810943429583318666L;
	private transient Player player;
	private HashMap<Requirement, Integer> progress;
	private int received;

	public CompletionistCapeManager(Player player) {
		this.player = player;
		this.progress = new HashMap<Requirement, Integer>();
		this.received = 0;
		for (Requirement req : Requirement.values())
			if (!progress.containsKey(req))
				progress.put(req, 0);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean receivedCape(int type) {
		return received >= type;
	}

	public void setReceived(int type) {
		received = type;
	}

	public boolean hasCompleted(Requirement req) {
		return progress.get(req) >= req.amount;
	}

	public boolean hasUntrimmedRequirements() {
		for (Requirement req : Requirement.values()) {
			if (req.trimmed)
				continue;
			if (!hasCompleted(req))
				return false;
		}
		return true;
	}

	public boolean hasTrimmedRequirements() {
		for (Requirement req : Requirement.values())
			if (!hasCompleted(req))
				return false;
		return true;
	}

	public void sendComplete() {
		for (Requirement req : Requirement.values())
			progress.put(req, req.amount);
		World.sendWorldMessage(player.getDisplayName() + " has just successfully achieved the trimmed completionist cape!", false, false);
	}

	public void increaseRequirement(Requirement req, int amount) {
		if (!progress.containsKey(req))
			progress.put(req, 0);
		if (hasCompleted(req))
			return;
		progress.put(req, progress.get(req) + amount);
		if (progress.get(req) >= req.amount && !req.isBadRequirement) {
			player.getPackets().sendGameMessage(Color.ORANGE, "Congratulations, you have successfully finished the " + req.toString().toLowerCase().replace("_", " ") + " completionist cape requirement!");
			World.sendWorldMessage(player.getDisplayName() + " has just successfully finished the " + req.toString().toLowerCase().replace("_", " ") + " completionist cape requirement!", false, false);
		}
	}

	public void setRequirement(Requirement req, int amount) {
		progress.put(req, amount);
		increaseRequirement(req, 0);
	}

	public void sendInterface() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Completionist Cape");
		player.getPackets().sendIComponentText(275, 10, "- Untrimmed Requirements -");
		int index = 0;
		for (Requirement req : Requirement.values()) {
			if (!progress.containsKey(req))
				progress.put(req, 0);
			if (!req.trimmed && !req.isBadRequirement)
				player.getPackets().sendIComponentText(275, 10 + ++index, (hasCompleted(req) ? "<str>" : "")
						+ req.description + " (" + progress.get(req) + "/" + req.amount + ")");
		}
		player.getPackets().sendIComponentText(275, 10 + ++index, "");
		player.getPackets().sendIComponentText(275, 10 + ++index, "- Trimmed Requirements -");
		for (Requirement req : Requirement.values())
			if (req.trimmed && !req.isBadRequirement)
				player.getPackets().sendIComponentText(275, 10 + ++index, (hasCompleted(req) ? "<str>" : "")
						+ req.description + " (" + progress.get(req) + "/" + req.amount + ")");
		for (int i = index; i < 299; i++)
			player.getPackets().sendIComponentText(275, 10 + i, "");
	}

	public enum Requirement {
		
		//Bad Requirements
		DOMINION_TOWER("I must have killed any boss in the dominion tower.", 0, true, true),
		REAPER_TASK("I must have completed reaper tasks.", 0, true, true),
		VORAGO("I must have slain the ultimate vorago.", 0, true, true),

		// Untrimmed Requirements
		VOTES("I must have voted for the server multiple times.", 100, false, false),
		FIGHT_CAVES("I must have completed the fight caves minigame.", 1, false, false),
		SKILL_LEVELS("I must have the highest possible level in every skill.", 2596, false, false),
		PEST_CONTROL("I must have completed the pest control minigame.", 1, false, false),
		BARROWS("I must have looted the chest in multiple games of barrows.", 250, false, false),
		IMPLINGS("I must have looted any type of impling.", 250, false, false),
		TREASURE_HUNTER("I must have tested my luck with the treasure hunter.", 300, false, false),
		QUEEN_BLACK_DRAGON("I must have slain the queen black dragon.", 100, false, false),
		HEFIN_AGILITY("I must have completed the hefin agility course.", 200, false, false),
		PICK_POCKETING("I must have stolen from an elf without getting caught.", 200, false, false),
		CANNON_BALLS("I must have smelted cannon balls in a furnace.", 1500, false, false),
		ROCK_TAILS("I must have fished a load of rock tails.", 500, false, false),
		SUMMONING_POUCHES("I must have infused and created summoning pouches.", 1500, false, false),
		CRYSTAL_CHEST("I must have looted the crystal chest.", 50, false, false),
		CLEAN_HERBS("I must have cleaned a large amount of herbs.", 1000, false, false),
		TROLL_INVASION("I must have defeated the army in troll invasion.", 10, false, false),
		SLAYER_TASKS("I must have completed my slayer tasks.", 55, false, false),
		COOK_FISH("I must have successfully cooked any type of fish.", 2500, false, false),
		BURNING_LOGS("I must have burnt a large amount of logs.", 6000, false, false),
		OVERLOADS("I must create a handful of overloads.", 250, false, false),
		CRYSTAL_FLASK("I must have crafted a large number of crystal flasks.", 250, false, false),
		CLUE_SCROLLS("I must have solved and looted hard clue scrolls.", 100, false, false),
		HIDE_AND_SEEK("I must have spotted many penguins from hide and seek.", 50, false, false),
		GEM_CUTTING("I must have cut a large amount of diamonds.", 500, false, false),
		BOX_TRAPPING("I must have captured many grenwalls in isfador.", 300, false, false),
		ELDER_TREES("I must have chopped elder logs from a tree.", 350, false, false),
		FARMING("I must have harvested a yield from farming trees.", 200, false, false),
		
		// Trimmed Requirements
		KALPHITE_KING("I must have slain the ultimate kalphite king.", 100, true, false),
		RUNITE_MINING("I must have mined a lot of runite ore.", 500, true, false),
		ELITE_CLUE_SCROLLS("I must have solved and looted elite clue scrolls.", 100, true, false),
		FIGHT_KILN("I must have completed the fight kiln minigame.", 2, true, false),
		NEX("I must have slain the ultimate nex.", 200, true, false),
		WILDY_WYRM("I must have slain the ultimate wildy wyrm.", 150, true, false),
		TEXT_FIX("Note: Make sure this one is always last to display the last requirement.", 0, true, false);

		private String description;
		private int amount;
		private boolean trimmed;
		private boolean isBadRequirement;

		Requirement(String description, int amount, boolean trimmed, boolean isBadRequirement) {
			this.description = description;
			this.amount = amount;
			this.trimmed = trimmed;
			this.isBadRequirement = isBadRequirement;
		}
	}

}