package management;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.MEDIUM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;

public class GameDifficultyManagerTest {

	private GameDifficultyManager gameDifficultyManager;

	@BeforeEach
	public void setUp() {
		gameDifficultyManager = new GameDifficultyManager(MEDIUM);
	}

	@Test
	public void testDifficulty() {
		// Then
		assertTrue(0.5 <= gameDifficultyManager.getDifficultyMultiplier());
		assertTrue(2 >= gameDifficultyManager.getDifficultyMultiplier());
	}
}
