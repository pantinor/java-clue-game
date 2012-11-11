package org.antinori.astar;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.antinori.game.Card;

public class Location implements Node<Location>, Serializable {

	private final int x;
	private final int y;
	private int height;
	private boolean blocked;
	private boolean highlight;
	private boolean isRoom;
	private int roomId = -1;
	private Color color = Color.gray;

	private transient List<Location> neighbors;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
		neighbors = new ArrayList<Location>();
	}

	public Location(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
		neighbors = new ArrayList<Location>();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x * y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location) {
			Location loc = (Location) obj;
			return (loc.getX() == this.x && loc.getY() == this.y);
		} else {
			return false;
		}
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean getBlocked() {
		return blocked;
	}

	public void setHighlighted(boolean highlight) {
		this.highlight = highlight;
	}

	public boolean getHighlighted() {
		return highlight;
	}

	public void setIsRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public void setRoomId(int id) {
		this.roomId = id;
	}

	public int getRoomId() {
		return roomId;
	}

	public Card getRoomCard() {
		return (roomId != -1 ? new Card(Card.TYPE_ROOM, roomId) : null);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getDistance(Location dest) {
		double a = dest.x - x;
		double b = dest.y - y;
		return Math.sqrt(a * a + b * b);
	}

	public double pathCostEstimate(Location goal) {
		return getDistance(goal) * 0.99;
	}

	public double traverseCost(Location target) {
		double distance = getDistance(target);
		double diff = target.getHeight() - getHeight();
		return Math.abs(diff) + distance;
	}

	public Iterable<Location> neighbors() {
		List<Location> realNeighbors = new ArrayList<Location>();
		if (!blocked) {
			for (Location loc : neighbors) {
				if (!loc.blocked) {
					realNeighbors.add(loc);
				}
			}
		}

		return realNeighbors;
	}

	public void addNeighbor(Location l) {
		neighbors.add(l);
	}

	public void removeNeighbor(Location l) {
		neighbors.remove(l);
	}

	@Override
	public String toString() {
		return "Location [" + x + "][" + y + "]";
	}

}
