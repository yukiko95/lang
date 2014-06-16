package newCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFieldInfinity extends JFrame implements ActionListener {
    private int SIZE = 20;
    private String[] titlesJFrame = new String[] {"Игра с компьютером", "2 игрока"};

    private String[][] namesGameMode = new String[][] {
            {"игрок", "компьютер"},
            {"игрок 1", "игрок 2"},
    };
    private int players;
    private int sound;
    private int turnPlayer; // 0 - player1, 1 - player2/bot
    private JButton[][] field;
    private JLabel infoLabel;

    public GameFieldInfinity(int players, int sound) {
        this.players = players;
        this.sound = sound;
        field = new JButton[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = new JButton("");
//                field[i][j].setFont(new Font("Arial", Font.ITALIC, 5));
                field[i][j].addActionListener(this);
            }
        }
        infoLabel = new JLabel("");
        initGame();

        setTitle(titlesJFrame[players - 1]);
        setSize(600, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        addWindowListener(new WindowAdapter() {
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

        add(createButtonsPanel(), BorderLayout.NORTH);
        add(createFieldPanel(), BorderLayout.CENTER);
        add(createInfoPanel(), BorderLayout.SOUTH);
    }

    private Component createInfoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(new JLabel("Ходит: "));
        infoPanel.add(infoLabel);
        return infoPanel;
    }

    private Component createFieldPanel() {
        JPanel fieldPanel = new JPanel(new GridLayout(SIZE, SIZE, 1, 1));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                fieldPanel.add(field[i][j]);
            }
        }
        return fieldPanel;
    }

    private Component createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newGameButton = new JButton(new ImageIcon("pictures/update.png"));
        JButton exitButton = new JButton(new ImageIcon("pictures/back.png"));
        buttonsPanel.add(exitButton);
        buttonsPanel.add(newGameButton);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGame();
            }
        });

        exitButton.addActionListener(new ExitActionListener(this));
        return buttonsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (field[i][j] == clickedButton) {
                    clickedButton.setText(turn());
                    clickedButton.setEnabled(false);
                    String winner = checkWin(field);
                    if (winner != null) {
                        showGameDialog(winner);
                    }
                    if (players == 1) {
                        runAI();
                    }
                }
            }
        }
    }

    private void runAI() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (field[i][j].getText().equals("")) {
                    field[i][j].setText("O");
                    field[i][j].setEnabled(false);
                    turn();
                    return;
                }
            }
        }

    }

    private void showGameDialog(String winner) {
        int res = JOptionPane.showConfirmDialog(null,
                winner + "\nНачать новую игру ?",
                "Итог",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        initGame();
        if (res == JOptionPane.NO_OPTION) {
            this.dispose();
        }
    }

    private String turn() {
        turnPlayer = 1 - turnPlayer;
        infoLabel.setText(namesGameMode[players - 1][turnPlayer]);
        return turnPlayer == 0 ? "O" : "X";
    }

    private class ExitActionListener implements ActionListener {
        JFrame frame;

        public ExitActionListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    }

    private void initGame() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j].setText("");
                field[i][j].setEnabled(true);
            }
        }
        turnPlayer = 0;
        infoLabel.setText(namesGameMode[players - 1][turnPlayer]);
    }

    public String checkWin(JButton[][] buttons) {
        int emptyCells = 0;
        char[][] p = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                try {
                    p[i][j] = buttons[i][j].getText().charAt(0);
                } catch (Exception e) {
                    emptyCells += 1;
                    p[i][j] = '\0';
                }
            }
        }
//        for (int i = 0; i < SIZE; i++) {
//            if (p[i][0] != '\0' && p[i][0] == p[i][1] && p[i][0] == p[i][2]) {
//                return getNameByChar(p[i][0]);
//            }
//            if (p[0][i] != '\0' && p[0][i] == p[1][i] && p[0][i] == p[2][i]) {
//                return getNameByChar(p[0][i]);
//            }
//        }
//        if (p[0][0] != '\0' && p[0][0] == p[1][1] && p[0][0] == p[2][2]) {
//            return getNameByChar(p[0][0]);
//        }
//        if (p[0][2] != '\0' && p[0][2] == p[1][1] && p[0][2] == p[2][0]) {
//            return getNameByChar(p[0][2]);
//        }
        if (emptyCells == 0) {
            return getNameByChar('\0');
        }
        return null;
    }

    private String getNameByChar(char ch) {
        if (ch != '\0') {
            return "Победил " + (ch == 'X' ? namesGameMode[players - 1][0] : namesGameMode[players - 1][1]);
        }
        return "Ничья";
    }
}
