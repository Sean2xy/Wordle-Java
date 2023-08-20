package wordle;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that deals with the game logic
 */
public class GameUtils {
    private static final GameUtils gameUtils = new GameUtils();

    private String word;
    private int attemptCount;
    private boolean correct;
    private boolean notCount;
    private boolean displayWord;
    private boolean randomWord;

    private GameUtils() {
    }

    public static GameUtils getGameUtils() {
        return gameUtils;
    }

    /**
     Check if the game can still be tried. If guessing coreect
     or have tried 6 times, cannot continue trying
     */
    public boolean canTry() {
        return !correct && attemptCount < 6;
    }

    /**
     * Starting a new game and generate a new word from the word list
     */
    public void startGame(boolean notCount, boolean show, boolean randomWord) {
        attemptCount = 0;
        correct = false;
        this.notCount = notCount;
        this.displayWord = show;
        this.randomWord = randomWord;
        word = ReadfromFiles.getInstance().getWord();
    }

    /**
     * Reset the game and generate a new word if user selects random generating
     */
    public void restartGame() {
        attemptCount = 0;
        correct = false;
        if (randomWord) {
            word = ReadfromFiles.getInstance().getWord();
        }
    }

    /**
     * Check if the guess is correct and return a flag indicating whether an error has occurred.
     */
    public boolean checkGuess(List<GuessedLetter> letters) {

        /**
         * Compare the guessed letters with random words,
         * and if the letters are correct and positioned correctly, set them to green;
         * if the letter is in the word but the position is incorrect, set it to yellow;
         * If the letter is not in a random word, set the color of the letter to gray
         */
        for (int i = 0; i < letters.size(); i++) {
            GuessedLetter guessed = letters.get(i);
            if (!word.toUpperCase().contains(guessed.getLetter())) {
                guessed.setColor(Color.GRAY);
            } else if (word.toUpperCase().substring(i, i + 1).equals(guessed.getLetter())) {
                guessed.setColor(Color.GREEN);
            } else {
                guessed.setColor(Color.YELLOW);
            }
        }

        /**
         * Check if all guessed letters are green, if so, guessing correct
         */
        correct = letters.stream().allMatch(l -> l.getColor().equals(Color.green));
        /**
         * Check if the guessed words exist in the dictionary
         */
        boolean exist = ReadfromFiles.getInstance().exist(letters.stream().map(GuessedLetter::getLetter).collect(Collectors.joining()));
        /**
         * If the guessed word does not exist in the dictionary, an error will occur and the letter list will be cleared
         */
        if (!notCount || exist) {
            attemptCount++;
        }
        boolean error = notCount && !exist;
        if (error) {
            letters.clear();
        }
        return error;
    }

    /**
     * Getter and setter
     */
    public String getWord() {
        return word;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isNotCount() {
        return notCount;
    }

    public boolean isDisplayWord() {
        return displayWord;
    }

    public boolean isRandomWord() {
        return randomWord;
    }

    public void setNotCount(boolean notCount) {
        this.notCount = notCount;
    }

    public void setDisplayWord(boolean displayWord) {
        this.displayWord = displayWord;
    }

    public void setRandomWord(boolean randomWord) {
        this.randomWord = randomWord;
    }
}
