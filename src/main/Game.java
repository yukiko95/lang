package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame {
    public static final int WIDTH_WINDOW_1 = 300;  //
    public static final int HEIGHT_WINDOW_1 = 350; // for field 3x3

    public static final int WIDTH_WINDOW_2 = 450;  //
    public static final int HEIGHT_WINDOW_2 = 500; // for field 20x20

    public static final String[][] gameTurns = new String[][] {
            {"игрок", "компьютер"},
            {"игрок 1", "игрок 2"},
    };

    public final int size;
    public final int players;
    public final String player1;
    public final String player2;
    public final Image imgCross;
    public final Image imgNought;

    private int[][] field;
    private int turnPlayer;
    private JLabel turnInfo;
    private JLabel numberOfWinsFirstPlayer;
    private JLabel numberOfWinsSecondPlayer;
    private JPanel[][] cellsPanel;
    private int numberOfMatches = 0;
    private AISmall aiSmall;
    private AIBig aiBig;

    public Game(int size, int players) {
        this.size = size;
        this.players = players;
        turnPlayer = 0;
        field = new int[size][size];
        player1 = gameTurns[players - 1][0];
        player2 = gameTurns[players - 1][1];
        cellsPanel = new JPanel[size][size];
        imgCross = new ImageIcon("images/cross.gif").getImage();
        imgNought = new ImageIcon("images/nought.gif").getImage();
        numberOfWinsFirstPlayer = new JLabel("0");
        numberOfWinsSecondPlayer = new JLabel("0");
        turnInfo = new JLabel(player1);
        if (players == 1 && size == TicTacToeMain.SIZE_SMALL_FIELD) {
            aiSmall = new AISmall(this);
        } else if (players == 1) {
            aiBig = new AIBig(this);
        }

        this.setTitle(players == 1 ? "C компьютером" : "Друг против друга");
        setSize(size == TicTacToeMain.SIZE_SMALL_FIELD ? WIDTH_WINDOW_1 : WIDTH_WINDOW_2,
                size == TicTacToeMain.SIZE_SMALL_FIELD ? HEIGHT_WINDOW_1 : HEIGHT_WINDOW_2);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        setLayout(new BorderLayout());
        add(buttonsPanel(), BorderLayout.NORTH);
        add(fieldPanel(), BorderLayout.CENTER);
        add(infoPanel(), BorderLayout.SOUTH);
    }

    private void initGame() {
        turnPlayer = 0;
        turnInfo.setText((numberOfMatches % 2 == 0) ? player1 : player2);
        if (players == 1) {
            turnInfo.setText(player1);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = 0;
                CellsPanel cell = (CellsPanel) cellsPanel[i][j];
                cell.setImg(null);
                cell.repaint();
                cell.setEnabled(true);
            }
        }
    }

    private Component infoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(new JLabel("Ходит: "));
        infoPanel.add(turnInfo);
        return infoPanel;
    }

    private Component fieldPanel() {
        JPanel fieldPanel = new JPanel(new GridLayout(size, size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellsPanel[i][j] = new CellsPanel(i, j);
                cellsPanel[i][j].addMouseListener(new CellsMouseListener(this, (CellsPanel) cellsPanel[i][j]));
                fieldPanel.add(cellsPanel[i][j]);
            }
        }
        return fieldPanel;
    }

    private Component buttonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 3, 3));

        JPanel firstPlayerPanel = new JPanel(new FlowLayout());
        firstPlayerPanel.add(new JLabel(player1 + ": "));
        firstPlayerPanel.add(numberOfWinsFirstPlayer);
        firstPlayerPanel.add(new JLabel(" побед"));

        JPanel secondPlayerPanel = new JPanel(new FlowLayout());
        secondPlayerPanel.add(new JLabel(player2 + ": "));
        secondPlayerPanel.add(numberOfWinsSecondPlayer);
        secondPlayerPanel.add(new JLabel(" побед"));

        JButton clearFieldButton = new JButton("Новая игра");
        JButton clearStatisticButton = new JButton("Очистить статискику");
        JButton exitButton = new JButton("Закончить игру");

        buttonsPanel.add(clearFieldButton);
        buttonsPanel.add(firstPlayerPanel);
        buttonsPanel.add(clearStatisticButton);
        buttonsPanel.add(secondPlayerPanel);
        buttonsPanel.add(exitButton);

        clearFieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGame();
            }
        });

        clearStatisticButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfWinsFirstPlayer.setText("0");
                numberOfWinsSecondPlayer.setText("0");
            }
        });

        exitButton.addActionListener(new Exit(this));
        return buttonsPanel;
    }

    public int whoseTurn() {
        return turnPlayer;
    }

    public void wasTurn() {
        turnPlayer = 1 - turnPlayer;
        turnInfo.setText(turnPlayer == 0 ? player1 : player2);
        if (!showWinnerMessage((size == TicTacToeMain.SIZE_SMALL_FIELD) ? checkWinSmall() : checkWinBig())
                && players == 1 && turnPlayer == 1) {
            if (size == TicTacToeMain.SIZE_SMALL_FIELD) {
                aiSmall.makeMove();
            } else {
                aiBig.makeMove();
            }
        }
    }

    public void updateField(int x, int y, int z) {
        field[x][y] = z;
    }

    public int[][] getField() {
        return field;
    }

    public CellsPanel getCell(int x, int y) {
        return (CellsPanel) cellsPanel[x][y];
    }

    private boolean showWinnerMessage(int winner) {
        if (winner == 0) {
            return false;
        }
        String text = "";
        if (winner == -1) {
            text = "Ничья :-)";
        }
        if (numberOfMatches % 2 == 1 && players == 2) { // if started second player then swap winners, except game with AI
            winner = (winner % 2) + 1;
        }
        if (winner > 0) {
            text = "Выйграл " + gameTurns[players - 1][winner - 1];
            updateStatistic(winner);
        }
        JOptionPane.showOptionDialog(null,
                text,
                "Итог",
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[] {"Хорошо"},
                null);
        numberOfMatches += 1;
        initGame();
        return true;
    }

    private void updateStatistic(int winner) {
        if (winner == 1) {
            numberOfWinsFirstPlayer.setText(String.valueOf(Integer.parseInt(numberOfWinsFirstPlayer.getText()) + 1));
        } else {
            numberOfWinsSecondPlayer.setText(String.valueOf(Integer.parseInt(numberOfWinsSecondPlayer.getText()) + 1));
        }
    }

    public static final int[][] CARDINAL = new int[][] {
            {-1, -1},
            {-1,  0},
            {-1,  1},
            { 0, -1},
            { 0,  1},
            { 1, -1},
            { 1,  0},
            { 1,  1}
    };

    private int checkWinBig() {
        int emptyCells = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 0) {
                    emptyCells += 1;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 0) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    int x = i;
                    int y = j;
                    boolean fail = false;
                    for (int s = 0; s < 4; s++) {
                        x += CARDINAL[k][0];
                        y += CARDINAL[k][1];
                        if (x < 0 || y < 0 || x >= size || y >= size || field[i][j] != field[x][y]) {
                            fail = true;
                            break;
                        }
                    }
                    if (!fail) {
                        return field[i][j];
                    }
                }
            }
        }
        if (emptyCells == 0) {
            return -1;
        }
        return 0;
    }

    private int checkWinSmall() {
        int emptyCells = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 0) {
                    emptyCells += 1;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            if (field[i][0] != 0 && field[i][0] == field[i][1] && field[i][0] == field[i][2]) {
                return field[i][0];
            }
            if (field[0][i] != 0 && field[0][i] == field[1][i] && field[0][i] == field[2][i]) {
                return field[0][i];
            }
        }
        if (field[0][0] != 0 && field[0][0] == field[1][1] && field[0][0] == field[2][2]) {
            return field[0][0];
        }
        if (field[0][2] != 0 && field[0][2] == field[1][1] && field[0][2] == field[2][0]) {
            return field[0][2];
        }
        if (emptyCells == 0) {
            return -1;
        }
        return 0;
    }
}
