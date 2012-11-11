package org.antinori.astar;

import static org.antinori.game.Card.ROOM_BALLROOM;
import static org.antinori.game.Card.ROOM_BILLIARD;
import static org.antinori.game.Card.ROOM_CONSERVATORY;
import static org.antinori.game.Card.ROOM_DINING;
import static org.antinori.game.Card.ROOM_HALL;
import static org.antinori.game.Card.ROOM_KITCHEN;
import static org.antinori.game.Card.ROOM_LIBRARY;
import static org.antinori.game.Card.ROOM_LOUNGE;
import static org.antinori.game.Card.ROOM_STUDY;
import static org.antinori.game.Card.SUSPECT_GREEN;
import static org.antinori.game.Card.SUSPECT_MUSTARD;
import static org.antinori.game.Card.SUSPECT_PEACOCK;
import static org.antinori.game.Card.SUSPECT_PLUM;
import static org.antinori.game.Card.SUSPECT_SCARLET;
import static org.antinori.game.Card.SUSPECT_WHITE;
import static org.antinori.game.Card.WEAPON_CANDLE;
import static org.antinori.game.Card.WEAPON_KNIFE;
import static org.antinori.game.Card.WEAPON_PIPE;
import static org.antinori.game.Card.WEAPON_REVOLVER;
import static org.antinori.game.Card.WEAPON_ROPE;
import static org.antinori.game.Card.WEAPON_WRENCH;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.antinori.game.ClueMain;
import org.antinori.game.SoundEffect;

public class MapView extends JPanel implements MouseListener {

	private static final int width = 32;
	private ClueMap map;
	private List<Location> path;
	
	
	public static final ArrayList<BufferedImage> room_images = new ArrayList<BufferedImage>(10);
	
	public static final Integer[] scarlet_coord = {SUSPECT_SCARLET,5,55,190,200};
	public static final Integer[] mustard_coord = {SUSPECT_MUSTARD,5,55,190,200};
	public static final Integer[] green_coord = {SUSPECT_GREEN,5,55,190,200};
	public static final Integer[] plum_coord = {SUSPECT_PLUM,5,55,190,200};
	public static final Integer[] white_coord = {SUSPECT_WHITE,5,55,190,200};
	public static final Integer[] peacock_coord = {SUSPECT_PEACOCK,5,55,190,200};
	
	public static final ArrayList<BufferedImage> suspect_images = new ArrayList<BufferedImage>(6);
	
	public static final Integer[] knife_coord = {WEAPON_KNIFE,54,32,582,853};
	public static final Integer[] rope_coord = {WEAPON_ROPE,54,32,582,853};
	public static final Integer[] candle_coord = {WEAPON_CANDLE,54,32,582,853};
	public static final Integer[] pipe_coord = {WEAPON_PIPE,54,32,582,853};
	public static final Integer[] wrench_coord = {WEAPON_WRENCH,54,32,582,853};
	public static final Integer[] revolver_coord = {WEAPON_REVOLVER,54,32,582,853};

	public static final ArrayList<BufferedImage> weapon_images = new ArrayList<BufferedImage>(6);
	
	public static final ArrayList<Image> dice_faces = new ArrayList<Image>(6);
	public Image rolledDiceImageLeft = null;
	public Image rolledDiceImageRight = null;


	
	public MapView(ClueMap map) {
		this.map = map;

		setBackground(Color.gray);
		//addMouseListener(this);
		
		try {
			
			
			room_images.add(ClueMain.loadIcon("hall.map.image.png"));
			room_images.add(ClueMain.loadIcon("lounge.map.image.png"));
			room_images.add(ClueMain.loadIcon("dining.map.png"));
			room_images.add(ClueMain.loadIcon("kitchen.map.image.png"));
			room_images.add(ClueMain.loadIcon("ballroom.map.image.png"));
			room_images.add(ClueMain.loadIcon("conservatory.map.image.png"));
			room_images.add(ClueMain.loadIcon("billiard.map.image.png"));
			room_images.add(ClueMain.loadIcon("study.map.image.png"));
			room_images.add(ClueMain.loadIcon("library.map.image.png"));
			room_images.add(ClueMain.loadIcon("clue.stairs.map.image.png"));


			suspect_images.add(ClueMain.loadIcon("suspect.scarlet.jpg",scarlet_coord[1],scarlet_coord[2],scarlet_coord[3],scarlet_coord[4]));
			suspect_images.add(ClueMain.loadIcon("suspect.white.jpg",white_coord[1],white_coord[2],white_coord[3],white_coord[4]));
			suspect_images.add(ClueMain.loadIcon("suspect.plum.jpg",plum_coord[1],plum_coord[2],plum_coord[3],plum_coord[4]));
			suspect_images.add(ClueMain.loadIcon("suspect.mustard.jpg",mustard_coord[1],mustard_coord[2],mustard_coord[3],mustard_coord[4]));
			suspect_images.add(ClueMain.loadIcon("suspect.green.jpg",green_coord[1],green_coord[2],green_coord[3],green_coord[4]));
			suspect_images.add(ClueMain.loadIcon("suspect.peacock.jpg",peacock_coord[1],peacock_coord[2],peacock_coord[3],peacock_coord[4]));

			weapon_images.add(ClueMain.loadIcon("weapon.knife.jpg",knife_coord[1],knife_coord[2],knife_coord[3],knife_coord[4]));
			weapon_images.add(ClueMain.loadIcon("weapon.rope.jpg",rope_coord[1],rope_coord[2],rope_coord[3],rope_coord[4]));
			weapon_images.add(ClueMain.loadIcon("weapon.revolver.jpg",revolver_coord[1],revolver_coord[2],revolver_coord[3],revolver_coord[4]));
			weapon_images.add(ClueMain.loadIcon("weapon.wrench.jpg",wrench_coord[1],wrench_coord[2],wrench_coord[3],wrench_coord[4]));
			weapon_images.add(ClueMain.loadIcon("weapon.lead.pipe.jpg",pipe_coord[1],pipe_coord[2],pipe_coord[3],pipe_coord[4]));
			weapon_images.add(ClueMain.loadIcon("weapon.candlestick.jpg",candle_coord[1],candle_coord[2],candle_coord[3],candle_coord[4]));
			
			dice_faces.add(ClueMain.loadIcon("DiceSheet.png",1,1,50,50));
			dice_faces.add(ClueMain.loadIcon("DiceSheet.png",55,1,50,50));
			dice_faces.add(ClueMain.loadIcon("DiceSheet.png",105,1,50,50));
			dice_faces.add(ClueMain.loadIcon("DiceSheet.png",155,1,50,50));
			dice_faces.add(ClueMain.loadIcon("DiceSheet.png",205,1,50,50));
			dice_faces.add(ClueMain.loadIcon("DiceSheet.png",255,1,50,50));


		} catch (Exception e) {
			e.printStackTrace();
		}
		



	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			addMouseListener(this);
		} else {
			removeMouseListener(this);
		}
	}

	protected double getCellWidth() {
		return (double) getWidth() / (double) map.getXSize();
	}

	protected double getCellHeight() {
		return (double) getHeight() / (double) map.getYSize();
	}

	public void mouseClicked(MouseEvent e) {
		
		int x = (int) (e.getX() / getCellWidth());
		int y = (int) (e.getY() / getCellHeight());
		
		Location loc = map.getLocation(x, y);
		if (loc.getHighlighted() && !loc.equals(ClueMain.yourPlayer.getLocation())) {
			map.resetHighlights();
			ClueMain.setPlayerLocationFromMapClick(loc);
			repaint();
		}
		System.out.println("x=" + e.getX() + " y=" + e.getY() + " cell x=" + x + " cell y=" + y );
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void setMap(ClueMap map) {
		this.map = map;
		repaint();
	}
	
	public int getRoomRoomNameAtLocation(int x, int y) {
		Location loc = map.getLocation(x, y);
		return loc.getRoomId();
	}
	
	public int rollDice() {
		
		int roll1 = ClueMain.dice.roll();
		int roll2 = ClueMain.dice.roll();
		SoundEffect.DICE.play();
		
		rolledDiceImageLeft = dice_faces.get(roll1-1);
		rolledDiceImageRight = dice_faces.get(roll2-1);
		
		ClueMain.multiplayerFrame.sendDiceRollEvent(roll1, roll2);
		
		return roll1+roll2;
		
	}


	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width * map.getXSize(), width * map.getYSize());
	}

	private Ellipse2D buildCircle(int x, int y, double radius) {
		double a = x * getCellWidth() + (getCellWidth() - radius) / 2.0;
		double b = y * getCellHeight() + (getCellHeight() - radius) / 2.0;
		return new Ellipse2D.Double(a, b, radius, radius);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//paint all the black squares
		g2.setColor(new Color(44, 44, 44));
		for (int i = 0; i < map.getXSize(); i++) {
			for (int j = 0; j < map.getYSize(); j++) {
				Shape bg = new Rectangle2D.Double(i * getCellWidth(), j * getCellHeight(), getCellWidth(),getCellHeight());
				g2.fill(bg);
			}
		}
		
		
		//draw in the room images
		paintRoomImage(g2,ROOM_HALL,292,577);
		paintRoomImage(g2,ROOM_LOUNGE,0,610);
		paintRoomImage(g2,ROOM_DINING,0,289);
		paintRoomImage(g2,ROOM_KITCHEN,0,0);
		paintRoomImage(g2,ROOM_BALLROOM,254,32);
		paintRoomImage(g2,ROOM_CONSERVATORY,544,0);
		paintRoomImage(g2,ROOM_BILLIARD,576,225);
		paintRoomImage(g2,ROOM_STUDY,544,642);
		paintRoomImage(g2,ROOM_LIBRARY,544,415);
		
		//clue stairs image
		paintRoomImage(g2,9,321,321);
		//little frame border around the clue middle box
		g2.fillRect(320, 320, 160, 5);
		g2.fillRect(320, 320, 5, 222);
		g2.fillRect(475, 320, 5, 222);
		g2.fillRect(320, 538, 160, 5);


		
		//paint the tiles, doors, and highlights
		for (int i = 0; i < map.getXSize(); i++) {
			for (int j = 0; j < map.getYSize(); j++) {
				Location t = map.getLocation(i, j);
				Color tileColor = Color.blue;
				Color borderColor = Color.black;
				if (t.getBlocked()) {
					continue;
				} else if (t.getHighlighted()) {
					tileColor = new Color(0, 100, 0);
					borderColor = new Color(206, 206, 206);
					if (t.isRoom()) borderColor = new Color(128, 64, 0);
				} else if (t.isRoom()) {
					tileColor = new Color(128, 64, 0);
					borderColor = new Color(98, 49, 0);
				} else {
					tileColor = new Color(224, 224, 224);
					borderColor = new Color(206, 206, 206);
				}

				//larger tile
				g2.setColor(borderColor);
				Shape bg = new Rectangle2D.Double(i * getCellWidth(), j * getCellHeight(), getCellWidth(),
						getCellHeight());
				g2.fill(bg);

				//smaller tile
				if (tileColor != null) {
					g2.setColor(tileColor);
					Shape cell = new Rectangle2D.Double(i * getCellWidth() + 1, j * getCellHeight() + 1,
							getCellWidth() - 2, getCellHeight() - 2);
					g2.fill(cell);
				}

			}
		}


		
		//dice rolled
		if (rolledDiceImageLeft!=null) g2.drawImage(rolledDiceImageLeft,350,468,50,50,null);
		if (rolledDiceImageRight!=null) g2.drawImage(rolledDiceImageRight,403,468,50,50,null);

		
		paintPlayerLocation(g2,SUSPECT_SCARLET);
		paintPlayerLocation(g2,SUSPECT_MUSTARD);
		paintPlayerLocation(g2,SUSPECT_GREEN);
		paintPlayerLocation(g2,SUSPECT_PLUM);
		paintPlayerLocation(g2,SUSPECT_WHITE);
		paintPlayerLocation(g2,SUSPECT_PEACOCK);
		
		ClueMain.playerIconPlacement.paintIcons(g2);



		setRoomLabels(g2, "Kitchen", 50, 75, 18, getRoomTextColor(ROOM_KITCHEN));
		setRoomLabels(g2, "Ballroom", 323, 128, 18, getRoomTextColor(ROOM_BALLROOM));
		setRoomLabels(g2, "Conservatory", 610, 75, 18, getRoomTextColor(ROOM_CONSERVATORY));
		setRoomLabels(g2, "Dining Room", 50, 400, 18, getRoomTextColor(ROOM_DINING));
		setRoomLabels(g2, "Billiard Room", 621, 335, 18, getRoomTextColor(ROOM_BILLIARD));
		setRoomLabels(g2, "Library", 610, 529, 18, getRoomTextColor(ROOM_LIBRARY));
		setRoomLabels(g2, "Study", 610, 712, 18, getRoomTextColor(ROOM_STUDY));
		setRoomLabels(g2, "Hall", 370, 712, 18, getRoomTextColor(ROOM_HALL));
		setRoomLabels(g2, "Lounge", 100, 712, 18, getRoomTextColor(ROOM_LOUNGE));

		//clue logo in the middle green with black shadow font size 48
		setRoomLabels(g2, "Clue", 360, 423, 48, Color.black);
		setRoomLabels(g2, "Clue", 358, 421, 48, new Color(0, 100, 0));

		

	}
	
	void paintRoomImage(Graphics2D g, int room, int x, int y) {
		BufferedImage img = room_images.get(room);
		g.drawImage(img,x,y,img.getWidth(),img.getHeight(),null);
	}
	
	void paintPlayerLocation(Graphics2D g2, int id) {
		Location location = ClueMain.getPlayerLocation(id);
		if (location == null) return;
		
		if (ClueMain.currentTurnPlayer.getSuspectNumber()==id) {
			g2.setColor(Color.pink);
			double circleWidth = Math.min(getCellWidth() / 1.5, getCellHeight() / 1.5);
			Shape sCircle = buildCircle(location.getX(), location.getY(), circleWidth);
			g2.fill(sCircle);
		}
		
		g2.setColor(location.getColor());
		double circleWidth = Math.min(getCellWidth() / 2.0, getCellHeight() / 2.0);
		Shape sCircle = buildCircle(location.getX(), location.getY(), circleWidth);
		g2.fill(sCircle);
		
	}

	public Color getRoomTextColor(int room_id) {

		Color textColor = Color.yellow;

		for (Location location : map.getLocations()) {
			if (!location.isRoom())
				continue;
			if (location.getHighlighted() && location.getRoomId() == room_id)
				return new Color(0, 100, 0);//greenish
		}

		return textColor;
	}

	public void setRoomLabels(Graphics2D g, String label, int x, int y, int font_size, Color color) {

		Font font = new Font("Berlin Sans FB Demi", Font.PLAIN, font_size);
		g.setFont(font);
		
		g.setColor(Color.black);
		g.drawString(label, x+2, y+2);
		
		g.setColor(color);
		g.drawString(label, x, y);

	}

}
