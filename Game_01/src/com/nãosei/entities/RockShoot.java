package com.nãosei.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nãosei.main.Game;
import com.nãosei.world.Camera;

public class RockShoot extends Entity{
	
	private double dx;
	private double dy;
	private double spd = 4;
	
	private int life = 30, curLife = 0;
	
	public RockShoot(int x, int y, int width, int heigth, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, heigth, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		
		curLife++;
		if(curLife == life) {
			Game.rocks.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.darkGray);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
	
}
