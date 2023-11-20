package pl.agh.edu.engine.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.generator.PossibleEmployeeGenerator;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.utils.RandomUtils;

public class PossibleEmployeeHandler {
	private final HotelHandler hotelHandler;

	private final List<PossibleEmployee> possibleEmployees = new ArrayList<>();

	public PossibleEmployeeHandler(HotelHandler hotelHandler) {
		this.hotelHandler = hotelHandler;
		initialize();
	}

	public void initialize() {
		IntStream.range(0, JSONGameDataLoader.employeesToHireListSize).forEach(i -> possibleEmployees.add(PossibleEmployeeGenerator.generatePossibleEmployee()));
	}

	public List<PossibleEmployee> getPossibleEmployees() {
		return possibleEmployees;
	}

	public void dailyUpdate() {
		possibleEmployees.removeIf((employee -> RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.possibleEmployeeRemovalProbability)));
		IntStream.range(possibleEmployees.size(), JSONGameDataLoader.employeesToHireListSize)
				.forEach(i -> possibleEmployees.add(PossibleEmployeeGenerator.generatePossibleEmployee()));
	}

	public OfferResponse offerJob(PossibleEmployee possibleEmployee, Offer offer) {
		if (possibleEmployee.offerJob(offer) == OfferResponse.POSITIVE) {
			hotelHandler.employeeHandler.hireEmployee(new Employee(possibleEmployee, offer));
			possibleEmployees.remove(possibleEmployee);
		}
		return possibleEmployee.offerJob(offer);
	}

}
