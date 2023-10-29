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

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmploymentPreferences;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.contract.TypeOfContract;

public class EmployeeTest {

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

		Offer contractOffer = new Offer(MORNING, BigDecimal.valueOf(5000), PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		boolean result = employee.isAtWork(time);

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
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(desiredShift)
						.acceptableWage(BigDecimal.valueOf(4000))
						.desiredWage(desiredWage)
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		Offer contractOffer = new Offer(MORNING, actualWage, PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		employee.addBonus(BigDecimal.valueOf(1000));
		BigDecimal satisfaction = employee.getSatisfaction();

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
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(desiredShift)
						.acceptableWage(BigDecimal.valueOf(4000))
						.desiredWage(desiredWage)
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		Offer contractOffer = new Offer(MORNING, actualWage, PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		BigDecimal satisfaction = employee.getSatisfaction();

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
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(skills)
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		Offer contractOffer = new Offer(MORNING, BigDecimal.valueOf(4000 * satisfaction), PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		Duration result = employee.getServiceExecutionTime();

		// Then
		assertEquals(expected, result);
	}

	@Test
	void setContract_ShouldUpdateContractInformation() {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.5"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		Offer contractOffer = new Offer(MORNING, BigDecimal.valueOf(3000), PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		BigDecimal newWage = BigDecimal.valueOf(5500);
		TypeOfContract newContractType = PERMANENT;
		Shift newShift = NIGHT;

		Offer newContractOffer = new Offer(newShift, newWage, newContractType);

		// When
		employee.setContract(newContractOffer);

		// Then
		assertEquals(newWage, employee.wage);
		assertEquals(newContractType, employee.typeOfContract);
		assertEquals(newShift, employee.shift);
	}

	@Test
	void getSatisfaction_ShouldCalculateSatisfaction() {

		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.5"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		Offer contractOffer = new Offer(MORNING, BigDecimal.valueOf(3000), PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		BigDecimal newWage = BigDecimal.valueOf(4000);
		Offer newContractOffer = new Offer(MORNING, newWage, PERMANENT);

		// When
		employee.setContract(newContractOffer);

		// Then
		BigDecimal expectedSatisfaction = ONE.min(newWage.divide(employee.preferences.desiredWage, 4, HALF_EVEN));
		assertEquals(expectedSatisfaction.setScale(2, HALF_EVEN).stripTrailingZeros(), employee.getSatisfaction());
	}

	public static Stream<Arguments> provideOfferNewContractArgs() {
		return Stream.of(
				Arguments.of(MORNING, new Offer(MORNING, BigDecimal.valueOf(3000), PERMANENT), NEGATIVE),
				Arguments.of(MORNING, new Offer(MORNING, BigDecimal.valueOf(3001), PERMANENT), POSITIVE),
				Arguments.of(EVENING, new Offer(EVENING, BigDecimal.valueOf(2999), PERMANENT), NEGATIVE),
				Arguments.of(EVENING, new Offer(EVENING, BigDecimal.valueOf(3000), PERMANENT), POSITIVE),
				Arguments.of(MORNING, new Offer(EVENING, BigDecimal.valueOf(3999), PERMANENT), NEGATIVE),
				Arguments.of(MORNING, new Offer(EVENING, BigDecimal.valueOf(4000), PERMANENT), POSITIVE));
	}

	@ParameterizedTest
	@MethodSource("provideOfferNewContractArgs")
	void offerNewContractTest(Shift desiredShift, Offer newContract, OfferResponse expected) {

		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.5"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(desiredShift)
						.acceptableWage(BigDecimal.valueOf(3000))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		Offer contractOffer = new Offer(MORNING, BigDecimal.valueOf(3000), PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		OfferResponse result = employee.offerNewContract(newContract);

		// Then
		assertEquals(expected, result);
	}
}
