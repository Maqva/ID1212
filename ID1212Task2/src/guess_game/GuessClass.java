package guess_game;

import java.util.concurrent.ThreadLocalRandom;

public class GuessClass {
	private int noOfGuesses = 0;
	private int value;
	
	public GuessClass() {
		value = ThreadLocalRandom.current().nextInt(1, 101);
	}
	
	public int makeGuess(int guess) {
		noOfGuesses ++;
		//0 if the guess is Correct
		if(guess == value)
			return 0;
		//positive if the guess was too high
		else if(guess > value)
			return 1;
		//negative if the guess is too low
		else 
			return -1;
	}
	
	public int getGuesses() {
		return noOfGuesses;
	}
}
