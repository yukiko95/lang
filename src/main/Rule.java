package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Rule implements ActionListener {
    /**
     * При вызове выводятся правила игры
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String rule = "Реальное поле имеет размер 20*20 клеток, но для игры\n" +
                "это равносильно бесконечности. Цель игры -  построить\n" +
                "непрерывный ряд из 5 крестиков (ноликов), если это\n" +
                "бесконечная игра и ряд из 3 крестиков (ноликов), если\n" +
                "это игра 3*3 по горизонтали, вертикали или диагонали.\n" +
                "Первый ход всегда предоставляется человеку.";

        JOptionPane.showMessageDialog(null, rule, "Правила", JOptionPane.INFORMATION_MESSAGE);
    }
}
