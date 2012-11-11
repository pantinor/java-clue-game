package org.antinori.multiplayer;

import java.util.List;

import org.antinori.game.Clue;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class ShowCardsRunnable implements Runnable {
	
	Clue deck;
	User sender;
	ISFSObject suggestion_params;
	ClueGameExtension extension;
	int num_users = 0;
	ISFSObject response_params = null;
	List<User> users;
	
	ShowCardsRunnable(ClueGameExtension extension, Clue deck, User sender, ISFSObject suggestion_params, List<User> users) {
		
		this.extension = extension;
		this.deck = deck;
		this.sender = sender;
		this.suggestion_params = suggestion_params;
		this.num_users = users.size();
		this.users = users;
		
	}

	public void run() {
		
		String name = sender.getName();
		boolean shown_card = false;
		int tried_count = 1;

		do {
			name = deck.getAdjacentPlayerName(name);
			User user = extension.getParentZone().getUserByName(name);
			extension.send("showCardRequest", suggestion_params, user);
			
			extension.trace("ShowCardsRunnable : sent show cards request to adjacent player: " + name);
			
			//wait here for response max of 5 minutes
			try {
				int timeout = 0;
				while (this.response_params == null && timeout < 300) {
					Thread.sleep(1000);
					timeout ++;
				}			
			} catch (Exception e) {
			}
			
			extension.trace("ShowCardsRunnable : processing a show cards response : " + response_params);
			
			if (response_params != null) {
			
				String player_to_show = response_params.getUtfString("player_to_show");
				String showing_player_name = response_params.getUtfString("showing_player_name");
				//int showing_player_suspectId = response_params.getInt("showing_player_suspectId");
				//int card_value = response_params.getInt("card_value");
				int card_type = response_params.getInt("card_type");
				
				User user_to_show = extension.getParentZone().getUserByName(player_to_show);
				User showing_user = extension.getParentZone().getUserByName(showing_player_name);
	
				//send the response out
				if (card_type == -1) {
					
					for (User u : users) {
						if (u.equals(showing_user)) continue;
						extension.send("showCardResponse", response_params, u);
						extension.trace("ShowCardsRunnable : sent no shown card notice to " + u);
					}
	
				} else {
					extension.send("showCardResponse", response_params, user_to_show);
					shown_card = true;
					
					//remove the showing player and player to show from the users list and 
					//let the remaining players know that a card was shown
					response_params.removeElement("card_type");
					response_params.putInt("card_type", -99);
					for (User u : users) {
						if (u.equals(showing_user) || u.equals(user_to_show)) continue;
						extension.send("showCardResponse", response_params, u);
						extension.trace("ShowCardsRunnable :  sent shown card response to " + u);
					}
				}
			}
			
			tried_count ++;
			this.response_params = null;

		} while (!shown_card && tried_count < num_users);
		
		if (!shown_card) {
			ISFSObject resObj = SFSObject.newInstance();
			resObj.putInt("card_type", -100);
			extension.send("showCardResponse", resObj, sender);
			extension.trace("ShowCardsRunnable : sent final no one had any cards to show to " + sender);
		}
		
		extension.trace("ShowCardsRunnable : finished");

		
		
	}
	
	public void setResponseParams(ISFSObject params) {
		this.response_params = params;
	}

}
