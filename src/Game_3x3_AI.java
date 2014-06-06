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
import java.util.Timer;

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
                        checkVin();
                        setWeights(i, j);
                        emptyCells -= 1;
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
                    System.out.println(max);    //
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
        setWeights(cellIndex.get(0), cellIndex.get(1));
        emptyCells -= 1;
        checkVin();

    }

    public void setWeights(int i, int j) {
        checkXX();
        weights[i][j] = weights[i][j] - 100000;
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
            weights[1][1] = weights[1][1] + 1000;
        }
        if ((buttons[0][0].getText().equals(buttons[2][0].getText())&& !buttons[0][0].getText().equals(""))) {
            weights[1][0] = weights[1][0] + 1000;
        }
        if ((buttons[0][0].getText().equals(buttons[0][2].getText())&& !buttons[0][0].getText().equals(""))) {
            weights[0][1] = weights[0][1] + 1000;
        }
        if ((buttons[2][0].getText().equals(buttons[2][2].getText())&& !buttons[2][0].getText().equals(""))) {
            weights[2][1] = weights[2][1] + 1000;
        }
        if ((buttons[0][2].getText().equals(buttons[2][2].getText())&& !buttons[0][2].getText().equals(""))) {
            weights[1][2] = weights[1][2] + 1000;
        }
        if ((buttons[0][0].getText().equals(buttons[1][1].getText())&& !buttons[0][0].getText().equals(""))) {
            weights[2][2] = weights[2][2] + 1000;
        }
        if ((buttons[1][1].getText().equals(buttons[2][2].getText())&& !buttons[1][1].getText().equals(""))) {
            weights[0][0] = weights[0][0] + 1000;
        }
        if ((buttons[2][0].getText().equals(buttons[1][1].getText())&& !buttons[2][0].getText().equals(""))) {
            weights[0][2] = weights[0][2] + 1000;
        }
        if ((buttons[0][2].getText().equals(buttons[1][1].getText())&& !buttons[0][2].getText().equals(""))) {
            weights[2][0] = weights[2][0] + 1000;
        }
        for (int i = 0; i < SIZE - 1; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if ((buttons[i][j].getText().equals(buttons[i][j + 1].getText()) && j + 1 < SIZE && !buttons[i][j].getText().equals(""))) {
                    if (j - 1 >= 0) {
                        weights[i][j - 1] = weights[i][j - 1] + 1000;
                    }
                    if (j + 2 < SIZE) {
                        weights[i][j + 2] = weights[i][j + 2] + 1000;
                    }
                }
                if ((buttons[j][i].getText().equals(buttons[j + 1][i].getText()) && j + 1 < SIZE && !buttons[j][i].getText().equals(""))) {
                    if (j - 1 >= 0) {
                        weights[j - 1][i] = weights[j - 1][i] + 1000;
                    }
                    if (j + 2 < SIZE) {
                        weights[j + 2][i] = weights[j + 2][i] + 1000;
                    }
                }
            }
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
//            JOptionPane.showMessageDialog(this, "Ничья", "Итог", JOptionPane.DEFAULT_OPTION);
            newGame("Ничья");
        }
    }

    public void newGame(String win){
        final JFrame frame = new JFrame("Итог");
        frame.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        frame.setDefaultCloseOperation(1);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(180,130);
        frame.add(new JLabel(win), BorderLayout.NORTH);
        frame.add(new JLabel("Начать новую игру?"), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setSize(100, 50);
        JButton yes = new JButton("Да");
        JButton no = new JButton("Нет");
        panel.add(yes,BorderLayout.WEST);
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
                try {
                    new Menu();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
            return "O";
        }
        return "X";
    }
}

