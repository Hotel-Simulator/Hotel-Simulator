package pl.agh.edu.management.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import pl.agh.edu.generator.possible_employee_generator.PossibleEmployeeGenerator;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.employee.*;
import pl.agh.edu.utils.RandomUtils;

public class PossibleEmployeeHandler {
	private final HotelHandler hotelHandler;

	private final List<PossibleEmployee> possibleEmployees =  new ArrayList<>();

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

	public void offerJob(PossibleEmployee possibleEmployee, JobOffer jobOffer) {
		if (possibleEmployee.offerJob(jobOffer) == JobOfferResponse.POSITIVE) {
			hotelHandler.employeeHandler.hireEmployee(new Employee(possibleEmployee, jobOffer));
			possibleEmployees.remove(possibleEmployee);
		}
	}

}
