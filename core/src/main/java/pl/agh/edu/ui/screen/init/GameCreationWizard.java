package pl.agh.edu.ui.screen.init;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import pl.agh.edu.GdxGame;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.ui.panel.DifficultyPanel;
import pl.agh.edu.ui.panel.ScenarioPanel;
import pl.agh.edu.ui.screen.main.MainScreen;

import java.util.Optional;

public class GameCreationWizard {
    public final Table mainTable;
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    public final ScenarioPanel scenarioPanel = new ScenarioPanel(this::goToDifficultyPanel);
    public final DifficultyPanel difficultyPanel = new DifficultyPanel(this::goToScenarioPanel, this::startGame);
    public final Actor scenarioActor = scenarioPanel.getActor();
    public final Actor difficultyActor = difficultyPanel.getActor();

    public GameCreationWizard(Table mainTable){
        this.mainTable = mainTable;
        mainTable.add(scenarioActor);
    }

    public void goToDifficultyPanel() {
        mainTable.clear();
        mainTable.addActor(difficultyActor);
    }

    public void goToScenarioPanel() {
        mainTable.clear();
        mainTable.addActor(scenarioActor);
    }

    public void startGame() {
        Optional<DifficultyLevel> difficultyLevel = difficultyPanel.getSelectedDifficulty();
        Optional<HotelType> hotelType = scenarioPanel.getSelectedScenario();
        if (difficultyLevel.isPresent() && hotelType.isPresent()) {
            game.createEngine(hotelType.get(), difficultyLevel.get());
        }
        game.setScreen(new MainScreen(game));
    }
}
