package pl.agh.edu.engine.employee;

import static java.time.LocalTime.MIDNIGHT;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONEmployeeDataLoader;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class EmployeeHandler {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final List<Employee> employees;

	public static void kryoRegister() {
		KryoConfig.kryo.register(EmployeeHandler.class, new Serializer<EmployeeHandler>() {
			@Override
			public void write(Kryo kryo, Output output, EmployeeHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.employees, KryoConfig.listSerializer(Employee.class));
			}

			@Override
			public EmployeeHandler read(Kryo kryo, Input input, Class<? extends EmployeeHandler> type) {

				return new EmployeeHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Employee.class)));
			}
		});
	}

	public EmployeeHandler() {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.employees = getInitialEmployees();
	}

	private EmployeeHandler(Time time, TimeCommandExecutor timeCommandExecutor, List<Employee> employees) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.employees = employees;
	}

	public boolean canNegotiateContractWith(Employee employee) {
		return employee.getStatus() == HIRED_WORKING;
	}

	public void offerNewContract(Employee employee, Offer offer) {
		if (employee.offerNewContract(offer) == POSITIVE) {
			employee.setContract(offer);
		}
	}

	public void hireEmployee(Employee employee) {
		this.employees.add(employee);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> employee.setStatus(HIRED_WORKING),
						LocalDateTime.of(time.getTime()
								.toLocalDate()
								.minusDays(time.getTime().getDayOfMonth() - 1)
								.plusMonths(1),
								MIDNIGHT)));
	}

	public void fireEmployee(Employee employee) {
		employee.setStatus(EmployeeStatus.FIRED_WORKING);
		timeCommandExecutor.addCommand(
				new TimeCommand(
						() -> this.removeEmployee(employee),
						LocalDateTime.of(LocalDate.of(time.getTime().getYear(), time.getTime().getMonth(), 1).plusMonths(JSONEmployeeDataLoader.noticePeriodInMonths + 1),
								MIDNIGHT)));
	}

	public void removeEmployee(Employee employee) {
		employees.remove(employee);
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public List<Employee> getWorkingEmployees() {
		return employees.stream()
				.filter(employee -> employee.getStatus() != HIRED_NOT_WORKING)
				.collect(Collectors.toList());
	}

	public List<Employee> getWorkingEmployeesByProfession(Profession profession) {
		return getWorkingEmployees().stream()
				.filter(employee -> employee.profession == profession)
				.collect(Collectors.toList());
	}

	private List<Employee> getInitialEmployees() {
		List<Employee> initialEmployees = Stream.of(
				new Employee(new PossibleEmployee.Builder()
						.firstName("Jan")
						.lastName("Kowalski")
						.age(23)
						.skills(new BigDecimal("0.55"))
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4000))
								.desiredWage(BigDecimal.valueOf(6000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(CLEANER)
						.build(),
						new Offer(MORNING, BigDecimal.valueOf(4500), PERMANENT)),
				new Employee(new PossibleEmployee.Builder()
						.firstName("Maria")
						.lastName("Nowak")
						.age(23)
						.skills(new BigDecimal("0.65"))
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4500))
								.desiredWage(BigDecimal.valueOf(7000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(TECHNICIAN)
						.build(),
						new Offer(MORNING, BigDecimal.valueOf(5500), PERMANENT)),
				new Employee(new PossibleEmployee.Builder()
						.firstName("Zofia")
						.lastName("Wrona")
						.age(23)
						.skills(new BigDecimal("0.65"))
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4500))
								.desiredWage(BigDecimal.valueOf(7000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(RECEPTIONIST)
						.build(),
						new Offer(EVENING, BigDecimal.valueOf(5500), PERMANENT)),
				new Employee(new PossibleEmployee.Builder()
						.firstName("Marcin")
						.lastName("Szpak")
						.age(45)
						.skills(new BigDecimal("0.45"))
						.preferences(new EmploymentPreferences.Builder()
								.desiredShift(MORNING)
								.acceptableWage(BigDecimal.valueOf(4000))
								.desiredWage(BigDecimal.valueOf(6000))
								.desiredTypeOfContract(PERMANENT)
								.build())
						.profession(RECEPTIONIST)
						.build(),
						new Offer(MORNING, BigDecimal.valueOf(4500), PERMANENT)))
				.collect(Collectors.toList());

		initialEmployees.forEach(employee -> employee.setStatus(HIRED_WORKING));

		return initialEmployees;
	}
}
