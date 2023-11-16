package pl.agh.edu.engine.hotel.dificulty;

import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.MEDIUM;

import java.math.BigDecimal;

import pl.agh.edu.data.loader.JSONGameDataLoader;

public class GameDifficultyManager {
	private static GameDifficultyManager instance;
	private double difficultyMultiplier;
	private BigDecimal initialBalance;

	private GameDifficultyManager() {
		// Set user input here (set difficultyLevel)
		setDifficulty(MEDIUM);
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
