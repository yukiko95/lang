package main;

import javafx.util.Pair;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellsMouseListener extends MouseAdapter {
    private CellsPanel cell;
    private Game game;

    public CellsMouseListener(Game game, CellsPanel cell) {
        this.cell = cell;
        this.game = game;
    }

    /**
     * В пустую клетку вставляет определенную картинку, вызывает методы updateField и wasTurn
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (cell.getImg() != null) {
            return;
        }
        cell.setImg(game.whoseTurn() == 0 ? game.imgCross : game.imgNought);
        cell.repaint();
        Pair<Integer, Integer> coords = cell.getCoords();
        game.updateField(coords.getKey(), coords.getValue(), game.whoseTurn() + 1);
        game.wasTurn();
    }
}
