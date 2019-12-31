package gdx.clue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import gdx.clue.ClueMain.Suspect;
import java.util.ArrayList;
import java.util.List;
import gdx.clue.astar.Location;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.suspect);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (this.suspect != other.suspect) {
            return false;
        }
        return true;
    }

    public void setLocation(Location location) {

        if (location == null) {
            return;
        }

        //reset the height back to 100
        if (this.location != null) {
            this.location.setHeight(100);
        }

        this.location = location;

        //allow multiple players on a room tile but block regular tiles with one player
        if (!this.location.isRoom()) {
            this.location.setHeight(1000);
        }
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
        return suspect + "\n" + this.notebook.toString();
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
