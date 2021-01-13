package com.nãosei.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.nãosei.main.Game;
import com.nãosei.world.Camera;
import com.nãosei.world.World;

public class Slime extends Entity{
	
	private double speed = 0.5;
	
	private int maskx = 2;
	private int masky = 3;
	private int maskw = 12;
	private int maskh = 11;
	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 2;
	private boolean moved;
	
	private BufferedImage[] sprites;

	public Slime(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}
	
	public void tick() {
		moved = false;
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
			if(Game.rand.nextInt(100) > 90) {
				Game.player.life -= Game.rand.nextInt(5);
				System.out.println(Game.player.life);
				if(Game.player.life <= 0) {
					System.out.println("Game Over");
					System.exit(1);
				}
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
	}
	

	
	public void render(Graphics g) {
		super.render(g);
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
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
