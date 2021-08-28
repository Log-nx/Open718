package com.rs.game.player.content;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.javacord.api.entity.message.MessageBuilder;

import com.rs.GameServer;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.managers.QuestManager.Quests;
import com.rs.utils.ItemExamines;
import com.rs.utils.ItemSetsKeyGenerator;
import com.rs.utils.Utils;

public class Shop {

	private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator
			.generateKey();

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995;

	private String name;
	private Item[] mainStock;
	private int[] defaultQuantity;
	private Item[] generalStock;
	private int money;
	private int amount;

	private CopyOnWriteArrayList<Player> viewingPlayers;

	public static int[][] loyaltyPrices = { { 20958, 5000 }, { 22268, 9000 },
			{ 20962, 5000 }, { 22270, 10000 }, { 20967, 5000 },
			{ 22272, 8000 }, { 22280, 5000 }, { 22282, 9000 }, { 22284, 5000 },
			{ 22286, 8000 }, { 20966, 5000 }, { 22274, 10000 },
			{ 20965, 5000 }, { 22276, 8000 }, { 22288, 5000 }, { 22290, 8000 },
			{ 22292, 5000 }, { 22294, 10000 }, { 22300, 7000 },
			{ 22296, 5000 }, { 22298, 10000 }, { 22302, 9000 },
			{ 22905, 13000 }, { 23874, 20000 }, { 23848, 13000 },
			{ 23854, 20000 }, { 22899, 13000 }, { 23876, 20000 } };

	public static int[][] votePrices = { { 24433, 50 }, { 23674, 60 }, { 20929, 40 }, { 15098, 18 }, { 4084, 30 }, { 18744, 15}, { 18745, 15 }, { 18746, 15 }, { 13101, 5 }, { 28006, 20 }, { 25322, 13 } };
	
	public static int[][] dominionTower = { { 22358, 300000 },
			{ 22359, 300000 }, { 22360, 300000 }, { 22361, 300000 },
			{ 22362, 300000 }, { 22363, 300000 }, { 22364, 300000 },
			{ 22365, 300000 }, { 22366, 300000 }, { 22367, 300000 },
			{ 22368, 300000 }, { 22369, 300000 } };
	
	public static int[][] skillShop = { { 13661, 500 }, { 10933, 75 }, { 10939, 175 }, { 10940, 150 }, { 10941, 100 }, { 15402, 650 } };
	
	public static int[][] pkShop = { { 15614, 250 }, { 15612, 250 },
			{ 15616, 250 }, { 15602, 250 }, { 15600, 250 }, { 15604, 250 },
			{ 15608, 250 }, { 15606, 250 }, { 15610, 250 }, { 19784, 1850 },
			{ 15220, 400 }, { 15019, 400 }, { 15018, 400 }, { 13920, 150 },
			{ 13917, 150 }, { 13911, 150 }, { 13908, 150 }, { 13914, 150 },
			{ 29949, 2500 } };
	
	public static int[][] easterShop = { { 7927, 5 }, { 5609, 1 },
			{ 11019, 2 }, { 11020, 2 }, { 11021, 2 }, { 11022, 2 } };
	
	public static int[][] AchievementPrices
	= { {10933,75}, {24431, 1500}, {10939, 200}, {10940, 200}, {10941,200},{24427,200}, {24428, 200}, {24429,200}, {24430, 200}, {20789, 200},{20791,200},{20790,200},{20787, 200},{20788, 200} };

	public static int[][] donorShop = { {26579, 1500}, {26584, 1000}, 
										{26587, 1500}, {26591, 1000},
										{26595, 1500}, {26599, 1000}, {18768, 500},};
	
	public static int[][] dungPrices = { 
		{ 18349, 220000 }, { 25991, 175000 },
	    { 18351, 220000 }, { 25993, 175000 }, 
	    { 18357, 220000 },{ 25995, 175000 }, 
	    { 18353, 220000 }, { 18355, 220000 },
	    { 18359, 20000 }, { 18361, 165000 }, { 18363, 165000 } };
	
	public static int[][] pvmShop = { { 10548, 1000 }, { 10551, 1500 }, { 19712, 3500 }, { 2572, 5000 }, 
			{ 11716, 20000 }, { 22348, 30000 }, { 11716, 750 }, { 17291, 2750 }, { 21369, 1000 },{ 12929, 350 }  };
	
	public static int loyaltyShop = 0;
	public static int voteShop = 0;
	public static int dominionShop = 0;
	public static int pvmShop1 = 0;
	public static int skillShop1 = 0;
	public static int AchievementShop = 2;
	public static int donorShop1 = 0;
	public static int pkShop1 = 0;
	public static int easterShop1 = 0;
    public static int dungShop = 0;
    
	public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.name = name;
		this.money = money;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = mainStock[i].getAmount();
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null)
					continue;
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				viewingPlayers.remove(player);
				player.getTemporaryAttributtes().remove("Shop");
			}
		});
		player.getPackets().sendConfig(118, MAIN_STOCK_ITEMS_KEY);
		sendStore(player);
		player.getInterfaceManager().sendInterface(1265);
		player.getPackets().sendGlobalConfig(1876, -1);
		player.getPackets().sendConfig(1496, -1);
		player.getPackets().sendConfig(532, 995);
		player.getPackets().sendIComponentSettings(1265, 20, 0, getStoreSize() * 6, 1150);
		player.getPackets().sendIComponentSettings(1265, 26, 0, getStoreSize() * 6, 82903066);
		player.getPackets().sendIComponentText(1265, 85, name);
		if (isGeneralStore())
			player.getPackets().sendHideIComponent(1265, 52, false);
		sendInventory(player);
		sendExtraConfigs(player);
	}

	public void buy(Player player, int slotId, int quantity) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage("There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		if (price <= 0) {
			player.getPackets().sendGameMessage("It appears that the item has no shop value.");
			return;
		}
		HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
		int amountCoins = player.getInventory().getItems().getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();
		int inPouch = player.getMoneyPouch().getCoinsAmount();
		int maxPouch = inPouch / price;
		boolean hasRequiriments = true;
		boolean enoughCoins = maxQuantity >= buyQ;
		if (item.getName().contains("cape (t)") || item.getName().contains(" master cape") || item.getName().equalsIgnoreCase("Dungeoneering cape")) {
			int skill = 0, level = 99;
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				level = requiriments.get(skillId);
				skill = skillId;
				if (player.getSkills().getLevelForXp(skillId) < requiriments
						.get(skillId)) {
					hasRequiriments = false;
				}
			}
			if (!hasRequiriments) {
				player.getPackets().sendGameMessage("You need " + player.getSkills().getSkillName(skill) + " of " + level + " to buy  " + item.getName() + ".");
				return;
			}
		}
		if (item.getName().equalsIgnoreCase("blue cape") || item.getName().equalsIgnoreCase("red cape")) {
			if (!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
				player.getPackets().sendGameMessage("You need to complete Nomad's Requiem to buy a " + item.getName() + ".");
				return;
			}
		}
		if (money != 995) {
			for (int i11 = 0; i11 < loyaltyPrices.length; i11++) {
				loyaltyShop = 1;
				if (item.getId() == loyaltyPrices[i11][0]) {
					if (player.getStatistics().getLoyaltyPoints() < loyaltyPrices[i11][1] * quantity) {
						player.getPackets().sendGameMessage("You need " + loyaltyPrices[i11][1] + " Loyalty points to buy this item.");
						return;
					} else
						loyaltyShop = 1;
					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the loyalty store.");
					player.getInventory().addItem(loyaltyPrices[i11][0], 1);
					player.getStatistics().setLoyaltyPoints(player.getStatistics().getLoyaltyPoints() - loyaltyPrices[i11][1]);
					return;
				}
			}
			for (int i11 = 0; i11 < AchievementPrices.length; i11++) {
				AchievementShop = 2;
			if (item.getId() == AchievementPrices[i11][0]) {
				if (player.getStatistics().getAchievementPoints() < AchievementPrices[i11][1] * quantity) {
				player.getPackets().sendGameMessage("You need " + AchievementPrices[i11][1] + " Achievement Points to buy this item!");
				return;
			} else
				AchievementShop = 2;
				player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the Achievement store.");
				player.getInventory().addItem(AchievementPrices[i11][0], 1);
				player.getStatistics().setAchievementPoints(player.getStatistics().getAchievementPoints() - AchievementPrices[i11][1]);
				return;
		    }
    }
			for (int i11 = 0; i11 < votePrices.length; i11++) {
				voteShop = 1;
				if (item.getId() == votePrices[i11][0]) {
					if (player.getStatistics().getVotePoints() < votePrices[i11][1] * quantity) {
						player.getPackets().sendGameMessage("You need " + votePrices[i11][1] + " Vote Points to buy this item.");
						return;
					} else
						voteShop = 1;
					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the vote store.");
					player.getInventory().addItem(votePrices[i11][0], 1);
					player.getStatistics().setVotePoints(player.getStatistics().getVotePoints() - votePrices[i11][1]);
					return;
				}
			}
			 for (int i11 = 0; i11 < dungPrices.length; i11++) {
					dungShop = 1;
					if (item.getId() == dungPrices[i11][0]) {
					    if (player.getStatistics().getDungeoneeringTokens() < dungPrices[i11][1] * quantity) {
						player.getPackets().sendGameMessage("You need " + dungPrices[i11][1] + " dungeoneering tokens to buy this item.");
						return;
					    } else
						dungShop = 1;
					    player.getPackets().sendGameMessage( "You have bought a "  + item.getDefinitions().getName()  + " from the dungeoneering store.");
					    player.getInventory().addItem(dungPrices[i11][0], 1);
					    player.getStatistics().setDungeoneeringTokens(player.getStatistics().getDungeoneeringTokens()  - dungPrices[i11][1]);
					    return;
					}
				   }
			for (int i11 = 0; i11 < dominionTower.length; i11++) {
				dominionShop = 1;
				if (item.getId() == dominionTower[i11][0]) {
					if (player.getDominionTower().getDominionFactor() < dominionTower[i11][1] * quantity) {
						player.getPackets().sendGameMessage("You need " + dominionTower[i11][1] + " Dominion Factor to buy this item.");
						return;
					} else
						dominionShop = 1;
					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the Dominion tower Shop.");
					player.getInventory().addItem(dominionTower[i11][0], 1);
					player.getDominionTower().setDominionFactor(player.getDominionTower().getDominionFactor() - dominionTower[i11][1]);
					return;
				}
			}
			for (int i11 = 0; i11 < pvmShop.length; i11++) {
				pvmShop1 = 1;
				if (item.getId() == pvmShop[i11][0]) {
					if (player.getPvmPoints() < pvmShop[i11][1] * quantity) {
						player.getPackets().sendGameMessage("You need " + pvmShop[i11][1] + " Pvm Points to buy this item.");
						return;
					} else
						pvmShop1 = 1;
					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the Pvm Shop.");
					player.getInventory().addItem(pvmShop[i11][0], 1);
					player.setPvmPoints(player.getPvmPoints() - pvmShop[i11][1]);
					return;
				}
			}
			for (int i11 = 0; i11 < skillShop.length; i11++) {
				skillShop1 = 1;
				if (item.getId() == skillShop[i11][0]) {
					if (player.getSkillPoints() < skillShop[i11][1] * quantity) {
						player.getPackets().sendGameMessage("You need " + skillShop[i11][1] + " Skill Points to buy this item.");
						return;
					} else
						skillShop1 = 1;
					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the Skill Points Shop.");
					player.getInventory().addItem(skillShop[i11][0], 1);
					player.setSkillPoints(player.getSkillPoints() - skillShop[i11][1]);
					return;
				}
			}
			for (int i11 = 0; i11 < donorShop.length; i11++) {
				donorShop1 = 1;
				if (item.getId() == donorShop[i11][0]) {
					if (player.getStatistics().getDonationPoints() < donorShop[i11][1] * quantity) {
						player.getPackets().sendGameMessage("You need " + donorShop[i11][1] + " donation credits to buy this item.");
						return;
					} else
						donorShop1 = 1;
					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the donation credit Store.");
					player.getInventory().addItem(donorShop[i11][0], 1);
					player.getStatistics().setDonationPoints(player.getStatistics().getDonationPoints() - donorShop[i11][1]);
					return;
				}
			}
		}
		boolean enoughInPouch = maxPouch >= buyQ;
		if (!enoughCoins && !enoughInPouch) {
			player.getPackets().sendGameMessage("You don't have enough coins.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.getPackets().sendGameMessage(
					"The shop has run out of stock.");
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			if (amountCoins + price > 0) {
				if (money == 995) {
					if (player.getInventory().getCoinsAmount() >= totalPrice) {
						player.getInventory().deleteItem(money, totalPrice);
						player.getInventory().addItem(item.getId(), buyQ);
					} else {
						player.getPackets().sendGameMessage("You can't afford to buy that many.");
						return;
					}
				} else if (money != 995) {
					if (player.getInventory().containsItem(money, totalPrice)) {
						player.getInventory().deleteItem(money, totalPrice);
						player.getInventory().addItem(item.getId(), buyQ);
					}
				}
			}
			item.setAmount(item.getAmount() - buyQ);
			if (item.getAmount() <= 0 && slotId >= mainStock.length)
				generalStock[slotId - mainStock.length] = null;
			refreshShop();
			sendInventory(player);
			new MessageBuilder().append(player.getDisplayName() + " has bought x" + buyQ + " " + ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase() + "'s for " + Utils.formatNumber(price * quantity) + " coins").send(GameServer.getDiscordBot().getAPI().getTextChannelById("573454147433529345").get());
		}
	}

	public int getAmount() {
		return this.amount;
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++)
			if (mainStock[i].getId() == itemId)
				return defaultQuantity[i];
		return -1;
	}

	public Item[] getMainStock() {
		return this.mainStock;
	}

	public int getStoreSize() {
		return mainStock.length
				+ (generalStock != null ? generalStock.length : 0);
	}

	/**
	 * Checks if the player is buying an item or selling it.
	 * 
	 * @param player
	 *            The player
	 * @param slotId
	 *            The slot id
	 * @param amount
	 *            The amount
	 */
	public void handleShop(Player player, int slotId, int amount) {
		boolean isBuying = player.getTemporaryAttributtes().get("shop_buying") != null;
		if (isBuying)
			buy(player, slotId, amount);
		else
			sell(player, slotId, amount);
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0,
					getStoreSize() * 6, 1150);
		}
	}

	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null)
					continue;
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0)
					generalStock[i] = null;
				needRefresh = true;
			}
		}
		if (needRefresh)
			refreshShop();
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		int originalId = item.getId();
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (item.getDefinitions().isDestroyItem()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1
				|| !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		if (item.getId() == 1038 ||item.getId() == 1040 ||item.getId() == 1042 ||item.getId() == 1044
				||item.getId() == 1046 ||item.getId() == 1048
			||item.getId() == 1053 ||item.getId() == 1055 ||item.getId() == 1057 || item.getId() == 1050
			|| item.getId() == 26579 || item.getId() == 26584 || item.getId() == 26587
			|| item.getId() == 26591 || item.getId() == 26595 || item.getId() == 26599 || item.getId() == 18768)
			{
				player.getPackets().sendGameMessage("This item can't be sold here.");
		return;
	}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage("You can't sell this item to this shop.");
			return;
		}
		if (item.getId() == 1464)
		quantity = 10;
		int price = getSellPrice(item, dq);
		int numberOff = player.getInventory().getItems()
				.getNumberOf(originalId);
		if (quantity > numberOff)
			quantity = numberOff;
		if (numberOff + quantity >= Integer.MAX_VALUE
				|| numberOff + quantity < 0) {
			player.getPackets().sendGameMessage("The shop would go over MAX if you sold all these items.");
			return;
		}
		int numberOffCash = player.getInventory().getItems().getNumberOf(995);
		if (numberOffCash + price * quantity < 0) {
			player.getPackets().sendGameMessage("Your inventory and money pouch can't hold more coins.");
			return;
		}
		if (!addItem(item.getId(), quantity)) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		player.getInventory().deleteItem(originalId, quantity);
		player.getMoneyPouch().sendDynamicInteraction(price * quantity, false);
		refreshShop();
		new MessageBuilder().append(player.getDisplayName() + " has sold x" + quantity + " " + ItemDefinitions.getItemDefinitions(originalId).getName().toLowerCase() + "'s for " + price * quantity + " coins").send(GameServer.getDiscordBot().getAPI().getTextChannelById("573454147433529345").get());
	}

	public void sendExamine(Player player, int slotId) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	 public int getBuyPrice(Item item, int dq) { //TODO
	    	switch (item.getId()) {
	    	case 15707:
			   	item.getDefinitions().setValue(1);
				break;
	    	case 1243:
			   	item.getDefinitions().setValue(4000);
				break;
	    	case 1245:
			   	item.getDefinitions().setValue(11000);
				break;
	    	case 1247:
			   	item.getDefinitions().setValue(17000);
				break;
	    	case 10933:
	    	case 10939:
	    	case 10940:
	    	case 10941:
	    	case 24427:
	    	case 24428:
	    	case 24429:
	    	case 24430:
	    	case 20789:
	    	case 20791:
	        case 20790:
	        case 20787:
	        case 20788:
			   	item.getDefinitions().setValue(200);
				break;
		   case 11814:
			   	item.getDefinitions().setValue(1000);
				break;
	        case 11818:
			   	item.getDefinitions().setValue(2500);
				break;
	        case 11822:
			   	item.getDefinitions().setValue(5000);
				break;
	        case 11826:
			   	item.getDefinitions().setValue(7500);
				break;
	        case 11830:
			   	item.getDefinitions().setValue(10000);
				break;
	        case 11834:
			   	item.getDefinitions().setValue(25000);
				break;
	        case 11838:
			   	item.getDefinitions().setValue(225000);
				break;
	        case 14527:
			   	item.getDefinitions().setValue(150000);
				break;
				
	        case 11864:
	        	item.getDefinitions().setValue(20000);
				break;
	        case 11866:
	        	item.getDefinitions().setValue(35000);
				break;
	        case 11868:
	        	item.getDefinitions().setValue(50000);
				break;
	        case 11870:
	        	item.getDefinitions().setValue(75000);
				break;
	        case 14684:
	        	item.getDefinitions().setValue(35000);
				break;
	        case 11876:
	        case 11872:
	        case 11960:
	        case 11962:
	        	item.getDefinitions().setValue(100000);
				break;
	    	}
			return item.getDefinitions().getValue();
		}
	 public int getSellPrice(Item item, int dq) {
			switch (item.getId()) {
			case 1243:
			   	item.getDefinitions().setValue(4000);
				break;
	    	case 1245:
			   	item.getDefinitions().setValue(11000);
				break;
	    	case 1247:
			   	item.getDefinitions().setValue(17000);
				break;
		}
			return item.getDefinitions().getValue() / 2;
		}
	public void sendExtraConfigs(Player player) {
		player.getPackets().sendConfig(2561, -1);
		player.getPackets().sendConfig(2562, -1);
		player.getPackets().sendConfig(2563, -1);
		player.getPackets().sendConfig(2565, 0);
		player.getTemporaryAttributtes().put("shop_buying", true);
		setAmount(player, 1);
	}

	public void sendInfo(Player player, int slotId, boolean isBuying) {
		if (isBuying) {
			if (slotId >= getStoreSize())
				return;
			Item item = slotId >= mainStock.length ? generalStock[slotId
					- mainStock.length] : mainStock[slotId];
			if (item == null)
				return;
			int dq = getDefaultQuantity(item.getId());
			int price = getBuyPrice(item, dq);
			player.getTemporaryAttributtes().put("BuySelectedSlot", slotId);
			player.getPackets().sendConfig(2561, MAIN_STOCK_ITEMS_KEY);
			player.getPackets().sendConfig(2562, item.getId());
			player.getPackets().sendConfig(2563,
					item.getId() == 995 ? -1 : slotId);
			player.getPackets().sendGlobalConfig(1876,
					ItemDefinitions.getEquipType(item.getName()));
			player.getPackets().sendConfig(2564, 1);
			player.getPackets().sendIComponentText(1265, 40,
					ItemExamines.getExamine(item));
			for (int i = 0; i < loyaltyPrices.length; i++) {
				if (item.getId() == loyaltyPrices[i][0]) {
					player.getPackets().sendGameMessage(
							"" + item.getDefinitions().getName() + ": shop will sell for "
									+ loyaltyPrices[i][1] + " loyalty points.");
					player.getPackets().sendConfig(2564, loyaltyPrices[i][1]);
					return;
				}
			}
			for (int i = 0; i < votePrices.length; i++) {
				if (item.getId() == votePrices[i][0]) {
					if (item.getId() == 19752) {
						player.getPackets()
								.sendGameMessage(
										"Want a wish to become true? This lamp will give you 3 wishes from Enth!");
					} else {
						player.getPackets().sendGameMessage(
								"" + item.getDefinitions().getName()
										+ ": shop will sell for " + votePrices[i][1]
										+ " vote points.");
					}
					player.getPackets().sendConfig(2564, votePrices[i][1]);
					return;
				}
			}
			 for (int i = 0; i < dungPrices.length; i++) {
					if (item.getId() == dungPrices[i][0]) {
							player.getPackets().sendGameMessage(
								"" + item.getDefinitions().getName()
									+ ": shop will sell for " + dungPrices[i][1]
									+ " dungeoneering tokens.");
						    player.getPackets().sendConfig(2564, dungPrices[i][1]);
						    return;
						}
					 }
			for (int i = 0; i < dominionTower.length; i++) {
				if (item.getId() == dominionTower[i][0]) {
					player.getPackets()
							.sendGameMessage(
									"" + item.getDefinitions().getName()
											+ ": shop will sell for " + dominionTower[i][1]
											+ " dominion factor.");
					player.getPackets().sendConfig(2564, dominionTower[i][1]);
					return;
				}
			}
			for (int i = 0; i < pvmShop.length; i++) {
				if (item.getId() == pvmShop[i][0]) {
					player.getPackets().sendGameMessage(
							"" + item.getDefinitions().getName() + ": shop will sell for "
									+ pvmShop[i][1] + " pvm points.");
					player.getPackets().sendConfig(2564, pvmShop[i][1]);
					return;
				}
			}
			for (int i = 0; i < skillShop.length; i++) {
				if (item.getId() == skillShop[i][0]) {
					player.getPackets().sendGameMessage(
							"" + item.getDefinitions().getName() + ": shop will sell for "
									+ skillShop[i][1] + " skill points.");
					player.getPackets().sendConfig(2564, skillShop[i][1]);
					return;
				}
			}
			for (int i = 0; i < pkShop.length; i++) {
				if (item.getId() == pkShop[i][0]) {
					player.getPackets().sendGameMessage(
							"" + item.getDefinitions().getName() + ": shop will sell for "
									+ pkShop[i][1] + " pk points.");
					player.getPackets().sendConfig(2564, pkShop[i][1]);
					return;
				}
			}
			for (int i = 0; i < donorShop.length; i++) {
				if (item.getId() == donorShop[i][0]) {
					player.getPackets().sendGameMessage(
							"" + item.getDefinitions().getName() + ": shop will sell for "
									+ donorShop[i][1] + " donation credits.");
					player.getPackets().sendConfig(2564, donorShop[i][1]);
					return;
				}
			}
			for (int i = 0; i < AchievementPrices.length; i++) {
				if (item.getId() == AchievementPrices[i][0]) {
					player.getPackets().sendGameMessage(
							"" + item.getDefinitions().getName() + ": shop will sell for "
									+ AchievementPrices[i][1] + " achievement points.");
					player.getPackets().sendConfig(2564, AchievementPrices[i][1]);
					return;
				}
			}
			for (int i = 0; i < easterShop.length; i++) {
				if (item.getId() == easterShop[i][0]) {
					player.getPackets().sendGameMessage(
							"" + item.getDefinitions().getName() + ": shop will sell for "
									+ easterShop[i][1] + " easter points.");
					player.getPackets().sendConfig(2564, easterShop[i][1]);
					return;
				}
			}
			if (isGeneralStore()) {
				player.getPackets().sendHideIComponent(1265, 52, false);
			}
			player.getPackets().sendGameMessage(
					item.getDefinitions().getName()
							+ ": shop will "
							+ (isBuying ? "sell" : "buy")
							+ " for "
							+ price
							+ " "
							+ ItemDefinitions.getItemDefinitions(money)
									.getName().toLowerCase() + ".");
		} else if (!isBuying) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				return;
			int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
			int price = getSellPrice(item, dq);
			player.getTemporaryAttributtes().put("SellSelectedSlot", slotId);
			player.getPackets().sendConfig(2561, MAIN_STOCK_ITEMS_KEY);
			player.getPackets().sendConfig(2562, item.getId());
			player.getPackets().sendConfig(2563,
					item.getId() == 995 ? -1 : slotId);
			player.getPackets().sendConfig(1496, item.getId());
			player.getPackets().sendGlobalConfig(1876,
					ItemDefinitions.getEquipType(item.getName()));
			player.getPackets().sendConfig(2564, 1);
			player.getPackets().sendIComponentText(1265, 40,
					ItemExamines.getExamine(item));
			player.getPackets().sendGameMessage(
					item.getDefinitions().getName()
							+ ": shop will "
							+ (isBuying ? "sell" : "buy")
							+ " for "
							+ price
							+ " "
							+ ItemDefinitions.getItemDefinitions(money)
									.getName().toLowerCase() + ".");
		}
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 28, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7,
				"Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
	}

	public void sendSellStore(Player player, Item[] inventory) {
		Item[] stock = new Item[inventory.length
				+ (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(inventory, 0, stock, 0, inventory.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, inventory.length,
					generalStock.length);
		player.getPackets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length
				+ (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, mainStock.length,
					generalStock.length);
		player.getPackets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
	}

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (item.getDefinitions().isNoted() || !ItemConstants.isTradeable(item)
				|| item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item, dq);
		if (money == 995)
			player.getPackets().sendGameMessage(
					item.getDefinitions().getName()
							+ ": shop will buy for: "
							+ price
							+ " "
							+ ItemDefinitions.getItemDefinitions(money)
									.getName().toLowerCase()
							+ ". Right-click the item to sell.");

	}

	public void setAmount(Player player, int amount) {
		this.amount = amount;
		player.getPackets()
				.sendIComponentText(1265, 67, String.valueOf(amount));
	}
}