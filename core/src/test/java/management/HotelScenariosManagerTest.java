package management;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.management.hotel.HotelScenariosManager;

public class HotelScenariosManagerTest {
	private static HotelScenariosManager hotelScenariosManager;

	@BeforeAll
	public static void setUpClass() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@BeforeEach
	public void setUp() {
		hotelScenariosManager = new HotelScenariosManager(HotelType.HOTEL);
	}

	@Test
	public void testGetHotelVisitPurposeProbabilities() {
		// Then
		assertNotNull(hotelScenariosManager.getHotelVisitPurposeProbabilities());
	}

	@Test
	public void testGetCurrentDayMultiplier() {
		// When
		double currentDayMultiplier = hotelScenariosManager.getCurrentDayMultiplier();

		// Then
		assertTrue(currentDayMultiplier >= 0.0 && currentDayMultiplier <= 2.0);
	}

	@Test
	public void testHotelSetUp() {
		// Then
		assertNotNull(hotelScenariosManager.getHotelVisitPurposeProbabilities());
	}

	@Test
	public void testHotelScenariosManagerInitialization() {
		// Then
		assertNotNull(hotelScenariosManager.getHotelVisitPurposeProbabilities());
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {
		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
