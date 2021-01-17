package com.n�osei.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.n�osei.entities.Player;
import com.n�osei.main.Game;

public class UI {
	
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
		g.drawString("Muni��o: " + Game.player.ammo, 175, 12);
		
	}

}
