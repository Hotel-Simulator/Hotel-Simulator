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
						.truncatedTo(ChronoUnit.MONTHS)
						.withDayOfMonth(JSONEmployeeDataLoader.payDayOfMonth)
						.withHour(12)));
	}

	public void giveBonus(Employee employee, BigDecimal bonus) {
		addBonusEffect(employee, bonus);
		bankAccountHandler.registerExpense(bonus);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> removeBonusEffect(employee, bonus), time.getTime().plusMonths(1)));
	}

	private void addBonusEffect(Employee employee, BigDecimal bonus) {
		employee.setWageSatisfaction(getMoneyEarnedInLast30Days(employee).add(bonus)
				.divide(employee.preferences.desiredWage, 4, RoundingMode.HALF_EVEN));
	}

	private void removeBonusEffect(Employee employee, BigDecimal bonus) {
		employee.setWageSatisfaction(getMoneyEarnedInLast30Days(employee).subtract(bonus)
				.divide(employee.preferences.desiredWage, 4, RoundingMode.HALF_EVEN));
	}

	private BigDecimal getMoneyEarnedInLast30Days(Employee employee) {
		return employee.getWageSatisfaction().multiply(employee.preferences.desiredWage);
	}

}
