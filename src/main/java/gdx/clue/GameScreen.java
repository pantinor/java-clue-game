package gdx.clue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static gdx.clue.Card.*;
import static gdx.clue.ClueMain.TILE_DIM;
import static gdx.clue.ClueMain.SCREEN_DIM_HEIGHT;
import gdx.clue.ClueMain.Suspect;
import gdx.clue.astar.AStar;
import gdx.clue.astar.Location;
import gdx.clue.astar.PathFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GameScreen implements Screen, InputProcessor {

    private final Stage stage;
    private final Batch batch;
    private final Viewport viewport = new ScreenViewport();
    private final InputMultiplexer input;
    private final Clue game;
    private final ClueMap map;
    private final PathFinder<Location> pathfinder;

    private int index;
    private Player currentTurnPlayer;
    private Player yourPlayer;

    private final Vector3 screenPos = new Vector3();
    private final Vector3 gridPos = new Vector3();

    private final RoomIconPlacement playerIconPlacement;
    private TextureRegion rolledDiceImageLeft;
    private TextureRegion rolledDiceImageRight;
    private final NotebookPanel notebookPanel = new NotebookPanel();
    private final MainPanel mainPanel;
    private final LogScrollPane logPanel = new LogScrollPane(new Table());
    private final ShowCardsRoutine showCards;

    public static final int ACTION_VALID_ACCUSATION = 200;
    public static final int ACTION_INVALID_ACCUSATION = 500;
    public static final int ACTION_MADE_SUGGESTION = 300;
    public static final int ACTION_TOOK_PASSAGE = 310;
    public static final int ACTION_ROLLED_DICE = 320;

    private boolean gameOver;

    public GameScreen() {
        game = new Clue();
        map = new ClueMap();
        pathfinder = new AStar<>();
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        input = new InputMultiplexer(this, stage);
        playerIconPlacement = new RoomIconPlacement();
        mainPanel = new MainPanel(stage, this);
        showCards = new ShowCardsRoutine(this);

        stage.addActor(logPanel);

        Label label1 = new Label("Kitchen", ClueMain.skin, "default-yellow");
        label1.setBounds(TILE_DIM * 8 + 50, SCREEN_DIM_HEIGHT - 75, 300, 25);
        stage.addActor(label1);

        Label label2 = new Label("Ballroom", ClueMain.skin, "default-yellow");
        label2.setBounds(TILE_DIM * 8 + 323, SCREEN_DIM_HEIGHT - 128, 300, 25);
        stage.addActor(label2);

        Label label3 = new Label("Conservatory", ClueMain.skin, "default-yellow");
        label3.setBounds(TILE_DIM * 8 + 610, SCREEN_DIM_HEIGHT - 75, 300, 25);
        stage.addActor(label3);

        Label label4 = new Label("Dining Room", ClueMain.skin, "default-yellow");
        label4.setBounds(TILE_DIM * 8 + 50, SCREEN_DIM_HEIGHT - 400, 300, 25);
        stage.addActor(label4);

        Label label5 = new Label("Billiard Room", ClueMain.skin, "default-yellow");
        label5.setBounds(TILE_DIM * 8 + 621, SCREEN_DIM_HEIGHT - 335, 300, 25);
        stage.addActor(label5);

        Label label6 = new Label("Library", ClueMain.skin, "default-yellow");
        label6.setBounds(TILE_DIM * 8 + 610, SCREEN_DIM_HEIGHT - 529, 300, 25);
        stage.addActor(label6);

        Label label7 = new Label("Study", ClueMain.skin, "default-yellow");
        label7.setBounds(TILE_DIM * 8 + 610, SCREEN_DIM_HEIGHT - 712, 300, 25);
        stage.addActor(label7);

        Label label8 = new Label("Hall", ClueMain.skin, "default-yellow");
        label8.setBounds(TILE_DIM * 8 + 370, SCREEN_DIM_HEIGHT - 712, 300, 25);
        stage.addActor(label8);

        Label label9 = new Label("Lounge", ClueMain.skin, "default-yellow");
        label9.setBounds(TILE_DIM * 8 + 100, SCREEN_DIM_HEIGHT - 712, 300, 25);
        stage.addActor(label9);

        Label label10 = new Label("CLUE", ClueMain.skin, "default-green");
        label10.setBounds(TILE_DIM * 8 + 360, SCREEN_DIM_HEIGHT - 375, 300, 25);
        stage.addActor(label10);

        ClueMain.ACTIVE_INDICATOR = new Actor() {
            Texture t = ClueMain.getCursorTexture();

            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                batch.draw(t, getX(), getY());
            }
        };

        ClueMain.ACTIVE_INDICATOR.addAction(forever(sequence(fadeOut(0.5f), fadeIn(0.5f))));
    }

    public Clue getGame() {
        return this.game;
    }

    public ClueMap getMap() {
        return this.map;
    }

    public Stage getStage() {
        return this.stage;
    }

    public PathFinder<Location> getPathfinder() {
        return this.pathfinder;
    }

    public ShowCardsRoutine getShowCards() {
        return showCards;
    }

    public Player getYourPlayer() {
        return yourPlayer;
    }

    public RoomIconPlacement getPlayerIconPlacement() {
        return playerIconPlacement;
    }

    public int rollDice() {

        int roll1 = ClueMain.DICE.roll();
        int roll2 = ClueMain.DICE.roll();
        Sounds.play(Sound.DICE);

        rolledDiceImageLeft = ClueMain.DICE_TEXTURES[0][roll1 - 1];
        rolledDiceImageRight = ClueMain.DICE_TEXTURES[0][roll2 - 1];

        return roll1 + roll2;

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(ClueMain.ROOMS, TILE_DIM * 8, 0);

        for (int i = 0; i < map.getXSize(); i++) {
            for (int j = 0; j < map.getYSize(); j++) {
                Location t = map.getLocation(i, j);
                if (t.getBlocked()) {
                    //nothing
                } else if (t.getHighlighted()) {
                    batch.draw(ClueMain.TILE_DARK_GREEN, TILE_DIM * 8 + i * TILE_DIM, SCREEN_DIM_HEIGHT - j * TILE_DIM - TILE_DIM);
                } else if (t.isRoom()) {
                    batch.draw(ClueMain.TILE_BROWN, TILE_DIM * 8 + i * TILE_DIM, SCREEN_DIM_HEIGHT - j * TILE_DIM - TILE_DIM);
                } else {
                    batch.draw(ClueMain.TILE_LIGHT_GRAY, TILE_DIM * 8 + i * TILE_DIM, SCREEN_DIM_HEIGHT - j * TILE_DIM - TILE_DIM);
                }
            }
        }

        if (rolledDiceImageLeft != null) {
            batch.draw(rolledDiceImageLeft, TILE_DIM * 8 + 350, SCREEN_DIM_HEIGHT - 475);
        }
        if (rolledDiceImageRight != null) {
            batch.draw(rolledDiceImageRight, TILE_DIM * 8 + 403, SCREEN_DIM_HEIGHT - 475);
        }

        //draw suspect icons for players in rooms
        playerIconPlacement.drawIcons(batch);

        ClueMain.skin.get(BitmapFont.class).draw(batch, String.format("%s, %s\n", gridPos.x, gridPos.y), 20, 20);

        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenPos.x = screenX;
        screenPos.y = screenY;

        gridPos.set(
                Math.round(((screenPos.x - TILE_DIM * 8) / TILE_DIM) - 0.5f),
                Math.round(((screenPos.y - TILE_DIM) / TILE_DIM) + 0.5f),
                0);

        Location loc = map.getLocation((int) gridPos.x, (int) gridPos.y);
        if (loc != null && currentTurnPlayer == yourPlayer && loc.getHighlighted() && !loc.equals(yourPlayer.getLocation())) {
            map.resetHighlights();
            setPlayerLocationFromMapClick(currentTurnPlayer, loc);
            ClueMain.END_BUTTON.setVisible(true);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public Player nextPlayer() {
        this.index++;
        if (this.index >= this.game.getPlayers().size()) {
            this.index = 0;
        }
        Player player = this.game.getPlayers().get(this.index);
        setCurrentPlayer(player);
        return player;
    }

    public void setCurrentPlayer(Player player) {
        if (this.currentTurnPlayer != null) {
            this.currentTurnPlayer.getStageActor().clearActions();
        }
        this.currentTurnPlayer = player;

        Location location = player.getLocation();
        int x = TILE_DIM * 8 + location.getX() * TILE_DIM;
        int y = SCREEN_DIM_HEIGHT - TILE_DIM * location.getY() - TILE_DIM;

        ClueMain.ACTIVE_INDICATOR.setBounds(x, y, TILE_DIM, TILE_DIM);
    }

    public void setPlayerLocationFromMapClick(Player player, Location location) {
        playerIconPlacement.removePlayerIcon(player.getSuspect().id());
        playerIconPlacement.addPlayerIcon(location.getRoomId(), player.getSuspect().id());
        player.setLocation(location);

        int x = TILE_DIM * 8 + location.getX() * TILE_DIM;
        int y = SCREEN_DIM_HEIGHT - TILE_DIM * location.getY() - TILE_DIM;

        ClueMain.ACTIVE_INDICATOR.setBounds(x, y, TILE_DIM, TILE_DIM);
    }

    public void addMessage(String text, Color color) {
        this.logPanel.add(text, color);
    }

    private class PlayerDotActor extends Actor {

        public Player player;

        public PlayerDotActor(Player player) {
            this.player = player;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            Location location = player.getLocation();
            int x = TILE_DIM * 8 + location.getX() * TILE_DIM;
            int y = SCREEN_DIM_HEIGHT - TILE_DIM * location.getY() - TILE_DIM;

            this.setX(x);
            this.setY(y);

            batch.draw(player.getSuspect().circle(), getX(), getY());
        }

    }

    private class TurnDialog extends Dialog {

        Location location;
        Player player;

        public TurnDialog(Location location, Player player, boolean showDiceButton, boolean showSecretPassageButton, boolean showSuggestionButton) {
            super("YOUR TURN! YOU MAY CHOOSE:", ClueMain.skin, "dialog");
            this.location = location;
            this.player = player;
            if (showDiceButton) {
                button("ROLL DICE", ACTION_ROLLED_DICE);
            }
            if (showSecretPassageButton) {
                button("TAKE SECRET PASSAGE", ACTION_TOOK_PASSAGE);
            }
            if (showSuggestionButton) {
                button("MAKE SUGGESTION", ACTION_MADE_SUGGESTION);
            }

            getButtonTable().pad(10);
        }

        @Override
        protected void result(Object action) {

            if (action.equals(ACTION_ROLLED_DICE)) {
                map.resetHighlights();
                int roll = rollDice();

                addMessage("You rolled a " + roll + ".  Select your next location.", player.getSuspect().color());

                map.highlightReachablePaths(location, pathfinder, roll);
            }

            if (action.equals(ACTION_TOOK_PASSAGE)) {

                int current_room = location.getRoomId();
                switch (current_room) {
                    case ROOM_LOUNGE:
                        setPlayerLocationFromMapClick(this.player, map.getRoomLocation(ROOM_CONSERVATORY));
                        break;
                    case ROOM_STUDY:
                        setPlayerLocationFromMapClick(this.player, map.getRoomLocation(ROOM_KITCHEN));
                        break;
                    case ROOM_CONSERVATORY:
                        setPlayerLocationFromMapClick(this.player, map.getRoomLocation(ROOM_LOUNGE));
                        break;
                    case ROOM_KITCHEN:
                        setPlayerLocationFromMapClick(this.player, map.getRoomLocation(ROOM_STUDY));
                        break;
                }

                ClueMain.END_BUTTON.setVisible(true);
            }

            if (action.equals(ACTION_MADE_SUGGESTION)) {

                int room_id = player.getLocation().getRoomId();
                if (room_id == -1) {
                    return;
                }

                SuggestionDialog sg = new SuggestionDialog(showCards, GameScreen.this, yourPlayer, new Card(TYPE_ROOM, room_id));
                sg.show(stage);
            }

        }
    }

    public void startGame() {

        this.game.createDeck();
        this.game.dealShuffledDeck();

        for (int i = 0; i < this.game.getPlayers().size(); i++) {
            Player player = this.game.getPlayers().get(i);

            Notebook book = new Notebook(player);
            player.setNotebook(book);

            Suspect sus = player.getSuspect();
            player.setLocation(this.map.getLocation(sus.startX(), sus.startY()));

            Actor actor = new PlayerDotActor(player);
            player.setStageActor(actor);
            this.stage.addActor(actor);

            if (!player.isComputerPlayer()) {
                this.index = i;
                setCurrentPlayer(player);
                yourPlayer = player;
                notebookPanel.setNotebook(book, this.stage);
            }
        }

        stage.addActor(ClueMain.ACTIVE_INDICATOR);

        turn(currentTurnPlayer);
    }

    public void turn(Player player) {

        if (player.isComputerPlayer()) {

            Location startingLocation = player.getLocation();

            Card currentRoomCard = startingLocation.isRoom() ? new Card(TYPE_ROOM, startingLocation.getRoomId()) : null;

            //make a suggestion if the room they are in is not toggled and they did not just enter into a room
            if (startingLocation.isRoom() && !player.getNotebook().isLocationCardInHandOrToggled(currentRoomCard)) {
                makeSuggestionComputerPlayer(player);
            } else {

                if (startingLocation.isRoom() && player.getNotebook().isLocationCardInHandOrToggled(currentRoomCard)) {
                    Sounds.play(Sound.HMM);
                    //indicates maybe your player should pay attention that this room 
                    //is in their hand and you may be able to mark it off in your notebook
                    //but you don't know if the card that was shown was a room card earlier so its not a sure thing
                    addMessage(player.getSuspect().title() + " is leaving the " + currentRoomCard, player.getSuspect().color());
                }

                //move them
                Location newLocation = getNextComputerPlayerLocation(player);
                
                this.map.resetHighlights();
            }

            ClueMain.END_BUTTON.setVisible(true);

        } else {

            Location location = player.getLocation();
            boolean isInRoom = location.getRoomId() != -1;
            boolean showSecret = (location.getRoomId() == ROOM_LOUNGE || location.getRoomId() == ROOM_STUDY || location.getRoomId() == ROOM_CONSERVATORY || location.getRoomId() == ROOM_KITCHEN);

            TurnDialog dialog = new TurnDialog(location, player, true, showSecret, isInRoom);
            dialog.show(this.stage);

        }

    }

    private Location getNextComputerPlayerLocation(Player player) {

        Location new_location = null;

        // try move the player to the room which is not in their cards or toggled in their notebook
        Location currentLocation = player.getLocation();

        List<Location> rooms = map.getAllRoomLocations();

        // remove the rooms which are toggled as marked off in their notebook or in their dealt hand
        for (Iterator<Location> it = rooms.iterator(); it.hasNext();) {
            Location l = (Location) it.next();
            Card room_card = new Card(TYPE_ROOM, l.getRoomId());
            if (player.getNotebook().isLocationCardInHandOrToggled(room_card)) {
                it.remove();
            }
        }

        int roll = rollDice();

        List<Location> reachableLocations = map.highlightReachablePaths(currentLocation, this.pathfinder, roll);

        // secret passage linkages
        if (currentLocation.isRoom()) {
            if (currentLocation.getRoomId() == ROOM_KITCHEN) {
                if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_STUDY))) {
                    reachableLocations.add(map.getRoomLocation(ROOM_STUDY));
                }
            }
            if (currentLocation.getRoomId() == ROOM_STUDY) {
                if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_KITCHEN))) {
                    reachableLocations.add(map.getRoomLocation(ROOM_KITCHEN));
                }
            }
            if (currentLocation.getRoomId() == ROOM_CONSERVATORY) {
                if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_LOUNGE))) {
                    reachableLocations.add(map.getRoomLocation(ROOM_LOUNGE));
                }
            }
            if (currentLocation.getRoomId() == ROOM_LOUNGE) {
                if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_CONSERVATORY))) {
                    reachableLocations.add(map.getRoomLocation(ROOM_CONSERVATORY));
                }
            }
        }

        Collections.shuffle(rooms);
        Collections.shuffle(reachableLocations);

        // see if they can move to a highlighted room which is not in their hand or toggled
        for (Location reachableLocation : reachableLocations) {
            if (rooms.contains(reachableLocation)) {
                new_location = reachableLocation;
                break;
            }
        }

        //move to a closest room which is potential
        if (new_location == null) {
            int closest = 100;
            // find a room location which is closest to them which is not in their hand or toggled
            for (Location reachableLocation : reachableLocations) {
                for (Location room : rooms) {
                    List<Location> path = this.pathfinder.findPath(map.getLocations(), reachableLocation, Collections.singleton(room));
                    if (path.size() <= closest) {
                        closest = path.size();
                        new_location = reachableLocation;
                    }
                }
            }
        }

        addMessage(String.format("%s rolled a %d", player.getSuspect().title(), roll), player.getSuspect().color());

        setPlayerLocationFromMapClick(player, new_location);

        return new_location;
    }

    private void makeSuggestionComputerPlayer(Player player) {

        Location location = player.getLocation();
        if (location.getRoomId() == -1) {
            return;
        }

        Card selected_suspect_card = player.getNotebook().randomlyPickCardOfType(TYPE_SUSPECT);
        Card selected_weapon_card = player.getNotebook().randomlyPickCardOfType(TYPE_WEAPON);
        Card selected_room_card = new Card(TYPE_ROOM, location.getRoomId());

        List<Card> suggestion = new ArrayList<>();
        suggestion.add(selected_suspect_card);
        suggestion.add(selected_room_card);
        suggestion.add(selected_weapon_card);

        this.showCards.setSuggestion(suggestion, player);
        this.showCards.showCards();

    }

    public void makeAccusation(Player player, List<Card> accusation) {

        boolean matches = this.game.matchesVictimSet(accusation);

        String text = String.format(ClueMain.accusationFormatter, player.getSuspect().title(), accusation.get(0), accusation.get(1), accusation.get(2), matches);
        addMessage(text, matches ? Color.GREEN : Color.RED);

        if (matches) {
            Sounds.play(Sound.APPLAUSE);
        } else {
            Sounds.play(Sound.NEGATIVE_EFFECT);
        }

    }
}
