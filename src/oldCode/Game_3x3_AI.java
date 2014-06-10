package oldCode;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;

/**
 * Created by darya on 17.05.14.
 */
public class Game_3x3_AI extends JFrame implements ActionListener, GameInterf {
    private final int sounds = (new GetSettings()).getSounds();
    private static int SIZE = 3;
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private JButton backToMenuButton;
    private JButton newGameButton;
    private JLabel label;
    private int emptyCells = 9;
    private final String name1 = "Player1";
    private final String name2 = "AI";
    private boolean win = false;
    private JFrame mainFrame;
    private int[][] weights = new int[3][3];
    private boolean flag = true;

    public void initGame() {
        mainFrame = new JFrame("Игрок против компьютера");
        mainFrame.setSize(150, 150);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        new Game();

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

        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        initGame();
    }

    private class Game extends JFrame implements ActionListener {
        Game() {
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridLayout(SIZE, SIZE));
            mainFrame.add(centerPanel, BorderLayout.CENTER);

            JPanel buttonsPanel = new JPanel();
            JPanel labelPanel = new JPanel();

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

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].addActionListener(this);
                    centerPanel.add(buttons[i][j]);
                }
            }

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton theButton = (JButton) e.getSource();
            if (theButton == backToMenuButton) {
                mainFrame.dispose();
            }
            if (theButton == newGameButton) {
                mainFrame.dispose();
                initGame();
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (theButton == buttons[i][j]) {
                        playSound();
                        theButton.setText(whoseTurn());
                        flag = false;
                        theButton.setEnabled(false);
                        checkWin();
                        setWeights(i, j);
                        emptyCells -= 1;
                        runAI();
                    }
                }
            }
        }
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/sound.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue((float)(new GetSettings()).getSounds() - 100);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
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
                showMessage(buttons[i][0]);
                win = true;
                break;
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
                showMessage(buttons[0][i]);
                win = true;
                break;
            }
        }
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().equals("")) {
            showMessage(buttons[0][0]);
            win = true;
        }
        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                !buttons[0][2].getText().equals("")) {
            showMessage(buttons[0][2]);
            win = true;
        }
        if (!win && emptyCells == 0) {
//            JOptionPane.showMessageDialog(this, "Ничья", "Итог", JOptionPane.DEFAULT_OPTION);
            newGame("Ничья");
        }
    }

    public void newGame(String win) {
        final JFrame frame = new JFrame("Итог");
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(180, 130);
        frame.add(new JLabel(win), BorderLayout.NORTH);
        frame.add(new JLabel("Начать новую игру?"), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setSize(100, 50);
        JButton yes = new JButton("Да");
        JButton no = new JButton("Нет");
        panel.add(yes, BorderLayout.WEST);
        panel.add(no, BorderLayout.EAST);
        frame.add(panel, BorderLayout.SOUTH);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Game_3x3_AI();
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.setVisible(true);
    }

    @Override
    public void showMessage(JButton buttons) {
        if (buttons.getText().equals("X")) {
//            JOptionPane.showMessageDialog(this, "Победил " + name1, "Итог", JOptionPane.DEFAULT_OPTION);
            newGame("Победил " + name1);
        } else {
//            JOptionPane.showMessageDialog(this, "Победил " + name2, "Итог", JOptionPane.DEFAULT_OPTION);
            newGame("Победил " + name2);
        }
    }

    @Override
    public String whoseTurn() {
        if (emptyCells != 0 && !flag) {
            label.setText("Ход " + name1);
            return "O";
        }
        label.setText("Ход " + name2);
        return "X";
    }
}
