package pl.agh.edu.management.bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.management.employee.EmployeeHandler;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class EmployeeSalaryHandler {
	private final EmployeeHandler employeeHandler;
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final BankAccountHandler bankAccountHandler;
	private final Time time = Time.getInstance();

	public EmployeeSalaryHandler(EmployeeHandler employeeHandler, BankAccountHandler bankAccountHandler) {
		this.employeeHandler = employeeHandler;
		this.bankAccountHandler = bankAccountHandler;
	}

	public void monthlyUpdate() {
		var salaryToPayForThisMonth = employeeHandler.getWorkingEmployees().stream()
				.map(Employee::getWageWithBonus)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		timeCommandExecutor.addCommand(new TimeCommand(() -> bankAccountHandler.registerExpense(salaryToPayForThisMonth),
				LocalDateTime.of(
						LocalDate.of(
								time.getTime().getYear(),
								time.getTime().plusMonths(1).getMonth(),
								JSONEmployeeDataLoader.payDayOfMonth),
						LocalTime.NOON)));

	}

}
