package main;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;

public class CellsPanel extends JPanel {
    public static final int PADDING = 20; //Отступ для размещения картинки
    private Image img = null;
    private Pair<Integer, Integer> coords;

    //Конструктор, в который передаются координаты клетки
    public CellsPanel(final int x, final int y) {
        coords = new Pair<Integer, Integer>(x, y);
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
    }

    /**
     * Устанавливает новое значение для переменной img
     *
     * @param img картинка с крестиком/ноликом
     */
    public void setImg(final Image img) {
        this.img = img;
    }

    /**
     * @return переменная типа Image
     */
    public Image getImg() {
        return img;
    }

    /**
     * @return x, y
     */
    public Pair<Integer, Integer> getCoords() {
        return coords;
    }

    /**
     * Добавляет картинку на панель
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();
        g.drawImage(img, PADDING, PADDING, width - PADDING * 2, height - PADDING * 2, null);
        setBackground(new Color(255, 250, 221));
    }
}
