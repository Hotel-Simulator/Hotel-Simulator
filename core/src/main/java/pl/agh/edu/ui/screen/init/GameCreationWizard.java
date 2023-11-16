package pl.agh.edu.ui.screen.init;

import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.GdxGame;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.ui.panel.DifficultyPanel;
import pl.agh.edu.ui.panel.ScenarioPanel;
import pl.agh.edu.ui.screen.main.MainScreen;

public class GameCreationWizard {
	public final Table mainTable;
	public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
	public final ScenarioPanel scenarioPanel = new ScenarioPanel(this::goToDifficultyPanel);
	public final DifficultyPanel difficultyPanel = new DifficultyPanel(this::goToScenarioPanel, this::startGame);

	public GameCreationWizard(Table mainTable) {
		this.mainTable = mainTable;
		mainTable.add(scenarioPanel.frame);
	}

	public void goToDifficultyPanel() {
		mainTable.clearChildren();
		mainTable.addActor(difficultyPanel.frame);
	}

	public void goToScenarioPanel() {
		mainTable.clearChildren();
		mainTable.addActor(scenarioPanel.frame);
	}

	public void startGame() {
		Optional<DifficultyLevel> difficultyLevel = difficultyPanel.getSelectedDifficulty();
		Optional<HotelType> hotelType = scenarioPanel.getSelectedScenario();
		if (difficultyLevel.isPresent() && hotelType.isPresent()) {
			// Create engine
		}
		game.setScreen(new MainScreen(game));
	}
}
