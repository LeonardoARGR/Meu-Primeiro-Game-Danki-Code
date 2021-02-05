package com.n�osei.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.n�osei.entities.Entity;
import com.n�osei.entities.Heart;
import com.n�osei.entities.Particle;
import com.n�osei.entities.Player;
import com.n�osei.entities.Rock;
import com.n�osei.entities.RockShoot;
import com.n�osei.entities.Slime;
import com.n�osei.entities.Slingshot;
import com.n�osei.graficos.Spritesheet;
import com.n�osei.main.Game;

public class World {
	
	public static Tile[] tiles;
	
	public static int WIDTH, HEIGHT;
	
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000){
						//Ch�o
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFFFF){
						//Parede
						tiles[xx + (yy* WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					}else if(pixelAtual == 0xFF0026FF){
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixelAtual == 0xFFFF0000){
						//Inimigo
						Slime slime = new Slime(xx*16, yy*16, 16, 16, Entity.SLIME_EN);
						Game.entities.add(slime);
						Game.slimes.add(slime);
					}else if(pixelAtual == 0xFF7F3300){
						//Estilingue
						Game.entities.add(new Slingshot(xx*16, yy*16, 16, 16, Entity.SLINGSHOT_EN));
					}else if(pixelAtual == 0xFFFF3F3F){
						//Vida
						Game.entities.add(new Heart(xx*16, yy*16, 16, 16, Entity.HEART_EN));
					}else if(pixelAtual == 0xFFA0A0A0){
						//Pedrinhas
						Game.entities.add(new Rock(xx*16, yy*16, 16, 16, Entity.ROCK_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		Game.player.setX(0);
		Game.player.setY(0);
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH*HEIGHT];

		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
			}
		}

		int dir = 0;
		int xx = 0, yy = 0;

		for(int i = 0; i < 200; i++) {
			tiles[xx+yy*WIDTH] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);

			if(dir == 0) {
				//direita
				if(xx < WIDTH) {
					xx++;
				}
			}else if(dir == 1) {
				//esquerda
				if(xx > 0) {
					xx--;
				}
			}else if(dir == 2) {
				//baixo
				if(yy < HEIGHT) {
					yy++;
				}
			}else if(dir == 3) {
				//cima
				if(yy > 0) {
					yy--;
				}
			}
			
			if(Game.rand.nextInt() < 50) {
				dir = Game.rand.nextInt(4);
			}

		}
		*/

	}
	
	public static void restartGame(String level) {
		Game.rand = new Random();
		Game.entities = new ArrayList<Entity>();
		Game.slimes = new ArrayList<Slime>();
		Game.rocks = new ArrayList<RockShoot>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}
	
	public static void generateParticles(int amount, int x, int y, Color color) {
		for(int i = 0; i < amount; i++) {
			Particle particle = new Particle(x, y, 3, 3, null);
			particle.color = color;
			Game.entities.add(particle);
		}
		
	}
	
	public static boolean isFree(int x_next, int y_next) {
		int x1 = x_next / TILE_SIZE;
		int y1 = y_next / TILE_SIZE;
		
		int x2 = (x_next + TILE_SIZE-1) / TILE_SIZE;
		int y2 = y_next / TILE_SIZE;
		
		int x3 = x_next / TILE_SIZE;
		int y3 = (y_next + TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (x_next + TILE_SIZE-1) / TILE_SIZE;
		int y4 = (y_next + TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	public static boolean isFreeDynamic(int x_next, int y_next, int width, int height) {
		int x1 = x_next / TILE_SIZE;
		int y1 = y_next / TILE_SIZE;
		
		int x2 = (x_next + width - 1) / TILE_SIZE;
		int y2 = y_next / TILE_SIZE;
		
		int x3 = x_next / TILE_SIZE;
		int y3 = (y_next + height - 1) / TILE_SIZE;
		
		int x4 = (x_next + width - 1) / TILE_SIZE;
		int y4 = (y_next + height - 1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
