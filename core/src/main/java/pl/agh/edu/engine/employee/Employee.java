package pl.agh.edu.engine.employee;

import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;

import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.contract.TypeOfContract;

public abstract class Employee {
	public final String firstName;
	public final String lastName;
	public final int age;
	public final BigDecimal skills;
	public final EmployeePreferences preferences;
	public final Profession profession;
	public final int acceptancePointsThreshold;

	protected Employee(
			String firstName,
			String lastName,
			int age,
			BigDecimal skills,
			EmployeePreferences preferences,
			Profession profession,
			int acceptancePointsThreshold) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.skills = skills;
		this.preferences = preferences;
		this.profession = profession;
		this.acceptancePointsThreshold = acceptancePointsThreshold;
	}

	public OfferResponse offerContract(EmployeeOffer employeeOffer) {

		Shift offerShift = employeeOffer.shift();
		BigDecimal offeredWage = employeeOffer.offeredWage();
		TypeOfContract offerTypeOfContract = employeeOffer.typeOfContract();

		int acceptancePoints = (offerShift == preferences.desiredShift ? 1 : 0) +
				(offeredWage.compareTo(preferences.desiredWage.multiply(BigDecimal.valueOf(2))) >= 0 ? 2 : 0) +
				(offeredWage.compareTo(preferences.desiredWage) >= 0 ? 1 : 0) +
				(offeredWage.compareTo(preferences.acceptableWage) < 0 ? -1 : 0) +
				(offeredWage.compareTo(preferences.acceptableWage.divide(BigDecimal.valueOf(2), DOWN)) < 0 ? -2 : 0) +
				(offerTypeOfContract == preferences.desiredTypeOfContract ? 1 : 0);

		return acceptancePoints >= acceptancePointsThreshold ? OfferResponse.POSITIVE : OfferResponse.NEGATIVE;
	}
}
