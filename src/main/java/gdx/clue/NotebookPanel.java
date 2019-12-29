package gdx.clue;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import static gdx.clue.Card.*;
import gdx.clue.ClueMain.Suspect;
import static gdx.clue.ClueMain.TILE_DIM;

public class NotebookPanel {

    private Notebook notebook;
    private Table table;
    private ScrollPane pane;

    public void setNotebook(Notebook notebook, Stage stage) {
        this.notebook = notebook;

        this.table = new Table(ClueMain.skin);
        this.table.defaults().padLeft(5).align(Align.left);

        if (this.pane != null) {
            this.pane.remove();
        }

        this.pane = new ScrollPane(this.table, ClueMain.skin);
        
        this.table.add(new Image(notebook.getPlayer().getSuspect().icon()));
        this.table.row();
        this.table.add(new Label(notebook.getPlayer().getPlayerName().toUpperCase() + "'S CLUES", ClueMain.skin));
        this.table.row();
        this.table.add(new Label("(blue = your cards in hand)", ClueMain.skin, "default-blue"));

        this.table.row();
        this.table.add(new Label("", ClueMain.skin));
        this.table.row();
        this.table.add(new Label("SUSPECTS", ClueMain.skin, "default-yellow"));
        for (int i = 0; i < NUM_SUSPECTS; i++) {
            this.table.row();
            Card card = new Card(TYPE_SUSPECT, i);
            this.table.add(new Entry(card));
        }

        this.table.row();
        this.table.add(new Label("", ClueMain.skin));
        this.table.row();
        this.table.add(new Label("WEAPONS", ClueMain.skin, "default-yellow"));
        for (int i = 0; i < NUM_WEAPONS; i++) {
            this.table.row();
            Card card = new Card(TYPE_WEAPON, i);
            this.table.add(new Entry(card));
        }

        this.table.row();
        this.table.add(new Label("", ClueMain.skin));
        this.table.row();
        this.table.add(new Label("ROOMS", ClueMain.skin, "default-yellow"));
        for (int i = 0; i < NUM_ROOMS; i++) {
            this.table.row();
            Card card = new Card(TYPE_ROOM, i);
            this.table.add(new Entry(card));
        }

        pane.setBounds(TILE_DIM * 8 + TILE_DIM * 24 + 2, 0, TILE_DIM * 8, TILE_DIM * 25);
        stage.addActor(pane);

    }

    public void setBystanderIndicator(boolean flag) {
        //setCellData(cellArray[1][cell_count_yaxis - 1], null, flag ? "Bystanding" : "", false, Color.red);
    }

    private class Entry extends Group {

        Card card;
        CheckBox checkbox;

        Entry(Card card) {
            this.card = card;
            if (notebook.isCardInHand(card)) {
                this.checkbox = new CheckBox(card.toString(), ClueMain.skin, "card-in-hand");
                this.checkbox.setDisabled(true);
                this.checkbox.setChecked(true);
            } else {
                this.checkbox = new CheckBox(card.toString(), ClueMain.skin);
            }
            this.checkbox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    notebook.setToggled(Entry.this.card);
                    Sounds.play(Sound.BUTTON);
                }
            });
            this.addActor(this.checkbox);
            this.setBounds(getX(), getY(), 200, 20);
        }

    }

}
