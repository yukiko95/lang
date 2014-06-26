package main;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;

public class CellsPanel extends JPanel {
    public static final int PADDING = 20;

    private Image img = null;
    private Pair<Integer, Integer> coords;

    public CellsPanel(int x, int y) {
        coords = new Pair<Integer, Integer>(x, y);
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    public Pair<Integer, Integer> getCoords() {
        return coords;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();
        g.drawImage(img, PADDING, PADDING, width - PADDING * 2, height - PADDING * 2, null);
    }
}
