import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by darya on 23.05.14.
 */

public class GetSettings {

    protected int getSounds(){
        Properties prop = new Properties();
        int sounds = 100;
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
            sounds = Integer.parseInt(prop.getProperty("sounds"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sounds;
    }

    protected int getGame(){
        Properties prop = new Properties();
        int game = 0;
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
            game = Integer.parseInt(prop.getProperty("game"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return game;
    }

    protected int getPlayer(){
        Properties prop = new Properties();
        int player = 2;
        try {
            prop.load(new InputStreamReader(new FileInputStream("config/settings.ini"), "UTF-8"));
            player = Integer.parseInt(prop.getProperty("players"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return player;
    }
}
