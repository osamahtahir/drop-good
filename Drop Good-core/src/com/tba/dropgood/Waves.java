package com.tba.dropgood;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Waves {
	
	private WavePoint[] waves;
	private static final float k = 0.02f;
	private static final float dampening = 0.04f, spread = 0.2f;
	public static float fillRate = 0.1f;
	public float toAdd;
	
	private static final float gravity = -0.3f;
	
	public class WaveParticle {
		public Vector2 pos, v;
		public float rotation;
		
		public WaveParticle(Vector2 pos, Vector2 v, float rotation) {
			this.pos = pos;
			this.v = v;
			this.rotation = rotation;
		}
		
		public void tick() {
			v.y += gravity;
			pos.x += v.x;
			pos.y += v.y;
			
		}
	}
	
	private class WavePoint {
		
		public float v, h, i;
		
		public WavePoint(float i) {
			this.i = i;
		}
		
		public void tick() {
			float x = h - DropGood.waterH;
			float a = -k * x - dampening * v;
			
			h += v;
			v += a;
		}
	}
	
	public Waves() {
		waves = new WavePoint[100];
		for(int i = 0; i < waves.length; i++) {
			waves[i] = new WavePoint(DropGood.W / (float) waves.length * (float) i);
		}
		toAdd = 0;
	}
	
	public void tick() {
		for(int i = 0; i < waves.length; i++) {
			waves[i].tick();
		}
		
		float[] lD = new float[waves.length];
		float[] rD = new float[waves.length];
		
		for(int j = 0; j < 8; j++) {
			for(int i = 0; i < waves.length; i++) {
				if(i > 0) {
					lD[i] = spread * (waves[i].h - waves[i-1].h);
					waves[i-1].v += lD[i];
				}
				if(i < waves.length - 1) {
					rD[i] = spread * (waves[i].h - waves[i+1].h);
					waves[i+1].v += rD[i];
				}
			}
			for(int i = 0; i < waves.length; i++) {
				if(i > 0) {
					waves[i-1].h += lD[i];
				}
				if(i < waves.length - 1) {
					waves[i+1].h += rD[i];
				}
			}
		}
		if(toAdd > 0) {
			DropGood.waterH += fillRate;
			toAdd -= fillRate;
		}
	}
	
	public void splash(float x, float v) {
		int i = Math.round(x * waves.length / DropGood.W);
		if(i == 100) {
			i = 99;
		}
		int mod = 6;
		waves[i].v = v * mod;
	}
	
	public void draw(ShapeRenderer sr) {
		Color dark = new Color(.678f, .847f, .902f, 1f);
		Color light = new Color(.831f, .922f, .949f, 1f);
		
		float scale = DropGood.W / ((float) waves.length - 1f);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);
		sr.setColor(dark);
		for(int i = 1; i < waves.length; i++) {
			Vector2 p1 = new Vector2((i - 1) * scale, waves[i-1].h);
			Vector2 p2 = new Vector2(i * scale, waves[i].h);
			Vector2 p3 = new Vector2(p2.x, 0);
			Vector2 p4 = new Vector2(p1.x, 0);
			
			sr.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, light, light, dark);
			sr.triangle(p1.x, p1.y, p3.x, p3.y, p4.x, p4.y, light, dark, dark);
		}
		sr.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}