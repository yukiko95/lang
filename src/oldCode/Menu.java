package oldCode; /**
 * Created by darya on 1.05.14.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
        imageIcon.setIcon(new ImageIcon("pictures/font.jpeg"));
        imageIcon.setLocation(0, 0);
        imageIcon.setSize(500, 375);
        jLayeredPane.setLayer(imageIcon, 0);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,10));
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
        jLayeredPane.setLayer(buttonPanel,1);
        jLayeredPane.add(buttonPanel);
        buttonPanel.setLocation(175,100);
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
        int game = getSettings.getGame();
        int players = getSettings.getPlayer();
        System.out.println(game);
        System.out.println(players);
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
