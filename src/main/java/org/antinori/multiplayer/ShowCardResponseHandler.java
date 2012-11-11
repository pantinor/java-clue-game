package org.antinori.multiplayer;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;



public class ShowCardResponseHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User sender, ISFSObject params) {
		
		trace("ClueGameExtension ShowCardResponseHandler got sender: " + sender);
		
		ClueGameExtension extension = (ClueGameExtension)this.getParentExtension();
		extension.showCardsRunnable.setResponseParams(params);


	}
	

}
