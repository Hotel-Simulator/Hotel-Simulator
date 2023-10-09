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
	public final BigDecimal skills;
	public final EmploymentPreferences preferences;
	public final Profession profession;
	public BigDecimal wage;
	public TypeOfContract typeOfContract;
	public Shift shift;
	private boolean isOccupied;
	private final Duration basicServiceExecutionTime;
	private EmployeeStatus employeeStatus = EmployeeStatus.HIRED_NOT_WORKING;
	private BigDecimal satisfaction;

	public Employee(PossibleEmployee possibleEmployee, ContractOffer contractOffer) {
		this.firstName = possibleEmployee.firstName;
		this.lastName = possibleEmployee.lastName;
		this.age = possibleEmployee.age;
		this.skills = possibleEmployee.skills;
		this.profession = possibleEmployee.profession;
		this.preferences = possibleEmployee.preferences;

		setContract(contractOffer);

		this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(possibleEmployee.profession);
		this.satisfaction = BigDecimal.ONE.min(wage.divide(preferences.desiredWage, 4, RoundingMode.CEILING));
	}

	public void setContract(ContractOffer contractOffer) {
		this.wage = contractOffer.offeredWage();
		this.typeOfContract = contractOffer.typeOfContract();
		this.shift = contractOffer.shift();
	}

	public BigDecimal getSatisfaction() {
		return satisfaction.setScale(2, RoundingMode.HALF_EVEN).stripTrailingZeros();
	}

	public void setSatisfaction(BigDecimal satisfaction) {
		this.satisfaction = satisfaction;
	}

	public void giveBonus(BigDecimal bonus) {
		var moneyEarnedInLast30Days = satisfaction.multiply(preferences.desiredWage);
		satisfaction = BigDecimal.ONE.min(moneyEarnedInLast30Days.add(bonus).divide(preferences.desiredWage, 4, RoundingMode.HALF_EVEN));
	}

	public ContractOfferResponse offerNewContract(ContractOffer contractOffer) {

		if(contractOffer.shift() == shift) {
			if(contractOffer.offeredWage().compareTo(wage) > 0){
				return ContractOfferResponse.POSITIVE;
			}
		} else {
			if(contractOffer.shift() == preferences.desiredShift && contractOffer.offeredWage().compareTo(preferences.acceptableWage) >= 0) {
				return ContractOfferResponse.POSITIVE;
			}
			if(contractOffer.shift() != preferences.desiredShift && contractOffer.offeredWage().compareTo(preferences.desiredWage) >= 0) {
				return ContractOfferResponse.POSITIVE;
			}
		}
		return ContractOfferResponse.NEGATIVE;
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
