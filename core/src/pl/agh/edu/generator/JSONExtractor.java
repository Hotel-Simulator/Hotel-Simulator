package pl.agh.edu.generator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class JSONExtractor {
    private static final JSONParser parser = new JSONParser();


    public static EnumMap<HotelVisitPurpose,Integer> getHotelVisitPurposeProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose,Integer> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("assets/jsons/hotel_visit_purpose_probabilities.json"));
        for(HotelVisitPurpose hotelVisitPurpose : HotelVisitPurpose.values()){
            int probability = ((Long) jsonObject.get(hotelVisitPurpose.toString())).intValue();
            probabilities.put(hotelVisitPurpose,probability);
        }
        return probabilities;
    }

    public static EnumMap<HotelVisitPurpose,EnumMap<RoomRank,Integer>> getDesiredRoomRankProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose,EnumMap<RoomRank,Integer>> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("assets/jsons/desired_rank_probabilities.json"));
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




    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> getRoomSizeProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose, Map<Integer,Integer>> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("assets/jsons/room_size_probabilities.json"));
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

    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> getNumberOfNightsProbabilitiesFromJSON() throws IOException, ParseException {
        EnumMap<HotelVisitPurpose, Map<Integer,Integer>> probabilities = new EnumMap<>(HotelVisitPurpose.class);
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("assets/jsons/number_of_nights_probabilities.json"));
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

    public static HashMap<String, Long> getAttractivenesConstatns() throws IOException, ParseException {
        HashMap<String, Long> constants = new HashMap<>();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("assets/jsons/attractiveness_constants.json"));

        for(Object constant: jsonObject.keySet()){
            constants.put(String.valueOf(constant), (Long) jsonObject.get(constant));
        }
        return constants;
    }

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(getHotelVisitPurposeProbabilitiesFromJSON());
        System.out.println(getDesiredRoomRankProbabilitiesFromJSON());
        System.out.println(getRoomSizeProbabilitiesFromJSON());
        System.out.println(getNumberOfNightsProbabilitiesFromJSON());
        getAttractivenesConstatns();
    }


}
