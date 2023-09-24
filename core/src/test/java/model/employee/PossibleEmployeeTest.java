package model.employee;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.model.employee.*;

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
				Arguments.of(new JobOffer(Shift.EVENING, BigDecimal.valueOf(6000), TypeOfContract.PART_TIME), JobOfferResponse.POSITIVE));
	}

	@ParameterizedTest
	@MethodSource("providePossibleEmployees")
	public void jobOfferTest(JobOffer jobOffer, JobOfferResponse expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(Shift.MORNING)
						.acceptableWage(BigDecimal.valueOf(5000))
						.desiredWage(BigDecimal.valueOf(6000))
						.desiredTypeOfContract(TypeOfContract.AGREEMENT)
						.build())
				.profession(Profession.CLEANER)
				.build();
		// When
		JobOfferResponse response = possibleEmployee.offerJob(jobOffer);
		// Then
		assertEquals(expected, response);
	}

}
