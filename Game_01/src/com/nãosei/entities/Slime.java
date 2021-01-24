package com.n�osei.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.n�osei.main.Game;
import com.n�osei.world.Camera;
import com.n�osei.world.World;

public class Slime extends Entity{
	
	private double speed = 0.5;
	
	private int maskx = 2;
	private int masky = 3;
	private int maskw = 12;
	private int maskh = 11;
	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 2;
	private boolean moved;
	private int enemyLife = 10;
	private boolean isDamaged;
	private int damagedFrames = 0;
	
	private BufferedImage[] sprites;

	public Slime(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, null);
		sprites = new BufferedImage[4];
		for(int i = 0; i < 4; i++) {
			sprites[i] = Game.spritesheet.getSprite(80 + (16*i), 16, 16, 16);
		}
	}
	
	public void tick() {
		moved = false;
		
		if(isDamaged) {
			damagedFrames++;
			if(damagedFrames == 10) {
				isDamaged = false;
			}
		}
		
		if(isCollidingWithPlayer() == false) {
			if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
					&& !isColliding((int)(x+speed), this.getY())) {
				moved = true;
				x+=speed;
			}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
					&& !isColliding((int)(x-speed), this.getY())) {
				moved = true;
				x-=speed;
			}if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
					&& !isColliding(this.getX(), (int)(y+speed))) {
				moved = true;
				y+=speed;
			}else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
					&& !isColliding(this.getX(), (int)(y-speed))) {
				moved = true;
				y-=speed;
			}
		}else {
			if(Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(5);
				Game.player.isDamaged = true;
			}
		}
		
		
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				index++;
				frames = 0;
				if(index == maxIndex) {
					index = 0;
				}
			}
		}
		
		collidingIfRock();
		
		if(enemyLife <= 0) {
			destroySelf();
		}
		
	}
	
	
	public void destroySelf() {
		Game.entities.remove(this);
		Game.slimes.remove(this);
	}
	
	
	public void collidingIfRock() {
		
		for(int i = 0; i < Game.rocks.size(); i++) {
			Entity e = Game.rocks.get(i);
			if(Entity.isColliding(this, e)) {
				enemyLife -= Game.player.damage;
				isDamaged = true;
				Game.rocks.remove(i);
				return;
			}
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else {
			g.drawImage(sprites[index+2], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		/*
		g.setColor(Color.blue);
		g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
		*/
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle slimeCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player =  new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return slimeCurrent.intersects(player);
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle slimeCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for(int i = 0; i < Game.slimes.size(); i++) {
			Slime s = Game.slimes.get(i);
			if(s == this) {
				continue;
			}
			Rectangle targetSlime = new Rectangle(s.getX() + maskx, s.getY() + masky, maskw, maskh);
			if(slimeCurrent.intersects(targetSlime)) {
				return true;
			}
		}
		
		return false;
	}

}
