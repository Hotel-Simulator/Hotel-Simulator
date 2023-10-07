package pl.agh.edu.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	private final Container<Image> blurContainer = new Container();
	private final Container<OptionFrame> optionFrameContainer = new Container();
	private final Table table = new Table();
	private boolean isOptionsOpen = false;
	private final OptionFrame optionFrame = new OptionFrame();
	private final BlurShader blurShader = new BlurShader((SpriteBatch) stage.getBatch());

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
		stack.add(blurContainer);
		stack.add(optionFrameContainer);
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
		if(isOptionsOpen || blurShader.isOpen()){
			Texture blurredBackground = blurShader.renderWithBlur(stage);
			stage.getBatch().begin();
			stage.getBatch().draw(blurredBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
			stage.getBatch().end();
		}
		stage.act();
		stage.draw();
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
	}

	private void openOptions() {
		if (isOptionsOpen)
			return;
		blurShader.startBlur();
		isOptionsOpen = true;
		optionFrameContainer.setActor(optionFrame);
	}

	private void closeOptions() {
		if (!isOptionsOpen)
			return;
		blurShader.stopBlur();
		isOptionsOpen = false;
		optionFrameContainer.clear();
	}
}
