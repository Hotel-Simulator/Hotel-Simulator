package pl.agh.edu.management.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	private final BankAccountHandler bankAccountHandler;
	private final Time time = Time.getInstance();

	public EmployeeSalaryHandler(EmployeeHandler employeeHandler, BankAccountHandler bankAccountHandler) {
		this.employeeHandler = employeeHandler;
		this.bankAccountHandler = bankAccountHandler;
	}

	public void monthlyUpdate() {
		var salaryToPayForThisMonth = employeeHandler.getWorkingEmployees().stream()
				.map(employee -> employee.wage)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		timeCommandExecutor.addCommand(new TimeCommand(() -> bankAccountHandler.registerExpense(salaryToPayForThisMonth),
				time.getTime().plusMonths(1)
						.truncatedTo(ChronoUnit.DAYS)
						.withDayOfMonth(JSONEmployeeDataLoader.payDayOfMonth)
						.withHour(12)));
	}

	public void giveBonus(Employee employee, BigDecimal bonus) {
		employee.giveBonus(bonus);
		bankAccountHandler.registerExpense(bonus);
		timeCommandExecutor.addCommand(new TimeCommand(
				() -> {
					var moneyEarnedInLast30Days = employee.getSatisfaction()
							.multiply(employee.preferences.desiredWage);
					employee.setSatisfaction(BigDecimal.ONE.min(moneyEarnedInLast30Days.subtract(bonus)
							.divide(employee.preferences.desiredWage, 4, RoundingMode.HALF_EVEN)));
				},
				time.getTime().plusMonths(1)));
	}

}
