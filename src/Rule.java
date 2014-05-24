/**
 * Created by darya on 1.05.14.
 */

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
                ruleFrame.setSize(300, 220);
                ruleFrame.setLocationRelativeTo(null);
                ruleFrame.setResizable(true);
                ruleFrame.setVisible(true);
                ruleFrame.setLayout(null);

                JLabel text = new JLabel("<html><font face=’verdana’ size = 3>" +
                                               "Реальное поле имеет размер " +
                                               "20*20 клеток, но для игры " +
                                               "это равносильно бесконечности. " +
                                               "Цель игры - построить непрерывный " +
                                               "ряд из 5 крестиков (ноликов), если это бесконечная игра " +
                                               "и ряд из 3 крестиков (ноликов), если это игра 3*3 " +
                                               "по горизонтали, вертикали или диагонали. " +
                                               "Первый ход всегда предоставляется " +
                                               "человеку.</html>");

                text.setBackground(new Color(230, 230, 250));
                text.setSize(280,170);
                text.setLocation(10,10);

                JButton ok = new JButton("Ok");
                ok.setSize(70,30);
                ok.setLocation(115, 185);

                ruleFrame.add(text);
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
/*
 JDialog
 OK, "asdasd" OK_WINDOW
 */