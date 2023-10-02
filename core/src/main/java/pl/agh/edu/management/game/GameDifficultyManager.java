package pl.agh.edu.management.game;

import java.math.BigDecimal;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;

public class GameDifficultyManager {
	private static GameDifficultyManager instance;
	private double difficultyMultiplier;
	private BigDecimal initialBalance;

	private GameDifficultyManager() {
		// Set user input here (set difficultyLevel)
		setDifficulty(DifficultyLevel.MEDIUM);
	}

	public static GameDifficultyManager getInstance() {
		if (instance == null) {
			instance = new GameDifficultyManager();
		}
		return instance;
	}

	public double getDifficultyMultiplier() {
		return difficultyMultiplier;
	}

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public void setDifficulty(DifficultyLevel difficulty) {
		this.difficultyMultiplier = JSONGameDataLoader.difficultyMultiplier.get(difficulty);
		this.initialBalance = JSONGameDataLoader.initialBalance.get(difficulty);
	}
}
