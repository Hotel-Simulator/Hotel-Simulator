package pl.agh.edu.engine.employee;

import java.math.BigDecimal;

import pl.agh.edu.engine.employee.contract.TypeOfContract;

public class EmploymentPreferences {
	public final Shift desiredShift;
	public final BigDecimal acceptableWage;
	public final BigDecimal desiredWage;
	public final TypeOfContract desiredTypeOfContract;

	private EmploymentPreferences(Builder builder) {
		this.desiredShift = builder.desiredShift;
		this.acceptableWage = builder.acceptableWage;
		this.desiredWage = builder.desiredWage;
		this.desiredTypeOfContract = builder.desiredTypeOfContract;
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
