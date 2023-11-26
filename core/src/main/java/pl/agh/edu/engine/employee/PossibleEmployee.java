package pl.agh.edu.engine.employee;

import java.math.BigDecimal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.serialization.KryoConfig;

public class PossibleEmployee {
	public final String firstName;
	public final String lastName;
	public final int age;
	public final BigDecimal skills;
	public final EmploymentPreferences preferences;
	public final Profession profession;

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
			}

			@Override
			public PossibleEmployee read(Kryo kryo, Input input, Class<? extends PossibleEmployee> type) {
				return new PossibleEmployee.Builder()
						.firstName(kryo.readObject(input, String.class))
						.lastName(kryo.readObject(input, String.class))
						.age(kryo.readObject(input, Integer.class))
						.skills(kryo.readObject(input, BigDecimal.class))
						.preferences(kryo.readObject(input, EmploymentPreferences.class))
						.profession(kryo.readObject(input, Profession.class))
						.build();
			}
		});
	}

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
