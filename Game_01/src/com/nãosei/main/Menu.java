package com.nãosei.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"Novo jogo", "Carregar jogo", "Sair do jogo"};
	private int currentOption = 0, maxOption = options.length - 1;
	public boolean up, down, enter, esc;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}else if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
			}else if(options[currentOption] == "Sair do jogo") {
				System.exit(1);
			}
		}
		
		if(esc) {
			esc = false;
			options[0] = "Continuar";
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0, 220));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 90));
		g.drawString("Game_01", 80*Game.SCALE, 30*Game.SCALE);
		g.setFont(new Font("arial", Font.BOLD, 50));
		for(int i = 0; i < options.length; i++) {
			g.drawString(options[i], 90*Game.SCALE, (70*Game.SCALE) + (i*120));
			g.drawString(">", 80*Game.SCALE, (70*Game.SCALE) + (currentOption*120));
		}
	}
	
}
