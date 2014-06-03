import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Created by darya on 17.05.14.
 */
public class Game_3x3_AI  extends JFrame implements ActionListener,  GameInterf{
    private final int sounds = (new GetSettings()).getSounds();
    private static int SIZE = 3;
    private JButton[][] matrixButtons = new JButton[SIZE][SIZE];
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

            if (!flag){
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                    }
                }
            }

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (theButton == matrixButtons[i][j]) {
                        theButton.setText(whoseTurn());
                        theButton.setEnabled(false);
                        emptyCells -= 1;
                        checkVin();
                    }
                }
            }
        }
    }

    public void runAI(){
        flag = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

            }
        }
    }

    public void runPlayer(){
        flag = false;
    }

    @Override
    public void checkVin() {
        for (int i = 0; i < SIZE; i++) {
            if (matrixButtons[i][0].getText().equals("")) {
                continue;
            }
            boolean check = true;
            for (int j = 1; j < SIZE; j++) {
                if (!matrixButtons[i][j - 1].getText().equals(matrixButtons[i][j].getText())) {
                    check = false;
                    break;
                }
            }
            if (check) {
                showMessage(matrixButtons[i][0]);
                vin = true;
                break;
            }
            if (i == 0 || matrixButtons[0][i].getText().equals("")) {
                continue;
            }
            check = true;
            for (int j = 0; j < SIZE; j++) {
                if (!matrixButtons[j][i - 1].getText().equals(matrixButtons[j][i].getText())) {
                    check = false;
                    break;
                }
            }
            if (check) {
                showMessage(matrixButtons[0][i]);
                vin = true;
                break;
            }
        }
        if (matrixButtons[0][0].getText().equals(matrixButtons[1][1].getText()) &&
                matrixButtons[1][1].getText().equals(matrixButtons[2][2].getText()) &&
                !matrixButtons[0][0].getText().equals("")) {
            showMessage(matrixButtons[0][0]);
            vin = true;
        }
        if (matrixButtons[0][2].getText().equals(matrixButtons[1][1].getText()) &&
                matrixButtons[1][1].getText().equals(matrixButtons[2][0].getText()) &&
                !matrixButtons[0][2].getText().equals("")) {
            showMessage(matrixButtons[0][2]);
            vin = true;
        }
        if (!vin && emptyCells == 0) {
            JOptionPane.showMessageDialog(this, "Ничья", "Итог", JOptionPane.DEFAULT_OPTION);
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

    @Override
    public String whoseTurn() {
        if (emptyCells != 0 && !flag) {
            return "O";
        }
        return "X";
    }
}
