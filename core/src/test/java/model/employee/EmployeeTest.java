package model.employee;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.employee.*;

public class EmployeeTest {

	static {
		try {
			changeJSONPath();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

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
						.desiredTypeOfContract(TypeOfContract.AGREEMENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		JobOffer jobOffer = new JobOffer(Shift.MORNING, BigDecimal.valueOf(5000), TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);

		// When
		boolean result = employee.isAtWork(time);

		// Then
		assertEquals(expected, result);
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithoutBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(6000), BigDecimal.valueOf(8000), new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), BigDecimal.ONE),
				Arguments.of(BigDecimal.valueOf(9000), BigDecimal.valueOf(8000), BigDecimal.ONE)

		);
	}

	@ParameterizedTest
	@MethodSource("provideWagesForSatisfactionWithoutBonusTest")
	public void satisfactionWithoutBonusTest(BigDecimal actualWage, BigDecimal desiredWage, BigDecimal expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(Shift.MORNING)
						.acceptableWage(BigDecimal.valueOf(4000))
						.desiredWage(desiredWage)
						.desiredTypeOfContract(TypeOfContract.AGREEMENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		JobOffer jobOffer = new JobOffer(Shift.MORNING, actualWage, TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);

		// When
		BigDecimal satisfaction = employee.getSatisfaction();

		// Then
		assertEquals(expected, satisfaction);
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(5000), BigDecimal.valueOf(8000), new BigDecimal("0.75")),
				Arguments.of(BigDecimal.valueOf(7000), BigDecimal.valueOf(8000), BigDecimal.ONE),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), BigDecimal.ONE)

		);
	}

	@ParameterizedTest
	@MethodSource("provideWagesForSatisfactionWithBonusTest")
	public void satisfactionWithBonusTest(BigDecimal actualWage, BigDecimal desiredWage, BigDecimal expected) {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(Shift.MORNING)
						.acceptableWage(BigDecimal.valueOf(4000))
						.desiredWage(desiredWage)
						.desiredTypeOfContract(TypeOfContract.AGREEMENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		JobOffer jobOffer = new JobOffer(Shift.MORNING, actualWage, TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);

		// When
		employee.giveBonus(BigDecimal.valueOf(1000));
		BigDecimal satisfaction = employee.getSatisfaction();

		// Then
		assertEquals(expected, satisfaction);
	}

	private static Stream<Arguments> provideSatisfactionAndSkillsForSatisfactionWithBonusTest() {
		return Stream.of(
				Arguments.of(1., BigDecimal.ZERO, Duration.ofMinutes(30)),
				Arguments.of(0.4, BigDecimal.ONE, Duration.ofMinutes(24)),
				Arguments.of(1., BigDecimal.ONE, Duration.ofMinutes(15)));
	}

	@ParameterizedTest
	@MethodSource("provideSatisfactionAndSkillsForSatisfactionWithBonusTest")
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
						.desiredTypeOfContract(TypeOfContract.AGREEMENT)
						.build())
				.profession(Profession.CLEANER)
				.build();

		JobOffer jobOffer = new JobOffer(Shift.MORNING, BigDecimal.valueOf(4000 * satisfaction), TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);

		// When
		Duration result = employee.getServiceExecutionTime();

		// Then
		assertEquals(expected, result);
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
