package pl.agh.edu.ui.screen.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.panel.ScenarioPanel;
import pl.agh.edu.utils.MyInputAdapter;

public class ScenarioScreen implements Screen {
	private Stage stage = new Stage(GraphicConfig.getViewport());
	public final Skin skin = GameSkin.getInstance();
	private final GdxGame game;

	public ScenarioScreen(GdxGame game) {
		this.game = game;
		stage.getViewport().update(GraphicConfig.getResolution().WIDTH, GraphicConfig.getResolution().HEIGHT, true);
		ScenarioPanel frame = new ScenarioPanel();
		stage.addActor(frame.getActor());

		InputProcessor inputMultiplexer = new MyInputAdapter(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void show() {
		// InputMultiplexer multiplexer = new InputMultiplexer();
		// multiplexer.addProcessor(stage);
		// Gdx.input.setInputProcessor(multiplexer);
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
