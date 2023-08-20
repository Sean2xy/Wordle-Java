package wordle;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Operate word dictionaries read from text files
 */
public class ReadfromFiles {
    private static ReadfromFiles instance;
    private static final Random random = new Random();
    // Target word
    private static final List<String> targetWord = new ArrayList<>();
    // Valid guess
    private static final List<String> validGuessWords = new ArrayList<>();


    private ReadfromFiles() {
        /**
         * Initialize the word dictionary
         * by reading the words from two text files common.txt and words.txt
         * and adds each line as a word to the corresponding list
         */
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("common.txt")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    targetWord.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("words.txt")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    validGuessWords.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check multiple object exists or not
     */
    public static ReadfromFiles getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ReadfromFiles();
        }
        return instance;
    }

    /**
     * Randomly generate a word from the common.txt
     */
    public String getWord() {
        int index = random.nextInt(targetWord.size());
        return targetWord.get(index);
    }
    /**
     * Check if the given word exists in the files or not
     */
    public boolean exist(String word) {
        return targetWord.stream().anyMatch(s -> s.equals(word.toLowerCase())) ||
                validGuessWords.stream().anyMatch(s -> s.equals(word.toLowerCase()));
    }
}
