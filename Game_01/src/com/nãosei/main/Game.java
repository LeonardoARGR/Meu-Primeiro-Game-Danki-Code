package com.nãosei.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import com.nãosei.world.Camera;
import com.nãosei.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	//Resolução da tela
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	private final int SCALE = 5;
	
	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Slime> slimes;
	public static List<RockShoot> rocks;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	public UI ui;
	
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		// Inicializando objetos
		rand = new Random();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		slimes = new ArrayList<Slime>();
		rocks = new ArrayList<RockShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
		world = new World("/map.png");
		ui = new UI();
	}
	
	public void initFrame() {
		frame = new JFrame("Meu jogo");
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
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		for(int i = 0; i < rocks.size(); i++) {
			RockShoot r = rocks.get(i);
			r.tick();
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
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
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
				System.out.println("FPS: "+ frames);
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
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			//Andar para baixo
			player.down = true;

		}
		
		if(e.getKeyCode() == KeyEvent.VK_E) {
			player.shoot = true;
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
		player.mouseShoot = true;
		player.mx = (e.getX() / 5);
		player.my = (e.getY() / 5);
	}

	public void mouseReleased(MouseEvent e) {
		player.mouseShoot = false;
	}
}
