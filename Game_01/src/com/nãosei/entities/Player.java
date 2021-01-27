package com.nãosei.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nãosei.graficos.Spritesheet;
import com.nãosei.graficos.UI;
import com.nãosei.main.Game;
import com.nãosei.world.Camera;
import com.nãosei.world.World;

public class Player extends Entity{
	
	public boolean right, left, up, down;
	public boolean isDamaged;
	public int left_dir = 0, right_dir = 1;
	public int up_dir = 2, down_dir = 3;
	public int dir = 0;
	public double speed = 2;
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 2;
	private boolean moved = false;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] stopPlayer;
	private BufferedImage[] damagedPlayer;
	public double life = 100, maxLife = 100;
	public int ammo = 0;
	private boolean hasGun = false;
	public boolean shoot, mouseShoot;
	public int mx, my;
	public boolean isCharging = false;
	public int phase_1 = 30, phase_2 = 60, phase_3 = 110, phase_count = 0, lastPhase = 0;
	public int damage;
	private int damagedFrames = 0;
	public int aimx, aimy;
	public double mouseAngle;

	public Player(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, sprite);
		
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		stopPlayer = new BufferedImage[4];
		damagedPlayer = new BufferedImage[3];
		
		for(int i = 0; i < 2; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(64 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(16 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(112 + (i*16), 0, 16, 16);
		}	
		for(int i = 0; i < 2; i++){
			leftPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 16, 16, 16);
		}
		
		for(int i = 0; i < 3; i++) {
			damagedPlayer[i] = Game.spritesheet.getSprite(32 + (16*i), 16, 16, 16);
		}
		
		stopPlayer[0] = Game.spritesheet.getSprite(0, 0, 16, 16);
		stopPlayer[1] = Game.spritesheet.getSprite(48, 0, 16, 16);
		stopPlayer[2] = Game.spritesheet.getSprite(96, 0, 16, 16);
		stopPlayer[3] = Game.spritesheet.getSprite(144, 0, 16, 16);
		
	}
	
	public void tick() {
		moved = false;
		mouseAngle = Math.atan2(aimy - (getY() - Camera.y), aimx - (getX() - Camera.x));
		
		if(isDamaged) {
			damagedFrames++;
			if(damagedFrames == 15) {
				isDamaged = false;
				damagedFrames = 0;
			}
		}
		
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
		
		if(isCharging) {
			//O problema tá no contador eu do passado
			phase_count++;
			if(phase_count == phase_1) {
				lastPhase = phase_1;
			}else if(phase_count == phase_2) {
				lastPhase = phase_2;
			}else if(phase_count == phase_3) {
				lastPhase = phase_3;
			}
			
			if(-0.5 < mouseAngle && mouseAngle < 0.5) {
				dir = right_dir;
			}else if(-2.5 < mouseAngle && mouseAngle < -0.5){
				dir = up_dir;
			}else if(0.5 < mouseAngle && mouseAngle < 2.5) {
				dir = down_dir;
			}else {
				dir = left_dir;
			}
		}else {
			phase_count = 0;
			if(lastPhase == phase_1) {
				isCharging = false;
				lastPhase = 0;
			}
			else if(lastPhase == phase_2) {
				mouseShoot();
				isCharging = false;
				lastPhase = 0;
				damage = 5;
			}else if(lastPhase == phase_3) {
				mouseShoot();
				isCharging = false;
				lastPhase = 0;
				damage = 10;
			}else if(lastPhase == 0) {
				isCharging = false;
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
		
		checkItens();
		initCamera();
		
		if(life<=0) {
			Game.gameState = "GAME_OVER";
			life = 0;
		}
	}
	
	public void render(Graphics g) {
		if(isDamaged == false) {
			if(moved == false) {
				if(dir == right_dir) {
					g.drawImage(stopPlayer[2], this.getX() - Camera.x, this.getY() - Camera.y, null);
					if(hasGun) {
						g.drawImage(Entity.SLINGSHOT_RIGHT, this.getX()+7 - Camera.x, this.getY() - Camera.y, null);
					}
				}else if(dir == left_dir) {
					g.drawImage(stopPlayer[3], this.getX() - Camera.x, this.getY() - Camera.y, null);
					if(hasGun) {
						g.drawImage(Entity.SLINGSHOT_LEFT, this.getX()-7 - Camera.x, this.getY() - Camera.y, null);
					}
				}
				else if(dir == up_dir) {
					g.drawImage(stopPlayer[1], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else if(moved == false && dir == down_dir) {
					g.drawImage(stopPlayer[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
					if(hasGun) {
						g.drawImage(Entity.SLINGSHOT_EN, this.getX() - Camera.x, this.getY()+5 - Camera.y, null);
					}
				}
			}
			else {
				if(dir == right_dir) {
					g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					if(hasGun) {
						g.drawImage(Entity.SLINGSHOT_RIGHT, this.getX()+7 - Camera.x, this.getY() - Camera.y, null);
					}
				}else if(dir == left_dir) {
					g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					if(hasGun) {
						g.drawImage(Entity.SLINGSHOT_LEFT, this.getX()-7 - Camera.x, this.getY() - Camera.y, null);
					}
				}
				else if(dir == up_dir) {
					g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else if(dir == down_dir) {
					g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					if(hasGun) {
						g.drawImage(Entity.SLINGSHOT_EN, this.getX() - Camera.x, this.getY()+5 - Camera.y, null);
					}
				}
			}
		}else {
			if(dir == right_dir) {
				g.drawImage(damagedPlayer[1], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}else if(dir == left_dir) {
				g.drawImage(damagedPlayer[2], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}else {
				g.drawImage(damagedPlayer[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}
	
	public void checkItens() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Heart) {
				if(Entity.isColliding(this, atual)) {
					if(life == maxLife) {
						continue;
					}
					life += 10;
					if(life > maxLife) {
						life = maxLife;
					}
					Game.entities.remove(i);
				}
			}else if(atual instanceof Rock) {
				if(Entity.isColliding(this, atual)) {
					if(ammo == 3) {
						continue;
					}
					ammo++;
					Game.entities.remove(i);
				}
			}else if(atual instanceof Slingshot) {
				if(Entity.isColliding(this, atual)) {
					hasGun = true;
					Game.entities.remove(i);
				}
			}
		}
	}
	
	public void mouseShoot() {
		if(hasGun && ammo > 0) {
			ammo--;
			
			int px = 0;
			int py = 0;
			
			if(dir == right_dir) {
				px = 16;
				py = 4;
			}else if(dir == left_dir) {
				px = -2;
				py = 4;
			}else if(dir == up_dir) {
				px = 6;
				py = 0;
			}else if(dir == down_dir){
				px = 6;
				py = 5;
			}
			
			double angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+px - Camera.x));

			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			
			RockShoot rock = new RockShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			Game.rocks.add(rock);


		}
	}
	
	public void initCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
}
