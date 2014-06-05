import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by darya on 17.05.14.
 */
public class Game_3x3_AI extends JFrame implements ActionListener, GameInterf {
    private final int sounds = (new GetSettings()).getSounds();
    private static int SIZE = 3;
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private JButton backToMenuButton;
    private int emptyCells = 9;
    private final String name1 = "Player1";
    private final String name2 = "AI";
    private boolean vin = false;
    private JFrame mainFrame = new JFrame("Игрок против компьютера");
    private int[][] weights = new int[3][3];
    private boolean flag = true;

    @Override
    public void actionPerformed(ActionEvent e) {
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

    private class Game extends JFrame implements ActionListener {
        Game() {
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridLayout(SIZE, SIZE));
            mainFrame.add(centerPanel, BorderLayout.CENTER);

            backToMenuButton = new JButton("working!!");
            backToMenuButton.addActionListener(this);
            mainFrame.add(backToMenuButton, BorderLayout.NORTH);

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
                try {
                    new Menu();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (theButton == buttons[i][j]) {
                        theButton.setText(whoseTurn());
                        flag = false;
                        theButton.setEnabled(false);
                        emptyCells -= 1;
                        checkVin();
                        setWeights(i, j);
                        runAI();
                    }
                }
            }
        }
    }

    public void runAI() {
        int max = weights[0][0];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(weights[i][j] + " ");
                if (weights[i][j] > max) {
                    max = weights[i][j];
                }
            }
            System.out.println();
        }
        System.out.println("---------------");
        Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
        int k = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ArrayList<Integer> list = new ArrayList<Integer>();
                if (weights[i][j] == max) {
                    System.out.println(max);
                    list.add(i);
                    list.add(j);
                    map.put(k, list);
                    k++;
                }
            }
        }
        int index = (int) (Math.random() * (map.size() - 1));
        ArrayList<Integer> cellIndex = map.get(index);
        buttons[cellIndex.get(0)][cellIndex.get(1)].setText(whoseTurn());
        flag = true;
        buttons[cellIndex.get(0)][cellIndex.get(1)].setEnabled(false);
        emptyCells -= 1;
        checkVin();
        setWeights(cellIndex.get(0), cellIndex.get(1));
    }

    public void setWeights(int i, int j) {
        checkXX();
        weights[i][j] = -10000;
        if (i - 1 >= 0) {
            if (j - 1 >= 0) {
                weights[i - 1][j - 1] = weights[i - 1][j - 1] + 10;
            }
            if (j + 1 < SIZE) {
                weights[i - 1][j + 1] = weights[i - 1][j + 1] + 10;
            }
            weights[i - 1][j] = weights[i - 1][j] + 10;
        }
        if (i + 1 < SIZE) {
            if (j - 1 >= 0) {
                weights[i + 1][j - 1] = weights[i + 1][j - 1] + 10;
            }
            if (j + 1 < SIZE) {
                weights[i + 1][j + 1] = weights[i + 1][j + 1] + 10;
            }
            weights[i + 1][j] = weights[i + 1][j] + 10;
        }
        if (j - 1 >= 0) {
            weights[i][j - 1] = weights[i][j - 1] + 10;
        }
        if (j + 1 < SIZE) {
            weights[i][j + 1] = weights[i][j + 1] + 10;
        }
    }

    public void checkXX() {
        for (int i = 0; i < SIZE - 1; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (buttons[i][j].getText().equals(buttons[i][j + 1])) {
                    if (j - 1 >= 0) {
                        weights[i][j - 1] = 1000;
                    }
                    if (j + 2 < SIZE) {
                        weights[i][j + 2] = 1000;
                    }
                }
                if (buttons[j][i].getText().equals(buttons[j][i + 1])) {
                    if (j - 1 >= 0) {
                        weights[j - 1][i] = 1000;
                    }
                    if (j + 2 < SIZE) {
                        weights[j + 2][i] = 1000;
                    }
                }
                if (buttons[i][j].getText().equals(buttons[i + 1][j + 1])) {
                    if (j - 1 >= 0 && i - 1 >= 0) {
                        weights[i - 1][j - 1] = 1000;
                    }
                    if (j + 2 < SIZE && i + 2 < SIZE) {
                        weights[i + 2][j + 2] = 1000;
                    }
                }
            }
        }
        for (int i = SIZE - 1; i > 0; i--) {
            for (int j = SIZE - 1; j > 0; j--) {
                if (buttons[i][j].getText().equals(buttons[i - 1][j - 1])) {
                    if (j - 1 >= 0 && i - 1 >= 0) {
                        weights[i - 1][j - 1] = 1000;
                    }
                    if (j + 1 < SIZE && i + 1 < SIZE) {
                        weights[i + 1][j + 1] = 1000;
                    }
                }
            }
        }
        if (buttons[1][0].getText().equals(buttons[1][2]) || buttons[0][0].getText().equals(buttons[2][2]) ||
                buttons[0][2].getText().equals(buttons[2][0].getText())) {
            weights[1][1] = 1000;
        }
        if (buttons[0][0].getText().equals(buttons[2][0])) {
            weights[1][0] = 1000;
        }
        if (buttons[0][0].getText().equals(buttons[0][2])) {
            weights[0][1] = 1000;
        }
        if (buttons[2][0].getText().equals(buttons[2][2])) {
            weights[2][1] = 1000;
        }
        if (buttons[0][2].getText().equals(buttons[2][2])) {
            weights[1][2] = 1000;
        }
    }

    @Override
    public void checkVin() {
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
                vin = true;
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
                vin = true;
                break;
            }
        }
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().equals("")) {
            showMessage(buttons[0][0]);
            vin = true;
        }
        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                !buttons[0][2].getText().equals("")) {
            showMessage(buttons[0][2]);
            vin = true;
        }
        if (!vin && emptyCells == 0) {
            JOptionPane.showMessageDialog(this, "Ничья", "Итог", JOptionPane.DEFAULT_OPTION);
        }
        return;
    }

    @Override
    public void showMessage(JButton buttons) {
        if (buttons.getText().equals("X")) {
            JOptionPane.showMessageDialog(this, "Победил " + name1, "Итог", JOptionPane.DEFAULT_OPTION);
        } else {
            JOptionPane.showMessageDialog(this, "Победил " + name2, "Итог", JOptionPane.DEFAULT_OPTION);
        }
    }

    @Override
    public String whoseTurn() {
        if (emptyCells != 0 && !flag) {
            return "O";
        }
        return "X";
    }
}

