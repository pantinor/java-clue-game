package gdx.clue.astar;

import java.util.Collection;
import java.util.List;

/**
 * An interface implemented by classes that provide a mechanism to find routes
 * between nodes.When the path has been found or all possible paths have been exhausted, a
 {@link PathEvent} will be sent.
 *
 *
 * @author Gene McCulley
 * @param <T>
 */
public interface PathFinder<T extends Node> {

    /**
     * Cancels the execution.
     */
    void cancel();

    /**
     * Find a path between the start and the goal {@link Node}s.
     *
     * @param graph the graph
     * @param start the starting @{link Node}
     * @param goals the goal @{link Node}s
     * @return a {@link java.util.List} of {@link Node} elements or
     * <tt>null</tt> if no path was found.
     */
    List<T> findPath(Collection<T> graph, T start, Collection<T> goals);

    /**
     * Add a listener for {@link PathEvent}s.
     *
     * @param l the listener to add.
     */
    void addPathListener(PathListener<T> l);

    /**
     * Remove a listener for {@link PathEvent}s.
     *
     * @param l the listener to remove.
     */
    void removePathListener(PathListener<T> l);

    /**
     * Returns the name of the algorithm.
     *
     * @return the name of the algorithm
     */
    String name();

}
