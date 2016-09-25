package com.tba.dropgood;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.tba.dropgood.util.Delayer;
import com.tba.dropgood.util.PSTimer;
import com.tba.dropgood.util.RTimer;

public class DropGood extends ApplicationAdapter {

	public static int state = 0;

	public static int score;

	public static final boolean isGravity = true;

	public static float dropVolume = 6;

	public static final boolean DEBUG = true;
	public static final float W = 480, H = 800;
	public static Matrix4 defProj;

	public static int FPS = 60, TPS = 60;

	private static PSTimer fpsT, tpsT, tracker;
	private static RTimer dropsPS;
	private static PSTimer dripsPS;

	private static int fC, tC;
	private static int tps = 0, fps = 0;

	public static float difficulty = 12;
	private static float loseH = 100f;

	private static Delayer cloudDelay, endDelay;

	public static float waterH;

	private SpriteBatch sb;
	private ShapeRenderer sr;
	private OrthographicCamera oc;

	private BitmapFont defFont;

	private Color defColor;

	private Texture background;
	private Color backTop, backBottom;

	private Cloud[] clouds;
	private ArrayList<Drop> drops;
	private ArrayList<Drip> drips;
	private ArrayList<Waves.WaveParticle> particles;
	private Waves waves;

	private Strobe strobe;

	@Override
	public void create() {
		// change start state (for debugging)
		changeToState(5318008);

		defProj = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		fpsT = new PSTimer(FPS);
		fpsT.start();
		tpsT = new PSTimer(TPS);
		tpsT.start();
		tracker = new PSTimer(1);
		tracker.start();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		oc = new OrthographicCamera();
		oc.setToOrtho(false, W, H);

		defFont = new BitmapFont();
		defColor = sb.getColor();

		// background = new Texture("dg_background.png");
		backTop = new Color(40f / 255f, 170f / 255f, 1, 1);
		backBottom = new Color(115f / 255f, 208f / 255f, 1, 1);

		clouds = new Cloud[4];
		for (int i = 0; i < clouds.length; i++) {
			clouds[i] = new Cloud(i);
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.831f, 0.922f, 0.949f, 1);
		;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		oc.update();

		// if(tpsT.isReady())
		tick();

		sb.setProjectionMatrix(oc.combined);
		sr.setProjectionMatrix(oc.combined);

		sr.begin(ShapeType.Filled);
		sr.rect(0, 0, W, H, backTop, backTop, backBottom, backBottom);
		sr.end();

		// if(fpsT.isReady())
		draw();

		if (DEBUG) {
			sb.setProjectionMatrix(defProj);
			sb.begin();
			if (tracker.isReady()) {
				tps = tC;
				fps = fC;
				tC = 0;
				fC = 0;
			}
			defFont.draw(sb, "FPS = " + (fps - 1) + " - TPS = " + (tps - 1), 5, Gdx.graphics.getHeight() - 5);
			sb.end();
		} else {
			tC = 0;
			fC = 0;
		}
	}

	public void tick() {
		tC++;
		switch (state) {
		case 5318008:
			waves.tick();

			if(Gdx.input.isButtonPressed(Keys.ENTER))
				changeToState(420);
			break;
		case 420:
			for (Cloud c : clouds) {
				c.tick();
			}
			// strobe.tick();
			if (cloudDelay.isDone()) {
				if (dropsPS.isReady()) {
					drops.add(new Drop(isGravity));
				}
				if (dripsPS.isReady()) {
					drips.add(new Drip());
				}
				waves.tick();
				if (!drops.isEmpty()) {
					for (int i = 0; i < drops.size(); i++) {
						drops.get(i).tick();

						if (!drops.get(i).gone) {
							if (drops.get(i).getY() <= waterH) {
								dropMissed(i);
								continue;
							}
							if (drops.get(i).getY() + drops.get(i).getH() < 0) {
								drops.get(i).gone = true;
							}
							if (drops.get(i).flagged) {
								dropPopped(i);
								continue;
							}
							if (drops.get(i).gone) {
								removeDrop(i);
								continue;
							}
						}
					}
				}
				if (!drips.isEmpty()) {
					for (int i = 0; i < drips.size(); i++) {
						drips.get(i).tick();
						if (drips.get(i).getY() <= waterH) {
							drips.remove(i);
						}
					}
				}
			}
			if (Gdx.input.justTouched()) {
				float x = W / Gdx.graphics.getWidth() * Gdx.input.getX();
				float y = H / Gdx.graphics.getHeight() * (Gdx.graphics.getHeight() - Gdx.input.getY());

				if (x >= 0 && x <= W) {
					if (y >= 0 && y <= waterH) {
						waves.splash(x, 12f);
					}
				}
			}
			if (waterH >= loseH) {
				changeToState(69);
			}
			break;

		case 69:
			if (endDelay.isDone()) {
				if (Gdx.input.justTouched()) {
					float x = W / Gdx.graphics.getWidth() * Gdx.input.getX();
					float y = H / Gdx.graphics.getHeight() * (Gdx.graphics.getHeight() - Gdx.input.getY());

					if (x >= 0 && x <= W) {
						if (y >= 0 && y <= waterH) {
							waves.splash(x, 12f);
						}
					}
				}
			} else {
				for(Cloud c: clouds) {
					c.increaseH();
				}
			}
			waves.tick();
			if(Gdx.input.isButtonPressed(Keys.ENTER))
				changeToState(5318008);
			break;
		}
	}

	public void draw() {
		fC++;
		switch (state) {
		case 5318008:
			
			break;
		case 420:
			sb.begin();
			// strobe.draw(sb);
			for (Drip d : drips) {
				d.draw(sb);
			}
			for (Cloud c : clouds) {
				if (!c.inFront) {
					c.draw(sb);

				}
			}
			sb.setColor(0.831f, 0.922f, 0.949f, 1);
			for (Drop d : drops) {
				d.draw(sb);
			}
			sb.setColor(defColor);
			sb.end();

			sb.begin();
			for (Cloud c : clouds) {
				if (c.inFront) {
					c.draw(sb);

				}
			}
			sb.setColor(defColor);
			sb.end();

			waves.draw(sr);
			break;

		case 69:
			if (!endDelay.isDone()) {
				sb.begin();
				for (Cloud c : clouds) {
					if (c.inFront) {
						c.draw(sb);
					}
				}
				sb.setColor(defColor);
				sb.end();
			}

			waves.draw(sr);
			break;
		}
	}

	@Override
	public void dispose() {

	}

	public void changeToState(int state) {
		switch (DropGood.state) {
		case 5318008:
			Waves.fillRate = 0.3f;
			break;
		case 420:
			dropsPS = null;
			dripsPS = null;
			drops = null;
			drips = null;
			cloudDelay = null;
			break;
		}
		switch (state) {
		case 5318008:
			waves = new Waves();
			waterH = H-150-loseH;
			break;
		case 420:
			waterH = H - 150 - loseH;
			dropsPS = new RTimer(250, 1000);
			dropsPS.start();
			dripsPS = new PSTimer(20);
			dripsPS.start();
			drops = new ArrayList<Drop>();
			drips = new ArrayList<Drip>();
			cloudDelay = new Delayer(5);
			strobe = new Strobe();
			score = 0;
			Waves.fillRate = -10.81f;
			break;
		case 69:
			waves.toAdd += H - 150 - loseH;
			Waves.fillRate = 10.81f;
			endDelay = new Delayer(2);
			waves.splash(W, 40);
			break;
		}
		DropGood.state = state;
	}

	public void removeDrop(int drop) {
		drops.remove(drop);
	}

	public void dropPopped(int drop) {
		// animate pop
		removeDrop(drop);
		score += 64717;
		System.out.println(getScore());
	}

	public void dropMissed(int drop) {
		// animate splash

		waves.splash(drops.get(drop).getX() + drops.get(drop).getW() / 2, drops.get(drop).getV());
		drops.get(drop).gone = true;
		waves.toAdd += dropVolume;
	}

	public int getScore() {
		return score / 64717;
	}
}
