package pl.agh.edu.engine.hotel.dificulty;

import java.math.BigDecimal;

import pl.agh.edu.data.loader.JSONGameDataLoader;

public class GameDifficultyManager {
	public final DifficultyLevel difficultyLevel;
	public final double difficultyMultiplier;
	public final BigDecimal initialBalance;

	public GameDifficultyManager(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
		this.difficultyMultiplier = JSONGameDataLoader.difficultyMultiplier.get(difficultyLevel);
		this.initialBalance = JSONGameDataLoader.initialBalance.get(difficultyLevel);
	}
}
