package com.nãosei.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nãosei.entities.Player;
import com.nãosei.main.Game;

public class UI {
	
	private BufferedImage ammo_HUD;
	
	public UI() {
		ammo_HUD = Game.spritesheet.getSprite(32, 16, 16, 16);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4, 70, 10);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int)((Player.life/Player.maxLife)*70), 10);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 10));
		if(Player.life == Player.maxLife) {
			g.drawString((int)Player.life + "/" + (int)Player.maxLife, 24, 12);
		}else{
			g.drawString("0" + (int)Player.life + "/" + (int)Player.maxLife, 24, 12);
		}
		
		for(int i = 0; i < Game.player.ammo; i++) {
			int pos = i*10;
			g.drawImage(ammo_HUD, 200+pos, 1, null);
		}
		//g.drawString("Munição: " + Game.player.ammo, 175, 12);
		
	}

}
