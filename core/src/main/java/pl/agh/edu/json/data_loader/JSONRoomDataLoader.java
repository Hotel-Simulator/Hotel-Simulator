package pl.agh.edu.json.data_loader;

import org.json.simple.JSONObject;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

import java.util.Map;

public class JSONRoomDataLoader  {

    private static final JSONFilePath JSON_FILE_PATH = JSONFilePath.ROOM_CONFIG;

    public static int maxSize;
    public static Map<String,Long> upgradeTimes;

    private JSONRoomDataLoader(){}

    static{
        load();
    }

    protected static void load(){
        maxSize = JSONValueUtil.getInt(JSONDataExtractor.extract(JSON_FILE_PATH,"max_size",Long.class));
        upgradeTimes = JSONValueUtil.getMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"upgrade_times", JSONObject.class),
                (entry -> (String)entry.getKey()),
                (entry -> (Long)entry.getValue()));

    }








}
