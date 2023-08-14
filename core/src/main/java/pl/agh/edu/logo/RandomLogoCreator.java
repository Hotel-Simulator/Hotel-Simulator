package pl.agh.edu.logo;

import java.util.Random;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import pl.agh.edu.GdxGame;

public class RandomLogoCreator implements Screen {

	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	private final GdxGame game;
	private final Stage stage;

	private Texture[] backgrounds;
	private Texture[] frames;
	private Texture[] pictures;

	private SpriteBatch spriteBatch;
	private Random random;
	private boolean chosen = false;

	public RandomLogoCreator(GdxGame game) {
		this.game = game;
		this.stage = new Stage(new ScreenViewport());
	}

	@Override
	public void show() {
		if (!chosen) {
			spriteBatch = new SpriteBatch();
			random = new Random();

			String path = "assets/img/";

			backgrounds = new Texture[]{
					new Texture(path + "background1.png"),
					new Texture(path + "background2.png"),
					new Texture(path + "background3.png")
			};
			frames = new Texture[]{
					new Texture(path + "frame1.png"),
					new Texture(path + "frame2.png"),
					new Texture(path + "frame3.png")
			};
			pictures = new Texture[]{
					new Texture(path + "picture1.png"),
					new Texture(path + "picture2.png"),
					new Texture(path + "picture3.png")
			};
		}
	}

	@Override
	public void render(float delta) {
		if (!chosen) {

			chosen = true;

			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			spriteBatch.begin();

			Texture background = getRandomTexture(backgrounds);
			spriteBatch.draw(background, 0, 0, WIDTH, HEIGHT);

			Texture frame = getRandomTexture(frames);
			float frameX = (WIDTH - frame.getWidth()) / 2f;
			float frameY = (HEIGHT - frame.getHeight()) / 2f;
			spriteBatch.draw(frame, frameX, frameY);

			Texture picture = getRandomTexture(pictures);
			float pictureX = frameX + (frame.getWidth() - picture.getWidth()) / 2f;
			float pictureY = frameY + (frame.getHeight() - picture.getHeight()) / 2f;
			spriteBatch.draw(picture, pictureX, pictureY);

			spriteBatch.end();

		}

	}

	private Texture getRandomTexture(Texture[] textures) {
		int index = random.nextInt(textures.length);
		return textures[index];
	}

	@Override
	public void resize(int width, int height) {
		Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();

		for (Texture texture : backgrounds) {
			texture.dispose();
		}
		for (Texture texture : frames) {
			texture.dispose();
		}
		for (Texture texture : pictures) {
			texture.dispose();
		}
	}
}
