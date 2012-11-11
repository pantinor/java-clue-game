package org.antinori.game;

import static org.antinori.game.Card.*;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.antinori.astar.Location;
import org.antinori.multiplayer.MultiplayerFrame;

public class Turn {

	public BufferedImage green_background = ClueMain.loadIcon("green-pattern-cropped.jpg");

	public static final int ACTION_VALID_ACCUSATION = 200;
	public static final int ACTION_INVALID_ACCUSATION = 500;
	public static final int ACTION_MADE_SUGGESTION = 300;
	public static final int ACTION_TOOK_PASSAGE = 310;
	public static final int ACTION_ROLLED_DICE = 320;
	
	public boolean gameOver = false;
	public boolean multiplayerGotShowCardResponse = false;

	public Turn() {

	}

	public void startGame(ArrayList<Player> players) {

		ClueMain.mapView.setEnabled(false); // disable clicking on the map until
											// they roll the dice

		while (!gameOver) {

			for (Player player : players) {

				ClueMain.setCurrentPlayer(player);

				MultiplayerFrame.endTurnButton.setEnabled(false);
				MultiplayerFrame.accuseButton.setEnabled(false);


				if (player.isComputerPlayer()) {

					Location new_location = clickOnMapComputerPlayer(player);
					// let them make a suggestion
					makeSuggestionComputerPlayer(player, players);

					if (canMakeAccusationComputerPlayer(player)) {
						gameOver = true;
						break;
					}
					

				} else {
					// dialog for showing roll dice button, take secret passage
					// button or make suggestion button or make accusation
					
					if (player.hasMadeFalseAccusation()) {
						ClueMain.notebookpanel.setBystanderIndicator(true);
						JOptionPane.showMessageDialog(ClueMain.frame, "You made a false accusation and are bystanding to show cards.", "Accusation", JOptionPane.PLAIN_MESSAGE);
						ClueMain.multiplayerFrame.endTurnButton.doClick();
						continue;
					}
					
					Location location = player.getLocation();
					boolean isInRoom = location.getRoomId() != -1;
					boolean showSecret = (location.getRoomId() == ROOM_LOUNGE || location.getRoomId() == ROOM_STUDY || location.getRoomId() == ROOM_CONSERVATORY || location.getRoomId() == ROOM_KITCHEN);

					TurnDialog2 dialog1 = new TurnDialog2(player, true, showSecret, isInRoom);
					int action = dialog1.showDialog();

					if (action == ACTION_ROLLED_DICE) {

						// wait here until they click on the new location
						ClueMain.mapView.setEnabled(true);// let them click on
															// the map
						Location new_location = null;
						do {
							try {
								Thread.currentThread().sleep(1000);
								new_location = player.getLocation();
								// System.out.println("Waiting to click on map..");
							} catch (Exception e) {
							}
						} while (new_location == location);
						ClueMain.mapView.setEnabled(false);// disable map clicks
															// again

						// see if they made it to a room and let them make a
						// suggestion
						isInRoom = new_location.getRoomId() != -1;
						if (isInRoom) {
							TurnDialog2 dialog2 = new TurnDialog2(player, false, false, true);
							dialog2.showDialog();
						}

					} else if (action == ACTION_TOOK_PASSAGE) {

						int current_room = location.getRoomId();
						switch (current_room) {
						case ROOM_LOUNGE:
							ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_CONSERVATORY));
							break;
						case ROOM_STUDY:
							ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_KITCHEN));
							break;
						case ROOM_CONSERVATORY:
							ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_LOUNGE));
							break;
						case ROOM_KITCHEN:
							ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_STUDY));
							break;
						}

						ClueMain.mapView.repaint();

						TurnDialog2 dialog2 = new TurnDialog2(player, false, false, true);
						dialog2.showDialog();


					} else if (action == ACTION_MADE_SUGGESTION) {
						// next player
					}
					
					MultiplayerFrame.accuseButton.setEnabled(true);

				}
				
				waitEndTurnButton();

				if (gameOver) break;

			} // for loop on players

		} // while not game over

	}
	
	public void waitEndTurnButton() {
		MultiplayerFrame.endTurnButton.setEnabled(true);
		// wait until end turn is pressed
		do {
			try {
				Thread.currentThread().sleep(1000);
				MultiplayerFrame.endTurnButton.setBackground(Color.red);
				Thread.currentThread().sleep(1000);
				MultiplayerFrame.endTurnButton.setBackground(Color.white);
			} catch (Exception e) {
			}
		} while (MultiplayerFrame.endTurnButton.isEnabled() && !gameOver);
	}
	
	public void waitShowCardResponse() {
		multiplayerGotShowCardResponse = false;
		do {
			try {
				System.out.println("waitShowCardResponse "+multiplayerGotShowCardResponse);
				Thread.currentThread().sleep(1000);
			} catch (Exception e) {
			}
		} while (!multiplayerGotShowCardResponse);
		multiplayerGotShowCardResponse = false;
	}

	public Location clickOnMapComputerPlayer(Player player) {

		Location new_location = null;

		// try move the player to the room which is not in their cards or
		// toggled
		Location location = player.getLocation();
		boolean isInRoom = location.getRoomId() != -1;

		// get all other rooms except the one they are in
		ArrayList<Location> rooms = ClueMain.map.getAllRoomLocations(location.getRoomId());

		// remove the rooms which are toggled or in their hand
		for (Iterator<Location> it = rooms.iterator(); it.hasNext();) {
			Location l = (Location) it.next();
			Card room_card = new Card(TYPE_ROOM, l.getRoomId());
			if (player.getNotebook().isCardInHand(room_card) || player.getNotebook().isCardToggled(room_card)) {
				it.remove();
			}
		}

		int roll = ClueMain.mapView.rollDice();

		do {
			if (isInRoom) {

				int rand = new Random().nextInt(10); // randomly decide
				if (ClueMain.difficult_setting) rand = 0;

				// see if they want to take the secret passages
				if (location.getRoomId() == ROOM_KITCHEN) {
					if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_STUDY)) && rand < 5) {
						new_location = ClueMain.map.getRoomLocation(ROOM_STUDY);
						SoundEffect.CREAK.play();
						break;
					}
				}

				if (location.getRoomId() == ROOM_STUDY) {
					if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_KITCHEN)) && rand < 5) {
						new_location = ClueMain.map.getRoomLocation(ROOM_KITCHEN);
						SoundEffect.CREAK.play();
						break;
					}
				}

				if (location.getRoomId() == ROOM_CONSERVATORY) {
					if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_LOUNGE)) && rand < 5) {
						new_location = ClueMain.map.getRoomLocation(ROOM_LOUNGE);
						SoundEffect.CREAK.play();
						break;
					}
				}

				if (location.getRoomId() == ROOM_LOUNGE) {
					if (!player.getNotebook().isLocationCardInHandOrToggled(new Card(TYPE_ROOM, ROOM_CONSERVATORY)) && rand < 5) {
						new_location = ClueMain.map.getRoomLocation(ROOM_CONSERVATORY);
						SoundEffect.CREAK.play();
						break;
					}
				}

				// see if the room is in their hand or toggled
				if (!player.getNotebook().isLocationCardInHandOrToggled(location) && rand < 5) {
					new_location = location;// just keep them in the same room
					break;
				}

			}

			ArrayList<Location> choices = ClueMain.map.highlightReachablePaths(location, ClueMain.pathfinder, roll);
			ClueMain.mapView.repaint();

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
						List<Location> path = ClueMain.pathfinder.findPath(ClueMain.map.getLocations(), choice, Collections.singleton(room));
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
			JOptionPane.showMessageDialog(ClueMain.frame, player.toString() + " is staying in the same room.", "", JOptionPane.PLAIN_MESSAGE);

		} else {

			ClueMain.setPlayerLocationFromMapClick(new_location);
			JOptionPane.showMessageDialog(ClueMain.frame, player.toString() + " rolled a " + roll + (new_location == location ? " has stayed in the room." : " and has moved."), "", JOptionPane.PLAIN_MESSAGE);
			ClueMain.mapView.repaint();
			ClueMain.map.resetHighlights();
		}

		return new_location;

	}

	public void makeSuggestionComputerPlayer(Player player, ArrayList<Player> players) {

		Location location = player.getLocation();
		if (location.getRoomId() == -1)
			return;

		Card selected_suspect_card = player.getNotebook().randomlyPickCardOfType(TYPE_SUSPECT);
		Card selected_weapon_card = player.getNotebook().randomlyPickCardOfType(TYPE_WEAPON);

		// the room they are in
		Card selected_room_card = new Card(TYPE_ROOM, location.getRoomId());

		ArrayList<Card> suggestion = new ArrayList<Card>();
		suggestion.add(selected_suspect_card);
		suggestion.add(selected_room_card);
		suggestion.add(selected_weapon_card);

		ClueMain.showcards.setSuggestion(suggestion, player, ClueMain.yourPlayer, players);
		ClueMain.showcards.showCards();

	}

	public boolean canMakeAccusationComputerPlayer(Player player) {
		
		ArrayList<Card> accusation = player.getNotebook().canMakeAccusation();
		if (accusation == null) return false;
		
		String text = String.format(ClueMain.accusationFormatter,player.toString(), accusation.get(0).toString(), accusation.get(1).toString(), accusation.get(2).toString());
		
		SoundEffect.GASP.play();
		
		JOptionPane.showMessageDialog(ClueMain.frame, text + "\n\nThe accusation is true.  Game over.", "Accusation", JOptionPane.PLAIN_MESSAGE);
		
		return true;
	}


	
	public void startTurnMultiplayerTurn(Player player) {

		ClueMain.mapView.setEnabled(false); // disable clicking on the map until
											// they roll the dice

		MultiplayerFrame.endTurnButton.setEnabled(false);
		MultiplayerFrame.accuseButton.setEnabled(false);
		
		if (player.hasMadeFalseAccusation()) {
			ClueMain.notebookpanel.setBystanderIndicator(true);
			ClueMain.multiplayerFrame.showTimedDialogAlert("You made a false accusation\nand are bystanding to show cards.");
			ClueMain.multiplayerFrame.endTurnButton.doClick();
			return;
		}


		Location location = player.getLocation();
		boolean isInRoom = location.getRoomId() != -1;
		boolean showSecret = (location.getRoomId() == ROOM_LOUNGE || location.getRoomId() == ROOM_STUDY || location.getRoomId() == ROOM_CONSERVATORY || location.getRoomId() == ROOM_KITCHEN);

		//JOptionPane.showMessageDialog(ClueMain.frame,"showSecret " + showSecret,"",JOptionPane.PLAIN_MESSAGE);
		
		TurnDialog2 dialog1 = new TurnDialog2(player, true, showSecret, isInRoom);
		int action = dialog1.showDialog();

		if (action == ACTION_ROLLED_DICE) {

			// wait here until they click on the new location
			ClueMain.mapView.setEnabled(true);// let them click on the map
			Location new_location = null;
			do {
				try {
					Thread.currentThread().sleep(1000);
					new_location = player.getLocation();
				} catch (Exception e) {
				}
			} while (new_location == location);
			ClueMain.mapView.setEnabled(false);// disable map clicks again
			
			//send the location to the server
			ClueMain.multiplayerFrame.sendMoveEvent(player, location.getX(), location.getY(), new_location.getX(), new_location.getY(), player.getPlayerColor(), false);

			// see if they made it to a room and let them make a suggestion
			isInRoom = new_location.getRoomId() != -1;
			if (isInRoom) {
				TurnDialog2 dialog2 = new TurnDialog2(player, false, false, true);
				dialog2.showDialog();
				waitShowCardResponse();
			}

		} else if (action == ACTION_TOOK_PASSAGE) {

			int current_room = location.getRoomId();
			switch (current_room) {
			case ROOM_LOUNGE:
				ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_CONSERVATORY));
				break;
			case ROOM_STUDY:
				ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_KITCHEN));
				break;
			case ROOM_CONSERVATORY:
				ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_LOUNGE));
				break;
			case ROOM_KITCHEN:
				ClueMain.setPlayerLocationFromMapClick(ClueMain.map.getRoomLocation(ROOM_STUDY));
				break;
			}

			ClueMain.mapView.repaint();
			
			ClueMain.multiplayerFrame.sendMoveEvent(player, location.getX(), location.getY(), player.getLocation().getX(), player.getLocation().getY(), player.getPlayerColor(), true);


			TurnDialog2 dialog2 = new TurnDialog2(player, false, false, true);
			dialog2.showDialog();
			waitShowCardResponse();

		} else if (action == ACTION_MADE_SUGGESTION) {
			
			waitShowCardResponse();

		}

		MultiplayerFrame.accuseButton.setEnabled(true);
		
		waitEndTurnButton();


	}
	
	
	
}
