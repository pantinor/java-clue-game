package gdx.clue;

import com.badlogic.gdx.graphics.Color;
import static gdx.clue.Card.*;
import java.util.List;
import gdx.clue.astar.Location;

public class ShowCardsRoutine {

    List<Card> suggestion;
    List<Player> players;
    Player suggesting_player;
    Player your_player;
    String suggestion_text;
    
    GameScreen screen;
    
    public ShowCardsRoutine(GameScreen screen) {
        this.screen = screen;
    }

    public void setSuggestion(List<Card> suggestion, Player suggesting_player, Player your_player, List<Player> players) {
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
            if (player.getSuspect().id() == suspect.getValue()) {
                screen.getPlayerIconPlacement().removePlayerIcon(player.getSuspect().id());
                
                Location room_location = suggesting_player.getLocation();
                
                screen.getPlayerIconPlacement().addPlayerIcon(room_location.getRoomId(), player.getSuspect().id());

                player.setLocation(room_location);
            }
        }

        suggestion_text = String.format(ClueMain.formatter, suggesting_player.toString(), suspect.toString(), weapon.toString(), room.toString());


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
                    //PickCardsToShowDialog2 dialog = new PickCardsToShowDialog2(suggestion, suggestion_text, next_player);
                    //card_to_show = (Card) dialog.showDialog();
                }

            } else {

                List<Card> cards_in_hand = next_player.getCardsInHand();

                for (Card card : suggestion) {
                    if (cards_in_hand.contains(card)) {

                        String text = next_player.toString() + "\nis showing the\n" + card + " card.";
                        if (suggesting_player != your_player) {
                            text = next_player.toString() + "\nis showing a card\nto " + suggesting_player.toString();
                        }

                        text = text + "\n\n" + suggestion_text;

                        //ShowCardDialog2 dialog = new ShowCardDialog2(text);
                        //dialog.setVisible(true);

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

            //ShowCardDialog2 dialog = new ShowCardDialog2(next_player.toString() + "\ndoes not have\na card to show.");
            //dialog.setVisible(true);

        }

        if (card_to_show == null) {
            //ShowCardDialog2 dialog = new ShowCardDialog2("No one has a\ncard to show.");
            //dialog.setVisible(true);
        } else {
            //set the card as toggled in their notebook
            if (suggesting_player.isComputerPlayer()) {
                suggesting_player.getNotebook().setToggled(card_to_show);
            }
        }

    }

}
