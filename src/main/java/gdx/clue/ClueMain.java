package gdx.clue;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClueMain extends Game {

    public static final int TILE_DIM = 32;
    public static final int SCREEN_DIM_WIDTH = TILE_DIM * 40;//8 + 24 + 8
    public static final int SCREEN_DIM_HEIGHT = TILE_DIM * 25;

    public static final Dice DICE = new Dice(1, 6);

    public static boolean difficult_setting = false;

    public static final String formatter = "%s suggests\n%s\ncommitted the crime\nwith the %s\nin the %s.";
    public static final String accusationFormatter = "%s makes\nan accusation that\n%s\ncommitted the crime\nwith the %s\nin the %s.";

    public static Skin skin;
    public static BitmapFont FONT_14;
    public static BitmapFont FONT_18;
    public static BitmapFont FONT_24;
    public static BitmapFont FONT_48;

    public static TextureRegion[][] DICE_TEXTURES;

    public static Texture ROOMS;

    public static Texture TILE_BROWN;
    public static Texture TILE_LIGHT_GRAY;
    public static Texture TILE_DARK_GREEN;

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
        FONT_14 = generator.generateFont(parameter);

        parameter.size = 18;
        FONT_18 = generator.generateFont(parameter);

        parameter.size = 24;
        parameter.color = Color.YELLOW;
        FONT_24 = generator.generateFont(parameter);

        parameter.size = 48;
        parameter.color = Color.FOREST;
        FONT_48 = generator.generateFont(parameter);

        parameter.size = 14;
        parameter.color = Color.BLACK;
        BitmapFont small = generator.generateFont(parameter);

        generator.dispose();

        skin = new Skin(Gdx.files.classpath("assets/skin/uiskin.json"));
        skin.remove("default-font", BitmapFont.class);
        skin.add("default-font", FONT_14, BitmapFont.class);
        skin.add("small-font", small, BitmapFont.class);
        Label.LabelStyle ls = new Label.LabelStyle();
        skin.add("small-font", ls, Label.LabelStyle.class);
        ls.font = small;

        ROOMS = new Texture(Gdx.files.classpath("room-sheet.png"));
        DICE_TEXTURES = TextureRegion.split(new Texture(Gdx.files.classpath("DiceSheet.png")), 51, 51);

        TILE_BROWN = createSquare(Color.FIREBRICK, Color.BROWN, TILE_DIM, TILE_DIM);
        TILE_LIGHT_GRAY = createSquare(Color.LIGHT_GRAY, Color.GRAY, TILE_DIM, TILE_DIM);
        TILE_DARK_GREEN = createSquare(Color.GREEN, Color.FOREST, TILE_DIM, TILE_DIM);

        GameScreen sc = new GameScreen();
        setScreen(sc);

    }

    private static Texture createCircle(Color color, int w, int h, int radius) {
        Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fillCircle(w / 2, h / 2, radius);
        return new Texture(pix);
    }

    private static Texture createSquare(Color color, Color border, int w, int h) {
        Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pix.setColor(border);
        pix.fill();
        pix.setColor(color);
        pix.fillRectangle(1, 1, w - 2, h - 2);
        return new Texture(pix);
    }

    public static enum Suspect {

        SCARLET("MsScarlett1.png", Color.RED, 0, "Miss Scarlet", 7, 24),
        WHITE("MrsWhite1.png", Color.ORANGE, 1, "Mrs. White", 9, 0),
        PLUM("ProfPlum1.png", Color.PURPLE, 2, "Professor Plum", 23, 18),
        MUSTARD("ColMustard1.png", Color.GOLDENROD, 3, "Colonel Mustard", 0, 17),
        GREEN("MrGreen1.png", Color.FOREST, 4, "Mr. Green", 13, 0),
        PEACOCK("MrsPeacock1.png", Color.MAGENTA, 5, "Mrs. Peacock", 23, 5);

        private Texture icon;
        private Texture circle;
        private Color color;
        private int id;
        private String title;
        private int startX;
        private int startY;

        Suspect(String filename, Color c, int id, String title, int sx, int sy) {
            this.id = id;
            this.color = c;
            this.circle = createCircle(c, TILE_DIM, TILE_DIM, 8);
            this.icon = new Texture(Gdx.files.internal(filename));
            this.title = title;
            this.startX = sx;
            this.startY = sy;
        }

        public Texture icon() {
            return icon;
        }

        public Texture circle() {
            return circle;
        }

        public int id() {
            return this.id;
        }

        public Color color() {
            return this.color;
        }

        public String title() {
            return this.title;
        }

        public int startX() {
            return this.startX;
        }

        public int startY() {
            return this.startY;
        }
    }

}
