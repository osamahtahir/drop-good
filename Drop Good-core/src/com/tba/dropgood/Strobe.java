package com.tba.dropgood;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tba.dropgood.util.Game;

public class Strobe implements Game {
	
	private TextureRegion strobe;
	
	private float x, y, r, rC;
	private float rot;
	
	private float rotSpeed;
	
	private int threshold;
	
	public Strobe() {
		strobe = new TextureRegion(new Texture("rays.png"));
		
		r = 700;
		
		x = -175;
		y = 0 - r/5;
		
		rot = 0;
		//first num is degrees per tick
		rotSpeed =  (float) (40 * Math.PI / 180);
	}

	@Override
	public void tick() {
		rot += rotSpeed % (2 * Math.PI);
	}

	@Override
	public void draw(Batch b) {
		b.draw(strobe, x, y, (x + r), (y + r), r * 2, r * 2, 1, 1, rot);
	}

}
