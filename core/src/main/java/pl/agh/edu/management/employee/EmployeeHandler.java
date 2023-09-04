package pl.agh.edu.management.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.model.employee.*;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class EmployeeHandler {
	private final List<Employee> employees;
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final Time time = Time.getInstance();

	public EmployeeHandler() {
		this.employees = getInitialEmployees();
	}

	public void hireEmployee(Employee employee) {
		this.employees.add(employee);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> employee.setStatus(EmployeeStatus.HIRED_WORKING),
						LocalDateTime.of(time.getTime()
								.toLocalDate()
								.minusDays(time.getTime().getDayOfMonth() - 1)
								.plusMonths(1),
								LocalTime.MIDNIGHT)));
	}

	public void fireEmployee(Employee employee) {
		employee.setStatus(EmployeeStatus.FIRED_WORKING);
		timeCommandExecutor.addCommand(
				new TimeCommand(
						() -> this.removeEmployee(employee),
						LocalDateTime.of(LocalDate.of(time.getTime().getYear(), time.getTime().getMonth(), 1).plusMonths(JSONEmployeeDataLoader.noticePeriodInMonths + 1),
								LocalTime.MIDNIGHT)));
	}

	public void removeEmployee(Employee employee) {
		employees.remove(employee);
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public List<Employee> getWorkingEmployees() {
		return employees.stream()
				.filter(employee -> employee.getStatus() != EmployeeStatus.HIRED_NOT_WORKING)
				.collect(Collectors.toList());
	}

	public List<Employee> getWorkingEmployeesByProfession(Profession profession) {
		return getWorkingEmployees().stream()
				.filter(employee -> employee.profession == profession)
				.collect(Collectors.toList());
	}

	public void monthlyUpdate() {
		employees.forEach(Employee::update);
	}

	private List<Employee> getInitialEmployees() {
		List<Employee> initialEmployees = Stream.of(
				new Employee(new PossibleEmployee.Builder()
						.firstName("Jan")
						.lastName("Kowalski")
						.age(23)
						.skills(0.55)
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(Shift.MORNING)
								.acceptableWage(BigDecimal.valueOf(4000))
								.desiredWage(BigDecimal.valueOf(6000))
								.desiredTypeOfContract(TypeOfContract.AGREEMENT)
								.build())
						.profession(Profession.CLEANER)
						.build(),
						new JobOffer(Shift.MORNING, BigDecimal.valueOf(4500), TypeOfContract.AGREEMENT)),
				new Employee(new PossibleEmployee.Builder()
						.firstName("Maria")
						.lastName("Nowak")
						.age(23)
						.skills(0.65)
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(Shift.MORNING)
								.acceptableWage(BigDecimal.valueOf(4500))
								.desiredWage(BigDecimal.valueOf(7000))
								.desiredTypeOfContract(TypeOfContract.AGREEMENT)
								.build())
						.profession(Profession.CLEANER)
						.build(),
						new JobOffer(Shift.MORNING, BigDecimal.valueOf(5500), TypeOfContract.AGREEMENT)),
				new Employee(new PossibleEmployee.Builder()
						.firstName("Zofia")
						.lastName("Wrona")
						.age(23)
						.skills(0.65)
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(Shift.MORNING)
								.acceptableWage(BigDecimal.valueOf(4500))
								.desiredWage(BigDecimal.valueOf(7000))
								.desiredTypeOfContract(TypeOfContract.AGREEMENT)
								.build())
						.profession(Profession.RECEPTIONIST)
						.build(),
						new JobOffer(Shift.EVENING, BigDecimal.valueOf(5500), TypeOfContract.AGREEMENT)),
				new Employee(new PossibleEmployee.Builder()
						.firstName("Marcin")
						.lastName("Szpak")
						.age(45)
						.skills(0.45)
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(Shift.MORNING)
								.acceptableWage(BigDecimal.valueOf(4000))
								.desiredWage(BigDecimal.valueOf(6000))
								.desiredTypeOfContract(TypeOfContract.AGREEMENT)
								.build())
						.profession(Profession.RECEPTIONIST)
						.build(),
						new JobOffer(Shift.MORNING, BigDecimal.valueOf(4500), TypeOfContract.AGREEMENT))).toList();
		initialEmployees.forEach(employee -> employee.setStatus(EmployeeStatus.HIRED_WORKING));

		return initialEmployees;
	}
}
