package gdx.clue;

import static gdx.clue.Card.NUM_ROOMS;
import static gdx.clue.Card.NUM_SUSPECTS;
import static gdx.clue.Card.NUM_WEAPONS;
import static gdx.clue.Card.TOTAL;
import static gdx.clue.Card.TYPE_ROOM;
import static gdx.clue.Card.TYPE_SUSPECT;
import static gdx.clue.Card.TYPE_WEAPON;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import gdx.clue.astar.Location;

public class Notebook {

    private Player player;
    private final LinkedHashMap<Card, Entry> entries = new LinkedHashMap<>();

    public Notebook(Player player) {
        this.setPlayer(player);

        ArrayList<Card> deck = new ArrayList<>(TOTAL);
        for (int i = 0; i < NUM_SUSPECTS; i++) {
            deck.add(new Card(TYPE_SUSPECT, i));
        }
        for (int i = 0; i < NUM_WEAPONS; i++) {
            deck.add(new Card(TYPE_WEAPON, i));
        }
        for (int i = 0; i < NUM_ROOMS; i++) {
            deck.add(new Card(TYPE_ROOM, i));
        }

        for (Card card : deck) {
            entries.put(card, new Entry(card));
        }

        //set cards in hand
        for (Card card : player.getCardsInHand()) {
            Entry entry = entries.get(card);
            entry.setInHand(true);
        }

    }

    public void setToggled(Card card) {
        Entry entry = entries.get(card);
        entry.setToggled(!entry.getToggled());
    }

    public boolean isCardInHand(Card card) {
        Entry entry = entries.get(card);
        return entry.inHand();
    }

    public boolean isCardToggled(Card card) {
        Entry entry = entries.get(card);
        return entry.getToggled();
    }

    public boolean isLocationCardInHandOrToggled(Location location) {
        Card roomCard = (location.getRoomId() != -1 ? new Card(Card.TYPE_ROOM, location.getRoomId()) : null);
        return isLocationCardInHandOrToggled(roomCard);
    }

    public boolean isLocationCardInHandOrToggled(Card card) {
        if (isCardInHand(card) || isCardToggled(card)) {
            return true;
        }
        return false;
    }

    public String toString() {
        String text = getPlayer().toString() + "'s Notebook :";
        for (Entry entry : entries.values()) {
            text += entry.toString();
        }
        return text;
    }

    public ArrayList<Card> canMakeAccusation() {
        ArrayList<Card> accusation = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < NUM_SUSPECTS; i++) {
            Card card = new Card(TYPE_SUSPECT, i);
            if (!isCardInHand(card) && !isCardToggled(card)) {
                count++;
            }
        }

        if (count == 1) {
            for (int i = 0; i < NUM_SUSPECTS; i++) {
                Card card = new Card(TYPE_SUSPECT, i);
                if (!isCardInHand(card) && !isCardToggled(card)) {
                    accusation.add(card);
                }
            }
        }

        count = 0;
        for (int i = 0; i < NUM_WEAPONS; i++) {
            Card card = new Card(TYPE_WEAPON, i);
            if (!isCardInHand(card) && !isCardToggled(card)) {
                count++;
            }
        }

        if (count == 1) {
            for (int i = 0; i < NUM_WEAPONS; i++) {
                Card card = new Card(TYPE_WEAPON, i);
                if (!isCardInHand(card) && !isCardToggled(card)) {
                    accusation.add(card);
                }
            }
        }

        count = 0;
        for (int i = 0; i < NUM_ROOMS; i++) {
            Card card = new Card(TYPE_ROOM, i);
            if (!isCardInHand(card) && !isCardToggled(card)) {
                count++;
            }
        }

        if (count == 1) {
            for (int i = 0; i < NUM_ROOMS; i++) {
                Card card = new Card(TYPE_ROOM, i);
                if (!isCardInHand(card) && !isCardToggled(card)) {
                    accusation.add(card);
                }
            }
        }

        if (accusation.size() != 3) {
            accusation = null;
        }

        return accusation;
    }

    public Card randomlyPickCardOfType(int type) {

        //select a card of indicated type and check the cards in your hand
        Card picked_card = null;
        ArrayList<Card> picks = new ArrayList<>();

        int total = 0;
        if (type == TYPE_SUSPECT) {
            total = NUM_SUSPECTS;
        }
        if (type == TYPE_ROOM) {
            total = NUM_ROOMS;
        }
        if (type == TYPE_WEAPON) {
            total = NUM_WEAPONS;
        }

        for (int i = 0; i < total; i++) {
            Card card = new Card(type, i);
            if (isCardInHand(card) || isCardToggled(card)) {
                continue;
            }
            picks.add(card);
        }

        if (picks.size() > 1) {
            int r = new Random().nextInt(picks.size());
            picked_card = picks.get(r);
        } else if (picks.size() == 1) {
            picked_card = picks.get(0);
        } else {
            //just return a random card of this type
            int r = new Random().nextInt(total);
            picked_card = new Card(type, r);
        }

        return picked_card;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    class Entry {

        Card value;
        boolean inHand = false;
        boolean toggled = false;

        Entry(Card value) {
            this.value = value;
        }

        boolean inHand() {
            return inHand;
        }

        void setInHand(boolean inHand) {
            this.inHand = inHand;
        }

        boolean getToggled() {
            return toggled;
        }

        void setToggled(boolean toggled) {
            this.toggled = toggled;
        }

        String getValue() {
            return value.toString();
        }

        public String toString() {
            return "[" + value.toString() + " inHand:" + inHand + " toggled:" + toggled + "] ";
        }

    }

}
