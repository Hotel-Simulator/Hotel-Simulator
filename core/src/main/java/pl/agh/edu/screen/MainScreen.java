package pl.agh.edu.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.button.OptionButton;
import pl.agh.edu.actor.component.navbar.NavbarBottom;
import pl.agh.edu.actor.component.navbar.NavbarTop;
import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.actor.frame.OptionFrame;
import pl.agh.edu.actor.frame.TestFrame;
import pl.agh.edu.actor.shader.BlurShader;
import pl.agh.edu.config.GraphicConfig;

public class MainScreen implements Screen {
	private final Stage stage = GraphicConfig.stage;
	private final Cell<?> currentFrame;

	private final Skin skin = HotelSkin.getInstance();

	private final Stack stack = new Stack();
	private final Table table = new Table();
	private boolean isOptionsOpen = false;
	private final OptionFrame optionFrame = new OptionFrame();
	private final BlurShader blurShader;
	private final FrameBuffer fbo;
	private final SpriteBatch spriteBatch;
	private final Texture guiTexture;

	public MainScreen(GdxGame game) {
		Image background = new Image(skin.getDrawable("night-city"));
		background.setScaling(Scaling.stretch);
		background.setAlign(Align.center);
		stack.setFillParent(true);
		stack.add(background);
		stage.addActor(stack);

		table.setFillParent(true);
		this.stage.addActor(table);
		table.add().uniform();
		table.add(new NavbarTop("default")).growX();
		table.add(new OptionButton(this::openOptions, this::closeOptions)).uniform();
		table.row();
		table.add();
		currentFrame = table.add();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", this)).growX();
		table.add();

		stack.add(table);

		blurShader = new BlurShader(1024);
		fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 1080, false);
		spriteBatch = new SpriteBatch();
		guiTexture = new Texture("night-city.png");
	}

	public void changeFrame(BaseFrame frame) {
		currentFrame.setActor(frame).grow();
	}

	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
		changeFrame(new TestFrame("test"));
	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
		fbo.begin();
		// Render your scene here
		fbo.end();

		blurShader.renderBlur(fbo, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 5.0f);

		spriteBatch.begin();
		// Draw the blurred scene
		spriteBatch.draw(blurShader.getBlurredTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// Draw GUI elements
		spriteBatch.draw(guiTexture, Gdx.graphics.getWidth() / 2f - guiTexture.getWidth() / 2f,
				Gdx.graphics.getHeight() / 2f - guiTexture.getHeight() / 2f);
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		blurShader.dispose();
		backgroundTexture.dispose();
	}

	private void openOptions() {
		if (isOptionsOpen)
			return;
		isOptionsOpen = true;
		stack.add(optionFrame);
	}

	private void closeOptions() {
		if (!isOptionsOpen)
			return;
		isOptionsOpen = false;
		stack.removeActor(optionFrame);
	}
}
