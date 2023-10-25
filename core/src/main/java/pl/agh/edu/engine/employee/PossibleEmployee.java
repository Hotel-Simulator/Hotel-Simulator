package pl.agh.edu.engine.employee;

import java.math.BigDecimal;

import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;

public class PossibleEmployee {
	public final String firstName;
	public final String lastName;
	public final int age;
	public final BigDecimal skills;
	public final EmploymentPreferences preferences;
	public final Profession profession;

	private PossibleEmployee(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.age = builder.age;
		this.skills = builder.skills;
		this.preferences = builder.preferences;
		this.profession = builder.profession;
	}

	public OfferResponse offerJob(Offer offer) {

		Shift offerShift = offer.shift();
		BigDecimal offeredWage = offer.offeredWage();

		boolean isPositive = (preferences.desiredShift == offerShift && offeredWage.compareTo(preferences.acceptableWage) >= 0) ||
				(preferences.desiredShift != offerShift && offeredWage.compareTo(preferences.desiredWage) >= 0);

		return isPositive ? OfferResponse.POSITIVE : OfferResponse.NEGATIVE;
	}

	public static class Builder {
		private String firstName;
		private String lastName;
		private int age;
		private BigDecimal skills;
		private EmploymentPreferences preferences;
		private Profession profession;

		public Builder() {}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder age(int age) {
			this.age = age;
			return this;
		}

		public Builder skills(BigDecimal skills) {
			this.skills = skills;
			return this;
		}

		public Builder preferences(EmploymentPreferences preferences) {
			this.preferences = preferences;
			return this;
		}

		public Builder profession(Profession profession) {
			this.profession = profession;
			return this;
		}

		public PossibleEmployee build() {
			return new PossibleEmployee(this);
		}
	}
}
