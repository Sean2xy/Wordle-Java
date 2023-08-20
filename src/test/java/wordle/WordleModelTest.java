package wordle;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.stream.Collectors;


/**
 * Test class for testing the add, remove
 * and guess letter functions in WordleModel class
 * using Junit 4 with assertions
 */
public class WordleModelTest {
    // test scenario 1
    @Test
    public void addLetter() {
        // Scenario: Testing the model with adding a new letter 'A'

        // Given
        WordleModel wordleModel = new WordleModel();

        // When
        wordleModel.addLetter("A");

        // Then
        assertEquals("The guessedWords length of the gameModel equals 1",
                              wordleModel.getGuessedWord().size(), 1);
        assertEquals("The first guessed letter is A",
                              wordleModel.getCurrentGuess().getLetters().get(0).getLetter(),
                              "A");
    }
    // test scenario 2
    @Test
    public void deleteLetter() {
        // Scenario: Testing the model with removing a letter
        // after adding two letters

        // Given
        WordleModel wordleModel = new WordleModel();

        // When
        wordleModel.addLetter("A");
        wordleModel.addLetter("B");
        assertEquals("Add two letter in guessed word",
                      wordleModel.getCurrentGuess().getLetters().size(), 2);
        wordleModel.deleteLetter();

        // Then
        assertEquals("Only one letter in current guess word " +
                              "after deleting, which is A",
                              wordleModel.getCurrentGuess().getLetters().get(0).getLetter(),
                              "A");

    }
    // test scenario 3
    @Test
    public void guess() {
        // Scenario: Testing the model with guess function after adding the target word

        // Given
        GameUtils.getGameUtils().startGame(false, false, false);
        String word = GameUtils.getGameUtils().getWord();
        System.out.println("Target word is:" + word);
        WordleModel wordleModel = new WordleModel();

        // When
        char[] letters = word.toCharArray();
        for (char letter : letters){
            wordleModel.addLetter(Character.toString(letter).toUpperCase());
        }
        String guess = wordleModel.getCurrentGuess().getLetters().
                       stream().map(GuessedLetter::getLetter).collect(Collectors.joining());

        // Then
        assertEquals("The current guess word equals target word", guess, word.toUpperCase());
        GameUtils.getGameUtils().checkGuess(wordleModel.getCurrentGuess().getLetters());
        assertTrue("Guess correct", GameUtils.getGameUtils().isCorrect());
    }
}
