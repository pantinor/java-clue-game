package org.antinori.multiplayer;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class ClueGameExtension extends SFSExtension {

    public ShowCardsRunnable showCardsRunnable = null;

    public void init() {

        trace("ClueGameExtension init");

        addRequestHandler("validateJoinRoom", ValidateJoinRoomHandler.class);
        addRequestHandler("deal", DealRequestHandler.class);
        addRequestHandler("move", MoveRequestHandler.class);

        addRequestHandler("suggestion", SuggestionRequestHandler.class);
        addRequestHandler("showCardResponse", ShowCardResponseHandler.class);

        addRequestHandler("accusation", AccusationRequestHandler.class);
        addRequestHandler("endTurn", StartTurnEventHandler.class);

        addRequestHandler("diceRoll", DiceRollEventHandler.class);

        addEventHandler(SFSEventType.USER_LOGOUT, UserDisconnectEventHandler.class);
        addEventHandler(SFSEventType.USER_LEAVE_ROOM, UserDisconnectEventHandler.class);
        addEventHandler(SFSEventType.USER_DISCONNECT, UserDisconnectEventHandler.class);

    }

}
