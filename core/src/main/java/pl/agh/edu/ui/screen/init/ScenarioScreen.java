package pl.agh.edu.ui.screen.init;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;

public class ScenarioScreen implements Screen {
	public final Stage stage = new Stage(GraphicConfig.getViewport());
	public final Skin skin = GameSkin.getInstance();
	public final Stack stack = new Stack();
	public final GameStartContainer gameStartContainer = new GameStartContainer();

	public ScenarioScreen() {
		stack.setFillParent(true);
		stage.addActor(stack);
		gameStartContainer.background(skin.getDrawable("hotel-room"));
		stack.add(gameStartContainer);

	}

	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
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
