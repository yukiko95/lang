package main;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class AISmall {
    //координаты прилегающих клеток к данной
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
    private static final int NUMBER_OF_ATTEMPTS = 121;

    private Game game;
    private int[][] field;
    private final int size;

    public AISmall(Game game) {
        this.game = game;
        this.size = TicTacToeMain.SIZE_SMALL_FIELD;
    }

    /**
     * С помощью метода whoseTurn узнает, какую картинку необходимо вставить на панель
     * @param x координата панели
     * @param y координата панели
     */
    public void move(int x, int y) {
        CellsPanel cell = game.getCell(x, y);
        cell.setImg(game.whoseTurn() == 0 ? game.imgCross : game.imgNought);
        cell.repaint();
        Pair<Integer, Integer> coords = cell.getCoords();
        game.updateField(coords.getKey(), coords.getValue(), game.whoseTurn() + 1);
        game.wasTurn();
    }

    /**
     * Проверяет наличие выигрышный позиций, а также позициий, которые принесут поражение
     */
    public void makeMove() {
        field = game.getField();
        ArrayList<Pair<Integer, Integer>> playerCoordsTwoInRow = twoInRow(1);
        ArrayList<Pair<Integer, Integer>> playerCoordsTwoInRowWithHole = twoInRowWithHole(1);

        ArrayList<Pair<Integer, Integer>> myCoordsTwoInRow = twoInRow(2);
        ArrayList<Pair<Integer, Integer>> myCoordsTwoInRowWithHole = twoInRowWithHole(2);

        if (myCoordsTwoInRow != null) {
            move(myCoordsTwoInRow.get(0).getKey(), myCoordsTwoInRow.get(0).getValue());
            return;
        }

        if (myCoordsTwoInRowWithHole != null) {
            move(myCoordsTwoInRowWithHole.get(1).getKey(), myCoordsTwoInRowWithHole.get(1).getValue());
            return;
        }

        if (playerCoordsTwoInRow != null) {
            move(playerCoordsTwoInRow.get(0).getKey(), playerCoordsTwoInRow.get(0).getValue());
            return;
        }


        if (playerCoordsTwoInRowWithHole != null) {
            move(playerCoordsTwoInRowWithHole.get(1).getKey(), playerCoordsTwoInRowWithHole.get(1).getValue());
            return;
        }

        int X = -1;
        int Y = -1;
        loop: for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 2) {
                    X = i;
                    Y = j;
                    break loop;
                }
            }
        }
        //
        if (X != -1) {
            Pair<Integer, Integer> averageRandomCoords = findAverageRandomCoords(X, Y);
            move(averageRandomCoords.getKey(), averageRandomCoords.getValue());
            return;
        }
        loop: for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 1) {
                    X = i;
                    Y = j;
                    break loop;
                }
            }
        }
        Pair<Integer, Integer> averageRandomCoords = findAverageRandomCoords(X, Y);
        move(averageRandomCoords.getKey(), averageRandomCoords.getValue());
    }

    /**
     *
     * @param curX
     * @param curY
     * @return
     */
    private Pair<Integer, Integer> findAverageRandomCoords(int curX, int curY) {
        for (int t = 0; t < NUMBER_OF_ATTEMPTS; t++) {
            Random rnd = new Random();
            int cardinal = rnd.nextInt(8);
            int newX = curX += CARDINAL[cardinal][0];
            int newY = curY += CARDINAL[cardinal][1];
            if (newX < 0 || newY < 0 || newX >= size || newY >= size) {
                continue;
            }
            if (field[newX][newY] == 0) {
                return new Pair<Integer, Integer>(newX, newY);
            }
            curX = newX;
            curY = newY;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 0) {
                    return new Pair<Integer, Integer>(i, j);
                }
            }
        }
        return null;
    }

    /**
     *
     * @param i
     * @param j
     * @param k
     * @return
     */
    private ArrayList<Pair<Integer, Integer>> getCoords(int i, int j, int k) {
        int x = i;
        int y = j;
        boolean fail = false;
        ArrayList<Pair<Integer, Integer>> coords = new ArrayList<Pair<Integer, Integer>>();
        coords.add(new Pair<Integer, Integer>(i, j));
        for (int s = 0; s < 2; s++) {
            x += CARDINAL[k][0];
            y += CARDINAL[k][1];
            if (x < 0 || y < 0 || x >= size || y >= size) {
                fail = true;
                break;
            } else {
                coords.add(new Pair<Integer, Integer>(x, y));
            }
        }
        return !fail ? coords : null;
    }

    /**
     *
     * @param player
     * @return
     */
    private ArrayList<Pair<Integer, Integer>> twoInRow(int player) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != 0) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    ArrayList<Pair<Integer, Integer>> coords = getCoords(i, j, k);
                    if (coords != null && field[coords.get(1).getKey()][coords.get(1).getValue()] == player &&
                                          field[coords.get(2).getKey()][coords.get(2).getValue()] == player) {
                        return coords;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Ищет ячейку, в которую сходил данный игрок,
     * @param player текущий игрок
     * @return
     */
    private ArrayList<Pair<Integer, Integer>> twoInRowWithHole(int player) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != player) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    ArrayList<Pair<Integer, Integer>> coords = getCoords(i, j, k);
                    if (coords != null && field[coords.get(1).getKey()][coords.get(1).getValue()] == 0 &&
                            field[coords.get(2).getKey()][coords.get(2).getValue()] == player) {
                        return coords;
                    }
                }
            }
        }
        return null;
    }
}
