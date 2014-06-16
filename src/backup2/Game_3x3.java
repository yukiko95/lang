package backup2;

import javax.swing.*;

/**
 * Created by crazyministr on 6/10/14.
 */
public class Game_3x3 {
    private int SIZE = 3;

    /**
     * check who is winner
     *
     * @param buttons game field
     * @param emptyCells count empty cells
     * @return "X" if winner is first player,
     *         "O" if winner is second player,
     *         "XO" if draw and empty string if not found winner
     */
    public String checkWin(JButton[][] buttons, int emptyCells) {
        char[][] p = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                try {
                    p[i][j] = buttons[i][j].getText().charAt(0);
                } catch (Exception e) {
                    p[i][j] = '\0';
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                System.out.print(p[i][j] == '\0' ? '_' : p[i][j]);
            System.out.println("");
        }
        for (int i = 0; i < SIZE; i++) {
            if (p[i][0] != '\0' && p[i][0] == p[i][1] && p[i][0] == p[i][2]) {
                return String.valueOf(p[i][0]);
            }
            if (p[0][i] != '\0' && p[0][i] == p[1][i] && p[0][i] == p[2][i]) {
                return String.valueOf(p[0][i]);
            }
        }
        if (p[0][0] != '\0' && p[0][0] == p[1][1] && p[0][0] == p[2][2]) {
            return String.valueOf(p[0][0]);
        }
        if (p[0][2] != '\0' && p[0][2] == p[1][1] && p[0][2] == p[2][0]) {
            return String.valueOf(p[0][2]);
        }
        if (emptyCells == 0) {
            return "XO";
        }
        return "";
    }
}
