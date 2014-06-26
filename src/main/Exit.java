package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Exit implements ActionListener {
    private JFrame menuFrame;

    public Exit(JFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int res = JOptionPane.showOptionDialog(
                null,
                "Вы уверены, что хотите выйти ?",
                "Выход",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[] {"Да", "Нет"},
                null);

        if (res == JOptionPane.YES_OPTION) {
            menuFrame.dispose();
        }
    }
}
