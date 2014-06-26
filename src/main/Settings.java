package main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;

public class Settings extends JFrame implements ActionListener {
    private JFrame menuFrame;
    private int game = 0;
    private int players = 1;
    private Properties prop;

    public Settings(JFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setSize(200, 300);
        setTitle("Настройки");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonsPanel(), BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private Component createButtonsPanel() {
        JPanel buttonsOkCancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");
        buttonsOkCancelPanel.add(cancelButton);
        buttonsOkCancelPanel.add(okButton);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSettings(game, players);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return buttonsOkCancelPanel;
    }

    private Component createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // second panel
        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.Y_AXIS));
        TitledBorder sizeTitle = new TitledBorder("Размер поля");
        secondPanel.setBorder(sizeTitle);

        ButtonGroup bG1 = new ButtonGroup();
        JRadioButton rButtonGame1 = new JRadioButton("3*3");
        JRadioButton rButtonGame2 = new JRadioButton("Бесконечное поле");
        bG1.add(rButtonGame1);
        bG1.add(rButtonGame2);
        if (Integer.valueOf(prop.getProperty("game")) == 0) {
            rButtonGame1.setSelected(true);
            rButtonGame2.setSelected(false);
        } else {
            rButtonGame1.setSelected(false);
            rButtonGame2.setSelected(true);
        }
        secondPanel.add(rButtonGame1);
        secondPanel.add(rButtonGame2);

        // third panel
        JPanel thirdPanel = new JPanel();
        thirdPanel.setLayout(new BoxLayout(thirdPanel, BoxLayout.Y_AXIS));

        TitledBorder gameModeTitle = new TitledBorder("Режим игры");
        thirdPanel.setBorder(gameModeTitle);

        ButtonGroup bG2 = new ButtonGroup();
        JRadioButton rButtonPlayer1 = new JRadioButton("C компьютером");
        JRadioButton rButtonPlayer2 = new JRadioButton("Друг против друга");
        bG2.add(rButtonPlayer1);
        bG2.add(rButtonPlayer2);

        if (Integer.valueOf(prop.getProperty("players")) == 1) {
            rButtonPlayer1.setSelected(true);
            rButtonPlayer2.setSelected(false);
        } else {
            rButtonPlayer1.setSelected(false);
            rButtonPlayer2.setSelected(true);
        }
        thirdPanel.add(rButtonPlayer1);
        thirdPanel.add(rButtonPlayer2);

        rButtonGame1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = 0;
            }
        });

        rButtonGame2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = 1;
            }
        });

        rButtonPlayer1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players = 1;
            }
        });

        rButtonPlayer2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players = 2;
            }
        });

        // mainPanel
        mainPanel.add(secondPanel);
        mainPanel.add(thirdPanel);
        return mainPanel;
    }

    private void setSettings(final int game, final int players) {
        Properties prop = new Properties();
        try {
            prop.setProperty("game", String.valueOf(game));
            prop.setProperty("players", String.valueOf(players));
            prop.store(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("config/settings.ini"), "UTF-8")), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
