package pl.eskaxoxo.h1recoil;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;

public class H1Recoil extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, bullethole;
	Sound ar15_sound;
	BitmapFont font;
	int maxBulletHoles = 10;
	List<Bullet> bullets;

	// horizontal stuff
	float recoilResetTime = 0.2f;
	int recoilPixelChange = 20;
	boolean isHorizontalEnabled = false;
	float timer = 0;
	int clickNo = 0;

	// vertical stuff
	float vrecoilResetTime = 0.2f;
	int vrecoilPixelChange = 20;
	boolean isVerticalEnabled = false;
	int vclickNo = 0;
	float vtimer = 0;

	@Override
	public void create() {

		batch = new SpriteBatch();
		background = new Texture("background.png");
		bullethole = new Texture("bullethole.png");
		bullets = new ArrayList<Bullet>();
		font = new BitmapFont();

		// sound
		ar15_sound = Gdx.audio.newSound(Gdx.files.internal("audio/ar15.mp3"));

		// crosshair
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("crosshair3.png")), 8, 8);
		Gdx.graphics.setCursor(customCursor);

		// keyboard and mouse events

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int x, int y, int pointer, int button) {
				mouseClicked(x, y, pointer, button);
				return true;
			}

			@Override
			public boolean keyDown(int keycode) {

				switch (keycode) {
				case Keys.NUM_1:
					isHorizontalEnabled = !isHorizontalEnabled;
					break;
				case Keys.NUM_2:
					recoilPixelChange -= 1;
					break;
				case Keys.NUM_3:
					recoilPixelChange += 1;
					break;
				case Keys.NUM_4:
					recoilResetTime -= 0.01f;
					break;
				case Keys.NUM_5:
					recoilResetTime += 0.01f;
					break;

				case Keys.NUM_6:
					isVerticalEnabled = !isVerticalEnabled;
					break;

				case Keys.NUM_7:
					vrecoilPixelChange -= 1;
					break;
				case Keys.NUM_8:
					vrecoilPixelChange += 1;
					break;
				case Keys.NUM_9:
					vrecoilResetTime -= 0.01f;
					break;
				case Keys.NUM_0:
					vrecoilResetTime += 0.01f;
					break;
				}

				return true;
			}
		});

	}

	@Override
	public void render() {

		// add time
		timer += Gdx.graphics.getDeltaTime();
		vtimer += Gdx.graphics.getDeltaTime();

		// reduce recoil if timer > recoilResetTime
		if (isHorizontalEnabled && timer > recoilResetTime) {
			reduceRecoil();
			timer = 0;
			clickNo = 0;
		}

		// reduce vrecoil if vtimer > vrecoilResetTime
		if (isVerticalEnabled && vtimer > vrecoilResetTime) {
			reduceVRecoil();
			vtimer = 0;
			vclickNo = 0;
		}

		// clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0);

		// draw bullets (max maxBulletHoles)
		for (Bullet b : bullets) {
			if (bullets.size() - bullets.indexOf(b) < maxBulletHoles) {
				batch.draw(bullethole, b.getX() - 8, 450 - b.getY() - 8, 16, 16);
			}
		}

		// UI draw texts
		String formattedRecoilResetTime = String.format("%.02f", recoilResetTime);
		String formattedVRecoilResetTime = String.format("%.02f", vrecoilResetTime);
		font.setColor(Color.CHARTREUSE);
		font.draw(batch, "Horizontal recoil enabled: " + isHorizontalEnabled + " <1>", 17, 51);
		font.draw(batch, "Horizontal Recoil pixels: " + recoilPixelChange / 2 + "px <2 3>", 17, 34);
		font.draw(batch, "Horizontal Recoil reset: " + formattedRecoilResetTime + "s <4 5>", 17, 17);

		font.setColor(Color.MAGENTA);
		font.draw(batch, "Vertical recoil enabled: " + isVerticalEnabled + " <6>", 300, 51);
		font.draw(batch, "Vertical Recoil pixels: " + vrecoilPixelChange / 2 + "px <7 8>", 300, 34);
		font.draw(batch, "Vertical Recoil reset: " + formattedVRecoilResetTime + "s <9 0>", 300, 17);

		batch.end();

	}

	protected void mouseClicked(int x, int y, int pointer, int button) {
		if (isHorizontalEnabled)
			clickNo++;
		if (isVerticalEnabled)
			vclickNo++;

		// add bullethole
		bullets.add(new Bullet(x, y, 0));
		ar15_sound.play(1.0f);

		if (isHorizontalEnabled)
			addRecoil();
		if (isVerticalEnabled)
			addVRecoil();
	}

	private void reduceRecoil() {

		// all steps
		if (clickNo > 0) {

			if (clickNo == 1) {
				// right --->
				Gdx.input.setCursorPosition(Gdx.input.getX() + clickNo * recoilPixelChange, Gdx.input.getY());
			} else if (clickNo % 2 == 0) {
				// left <---
				Gdx.input.setCursorPosition(Gdx.input.getX() - (clickNo / 2) * recoilPixelChange, Gdx.input.getY());
			} else {
				// right --->
				Gdx.input.setCursorPosition(Gdx.input.getX() + (clickNo / 2 + 1) * recoilPixelChange, Gdx.input.getY());
			}

			// clickNo = 0;
		}
	}

	private void reduceVRecoil() {

		// all steps
		if (vclickNo > 0) {

			// up ^^^
			Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY() + vclickNo * vrecoilPixelChange);

			// clickNo = 0;
		}
	}

	private void addRecoil() {

		timer = 0;

		// move back to center

		if (clickNo % 2 == 0) {
			// right --->
			Gdx.input.setCursorPosition(Gdx.input.getX() + clickNo * recoilPixelChange, Gdx.input.getY());
		} else {
			// left <---
			Gdx.input.setCursorPosition(Gdx.input.getX() - clickNo * recoilPixelChange, Gdx.input.getY());
		}
	}

	private void addVRecoil() {

		vtimer = 0;

		// up ^^^^^^^^^^^^^
		Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY() - vrecoilPixelChange);

	}

	@Override
	public void dispose() {
		ar15_sound.dispose();
		bullethole.dispose();
		batch.dispose();
		background.dispose();
	}
}
