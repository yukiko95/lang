package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Game extends JFrame {
    //Размер окна для игры 3х3
    public static final int WIDTH_WINDOW_1 = 390;
    public static final int HEIGHT_WINDOW_1 = 410;

    //Размер окна для игры 20х20
    public static final int WIDTH_WINDOW_2 = 450;
    public static final int HEIGHT_WINDOW_2 = 500;

    public static final String[][] gameTurns = new String[][]{
            {"игрок", "бот"},
            {"игрок 1", "игрок 2"},
    };

    private final JFrame menuFrame;
    public final int size;
    public final int players;
    public final String player1;
    public final String player2;
    public final Image imgCross;
    public final Image imgNought;

    private int[][] field;
    private int turnPlayer; // = 0 - ход крестиков, 1 - ход ноликов
    private JLabel turnInfo;
    private JLabel numberOfWinsFirstPlayer;
    private JLabel numberOfWinsSecondPlayer;
    private JPanel[][] cellsPanel;
    private int numberOfMatches = 0;
    private AISmall aiSmall;
    private AIBig aiBig;

    /**
     * @param size    размер поля
     * @param players число игроков
     */
    public Game(final JFrame menuFrame, final int size, final int players) {
        this.menuFrame=menuFrame;
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                menuFrame.setEnabled(false);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                menuFrame.setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                menuFrame.setEnabled(true);
            }
        });
    }

    /**
     * Устанавливает на infoPanel чей ход, очищает игровое поле
     */
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
            }
        }
    }

    /**
     * Создает панель с информацией о ходе соперника
     *
     * @return infoPanel
     */
    private Component infoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(new JLabel("Ходит: "));
        infoPanel.add(turnInfo);
        return infoPanel;
    }

    /**
     * Создает панель с панелями
     *
     * @return fieldPanel
     */
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

    /**
     * Создает панель с кнопками "Новая игра", "Очистить статистику", "Закончить игру", а также информацией о количестве
     * побед пользователей/бота
     *
     * @return buttonsPanel
     */
    private Component buttonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 3, 3));

        JPanel firstPlayerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        firstPlayerPanel.add(new JLabel(player1 + ": "));
        firstPlayerPanel.add(numberOfWinsFirstPlayer);
        firstPlayerPanel.add(new JLabel(" победа(ы)"));

        JPanel secondPlayerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        secondPlayerPanel.add(new JLabel(player2 + ": "));
        secondPlayerPanel.add(numberOfWinsSecondPlayer);
        secondPlayerPanel.add(new JLabel(" победа(ы)"));

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

    /**
     * @return имя игрока, чей ход
     */
    public int whoseTurn() {
        return turnPlayer;
    }

    /**
     * Меняет текст внутри turnInfo, вызывает бота
     */
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

    /**
     * Обновляет значение переменной field[x][y]
     *
     * @param x координата
     * @param y координата
     * @param z значение переменной field[x][y]. Может быть = 0 - ячейка пуста, 1 - в ячейке Х, 2 - в ячейке О
     */
    public void updateField(int x, int y, int z) {
        field[x][y] = z;
    }

    /**
     * @return матрицу field
     */
    public int[][] getField() {
        return field;
    }

    /**
     * @param x координата панели
     * @param y координата панели
     * @return панель с заданными координатами
     */
    public CellsPanel getCell(int x, int y) {
        return (CellsPanel) cellsPanel[x][y];
    }

    /**
     * @param winner -1, если ничья,
     *               0, если еще есть свободные ячейки и никто не выиграл
     *               1 или 2, если кто-то выиграл
     * @return true, если есть победитель/ничья, иначе false
     */
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
            text = "Выиграл " + gameTurns[players - 1][winner - 1];
            updateStatistic(winner);
        }
        JOptionPane.showOptionDialog(null,
                text,
                "Итог",
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Хорошо"},
                null);
        numberOfMatches += 1;
        initGame();
        return true;
    }

    /**
     * Если есть победа, то обновляет статистику
     *
     * @param winner
     */
    private void updateStatistic(int winner) {
        if (winner == 1) {
            numberOfWinsFirstPlayer.setText(String.valueOf(Integer.parseInt(numberOfWinsFirstPlayer.getText()) + 1));
        } else {
            numberOfWinsSecondPlayer.setText(String.valueOf(Integer.parseInt(numberOfWinsSecondPlayer.getText()) + 1));
        }
    }

    //координаты прилегающих клеток к данной
    public static final int[][] CARDINAL = new int[][]{
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
    };

    /**
     * Проходит по массиву field и считает количество свободных клеток, затем исследуются все прилегающие к данной
     * клетке ячейки
     *
     * @return -1, если ничья,
     * 0, если никто не выиграл
     * иначе, значение field[i][j]
     */
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

    /**
     * Проходит по массиву field и считает количество свободных клеток, затем исследуются возможные выигрышные варианты
     *
     * @return -1, если ничья,
     * 0, если никто не выиграл
     * иначе, значение field[i][j]
     */
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
