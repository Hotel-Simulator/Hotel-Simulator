package pl.agh.edu.model.employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;

public class Employee {

	public final String firstName;
	public final String lastName;
	public final int age;
	public final BigDecimal skills;
	public final EmploymentPreferences preferences;
	public final Profession profession;
	public final BigDecimal wage;
	public final TypeOfContract typeOfContract;
	public final Shift shift;
	private boolean isOccupied;
	private final Duration basicServiceExecutionTime;
	private EmployeeStatus employeeStatus = EmployeeStatus.HIRED_NOT_WORKING;
	private final List<BigDecimal> bonuses = new ArrayList<>();

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

		this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(possibleEmployee.profession);
	}

	public BigDecimal getSatisfaction() {
		BigDecimal desiredShiftModifier = shift == preferences.desiredShift
				? BigDecimal.ZERO
				: new BigDecimal("0.25");
		return BigDecimal.ONE.min(getWageSatisfaction().subtract(desiredShiftModifier));
	}

	private BigDecimal getWageSatisfaction() {
		return wage.add(bonuses.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
				.divide(preferences.desiredWage, 4, RoundingMode.CEILING)
				.setScale(2, RoundingMode.HALF_EVEN).stripTrailingZeros();
	}

	public void addBonus(BigDecimal bonus) {
		bonuses.add(bonus);
	}

	public void removeBonus(BigDecimal bonus) {
		bonuses.remove(bonus);
	}

	public boolean isAtWork(LocalTime time) {
		return time.isBefore(shift.getEndTime()) && !time.isBefore(shift.getStartTime());
	}

	public Duration getServiceExecutionTime() {
		return Duration.ofSeconds(
				(long) (basicServiceExecutionTime.getSeconds() *
						(BigDecimal.ONE.subtract(skills.min(getSatisfaction()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_EVEN))).doubleValue()));
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
