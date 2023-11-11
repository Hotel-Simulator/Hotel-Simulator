package pl.agh.edu.engine.employee;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONEmployeeDataLoader;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.contract.TypeOfContract;
import pl.agh.edu.serialization.KryoConfig;

public class Employee {

	public final String firstName;
	public final String lastName;
	public final int age;
	public final BigDecimal skills;
	public final EmploymentPreferences preferences;
	public final Profession profession;
	private final Duration basicServiceExecutionTime;
	private final List<BigDecimal> bonuses = new ArrayList<>();
	public Shift shift;
	public BigDecimal wage;
	public TypeOfContract typeOfContract;
	private boolean isOccupied;
	private EmployeeStatus employeeStatus = EmployeeStatus.HIRED_NOT_WORKING;

	static {
		KryoConfig.kryo.register(Employee.class, new Serializer<Employee>() {
			@Override
			public void write(Kryo kryo, Output output, Employee object) {
				kryo.writeObject(output, object.firstName);
				kryo.writeObject(output, object.lastName);
				kryo.writeObject(output, object.age);
				kryo.writeObject(output, object.skills);
				kryo.writeObject(output, object.preferences);
				kryo.writeObject(output, object.profession);
				kryo.writeObject(output, object.shift);
				kryo.writeObject(output, object.wage);
				kryo.writeObject(output, object.typeOfContract);
				kryo.writeObject(output, object.bonuses, KryoConfig.listSerializer(BigDecimal.class));
				kryo.writeObject(output, object.isOccupied);
				kryo.writeObject(output, object.employeeStatus);
			}

			@Override
			public Employee read(Kryo kryo, Input input, Class<? extends Employee> type) {
				PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
						.firstName(kryo.readObject(input, String.class))
						.lastName(kryo.readObject(input, String.class))
						.age(kryo.readObject(input, Integer.class))
						.skills(kryo.readObject(input, BigDecimal.class))
						.preferences(kryo.readObject(input, EmploymentPreferences.class))
						.profession(kryo.readObject(input, Profession.class))
						.build();
				Offer offer = new Offer(
						kryo.readObject(input, Shift.class),
						kryo.readObject(input, BigDecimal.class),
						kryo.readObject(input, TypeOfContract.class));
				Employee employee = new Employee(possibleEmployee, offer);
				List<BigDecimal> bonuses = kryo.readObject(input, List.class, KryoConfig.listSerializer(BigDecimal.class));
				employee.bonuses.addAll(bonuses);
				employee.isOccupied = kryo.readObject(input, Boolean.class);
				employee.employeeStatus = kryo.readObject(input, EmployeeStatus.class);

				return employee;
			}
		});
	}

	public Employee(PossibleEmployee possibleEmployee, Offer offer) {
		this.firstName = possibleEmployee.firstName;
		this.lastName = possibleEmployee.lastName;
		this.age = possibleEmployee.age;
		this.skills = possibleEmployee.skills;
		this.preferences = possibleEmployee.preferences;
		this.profession = possibleEmployee.profession;

		setContract(offer);

		this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(possibleEmployee.profession);
	}

	public void setContract(Offer offer) {
		this.wage = offer.offeredWage();
		this.typeOfContract = offer.typeOfContract();
		this.shift = offer.shift();
	}

	public BigDecimal getSatisfaction() {
		BigDecimal desiredShiftModifier = shift == preferences.desiredShift
				? ZERO
				: new BigDecimal("0.25");
		return ONE.min(getWageSatisfaction().subtract(desiredShiftModifier));
	}

	private BigDecimal getWageSatisfaction() {
		return wage.add(bonuses.stream().reduce(ZERO, BigDecimal::add))
				.divide(preferences.desiredWage, 4, RoundingMode.CEILING)
				.setScale(2, HALF_EVEN).stripTrailingZeros();
	}

	public void addBonus(BigDecimal bonus) {
		bonuses.add(bonus);
	}

	public void removeBonus(BigDecimal bonus) {
		bonuses.remove(bonus);
	}

	public OfferResponse offerNewContract(Offer offer) {

		Shift offerShift = offer.shift();
		BigDecimal offeredWage = offer.offeredWage();

		boolean isPositive = (offerShift == shift && offeredWage.compareTo(wage) > 0) ||
				(offerShift != shift && offerShift == preferences.desiredShift && offeredWage.compareTo(preferences.acceptableWage) >= 0) ||
				(offerShift != shift && offerShift != preferences.desiredShift && offeredWage.compareTo(preferences.desiredWage) >= 0);

		return isPositive ? OfferResponse.POSITIVE : OfferResponse.NEGATIVE;
	}

	public boolean isAtWork(LocalTime time) {
		return shift.lasts(time);
	}

	public Duration getServiceExecutionTime() {
		return Duration.ofSeconds(
				(long) (basicServiceExecutionTime.getSeconds() *
						(ONE.subtract(skills.min(getSatisfaction()).divide(BigDecimal.valueOf(2), 2, HALF_EVEN))).doubleValue()));
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
