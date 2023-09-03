package pl.agh.edu.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

import pl.agh.edu.GameConfig;
import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.navbar.NavbarBottom;
import pl.agh.edu.actor.component.navbar.NavbarTop;
import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.actor.frame.TestFrame;

public class MainScreen implements Screen {
	private final Stage stage;
	private final Cell<BaseFrame> currentFrame;

	public MainScreen(GdxGame game) {
		FitViewport screenViewport = new FitViewport(GameConfig.RESOLUTION.getWidth(), GameConfig.RESOLUTION.getHeight());
		this.stage = new Stage(screenViewport);
		Stack stack = new Stack();
		Image background = new Image(HotelSkin.getInstance().getDrawable("night-city"));
		background.setScaling(Scaling.stretch);
		background.setAlign(Align.center);
		stack.setFillParent(true);
		stack.add(background);
		Table table = new Table();
		stack.add(table);
		stage.addActor(stack);

		table.setFillParent(true);
		this.stage.addActor(table);
		table.add();
		table.add(new NavbarTop("default")).growX();
		table.add();
		table.row();
		table.add();
		currentFrame = table.add();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", this)).growX();
		table.add();
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

}
