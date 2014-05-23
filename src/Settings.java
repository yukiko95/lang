/**
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
    private int game = 1;
    private int player = 2;

    @Override
    public void actionPerformed(ActionEvent e) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                settingsFrame = new JFrame("Настройки");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                settingsFrame.setLocationRelativeTo(null);
                settingsFrame.setResizable(false);
                settingsFrame.setVisible(true);
                settingsFrame.setSize(300, 235);
                settingsFrame.setLayout(new FlowLayout(FlowLayout.CENTER));

                final JSlider soundSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
                soundSlider.setSize(100, 30);
                soundSlider.setLocation(90, 50);

                JLabel soundLabel = new JLabel("Звуки");
                soundLabel.setSize(50, 30);
                soundLabel.setLocation(30, 50);

                numberLabel = new JLabel("100");
                numberLabel.setSize(30, 30);
                numberLabel.setLocation(150, 50);

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
                box.setSize(140, 60);
//                box.setLocation(60, 60);
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
                box2.setSize(140, 60);
//                box2.setLocation(60, 130);
                settingsFrame.add(box2);
                settingsFrame.add(new JSeparator(SwingConstants.HORIZONTAL));

                JButton ok = new JButton("Ok");
                ok.setSize(30, 20);
//                ok.setLocation(85, 210);
                settingsFrame.add(ok, BorderLayout.EAST);

                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            setSettings(sounds, game);
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
                        player = 1;
                    }
                });

                rButtonPlayer2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        player = 2;
                    }
                });

                soundSlider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        sounds = soundSlider.getValue();
                    }
                });
            }
        });
    }

    private void setSettings(final int sounds, final int game) throws IOException {
        Properties prop = new Properties();
        try {
            prop.setProperty("sounds", String.valueOf(sounds));
            prop.setProperty("game", String.valueOf(game));
            prop.setProperty("players", String.valueOf(player));
            prop.store(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("config/settings.ini"), "UTF-8")), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
