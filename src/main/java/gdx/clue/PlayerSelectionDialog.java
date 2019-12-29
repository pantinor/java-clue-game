package gdx.clue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import static gdx.clue.Card.*;
import static gdx.clue.Player.*;

public class PlayerSelectionDialog extends Window {

    public static int WIDTH = 300;
    public static int HEIGHT = 400;

    Actor previousKeyboardFocus, previousScrollFocus;
    private final FocusListener focusListener;
    private final GameScreen screen;

    public PlayerSelectionDialog(Clue game, GameScreen screen, final TextButton startButton) {
        super("Player Selection", ClueMain.skin.get("dialog", Window.WindowStyle.class));
        this.screen = screen;

        setSkin(ClueMain.skin);
        setModal(true);
        defaults().pad(10);

        Table table = new Table();
        table.align(Align.left | Align.top);
        table.columnDefaults(0).expandX().left().uniformX();
        table.columnDefaults(1).expandX().left().uniformX();

        ScrollPane sp = new ScrollPane(table, ClueMain.skin);
        add(sp).expand().fill().minWidth(200);
        row();

        table.add(new Label("Select your player", ClueMain.skin));
        table.row();
        final CheckBox cb1 = new CheckBox("Miss Scarlet", ClueMain.skin, "selection-blue");
        final CheckBox cb2 = new CheckBox("Mr. Green", ClueMain.skin, "selection-blue");
        final CheckBox cb3 = new CheckBox("Mrs. White", ClueMain.skin, "selection-blue");
        final CheckBox cb4 = new CheckBox("Professor Plum", ClueMain.skin, "selection-blue");
        final CheckBox cb5 = new CheckBox("Mrs. Peacock", ClueMain.skin, "selection-blue");
        final CheckBox cb6 = new CheckBox("Colonel Mustard", ClueMain.skin, "selection-blue");
        ButtonGroup buttonGroup = new ButtonGroup(cb1, cb2, cb3, cb4, cb5, cb6);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(0);
        table.add(cb1);
        table.add(cb4);
        table.row();
        table.add(cb2);
        table.add(cb5);
        table.row();
        table.add(cb3);
        table.add(cb6);
        table.row();

        table.add(new Label("", ClueMain.skin));
        table.row();

        table.add(new Label("Select at least 2 opposing players", ClueMain.skin));
        table.row();
        final CheckBox cb11 = new CheckBox("Miss Scarlet", ClueMain.skin, "selection-yellow");
        final CheckBox cb12 = new CheckBox("Mr. Green", ClueMain.skin, "selection-yellow");
        final CheckBox cb13 = new CheckBox("Mrs. White", ClueMain.skin, "selection-yellow");
        final CheckBox cb14 = new CheckBox("Professor Plum", ClueMain.skin, "selection-yellow");
        final CheckBox cb15 = new CheckBox("Mrs. Peacock", ClueMain.skin, "selection-yellow");
        final CheckBox cb16 = new CheckBox("Colonel Mustard", ClueMain.skin, "selection-yellow");
        table.add(cb11);
        table.add(cb14);
        table.row();
        table.add(cb12);
        table.add(cb15);
        table.row();
        table.add(cb13);
        table.add(cb16);
        table.row();

        table.add(new Label("", ClueMain.skin));
        table.row();

        TextButton close = new TextButton("OK", ClueMain.skin);
        close.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.toString().equals("touchDown")) {

                    int count = 0;
                    if (cb11.isChecked()) {
                        count++;
                    }
                    if (cb12.isChecked()) {
                        count++;
                    }
                    if (cb13.isChecked()) {
                        count++;
                    }
                    if (cb14.isChecked()) {
                        count++;
                    }
                    if (cb15.isChecked()) {
                        count++;
                    }
                    if (cb16.isChecked()) {
                        count++;
                    }

                    if (count < 2) {
                        return false;
                    }

                    if (cb1.isChecked() && cb11.isChecked()) {
                        return false;
                    }
                    if (cb2.isChecked() && cb12.isChecked()) {
                        return false;
                    }
                    if (cb3.isChecked() && cb13.isChecked()) {
                        return false;
                    }
                    if (cb4.isChecked() && cb14.isChecked()) {
                        return false;
                    }
                    if (cb5.isChecked() && cb15.isChecked()) {
                        return false;
                    }
                    if (cb6.isChecked() && cb16.isChecked()) {
                        return false;
                    }

                    if (cb1.isChecked()) {
                        game.addPlayer(Card.scarlet, "Player", COLOR_SCARLET, false);
                    }
                    if (cb2.isChecked()) {
                        game.addPlayer(Card.green, "Player", COLOR_GREEN, false);
                    }
                    if (cb3.isChecked()) {
                        game.addPlayer(Card.white, "Player", COLOR_WHITE, false);
                    }
                    if (cb4.isChecked()) {
                        game.addPlayer(Card.plum, "Player", COLOR_PLUM, false);
                    }
                    if (cb5.isChecked()) {
                        game.addPlayer(Card.peacock, "Player", COLOR_PEACOCK, false);
                    }
                    if (cb6.isChecked()) {
                        game.addPlayer(Card.mustard, "Player", COLOR_MUSTARD, false);
                    }

                    if (cb11.isChecked()) {
                        game.addPlayer(Card.scarlet, "", COLOR_SCARLET, true);
                    }
                    if (cb12.isChecked()) {
                        game.addPlayer(Card.green, "", COLOR_GREEN, true);
                    }
                    if (cb13.isChecked()) {
                        game.addPlayer(Card.white, "", COLOR_WHITE, true);
                    }
                    if (cb14.isChecked()) {
                        game.addPlayer(Card.plum, "", COLOR_PLUM, true);
                    }
                    if (cb15.isChecked()) {
                        game.addPlayer(Card.peacock, "", COLOR_PEACOCK, true);
                    }
                    if (cb16.isChecked()) {
                        game.addPlayer(Card.mustard, "", COLOR_MUSTARD, true);
                    }

                    screen.startGame();
                    
                    startButton.setDisabled(true);

                    hide();
                }
                return false;
            }
        });
        table.add(close);

        focusListener = new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                if (!focused) {
                    focusChanged(event);
                }
            }

            @Override
            public void scrollFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                if (!focused) {
                    focusChanged(event);
                }
            }

            private void focusChanged(FocusListener.FocusEvent event) {
                Stage stage = getStage();
                if (isModal() && stage != null && stage.getRoot().getChildren().size > 0 && stage.getRoot().getChildren().peek() == PlayerSelectionDialog.this) {
                    Actor newFocusedActor = event.getRelatedActor();
                    if (newFocusedActor != null && !newFocusedActor.isDescendantOf(PlayerSelectionDialog.this) && !(newFocusedActor.equals(previousKeyboardFocus) || newFocusedActor.equals(previousScrollFocus))) {
                        event.cancel();
                    }
                }
            }
        };
    }

    public void show(Stage stage) {

        clearActions();

        removeCaptureListener(ignoreTouchDown);

        previousKeyboardFocus = null;
        Actor actor = stage.getKeyboardFocus();
        if (actor != null && !actor.isDescendantOf(this)) {
            previousKeyboardFocus = actor;
        }

        previousScrollFocus = null;
        actor = stage.getScrollFocus();
        if (actor != null && !actor.isDescendantOf(this)) {
            previousScrollFocus = actor;
        }

        pack();

        stage.addActor(this);
        //stage.setKeyboardFocus(playerSelection);
        stage.setScrollFocus(this);

        Gdx.input.setInputProcessor(stage);

        Action action = sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade));
        addAction(action);

        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
    }

    public void hide() {
        Action action = sequence(fadeOut(0.4f, Interpolation.fade), Actions.removeListener(ignoreTouchDown, true), Actions.removeActor());

        Stage stage = getStage();

        if (stage != null) {
            removeListener(focusListener);
        }

        if (action != null) {
            addCaptureListener(ignoreTouchDown);
            addAction(sequence(action, Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
        } else {
            remove();
        }

        Gdx.input.setInputProcessor(new InputMultiplexer(screen, stage));
    }

    protected InputListener ignoreTouchDown = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            event.cancel();
            return false;
        }
    };

}
