package org.antinori.game;

import static org.antinori.game.Card.TYPE_ROOM;
import static org.antinori.game.Card.TYPE_SUSPECT;
import static org.antinori.game.Card.TYPE_WEAPON;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.antinori.astar.Location;

public class ShowCardsRoutine {

    ArrayList<Card> suggestion = null;
    ArrayList<Player> players = null;
    Player suggesting_player = null;
    Player your_player = null;
    String suggestion_text = null;

    BufferedImage green_background = ClueMain.loadIcon("green-pattern-cropped.jpg");

    public void setSuggestion(ArrayList<Card> suggestion, Player suggesting_player, Player your_player, ArrayList<Player> players) {
        this.suggestion = suggestion;
        this.players = players;
        this.suggesting_player = suggesting_player;
        this.your_player = your_player;

        Card room = null, suspect = null, weapon = null;
        for (Card card : suggestion) {
            if (card.getType() == TYPE_SUSPECT) {
                suspect = card;
            }
            if (card.getType() == TYPE_WEAPON) {
                weapon = card;
            }
            if (card.getType() == TYPE_ROOM) {
                room = card;
            }
        }

        //call the suspect over to the room and set their current location
        for (Player player : players) {
            if (player.getSuspectNumber() == suspect.getValue()) {

                ClueMain.playerIconPlacement.removePlayerIcon(player.getSuspectNumber());

                //reset original color back to gray
                ClueMain.map.setLocationColor(player.getLocation(), Color.gray);

                Location room_location = suggesting_player.getLocation();

                ClueMain.playerIconPlacement.addPlayerIcon(room_location.getRoomId(), player.getSuspectNumber());

                //set the players location 
                player.setLocation(room_location);
                //set location color to the players color
                room_location.setColor(player.getPlayerColor());

                ClueMain.multiplayerFrame.sendMoveEvent(player, player.getLocation().getX(), player.getLocation().getY(),
                        room_location.getX(), room_location.getY(), player.getPlayerColor(), false);

				//ClueMain.multiplayerFrame.showTimedDialogAlert(player.toString() + " has been called to the " + new Card(TYPE_ROOM,room_location.getRoomId()).toString());					
            }
        }

        suggestion_text = String.format(ClueMain.formatter, suggesting_player.toString(), suspect.toString(), weapon.toString(), room.toString());
        ClueMain.multiplayerFrame.sendSetSuggestionEvent(suspect, weapon, room, suggesting_player);

        ClueMain.mapView.repaint();

    }

    public void showCards() {

        if (players == null) {
            return;
        }
        if (suggesting_player == null) {
            return;
        }

        int suggestingPlayerIndex = players.indexOf(suggesting_player);
        Card card_to_show = null;

        //get the next player to the right and ask to show a card
        int index = suggestingPlayerIndex + 1;
        if (index == players.size()) {
            index = 0;
        }

        while (card_to_show == null && index < players.size()) {

            Player next_player = players.get(index);
            if (next_player == suggesting_player) {
                break;//done
            }

            if (next_player == your_player) {

                if (!next_player.isHoldingCardInSuggestion(suggestion)) {
                    //nothing
                } else {
                    PickCardsToShowDialog2 dialog = new PickCardsToShowDialog2(suggestion, suggestion_text, next_player);
                    card_to_show = (Card) dialog.showDialog();
                }

            } else {

                ArrayList<Card> cards_in_hand = next_player.getCardsInHand();

                for (Card card : suggestion) {
                    if (cards_in_hand.contains(card)) {

                        String text = next_player.toString() + "\nis showing the\n" + card + " card.";
                        if (suggesting_player != your_player) {
                            text = next_player.toString() + "\nis showing a card\nto " + suggesting_player.toString();
                        }

                        text = text + "\n\n" + suggestion_text;

                        ShowCardDialog2 dialog = new ShowCardDialog2(text);
                        dialog.setVisible(true);

                        card_to_show = card;
                        break;
                    }
                }

            }

            if (card_to_show != null) {
                break;
            }

            index++;
            if (index == players.size()) {
                index = 0;
            }

            ShowCardDialog2 dialog = new ShowCardDialog2(next_player.toString() + "\ndoes not have\na card to show.");
            dialog.setVisible(true);

        }

        if (card_to_show == null) {
            ShowCardDialog2 dialog = new ShowCardDialog2("No one has a\ncard to show.");
            dialog.setVisible(true);
        } else {
            //set the card as toggled in their notebook
            if (suggesting_player.isComputerPlayer()) {
                suggesting_player.getNotebook().setToggled(card_to_show);
            }
        }

    }

}
