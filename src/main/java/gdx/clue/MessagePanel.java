package gdx.clue;

import java.util.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import static gdx.clue.ClueMain.TILE_DIM;
import java.util.ArrayList;
import org.apache.commons.collections.iterators.ReverseListIterator;

public class MessagePanel {

    static final int LOG_AREA_WIDTH = TILE_DIM * 7;
    static final int LOG_AREA_TOP = 375;
    static final int LOG_X = 16;

    private final BitmapFont font = ClueMain.skin.get(BitmapFont.class);
    private final List<LogEntry> logs = new FixedSizeArrayList<>(25);
    private final GlyphLayout layout = new GlyphLayout(font, "");

    public void add(String s, Color color) {
        synchronized (logs) {
            logs.add(new LogEntry(s, color));
        }
    }

    public void render(Batch batch) {

        int y = 20;

        synchronized (logs) {
            ReverseListIterator iter = new ReverseListIterator(logs);
            while (iter.hasNext()) {
                LogEntry next = (LogEntry) iter.next();
                layout.setText(font, next.text, next.color, LOG_AREA_WIDTH, Align.left, true);
                y += layout.height + 12;
                if (y > LOG_AREA_TOP) {
                    break;
                }
                font.draw(batch, layout, LOG_X, y);
            }
        }
    }

    private class LogEntry {

        private String text;
        private Color color;

        public LogEntry(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }

    private class FixedSizeArrayList<T> extends ArrayList<T> {

        private final int maxSize;

        public FixedSizeArrayList(int maxSize) {
            super();
            this.maxSize = maxSize;
        }

        public boolean add(T t) {
            if (size() >= maxSize) {
                remove(0);
            }
            return super.add(t);
        }

    }
}
