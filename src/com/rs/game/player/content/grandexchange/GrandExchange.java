package com.rs.game.player.content.grandexchange;

import java.awt.Color;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.rs.GameServer;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.content.items.VirtualValues;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class GrandExchange {

	private static final Object LOCK = new Object();
	private static HashMap<Long, Offer> OFFERS;
	private static ArrayList<OfferHistory> OFFERS_TRACK;
	private static HashMap<Integer, Integer> PRICES;
	public static final Object[] UNLIMITED_BUYABLES = { 12183, 15262, 12155, 29864, 30028 };

	private static boolean edited;

	public static void init() {
		OFFERS = SerializableFilesManager.loadGEOffers();
		OFFERS_TRACK = SerializableFilesManager.loadGEHistory();
		PRICES = SerializableFilesManager.loadGEPrices();
		UnlimitedGEReader.init();
	}

	public static void reset(boolean track, boolean price) {
		if (track)
			OFFERS_TRACK.clear();
		if (price)
			PRICES.clear();
		recalcPrices();
	}

	public static void recalcPrices() {
		ArrayList<OfferHistory> track = new ArrayList<OfferHistory>(OFFERS_TRACK);
		HashMap<Integer, BigInteger> averagePrice = new HashMap<Integer, BigInteger>();
		HashMap<Integer, BigInteger> averageQuantity = new HashMap<Integer, BigInteger>();
		for (OfferHistory o : track) {
			BigInteger price = averagePrice.get(o.getId());
			if (price != null) {
				BigInteger quantity = averageQuantity.get(o.getId());
				averagePrice.put(o.getId(), price.add(BigInteger.valueOf(o.getPrice())));
				averageQuantity.put(o.getId(), quantity.add(BigInteger.valueOf(o.getQuantity())));
			} else {
				averagePrice.put(o.getId(), BigInteger.valueOf(o.getPrice()));
				averageQuantity.put(o.getId(), BigInteger.valueOf(o.getQuantity()));
			}
		}
		
		for (int id : averagePrice.keySet()) {
			BigInteger price = averagePrice.get(id);
			BigInteger quantity = averageQuantity.get(id);

			long oldPrice = getPrice(id);
			long newPrice = price.divide(quantity).longValue();
			long min = (long) (oldPrice * 0.95 + -1);
			long max = (long) (oldPrice * 1.05 + 1);
			if (newPrice < min)
				newPrice = min;
			else if (newPrice > max)
				newPrice = max;
			if (newPrice < 1)
				newPrice = 1;
			else if (newPrice > Integer.MAX_VALUE)
				newPrice = Integer.MAX_VALUE;
			int shopValue =  ItemDefinitions.getItemDefinitions(id).value;
			if (newPrice < shopValue)
				newPrice = shopValue;
			PRICES.put(id, (int) newPrice);
		}
		VirtualValues.setValues();
		savePrices();
	}

	public static void savePrices() {
		SerializableFilesManager.saveGEPrices(PRICES);
	}

	public static final void save() {
		if (!edited)
			return;
		SerializableFilesManager.saveGEOffers(OFFERS);
		SerializableFilesManager.saveGEHistory(OFFERS_TRACK);
		edited = false;
	}

	public static void linkOffers(Player player) {
		boolean itemsWaiting = false;
		for (int slot = 0; slot < player.getGEManager().getOfferUIds().length; slot++) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				continue;
			offer.link(slot, player, System.currentTimeMillis());
			offer.update();
			if (!itemsWaiting && offer.hasItemsWaiting()) {
				itemsWaiting = true;
				player.getPackets().sendGameMessage("You have items from the Grand Exchange waiting in your collection box.");
			}
		}
	}

	public static Offer getOffer(Player player, int slot) {
		synchronized (LOCK) {
			long uid = player.getGEManager().getOfferUIds()[slot];
			if (uid == 0)
				return null;
			Offer offer = OFFERS.get(uid);
			if (offer == null) {
				player.getGEManager().getOfferUIds()[slot] = 0;
				return null;
			}
			return offer;
		}
	}

	public static void sendOffer(Player player, int slot, int itemId, int amount, int price, boolean buy) {
		synchronized (LOCK) {
			String type = "";
			Offer offer = new Offer(itemId, amount, price, buy);
			player.getGEManager().getOfferUIds()[slot] = createOffer(offer);
			offer.link(slot, player, System.currentTimeMillis());
			if (!offer.isBuying()) {
				findBuyerSeller(offer);
				if (Settings.DISCORD) {
					type = "Selling";
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("Grand Exchange");
					eb.setColor(Color.RED);
					eb.addField(type, player.getDisplayName() + " is selling x" + amount + " " + ItemDefinitions.getItemDefinitions(itemId).getName().toLowerCase() + " for the price of " + Utils.formatNumber(price) + " coins" , false);
					eb.setTimestamp(Instant.now());
					new MessageBuilder().setEmbed(eb).send(GameServer.getDiscordBot().getAPI().getTextChannelById("585351703717609473").get());
				}
			} else {
				for (Object purchase : UNLIMITED_BUYABLES) {
					int unlimitedItem = (Integer) purchase;
						if (offer.getId() == unlimitedItem && offer.getPrice() >= GrandExchange.getPrice(itemId) || player.getRights() >= 2) {
							offer.buyOffer(offer);
							return;
						}
					}
				if (Settings.DISCORD) {
					type = "Buying";
					findBuyerSeller(offer);
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("Grand Exchange");
					eb.setColor(Color.GREEN);
					eb.addField(type, player.getDisplayName() + " is buying x" + amount + " " + ItemDefinitions.getItemDefinitions(itemId).getName().toLowerCase() + " for the price of " + Utils.formatNumber(price) + " coins" , false);
					eb.setTimestamp(Instant.now());
					new MessageBuilder().setEmbed(eb).send(GameServer.getDiscordBot().getAPI().getTextChannelById("585351703717609473").get());
					}
				}
			}
		}

	public static void abortOffer(Player player, int slot) {
		synchronized (LOCK) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				return;
			edited = true;
			if (offer.cancel() && offer.forceRemove()) {
				deleteOffer(player, slot); // shouldnt here happen anyway
			}
		}
	}

	public static void collectItems(Player player, int slot, int invSlot, int option) {
		synchronized (LOCK) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				return;
			edited = true;
			if (offer.collectItems(invSlot, option) && offer.forceRemove()) {
				deleteOffer(player, slot); // should happen after none left and offer completed
				if (offer.getTotalAmountSoFar() != 0) {
					OfferHistory o = new OfferHistory(offer.getId(), offer.getTotalAmountSoFar(), offer.getTotalPriceSoFar(), offer.isBuying());
					OFFERS_TRACK.add(o);
					player.getGEManager().addOfferHistory(o);
				}
			}
		}
	}

	private static void deleteOffer(Player player, int slot) {
		player.getGEManager().cancelOffer(); // sends back to original screen if seeing an offer
		OFFERS.remove(player.getGEManager().getOfferUIds()[slot]);
		player.getGEManager().getOfferUIds()[slot] = 0;
	}

	private static void findBuyerSeller(Offer offer) {
		while (!offer.isCompleted()) {
			Offer bestOffer = null;
			for (Offer o : OFFERS.values()) {
				if (o.isBuying() == offer.isBuying()
						|| o.getId() != offer.getId()
						|| o.isCompleted()
						|| (offer.isBuying() && o.getPrice() > offer.getPrice())
						|| (!offer.isBuying() && o.getPrice() < offer
								.getPrice()) || offer.isOfferTooHigh(o))
					continue;
				if (bestOffer == null
						|| (offer.isBuying() && o.getPrice() < bestOffer
								.getPrice())
						|| (!offer.isBuying() && o.getPrice() > bestOffer
								.getPrice()))
					bestOffer = o;
			}
			if (bestOffer == null)
				break;
			offer.updateOffer(bestOffer);
		}
		offer.update();
	}

	private static long createOffer(Offer offer) {
		edited = true;
		long uid = getUId();
		OFFERS.put(uid, offer);
		return uid;
	}

	private static long getUId() {
		while (true) {
			long uid = Utils.RANDOM.nextLong();
			if (OFFERS.containsKey(uid))
				continue;
			return uid;
		}
	}

	public static int getPrice(int itemId) {
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		return defs.getGrandExchangePrice();
	}
	
	public static void unlinkOffers(Player player) {
		for (int slot = 0; slot < player.getGEManager().getOfferUIds().length; slot++) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				continue;
			offer.unlink();
		}
	}

	public static List<OfferHistory> getHistory() {
		return OFFERS_TRACK;
	}

	public static int getTotalBuyQuantity(int itemId) {
		int quantity = 0;
		for (Offer offer : OFFERS.values()) {
			if (!offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
				continue;
			quantity += offer.getAmount() - offer.getTotalAmountSoFar();
		}
		return quantity;
	}

	public static int getTotalSellQuantity(int itemId) {
		int quantity = 0;
		for (Offer offer : OFFERS.values()) {
			if (offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
				continue;
			quantity += offer.getAmount() - offer.getTotalAmountSoFar();
		}
		return quantity;
	}
	
	public static int getBestBuyPrice(int itemId) {
		int price = -1;
		for (Offer offer : OFFERS.values()) {
			if (!offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
				continue;
			if (offer.getPrice() > price || price == -1) {
				price = offer.getPrice();
			}
		}
		return price;
	}

	public static int getBuyQuantity(int itemId) {
		int quantity = 0;
		for (Offer offer : OFFERS.values()) {
			if (!offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
				continue;
			if (offer.getPrice() == getBestBuyPrice(itemId))
				quantity += offer.getAmount() - offer.getTotalAmountSoFar();
		}
		return quantity;
	}

	public static int getCheapestSellPrice(int itemId) {
		int price = -1;
		for (Offer offer : OFFERS.values()) {
			if (offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
				continue;
			if (offer.getPrice() < price || price == -1) {
				price = offer.getPrice();
			}
		}
		return price;
	}

	public static int getSellQuantity(int itemId) {
		int quantity = 0;
		for (Offer offer : OFFERS.values()) {
			if (offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
				continue;
			if (offer.getPrice() == getCheapestSellPrice(itemId))
				quantity += offer.getAmount() - offer.getTotalAmountSoFar();
		}
		return quantity;
	}

	public static void setPrice(int id, int price) {
		PRICES.put(id, price);
	}

	public static long getTotalOfferValues(Player player) {
		long grandexchangeValue = 0;
		for (Entry<Long, Offer> entry : OFFERS.entrySet()) {
			Offer offer = entry.getValue();
			if (offer.getName().equals(player.getUsername())) {
				if (offer.isCompleted())
					continue;
				grandexchangeValue += ItemDefinitions.getItemDefinitions(offer.getId()).getTipitPrice()
						* offer.getAmount();
			}
		}
		return grandexchangeValue;

	}

	public static long getTotalCollectionValue(Player player) {
		long collectionValue = 0;
		for (Entry<Long, Offer> entry : OFFERS.entrySet()) {
			Offer offer = entry.getValue();
			if (offer.getName().equals(player.getUsername())) {
				if (!offer.isCompleted())
					continue;
				collectionValue += (offer.getPrice()) * (offer.getTotalAmountSoFar());
			}
		}
		return collectionValue;

	}
}