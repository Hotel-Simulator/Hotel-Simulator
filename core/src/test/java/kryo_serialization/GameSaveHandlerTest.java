package kryo_serialization;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.agh.edu.engine.hotel.HotelType.CITY;
import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.EASY;

import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.Engine;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.serialization.GameSaveHandler;

public class GameSaveHandlerTest {

	private static final GameSaveHandler gameSaveHandler = GameSaveHandler.getInstance();
	private static final HotelType hotelType = CITY;
	private static final DifficultyLevel difficultyLevel = EASY;

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
}
