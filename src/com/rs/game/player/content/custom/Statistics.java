package com.rs.game.player.content.custom;

import java.io.Serializable;

import com.rs.game.player.PlayTime;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Statistics implements Serializable {

	private static final long serialVersionUID = 4060924202351034976L;
	
	private transient Player player;
	
	public void setPlayer(Player player) {
		
	}
	
	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

	private int onlineTime;
	
	public transient PlayTime Playtime;

	public PlayTime getPlayTime() {
		return Playtime;
	}
	
	public void setPlayPoints(int Playpoints) {
		this.Playpoints = Playpoints;
	}
	
	private int Playpoints;

	public int getPlayPoints() {
		return Playpoints;
	}
	
	private int oresMined, barsSmelt;

	public void addOresMined() {
		oresMined++;
	}

	public int getOresMined() {
		return oresMined;
	}

	public void addBarsSmelt() {
		barsSmelt++;
	}

	public int getBarsSmelt() {
		return barsSmelt;
	}

	private int logsChopped, logsBurned;

	public void addLogsChopped() {
		logsChopped++;
	}

	public int getLogsChopped() {
		return logsChopped;
	}

	public void addLogsBurned() {
		logsBurned++;
	}

	public int getLogsBurned() {
		return logsBurned;
	}

	private int lapsRan;

	public void addLapsRan() {
		lapsRan++;
	}

	private int gnomeLapsRan;

	public void addGnomeLapsRan() {
		gnomeLapsRan++;
	}

	public int getGnomeLapsRan() {
		return gnomeLapsRan;
	}

	public int getLapsRan() {
		return lapsRan;
	}

	private int bonesOffered;

	public void addBonesOffered() {
		bonesOffered++;
	}

	public int getBonesOffered() {
		return bonesOffered;
	}

	private int potionsMade;

	public void addPotionsMade() {
		potionsMade++;
	}

	public int getPotionsMade() {
		return potionsMade;
	}

	private int timesStolen;

	public void addTimesStolen() {
		timesStolen++;
	}

	public int getTimesStolen() {
		return timesStolen;
	}

	private int itemsMade;

	public void addItemsMade() {
		itemsMade++;
	}

	public int getItemsMade() {
		return itemsMade;
	}

	private int itemsFletched;

	public void addItemsFletched() {
		itemsFletched++;
	}

	public int getItemsFletched() {
		return itemsFletched;
	}

	private int creaturesCaught;

	public void addCreaturesCaught() {
		creaturesCaught++;
	}

	public int getCreaturesCaught() {
		return creaturesCaught;
	}

	private int fishCaught;

	public void addFishCaught(boolean perk) {
		fishCaught += perk ? 2 : 1;
	}

	public int getFishCaught() {
		return fishCaught;
	}

	private int foodCooked;

	public void addFoodCooked() {
		foodCooked++;
	}

	public int getFoodCooked() {
		return foodCooked;
	}

	public int produceGathered;

	public void addProduceGathered() {
		produceGathered++;
	}

	public int getProduceGathered() {
		return produceGathered;
	}

	public int pouchesMade;

	public void setPouchesMade(int pouches) {
		pouchesMade = pouches;
	}

	public int getPouchesMade() {
		return pouchesMade;
	}

	private int memoriesCollected;

	public int getMemoriesCollected() {
		return memoriesCollected;
	}

	public void addMemoriesCollected() {
		memoriesCollected++;
	}
	
	private int runesMade;
	
	public int getRunesMade() {
		return runesMade;
	}

	public void addRunesMade(int runes) {
		runesMade += runes;
	}
	
	private byte serenStonesMined;

	public void addSerenStonesMined() {
		serenStonesMined++;
	}

	public byte getSerenStonesMined() {
		return serenStonesMined;
	}

	/**
	 * Shooting Stars
	 */

	private boolean foundShootingStar;
	private long lastStarSprite;
	private int starsFound;

	public long getLastStarSprite() {
		return lastStarSprite;
	}

	public void setLastStarSprite(long lastStarSprite) {
		this.lastStarSprite = lastStarSprite;
	}

	public boolean isFoundShootingStar() {
		return foundShootingStar;
	}

	public void setFoundShootingStar() {
		foundShootingStar = true;
	}

	public int getStarsFound() {
		return starsFound;
	}

	public void incrementStarsFound() {
		starsFound++;
	}

	/**
	 * RuneCrafted runes; for staves/omni-staff.
	 */

	private int air, mind, water, earth, fire, body, cosmic, chaos, nature, law, death, blood;
	
	public void addAirRunesMade(int amount) {
		air += amount;
	}

	public void addWeaponKill(int amount) {
		air += amount;
	}

	public void addMindRunesMade(int amount) {
		mind += amount;
	}

	public void addWaterRunesMade(int amount) {
		water += amount;
	}

	public void addEarthRunesMade(int amount) {
		earth += amount;
	}

	public void addFireRunesMade(int amount) {
		fire += amount;
	}

	public void addBodyRunesMade(int amount) {
		body += amount;
	}

	public void addCosmicRunesMade(int amount) {
		cosmic += amount;
	}

	public void addChaosRunesMade(int amount) {
		chaos += amount;
	}

	public void addNatureRunesMade(int amount) {
		nature += amount;
	}

	public void addLawRunesMade(int amount) {
		law += amount;
	}

	public void addDeathRunesMade(int amount) {
		death += amount;
	}

	public void addBloodRunesMade(int amount) {
		blood += amount;
	}

	public int getAirRunesMade() {
		return air;
	}

	public int getMindRunesMade() {
		return mind;
	}

	public int getWaterRunesMade() {
		return water;
	}

	public int getEarthRunesMade() {
		return earth;
	}

	public int getFireRunesMade() {
		return fire;
	}

	public int getBodyRunesMade() {
		return body;
	}

	public int getCosmicRunesMade() {
		return cosmic;
	}

	public int getChaosRunesMade() {
		return chaos;
	}

	public int getNatureRunesMade() {
		return nature;
	}

	public int getLawRunesMade() {
		return law;
	}

	public int getDeathRunesMade() {
		return death;
	}

	public int getBloodRunesMade() {
		return blood;
	}

	/* Barrows */
	private int barrowsRunsDone;
	
	public int getBarrowsRunsDone() {
		return barrowsRunsDone;
	}

	public void incrementBarrowsRunsDone() {
		barrowsRunsDone++;
	}
	
	/* Mask of Mourning */
	private int bansheeKills;
	
	public int getBansheeKills() {
		return bansheeKills;
	}

	public void increaseBansheeKills() {
		bansheeKills++;
	}
	
	/* Mask of Broken Fingers */
	private int crawlingHandKills;
	
	public int getCrawlingHandKills() {
		return crawlingHandKills;
	}

	public void increaseCrawlingHandKills() {
		crawlingHandKills++;
	}
	
	/* Mask of Granite */
	private int gargoyleKills;
	
	public int getGargoyleKills() {
		return gargoyleKills;
	}

	public void increaseGargoyleKills() {
		gargoyleKills++;
	}
	
	/* Mask of Reflection */
	private int basiliskKills;
	
	public int getBasiliskKills() {
		return basiliskKills;
	}

	public void increaseBasiliskKills() {
		basiliskKills++;
	}

	/* Clue Scrolls */
	private int completedClues;

	public int getCompletedClues() {
		return completedClues;
	}

	public void incrementCompletedClues() {
		completedClues++;
	}
	
	/* Hefin Laps */
	private short hefinLaps;

	public void addHefinLaps() {
		hefinLaps++;
	}

	public short getHefinLaps() {
		return hefinLaps;
	}
	
	/* Voting */
	private int votes;
	
	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	private int votePoints;

	public int getVotePoints() {
		return votePoints;
	}

	private int VotedTimes;

	public int getVotedTimes() {
		return VotedTimes;
	}

	public void setVotePoints(int votePoints) {
		this.votePoints = votePoints;
	}

	public void setVotedTimes(int VotedTimes) {
		this.VotedTimes = VotedTimes;
	}
	
	/* Trivia Bot */
	public int triviaPoints = 0;
	
	/* Loyalty Points */
	private int loyaltyPoints;

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(int lps) {
		loyaltyPoints = lps;
	}
	
	/* Black Marks */
	public int blackMarks;

	public void setBlackMarks(int blackMarks) {
		this.blackMarks = blackMarks;
	}

	public void addBlackMarks(int blackMarks) {
		this.blackMarks += blackMarks;
	}

	public void takeBlackMarks(int blackMarks) {
		this.blackMarks -= blackMarks;
	}

	public int getBlackMarks() {
		return blackMarks;
	}

	/* Achievement Points */
	public int achievementPoints = 0;
	
	public void setAchievementPoints(int achievementPoints) {
		this.achievementPoints = achievementPoints;
	}
	
	public int getAchievementPoints() {
		return achievementPoints;
	}
	
	/* Rune Coins */
	private int runeCoin;

	public int getRuneCoins() {
		return runeCoin;
	}

	public void setRuneCoins(int amount) {
		this.runeCoin = amount;
	}

	public void addRuneCoins(int amount) {
		this.runeCoin += amount;
	}
	
	/* Tutorial Stage */
	public int tutorialStage;
	
	public int getTutorialStage() {
		return tutorialStage;
	}
	
	/* Dungeoneering Tokens */
	public int dungeoneeringTokens;
	private int AssassinTokens;

	public int getDungeoneeringTokens() {
		return dungeoneeringTokens;
	}

	public void setDungeoneeringTokens(int DungeoneeringTokens) {
		this.dungeoneeringTokens = DungeoneeringTokens;
	}

	public int getAssassinTokens() {
		return AssassinTokens;
	}

	public void setAssassinTokens(int AssassinTokens) {
		this.AssassinTokens = AssassinTokens;
	}
	
	/* Sophanem Corruption */
	private int corruption;
	
	public void setCorruption(int corruption) {
		this.corruption = corruption;
	}

	public int getCorruption() {
		return this.corruption;
	}
	
	/* Donation Points */
	private int donationPoints;

	public int getDonationPoints() {
		return donationPoints;
	}

	public void setDonationPoints(int DonorPoints) {
		this.donationPoints = DonorPoints;
	}

	private boolean skippedTutorial = false;
	
	public void skippedTutorial(boolean skippedTutorial) {
		this.skippedTutorial = skippedTutorial;
	}
	
	public boolean skippedTutorial() {
		return skippedTutorial;
	}

	public void unlockFerociousRingUpgrade(boolean ferociousUpgrade) {
		player.ferociousUpgrade = ferociousUpgrade;
	}

	public boolean hasUnlockedFerociousRingUpgrade() {
		return player.ferociousUpgrade;
	}
	
	private int gamePoints;
	
	public int getGamePoints() {
		return gamePoints;
	}

	public void increaseGamePoints(int amount) {
		this.gamePoints += amount;
	}
}
