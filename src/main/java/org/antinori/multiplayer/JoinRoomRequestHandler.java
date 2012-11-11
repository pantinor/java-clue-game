package org.antinori.multiplayer;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class JoinRoomRequestHandler extends BaseServerEventHandler {

	public void handleServerEvent(ISFSEvent event) throws SFSException {
		Room room = (Room) event.getParameter(SFSEventParam.ROOM);
		User user = (User) event.getParameter(SFSEventParam.USER);

		trace("ClueGameExtension JoinRoomEventHandler got room: " + room);
		trace("ClueGameExtension JoinRoomEventHandler got user: " + user);



		

	}

}
