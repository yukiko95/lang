/**
 * Created by darya on 1.05.14.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;

public class Menu extends JFrame {
    private static JPanel imagePanel = new JPanel();

    Menu() throws IOException {
        super("Крестики нолики");
        setSize(500, 375);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        imagePanel.setLayout(null);


        JLabel imageIcon = new JLabel();
        imageIcon.setIcon(new ImageIcon("pic1.jpeg"));
        imageIcon.setLocation(0, 0);
        imageIcon.setSize(500, 375);
        imagePanel.add(imageIcon);

        setContentPane(imagePanel);

        JButton buttonPlay = new JButton("Играть");
        buttonPlay.setSize(120, 30);
        buttonPlay.setLocation(190, 100);
        JButton buttonRule = new JButton("Правила");
        buttonRule.setSize(120, 30);
        buttonRule.setLocation(190, 150);
        JButton buttonSettings = new JButton("Настройки");
        buttonSettings.setSize(120, 30);
        buttonSettings.setLocation(190, 200);
        JButton buttonExit = new JButton("Выход");
        buttonExit.setSize(120, 30);
        buttonExit.setLocation(190, 250);

        imagePanel.add(buttonPlay);
        imagePanel.add(buttonRule);
        imagePanel.add(buttonSettings);
        imagePanel.add(buttonExit);

        setContentPane(imagePanel);

        buttonPlay.addActionListener(playGame());

        buttonRule.addActionListener(new Rule());

        buttonSettings.addActionListener(new Settings());

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.getFrames()[0].dispose();
            }
        });
    }

    private GameInterf playGame() throws IOException {
        GetSettings getSettings = new GetSettings();
        int game = getSettings.getGame();
        int players = getSettings.getPlayer();
        if (game == 0) {
            if (players == 2) {
               return new Game_3x3_2_Players();
            } else {
                return new Game_3x3_AI();
            }
        } else {
            if (players == 2) {
                return new Game_Infinity_2_Players();
            }
        }
         return new Game_Infinity_AI();
    }
}
