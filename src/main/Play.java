package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Play implements ActionListener {
    private JFrame menuFrame;

    public Play(JFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
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
        });
    }
}
