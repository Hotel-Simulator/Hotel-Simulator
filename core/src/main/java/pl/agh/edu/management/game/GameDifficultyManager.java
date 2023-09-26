package pl.agh.edu.management.game;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;

public class GameDifficultyManager {
	private static GameDifficultyManager instance;
	private double difficultyMultiplier;

	private GameDifficultyManager() {
		// Set user input here (set difficultyLevel)
		setDifficulty(DifficultyLevel.MEDIUM);
	}

	public static GameDifficultyManager getInstance() {
		if (instance == null) {
			synchronized (GameDifficultyManager.class) {
				if (instance == null) {
					instance = new GameDifficultyManager();
				}
			}
		}
		return instance;
	}

	public double getDifficultyMultiplier() {
		return difficultyMultiplier;
	}

	public void setDifficulty(DifficultyLevel difficulty) {
		this.difficultyMultiplier = JSONGameDataLoader.difficultyMultiplier.get(difficulty);
	}
}
