package org.antinori.astar;



import java.util.ArrayList;
import java.util.List;

/**
* Basic functionality of path finders to make it easy to create implementations of {@link PathFinder}.
*
* @author Gene McCulley
*/
public abstract class AbstractPathFinder<T extends Node> implements PathFinder<T> {

 protected final List<PathListener<T>> listeners = new ArrayList<PathListener<T>>();
 protected volatile boolean canceled;

 public void cancel() {
     canceled = true;
 }

 protected void fireConsidered(PathEvent<T> pathEvent) {
     for (PathListener<T> listener : listeners) {
         listener.considered(pathEvent);
     }
 }

 public void addPathListener(PathListener<T> l) {
     listeners.add(l);
 }

 public void removePathListener(PathListener<T> l) {
     listeners.remove(l);
 }

}

