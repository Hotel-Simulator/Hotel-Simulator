package model.employee;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
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
		// given
		PossibleEmployee possibleEmployee = new PossibleEmployee(
				"",
				"",
				18,
				0.45,
				new EmploymentPreferences(
						Shift.MORNING,
						BigDecimal.valueOf(5000),
						BigDecimal.valueOf(6000),
						TypeOfContract.AGREEMENT),
				Profession.CLEANER);

		JobOffer jobOffer = new JobOffer(Shift.MORNING, BigDecimal.valueOf(5000), TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);
		// when
		boolean result = employee.isAtWork(time);
		// then
		assertEquals(expected, result);
	}

	@Test
	public void bonusTest() throws NoSuchFieldException , IllegalAccessException {
		// given
		PossibleEmployee possibleEmployee = new PossibleEmployee(
				"",
				"",
				18,
				0.45,
				new EmploymentPreferences(
						Shift.MORNING,
						BigDecimal.valueOf(5000),
						BigDecimal.valueOf(6000),
						TypeOfContract.AGREEMENT),
				Profession.CLEANER);

		JobOffer jobOffer = new JobOffer(Shift.MORNING, BigDecimal.valueOf(5000), TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);
		// when
		employee.giveBonus(BigDecimal.valueOf(30));
		employee.giveBonus(BigDecimal.valueOf(30));
		employee.giveBonus(BigDecimal.valueOf(30));

		Field field = Employee.class.getDeclaredField("bonusForThisMonth");
		field.setAccessible(true);
		// then
		assertEquals(BigDecimal.valueOf(90), field.get(employee));
	}

	@Test
	public void bonusAfterUpdateTest() throws NoSuchFieldException , IllegalAccessException {
		// given
		PossibleEmployee possibleEmployee = new PossibleEmployee(
				"",
				"",
				18,
				0.45,
				new EmploymentPreferences(
						Shift.MORNING,
						BigDecimal.valueOf(5000),
						BigDecimal.valueOf(6000),
						TypeOfContract.AGREEMENT),
				Profession.CLEANER);

		JobOffer jobOffer = new JobOffer(Shift.MORNING, BigDecimal.valueOf(5000), TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);
		// when
		employee.giveBonus(BigDecimal.valueOf(30));
		employee.giveBonus(BigDecimal.valueOf(30));
		employee.giveBonus(BigDecimal.valueOf(30));

		employee.update();

		Field field = Employee.class.getDeclaredField("bonusForThisMonth");
		field.setAccessible(true);
		// then
		assertEquals(BigDecimal.ZERO, field.get(employee));
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithoutBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(6000), BigDecimal.valueOf(8000), 0.75),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), 1),
				Arguments.of(BigDecimal.valueOf(9000), BigDecimal.valueOf(8000), 1)

		);
	}

	@ParameterizedTest
	@MethodSource("provideWagesForSatisfactionWithoutBonusTest")
	public void satisfactionWithoutBonusTest(BigDecimal actualWage, BigDecimal desiredWage, double expected) {
		// given
		PossibleEmployee possibleEmployee = new PossibleEmployee(
				"",
				"",
				18,
				0.45,
				new EmploymentPreferences(
						Shift.MORNING,
						BigDecimal.valueOf(4000),
						desiredWage,
						TypeOfContract.AGREEMENT),
				Profession.CLEANER);

		JobOffer jobOffer = new JobOffer(Shift.MORNING, actualWage, TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);

		// when
		double satisfaction = employee.getSatisfaction();
		// then
		assertEquals(0, Double.compare(expected, satisfaction));
	}

	private static Stream<Arguments> provideWagesForSatisfactionWithBonusTest() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(5000), BigDecimal.valueOf(8000), 0.75),
				Arguments.of(BigDecimal.valueOf(7000), BigDecimal.valueOf(8000), 1),
				Arguments.of(BigDecimal.valueOf(8000), BigDecimal.valueOf(8000), 1)

		);
	}

	@ParameterizedTest
	@MethodSource("provideWagesForSatisfactionWithBonusTest")
	public void satisfactionWithBonusTest(BigDecimal actualWage, BigDecimal desiredWage, double expected) {
		// given
		PossibleEmployee possibleEmployee = new PossibleEmployee(
				"",
				"",
				18,
				0.45,
				new EmploymentPreferences(
						Shift.MORNING,
						BigDecimal.valueOf(4000),
						desiredWage,
						TypeOfContract.AGREEMENT),
				Profession.CLEANER);

		JobOffer jobOffer = new JobOffer(Shift.MORNING, actualWage, TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);

		// when
		employee.giveBonus(BigDecimal.valueOf(1000));
		double satisfaction = employee.getSatisfaction();
		// then
		assertEquals(0, Double.compare(expected, satisfaction));
	}

	private static Stream<Arguments> provideSatisfactionAndSkillsForSatisfactionWithBonusTest() {
		return Stream.of(
				Arguments.of(1., 0., Duration.ofMinutes(30)),
				Arguments.of(0.4, 1., Duration.ofMinutes(24)),
				Arguments.of(1., 1., Duration.ofMinutes(15)));
	}

	@ParameterizedTest
	@MethodSource("provideSatisfactionAndSkillsForSatisfactionWithBonusTest")
	public void serviceExecutionTimeTest(double satisfaction, double skills, Duration expected) {
		// given
		PossibleEmployee possibleEmployee = new PossibleEmployee(
				"",
				"",
				18,
				skills,
				new EmploymentPreferences(
						Shift.MORNING,
						BigDecimal.valueOf(0),
						BigDecimal.valueOf(4000),
						TypeOfContract.AGREEMENT),
				Profession.CLEANER);

		JobOffer jobOffer = new JobOffer(Shift.MORNING, BigDecimal.valueOf(4000 * satisfaction), TypeOfContract.AGREEMENT);
		Employee employee = new Employee(possibleEmployee, jobOffer);
		// when
		Duration result = employee.getServiceExecutionTime();
		// then
		assertEquals(expected, result);
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
