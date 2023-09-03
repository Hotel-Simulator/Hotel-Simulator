package pl.agh.edu.management.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import pl.agh.edu.generator.possible_employee_generator.PossibleEmployeeGenerator;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.employee.*;

public class PossibleEmployeeHandler {
	private final HotelHandler hotelHandler;

	private final List<PossibleEmployee> possibleEmployees;
	private final int employeesToHireListSize;
	private final double possibleEmployeeRemovalProbability;
	private final Random random;

	public PossibleEmployeeHandler(HotelHandler hotelHandler) {
		employeesToHireListSize = JSONGameDataLoader.employeesToHireListSize;
		possibleEmployeeRemovalProbability = JSONGameDataLoader.possibleEmployeeRemovalProbability;
		this.possibleEmployees = new ArrayList<>();
		random = new Random();
		this.hotelHandler = hotelHandler;
		initialize();
	}

	public void initialize() {
		IntStream.range(0, employeesToHireListSize).forEach(i -> possibleEmployees.add(PossibleEmployeeGenerator.generatePossibleEmployee()));
	}

	public List<PossibleEmployee> getPossibleEmployees() {
		return possibleEmployees;
	}

	public void dailyUpdate() {
		possibleEmployees.removeIf((employee -> random.nextDouble() <= possibleEmployeeRemovalProbability));
		IntStream.range(possibleEmployees.size(), employeesToHireListSize)
				.forEach(i -> possibleEmployees.add(PossibleEmployeeGenerator.generatePossibleEmployee()));
	}

	public void offerJob(PossibleEmployee possibleEmployee, JobOffer jobOffer) {
		if (possibleEmployee.offerJob(jobOffer) == JobOfferResponse.POSITIVE) {
			hotelHandler.employeeHandler.hireEmployee(new Employee(possibleEmployee, jobOffer));
			possibleEmployees.remove(possibleEmployee);
		}
	}

}
