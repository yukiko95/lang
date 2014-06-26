package main;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    public static final int WIDTH_WINDOW = 500;
    public static final int HEIGHT_WINDOW = 375;

    public static final int WIDTH_PANEL = 113;
    public static final int HEIGHT_PANEL = 121;

    public Menu() {
        super("Крестики-нолики");
        setSize(WIDTH_WINDOW, HEIGHT_WINDOW);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        JLayeredPane mainPane = new JLayeredPane();
        JLabel imageIcon = new JLabel();
        imageIcon.setIcon(new ImageIcon("images/font.jpeg"));
        imageIcon.setSize(WIDTH_WINDOW, HEIGHT_WINDOW);
        mainPane.setLayer(imageIcon, 0);

        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        buttonsPanel.setSize(WIDTH_PANEL, HEIGHT_PANEL);
        buttonsPanel.setOpaque(false);

        JButton playButton = new JButton("Играть");
        JButton ruleButton = new JButton("Правила");
        JButton settingsButton = new JButton("Настройки");
        JButton exitButton = new JButton("Выход");

        buttonsPanel.add(playButton);
        buttonsPanel.add(ruleButton);
        buttonsPanel.add(settingsButton);
        buttonsPanel.add(exitButton);

        mainPane.add(imageIcon);
        mainPane.setLayer(buttonsPanel, 1);
        mainPane.add(buttonsPanel, BorderLayout.CENTER);
        buttonsPanel.setLocation(WIDTH_WINDOW / 2 - WIDTH_PANEL / 2,
                                 HEIGHT_WINDOW / 2 - HEIGHT_PANEL / 2);
        add(mainPane);

        playButton.addActionListener(new Play(this));
        ruleButton.addActionListener(new Rule());
        settingsButton.addActionListener(new Settings(this));
        exitButton.addActionListener(new Exit(this));
    }
}
