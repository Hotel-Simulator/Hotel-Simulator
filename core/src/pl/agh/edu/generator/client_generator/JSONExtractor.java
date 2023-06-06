package pl.agh.edu.generator.client_generator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JSONExtractor {
    private static final JSONParser parser = new JSONParser();
    private static final String filePath = "assets/jsons/data.json";

    public static EnumMap<HotelVisitPurpose,Integer> getHotelVisitPurposeProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose,Integer> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("hotel_visit_purpose_probabilities");
        for(HotelVisitPurpose hotelVisitPurpose : HotelVisitPurpose.values()){
            int probability = ((Long) jsonObject.get(hotelVisitPurpose.toString())).intValue();
            probabilities.put(hotelVisitPurpose,probability);
        }
        return probabilities;
    }

    public static EnumMap<HotelVisitPurpose,Map<RoomRank,Integer>> getDesiredRoomRankProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose,Map<RoomRank,Integer>> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("desired_rank_probabilities");
        for(HotelVisitPurpose hotelVisitPurpose : HotelVisitPurpose.values()){
            JSONObject roomRankProbabilitiesJSONObject = (JSONObject) jsonObject.get(hotelVisitPurpose.toString());
            EnumMap<RoomRank,Integer> roomRankProbabilities = new EnumMap<>(RoomRank.class);
            for(RoomRank roomRank : RoomRank.values()){
                int probability = ((Long) roomRankProbabilitiesJSONObject.get(roomRank.toString())).intValue();
                roomRankProbabilities.put(roomRank,probability);
            }
            probabilities.put(hotelVisitPurpose,roomRankProbabilities);
        }
        return probabilities;
    }


    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> getNumberOfNightsProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose, Map<Integer,Integer>> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("number_of_nights_probabilities");
        for(HotelVisitPurpose hotelVisitPurpose : HotelVisitPurpose.values()){
            JSONArray roomSizeProbabilitiesJSONArray = (JSONArray) jsonObject.get(hotelVisitPurpose.toString());
            Map<Integer,Integer> roomSizeProbabilities = new HashMap<>();
            int roomSize = 1;
            for (Object o : roomSizeProbabilitiesJSONArray) {
                roomSizeProbabilities.put(roomSize++,((Long)o).intValue());
            }

            probabilities.put(hotelVisitPurpose,roomSizeProbabilities);
        }
        return probabilities;
    }

    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> getRoomSizeProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose, Map<Integer,Integer>> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("room_size_probabilities");
        for(HotelVisitPurpose hotelVisitPurpose : HotelVisitPurpose.values()){
            JSONArray roomSizeProbabilitiesJSONArray = (JSONArray) jsonObject.get(hotelVisitPurpose.toString());
            Map<Integer,Integer> roomSizeProbabilities = new HashMap<>();
            int roomSize = 1;
            for (Object o : roomSizeProbabilitiesJSONArray) {
                roomSizeProbabilities.put(roomSize++,((Long)o).intValue());
            }

            probabilities.put(hotelVisitPurpose,roomSizeProbabilities);
        }
        return probabilities;
    }

    public static HashMap<String, Long> getAttractivenessConstantsFromJSON() throws IOException, ParseException {
        HashMap<String, Long> constants = new HashMap<>();
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("attractiveness_constants");

        for(Object constant: jsonObject.keySet()){
            constants.put(String.valueOf(constant), (Long) jsonObject.get(constant));
        }
        return constants;
    }

    public static EnumMap<RoomRank,Map<Integer,Integer>> getAveragePricesPerNightFromJSON() throws IOException, ParseException {
        EnumMap<RoomRank, Map<Integer,Integer>> prices = new EnumMap<>(RoomRank.class);
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("average_prices_per_nights");
        for(RoomRank roomRank : RoomRank.values()){
            JSONArray pricesJSONArray = (JSONArray) jsonObject.get(roomRank.toString());
            Map<Integer,Integer> averagePricesPerNightForRoomRank = new HashMap<>();
            int roomSize = 1;
            for (Object o : pricesJSONArray) {
                averagePricesPerNightForRoomRank.put(roomSize++,((Long)o).intValue());
            }
            prices.put(roomRank,averagePricesPerNightForRoomRank);
        }
        return prices;
    }

    public static Integer getMaxRoomSize() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("max_room_size");
        Integer res = -1;
        for(Object constant: jsonObject.keySet()){
            res = (Integer) jsonObject.get(constant);
        }
        return res;
    }

    public static void main(String[] args) throws IOException, ParseException {
//        System.out.println(getHotelVisitPurposeProbabilitiesFromJSON());
//        System.out.println(getDesiredRoomRankProbabilitiesFromJSON());
//        System.out.println(getRoomSizeProbabilitiesFromJSON());
//        System.out.println(getNumberOfNightsProbabilitiesFromJSON());
//        System.out.println(getAttractivenessConstantsFromJSON());
//        System.out.println(getAveragePricesPerNightFromJSON());

//        System.out.println(getMaxRoomSize());

    }


}
