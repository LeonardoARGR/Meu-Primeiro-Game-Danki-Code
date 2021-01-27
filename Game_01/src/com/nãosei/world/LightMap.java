package com.nãosei.world;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.nãosei.main.Game;

public class LightMap {
	
	private BufferedImage lightmap;
	public int[] pixels;
	
	private int[] lightMapPixels;
	
	public LightMap(String path) {
		try {
			lightmap = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());
		
	}
	
	public void applyLight() {
		for(int xx = 0; xx < lightmap.getWidth(); xx++) {
			for(int yy = 0; yy < lightmap.getHeight(); yy++) {
				if(lightMapPixels[xx + (yy * Game.WIDTH)] == 0xFFFFFFFF) {
					Game.pixels[xx + (yy * Game.WIDTH)] = 0;
				}
			}
		}
	}
	
	
}
