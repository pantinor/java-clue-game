package org.antinori.multiplayer;

import java.util.List;

import org.antinori.game.Clue;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;



public class SuggestionRequestHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User sender, ISFSObject params) {
		
		trace("ClueGameExtension SuggestionRequestHandler got sender: " + sender);
		
		Room room = sender.getLastJoinedRoom();
		
		//send the suggestion to all other players
		List<User> users = room.getUserList();
		send("suggestion", params, users);

		RoomVariable rv = room.getVariable("deck");
		Clue deck = (Clue)rv.getSFSObjectValue();
		
		ClueGameExtension extension = (ClueGameExtension)this.getParentExtension();
		extension.showCardsRunnable = new ShowCardsRunnable(extension, deck, sender, params, room.getUserList());
		Thread thread = new Thread(extension.showCardsRunnable);
		thread.start();

	}
	

}
