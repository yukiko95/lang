package newCode;

import oldCode.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        setSettings(sounds, game, players);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        settingsFrame = new JFrame("Настройки");
        settingsFrame.setSize(200, 200);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Звуки");

        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Box box3 = Box.createVerticalBox();
        final JCheckBox check = new JCheckBox("Вкл.");
        if (Integer.valueOf(prop.getProperty("sounds")) != 0) {
            check.setSelected(true);
        }
        box3.add(check);

        Box box = Box.createVerticalBox();
        ButtonGroup bg = new ButtonGroup();
        JRadioButton rButtonGame1;
        JRadioButton rButtonGame2;
        if (Integer.valueOf(prop.getProperty("game")) == 0) {
            rButtonGame1 = new JRadioButton("3*3", true);
            rButtonGame2 = new JRadioButton("Бесконечное поле");
        } else {
            rButtonGame1 = new JRadioButton("3*3");
            rButtonGame2 = new JRadioButton("Бесконечное поле", true);
        }
        bg.add(rButtonGame1);
        bg.add(rButtonGame2);
        box.add(rButtonGame1);
        box.add(rButtonGame2);

        Box box2 = Box.createVerticalBox();
        ButtonGroup bg2 = new ButtonGroup();
        JRadioButton rButtonPlayer1;
        JRadioButton rButtonPlayer2;
        if (Integer.valueOf(prop.getProperty("players")) == 1) {
            rButtonPlayer1 = new JRadioButton("С компьютером", true);
            rButtonPlayer2 = new JRadioButton("2 игрока");
        } else {
            rButtonPlayer1 = new JRadioButton("С компьютером");
            rButtonPlayer2 = new JRadioButton("2 игрока", true);
        }
        bg2.add(rButtonPlayer1);
        bg2.add(rButtonPlayer2);
        box2.add(rButtonPlayer1);
        box2.add(rButtonPlayer2);

        JButton ok = new JButton("Ok");

        settingsFrame.add(label);
        settingsFrame.add(box3);
        settingsFrame.add(box);
        settingsFrame.add(box2);
        settingsFrame.add(new JSeparator());
        settingsFrame.add(ok);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSettings(sounds, game, players);
                settingsFrame.dispose();
            }
        });

        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (check.isSelected()) {
                    sounds = 1;
                } else {
                    sounds = 0;
                }
            }
        });

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
