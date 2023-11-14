package pl.agh.edu.engine.hotel.dificulty;

import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.MEDIUM;

import java.math.BigDecimal;

import pl.agh.edu.data.loader.JSONGameDataLoader;

public class GameDifficultyManager {
	private double difficultyMultiplier;
	private BigDecimal initialBalance;

	public GameDifficultyManager() {
		setDifficulty(MEDIUM);
	}

	public GameDifficultyManager(DifficultyLevel difficultyLevel) {
		setDifficulty(difficultyLevel);
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
