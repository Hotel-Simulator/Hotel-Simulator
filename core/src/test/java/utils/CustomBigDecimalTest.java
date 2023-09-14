package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.agh.edu.utils.CustomBigDecimal;

import java.io.File;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class CustomBigDecimalTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/utils/customBigDecimalToString.csv")
    public void testToString(String value, String printed){
        assertEquals(printed, new CustomBigDecimal(value).toString());
    }

}
