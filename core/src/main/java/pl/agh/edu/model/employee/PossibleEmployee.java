package pl.agh.edu.model.employee;

public class PossibleEmployee {
	private final String firstName;
	private final String lastName;
	private final int age;
	private final double skills;
	private final EmploymentPreferences preferences;
	private final Profession profession;

	private PossibleEmployee(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.age = builder.age;
		this.skills = builder.skills;
		this.preferences = builder.preferences;
		this.profession = builder.profession;
	}

	public String firstName() {
		return firstName;
	}

	public String lastName() {
		return lastName;
	}

	public int age() {
		return age;
	}

	public double skills() {
		return skills;
	}

	public EmploymentPreferences preferences() {
		return preferences;
	}

	public Profession profession() {
		return profession;
	}

	public JobOfferResponse offerJob(JobOffer jobOffer) {

		if (preferences.desiredShift() == jobOffer.shift()
				&& jobOffer.typeOfContract() == preferences.desiredTypeOfContract()) {

			if (jobOffer.offeredWage().doubleValue() >= preferences.acceptableWage().doubleValue()) {
				return JobOfferResponse.POSITIVE;
			}
		} else if (preferences.desiredShift() == jobOffer.shift()
				|| jobOffer.typeOfContract() == preferences.desiredTypeOfContract()) {

			if (jobOffer.offeredWage().doubleValue() * 2 >= preferences.acceptableWage().doubleValue() + preferences.desiredWage().doubleValue()) {
				return JobOfferResponse.POSITIVE;
			}
		} else {
			if (jobOffer.offeredWage().doubleValue() >= preferences.desiredWage().doubleValue()) {
				return JobOfferResponse.POSITIVE;
			}
		}
		return JobOfferResponse.NEGATIVE;
	}

	public static class Builder {
		private String firstName;
		private String lastName;
		private int age;
		private double skills;
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

		public Builder skills(double skills) {
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
