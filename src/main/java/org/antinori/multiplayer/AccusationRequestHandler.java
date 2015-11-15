package org.antinori.multiplayer;

import java.util.List;

import org.antinori.game.Clue;
import org.antinori.game.ClueMain;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AccusationRequestHandler extends BaseClientRequestHandler {

    public void handleClientRequest(User sender, ISFSObject params) {

        trace("ClueGameExtension AccusationRequestHandler got sender: " + sender);

        int suspectId = params.getInt("suspect");
        int weaponId = params.getInt("weapon");
        int roomId = params.getInt("room");

        Room room = sender.getLastJoinedRoom();

        RoomVariable rv = room.getVariable("deck");
        Clue deck = (Clue) rv.getSFSObjectValue();

        boolean validAccusation = deck.matchesVictimSet(suspectId, weaponId, roomId);

        params.putBool("valid", validAccusation);
        params.putUtfString("accusingPlayer", sender.getName());

        //send the suggestion to all other players
        List<User> users = room.getUserList();
        send("accusation", params, users);

        if (validAccusation) {
            ISFSObject resObj = SFSObject.newInstance();
            resObj.putUtfString("message", "Game is over because a valid accusation was made. The winner is " + sender.getName());
            send("gameOver", resObj, users);
        }

    }

}
