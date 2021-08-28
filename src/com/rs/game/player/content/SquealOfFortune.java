package com.rs.game.player.content;

import java.io.Serializable;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.VarBitDefinitions;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Utils;

public class SquealOfFortune implements Serializable {
    
    private static final long serialVersionUID = -5330047553089876572L;
    
    private static final int RARITY_COMMON = 0;
    private static final int RARITY_UNCOMMON = 1;
    private static final int RARITY_RARE = 2;
    private static final int RARITY_JACKPOT = 3;
    
    private static final int SPIN_TYPE_DAILY = 0;
    private static final int SPIN_TYPE_EARNED = 1;
    private static final int SPIN_TYPE_BOUGHT = 2;
    
    private static final int SOF_STATUS_CLAIMINVOK = 1;
    private static final int SOF_STATUS_CLAIMINVBAD = 2;
    private static final int SOF_STATUS_CLAIMBANKOK = 3;
    private static final int SOF_STATUS_CLAIMBANKBAD = 4;
    private static final int SOF_STATUS_CLAIMPOUCHOK = 5;
    private static final int SOF_STATUS_CLAIMPOUCHBAD = 6;
    private static final int SOF_STATUS_DISABLED = 7;
    
    
    public static final boolean SQUEAL_OF_FORTUNE_ENABLED = true; // if not,
	// people
	// will be
	// able to
	// spin but
	// not claim
public static final String BUY_SPINS_LINK = "";

public static final double[] SOF_CHANCES = new double[] { 1.0D, 0.35D, 0.0089D, 0.0001D };

/**
 * arrays of possible rewards
 */
public static final int[] SOF_UNCOMMON_CASH_AMOUNTS = new int[] { 100000, 250000, 500000, 1000000, 5000000 };
public static final int[] SOF_RARE_CASH_AMOUNTS = new int[] { 10000000, 25000000, 50000000, 100000000, 200000000 };
public static final int[] SOF_JACKPOT_CASH_AMOUNTS = new int[] { 50 * 1000000, 50 * 1000000, 75 * 1000000, 100 * 1000000 };
public static final int[] SOF_COMMON_LAMPS = new int[] { 23713, 23717, 23721, 23725, 23729, 23737, 23733, 23741, 23745, 23749, 23753, 23757, 23761, 23765, 23769, 23778, 23774, 23786, 23782, 23794, 23790, 23802, 23798, 23810, 23806, 23814 };
public static final int[] SOF_UNCOMMON_LAMPS = new int[] { 23714, 23718, 23722, 23726, 23730, 23738, 23734, 23742, 23746, 23750, 23754, 23758, 23762, 23766, 23770, 23779, 23775, 23787, 23783, 23795, 23791, 23803, 23799, 23811, 23807, 23815 };
public static final int[] SOF_RARE_LAMPS = new int[] { 23715, 23719, 23723, 23727, 23731, 23739, 23735, 23743, 23747, 23751, 23755, 23759, 23763, 23767, 23771, 23780, 23776, 23788, 23784, 23796, 23792, 23804, 23800, 23812, 23808, 23816 };
public static final int[] SOF_JACKPOT_LAMPS = new int[] { 23716, 23720, 23724, 23728, 23732, 23740, 23736, 23744, 23748, 23752, 23756, 23760, 23764, 23768, 23773, 23781, 23777, 23789, 23785, 23797, 23793, 23805, 23801, 23813, 23809, 23817 };
public static final int[] SOF_COMMON_OTHERS = new int[] {25468, 25468,31350, 33740, 27153, 27154, 25301, 25299, 25307 };
public static final int[] SOF_UNCOMMON_OTHERS = new int[] { 29290,29312,31086,31088,29309,28024,28025,28029,25469,25469,24154, 24154, 24155, 24155, 27155, /*31773,*/ 25302, 25300, 25308, 27616, 27618, 27620, 27622, 27624, 28686, 28688, 28690, 28692, 28694, 31089, 31091, 31095, 31097, 31099 };

public static final int[] SOF_RARE_OTHERS = new int[] { 
		995, 995, 995, 995, //gold
		33522, 24431, 30920, 
		23679,23680,23681, 23682, //lucky godswords
		23691, //lucky whip
		23692, //lucky d fullhelm
		23693,//lucky dragon platebody
		23694, //lucky dragon chainbody
		23695, 23696, 23687, 23688, 23689, 23684, 23686, 23685, 23697, 23690, 23699, 23700, 23683, 23698 };


public static final int[] SOF_JACKPOT_OTHERS = new int[] { 995, 995, 995, 995, 20929, 23671, 23672, 25202 };
 /**
  * array of the cosmetic item which you can only get once
  */
	public static int[] cosmetics = new int[] {
			//kalphite helm.qbdhelm/wand
			28024,28025,28029,
			//masks
			27616, 27618, 27620, 27622, 27624, 28686, 28688, 28690, 28692, 28694, 31089, 31091, 31095, 31097, 31099,
			//lucky items
			23679,23680,23681, 23682, //lucky godswords
			23691, //lucky whip
			23692, //lucky d fullhelm
			23693,//lucky dragon platebody
			23694, //lucky dragon chainbody
			23695, 23696, 23687, 23688, 23689, 23684, 23686, 23685, 23697, 23690, 23699, 23700, 23683, 23698};
	/**
	 * player stuff
	 */
    private transient Player player;
    
    private long lastDailySpinsGiveaway;
    
    private int dailySpins;
    private int earnedSpins;
    private int boughtSpins;
    

    private int rewardSlot;
    private int jackpotSlot;
    private Item[] rewards;

    public SquealOfFortune() {
	rewardSlot = -1;
	jackpotSlot = -1;
    }
    
    public void setPlayer(Player player) {
	this.player = player;

    }
    
    public void processClick(int packetId, int interfaceId, int componentId, int e1) {
	if (interfaceId == 1139) { // squeal tab
	    if (componentId == 18)
	    //player.getPackets().sendGameMessage("The Squeal Of Fortune is currently under development");
		openSpinInterface();
	    else {
	    	player.getPackets().sendGameMessage("The Squeal Of Fortune is currently under development");
	    }
	    /*	player.getDialogueManager().startDialogue(new Dialogue() {

				@Override
				public void start() {
					sendOptionsDialogue("Quick spin options", "1x","5x", "10x", "50x","100x");
					stage = 1;
					
				}

				@Override
				public void run(int interfaceId, int componentId)  {
				if(stage == 1){
					if(componentId == OPTION_1){
						quickSpin(1);
						end();
					} else if(componentId == OPTION_2){
						quickSpin(5);
						end();
					} else if(componentId == OPTION_3){
						quickSpin(10);
						end();
					}else if(componentId == OPTION_4){
						quickSpin(50);
						end();
					}else if(componentId == OPTION_5){
						quickSpin(100);
						end();
					}
						
				}
					
				}

				@Override
				public void finish() {
					// TODO Auto-generated method stub
					
				} });
	    } */
	}
	else if (interfaceId == 1252) { // squeal overlay
	    if (componentId == 5) {
		player.getPackets().sendGameMessage("You can access the Squeal of Fortune from the side panel, and you can show the button again by logging out and back in.");
		player.getInterfaceManager().closeSquealOverlay();
	    }
	    else {
		openSpinInterface();
	    }
	}
	else if (interfaceId == 1253) { // squeal main
	    if (componentId == 106 || componentId == 258) { // hide/close button
		player.getInterfaceManager().sendWindowPane();
	    }
	    else if (componentId == 7 || componentId == 321) { // buy spins on main/reward
		player.getPackets().sendOpenURL(BUY_SPINS_LINK);
	    }
	    else if (componentId == 93 && jackpotSlot != -1 && rewardSlot == -1) { // spin button
	    player.spinsCount++;
		pickReward();
	    }
	    else if ((componentId == 192 || componentId == 239) && rewardSlot != -1) { // picking reward
		obtainReward(componentId == 239);
	    }
	    else if (componentId == 273 && jackpotSlot == -1 && getTotalSpins() > 0) { // play again
		generateRewards(getNextSpinType());
		player.getPackets().sendConfigByFile(11155, jackpotSlot + 1);
		player.getPackets().sendItems(665, rewards);
		player.getPackets().sendConfigByFile(10861, 0);
		player.getPackets().sendRunScript(5879); // sof_setupHooks();
		sendSpinCounts();
	    }
	}
    }
    
    public void processItemClick(int slotId, int itemId, Item item) {
	if (itemId == 24154 || itemId == 24155) { // spin ticket and double spin ticket
	   player.getInventory().deleteItem(item);
	   giveEarnedSpins(itemId == 24154 ? 1 : 2);
	}
    }
    /**
     * picks a rew�rd
     */
    private void pickReward() {
	if (!useSpin())
	    return;
	int rewardRarity = RARITY_COMMON;
	double roll = Utils.randomDouble();
	if (roll <= SOF_CHANCES[RARITY_JACKPOT])
	    rewardRarity = RARITY_JACKPOT; // we have a winner here...
	else if (roll <= SOF_CHANCES[RARITY_RARE])
	    rewardRarity = RARITY_RARE;
	else if (roll <= SOF_CHANCES[RARITY_UNCOMMON])
	    rewardRarity = RARITY_UNCOMMON;
	
	int[] possibleSlots = new int[13];
	int possibleSlotsCount = 0;
	for (int i = 0; i < 13; i++) {
	    if (getSlotRarity(i, jackpotSlot) == rewardRarity)
		possibleSlots[possibleSlotsCount++] = i;
	}
	
	rewardSlot = possibleSlots[Utils.random(possibleSlotsCount)];
	
	if (rewardRarity >= RARITY_RARE) {
	    announceWin();
	}
	
	player.getPackets().sendConfigByFile(10860, rewardSlot);
	player.getPackets().sendConfigByFile(10861, 1); // block spin & set reward
	player.getPackets().sendGlobalConfig(1790, getRewardStatusType());
	player.getPackets().sendGlobalConfig(1781, getBestRewardSpoofSlot());
	
    }
    /*
     * used for quickspin options.
     */
    public void quickSpin(int amount){
    	if (getTotalSpins() < amount){
    		player.sm("You don't have enough spins for this action.");
    		return;
    	}
    	for(int i =0 ; i< amount; i ++){
          	 generateRewards(getNextSpinType());
    		 pickReward();
    		 obtainReward();
    	}
    }
    /**
     * obtains the reward
     * @param discard
     */
    private void obtainReward(boolean discard) {
	int type = getRewardStatusType();
	if ((discard && type == SOF_STATUS_DISABLED) || (!discard && type != SOF_STATUS_CLAIMINVOK && type != SOF_STATUS_CLAIMPOUCHOK && type != SOF_STATUS_CLAIMBANKOK))
	    return;
	
	player.getPackets().sendConfigByFile(10861, 0); // prepare for next spin
	player.getPackets().sendGlobalConfig(1790, 0);
	player.getPackets().sendItems(665, new Item[13]);
	
	if (!discard) {
	    if (type == SOF_STATUS_CLAIMPOUCHOK) {
	    	for(int i = 0; i < cosmetics.length; i++){
		 		if(cosmetics[i] == rewards[rewardSlot].getId())
		 			player.getSofItems2().add(rewards[rewardSlot].getId());
		 	}
	    	player.getInventory().addItem(rewards[rewardSlot]);
	    }
	    else if (type == SOF_STATUS_CLAIMINVOK) {
	 	for(int i = 0; i < cosmetics.length; i++){
	 		if(cosmetics[i] == rewards[rewardSlot].getId())
	 			player.getSofItems2().add(rewards[rewardSlot].getId());
	 	}
		player.getInventory().addItem(rewards[rewardSlot]);
		player.getInventory().refresh();
	    }
	    else if (type == SOF_STATUS_CLAIMBANKOK) {
	 	   for(int i = 0; i < cosmetics.length; i++){
		 		if(cosmetics[i] == rewards[rewardSlot].getId())
		 			player.getSofItems2().add(rewards[rewardSlot].getId());
		 	}
		player.getBank().addItem(rewards[rewardSlot].getId(), rewards[rewardSlot].getAmount(),true );
	    }
	}
	
	rewards = null;
	jackpotSlot = -1;
	rewardSlot = -1;
    }
    
    private void obtainReward() {
    	for(int i = 0; i < cosmetics.length; i++){
	 		if(cosmetics[i] == rewards[rewardSlot].getId())
	 			player.getSofItems2().add(rewards[rewardSlot].getId());
	 	}
    	player.getBank().addItem(rewards[rewardSlot].getId(), rewards[rewardSlot].getAmount(),true );
    	rewards = null;
    	jackpotSlot = -1;
    	rewardSlot = -1;
        }
    
    private void announceWin() {
	Item item = rewards[rewardSlot];
	if(item.getName().contains("lamp"))
		return;
	if (item.getDefinitions().isStackable() || item.getDefinitions().isNoted() || item.getAmount() > 1) {
	    World.sendWorldMessage("<img=7><col=ff0000>News: " + player.getDisplayName() + " has just won " + item.getAmount() + " x " + item.getName() + " on Squeal of Fortune!", false, false);
	}
	else {
	    World.sendWorldMessage("<img=7><col=ff0000>News: " + player.getDisplayName() + " has just won " + item.getName() + " on Squeal of Fortune!", false, false);
	}
	
    }
    
    
    public void openSpinInterface() {
	if (player.getInterfaceManager().containsInventoryInter() || player.getInterfaceManager().containsScreenInter()) {
	    player.getPackets().sendGameMessage("Please finish what you are doing before opening Squeal of Fortune.");
	    return;
	}
	if (player.getControllerManager().getController() != null) {
	    player.getPackets().sendGameMessage("You can't open Squeal of Fortune in this area.");
	    return;
	}
	player.stopAll();
	
	sendSpinCounts();
	if (rewardSlot != -1) {
	    openExistingReward();
	}
	else if (getTotalSpins() < 1) {
	    openNoSpinsLeft();
	}
	else {
	    openSpin();
	}
    }
    
    private void openExistingReward() {
	player.getPackets().sendConfigByFile(11155, jackpotSlot + 1); // need to send all items because otherwise it will set wrong color for rarity etc
	player.getPackets().sendItems(665, rewards);
	player.getPackets().sendRootInterface(1253, 0);
	player.getPackets().sendConfigByFile(10860, rewardSlot);
	player.getPackets().sendConfigByFile(10861, 1); // block spin & set reward
	player.getPackets().sendGlobalConfig(1790, getRewardStatusType());
	player.getPackets().sendRunScript(5906); // force call to sof_displayPrize();
	
    }
    
    private void openNoSpinsLeft() {
	player.getPackets().sendConfigByFile(11155, Utils.random(13) + 1);
	player.getPackets().sendItems(665, new Item[13]);
	player.getPackets().sendRootInterface(1253, 0);
	player.getPackets().sendConfigByFile(10861, 0);
	player.getPackets().sendGlobalConfig(1790, 0);
	player.getPackets().sendRunScript(5906); // force call to sof_displayPrize();
    }
    
    private void openSpin() {
	if (rewards == null) { 
	    generateRewards(getNextSpinType());
	}
	
		player.getPackets().sendConfigByFile(11155, jackpotSlot + 1);
		player.getPackets().sendItems(665, rewards);
		player.getPackets().sendRootInterface(1253, 0);
		player.getPackets().sendConfigByFile(10861, 0); // force allow spin
    }
    
    
    public void sendSpinCounts() {
		player.getPackets().sendConfigByFile(10862, dailySpins);
		player.getPackets().sendConfigByFile(11026, earnedSpins);
		player.getPackets().sendGlobalConfig(1800, boughtSpins);
    }
    
    private void generateRewards(int spinType) {
	jackpotSlot = Utils.random(13);
	rewards = new Item[13];
	for (int i = 0; i < rewards.length; i++)
	    rewards[i] = generateReward(spinType, getSlotRarity(i, jackpotSlot));
    }
    /**
     * generats a random reward
     * @param spinType  not used
     * @param rarityType rarity of the item
     * @return
     */
    private Item generateReward(int spinType, int rarityType) {
	boolean isLamp = Utils.random(3) == 0; // lamp 1/3 others 2/3
	if (isLamp) {
	    int[] lamps = SOF_COMMON_LAMPS;
	    if (rarityType == RARITY_JACKPOT)
		lamps = SOF_JACKPOT_LAMPS;
	    else if (rarityType == RARITY_RARE)
		lamps = SOF_RARE_LAMPS;
	    else if (rarityType == RARITY_UNCOMMON)
		lamps = SOF_UNCOMMON_LAMPS;
	    
	    return new Item(lamps[Utils.random(lamps.length)], 1);
	}
	else {
		// SOF_UNCOMMON_OTHERS
	    int[] items = SOF_COMMON_OTHERS;
	    if (rarityType == RARITY_JACKPOT)
		items = SOF_JACKPOT_OTHERS;
	    else if (rarityType == RARITY_RARE)
		items = SOF_RARE_OTHERS;
	    else if (rarityType == RARITY_UNCOMMON)
		items = SOF_UNCOMMON_OTHERS;
	    
	    int itemId = items[Utils.random(items.length)];
	    int amount;	
	    //if coins
		if (itemId == 995) {
		int[] amounts = new int[250000];
		if (rarityType == RARITY_JACKPOT)
		    amounts = SOF_JACKPOT_CASH_AMOUNTS;
		else if (rarityType == RARITY_RARE)
		    amounts = SOF_RARE_CASH_AMOUNTS;
		else if (rarityType == RARITY_UNCOMMON)
		    amounts = SOF_UNCOMMON_CASH_AMOUNTS;
		amount = amounts[Utils.random(amounts.length)];
	    }  else {
	    for(int i = 0; i < cosmetics.length; i ++){
	    	if(cosmetics[i] == itemId && player.getSofItems2().contains(itemId)){
	    		while(player.getSofItems2().contains(itemId)){
	    			itemId = items[Utils.random(items.length)];
	    		}
	    	}
	    		
	    }
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		amount = rarityType > RARITY_COMMON 
				|| (!defs.isStackable() && !defs.isNoted()) ? 1 :Utils.random(10,100);
	    }
	    
	    return new Item(itemId, amount);
	}
    }
    
    
    private int getRewardStatusType() {
	if (!SQUEAL_OF_FORTUNE_ENABLED)
	    return SOF_STATUS_DISABLED;
	
	Item reward = rewards[rewardSlot]; 
	if (!reward.getDefinitions().isStackable() && !reward.getDefinitions().isNoted() && reward.getAmount() == 1) { // non stackable items to inv
	    return player.getInventory().hasFreeSlots() ? SOF_STATUS_CLAIMINVOK : SOF_STATUS_CLAIMINVBAD;
	}
	else { // other items go to bank
	    if (player.getBank().getItem(reward.getId()) != null) {
		long amt = player.getBank().getItem(reward.getId()).getAmount() + reward.getAmount();
		return amt > Integer.MAX_VALUE || amt <= 0 ? SOF_STATUS_CLAIMBANKBAD : SOF_STATUS_CLAIMBANKOK;
	    }
	    else
		return player.getBank().hasBankSpace() ? SOF_STATUS_CLAIMBANKOK : SOF_STATUS_CLAIMBANKBAD;
	}
    }
    
    private int getBestRewardSpoofSlot() {
	int wonRarity = getSlotRarity(rewardSlot, jackpotSlot);
	if (wonRarity == RARITY_JACKPOT)
	    return rewardSlot; // nothing to spoof
	int spoofMinType = wonRarity == RARITY_RARE ? RARITY_JACKPOT : RARITY_RARE;
	int bestSlot = -1;
	int bestDistance = Integer.MAX_VALUE;
	for (int i = 0; i < 13; i++) {
	    if (i == rewardSlot || getSlotRarity(i, jackpotSlot) < spoofMinType)
		continue; // skip self & not rare
	    int distance = distanceTo(i, rewardSlot);
	    if (bestSlot == -1 || distance < bestDistance) {
		bestSlot = i;
		bestDistance = distance;
	    }
	}
	
	return bestSlot;
    }
    
    private int distanceTo(int from, int to) {
	if (from == to)
	    return 0;
	else if (from > to)
	    return (13 - from) + to;
	else // (from < to)
	    return (to - from);
    }
    
    private int getSlotRarity(int slot, int jackpotSlot) {
	if (slot == jackpotSlot) // jackpot overrides the slot
	    return RARITY_JACKPOT;
	switch (slot) {
	    case 1:
	    case 3:
	    case 5:
	    case 7:
	    case 10:
	    case 12:
		return RARITY_COMMON;
	    case 2:
	    case 6:
	    case 9:
	    case 11:
		return RARITY_UNCOMMON;
	    case 0:
	    case 4:
	    case 8:
		return RARITY_RARE;
	    default: // default case added so compiler can add tableswitch instruction instead of lookupswitch
		throw new RuntimeException("Bad slot");
	}
    }
    
    public int getTotalSpins() {
	return dailySpins + earnedSpins + boughtSpins;
    }
    
    private int getNextSpinType() {
	if (dailySpins > 0)
	    return SPIN_TYPE_DAILY;
	else if (earnedSpins > 0)
	    return SPIN_TYPE_EARNED;
	else if (boughtSpins > 0)
	    return SPIN_TYPE_BOUGHT;
	else
	    return -1;
    }
    
    private boolean useSpin() {
	int type = getNextSpinType();
	if (type == -1)
	    return false;
	
	if (type == SPIN_TYPE_DAILY)
	    setDailySpins(dailySpins - 1);
	else if (type == SPIN_TYPE_EARNED)
	    setEarnedSpins(earnedSpins - 1);
	else if (type == SPIN_TYPE_BOUGHT)
	    setBoughtSpins(boughtSpins - 1);
	
	return true;
    }
    
    /**
     * Give's daily spins for donators, 
     * only if daily spins < 2.
     */
    public void giveDailySpins() {
	if ((Utils.currentTimeMillis() - lastDailySpinsGiveaway) < (12 * 60 * 60 * 1000)) // 12 hours
	    return;
	lastDailySpinsGiveaway = Utils.currentTimeMillis();
	int previous = dailySpins;
	dailySpins = 2;
	if(player.isDonator()){
		dailySpins++;
	} if(player.isExtremeDonator()){
		dailySpins++;
	}
	
	if (dailySpins > previous) {
		player.getPackets().sendGameMessage("<col=FF0000>You have been awarded " + (dailySpins - previous) + " squeal of fortune spins.");
	    player.getPackets().sendGameMessage("<col=FF0000>Click on the squeal of fortune tab to use them.");
	}
	
	if (dailySpins != previous);
    }
    
    public void giveEarnedSpins(int amount) {
	int previous = earnedSpins;
	earnedSpins += amount;
	if (earnedSpins > previous) {
	    player.getPackets().sendGameMessage("<col=FF0000>You have earned " + (earnedSpins - previous) + " squeal of fortune spins.");
	    player.getPackets().sendGameMessage("<col=FF0000>Click on the squeal of fortune tab to use them.");
	}
	
	if (earnedSpins != previous)
	    sendSpinCounts();
    }
    
    public void giveBoughtSpins(int amount) {
	int previous = boughtSpins;
	boughtSpins += amount;
	if (boughtSpins > previous) {
	    player.getPackets().sendGameMessage("<col=FF0000>You have bought " + (boughtSpins - previous) + " squeal of fortune spins.");
	    player.getPackets().sendGameMessage("<col=FF0000>Click on the squeal of fortune tab to use them.");
	}
	
	if (boughtSpins != previous)
	    sendSpinCounts();
    }
    
    public void resetSpins() {
	dailySpins = 0;
	earnedSpins = 0;
	boughtSpins = 0;
	sendSpinCounts();
    }
    

    public int getDailySpins() {
	return dailySpins;
    }

    public void setDailySpins(int dailySpins) {
	int previous = this.dailySpins;
	this.dailySpins = dailySpins;
	if (this.dailySpins != previous)
	    sendSpinCounts();
    }

    public int getEarnedSpins() {
	return earnedSpins;
    }

    public void setEarnedSpins(int earnedSpins) {
	int previous = this.earnedSpins;
	this.earnedSpins = earnedSpins;
	if (this.earnedSpins != previous)
	    sendSpinCounts();
    }

    public int getBoughtSpins() {
	return boughtSpins;
    }

    public void setBoughtSpins(int boughtSpins) {
	int previous = this.boughtSpins;
	this.boughtSpins = boughtSpins;
	if (this.boughtSpins != previous)
	    sendSpinCounts();
    }

}