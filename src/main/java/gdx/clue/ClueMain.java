package gdx.clue;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.ArrayList;
import gdx.clue.astar.AStar;
import gdx.clue.astar.Location;
import gdx.clue.astar.PathFinder;

public class ClueMain extends Game {

    public static Clue CLUE_GAME;
    public static ClueMap MAP;

    public static final int TILE_DIM = 32;
    public static final int SCREEN_DIM_WIDTH = TILE_DIM * 40;//8 + 24 + 8
    public static final int SCREEN_DIM_HEIGHT = TILE_DIM * 25;
    public static final int VIEWPORT_DIM_WIDTH = TILE_DIM * 24;
    public static final int VIEWPORT_DIM_HEIGHT = TILE_DIM * 25;

    public static PathFinder<Location> PATHFINDER;

    public static final Dice DICE = new Dice(1, 6);

    public static ArrayList<Player> PLAYERS = null;
    public static Player currentTurnPlayer = null;
    public static Player yourPlayer = null;

    public static boolean difficult_setting = false;

    public static final String formatter = "%s suggests\n%s\ncommitted the crime\nwith the %s\nin the %s.";
    public static final String accusationFormatter = "%s makes\nan accusation that\n%s\ncommitted the crime\nwith the %s\nin the %s.";

    public static Skin skin;
    public static BitmapFont font_14;
    public static BitmapFont font_18;
    public static BitmapFont font_24;

    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Clue";
        cfg.width = SCREEN_DIM_WIDTH;
        cfg.height = SCREEN_DIM_HEIGHT;
        cfg.addIcon("clue-icon.png", Files.FileType.Classpath);
        new LwjglApplication(new ClueMain(), cfg);

    }

    @Override
    public void create() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.classpath("assets/fonts/gnuolane.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 14;
        font_14 = generator.generateFont(parameter);

        parameter.size = 18;
        font_18 = generator.generateFont(parameter);
        
        parameter.size = 24;
        font_24 = generator.generateFont(parameter);

        parameter.size = 14;
        parameter.color = Color.BLACK;
        BitmapFont small = generator.generateFont(parameter);

        generator.dispose();

        skin = new Skin(Gdx.files.classpath("assets/skin/uiskin.json"));
        skin.remove("default-font", BitmapFont.class);
        skin.add("default-font", font_14, BitmapFont.class);
        skin.add("small-font", small, BitmapFont.class);
        Label.LabelStyle ls = new Label.LabelStyle();
        skin.add("small-font", ls, Label.LabelStyle.class);
        ls.font = small;

        MAP = new ClueMap();
        PATHFINDER = new AStar<>();

    }

}
