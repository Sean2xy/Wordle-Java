package wordle;

import java.util.*;

/**
 * Observe changes to the WordleModel and handle user inputs
 * and interact with the model using the WordleController
 */
public class CliWordle implements Observer {
    private final WordleModel wordleModel = new WordleModel();
    private final WordleController wordleController = new WordleController(wordleModel);
    private final String[][] guessedLetters = new String[6][5];
    private final List<String> guessedWords = new ArrayList<>();

    /**
     * Replay the game or start a new game with three different options
     */
    public CliWordle(boolean notCount, boolean displayTargetWord, boolean random, boolean restart) {
        if (!restart) {
            GameUtils.getGameUtils().startGame(notCount, displayTargetWord, random);
        } else {
            GameUtils.getGameUtils().restartGame();
        }
        //register to observe model change event
        wordleModel.registerObserver(this);
        for (String[] letter : guessedLetters) {
            Arrays.fill(letter, "");
        }
    }

    /**
     * Main class for CLI version
     */
    public static void main(String[] args) {
        System.out.println("Please guess a five-letter word within six attempts");
        showMenu();
        CliWordle newGame = new CliWordle(false, false, false, false);

        // Read user input corresponding to different operations
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            switch (input) {
                case "0":
                    showMenu();
                    break;
                case "1":
                    GameUtils.getGameUtils().setNotCount(true);
                    break;
                case "2":
                    System.out.println("The target word is: " + GameUtils.getGameUtils().getWord());
                    break;
                case "3":
                    GameUtils.getGameUtils().setRandomWord(true);
                    break;
                case "4":
                    newGame = new CliWordle(GameUtils.getGameUtils().isNotCount(),
                            GameUtils.getGameUtils().isDisplayWord(),
                            GameUtils.getGameUtils().isRandomWord(),
                            true);
                    System.out.println("Please guess a five-letter word within six attempts");
                    showMenu();
                    break;
                case "5":
                    System.out.println("Goodbye");
                    System.exit(0);
                default:

                    /**
                     * Once the player inputs a guess, the program checks if it is valid,
                     * updates the model with the guess, and prints the guessed words records.
                     * If the player wins or tried all the attempts, the program prints the appropriate message
                     * and exits or provides to replay the game
                     */
                    if (!GameUtils.getGameUtils().canTry()) {
                        if (GameUtils.getGameUtils().isCorrect()) {
                            System.out.println("Correct guess");
                        } else {
                            System.out.println("Sorry,you have already tried six attempts");
                        }
                        break;
                    }
                    if (input.length() != 5) {
                        System.out.println("Invalid guess,please input a five-letter word");
                        break;
                    }
                    for (int i = 0; i < input.length(); i++) {
                        newGame.wordleController.keyPressed(input.substring(i, i + 1).toUpperCase());
                    }
                    newGame.wordleController.keyPressed("Enter");
                    break;
            }
        }

    }

    /**
     * Provide each different operation
     */
    private static void showMenu() {
        System.out.println();
        System.out.println("input 0, Showing the game menu");
        System.out.println("input 1, The attempt will be not count if guess is not in word lost");
        System.out.println("input 2, Showing the target word");
        System.out.println("input 3, Selecting the word randomly");
        System.out.println("input 4, Replaying the game");
        System.out.println("input 5, Exiting the game");
        System.out.println();
        System.out.println("Please input a five-letter word");


    }

    private void printGuessedWords() {
        System.out.println("Guessed words records:");
        for (String word : guessedWords) {
            System.out.println(word);
        }
        System.out.println();
        System.out.println("Please input a five-letter word");
    }

    @Override
    public void update(Observable o, Object arg) {
        WordleModel.ModelChangedOperation operation = (WordleModel.ModelChangedOperation) arg;

        /**
         * Handle different types of operations
         */
        switch (operation.getType()) {
            // Update the guessedLetters array with the newly added letter at the specified row and column.
            case WordleModel.addLetter:
                guessedLetters[operation.getRow()][operation.getColumn()] = operation.getLetter();
                break;
            // Check if the guess was correct or not
            case WordleModel.guess:
                if (GameUtils.getGameUtils().isCorrect()) {
                    System.out.println("Great! Guess Correct");
                } else {
                    if (GameUtils.getGameUtils().canTry()) {
                        System.out.println("Sorry, you have a wrong guess but you still have chances");

                    } else {
                        System.out.println("Sorry, you have a wrong guess ");
                    }
                }
                /**
                 * Retrieve the guessed letters and word from the model's guess at the specified row
                 */
                List<GuessedLetter> letters = wordleModel.getGuess(operation.getRow()).getLetters();
                String[] word = guessedLetters[operation.getRow()];
                guessedWords.add(String.join("", word));
                for (int i = 0; i < word.length; i++) {
                    if (letters.isEmpty()) {
                        word[i] = "";
                    }
                }
                if (operation.isDisplayError()) {
                    System.out.println("This word is not in the word list！");
                }
                printGuessedWords();
                // If the player has no more chances to guess
                if (!GameUtils.getGameUtils().canTry()) {
                    System.out.println("You can input 4 to replay the game");
                }
                break;
        }
    }
}
