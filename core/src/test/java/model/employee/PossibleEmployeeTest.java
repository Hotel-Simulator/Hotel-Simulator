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
				Arguments.of(new ContractOffer(Shift.MORNING, BigDecimal.valueOf(4999), TypeOfContract.PERMANENT), ContractOfferResponse.NEGATIVE),
				Arguments.of(new ContractOffer(Shift.MORNING, BigDecimal.valueOf(5000), TypeOfContract.PERMANENT), ContractOfferResponse.POSITIVE),
				Arguments.of(new ContractOffer(Shift.EVENING, BigDecimal.valueOf(5999), TypeOfContract.PERMANENT), ContractOfferResponse.NEGATIVE),
				Arguments.of(new ContractOffer(Shift.EVENING, BigDecimal.valueOf(6000), TypeOfContract.PERMANENT), ContractOfferResponse.POSITIVE));
	}

	@ParameterizedTest
	@MethodSource("providePossibleEmployees")
	public void jobOfferTest(ContractOffer contractOffer, ContractOfferResponse expected) {
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
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();
		// When
		ContractOfferResponse response = possibleEmployee.offerJob(contractOffer);
		// Then
		assertEquals(expected, response);
	}

}
