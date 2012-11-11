package org.antinori.multiplayer;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ValidateJoinRoomHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User user, ISFSObject params) {

		String message = "success";

		// Get the client parameters
		String ch = params.getUtfString("character");
		String roomName = params.getUtfString("room");
		
		Room room = getParentExtension().getParentZone().getRoomByName(roomName);

		trace("ValidateJoinRoomHandler on room: " + room);
		trace("ValidateJoinRoomHandler for user: " + user + " for " + ch);
		
		List<User> users = room.getUserList();


		do {

			if (!room.isGame()) {
				message = "Room is not a game.";
				break;
			}

			boolean isGameStarted = (room.getVariable("isGameStarted") != null && room.getVariable("isGameStarted").getBoolValue());
			if (isGameStarted) {
				message = "The game is already started.  Create a new game room to join.";
				break;
			}
			
			for (User u : users) {
				UserVariable uv = u.getVariable("character");
				if (uv==null) continue;
				if (uv.getStringValue().equals(ch)) {
					message = "The character is already selected by another player.  Select a different player before joining.";
					break;
				}
			}
			

		} while (false);
		
		if (message.equals("success")) {
			try {
				//room.addUser(user);
				this.getApi().joinRoom(user, room);
			} catch(Exception e) {
				trace("got exception: " + e.getMessage());
			}
		}
		
		ISFSObject resObj = SFSObject.newInstance();
		resObj.putBool("validated",message.equals("success"));
		resObj.putUtfString("message", message);

		send("validateJoinRoom", resObj, user);
		
		trace("user count is: " + room.getSize().getUserCount());
		

	}

}
