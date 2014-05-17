import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Rule extends JFrame implements ActionListener {
    private JFrame ruleFrame;
    @Override
    public void actionPerformed(ActionEvent e) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ruleFrame = new JFrame("Правила");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                ruleFrame.setSize(300, 180);
                ruleFrame.setLocationRelativeTo(null);
                ruleFrame.setResizable(false);
                ruleFrame.setVisible(true);
                ruleFrame.setLayout(new FlowLayout());

                JLabel text = new JLabel("Реальное поле имеет размер \n" +
                                               "20*20 клеток, но для игры \n" +
                                               "это равносильно бесконечности. \n" +
                                               "Цель игры - построить непрерывный \n" +
                                               "ряд из 5 крестиков (ноликов) \n" +
                                               "по горизонтали, вертикали или диагонали. \n" +
                                               "Первый ход всегда предоставляется \n" +
                                               "человеку.");
                text.setForeground(Color.CYAN);

                JButton ok = new JButton("Ok");

                ruleFrame.add(text);
                ruleFrame.add(new JSeparator());
                ruleFrame.add(ok);


                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ruleFrame.dispose();
                    }
                });

                ruleFrame.addWindowListener(new WindowAdapter() {
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
