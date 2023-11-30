package kryo_serialization;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.agh.edu.engine.hotel.HotelType.CITY;
import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.EASY;

import java.io.File;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.Engine;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.serialization.GameSaveHandler;

public class GameSaveHandlerTest {

	private static final GameSaveHandler gameSaveHandler = GameSaveHandler.getInstance();
	private static final HotelType hotelType = CITY;
	private static final DifficultyLevel difficultyLevel = EASY;

	@BeforeAll
	static void setup() {
		changeSaveFolder();
	}

	@Test
	public void saveNewGameTest() {
		// Given
		String gameSaveName = "myFirstGame";

		// When
		Engine engine = gameSaveHandler.startNewGame(gameSaveName, hotelType, difficultyLevel);
		gameSaveHandler.saveGame(engine);

		// Then
		assertTrue(gameSaveHandler.getGameSavesNames().contains(gameSaveName));
	}

	@Test
	public void loadGameTest() {
		// Given
		String gameSaveName = "mySecondGame";

		// When
		Engine engine = gameSaveHandler.startNewGame(gameSaveName, hotelType, difficultyLevel);
		gameSaveHandler.saveGame(engine);

		// Then
		assertDoesNotThrow(() -> {
			gameSaveHandler.loadSavedGame(gameSaveName);
		});
	}

	private static void changeSaveFolder() {

		Class<?> clazz = GameSaveHandler.class;

		Field privateField;
		try {
			privateField = clazz.getDeclaredField("saveFolder");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}

		privateField.setAccessible(true);

		try {
			privateField.set(gameSaveHandler, new File("."));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		privateField.setAccessible(false);
	}
}
