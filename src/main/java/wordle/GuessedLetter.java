package wordle;



import java.awt.*;

/**
 * A class for getting the guessed letter and setting their colors
 */
public class GuessedLetter {
    private String letter;
    private Color color = Color.WHITE;

    public GuessedLetter(String letter) {
        this.letter = letter;
    }

    /**
     * Getter and setter
     */
    public String getLetter() {
        return letter;
    }
    public void setLetter(String letter) {
        this.letter = letter;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
}
