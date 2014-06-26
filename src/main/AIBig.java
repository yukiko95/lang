package main;

import javafx.util.Pair;

public class AIBig {
    private Game game;

    public AIBig(Game game) {
        this.game = game;
    }

    public void makeMove() {
        int[][] field = game.getField();
        for (int i = 0; i < TicTacToeMain.SIZE_INFINITY_FIELD; i++) {
            for (int j = 0; j < TicTacToeMain.SIZE_INFINITY_FIELD; j++) {
                if (field[i][j] == 0) {
                    CellsPanel cell = game.getCell(i, j);
                    cell.setImg(game.whoseTurn() == 0 ? game.imgCross : game.imgNought);
                    cell.repaint();
                    Pair<Integer, Integer> coords = cell.getCoords();
                    game.updateField(coords.getKey(), coords.getValue(), game.whoseTurn() + 1);
                    game.wasTurn();
                    return;
                }
            }
        }
    }
}
