package newCode;

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

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        buttonPanel.setSize(130, 170);
        buttonPanel.setOpaque(false);

        JButton buttonPlay = new JButton("   Играть   ");
        buttonPlay.setOpaque(false);
        JButton buttonRule = new JButton("  Правила ");
        buttonPlay.setOpaque(false);

        JButton buttonSettings = new JButton("Настройки");
        buttonPlay.setOpaque(false);

        JButton buttonExit = new JButton("   Выход    ");
        buttonPlay.setOpaque(false);

        buttonPanel.add(buttonPlay);
        buttonPanel.add(buttonRule);
        buttonPanel.add(buttonSettings);
        buttonPanel.add(buttonExit);

        jLayeredPane.add(imageIcon);
        jLayeredPane.setLayer(buttonPanel, 1);
        jLayeredPane.add(buttonPanel);
        buttonPanel.setLocation(175, 100);
        add(jLayeredPane);

        buttonPlay.addActionListener(getGame());

        buttonRule.addActionListener(new newCode.Rule());

        buttonSettings.addActionListener(new newCode.Settings());

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private GameInterface getGame() {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(prop.getProperty("game")) == 0) {
            if (Integer.parseInt(prop.getProperty("players")) == 1) {
                return new Game_3x3_AI(Integer.parseInt(prop.getProperty("sounds")));
            } else {
                return new Game_3x3_2Players(Integer.parseInt(prop.getProperty("sounds")));
            }
        } else {
            if (Integer.parseInt(prop.getProperty("players")) == 2) {
                return new Game_Infinity_2Players(Integer.parseInt(prop.getProperty("sounds")));
            }
        }
        return new Game_Infinity_2Players(Integer.parseInt(prop.getProperty("sounds")));
    }
}
