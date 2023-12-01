package pl.agh.edu.engine.employee;

import java.math.BigDecimal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.employee.contract.TypeOfContract;
import pl.agh.edu.serialization.KryoConfig;

public class EmployeePreferences {
	public final Shift desiredShift;
	public final BigDecimal acceptableWage;
	public final BigDecimal desiredWage;
	public final TypeOfContract desiredTypeOfContract;

	public static void kryoRegister() {
		KryoConfig.kryo.register(EmployeePreferences.class, new Serializer<EmployeePreferences>() {
			@Override
			public void write(Kryo kryo, Output output, EmployeePreferences object) {
				kryo.writeObject(output, object.desiredShift);
				kryo.writeObject(output, object.acceptableWage);
				kryo.writeObject(output, object.desiredWage);
				kryo.writeObject(output, object.desiredTypeOfContract);
			}

			@Override
			public EmployeePreferences read(Kryo kryo, Input input, Class<? extends EmployeePreferences> type) {
				return new EmployeePreferences.Builder()
						.desiredShift(kryo.readObject(input, Shift.class))
						.acceptableWage(kryo.readObject(input, BigDecimal.class))
						.desiredWage(kryo.readObject(input, BigDecimal.class))
						.desiredTypeOfContract(kryo.readObject(input, TypeOfContract.class))
						.build();
			}
		});
	}

	private EmployeePreferences(Builder builder) {
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

		public EmployeePreferences build() {
			return new EmployeePreferences(this);
		}
	}
}
