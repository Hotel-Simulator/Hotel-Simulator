package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.agh.edu.utils.CustomBigDecimal;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class CustomBigDecimalTest {

    @ParameterizedTest
    @CsvSource(value = {"1:1", "10:10", "11:11", "100:100", "101:101", "1000:1.00k","1001:1.00k","1010:1.01k","1100:1.10k","10000:10.0k","10001:10.0k","10010:10.0k","10100:10.1k","11000:11.0k",
    "1000000000000:1.00T","999000000000000:999T","1000000000000000:1000T"},delimiter = ':')
    public void testToString(String value, String printed){
        assertEquals(printed, new CustomBigDecimal(value).toString());
    }

}
