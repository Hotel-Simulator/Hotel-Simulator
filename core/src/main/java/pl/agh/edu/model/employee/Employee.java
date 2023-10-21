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
	public BigDecimal wage;
	public TypeOfContract typeOfContract;
	public Shift shift;
	private boolean isOccupied;
	private final Duration basicServiceExecutionTime;
	private EmployeeStatus employeeStatus = EmployeeStatus.HIRED_NOT_WORKING;
	private final List<BigDecimal> bonuses = new ArrayList<>();

	public Employee(PossibleEmployee possibleEmployee, ContractOffer contractOffer) {
		this.firstName = possibleEmployee.firstName;
		this.lastName = possibleEmployee.lastName;
		this.age = possibleEmployee.age;
		this.skills = possibleEmployee.skills;
		this.profession = possibleEmployee.profession;
		this.preferences = possibleEmployee.preferences;

		setContract(contractOffer);

		this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(possibleEmployee.profession);
	}

	public void setContract(ContractOffer contractOffer) {
		this.wage = contractOffer.offeredWage();
		this.typeOfContract = contractOffer.typeOfContract();
		this.shift = contractOffer.shift();
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

	public ContractOfferResponse offerNewContract(ContractOffer contractOffer) {

		Shift offerShift = contractOffer.shift();
		BigDecimal offeredWage = contractOffer.offeredWage();

		boolean isPositive = (offerShift == shift && offeredWage.compareTo(wage) > 0) ||
				(offerShift != shift && offerShift == preferences.desiredShift && offeredWage.compareTo(preferences.acceptableWage) >= 0) ||
				(offerShift != shift && offerShift != preferences.desiredShift && offeredWage.compareTo(preferences.desiredWage) >= 0);

		return isPositive ? ContractOfferResponse.POSITIVE : ContractOfferResponse.NEGATIVE;
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
