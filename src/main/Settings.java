package main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

public class Settings extends JFrame implements ActionListener {
    private final JFrame menuFrame;
    private int game = 0; //Начальное значение переменной 0 - игра 3х3
    private int players = 1; //Начальное значение переменной 1 - игра с компьютером
    private Properties prop;

    public Settings(final JFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    /**
     * При вызове Settings создается окно, в котором можно изменить параметры игры
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        setSize(200, 300);
        setTitle("Настройки");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (getDefaultCloseOperation() == 0){
            menuFrame.setEnabled(true);
        }
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                menuFrame.setEnabled(false);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                menuFrame.setEnabled(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                menuFrame.setEnabled(true);
            }
        });
    }

    /**
     * Создает панель с 2 кнопками ("Ок" и "Отмена")
     *
     * @return панель с кнопками "Ок" и "Отмена"
     */
    private Component createButtonsPanel() {
        JPanel buttonsOkCancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");
        buttonsOkCancelPanel.add(okButton);
        buttonsOkCancelPanel.add(cancelButton);

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

    /**
     * @return панель с настройками
     */
    private Component createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel firstPanel = new JPanel(); //панель с флажками для выбора поля
        firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.Y_AXIS));
        TitledBorder sizeTitle = new TitledBorder("Размер поля");
        firstPanel.setBorder(sizeTitle);

        ButtonGroup bG1 = new ButtonGroup();
        JRadioButton rButtonGame1 = new JRadioButton("3*3");
        JRadioButton rButtonGame2 = new JRadioButton("Бесконечное поле");
        bG1.add(rButtonGame1);
        bG1.add(rButtonGame2);

        //считываем данные из файла и выбираем соответствующим образом настройки
        if (Integer.valueOf(prop.getProperty("game")) == 0) {
            rButtonGame1.setSelected(true);
            rButtonGame2.setSelected(false);
        } else {
            rButtonGame1.setSelected(false);
            rButtonGame2.setSelected(true);
        }
        firstPanel.add(rButtonGame1);
        firstPanel.add(rButtonGame2);

        JPanel secondPanel = new JPanel(); //панель с флажками для выбора количества игроков
        secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.Y_AXIS));

        TitledBorder gameModeTitle = new TitledBorder("Режим игры");
        secondPanel.setBorder(gameModeTitle);

        ButtonGroup bG2 = new ButtonGroup();
        JRadioButton rButtonPlayer1 = new JRadioButton("C компьютером");
        JRadioButton rButtonPlayer2 = new JRadioButton("Друг против друга");
        bG2.add(rButtonPlayer1);
        bG2.add(rButtonPlayer2);

        //считываем данные из файла и выбираем соответствующим образом настройки
        if (Integer.valueOf(prop.getProperty("players")) == 1) {
            rButtonPlayer1.setSelected(true);
            rButtonPlayer2.setSelected(false);
        } else {
            rButtonPlayer1.setSelected(false);
            rButtonPlayer2.setSelected(true);
        }
        secondPanel.add(rButtonPlayer1);
        secondPanel.add(rButtonPlayer2);

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


        mainPanel.add(firstPanel);
        mainPanel.add(secondPanel);
        return mainPanel;
    }

    /**
     * Записывает настройки в файл
     *
     * @param game    = 0, если игра 3х3, 1 - игра 20х20(эквивалент бесконечной)
     * @param players = 1, если игра против компьютера, = 2, если играют двое человек
     */
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
