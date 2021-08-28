package com.rs.game.player.content;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class Lottery {
	
	/**
	 * The {@link Lottery} instance.
	 */
	public final static Lottery INSTANCE = new Lottery();
	
	/**
	 * The ticket price for each ticket displayed in the amount of coins.
	 */
	public final static Item TICKET_PRICE = new Item(995, 5 * 100 * 10000);
		
	/**
	 * The max amount of tickets each player can have.
	 */
	private final static int MAX_TICKET_EACH_PLAYER = 10;
	
	/**
	 * The list that holds the bought tickets for this {@link Lottery}.
	 */
	private final ArrayList<Player> TICKETS = new ArrayList<Player>();
	
	private final ArrayList<String> USERNAMES = new ArrayList<String>();
	
	/**
	 * Establishes this {@link Lottery}.
	 */
	public void establish() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			
			/**
			 * The amount of minutes that uses the {@link AtomicInteger}.
			 */
			private final AtomicInteger MINUTES = new AtomicInteger(360);

					
			@Override
			public void run() {
				switch(MINUTES.getAndDecrement()) {
				case 18000:
					message("The lottery jackpot will be given out in 5 hours.");
					break;
				case 14400:
					message("The lottery jackpot will be given out in 4 hours.");
					break;
				case 10800:
					message("The lottery jackpot will be given out in 3 hours.");
					break;
				case 7200:
					message("The lottery jackpot will be given out in 2 hours.");
					break;
				case 3600:
					message("The lottery jackpot will be given out in 1 hour.");
					break;
				case 1800:
				case 900:
				case 600:
				case 300:
					message("The lottery jackpot will be given out in " + (MINUTES.get() / 60) + " minutes.");
					break;
				case 0:
					giveLotteryPrice();
					message("A new lottery has started, the lottery jackpot will be given in 6 hours.");
					MINUTES.set(21600);
					break;
				}
			}
		}, 0, 1, Settings.DEBUG ? TimeUnit.SECONDS : TimeUnit.MINUTES);
	}
	
	/**
	 * Stop the lottery and gives out the price.
	 */
	public void giveLotteryPrice() {
		final ArrayList<Player> POSSIBLE_WINNERS = new ArrayList<Player>(TICKETS.size());
		for (final Player e : TICKETS) {
			if (e != null && e.getPrize() == null)
				POSSIBLE_WINNERS.add(e);
		}
		if (POSSIBLE_WINNERS.size() > 0) {
			Player winner = POSSIBLE_WINNERS.get(Utils.random(POSSIBLE_WINNERS.size()));
			final Item prize = getPrize();
			message(winner.getDisplayName() + " has just won the lottery with a price of "
					+ getFormattedNumber(prize.getAmount()) + "x " + prize.getDefinitions().name + ".");
			Player copy = World.getPlayer(winner.getUsername());
			if (copy != null) {
				copy.setPrize(prize);
				copy.getPackets().sendGameMessage("Congratulations, you have just won the lottery.");
				copy.getPackets().sendGameMessage("Talk-to Giles to claim your prize, he can be found at home.");
			} else {
				winner.setPrize(prize);
				SerializableFilesManager.savePlayer(winner);
			}
		}
		TICKETS.clear();
		USERNAMES.clear();
	}
	
	/**
	 * Add a player to this {@link Lottery}.
	 * @param player The player.
	 * @param npc The npc.
	 */
	public void addPlayer(Player player, NPC npc) {
		if (canEnter(player) && player.getInventory().containsItem(TICKET_PRICE.getId(), TICKET_PRICE.getAmount())) {
			player.getInventory().deleteItem(TICKET_PRICE.getId(), TICKET_PRICE.getAmount());
			TICKETS.add(player);
			player.getPackets().sendGameMessage("You have entered the lottery.");
			USERNAMES.add(Utils.formatPlayerNameForDisplay(player.getUsername()));
			final Item prize = getPrize();
			checkForMessage(prize);
			if (npc != null)
				npc.setNextForceTalk(new ForceTalk(player.getDisplayName() + " has entered the lottery, the lottery jackpot is now: " + getFormattedNumber(prize.getAmount()) + "x " + prize.getDefinitions().name + "."));
			//message(player.getDisplayName() + " has entered the lottery! The lottery jackpot is now " + getFormattedNumber(prize.getAmount()) + "x " + prize.getDefinitions().name + "!");
		} else if (!player.getInventory().containsItem(TICKET_PRICE.getId(), TICKET_PRICE.getAmount()))
			player.getPackets().sendGameMessage("You can not pay the ticket price to enter the lottery.");
	}
	
	private void checkForMessage(final Item item) {
		if (item.getAmount() == 1000000 * 10 || item.getAmount() == 1000000 * 25 || item.getAmount() == 50000000 || item.getAmount() == 100000000 || item.getAmount() == 1000000 * 200 || item.getAmount() == 1000000 * 500 || item.getAmount() == 1000000 * 1000 || item.getAmount() == 1000000 * 1500 || item.getAmount() == 1000000 * 2000)
			message("The lottery jackpot is now on " + getFormattedNumber(item.getAmount()) + " coins.");
	}
	
	/**
	 * Checks if the player can enter this {@link Lottery}.
	 * @param player The player to enter.
	 * @return {@code true} If entering is possible.
	 */
	private boolean canEnter(Player player) {
		int amountOfTickets = 0;
		for (final String e : USERNAMES) {
			if (e != null && e.equals(Utils.formatPlayerNameForDisplay(player.getUsername())) && ++amountOfTickets == MAX_TICKET_EACH_PLAYER) {
				player.getPackets().sendGameMessage("You can only have a maximum of " + MAX_TICKET_EACH_PLAYER + " tickets.");
				return false;
			}
		}
		if (player.checkTotalLevel(500) < 500) {
			player.getPackets().sendGameMessage("You need a total level of 500 to participate.");
			return false;
		}
		if (getRealPrize().getAmount() + TICKET_PRICE.getAmount() < 0) {
			player.getPackets().sendGameMessage("You can not enter the lottery, the jackport is already high enough.");
			return false;
		}
		return true;
	}

	/**
	 * Get the formatted number of amount.
	 * @param amount The amount.
	 * @return The formatted number of amount.
	 */
	public final String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount).replace(".", ",").toString();
	}
	
	/**
	 * Sends a message to all the players in the gameserver.
	 * @param message The message to sent.
	 */
	private void message(final String message) {
		World.sendWorldMessage("[<col=ffa500>Lottery System</col>] <col=ffa500>" + message + "</col>", false, false);
	}
	
	/**
	 * Get the real prize.
	 */
	private final Item getRealPrize() {
		return new Item(TICKET_PRICE.getId(), (int) Math.floor(TICKET_PRICE.getAmount() * TICKETS.size()));
	}
	/**
	 * Get the final prize.
	 */
	public final Item getPrize() {
		return new Item(TICKET_PRICE.getId(), (int) Math.floor((TICKET_PRICE.getAmount() * TICKETS.size()) * 0.90D));
	}
	
	/**
	 * Cancel this {@link Lottery}.
	 */
	public void cancelLottery() {
		for (final Player e : TICKETS) {
			if (e == null)
				return;
			if (!e.hasFinished())
				e.getPackets().sendGameMessage("<col=ff0000>The server is going to restart, you can always re-claim your tickets by talking to Giles.");
			e.setPrize(TICKET_PRICE);
		}
		TICKETS.clear();
	}
}