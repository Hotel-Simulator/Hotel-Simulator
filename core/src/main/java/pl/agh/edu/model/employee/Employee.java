package pl.agh.edu.model.employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;

public class Employee {

	public final String firstName;
	public final String lastName;
	public final int age;
	public final double skills;
	public final EmploymentPreferences preferences;
	public final Profession profession;
	public final BigDecimal wage;
	public final TypeOfContract typeOfContract;
	public final Shift shift;
	private boolean isOccupied;
	private BigDecimal bonusForThisMonth;
	private final Duration basicServiceExecutionTime;
	private EmployeeStatus employeeStatus;

	public Employee(PossibleEmployee possibleEmployee, JobOffer jobOffer) {
		this.firstName = possibleEmployee.firstName;
		this.lastName = possibleEmployee.lastName;
		this.age = possibleEmployee.age;
		this.skills = possibleEmployee.skills;
		this.profession = possibleEmployee.profession;
		this.preferences = possibleEmployee.preferences;
		this.wage = jobOffer.offeredWage();
		this.typeOfContract = jobOffer.typeOfContract();
		this.shift = jobOffer.shift();

		this.isOccupied = false;
		this.bonusForThisMonth = BigDecimal.ZERO;
		this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(possibleEmployee.profession);
		this.employeeStatus = EmployeeStatus.HIRED_NOT_WORKING;
	}

	public double getSatisfaction() {
		return Math.min(1., wage.add(bonusForThisMonth).divide(preferences.desiredWage, 2, RoundingMode.CEILING).doubleValue());
	}

	public boolean isAtWork(LocalTime time) {
		return time.isBefore(shift.getEndTime()) && !time.isBefore(shift.getStartTime());
	}

	public Duration getServiceExecutionTime() {
		return Duration.ofSeconds(
				(long) (basicServiceExecutionTime.getSeconds() *
						(1 - 0.5 * Math.min(skills, getSatisfaction()))));
	}

	public void giveBonus(BigDecimal bonus) {
		bonusForThisMonth = bonusForThisMonth.add(bonus);
	}

	public void update() {
		bonusForThisMonth = BigDecimal.ZERO;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}

	public EmployeeStatus getStatus() {
		return employeeStatus;
	}

	public void setStatus(EmployeeStatus employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

}
