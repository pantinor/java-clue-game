package gdx.clue;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.util.ArrayList;
import gdx.clue.astar.AStar;
import gdx.clue.astar.Location;
import gdx.clue.astar.PathFinder;

public class ClueMain extends Game {

    public static Clue CLUE_GAME;
    public static ClueMap MAP;

    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;

    public static PathFinder<Location> PATHFINDER;

    public static final Dice DICE = new Dice(1, 6);

    public static ArrayList<Player> PLAYERS = null;
    public static Player currentTurnPlayer = null;
    public static Player yourPlayer = null;

    public static boolean difficult_setting = false;

    public static final String formatter = "%s suggests\n%s\ncommitted the crime\nwith the %s\nin the %s.";
    public static final String accusationFormatter = "%s makes\nan accusation that\n%s\ncommitted the crime\nwith the %s\nin the %s.";

    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Clue";
        cfg.width = SCREEN_WIDTH;
        cfg.height = SCREEN_HEIGHT;
        cfg.addIcon("clue-icon.png", Files.FileType.Classpath);
        new LwjglApplication(new ClueMain(), cfg);

    }

    @Override
    public void create() {

        MAP = new ClueMap();
        PATHFINDER = new AStar<>();
        

    }

}
