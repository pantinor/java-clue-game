package gdx.clue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import static gdx.clue.ClueMain.TILE_DIM;

public class LogScrollPane extends ScrollPane {

    private final Table internalTable;

    public static final int LOG_AREA_WIDTH = TILE_DIM * 8;

    public LogScrollPane(Table table) {
        super(table, ClueMain.skin);

        setBounds(0, 0, LOG_AREA_WIDTH, TILE_DIM * 20);

        this.internalTable = table;

        clear();
        setScrollingDisabled(true, false);

        internalTable.align(Align.topLeft);
    }

    public void add(String text) {
        add(text, Color.WHITE, true);
    }

    public void add(String text, Color color) {
        add(text, color, true);
    }

    public void add(String text, Color color, boolean scrollBottom) {

        if (text == null) {
            return;
        }

        LabelStyle ls = new LabelStyle(ClueMain.skin.get("default", BitmapFont.class), color != null ? color : Color.WHITE);

        Label label = new Label(text, ls);
        label.setWrap(true);
        label.setAlignment(Align.topLeft, Align.left);

        internalTable.add(label).pad(1).width(LOG_AREA_WIDTH - 10);
        internalTable.row();

        pack();
        if (scrollBottom) {
            scrollTo(0, 0, 0, 0);
        }

    }

    @Override
    public void clear() {
        internalTable.clear();
        pack();
    }

    @Override
    public float getPrefWidth() {
        return this.getWidth();
    }

    @Override
    public float getPrefHeight() {
        return this.getHeight();
    }

    @Override
    public float getMaxWidth() {
        return this.getWidth();
    }

    @Override
    public float getMaxHeight() {
        return this.getHeight();
    }
}
