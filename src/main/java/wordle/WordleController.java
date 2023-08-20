package wordle;

/**
 * Responding to key press，enable/disable buttons
 */
public class WordleController {
    private final WordleModel wordleModel;

    public WordleController(WordleModel wordleModel) {
        this.wordleModel = wordleModel;
    }

    /**
     * Handling button click events with different logics
     * if click enter, checking the guessed letter meets the conditions or not
     * if click delete, calling the delete method in model
     * if input a letter, calling the addLetter method in model
     */
    public void keyPressed(String keyboard) {
        if (!GameUtils.getGameUtils().canTry()) {
            return;
        }

        if (keyboard.equals("Enter")) {
            WordleModel.GuessedWord word = wordleModel.getCurrentGuess();
            if (word == null) {
                return;
            }

            if (word.getLetters().size() < 5) {
                return;
            }

            /**
             * calling the model to add a new world if there is no error and attempt time < 6
             */
            boolean displayError = GameUtils.getGameUtils().checkGuess(word.getLetters());
            int row = wordleModel.getGuessedWord().size() - 1;
            if (GameUtils.getGameUtils().canTry() && !displayError) {
                wordleModel.getGuessedWord().add(new WordleModel.GuessedWord());
            }
            wordleModel.notifyGuess(row,displayError);
            return;
        }
        if (keyboard.equals("Delete")) {
            wordleModel.deleteLetter();
            return;
        }
        wordleModel.addLetter(keyboard);
    }


}
