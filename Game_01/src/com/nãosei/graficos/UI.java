package com.nãosei.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.nãosei.entities.Player;
import com.nãosei.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4, 60, 8);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int)((Player.life/Player.maxLife)*60), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		if(Player.life == Player.maxLife) {
			g.drawString((int)Player.life + "/" + (int)Player.maxLife, 25, 11);
		}else{
			g.drawString("0" + (int)Player.life + "/" + (int)Player.maxLife, 25, 11);
		}
		
	}

}
