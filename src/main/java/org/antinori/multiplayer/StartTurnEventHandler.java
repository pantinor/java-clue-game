package org.antinori.multiplayer;

import org.antinori.game.Clue;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class StartTurnEventHandler extends BaseClientRequestHandler {

    public void handleClientRequest(User sender, ISFSObject params) {

        trace("ClueGameExtension StartTurnEventHandler got sender: " + sender);

        Room room = sender.getLastJoinedRoom();
        RoomVariable rv = room.getVariable("deck");
        Clue deck = (Clue) rv.getSFSObjectValue();

        //tell the adjacent player that they can start their turn
        User user = getParentExtension().getParentZone().getUserByName(deck.getAdjacentPlayerName(sender.getName()));

        trace("got adjacent user:" + user);

        ISFSObject resObj = SFSObject.newInstance();
        send("startTurn", resObj, user);

    }

}
