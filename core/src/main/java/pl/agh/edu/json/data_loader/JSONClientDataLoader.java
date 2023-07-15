package pl.agh.edu.json.data_loader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.model.bank.json_data.BankData;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;

public class JSONClientDataLoader {
    private static final JSONFilePath JSON_FILE_PATH = JSONFilePath.CLIENT_CONFIG;

    public static EnumMap<HotelVisitPurpose,Double> hotelVisitPurposeProbabilities;
    public static EnumMap<HotelVisitPurpose,EnumMap<RoomRank,Integer>> desiredRankProbabilities;
    public static EnumMap<HotelVisitPurpose,List<Integer>> numberOfNightsProbabilities;
    public static EnumMap<HotelVisitPurpose,List<Integer>> roomSizeProbabilities;
    public static EnumMap<HotelVisitPurpose,List<Integer>> averagePricesPerNight;



    static{
        load();
    }

    public static void load(){

    }
}
