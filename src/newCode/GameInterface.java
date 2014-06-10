package newCode;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by Darya on 6/9/14.
 */

public interface GameInterface extends ActionListener {
    public void initGame();

    interface Game {

        public String whoseTurn();

        public void checkWin();

        public void playSound();

        public void showMessage(JButton buttons);

        public void newGame(String win);
    }
}
