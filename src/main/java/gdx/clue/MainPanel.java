package gdx.clue;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import static gdx.clue.ClueMain.SCREEN_DIM_HEIGHT;
import static gdx.clue.ClueMain.TILE_DIM;

public class MainPanel {

    private Table table;
    private ScrollPane pane;

    private TextButton start;
    private TextButton end;
    private TextButton accuse;

    public MainPanel(final Stage stage, final GameScreen screen) {
        this.table = new Table(ClueMain.skin);
        this.table.defaults().pad(8);

        if (this.pane != null) {
            this.pane.remove();
        }

        this.pane = new ScrollPane(this.table, ClueMain.skin);

        this.start = new TextButton("START", ClueMain.skin);
        this.accuse = new TextButton("ACCUSE", ClueMain.skin);
        this.end = new TextButton("END TURN", ClueMain.skin);

        this.table.add(this.start).size(120, 25);
        this.table.row();
        this.table.add(this.accuse).size(120, 25);
        this.table.row();
        this.table.add(this.end).size(120, 25);

        this.start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Sounds.play(Sound.BUTTON);
                new PlayerSelectionDialog(screen.getGame(), screen, start).show(stage);
            }
        });

        this.end.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Sounds.play(Sound.BUTTON);
                ClueMain.END_BUTTON_CLICK_INDICATOR.remove();
                screen.turn(screen.nextPlayer());
            }
        });

        this.accuse.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                Sounds.play(Sound.BUTTON);
            }
        });

        this.pane.setBounds(0, SCREEN_DIM_HEIGHT - TILE_DIM * 5, TILE_DIM * 8, TILE_DIM * 5);
        stage.addActor(pane);
    }

}
