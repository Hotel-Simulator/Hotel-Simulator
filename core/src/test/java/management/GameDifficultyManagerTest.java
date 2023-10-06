package management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.management.game.GameDifficultyManager;

public class GameDifficultyManagerTest {

	private GameDifficultyManager gameDifficultyManager;

	@BeforeEach
	public void setUp() {
		gameDifficultyManager = GameDifficultyManager.getInstance();
	}

	@Test
	public void testDifficulty() {
		// Then
		assertTrue(0.5 <= gameDifficultyManager.getDifficultyMultiplier());
		assertTrue(2 >= gameDifficultyManager.getDifficultyMultiplier());
	}
}
