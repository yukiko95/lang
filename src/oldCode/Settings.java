package oldCode; /**
 * Created by darya on 1.05.14.
 */

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

public class Settings extends JFrame implements ActionListener {
    private JFrame settingsFrame;
    private JLabel numberLabel;
    private int sounds = 100;
    private int game = 0;
    private int players = 2;

    @Override
    public void actionPerformed(ActionEvent e) {
        settingsFrame = new JFrame("Настройки");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setResizable(false);
        settingsFrame.setLayout(new FlowLayout(FlowLayout.CENTER));

        final JSlider soundSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);

        JLabel soundLabel = new JLabel("Звуки");

        numberLabel = new JLabel("100");

        settingsFrame.add(soundLabel);
        settingsFrame.add(soundSlider);
        settingsFrame.add(numberLabel);

        soundSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                numberLabel.setText(String.valueOf(soundSlider.getValue()));
                settingsFrame.repaint();
            }
        });

        Box box = Box.createVerticalBox();
        ButtonGroup bg = new ButtonGroup();
        JRadioButton rButtonGame1 = new JRadioButton("3*3", true);
        JRadioButton rButtonGame2 = new JRadioButton("Бесконечное поле");
        bg.add(rButtonGame1);
        bg.add(rButtonGame2);
        box.add(rButtonGame1);
        box.add(rButtonGame2);
        settingsFrame.add(box);
        settingsFrame.add(new JSeparator(SwingConstants.HORIZONTAL));

        Box box2 = Box.createVerticalBox();
        ButtonGroup bg2 = new ButtonGroup();
        JRadioButton rButtonPlayer1 = new JRadioButton("С компьютером");
        JRadioButton rButtonPlayer2 = new JRadioButton("2 игрока", true);
        bg2.add(rButtonPlayer1);
        bg2.add(rButtonPlayer2);
        box2.add(rButtonPlayer1);
        box2.add(rButtonPlayer2);
        settingsFrame.add(box2);
        settingsFrame.add(new JSeparator(SwingConstants.HORIZONTAL));

        JButton ok = new JButton("Ok");
        settingsFrame.add(ok, BorderLayout.EAST);

        settingsFrame.pack();

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setSettings(sounds, game, players);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                settingsFrame.dispose();
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

        soundSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sounds = soundSlider.getValue();
            }
        });
        settingsFrame.setVisible(true);

    }

    private void setSettings(final int sounds, final int game, final int players) throws IOException {
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
