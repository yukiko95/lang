package newCode;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by crazyministr on 6/10/14.
 */
public class Game_3x3_AI extends JFrame implements ActionListener, GameInterface {
    private JFrame mainFrame;
    private JButton backToMenuButton;
    private JButton newGameButton;
    private JLabel label;
    private static int SIZE = 3;
    private JButton[][] buttons;
    private int[][] weights;
    private int emptyCells = 9;
    private final int sounds;
    private final String name1 = "Player1";
    private final String name2 = "Player2";
    private boolean win = false;
    private boolean flag = true;

    public Game_3x3_AI(final int sounds) {
        this.sounds = sounds;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        initGame();
    }

    @Override
    public void initGame() {
        mainFrame = new JFrame("2 игрока");
        mainFrame.setSize(150, 150);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        buttons = new JButton[SIZE][SIZE];
        new GameImpl();
        mainFrame.setVisible(true);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Menu.getFrames()[0].setEnabled(false);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Menu.getFrames()[0].setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                Menu.getFrames()[0].setEnabled(true);
            }
        });
    }

    private class GameImpl extends JFrame implements Game, ActionListener {
        public GameImpl() {
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridLayout(SIZE, SIZE));
            mainFrame.add(centerPanel, BorderLayout.CENTER);

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setBackground(new Color(229, 223, 198));
            JPanel labelPanel = new JPanel();
            labelPanel.setBackground(new Color(229, 223, 198));

            backToMenuButton = new JButton(new ImageIcon("pictures/back.png"));
            newGameButton = new JButton(new ImageIcon("pictures/update.png"));

            backToMenuButton.addActionListener(this);
            newGameButton.addActionListener(this);
            buttonsPanel.add(backToMenuButton, BorderLayout.WEST);
            buttonsPanel.add(newGameButton, BorderLayout.EAST);

            mainFrame.add(buttonsPanel, BorderLayout.NORTH);

            label = new JLabel("Ход " + name1);
            labelPanel.add(label, BorderLayout.CENTER);

            mainFrame.add(labelPanel, BorderLayout.SOUTH);

            weights = new int[3][3];

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    buttons[i][j] = new JButton("");
                    buttons[i][j].setBackground(Color.white);
                    buttons[i][j].setFont(new Font(null, Font.ITALIC, 8));
                    buttons[i][j].addActionListener(this);
                    centerPanel.add(buttons[i][j]);
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton theButton = (JButton) e.getSource();
            if (theButton == backToMenuButton) {
                clearField();
                mainFrame.dispose();
            }
            if (theButton == newGameButton) {
                clearField();
                return;
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (theButton == buttons[i][j]) {
                        theButton.setText(whoseTurn());
                        theButton.setEnabled(false);
                        emptyCells -= 1;
                        checkWin();
                        setWeights(i,j);
                        runAI();
                    }
                }
            }
        }

        @Override
        public String whoseTurn() {
            if (sounds != 0) {
                playSound();
            }
            label.setText("Ход " + (flag ? name2 : name1));
            flag = !flag;
            return flag ? "O" : "X";
        }

        public void runAI() {
            int max = weights[0][0];
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (weights[i][j] > max) {
                        max = weights[i][j];
                    }
                }
            }
            Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
            int k = 0;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    if (weights[i][j] == max) {
                        list.add(i);
                        list.add(j);
                        map.put(k, list);
                        k++;
                    }
                }
            }
            int index = (int) (Math.random() * (map.size() - 1));
            ArrayList<Integer> cellIndex = map.get(index);
            playSound();
            buttons[cellIndex.get(0)][cellIndex.get(1)].setText(whoseTurn());
            flag = true;
            buttons[cellIndex.get(0)][cellIndex.get(1)].setEnabled(false);
            setWeights(cellIndex.get(0), cellIndex.get(1));
            emptyCells -= 1;
            checkWin();
        }

        public void setWeights(int i, int j) {
            checkXX();
            weights[i][j] = weights[i][j] - 100000;
            if (i - 1 >= 0) {
                if (j - 1 >= 0) {
                    weights[i - 1][j - 1] = weights[i - 1][j - 1] + 1;
                }
                if (j + 1 < SIZE) {
                    weights[i - 1][j + 1] = weights[i - 1][j + 1] + 1;
                }
                weights[i - 1][j] = weights[i - 1][j] + 1;
            }
            if (i + 1 < SIZE) {
                if (j - 1 >= 0) {
                    weights[i + 1][j - 1] = weights[i + 1][j - 1] + 1;
                }
                if (j + 1 < SIZE) {
                    weights[i + 1][j + 1] = weights[i + 1][j + 1] + 1;
                }
                weights[i + 1][j] = weights[i + 1][j] + 1;
            }
            if (j - 1 >= 0) {
                weights[i][j - 1] = weights[i][j - 1] + 1;
            }
            if (j + 1 < SIZE) {
                weights[i][j + 1] = weights[i][j + 1] + 1;
            }
            for (int k = 0; k < SIZE; k++) {
                for (int n = 0; n < SIZE; n++) {
                    System.out.print(weights[k][n] + " ");
                }
                System.out.println();
            }
            System.out.println("---------------");
        }

        public void checkXX() {
            if ((buttons[1][0].getText().equals(buttons[1][2].getText()) && !buttons[1][0].getText().equals("")) ||
                    (buttons[0][0].getText().equals(buttons[2][2].getText()) && !buttons[0][0].getText().equals("")) ||
                    (buttons[0][2].getText().equals(buttons[2][0].getText()) && !buttons[0][2].getText().equals("")) ||
                    (buttons[0][1].getText().equals(buttons[2][1].getText()) && !buttons[0][1].getText().equals(""))) {
                weights[1][1] = weights[1][1] + 10000;
            }
            if ((buttons[0][0].getText().equals(buttons[2][0].getText()) && !buttons[0][0].getText().equals(""))) {
                weights[1][0] = weights[1][0] + 10000;
            }
            if ((buttons[0][0].getText().equals(buttons[0][2].getText()) && !buttons[0][0].getText().equals(""))) {
                weights[0][1] = weights[0][1] + 10000;
            }
            if ((buttons[2][0].getText().equals(buttons[2][2].getText()) && !buttons[2][0].getText().equals(""))) {
                weights[2][1] = weights[2][1] + 10000;
            }
            if ((buttons[0][2].getText().equals(buttons[2][2].getText()) && !buttons[0][2].getText().equals(""))) {
                weights[1][2] = weights[1][2] + 10000;
            }
            if ((buttons[0][0].getText().equals(buttons[1][1].getText()) && !buttons[0][0].getText().equals(""))) {
                weights[2][2] = weights[2][2] + 10000;
            }
            if ((buttons[1][1].getText().equals(buttons[2][2].getText()) && !buttons[1][1].getText().equals(""))) {
                weights[0][0] = weights[0][0] + 10000;
            }
            if ((buttons[2][0].getText().equals(buttons[1][1].getText()) && !buttons[2][0].getText().equals(""))) {
                weights[0][2] = weights[0][2] + 10000;
            }
            if ((buttons[0][2].getText().equals(buttons[1][1].getText()) && !buttons[0][2].getText().equals(""))) {
                weights[2][0] = weights[2][0] + 10000;
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (j + 1 < SIZE) {
                        if ((buttons[i][j].getText().equals(buttons[i][j + 1].getText()) && !buttons[i][j].getText().equals(""))) {
                            if (j - 1 >= 0) {
                                weights[i][j - 1] = weights[i][j - 1] + 10000;
                            }
                            if (j + 2 < SIZE) {
                                weights[i][j + 2] = weights[i][j + 2] + 10000;
                            }
                        }
                        if ((buttons[j][i].getText().equals(buttons[j + 1][i].getText()) && !buttons[j][i].getText().equals(""))) {
                            if (j - 1 >= 0) {
                                weights[j - 1][i] = weights[j - 1][i] + 10000;
                            }
                            if (j + 2 < SIZE) {
                                weights[j + 2][i] = weights[j + 2][i] + 10000;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void checkWin() {
            for (int i = 0; i < SIZE; i++) {
                if (buttons[i][0].getText().equals("")) {
                    continue;
                }
                boolean check = true;
                for (int j = 1; j < SIZE; j++) {
                    if (!buttons[i][j - 1].getText().equals(buttons[i][j].getText())) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    win = true;
                    showMessage(buttons[i][0]);
                    return;
                }
                if (i == 0 || buttons[0][i].getText().equals("")) {
                    continue;
                }
                check = true;
                for (int j = 0; j < SIZE; j++) {
                    if (!buttons[j][i - 1].getText().equals(buttons[j][i].getText())) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    win = true;
                    showMessage(buttons[0][i]);
                    return;
                }
            }
            if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                    buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                    !buttons[0][0].getText().equals("")) {
                win = true;
                showMessage(buttons[0][0]);
                return;
            }
            if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                    buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                    !buttons[0][2].getText().equals("")) {
                win = true;
                showMessage(buttons[0][2]);
                return;
            }
            if (!win && emptyCells == 0) {
                newGame("Ничья");
            }
        }

        @Override
        public void playSound() {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/sound.wav").getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception ex) {
                System.out.println("Error with playing sound.");
                ex.printStackTrace();
            }
        }

        @Override
        public void showMessage(JButton button) {
            if (button.getText().equals("X")) {
                newGame("Победил " + name1);
            } else {
                newGame("Победил " + name2);
            }
        }

        @Override
        public void newGame(String win) {
            int res = JOptionPane.showConfirmDialog(null,
                    win + "\nНачать новую игру ?",
                    "Итог",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            clearField();
            if (res == JOptionPane.NO_OPTION) {
                mainFrame.dispose();
            }
        }

        private void clearField() {
            flag = true;
            win = false;
            emptyCells = 9;
            label.setText("Ход " + name1);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(true);
                    weights[i][j] = 0;
                }
            }
        }
    }
}
