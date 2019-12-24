package gdx.clue.astar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Map implements Serializable {

    private final Location[][] nodes;
    private final static Random rng = new Random();

    public Location getLocation(int x, int y) {
        return nodes[x][y];
    }

    public int getXSize() {
        return nodes.length;
    }

    public int getYSize() {
        return nodes[0].length;
    }

    public Collection<Location> getLocations() {
        Collection<Location> locations = new ArrayList<>();
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                locations.add(nodes[i][j]);
            }
        }
        return locations;
    }

    private void attachNeighbors() {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                Location t = nodes[i][j];
                if (i != 0) {
                    t.addNeighbor(nodes[i - 1][j]);
                    if (j != 0) {
                        t.addNeighbor(nodes[i - 1][j - 1]);
                    }

                    if (j != nodes[0].length - 1) {
                        t.addNeighbor(nodes[i - 1][j + 1]);
                    }
                }

                if (i != nodes.length - 1) {
                    t.addNeighbor(nodes[i + 1][j]);

                    if (j != 0) {
                        t.addNeighbor(nodes[i + 1][j - 1]);
                    }

                    if (j != nodes[0].length - 1) {
                        t.addNeighbor(nodes[i + 1][j + 1]);
                    }
                }

                if (j != 0) {
                    t.addNeighbor(nodes[i][j - 1]);
                }

                if (j != nodes[0].length - 1) {
                    t.addNeighbor(nodes[i][j + 1]);
                }
            }

        }
    }

    public void randomize() {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                if (rng.nextInt(4) == 0) {
                    Location t = nodes[i][j];
                    if (rng.nextInt(2) == 0) {
                        t.setHeight(100);
                    } else {
                        t.setBlocked(true);
                    }
                }
            }
        }
    }

    public Map(int bounds) {
        nodes = new Location[bounds][bounds];
        for (int i = 0; i < bounds; i++) {
            for (int j = 0; j < bounds; j++) {
                nodes[i][j] = new Location(i, j);
            }
        }

        attachNeighbors();
    }

    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        attachNeighbors();
    }

}
