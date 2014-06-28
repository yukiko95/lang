package main;

import javafx.util.*;
import java.util.ArrayList;
import java.util.Random;

public class AIBig {
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
    private static final int NUMBER_OF_ATTEMPTS = 113121;

    private Game game;
    private final int size;
    private int[][] field;

    public AIBig(Game game) {
        this.game = game;
        this.size = TicTacToeMain.SIZE_INFINITY_FIELD;
    }

    /**
     * С помощью метода whoseTurn узнает, какую картинку необходимо вставить на панель
     *
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
        ArrayList<Pair<Integer, Integer>> playerCoordsThreeInRow = threeInRow(1);
        ArrayList<Pair<Integer, Integer>> playerCoordsFourInRow = fourInRow(1);

        ArrayList<Pair<Integer, Integer>> myCoordsThreeInRow = threeInRow(2);
        ArrayList<Pair<Integer, Integer>> myCoordsFourInRow = fourInRow(2);

        if (myCoordsFourInRow != null) {
            move(myCoordsFourInRow.get(0).getKey(), myCoordsFourInRow.get(0).getValue());
            return;
        }

        if (playerCoordsFourInRow != null) {
            move(playerCoordsFourInRow.get(0).getKey(), playerCoordsFourInRow.get(0).getValue());
            return;
        }

        if (myCoordsThreeInRow != null) {
            move(myCoordsThreeInRow.get(4).getKey(), myCoordsThreeInRow.get(4).getValue());
            return;
        }

        if (playerCoordsThreeInRow != null) {
            move(playerCoordsThreeInRow.get(4).getKey(), playerCoordsThreeInRow.get(4).getValue());
            return;
        }

        //Если имеем 3 в ряд и есть место для 4го и для 5го
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != 0) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    ArrayList<Pair<Integer, Integer>> coords = getCoords(i, j, k);
                    if (coords != null && field[coords.get(1).getKey()][coords.get(1).getValue()] == 0 &&
                            field[coords.get(2).getKey()][coords.get(2).getValue()] == 2 &&
                            field[coords.get(3).getKey()][coords.get(3).getValue()] == 2 &&
                            field[coords.get(4).getKey()][coords.get(4).getValue()] == 2) {
                        move(coords.get(1).getKey(), coords.get(1).getValue());
                        return;
                    }
                }
            }
        }

        //Если имеем 2 в ряд и есть место для 3го, 4го и для 5го
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != 0) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    ArrayList<Pair<Integer, Integer>> coords = getCoords(i, j, k);
                    if (coords != null && field[coords.get(1).getKey()][coords.get(1).getValue()] == 0 &&
                            field[coords.get(2).getKey()][coords.get(2).getValue()] == 0 &&
                            field[coords.get(3).getKey()][coords.get(3).getValue()] == 2 &&
                            field[coords.get(4).getKey()][coords.get(4).getValue()] == 2) {
                        move(coords.get(2).getKey(), coords.get(2).getValue());
                        return;
                    }
                }
            }
        }

        int sumX = 0;
        int sumY = 0;
        int cntSum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 2) {
                    sumX += i;
                    sumY += j;
                    cntSum += 1;
                }
            }
        }
        if (cntSum != 0) {
            Pair<Integer, Integer> averageRandomCoords = findAverageRandomCoords(sumX / cntSum, sumY / cntSum);
            move(averageRandomCoords.getKey(), averageRandomCoords.getValue());
            return;
        }
        loop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 1) {
                    sumX = i;
                    sumY = j;
                    break loop;
                }
            }
        }
        Pair<Integer, Integer> averageRandomCoords = findAverageRandomCoords(sumX, sumY);
        move(averageRandomCoords.getKey(), averageRandomCoords.getValue());
    }

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

    private ArrayList<Pair<Integer, Integer>> getCoords(int i, int j, int k) {
        int x = i;
        int y = j;
        boolean fail = false;
        ArrayList<Pair<Integer, Integer>> coords = new ArrayList<Pair<Integer, Integer>>();
        coords.add(new Pair<Integer, Integer>(i, j));
        for (int s = 0; s < 4; s++) {
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

    private ArrayList<Pair<Integer, Integer>> threeInRow(int player) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != 0) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    ArrayList<Pair<Integer, Integer>> coords = getCoords(i, j, k);
                    if (coords != null && field[coords.get(1).getKey()][coords.get(1).getValue()] == player &&
                            field[coords.get(2).getKey()][coords.get(2).getValue()] == player &&
                            field[coords.get(3).getKey()][coords.get(3).getValue()] == player &&
                            field[coords.get(4).getKey()][coords.get(4).getValue()] == 0) {
                        return coords;
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<Pair<Integer, Integer>> fourInRow(int player) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != 0) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    ArrayList<Pair<Integer, Integer>> coords = getCoords(i, j, k);
                    if (coords != null && field[coords.get(1).getKey()][coords.get(1).getValue()] == player &&
                            field[coords.get(2).getKey()][coords.get(2).getValue()] == player &&
                            field[coords.get(3).getKey()][coords.get(3).getValue()] == player &&
                            field[coords.get(4).getKey()][coords.get(4).getValue()] == player) {
                        return coords;
                    }
                }
            }
        }
        return null;
    }
}
