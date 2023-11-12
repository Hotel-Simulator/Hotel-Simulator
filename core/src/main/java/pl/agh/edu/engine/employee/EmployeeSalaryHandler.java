package pl.agh.edu.engine.employee;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import pl.agh.edu.data.loader.JSONEmployeeDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.SerializableRunnable;
import pl.agh.edu.engine.time.command.TimeCommand;

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
				.reduce(ZERO, BigDecimal::add);

		timeCommandExecutor.addCommand(new TimeCommand((SerializableRunnable)() -> bankAccountHandler.registerExpense(salaryToPayForThisMonth),
				time.getTime().plusMonths(1)
						.truncatedTo(ChronoUnit.DAYS)
						.withDayOfMonth(JSONEmployeeDataLoader.payDayOfMonth)
						.withHour(12)));
	}

	public void giveBonus(Employee employee, BigDecimal bonus) {
		employee.addBonus(bonus);
		bankAccountHandler.registerExpense(bonus);
		timeCommandExecutor.addCommand(
				new TimeCommand((SerializableRunnable)() -> employee.removeBonus(bonus), time.getTime().plusMonths(1)));
	}

}
