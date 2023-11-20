package pl.agh.edu.engine.employee;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.generator.PossibleEmployeeGenerator;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.RandomUtils;

public class PossibleEmployeeHandler {

	private final List<PossibleEmployee> possibleEmployees;
	private final EmployeeHandler employeeHandler;

	public static void kryoRegister() {
		KryoConfig.kryo.register(PossibleEmployeeHandler.class, new Serializer<PossibleEmployeeHandler>() {
			@Override
			public void write(Kryo kryo, Output output, PossibleEmployeeHandler object) {
				kryo.writeObject(output, object.employeeHandler);
				kryo.writeObject(output, object.possibleEmployees, KryoConfig.listSerializer(PossibleEmployee.class));
			}

			@Override
			public PossibleEmployeeHandler read(Kryo kryo, Input input, Class<? extends PossibleEmployeeHandler> type) {
				return new PossibleEmployeeHandler(
						kryo.readObject(input, EmployeeHandler.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(PossibleEmployee.class)));
			}
		});
	}

	public PossibleEmployeeHandler(EmployeeHandler employeeHandler) {
		this.employeeHandler = employeeHandler;
		this.possibleEmployees = initPossibleEmployees();
	}

	private PossibleEmployeeHandler(EmployeeHandler employeeHandler, List<PossibleEmployee> possibleEmployees) {
		this.employeeHandler = employeeHandler;
		this.possibleEmployees = possibleEmployees;
	}

	public List<PossibleEmployee> initPossibleEmployees() {
		return IntStream.range(0, JSONGameDataLoader.employeesToHireListSize)
				.mapToObj(i -> PossibleEmployeeGenerator.generatePossibleEmployee())
				.collect(Collectors.toList());
	}

	public List<PossibleEmployee> getPossibleEmployees() {
		return possibleEmployees;
	}

	public void dailyUpdate() {
		possibleEmployees.removeIf((employee -> RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.possibleEmployeeRemovalProbability)));
		IntStream.range(possibleEmployees.size(), JSONGameDataLoader.employeesToHireListSize)
				.forEach(i -> possibleEmployees.add(PossibleEmployeeGenerator.generatePossibleEmployee()));
	}

	public void offerJob(PossibleEmployee possibleEmployee, Offer offer) {
		if (possibleEmployee.offerJob(offer) == OfferResponse.POSITIVE) {
			employeeHandler.hireEmployee(new Employee(possibleEmployee, offer));
			possibleEmployees.remove(possibleEmployee);
		}
	}

}
