package utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import pl.agh.edu.utils.CustomBigDecimal;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CustomBigDecimalTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/utils/customBigDecimal/toString.csv")
    public void testToString(String value, String expected){
        assertEquals(expected, new CustomBigDecimal(value).toString());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/utils/customBigDecimal/roundToStringValue.csv")
    public void testRounding(String value, String expected){
        BigDecimal actual = new CustomBigDecimal(value).roundToStringValue().getValue();
        assertEquals(new BigDecimal(expected), actual);
    }


}
