package com.nãosei.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nãosei.main.Game;
import com.nãosei.world.Camera;

public class Entity {
	
	public static BufferedImage HEART_EN = Game.spritesheet.getSprite(16*4, 16, 16, 16);
	public static BufferedImage SLINGSHOT_EN = Game.spritesheet.getSprite(16*5, 16, 16, 16);
	public static BufferedImage ROCK_EN = Game.spritesheet.getSprite(16*6, 16, 16, 16);
	public static BufferedImage SLIME_EN = Game.spritesheet.getSprite(16*7, 16, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int width, int heigth, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = heigth;
		this.sprite = sprite;
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
	
}
