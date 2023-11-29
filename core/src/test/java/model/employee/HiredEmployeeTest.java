package model.employee;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.employee.Profession.CLEANER;
import static pl.agh.edu.engine.employee.Shift.EVENING;
import static pl.agh.edu.engine.employee.Shift.MORNING;
import static pl.agh.edu.engine.employee.Shift.NIGHT;
import static pl.agh.edu.engine.employee.contract.OfferResponse.NEGATIVE;
import static pl.agh.edu.engine.employee.contract.OfferResponse.POSITIVE;
import static pl.agh.edu.engine.employee.contract.TypeOfContract.PERMANENT;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.engine.employee.EmployeePreferences;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.contract.TypeOfContract;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.possible.PossibleEmployee;

public class HiredEmployeeTest {

	private static Stream<Arguments> provideLocalTime() {
		return Stream.of(
				Arguments.of(LocalTime.of(7, 59), false),
				Arguments.of(LocalTime.of(8, 0), true),
				Arguments.of(LocalTime.of(15, 59), true),
				Arguments.of(LocalTime.of(16, 0), false));
	}

	@ParameterizedTest
	@MethodSource("provideLocalTime")
	public void isAtWorkTest(LocalTime time, boolean expected) {
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

		EmployeeOffer contractEmployeeOffer = new EmployeeOffer(MORNING, BigDecimal.valueOf(5000), PERMANENT);
		HiredEmployee hiredEmployee = new HiredEmployee(possibleEmployee, contractEmployeeOffer);

		// When
		boolean result = hiredEmployee.isAtWork(time);

		// Then
		assertEquals(expected, result);
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(5000), BigDecimal.valueOf(8000), MORNING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(7000), BigDecimal.valueOf(8000), MORNING, ONE),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), MORNING, ONE),
				Arguments.of(BigDecimal.valueOf(5000), BigDecimal.valueOf(8000), EVENING, new BigDecimal("0.50")),
				Arguments.of(BigDecimal.valueOf(7000), BigDecimal.valueOf(8000), EVENING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(9000), BigDecimal.valueOf(8000), EVENING, ONE));
	}

	@ParameterizedTest
	@MethodSource("provideWagesForSatisfactionWithBonusTest")
	public void satisfactionWithBonusTest(BigDecimal actualWage, BigDecimal desiredWage, Shift desiredShift, BigDecimal expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(desiredShift)
						.acceptableWage(BigDecimal.valueOf(4000))
						.desiredWage(desiredWage)
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		EmployeeOffer contractEmployeeOffer = new EmployeeOffer(MORNING, actualWage, PERMANENT);
		HiredEmployee hiredEmployee = new HiredEmployee(possibleEmployee, contractEmployeeOffer);

		// When
		hiredEmployee.addBonus(BigDecimal.valueOf(1000));
		BigDecimal satisfaction = hiredEmployee.getSatisfaction();

		// Then
		assertEquals(expected, satisfaction);
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithoutBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(6000), BigDecimal.valueOf(8000), MORNING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), MORNING, ONE),
				Arguments.of(BigDecimal.valueOf(9000), BigDecimal.valueOf(8000), MORNING, ONE),
				Arguments.of(BigDecimal.valueOf(6000), BigDecimal.valueOf(8000), EVENING, new BigDecimal("0.50")),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), EVENING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(8000), EVENING, ONE));
	}

	@ParameterizedTest
	@MethodSource("provideWagesForSatisfactionWithoutBonusTest")
	public void satisfactionWithoutBonusTest(BigDecimal actualWage, BigDecimal desiredWage, Shift desiredShift, BigDecimal expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(desiredShift)
						.acceptableWage(BigDecimal.valueOf(4000))
						.desiredWage(desiredWage)
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		EmployeeOffer contractEmployeeOffer = new EmployeeOffer(MORNING, actualWage, PERMANENT);
		HiredEmployee hiredEmployee = new HiredEmployee(possibleEmployee, contractEmployeeOffer);

		// When
		BigDecimal satisfaction = hiredEmployee.getSatisfaction();

		// Then
		assertEquals(expected, satisfaction);
	}

	private static Stream<Arguments> provideServiceExecutionTimeTestArgs() {
		return Stream.of(
				Arguments.of(1., ZERO, Duration.ofMinutes(30)),
				Arguments.of(0.4, ONE, Duration.ofMinutes(24)),
				Arguments.of(1., ONE, Duration.ofMinutes(15)));
	}

	@ParameterizedTest
	@MethodSource("provideServiceExecutionTimeTestArgs")
	public void serviceExecutionTimeTest(double satisfaction, BigDecimal skills, Duration expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(skills)
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		EmployeeOffer contractEmployeeOffer = new EmployeeOffer(MORNING, BigDecimal.valueOf(4000 * satisfaction), PERMANENT);
		HiredEmployee hiredEmployee = new HiredEmployee(possibleEmployee, contractEmployeeOffer);

		// When
		Duration result = hiredEmployee.getServiceExecutionTime();

		// Then
		assertEquals(expected, result);
	}

	@Test
	void setContract_ShouldUpdateContractInformation() {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.5"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		EmployeeOffer contractEmployeeOffer = new EmployeeOffer(MORNING, BigDecimal.valueOf(3000), PERMANENT);
		HiredEmployee hiredEmployee = new HiredEmployee(possibleEmployee, contractEmployeeOffer);

		BigDecimal newWage = BigDecimal.valueOf(5500);
		TypeOfContract newContractType = PERMANENT;
		Shift newShift = NIGHT;

		EmployeeOffer newContractEmployeeOffer = new EmployeeOffer(newShift, newWage, newContractType);

		// When
		hiredEmployee.setContract(newContractEmployeeOffer);

		// Then
		assertEquals(newWage, hiredEmployee.getWage());
		assertEquals(newContractType, hiredEmployee.getTypeOfContract());
		assertEquals(newShift, hiredEmployee.getShift());
	}

	@Test
	void getSatisfaction_ShouldCalculateSatisfaction() {

		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.5"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		EmployeeOffer contractEmployeeOffer = new EmployeeOffer(MORNING, BigDecimal.valueOf(3000), PERMANENT);
		HiredEmployee hiredEmployee = new HiredEmployee(possibleEmployee, contractEmployeeOffer);

		BigDecimal newWage = BigDecimal.valueOf(4000);
		EmployeeOffer newContractEmployeeOffer = new EmployeeOffer(MORNING, newWage, PERMANENT);

		// When
		hiredEmployee.setContract(newContractEmployeeOffer);

		// Then
		BigDecimal expectedSatisfaction = ONE.min(newWage.divide(hiredEmployee.preferences.desiredWage, 4, HALF_EVEN));
		assertEquals(expectedSatisfaction.setScale(2, HALF_EVEN).stripTrailingZeros(), hiredEmployee.getSatisfaction());
	}

	public static Stream<Arguments> provideOfferNewContractArgs() {
		return Stream.of(
				Arguments.of(MORNING, new EmployeeOffer(MORNING, BigDecimal.valueOf(3000), PERMANENT), NEGATIVE),
				Arguments.of(MORNING, new EmployeeOffer(MORNING, BigDecimal.valueOf(3001), PERMANENT), POSITIVE),
				Arguments.of(EVENING, new EmployeeOffer(EVENING, BigDecimal.valueOf(2999), PERMANENT), NEGATIVE),
				Arguments.of(EVENING, new EmployeeOffer(EVENING, BigDecimal.valueOf(3000), PERMANENT), POSITIVE),
				Arguments.of(MORNING, new EmployeeOffer(EVENING, BigDecimal.valueOf(3999), PERMANENT), NEGATIVE),
				Arguments.of(MORNING, new EmployeeOffer(EVENING, BigDecimal.valueOf(4000), PERMANENT), POSITIVE));
	}

	@ParameterizedTest
	@MethodSource("provideOfferNewContractArgs")
	void offerNewContractTest(Shift desiredShift, EmployeeOffer newContract, OfferResponse expected) {

		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.5"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(desiredShift)
						.acceptableWage(BigDecimal.valueOf(3000))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		EmployeeOffer contractEmployeeOffer = new EmployeeOffer(MORNING, BigDecimal.valueOf(3000), PERMANENT);
		HiredEmployee hiredEmployee = new HiredEmployee(possibleEmployee, contractEmployeeOffer);

		// When
		OfferResponse result = hiredEmployee.offerNewContract(newContract);

		// Then
		assertEquals(expected, result);
	}
}
