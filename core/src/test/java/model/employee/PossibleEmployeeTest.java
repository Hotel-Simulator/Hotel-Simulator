package model.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.employee.Profession.CLEANER;
import static pl.agh.edu.engine.employee.Shift.EVENING;
import static pl.agh.edu.engine.employee.Shift.MORNING;
import static pl.agh.edu.engine.employee.contract.OfferResponse.NEGATIVE;
import static pl.agh.edu.engine.employee.contract.OfferResponse.POSITIVE;
import static pl.agh.edu.engine.employee.contract.TypeOfContract.PERMANENT;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.engine.employee.EmploymentPreferences;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;

public class PossibleEmployeeTest {
	public static Stream<Arguments> providePossibleEmployees() {
		return Stream.of(
				Arguments.of(new Offer(MORNING, BigDecimal.valueOf(4999), PERMANENT), NEGATIVE),
				Arguments.of(new Offer(MORNING, BigDecimal.valueOf(5000), PERMANENT), POSITIVE),
				Arguments.of(new Offer(EVENING, BigDecimal.valueOf(5999), PERMANENT), NEGATIVE),
				Arguments.of(new Offer(EVENING, BigDecimal.valueOf(6000), PERMANENT), POSITIVE));
	}

	@ParameterizedTest
	@MethodSource("providePossibleEmployees")
	public void jobOfferTest(Offer contractOffer, OfferResponse expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(5000))
						.desiredWage(BigDecimal.valueOf(6000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();
		// When
		OfferResponse response = possibleEmployee.offerJob(contractOffer);
		// Then
		assertEquals(expected, response);
	}

}
