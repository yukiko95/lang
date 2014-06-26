package main;

import javax.swing.*;
import java.io.IOException;

public class TicTacToeMain {
    public static final int SIZE_SMALL_FIELD = 3;
    public static final int SIZE_INFINITY_FIELD = 20;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Menu();
            }
        });
    }
}
