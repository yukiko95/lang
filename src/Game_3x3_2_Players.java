import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by darya on 17.05.14.
 */

public class Game_3x3_2_Players extends JFrame implements ActionListener, GameInterf {
    private final int sounds = (new GetSettings()).getSounds();
    private static int SIZE = 3;
    private JButton[][] matrixButtons = new JButton[SIZE][SIZE];
    private JButton backToMenuButton;
    private int emptyCells = 9;
    private final String name1 = "Player1";
    private final String name2 = "Player2";
    private boolean vin = false;
    private JFrame mainFrame = new JFrame("2 игрока");

    @Override
    public void actionPerformed(ActionEvent e) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame.setSize(300, 200);
                mainFrame.setResizable(false);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                mainFrame.setLayout(new BorderLayout());
                new Game();
            }
        });

    }

    private class Game extends JFrame implements ActionListener {
        Game() {
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridLayout(SIZE, SIZE));
            mainFrame.add(centerPanel, "Center");

            backToMenuButton = new JButton();
            backToMenuButton.setSize(15, 25);
            backToMenuButton.addActionListener(this);
            mainFrame.add(backToMenuButton, "North");

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    matrixButtons[i][j] = new JButton();
                    matrixButtons[i][j].addActionListener(this);
                    centerPanel.add(matrixButtons[i][j]);
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
                    if (theButton == matrixButtons[i][j]) {
                        theButton.setText(whoseTurn());
                        theButton.setEnabled(false);
                        emptyCells--;
                        checkVin();
                    }
                }
            }
        }
    }

    @Override
    public String whoseTurn() {
        if (emptyCells != 0) {
            if (emptyCells % 2 == 0) {
                return name2;
            }
        }
        return name1;
    }

    @Override
    public void checkVin() {
        for (int i = 0; i < SIZE; i++) {
            if (matrixButtons[i][0].getText().equals(matrixButtons[i][1].getText()) &&
                    matrixButtons[i][1].getText().equals(matrixButtons[i][2].getText()) &&
                    matrixButtons[i][1].getText() != null) {
                showMessage(matrixButtons[i][0]);
            }
            if (matrixButtons[0][i].getText().equals(matrixButtons[1][i].getText()) &&
                    matrixButtons[1][i].getText().equals(matrixButtons[2][i].getText()) &&
                    matrixButtons[1][i].getText() != null) {
                showMessage(matrixButtons[0][i]);
                vin = true;
            }
            if (matrixButtons[0][0].getText().equals(matrixButtons[1][1].getText()) &&
                    matrixButtons[1][1].getText().equals(matrixButtons[2][2].getText()) &&
                    matrixButtons[0][0].getText() != null) {
                showMessage(matrixButtons[0][0]);
                vin = true;
            }
            if (matrixButtons[0][2].getText().equals(matrixButtons[1][1].getText()) &&
                    matrixButtons[1][1].getText().equals(matrixButtons[2][0].getText()) &&
                    matrixButtons[0][2].getText() != null) {
                showMessage(matrixButtons[0][2]);
                vin = true;
            }
            if (emptyCells == 0 && !vin) {
                JOptionPane.showMessageDialog(this, "Ничья", "Итог", JOptionPane.DEFAULT_OPTION);
            }
        }
    }

    @Override
    public void showMessage(JButton matrixButtons) {
        if (matrixButtons.getText().equals("X")) {
            JOptionPane.showMessageDialog(this, "Победил " + name1, "Итог", JOptionPane.DEFAULT_OPTION);
        } else {
            JOptionPane.showMessageDialog(this, "Победил " + name2, "Итог", JOptionPane.DEFAULT_OPTION);
        }
    }
}
