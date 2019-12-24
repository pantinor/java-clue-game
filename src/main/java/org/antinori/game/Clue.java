package org.antinori.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import static org.antinori.game.Card.*;
import java.util.Random;

public class Clue {

    private final ArrayList<Player> players = new ArrayList<>(6);

    private final ArrayList<Card> shuffled = new ArrayList<>(TOTAL);
    private final ArrayList<Card> victimSet = new ArrayList<>(3);

    public Clue() {

    }

    public void createDeck() {

        ArrayList<Card> deck = new ArrayList<>(TOTAL);

        //create deck
        for (int i = 0; i < NUM_ROOMS; i++) {
            deck.add(new Card(TYPE_ROOM, i));
        }
        for (int i = 0; i < NUM_SUSPECTS; i++) {
            deck.add(new Card(TYPE_SUSPECT, i));
        }
        for (int i = 0; i < NUM_WEAPONS; i++) {
            deck.add(new Card(TYPE_WEAPON, i));
        }

        // shuffle it
        Random rand = new Random();
        for (int i = 0; i < TOTAL; i++) {
            int r = rand.nextInt(deck.size());
            Card c = deck.remove(r);
            shuffled.add(c);
        }

        //pull the victim set
        int w = rand.nextInt(NUM_WEAPONS);
        int r = rand.nextInt(NUM_ROOMS);
        int s = rand.nextInt(NUM_SUSPECTS);

        Card weapon = new Card(TYPE_WEAPON, w);
        Card suspect = new Card(TYPE_SUSPECT, s);
        Card room = new Card(TYPE_ROOM, r);

        shuffled.remove(weapon);
        shuffled.remove(suspect);
        shuffled.remove(room);

        victimSet.add(weapon);
        victimSet.add(suspect);
        victimSet.add(room);

    }

    public Player addPlayer(Card p, String name, Color color, boolean computer) {
        Player player = new Player(p, name, color, computer);
        players.add(player);
        return player;
    }

    public int getCurrentPlayerCount() {
        return players.size();
    }

    public boolean containsSuspect(Card card) {
        return players.contains(card);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int id) {
        Player player = null;
        for (Player p : players) {
            if (p.getSuspectNumber() == id) {
                player = p;
            }
        }
        return player;
    }

    public Player getPlayer(String name) {
        Player player = null;
        for (Player p : players) {
            if (p.getPlayerName().equals(name)) {
                player = p;
            }
        }
        return player;
    }

    public String dealShuffledDeck() throws Exception {

        if (shuffled == null) {
            throw new Exception("Shuffled Deck is null.");
        }

        if (players == null) {
            throw new Exception("Players is null.");
        }

        //deal the cards
        int player_index = 0;
        for (int i = 0; i < shuffled.size(); i++) {
            Card card = shuffled.get(i);
            if (player_index == players.size()) {
                player_index = 0;
            }
            Player player = players.get(player_index);

            player.addCard(card);

            player_index++;
        }

        String msg = "Cards have been dealt, and the players are:\n";
        for (int j = 0; j < players.size(); j++) {
            msg += players.get(j).toLongString() + "\n";
        }

        return msg;
    }

    public String getAdjacentPlayerName(String name) {
        String adjPlayerName = null;
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (p.getPlayerName().equals(name)) {
                int next = i + 1;
                if (next == players.size()) {
                    next = 0;
                }
                adjPlayerName = players.get(next).getPlayerName();
                break;
            }
        }
        return adjPlayerName;
    }

    public List<Card> getShuffledDeck() {
        return shuffled;
    }

    public boolean matchesVictimSet(ArrayList<Card> accusation) {
        Card weapon = null, suspect = null, room = null;
        for (Card card : accusation) {
            if (card.getType() == TYPE_WEAPON) {
                weapon = card;
            }
            if (card.getType() == TYPE_ROOM) {
                room = card;
            }
            if (card.getType() == TYPE_SUSPECT) {
                suspect = card;
            }
        }
        return matchesVictimSet(weapon, suspect, room);
    }

    public boolean matchesVictimSet(Card weapon, Card suspect, Card room) {
        return (victimSet.contains(weapon) && victimSet.contains(suspect) && victimSet.contains(room));
    }

    public boolean matchesVictimSet(int w, int s, int r) {
        Card suspect = new Card(TYPE_SUSPECT, s);
        Card weapon = new Card(TYPE_WEAPON, w);
        Card room = new Card(TYPE_ROOM, r);
        return (victimSet.contains(weapon) && victimSet.contains(suspect) && victimSet.contains(room));
    }

}
