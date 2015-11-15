// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package org.antinori.astar;

import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the A* path finding algorithm.
 *
 * @author Gene McCulley
 */
public class AStar<T extends Node<T>> extends AbstractPathFinder<T> {

    private class State extends NodeState<T> implements Comparable<State> {

        private final double costFromStart;
        private final double costToGoal;

        private State(T node, double costFromStart, State parent, Collection<T> goals) {
            super(node, parent);
            this.costFromStart = costFromStart;
            costToGoal = minimumPathCostEstimate(node, goals);
        }

        private double minimumPathCostEstimate(T node, Collection<T> goals) {
            double min = Double.MAX_VALUE;
            for (T goal : goals) {
                double cost = node.pathCostEstimate(goal);
                if (cost < min) {
                    min = cost;
                }
            }

            return min;
        }

        private double totalCost() {
            return costFromStart + costToGoal;
        }

        public int compareTo(State other) {
            return (int) (totalCost() - other.totalCost());
        }

    }

    public List<T> findPath(Collection<T> graph, T start, Collection<T> goals) {
        canceled = false;
        Map<T, State> open = new HashMap<T, State>();
        Map<T, State> closed = new HashMap<T, State>();
        State startState = new State(start, 0, null, goals);
        open.put(start, startState);
        Ordering<Map.Entry<T, State>> orderByEntryValue = Utilities.orderByEntryValue();
        while (!(open.isEmpty() || canceled)) {
            final State state = open.remove(orderByEntryValue.min(open.entrySet()).getKey());
            fireConsidered(new PathEvent<T>(this) {

                @Override
                public List<T> getPath() {
                    return state.makePath();
                }

            });
            if (goals.contains(state.node)) {
                return state.makePath();
            } else {
                for (T newNode : state.node.neighbors()) {
                    double newCost = state.costFromStart + state.node.traverseCost(newNode);
                    State openNode = open.get(newNode);
                    if (openNode != null && openNode.costFromStart <= newCost) {
                        continue;
                    }

                    State closedNode = closed.get(newNode);
                    if (closedNode != null && closedNode.costFromStart <= newCost) {
                        continue;
                    }

                    if (closedNode != null) {
                        closed.remove(newNode);
                    }

                    if (openNode != null) {
                        open.remove(newNode);
                    }

                    State newState = new State(newNode, newCost, state, goals);
                    open.put(newNode, newState);
                }
            }

            closed.put(state.node, state);
        }

        return null;
    }

    public String name() {
        return "A*";
    }

}
