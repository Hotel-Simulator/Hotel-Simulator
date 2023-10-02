package management;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.management.game.GameDifficultyManager;

public class GameDifficultyManagerTest {

	private GameDifficultyManager gameDifficultyManager;

	@BeforeAll
	public static void setUpClass() throws ReflectiveOperationException {
		changeJSONPath();
	}

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

	private static void changeJSONPath() throws ReflectiveOperationException {
		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
