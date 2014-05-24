/**
 * Created by darya on 1.05.14.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;

public class Menu extends JFrame {

    enum FieldSize {
        THREE_THREE, INFINITY
    }


    Menu() throws IOException {
        super("Крестики нолики");
        setSize(500, 375);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLayeredPane jLayeredPane = new JLayeredPane();

        JLabel imageIcon = new JLabel();
        imageIcon.setIcon(new ImageIcon("pic1.jpeg"));
        imageIcon.setLocation(0, 0);
        imageIcon.setSize(500, 375);
        jLayeredPane.setLayer(imageIcon, 0);


        JButton buttonPlay = new JButton("Играть");
        buttonPlay.setSize(120, 30);
        buttonPlay.setLocation(190, 100);
        buttonPlay.setOpaque(false);
        JButton buttonRule = new JButton("Правила");
        buttonRule.setSize(120, 30);
        buttonRule.setLocation(190, 150);
        buttonPlay.setOpaque(false);

        JButton buttonSettings = new JButton("Настройки");
        buttonSettings.setSize(120, 30);
        buttonSettings.setLocation(190, 200);
        buttonPlay.setOpaque(false);

        JButton buttonExit = new JButton("Выход");
        buttonExit.setSize(120, 30);
        buttonExit.setLocation(190, 250);
        buttonPlay.setOpaque(false);

        jLayeredPane.setLayer(buttonPlay, 1);
        jLayeredPane.setLayer(buttonRule, 1);
        jLayeredPane.setLayer(buttonSettings, 1);
        jLayeredPane.setLayer(buttonExit, 1);

        jLayeredPane.add(imageIcon);
        jLayeredPane.add(buttonPlay);
        jLayeredPane.add(buttonRule);
        jLayeredPane.add(buttonSettings);
        jLayeredPane.add(buttonExit);

        add(jLayeredPane);
        buttonPlay.addActionListener(playGame());

        buttonRule.addActionListener(new Rule());

        buttonSettings.addActionListener(new Settings());

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private GameInterf playGame() throws IOException {
        GetSettings getSettings = new GetSettings();
        int game = 0;
        int players = 2;
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
            game = Integer.parseInt(prop.getProperty("game"));
            players = Integer.parseInt(prop.getProperty("players"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(game);
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
