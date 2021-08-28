package com.rs.game.minigames.hunger;

import java.util.LinkedList;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.appearance.Equipment;
import com.rs.utils.Utils;

public class HungerGames {
	
	/**
	 * Lobby/Game players.
	 */
	private static final LinkedList<Player> lobbyPlayers = new LinkedList<Player>();
	private static final LinkedList<Player> gamePlayers = new LinkedList<Player>();
	/**
	 * Timers.
	 */
	private static boolean gameStarted = false;
	private static long gameTimer;
	private static long startTime;
	/**
	 * Players needed to start.
	 */
	private static int PLAYERS_NEEDED = 3;
	/**
	 * Random teleports.
	 */
	public static final WorldTile[] TELEPORTS = { new WorldTile(2788, 2978, 0),
			new WorldTile(2791, 2979, 0), new WorldTile(2774, 2979, 0),
			new WorldTile(2770, 2982, 0), new WorldTile(2789, 2981, 0),
			new WorldTile(2760, 2989, 0), new WorldTile(2768, 2973, 0),
			new WorldTile(2762, 2976, 0) };
	
	/**
	 * Lobby.
	 */
	public static final WorldTile LOBBY = new WorldTile(Settings.RESPAWN_PLAYER_LOCATION);
	
	/**
	 * Armour for game.
	 */
	public static int WEAPON[] = { 1291, 1307, 1321, 1337 };
	public static int PLATEBODY[] = { 1103, 1117, 1103, 1117 };
	public static int PLATELEGS[] = { 1075, 1087, 1075, 1087 };
	public static int HELMET[] = { 1155, 1139, 1139 };

	/**
	 * Checks if the game is currently started.
	 * @return isGameStarted?
	 */
	public static boolean isGameStarted() {
		return gameStarted;
	}

	/**
	 * Set the game started.
	 * @param gameStarted
	 */
	public static void setGameStarted(boolean gameStarted) {
		HungerGames.gameStarted = gameStarted;
	}
	
	/**
	 * Add player to game
	 */
	public static void addPlayer(Player player) {
			if(HungerGames.getLobbyPlayers().contains(player)) {
				HungerGames.removeLobbyPlayer(player);
					return;
				}
			if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis() ||
					(player.isLocked()
						|| player.getControllerManager().getController() != null)) {
			player.getPackets()
					.sendGameMessage(
								"You can't teleport right now.");
				return;
					}
					player.getControllerManager().startController("HungerGamesControler");
				}

	/**
	 * Starts the game.
	 */
	public static void startGame() {
		setGameStarted(true);
		for (Player gamePlayers : getGamePlayers()) {
			gamePlayers.getCombatDefinitions().setAutoCastSpell(0);
			gamePlayers.getCombatDefinitions().refreshAutoCastSpell();
			gamePlayers.getBank().depositAllBob(false);
			gamePlayers.getBank().depositAllEquipment(false);
			gamePlayers.getBank().depositAllInventory(false);
            //gamePlayers.sm("Any items you may have had with you have been added to your bank.");
			//gamePlayers.sm("May the odds be ever in your favor.");
			gamePlayers.sm("Good luck warrior!");
			gamePlayers.setNextWorldTile(TELEPORTS[Utils.random(TELEPORTS.length)]);
			gameTimer = Utils.currentTimeMillis();
			gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getHelmet()), new Item(getHelmet()));
			gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getWeapon()), new Item(getWeapon()));
			gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getPlatebody()), new Item(getPlatebody()));
			gamePlayers.getEquipment().getItems().set(Equipment.getItemSlot(getPlatelegs()), new Item(getPlatelegs()));
			gamePlayers.getEquipment().init();
			gamePlayers.getAppearence().generateAppearenceData();
			gamePlayers.setCanPvp(true);
		}
	}
	/**
	 * Process the game.
	 * Processes every 30 seconds.
	 */
	public static void processCage() {
		if (isGameStarted()) {
			if(getGamePlayers().size() == 1) {
				endGame();
			}
			if (getGameTimer() + 300000 < Utils.currentTimeMillis()) {
				for (Player gamers : getGamePlayers()) {
					gamers.sm("Sorry! Time has ran out!");
					endGame();
				}
			}
		}
		if(getLobbyPlayers().size() < PLAYERS_NEEDED && !isGameStarted()) {
			if(getStartTime() + 30000 < Utils.currentTimeMillis()) {
				for(Player players: getLobbyPlayers()) {
					players.sm(getLobbyPlayers().size() == 1 ? "There is currently <col=ff0000>1</col>"
							+ " player in the lobby!" :
						"There are currently <col=ff0000>"+getLobbyPlayers().size()+ ""
								+ "</col> players in the lobby!");
					players.sm("It requires at least <col=800000>"+PLAYERS_NEEDED+"</col> in the lobby to start the game.");
					setStartTime(Utils.currentTimeMillis());
				}
			}
		} else if (getLobbyPlayers().size() >= PLAYERS_NEEDED && !isGameStarted()) {
			if(getStartTime() + 30000 < Utils.currentTimeMillis()) {
				getGamePlayers().addAll(lobbyPlayers);
				getLobbyPlayers().removeAll(lobbyPlayers);
				startGame();
				setStartTime(Utils.currentTimeMillis());
			}
		}
	}
	/**
	 * Ends the game.
	 */
	public static void endGame() {
		for(Player gamers: getGamePlayers()) {
			if(gamers == null)
				continue;
			Player winner = (Player) getGamePlayers().poll();
			if(winner == null)
				continue;
			gamers.setCanPvp(false);
			gamers.getEquipment().reset();
			winner.sm("Congratulations, you've won!");
			winner.setHungerPoints(winner.getHungerPoints() + 10);//10 hungerpoints
			winner.prestigeLevel++;
			winner.setNextWorldTile(LOBBY);
			//gamers.sm("Game over, looser!");
			setGameStarted(false);
			gamers.getControllerManager().forceStop();
			gamers.getEquipment().deleteItem(PLAYERS_NEEDED, PLAYERS_NEEDED);
			gamers.getAppearence().generateAppearenceData();
		}
	}
	/**
	 * Gets the gameTimer.
	 * @return gameTimer
	 */
	public static long getGameTimer() {
		return gameTimer;
	}

	/**
	 * Set the gameTimer.
	 * @param gameTimer
	 */
	public static void setGameTimer(long gameTimer) {
		HungerGames.gameTimer = gameTimer;
	}
	/**
	 * Adds a player to the "game".
	 * @param player
	 */
	public static void addGamePlayer(Player player) {
		if(!getGamePlayers().contains(player)) 
			getGamePlayers().add(player);
	}
	/**
	 * Adds a player to the "lobby".
	 * @param player
	 */
	public static void addLobbyPlayer(Player player) {
		if(!getLobbyPlayers().contains(player)) {
			getLobbyPlayers().add(player);
			player.setNextWorldTile(new WorldTile(2787, 2968, 0));
	} else {
		removeLobbyPlayer(player);
		}
	}
	/**
	 * Removes a player from the "lobby".
	 * @param player
	 */
	public static void removeLobbyPlayer(Player player) {
		getLobbyPlayers().remove(player);
		player.getControllerManager().removeControllerWithoutCheck();
		player.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
	}
	/**
	 * Gets all the players in the LinkedList<Player> gamePlayers.
	 * @return gamePlayers
	 */
	public static LinkedList<Player> getGamePlayers() {
		return gamePlayers;
	}
	/**
	 * Gets all the players in the LinkedList<Player> lobbyPlayers.
	 * @return lobbyPlayers
	 */
	public static LinkedList<Player> getLobbyPlayers() {
		return lobbyPlayers;
	}
	/**
	 * Gets the random weapon.
	 * @return weapon
	 */
	public static int getWeapon() {
		return WEAPON[(int) (Math.random() * WEAPON.length)];
	}
	/**
	 * Get the random plateBody.
	 * @return platebody
	 */
	public static int getPlatebody() {
		return PLATEBODY[(int) (Math.random() * PLATEBODY.length)];
	}
	/**
	 * Gets the random platelegs.
	 * @return platelegs
	 */
	public static int getPlatelegs() {
		return PLATELEGS[(int) (Math.random() * PLATELEGS.length)];
	}
	/**
	 * Gets the random helmet.
	 * @return helmet.
	 */
	public static int getHelmet() {
		return HELMET[(int) (Math.random() * HELMET.length)];
	}
	/**
	 * Gets the startTime.
	 * @return startTime
	 */
	public static long getStartTime() {
		return startTime;
	}
	/**
	 * Sets the startTime.
	 * @param startTime
	 */
	public static void setStartTime(long startTime) {
		HungerGames.startTime = startTime;
	}

}
