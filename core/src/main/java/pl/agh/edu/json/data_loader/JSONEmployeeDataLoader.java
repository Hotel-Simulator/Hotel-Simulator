package pl.agh.edu.json.data_loader;

import org.json.simple.JSONObject;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.model.employee.Shift;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

public class JSONEmployeeDataLoader {

    private static final JSONFilePath JSON_FILE_PATH = JSONFilePath.EMPLOYEE_CONFIG;

    public static BigDecimal minWage;
    public static int noticePeriodInMonths;
    public static EnumMap<Shift,Integer> shiftProbabilities;
    public static Map<String,Long> maintenanceTimes;

    static{
        load();
    }

    public static void load(){
        minWage = JSONValueUtil.getBigDecimal(
                JSONDataExtractor.extract(JSON_FILE_PATH,"min_wage",Long.class));
        noticePeriodInMonths = JSONValueUtil.getInt(
                JSONDataExtractor.extract(JSON_FILE_PATH,"notice_period_in_months",Long.class));
        shiftProbabilities = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"shift_probabilities", JSONObject.class),
                entry -> Shift.valueOf(entry.getKey().toString()),
                entry ->JSONValueUtil.getInt((Long)entry.getValue()),
                Shift.class
        );
        maintenanceTimes = JSONValueUtil.getMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"maintenance_times", JSONObject.class),
                entry -> (String) entry.getKey(),
                entry -> (Long) entry.getValue()
        );


    }
}
