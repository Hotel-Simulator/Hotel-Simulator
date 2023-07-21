package pl.agh.edu.json.data_loader;
import org.json.simple.JSONObject;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import java.util.EnumMap;
import java.util.Map;


public class JSONClientDataLoader {
    private static final JSONFilePath JSON_FILE_PATH = JSONFilePath.CLIENT_CONFIG;

    public static EnumMap<HotelVisitPurpose,Double> hotelVisitPurposeProbabilities;
    public static EnumMap<HotelVisitPurpose,EnumMap<RoomRank,Integer>> desiredRankProbabilities;
    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> numberOfNightsProbabilities;
    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> roomSizeProbabilities;
    public static EnumMap<RoomRank,Map<Integer,Integer>> averagePricesPerNight;

    private JSONClientDataLoader(){}

    static{
        load();
    }

    public static void load(){
        hotelVisitPurposeProbabilities = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"hotel_visit_purpose_probabilities",JSONObject.class),
                entry ->(Double)entry.getValue(),
                HotelVisitPurpose.class
        );


        desiredRankProbabilities = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"desired_rank_probabilities",JSONObject.class),
                entry -> JSONValueUtil.getEnumMap(
                        (JSONObject) entry.getValue(),
                        entry2 -> JSONValueUtil.getInt((Long)entry2.getValue()),
                        RoomRank.class),
                HotelVisitPurpose.class
        );


        numberOfNightsProbabilities = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"number_of_nights_probabilities",JSONObject.class),
                entry -> JSONValueUtil.getMap(
                        (JSONObject) entry.getValue(),
                        entry2 -> Integer.parseInt((String)entry2.getKey()),
                        entry2 -> JSONValueUtil.getInt((Long)entry2.getValue())
                ),
                HotelVisitPurpose.class
        );
        roomSizeProbabilities = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"room_size_probabilities",JSONObject.class),
                entry -> JSONValueUtil.getMap(
                        (JSONObject) entry.getValue(),
                        entry2 -> Integer.parseInt((String)entry2.getKey()),
                        entry2 -> JSONValueUtil.getInt((Long)entry2.getValue())
                ),
                HotelVisitPurpose.class
        );

        averagePricesPerNight = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"average_prices_per_nights",JSONObject.class),
                entry -> JSONValueUtil.getMap(
                        (JSONObject) entry.getValue(),
                        entry2 -> Integer.parseInt((String)entry2.getKey()),
                        entry2 -> JSONValueUtil.getInt((Long)entry2.getValue())
                ),
                RoomRank.class
        );

    }

}
