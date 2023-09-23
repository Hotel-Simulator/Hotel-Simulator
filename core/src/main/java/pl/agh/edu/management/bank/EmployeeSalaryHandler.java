package pl.agh.edu.management.bank;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.management.employee.EmployeeHandler;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class EmployeeSalaryHandler {
	private final EmployeeHandler employeeHandler;
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final BankConnector bankConnector;
	private final Time time = Time.getInstance();

	public EmployeeSalaryHandler(EmployeeHandler employeeHandler, BankConnector bankConnector) {
		this.employeeHandler = employeeHandler;
		this.bankConnector = bankConnector;
	}

	public void monthlyUpdate() {
		var salaryToPayForThisMonth = employeeHandler.getWorkingEmployees().stream()
				.map(Employee::getWageWithBonus)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		timeCommandExecutor.addCommand(new TimeCommand(() -> bankConnector.registerExpense(salaryToPayForThisMonth),
				time.getTime().plusMonths(1)
						.truncatedTo(ChronoUnit.MONTHS)
						.withDayOfMonth(JSONEmployeeDataLoader.payDayOfMonth)
						.withHour(12)));
	}

}
