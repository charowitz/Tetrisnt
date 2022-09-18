package tetrisnt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Random;

public class Panel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 700;
	static final int UNIT_SIZE = 25;
	static int DELAY = 40; //execution rate basically
	
	boolean running = false;
	boolean started = false;
	Timer timer;
	Random random;
	static int time = 0;
	static int level = 18;
	static int curLevel = 18;
	
	Color gray = new Color(82, 78, 75);
	Color orange = new Color(181, 100, 34);
	
	Color activeColor = Color.white;
	Color placedColor = new Color(181, 167, 156);
	
	
	int boardx = 20;
	int boardy = 80;
	
	public Block[][] board = new Block[22][12];
	
	int nextPiece;
	int currentPiece;
	
	int TLUX;
	int TLUY;
	
	int lines = 0;
	
	Panel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {		
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				board[i][j] = new Block();
			}
		}
		for(int j = 1; j <= 10; j++) {
			board[21][j].state = 1;
		}
		nextPiece = random.nextInt(7);
		spawnPiece();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
			if(!started) { //title screen
				g.setColor(orange);
				g.setFont((new Font("Impact", Font.PLAIN, 140)));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("TETRISN'T", (SCREEN_WIDTH - metrics.stringWidth("TETRISN'T"))/2, SCREEN_HEIGHT/3);
				g.setFont((new Font("Impact", Font.PLAIN, 50)));
				g.drawString("PRESS SPACE", (SCREEN_WIDTH - metrics.stringWidth("PRES"))/2, (int) (SCREEN_HEIGHT/1.5));			
			}
			else { //game
				this.setBackground(gray);
				g.setColor(Color.black);
				g.fillRect(boardx, boardy, 300, 600); //playing field
				g.fillRect(boardx, boardy - 60, 300, 50); // lines box
				g.fillRect(350, boardy - 60, 150, 150); //up next
				g.setFont((new Font("Impact", Font.PLAIN, 50))); //lines text
				g.setColor(Color.white);
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString(" LINES: " + lines, boardx, boardy - 15);
				//grid
				g.setColor(gray);
				for(int i = boardx; i < boardx + 300; i+=30) {
					g.drawLine(i, boardy, i, boardy + 600);
				}
				for(int j = boardy; j < boardy + 600; j+=30) {
					g.drawLine(boardx, j, boardx + 300, j);
				}
				//board contents
				for(int i = 1; i < 21; i++) {
					for(int j = 1; j < 11; j++) {
						if(board[i][j].state != 0) {
							if(board[i][j].state == 1) {
								g.setColor(placedColor);
							}
							else if(board[i][j].state == 2) {
								g.setColor(activeColor);
							}
							g.fillRect(boardy + ((j-1) * 30) - 60, boardx + ((i-1) * 30) + 60, 30, 30);
						}
						g.setColor(placedColor);
						
					}
				}
				//preview
				g.setColor(activeColor);
				for(int i = 0; i < Tetrominos.pieces[nextPiece].length; i++) {
					for(int j = 0; j < Tetrominos.pieces[nextPiece].length; j++) {
						if(currentPiece == 5) {
							if(Tetrominos.pieces[nextPiece][i][j] == 1) {
								g.fillRect(385 + (j * 30), boardy - 5 + (i * 30), 30, 30);
							}	
						}
						else {
							if(Tetrominos.pieces[nextPiece][i][j] == 1) {
								g.fillRect(370 + (j * 30), boardy - 20 + (i * 30), 30, 30);
							}
						}																	
					}
				}
			}			
		}
		else {
			gameOver(g);
		}
	}

	
	public void gameOver(Graphics g) {
		//game over text
		g.setColor(Color.red);
		g.setFont((new Font("Impact", Font.PLAIN, 75)));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("You Are Die", (SCREEN_WIDTH - metrics.stringWidth("You Are Die"))/2, SCREEN_HEIGHT/2);
		g.setColor(Color.red);
		g.setFont((new Font("Impact", Font.PLAIN, 40)));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + "▟", (SCREEN_WIDTH - metrics1.stringWidth("Score: " + 0))/2, (SCREEN_HEIGHT/2) + g.getFont().getSize() * 2);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		 if(running && started) {
			 time++;
			 if(lines > 3) {
					curLevel = 10;
				}
				if(lines > 5) {
					curLevel = 5;
				}
				if(lines > 7) {
					curLevel = 3;
				}
			 if(time%level == 0) {
				 for(int i = 1; i < 22; i++) {
					 for(int j = 1; j < 12; j++) {
						 if(board[i][j].state == 2) {
							 if(board[i+1][j].state == 1 || i>=20) {
								 place();								 
							 }							 
						 }
					 }
				 }
				 drop();
				 checkRows();
			 }			 			 
		 }
		 repaint();	
	}
	//start game functions
	public void spawnPiece() {
		TLUX = 5;
		TLUY = 0;
		currentPiece = nextPiece;
		nextPiece = random.nextInt(7);
		for(int i = 0; i < Tetrominos.pieces[currentPiece].length; i++) {
			for(int j = 0; j < Tetrominos.pieces[currentPiece].length; j++) {
				if(Tetrominos.pieces[currentPiece][i][j] == 1) {
					board[i][5 + j].state = 2;
				}
			}
		}
	}
	public void drop() {
		for(int i = 19; i >= 0; i--) {
			for(int j = 1; j < 12; j++) {
				if(board[i][j].state == 2) {
					board[i][j].state = 0;
					board[i + 1][j].state = 2;
				}
			}
		}
		TLUY++;
	}
	public void place() {
		for(int i = 1; i < 21; i++) { // search for active pieces needing to be placed
			 for(int j = 1; j < 12; j++) {
				 if(board[i][j].state == 2) {
					 board[i][j].state = 1;
				 }
			 }
		 }
		spawnPiece();
	}
	public void checkRows() {
		boolean isFull = true;
		for(int i = 1; i < 21; i++) {
			isFull = true;
			for(int j = 1; j < 11; j++) {
				if(board[i][j].state != 1) {
					isFull = false;
					break;
				}
			}
			if(isFull) {
				clearRow(i);
				lines++;
			}
		}
	}
	public void clearRow(int r) {
		for(int j = 1; j < 11; j++) {
			board[r][j].state = 0;
		}
		for(int i = r - 1; i >= 1; i--) {
			for(int j = 1; j < 11; j++) {
				if(board[i][j].state != 2 && board[i+1][j].state != 2) {
					board[i+1][j].state = board[i][j].state;						
				}							
			}
		}
	}
	public void moveRight() {
		boolean canMove = true;
		for(int i = 1; i < 21; i++) {
			for(int j = 1; j < 10; j++) {
				if(board[i][j].state == 2) {
					if(board[i][j+1].state == 1) {
						canMove = false;
					}
				}
			}
		}
		//find rightmost protrusion
		int bigJ = 0;
		int bigI = 0;
		for(int i = 1; i < 21; i++) {
			for(int j = 10; j >= 1; j--) {
				if(board[i][j].state == 2) {
					if(j > bigJ) {
						bigJ = j;
						bigI = i;
					}
				}
			}
		}
		if(bigJ <= 9 && board[bigI][bigJ+1].state != 1 && canMove) {
			if(TLUX<10) {
				TLUX++;
			}			
			for(int i = 1; i < 21; i++) {
				for(int j = 10; j >= 1; j--) {
					if(board[i][j].state == 2) {
						board[i][j].state = 0;
						board[i][j+1].state = 2;
					}
				}
			}
		}
	}
	public void moveLeft() {
		boolean canMove = true;
		for(int i = 1; i < 21; i++) {
			for(int j = 2; j < 11; j++) {
				if(board[i][j].state == 2) {
					if(board[i][j-1].state == 1) {
						canMove = false;
					}
				}
			}
		}
		//find leftmost protrusion
		int smallJ = 20;
		int smallI = 20;
		for(int i = 1; i < 21; i++) {
			for(int j = 1; j <= 10; j++) {
				if(board[i][j].state == 2) {
					if(j < smallJ) {
						smallJ = j;
						smallI = i;
					}
				}
			}
		}
		if(smallJ >= 2 && board[smallI][smallJ-1].state != 1 && canMove) {
			if(TLUX>1) {
				TLUX--;
			}
			for(int i = 1; i < 21; i++) {
				for(int j = 1; j <= 10; j++) {
					if(board[i][j].state == 2) {
						board[i][j].state = 0;
						board[i][j-1].state = 2;
					}
				}
			}
		}		
	}
	public void rotateRight() {
		int[][] rot = new int[Tetrominos.pieces[currentPiece].length][Tetrominos.pieces[currentPiece].length];
		for(int i = 0; i < Tetrominos.pieces[currentPiece].length; i++) {
			for(int j = 0; j < Tetrominos.pieces[currentPiece].length; j++) {
				if(board[i+TLUY][j+TLUX].state == 2) {
					rot[j][(Tetrominos.pieces[currentPiece].length-1)-i] = 1;
				}
			}
		}
		placeRotation(rot);
	}
	public void rotateLeft() {
		int[][] rot = new int[Tetrominos.pieces[currentPiece].length][Tetrominos.pieces[currentPiece].length];
		for(int i = 0; i < Tetrominos.pieces[currentPiece].length; i++) {
			for(int j = 0; j < Tetrominos.pieces[currentPiece].length; j++) {
				if(board[i+TLUY][j+TLUX].state == 2) {
					rot[j][(Tetrominos.pieces[currentPiece].length-1)-i] = 1;
				}
			}
		}
		placeRotation(rot);
	}
	public void placeRotation(int[][] rot) {
		boolean canPlace = true;
		for(int i = 0; i < Tetrominos.pieces[currentPiece].length; i++) {
			for(int j = 0; j < Tetrominos.pieces[currentPiece].length; j++) {
				if(TLUX + j > 10) {
					canPlace = false;
					break;
				}
				else if(board[TLUY + i][TLUX + j].state == 1) {
					canPlace = false;
					break;
				}
			}
		}
		if(canPlace) {
			for(int i = 1; i < 21; i ++) {
				for(int j = 1; j < 11; j++) {
					if(board[i][j].state == 2) { 
						board[i][j].state = 0;
					}
				}
			}
			for(int i = 0; i < Tetrominos.pieces[currentPiece].length; i++) {
				for(int j = 0; j < Tetrominos.pieces[currentPiece].length; j++) {
					if(rot[i][j] == 1) {
						board[i+TLUY][j+TLUX].state = 2;
					}
				}
			}
		}		
	}
	
	
	//end game functions	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				moveLeft();
				break;
			case KeyEvent.VK_D:
				moveRight();
				break;
			case KeyEvent.VK_UP:

				break;
			case KeyEvent.VK_S:
				if(level > 2) {
					level = 2;
				}
				break;
			case KeyEvent.VK_SPACE:
				if(!started) {
					started = true;				
				}
				break;
			case KeyEvent.VK_RIGHT:
				rotateRight();
				break;
			case KeyEvent.VK_LEFT:
				rotateLeft();
				break;
			}										
		}
		public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				
				break;
			case KeyEvent.VK_RIGHT:
				
				break;
			case KeyEvent.VK_UP:

				break;
			case KeyEvent.VK_S:
				level = curLevel;
				break;
			case KeyEvent.VK_SPACE:

				break;
			
			}
		}
	}
public class MyMouseListener implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub		
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {			
		
		}
		
	}
	class Block{
		public int state = 0; //0 = empty, 1 = placed, 2 = active
	}
	public static class Tetrominos {
		
		public static int[][] T = new int[][] {
			{0, 1, 0}, 
			{1, 1, 1}, 
			{0, 0, 0}};
		public static int[][] S = new int[][] {
			{0, 1, 1}, 
			{1, 1, 0}, 
			{0, 0, 0}};	
		public static int[][] Z = new int[][] {
			{1, 1, 0}, 
			{0, 1, 1}, 
			{0, 0, 0}};			
		public static int[][] L = new int[][] {
			{0, 0, 1}, 
			{1, 1, 1}, 
			{0, 0, 0}};		
		public static int[][] J = new int[][] {
			{1, 0, 0}, 
			{1, 1, 1}, 
			{0, 0, 0}};	
		public static int[][] O = new int[][] {
			{1, 1}, 
			{1, 1}};
		public static int[][] I = new int[][] { //only rotate right or left selectively
			{0, 0, 1, 0}, 
			{0, 0, 1, 0}, 
			{0, 0, 1, 0},
			{0, 0, 1, 0}};
			
		public static int [][][] pieces = new int[][][] {T,S,Z,L,J,O,I};							
	}

}