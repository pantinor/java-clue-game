package org.antinori.multiplayer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.antinori.game.Card;
import org.antinori.game.Clue;
import org.antinori.game.Player;

public class DealRequestHandler extends BaseClientRequestHandler {

    public void handleClientRequest(User sender, ISFSObject params) {

        trace("ClueGameExtension DealRequestHandler got sender: " + sender);

        String roomName = params.getUtfString("room");

        Room room = getParentExtension().getParentZone().getRoomByName(roomName);

        boolean isGameStarted = (room.getVariable("isGameStarted") != null && room.getVariable("isGameStarted").getBoolValue());
        if (isGameStarted) {

            ISFSObject resObj = SFSObject.newInstance();
            resObj.putUtfString("error", "The game is already started.  You cannot start at this time.");
            send("error", resObj, sender);

            return;
        }

        if (room.getSize().getUserCount() < 3) {

            ISFSObject resObj = SFSObject.newInstance();
            resObj.putUtfString("error", "3 players are required to start the game.");
            send("error", resObj, sender);

            return;
        }

        Clue deck = new Clue();
        deck.createDeck();

        //set the deck in the room variable
        ArrayList<RoomVariable> vars = new ArrayList<RoomVariable>();
        vars.add(new SFSRoomVariable("deck", deck, false, true, true));
        vars.add(new SFSRoomVariable("isGameStarted", true, false, true, true));
        this.getApi().setRoomVariables(sender, room, vars);

        //set the players in the game deck on the server 
        for (User user : room.getUserList()) {

            String ch = user.getVariable("character").getStringValue();
            trace("Got character:" + ch);

            if (ch.equals("Miss Scarlet")) {
                deck.addPlayer(Card.scarlet, user.getName(), Color.black, false);
            }
            if (ch.equals("Colonel Mustard")) {
                deck.addPlayer(Card.mustard, user.getName(), Color.black, false);
            }
            if (ch.equals("Mr. Green")) {
                deck.addPlayer(Card.green, user.getName(), Color.black, false);
            }
            if (ch.equals("Professor Plum")) {
                deck.addPlayer(Card.plum, user.getName(), Color.black, false);
            }
            if (ch.equals("Mrs. White")) {
                deck.addPlayer(Card.white, user.getName(), Color.black, false);
            }
            if (ch.equals("Mrs. Peacock")) {
                deck.addPlayer(Card.peacock, user.getName(), Color.black, false);
            }
        }

        trace("getCurrentPlayerCount = " + deck.getCurrentPlayerCount());

        deck.setMultiplayerHandler(this);

        //send the list of players to everyone
        ISFSArray ids = new SFSArray();
        ISFSArray names = new SFSArray();
        for (Player player : deck.getPlayers()) {
            ids.addInt(player.getSuspectNumber());
            names.addUtfString(player.getPlayerName());
        }
        ISFSObject resObj = SFSObject.newInstance();
        resObj.putSFSArray("ids", ids);
        resObj.putSFSArray("names", names);
        send("setPlayers", resObj, room.getUserList());

        try {
            deck.dealShuffledDeck();
        } catch (Exception e) {
            trace("got exception: " + e.getMessage());
        }

    }

    //send the dealt card to the specified user/player
    public void dealCard(Card card, Player player) {
        User user = getParentExtension().getParentZone().getUserByName(player.getPlayerName());
        ISFSObject resObj = SFSObject.newInstance();
        resObj.putClass("card", card);
        send("dealtCard", resObj, user);
        trace("sent dealtCard event");
    }

    //send the getSet event to all the players 
    public void getSet(Player player) {
        User user = getParentExtension().getParentZone().getUserByName(player.getPlayerName());
        Room room = user.getLastJoinedRoom();
        ISFSObject resObj = SFSObject.newInstance();
        send("getSet", resObj, room.getUserList());
        trace("sent getSet event");
    }

    //send the start turn event to the first player
    public void startTurn(Player startingPlayer) {
        User user = getParentExtension().getParentZone().getUserByName(startingPlayer.getPlayerName());
        ISFSObject resObj = SFSObject.newInstance();
        send("startTurn", resObj, user);
        trace("sent startTurn event");
    }

}
