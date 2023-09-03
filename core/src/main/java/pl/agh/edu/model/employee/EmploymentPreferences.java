package pl.agh.edu.model.employee;

import java.math.BigDecimal;

import pl.agh.edu.enums.TypeOfContract;

public class EmploymentPreferences {
	private final Shift desiredShift;
	private final BigDecimal acceptableWage;
	private final BigDecimal desiredWage;
	private final TypeOfContract desiredTypeOfContract;

	// Prywatny konstruktor, aby zapobiec tworzeniu obiektu bezpośrednio.
	private EmploymentPreferences(Builder builder) {
		this.desiredShift = builder.desiredShift;
		this.acceptableWage = builder.acceptableWage;
		this.desiredWage = builder.desiredWage;
		this.desiredTypeOfContract = builder.desiredTypeOfContract;
	}

	public Shift desiredShift() {
		return desiredShift;
	}

	public BigDecimal acceptableWage() {
		return acceptableWage;
	}

	public BigDecimal desiredWage() {
		return desiredWage;
	}

	public TypeOfContract desiredTypeOfContract() {
		return desiredTypeOfContract;
	}

	public static class Builder {
		private Shift desiredShift;
		private BigDecimal acceptableWage;
		private BigDecimal desiredWage;
		private TypeOfContract desiredTypeOfContract;

		public Builder desiredShift(Shift desiredShift) {
			this.desiredShift = desiredShift;
			return this;
		}

		public Builder acceptableWage(BigDecimal acceptableWage) {
			this.acceptableWage = acceptableWage;
			return this;
		}

		public Builder desiredWage(BigDecimal desiredWage) {
			this.desiredWage = desiredWage;
			return this;
		}

		public Builder desiredTypeOfContract(TypeOfContract desiredTypeOfContract) {
			this.desiredTypeOfContract = desiredTypeOfContract;
			return this;
		}

		public EmploymentPreferences build() {
			return new EmploymentPreferences(this);
		}
	}
}
