package com.tba.dropgood;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tba.dropgood.util.Game;

public class Drip implements Game {
	
	private Texture drip;
	
	private float x, y, v;
	private float w, h;

	public Drip() {
		drip = new Texture("drip.png");
		
		w = 1f;
		h = 112;
		
		v = 12;
		
		x = (float) (Math.random() * (DropGood.W - w));
		y = DropGood.H + 1;
		
	}

	@Override
	public void tick() {
		y -= v;
	}

	@Override
	public void draw(Batch b) {
		b.draw(drip, x, y, w, h);
	}
	
	public float getY() {
		return y;
	}
	
	public float getH() {
		return h;
	}
}
