package pl.agh.edu.engine.employee;

import java.util.List;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.generator.PossibleEmployeeGenerator;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.RandomUtils;

public class PossibleEmployeeHandler {
	private final HotelHandler hotelHandler;

	private final List<PossibleEmployee> possibleEmployees;

	static {
		KryoConfig.kryo.register(PossibleEmployeeHandler.class, new Serializer<PossibleEmployeeHandler>() {
			@Override
			public void write(Kryo kryo, Output output, PossibleEmployeeHandler object) {
				kryo.writeObject(output, object.hotelHandler);
				kryo.writeObject(output, object.possibleEmployees, KryoConfig.listSerializer(PossibleEmployee.class));
			}

			@Override
			public PossibleEmployeeHandler read(Kryo kryo, Input input, Class<? extends PossibleEmployeeHandler> type) {
				return new PossibleEmployeeHandler(
						kryo.readObject(input, HotelHandler.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(PossibleEmployee.class)));
			}
		});
	}

	public PossibleEmployeeHandler(HotelHandler hotelHandler) {
		this.hotelHandler = hotelHandler;
		this.possibleEmployees = initPossibleEmployees();
	}

	private PossibleEmployeeHandler(HotelHandler hotelHandler, List<PossibleEmployee> possibleEmployees) {
		this.hotelHandler = hotelHandler;
		this.possibleEmployees = possibleEmployees;
	}

	public List<PossibleEmployee> initPossibleEmployees() {
		return IntStream.range(0, JSONGameDataLoader.employeesToHireListSize)
				.mapToObj(i -> PossibleEmployeeGenerator.generatePossibleEmployee())
				.toList();
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
			hotelHandler.employeeHandler.hireEmployee(new Employee(possibleEmployee, offer));
			possibleEmployees.remove(possibleEmployee);
		}
	}

}
