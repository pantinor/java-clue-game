package org.antinori.astar;

import java.util.LinkedList;
import java.util.List;

/**
 * State used by path finders when walking potential node paths.
 * 
* @author Gene McCulley
 */
class NodeState<T extends Node<T>> {

    final T node;
    NodeState<T> previous;

    NodeState(T node, NodeState<T> previous) {
        this.node = node;
        this.previous = previous;
    }

    List<T> makePath() {
        List<T> result = new LinkedList<T>();
        NodeState<T> s = this;
        while (s != null) {
            result.add(0, s.node);
            s = s.previous;
        }

        return result;
    }

}
