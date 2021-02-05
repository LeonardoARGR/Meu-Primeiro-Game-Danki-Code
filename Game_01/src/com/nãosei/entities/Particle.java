package com.nãosei.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nãosei.main.Game;
import com.nãosei.world.Camera;

public class Particle extends Entity{
	public int lifeTime = 10;
	public int curLife = 0;
	
	public double spd = 2;
	public double dx = 0;
	public double dy = 0;
	
	public Color color;
	
	public Particle(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, sprite);
		
		dx = Game.rand.nextGaussian();
		dy = Game.rand.nextGaussian();
	}
	
	public void tick() {
		x += dx*spd;
		y += dy*spd;
		curLife++;
		if(curLife == lifeTime) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(color);
		g.fillOval(getX()-Camera.x, getY()-Camera.y, width, height);
	}

}
