package com.tba.dropgood;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tba.dropgood.util.Game;

public class Drop implements Game {
	
	private Texture drop;
	
	private float x, y, w, h;
	private float v, a;
	public boolean flagged, gone;
	
	public Drop(boolean isGravity) {
		drop = new Texture("drop.png");
		
		w = 75;
		h = w * 3 / 2;
		
		x = (float) (Math.random() * (DropGood.W - w));
		y = DropGood.H + h;
		
		v = isGravity ? ((float) new Random().nextInt(500)) / 1000 : ((float) new Random().nextInt(5000-3000) + 3000) / 1000;
		a = isGravity ? 0.1f : 0;
		
		flagged = false;
		gone = false;
	}

	@Override
	public void tick() {
		if(Gdx.input.justTouched() && !gone) {
			float i = DropGood.W/Gdx.graphics.getWidth() * Gdx.input.getX();
			float j = DropGood.H - DropGood.H/Gdx.graphics.getHeight() * Gdx.input.getY();
			if(i >= x + DropGood.difficulty && i <= x + DropGood.difficulty + w) {
				if(j >= y - DropGood.difficulty && j <= y + DropGood.difficulty + h) {
					flagged = true;
				}
			}
		}
		
		y -= v;
		v += a;
	}

	@Override
	public void draw(Batch b) {
		b.draw(drop, x, y, w, h);
	}
	
	public float getY() {
		return y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getW() {
		return w;
	}
	
	public float getH() {
		return h;
	}
	
	public float getV() {
		return v;
	}

}
