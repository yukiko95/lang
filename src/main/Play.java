package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Play implements ActionListener {
    private final JFrame menuFrame;

    public Play(final JFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    /**
     * При вызове считываются данные из файла с настройками и создается экземпляр класса Game с заданными параметрами
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        new Game(Integer.valueOf(prop.getProperty("game")) == 0 ? TicTacToeMain.SIZE_SMALL_FIELD :
                TicTacToeMain.SIZE_INFINITY_FIELD,
                Integer.valueOf(prop.getProperty("players")));
    }
}
