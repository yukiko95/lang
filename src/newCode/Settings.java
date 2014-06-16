package newCode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

/**
 * Created by Darya on 6/9/14.
 */

public class Settings extends JFrame implements ActionListener {
    private JFrame settingsFrame;
    private int sounds = 0;
    private int game = 0;
    private int players = 1;
    private Properties prop;

    @Override
    public void actionPerformed(ActionEvent e) {
        setSettings(sounds, game, players);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        settingsFrame = new JFrame("Настройки");
        settingsFrame.setSize(220, 250);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new BorderLayout());

        prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        settingsFrame.add(createMainPanel(), BorderLayout.CENTER);
        settingsFrame.add(createButtonsPanel(), BorderLayout.SOUTH);

        settingsFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Menu.getFrames()[0].setEnabled(false);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Menu.getFrames()[0].setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                Menu.getFrames()[0].setEnabled(true);
            }
        });

        settingsFrame.setVisible(true);
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
                setSettings(sounds, game, players);
                settingsFrame.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsFrame.dispose();
            }
        });

        return buttonsOkCancelPanel;
    }

    private Component createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // first panel
        JPanel firstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JCheckBox soundCheckbox = new JCheckBox("Вкл.");
        if (Integer.valueOf(prop.getProperty("sounds")) != 0) {
            soundCheckbox.setSelected(true);
        }
        firstPanel.add(new JLabel("Звук"));
        firstPanel.add(soundCheckbox);

        soundCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (soundCheckbox.isSelected()) {
                    sounds = 1;
                } else {
                    sounds = 0;
                }
            }
        });

        // second panel
        final JPanel secondPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final TitledBorder sizeTitle = new TitledBorder("Размер поля");
        secondPanel.setBorder(sizeTitle);

        ButtonGroup bG1 = new ButtonGroup();
        JRadioButton rButtonGame1 = new JRadioButton("3*3");
        JRadioButton rButtonGame2 = new JRadioButton("Бесконечное поле");
        bG1.add(rButtonGame1);
        bG1.add(rButtonGame2);
        if (Integer.valueOf(prop.getProperty("game")) == 0) {
            rButtonGame1.setSelected(true);
        } else {
            rButtonGame2.setSelected(true);
        }
        secondPanel.add(rButtonGame1);
        secondPanel.add(rButtonGame2);
        secondPanel.add(new JLabel(""));

        // third panel
        final JPanel thirdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final TitledBorder gameModeTitle = new TitledBorder("Режим игры");
        thirdPanel.setBorder(gameModeTitle);

        ButtonGroup bG2 = new ButtonGroup();
        JRadioButton rButtonPlayer1 = new JRadioButton("С компьютером");
        JRadioButton rButtonPlayer2 = new JRadioButton("2 игрока");
        bG2.add(rButtonPlayer1);
        bG2.add(rButtonPlayer2);

        if (Integer.valueOf(prop.getProperty("players")) == 1) {
            rButtonPlayer1.setSelected(true);
        } else {
            rButtonPlayer2.setSelected(true);
        }
        thirdPanel.add(rButtonPlayer1);
        thirdPanel.add(rButtonPlayer2);
        thirdPanel.add(new JLabel(""));

        rButtonGame1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = 0;
                secondPanel.setBorder(sizeTitle);
            }
        });

        rButtonGame2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = 1;
                secondPanel.setBorder(sizeTitle);
            }
        });

        rButtonPlayer1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players = 1;
                thirdPanel.setBorder(gameModeTitle);
            }
        });

        rButtonPlayer2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players = 2;
                thirdPanel.setBorder(gameModeTitle);
            }
        });

        // mainPanel
        mainPanel.add(firstPanel);
        mainPanel.add(secondPanel);
        mainPanel.add(thirdPanel);
        return mainPanel;
    }

    private void setSettings(final int sounds, final int game, final int players) {
        Properties prop = new Properties();
        try {
            prop.setProperty("sounds", String.valueOf(sounds));
            prop.setProperty("game", String.valueOf(game));
            prop.setProperty("players", String.valueOf(players));
            prop.store(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("config/settings.ini"), "UTF-8")), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
