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

    public MainPanel(final Stage stage, final GameScreen screen) {
        this.table = new Table(ClueMain.skin);
        this.table.defaults().pad(5);

        if (this.pane != null) {
            this.pane.remove();
        }

        this.pane = new ScrollPane(this.table, ClueMain.skin);

        ClueMain.START_BUTTON = new TextButton("START", ClueMain.skin);
        ClueMain.ACCUSE_BUTTON = new TextButton("ACCUSE", ClueMain.skin);
        ClueMain.END_BUTTON = new TextButton("END TURN", ClueMain.skin, "end-turn");
        TextButton debug = new TextButton("debug", ClueMain.skin);

        this.table.add(ClueMain.START_BUTTON).size(120, 25);
        this.table.row();
        this.table.add(ClueMain.ACCUSE_BUTTON).size(120, 25);
        this.table.row();
        this.table.add(ClueMain.END_BUTTON).size(120, 25);

        this.table.row();
        this.table.add(debug).size(120, 25);

        ClueMain.END_BUTTON.setVisible(false);
        ClueMain.ACCUSE_BUTTON.setVisible(false);

        ClueMain.START_BUTTON.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Sounds.play(Sound.BUTTON);
                ClueMain.ACCUSE_BUTTON.setVisible(true);
                new PlayerSelectionDialog(screen.getGame(), screen).show(stage);
            }
        });

        ClueMain.END_BUTTON.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ClueMain.END_BUTTON.setVisible(false);
                Sounds.play(Sound.BUTTON);
                screen.turn(screen.nextPlayer());
            }
        });

        ClueMain.ACCUSE_BUTTON.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Sounds.play(Sound.BUTTON);
                new AccusationDialog(screen, screen.getYourPlayer()).show(stage);
            }
        });

        debug.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Sounds.play(Sound.BUTTON);
                System.out.println(screen.getGame().toString());
            }
        });

        this.pane.setBounds(0, SCREEN_DIM_HEIGHT - TILE_DIM * 5, TILE_DIM * 8, TILE_DIM * 5);
        stage.addActor(pane);
    }

}
