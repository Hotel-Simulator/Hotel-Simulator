package pl.agh.edu.ui.screen;

import pl.agh.edu.engine.Engine;
import pl.agh.edu.ui.screen.init.WelcomeScreenManager;
import pl.agh.edu.ui.screen.main.MainScreenManager;

public class GameScreenManager {
	private GameScreen gameScreen;

	public GameScreenManager() {}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void render(float delta) {
		gameScreen.render(delta);
	}

	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}

	public void dispose() {
		gameScreen.dispose();
	}

	public void showWelcomeScreen() {
		gameScreen = new WelcomeScreenManager().gameScreen;
	}

	public void showMainScreen(Engine engine) {
		MainScreenManager mainScreenManager = MainScreenManager.initialize(engine);
		mainScreenManager.setupUI();
		gameScreen = mainScreenManager.gameScreen;
	}

}
