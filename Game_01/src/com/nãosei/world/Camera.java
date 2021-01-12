package com.nãosei.world;

public class Camera {
	
	public static int x = 0;
	public static int y = 0;
	
	public static int clamp(int posAtual, int posMin, int posMax) {
		
		if(posAtual < posMin) {
			posAtual = posMin;
		}
		
		if(posAtual > posMax) {
			posAtual = posMax;
		}
		
		return posAtual;
	}

}
