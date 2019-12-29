package gdx.clue;

import gdx.clue.ClueMain.Suspect;

public class Card {

    private int type;
    private int value;

    public static final int NUM_SUSPECTS = 6;
    public static final int NUM_ROOMS = 9;
    public static final int NUM_WEAPONS = 6;
    public static final int TOTAL = NUM_ROOMS + NUM_SUSPECTS + NUM_WEAPONS;

    public static final int TYPE_SUSPECT = 0;
    public static final int TYPE_WEAPON = 1;
    public static final int TYPE_ROOM = 2;

    public static final int ROOM_HALL = 0;
    public static final int ROOM_LOUNGE = 1;
    public static final int ROOM_DINING = 2;
    public static final int ROOM_KITCHEN = 3;
    public static final int ROOM_BALLROOM = 4;
    public static final int ROOM_CONSERVATORY = 5;
    public static final int ROOM_BILLIARD = 6;
    public static final int ROOM_STUDY = 7;
    public static final int ROOM_LIBRARY = 8;

    public static final int WEAPON_KNIFE = 0;
    public static final int WEAPON_ROPE = 1;
    public static final int WEAPON_REVOLVER = 2;
    public static final int WEAPON_WRENCH = 3;
    public static final int WEAPON_PIPE = 4;
    public static final int WEAPON_CANDLE = 5;

    public Card(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return type + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card c = (Card) obj;
            return (c.type == this.type && c.value == this.value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String desc = null;

        switch (type) {
            case TYPE_SUSPECT:
                desc = Suspect.values()[value].title();
                break;
            case TYPE_ROOM:
                switch (value) {
                    case ROOM_HALL:
                        desc = "Hall";
                        break;
                    case ROOM_LOUNGE:
                        desc = "Lounge";
                        break;
                    case ROOM_DINING:
                        desc = "Dining Room";
                        break;
                    case ROOM_KITCHEN:
                        desc = "Kitchen";
                        break;
                    case ROOM_BALLROOM:
                        desc = "Ballroom";
                        break;
                    case ROOM_CONSERVATORY:
                        desc = "Conservatory";
                        break;
                    case ROOM_BILLIARD:
                        desc = "Billiard Room";
                        break;
                    case ROOM_STUDY:
                        desc = "Study";
                        break;
                    case ROOM_LIBRARY:
                        desc = "Library";
                        break;
                }
                break;
            case TYPE_WEAPON:
                switch (value) {
                    case WEAPON_KNIFE:
                        desc = "Knife";
                        break;
                    case WEAPON_ROPE:
                        desc = "Rope";
                        break;
                    case WEAPON_REVOLVER:
                        desc = "Revolver";
                        break;
                    case WEAPON_WRENCH:
                        desc = "Wrench";
                        break;
                    case WEAPON_PIPE:
                        desc = "Pipe";
                        break;
                    case WEAPON_CANDLE:
                        desc = "Candlestick";
                        break;
                }
                break;
        }

        return desc;

    }

}
