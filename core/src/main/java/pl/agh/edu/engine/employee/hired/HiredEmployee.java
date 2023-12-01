package pl.agh.edu.engine.employee.hired;

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
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeContractStatus;
import pl.agh.edu.engine.employee.EmployeePreferences;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.contract.TypeOfContract;
import pl.agh.edu.engine.employee.possible.PossibleEmployee;
import pl.agh.edu.serialization.KryoConfig;

public class HiredEmployee extends Employee {
	private final Duration basicServiceExecutionTime;
	private final List<BigDecimal> bonuses;
	private Shift shift;
	private BigDecimal wage;
	private TypeOfContract typeOfContract;
	private boolean isOccupied;
	private EmployeeContractStatus employeeContractStatus = EmployeeContractStatus.PENDING;

	public static void kryoRegister() {
		KryoConfig.kryo.register(HiredEmployee.class, new Serializer<HiredEmployee>() {
			@Override
			public void write(Kryo kryo, Output output, HiredEmployee object) {
				kryo.writeObject(output, object.firstName);
				kryo.writeObject(output, object.lastName);
				kryo.writeObject(output, object.age);
				kryo.writeObject(output, object.skills);
				kryo.writeObject(output, object.preferences);
				kryo.writeObject(output, object.profession);
				kryo.writeObject(output, object.acceptancePointsThreshold);
				kryo.writeObject(output, object.shift);
				kryo.writeObject(output, object.wage);
				kryo.writeObject(output, object.typeOfContract);
				kryo.writeObject(output, object.bonuses, KryoConfig.listSerializer(BigDecimal.class));
				kryo.writeObject(output, object.isOccupied);
				kryo.writeObject(output, object.employeeContractStatus);
			}

			@Override
			public HiredEmployee read(Kryo kryo, Input input, Class<? extends HiredEmployee> type) {
				HiredEmployee employee = new HiredEmployee(
						kryo.readObject(input, String.class),
						kryo.readObject(input, String.class),
						kryo.readObject(input, Integer.class),
						kryo.readObject(input, BigDecimal.class),
						kryo.readObject(input, EmployeePreferences.class),
						kryo.readObject(input, Profession.class),
						kryo.readObject(input, Integer.class),
						kryo.readObject(input, Shift.class),
						kryo.readObject(input, BigDecimal.class),
						kryo.readObject(input, TypeOfContract.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(BigDecimal.class)));

				employee.isOccupied = kryo.readObject(input, Boolean.class);
				employee.employeeContractStatus = kryo.readObject(input, EmployeeContractStatus.class);

				return employee;
			}
		});
	}

	public HiredEmployee(PossibleEmployee possibleEmployee, EmployeeOffer offer) {
		super(
				possibleEmployee.firstName,
				possibleEmployee.lastName,
				possibleEmployee.age,
				possibleEmployee.skills,
				possibleEmployee.preferences,
				possibleEmployee.profession,
				possibleEmployee.acceptancePointsThreshold);

		this.bonuses = new ArrayList<>();

		setContract(offer);

		this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(possibleEmployee.profession);
	}

	private HiredEmployee(String firstName,
			String lastName,
			int age,
			BigDecimal skills,
			EmployeePreferences preferences,
			Profession profession,
			int acceptancePointsThreshold,
			Shift shift,
			BigDecimal wage,
			TypeOfContract typeOfContract,
			List<BigDecimal> bonuses) {
		super(firstName, lastName, age, skills, preferences, profession, acceptancePointsThreshold);
		this.shift = shift;
		this.wage = wage;
		this.typeOfContract = typeOfContract;
		this.bonuses = bonuses;

		this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(profession);

	}

	public void setContract(EmployeeOffer offer) {
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

	public BigDecimal getTotalBonus() {
		return bonuses.stream().reduce(ZERO, BigDecimal::add);
	}

	public void removeBonus(BigDecimal bonus) {
		bonuses.remove(bonus);
	}

	public OfferResponse offerNewContract(EmployeeOffer offer) {

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

	public EmployeeContractStatus getStatus() {
		return employeeContractStatus;
	}

	public void setStatus(EmployeeContractStatus employeeContractStatus) {
		this.employeeContractStatus = employeeContractStatus;
	}

	public BigDecimal getWage() {
		return wage;
	}

	public Shift getShift() {
		return shift;
	}

	public TypeOfContract getTypeOfContract() {
		return typeOfContract;
	}

}
