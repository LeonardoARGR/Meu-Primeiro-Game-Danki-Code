package com.n�osei.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.n�osei.world.Camera;

public class Heart extends Entity{

	public Heart(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, sprite);
		setMask(0, 0, 16, 16);
	}
	
	/*public void render(Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
		
	}
	*/

}
