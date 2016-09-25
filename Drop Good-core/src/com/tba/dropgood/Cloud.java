package com.tba.dropgood;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tba.dropgood.util.Game;

public class Cloud implements Game {
	
	private Texture cloud;
	
	private static float heightIncrease = 1;
	
	private float x, y, v;
	private float w, h;
	public boolean inFront;

	public Cloud(int id) {
		cloud = new Texture((id) % 11 == 0 ? "Cloud.png" : "Cloud-" + id % 11 + ".png");
		
		w = 600;
		h = 400;
		
		x = -w;
		y = DropGood.H - (id % 11 + 1) * 50 - h/4;
		v = (float) ((Math.random() * 2f + 0.1f));
		
		int i = (int) (Math.random() * 4);
		inFront = i < 3 ? true : false;
	}

	@Override
	public void tick() {
		x += v;
		if(v > 0) {
			x = x >= DropGood.W ? -w : x;
		} else {
			x = x <= -w ? DropGood.W : x;
		}
	}

	@Override
	public void draw(Batch b) {
		b.draw(this.cloud, x, y, w, h);
	}

	public void increaseH() {
		y += heightIncrease;
	}
}
