package pl.agh.edu.json.data_loader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;

import org.json.simple.JSONObject;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

public class JSONGameDataLoader {
	private static final String JSON_FILE_PATH = JSONFilePath.GAME_INIT_DATA.get();

	public static LocalDate startDate;
	public static LocalDate endDate;

	public static int employeesToHireListSize;
	public static double possibleEmployeeRemovalProbability;

	public static double roomFaultProbability;
	public static EnumMap<DifficultyLevel, Double> difficultyMultiplier;
	public static EnumMap<DifficultyLevel, BigDecimal> initialBalance;

	private JSONGameDataLoader() {}

	static {
		load();
	}

	public static void load() {
		startDate = JSONValueUtil.getLocalDate(
				JSONDataExtractor.extract(JSON_FILE_PATH, "start_date", String.class));
		endDate = JSONValueUtil.getLocalDate(
				JSONDataExtractor.extract(JSON_FILE_PATH, "end_date", String.class));
		employeesToHireListSize = JSONValueUtil.getInt(
				JSONDataExtractor.extract(JSON_FILE_PATH, "employees_to_hire_list_size", Long.class));
		possibleEmployeeRemovalProbability = JSONDataExtractor.extract(JSON_FILE_PATH, "possible_employee_removal_probability", Double.class);
		roomFaultProbability = JSONDataExtractor.extract(JSON_FILE_PATH, "room_fault_probability", Double.class);
		difficultyMultiplier = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "difficulty_multiplier", JSONObject.class),
				entry -> (Double) entry.getValue(),
				DifficultyLevel.class);
		initialBalance = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "initial_balance", JSONObject.class),
				entry -> BigDecimal.valueOf((long) entry.getValue()),
				DifficultyLevel.class);
	}
}
