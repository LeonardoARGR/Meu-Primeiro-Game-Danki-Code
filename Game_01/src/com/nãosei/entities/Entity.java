package com.nãosei.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.nãosei.main.Game;
import com.nãosei.world.Camera;
import com.nãosei.world.Node;
import com.nãosei.world.Vector2i;

public class Entity {
	
	public static BufferedImage HEART_EN = Game.spritesheet.getSprite(144, 16, 16, 16);
	public static BufferedImage SLINGSHOT_EN = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage SLINGSHOT_RIGHT = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage SLINGSHOT_LEFT = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage ROCK_EN = Game.spritesheet.getSprite(64, 32, 16, 16);
	public static BufferedImage SLIME_EN = Game.spritesheet.getSprite(16*7, 16, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	protected int maskx;
	protected int masky;
	protected int mwidth;
	protected int mheight;
	
	protected List<Node> path;
	
	public Entity(int x, int y, int width, int heigth, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = heigth;
		this.sprite = sprite;
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = getWidth();
		this.mheight = getHeight();
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x * 16) {
					x++;
				}else if(x > target.x * 16) {
					x--;
				}
				
				if(y < target.y * 16) {
					y++;
				}else if(y > target.y * 16) {
					y--;
				}
				
				if(x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() - 1);
				}
			}
		}
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		maskx = this.maskx;
		masky = this.masky;
		mwidth = this.mwidth;
		mheight = this.mheight;
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public boolean isCollidingWithSlime(int xnext, int ynext) {
		Rectangle slimeCurrent = new Rectangle(xnext + maskx, ynext + masky, mwidth, mheight);
		for(int i = 0; i < Game.slimes.size(); i++) {
			Slime s = Game.slimes.get(i);
			if(s == this) {
				continue;
			}
			Rectangle targetSlime = new Rectangle(s.getX() + maskx, s.getY() + masky, mwidth, mheight);
			if(slimeCurrent.intersects(targetSlime)) {
				return true;
			}
		}
		
		return false;
	}
	
	
}
