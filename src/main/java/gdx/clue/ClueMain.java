package gdx.clue;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ClueMain extends Game {

    public static final int TILE_DIM = 32;
    public static final int SCREEN_DIM_WIDTH = TILE_DIM * 40;//8 + 24 + 8
    public static final int SCREEN_DIM_HEIGHT = TILE_DIM * 25;

    public static final Dice DICE = new Dice(1, 6);

    public static boolean difficult_setting = false;

    public static final String formatter = "%s suggests\n%s\ncommitted the crime\nwith the %s\nin the %s.";
    public static final String accusationFormatter = "%s makes\nan accusation that\n%s\ncommitted the crime\nwith the %s\nin the %s.\n\nThe accusation is %s.";

    public static Skin skin;

    public static TextureRegion[][] CHAR_ICONS;
    public static TextureRegion[][] DICE_TEXTURES;

    public static Texture ROOMS;
    public static Actor ACTIVE_INDICATOR;

    public static TextButton START_BUTTON;
    public static TextButton END_BUTTON;
    public static TextButton ACCUSE_BUTTON;

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

        skin = new Skin(Gdx.files.classpath("assets/skin/uiskin.json"));

        ROOMS = new Texture(Gdx.files.classpath("room-sheet.png"));
        DICE_TEXTURES = TextureRegion.split(new Texture(Gdx.files.classpath("DiceSheet.png")), 56, 56);
        CHAR_ICONS = TextureRegion.split(new Texture(Gdx.files.classpath("char-icons.png")), 54, 62);

        TILE_BROWN = createSquare(Color.TAN, Color.YELLOW, TILE_DIM, TILE_DIM);
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

    public static Texture createSquare(Color color, Color border, int w, int h) {
        Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pix.setColor(border);
        pix.fill();
        pix.setColor(color);
        pix.fillRectangle(1, 1, w - 2, h - 2);
        return new Texture(pix);
    }

    public static Texture getCursorTexture() {
        Pixmap pixmap = new Pixmap(TILE_DIM, TILE_DIM, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        int w = 4;
        pixmap.fillRectangle(0, 0, w, TILE_DIM);
        pixmap.fillRectangle(TILE_DIM - w, 0, w, TILE_DIM);
        pixmap.fillRectangle(w, 0, TILE_DIM - 2 * w, w);
        pixmap.fillRectangle(w, TILE_DIM - w, TILE_DIM - 2 * w, w);
        return new Texture(pixmap);
    }

    public static enum Suspect {

        SCARLET(0, 0, Color.RED, 0, "Miss Scarlet", 7, 24),
        WHITE(0, 2, Color.WHITE, 1, "Mrs. White", 9, 0),
        PLUM(1, 2, Color.PURPLE, 2, "Professor Plum", 23, 18),
        MUSTARD(0, 1, Color.GOLDENROD, 3, "Colonel Mustard", 0, 17),
        GREEN(1, 0, Color.FOREST, 4, "Mr. Green", 13, 0),
        PEACOCK(1, 1, Color.TEAL, 5, "Mrs. Peacock", 23, 5);

        private int ix;
        private int iy;
        private TextureRegion icon;
        private Texture circle;
        private Color color;
        private int id;
        private String title;
        private int startX;
        private int startY;

        Suspect(int ix, int iy, Color c, int id, String title, int sx, int sy) {
            this.id = id;
            this.color = c;
            this.circle = createCircle(c, TILE_DIM, TILE_DIM, 10);
            this.icon = CHAR_ICONS[ix][iy];
            this.title = title;
            this.startX = sx;
            this.startY = sy;
        }

        public TextureRegion icon() {
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
