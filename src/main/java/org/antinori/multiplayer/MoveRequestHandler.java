package org.antinori.multiplayer;

import java.util.List;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;



public class MoveRequestHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User sender, ISFSObject params) {
		
		trace("ClueGameExtension MoveRequestHandler got sender: " + sender);
				
		Room room = sender.getLastJoinedRoom();
		
		trace("got move:" + sender);
		
		List<User> users = room.getUserList();
		
		//remove the sender
		users.remove(sender);

		send("move", params, users);

	}
	

}
