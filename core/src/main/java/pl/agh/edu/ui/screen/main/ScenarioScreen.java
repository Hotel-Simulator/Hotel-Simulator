package pl.agh.edu.ui.screen.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import org.apache.commons.lang3.builder.Diff;
import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.panel.DifficultyPanel;
import pl.agh.edu.ui.panel.ScenarioPanel;

import java.util.Optional;

public class ScenarioScreen implements Screen {
	public final Stage stage = new Stage(GraphicConfig.getViewport());
	public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
	public final Skin skin = GameSkin.getInstance();
	public final ScenarioPanel scenarioPanel = new ScenarioPanel(this::goToDifficultyPanel);
	public final DifficultyPanel difficultyPanel = new DifficultyPanel(this::goToScenarioPanel, this::startGame);
	public final Actor scenarioActor = scenarioPanel.getActor();
	public final Actor difficultyActor = difficultyPanel.getActor();

	public ScenarioScreen() {
		stage.getViewport().update(GraphicConfig.getResolution().WIDTH, GraphicConfig.getResolution().HEIGHT, true);
		stage.addActor(scenarioActor);
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

	public void goToDifficultyPanel() {
		stage.clear();
		stage.addActor(difficultyActor);
	}

	public void goToScenarioPanel() {
		stage.clear();
		stage.addActor(scenarioActor);
	}

	public void startGame() {
		Optional<DifficultyLevel> lvl = difficultyPanel.getSelectedDifficulty();
		Optional<HotelType> type = scenarioPanel.getSelectedScenario();
		if(lvl.isPresent()) System.out.println(lvl.get());
		if(type.isPresent()) System.out.println(type.get());
		game.setScreen(new MainScreen(game));
	}
}
