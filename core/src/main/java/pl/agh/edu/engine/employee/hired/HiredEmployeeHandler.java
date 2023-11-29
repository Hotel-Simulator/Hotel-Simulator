package pl.agh.edu.engine.employee.hired;

import static java.time.LocalTime.MIDNIGHT;
import static java.time.LocalTime.of;
import static pl.agh.edu.engine.employee.EmployeeStatus.FIRED_WORKING;
import static pl.agh.edu.engine.employee.EmployeeStatus.HIRED_NOT_WORKING;
import static pl.agh.edu.engine.employee.EmployeeStatus.HIRED_WORKING;
import static pl.agh.edu.engine.employee.Profession.CLEANER;
import static pl.agh.edu.engine.employee.Profession.RECEPTIONIST;
import static pl.agh.edu.engine.employee.Profession.TECHNICIAN;
import static pl.agh.edu.engine.employee.Shift.EVENING;
import static pl.agh.edu.engine.employee.Shift.MORNING;
import static pl.agh.edu.engine.employee.contract.OfferResponse.POSITIVE;
import static pl.agh.edu.engine.employee.contract.TypeOfContract.PERMANENT;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.stream.Stream;
import pl.agh.edu.data.loader.JSONEmployeeDataLoader;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.EmployeePreferences;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.possible.PossibleEmployee;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class HiredEmployeeHandler extends EmployeeHandler<HiredEmployee> {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	public static void kryoRegister() {
		KryoConfig.kryo.register(HiredEmployeeHandler.class, new Serializer<HiredEmployeeHandler>() {
			@Override
			public void write(Kryo kryo, Output output, HiredEmployeeHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.employeeList, KryoConfig.listSerializer(HiredEmployee.class));
			}

			@Override
			public HiredEmployeeHandler read(Kryo kryo, Input input, Class<? extends HiredEmployeeHandler> type) {

				return new HiredEmployeeHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(HiredEmployee.class)));
			}
		});
	}

	public HiredEmployeeHandler() {
		super(getInitialEmployees());
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
	}

	@Override
	public OfferResponse offerContract(HiredEmployee employee, EmployeeOffer employeeOffer) {
		OfferResponse offerResponse = employee.offerContract(employeeOffer);
		if (offerResponse == POSITIVE) {
			employee.setContract(employeeOffer);
		}
		System.out.println("Offer response: " + offerResponse);
		return offerResponse;
	}

	private HiredEmployeeHandler(Time time, TimeCommandExecutor timeCommandExecutor, List<HiredEmployee> employees) {
		super(employees);
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
	}

	public boolean canNegotiateContractWith(HiredEmployee employee) {
		return employee.getStatus() == HIRED_WORKING;
	}
	public void hireEmployee(HiredEmployee employee) {
		this.employeeList.add(employee);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> employee.setStatus(HIRED_WORKING),
						LocalDateTime.of(time
										.getTime()
										.toLocalDate()
										.minusDays(time.getTime().getDayOfMonth() - 1)
										.plusMonths(1),
								MIDNIGHT)
				)
		);
	}

	public void fireEmployee(HiredEmployee employee) {
		employee.setStatus(FIRED_WORKING);
		timeCommandExecutor.addCommand(
				new TimeCommand(
						() -> this.removeEmployee(employee),
						LocalDateTime.of(LocalDate.of(time.getTime().getYear(), time.getTime().getMonth(), 1).plusMonths(JSONEmployeeDataLoader.noticePeriodInMonths + 1), MIDNIGHT)
				)
		);
	}

	public void removeEmployee(HiredEmployee employee) {
		this.employeeList.remove(employee);
	}

	public List<HiredEmployee> getEmployees() {
		return this.employeeList;
	}

	public List<HiredEmployee> getWorkingEmployees() {
		return this.employeeList.stream()
				.filter(employee -> employee.getStatus() != HIRED_NOT_WORKING)
				.collect(Collectors.toList());
	}

	public List<HiredEmployee> getWorkingEmployeesByProfession(Profession profession) {
		return getWorkingEmployees().stream()
				.filter(employee -> employee.profession == profession)
				.collect(Collectors.toList());
	}

	private static List<HiredEmployee> getInitialEmployees() {
		List<HiredEmployee> initialEmployees = Stream.of(
				new HiredEmployee(new PossibleEmployee.PossibleEmployeeBuilder()
						.firstName("Jan")
						.lastName("Kowalski")
						.age(23)
						.skills(new BigDecimal("0.55"))
						.preferences(new EmployeePreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4000))
								.desiredWage(BigDecimal.valueOf(6000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(CLEANER)
						.acceptancePointsThreshold(2)
						.build(),
						new EmployeeOffer(MORNING, BigDecimal.valueOf(4500), PERMANENT)),
				new HiredEmployee(new PossibleEmployee.PossibleEmployeeBuilder()
						.firstName("Maria")
						.lastName("Nowak")
						.age(23)
						.skills(new BigDecimal("0.65"))
						.preferences(new EmployeePreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4500))
								.desiredWage(BigDecimal.valueOf(7000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(TECHNICIAN)
						.acceptancePointsThreshold(3)
						.build(),
						new EmployeeOffer(MORNING, BigDecimal.valueOf(5500), PERMANENT)),
				new HiredEmployee(new PossibleEmployee.PossibleEmployeeBuilder()
						.firstName("Zofia")
						.lastName("Wrona")
						.age(23)
						.skills(new BigDecimal("0.65"))
						.preferences(new EmployeePreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4500))
								.desiredWage(BigDecimal.valueOf(7000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(RECEPTIONIST)
						.acceptancePointsThreshold(2)
						.build(),
						new EmployeeOffer(EVENING, BigDecimal.valueOf(5500), PERMANENT)),
				new HiredEmployee(new PossibleEmployee.PossibleEmployeeBuilder()
						.firstName("Marcin")
						.lastName("Szpak")
						.age(45)
						.skills(new BigDecimal("0.45"))
						.preferences(new EmployeePreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4000))
								.desiredWage(BigDecimal.valueOf(6000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(RECEPTIONIST)
						.acceptancePointsThreshold(4)
						.build(),
						new EmployeeOffer(MORNING, BigDecimal.valueOf(4500), PERMANENT)))
				.collect(Collectors.toList());

		initialEmployees.forEach(employee -> employee.setStatus(HIRED_WORKING));

		return initialEmployees;
	}
}
