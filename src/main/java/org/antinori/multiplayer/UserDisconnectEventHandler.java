package org.antinori.multiplayer;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserDisconnectEventHandler extends BaseServerEventHandler {

    public void handleServerEvent(ISFSEvent event) throws SFSException {

        User user = (User) event.getParameter(SFSEventParam.USER);

        trace("ClueGameExtension UserDisconnectEventHandler got user: " + user);

        UserVariable uv = user.getVariable("room");

        if (uv == null) {
            return;
        }

        Room room = (Room) getParentExtension().getParentZone().getRoomByName(uv.getStringValue());

        trace("ClueGameExtension UserDisconnectEventHandler got room: " + room);

		//handles both the user disconnected event and the user exit room events
        if (room != null) {
            //set the game to over
            if (room.getUserList().size() < 3) {
                room.removeVariable("isGameStarted");
            }
        }

        //send the note to all other players
        List<User> users = room.getUserList();
        ISFSObject resObj = SFSObject.newInstance();
        resObj.putUtfString("message", "Game is over because there are less than 3 players.  A player has disconnected from the server.");
        send("gameOver", resObj, users);

    }

}
