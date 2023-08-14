package pl.agh.edu.json.data_loader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;

import org.json.simple.JSONObject;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;

public class JSONEmployeeDataLoader {

	private static final String JSON_FILE_PATH = JSONFilePath.EMPLOYEE_CONFIG.get();

	public static BigDecimal minWage;
	public static int noticePeriodInMonths;
	public static EnumMap<Shift, Integer> shiftProbabilities;
	public static EnumMap<Profession, Duration> basicServiceExecutionTimes;
	public static EnumMap<Profession, Integer> professionProbabilities;
	public static EnumMap<TypeOfContract, Integer> typeOfContractProbabilities;

	private JSONEmployeeDataLoader() {}

	static {
		load();
	}

	public static void load() {
		minWage = JSONValueUtil.getBigDecimal(
				JSONDataExtractor.extract(JSON_FILE_PATH, "min_wage", Long.class));
		noticePeriodInMonths = JSONValueUtil.getInt(
				JSONDataExtractor.extract(JSON_FILE_PATH, "notice_period_in_months", Long.class));
		shiftProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "shift_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getInt((Long) entry.getValue()),
				Shift.class);
		basicServiceExecutionTimes = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "basic_service_execution_time_in_minutes", JSONObject.class),
				entry -> JSONValueUtil.getDuration((Long) entry.getValue()),
				Profession.class);
		professionProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "profession_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getInt((Long) entry.getValue()),
				Profession.class);
		typeOfContractProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "type_of_contract_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getInt((Long) entry.getValue()),
				TypeOfContract.class);
	}
}
