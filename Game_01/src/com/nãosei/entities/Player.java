package com.nãosei.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.nãosei.graficos.Spritesheet;
import com.nãosei.main.Game;
import com.nãosei.world.Camera;
import com.nãosei.world.World;

public class Player extends Entity{
	
	public boolean right, left, up, down;
	public int right_dir = 1, left_dir = 0;
	public int up_dir = 2, down_dir = 3;
	public int dir = 0;
	public double speed = 2;
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;

	public Player(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, sprite);
		
		upPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];
		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		
		for(int i = 0; i < 3; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(48 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 3; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 3; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(96 + (i*16), 0, 16, 16);
		}	
		for(int i = 0; i < 3; i++){
			leftPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 16, 16, 16);
		}
		
	}
	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}else if(left && World.isFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed; 
		}
		
		if(up && World.isFree(this.getX(), (int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y-=speed;
		}else if(down && World.isFree(this.getX(), (int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
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
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(dir == up_dir) {
			g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(dir == down_dir) {
			g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}
