package model.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.Room;
import pl.agh.edu.room_builder.Builder;

public class BuilderTest {

	@BeforeEach
	public void setUp() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@Test
	public void builderTest_IsOccupied() {
		// Given
		Builder builder = new Builder();
		Room room = new Room(RoomRank.TWO, 5);

		// When
		builder.upgradeRoom(room, 1);

		// Then
		assertTrue(builder.isOccupied());
	}

	@Test
	public void builderTest_IsNotOccupied() {
		// Given
		Builder builder = new Builder();
		Room room = new Room(RoomRank.TWO, 5);

		// When
		builder.upgradeRoom(room, 1);
		builder.finishUpgrade();

		// Then
		assertFalse(builder.isOccupied());
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
