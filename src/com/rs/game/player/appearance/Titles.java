package com.rs.game.player.appearance;

import java.io.Serializable;

import com.rs.Settings;
import com.rs.game.player.Player;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class Titles implements Serializable {

	/**
	 * The generated serial ID.
	 */
	private static final long serialVersionUID = -8142899759527457838L;

	/**
	 * The player-class to save to.
	 */
	private transient Player player;

	/**
	 * The player to set the class to.
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Sends the component text on interface.
	 * 
	 * @param componentId
	 *            the componentId.
	 * @param text
	 *            the String to send.
	 */
	private void sendString(int componentId, String text) {
		player.getPackets().sendIComponentText(1082, componentId, " " + text);
	}

	/**
	 * Checks if the player is a male or not.
	 * 
	 * @return true if Male.
	 */
	private boolean isMale() {
		if (player.getAppearence().isMale()) {
			return true;
		}
		return false;
	}

	/**
	 * The players username.
	 * 
	 * @return the Username to return.
	 */
	private String username() {
		return player.getDisplayName();
	}

	/**
	 * Opens the Title shop.
	 */
	public void openShop() {

		/**
		 * Clean out the interface.
		 */
		for (int i = 0; i < Utils.getInterfaceDefinitionsComponentsSize(1082); i++) {
			sendString(i, "");
		}

		/**
		 * Sending Essentials.
		 */
		player.getPackets().sendHideIComponent(1082, 159, true);
		sendString(159, Settings.SERVER_NAME + " Title Manager");
		sendString(11, Colors.RED + "Red = Locked" + Colors.WHITE + ".   " + Colors.GREEN + "Green = Unlocked" + Colors.WHITE + ".<br>" + Colors.WHITE + "To activate a Title of your choice - press on the requirement text.");
		sendString(41, Colors.CYAN + "   - Previews:");
		sendString(42, Colors.CYAN + "   - Requirements:");

		/**
		 * Title Previews.
		 */
		sendString(30, Colors.GREY + (isMale() ? "Ironman" : "Ironwoman") + " " + Colors.WHITE + username());
		sendString(32, Colors.WHITE + username() + " " + Colors.GREY + (isMale() ? "the Ironman" : "the Ironwoman"));
		sendString(34, Colors.WHITE + username() + " " + Colors.DARK_RED + "the Survivor");

		sendString(36, Colors.WHITE + username() + Colors.DARK_RED + " the Squire");
		sendString(38, Colors.WHITE + username() + Colors.DARK_RED + " the Knight");
		sendString(40, Colors.WHITE + username() + Colors.DARK_RED + " the Legend");

		/** Goodwill well titles **/
		sendString(49, Colors.WHITE + username() + Colors.BROWN + " the Wishful");
		sendString(51, Colors.WHITE + username() + Colors.BROWN + " the Generous");
		sendString(53, Colors.WHITE + username() + Colors.BROWN + " the Millionaire");
		sendString(55, Colors.WHITE + username() + Colors.GOLDEN + " the Charitable");
		sendString(57, Colors.WHITE + username() + Colors.ORANGE + " the Awesome");

		/** Prifddinas titles **/
		sendString(59, Colors.WHITE + username() + "<col=FF08A0> of the Trahaearn");
		sendString(62, Colors.WHITE + username() + "<col=964F03> of the Hefin");

		/** Cloak of Seasons combination **/
		sendString(64, Colors.WHITE + username() + Colors.GREEN + "<shad=000000> of Seasons");

		/** Chronicle Fragment offering **/
		sendString(66, Colors.WHITE + username() + Colors.GREEN + "<shad=000000> of Guthix");

		/** Member Rank titles **/
		sendString(68, Colors.GREEN + "Regular Donator " + Colors.WHITE + username());
		sendString(70, Colors.RED + "Extreme Donator " + Colors.WHITE + username());
		sendString(72, Colors.PURPLE + "Legendary Donator " + Colors.WHITE + username());
		sendString(74, "<col=5F94F5>Supreme Donator " + Colors.WHITE + username());
		sendString(76, Colors.GOLDEN + "Ultimate Donator " + Colors.WHITE + username());

		/** Special titles **/
		sendString(190, "<col=FC0000>No-lifer " + Colors.WHITE + username());
		sendString(79, "<col=977EBF>Enthusiast " + Colors.WHITE + username());
		sendString(81, "<col=D966AF>The Maxed " + Colors.WHITE + username());
		sendString(83, "<col=A200FF>The Completionist " + Colors.WHITE + username());
		sendString(85, "<col=6200FF>The Perfectionist " + Colors.WHITE + username());

		/** Wealth titles */
		sendString(88, Colors.WHITE + username() + Colors.BROWN + " the Poor");
		sendString(90, Colors.WHITE + username() + Colors.ORANGE + " the Millionaire");//Colors.ORANGE + " the Millionaire</col>
		sendString(92, Colors.WHITE + username() + Colors.CYAN + " the Billionaire");
		sendString(94, Colors.WHITE + username() + Colors.GREEN + " the Monopolist");
		
		/** Prestige ranks titles */
		sendString(97, Colors.WHITE + username() + Colors.YELLOW + " the Determined");
		sendString(99, Colors.WHITE + username() + Colors.GOLDEN + " the Preseverant");
		sendString(101, Colors.RED + "Grandmaster </col " + Colors.WHITE + username());
		
		/** Veteran rank */
		sendString(104, Colors.WHITE + username() + " <col=977EBF>the Veteran</col>");
		
		/**
		 * Title Description/Requirement.
		 */
		sendString(31, (player.isIronman() ? Colors.GREEN : Colors.RED) + "Be an " + (isMale() ? "Ironman" : "Ironwoman") + " to use this title.");
		sendString(33, (player.isIronman() ? Colors.GREEN : Colors.RED) + "Be an " + (isMale() ? "Ironman" : "Ironwoman") + " to use this title.");
		sendString(39, (player.getDominionTower().getTotalScore() >= 50000 ? Colors.GREEN + "<i>" : Colors.RED) + "Get 50'000 Dominion Factor to use this title.");

		/** Goodwill well titles **/

		/** Prifddinas titles **/
		sendString(73, (player.getStatistics().getSerenStonesMined() >= 100 ? Colors.GREEN : Colors.RED) + "Mined a total of 100 Seren stones.");
		sendString(100, (player.getStatistics().getHefinLaps() >= 200 ? Colors.GREEN : Colors.RED) + "Ran a total of 200 Hefin Agility laps");

		/** Cloak of Seasons combination **/

		/** Chronicle Fragment offering **/

		/** Member Rank titles **/
		sendString(80, (player.getDonationManager().isExtremeDonator() ? Colors.GREEN : Colors.RED) + "Be ranked as Silver member (50$)");
		sendString(82, (player.getDonationManager().isLegendaryDonator() ? Colors.GREEN : Colors.RED) + "Be ranked as Gold member (100$)");
		sendString(84, (player.getDonationManager().isImmortalDonator() ? Colors.GREEN : Colors.RED) + "Be ranked as Platinum member (250$)");
		sendString(86, (player.getDonationManager().isDivineDonator() ? Colors.GREEN : Colors.RED) + "Be ranked as Diamond member (500$)");

		/** Special titles **/
		sendString(91, (player.getStatistics().getVotes() >= 250 ? Colors.GREEN : Colors.RED) + "Voted for at least 250 times");
		
		/** Now we send the actual interface with all the data **/
		player.getInterfaceManager().sendInterface(1082);
	}

	/**
	 * Sets the players title.
	 * 
	 * @param titleId
	 *            the Title to set.
	 */
	private void setTitle(int titleId) {
		player.getAppearence().setTitle(titleId);
		player.getAppearence().generateAppearenceData();
		player.sm("Your title has been successfully changed.");
	}

	/**
	 * Handles the Title shop buttons.
	 * 
	 * @param componentId
	 *            the interfaces buttonId.
	 */
	public void handleShop(int componentId) {
		player.getInterfaceManager().closeChatBoxInterface();
		switch (componentId) {
		case 31: /** Ironman after **/
			if (!player.isIronman()) {
				player.getDialogueManager().startDialogue("SimpleMessage", "This title is only available for " + (isMale() ? "Ironman" : "Ironwoman") + " accounts.");
				return;
			}
			setTitle(1000);
			break;
		case 33: /** Ironman before **/
			if (!player.isIronman()) {
				player.getDialogueManager().startDialogue("SimpleMessage", "This title is only available for " + (isMale() ? "Ironman" : "Ironwoman") + " accounts.");
				return;
			}
			setTitle(1500);
			break;
		case 39: /** the Survivor **/
			if (player.getDominionTower().getTotalScore() < 50000) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You have to have a Dominion Factor of at least 50'000 to use this title.");
				return;
			}
			setTitle(1502);
			break;
		case 50: /** Squire before **/
			if (!(player.getGameMode() == 0)) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You have to be on the Easy EXP game mode to use this title.");
				return;
			}
			setTitle(1002);
			break;
		case 52: /** Intermediate before **/
			if (!(player.getGameMode() == 1)) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You have to be on the Intermediate EXP game mode to use this title.");
				return;
			}
			setTitle(1003);
			break;

		}
	}
}