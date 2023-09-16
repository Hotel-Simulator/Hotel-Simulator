package pl.agh.edu.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.GameConfig;
import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.button.OptionButton;
import pl.agh.edu.actor.component.navbar.NavbarBottom;
import pl.agh.edu.actor.component.navbar.NavbarTop;
import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.actor.frame.OptionFrame;
import pl.agh.edu.actor.frame.TestFrame;

public class MainScreen implements Screen {
	private final Stage stage = GameConfig.stage;
	private final Cell<?> currentFrame;

	private final Skin skin = HotelSkin.getInstance();

	private final Stack stack = new Stack();
	private final Table table = new Table();

	private boolean isOptionsOpen = false;
	private final OptionFrame optionFrame = new OptionFrame();

	public MainScreen(GdxGame game) {
		Image background = new Image(skin.getDrawable("night-city"));
		background.setScaling(Scaling.stretch);
		background.setAlign(Align.center);
		stack.setFillParent(true);
		stack.add(background);
		stage.addActor(stack);

		table.setFillParent(true);
		this.stage.addActor(table);
		table.add();
		table.add(new NavbarTop("default")).growX();
		table.add(new OptionButton(this::openOptions, this::closeOptions));
		table.row();
		table.add();
		currentFrame = table.add();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", this)).growX();
		table.add();

		stack.add(table);
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
