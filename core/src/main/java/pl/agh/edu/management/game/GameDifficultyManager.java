package pl.agh.edu.management.game;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;

public class GameDifficultyManager {
	private double difficultyMultiplier;

	public GameDifficultyManager(DifficultyLevel difficultyLevel) {
		setDifficulty(difficultyLevel);
	}

	public double getDifficultyMultiplier() {
		return difficultyMultiplier;
	}

	private void setDifficulty(DifficultyLevel difficulty) {
		difficultyMultiplier = JSONGameDataLoader.difficultyMultiplier.get(difficulty);
	}
}
