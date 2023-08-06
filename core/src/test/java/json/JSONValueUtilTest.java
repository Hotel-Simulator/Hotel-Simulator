package json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JSONValueUtilTest {

    @Test
    public void getListCorrectValuesTest(){
        //given
        String path = "./src/test/resources/test.json";
        String key  = "list";
        Class<JSONArray> type = JSONArray.class;
        List<Long> expected = List.of(2L,3L,4L);
        //when
        List<Long> result = JSONValueUtil.getList(JSONDataExtractor.extract(path,key,type),Long.class::cast);
        //then
        assertEquals(expected, result);
    }

    @Test
    public void getLocalTimeCorrectValuesTest(){
        //given
        //when
        LocalTime result = JSONValueUtil.getLocalTime("00:00:00");
        //then
        assertEquals(LocalTime.MIDNIGHT, result);
    }

    @Test
    public void getLocalDateCorrectValuesTest(){
        //given
        //when
        LocalDate result = JSONValueUtil.getLocalDate("2020-01-01");
        //then
        assertEquals(LocalDate.of(2020,1,1), result);
    }

    @Test
    public void getDurationCorrectValuesTest(){
        //given
        //when
        Duration result = JSONValueUtil.getDuration(35L);
        //then
        assertEquals(Duration.ofMinutes(35), result);
    }

    @Test
    public void getIntCorrectValuesTest(){
        //given
        //when
        int result = JSONValueUtil.getInt(1L);
        //then
        assertEquals(1, result);
    }

    @Test
    public void getBigDecimalCorrectValuesTest(){
        //given
        //when
        BigDecimal result = JSONValueUtil.getBigDecimal(1L);
        //then
        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    public void getMapCorrectValuesTest(){
        //given
        String path = "./src/test/resources/test.json";
        String key  = "map";
        Class<JSONObject> type = JSONObject.class;
        Map<Long,Long> expected = Map.of(1L,1L,2L,2L,3L,3L);
        //when
        Map<Long,Long> result = JSONValueUtil.getMap(
                JSONDataExtractor.extract(path,key,type),
                entry -> Long.parseLong((String)entry.getKey()),
                entry -> (Long)entry.getValue());
        //then
        assertEquals(expected, result);
    }

    @Test
    public void getEnumMapCorrectValuesTest(){
        //given
        String path = "./src/test/resources/test.json";
        String key  = "enum_map";
        Class<JSONObject> type = JSONObject.class;
        EnumMap<HotelVisitPurpose,Long> expected = new EnumMap<>(HotelVisitPurpose.class);
        expected.put(HotelVisitPurpose.VACATION,1L);
        expected.put(HotelVisitPurpose.BUSINESS_TRIP,2L);
        expected.put(HotelVisitPurpose.REHABILITATION,3L);

        //when
        EnumMap<HotelVisitPurpose,Long> result = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(path,key,type),
                entry -> (Long)entry.getValue(),
                HotelVisitPurpose.class);
        //then
        assertEquals(expected, result);
    }








}
