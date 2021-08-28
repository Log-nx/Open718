package com.rs.game.player.content.trivia;

import java.util.Random;

import com.rs.game.World;
import com.rs.game.player.Player;

public class TriviaBot {
	
	private static String questions [][] = {
		{"What do you receive when a fire disappears?", "Ashes"},
		{"What was the name of BigFuckinChungus's favorite 718 server?", "Elveron"},
		{"What is the name of the new firecape?", "TokHaar-Kal"}
	};
	
	public static int questionid = -1;
	public static int round = 0;
	public static boolean victory = false;
	public static int answers = 0;

	public TriviaBot() {
	}
	
	public static void Run() {
		int rand = RandomQuestion();
		questionid = rand;
		victory = false;
		for(Player participant : World.getPlayers()) {
			if(participant == null)
				continue;
				participant.hasAnswered = false;
				participant.getPackets().sendGameMessage("<col=56A5EC>[Trivia] "+questions[rand][0]);
		}
	}
	
	public static void sendRoundWinner(String winner, Player player)
	{
		if (answers < 10)
			answers++;
		if (answers == 10)
			victory = true;
		if (answers == 1)
			player.getStatistics().triviaPoints += 3;
		else
			player.getStatistics().triviaPoints++;

		World.sendWorldMessage("<col=56A5EC>[Winner] <col=FF0000>" + winner + "</col><col=56A5EC> answered the question correctly (" + answers
		        + "/10)! Answer with ::answer.</col>", false, false);

		player.hasAnswered = true;

		player.sm("<col=56A5EC>[Trivia] " + winner + ", you now have " + player.getStatistics().triviaPoints + " Trivia Points.</col>");
	}
	
	public static void verifyAnswer(final Player player, String answer) {
		
		if (victory) {
			player.sm("That round has already been won, wait for the next round.");
			
		} else if (player.hasAnswered) {
			player.sm("You have already answered this question.");
			
		} else if (questions[questionid][1].equalsIgnoreCase(answer)) {
			round++;
			sendRoundWinner(player.getDisplayName(), player);
		} else {
			player.sm("That answer wasn't correct, please try it again.");
		}
	}
	
	public static int RandomQuestion() {
		int random = 0;
		Random rand = new Random();
		random = rand.nextInt(questions.length);
		return random;
	}
	
	public static boolean TriviaArea(final Player participant) {
		if(participant.getX() >= 2630 && participant.getX() <= 2660 && participant.getY() >= 9377 && participant.getY() <= 9400) {
			return true;
		}
		return false;
	}
}
