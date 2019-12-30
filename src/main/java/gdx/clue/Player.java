package gdx.clue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import gdx.clue.ClueMain.Suspect;
import java.util.ArrayList;
import java.util.List;
import gdx.clue.astar.Location;

public class Player {

    private final Suspect suspect;
    private String name;
    private Card card;
    private final List<Card> cardsInHand = new ArrayList<>();
    private boolean computerPlayer;
    private Location location;
    private Notebook notebook;
    private Actor stageActor;
    private boolean hasMadeFalseAccusation = false;

    public Player(Card card, String name, Suspect suspect, boolean computer) {
        this.name = name;
        this.card = card;
        this.suspect = suspect;
        this.computerPlayer = computer;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }

    public Notebook getNotebook() {
        return this.notebook;
    }

    public void addCard(Card card) {
        this.cardsInHand.add(card);
    }

    public List<Card> getCardsInHand() {
        return this.cardsInHand;
    }

    public boolean isCardInHand(Card card) {
        return this.cardsInHand.contains(card);
    }

    public boolean isCardInHand(int type, int id) {
        Card card = new Card(type, id);
        return this.cardsInHand.contains(card);
    }

    public boolean isHoldingCardInSuggestion(List<Card> suggestion) {
        boolean hasCards = false;
        for (Card card : this.cardsInHand) {
            if (suggestion.contains(card)) {
                hasCards = true;
            }
        }
        return hasCards;
    }

    public Actor getStageActor() {
        return stageActor;
    }

    public void setStageActor(Actor stageActor) {
        this.stageActor = stageActor;
    }

    @Override
    public String toString() {
        return "Player{" + "suspect=" + suspect + ", card=" + card + ", computerPlayer=" + computerPlayer + ", location=" + location + '}';
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card playerCard) {
        this.card = playerCard;
    }

    public Color getPlayerColor() {
        return this.suspect.color();
    }

    public String getPlayerName() {
        return name;
    }

    public void setPlayerName(String playerName) {
        this.name = playerName;
    }

    public Suspect getSuspect() {
        return this.suspect;
    }

    public boolean isComputerPlayer() {
        return computerPlayer;
    }

    public void setComputerPlayer(boolean computerPlayer) {
        this.computerPlayer = computerPlayer;
    }

    public boolean hasMadeFalseAccusation() {
        return hasMadeFalseAccusation;
    }

    public void setHasMadeFalseAccusation() {
        hasMadeFalseAccusation = true;
    }

}
