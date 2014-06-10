package oldCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by darya on 23.05.14.
 */
public interface GameInterf extends ActionListener {

    void checkWin();

    void showMessage(JButton matrixButtons);

    String whoseTurn();

    @Override
    void actionPerformed(ActionEvent e);
}
