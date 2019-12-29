package gdx.clue;

import static org.antinori.game.Card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import gdx.clue.astar.Location;

public class Turn {

    public static final int ACTION_VALID_ACCUSATION = 200;
    public static final int ACTION_INVALID_ACCUSATION = 500;
    public static final int ACTION_MADE_SUGGESTION = 300;
    public static final int ACTION_TOOK_PASSAGE = 310;
    public static final int ACTION_ROLLED_DICE = 320;

    public boolean gameOver = false;
    
    private GameScreen screen;

    public Turn(GameScreen screen) {
        this.screen = screen;
    }

    public void mainLoop(List<Player> players) {

        //ClueMain.mapView.setEnabled(false); 
        // disable clicking on the map until
        // they roll the dice

        while (!gameOver) {

            for (Player player : players) {

                screen.setCurrentPlayer(player);

                if (player.isComputerPlayer()) {

                    Location new_location = clickOnMapComputerPlayer(player);
                    
                    makeSuggestionComputerPlayer(player, players);

                    if (canMakeAccusationComputerPlayer(player)) {
                        gameOver = true;
                        break;
                    }

                } else {
                    // dialog for showing roll dice button, take secret passage
                    // button or make suggestion button or make accusation

                    if (player.hasMadeFalseAccusation()) {
                        //player.setBystanderIndicator(true);
                        screen.addMessage("You made a false accusation and are bystanding to show cards.");
                        continue;
                    }

                    Location location = player.getLocation();
                    boolean isInRoom = location.getRoomId() != -1;
                    boolean showSecret = (location.getRoomId() == ROOM_LOUNGE || location.getRoomId() == ROOM_STUDY || location.getRoomId() == ROOM_CONSERVATORY || location.getRoomId() == ROOM_KITCHEN);

                    //TurnDialog2 dialog1 = new TurnDialog2(player, true, showSecret, isInRoom);
                    int action = 0;//dialog1.showDialog();

                    if (action == ACTION_ROLLED_DICE) {

                        // wait here until they click on the new location
                        Location new_location = null;
                        do {
                            try {
                                Thread.currentThread().sleep(1000);
                                new_location = player.getLocation();
                                // System.out.println("Waiting to click on map..");
                            } catch (Exception e) {
                            }
                        } while (new_location == location);

                        // see if they made it to a room and let them make a suggestion
                        isInRoom = new_location.getRoomId() != -1;
                        if (isInRoom) {
                            //TurnDialog2 dialog2 = new TurnDialog2(player, false, false, true);
                            //dialog2.showDialog();
                        }

                    } else if (action == ACTION_TOOK_PASSAGE) {

                        int current_room = location.getRoomId();
                        switch (current_room) {
                            case ROOM_LOUNGE:
                                screen.setPlayerLocationFromMapClick(screen.getMap().getRoomLocation(ROOM_CONSERVATORY));
                                break;
                            case ROOM_STUDY:
                                screen.setPlayerLocationFromMapClick(screen.getMap().getRoomLocation(ROOM_KITCHEN));
                                break;
                            case ROOM_CONSERVATORY:
                                screen.setPlayerLocationFromMapClick(screen.getMap().getRoomLocation(ROOM_LOUNGE));
                                break;
                            case ROOM_KITCHEN:
                                screen.setPlayerLocationFromMapClick(screen.getMap().getRoomLocation(ROOM_STUDY));
                                break;
                        }

                        //TurnDialog2 dialog2 = new TurnDialog2(player, false, false, true);
                        //dialog2.showDialog();

                    } else if (action == ACTION_MADE_SUGGESTION) {
                        // next player
                    }


                }

                waitEndTurnButton();

                if (gameOver) {
                    break;
                }

            } // for loop on players

        } // while not game over

    }
    
    private void waitEndTurnButton() {
        Sounds.play(Sound.LAUGH);
//        MultiplayerFrame.endTurnButton.setEnabled(true);
//        // wait until end turn is pressed
//        do {
//            try {
//                Thread.currentThread().sleep(1000);
//                MultiplayerFrame.endTurnButton.setBackground(Color.red);
//                Thread.currentThread().sleep(1000);
//                MultiplayerFrame.endTurnButton.setBackground(Color.white);
//            } catch (Exception e) {
//            }
//        } while (MultiplayerFrame.endTurnButton.isEnabled() && !gameOver);
    }

    private Location clickOnMapComputerPlayer(Player player) {

        Location new_location = null;

        // try move the player to the room which is not in their cards or
        // toggled
        Location location = player.getLocation();
        boolean isInRoom = location.getRoomId() != -1;

        // get all other rooms except the one they are in
        ArrayList<Location> rooms = screen.getMap().getAllRoomLocations(location.getRoomId());

        // remove the rooms which are toggled or in their hand
        for (Iterator<Location> it = rooms.iterator(); it.hasNext();) {
            Location l = (Location) it.next();
            Card room_card = new Card(TYPE_ROOM, l.getRoomId());
            if (player.getNotebook().isCardInHand(room_card) || player.getNotebook().isCardToggled(room_card)) {
                it.remove();
            }
        }

        int roll = screen.rollDice();

        do {
            if (isInRoom) {

                int rand = new Random().nextInt(10); // randomly decide
                if (ClueMain.difficult_setting) {
                    rand = 0;
                }

                // see if they want to take the secret passages
                if (location.getRoomId() == ROOM_KITCHEN) {
                    if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_STUDY)) && rand < 5) {
                        new_location = screen.getMap().getRoomLocation(ROOM_STUDY);
                        Sounds.play(Sound.CREAK);
                        break;
                    }
                }

                if (location.getRoomId() == ROOM_STUDY) {
                    if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_KITCHEN)) && rand < 5) {
                        new_location = screen.getMap().getRoomLocation(ROOM_KITCHEN);
                        Sounds.play(Sound.CREAK);
                        break;
                    }
                }

                if (location.getRoomId() == ROOM_CONSERVATORY) {
                    if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_LOUNGE)) && rand < 5) {
                        new_location = screen.getMap().getRoomLocation(ROOM_LOUNGE);
                        Sounds.play(Sound.CREAK);
                        break;
                    }
                }

                if (location.getRoomId() == ROOM_LOUNGE) {
                    if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_CONSERVATORY)) && rand < 5) {
                        new_location = screen.getMap().getRoomLocation(ROOM_CONSERVATORY);
                        Sounds.play(Sound.CREAK);
                        break;
                    }
                }

                // see if the room is in their hand or toggled
                if (!player.getNotebook().isLocationCardInHandOrToggled(location) && rand < 5) {
                    new_location = location;// just keep them in the same room
                    break;
                }

            }

            ArrayList<Location> choices = screen.getMap().highlightReachablePaths(location, screen.getPathfinder(), roll);

            // see if they can move to a highlighted room which is not in their
            // hand or toggled
            for (Location choice : choices) {
                if (rooms.contains(choice)) {
                    new_location = choice;
                    break;
                }
            }

            if (new_location == null) {
                int closest = 100;
                // find a room location which is closest to them which is not in
                // their hand or toggled
                for (Location choice : choices) {
                    for (Location room : rooms) {
                        List<Location> path = screen.getPathfinder().findPath(screen.getMap().getLocations(), choice, Collections.singleton(room));
                        if (path.size() < closest) {
                            closest = path.size();
                            new_location = choice;
                        }
                    }
                }
            }

        } while (false);
//		System.out.println(player.toLongString() + " rolled a " + roll);
//		System.out.println("clickOnMapComputerPlayer rooms size = " + rooms.size());
//		System.out.println("clickOnMapComputerPlayer location = " + new_location);
//		System.out.println("clickOnMapComputerPlayer " + player.getNotebook().toString());
        if (new_location == null) {
            new_location = location;// just keep them in the same room then
            screen.addMessage(player.toString() + " is staying in the same room.");
        } else {
            screen.setPlayerLocationFromMapClick(new_location);
            screen.addMessage(player.toString() + " rolled a " + roll + (new_location == location ? "and has stayed in the room." : " and has moved."));
        }

        return new_location;

    }

    private void makeSuggestionComputerPlayer(Player player, List<Player> players) {

        Location location = player.getLocation();
        if (location.getRoomId() == -1) {
            return;
        }

        Card selected_suspect_card = player.getNotebook().randomlyPickCardOfType(TYPE_SUSPECT);
        Card selected_weapon_card = player.getNotebook().randomlyPickCardOfType(TYPE_WEAPON);

        // the room they are in
        Card selected_room_card = new Card(TYPE_ROOM, location.getRoomId());

        ArrayList<Card> suggestion = new ArrayList<>();
        suggestion.add(selected_suspect_card);
        suggestion.add(selected_room_card);
        suggestion.add(selected_weapon_card);

        screen.getShowCards().setSuggestion(suggestion, player, screen.getYourPlayer(), players);
        screen.getShowCards().showCards();

    }

    private boolean canMakeAccusationComputerPlayer(Player player) {

        ArrayList<Card> accusation = player.getNotebook().canMakeAccusation();
        if (accusation == null) {
            return false;
        }

        String text = String.format(ClueMain.accusationFormatter, player.toString(), accusation.get(0).toString(), accusation.get(1).toString(), accusation.get(2).toString());

        //SoundEffect.GASP.play();

        screen.addMessage(text + "\n\nThe accusation is true.  Game over.");

        return true;
    }

}
