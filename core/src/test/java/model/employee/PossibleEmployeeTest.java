package model.employee;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.model.employee.*;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PossibleEmployeeTest {
    public static Stream<Arguments> providePossibleEmployees() {
        return Stream.of(
                Arguments.of(new JobOffer(Shift.MORNING, BigDecimal.valueOf(4999), TypeOfContract.AGREEMENT), JobOfferResponse.NEGATIVE),
                Arguments.of(new JobOffer(Shift.MORNING, BigDecimal.valueOf(5000), TypeOfContract.AGREEMENT), JobOfferResponse.POSITIVE),
                Arguments.of(new JobOffer(Shift.MORNING, BigDecimal.valueOf(5499), TypeOfContract.PART_TIME), JobOfferResponse.NEGATIVE),
                Arguments.of(new JobOffer(Shift.MORNING, BigDecimal.valueOf(5500), TypeOfContract.PART_TIME), JobOfferResponse.POSITIVE),
                Arguments.of(new JobOffer(Shift.EVENING, BigDecimal.valueOf(5499), TypeOfContract.AGREEMENT), JobOfferResponse.NEGATIVE),
                Arguments.of(new JobOffer(Shift.EVENING, BigDecimal.valueOf(5500), TypeOfContract.AGREEMENT), JobOfferResponse.POSITIVE),
                Arguments.of(new JobOffer(Shift.EVENING, BigDecimal.valueOf(5999), TypeOfContract.PART_TIME), JobOfferResponse.NEGATIVE),
                Arguments.of(new JobOffer(Shift.EVENING, BigDecimal.valueOf(6000), TypeOfContract.PART_TIME), JobOfferResponse.POSITIVE)
        );
    }

    @ParameterizedTest
    @MethodSource("providePossibleEmployees")
    public void jobOfferTest(JobOffer jobOffer, JobOfferResponse expected) {
        //given
        PossibleEmployee possibleEmployee = new PossibleEmployee(
                "",
                "",
                18,
                0.45,
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(6000),
                Shift.MORNING,
                TypeOfContract.AGREEMENT,
                Profession.CLEANER);
        //when
        JobOfferResponse response = possibleEmployee.offerJob(jobOffer);
        //then
        assertEquals(expected, response);
    }

}
