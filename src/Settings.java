import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Settings extends JFrame implements ActionListener {
    private JFrame settingsFrame;
    private JLabel numberLabel;

    @Override
    public void actionPerformed(ActionEvent e) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                settingsFrame = new JFrame("Настройки");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                settingsFrame.setLocationRelativeTo(null);
                settingsFrame.setResizable(false);
                settingsFrame.setVisible(true);
                settingsFrame.setSize(300, 100);
                settingsFrame.setLayout(new FlowLayout());

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
                JRadioButton rButton1 = new JRadioButton("3*3");
                JRadioButton rButton2 = new JRadioButton("Бесконечное поле");
                bg.add(rButton1);
                bg.add(rButton2);
                box.add(rButton1);
                box.add(rButton2);
                box.setSize(140, 60);
                box.setLocation(60, 60);
                settingsFrame.add(box);
                settingsFrame.add(new JSeparator(SwingConstants.HORIZONTAL));

                JButton ok = new JButton("Ok");
                ok.setSize(30, 20);
                ok.setLocation(85, 130);

                settingsFrame.add(ok);
//                settingsFrame.pack();

                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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
            }
        });
    }
}
