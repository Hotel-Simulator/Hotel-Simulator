package pl.agh.edu.engine.employee.possible;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.engine.generator.PossibleEmployeeGenerator;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.RandomUtils;

public class PossibleEmployeeHandler extends EmployeeHandler<PossibleEmployee> {
	private final HiredEmployeeHandler employeeHandler;

	public static void kryoRegister() {
		KryoConfig.kryo.register(PossibleEmployeeHandler.class, new Serializer<PossibleEmployeeHandler>() {
			@Override
			public void write(Kryo kryo, Output output, PossibleEmployeeHandler object) {
				kryo.writeObject(output, object.employeeHandler);
				kryo.writeObject(output, object.employeeList, KryoConfig.listSerializer(PossibleEmployee.class));
			}

			@Override
			public PossibleEmployeeHandler read(Kryo kryo, Input input, Class<? extends PossibleEmployeeHandler> type) {
				return new PossibleEmployeeHandler(
						kryo.readObject(input, HiredEmployeeHandler.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(PossibleEmployee.class)));
			}
		});
	}

	public PossibleEmployeeHandler(HiredEmployeeHandler employeeHandler) {
		super(initPossibleEmployees());
		this.employeeHandler = employeeHandler;
	}

	private PossibleEmployeeHandler(HiredEmployeeHandler employeeHandler, List<PossibleEmployee> possibleEmployees) {
		super(possibleEmployees);
		this.employeeHandler = employeeHandler;
	}

	public static List<PossibleEmployee> initPossibleEmployees() {
		return IntStream.range(0, JSONGameDataLoader.employeesToHireListSize)
				.mapToObj(i -> PossibleEmployeeGenerator.generatePossibleEmployee())
				.collect(Collectors.toList());
	}

	public List<PossibleEmployee> getPossibleEmployees() {
		return employeeList;
	}

	public void dailyUpdate() {
		employeeList.removeIf((employee -> RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.everyDayPossibleEmployeeRemovalProbability)));
		IntStream.range(employeeList.size(), JSONGameDataLoader.employeesToHireListSize)
				.forEach(i -> employeeList.add(PossibleEmployeeGenerator.generatePossibleEmployee()));
	}
	@Override
	public OfferResponse offerContract(PossibleEmployee possibleEmployee, EmployeeOffer employeeOffer) {
		OfferResponse response = possibleEmployee.offerContract(employeeOffer);
		if (response == OfferResponse.POSITIVE) {
			employeeHandler.hireEmployee(new HiredEmployee(possibleEmployee, employeeOffer));
			employeeList.remove(possibleEmployee);
		} else if (RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.afterNegativeResponsePossibleEmployeeRemovalProbability)) {
			employeeList.remove(possibleEmployee);
		}
		return response;
	}
}
