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

import pl.agh.edu.engine.employee.EmployeePreferences;
import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.possible.PossibleEmployee;

public class PossibleHiredEmployeeTest {
	public static Stream<Arguments> providePossibleEmployees() {
		return Stream.of(
				Arguments.of(new EmployeeOffer(MORNING, BigDecimal.valueOf(4999), PERMANENT), NEGATIVE),
				Arguments.of(new EmployeeOffer(MORNING, BigDecimal.valueOf(5000), PERMANENT), POSITIVE),
				Arguments.of(new EmployeeOffer(EVENING, BigDecimal.valueOf(5999), PERMANENT), NEGATIVE),
				Arguments.of(new EmployeeOffer(EVENING, BigDecimal.valueOf(6000), PERMANENT), POSITIVE));
	}

	@ParameterizedTest
	@MethodSource("providePossibleEmployees")
	public void jobOfferTest(EmployeeOffer contractEmployeeOffer, OfferResponse expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(5000))
						.desiredWage(BigDecimal.valueOf(6000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();
		// When
		OfferResponse response = possibleEmployee.offerContract(contractEmployeeOffer);
		// Then
		assertEquals(expected, response);
	}

}
