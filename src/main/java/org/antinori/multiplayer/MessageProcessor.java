package org.antinori.multiplayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MessageProcessor {

    private static final String[] emotesCodes = {":\\)", "\\;\\)", ":\\(", "\\(\\)\\]", "\\(brb\\)", "\\;\\(", "8\\)", ":D", "&lt\\;3", "\\(sfs\\)",};

    private static final String[] emotesFileNames = {"smile.png", "wink.png", "sad.png", "beer.png", "brb.png", "crying.png", "cool.png", "laugh.png", "love.png", "sfs.png"};

    private static final Map<String, String> emotesURL;

    static {
        emotesURL = new HashMap<String, String>();
        URL emoteURL;

        for (int i = 0; i < emotesCodes.length; i++) {
            emoteURL = MessageProcessor.class.getClassLoader().getResource(emotesFileNames[i]);
            if (emoteURL != null) {
                emotesURL.put(emotesCodes[i], emoteURL.toString());
            }
        }
    }

    public static String parseSmiles(String message) {
        String msg = message;

        for (String smile : emotesURL.keySet()) {
            msg = msg.replaceAll(smile, "<img src='" + emotesURL.get(smile) + "' alt='" + smile + "'>");
        }

        return msg;
    }
}
