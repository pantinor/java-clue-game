package org.antinori.astar;

import java.util.EventObject;
import java.util.List;

/**
 * An encapsulation of the data describing an event related to a
 * {@link PathFinder}.
 * 
* @author Gene McCulley
 */
public abstract class PathEvent<T extends Node> extends EventObject {

    /**
     * Create a new path event.
     *
     * @param source the source of this event
     * @param path the path related to the event
     */
    protected PathEvent(Object source) {
        super(source);
    }

    /**
     * Returns the path related to this event.
     *
     * @return the path related to the event in the form of a
     * {@link java.util.List} of {@link Node}s or <tt>null</tt> if no path was
     * found
     */
    public abstract List<T> getPath();

}
