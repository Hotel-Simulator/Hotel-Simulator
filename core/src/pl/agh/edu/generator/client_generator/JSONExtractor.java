package pl.agh.edu.generator.client_generator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;


import pl.agh.edu.generator.event_generator.json_data.ClientNumberModificationCyclicTemporaryEventData;
import pl.agh.edu.generator.event_generator.json_data.ClientNumberModificationRandomTemporaryEventData;
import pl.agh.edu.model.advertisement.json_data.ConstantAdvertisementData;
import pl.agh.edu.model.advertisement.ConstantAdvertisementType;
import pl.agh.edu.model.advertisement.SingleAdvertisementType;
import pl.agh.edu.model.advertisement.json_data.SingleAdvertisementData;
import pl.agh.edu.model.bank.json_data.BankData;
import pl.agh.edu.model.employee.Shift;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class JSONExtractor {
    private static final JSONParser parser = new JSONParser();
    private static final String filePath = "assets/jsons/data.json";

    public static EnumMap<HotelVisitPurpose,Double> getHotelVisitPurposeProbabilitiesFromJSON() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("hotel_visit_purpose_probabilities");
        return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                e -> e,
                e -> (double)jsonObject.get(e.toString()),
                (a,b) -> b,
                () -> new EnumMap<>(HotelVisitPurpose.class)
        ));
    }

    public static EnumMap<HotelVisitPurpose,Map<RoomRank,Integer>> getDesiredRoomRankProbabilitiesFromJSON() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("desired_rank_probabilities");
         return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                e->e,
                e-> {
                    JSONObject roomRankProbabilitiesJSONObject = (JSONObject) jsonObject.get(e.toString());
                    return Stream.of(RoomRank.values()).collect(Collectors.toMap(
                            r -> r,
                            r -> ((Long) roomRankProbabilitiesJSONObject.get(r.toString())).intValue(),
                            (a,b) -> b,
                            () -> new EnumMap<>(RoomRank.class)
                    ));
                },
                (a,b) -> b,
                () -> new EnumMap<>(HotelVisitPurpose.class)
        ));
    }


    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> getNumberOfNightsProbabilitiesFromJSON() throws IOException, ParseException {

        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("number_of_nights_probabilities");
        return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                e->e,
                e-> {
                    JSONArray roomSizeProbabilitiesJSONArray = (JSONArray) jsonObject.get(e.toString());
                    return IntStream.range(0, roomSizeProbabilitiesJSONArray.size())
                            .boxed()
                            .collect(Collectors.toMap(
                                    i -> i + 1,
                                    i -> ((Long) roomSizeProbabilitiesJSONArray.get(i)).intValue(),
                                    (a, b) -> b,
                                    HashMap::new
                            ));
                },
                (a,b) -> b,
                () -> new EnumMap<>(HotelVisitPurpose.class)
        ));
    }

    public static EnumMap<HotelVisitPurpose,Map<Integer,Integer>> getRoomSizeProbabilitiesFromJSON() throws IOException, ParseException {

        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("room_size_probabilities");
        return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                e->e,
                e-> {
                    JSONArray roomSizeProbabilitiesJSONArray = (JSONArray) jsonObject.get(e.toString());
                    return IntStream.range(0, roomSizeProbabilitiesJSONArray.size())
                            .boxed()
                            .collect(Collectors.toMap(
                                    i -> i + 1,
                                    i -> ((Long) roomSizeProbabilitiesJSONArray.get(i)).intValue(),
                                    (a, b) -> b,
                                    HashMap::new
                            ));
                },
                (a,b) -> b,
                () -> new EnumMap<>(HotelVisitPurpose.class)
        ));
    }

    public static HashMap<String, Long> getAttractivenessConstantsFromJSON() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("attractiveness_constants");

        return Stream.of(jsonObject.keySet().toArray())
                .collect(Collectors.toMap(
                        e -> (String)e,
                        e -> (Long)jsonObject.get(e),
                        (a,b) -> b,
                        HashMap::new
                ));
    }

    public static EnumMap<RoomRank,Map<Integer,Integer>> getAveragePricesPerNightFromJSON() throws IOException, ParseException {

        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("average_prices_per_nights");
        return Stream.of(RoomRank.values()).collect(Collectors.toMap(
                e->e,
                e-> {
                    JSONArray pricesJSONArray = (JSONArray) jsonObject.get(e.toString());
                    return IntStream.range(0, pricesJSONArray.size())
                            .boxed()
                            .collect(Collectors.toMap(
                                    i -> i + 1,
                                    i -> ((Long) pricesJSONArray.get(i)).intValue(),
                                    (a, b) -> b,
                                    HashMap::new
                            ));
                },
                (a,b) -> b,
                () -> new EnumMap<>(RoomRank.class)
        ));
    }

    public static double getAdvertisementMultiplier() throws IOException, ParseException {
        return (double)((JSONObject) parser.parse(new FileReader(filePath))).get("advertisement_multiplier");
    }
    public static EnumMap<SingleAdvertisementType, SingleAdvertisementData> getSingleAdvertisementDataFromJSON() throws IOException, ParseException{

        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("single_advertisement");
        double advertisementMultiplier = getAdvertisementMultiplier();

        return Stream.of(SingleAdvertisementType.values()).collect(Collectors.toMap(
                e -> e,
                e -> {
                    JSONObject data = (JSONObject) jsonObject.get(e.toString());
                    JSONObject effectivenessData = (JSONObject) data.get("effectiveness");
                    return  new SingleAdvertisementData(
                            BigDecimal.valueOf(((Long)data.get("cost_of_purchase")).doubleValue()),
                            ((Long)data.get("preparation_days")).intValue(),
                            (String)data.get("image_path"),
                            Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                                    h -> h,
                                    h -> ((Long)effectivenessData.get(h.toString())) * advertisementMultiplier,
                                    (a,b) -> b,
                                    () -> new EnumMap<>(HotelVisitPurpose.class)
                            )));
                },
                (a,b) -> a,
                () -> new EnumMap<>(SingleAdvertisementType.class)

        ));
    }

    public static EnumMap<ConstantAdvertisementType, ConstantAdvertisementData> getConstantAdvertisementDataFromJSON() throws IOException, ParseException{
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("constant_advertisement");
        double advertisementMultiplier = getAdvertisementMultiplier();
         return Stream.of(ConstantAdvertisementType.values()).collect(Collectors.toMap(
                e -> e,
                e -> {
                    JSONObject data = (JSONObject) jsonObject.get(e.toString());
                    JSONObject effectivenessData = (JSONObject) data.get("effectiveness");
                    return  new ConstantAdvertisementData(
                            BigDecimal.valueOf(((Long)data.get("cost_of_purchase")).doubleValue()),
                            BigDecimal.valueOf(((Long)data.get("cost_of_maintenance")).doubleValue()),
                            ((Long)data.get("preparation_days")).intValue(),
                            (String)data.get("image_path"),
                            Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                                    h -> h,
                                    h -> ((Long)effectivenessData.get(h.toString())) * advertisementMultiplier,
                                    (a,b) -> b,
                                    () -> new EnumMap<>(HotelVisitPurpose.class)
                            )));
                },
                 (a,b) -> a,
                 () -> new EnumMap<>(ConstantAdvertisementType.class)

        ));
    }


    public static List<BankData> getBanksFromJSON() throws IOException, ParseException{
        JSONArray jsonArray = (JSONArray)((JSONObject) parser.parse(new FileReader(filePath))).get("bank_scenarios");
        return Stream.of(jsonArray.toArray())
                .map(
                        e -> {
                            JSONObject data = (JSONObject) e;
                            return new BankData(
                                    (String) data.get("name"),
                                    Math.round((Long) data.get("loan_interest_rate")),
                                    Math.round((Long) data.get("deposit_interest_rate")),
                                    BigDecimal.valueOf((Long) data.get("account_fee"))
                            );
                        }
                )
                .collect(Collectors.toList());
    }

    public static int getMaxRoomSize() throws IOException, ParseException {
        return  ((Long)((JSONObject) parser.parse(new FileReader(filePath))).get("max_room_size")).intValue();

    }

    public static HashMap<String, Long> getMaintenanceTimesFromJSON() throws IOException, ParseException {
        HashMap<String, Long> constants = new HashMap<>();
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("maintenance_times");

        for(Object constant: jsonObject.keySet()){
            constants.put(String.valueOf(constant), (Long) jsonObject.get(constant));
        }
        return constants;
    }

    public static HashMap<String, Long> getUpgradeTimesFromJSON() throws IOException, ParseException {
        HashMap<String, Long> constants = new HashMap<>();
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("upgrade_times");

        for(Object constant: jsonObject.keySet()){
            constants.put(String.valueOf(constant), (Long) jsonObject.get(constant));
        }
        return constants;
    }

    public static LocalDate getDate(String key) throws IOException, ParseException {
        return LocalDate.parse((String)(((JSONObject) parser.parse(new FileReader(filePath))).get(key)), DateTimeFormatter.ISO_LOCAL_DATE) ;
    }

    public static List<ClientNumberModificationCyclicTemporaryEventData> getClientModificationCyclicTemporaryEventData() throws IOException, ParseException {
        JSONArray jsonArray = (JSONArray) ((JSONObject)((JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("events")).get("temporary")).get("cyclic");
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("--MM-dd")
                .parseDefaulting(ChronoField.YEAR, 0)
                .toFormatter();

        return Stream.of(jsonArray.toArray())
                .map(
                        e ->{
                            JSONObject JSONEvent = (JSONObject) e;
                            JSONObject JSONModifiers = (JSONObject) JSONEvent.get("modifiers");
                            return new ClientNumberModificationCyclicTemporaryEventData(
                                    (String) JSONEvent.get("name"),
                                    (String) JSONEvent.get("calendar_description"),
                                    LocalDate.parse((String)(JSONEvent.get("start_date")), formatter),
                                    LocalDate.parse((String)(JSONEvent.get("end_date")), formatter),
                                    Stream.of(HotelVisitPurpose.values())
                                            .collect(Collectors.toMap(
                                                    f -> f,
                                                    f -> (double) JSONModifiers.get(f.toString()),
                                                    (a, b) -> b,
                                                    () -> new EnumMap<>(HotelVisitPurpose.class)))

                            );
                        }

                )
                .collect(Collectors.toList());
    }

    public static List<ClientNumberModificationRandomTemporaryEventData> getClientNumberModificationRandomTemporaryEventData() throws IOException, ParseException {
        JSONArray jsonArray = (JSONArray) ((JSONObject)((JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("events")).get("temporary")).get("random");
        return Stream.of(jsonArray.toArray())
                .map(
                        e -> {
                            JSONObject JSONEvent = (JSONObject) e;
                            JSONObject JSONModifiers = (JSONObject) JSONEvent.get("modifiers");
                            return new ClientNumberModificationRandomTemporaryEventData(
                                    (String) JSONEvent.get("name"),
                                    (String) JSONEvent.get("calendar_description"),
                                    (String) JSONEvent.get("popup_description"),
                                    ((Long)JSONEvent.get("min_duration_days")).intValue(),
                                    ((Long)JSONEvent.get("max_duration_days")).intValue(),
                                    Stream.of(HotelVisitPurpose.values())
                                            .collect(Collectors.toMap(
                                                    f -> f,
                                                    f -> (double) JSONModifiers.get(f.toString()),
                                                    (a, b) -> b,
                                                    () -> new EnumMap<>(HotelVisitPurpose.class))),
                                    (double)JSONEvent.get("occurrence_probability"),
                                    (String) JSONEvent.get("image_path")

                                    );
                        }
                ).collect(Collectors.toList());
    }
    public static BigDecimal getMinWageFromJSON() throws IOException, ParseException {
        return BigDecimal.valueOf((Long)(((JSONObject) parser.parse(new FileReader(filePath))).get("min_wage")));
    }


    public static Map<Shift,Integer> getShiftProbabilitiesFromJSON() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject) ((JSONObject) parser.parse(new FileReader(filePath))).get("shift_probabilities");
        return Stream.of(Shift.values()).collect(Collectors.toMap(
                e -> e,
                e -> ((Long) jsonObject.get(e.toString())).intValue(),
                (a, b) -> b,
                HashMap::new
        ));
    }
    public static int getEmployeesToHireListSizeFromJSON() throws IOException, ParseException {
        return ((Long)((JSONObject) parser.parse(new FileReader(filePath))).get("employees_to_hire_list_size")).intValue();
    }

    public static HashMap<String, Integer> getHotelStartingValues() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject)((JSONObject) parser.parse(new FileReader(filePath))).get("hotel_starting_data");
        return Stream.of(jsonObject.keySet().toArray()).collect(Collectors.toMap(
                Object::toString,
                e ->((Long)jsonObject.get(e.toString())).intValue(),
                (a,b) -> b,
                HashMap::new
        ));
    }

    public static HashMap<String, LocalTime> getHotelTimes() throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject) ((JSONObject) parser.parse(new FileReader(filePath))).get("hotel_check_in_out_times");
        return Stream.of(jsonObject.keySet().toArray()).collect(Collectors.toMap(
                Object::toString,
                e -> LocalTime.parse(jsonObject.get(e.toString()).toString()),
                (a, b) -> b,
                HashMap::new
        ));
    }
    public static int getNoticePeriodInMonthsFromJSON() throws IOException, ParseException {
        return ((Long)((JSONObject) parser.parse(new FileReader(filePath))).get("notice_period_in_months")).intValue();
    }


    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(getHotelVisitPurposeProbabilitiesFromJSON());
        System.out.println(getDesiredRoomRankProbabilitiesFromJSON());
        System.out.println(getRoomSizeProbabilitiesFromJSON());
        System.out.println(getNumberOfNightsProbabilitiesFromJSON());
        System.out.println(getAttractivenessConstantsFromJSON());
        System.out.println(getAveragePricesPerNightFromJSON());
        System.out.println(getSingleAdvertisementDataFromJSON());
        System.out.println(getConstantAdvertisementDataFromJSON());
        System.out.println(getBanksFromJSON());
        System.out.println(getDate("game_start_date"));
        System.out.println(getClientModificationCyclicTemporaryEventData());
        System.out.println(getClientNumberModificationRandomTemporaryEventData());
        System.out.println(getShiftProbabilitiesFromJSON());
        System.out.println(getNoticePeriodInMonthsFromJSON());
    }


}
