package newCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Darya on 6/9/14.
 */

public class Rule extends JFrame implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String rule = "Реальное поле имеет размер 20*20 клеток, но для игры\n" +
                "это равносильно бесконечности. Цель игры -  построить\n" +
                "непрерывный ряд из 5 крестиков (ноликов), если это\n" +
                "бесконечная игра и ряд из 3 крестиков (ноликов), если\n" +
                "это игра 3*3 по горизонтали, вертикали или диагонали.\n" +
                "Первый ход всегда предоставляется человеку.";

        JOptionPane.showMessageDialog(this, rule, "Правила", JOptionPane.DEFAULT_OPTION);
    }
}
