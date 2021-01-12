package com.nãosei.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.nãosei.entities.Entity;
import com.nãosei.entities.Heart;
import com.nãosei.entities.Rock;
import com.nãosei.entities.Slime;
import com.nãosei.entities.Slingshot;
import com.nãosei.main.Game;

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
						//Chão
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
