package pl.agh.edu.engine.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class Credit {
	public final BigDecimal value;
	public final long lengthInMonths;
	public final BigDecimal interestRate;
	public final LocalDate takeOutDate;
	public final BigDecimal valueWithInterest;
	public final BigDecimal monthlyPayment;

	public static void kryoRegister() {
		KryoConfig.kryo.register(Credit.class, new Serializer<Credit>() {
			@Override
			public void write(Kryo kryo, Output output, Credit object) {
				kryo.writeObject(output, object.value);
				kryo.writeObject(output, object.lengthInMonths);
				kryo.writeObject(output, object.interestRate);
				kryo.writeObject(output, object.takeOutDate);
			}

			@Override
			public Credit read(Kryo kryo, Input input, Class<? extends Credit> type) {
				return new Credit(
						kryo.readObject(input, BigDecimal.class),
						kryo.readObject(input, Long.class),
						kryo.readObject(input, BigDecimal.class),
						kryo.readObject(input, LocalDate.class));
			}
		});
	}

	public Credit(BigDecimal value, long lengthInMonths, BigDecimal interestRate, LocalDate takeOutDate) {
		this.value = value;
		this.lengthInMonths = lengthInMonths;
		this.interestRate = interestRate;
		this.takeOutDate = takeOutDate;
		this.valueWithInterest = value.multiply(BigDecimal.ONE.add(interestRate));
		this.monthlyPayment = valueWithInterest.divide(BigDecimal.valueOf(lengthInMonths), 2, RoundingMode.HALF_UP);
	}
}
