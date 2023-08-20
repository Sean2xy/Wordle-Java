package wordle;
import java.util.*;
public class WordleModel extends Observable {
    /**
     * Representing different types of operations
     */
    public static final int guess = 1;
    public static final int addLetter = 2;
    public static final int delLetter = 3;

    /**
     * A class that store guessed letters
     * with the operations of add and delete letter
     */
    public static class GuessedWord {
        private final List<GuessedLetter> letters = new ArrayList<>();
        // Add the guessed letter when the word length is less than 5
        public boolean addGuessedLetter(String l) {
            if (letters.size() < 5) {
                letters.add(new GuessedLetter(l));
                return true;
            }
            return false;
        }
        // Remove the last letter in the word
        public boolean delete() {
            if (letters.size() == 0) {
                return false;
            }
            letters.remove(letters.size() - 1);
            return true;
        }

        public List<GuessedLetter> getLetters() {
            return letters;
        }
    }

    /**
     * Storing guessed words
     */
    private final List<GuessedWord> guessedWord = new ArrayList<>();


    /**
     * Returning all guessed words
     */
    public List<GuessedWord> getGuessedWord() {
        return guessedWord;
    }

    /**
     * Adding a letter and creating a new guessing word if the current word is empty
     */
    public void addLetter(String letter) {

        // letter not null and must be one letter
        assert letter != null && letter.length() == 1;

        GuessedWord word = getCurrentGuess();
        if (word == null) {
            word = new GuessedWord();
            guessedWord.add(word);
        }
        if (word.addGuessedLetter(letter)) {
            setChanged();
            notifyObservers(new ModelChangedOperation(
                    addLetter, guessedWord.size() - 1, word.getLetters().size() - 1, letter, false));
        }
    }

    /**
     * Delete a letter. If the deletion is successful
     * it will notify the observer
     */
    public void deleteLetter() {
        GuessedWord word = getCurrentGuess();

        if (word == null) {
            return;
        }
        int size = word.getLetters().size();
        if (word.delete()) {

            // size of target word should increase 1
            assert size == word.getLetters().size() + 1;

            setChanged();
            notifyObservers(new ModelChangedOperation(
                    delLetter, guessedWord.size() - 1, word.getLetters().size(), null, false));
        }
    }

    /**
     * Returning the last word in the list, which is the current one
     */
    public GuessedWord getCurrentGuess() {
        int size = guessedWord.size();
        if (size == 0) {
            return null;
        }

        GuessedWord word = guessedWord.get(size - 1);

        // word is not null
        assert word != null;

        return word;
    }

    /**
     * Getting the guessing word for the specified number of rows
     */
    public GuessedWord getGuess(int row) {

        // pre-condition
        assert row >= 0;

        GuessedWord word = guessedWord.get(row);

        // post-condition
        assert word != null;

        return word;
    }


    /**
     * A class that represents the operations changing the model state,
     * which contains operation type, number of rows, number of columns,
     * letters, and whether an error is displayed.
     */
    public static class ModelChangedOperation {
        private int type;
        private int row;
        private int column;
        private String letter;
        private boolean displayError;

        public ModelChangedOperation(int type, int row, int column, String letter, boolean displayError) {
            this.type = type;
            this.row = row;
            this.column = column;
            this.letter = letter;
            this.displayError = displayError;

        }
        public int getType() {
            return type;
        }
        public int getRow() {
            return row;
        }
        public int getColumn() {
            return column;
        }
        public String getLetter() {
            return letter;
        }
        public boolean isDisplayError() {
            return displayError;
        }

    }

    /**
     * Adding Observer to Observer List
     */
    public void registerObserver(Observer observer) {
        addObserver(observer);
    }

    /**
     * Notifying the observer
     */
    public void notifyGuess(int row, boolean displayError) {

        // pre-condition
        assert row >= 0;

        setChanged();
        notifyObservers(
                new ModelChangedOperation(guess, row, 0, null, displayError)
        );
    }
}
