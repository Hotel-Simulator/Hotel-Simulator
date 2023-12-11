package pl.agh.edu.ui.screen.init;

import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.GdxGame;
import pl.agh.edu.engine.Engine;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.serialization.GameSaveHandler;
import pl.agh.edu.ui.panel.DifficultyPanel;
import pl.agh.edu.ui.panel.ScenarioPanel;

public class GameStartContainer extends Table {
	public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
	public final ScenarioPanel scenarioPanel = new ScenarioPanel(this::goToDifficultyPanel);
	public final DifficultyPanel difficultyPanel = new DifficultyPanel(this::goToScenarioPanel, this::startGame);

	public GameStartContainer() {
		left();
		addActor(scenarioPanel.frame);
	}

	public void goToDifficultyPanel() {
		clear();
		addActor(difficultyPanel.frame);
	}

	public void goToScenarioPanel() {
		clear();
		addActor(scenarioPanel.frame);
	}

	public void startGame() {
		Optional<DifficultyLevel> difficultyLevel = difficultyPanel.getSelectedDifficulty();
		Optional<HotelType> hotelType = scenarioPanel.getSelectedScenario();
		if (difficultyLevel.isPresent() && hotelType.isPresent()) {
			Engine engine = GameSaveHandler.getInstance().startNewGame("myHotel", hotelType.get(), difficultyLevel.get());
			game.gameScreenManager.showMainScreen(engine);
		}
	}
}
