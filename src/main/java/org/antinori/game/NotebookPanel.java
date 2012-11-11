package org.antinori.game;

import static org.antinori.game.Card.NUM_ROOMS;
import static org.antinori.game.Card.NUM_SUSPECTS;
import static org.antinori.game.Card.NUM_WEAPONS;
import static org.antinori.game.Card.TYPE_ROOM;
import static org.antinori.game.Card.TYPE_SUSPECT;
import static org.antinori.game.Card.TYPE_WEAPON;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class NotebookPanel extends JPanel implements MouseListener {

	Notebook notebook = null;
	String name = "";
	public static final int cell_width = 15;
	public static final int cell_height = 25;
	public static final int cell_count_xaxis = 16;
	public static final int cell_count_yaxis = 30;
	
	Cell[][] cellArray = new Cell[cell_count_xaxis][cell_count_yaxis];
	
	public static Color WHITE = new Color(224, 224, 224);
	public static Color NAME_COLOR = new Color(77,26,26);
	public static Color CATEGORY_COLOR = new Color(88,86,63);
	public static Color ITEM_COLOR = new Color(88,71,20);
	public static Color LINES_COLOR = new Color(219,170,117);

	
	BufferedImage paper_background = ClueMain.loadIcon("antique-paper-cropped.jpg");

	public NotebookPanel() {
		
		addMouseListener(this);
		
		for (int i = 0; i < cell_count_xaxis; i++) {
			for (int j = 0; j < cell_count_yaxis; j++) {
				Cell cell = new Cell(i,j);
				cell.x = cell_width * i;
				cell.y = cell_height * j;
				cellArray[i][j] = cell;
			}
		}
	}


	public void setNotebook(Notebook notebook) {
		this.notebook = notebook;
		
		//set labels
		
		name = notebook.getPlayer().getPlayerName();
		
		setCellData(cellArray[1][2], null, "SUSPECTS", false,CATEGORY_COLOR);
		
		for (int i=0;i<NUM_SUSPECTS;i++) {
			Card card = new Card(TYPE_SUSPECT,i);
			setCellData(cellArray[1][3+i], null, card.toString(), false, ITEM_COLOR);
			setCellData(cellArray[13][3+i], card, null, true, ITEM_COLOR);
		}
		
		setCellData(cellArray[1][10], null, "WEAPONS", false,CATEGORY_COLOR);
		
		for (int i=0;i<NUM_WEAPONS;i++) {
			Card card = new Card(TYPE_WEAPON,i);
			setCellData(cellArray[1][11+i], null, card.toString(), false, ITEM_COLOR);
			setCellData(cellArray[13][11+i], card, null, true, ITEM_COLOR);
		}
		
		setCellData(cellArray[1][18], null, "ROOMS", false,CATEGORY_COLOR);
		
		for (int i=0;i<NUM_ROOMS;i++) {
			Card card = new Card(TYPE_ROOM,i);
			setCellData(cellArray[1][19+i], null, card.toString(), false, ITEM_COLOR);
			setCellData(cellArray[13][19+i], card, null, true, ITEM_COLOR);
		}
		
		
		
		repaint();

		
	}
	
	public void setBystanderIndicator(boolean flag) {
		setCellData(cellArray[1][cell_count_yaxis-1], null, flag?"Bystanding":"", false, Color.red);
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		int x = (int) (e.getX() / cell_width);
		int y = (int) (e.getY() / cell_height);
		if (x >= cell_count_xaxis || x < 0) return;
		if (y >= cell_count_yaxis || y < 0) return;
		
		//set toggled
		if (cellArray[x][y].isButton() && !notebook.isCardInHand(cellArray[x][y].card)) {
			notebook.setToggled(cellArray[x][y].card);
			SoundEffect.BUTTON.play();
		}
		
		System.out.println("x=" + e.getX() + " y=" + e.getY() + " cell x=" + x + " cell y=" + y + " card=" + cellArray[x][y].card);
		repaint();
	
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		int x = (int) (e.getX() / cell_width);
		int y = (int) (e.getY() / cell_height);
		if (x >= cell_count_xaxis || x < 0) return;
		if (y >= cell_count_yaxis || y < 0) return;
		if (cellArray[x][y].isButton()) {
			 Cursor cursor = Cursor.getDefaultCursor();
		     cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR); 
		     setCursor(cursor);
		}
	}

	public void mouseExited(MouseEvent e) {
		int x = (int) (e.getX() / cell_width);
		int y = (int) (e.getY() / cell_height);
		if (x >= cell_count_xaxis || x < 0) return;
		if (y >= cell_count_yaxis || y < 0) return;
		if (cellArray[x][y].isButton()) {
			 Cursor cursor = Cursor.getDefaultCursor();
		     cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR); 
		     setCursor(cursor);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(240,800);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.drawImage(paper_background,0,0,null);
		
		
		//draw players name in a box
		g.setColor(new Color(246,231,198));
		g.fillRect(10, 10, 150, 40);
		g.setColor(new Color(243,227,175));
		g.fillRect(15, 15, 140, 30);
		g.setFont(ClueMain.FONT_24);
		g.setColor(NAME_COLOR);
		g.drawString(name, 24, 37);

		for (int i = 0; i < cell_count_xaxis; i++) {
			for (int j = 0; j < cell_count_yaxis; j++) {
				Cell cell = cellArray[i][j];
			
				if (cell.getText() != null) {
			        g.setFont(ClueMain.FONT_18);
			        g.setColor(cell.color);
			        g.drawString(cell.getText(), cell.x, cell.y + cell_height);
				}
	
				
				if (cell.isButton()) {
					double big_radius = Math.min(cell_width / .8, cell_height / .8);
					g2.setColor(Color.black);
					Shape big = buildCircle(i, j, big_radius);
					g2.fill(big);
					
					double small_radius = Math.min(cell_width / 1.1, cell_height / 1.1);
					if (notebook.isCardInHand(cell.card)) {
						g2.setColor(Color.blue);
					} else if (notebook.isCardToggled(cell.card)) {
						g2.setColor(Color.red);
					} else {
						g2.setColor(WHITE);
					}

					Shape small = buildCircle(i, j, small_radius);
					g2.fill(small);
				}
			}

		}
		
		//draw grid lines
		g2.setColor(LINES_COLOR);
		g2.drawLine(cell_width*12, cell_height*3+3, cell_width*12, cell_height*28+3);//vertical line
		
		//g2.drawLine(0,cell_height*cell_count_yaxis,cell_width*cell_count_xaxis,cell_height*cell_count_yaxis);//bottom

		
		for (int i=0;i<=NUM_SUSPECTS;i++) {
			g2.drawLine(0, cell_height*(3+i)+3, cell_width*cell_count_xaxis, cell_height*(3+i)+3);
		}
		
		
		for (int i=0;i<=NUM_WEAPONS;i++) {
			g2.drawLine(0, cell_height*(11+i)+3, cell_width*cell_count_xaxis, cell_height*(11+i)+3);
		}

		
		for (int i=0;i<=NUM_ROOMS;i++) {
			g2.drawLine(0, cell_height*(19+i)+3, cell_width*cell_count_xaxis, cell_height*(19+i)+3);
		}

		

	}
	
	void setCellData(Cell cell, Card card, String text, boolean isButton, Color color) {
		cell.label = text;
		cell.isButton = isButton;
		cell.color = color;
		cell.card = card;
	}

	Ellipse2D buildCircle(int x, int y, double radius) {
		int offset = 3;//offset a little from the lines we drew
		double a = x * cell_width + (cell_width - radius) / 2.0;
		double b = y * cell_height + (cell_height - radius) / 2.0;
		return new Ellipse2D.Double(a + offset, b + offset, radius, radius);
	}
	

	class Cell {
		
		Card card = null;
		int x;
		int y;
		boolean isButton = false;
		String label = null;
		Color color = WHITE;
		
		Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		void setButton() {
			this.isButton = true;
		}
		
		boolean isButton() {
			return isButton;
		}
		
		String getText() {
			return label;
		}

	}

}
