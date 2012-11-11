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



public class DiceRollEventHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User sender, ISFSObject params) {
		
		trace("ClueGameExtension DiceRollEventHandler got sender: " + sender);

		Room room = sender.getLastJoinedRoom();
		List<User> users = room.getUserList();
		
		for (User u : users) {
			if (u.equals(sender)) continue;
			send("diceRoll", params, u);
			trace("DiceRollEventHandler :  sent diceRoll to " + u);
		}
		

		

	}
	

}
