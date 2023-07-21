package pl.agh.edu.json.data_loader;

import org.json.simple.JSONObject;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class JSONGameDataLoader {
    private static final JSONFilePath JSON_FILE_PATH = JSONFilePath.GAME_CONFIG;

    public static LocalDate startDate;
    public static LocalDate endDate;

    public static int employeesToHireListSize;

    private JSONGameDataLoader(){}

    static{
        load();
    }

    public static void load(){
        startDate = JSONValueUtil.getLocalDate(
                JSONDataExtractor.extract(JSON_FILE_PATH,"start_date",String.class));
        endDate = JSONValueUtil.getLocalDate(
                JSONDataExtractor.extract(JSON_FILE_PATH,"end_date",String.class));
        employeesToHireListSize = JSONValueUtil.getInt(
                JSONDataExtractor.extract(JSON_FILE_PATH,"employees_to_hire_list_size",Long.class));
    }
}
