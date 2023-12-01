package pl.agh.edu.engine.employee.possible;

import java.math.BigDecimal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeePreferences;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.serialization.KryoConfig;

public class PossibleEmployee extends Employee {
	public static void kryoRegister() {
		KryoConfig.kryo.register(PossibleEmployee.class, new Serializer<PossibleEmployee>() {
			@Override
			public void write(Kryo kryo, Output output, PossibleEmployee object) {
				kryo.writeObject(output, object.firstName);
				kryo.writeObject(output, object.lastName);
				kryo.writeObject(output, object.age);
				kryo.writeObject(output, object.skills);
				kryo.writeObject(output, object.preferences);
				kryo.writeObject(output, object.profession);
				kryo.writeObject(output, object.acceptancePointsThreshold);
			}

			@Override
			public PossibleEmployee read(Kryo kryo, Input input, Class<? extends PossibleEmployee> type) {
				return new PossibleEmployeeBuilder()
						.firstName(kryo.readObject(input, String.class))
						.lastName(kryo.readObject(input, String.class))
						.age(kryo.readObject(input, Integer.class))
						.skills(kryo.readObject(input, BigDecimal.class))
						.preferences(kryo.readObject(input, EmployeePreferences.class))
						.profession(kryo.readObject(input, Profession.class))
						.acceptancePointsThreshold(kryo.readObject(input, Integer.class))
						.build();
			}
		});
	}

	private PossibleEmployee(PossibleEmployeeBuilder builder) {
		super(builder.firstName, builder.lastName, builder.age, builder.skills, builder.preferences, builder.profession, builder.acceptancePointsThreshold);
	}

	public static class PossibleEmployeeBuilder {
		private String firstName;
		private String lastName;
		private int age;
		private BigDecimal skills;
		private EmployeePreferences preferences;
		private Profession profession;
		private int acceptancePointsThreshold;

		public PossibleEmployeeBuilder() {}

		public PossibleEmployeeBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public PossibleEmployeeBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public PossibleEmployeeBuilder age(int age) {
			this.age = age;
			return this;
		}

		public PossibleEmployeeBuilder skills(BigDecimal skills) {
			this.skills = skills;
			return this;
		}

		public PossibleEmployeeBuilder preferences(EmployeePreferences preferences) {
			this.preferences = preferences;
			return this;
		}

		public PossibleEmployeeBuilder profession(Profession profession) {
			this.profession = profession;
			return this;
		}

		public PossibleEmployeeBuilder acceptancePointsThreshold(int acceptancePointsThreshold) {
			this.acceptancePointsThreshold = acceptancePointsThreshold;
			return this;
		}

		public PossibleEmployee build() {
			return new PossibleEmployee(this);
		}
	}
}
