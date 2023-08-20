package wordle;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * GUI for wordle which observes model so that updates the GUI
 * when the model state is changed
 */
public class WordleView implements Observer {
    private static final int widthAndHeight = 50; // label size
    private static final int gap = 5;
    private static final String defaultText = " ";
    private static final Color defaultColor = Color.WHITE;
    private static final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);

    /**
     * x-axis and y-xis for keyboard and label
     */
    private static final int labelX = 250;
    private static final int labelY = 20;
    private static final int keyboardX = 125;
    private static final int keyboardY = 380;
    private static final String[] keyBoard =
            {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
            "A", "S", "D", "F", "G", "H", "J", "K", "L",
            "Enter", "Z", "X", "C", "V", "B", "N", "M", "Delete"};

    /**
     * Define the components, models, and controllers required for the view
     */
    private final JFrame jf = new JFrame();
    private final JLabel[][] LABELS = new JLabel[6][5];
    private final JButton[] BUTTONS = new JButton[28];
    private final JButton replayBtn;
    private final JLabel labelWord;
    private final WordleModel wordleModel = new WordleModel();
    private final WordleController wordleController = new WordleController(wordleModel);
    public WordleView(boolean notCount, boolean show, boolean random, boolean restart) {
        //Start or restart game
        if (!restart) {
            GameUtils.getGameUtils().startGame(notCount, show, random);
        } else {
            GameUtils.getGameUtils().restartGame();
        }

        //Register to observe model
        wordleModel.registerObserver(this);

        //Set the window size
        JFrame.setDefaultLookAndFeelDecorated(true);
        jf.setTitle("Wordle Game");
        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set container
        JPanel panel = new JPanel();
        panel.setLayout(null);

        //Initialize the target word
        labelWord = new JLabel("Target word: " + GameUtils.getGameUtils().getWord());
        labelWord.setBounds(labelX + (widthAndHeight + gap) * 5 + 50, labelY + (widthAndHeight + gap) * 2, 120, 30);
        labelWord.setVisible(show);
        panel.add(labelWord);


        // Initialize three options for user to select
        JCheckBox notCountCheckBox = new JCheckBox("Not count if the word not exists");
        notCountCheckBox.setBounds(10, 50, 220, 50);
        notCountCheckBox.setSelected(notCount);
        notCountCheckBox.addItemListener(e -> GameUtils.getGameUtils().setNotCount(notCountCheckBox.isSelected()));
        panel.add(notCountCheckBox);

        JCheckBox showCheckBox = new JCheckBox("Display target word for testing");
        showCheckBox.setBounds(10, 100, 220, 50);
        showCheckBox.setSelected(show);
        showCheckBox.addItemListener(e -> {
            GameUtils.getGameUtils().setDisplayWord(showCheckBox.isSelected());
            labelWord.setVisible(showCheckBox.isSelected());
        });
        panel.add(showCheckBox);

        JCheckBox randomCheckBox = new JCheckBox("Selecting word randomly");
        randomCheckBox.setBounds(10, 150, 220, 50);
        randomCheckBox.setSelected(random);
        randomCheckBox.addItemListener(e -> GameUtils.getGameUtils().setRandomWord(randomCheckBox.isSelected()));
        panel.add(randomCheckBox);


        // Initialize wordle panel
        initLabels(panel);

        /**
         * Initialize replay button
         */
        replayBtn = new JButton("Replay");
        replayBtn.setBounds(labelX + (widthAndHeight + gap) * 5 + 50, labelY + (widthAndHeight + gap) * 3, 120, 50);
        replayBtn.setEnabled(false);
        replayBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        replayBtn.addActionListener(e -> {
            jf.dispose();
            new WordleView(GameUtils.getGameUtils().isNotCount(), GameUtils.getGameUtils().isDisplayWord(), GameUtils.getGameUtils().isRandomWord(), true);
        });
        panel.add(replayBtn);

        // Initialize keyboard
        initKeyboard(panel);

        // Display interface
        jf.getContentPane().add(panel);
        jf.setVisible(true);
    }

    /**
    * Initialize labels for guessing words
    */
    private void initLabels (JPanel panel) {
        for (int i = 0; i < LABELS.length; i++) {
            for (int j = 0; j < LABELS[i].length; j++) {
                LABELS[i][j] = new JLabel(defaultText);
                LABELS[i][j].setFont(font);
                LABELS[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                LABELS[i][j].setHorizontalAlignment(JLabel.CENTER);
                LABELS[i][j].setVerticalAlignment(JLabel.CENTER);
                LABELS[i][j].setOpaque(true);
                LABELS[i][j].setBackground(defaultColor);
                LABELS[i][j].setBounds(labelX + (gap + widthAndHeight) * j, labelY + (gap + widthAndHeight) * i, widthAndHeight, widthAndHeight);
                panel.add(LABELS[i][j]);
            }
        }
    }
    /**
     * Initialize keyboard
     */
    private void initKeyboard (JPanel panel) {
        for (int i = 0; i < 28; i++) {
            if (i < 10) {
                BUTTONS[i] = createButton(keyBoard[i], keyboardX + (gap + widthAndHeight) * i, keyboardY, widthAndHeight);
            } else if (i < 19) {
                BUTTONS[i] = createButton(keyBoard[i], keyboardX + (gap + (int) (widthAndHeight * 1.12)) * (i - 10), keyboardY + (gap + widthAndHeight), (int) (widthAndHeight * 1.12));
            } else if (i == 19) {
                BUTTONS[i] = createButton(keyBoard[i], keyboardX, keyboardY + (gap + widthAndHeight) * 2, (int) (widthAndHeight * 1.54));
            } else if (i < 27) {
                BUTTONS[i] = createButton(keyBoard[i], keyboardX + gap + (int) (widthAndHeight * 1.54) + (gap + widthAndHeight) * (i - 20), keyboardY + (gap + widthAndHeight) * 2, widthAndHeight);
            } else {
                BUTTONS[i] = createButton(keyBoard[i], keyboardX + (int) (widthAndHeight * 1.54) + gap + (gap + widthAndHeight) * 7, keyboardY + (gap + widthAndHeight) * 2, (int) (widthAndHeight * 1.54));
            }
            panel.add(BUTTONS[i]);
        }
    }

    /**
     * Create the button with the action listener
     */
    private JButton createButton(String text, int x, int y, int width) {
        JButton btn = new JButton();
        btn.setFont(font);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btn.setHorizontalAlignment(JLabel.CENTER);
        btn.setVerticalAlignment(JLabel.CENTER);
        btn.setOpaque(true);
        btn.setBackground(defaultColor);
        btn.setText(text);
        btn.setBounds(x, y, width, widthAndHeight);
        btn.addActionListener(event -> wordleController.keyPressed(btn.getText()));
        return btn;
    }

    /**
     * Update the label and button status of the interface
     */
    private void updateButtons(List<GuessedLetter> letters) {
        for (JButton btn : BUTTONS) {
            String text = btn.getText();
            for (GuessedLetter guess : letters) {
                String letter = guess.getLetter();
                Color color = guess.getColor();
                if (text.equals(letter)) {
                    btn.setBackground(color);
                }
            }

        }
    }

    /**
     * Check if the text matches the letters in the letter list.
     * If it matches, set the background color of the button
     * to the color of the corresponding letter.
     */
    @Override
    public void update(Observable o, Object arg) {
        WordleModel.ModelChangedOperation operation = (WordleModel.ModelChangedOperation) arg;
        switch (operation.getType()) {
            case WordleModel.addLetter:
                LABELS[operation.getRow()][operation.getColumn()].setText(operation.getLetter());
                break;
            case WordleModel.delLetter:
                LABELS[operation.getRow()][operation.getColumn()].setText(defaultText);
                break;
            case WordleModel.guess:
                List<GuessedLetter> letters = wordleModel.getGuess(operation.getRow()).getLetters();
                JLabel[] labels = LABELS[operation.getRow()];
                for (int i = 0; i < labels.length; i++) {
                    if (!letters.isEmpty()) {
                        labels[i].setBackground(letters.get(i).getColor());
                    } else {
                        labels[i].setBackground(defaultColor);
                        labels[i].setText(defaultText);
                    }
                }
                updateButtons(letters);
                replayBtn.setEnabled(true);
                if (operation.isDisplayError()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "The guessed word does not exist in word list！",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

}
