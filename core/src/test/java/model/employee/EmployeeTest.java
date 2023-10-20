package model.employee;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.model.employee.*;

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
						.desiredShift(Shift.MORNING)
						.acceptableWage(BigDecimal.valueOf(5000))
						.desiredWage(BigDecimal.valueOf(6000))
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		ContractOffer contractOffer = new ContractOffer(Shift.MORNING, BigDecimal.valueOf(5000), TypeOfContract.PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		boolean result = employee.isAtWork(time);

		// Then
		assertEquals(expected, result);
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(5000), BigDecimal.valueOf(8000), Shift.MORNING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(7000), BigDecimal.valueOf(8000), Shift.MORNING, BigDecimal.ONE),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), Shift.MORNING, BigDecimal.ONE),
				Arguments.of(BigDecimal.valueOf(5000), BigDecimal.valueOf(8000), Shift.EVENING, new BigDecimal("0.50")),
				Arguments.of(BigDecimal.valueOf(7000), BigDecimal.valueOf(8000), Shift.EVENING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(9000), BigDecimal.valueOf(8000), Shift.EVENING, BigDecimal.ONE));
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
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		ContractOffer contractOffer = new ContractOffer(Shift.MORNING, actualWage, TypeOfContract.PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		employee.addBonus(BigDecimal.valueOf(1000));
		BigDecimal satisfaction = employee.getSatisfaction();

		// Then
		assertEquals(expected, satisfaction);
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithoutBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(6000), BigDecimal.valueOf(8000), Shift.MORNING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), Shift.MORNING, BigDecimal.ONE),
				Arguments.of(BigDecimal.valueOf(9000), BigDecimal.valueOf(8000), Shift.MORNING, BigDecimal.ONE),
				Arguments.of(BigDecimal.valueOf(6000), BigDecimal.valueOf(8000), Shift.EVENING, new BigDecimal("0.50")),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), Shift.EVENING, new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(8000), Shift.EVENING, BigDecimal.ONE));
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
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		ContractOffer contractOffer = new ContractOffer(Shift.MORNING, actualWage, TypeOfContract.PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		BigDecimal satisfaction = employee.getSatisfaction();

		// Then
		assertEquals(expected, satisfaction);
	}

	private static Stream<Arguments> provideServiceExecutionTimeTestArgs() {
		return Stream.of(
				Arguments.of(1., BigDecimal.ZERO, Duration.ofMinutes(30)),
				Arguments.of(0.4, BigDecimal.ONE, Duration.ofMinutes(24)),
				Arguments.of(1., BigDecimal.ONE, Duration.ofMinutes(15)));
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
						.desiredShift(Shift.MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		ContractOffer contractOffer = new ContractOffer(Shift.MORNING, BigDecimal.valueOf(4000 * satisfaction), TypeOfContract.PERMANENT);
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
						.desiredShift(Shift.MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		ContractOffer contractOffer = new ContractOffer(Shift.MORNING, BigDecimal.valueOf(3000), TypeOfContract.PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		BigDecimal newWage = BigDecimal.valueOf(5500);
		TypeOfContract newContractType = TypeOfContract.PERMANENT;
		Shift newShift = Shift.NIGHT;

		ContractOffer newContractOffer = new ContractOffer(newShift, newWage, newContractType);

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
						.desiredShift(Shift.MORNING)
						.acceptableWage(BigDecimal.valueOf(0))
						.desiredWage(BigDecimal.valueOf(4000))
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		ContractOffer contractOffer = new ContractOffer(Shift.MORNING, BigDecimal.valueOf(3000), TypeOfContract.PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		BigDecimal newWage = BigDecimal.valueOf(4000);
		TypeOfContract newContractType = TypeOfContract.PERMANENT;
		Shift newShift = Shift.MORNING;
		ContractOffer newContractOffer = new ContractOffer(newShift, newWage, newContractType);

		// When
		employee.setContract(newContractOffer);

		// Then
		BigDecimal expectedSatisfaction = BigDecimal.ONE.min(newWage.divide(employee.preferences.desiredWage, 4, RoundingMode.HALF_EVEN));
		assertEquals(expectedSatisfaction.setScale(2, RoundingMode.HALF_EVEN).stripTrailingZeros(), employee.getSatisfaction());
	}

	public static Stream<Arguments> provideOfferNewContractArgs() {
		return Stream.of(
				Arguments.of(Shift.MORNING, new ContractOffer(Shift.MORNING, BigDecimal.valueOf(3000), TypeOfContract.PERMANENT), ContractOfferResponse.NEGATIVE),
				Arguments.of(Shift.MORNING, new ContractOffer(Shift.MORNING, BigDecimal.valueOf(3001), TypeOfContract.PERMANENT), ContractOfferResponse.POSITIVE),
				Arguments.of(Shift.EVENING, new ContractOffer(Shift.EVENING, BigDecimal.valueOf(3999), TypeOfContract.PERMANENT), ContractOfferResponse.NEGATIVE),
				Arguments.of(Shift.EVENING, new ContractOffer(Shift.EVENING, BigDecimal.valueOf(4000), TypeOfContract.PERMANENT), ContractOfferResponse.POSITIVE),
				Arguments.of(Shift.MORNING, new ContractOffer(Shift.EVENING, BigDecimal.valueOf(3999), TypeOfContract.PERMANENT), ContractOfferResponse.NEGATIVE),
				Arguments.of(Shift.MORNING, new ContractOffer(Shift.EVENING, BigDecimal.valueOf(4000), TypeOfContract.PERMANENT), ContractOfferResponse.POSITIVE));
	}

	@ParameterizedTest
	@MethodSource("provideOfferNewContractArgs")
	void offerNewContractTest(Shift desiredShift, ContractOffer newContract, ContractOfferResponse result) {

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
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		ContractOffer contractOffer = new ContractOffer(Shift.MORNING, BigDecimal.valueOf(3000), TypeOfContract.PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);
	}
}
