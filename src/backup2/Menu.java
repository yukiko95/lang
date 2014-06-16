package backup2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Darya on 6/9/14.
 */

public class Menu extends JFrame {
    private JPanel buttonPanel;

    public Menu() throws IOException {
        super("Крестики нолики");
        setSize(500, 375);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLayeredPane jLayeredPane = new JLayeredPane();

        JLabel imageIcon = new JLabel();
        imageIcon.setIcon(new ImageIcon("pictures/font.jpeg"));
        imageIcon.setLocation(0, 0);
        imageIcon.setSize(500, 375);
        jLayeredPane.setLayer(imageIcon, 0);

        buttonPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        buttonPanel.setSize(113, 121);
        buttonPanel.setOpaque(false);

        JButton buttonPlay = new JButton("Играть");
        buttonPlay.setOpaque(false);
        JButton buttonRule = new JButton("Правила");
        buttonPlay.setOpaque(false);

        JButton buttonSettings = new JButton("Настройки");
        buttonPlay.setOpaque(false);

        JButton buttonExit = new JButton("Выход");
        buttonPlay.setOpaque(false);

        buttonPanel.add(buttonPlay);
        buttonPanel.add(buttonRule);
        buttonPanel.add(buttonSettings);
        buttonPanel.add(buttonExit);

        jLayeredPane.add(imageIcon);
        jLayeredPane.setLayer(buttonPanel, 1);
        jLayeredPane.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setLocation(500 / 2 - 113 / 2, 375 / 2 - 121 / 2);
        add(jLayeredPane);

//        buttonPlay.addActionListener(getGame());
        buttonPlay.addActionListener(new RunGame());

        buttonRule.addActionListener(new Rule());

        buttonSettings.addActionListener(new Settings());

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

//    private GameInterface getGame() {
//        Properties prop = new Properties();
//        try {
//            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (Integer.parseInt(prop.getProperty("game")) == 0) {
//            if (Integer.parseInt(prop.getProperty("players")) == 1) {
//                return new Game_3x3_AI(Integer.parseInt(prop.getProperty("sounds")));
//            } else {
//                return new Game_3x3_2Players(Integer.parseInt(prop.getProperty("sounds")));
//            }
//        } else {
//            if (Integer.parseInt(prop.getProperty("players")) == 2) {
//                return new Game_Infinity_2Players(Integer.parseInt(prop.getProperty("sounds")));
//            }
//        }
//        return new Game_Infinity_2Players(Integer.parseInt(prop.getProperty("sounds")));
//    }

    private class RunGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Properties prop = new Properties();
            try {
                prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (Integer.parseInt(prop.getProperty("game")) == 0) { // 3x3
//                new Game_3x3(Integer.parseInt(prop.getProperty("players")), Integer.parseInt(prop.getProperty("sounds")));
            } else { // infinity
            }
        }
    }
}
