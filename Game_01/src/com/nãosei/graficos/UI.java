package com.nãosei.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nãosei.entities.Player;
import com.nãosei.main.Game;

public class UI {
	
	private BufferedImage ammo_HUD;
	private BufferedImage[] charge_HUD;
	private BufferedImage aim_HUD;
	
	public UI() {
		ammo_HUD = Game.spritesheet.getSprite(48, 32, 16, 16);
		charge_HUD = new BufferedImage[4];
		for(int i = 0; i < 4; i++) {
			charge_HUD[i] = Game.spritesheet.getSprite(80 + (16*i), 32, 16, 16);
		}
		aim_HUD = Game.spritesheet.getSprite(144, 32, 16, 16);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4, 70, 10);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int)((Game.player.life/Game.player.maxLife)*70), 10);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 10));
		if(Game.player.life == Game.player.maxLife) {
			g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 24, 12);
		}else{
			g.drawString("0" + (int)Game.player.life + "/" + (int)Game.player.maxLife, 24, 12);
		}
		
		for(int i = 0; i < Game.player.ammo; i++) {
			int pos = i*10;
			g.drawImage(ammo_HUD, 200+pos, 1, null);
		}
		
			if(Game.player.phase_count >= 0 && Game.player.phase_count < Game.player.phase_1) {
				g.drawImage(charge_HUD[0], 205, 120, 16*2, 16*2, null);
			}else if(Game.player.phase_count < Game.player.phase_2 && Game.player.phase_count >= 20) {
				g.drawImage(charge_HUD[1], 205, 120, 16*2, 16*2, null);
			}else if(Game.player.phase_count < Game.player.phase_3 && Game.player.phase_count >= 30) {
				g.drawImage(charge_HUD[2], 205, 120, 16*2, 16*2, null);
			}else if (Game.player.phase_count >= Game.player.phase_3){
				g.drawImage(charge_HUD[3], 205, 120, 16*2, 16*2, null);
			}
		
		g.drawImage(aim_HUD, Game.player.aimx - 8, Game.player.aimy - 7, null);
		
	}

}
