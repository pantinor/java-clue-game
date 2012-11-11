package org.antinori.multiplayer;

import static org.antinori.game.Card.SUSPECT_GREEN;
import static org.antinori.game.Card.SUSPECT_MUSTARD;
import static org.antinori.game.Card.SUSPECT_PEACOCK;
import static org.antinori.game.Card.SUSPECT_PLUM;
import static org.antinori.game.Card.SUSPECT_SCARLET;
import static org.antinori.game.Card.SUSPECT_WHITE;
import static org.antinori.game.Card.TYPE_ROOM;
import static org.antinori.game.Card.TYPE_SUSPECT;
import static org.antinori.game.Card.TYPE_WEAPON;
import static org.antinori.game.Card.green;
import static org.antinori.game.Card.mustard;
import static org.antinori.game.Card.peacock;
import static org.antinori.game.Card.plum;
import static org.antinori.game.Card.scarlet;
import static org.antinori.game.Card.white;
import static org.antinori.game.Player.COLOR_GREEN;
import static org.antinori.game.Player.COLOR_MUSTARD;
import static org.antinori.game.Player.COLOR_PEACOCK;
import static org.antinori.game.Player.COLOR_PLUM;
import static org.antinori.game.Player.COLOR_SCARLET;
import static org.antinori.game.Player.COLOR_WHITE;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.antinori.astar.Location;
import org.antinori.game.AccusationDialog2;
import org.antinori.game.Card;
import org.antinori.game.Clue;
import org.antinori.game.ClueMain;
import org.antinori.game.PickCardsToShowDialog2;
import org.antinori.game.Player;
import org.antinori.game.SoundEffect;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.entities.match.BoolMatch;
import sfs2x.client.entities.match.MatchExpression;
import sfs2x.client.entities.match.RoomProperties;
import sfs2x.client.entities.variables.SFSUserVariable;
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.CreateRoomRequest;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.FindRoomsRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LeaveRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.LogoutRequest;
import sfs2x.client.requests.PrivateMessageRequest;
import sfs2x.client.requests.PublicMessageRequest;
import sfs2x.client.requests.RoomSettings;
import sfs2x.client.requests.SetUserVariablesRequest;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class MultiplayerFrame extends javax.swing.JPanel implements IEventListener {

	private SmartFox sfs;
	private RoomListModel roomListModel;
	private UserListModel userListModel;
	private StringBuilder chatHistory;
	
	String mySelectedCharacter = scarlet.toString();

	public MultiplayerFrame() {

		initComponents();

		// Creates new instance of SmatFoxClient and adds the event handlers
		sfs = new SmartFox();
		sfs.addEventListener(SFSEvent.CONFIG_LOAD_SUCCESS, this);
		sfs.addEventListener(SFSEvent.CONFIG_LOAD_FAILURE, this);
		sfs.addEventListener(SFSEvent.CONNECTION, this);
		sfs.addEventListener(SFSEvent.CONNECTION_LOST, this);
		sfs.addEventListener(SFSEvent.HANDSHAKE, this);

		sfs.addEventListener(SFSEvent.CONNECTION_RETRY, this);
		sfs.addEventListener(SFSEvent.CONNECTION_RESUME, this);

		sfs.addEventListener(SFSEvent.LOGIN, this);
		sfs.addEventListener(SFSEvent.LOGOUT, this);

		sfs.addEventListener(SFSEvent.ROOM_FIND_RESULT, this);
		sfs.addEventListener(SFSEvent.ROOM_ADD, this);
		sfs.addEventListener(SFSEvent.ROOM_CREATION_ERROR, this);
		sfs.addEventListener(SFSEvent.ROOM_REMOVE, this);
		sfs.addEventListener(SFSEvent.USER_COUNT_CHANGE, this);
		
		sfs.addEventListener(SFSEvent.ROOM_JOIN, this);
		sfs.addEventListener(SFSEvent.ROOM_JOIN_ERROR, this);
		
		sfs.addEventListener(SFSEvent.USER_ENTER_ROOM, this);
		sfs.addEventListener(SFSEvent.USER_EXIT_ROOM, this);
		
		sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE, this);
		sfs.addEventListener(SFSEvent.PRIVATE_MESSAGE, this);
		
		sfs.addEventListener(SFSEvent.OBJECT_MESSAGE, this);
		sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, this);


		// Sets the Room and User list models to the user and room lists
		this.roomListModel = (RoomListModel) listRooms.getModel();
		this.userListModel = (UserListModel) listUsers.getModel();

	}
	
	
	public boolean isConnected() {
		return sfs.isConnected();
	}


	public void dispatch(BaseEvent event) throws SFSException {

		if (event.getType().equals(SFSEvent.CONFIG_LOAD_SUCCESS)) {
						
		} else if (event.getType().equals(SFSEvent.CONFIG_LOAD_FAILURE)) {
						
		} else if (event.getType().equals(SFSEvent.CONNECTION)) {

			System.out.println(event.getArguments());

			if (event.getArguments().get("success") != null) {
				
				MatchExpression exp = new MatchExpression(RoomProperties.HAS_FREE_PLAYER_SLOTS, BoolMatch.EQUALS, true);
				sfs.send(new FindRoomsRequest(exp));
				
			} else {
				
				JOptionPane.showMessageDialog(ClueMain.frame,"Failed connecting to " + sfs.getConfig().getHost() + ":" + sfs.getConfig().getPort(), "", JOptionPane.ERROR_MESSAGE);

			}
			

		} else if (event.getType().equals(SFSEvent.CONNECTION_LOST)) {
			
			System.out.println(textUserName.getText()+" CONNECTION_LOST: " + event.getArguments());


			buttonJoin.setEnabled(false);
			buttonSendPrivate.setEnabled(false);
			buttonLogout.setEnabled(false);
			textChatMessage.setEnabled(false);
			buttonSend.setEnabled(false);
			
			LoginButton.setEnabled(true);

			toggleRadioButtons(false);

			roomListModel.removeRooms();
			userListModel.removeUsers();

			
		} else if (event.getType().equals(SFSEvent.CONNECTION_RETRY)) {
			System.out.println(textUserName.getText()+" CONNECTION_RETRY: " + event.getArguments());
			
		} else if (event.getType().equals(SFSEvent.CONNECTION_RESUME)) {
			System.out.println(textUserName.getText()+" CONNECTION_RESUME: " + event.getArguments());

		} else if (event.getType().equals(SFSEvent.LOGIN)) {
			System.out.println(textUserName.getText()+" LOGIN: " + event.getArguments());

			
		} else if (event.getType().equals(SFSEvent.HANDSHAKE)) {
			
			System.out.println(textUserName.getText()+" HANDSHAKE: " + event.getArguments());
			
			//login the user
			sfs.send(new LoginRequest(textUserName.getText(), "", "Clue"));
			LoginButton.setEnabled(false);

			chatHistory = new StringBuilder();
			textChatHistory.setText("");

			setMyCharacterUserVar();
			toggleRadioButtons(true);
			
			//get the list of rooms
			MatchExpression exp = new MatchExpression(RoomProperties.HAS_FREE_PLAYER_SLOTS, BoolMatch.EQUALS, true);
			sfs.send(new FindRoomsRequest(exp));
			
		} else if (event.getType().equals(SFSEvent.LOGOUT)) {
			
			System.out.println(textUserName.getText()+" LOGOUT: " + event.getArguments().toString());
			
			roomListModel.removeRooms();
			userListModel.removeUsers();
			
			LeaveRoomButton.setEnabled(false);

			sfs.disconnect();

			
		} else if (event.getType().equals(SFSEvent.OBJECT_MESSAGE)) {

			System.out.println(textUserName.getText()+" OBJECT_MESSAGE: " + event.getArguments().toString());
		
		} else if (event.getType().equals(SFSEvent.ROOM_FIND_RESULT)) {

			System.out.println("Rooms found: " + event.getArguments().get("rooms"));
			ArrayList<Room> rooms = (ArrayList) event.getArguments().get("rooms");
			for (Room room : rooms) {			
				roomListModel.addRoom(room);
			}

			// When new room is created it's added to the room list.
		} else if (event.getType().equals(SFSEvent.ROOM_ADD)) {

			System.out.println(textUserName.getText()+" ROOM_ADD: " + event.getArguments());

			Room room = (Room) event.getArguments().get("room");
			roomListModel.addRoom(room);

		}
		// If room creation failed an error message is shown
		else if (event.getType().equals(SFSEvent.ROOM_CREATION_ERROR)) {
			
			String error = "Room creation error: " + event.getArguments().get("errorMessage").toString();
			JOptionPane.showMessageDialog(ClueMain.frame,error, "", JOptionPane.ERROR_MESSAGE);
			
		}
		// When a room is deleted it's removed from the room list.
		else if (event.getType().equals(SFSEvent.ROOM_REMOVE)) {

			System.out.println(textUserName.getText()+" ROOM_REMOVE: " + event.getArguments());

			Room room = (Room) event.getArguments().get("room");
			roomListModel.removeRoom(room);
		}
		// When the user count change the room list is refreshed
		else if (event.getType().equals(SFSEvent.USER_COUNT_CHANGE)) {
			
			System.out.println(textUserName.getText()+" USER_COUNT_CHANGE: " + event.getArguments());

			Room room = (Room) event.getArguments().get("room");
			roomListModel.updateRoom(room);
			
		} else if (event.getType().equals(SFSEvent.ROOM_JOIN)) {
			//fired when your user joins a room
			
			System.out.println(textUserName.getText()+" ROOM_JOIN: " + event.getArguments());

			Room room = (Room) event.getArguments().get("room");
			
			roomListModel.updateRoom(room);
			
			listRooms.setSelectedValue(room, true);
			buttonJoin.setEnabled(false);
			textChatMessage.setEnabled(true);
			buttonSend.setEnabled(true);
			LeaveRoomButton.setEnabled(true);
			
			userListModel.setUserList(room.getUserList());
			
			chatHistory.append("<font color='#cc0000'>{ Room <b>");
			chatHistory.append(room.getName());
			chatHistory.append("</b> joined }</font><br>");
			textChatHistory.setText(chatHistory.toString());
			textChatMessage.requestFocus();
			
			scarletRadio.setEnabled(false);
			mustardRadio.setEnabled(false);
			greenRadio.setEnabled(false);
			whiteRadio.setEnabled(false);
			peacockRadio.setEnabled(false);
			plumRadio.setEnabled(false);
			
			//set the room name in the user so we can set the game over flag when they disconnect
			List<UserVariable> userVars = new ArrayList<UserVariable>(); 
			userVars.add(new SFSUserVariable("room", room.getName()));
			sfs.send(new SetUserVariablesRequest(userVars));		

			
		} else if (event.getType().equals(SFSEvent.EXTENSION_RESPONSE)) {
			System.out.println(textUserName.getText()+" EXTENSION_RESPONSE: " + event.getArguments());

			if (event.getArguments().get("cmd").equals("validateJoinRoom")) {
				
				SFSObject obj = (SFSObject)event.getArguments().get("params");
				System.out.println(obj.getUtfString("message"));

				if (!obj.getBool("validated")) 
					JOptionPane.showMessageDialog(ClueMain.frame,obj.getUtfString("message"), "", JOptionPane.ERROR_MESSAGE);
				
			} else if (event.getArguments().get("cmd").equals("gameOver")) {
				
				SFSObject obj = (SFSObject)event.getArguments().get("params");
				System.out.println(obj.getUtfString("message"));
				
				setChatText("GAME", obj.getUtfString("message"), false);

				//JOptionPane.showMessageDialog(ClueMain.frame,obj.getUtfString("message"), "", JOptionPane.WARNING_MESSAGE);
				
			} else if (event.getArguments().get("cmd").equals("setPlayers")) {
				
				ClueMain.clue = new Clue();
			    
				setYourMultiplayer();

				SFSObject obj = (SFSObject)event.getArguments().get("params");
				ISFSArray ids = obj.getSFSArray("ids");
				ISFSArray names = obj.getSFSArray("names");
				for (int i=0;i<ids.size();i++) {
					setOtherMultiplayer(ids.getInt(i), names.getUtfString(i));
				}
				
			} else if (event.getArguments().get("cmd").equals("dealtCard")) {
				
				SFSObject obj = (SFSObject)event.getArguments().get("params");
				Card card = (Card)obj.getClass("card");
				ClueMain.yourPlayer.addCard(card);
				System.out.println("Got card: "+card);
				
				dealButton.setEnabled(false);
				
			} else if (event.getArguments().get("cmd").equals("getSet")) {
				
				ClueMain.setUpMultiplayerGame();
				
				setChatText("GAME", "Your notebook has been set.", false);
				setChatText("GAME", "Player locations have been set.", false);
				
			} else if (event.getArguments().get("cmd").equals("startTurn")) {
				
				setChatText("GAME", "It's your turn now.", false);
								
				ClueMain.threadPoolExecutor.execute(new Runnable() {
					public void run() {
						ClueMain.setCurrentPlayer(ClueMain.yourPlayer);
						ClueMain.turn.startTurnMultiplayerTurn(ClueMain.yourPlayer);
					}
				});
				
				
			} else if (event.getArguments().get("cmd").equals("diceRoll")) {

				SFSObject obj = (SFSObject)event.getArguments().get("params");
				
				int roll1 = obj.getInt("roll1");
				int roll2 = obj.getInt("roll2");
				
				SoundEffect.DICE.play();
				
				ClueMain.mapView.rolledDiceImageLeft = ClueMain.mapView.dice_faces.get(roll1-1);
				ClueMain.mapView.rolledDiceImageRight = ClueMain.mapView.dice_faces.get(roll2-1);
				
				
			} else if (event.getArguments().get("cmd").equals("move")) {

				SFSObject obj = (SFSObject)event.getArguments().get("params");
				
				String name = obj.getUtfString("player");
				int id = obj.getInt("character");
				int fx = obj.getInt("from-x");
				int fy = obj.getInt("from-y");
				int tx = obj.getInt("to-x");
				int ty = obj.getInt("to-y");
				Color color = new Color(obj.getInt("playerColor"));
				
				boolean secret = obj.getBool("secretPassage");
				if (secret) SoundEffect.CREAK.play();
				
		    	ClueMain.setCurrentPlayer(ClueMain.clue.getPlayer(id));
				
				Location from = ClueMain.map.getLocation(fx, fy);
				Location to = ClueMain.map.getLocation(tx, ty);

				ClueMain.setPlayerLocationFromMapClick(ClueMain.currentTurnPlayer,color,from,to);
				ClueMain.mapView.repaint();
				
				//you have been called over in a suggestion
				if (ClueMain.clue.getPlayer(id) == ClueMain.yourPlayer) {
					int roomid = ClueMain.mapView.getRoomRoomNameAtLocation(tx,ty);
					showTimedDialogAlert("You have has been called to the " + (roomid!=-1?new Card(TYPE_ROOM,roomid).toString():""));
				}
				
				setChatText(name," has moved.", false);
				
			} else if (event.getArguments().get("cmd").equals("suggestion")) {
				
				SFSObject obj = (SFSObject)event.getArguments().get("params");
				
				String name = obj.getUtfString("suggesting_player_name");
				Card suggesting_suspect = new Card(TYPE_SUSPECT,obj.getInt("suggesting_player_suspectId"));
				Card suspect = new Card(TYPE_SUSPECT,obj.getInt("suspect"));
				Card weapon = new Card(TYPE_WEAPON,obj.getInt("weapon"));
				Card room = new Card(TYPE_ROOM,obj.getInt("room"));

				String suggestion_text = String.format(ClueMain.formatter, suggesting_suspect.toString(),suspect.toString(),weapon.toString(),room.toString());
				setChatText(name, suggestion_text, false);
				
			} else if (event.getArguments().get("cmd").equals("showCardRequest")) {
				
				SFSObject obj = (SFSObject)event.getArguments().get("params");
				
				String name = obj.getUtfString("suggesting_player_name");
				Card suggesting_suspect = new Card(TYPE_SUSPECT,obj.getInt("suggesting_player_suspectId"));
				Card suspect = new Card(TYPE_SUSPECT,obj.getInt("suspect"));
				Card weapon = new Card(TYPE_WEAPON,obj.getInt("weapon"));
				Card room = new Card(TYPE_ROOM,obj.getInt("room"));

				String suggestion_text = String.format(ClueMain.formatter, suggesting_suspect.toString(),suspect.toString(),weapon.toString(),room.toString());
				
				ArrayList<Card> cards = new ArrayList<Card>();
				cards.add(suspect);
				cards.add(weapon);
				cards.add(room);
				
				PickCardsToShowDialog2 dialog = new PickCardsToShowDialog2(cards,suggestion_text,ClueMain.yourPlayer);
				Card card_to_show = dialog.showDialog();
				
				sendShowCardResponse(card_to_show, ClueMain.yourPlayer, name);

			} else if (event.getArguments().get("cmd").equals("showCardResponse")) {
				
				SFSObject obj = (SFSObject)event.getArguments().get("params");

				int ct = obj.getInt("card_type");
				
				if (ct == -100) {
					ClueMain.turn.multiplayerGotShowCardResponse = true;
				} else {
					String showing_player_name = obj.getUtfString("showing_player_name");
					String player_to_show = obj.getUtfString("player_to_show");
					int cv = obj.getInt("card_value");
					String text = showing_player_name;
					
					if (ct==-1) text += " does not have a card to show.";
					else if (ct==-99) text += " showed a card to " + player_to_show;
					else text += " is showing " + new Card(ct,cv).toString();
					
					setChatText(showing_player_name, text, false);
					showTimedDialogAlert(text);
					
					if (ct >= 0) 
						ClueMain.turn.multiplayerGotShowCardResponse = true;

				}

			} else if (event.getArguments().get("cmd").equals("accusation")) {

				SFSObject obj = (SFSObject)event.getArguments().get("params");
				
				String name = obj.getUtfString("accusingPlayer");
				int s = obj.getInt("suspect");
				int w = obj.getInt("weapon");
				int r = obj.getInt("room");
				final boolean valid = obj.getBool("valid");
				
				Card suspect = new Card(TYPE_SUSPECT,s);
				Card weapon = new Card(TYPE_WEAPON,w);
				Card room = new Card(TYPE_ROOM,r);
				
				final String text = String.format(ClueMain.accusationFormatter , name, suspect.toString(), weapon.toString(), room.toString());
				
				setChatText("GAME", text, false);
				setChatText("GAME", "The accusation is "+valid+".", false);
				
				if (valid) {
					SoundEffect.GASP.play();
				} else {
					int rand = new Random().nextInt(10);
					if (rand>5) SoundEffect.LAUGH.play();
					else SoundEffect.GIGGLE.play();
					Player p = ClueMain.clue.getPlayer(name);
					if (p != null) p.setHasMadeFalseAccusation();
				}
				
				ClueMain.threadPoolExecutor.execute(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(ClueMain.frame, text + "\n\nThe accusation is "+valid+".", "Accusation", JOptionPane.PLAIN_MESSAGE);
					}
				});


			}
			
		} else if (event.getType().equals(SFSEvent.ROOM_JOIN_ERROR)) {
			System.out.println(textUserName.getText()+" ROOM_JOIN_ERROR: " + event.getArguments());

			JOptionPane.showMessageDialog(ClueMain.frame,""+event.getArguments().get("errorMessage"), "", JOptionPane.ERROR_MESSAGE);

		} else if (event.getType().equals(SFSEvent.USER_ENTER_ROOM)) {
			//fired when another user joins this room
			
			System.out.println(textUserName.getText()+" USER_ENTER_ROOM: " + event.getArguments());
			
			User user = (User) event.getArguments().get("user");
			userListModel.addUser(user);
			
			Room room = (Room) event.getArguments().get("room");

			if (room != null && room.isJoined() && room.getUserCount() > 2) {
				dealButton.setEnabled(true);
				setChatText("GAME", "You may start the game and deal the cards.", false);
			} else {
				dealButton.setEnabled(false);
				setChatText("GAME", "Waiting for more players to join.", false);
			}

		}
		// When a user leave the room the user list is updated
		else if (event.getType().equals(SFSEvent.USER_EXIT_ROOM)) {
			System.out.println(textUserName.getText()+" USER_EXIT_ROOM: " + event.getArguments());

			User user = (User) event.getArguments().get("user");
			userListModel.removeUser(user.getId());
		
		} else if (event.getType().equals(SFSEvent.USER_VARIABLES_UPDATE)) {
			System.out.println(textUserName.getText()+" USER_VARIABLES_UPDATE: " + event.getArguments());

			
		} else if (event.getType().equals(SFSEvent.ROOM_VARIABLES_UPDATE)) {
			System.out.println(textUserName.getText()+" ROOM_VARIABLES_UPDATE: " + event.getArguments());
		

		} else if (event.getType().equals(SFSEvent.PUBLIC_MESSAGE)) {
			
			User sender = (User) event.getArguments().get("sender");
			String msg = event.getArguments().get("message").toString();
			setChatText(sender.getName(), msg, false);

		} else if (event.getType().equals(SFSEvent.PRIVATE_MESSAGE)) {
			
			User sender = (User) event.getArguments().get("sender");
			String msg = event.getArguments().get("message").toString();
			setChatText(sender.getName(), msg, true);
		}
	}
	
	public void setChatText(String senderName, String msg, boolean pm) {
		msg = MessageProcessor.parseSmiles(msg);
		
		if (!pm) chatHistory.append("<b>[");
		else chatHistory.append("<b><font color='#550000'>[PM - ");

		chatHistory.append(senderName);
		chatHistory.append("]:</b> ");
		chatHistory.append(msg);
		chatHistory.append("<br>");
		textChatHistory.setText(chatHistory.toString());
		try {
			Document document = textChatHistory.getDocument();
			textChatHistory.setCaretPosition(document.getLength());
		} catch (Exception e) {
		}
	}
	
	public void showTimedDialogAlert(final String text) {
		ClueMain.threadPoolExecutor.execute(new Runnable() {
			public void run() {
				alertTextArea.setText(text);
				dialogAlert.start();
				dialogAlert.setVisible(true);
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(260,800);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		dialogNewRoom = new javax.swing.JDialog();
		labelRoomName = new javax.swing.JLabel();
		textNewRoomName = new javax.swing.JTextField();
		labelMaxUsers = new javax.swing.JLabel();
		labelPassword = new javax.swing.JLabel();
		textNewRoomPassword = new javax.swing.JPasswordField();
		buttonCreateRoomOk = new javax.swing.JButton();
		buttonCreateRoomCancel = new javax.swing.JButton();
		sliderNewRoomMaxUsers = new javax.swing.JSlider();
		dialogPrivate = new javax.swing.JDialog();
		labelPrivateMessage = new javax.swing.JLabel();
		textPrivateMessage = new javax.swing.JTextField();
		buttonSendPrivateOk = new javax.swing.JButton();
		buttonSendPrivateCancel = new javax.swing.JButton();
		dialogJoinPrivateRoom = new javax.swing.JDialog();
		labelPrivateRoomPassword = new javax.swing.JLabel();
		textJoinPrivatePassword = new javax.swing.JPasswordField();
		buttonJoinPrivateCancel = new javax.swing.JButton();
		buttonJoinPrivateOk = new javax.swing.JButton();
		dialogLogin = new javax.swing.JDialog();
		labelUserName = new javax.swing.JLabel();
		textUserName = new javax.swing.JTextField();
        serverIPTextField = new javax.swing.JTextField();
        ipLabel = new javax.swing.JLabel();

		buttonLogin = new javax.swing.JButton();
		dialogAlert = new TimedDialogAlert();
        alertTextArea = new javax.swing.JTextArea();
		buttonAlertOk = new javax.swing.JButton();
		labelChatHistory = new javax.swing.JLabel();
		scrollPaneChatHistory = new javax.swing.JScrollPane();
		textChatHistory = new javax.swing.JTextPane();
		textChatMessage = new javax.swing.JTextField();
		buttonSend = new javax.swing.JButton();
		scrollPaneRoomList = new javax.swing.JScrollPane();
		listRooms = new javax.swing.JList();
		labelRoomList = new javax.swing.JLabel();
		buttonJoin = new javax.swing.JButton();
		buttonNewRoom = new javax.swing.JButton();
		scrollPaneUserList = new javax.swing.JScrollPane();
		listUsers = new javax.swing.JList();
		buttonSendPrivate = new javax.swing.JButton();
		labelUserList = new javax.swing.JLabel();
		buttonLogout = new javax.swing.JButton();
        LeaveRoomButton = new javax.swing.JButton();
		LeaveRoomButton.setEnabled(false);
		
        PlayerIconLabel = new javax.swing.JLabel();
        PlayerDescriptionArea = new javax.swing.JTextArea();

        playerSelectGroup = new javax.swing.ButtonGroup();

        mustardRadio = new javax.swing.JRadioButton();
        plumRadio = new javax.swing.JRadioButton();
        peacockRadio = new javax.swing.JRadioButton();
        whiteRadio = new javax.swing.JRadioButton();
        greenRadio = new javax.swing.JRadioButton();
        scarletRadio = new javax.swing.JRadioButton();
        
		PlayerIconLabel.setIcon(PlayerIcon.SCARLET.get());
		PlayerDescriptionArea.setText(Card.descriptions[SUSPECT_SCARLET]);
        PlayerDescriptionArea.setColumns(20);
        PlayerDescriptionArea.setRows(5);
        PlayerDescriptionArea.setEditable(false);
        PlayerDescriptionArea.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        PlayerDescriptionArea.setLineWrap(true);
        
        jLabel1 = new javax.swing.JLabel();
        
        dealButton = new javax.swing.JButton();
        singlePlayerButton = new javax.swing.JButton();
        LoginButton = new javax.swing.JButton();
        endTurnButton = new javax.swing.JButton();
        endTurnButton.setEnabled(false);
        accuseButton = new javax.swing.JButton();
        accuseButton.setEnabled(false);
        exitGameButton = new javax.swing.JButton();
        
		dialogNewRoom.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		dialogNewRoom.setTitle("New Game Room");
		dialogNewRoom.setModal(true);
		dialogNewRoom.setName(null);
		dialogNewRoom.setResizable(false);

		labelRoomName.setText("Room name:");

		labelMaxUsers.setText("Max. users:");

		labelPassword.setText("Password (optional):");

		buttonCreateRoomOk.setText("Create");
		buttonCreateRoomOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonCreateRoomOkActionPerformed(evt);
			}
		});

		buttonCreateRoomCancel.setText("Cancel");
		buttonCreateRoomCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonCreateRoomCancelActionPerformed(evt);
			}
		});
		
		dealButton.setEnabled(false);
        dealButton.setText("Start Game");
        dealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dealButtonActionPerformed(evt);
            }
        });

		sliderNewRoomMaxUsers.setMajorTickSpacing(10);
		sliderNewRoomMaxUsers.setMaximum(50);
		sliderNewRoomMaxUsers.setMinimum(10);
		sliderNewRoomMaxUsers.setMinorTickSpacing(10);
		sliderNewRoomMaxUsers.setPaintLabels(true);
		sliderNewRoomMaxUsers.setPaintTicks(true);
		sliderNewRoomMaxUsers.setSnapToTicks(true);

		javax.swing.GroupLayout dialogNewRoomLayout = new javax.swing.GroupLayout(dialogNewRoom.getContentPane());
		dialogNewRoom.getContentPane().setLayout(dialogNewRoomLayout);
		dialogNewRoomLayout.setHorizontalGroup(dialogNewRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				dialogNewRoomLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								dialogNewRoomLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(
												dialogNewRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(textNewRoomName, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
														.addComponent(labelRoomName).addComponent(labelMaxUsers).addComponent(sliderNewRoomMaxUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(labelPassword).addComponent(textNewRoomPassword))
										.addGroup(dialogNewRoomLayout.createSequentialGroup().addComponent(buttonCreateRoomOk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(buttonCreateRoomCancel)))
						.addContainerGap(26, Short.MAX_VALUE)));
		dialogNewRoomLayout.setVerticalGroup(dialogNewRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				dialogNewRoomLayout.createSequentialGroup().addContainerGap().addComponent(labelRoomName).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(textNewRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(labelMaxUsers)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(sliderNewRoomMaxUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(labelPassword).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(textNewRoomPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)
						.addGroup(dialogNewRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(buttonCreateRoomOk).addComponent(buttonCreateRoomCancel))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		dialogPrivate.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		dialogPrivate.setTitle("Send Private Message");
		dialogPrivate.setModal(true);
		dialogPrivate.setName(null);
		dialogPrivate.setResizable(false);

		labelPrivateMessage.setText("Message:");

		buttonSendPrivateOk.setText("Send");
		buttonSendPrivateOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSendPrivateOkActionPerformed(evt);
			}
		});

		buttonSendPrivateCancel.setText("Cancel");
		buttonSendPrivateCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSendPrivateCancelActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout dialogPrivateLayout = new javax.swing.GroupLayout(dialogPrivate.getContentPane());
		dialogPrivate.getContentPane().setLayout(dialogPrivateLayout);
		dialogPrivateLayout.setHorizontalGroup(dialogPrivateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				dialogPrivateLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								dialogPrivateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(textPrivateMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE).addComponent(labelPrivateMessage)
										.addGroup(dialogPrivateLayout.createSequentialGroup().addComponent(buttonSendPrivateOk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(buttonSendPrivateCancel)))
						.addContainerGap()));
		dialogPrivateLayout.setVerticalGroup(dialogPrivateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				dialogPrivateLayout.createSequentialGroup().addContainerGap().addComponent(labelPrivateMessage).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(textPrivateMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)
						.addGroup(dialogPrivateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(buttonSendPrivateOk).addComponent(buttonSendPrivateCancel))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		dialogJoinPrivateRoom.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		dialogJoinPrivateRoom.setTitle("Join Private Room");
		dialogJoinPrivateRoom.setModal(true);
		dialogJoinPrivateRoom.setName(null);
		dialogJoinPrivateRoom.setResizable(false);

		labelPrivateRoomPassword.setText("Password:");

		buttonJoinPrivateCancel.setText("Cancel");
		buttonJoinPrivateCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonJoinPrivateCancelActionPerformed(evt);
			}
		});

		buttonJoinPrivateOk.setText("Join Game Room");
		buttonJoinPrivateOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonJoinPrivateOkActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout dialogJoinPrivateRoomLayout = new javax.swing.GroupLayout(dialogJoinPrivateRoom.getContentPane());
		dialogJoinPrivateRoom.getContentPane().setLayout(dialogJoinPrivateRoomLayout);
		dialogJoinPrivateRoomLayout.setHorizontalGroup(dialogJoinPrivateRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				dialogJoinPrivateRoomLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								dialogJoinPrivateRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(textJoinPrivatePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(labelPrivateRoomPassword)
										.addGroup(dialogJoinPrivateRoomLayout.createSequentialGroup().addComponent(buttonJoinPrivateOk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(buttonJoinPrivateCancel)))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		dialogJoinPrivateRoomLayout.setVerticalGroup(dialogJoinPrivateRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				dialogJoinPrivateRoomLayout.createSequentialGroup().addContainerGap().addComponent(labelPrivateRoomPassword).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(textJoinPrivatePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)
						.addGroup(dialogJoinPrivateRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(buttonJoinPrivateOk).addComponent(buttonJoinPrivateCancel))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));


		
        dialogLogin.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogLogin.setTitle("Login");
        dialogLogin.setModal(true);
        dialogLogin.setName(null);
        dialogLogin.setResizable(false);
		dialogLogin.setLocationRelativeTo(null);

        labelUserName.setText("Type your player name:");

        buttonLogin.setText("Login");
        buttonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoginActionPerformed(evt);
            }
        });

        serverIPTextField.setText("127.0.0.1");
        ipLabel.setText("Enter the game server IP or hostname:");
        
		textUserName.addKeyListener(new java.awt.event.KeyListener() {
			public void keyTyped(java.awt.event.KeyEvent evt) {}
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (KeyEvent.VK_ENTER == evt.getKeyCode()) {buttonLogin.doClick();}
			}
			public void keyReleased(java.awt.event.KeyEvent evt) {}
		});

        javax.swing.GroupLayout dialogLoginLayout = new javax.swing.GroupLayout(dialogLogin.getContentPane());
        dialogLogin.getContentPane().setLayout(dialogLoginLayout);
        dialogLoginLayout.setHorizontalGroup(
            dialogLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(textUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelUserName)
                        .addComponent(buttonLogin)
                        .addComponent(serverIPTextField))
                    .addComponent(ipLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dialogLoginLayout.setVerticalGroup(
            dialogLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelUserName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ipLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serverIPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonLogin)
                .addGap(30, 30, 30))
        );
		dialogLogin.pack();

		
		
		




				
        dialogAlert.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogAlert.setModal(true);
        dialogAlert.setResizable(false);
		ClueMain.setLocationInCenter(dialogAlert,-200,250);
        dialogAlert.setUndecorated(true);

        alertTextArea.setColumns(20);
        alertTextArea.setEditable(false);
        alertTextArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        alertTextArea.setLineWrap(true);
        alertTextArea.setRows(5);

        buttonAlertOk.setText("Ok");
        buttonAlertOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAlertOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogAlertLayout = new javax.swing.GroupLayout(dialogAlert.getContentPane());
        dialogAlert.getContentPane().setLayout(dialogAlertLayout);
        dialogAlertLayout.setHorizontalGroup(
            dialogAlertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogAlertLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alertTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonAlertOk, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addContainerGap())
        );
        dialogAlertLayout.setVerticalGroup(
            dialogAlertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogAlertLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogAlertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogAlertLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonAlertOk))
                    .addGroup(dialogAlertLayout.createSequentialGroup()
                        .addComponent(alertTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 14, Short.MAX_VALUE)))
                .addContainerGap())
        );
		dialogAlert.pack();

		
		
		setMinimumSize(new java.awt.Dimension(250, 650));
		setName("frmMain"); // NOI18N

		labelChatHistory.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		labelChatHistory.setText("Chat History");
		labelChatHistory.setName("labelChatHistory"); // NOI18N

		textChatHistory.setContentType("text/html");
		textChatHistory.setEditable(false); //uneditable
		textChatHistory.setHighlighter(null); //unselectable
		textChatHistory.setName("textChatHistory"); // NOI18N
		textChatHistory.setEditorKit(configureHtmlEditorKit(textChatHistory));
		scrollPaneChatHistory.setViewportView(textChatHistory);

		textChatMessage.setEnabled(false);
		textChatMessage.setName("textChatMessage"); // NOI18N
		textChatMessage.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				textChatMessageKeyPressed(evt);
			}
		});

		buttonSend.setText("Send");
		buttonSend.setEnabled(false);
		buttonSend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSendActionPerformed(evt);
			}
		});

		listRooms.setModel(new RoomListModel());
		listRooms.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		listRooms.setCellRenderer(new RoomListCellRenderer());
		listRooms.setName("listRooms"); // NOI18N
		listRooms.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				listRoomsValueChanged(evt);
			}
		});
		scrollPaneRoomList.setViewportView(listRooms);

		labelRoomList.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		labelRoomList.setText("Game Room List");

		buttonJoin.setText("Join");
		buttonJoin.setEnabled(false);
		buttonJoin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonJoinActionPerformed(evt);
			}
		});

		buttonNewRoom.setText("New Game Room");
		buttonNewRoom.setEnabled(false);
		buttonNewRoom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonNewRoomActionPerformed(evt);
			}
		});

		listUsers.setModel(new UserListModel());
		listUsers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		listUsers.setCellRenderer(new UserListCellRenderer());
		listUsers.setVisibleRowCount(20);
		listUsers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				listUsersValueChanged(evt);
			}
		});
		scrollPaneUserList.setViewportView(listUsers);

		buttonSendPrivate.setText("Send Private Message");
		buttonSendPrivate.setEnabled(false);
		buttonSendPrivate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSendPrivateActionPerformed(evt);
			}
		});

		labelUserList.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		labelUserList.setText("Player List");

		buttonLogout.setText("Logout");
		buttonLogout.setEnabled(false);
		buttonLogout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonLogoutActionPerformed(evt);
			}
		});

		
        
        LeaveRoomButton.setText("Exit Game Room");
        LeaveRoomButton.setActionCommand("leave");
        LeaveRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LeaveRoomButtonActionPerformed(evt);
            }
        });

        playerSelectGroup.add(mustardRadio);
        mustardRadio.setText("Colonel Mustard");
        mustardRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mustardRadioItemStateChanged(evt);
            }
        });

        playerSelectGroup.add(plumRadio);
        plumRadio.setText("Professor Plum");
        plumRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                plumRadioItemStateChanged(evt);
            }
        });

        playerSelectGroup.add(peacockRadio);
        peacockRadio.setText("Mrs. Peacock");
        peacockRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                peacockRadioItemStateChanged(evt);
            }
        });

        playerSelectGroup.add(whiteRadio);
        whiteRadio.setText("Mrs. White");
        whiteRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                whiteRadioItemStateChanged(evt);
            }
        });

        playerSelectGroup.add(greenRadio);
        greenRadio.setText("Mr. Green");
        greenRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                greenRadioItemStateChanged(evt);
            }
        });

        playerSelectGroup.add(scarletRadio);
        scarletRadio.setSelected(true);
        scarletRadio.setText("Miss Scarlet");
        scarletRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scarletRadioItemStateChanged(evt);
            }
        });

        jLabel1.setText("Select character before joining the game room");
                
        singlePlayerButton.setText("Start Single Player");
        singlePlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singlePlayerButtonActionPerformed(evt);
            }
        });

        LoginButton.setText("Login Multiplayer");
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        endTurnButton.setText("End Turn");
        endTurnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endTurnButtonActionPerformed(evt);
            }
        });

        accuseButton.setText("Make Accusation");
        accuseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accuseButtonActionPerformed(evt);
            }
        });

        exitGameButton.setText("Quit Game");
        exitGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitGameButtonActionPerformed(evt);
            }
        });

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		setLayout(layout);
		
		layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                            .addComponent(LoginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(singlePlayerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                            .addComponent(accuseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(endTurnButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
	                    .addGroup(layout.createSequentialGroup()
	                        .addComponent(labelChatHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(20, 20, 20)
	                        .addComponent(LeaveRoomButton))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                            .addComponent(scarletRadio)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                                    .addComponent(whiteRadio)
	                                    .addComponent(greenRadio, javax.swing.GroupLayout.Alignment.LEADING))
	                                .addGap(4, 4, 4)))
	                        .addGap(18, 18, 18)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(mustardRadio)
	                            .addComponent(plumRadio)
	                            .addComponent(peacockRadio)))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                            .addComponent(labelUserList)
	                            .addComponent(scrollPaneUserList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
	                            .addComponent(dealButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGap(10, 10, 10)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                            .addComponent(labelRoomList)
	                            .addComponent(buttonJoin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(scrollPaneRoomList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
	                            .addComponent(buttonNewRoom)))
	                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
	                            .addComponent(buttonLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                            .addComponent(exitGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                        .addComponent(scrollPaneChatHistory, javax.swing.GroupLayout.Alignment.LEADING)
	                        .addComponent(textChatMessage, javax.swing.GroupLayout.Alignment.LEADING)
	                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
	                            .addComponent(buttonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                            .addComponent(buttonSendPrivate)))
	                    .addGroup(layout.createSequentialGroup()
	                        .addComponent(PlayerIconLabel)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(PlayerDescriptionArea, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(singlePlayerButton)
	                    .addComponent(endTurnButton))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(LoginButton)
	                    .addComponent(accuseButton))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(PlayerIconLabel)
	                    .addComponent(PlayerDescriptionArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(scarletRadio)
	                    .addComponent(mustardRadio))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(plumRadio)
	                    .addComponent(greenRadio))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(peacockRadio)
	                    .addComponent(whiteRadio))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(labelUserList)
	                    .addComponent(labelRoomList))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                    .addComponent(scrollPaneRoomList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
	                    .addComponent(scrollPaneUserList, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addComponent(buttonJoin)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(buttonNewRoom))
	                    .addComponent(dealButton))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(LeaveRoomButton)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(labelChatHistory)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(scrollPaneChatHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(textChatMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(buttonSend)
	                    .addComponent(buttonSendPrivate))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(buttonLogout)
	                    .addComponent(exitGameButton))
	                .addGap(48, 48, 48))
	        );

	}// </editor-fold>//GEN-END:initComponents

	/**
	 * When the user selects new room enables "Join Room" button if the room is
	 * not the current, otherwise disables it.
	 */
	private void listRoomsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_listRoomsValueChanged
		Room room = (Room) listRooms.getSelectedValue();
		if (room != null) {
			buttonJoin.setEnabled(true);
		}

	}// GEN-LAST:event_listRoomsValueChanged

	/**
	 * Joins the user to the selected room. If the room is private a dilog box
	 * that asks for the password is shown.
	 */
	private void buttonJoinActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonJoinActionPerformed
		
		Room room = (Room) listRooms.getSelectedValue();
		
		//send validate if the room can be joined with the selected character here
		if (room != null) {
			ISFSObject obj = new SFSObject();
			obj.putUtfString("character", mySelectedCharacter);
			obj.putUtfString("room", room.getName());
			sfs.send(new ExtensionRequest("validateJoinRoom",obj,null));
		}

	}// GEN-LAST:event_buttonJoinActionPerformed

	/**
	 * Sends public message to the server.
	 */
	private void buttonSendActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonSendActionPerformed
		if (textChatMessage.getText().length() > 0) {
			sfs.send(new PublicMessageRequest(textChatMessage.getText()));
			textChatMessage.setText("");
			textChatMessage.requestFocus();
		}
	}// GEN-LAST:event_buttonSendActionPerformed

	/**
	 * When the user slects new user enables "Send Private Message" button if
	 * the user is not the current user, otherwise disables it.
	 */
	private void listUsersValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_listUsersValueChanged
		User user = (User) listUsers.getSelectedValue();
		if (user != null && user != sfs.getMySelf()) {
			buttonSendPrivate.setEnabled(true);
		} else {
			buttonSendPrivate.setEnabled(false);
		}
	}// GEN-LAST:event_listUsersValueChanged

	/**
	 * Displays a dialog that allows new room creation.
	 */
	private void buttonNewRoomActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonNewRoomActionPerformed
		dialogNewRoom.pack();
		dialogNewRoom.setLocationRelativeTo(null);
		textNewRoomName.setText("");
		textNewRoomPassword.setText("");
		sliderNewRoomMaxUsers.setValue(50);
		dialogNewRoom.setVisible(true);
	}// GEN-LAST:event_buttonNewRoomActionPerformed

	/**
	 * Shows dialog that asks for the private message to be send.
	 */
	private void buttonSendPrivateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonSendPrivateActionPerformed
		dialogPrivate.pack();
		dialogPrivate.setLocationRelativeTo(null);
		textChatMessage.setText("");
		dialogPrivate.setVisible(true);
	}// GEN-LAST:event_buttonSendPrivateActionPerformed

	/**
	 * Sends private message to the server.
	 */
	private void buttonSendPrivateOkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonSendPrivateOkActionPerformed
		String msg = textPrivateMessage.getText();
		if (msg.length() > 0) {
			dialogPrivate.dispose();
			User recipient = (User) listUsers.getSelectedValue();
			if (recipient != null) {
				sfs.send(new PrivateMessageRequest(msg, recipient.getId()));

			}
		}
	}// GEN-LAST:event_buttonSendPrivateOkActionPerformed

	/**
	 * Closes the private message dialog box.
	 */
	private void buttonSendPrivateCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonSendPrivateCancelActionPerformed
		dialogPrivate.dispose();
	}// GEN-LAST:event_buttonSendPrivateCancelActionPerformed

	/**
	 * Closes the new room dialog box.
	 */
	private void buttonCreateRoomCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonCreateRoomCancelActionPerformed
		dialogNewRoom.dispose();
	}// GEN-LAST:event_buttonCreateRoomCancelActionPerformed

	/**
	 * Creates new room
	 */
	private void buttonCreateRoomOkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonCreateRoomOkActionPerformed
		String roomName = textNewRoomName.getText();
		if (roomName.length() > 0) {
			dialogNewRoom.dispose();
			// Gets the new room properties for the user input
			// and sends them to the server.
			int maxUsers = sliderNewRoomMaxUsers.getValue();
			String password = new String(textNewRoomPassword.getPassword());

			// Create a new chat Room Room
			RoomSettings settings = new RoomSettings(roomName);
			settings.setMaxUsers(maxUsers);
			settings.setGroupId("chats");
			settings.setPassword(password);
			settings.setGame(true);

			sfs.send(new CreateRoomRequest(settings));
		}
	}// GEN-LAST:event_buttonCreateRoomOkActionPerformed

	/**
	 * Closes the dialog box that allows the user to join to private room
	 */
	private void buttonJoinPrivateCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonJoinPrivateCancelActionPerformed
		dialogJoinPrivateRoom.dispose();
	}// GEN-LAST:event_buttonJoinPrivateCancelActionPerformed

	/**
	 * Joins the user to private room
	 */
	private void buttonJoinPrivateOkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonJoinPrivateOkActionPerformed
		if (textJoinPrivatePassword.getPassword().length > 0) {
			dialogJoinPrivateRoom.dispose();
			

			// Gets the room password
			String password = new String(textJoinPrivatePassword.getPassword());
			// Joins the user to the currently selected room
			Room room = (Room) listRooms.getSelectedValue();
			
			sfs.send(new JoinRoomRequest(room.getId(), password));

		}
	}// GEN-LAST:event_buttonJoinPrivateOkActionPerformed

	/**
	 * Logs the user in.
	 */
	private void buttonLoginActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonLoginActionPerformed
		if (textUserName.getText().length() > 0) {
			dialogLogin.dispose();
		}
	}// GEN-LAST:event_buttonLoginActionPerformed

	/**
	 * Closes the dialog box with the error messages.
	 */
	private void buttonAlertOkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonAlertOkActionPerformed
		dialogAlert.dispose();
	}// GEN-LAST:event_buttonAlertOkActionPerformed

	/**
	 * Sends a public message to the server.
	 */
	private void textChatMessageKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_textChatMessageKeyPressed
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			buttonSendActionPerformed(null);
		}
	}// GEN-LAST:event_textChatMessageKeyPressed


	
	
	private void plumRadioItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_plumRadioItemStateChanged
		mySelectedCharacter = plum.toString();
		PlayerIconLabel.setIcon(PlayerIcon.PLUM.get());
		PlayerDescriptionArea.setText(Card.descriptions[SUSPECT_PLUM]);
		setMyCharacterUserVar();		

	}// GEN-LAST:event_plumRadioItemStateChanged

	private void peacockRadioItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_peacockRadioItemStateChanged
		mySelectedCharacter = peacock.toString();
		PlayerIconLabel.setIcon(PlayerIcon.PEACOCK.get());
		PlayerDescriptionArea.setText(Card.descriptions[SUSPECT_PEACOCK]);
		setMyCharacterUserVar();		

	}// GEN-LAST:event_peacockRadioItemStateChanged

	private void whiteRadioItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_whiteRadioItemStateChanged
		mySelectedCharacter = white.toString();
		PlayerIconLabel.setIcon(PlayerIcon.WHITE.get());
		PlayerDescriptionArea.setText(Card.descriptions[SUSPECT_WHITE]);
		setMyCharacterUserVar();		

	}// GEN-LAST:event_whiteRadioItemStateChanged

	private void greenRadioItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_greenRadioItemStateChanged
		mySelectedCharacter = green.toString();
		PlayerIconLabel.setIcon(PlayerIcon.GREEN.get());
		PlayerDescriptionArea.setText(Card.descriptions[SUSPECT_GREEN]);
		setMyCharacterUserVar();		

	}// GEN-LAST:event_greenRadioItemStateChanged

	private void scarletRadioItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_scarletRadioItemStateChanged
		mySelectedCharacter = scarlet.toString();
		PlayerIconLabel.setIcon(PlayerIcon.SCARLET.get());
		PlayerDescriptionArea.setText(Card.descriptions[SUSPECT_SCARLET]);
		setMyCharacterUserVar();		

	}// GEN-LAST:event_scarletRadioItemStateChanged

	private void mustardRadioItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_mustardRadioItemStateChanged
		mySelectedCharacter = mustard.toString();
		PlayerIconLabel.setIcon(PlayerIcon.MUSTARD.get());
		PlayerDescriptionArea.setText(Card.descriptions[SUSPECT_MUSTARD]);
		setMyCharacterUserVar();		

	}// GEN-LAST:event_mustardRadioItemStateChanged



    private void dealButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dealButtonActionPerformed
    	    	
		Room room = (Room) listRooms.getSelectedValue();
		ISFSObject obj = new SFSObject();
		obj.putUtfString("room", room.getName());
		sfs.send(new ExtensionRequest("deal",obj,null));
		    	
    }//GEN-LAST:event_dealButtonActionPerformed
    
	private void singlePlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_singlePlayerButtonActionPerformed
		LoginButton.setEnabled(false);
		singlePlayerButton.setEnabled(false);
		
		ClueMain.threadPoolExecutor.execute(new Runnable() {
			public void run() {
				ClueMain.startSinglePlayerGame();
			}
		});
	}// GEN-LAST:event_singlePlayerButtonActionPerformed

    private void endTurnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endTurnButtonActionPerformed
        endTurnButton.setEnabled(false);
		accuseButton.setEnabled(false);
		endTurnButton.setBackground(Color.white);

		ISFSObject obj = new SFSObject();
		if (sfs.isConnected()) sfs.send(new ExtensionRequest("endTurn",obj,null));
    }//GEN-LAST:event_endTurnButtonActionPerformed

    private void accuseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accuseButtonActionPerformed
    	
		AccusationDialog2 accusationDialog = new AccusationDialog2(ClueMain.frame, ClueMain.yourPlayer.getNotebook());
		ArrayList<Card> accusation = (ArrayList<Card>) accusationDialog.showDialog();
		
		accuseButton.setEnabled(false);
		
		if (accusation == null) {
			return;
		}

		if (!isConnected()) {
			boolean validAccusation = ClueMain.clue.matchesVictimSet(accusation);
			if (validAccusation) {
				SoundEffect.GASP.play();
				ClueMain.turn.gameOver = true;
			} else {
				int rand = new Random().nextInt(10);
				if (rand>5) SoundEffect.LAUGH.play();
				else SoundEffect.GIGGLE.play();
				ClueMain.yourPlayer.setHasMadeFalseAccusation();
			}
			JOptionPane.showMessageDialog(ClueMain.frame, "Your accusation is " + validAccusation + ".", "Accusation Validation", JOptionPane.PLAIN_MESSAGE);
		} else {
			Card suspect = null;
			Card weapon = null;
			Card room = null;
			
			for (Card card : accusation) {
				if (card.getType()==TYPE_SUSPECT) suspect = card;
				if (card.getType()==TYPE_WEAPON) weapon = card;
				if (card.getType()==TYPE_ROOM) room = card;
			}
			
			//send the accusation the server for validation
			ISFSObject obj = new SFSObject();
			obj.putInt("suspect", suspect.getValue());
			obj.putInt("room", room.getValue());
			obj.putInt("weapon", weapon.getValue());

			sfs.send(new ExtensionRequest("accusation",obj,null));
		}
		
    	
    }//GEN-LAST:event_accuseButtonActionPerformed



    public void sendMoveEvent(Player player, int fx, int fy, int tx, int ty, Color color, boolean secretPassage) {
    	if (!isConnected()) return;

		ISFSObject obj = new SFSObject();
		obj.putUtfString("player", player.getPlayerName());
		obj.putInt("character", player.getSuspectNumber());
		obj.putInt("from-x", fx);
		obj.putInt("from-y", fy);
		obj.putInt("to-x", tx);
		obj.putInt("to-y", ty);
		obj.putInt("playerColor", color.getRGB());
		obj.putBool("secretPassage", secretPassage);

		sfs.send(new ExtensionRequest("move",obj,null));
    	
    }
    
    public void sendDiceRollEvent(int roll1, int roll2) {
    	if (!isConnected()) return;

		ISFSObject obj = new SFSObject();
		obj.putInt("roll1", roll1);
		obj.putInt("roll2", roll2);

		sfs.send(new ExtensionRequest("diceRoll",obj,null));
    	
    }
    
    public void sendSetSuggestionEvent(Card suspect, Card weapon, Card room, Player suggesting_player) {
    	
    	if (!isConnected()) return;
    	
		ISFSObject obj = new SFSObject();
		obj.putUtfString("suggesting_player_name", suggesting_player.getPlayerName());
		obj.putInt("suggesting_player_suspectId", suggesting_player.getSuspectNumber());
		obj.putInt("suspect", suspect.getValue());
		obj.putInt("room", room.getValue());
		obj.putInt("weapon", weapon.getValue());

		sfs.send(new ExtensionRequest("suggestion",obj,null));
    	
    }
    
    public void sendShowCardResponse(Card card_to_show, Player showing_player, String player_to_show) {
    	if (!isConnected()) return;
    	
    	int card_value = -1;
    	int card_type = -1;
    	if (card_to_show != null) {
    		card_type = card_to_show.getType();
    		card_value = card_to_show.getValue();
    	}

		ISFSObject obj = new SFSObject();
		obj.putUtfString("player_to_show", player_to_show);
		obj.putUtfString("showing_player_name", showing_player.getPlayerName());
		obj.putInt("showing_player_suspectId", showing_player.getSuspectNumber());
		obj.putInt("card_value", card_value);
		obj.putInt("card_type", card_type);

		sfs.send(new ExtensionRequest("showCardResponse",obj,null));
    }
	
	public void setYourMultiplayer() {
    	Player player = null;
    	
    	if (scarletRadio.isSelected()) {
    		player = ClueMain.clue.addPlayer(scarlet, textUserName.getText(), COLOR_SCARLET, false);
        	ClueMain.frame.setIconImage(ClueMain.getImageIcon("MsScarlett.png").getImage());
    	}
    	if (greenRadio.isSelected()) {
    		player = ClueMain.clue.addPlayer(green, textUserName.getText(), COLOR_GREEN, false);
        	ClueMain.frame.setIconImage(ClueMain.getImageIcon("MrGreen.png").getImage());
    	}
    	if (mustardRadio.isSelected()) {
    		player = ClueMain.clue.addPlayer(mustard, textUserName.getText(), COLOR_MUSTARD, false);
        	ClueMain.frame.setIconImage(ClueMain.getImageIcon("ColMustard.png").getImage());
    	}
    	if (plumRadio.isSelected()) {
    		player = ClueMain.clue.addPlayer(plum, textUserName.getText(), COLOR_PLUM, false);
        	ClueMain.frame.setIconImage(ClueMain.getImageIcon("ProfPlum.png").getImage());
    	}
    	if (peacockRadio.isSelected()) {
    		player = ClueMain.clue.addPlayer(peacock, textUserName.getText(), COLOR_PEACOCK, false);
        	ClueMain.frame.setIconImage(ClueMain.getImageIcon("MrsPeacock.png").getImage());
    	}
    	if (whiteRadio.isSelected()) {
    		player = ClueMain.clue.addPlayer(white, textUserName.getText(), COLOR_WHITE, false);
        	ClueMain.frame.setIconImage(ClueMain.getImageIcon("MrsWhite.png").getImage());
    	}
    	
    	ClueMain.frame.setTitle("Clue - " + textUserName.getText());

    	ClueMain.setCurrentPlayer(player);
    	ClueMain.yourPlayer = ClueMain.currentTurnPlayer;
    	
	}
	
	public void setOtherMultiplayer(int id, String name) {
		
		if (ClueMain.clue.getPlayer(id)!=null) return;
    	    	
		switch (id) {
		case SUSPECT_SCARLET:
			ClueMain.clue.addPlayer(scarlet, name, COLOR_SCARLET, false);
			break;
		case SUSPECT_MUSTARD:
			ClueMain.clue.addPlayer(mustard, name, COLOR_MUSTARD, false);
			break;
		case SUSPECT_GREEN:
			ClueMain.clue.addPlayer(green, name, COLOR_GREEN, false);
			break;
		case SUSPECT_WHITE:
			ClueMain.clue.addPlayer(white, name, COLOR_WHITE, false);
			break;
		case SUSPECT_PEACOCK:
			ClueMain.clue.addPlayer(peacock, name, COLOR_PEACOCK, false);
			break;
		case SUSPECT_PLUM:
			ClueMain.clue.addPlayer(plum, name, COLOR_PLUM, false);
			break;
		}
    	
	}
	
	public void toggleRadioButtons(boolean flag) {

		scarletRadio.setEnabled(flag);
		mustardRadio.setEnabled(flag);
		greenRadio.setEnabled(flag);
		whiteRadio.setEnabled(flag);
		peacockRadio.setEnabled(flag);
		plumRadio.setEnabled(flag);
		
	}
	
	public void setMyCharacterUserVar() {
		//set the character in my user
		List<UserVariable> userVars = new ArrayList<UserVariable>(); 
		userVars.add(new SFSUserVariable("character", mySelectedCharacter));
		sfs.send(new SetUserVariablesRequest(userVars));		
	}
	
	public class TimedDialogAlert extends javax.swing.JDialog {
		public void start() {
			ClueMain.threadPoolExecutor.execute(new Runnable() {
				public void run() {
					SoundEffect.BUTTON.play();
					try {Thread.sleep(5000);}catch(Exception e){}
					dispose();
				}
			});
		}
		
	}
	
    private HTMLEditorKit configureHtmlEditorKit(javax.swing.JTextPane textPane) {
        final HTMLEditorKit kit = (HTMLEditorKit) textPane.getEditorKit();
        final StyleSheet css = new StyleSheet();
        css.addRule("body { font-family:tahoma; font-size: 12 }");
        kit.setStyleSheet(css);
        return kit;
    }
    
    
    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed
    	
		textUserName.setText("");
		dialogLogin.setVisible(true);
		buttonLogout.setEnabled(true);
		singlePlayerButton.setEnabled(false);
    	    	
    	sfs.connect(serverIPTextField.getText(),9933);
    	
    }//GEN-LAST:event_LoginButtonActionPerformed
    
	

	private void buttonLogoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonLogoutActionPerformed
		
		sfs.send(new LogoutRequest());
		
	}// GEN-LAST:event_buttonLogoutActionPerformed
	
	private void LeaveRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_LeaveRoomButtonActionPerformed
		
		sfs.send(new LeaveRoomRequest(sfs.getLastJoinedRoom()));
		
		buttonJoin.setEnabled(true);
		LeaveRoomButton.setEnabled(false);

		scarletRadio.setEnabled(true);
		mustardRadio.setEnabled(true);
		greenRadio.setEnabled(true);
		whiteRadio.setEnabled(true);
		peacockRadio.setEnabled(true);
		plumRadio.setEnabled(true);
		
		
	}// GEN-LAST:event_LeaveRoomButtonActionPerformed

    private void exitGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitGameButtonActionPerformed
    	if (isConnected()) buttonLogoutActionPerformed(null);
        System.exit(0);
    }//GEN-LAST:event_exitGameButtonActionPerformed
    

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton buttonAlertOk;
	private javax.swing.JButton buttonCreateRoomCancel;
	private javax.swing.JButton buttonCreateRoomOk;
	private javax.swing.JButton buttonJoin;
	private javax.swing.JButton buttonJoinPrivateCancel;
	private javax.swing.JButton buttonJoinPrivateOk;
	private javax.swing.JButton buttonLogin;
	private javax.swing.JButton buttonLogout;
	private javax.swing.JButton buttonNewRoom;
	private javax.swing.JButton buttonSend;
	private javax.swing.JButton buttonSendPrivate;
	private javax.swing.JButton buttonSendPrivateCancel;
	private javax.swing.JButton buttonSendPrivateOk;
	private TimedDialogAlert dialogAlert;
	private javax.swing.JDialog dialogJoinPrivateRoom;
	private javax.swing.JDialog dialogLogin;
    private javax.swing.JTextField serverIPTextField;
    private javax.swing.JLabel ipLabel;

	private javax.swing.JDialog dialogNewRoom;
	private javax.swing.JDialog dialogPrivate;
    private javax.swing.JTextArea alertTextArea;
	private javax.swing.JLabel labelChatHistory;
	private javax.swing.JLabel labelMaxUsers;
	private javax.swing.JLabel labelPassword;
	private javax.swing.JLabel labelPrivateMessage;
	private javax.swing.JLabel labelPrivateRoomPassword;
	private javax.swing.JLabel labelRoomList;
	private javax.swing.JLabel labelRoomName;
	private javax.swing.JLabel labelUserList;
	private javax.swing.JLabel labelUserName;
	private javax.swing.JList listRooms;
	private javax.swing.JList listUsers;
	private javax.swing.JScrollPane scrollPaneChatHistory;
	private javax.swing.JScrollPane scrollPaneRoomList;
	private javax.swing.JScrollPane scrollPaneUserList;
	private javax.swing.JSlider sliderNewRoomMaxUsers;
	private javax.swing.JTextPane textChatHistory;
	private javax.swing.JTextField textChatMessage;
	private javax.swing.JPasswordField textJoinPrivatePassword;
	private javax.swing.JTextField textNewRoomName;
	private javax.swing.JPasswordField textNewRoomPassword;
	private javax.swing.JTextField textPrivateMessage;
	private javax.swing.JTextField textUserName;
	
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton LeaveRoomButton;
    private javax.swing.JRadioButton whiteRadio;
    private javax.swing.JRadioButton greenRadio;
    private javax.swing.JRadioButton mustardRadio;
    private javax.swing.JRadioButton peacockRadio;
    private javax.swing.ButtonGroup playerSelectGroup;
    private javax.swing.JRadioButton plumRadio;
    private javax.swing.JRadioButton scarletRadio;
    private javax.swing.JButton dealButton;

    private javax.swing.JButton exitGameButton;
    private javax.swing.JButton LoginButton;
    public static javax.swing.JButton accuseButton;
    private javax.swing.JButton singlePlayerButton;
    public static javax.swing.JButton endTurnButton;
    
    private javax.swing.JTextArea PlayerDescriptionArea;
    private javax.swing.JLabel PlayerIconLabel;

	
    
	enum PlayerIcon {

		SCARLET("MsScarlett1.png"), 
		MUSTARD("ColMustard1.png"), 
		GREEN("MrGreen1.png"), 
		WHITE("MrsWhite1.png"), 
		PLUM("ProfPlum1.png"), 
		PEACOCK("MrsPeacock1.png");

		private ImageIcon image;

		PlayerIcon(String filename) {
			try {
				URL url = this.getClass().getClassLoader().getResource(filename);
				image = new ImageIcon(ImageIO.read(url));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public ImageIcon get() {
			return image;
		}

		public static void init() {
			values(); // calls the constructor for all the elements
		}
	}
}
