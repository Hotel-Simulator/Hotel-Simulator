package pl.agh.edu.management.employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.EmployeeStatus;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class EmployeeHandler {
	private final List<Employee> employees;
	private final TimeCommandExecutor timeCommandExecutor;
	private final Time time;

	public EmployeeHandler() {
		this.employees = new ArrayList<>();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.time = Time.getInstance();
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
				.filter(employee -> employee.getProfession() == profession)
				.collect(Collectors.toList());
	}

	public void monthlyUpdate() {
		employees.forEach(Employee::update);
	}
}
