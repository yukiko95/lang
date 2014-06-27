package main;

import javax.swing.*;

public class TicTacToeMain {
    public static final int SIZE_SMALL_FIELD = 3;  //размер игрового поля по вертикали/горизонтали для игры 3х3
    public static final int SIZE_INFINITY_FIELD = 20; //размер игрового поля по вертикали/горизонтали для игры 20х20

    /**
     * Запуск меню
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Menu();
            }
        });
    }
}
