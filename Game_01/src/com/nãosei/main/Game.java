package com.nãosei.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import com.nãosei.entities.Entity;
import com.nãosei.entities.Player;
import com.nãosei.entities.RockShoot;
import com.nãosei.entities.Slime;
import com.nãosei.graficos.Spritesheet;
import com.nãosei.graficos.UI;
import com.nãosei.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	//Resolução da tela
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 5;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	
	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Slime> slimes;
	public static List<RockShoot> rocks;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	public UI ui;
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true, restartGame = false;
	private int framesGameOver = 0;
	private Image cursorImage;
	private Cursor blankCursor;
	private Menu menu;
	
	public boolean saveGame = false;
	
	public Game() {
		Sound.musicBackground.play();
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		//Deixando o cursor do mouse transparente
		cursorImage = Toolkit.getDefaultToolkit().getImage("xparent.gif");
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "");
		setCursor(blankCursor);
		// Inicializando objetos
		rand = new Random();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		slimes = new ArrayList<Slime>();
		rocks = new ArrayList<RockShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
		world = new World("/level_1.png");
		ui = new UI();
		menu = new Menu();
	}
	
	public void initFrame() {
		frame = new JFrame("Game_01");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		if(gameState == "NORMAL") {
			if(saveGame) {
				saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {this.CUR_LEVEL};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Jogo salvo!");
			}
			
			restartGame = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for(int i = 0; i < rocks.size(); i++) {
				RockShoot r = rocks.get(i);
				r.tick();
			}
			
			if(slimes.size() == 0) {
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level_" + CUR_LEVEL + ".png";
				world.restartGame(newWorld);
			}
		}else if(gameState == "GAME_OVER") {
			framesGameOver++;
			if(framesGameOver == 30) {
				framesGameOver = 0;
				if(showMessageGameOver) {
					showMessageGameOver = false;
				}else {
					showMessageGameOver = true;
				}
			}
			
			if(restartGame) {
				restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level_" + CUR_LEVEL + ".png";
				world.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
			menu.tick();
		}
	}
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		/*Renderizando o Jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < rocks.size(); i++) {
			RockShoot r = rocks.get(i);
			r.render(g);
		}
		if(gameState == "NORMAL") {
			ui.render(g);
		}
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0, 220));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g2.setColor(Color.white);
			g2.setFont(new Font("arial", Font.BOLD, 20*SCALE));
			g2.drawString("Game Over", 65*SCALE, 70*SCALE);
			if(showMessageGameOver) {
				g2.setFont(new Font("arial", Font.BOLD, 10*SCALE));
				g2.drawString("Pressione ENTER para reiniciar", 45*SCALE, 100*SCALE);
			}
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		// Debugando
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				delta = 0;
				// Debugando
				frames++;
			}
			// Debugando
			if(System.currentTimeMillis() - timer >= 1000) {
				//System.out.println("FPS: "+ frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			//Andar para direita
			player.right = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			//Andar para esquerda
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			//Andar para cima
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			//Andar para baixo
			player.down = true;
			if(gameState == "MENU") {
				menu.down = true;
			}

		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Menu.esc = true;
			gameState = "MENU";
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(gameState == "NORMAL") {	
				this.saveGame = true;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			//Parar de andar para a direita
			player.right = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			//Parar de andar para a esquerda
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			//Parar de andar para cima
			player.up = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			//Parar de andar para baixo
			player.down = false;
			
		}
		/*
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			player.isCharging = false;
		}
		*/
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
	
	

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if(player.ammo > 0) {
			player.isCharging = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		player.isCharging = false;
		player.mx = (e.getX() / SCALE);
		player.my = (e.getY() / SCALE);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		player.aimx = e.getX() / SCALE;
		player.aimy = e.getY() / SCALE;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		player.aimx = e.getX() / SCALE;
		player.aimy = e.getY() / SCALE;
	}
}
