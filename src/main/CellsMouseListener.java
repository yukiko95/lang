package main;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CellsMouseListener extends MouseAdapter {
    private CellsPanel cell;
    private Game game;

    public CellsMouseListener(Game gameFrame, CellsPanel cell) {
        this.cell = cell;
        this.game = gameFrame;
    }

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
