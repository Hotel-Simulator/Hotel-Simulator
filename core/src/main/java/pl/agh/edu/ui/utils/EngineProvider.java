package pl.agh.edu.ui.utils;

import pl.agh.edu.engine.Engine;
import pl.agh.edu.ui.screen.main.MainScreenManager;

public interface EngineProvider {
	default Engine getEngine() {
		return MainScreenManager.getInstance().engine;
	}
}
